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

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.BindingSQLContext;
import org.jooq.Converter;
import org.jooq.impl.DSL;
import org.jooq.lambda.tuple.Range;
import org.jooq.lambda.tuple.Tuple;

@SuppressWarnings("serial")
public class PostgresInt4RangeBinding extends AbstractVarcharBinding<Range<Integer>> {

    private static final Pattern PATTERN = Pattern.compile("\\[(.*?),(.*?)\\)");
    
    @Override
    public Converter<Object, Range<Integer>> converter() {
        return new Converter<Object, Range<Integer>>() {
            @Override
            public Range<Integer> from(Object t) {
                if (t == null)
                    return null;
                
                Matcher m = PATTERN.matcher("" + t);
                if (m.find())
                    return Tuple.range(Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2)));
                
                throw new IllegalArgumentException("Unsupported range : " + t);
            }

            @Override
            public Object to(Range<Integer> u) {
                return u == null ? null : "[" + u.v1 + "," + u.v2 + ")";
            }

            @Override
            public Class<Object> fromType() {
                return Object.class;
            }

            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public Class<Range<Integer>> toType() {
                return (Class) Range.class;
            }
        };
    }

    @Override
    public void sql(BindingSQLContext<Range<Integer>> ctx) throws SQLException {
        ctx.render().visit(DSL.val(ctx.convert(converter()).value())).sql("::int4range");
    }
}
