/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.example.javafx;


import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableArrayList;
import static org.jooq.example.db.h2.Tables.COUNTRIES;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.select;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.stream.Stream;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.conf.Settings;
import org.jooq.example.db.h2.tables.records.CountriesRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.JDBCUtils;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class BarChartSample extends Application {

    Connection      connection;
    CountriesRecord selected;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Country statistics with jOOQ and JavaFX");
        Label error = new Label("");
        error.setPadding(new Insets(10.0));

        // Create two charts, plotting a specific metric, each.
        BarChart<String, Number> chart1 = chart(COUNTRIES.GDP_PER_CAPITA, "GDP per Capita", "USD");
        BarChart<String, Number> chart2 = chart(COUNTRIES.GOVT_DEBT, "Government Debt", "% of GDP");

        TableView<CountriesRecord> table = table(COUNTRIES);

        Runnable refresh = () -> {
            table.getItems().clear();
            table.getItems().addAll(ctx().fetch(COUNTRIES).sortAsc(COUNTRIES.YEAR).sortAsc(COUNTRIES.CODE));

            chartRefresh(chart1);
            chartRefresh(chart2);

            error.setText("");
            selected = ctx().newRecord(COUNTRIES);
        };

        refresh.run();

        table.getSelectionModel().getSelectedItems().addListener((Change<? extends CountriesRecord> c) -> {
            if (c.getList().isEmpty())
                selected = ctx().newRecord(COUNTRIES);
            else
                for (CountriesRecord record : c.getList())
                    selected = record;
        });

        GridPane editPane = new GridPane();
        int i = 0;
        for (Field<?> field : COUNTRIES.fields()) {
            Label label = new Label(field.getName());
            TextField textField = new TextField();

            textField.textProperty().addListener((o, oldV, newV) -> {
                selected.set((Field) field, newV);
            });

            table.getSelectionModel().getSelectedItems().addListener((Change<? extends CountriesRecord> c) -> {
                if (c.getList().isEmpty())
                    textField.setText("");
                else
                    for (CountriesRecord record : c.getList())
                        textField.setText(record.get(field, String.class));
            });

            editPane.addRow(i++, label, textField);
        }

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            try {
                if (selected.store() > 0)
                    refresh.run();
            }
            catch (DataAccessException e) {
                e.printStackTrace();
                error.setText(e.sqlStateClass() + ": " + e.getCause().getMessage());
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            try {
                if (selected.delete() > 0)
                    refresh.run();
            }
            catch (DataAccessException e) {
                e.printStackTrace();
                error.setText(e.sqlStateClass() + ": " + e.getCause().getMessage());
            }
        });

        GridPane buttonPane = new GridPane();
        buttonPane.addRow(0, saveButton, deleteButton);

        editPane.addRow(i++, new Label(""), buttonPane);

        GridPane chartPane = new GridPane();
        chartPane.addColumn(0, chart1, chart2);
        grow(chartPane);

        GridPane display = new GridPane();
        display.addRow(0, chartPane, table, editPane);
        grow(display);

        GridPane displayAndStatus = new GridPane();
        displayAndStatus.addColumn(0, display, error);
        displayAndStatus.setGridLinesVisible(true);
        grow(displayAndStatus);


        Scene scene = new Scene(displayAndStatus, 1000, 800);
        stage.setScene(scene);
        stage.show();
    }

    private DSLContext ctx() {
        return DSL.using(connection, new Settings().withUpdatablePrimaryKeys(true));
    }

    private void grow(GridPane pane) {
        ColumnConstraints col = new ColumnConstraints();
        col.setFillWidth(true);
        col.setHgrow(Priority.ALWAYS);
        pane.getColumnConstraints().add(col);

        RowConstraints row = new RowConstraints();
        row.setFillHeight(true);
        row.setVgrow(Priority.ALWAYS);
        pane.getRowConstraints().add(row);
    }

    private <R extends Record> TableView<R> table(Table<R> table) {
        TableView<R> view = new TableView<>();

        view.setEditable(true);
        view.getColumns().addAll(
            Stream.of(table.fields())
                  .map(f -> {
                      TableColumn<R, Object> column = new TableColumn<>(f.getName());
                      column.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().get(f)));
                      return column;
                  })
                  .collect(toList())
        );

        return view;
    }

    private BarChart<String, Number> chart(TableField<CountriesRecord, ? extends Number> field, String title, String yAxisLabel) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Country");
        yAxis.setLabel(yAxisLabel);

        BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setTitle(title);
        bc.setUserData(field);

        return bc;
    }

    private BarChart<String, Number> chartRefresh(BarChart<String, Number> bc) {
        TableField<CountriesRecord, Number> field = (TableField<CountriesRecord, Number>) bc.getUserData();

        bc.getData().clear();
        bc.getData().addAll(

            // SQL data transformation, executed in the database
            // -------------------------------------------------
            ctx()
               .select(
                   COUNTRIES.YEAR,
                   COUNTRIES.CODE,
                   field)
               .from(COUNTRIES)
               .join(
                   DSL.table(
                       select(COUNTRIES.CODE, avg(field).as("avg"))
                       .from(COUNTRIES)
                       .groupBy(COUNTRIES.CODE)
                   ).as("c1")
               )
               .on(COUNTRIES.CODE.eq(field(name("c1", COUNTRIES.CODE.getName()), String.class)))

               // order countries by their average projected value
               .orderBy(
                   field(name("avg")),
                   COUNTRIES.CODE,
                   COUNTRIES.YEAR)

               // The result produced by the above statement looks like this:
               // +----+----+---------+
               // |year|code|govt_debt|
               // +----+----+---------+
               // |2009|RU  |     8.70|
               // |2010|RU  |     9.10|
               // |2011|RU  |     9.30|
               // |2012|RU  |     9.40|
               // |2009|CA  |    51.30|
               // +----+----+---------+

            // Java data transformation, executed in application memory
            // --------------------------------------------------------

               // Group results by year, keeping sort order in place
               .fetchGroups(COUNTRIES.YEAR)

               // Stream<Entry<Integer, Result<Record3<BigDecimal, String, Integer>>>>
               .entrySet()
               .stream()

               // Map each entry into a { Year -> Projected value } series
               .map(entry -> new XYChart.Series<>(
                   entry.getKey().toString(),
                   observableArrayList(

                       // Map each country record into a chart Data object
                       entry.getValue()
                            .map(country -> new XYChart.Data<String, Number>(
                                 country.get(COUNTRIES.CODE),
                                 country.get(field)
                            ))
                   )
               ))
               .collect(toList())
        );

        // If you're using PostgreSQL any commercial database, you could have
        // also taken advantage of window functions, which would have greatly
        // simplified the above SQL query:
        ctx()
           .select(
               COUNTRIES.YEAR,
               COUNTRIES.CODE,
               field)
           .from(COUNTRIES)

           // order countries by their average projected value
           .orderBy(
               DSL.avg(field).over(partitionBy(COUNTRIES.CODE)),
               COUNTRIES.CODE,
               COUNTRIES.YEAR)
        // .fetch()
           ;

        return bc;
    }

    @Override
    public void init() throws Exception {
        super.init();

        Properties properties = new Properties();
        properties.load(BarChartSample.class.getResourceAsStream("/config.properties"));

        Class.forName(properties.getProperty("db.driver"));
        connection = DriverManager.getConnection(
            properties.getProperty("db.url"),
            properties.getProperty("db.username"),
            properties.getProperty("db.password")
        );
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