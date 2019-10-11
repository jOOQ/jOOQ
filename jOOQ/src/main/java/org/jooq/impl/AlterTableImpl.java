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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.jooq.Clause.ALTER_TABLE;
import static org.jooq.Clause.ALTER_TABLE_ADD;
import static org.jooq.Clause.ALTER_TABLE_ALTER;
import static org.jooq.Clause.ALTER_TABLE_ALTER_DEFAULT;
import static org.jooq.Clause.ALTER_TABLE_ALTER_NULL;
import static org.jooq.Clause.ALTER_TABLE_DROP;
import static org.jooq.Clause.ALTER_TABLE_RENAME;
import static org.jooq.Clause.ALTER_TABLE_RENAME_COLUMN;
import static org.jooq.Clause.ALTER_TABLE_RENAME_CONSTRAINT;
import static org.jooq.Clause.ALTER_TABLE_RENAME_INDEX;
import static org.jooq.Clause.ALTER_TABLE_TABLE;
import static org.jooq.Nullability.NOT_NULL;
import static org.jooq.Nullability.NULL;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
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
// ...
import static org.jooq.impl.ConstraintType.FOREIGN_KEY;
import static org.jooq.impl.ConstraintType.PRIMARY_KEY;
import static org.jooq.impl.DSL.begin;
import static org.jooq.impl.DSL.commentOnTable;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.constraint;
// ...
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.index;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sql;
// ...
import static org.jooq.impl.Keywords.K_ADD;
import static org.jooq.impl.Keywords.K_AFTER;
import static org.jooq.impl.Keywords.K_ALTER;
import static org.jooq.impl.Keywords.K_ALTER_COLUMN;
import static org.jooq.impl.Keywords.K_ALTER_CONSTRAINT;
import static org.jooq.impl.Keywords.K_ALTER_TABLE;
import static org.jooq.impl.Keywords.K_BEFORE;
import static org.jooq.impl.Keywords.K_CASCADE;
import static org.jooq.impl.Keywords.K_CHANGE;
import static org.jooq.impl.Keywords.K_CHANGE_COLUMN;
import static org.jooq.impl.Keywords.K_COMMENT;
import static org.jooq.impl.Keywords.K_CONSTRAINT;
import static org.jooq.impl.Keywords.K_DEFAULT;
import static org.jooq.impl.Keywords.K_DROP;
import static org.jooq.impl.Keywords.K_DROP_COLUMN;
import static org.jooq.impl.Keywords.K_DROP_CONSTRAINT;
import static org.jooq.impl.Keywords.K_DROP_NOT_NULL;
import static org.jooq.impl.Keywords.K_ELSE;
import static org.jooq.impl.Keywords.K_END_IF;
import static org.jooq.impl.Keywords.K_EXCEPTION;
import static org.jooq.impl.Keywords.K_EXEC;
import static org.jooq.impl.Keywords.K_FIRST;
import static org.jooq.impl.Keywords.K_FOREIGN_KEY;
import static org.jooq.impl.Keywords.K_IF;
import static org.jooq.impl.Keywords.K_IF_EXISTS;
import static org.jooq.impl.Keywords.K_IF_NOT_EXISTS;
import static org.jooq.impl.Keywords.K_LIKE;
import static org.jooq.impl.Keywords.K_MODIFY;
import static org.jooq.impl.Keywords.K_NOT_NULL;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.Keywords.K_PRIMARY_KEY;
import static org.jooq.impl.Keywords.K_RAISE;
import static org.jooq.impl.Keywords.K_RENAME;
import static org.jooq.impl.Keywords.K_RENAME_COLUMN;
import static org.jooq.impl.Keywords.K_RENAME_CONSTRAINT;
import static org.jooq.impl.Keywords.K_RENAME_INDEX;
import static org.jooq.impl.Keywords.K_RENAME_OBJECT;
import static org.jooq.impl.Keywords.K_RENAME_TABLE;
import static org.jooq.impl.Keywords.K_RENAME_TO;
import static org.jooq.impl.Keywords.K_REPLACE;
import static org.jooq.impl.Keywords.K_SET_DATA_TYPE;
import static org.jooq.impl.Keywords.K_SET_DEFAULT;
import static org.jooq.impl.Keywords.K_SET_NOT_NULL;
import static org.jooq.impl.Keywords.K_THEN;
import static org.jooq.impl.Keywords.K_TO;
import static org.jooq.impl.Keywords.K_TYPE;
import static org.jooq.impl.Keywords.K_USING_INDEX;
import static org.jooq.impl.Keywords.K_WHEN;
import static org.jooq.impl.Keywords.K_WITH_NO_DATACOPY;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.begin;
import static org.jooq.impl.Tools.beginExecuteImmediate;
import static org.jooq.impl.Tools.beginTryCatch;
import static org.jooq.impl.Tools.end;
import static org.jooq.impl.Tools.endExecuteImmediate;
import static org.jooq.impl.Tools.endTryCatch;
import static org.jooq.impl.Tools.fieldsByName;
import static org.jooq.impl.Tools.toSQLDDLTypeDeclaration;
import static org.jooq.impl.Tools.toSQLDDLTypeDeclarationForAddition;
import static org.jooq.impl.Tools.toSQLDDLTypeDeclarationIdentityAfterNull;
import static org.jooq.impl.Tools.toSQLDDLTypeDeclarationIdentityBeforeNull;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_CONSTRAINT_REFERENCE;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jooq.AlterTableAddStep;
import org.jooq.AlterTableAlterStep;
import org.jooq.AlterTableDropStep;
import org.jooq.AlterTableFinalStep;
import org.jooq.AlterTableRenameColumnToStep;
import org.jooq.AlterTableRenameConstraintToStep;
import org.jooq.AlterTableRenameIndexToStep;
import org.jooq.AlterTableStep;
import org.jooq.AlterTableUsingIndexStep;
import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.Context;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.FieldOrConstraint;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Nullability;
// ...
import org.jooq.Query;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
// ...

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class AlterTableImpl extends AbstractRowCountQuery implements

    // Cascading interface implementations for ALTER TABLE behaviour
    AlterTableStep,
    AlterTableAddStep,
    AlterTableDropStep,
    AlterTableAlterStep,
    AlterTableUsingIndexStep,
    AlterTableRenameColumnToStep,
    AlterTableRenameIndexToStep,
    AlterTableRenameConstraintToStep {

    /**
     * Generated UID
     */
    private static final long                serialVersionUID                      = 8904572826501186329L;
    private static final Clause[]            CLAUSES                               = { ALTER_TABLE };
    private static final Set<SQLDialect>     NO_SUPPORT_IF_EXISTS                  = SQLDialect.supported(CUBRID, DERBY, FIREBIRD, MARIADB);
    private static final Set<SQLDialect>     NO_SUPPORT_IF_EXISTS_COLUMN           = SQLDialect.supported(CUBRID, DERBY, FIREBIRD);
    private static final Set<SQLDialect>     SUPPORT_RENAME_COLUMN                 = SQLDialect.supported(DERBY);
    private static final Set<SQLDialect>     SUPPORT_RENAME_TABLE                  = SQLDialect.supported(DERBY);
    private static final Set<SQLDialect>     NO_SUPPORT_RENAME_QUALIFIED_TABLE     = SQLDialect.supported(POSTGRES);
    private static final Set<SQLDialect>     NO_SUPPORT_ALTER_TYPE_AND_NULL        = SQLDialect.supported(POSTGRES);
    private static final Set<SQLDialect>     NO_SUPPORT_DROP_CONSTRAINT            = SQLDialect.supported(MARIADB, MYSQL);
    private static final Set<SQLDialect>     REQUIRE_REPEAT_ADD_ON_MULTI_ALTER     = SQLDialect.supported(FIREBIRD, MARIADB, MYSQL, POSTGRES);
    private static final Set<SQLDialect>     REQUIRE_REPEAT_DROP_ON_MULTI_ALTER    = SQLDialect.supported(FIREBIRD, MARIADB, MYSQL, POSTGRES);













    private final Table<?>                   table;
    private final boolean                    ifExists;
    private boolean                          ifExistsColumn;
    private boolean                          ifNotExistsColumn;
    private Comment                          comment;
    private Table<?>                         renameTo;
    private Field<?>                         renameColumn;
    private Field<?>                         renameColumnTo;
    private Index                            renameIndex;
    private Index                            renameIndexTo;
    private Constraint                       renameConstraint;
    private Constraint                       renameConstraintTo;
    private QueryPartList<FieldOrConstraint> add;
    private Field<?>                         addColumn;
    private DataType<?>                      addColumnType;
    private Constraint                       addConstraint;
    private boolean                          addFirst;
    private Field<?>                         addBefore;
    private Field<?>                         addAfter;




    private Field<?>                         alterColumn;
    private Nullability                      alterColumnNullability;
    private DataType<?>                      alterColumnType;
    private Field<?>                         alterColumnDefault;
    private QueryPartList<Field<?>>          dropColumns;
    private boolean                          dropColumnCascade;
    private Constraint                       dropConstraint;
    private ConstraintType                   dropConstraintType;

    AlterTableImpl(Configuration configuration, Table<?> table) {
        this(configuration, table, false);
    }

    AlterTableImpl(Configuration configuration, Table<?> table, boolean ifExists) {
        super(configuration);

        this.table = table;
        this.ifExists = ifExists;
    }

    final Table<?>       $table()                  { return table; }
    final boolean        $ifExists()               { return ifExists; }
    final boolean        $ifExistsColumn()         { return ifExistsColumn; }
    final boolean        $ifNotExistsColumn()      { return ifNotExistsColumn; }
    final Field<?>       $addColumn()              { return addColumn; }
    final DataType<?>    $addColumnType()          { return addColumnType; }
    final boolean        $addFirst()               { return addFirst; }
    final Field<?>       $addBefore()              { return addBefore; }
    final Field<?>       $addAfter()               { return addAfter; }
    final Field<?>       $alterColumn()            { return alterColumn; }
    final Nullability    $alterColumnNullability() { return alterColumnNullability; }
    final DataType<?>    $alterColumnType()        { return alterColumnType; }
    final Field<?>       $alterColumnDefault()     { return alterColumnDefault; }
    final Table<?>       $renameTo()               { return renameTo; }
    final Field<?>       $renameColumn()           { return renameColumn; }
    final Field<?>       $renameColumnTo()         { return renameColumnTo; }
    final List<Field<?>> $dropColumns()            { return dropColumns; };
    final Constraint     $dropConstraint()         { return dropConstraint; }
    final ConstraintType $dropConstraintType()     { return dropConstraintType; }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final AlterTableImpl comment(String c) {
        return comment(DSL.comment(c));
    }

    @Override
    public final AlterTableImpl comment(Comment c) {
        this.comment = c;
        return this;
    }

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
    public final AlterTableImpl renameIndex(String oldName) {
        return renameIndex(name(oldName));
    }

    @Override
    public final AlterTableImpl renameIndex(Name oldName) {
        return renameIndex(index(oldName));
    }

    @Override
    public final AlterTableImpl renameIndex(Index oldName) {
        renameIndex = oldName;
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
    public final AlterTableImpl to(String newName) {
        return to(name(newName));
    }

    @Override
    public final AlterTableImpl to(Name newName) {
        if (renameColumn != null)
            return to(field(newName));
        else if (renameConstraint != null)
            return to(constraint(newName));
        else if (renameIndex != null) {
            return to(index(newName));
        }
        else
            throw new IllegalStateException();
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
    public final AlterTableImpl to(Index newName) {
        if (renameIndex != null)
            renameIndexTo = newName;
        else
            throw new IllegalStateException();

        return this;
    }

    @Override
    public final AlterTableImpl add(Field<?> field) {
        return addColumn(field);
    }

    @Override
    public final AlterTableImpl add(FieldOrConstraint... fields) {
        return add(Arrays.asList(fields));
    }

    @Override
    public final AlterTableImpl add(Collection<? extends FieldOrConstraint> fields) {
        add = new QueryPartList<>(fields);
        return this;
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
    public final AlterTableImpl addIfNotExists(Field<?> field) {
        return addColumnIfNotExists(field);
    }

    @Override
    public final <T> AlterTableImpl addIfNotExists(Field<T> field, DataType<T> type) {
        return addColumnIfNotExists(field, type);
    }

    @Override
    public final AlterTableImpl addIfNotExists(Name field, DataType<?> type) {
        return addColumnIfNotExists(field, type);
    }

    @Override
    public final AlterTableImpl addIfNotExists(String field, DataType<?> type) {
        return addColumnIfNotExists(field, type);
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
    public final AlterTableImpl addColumn(Field<?> field) {
        return addColumn(field, (DataType) field.getDataType());
    }

    @Override
    public final <T> AlterTableImpl addColumn(Field<T> field, DataType<T> type) {
        addColumn = field;
        addColumnType = type;
        return this;
    }

    @Override
    public final AlterTableImpl addColumnIfNotExists(String field, DataType<?> type) {
        return addColumnIfNotExists(name(field), type);
    }

    @Override
    public final AlterTableImpl addColumnIfNotExists(Name field, DataType<?> type) {
        return addColumnIfNotExists((Field) field(field, type), type);
    }

    @Override
    public final AlterTableImpl addColumnIfNotExists(Field<?> field) {
        return addColumnIfNotExists(field, (DataType) field.getDataType());
    }

    @Override
    public final <T> AlterTableImpl addColumnIfNotExists(Field<T> field, DataType<T> type) {
        ifNotExistsColumn = true;
        return addColumn(field, type);
    }

    @Override
    public final AlterTableImpl add(Constraint constraint) {
        addConstraint = constraint;
        return this;
    }

    @Override
    public final AlterTableImpl first() {
        addFirst = true;
        return this;
    }

    @Override
    public final AlterTableImpl before(String columnName) {
        return before(name(columnName));
    }

    @Override
    public final AlterTableImpl before(Name columnName) {
        return before(field(columnName));
    }

    @Override
    public final AlterTableImpl before(Field<?> columnName) {
        addBefore = columnName;
        return this;
    }

    @Override
    public final AlterTableImpl after(String columnName) {
        return after(name(columnName));
    }

    @Override
    public final AlterTableImpl after(Name columnName) {
        return after(field(columnName));
    }

    @Override
    public final AlterTableImpl after(Field<?> columnName) {
        addAfter = columnName;
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
    public final AlterTableImpl setNotNull() {
        alterColumnNullability = NOT_NULL;
        return this;
    }

    @Override
    public final AlterTableImpl dropNotNull() {
        alterColumnNullability = NULL;
        return this;
    }

    @Override
    public final AlterTableImpl defaultValue(Object literal) {
        return default_(literal);
    }

    @Override
    public final AlterTableImpl defaultValue(Field expression) {
        return default_(expression);
    }

    @Override
    public final AlterTableImpl default_(Object literal) {
        return default_(Tools.field(literal));
    }

    @Override
    public final AlterTableImpl default_(Field expression) {
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
    public final AlterTableImpl dropIfExists(Field<?> field) {
        return dropColumnIfExists(field);
    }

    @Override
    public final AlterTableImpl dropIfExists(Name field) {
        return dropColumnIfExists(field);
    }

    @Override
    public final AlterTableImpl dropIfExists(String field) {
        return dropColumnIfExists(field);
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
        return dropColumns0(Collections.singletonList(field));
    }

    @Override
    public final AlterTableImpl dropColumnIfExists(Name field) {
        return dropColumnIfExists(field(field));
    }

    @Override
    public final AlterTableImpl dropColumnIfExists(String field) {
        return dropColumnIfExists(name(field));
    }

    @Override
    public final AlterTableImpl dropColumnIfExists(Field<?> field) {
        ifExistsColumn = true;
        return dropColumn(field);
    }

    @Override
    public final AlterTableImpl drop(Field<?>... fields) {
        return dropColumns(fields);
    }

    @Override
    public final AlterTableImpl drop(Name... fields) {
        return dropColumns(fields);
    }

    @Override
    public final AlterTableImpl drop(String... fields) {
        return dropColumns(fields);
    }

    @Override
    public final AlterTableImpl dropColumns(Field<?>... fields) {
        return dropColumns(Arrays.asList(fields));
    }

    @Override
    public final AlterTableImpl dropColumns(Name... fields) {
        return dropColumns(fieldsByName(fields));
    }

    @Override
    public final AlterTableImpl dropColumns(String... fields) {
        return dropColumns(fieldsByName(fields));
    }

    @Override
    public final AlterTableImpl drop(Collection<? extends Field<?>> fields) {
        return dropColumns(fields);
    }

    @Override
    public final AlterTableImpl dropColumns(Collection<? extends Field<?>> fields) {
        return dropColumns0(fields);
    }

    private final AlterTableImpl dropColumns0(Collection<? extends Field<?>> fields) {
        dropColumns = new QueryPartList<>(fields);
        return this;
    }

    @Override
    public final AlterTableImpl drop(Constraint constraint) {
        dropConstraint = constraint;
        dropConstraintType = null;
        return this;
    }

    @Override
    public final AlterTableImpl dropConstraint(Constraint constraint) {
        return drop(constraint);
    }

    @Override
    public final AlterTableImpl dropConstraint(Name constraint) {
        return dropConstraint(DSL.constraint(constraint));
    }

    @Override
    public final AlterTableImpl dropConstraint(String constraint) {
        return dropConstraint(DSL.constraint(constraint));
    }

    @Override
    public final AlterTableImpl dropPrimaryKey() {
        dropConstraintType = PRIMARY_KEY;
        return this;
    }

    @Override
    public final AlterTableImpl dropPrimaryKey(Constraint constraint) {
        dropConstraint = constraint;
        dropConstraintType = PRIMARY_KEY;
        return this;
    }

    @Override
    public final AlterTableImpl dropPrimaryKey(Name constraint) {
        return dropPrimaryKey(constraint(constraint));
    }

    @Override
    public final AlterTableImpl dropPrimaryKey(String constraint) {
        return dropPrimaryKey(constraint(constraint));
    }

    @Override
    public final AlterTableImpl dropForeignKey(Constraint constraint) {
        dropConstraint = constraint;
        dropConstraintType = FOREIGN_KEY;
        return this;
    }

    @Override
    public final AlterTableImpl dropForeignKey(Name constraint) {
        return dropForeignKey(DSL.constraint(constraint));
    }

    @Override
    public final AlterTableImpl dropForeignKey(String constraint) {
        return dropForeignKey(DSL.constraint(constraint));
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
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.family());
    }

    private final boolean supportsIfExistsColumn(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS_COLUMN.contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if ((ifExists && !supportsIfExists(ctx)) || ((ifExistsColumn || ifNotExistsColumn) && !supportsIfExistsColumn(ctx))) {
            beginTryCatch(ctx, DDLStatementType.ALTER_TABLE, ifExists ? TRUE : null, ifExistsColumn ? TRUE : ifNotExistsColumn ? FALSE : null);
            accept0(ctx);
            endTryCatch(ctx, DDLStatementType.ALTER_TABLE, ifExists ? TRUE : null, ifExistsColumn ? TRUE : ifNotExistsColumn ? FALSE : null);
        }
        else {
            accept0(ctx);
        }
    }

    private final void accept0(Context<?> ctx) {
        SQLDialect family = ctx.family();

        if (comment != null) {
            switch (family) {




                case MARIADB:
                case MYSQL:
                    break;

                default:
                    ctx.visit(commentOnTable(table).is(comment));
                    return;
            }
        }























        if (renameIndexTo != null) {
            switch (family) {

                // [#5724] These databases use table-scoped index names






                case MYSQL:
                    break;

                // [#5724] Most databases use schema-scoped index names: Ignore the table.
                default:
                    ctx.visit(DSL.alterIndex(renameIndex).renameTo(renameIndexTo));
                    return;
            }
        }

        // [#3805] Compound statements to alter data type and change nullability in a single statement if needed.
        if (alterColumnType != null && alterColumnType.nullability() != Nullability.DEFAULT) {
            switch (family) {






                case POSTGRES:
                    alterColumnTypeAndNullabilityInBlock(ctx);
                    return;
            }
        }
































        accept1(ctx);
    }

    private final void accept1(Context<?> ctx) {
        SQLDialect family = ctx.family();

        boolean omitAlterTable =
               (renameConstraint != null && family == HSQLDB)
            || (renameColumn != null && SUPPORT_RENAME_COLUMN.contains(family));
        boolean renameTable = renameTo != null && SUPPORT_RENAME_TABLE.contains(family);
        boolean renameObject = renameTo != null && (false);

        if (!omitAlterTable) {
            ctx.start(ALTER_TABLE_TABLE)
               .visit(renameObject
                   ? K_RENAME_OBJECT
                   : renameTable
                   ? K_RENAME_TABLE
                   : K_ALTER_TABLE);

            if (ifExists && supportsIfExists(ctx))
                ctx.sql(' ').visit(K_IF_EXISTS);

            ctx.sql(' ').visit(table)
               .end(ALTER_TABLE_TABLE)
               .formatIndentStart()
               .formatSeparator();
        }

        if (comment != null) {
            ctx.visit(K_COMMENT).sql(' ').visit(comment);
        }
        else if (renameTo != null) {
            boolean qualify = ctx.qualify();

            ctx.start(ALTER_TABLE_RENAME);

            if (NO_SUPPORT_RENAME_QUALIFIED_TABLE.contains(ctx.family()))
                ctx.qualify(false);

            ctx.visit(renameObject || renameTable ? K_TO : K_RENAME_TO).sql(' ')
               .visit(renameTo);

            if (NO_SUPPORT_RENAME_QUALIFIED_TABLE.contains(ctx.family()))
                ctx.qualify(qualify);

            ctx.end(ALTER_TABLE_RENAME);
        }
        else if (renameColumn != null) {
            boolean qualify = ctx.qualify();
            ctx.start(ALTER_TABLE_RENAME_COLUMN);

            switch (ctx.family()) {



                case DERBY:
                    ctx.visit(K_RENAME_COLUMN).sql(' ')
                       .visit(renameColumn)
                       .formatSeparator()
                       .visit(K_TO).sql(' ')
                       .qualify(false)
                       .visit(renameColumnTo)
                       .qualify(qualify);
                    break;

                case H2:
                case HSQLDB:
                    ctx.qualify(false)
                       .visit(K_ALTER_COLUMN).sql(' ')
                       .visit(renameColumn)
                       .formatSeparator()
                       .visit(K_RENAME_TO).sql(' ')
                       .visit(renameColumnTo)
                       .qualify(qualify);
                    break;

                case FIREBIRD:
                    ctx.qualify(false)
                       .visit(K_ALTER_COLUMN).sql(' ')
                       .visit(renameColumn)
                       .formatSeparator()
                       .visit(K_TO).sql(' ')
                       .visit(renameColumnTo)
                       .qualify(qualify);
                    break;























                default:
                    ctx.qualify(false)
                       .visit(K_RENAME_COLUMN).sql(' ')
                       .visit(renameColumn)
                       .formatSeparator()
                       .visit(K_TO).sql(' ')
                       .visit(renameColumnTo)
                       .qualify(qualify);
                    break;
            }

            ctx.end(ALTER_TABLE_RENAME_COLUMN);
        }
        else if (renameIndex != null) {
            boolean qualify = ctx.qualify();

            ctx.start(ALTER_TABLE_RENAME_INDEX)
               .qualify(false)
               .visit(K_RENAME_INDEX).sql(' ')
               .visit(renameIndex)
               .formatSeparator()
               .visit(K_TO).sql(' ')
               .visit(renameIndexTo)
               .qualify(qualify)
               .end(ALTER_TABLE_RENAME_INDEX);
        }
        else if (renameConstraint != null) {
            boolean qualify = ctx.qualify();

            ctx.start(ALTER_TABLE_RENAME_CONSTRAINT);
            ctx.data(DATA_CONSTRAINT_REFERENCE, true);

            if (family == HSQLDB)
                ctx.qualify(false)
                   .visit(K_ALTER_CONSTRAINT).sql(' ')
                   .visit(renameConstraint)
                   .formatSeparator()
                   .visit(K_RENAME_TO).sql(' ')
                   .visit(renameConstraintTo)
                   .qualify(qualify);
            else
                ctx.qualify(false)
                   .visit( K_RENAME_CONSTRAINT).sql(' ')
                   .visit(renameConstraint)
                   .formatSeparator()
                   .visit(K_TO).sql(' ')
                   .visit(renameConstraintTo)
                   .qualify(qualify);

            ctx.data().remove(DATA_CONSTRAINT_REFERENCE);
            ctx.end(ALTER_TABLE_RENAME_CONSTRAINT);
        }
        else if (add != null) {
            boolean multiAdd = REQUIRE_REPEAT_ADD_ON_MULTI_ALTER.contains(ctx.family());
            boolean parens = !multiAdd;
            boolean comma = true;

            ctx.start(ALTER_TABLE_ADD)
               .visit(K_ADD)
               .sql(' ');

            if (parens)
                ctx.sql('(');

            boolean indent = !multiAdd && add.size() > 1;

            if (indent)
                ctx.formatIndentStart()
                   .formatNewLine();

            for (int i = 0; i < add.size(); i++) {
                if (i > 0)
                    if (multiAdd)
                        ctx.sql(comma ? "," : "").formatSeparator().visit(K_ADD).sql(' ');
                    else
                        ctx.sql(comma ? "," : "").formatSeparator();

                FieldOrConstraint part = add.get(i);
                ctx.visit(part);

                if (part instanceof Field) {
                    ctx.sql(' ');
                    toSQLDDLTypeDeclarationForAddition(ctx, ((Field<?>) part).getDataType());
                }
            }

            if (indent)
                ctx.formatIndentEnd()
                   .formatNewLine();

            if (parens)
                ctx.sql(')');

            acceptFirstBeforeAfter(ctx);
            ctx.end(ALTER_TABLE_ADD);
        }
        else if (addColumn != null) {
            boolean qualify = ctx.qualify();

            ctx.start(ALTER_TABLE_ADD)
               .visit(K_ADD).sql(' ');

            if (ifNotExistsColumn) {
                switch (ctx.family()) {











                    case H2:
                    case MARIADB:
                    case POSTGRES:
                    default:
                        ctx.visit(K_IF_NOT_EXISTS).sql(' ');
                        break;
                }
            }






            ctx.qualify(false)
               .visit(addColumn).sql(' ')
               .qualify(qualify);

            toSQLDDLTypeDeclarationForAddition(ctx, addColumnType);






            acceptFirstBeforeAfter(ctx);
            ctx.end(ALTER_TABLE_ADD);
        }
        else if (addConstraint != null) {
            ctx.start(ALTER_TABLE_ADD);

            ctx.visit(K_ADD)
               .sql(' ');







            ctx.visit(addConstraint);









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




                    case FIREBIRD:
                    case POSTGRES:
                        ctx.sql(' ').visit(K_TYPE);
                        break;
                }

                ctx.sql(' ');
                toSQLDDLTypeDeclaration(ctx, alterColumnType);
                toSQLDDLTypeDeclarationIdentityBeforeNull(ctx, alterColumnType);

                // [#3805] Some databases cannot change the type and the NOT NULL constraint in a single statement
                if (!NO_SUPPORT_ALTER_TYPE_AND_NULL.contains(family)) {
                    switch (alterColumnType.nullability()) {
                        case NULL:
                            ctx.sql(' ').visit(K_NULL);
                            break;
                        case NOT_NULL:
                            ctx.sql(' ').visit(K_NOT_NULL);
                            break;
                        case DEFAULT:
                            break;
                    }
                }

                toSQLDDLTypeDeclarationIdentityAfterNull(ctx, alterColumnType);
            }
            else if (alterColumnDefault != null) {
                ctx.start(ALTER_TABLE_ALTER_DEFAULT);

                switch (family) {








                    default:
                        ctx.sql(' ').visit(K_SET_DEFAULT);
                        break;
                }

                ctx.sql(' ').visit(alterColumnDefault)
                   .end(ALTER_TABLE_ALTER_DEFAULT);
            }
            else if (alterColumnNullability != null) {
                ctx.start(ALTER_TABLE_ALTER_NULL)
                   .sql(' ').visit(alterColumnNullability.nullable() ? K_DROP_NOT_NULL : K_SET_NOT_NULL)
                   .end(ALTER_TABLE_ALTER_NULL);
            }

            ctx.end(ALTER_TABLE_ALTER);
        }
        else if (dropColumns != null) {
            ctx.start(ALTER_TABLE_DROP);

            if (REQUIRE_REPEAT_DROP_ON_MULTI_ALTER.contains(family)) {
                String separator = "";

                for (Field<?> dropColumn : dropColumns) {
                    ctx.sql(separator)
                       .qualify(false);

                    acceptDropColumn(ctx);
                    acceptIfExistsColumn(ctx);

                    ctx.sql(' ')
                       .visit(dropColumn)
                       .qualify(true);







                    separator = ", ";
                }
            }
            else {
                acceptDropColumn(ctx);
                acceptIfExistsColumn(ctx);









                ctx.sql(' ');

                ctx.qualify(false)
                   .visit(dropColumns)
                   .qualify(true);






                if (dropColumnCascade)
                    ctx.sql(' ').visit(K_CASCADE);
            }






            ctx.end(ALTER_TABLE_DROP);
        }
        else if (dropConstraint != null) {
            ctx.start(ALTER_TABLE_DROP);
            ctx.data(DATA_CONSTRAINT_REFERENCE, true);

            if (dropConstraintType == FOREIGN_KEY && NO_SUPPORT_DROP_CONSTRAINT.contains(family))
                ctx.visit(K_DROP).sql(' ').visit(K_FOREIGN_KEY)
                   .sql(' ')
                   .visit(dropConstraint);
            else if (dropConstraintType == PRIMARY_KEY && NO_SUPPORT_DROP_CONSTRAINT.contains(family))
                ctx.visit(K_DROP).sql(' ').visit(K_PRIMARY_KEY);
            else
                ctx.visit(K_DROP_CONSTRAINT)
                   .sql(' ')
                   .visit(dropConstraint);

            ctx.data().remove(DATA_CONSTRAINT_REFERENCE);
            ctx.end(ALTER_TABLE_DROP);
        }
        else if (dropConstraintType == PRIMARY_KEY) {
            ctx.start(ALTER_TABLE_DROP);
            ctx.visit(K_DROP).sql(' ').visit(K_PRIMARY_KEY);
            ctx.end(ALTER_TABLE_DROP);
        }

        if (!omitAlterTable)
            ctx.formatIndentEnd();
    }

    private final void acceptFirstBeforeAfter(Context<?> ctx) {
        boolean previous = ctx.qualify();

        if (addFirst)
            ctx.sql(' ').visit(K_FIRST);
        else if (addBefore != null)
            ctx.sql(' ').visit(K_BEFORE).sql(' ').qualify(false).visit(addBefore).qualify(previous);
        else if (addAfter != null)
            ctx.sql(' ').visit(K_AFTER).sql(' ').qualify(false).visit(addAfter).qualify(previous);
    }

    private final void acceptDropColumn(Context<?> ctx) {
        switch (ctx.family()) {




















            default:
                ctx.visit(K_DROP);
                break;
        }
    }

    private final void acceptIfExistsColumn(Context<?> ctx) {
        if (ifExistsColumn) {
            switch (ctx.family()) {











                case H2:
                case MARIADB:
                case POSTGRES:
                default:
                    ctx.sql(' ').visit(K_IF_EXISTS);
                    break;
            }
        }
    }












































































    private final void alterColumnTypeAndNullabilityInBlock(Context<?> ctx) {
        begin(ctx);






        accept1(ctx);







        ctx.sql(';').formatSeparator();

        switch (ctx.family()) {



































            case POSTGRES: {
                AlterTableAlterStep<?> step = ctx.dsl().alterTable(table).alterColumn(alterColumn);
                ctx.visit(alterColumnType.nullable() ? step.dropNotNull() : step.setNotNull())
                   .sql(';');
                break;
            }
        }

        end(ctx);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
