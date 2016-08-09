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
package org.jooq.impl;

import static org.jooq.impl.DSL.name;

import java.util.List;
import java.util.function.BiFunction;

import org.jooq.Clause;
import org.jooq.CommonTableExpression;
import org.jooq.Context;
import org.jooq.DerivedColumnList;
import org.jooq.DerivedColumnList1;
import org.jooq.DerivedColumnList10;
import org.jooq.DerivedColumnList11;
import org.jooq.DerivedColumnList12;
import org.jooq.DerivedColumnList13;
import org.jooq.DerivedColumnList14;
import org.jooq.DerivedColumnList15;
import org.jooq.DerivedColumnList16;
import org.jooq.DerivedColumnList17;
import org.jooq.DerivedColumnList18;
import org.jooq.DerivedColumnList19;
import org.jooq.DerivedColumnList2;
import org.jooq.DerivedColumnList20;
import org.jooq.DerivedColumnList21;
import org.jooq.DerivedColumnList22;
import org.jooq.DerivedColumnList3;
import org.jooq.DerivedColumnList4;
import org.jooq.DerivedColumnList5;
import org.jooq.DerivedColumnList6;
import org.jooq.DerivedColumnList7;
import org.jooq.DerivedColumnList8;
import org.jooq.DerivedColumnList9;
import org.jooq.Field;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
final class DerivedColumnListImpl extends AbstractQueryPart
implements

    // [jooq-tools] START [implements-derived-column-list]
    DerivedColumnList1,
    DerivedColumnList2,
    DerivedColumnList3,
    DerivedColumnList4,
    DerivedColumnList5,
    DerivedColumnList6,
    DerivedColumnList7,
    DerivedColumnList8,
    DerivedColumnList9,
    DerivedColumnList10,
    DerivedColumnList11,
    DerivedColumnList12,
    DerivedColumnList13,
    DerivedColumnList14,
    DerivedColumnList15,
    DerivedColumnList16,
    DerivedColumnList17,
    DerivedColumnList18,
    DerivedColumnList19,
    DerivedColumnList20,
    DerivedColumnList21,
    DerivedColumnList22,

// [jooq-tools] END [implements-derived-column-list]

    DerivedColumnList {

    /**
     * Gemerated UID
     */
    private static final long                                             serialVersionUID = -369633206858851863L;

    final String                                                          name;
    final String[]                                                        fieldNames;

    final BiFunction<? super Field<?>, ? super Integer, ? extends String> fieldNameFunction;


    DerivedColumnListImpl(String name, String[] fieldNames) {
        this.name = name;
        this.fieldNames = fieldNames;

        this.fieldNameFunction = null;

    }


    DerivedColumnListImpl(String name, BiFunction<? super Field<?>, ? super Integer, ? extends String> fieldNameFunction) {
        this.name = name;
        this.fieldNames = null;
        this.fieldNameFunction = fieldNameFunction;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final CommonTableExpression as(Select select) {
        Select<?> s = select;


        if (fieldNameFunction != null) {
            List<Field<?>> source = s.getSelect();
            String[] names = new String[source.size()];
            for (int i = 0; i < names.length; i++)
                names[i] = fieldNameFunction.apply(source.get(i), i);
            return new CommonTableExpressionImpl(new DerivedColumnListImpl(name, names), s);
        }


        return new CommonTableExpressionImpl(this, s);
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(name(name));

        if (fieldNames != null && fieldNames.length > 0) {
            ctx.sql('(');

            for (int i = 0; i < fieldNames.length; i++) {
                if (i > 0)
                    ctx.sql(", ");

                ctx.visit(name(fieldNames[i]));
            }

            ctx.sql(')');
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
