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
package org.jooq.example.chart;

import static org.jooq.example.chart.db.Tables.*;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;
import static spark.Spark.*;

import java.util.Properties;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariDataSource;

import spark.Request;
import spark.Route;

/**
 * Reports on the Sakila database
 */
public class SakilaReportService {

    static Logger     log = LoggerFactory.getLogger(SakilaReportService.class);
    static DSLContext dsl;

    public static void main(String[] args) throws Exception {
        initDB();
        initSpark();

        get("/storeIds", storeIds());
        get("/countryIds", countryIds());
        get("/cumulativeEarnings", cumulativeEarnings());
        get("/rentalsPerCountry", rentalsPerCountry());
        get("/rentalsPerCategory", rentalsPerCategory());
    }

    private static void initDB() throws Exception {
        final Properties properties = new Properties();
        properties.load(SakilaReportService.class.getResourceAsStream("/config.properties"));

        Class.forName(properties.getProperty("db.driver"));
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(properties.getProperty("db.url"));
        ds.setUsername(properties.getProperty("db.username"));
        ds.setPassword(properties.getProperty("db.password"));

        // Some nice debug logging of formatted queries and the first 5 rows in each result set.
        dsl = DSL.using(new DefaultConfiguration()
            .set(ds)
            .set(DefaultExecuteListenerProvider.providers(new DefaultExecuteListener() {
                @Override
                public void executeEnd(ExecuteContext ctx) {
                    Configuration config = ctx.configuration().derive();
                    config.settings().setRenderFormatted(true);
                    log.info("\n" + DSL.using(config).renderInlined(ctx.query()));
                }

                @Override
                public void fetchEnd(ExecuteContext ctx) {
                    log.info("\n" + ctx.result().format(5));
                }
            })));
    }

    private static void initSpark() {
        staticFiles.location("/static");
        exception(Exception.class, (e, req, res) -> log.error("Error", e));
    }

    private static Route storeIds() {
        return (req, res) -> dsl
            .select(STORE.STORE_ID, ADDRESS.ADDRESS_)
            .from(STORE)
            .join(ADDRESS).using(ADDRESS.ADDRESS_ID)
            .orderBy(ADDRESS.ADDRESS_)
            .fetch()
            .formatJSON();
    }

    private static Route countryIds() {
        return (req, res) -> dsl
            .select(COUNTRY.COUNTRY_ID, COUNTRY.COUNTRY_)
            .from(COUNTRY)
            .orderBy(COUNTRY.COUNTRY_)
            .fetch()
            .formatJSON();
    }

    private static Route cumulativeEarnings() {
        return (req, res) -> dsl
            .select(
                STORE.STORE_ID,
                PAYMENT.PAYMENT_DATE.cast(DATE).as(PAYMENT.PAYMENT_DATE),
                sum(sum(PAYMENT.AMOUNT)).over(partitionBy(STORE.STORE_ID).orderBy(PAYMENT.PAYMENT_DATE.cast(DATE))).as(PAYMENT.AMOUNT)
            )
            .from(STORE)
            .join(INVENTORY).using(INVENTORY.STORE_ID)
            .join(RENTAL).using(RENTAL.INVENTORY_ID)
            .join(PAYMENT).using(PAYMENT.RENTAL_ID)
            .where(storeIdCondition(req))
            .and(countryIdSemiJoinCondition(req))
            .groupBy(PAYMENT.PAYMENT_DATE.cast(DATE), STORE.STORE_ID)
            .orderBy(PAYMENT.PAYMENT_DATE.cast(DATE))
            .fetch()
            .formatJSON();
    }

    private static Route rentalsPerCountry() {
        return (req, res) -> dsl
            .select(
                STORE.STORE_ID,
                COUNTRY.COUNTRY_,
                count(),
                sum(count()).over(partitionBy(COUNTRY.COUNTRY_)))
            .from(STORE)
            .join(INVENTORY).on(STORE.STORE_ID.eq(INVENTORY.STORE_ID))
            .join(RENTAL).on(INVENTORY.INVENTORY_ID.eq(RENTAL.INVENTORY_ID))
            .join(CUSTOMER).on(RENTAL.CUSTOMER_ID.eq(CUSTOMER.CUSTOMER_ID))
            .join(ADDRESS).on(CUSTOMER.ADDRESS_ID.eq(ADDRESS.ADDRESS_ID))
            .join(CITY).on(ADDRESS.CITY_ID.eq(CITY.CITY_ID))
            .join(COUNTRY).on(CITY.COUNTRY_ID.eq(COUNTRY.COUNTRY_ID))
            .where(storeIdCondition(req))
            .and(countryIdCondition(req))
            .groupBy(STORE.STORE_ID, COUNTRY.COUNTRY_)
            .orderBy(inline(4).desc(), COUNTRY.COUNTRY_.asc())
            .fetch()
            .formatJSON();
    }

    private static Route rentalsPerCategory() {
        return (req, res) -> dsl
            .select(
                STORE.STORE_ID,
                CATEGORY.NAME,
                count(),
                sum(count()).over(partitionBy(CATEGORY.NAME)))
            .from(STORE)
            .join(INVENTORY).on(STORE.STORE_ID.eq(INVENTORY.STORE_ID))
            .join(RENTAL).on(INVENTORY.INVENTORY_ID.eq(RENTAL.INVENTORY_ID))
            .join(FILM_CATEGORY).on(INVENTORY.FILM_ID.eq(FILM_CATEGORY.FILM_ID))
            .join(CATEGORY).on(FILM_CATEGORY.CATEGORY_ID.eq(CATEGORY.CATEGORY_ID))
            .where(storeIdCondition(req))
            .and(countryIdSemiJoinCondition(req))
            .groupBy(STORE.STORE_ID, CATEGORY.NAME)
            .orderBy(inline(4).desc(), CATEGORY.NAME.asc())
            .fetch()
            .formatJSON();
    }

    /**
     * Utility to create a condition based on an optional "storeId" HTTP GET parameter.
     */
    private static Condition storeIdCondition(Request req) {
        return storeIdAll(req)
            ? trueCondition()
            : STORE.STORE_ID.eq(val(req.queryParams("storeId"), int.class));
    }

    /**
     * Whether the "storeId" HTTP GET parameter is set.
     */
    private static boolean storeIdAll(Request req) {
        return req.queryParams("storeId") == null;
    }

    /**
     * Utility to create a condition based on an optional "countryId" HTTP GET parameter.
     */
    private static Condition countryIdCondition(Request req) {
        return countryIdAll(req)
            ? trueCondition()
            : COUNTRY.COUNTRY_ID.eq(val(req.queryParams("countryId"), int.class));
    }

    /**
     * Utility to create a semi condition based on an optional "countryId" HTTP GET parameter.
     */
    private static Condition countryIdSemiJoinCondition(Request req) {
        return countryIdAll(req)
            ? trueCondition()
            : RENTAL.CUSTOMER_ID.in(
                select(CUSTOMER.CUSTOMER_ID)
                .from(CUSTOMER)
                .join(ADDRESS).on(CUSTOMER.ADDRESS_ID.eq(ADDRESS.ADDRESS_ID))
                .join(CITY).on(ADDRESS.CITY_ID.eq(CITY.CITY_ID))
                .join(COUNTRY).on(CITY.COUNTRY_ID.eq(COUNTRY.COUNTRY_ID))
                .where(COUNTRY.COUNTRY_ID.eq(val(req.queryParams("countryId"), int.class))));
    }

    /**
     * Whether the "countryId" HTTP GET parameter is set.
     */
    private static boolean countryIdAll(Request req) {
        return req.queryParams("countryId") == null;
    }
}
