/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
 *
 *
 *
 */
package org.jooq.example.javafx;


import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableArrayList;
import static org.jooq.example.db.h2.Tables.COUNTRIES;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.jooq.TableField;
import org.jooq.example.db.h2.tables.records.CountriesRecord;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.JDBCUtils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class BarChartSample extends Application {

    Connection connection;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Country statistics with jOOQ and JavaFX");

        // Create two charts, plotting a specific metric, each.
        BarChart<String, Number> chart1 = chart(COUNTRIES.GDP_PER_CAPITA, "GDP per Capita", "USD");
        BarChart<String, Number> chart2 = chart(COUNTRIES.GOVT_DEBT, "Government Debt", "% of GDP");

        GridPane pane = new GridPane();
        pane.addColumn(0, chart1, chart2);

        Scene scene = new Scene(pane, 500, 800);
        stage.setScene(scene);
        stage.show();
    }

    private BarChart<String, Number> chart(TableField<CountriesRecord, BigDecimal> field, String title, String yAxisLabel) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Country");
        yAxis.setLabel(yAxisLabel);

        BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setTitle(title);
        bc.getData().addAll(

            // SQL data transformation, executed in the database
            // -------------------------------------------------
            DSL.using(connection)
               .select(
                   COUNTRIES.YEAR,
                   COUNTRIES.CODE,
                   field)
               .from(COUNTRIES)
               .join(
                   table(
                       select(COUNTRIES.CODE, avg(field).as("avg"))
                       .from(COUNTRIES)
                       .groupBy(COUNTRIES.CODE)
                   ).as("c1")
               )
               .on(COUNTRIES.CODE.eq(fieldByName(String.class, "c1", COUNTRIES.CODE.getName())))

               // order countries by their average projected value
               .orderBy(
                   fieldByName("avg"),
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
                                 country.getValue(COUNTRIES.CODE),
                                 country.getValue(field)
                            ))
                   )
               ))
               .collect(toList())
        );

        // If you're using PostgreSQL any commercial database, you could have
        // also taken advantage of window functions, which would have greatly
        // simplified the above SQL query:
        DSL.using(connection)
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