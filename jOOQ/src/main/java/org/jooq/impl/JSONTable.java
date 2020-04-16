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

import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Keywords.K_COLUMNS;
import static org.jooq.impl.Keywords.K_FOR;
import static org.jooq.impl.Keywords.K_JSON_TABLE;
import static org.jooq.impl.Keywords.K_ORDINALITY;
import static org.jooq.impl.Keywords.K_PATH;
import static org.jooq.impl.Names.N_JSON_TABLE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONTableColumnPathStep;
import org.jooq.Name;
// ...
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableOptions;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
final class JSONTable
extends AbstractTable<Record>
implements
    JSONTableColumnPathStep {

    /**
     * Generated UID
     */
    private static final long                    serialVersionUID     = -4881363881968319258L;
    private static final Set<SQLDialect>         REQUIRES_COLUMN_PATH = SQLDialect.supportedBy(MYSQL);
    private static final Set<SQLDialect>         REQUIRES_ALIASING    = SQLDialect.supportedBy(MYSQL);












































































































































        ctx.visit(path);
    }

    @Override
    public final Table<Record> as(Name alias) {
        return new TableAlias<>(this, alias);
    }

    @Override
    public boolean declaresTables() {
        return true;
    }

    private static class JSONTableColumn extends AbstractQueryPart {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 783627375014050176L;

        final Field<?>            field;
        final DataType<?>         type;
        final boolean             forOrdinality;
        final String              path;

        JSONTableColumn(Field<?> field, DataType<?> type, boolean forOrdinality, String path) {
            this.field = field;
            this.type = type;
            this.forOrdinality = forOrdinality;
            this.path = path;
        }

        @Override
        public final void accept(Context<?> ctx) {
            boolean previous = ctx.qualify();

            ctx.qualify(false)
               .visit(field)
               .qualify(previous)
               .sql(' ');

            if (forOrdinality)
                ctx.visit(K_FOR).sql(' ').visit(K_ORDINALITY);
            else
                Tools.toSQLDDLTypeDeclaration(ctx, type);

            if (path != null)
                ctx.sql(' ').visit(K_PATH).sql(' ').visit(inline(path));
            else if (!forOrdinality && REQUIRES_COLUMN_PATH.contains(ctx.dialect()))
                ctx.sql(' ').visit(K_PATH).sql(' ').visit(inline("$." + field.getName()));
        }
    }
}
