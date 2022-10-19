/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.emptyGroupingSet;

import java.util.Set;

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.GroupField;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.UniqueKey;

/**
 * @author Lukas Eder
 */
final class GroupFieldList extends QueryPartList<GroupField> {

    static final Set<SQLDialect> NO_SUPPORT_GROUP_BY_TABLE       = SQLDialect.supportedBy(DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB);
    static final Set<SQLDialect> NO_SUPPORT_GROUP_FUNCTIONAL_DEP = SQLDialect.supportedBy(DERBY, FIREBIRD);

    GroupFieldList() {
        super();
    }

    GroupFieldList(Iterable<? extends GroupField> wrappedList) {
        super(wrappedList);
    }

    GroupFieldList(GroupField[] wrappedList) {
        super(wrappedList);
    }

    @Override
    final boolean canAdd(GroupField e) {
        return super.canAdd(e) && !(e instanceof NoField);
    }

    @Override
    public final boolean rendersContent(Context<?> ctx) {
        return true;
    }

    @Override
    protected final void toSQLEmptyList(Context<?> ctx) {
        ctx.sql(' ').visit(emptyGroupingSet());
    }

    @Override
    protected final void acceptElement(Context<?> ctx, GroupField part) {
        if (part instanceof Table<?> t) {
            if (NO_SUPPORT_GROUP_BY_TABLE.contains(ctx.dialect())) {
                Field<?>[] f = fields(ctx, t);

                if (f.length > 1)
                    ctx.visit(QueryPartListView.wrap(f));
                else if (f.length == 1)
                    ctx.visit(f[0]);
                else
                    super.acceptElement(ctx, part);
            }
            else
                super.acceptElement(ctx, part);
        }
        else
            super.acceptElement(ctx, part);
    }

    private final Field<?>[] fields(Context<?> ctx, Table<?> t) {
        UniqueKey<?> pk = t.getPrimaryKey();

        if (pk == null || NO_SUPPORT_GROUP_FUNCTIONAL_DEP.contains(ctx.dialect()))
            return t.fields();
        else
            return t.fields(pk.getFieldsArray());
    }
}
