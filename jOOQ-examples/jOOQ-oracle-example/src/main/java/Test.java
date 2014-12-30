import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;


/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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

public class Test {

    public static void main(String[] args) throws SQLException {
        String sql = "select                                                             "+
                     "  language.*,                                                      "+
                     "  cast(multiset(                                                   "+
                     "    select film_t(                                                 "+
                     "      film_id,                                                     "+
                     "      title,                                                       "+
                     "      description,                                                 "+
                     "      release_year,                                                "+
                     "      (                                                            "+
                     "        select language_t(                                         "+
                     "          language_id,                                             "+
                     "          name,                                                    "+
                     "          last_update)                                             "+
                     "        from                                                       "+
                     "          language                                                 "+
                     "        where language.language_id = film.language_id              "+
                     "      ), (                                                         "+
                     "        select language_t(                                         "+
                     "          language_id,                                             "+
                     "          name,                                                    "+
                     "          last_update)                                             "+
                     "        from                                                       "+
                     "          language                                                 "+
                     "        where language.language_id = film.original_language_id     "+
                     "      ),                                                           "+
                     "      rental_duration,                                             "+
                     "      rental_rate,                                                 "+
                     "      length,                                                      "+
                     "      replacement_cost,                                            "+
                     "      rating,                                                      "+
                     "      special_features,                                            "+
                     "      last_update                                                  "+
                     "    )                                                              "+
                     "    from film                                                      "+
                     "    where film.language_id = language.language_id                  "+
                     "  ) as films_t)                                                    "+
                     "from                                                               "+
                     "  language                                                         ";

        try (Connection c = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SAKILA", "SAKILA")) {
            DSLContext ctx = DSL.using(c);

            try {
                ctx.execute("create type plain_sql_o1 as object(a int, b clob)");
                ctx.execute("create type plain_sql_t1 as table of plain_sql_o1");
                ctx.execute("create type plain_sql_o2 as object(x plain_sql_o1, y plain_sql_t1)");
                ctx.execute("create type plain_sql_t2 as table of plain_sql_t1");

                Result<Record> result = ctx.fetch(
                    " SELECT "
                  + "    cast(null as plain_sql_o1),"
                  + "    cast(null as plain_sql_t1),"
                  + "    plain_sql_t1(),"
                  + "    plain_sql_t1(null),"
                  + "    plain_sql_t1(plain_sql_o1(null, null)),"
                  + "    plain_sql_t1(plain_sql_o1(1, 'abc'))"
                  + " FROM dual");

                System.out.println(result);
            }
            finally {
                ctx.execute("drop type plain_sql_t2");
                ctx.execute("drop type plain_sql_o2");
                ctx.execute("drop type plain_sql_t1");
                ctx.execute("drop type plain_sql_o1");
            }

//            DSL.using(c).fetch(rs).forEach(r -> {
//                try {
//                    System.out.println();
//                    System.out.println(r);
//                    final Consumer<? super Object>[] structConsumer = new Consumer[1];
//                    structConsumer[0] = s -> {
//                        Struct str = (Struct) s;
//
//                        try {
//                            List<Object> array = Arrays.asList(str.getAttributes());
//                            for (int i = 0; i < array.size(); i++)
//                                if (array.get(i) instanceof Struct) {
//                                    structConsumer[0].accept(array.get(i));
//                                    array.set(i, Arrays.asList(str.getAttributes()));
//                                }
//
//                            System.out.println(array);
//                        }
//                        catch (SQLException e) {
//                            throw new RuntimeException(e);
//                        }
//                    };
//
//                    Arrays.asList((Object[]) ((Array) r.getValue(3)).getArray()).forEach(structConsumer[0]);
//                }
//                catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//            });
        }
    }

}
