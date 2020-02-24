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
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Keywords.F_OVERLAY;
import static org.jooq.impl.Keywords.K_FOR;
import static org.jooq.impl.Keywords.K_FROM;
import static org.jooq.impl.Keywords.K_PLACING;
import static org.jooq.impl.Names.N_OVERLAY;

import java.util.Set;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class Overlay extends AbstractField<String> {

    private static final long             serialVersionUID = 3544690069533526544L;
    private static final Set<SQLDialect>  NO_SUPPORT       = SQLDialect.supportedBy(DERBY, H2, HSQLDB, MARIADB, MYSQL, SQLITE);

    private final Field<String>           in;
    private final Field<String>           placing;
    private final Field<? extends Number> startIndex;
    private final Field<? extends Number> length;

    Overlay(Field<String> in, Field<String> placing, Field<? extends Number> startIndex) {
        this(in, placing, startIndex, null);
    }

    Overlay(Field<String> in, Field<String> placing, Field<? extends Number> startIndex, Field<? extends Number> length) {
        super(N_OVERLAY, in.getDataType());

        this.in = in;
        this.placing = placing;
        this.startIndex = startIndex;
        this.length = length;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (length != null) {
            if (NO_SUPPORT.contains(ctx.family())) {
                ctx.visit(
                    DSL.substring(in, inline(1), startIndex.minus(inline(1)))
                       .concat(placing)
                       .concat(DSL.substring(in, startIndex.plus(length)))
                );
            }
            else {
                ctx.visit(F_OVERLAY).sql('(').visit(in).sql(' ')
                   .visit(K_PLACING).sql(' ').visit(placing).sql(' ')
                   .visit(K_FROM).sql(' ').visit(startIndex).sql(' ')
                   .visit(K_FOR).sql(' ').visit(length).sql(')');
            }
        }
        else {
            if (NO_SUPPORT.contains(ctx.family())) {
                ctx.visit(
                    DSL.substring(in, inline(1), startIndex.minus(inline(1)))
                       .concat(placing)
                       .concat(DSL.substring(in, startIndex.plus(DSL.length(placing))))
                );
            }
            else {
                ctx.visit(F_OVERLAY).sql('(').visit(in).sql(' ')
                   .visit(K_PLACING).sql(' ').visit(placing).sql(' ')
                   .visit(K_FROM).sql(' ').visit(startIndex).sql(')');
            }
        }
    }
}
