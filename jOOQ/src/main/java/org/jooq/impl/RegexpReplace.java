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

import static org.jooq.impl.Keywords.K_IN;
import static org.jooq.impl.Keywords.K_OCCURRENCE;
import static org.jooq.impl.Keywords.K_WITH;
import static org.jooq.impl.Names.N_REGEXP_REPLACE;
import static org.jooq.impl.Names.N_REGEX_REPLACE;
import static org.jooq.impl.Names.N_REPLACE_REGEXPR;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class RegexpReplace extends AbstractField<String> {

    private final Field<String> field;
    private final Field<String> pattern;
    private final Field<String> replacement;
    private final boolean       all;

    RegexpReplace(Field<String> field, Field<String> pattern, Field<String> replacement, boolean all) {
        super(N_REGEXP_REPLACE, field.getDataType());

        this.field = field;
        this.pattern = pattern;
        this.replacement = replacement;
        this.all = all;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {



            case POSTGRES:
                ctx.visit(N_REGEXP_REPLACE).sql('(').visit(field).sql(", ").visit(pattern).sql(", ").visit(replacement);

                if (all)
                    ctx.sql(", 'g')");
                else
                    ctx.sql(')');

                break;


























            // [#10151] TODO: Emulate REGEXP_REPLACE_FIRST for these three
            case H2:
            case HSQLDB:
            case MARIADB:
            default:
                ctx.visit(N_REGEXP_REPLACE).sql('(').visit(field).sql(", ").visit(pattern).sql(", ").visit(replacement);

                if (all)
                    ctx.sql(')');
                else
                    ctx.sql(", 1, 1)");

                break;
        }
    }

    static String replacement(Context<?> ctx, int group) {
        switch (ctx.family()) {
            case MYSQL:
                return "$" + group;

            default:
                return "\\" + group;
        }
    }
}
