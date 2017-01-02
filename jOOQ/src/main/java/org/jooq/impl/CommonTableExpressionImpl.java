/*
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
package org.jooq.impl;

import java.util.List;

import org.jooq.CommonTableExpression;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class CommonTableExpressionImpl<R extends Record> extends AbstractTable<R> implements CommonTableExpression<R> {

    /**
     * Generated UID
     */
    private static final long           serialVersionUID = 2520235151216758958L;

    private final DerivedColumnListImpl name;
    private final Select<R>             select;
    private final Fields<R>             fields;

    CommonTableExpressionImpl(DerivedColumnListImpl name, Select<R> select) {
        super(name.name);

        this.name = name;
        this.select = select;
        this.fields = fields1();
    }

    @Override
    public final Class<? extends R> getRecordType() {
        return select.getRecordType();
    }

    @Override
    public final Table<R> as(String alias) {
        return new TableAlias<R>(this, alias);
    }

    @Override
    public final Table<R> as(String alias, String... fieldAliases) {
        return new TableAlias<R>(this, alias, fieldAliases);
    }

    @Override
    public final boolean declaresCTE() {
        return true;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ctx.declareCTE()) {
            boolean subquery = ctx.subquery();

            ctx.visit(name)
               .sql(' ')
               .keyword("as")
               .sql(" (")
               .subquery(true)
               .formatIndentStart()
               .formatNewLine()
               .visit(select)
               .formatIndentEnd()
               .formatNewLine()
               .subquery(subquery)
               .sql(')');
        }
        else {
            ctx.visit(DSL.name(name.name));
        }
    }

    @Override
    final Fields<R> fields0() {
        return fields;
    }

    final Fields<R> fields1() {
        List<Field<?>> s = select.getSelect();
        Field<?>[] f = new Field[s.size()];

        for (int i = 0; i < f.length; i++) {
            f[i] = DSL.field(
                DSL.name(
                    name.name,

                    // If the CTE has no explicit column names, inherit those of the subquery
                    name.fieldNames.length > 0
                        ? name.fieldNames[i]
                        : s.get(i).getName()),
                s.get(i).getDataType()
            );
        }

        Fields<R> result = new Fields<R>(f);
        return result;
    }
}
