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
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.Clause.ALTER_TABLE;
import static org.jooq.Clause.ALTER_TABLE_ADD;
import static org.jooq.Clause.ALTER_TABLE_ALTER;
import static org.jooq.Clause.ALTER_TABLE_ALTER_DEFAULT;
import static org.jooq.Clause.ALTER_TABLE_DROP;
import static org.jooq.Clause.ALTER_TABLE_RENAME;
import static org.jooq.Clause.ALTER_TABLE_RENAME_COLUMN;
import static org.jooq.Clause.ALTER_TABLE_RENAME_CONSTRAINT;
import static org.jooq.Clause.ALTER_TABLE_TABLE;
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.Keywords.K_ADD;
import static org.jooq.impl.Keywords.K_ALTER;
import static org.jooq.impl.Keywords.K_ALTER_COLUMN;
import static org.jooq.impl.Keywords.K_ALTER_CONSTRAINT;
import static org.jooq.impl.Keywords.K_ALTER_TABLE;
import static org.jooq.impl.Keywords.K_CASCADE;
import static org.jooq.impl.Keywords.K_CHANGE_COLUMN;
import static org.jooq.impl.Keywords.K_DEFAULT;
import static org.jooq.impl.Keywords.K_DROP;
import static org.jooq.impl.Keywords.K_DROP_COLUMN;
import static org.jooq.impl.Keywords.K_DROP_CONSTRAINT;
import static org.jooq.impl.Keywords.K_IF_EXISTS;
import static org.jooq.impl.Keywords.K_MODIFY;
import static org.jooq.impl.Keywords.K_NOT_NULL;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.Keywords.K_RENAME_COLUMN;
import static org.jooq.impl.Keywords.K_RENAME_CONSTRAINT;
import static org.jooq.impl.Keywords.K_RENAME_TABLE;
import static org.jooq.impl.Keywords.K_RENAME_TO;
import static org.jooq.impl.Keywords.K_SET_DATA_TYPE;
import static org.jooq.impl.Keywords.K_SET_DEFAULT;
import static org.jooq.impl.Keywords.K_TO;
import static org.jooq.impl.Keywords.K_TYPE;
import static org.jooq.impl.Keywords.K_USING_INDEX;
import static org.jooq.impl.Tools.toSQLDDLTypeDeclaration;
import static org.jooq.impl.Tools.DataKey.DATA_CONSTRAINT_REFERENCE;

import org.jooq.AlterTableAlterStep;
import org.jooq.AlterTableDropStep;
import org.jooq.AlterTableFinalStep;
import org.jooq.AlterTableRenameColumnToStep;
import org.jooq.AlterTableRenameConstraintToStep;
import org.jooq.AlterTableStep;
import org.jooq.AlterTableUsingIndexStep;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.Context;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.SQLDialect;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class AlterTableImpl extends AbstractQuery implements

    // Cascading interface implementations for ALTER TABLE behaviour
    AlterTableStep,
    AlterTableDropStep,
    AlterTableAlterStep,
    AlterTableUsingIndexStep,
    AlterTableRenameColumnToStep,
    AlterTableRenameConstraintToStep {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { ALTER_TABLE };

    private final Table<?>        table;
    private final boolean         ifExists;
    private Table<?>              renameTo;
    private Field<?>              renameColumn;
    private Field<?>              renameColumnTo;
    private Constraint            renameConstraint;
    private Constraint            renameConstraintTo;
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
        this(configuration, table, false);
    }

    AlterTableImpl(Configuration configuration, Table<?> table, boolean ifExists) {
        super(configuration);

        this.table = table;
        this.ifExists = ifExists;
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final AlterTableImpl renameTo(Table<?> newName) {
        this.renameTo = newName;
        return this;
    }

    @Override
    public final AlterTableImpl renameTo(Name newName) {
        return renameTo(DSL.table(newName));
    }

    @Override
    public final AlterTableImpl renameTo(String newName) {
        return renameTo(name(newName));
    }

    @Override
    public final AlterTableImpl renameColumn(Field<?> oldName) {
        renameColumn = oldName;
        return this;
    }

    @Override
    public final AlterTableImpl renameColumn(Name oldName) {
        return renameColumn(field(oldName));
    }

    @Override
    public final AlterTableImpl renameColumn(String oldName) {
        return renameColumn(name(oldName));
    }

    @Override
    public final AlterTableImpl renameConstraint(Constraint oldName) {
        renameConstraint = oldName;
        return this;
    }

    @Override
    public final AlterTableImpl renameConstraint(Name oldName) {
        return renameConstraint(constraint(oldName));
    }

    @Override
    public final AlterTableImpl renameConstraint(String oldName) {
        return renameConstraint(name(oldName));
    }

    @Override
    public final AlterTableImpl to(Field<?> newName) {
        if (renameColumn != null)
            renameColumnTo = newName;
        else
            throw new IllegalStateException();

        return this;
    }

    @Override
    public final AlterTableImpl to(Constraint newName) {
        if (renameConstraint != null)
            renameConstraintTo = newName;
        else
            throw new IllegalStateException();

        return this;
    }

    @Override
    public final AlterTableImpl to(Name newName) {
        if (renameColumn != null)
            return to(field(newName));
        else if (renameConstraint != null)
            return to(constraint(newName));
        else
            throw new IllegalStateException();
    }

    @Override
    public final AlterTableImpl to(String newName) {
        return to(name(newName));
    }

    @Override
    public final <T> AlterTableImpl add(Field<T> field, DataType<T> type) {
        return addColumn(field, type);
    }

    @Override
    public final AlterTableImpl add(Name field, DataType<?> type) {
        return addColumn(field, type);
    }

    @Override
    public final AlterTableImpl add(String field, DataType<?> type) {
        return addColumn(field, type);
    }

    @Override
    public final AlterTableImpl addColumn(String field, DataType<?> type) {
        return addColumn(name(field), type);
    }

    @Override
    public final AlterTableImpl addColumn(Name field, DataType<?> type) {
        return addColumn((Field) field(field, type), type);
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
    public final AlterTableImpl alter(Name field) {
        return alterColumn(field);
    }

    @Override
    public final AlterTableImpl alter(String field) {
        return alterColumn(field);
    }

    @Override
    public final AlterTableImpl alterColumn(Name field) {
        return alterColumn(field(field));
    }

    @Override
    public final AlterTableImpl alterColumn(String field) {
        return alterColumn(name(field));
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
        return defaultValue(Tools.field(literal));
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
    public final AlterTableImpl drop(Name field) {
        return dropColumn(field);
    }

    @Override
    public final AlterTableImpl drop(String field) {
        return dropColumn(field);
    }

    @Override
    public final AlterTableImpl dropColumn(Name field) {
        return dropColumn(field(field));
    }

    @Override
    public final AlterTableImpl dropColumn(String field) {
        return dropColumn(name(field));
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
    public final AlterTableImpl dropConstraint(Name constraint) {
        return drop(DSL.constraint(constraint));
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

    private final boolean supportsIfExists(Context<?> ctx) {
        return !asList(CUBRID, DERBY, FIREBIRD).contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx)) {
            Tools.executeImmediateIfExistsBegin(ctx, DDLStatementType.ALTER_TABLE, table);
            accept0(ctx);
            Tools.executeImmediateIfExistsEnd(ctx, DDLStatementType.ALTER_TABLE, table);
        }
        else {
            accept0(ctx);
        }
    }

    private final void accept0(Context<?> ctx) {
        SQLDialect family = ctx.family();





















        accept1(ctx);
    }

    private final void accept1(Context<?> ctx) {
        SQLDialect family = ctx.family();

        boolean omitAlterTable = family == HSQLDB && renameConstraint != null;
        boolean renameTable = asList().contains(family) && renameTo != null;

        if (!omitAlterTable) {
            ctx.start(ALTER_TABLE_TABLE)
               .visit(renameTable ? K_RENAME_TABLE : K_ALTER_TABLE);

            if (ifExists && supportsIfExists(ctx))
                ctx.sql(' ').visit(K_IF_EXISTS);

            ctx.sql(' ').visit(table)
               .end(ALTER_TABLE_TABLE)
               .formatIndentStart()
               .formatSeparator();
        }

        if (renameTo != null) {
            boolean qualify = ctx.qualify();

            ctx.start(ALTER_TABLE_RENAME)
               .qualify(false)
               .visit(renameTable ? K_TO : K_RENAME_TO).sql(' ')
               .visit(renameTo)
               .qualify(qualify)
               .end(ALTER_TABLE_RENAME);
        }
        else if (renameColumn != null) {
            boolean qualify = ctx.qualify();

            ctx.start(ALTER_TABLE_RENAME_COLUMN)
               .qualify(false);

            switch (ctx.family()) {
                case H2:
                case HSQLDB:
                    ctx.visit(K_ALTER_COLUMN).sql(' ')
                       .visit(renameColumn)
                       .formatSeparator()
                       .visit(K_RENAME_TO).sql(' ')
                       .visit(renameColumnTo);
                    break;

                default:
                    ctx.visit(K_RENAME_COLUMN).sql(' ')
                       .visit(renameColumn)
                       .formatSeparator()
                       .visit(K_TO).sql(' ')
                       .visit(renameColumnTo);
                    break;
            }

            ctx.qualify(qualify)
               .end(ALTER_TABLE_RENAME_COLUMN);
        }
        else if (renameConstraint != null) {
            boolean qualify = ctx.qualify();

            ctx.start(ALTER_TABLE_RENAME_CONSTRAINT);
            ctx.data(DATA_CONSTRAINT_REFERENCE, true);

            if (family == HSQLDB) {
                ctx.qualify(false)
                   .visit(K_ALTER_CONSTRAINT).sql(' ')
                   .visit(renameConstraint)
                   .formatSeparator()
                   .visit(K_RENAME_TO).sql(' ')
                   .visit(renameConstraintTo)
                   .qualify(qualify);
            }
            else {
                ctx.qualify(false)
                   .visit(K_RENAME_CONSTRAINT).sql(' ')
                   .visit(renameConstraint)
                   .formatSeparator()
                   .visit(K_TO).sql(' ')
                   .visit(renameConstraintTo)
                   .qualify(qualify);
            }

            ctx.data().remove(DATA_CONSTRAINT_REFERENCE);
            ctx.end(ALTER_TABLE_RENAME_CONSTRAINT);
        }
        else if (addColumn != null) {
            boolean qualify = ctx.qualify();

            ctx.start(ALTER_TABLE_ADD)
               .visit(K_ADD).sql(' ');






            ctx.qualify(false)
               .visit(addColumn).sql(' ')
               .qualify(qualify);

            toSQLDDLTypeDeclaration(ctx, addColumnType);

            if (!addColumnType.nullable())
                ctx.sql(' ').visit(K_NOT_NULL);

            // Some databases default to NOT NULL, so explicitly setting columns to NULL is mostly required here
            // [#3400] [#4321] ... but not in Derby, Firebird
            else if (!asList(DERBY, FIREBIRD).contains(family))
                ctx.sql(' ').visit(K_NULL);






            ctx.end(ALTER_TABLE_ADD);
        }
        else if (addConstraint != null) {
            boolean qualify = ctx.qualify();

            ctx.start(ALTER_TABLE_ADD);

            ctx.visit(K_ADD)
               .sql(' ')
               .qualify(false)
               .visit(addConstraint)
               .qualify(qualify);









            ctx.end(ALTER_TABLE_ADD);
        }
        else if (alterColumn != null) {
            ctx.start(ALTER_TABLE_ALTER);

            switch (family) {












                case CUBRID:
                case MARIADB:
                case MYSQL: {

                    // MySQL's CHANGE COLUMN clause has a mandatory RENAMING syntax...
                    if (alterColumnDefault == null)
                        ctx.visit(K_CHANGE_COLUMN)
                           .sql(' ').qualify(false).visit(alterColumn).qualify(true);
                    else
                        ctx.visit(K_ALTER_COLUMN);

                    break;
                }

                default:
                    ctx.visit(K_ALTER);
                    break;
            }

            ctx.sql(' ')
               .qualify(false)
               .visit(alterColumn)
               .qualify(true);

            if (alterColumnType != null) {
                switch (family) {





                    case DERBY:
                        ctx.sql(' ').visit(K_SET_DATA_TYPE);
                        break;

                    case POSTGRES:
                        ctx.sql(' ').visit(K_TYPE);
                        break;
                }

                ctx.sql(' ');
                toSQLDDLTypeDeclaration(ctx, alterColumnType);

                if (!alterColumnType.nullable())
                    ctx.sql(' ').visit(K_NOT_NULL);
            }
            else if (alterColumnDefault != null) {
                ctx.start(ALTER_TABLE_ALTER_DEFAULT);

                switch (family) {






                    default:
                        ctx.visit(K_SET_DEFAULT);
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











                default:
                    ctx.visit(K_DROP);
                    break;
            }

            ctx.sql(' ')
               .qualify(false)
               .visit(dropColumn)
               .qualify(true);

            switch (family) {






                default:
                    break;
            }

            if (dropColumnCascade)
                ctx.sql(' ').visit(K_CASCADE);

            ctx.end(ALTER_TABLE_DROP);
        }
        else if (dropConstraint != null) {
            ctx.start(ALTER_TABLE_DROP);
            ctx.data(DATA_CONSTRAINT_REFERENCE, true);

            ctx.visit(K_DROP_CONSTRAINT)
               .sql(' ')
               .visit(dropConstraint);

            ctx.data().remove(DATA_CONSTRAINT_REFERENCE);
            ctx.end(ALTER_TABLE_DROP);
        }

        if (!omitAlterTable)
            ctx.formatIndentEnd();
    }




































































    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
