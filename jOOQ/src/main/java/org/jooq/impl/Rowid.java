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

import static org.jooq.impl.DSL.unquotedName;

import org.jooq.Context;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class Rowid extends AbstractField<Object> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 1514220224208896376L;
    private final Table<?>    table;

    Rowid(Table<?> table) {
        super(table.getQualifiedName().append(unquotedName("rowid")), SQLDataType.OTHER);

        this.table = table;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case H2:
                ctx.visit(table.getQualifiedName().append(unquotedName("_rowid_")));
                break;

            case POSTGRES:
                ctx.visit(table.getQualifiedName().append(unquotedName("ctid")));
                break;









            case SQLITE:
            default:
                ctx.visit(getQualifiedName());
                break;
        }
    }
}
