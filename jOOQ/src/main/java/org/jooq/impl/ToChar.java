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
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.impl.Names.N_TO_CHAR;
import static org.jooq.impl.SQLDataType.VARCHAR;

import java.util.Set;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class ToChar extends AbstractField<String> {

    /**
     * Generated UID
     */
    private static final long            serialVersionUID            = 2484479701190490450L;
    private static final Set<SQLDialect> SUPPORT_NATIVE_WITHOUT_MASK = SQLDialect.supportedBy(H2);
    private static final Set<SQLDialect> SUPPORT_NATIVE_WITH_MASK    = SQLDialect.supportedBy(H2, POSTGRES);
    private final Field<?>               field;
    private final Field<String>          format;

    ToChar(Field<?> field, Field<String> format) {
        super(N_TO_CHAR, VARCHAR);

        this.field = field;
        this.format = format;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (format == null && !SUPPORT_NATIVE_WITHOUT_MASK.contains(ctx.dialect()))
            acceptCast(ctx);
        else if (format != null && !SUPPORT_NATIVE_WITH_MASK.contains(ctx.dialect()))
            acceptCast(ctx);
        else
            acceptNative(ctx);
    }

    private final void acceptNative(Context<?> ctx) {
        ctx.visit(N_TO_CHAR).sql('(').visit(field);

        if (format != null)
            ctx.sql(", ").visit(format);

        ctx.sql(')');
    }

    private final void acceptCast(Context<?> ctx) {
        ctx.visit(DSL.cast(field, VARCHAR));
    }
}
