/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.Clause.ALTER_TABLE;
import static org.jooq.Clause.ALTER_TABLE_ADD;
import static org.jooq.Clause.ALTER_TABLE_ALTER;
import static org.jooq.Clause.ALTER_TABLE_ALTER_DEFAULT;
import static org.jooq.Clause.ALTER_TABLE_DROP;
import static org.jooq.Clause.ALTER_TABLE_TABLE;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.HANA;
import static org.jooq.SQLDialect.SQLSERVER;
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

        /* [pro] */
        // SQL Server doesn't really allow to ALTER DEFAULT values, but we can run a T-SQL script to do that
        if (family == SQLSERVER && alterColumnDefault != null) {
            alterDefaultSQLServer(ctx);
        }
        else
        /* [/pro] */
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

            /* [pro] */
            if (family == HANA)
                ctx.sql('(');
            /* [/pro] */

            ctx.qualify(false)
               .visit(addColumn).sql(' ')
               .qualify(true);

            toSQLDDLTypeDeclaration(ctx, addColumnType);

            if (!addColumnType.nullable()) {
                ctx.sql(' ').keyword("not null");
            }

            // Some databases default to NOT NULL, so explicitly setting columns to NULL is mostly required here
            // [#3400] [#4321] ... but not in Derby, Firebird
            else if (!asList(DERBY, FIREBIRD).contains(family)) {
                ctx.sql(' ').keyword("null");
            }

            /* [pro] */
            if (family == HANA)
                ctx.sql(')');
            /* [/pro] */

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
                /* [pro] */
                case INFORMIX:
                case ORACLE:
                    ctx.sql(' ').keyword("modify");
                    break;

                case SQLSERVER:
                case VERTICA:
                    ctx.sql(' ').keyword("alter column");
                    break;
                /* [/pro] */

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
                    /* [pro] */
                    case DB2:
                    case VERTICA:
                    /* [/pro] */

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
                    /* [pro] */
                    case ORACLE:
                        ctx.sql(' ').keyword("default");
                        break;
                    /* [/pro] */

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
                /* [pro] */
                case ORACLE:
                case SQLSERVER:
                    ctx.sql(' ').keyword("drop column");
                    break;

                case HANA:
                    ctx.sql(' ').keyword("drop").sql('(');
                    break;
                /* [/pro] */

                default:
                    ctx.sql(' ').keyword("drop");
                    break;
            }

            ctx.sql(' ')
               .qualify(false)
               .visit(dropColumn)
               .qualify(true);

            switch (family) {
                /* [pro] */
                case HANA:
                    ctx.sql(')');
                    break;
                /* [/pro] */

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

    /* [pro] */
    private void alterDefaultSQLServer(Context<?> ctx) {
        DSLContext create = DSL.using(ctx.configuration());

        String inlinedTable = create.renderInlined(table);
        String inlinedAlter = create.renderInlined(alterColumn);
        String inlinedAlterDefault = create.renderInlined(alterColumnDefault);

        // [#2626] TODO: Externalise this SQL string in a .properties file and use jOOQ's
        //               templating mechanism to load it

        ctx.visit(sql(
                "DECLARE @constraint NVARCHAR(max);"
            + "\nDECLARE @command NVARCHAR(max);"
            + "\n"
            + "\nSELECT @constraint = name"
            + "\nFROM sys.default_constraints"
            + "\nWHERE parent_object_id = object_id({0})"
            + "\nAND parent_column_id = columnproperty(object_id({0}), {1}, 'ColumnId');"
            + "\n"
            + "\nIF @constraint IS NOT NULL"
            + "\nBEGIN"
            + "\n  SET @command = 'ALTER TABLE ' + {0} + ' DROP CONSTRAINT ' + @constraint"
            + "\n  EXECUTE sp_executeSQL @command"
            + "\n"
            + "\n  SET @command = 'ALTER TABLE ' + {0} + ' ADD CONSTRAINT ' + @constraint + ' DEFAULT ' + {2} + ' FOR ' + {1}"
            + "\n  EXECUTE sp_executeSQL @command"
            + "\nEND"
            + "\nELSE"
            + "\nBEGIN"
            + "\n  SET @command = 'ALTER TABLE ' + {0} + ' ADD DEFAULT ' + {2} + ' FOR ' + {1}"
            + "\n  EXECUTE sp_executeSQL @command"
            + "\nEND"

            , inline(inlinedTable)
            , inline(inlinedAlter)
            , inline(inlinedAlterDefault)
        ));
    }
    /* [/pro] */

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
