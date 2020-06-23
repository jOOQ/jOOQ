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
 *
 *
 *
 */
package org.jooq.impl;

// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_MATERIALIZED;
import static org.jooq.impl.Keywords.K_NOT;
import static org.jooq.impl.Tools.visitSubquery;

import java.util.List;
import java.util.Set;

import org.jooq.CommonTableExpression;
import org.jooq.Context;
import org.jooq.Field;
// ...
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.TableOptions;
import org.jooq.impl.Tools.DataKey;

/**
 * @author Lukas Eder
 */
final class CommonTableExpressionImpl<R extends Record> extends AbstractTable<R> implements CommonTableExpression<R> {

    /**
     * Generated UID
     */
    private static final long            serialVersionUID     = 2520235151216758958L;

    private static final Set<SQLDialect> SUPPORT_MATERIALIZED = SQLDialect.supportedBy(POSTGRES);





    private final DerivedColumnListImpl  name;
    private final Select<R>              select;
    private final Fields<R>              fields;
    private final Boolean                materialized;

    CommonTableExpressionImpl(DerivedColumnListImpl name, Select<R> select, Boolean materialized) {
        super(TableOptions.expression(), name.name);

        this.name = name;
        this.select = select;
        this.fields = fields1();
        this.materialized = materialized;
    }

    @Override
    public final Class<? extends R> getRecordType() {
        return select.getRecordType();
    }

    @Override
    public final boolean declaresCTE() {
        return true;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ctx.declareCTE()) {
            ctx.visit(name).sql(' ').visit(K_AS).sql(' ');

            Object previous = null;
            if (materialized != null) {
                if (SUPPORT_MATERIALIZED.contains(ctx.family())) {
                    if (materialized)
                        ctx.visit(K_MATERIALIZED).sql(' ');
                    else
                        ctx.visit(K_NOT).sql(' ').visit(K_MATERIALIZED).sql(' ');
                }





            }

            visitSubquery(ctx, select, true);






        }
        else
            ctx.visit(DSL.name(name.name));
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
                        : s.get(i).getUnqualifiedName()),
                s.get(i).getDataType()
            );
        }

        Fields<R> result = new Fields<>(f);
        return result;
    }
}
