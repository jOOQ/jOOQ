/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.explorer;

import static javafx.collections.FXCollections.observableArrayList;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.row;
import static org.jooq.lambda.Seq.seq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.explorer.components.JOOQFX;
import org.jooq.explorer.components.field.CField;
import org.jooq.impl.DSL;
import org.jooq.lambda.Seq;
import org.jooq.tools.jdbc.JDBCUtils;

@SuppressWarnings("restriction")
public class Explorer extends Application {

    private Connection                      connection;
    private DSLContext                      ctx;

    // A tedious to write border, in case we need it for debugging.
    private Border                          border         = new Border(new BorderStroke(Color.BLACK,
                                                               BorderStrokeStyle.SOLID,
                                                               CornerRadii.EMPTY,
                                                               new BorderWidths(2.0)));

    private ObservableValue<Schema>         selectedSchema;
    private ObservableValue<Table<?>>       selectedTable;
    private SimpleObjectProperty<Result<?>> selectedResult = new SimpleObjectProperty<>();
    private ObservableValue<Record>         selectedRecord;
    private ObservableValue<Tab>            selectedTab;

    private ComboBox<Schema>                uiSchemas;
    private TableView<Record>               uiResult;
    private GridPane                        uiRecord;
    private TabPane                         uiNavigation;
    private List<ChangeListener<Object>>    uiNavigationListeners;
    private ComboBox<Table<?>>              uiTables;

    @Override
    public void start(Stage stage) throws Exception {
        uiSchemas = new ComboBox<>(observableArrayList(ctx.meta().getSchemas()));
        selectedSchema = uiSchemas.getSelectionModel().selectedItemProperty();

        uiTables = new ComboBox<>();
        selectedTable = uiTables.getSelectionModel().selectedItemProperty();

        uiResult = new TableView<>();
        selectedRecord = uiResult.getSelectionModel().selectedItemProperty();

        uiRecord = new GridPane();
        uiRecord.setPrefHeight(400);

        uiNavigation = new TabPane();
        uiNavigationListeners = new ArrayList<>();
        selectedTab = uiNavigation.getSelectionModel().selectedItemProperty();

        selectedSchema.addListener((observable, oldV, newV) -> {
            uiTables.getItems().clear();
            uiTables.getItems().addAll(newV.getTables());
            uiTables.getSelectionModel().selectFirst();
        });

        selectedTable.addListener((observable, oldV, newV) -> {
            selectedResult.setValue(ctx.selectFrom(newV).fetch());
        });

        selectedRecord.addListener((observable, oldV, newV) -> {
            seq(uiRecord.getChildren()).ofType(CField.class).forEach(f -> f.setValue(newV));
        });

        selectedResult.addListener((observable, oldV, newV) -> {
            uiResult.getColumns().clear();
            uiResult.getColumns().addAll(Seq.of(newV.fields()).map(f -> {
                TableColumn<Record, Object> col = new TableColumn<Record, Object>(f.getName());
                col.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getValue(f)));
                return col;
            }).toList());

            uiResult.getItems().clear();
            uiResult.getItems().addAll(newV);

            uiRecord.getChildren().clear();
            for (int i = 0; i < newV.fields().length; i++) {
                Field<?> field = newV.field(i);
                uiRecord.addRow(i, JOOQFX.label(field, label -> label.setMinWidth(100)), JOOQFX.field(field));
            }

            uiNavigation.getTabs().clear();
            uiNavigationListeners.forEach(selectedTab::removeListener);
            uiNavigationListeners.forEach(selectedRecord::removeListener);
            uiNavigationListeners.clear();

            UniqueKey<?> pk = selectedTable.getValue().getPrimaryKey();
            if (pk != null) {
                for (ForeignKey<?, ?> reference : pk.getReferences()) {
                    TableView<Record> content = new TableView<>();

                    Tab tab = new Tab(reference.getTable().getName());
                    tab.setContent(content);
                    tab.setClosable(false);

                    ChangeListener<Object> listener = (observable1, oldV1, newV1) -> {
                        if (tab.isSelected()) {
                            content.getItems().clear();

                            content.getColumns().clear();
                            content.getColumns().addAll(Seq.of(reference.getTable().fields()).map(f -> {
                                TableColumn<Record, Object> col = new TableColumn<Record, Object>(f.getName());
                                col.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getValue(f)));
                                return col;
                            }).toList());

                            if (selectedRecord.getValue() != null) {
                                content.getItems().addAll(ctx
                                    .selectFrom(reference.getTable())
                                    .where(
                                        selectedRecord.getValue() != null
                                        ? row(reference.getFieldsArray()).eq(selectedRecord.getValue().into(pk.getFieldsArray()))
                                        : falseCondition()
                                    )
                                    .fetch());
                            }
                        }
                    };

                    uiNavigation.getTabs().add(tab);
                    uiNavigationListeners.add(listener);
                    selectedTab.addListener(listener);
                    selectedRecord.addListener(listener);
                }
            }

            uiNavigation.selectionModelProperty().get().clearAndSelect(0);
        });

        uiSchemas.getSelectionModel().selectFirst();

        HBox selection = new HBox(uiSchemas, uiTables);
        VBox left = new VBox(selection, uiResult);
        left.setPrefWidth(400);
        VBox.setMargin(selection, new Insets(5, 0, 5, 0));
        VBox.setVgrow(uiResult, Priority.ALWAYS);


        VBox right = new VBox(new ScrollPane(uiRecord), uiNavigation);

        HBox pane = new HBox(left, right);
        HBox.setHgrow(right, Priority.ALWAYS);
        HBoxResizer.makeResizable(pane);


        MenuBar menu = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuView = new Menu("View");
        menu.getMenus().addAll(menuFile, menuEdit, menuView);

        VBox root = new VBox(menu, pane);
        VBox.setVgrow(pane, Priority.ALWAYS);

        Scene scene = new Scene(root, 1200, 700);
        stage.setTitle("Database navigation");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();

        Properties properties = new Properties();
        properties.load(Explorer.class.getResourceAsStream("/config.properties"));

        Class.forName(properties.getProperty("db.driver"));
        connection = DriverManager.getConnection(
            properties.getProperty("db.url"),
            properties.getProperty("db.username"),
            properties.getProperty("db.password")
        );

        ctx = DSL.using(connection);
    }

    @Override
    public void stop() throws Exception {
        JDBCUtils.safeClose(connection);

        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
