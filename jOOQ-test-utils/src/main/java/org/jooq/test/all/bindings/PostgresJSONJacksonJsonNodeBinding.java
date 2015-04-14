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

import org.jooq.BindingSQLContext;
import org.jooq.Converter;
import org.jooq.impl.DSL;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.NullNode;

@SuppressWarnings("serial")
public class PostgresJSONJacksonJsonNodeBinding extends AbstractVarcharBinding<JsonNode> {

    @Override
    public Converter<Object, JsonNode> converter() {
        return new Converter<Object, JsonNode>() {
            @Override
            public JsonNode from(Object t) {
                try {
                    return t == null ? NullNode.instance : new ObjectMapper().readTree(t + "");
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Object to(JsonNode u) {
                try {
                    return u == null || u.equals(NullNode.instance) ? null : new ObjectMapper().writeValueAsString(u);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Class<Object> fromType() {
                return Object.class;
            }

            @Override
            public Class<JsonNode> toType() {
                return JsonNode.class;
            }
        };
    }

    @Override
    public void sql(BindingSQLContext<JsonNode> ctx) throws SQLException {
        ctx.render().visit(DSL.val(ctx.convert(converter()).value())).sql("::json");
    }
}
