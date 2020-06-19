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
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.Keywords.K_FOR;
import static org.jooq.impl.Keywords.K_LOCK_IN_SHARE_MODE;
import static org.jooq.impl.Keywords.K_NOWAIT;
import static org.jooq.impl.Keywords.K_OF;
import static org.jooq.impl.Keywords.K_READPAST;
import static org.jooq.impl.Keywords.K_ROWLOCK;
import static org.jooq.impl.Keywords.K_UPDLOCK;
import static org.jooq.impl.Keywords.K_WITH;
import static org.jooq.impl.Keywords.K_WITH_LOCK;
import static org.jooq.impl.QueryPartCollectionView.wrap;

import java.util.Set;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Keyword;
// ...
import org.jooq.QueryPart;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.impl.Tools.DataExtendedKey;

/**
 * A class wrapping a {@link Select} query's <code>FOR UPDATE</code> and other
 * locking clauses.
 *
 * @author Lukas Eder
 */
final class ForLock extends AbstractQueryPart {

    /**
     * Generated UID
     */
    private static final long            serialVersionUID                = 5315134153342265917L;
    private static final Set<SQLDialect> NO_SUPPORT_FOR_UPDATE_QUALIFIED = SQLDialect.supportedBy(DERBY, FIREBIRD, H2, HSQLDB);
    private static final Set<SQLDialect> NO_SUPPORT_STANDARD_FOR_SHARE   = SQLDialect.supportedUntil(MARIADB);





    QueryPartList<Field<?>>              forLockOf;
    TableList                            forLockOfTables;
    ForLockMode                          forLockMode;
    ForLockWaitMode                      forLockWaitMode;
    int                                  forLockWait;

    @Override
    public final void accept(Context<?> ctx) {





        switch (forLockMode) {
            case UPDATE:
                ctx.formatSeparator()
                       .visit(K_FOR)
                       .sql(' ')
                       .visit(forLockMode.toKeyword());
                break;

            case SHARE:
                if (NO_SUPPORT_STANDARD_FOR_SHARE.contains(ctx.dialect()))
                    ctx.formatSeparator()
                        .visit(K_LOCK_IN_SHARE_MODE);
                else
                    ctx.formatSeparator()
                        .visit(K_FOR)
                        .sql(' ')
                        .visit(forLockMode.toKeyword());
                break;

            case KEY_SHARE:
            case NO_KEY_UPDATE:
            default:
                ctx.formatSeparator()
                       .visit(K_FOR)
                       .sql(' ')
                       .visit(forLockMode.toKeyword());
                break;
        }

        if (Tools.isNotEmpty(forLockOf)) {

            // [#4151] [#6117] Some databases don't allow for qualifying column
            // names here. Copy also to TableList
            boolean unqualified = NO_SUPPORT_FOR_UPDATE_QUALIFIED.contains(ctx.family());
            boolean qualify = ctx.qualify();

            if (unqualified)
                ctx.qualify(false);

            ctx.sql(' ').visit(K_OF)
                   .sql(' ').visit(forLockOf);

            if (unqualified)
                ctx.qualify(qualify);
        }
        else if (Tools.isNotEmpty(forLockOfTables)) {
            ctx.sql(' ').visit(K_OF).sql(' ');

            switch (ctx.family()) {

                // Some dialects don't allow for an OF [table-names] clause
                // It can be emulated by listing the table's fields, though







                case DERBY: {
                    forLockOfTables.toSQLFields(ctx);
                    break;
                }

                // Render the OF [table-names] clause
                default:
                    ctx.visit(wrap(forLockOfTables).qualify(false).indentSize(0));
                    break;
            }
        }

        // [#3186] Firebird's FOR UPDATE clause has a different semantics. To achieve "regular"
        // FOR UPDATE semantics, we should use FOR UPDATE WITH LOCK
        if (ctx.family() == FIREBIRD)
            ctx.sql(' ').visit(K_WITH_LOCK);

        if (forLockWaitMode != null) {
            ctx.sql(' ');
            ctx.visit(forLockWaitMode.toKeyword());

            if (forLockWaitMode == ForLockWaitMode.WAIT) {
                ctx.sql(' ');
                ctx.sql(forLockWait);
            }
        }
    }

























    /**
     * The lock mode for the <code>FOR UPDATE</code> clause, if set.
     */
    static enum ForLockMode {
        UPDATE("update"),
        NO_KEY_UPDATE("no key update"),
        SHARE("share"),
        KEY_SHARE("key share"),

        ;

        private final Keyword keyword;

        private ForLockMode(String sql) {
            this.keyword = DSL.keyword(sql);
        }

        public final Keyword toKeyword() {
            return keyword;
        }
    }

    /**
     * The wait mode for the <code>FOR UPDATE</code> clause, if set.
     */
    static enum ForLockWaitMode {
        WAIT("wait"),
        NOWAIT("nowait"),
        SKIP_LOCKED("skip locked"),

        ;

        private final Keyword keyword;

        private ForLockWaitMode(String sql) {
            this.keyword = DSL.keyword(sql);
        }

        public final Keyword toKeyword() {
            return keyword;
        }
    }
}
