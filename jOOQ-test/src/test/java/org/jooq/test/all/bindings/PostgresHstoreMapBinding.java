/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.test.all.bindings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.BindingSQLContext;
import org.jooq.Converter;
import org.jooq.impl.DSL;
import org.jooq.lambda.Seq;
import org.jooq.tools.csv.CSVParser;

@SuppressWarnings("serial")
public class PostgresHstoreMapBinding extends AbstractVarcharBinding<Map<String, String>> {

    @Override
    public Converter<Object, Map<String, String>> converter() {
        return new Converter<Object, Map<String, String>>() {
            @Override
            public Map<String, String> from(Object t) {
                if (t == null)
                    return null;

                try {
                    String[] kvs = new CSVParser(',').parseLine(t + "");
                    Map<String, String> result = new LinkedHashMap<>();

                    for (String kv : kvs) {
                        String[] split = kv.split("=>");

                        if (split.length == 2) {
                            result.put(split[0].replaceAll("^\"?(.*?)\"?$", "$1"), split[1].replaceAll("^\"?(.*?)\"?$", "$1"));
                        }
                    }

                    return result;
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Object to(Map<String, String> u) {
                return u == null ? null : Seq.seq(u).map(t -> t.v1 + "=>" + t.v2).collect(Collectors.joining(","));
            }

            @Override
            public Class<Object> fromType() {
                return Object.class;
            }

            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public Class<Map<String, String>> toType() {
                return (Class) Map.class;
            }
        };
    }

    @Override
    public void sql(BindingSQLContext<Map<String, String>> ctx) throws SQLException {
        ctx.render().visit(DSL.val(ctx.convert(converter()).value())).sql("::hstore");
    }
}
