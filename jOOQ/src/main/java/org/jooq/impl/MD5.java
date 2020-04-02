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

import static org.jooq.impl.Keywords.K_VARCHAR;
import static org.jooq.impl.Names.N_CONVERT;
import static org.jooq.impl.Names.N_HASHBYTES;
import static org.jooq.impl.Names.N_LOWER;
import static org.jooq.impl.Names.N_MD5;
import static org.jooq.impl.Names.N_RAWTOHEX;
import static org.jooq.impl.Names.N_STANDARD_HASH;
import static org.jooq.impl.SQLDataType.VARCHAR;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
// ...
// ...

/**
 * @author Lukas Eder
 */
final class MD5 extends AbstractField<String> {








    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -7273879239726265322L;

    private final Field<String> argument;

    MD5(Field<String> argument) {
        super(N_MD5, VARCHAR);

        this.argument = argument;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.dialect()) {























            default:
                ctx.visit(N_MD5).sql('(').visit(argument).sql(')');
                break;
        }
    }
}
