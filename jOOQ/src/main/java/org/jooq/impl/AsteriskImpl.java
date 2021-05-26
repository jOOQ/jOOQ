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
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
// ...
import static org.jooq.impl.Keywords.K_EXCEPT;

import java.util.Arrays;
import java.util.Set;

import org.jooq.Asterisk;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class AsteriskImpl extends AbstractQueryPart implements Asterisk {
    static final AsteriskImpl     INSTANCE                        = new AsteriskImpl(new QueryPartList<>());
    static final Set<SQLDialect>  SUPPORT_NATIVE_EXCEPT           = SQLDialect.supportedBy(H2);
    static final Set<SQLDialect>  NO_SUPPORT_UNQUALIFIED_COMBINED = SQLDialect.supportedBy(FIREBIRD);
    final QueryPartList<Field<?>> fields;

    private AsteriskImpl(QueryPartList<Field<?>> fields) {
        this.fields = fields;
    }

    @Override
    public final void accept(Context<?> ctx) {











        ctx.sql('*');

        // [#7921] H2 has native support for EXCEPT. Emulations are implemented
        //         in SelectQueryImpl
        if (!fields.isEmpty())
            ctx.sql(' ').visit(K_EXCEPT).sql(" (").visit(fields).sql(')');
    }

    @Override
    public final Asterisk except(String... fieldNames) {
        return except(Tools.fieldsByName(fieldNames));
    }

    @Override
    public final Asterisk except(Name... fieldNames) {
        return except(Tools.fieldsByName(fieldNames));
    }

    @Override
    public final Asterisk except(Field<?>... f) {
        QueryPartList<Field<?>> list = new QueryPartList<>();

        list.addAll(fields);
        list.addAll(Arrays.asList(f));

        return new AsteriskImpl(list);
    }
}
