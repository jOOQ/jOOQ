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
package org.jooq.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.ConnectionCallable;
import org.jooq.DSLContext;
import org.jooq.Explain;
import org.jooq.Field;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.tools.jdbc.JDBCUtils;

/**
 * @author Lukas Eder
 */
final class ExplainQuery {

    static Explain explain(final DSLContext ctx, final Query query) {
        Result<Record> result = null;

        switch (ctx.family()) {






























            case H2:
                result = ctx.fetch("{explain analyze} {0}", query);
                break;

            case MARIADB:
            case MYSQL:
            case POSTGRES:
            default:
                result = ctx.fetch("{explain} {0}", query);
                break;
        }

        double cost = Double.NaN;
        double rows = Double.NaN;

        switch (ctx.family()) {























            case H2: {
                // H2's EXPLAIN ANALYZE output is rather difficult to digest
                break;
            }
            case MARIADB:
            case MYSQL: {
                // MySQL doesn't expose any cost (yet).
                rows = result.get(0).get("rows", double.class);
                break;
            }
            case POSTGRES: {
                Matcher matcher = Pattern
                    .compile(".*\\bcost=\\d+\\.\\d+\\.\\.(\\d+\\.\\d+)\\s+rows=(\\d+).*")
                    .matcher(result.get(0).get(0, String.class));
                cost = Double.parseDouble(matcher.replaceAll("$1"));
                rows = Double.parseDouble(matcher.replaceAll("$2"));

                break;
            }
        }

        return new ExplainImpl(rows, cost, result.format());
    }

    private static final class ExplainImpl implements Explain {

        private final double rows;
        private final double cost;
        private final String plan;

        ExplainImpl(double rows, double cost, String plan) {
            this.rows = rows;
            this.cost = cost;
            this.plan = plan;
        }

        @Override
        public final double rows() {
            return rows;
        }

        @Override
        public final double cost() {
            return cost;
        }

        @Override
        public final String plan() {
            return plan;
        }

        @Override
        public String toString() {
            return String.format("Explain [cost=%.2f, rows=%.2f]\n\n", cost, rows) + plan;
        }
    }
}
