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

import static org.jooq.impl.Keywords.K_EXCEPT;

import java.util.Arrays;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.QualifiedAsterisk;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class QualifiedAsteriskImpl extends AbstractQueryPart implements QualifiedAsterisk {

    private static final long     serialVersionUID = 4509166418833560865L;
    private final Table<?>        table;
    final QueryPartList<Field<?>> fields;

    QualifiedAsteriskImpl(Table<?> table) {
        this(table, null);
    }

    QualifiedAsteriskImpl(Table<?> table, QueryPartList<Field<?>> fields) {
        this.table = table;
        this.fields = fields == null ? new QueryPartList<>() : fields;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(table).sql('.').visit(AsteriskImpl.INSTANCE);

        // [#7921] H2 has native support for EXCEPT. Emulations are implemented
        //         in SelectQueryImpl
        if (!fields.isEmpty())
            ctx.sql(' ').visit(K_EXCEPT).sql(" (").visit(fields).sql(')');
    }

    @Override
    public final Table<?> qualifier() {
        return table;
    }

    @Override
    public final QualifiedAsterisk except(String... fieldNames) {
        return except(Tools.fieldsByName(fieldNames));
    }

    @Override
    public final QualifiedAsterisk except(Name... fieldNames) {
        return except(Tools.fieldsByName(fieldNames));
    }

    @Override
    public final QualifiedAsterisk except(Field<?>... f) {
        QueryPartList<Field<?>> list = new QueryPartList<>();

        list.addAll(fields);
        list.addAll(Arrays.asList(f));

        return new QualifiedAsteriskImpl(table, list);
    }
}
