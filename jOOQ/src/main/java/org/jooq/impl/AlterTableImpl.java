/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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

import static org.jooq.Clause.ALTER_TABLE;
import static org.jooq.Clause.ALTER_TABLE_ADD;
import static org.jooq.Clause.ALTER_TABLE_ALTER;
import static org.jooq.Clause.ALTER_TABLE_ALTER_DEFAULT;
import static org.jooq.Clause.ALTER_TABLE_DROP;
import static org.jooq.Clause.ALTER_TABLE_TABLE;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.queryPart;

import org.jooq.AlterTableAlterStep;
import org.jooq.AlterTableDropStep;
import org.jooq.AlterTableFinalStep;
import org.jooq.AlterTableStep;
import org.jooq.Clause;
import org.jooq.Configuration;
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
    private Field<?>              add;
    private DataType<?>           addType;
    private Field<?>              alter;
    private DataType<?>           alterType;
    private Field<?>              alterDefault;
    private Field<?>              drop;
    private boolean               dropCascade;

    AlterTableImpl(Configuration configuration, Table<?> table) {
        super(configuration);

        this.table = table;
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final AlterTableImpl add(String field, DataType<?> type) {
        return add((Field) field(field, type), type);
    }

    @Override
    public final <T> AlterTableImpl add(Field<T> field, DataType<T> type) {
        add = field;
        addType = type;
        return this;
    }

    @Override
    public final AlterTableImpl alter(String field) {
        return alter(field(field));
    }

    @Override
    public final <T> AlterTableImpl alter(Field<T> field) {
        alter = field;
        return this;
    }

    @Override
    public final AlterTableImpl set(DataType type) {
        alterType = type;
        return this;
    }

    @Override
    public final AlterTableImpl defaultValue(Object literal) {
        return defaultValue(inline(literal));
    }

    @Override
    public final AlterTableImpl defaultValue(Field expression) {
        alterDefault = expression;
        return this;
    }

    @Override
    public final AlterTableImpl drop(String field) {
        return drop(field(field));
    }

    @Override
    public final AlterTableImpl drop(Field<?> field) {
        drop = field;
        return this;
    }

    @Override
    public final AlterTableFinalStep cascade() {
        dropCascade = true;
        return this;
    }

    @Override
    public final AlterTableFinalStep restrict() {
        dropCascade = false;
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
        if (family == SQLSERVER && alterDefault != null) {
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
           .keyword("alter table").sql(" ").visit(table)
           .end(ALTER_TABLE_TABLE);

        if (add != null) {
            ctx.start(ALTER_TABLE_ADD)
               .sql(" ").keyword("add").sql(" ")
               .qualify(false)
               .visit(add).sql(" ")
               .qualify(true)
                // [#3341] This is currently incorrect for MySQL and MariaDB
               .keyword(addType.getCastTypeName(ctx.configuration()));

            if (!addType.nullable()) {
                ctx.sql(" ").keyword("not null");
            }

            // Some databases default to NOT NULL, so explicitly setting columns to NULL is mostly required here
            // [#3400] ... but not in Firebird
            else if (family != FIREBIRD) {
                ctx.sql(" ").keyword("null");
            }

            ctx.end(ALTER_TABLE_ADD);
        }
        else if (alter != null) {
            ctx.start(ALTER_TABLE_ALTER);

            switch (family) {
                /* [pro] */
                case ORACLE:
                    ctx.sql(" ").keyword("modify");
                    break;

                case SQLSERVER:
                    ctx.sql(" ").keyword("alter column");
                    break;
                /* [/pro] */

                case MARIADB:
                case MYSQL:
                    // MySQL's CHANGE COLUMN clause has a mandatory RENAMING syntax...
                    ctx.sql(" ").keyword("change column")
                       .sql(" ").qualify(false).visit(alter).qualify(true);
                    break;

                default:
                    ctx.sql(" ").keyword("alter");
                    break;
            }

            ctx.sql(" ")
               .qualify(false)
               .visit(alter)
               .qualify(true);

            if (alterType != null) {
                switch (family) {
                    /* [pro] */
                    case DB2:
                        ctx.sql(" ").keyword("set data type");
                        break;
                    /* [/pro] */

                    case POSTGRES:
                        ctx.sql(" ").keyword("type");
                        break;
                }

                // [#3341] This is currently incorrect for MySQL and MariaDB
                ctx.sql(" ").keyword(alterType.getCastTypeName(ctx.configuration()));

                if (!alterType.nullable()) {
                    ctx.sql(" ").keyword("not null");
                }
            }
            else if (alterDefault != null) {
                ctx.start(ALTER_TABLE_ALTER_DEFAULT);

                switch (family) {
                    /* [pro] */
                    case ORACLE:
                        ctx.sql(" ").keyword("default");
                        break;
                    /* [/pro] */

                    default:
                        ctx.sql(" ").keyword("set default");
                        break;
                }

                ctx.sql(" ").visit(alterDefault)
                   .end(ALTER_TABLE_ALTER_DEFAULT);
            }

            ctx.end(ALTER_TABLE_ALTER);
        }
        else if (drop != null) {
            ctx.start(ALTER_TABLE_DROP);

            switch (family) {
                /* [pro] */
                case ORACLE:
                case SQLSERVER:
                    ctx.sql(" ").keyword("drop column");
                    break;
                /* [/pro] */

                default:
                    ctx.sql(" ").keyword("drop");
                    break;
            }

            ctx.sql(" ")
               .qualify(false)
               .visit(drop)
               .qualify(true);

            if (dropCascade) {
                ctx.sql(" ").keyword("cascade");
            }

            ctx.end(ALTER_TABLE_DROP);
        }
    }

    /* [pro] */
    private void alterDefaultSQLServer(Context<?> ctx) {
        DSLContext create = DSL.using(ctx.configuration());

        String inlinedTable = create.renderInlined(table);
        String inlinedAlter = create.renderInlined(alter);
        String inlinedAlterDefault = create.renderInlined(alterDefault);

        ctx.visit(queryPart(
                "DECLARE @constraint NVARCHAR(max);"
            + "\nDECLARE @command NVARCHAR(max);"
            + "\n"
            + "\nSELECT @constraint = name"
            + "\nFROM sys.default_constraints"
            + "\nWHERE parent_object_id = object_id('" + inlinedTable + "')"
            + "\nAND parent_column_id = columnproperty(object_id('" + inlinedTable + "'), '" + inlinedAlter + "', 'ColumnId');"
            + "\n"
            + "\nIF @constraint IS NOT NULL"
            + "\nBEGIN"
            + "\n  SET @command = 'ALTER TABLE " + inlinedTable + " DROP CONSTRAINT ' + @constraint"
            + "\n  EXECUTE sp_executeSQL @command"
            + "\n"
            + "\n  SET @command = 'ALTER TABLE " + inlinedTable + " ADD CONSTRAINT ' + @constraint + ' DEFAULT '" + inlinedAlterDefault + "' FOR " + inlinedAlter + "'"
            + "\n  EXECUTE sp_executeSQL @command"
            + "\nEND"
            + "\nELSE"
            + "\nBEGIN"
            + "\n  SET @command = 'ALTER TABLE " + inlinedTable + " ADD DEFAULT '" + inlinedAlterDefault + "' FOR " + inlinedAlter + "'"
            + "\n  EXECUTE sp_executeSQL @command"
            + "\nEND"
        ));
    }
    /* [/pro] */

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
