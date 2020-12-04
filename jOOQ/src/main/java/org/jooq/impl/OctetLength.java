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

import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Names.N_BIT_LENGTH;
import static org.jooq.impl.Names.N_CHAR_LENGTH;
import static org.jooq.impl.Names.N_DATALENGTH;
import static org.jooq.impl.Names.N_LEN;
import static org.jooq.impl.Names.N_LENGTH;
import static org.jooq.impl.Names.N_LENGTHB;
import static org.jooq.impl.Names.N_OCTET_LENGTH;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.nullSafeNotNull;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class OctetLength extends AbstractField<Integer> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 1484652553287331042L;

    private final Field<String> field;

    OctetLength(Field<String> field) {
        super(N_BIT_LENGTH, INTEGER.nullable(field == null || field.getDataType().nullable()));

        this.field = nullSafeNotNull(field, VARCHAR);
    }

    @Override
    public void accept(Context<?> ctx) {
        switch (ctx.family()) {

















            case DERBY:
            case SQLITE:
                ctx.visit(function(N_LENGTH, getDataType(), field));
                break;

            default:
                ctx.visit(function(N_OCTET_LENGTH, getDataType(), field));
                break;
        }
    }
}
