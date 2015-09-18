/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static org.jooq.Clause.ALTER_TABLE;
import static org.jooq.Clause.ALTER_TABLE_ADD;
import static org.jooq.Clause.ALTER_TABLE_ALTER;
import static org.jooq.Clause.ALTER_TABLE_ALTER_DEFAULT;
import static org.jooq.Clause.ALTER_TABLE_DROP;
import static org.jooq.Clause.ALTER_TABLE_TABLE;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.Utils.DATA_DROP_CONSTRAINT;
import static org.jooq.impl.Utils.toSQLDDLTypeDeclaration;

import org.jooq.AlterTableAlterStep;
import org.jooq.AlterTableDropStep;
import org.jooq.AlterTableFinalStep;
import org.jooq.AlterTableStep;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.Context;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
class AlterTableImpl extends AbstractQuery implements

    // Cascading interface implementations for ALTER TABLE behaviour
    AlterTableStep,
    AlterTableDropStep,
    AlterTableAlterStep {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { ALTER_TABLE };

    private final Table<?>        table;
    private Field<?>              addColumn;
    private DataType<?>           addColumnType;
    private Constraint            addConstraint;
    private Field<?>              alterColumn;
    private DataType<?>           alterColumnType;
    private Field<?>              alterColumnDefault;
    private Field<?>              dropColumn;
    private boolean               dropColumnCascade;
    private Constraint            dropConstraint;

    AlterTableImpl(Configuration configuration, Table<?> table) {
        super(configuration);

        this.table = table;
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final <T> AlterTableImpl add(Field<T> field, DataType<T> type) {
        return addColumn(field, type);
    }

    @Override
    public final AlterTableImpl add(String field, DataType<?> type) {
        return addColumn(field, type);
    }

    @Override
    public final AlterTableImpl addColumn(String field, DataType<?> type) {
        return addColumn((Field) field(name(field), type), type);
    }

    @Override
    public final <T> AlterTableImpl addColumn(Field<T> field, DataType<T> type) {
        addColumn = field;
        addColumnType = type;
        return this;
    }

    @Override
    public final AlterTableImpl add(Constraint constraint) {
        addConstraint = constraint;
        return this;
    }

    @Override
    public final <T> AlterTableImpl alter(Field<T> field) {
        return alterColumn(field);
    }

    @Override
    public final AlterTableImpl alter(String field) {
        return alterColumn(field);
    }

    @Override
    public final AlterTableImpl alterColumn(String field) {
        return alterColumn(field(name(field)));
    }

    @Override
    public final <T> AlterTableImpl alterColumn(Field<T> field) {
        alterColumn = field;
        return this;
    }

    @Override
    public final AlterTableImpl set(DataType type) {
        alterColumnType = type;
        return this;
    }

    @Override
    public final AlterTableImpl defaultValue(Object literal) {
        return defaultValue(inline(literal));
    }

    @Override
    public final AlterTableImpl defaultValue(Field expression) {
        alterColumnDefault = expression;
        return this;
    }

    @Override
    public final AlterTableImpl drop(Field<?> field) {
        return dropColumn(field);
    }

    @Override
    public final AlterTableImpl drop(String field) {
        return dropColumn(field);
    }

    @Override
    public final AlterTableImpl dropColumn(String field) {
        return dropColumn(field(name(field)));
    }

    @Override
    public final AlterTableImpl dropColumn(Field<?> field) {
        dropColumn = field;
        return this;
    }

    @Override
    public final AlterTableImpl drop(Constraint constraint) {
        dropConstraint = constraint;
        return this;
    }

    @Override
    public final AlterTableImpl dropConstraint(String constraint) {
        return drop(DSL.constraint(constraint));
    }

    @Override
    public final AlterTableFinalStep cascade() {
        dropColumnCascade = true;
        return this;
    }

    @Override
    public final AlterTableFinalStep restrict() {
        dropColumnCascade = false;
        return this;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        SQLDialect family = ctx.configuration().dialect().family();

        /* [pro] xx
        xx xxx xxxxxx xxxxxxx xxxxxx xxxxx xx xxxxx xxxxxxx xxxxxxx xxx xx xxx xxx x xxxxx xxxxxx xx xx xxxx
        xx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxxxxxxxxxx xx xxxxx x
            xxxxxxxxxxxxxxxxxxxxxxxxxxx
        x
        xxxx
        xx [/pro] */
        {
            accept0(ctx);
        }
    }

    private final void accept0(Context<?> ctx) {
        SQLDialect family = ctx.configuration().dialect().family();

        ctx.start(ALTER_TABLE_TABLE)
           .keyword("alter table").sql(' ').visit(table)
           .end(ALTER_TABLE_TABLE);

        if (addColumn != null) {
            ctx.start(ALTER_TABLE_ADD)
               .sql(' ').keyword("add").sql(' ');

            /* [pro] xx
            xx xxxxxxx xx xxxxx
                xxxxxxxxxxxxx
            xx [/pro] */

            ctx.qualify(false)
               .visit(addColumn).sql(' ')
               .qualify(true);

            toSQLDDLTypeDeclaration(ctx, addColumnType);

            if (!addColumnType.nullable()) {
                ctx.sql(' ').keyword("not null");
            }

            // Some databases default to NOT NULL, so explicitly setting columns to NULL is mostly required here
            // [#3400] ... but not in Firebird
            else if (family != FIREBIRD) {
                ctx.sql(' ').keyword("null");
            }

            /* [pro] xx
            xx xxxxxxx xx xxxxx
                xxxxxxxxxxxxx
            xx [/pro] */

            ctx.end(ALTER_TABLE_ADD);
        }
        else if (addConstraint != null) {
            ctx.start(ALTER_TABLE_ADD);

            ctx.sql(' ')
               .keyword("add")
               .sql(' ')
               .visit(addConstraint);

            ctx.end(ALTER_TABLE_ADD);
        }
        else if (alterColumn != null) {
            ctx.start(ALTER_TABLE_ALTER);

            switch (family) {
                /* [pro] xx
                xxxx xxxxxxxxx
                xxxx xxxxxxx
                    xxxxxxxxx xxxxxxxxxxxxxxxxxxxxx
                    xxxxxx

                xxxx xxxxxxxxxx
                    xxxxxxxxx xxxxxxxxxxxxxxxxx xxxxxxxxx
                    xxxxxx
                xx [/pro] */

                case MARIADB:
                case MYSQL: {

                    if (alterColumnDefault == null) {
                        // MySQL's CHANGE COLUMN clause has a mandatory RENAMING syntax...
                        ctx.sql(' ').keyword("change column")
                           .sql(' ').qualify(false).visit(alterColumn).qualify(true);
                    }
                    else {
                        ctx.sql(' ').keyword("alter column");
                    }

                    break;
                }

                default:
                    ctx.sql(' ').keyword("alter");
                    break;
            }

            ctx.sql(' ')
               .qualify(false)
               .visit(alterColumn)
               .qualify(true);

            if (alterColumnType != null) {
                switch (family) {
                    /* [pro] xx
                    xxxx xxxx
                    xx [/pro] */

                    case DERBY:
                        ctx.sql(' ').keyword("set data type");
                        break;

                    case POSTGRES:
                        ctx.sql(' ').keyword("type");
                        break;
                }

                ctx.sql(' ');
                toSQLDDLTypeDeclaration(ctx, alterColumnType);

                if (!alterColumnType.nullable()) {
                    ctx.sql(' ').keyword("not null");
                }
            }
            else if (alterColumnDefault != null) {
                ctx.start(ALTER_TABLE_ALTER_DEFAULT);

                switch (family) {
                    /* [pro] xx
                    xxxx xxxxxxx
                        xxxxxxxxx xxxxxxxxxxxxxxxxxxxxxx
                        xxxxxx
                    xx [/pro] */

                    default:
                        ctx.sql(' ').keyword("set default");
                        break;
                }

                ctx.sql(' ').visit(alterColumnDefault)
                   .end(ALTER_TABLE_ALTER_DEFAULT);
            }

            ctx.end(ALTER_TABLE_ALTER);
        }
        else if (dropColumn != null) {
            ctx.start(ALTER_TABLE_DROP);

            switch (family) {
                /* [pro] xx
                xxxx xxxxxxx
                xxxx xxxxxxxxxx
                    xxxxxxxxx xxxxxxxxxxxxxxxx xxxxxxxxx
                    xxxxxx

                xxxx xxxxx
                    xxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxx
                xx [/pro] */

                default:
                    ctx.sql(' ').keyword("drop");
                    break;
            }

            ctx.sql(' ')
               .qualify(false)
               .visit(dropColumn)
               .qualify(true);

            switch (family) {
                /* [pro] xx
                xxxx xxxxx
                    xxxxxxxxxxxxx
                    xxxxxx
                xx [/pro] */

                default:
                    break;
            }

            if (dropColumnCascade) {
                ctx.sql(' ').keyword("cascade");
            }

            ctx.end(ALTER_TABLE_DROP);
        }
        else if (dropConstraint != null) {
            ctx.start(ALTER_TABLE_DROP);
            ctx.data(DATA_DROP_CONSTRAINT, true);

            ctx.sql(' ')
               .keyword("drop")
               .sql(' ')
               .visit(dropConstraint);

            ctx.data().remove(DATA_DROP_CONSTRAINT);
            ctx.end(ALTER_TABLE_DROP);
        }
    }

    /* [pro] xx
    xxxxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxx x
        xxxxxxxxxx xxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xxxxxx xxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xx xxxxxxx xxxxx xxxxxxxxxxx xxxx xxx xxxxxx xx x xxxxxxxxxxx xxxx xxx xxx xxxxxx
        xx               xxxxxxxxxx xxxxxxxxx xx xxxx xx

        xxxxxxxxxxxxxx
                xxxxxxxx xxxxxxxxxxx xxxxxxxxxxxxxxx
            x xxxxxxxxxx xxxxxxxx xxxxxxxxxxxxxxx
            x xxxx
            x xxxxxxxxx xxxxxxxxxxx x xxxxx
            x xxxxxxx xxxxxxxxxxxxxxxxxxxxxxxx
            x xxxxxxxx xxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxx
            x xxxxxx xxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxx xxxxxxxxxxxxx
            x xxxx
            x xxxxx xxxxxxxxxxx xx xxx xxxxx
            x xxxxxxxxx
            x xxx  xxx xxxxxxxx x xxxxxx xxxxx x x xxx x x xxxx xxxxxxxxxx x x xxxxxxxxxxxx
            x xxx  xxxxxxx xxxxxxxxxxxxx xxxxxxxxx
            x xxxx
            x xxx  xxx xxxxxxxx x xxxxxx xxxxx x x xxx x x xxx xxxxxxxxxx x x xxxxxxxxxxx x x xxxxxxx x x xxx x x xxx x x xxxx
            x xxx  xxxxxxx xxxxxxxxxxxxx xxxxxxxxx
            x xxxxxxx
            x xxxxxxxx
            x xxxxxxxxx
            x xxx  xxx xxxxxxxx x xxxxxx xxxxx x x xxx x x xxx xxxxxxx x x xxx x x xxx x x xxxx
            x xxx  xxxxxxx xxxxxxxxxxxxx xxxxxxxxx
            x xxxxxxx

            x xxxxxxxxxxxxxxxxxxxx
            x xxxxxxxxxxxxxxxxxxxx
            x xxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxx
    x
    xx [/pro] */

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
