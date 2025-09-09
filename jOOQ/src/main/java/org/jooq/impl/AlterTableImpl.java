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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
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
import static org.jooq.SQLDialect.*;
import static org.jooq.impl.ConstraintType.FOREIGN_KEY;
import static org.jooq.impl.ConstraintType.PRIMARY_KEY;
import static org.jooq.impl.ConstraintType.UNIQUE;
import static org.jooq.impl.DSL.alterTable;
import static org.jooq.impl.DSL.begin;
import static org.jooq.impl.DSL.commentOnColumn;
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
import static org.jooq.impl.Keywords.K_ADD_COLUMN;
import static org.jooq.impl.Keywords.K_AFTER;
import static org.jooq.impl.Keywords.K_ALTER;
import static org.jooq.impl.Keywords.K_ALTER_COLUMN;
import static org.jooq.impl.Keywords.K_ALTER_CONSTRAINT;
import static org.jooq.impl.Keywords.K_ALTER_TABLE;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_BEFORE;
import static org.jooq.impl.Keywords.K_BY;
import static org.jooq.impl.Keywords.K_CASCADE;
import static org.jooq.impl.Keywords.K_CHANGE;
import static org.jooq.impl.Keywords.K_CHANGE_COLUMN;
import static org.jooq.impl.Keywords.K_COLUMN;
import static org.jooq.impl.Keywords.K_COMMENT;
import static org.jooq.impl.Keywords.K_CONSTRAINT;
import static org.jooq.impl.Keywords.K_CONSTRAINTS;
import static org.jooq.impl.Keywords.K_DEFAULT;
import static org.jooq.impl.Keywords.K_DROP;
import static org.jooq.impl.Keywords.K_DROP_COLUMN;
import static org.jooq.impl.Keywords.K_DROP_CONSTRAINT;
import static org.jooq.impl.Keywords.K_DROP_DEFAULT;
import static org.jooq.impl.Keywords.K_DROP_NOT_NULL;
import static org.jooq.impl.Keywords.K_ELSE;
import static org.jooq.impl.Keywords.K_END_IF;
import static org.jooq.impl.Keywords.K_EXCEPTION;
import static org.jooq.impl.Keywords.K_EXEC;
import static org.jooq.impl.Keywords.K_FIRST;
import static org.jooq.impl.Keywords.K_FOREIGN_KEY;
import static org.jooq.impl.Keywords.K_GENERATED;
import static org.jooq.impl.Keywords.K_IDENTITY;
import static org.jooq.impl.Keywords.K_IF;
import static org.jooq.impl.Keywords.K_IF_EXISTS;
import static org.jooq.impl.Keywords.K_IF_NOT_EXISTS;
import static org.jooq.impl.Keywords.K_LIKE;
import static org.jooq.impl.Keywords.K_MODIFY;
import static org.jooq.impl.Keywords.K_NOT_NULL;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.Keywords.K_POSITION;
import static org.jooq.impl.Keywords.K_PRIMARY_KEY;
import static org.jooq.impl.Keywords.K_RAISE;
import static org.jooq.impl.Keywords.K_REMOVE;
import static org.jooq.impl.Keywords.K_RENAME;
import static org.jooq.impl.Keywords.K_RENAME_COLUMN;
import static org.jooq.impl.Keywords.K_RENAME_CONSTRAINT;
import static org.jooq.impl.Keywords.K_RENAME_INDEX;
import static org.jooq.impl.Keywords.K_RENAME_OBJECT;
import static org.jooq.impl.Keywords.K_RENAME_TABLE;
import static org.jooq.impl.Keywords.K_RENAME_TO;
import static org.jooq.impl.Keywords.K_REPLACE;
import static org.jooq.impl.Keywords.K_SET;
import static org.jooq.impl.Keywords.K_SET_DATA_TYPE;
import static org.jooq.impl.Keywords.K_SET_DEFAULT;
import static org.jooq.impl.Keywords.K_SET_NOT_NULL;
import static org.jooq.impl.Keywords.K_THEN;
import static org.jooq.impl.Keywords.K_TO;
import static org.jooq.impl.Keywords.K_TYPE;
import static org.jooq.impl.Keywords.K_USING_INDEX;
import static org.jooq.impl.Keywords.K_WHEN;
import static org.jooq.impl.Keywords.K_WITH_NO_DATACOPY;
import static org.jooq.impl.QOM.Cascade.CASCADE;
import static org.jooq.impl.QOM.Cascade.RESTRICT;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.NO_SUPPORT_DEFAULT_DATETIME_LITERAL_PREFIX;
import static org.jooq.impl.Tools.begin;
import static org.jooq.impl.Tools.beginExecuteImmediate;
import static org.jooq.impl.Tools.endExecuteImmediate;
import static org.jooq.impl.Tools.executeImmediateIf;
import static org.jooq.impl.Tools.fieldsByName;
import static org.jooq.impl.Tools.filter;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.toSQLDDLTypeDeclaration;
import static org.jooq.impl.Tools.toSQLDDLTypeDeclarationForAddition;
import static org.jooq.impl.Tools.toSQLDDLTypeDeclarationIdentityAfterNull;
import static org.jooq.impl.Tools.toSQLDDLTypeDeclarationIdentityBeforeNull;
import static org.jooq.impl.Tools.tryCatch;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_CONSTRAINT_REFERENCE;
import static org.jooq.impl.Tools.ExtendedDataKey.DATA_OMIT_DATETIME_LITERAL_PREFIX;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jooq.AlterTableAddStep;
import org.jooq.AlterTableAlterConstraintStep;
import org.jooq.AlterTableAlterStep;
import org.jooq.AlterTableChangeStep;
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
import org.jooq.Index;
import org.jooq.Keyword;
import org.jooq.Name;
import org.jooq.Nullability;
// ...
import org.jooq.Query;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableElement;
// ...
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.QOM.Cascade;
import org.jooq.impl.QOM.GenerationMode;
import org.jooq.impl.QOM.UNotYetImplemented;
import org.jooq.impl.Tools.ExtendedDataKey;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class AlterTableImpl
extends
    AbstractDDLQuery
implements
    AlterTableStep,
    AlterTableAddStep,
    AlterTableDropStep,
    AlterTableAlterStep,
    AlterTableAlterConstraintStep,
    AlterTableChangeStep<Object>,
    AlterTableUsingIndexStep,
    AlterTableRenameColumnToStep,
    AlterTableRenameIndexToStep,
    AlterTableRenameConstraintToStep,
    UNotYetImplemented
{

    private static final Clause[] CLAUSES                               = { ALTER_TABLE };
    static final Set<SQLDialect>  NO_SUPPORT_IF_EXISTS                  = SQLDialect.supportedUntil(CUBRID, DERBY, FIREBIRD, MARIADB, MYSQL);
    static final Set<SQLDialect>  NO_SUPPORT_IF_EXISTS_COLUMN           = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD);
    static final Set<SQLDialect>  NO_SUPPORT_IF_EXISTS_COLUMN_ALTER     = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, MYSQL, POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect>  NO_SUPPORT_IF_EXISTS_COLUMN_RENAME    = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, MYSQL, POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect>  NO_SUPPORT_IF_EXISTS_CONSTRAINT       = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, YUGABYTEDB);
    static final Set<SQLDialect>  NO_SUPPORT_IF_NOT_EXISTS_COLUMN       = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD);
    static final Set<SQLDialect>  SUPPORT_RENAME_COLUMN                 = SQLDialect.supportedBy(DERBY);
    static final Set<SQLDialect>  SUPPORT_RENAME_TABLE                  = SQLDialect.supportedBy(CLICKHOUSE, DERBY);
    static final Set<SQLDialect>  NO_SUPPORT_RENAME_QUALIFIED_TABLE     = SQLDialect.supportedBy(DERBY, DUCKDB, POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect>  NO_SUPPORT_ALTER_TYPE_AND_NULL        = SQLDialect.supportedBy(CLICKHOUSE, POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect>  NO_SUPPORT_DROP_CONSTRAINT            = SQLDialect.supportedBy(MARIADB, MYSQL);
    static final Set<SQLDialect>  NO_SUPPORT_CHANGE_COLUMN              = SQLDialect.supportedBy(CLICKHOUSE, CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, IGNITE, POSTGRES, SQLITE, TRINO, YUGABYTEDB);
    static final Set<SQLDialect>  REQUIRE_REPEAT_ADD_ON_MULTI_ALTER     = SQLDialect.supportedBy(CLICKHOUSE, FIREBIRD, MARIADB, MYSQL, POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect>  REQUIRE_REPEAT_DROP_ON_MULTI_ALTER    = SQLDialect.supportedBy(CLICKHOUSE, FIREBIRD, MARIADB, MYSQL, POSTGRES, YUGABYTEDB);



















    private final Table<?>               table;
    private final boolean                ifExists;
    private boolean                      ifExistsColumn;
    private boolean                      ifExistsConstraint;
    private boolean                      ifNotExistsColumn;
    private Comment                      comment;
    private Table<?>                     renameTo;
    private Field<?>                     renameColumn;
    private Field<?>                     renameColumnTo;
    private Index                        renameIndex;
    private Index                        renameIndexTo;
    private Constraint                   renameConstraint;
    private Constraint                   renameConstraintTo;
    private QueryPartList<TableElement>  add;
    private Field<?>                     addColumn;
    private DataType<?>                  addColumnType;
    private Constraint                   addConstraint;
    private boolean                      addFirst;
    private Field<?>                     addBefore;
    private Field<?>                     addAfter;




    private Constraint                   alterConstraint;
    private boolean                      alterConstraintEnforced;
    private Field<?>                     alterColumn;
    private Nullability                  alterColumnNullability;
    private DataType<?>                  alterColumnType;
    private Field<?>                     alterColumnDefault;
    private boolean                      alterColumnDropDefault;
    private GenerationMode               alterColumnSetIdentity;
    private boolean                      alterColumnDropIdentity;
    private Field<?>                     changeColumnFrom;
    private Field<?>                     changeColumnTo;
    private DataType<?>                  changeColumnType;
    private QueryPartList<Field<?>>      dropColumns;
    private Constraint                   dropConstraint;
    private ConstraintType               dropConstraintType;
    private Cascade                      dropCascade;

    AlterTableImpl(Configuration configuration, Table<?> table) {
        this(configuration, table, false);
    }

    AlterTableImpl(Configuration configuration, Table<?> table, boolean ifExists) {
        super(configuration);

        this.table = table;
        this.ifExists = ifExists;
    }

    final Table<?>                 $table()                   { return table; }
    final boolean                  $ifExists()                { return ifExists; }
    final boolean                  $ifExistsColumn()          { return ifExistsColumn; }
    final boolean                  $ifExistsConstraint()      { return ifExistsConstraint; }
    final boolean                  $ifNotExistsColumn()       { return ifNotExistsColumn; }
    final List<TableElement>       $add()                     { return add; }
    final Field<?>                 $addColumn()               { return addColumn; }
    final DataType<?>              $addColumnType()           { return addColumnType; }
    final Constraint               $addConstraint()           { return addConstraint; }
    final boolean                  $addFirst()                { return addFirst; }
    final Field<?>                 $addBefore()               { return addBefore; }
    final Field<?>                 $addAfter()                { return addAfter; }
    final Field<?>                 $alterColumn()             { return alterColumn; }
    final Nullability              $alterColumnNullability()  { return alterColumnNullability; }
    final DataType<?>              $alterColumnType()         { return alterColumnType; }
    final Field<?>                 $alterColumnDefault()      { return alterColumnDefault; }
    final boolean                  $alterColumnDropDefault()  { return alterColumnDropDefault; }
    final GenerationMode           $alterColumnSetIdentity()  { return alterColumnSetIdentity; }
    final boolean                  $alterColumnDropIdentity() { return alterColumnDropIdentity; }
    final Constraint               $alterConstraint()         { return alterConstraint; }
    final boolean                  $alterConstraintEnforced() { return alterConstraintEnforced; }
    final Field<?>                 $changeColumnFrom()        { return changeColumnFrom; }
    final Field<?>                 $changeColumnTo()          { return changeColumnTo; }
    final DataType<?>              $changeColumnType()        { return changeColumnType; }
    final Table<?>                 $renameTo()                { return renameTo; }
    final Field<?>                 $renameColumn()            { return renameColumn; }
    final Field<?>                 $renameColumnTo()          { return renameColumnTo; }
    final Constraint               $renameConstraint()        { return renameConstraint; }
    final Constraint               $renameConstraintTo()      { return renameConstraintTo; }
    final List<Field<?>>           $dropColumns()             { return dropColumns; }
    final Cascade                  $dropCascade()             { return dropCascade; }
    final Constraint               $dropConstraint()          { return dropConstraint; }
    final ConstraintType           $dropConstraintType()      { return dropConstraintType; }

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
    public final AlterTableImpl renameColumnIfExists(Field<?> oldName) {
        ifExistsColumn = true;
        return renameColumn(oldName);
    }

    @Override
    public final AlterTableImpl renameColumnIfExists(Name oldName) {
        return renameColumnIfExists(field(oldName));
    }

    @Override
    public final AlterTableImpl renameColumnIfExists(String oldName) {
        return renameColumnIfExists(name(oldName));
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
    public final AlterTableImpl add(TableElement field) {
        return add(Arrays.asList(field));
    }

    @Override
    public final AlterTableImpl add(TableElement... fields) {
        return add(Arrays.asList(fields));
    }

    @Override
    public final AlterTableImpl add(Collection<? extends TableElement> fields) {

        // [#9570] Better portability of single item ADD statements
        if (fields.size() == 1) {
            TableElement first = fields.iterator().next();

            if (first instanceof Field<?> f)
                return add(f);
            else if (first instanceof Constraint c)
                return add(c);
            else if (first instanceof Index i)
                throw new UnsupportedOperationException("ALTER TABLE .. ADD INDEX not yet supported, see https://github.com/jOOQ/jOOQ/issues/13006");
        }

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
    public final <T> AlterTableImpl alterIfExists(Field<T> field) {
        return alterColumnIfExists(field);
    }

    @Override
    public final AlterTableImpl alterIfExists(Name field) {
        return alterColumnIfExists(field);
    }

    @Override
    public final AlterTableImpl alterIfExists(String field) {
        return alterColumnIfExists(field);
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
    public final AlterTableImpl alterColumnIfExists(Name field) {
        return alterColumnIfExists(field(field));
    }

    @Override
    public final AlterTableImpl alterColumnIfExists(String field) {
        return alterColumnIfExists(name(field));
    }

    @Override
    public final <T> AlterTableImpl alterColumnIfExists(Field<T> field) {
        ifExistsColumn = true;
        return alterColumn(field);
    }

    @Override
    public final <T> AlterTableChangeStep<T> change(Field<?> oldName, Field<T> newName) {
        return changeColumn(oldName, newName);
    }

    @Override
    public final AlterTableChangeStep<Object> change(Name oldName, Name newName) {
        return changeColumn(oldName, newName);
    }

    @Override
    public final AlterTableChangeStep<Object> change(String oldName, String newName) {
        return changeColumn(oldName, newName);
    }

    @Override
    public final <T> AlterTableChangeStep<T> changeIfExists(Field<?> oldName, Field<T> newName) {
        return changeColumnIfExists(oldName, newName);
    }

    @Override
    public final AlterTableChangeStep<Object> changeIfExists(Name oldName, Name newName) {
        return changeColumnIfExists(oldName, newName);
    }

    @Override
    public final AlterTableChangeStep<Object> changeIfExists(String oldName, String newName) {
        return changeColumnIfExists(oldName, newName);
    }

    @Override
    public final AlterTableChangeStep<Object> changeColumn(Name oldName, Name newName) {
        return changeColumn(field(oldName), field(newName));
    }

    @Override
    public final AlterTableChangeStep<Object> changeColumn(String oldName, String newName) {
        return changeColumn(name(oldName), name(newName));
    }

    @Override
    public final <T> AlterTableChangeStep<T> changeColumn(Field<?> oldName, Field<T> newName) {
        changeColumnFrom = oldName;
        changeColumnTo = newName;
        return (AlterTableChangeStep<T>) this;
    }

    @Override
    public final AlterTableChangeStep<Object> changeColumnIfExists(Name oldName, Name newName) {
        return changeColumnIfExists(field(oldName), field(newName));
    }

    @Override
    public final AlterTableChangeStep<Object> changeColumnIfExists(String oldName, String newName) {
        return changeColumnIfExists(name(oldName), name(newName));
    }

    @Override
    public final <T> AlterTableChangeStep<T> changeColumnIfExists(Field<?> oldName, Field<T> newName) {
        ifExistsColumn = true;
        return changeColumn(oldName, newName);
    }

    @Override
    public final AlterTableImpl alter(Constraint constraint) {
        return alterConstraint(constraint);
    }

    @Override
    public final AlterTableImpl alterConstraint(Name constraint) {
        return alterConstraint(constraint(constraint));
    }

    @Override
    public final AlterTableImpl alterConstraint(String constraint) {
        return alterConstraint(constraint(constraint));
    }

    @Override
    public final AlterTableImpl alterConstraint(Constraint constraint) {
        alterConstraint = constraint;
        return this;
    }

    @Override
    public final AlterTableImpl enforced() {
        alterConstraintEnforced = true;
        return this;
    }

    @Override
    public final AlterTableImpl notEnforced() {
        alterConstraintEnforced = false;
        return this;
    }

    @Override
    public final AlterTableImpl set(DataType type) {
        if (changeColumnFrom != null)
            changeColumnType = type;
        else
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
        return setDefault(literal);
    }

    @Override
    public final AlterTableImpl defaultValue(Field expression) {
        return setDefault(expression);
    }

    @Override
    public final AlterTableImpl default_(Object literal) {
        return setDefault(literal);
    }

    @Override
    public final AlterTableImpl default_(Field expression) {
        return setDefault(expression);
    }

    @Override
    public final AlterTableImpl setDefault(Object literal) {
        return default_(Tools.field(literal));
    }

    @Override
    public final AlterTableImpl setDefault(Field expression) {
        alterColumnDefault = expression;
        return this;
    }

    @Override
    public final AlterTableImpl dropDefault() {
        alterColumnDropDefault = true;
        return this;
    }

    @Override
    public final AlterTableImpl setGeneratedByDefaultAsIdentity() {
        alterColumnSetIdentity = GenerationMode.BY_DEFAULT;
        return this;
    }

    @Override
    public final AlterTableImpl dropIdentity() {
        alterColumnDropIdentity = true;
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
    public final AlterTableImpl dropIfExists(Constraint constraint) {
        ifExistsConstraint = true;
        return drop(constraint);
    }

    @Override
    public final AlterTableImpl dropConstraintIfExists(Constraint constraint) {
        return dropIfExists(constraint);
    }

    @Override
    public final AlterTableImpl dropConstraintIfExists(Name constraint) {
        return dropIfExists(constraint(constraint));
    }

    @Override
    public final AlterTableImpl dropConstraintIfExists(String constraint) {
        return dropIfExists(constraint(constraint));
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
    public final AlterTableImpl dropUnique(Constraint constraint) {
        dropConstraint = constraint;
        dropConstraintType = UNIQUE;
        return this;
    }

    @Override
    public final AlterTableImpl dropUnique(Name constraint) {
        return dropUnique(constraint(constraint));
    }

    @Override
    public final AlterTableImpl dropUnique(String constraint) {
        return dropUnique(constraint(constraint));
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
        dropCascade = CASCADE;
        return this;
    }

    @Override
    public final AlterTableFinalStep restrict() {
        dropCascade = RESTRICT;
        return this;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    private final boolean supportsIfExistsColumn(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS_COLUMN.contains(ctx.dialect());
    }

    private final boolean supportsIfExistsColumnAlter(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS_COLUMN_ALTER.contains(ctx.dialect());
    }

    private final boolean supportsIfExistsColumnRename(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS_COLUMN_RENAME.contains(ctx.dialect());
    }

    private final boolean supportsIfExistsConstraint(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS_CONSTRAINT.contains(ctx.dialect());
    }

    private final boolean supportsIfNotExistsColumn(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS_COLUMN.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if ((ifExists && !supportsIfExists(ctx))
            || (ifExistsColumn && dropColumns  != null && !supportsIfExistsColumn(ctx))
            || (ifExistsColumn && alterColumn  != null && !supportsIfExistsColumnAlter(ctx))
            || (ifExistsColumn && changeColumnFrom != null && !supportsIfExistsColumnAlter(ctx))
            || (ifExistsColumn && renameColumn != null && !supportsIfExistsColumnRename(ctx))
            || (ifExistsConstraint && !supportsIfExistsConstraint(ctx))
            || (ifNotExistsColumn && !supportsIfNotExistsColumn(ctx))
        ) {
            tryCatch(
                ctx,
                DDLStatementType.ALTER_TABLE,
                ifExists ? TRUE : null,
                ifExistsColumn || ifExistsConstraint ? TRUE : ifNotExistsColumn ? FALSE : null,
                c -> accept0(c)
            );
        }
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        SQLDialect family = ctx.family();

        if (changeColumnFrom != null && NO_SUPPORT_CHANGE_COLUMN.contains(ctx.dialect())) {
            if (changeColumnFrom.getUnqualifiedName().equals(changeColumnTo.getUnqualifiedName())) {
                if (ifExistsColumn)
                    ctx.visit(alterTable(table).alterIfExists(changeColumnFrom).set(changeColumnType));
                else
                    ctx.visit(alterTable(table).alter(changeColumnFrom).set(changeColumnType));
            }
            else if (ifExistsColumn) {
                ctx.visit(begin(
                    alterTable(table).renameColumnIfExists(changeColumnFrom).to(changeColumnTo),
                    alterTable(table).alterIfExists(changeColumnTo).set(changeColumnType)
                ));
            }
            else {
                ctx.visit(begin(
                    alterTable(table).renameColumn(changeColumnFrom).to(changeColumnTo),
                    alterTable(table).alter(changeColumnTo).set(changeColumnType)
                ));
            }

            return;
        }

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

        if (family == FIREBIRD) {
            if (addFirst) {
                begin(ctx, c1 -> {
                    Tools.executeImmediate(c1, c2 -> accept1(c2));
                    c1.formatSeparator();
                    Tools.executeImmediate(c1, c2 -> {
                        c2.visit(K_ALTER_TABLE).sql(' ').visit(table).sql(' ').visit(K_ALTER).sql(' ').visit(addColumn).sql(' ').visit(K_POSITION).sql(" 1");
                    });
                });
                return;
            }
        }






























        if (renameIndexTo != null) {
            switch (family) {

                // [#5724] These databases use table-scoped index names











                case MARIADB:

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
                case YUGABYTEDB:
                    alterColumnTypeAndNullabilityInBlock(ctx);
                    return;
            }
        }








































        if (CreateTableImpl.EMULATE_COLUMN_COMMENT_IN_BLOCK.contains(ctx.dialect())) {
            List<Field<?>> comments = addColumnComments();

            if (!comments.isEmpty()) {
                begin(ctx, c1 -> {
                    executeImmediateIf(
                        CreateTableImpl.REQUIRE_EXECUTE_IMMEDIATE.contains(c1.dialect()),
                        c1,
                        c2 -> accept1(c2)
                    );

                    c1.formatSeparator();

                    for (Field<?> c : comments) {
                        executeImmediateIf(CreateTableImpl.REQUIRE_EXECUTE_IMMEDIATE.contains(ctx.dialect()), c1,
                            c2 -> c2.visit(commentOnColumn(table.getQualifiedName().append(c.getUnqualifiedName())).is(c.getComment()))
                        );
                    }
                });
                return;
            }
        }

        accept1(ctx);
    }

    private final List<Field<?>> addColumnComments() {
        if (addColumn != null) {
            if (!addColumn.getComment().isEmpty())
                return asList(addColumn);
        }
        else if (add != null)
            return map(filter(add, c -> c instanceof Field<?> && !c.getComment().isEmpty()), c -> (Field<?>) c);

        return emptyList();
    }

    private final void accept1(Context<?> ctx) {
        SQLDialect family = ctx.family();

        boolean omitAlterTable =
               (renameConstraint != null && family == HSQLDB)
            || (renameColumn != null && SUPPORT_RENAME_COLUMN.contains(ctx.dialect()));
        boolean renameTable = renameTo != null && SUPPORT_RENAME_TABLE.contains(ctx.dialect());
        boolean renameObject = renameTo != null && (false );

        if (!omitAlterTable) {
            ctx.start(ALTER_TABLE_TABLE)
               .visit(renameObject
                   ? K_RENAME_OBJECT
                   : renameTable
                   ? K_RENAME_TABLE
                   : K_ALTER_TABLE);

            if (ifExists && supportsIfExists(ctx))
                ctx.sql(' ').visit(K_IF_EXISTS);

            ctx.sql(' ').visit(table).sql(' ')
               .end(ALTER_TABLE_TABLE);
        }

        if (comment != null) {
            ctx.visit(K_COMMENT).sql(' ').visit(comment);
        }
        else if (renameTo != null) {
            boolean qualify = ctx.qualify();
            boolean unqualify = unqualifyRenameTo(ctx);

            ctx.start(ALTER_TABLE_RENAME);

            if (unqualify)
                ctx.qualify(false);

            Keyword renameToKeyword = K_RENAME_TO;

            if (renameObject || renameTable)
                renameToKeyword = K_TO;





            ctx.visit(renameToKeyword).sql(' ')
               .visit(renameTo);

            if (unqualify)
                ctx.qualify(qualify);

            ctx.end(ALTER_TABLE_RENAME);
        }
        else if (renameColumn != null) {
            ctx.start(ALTER_TABLE_RENAME_COLUMN);

            switch (ctx.family()) {


                case DERBY:
                    ctx.visit(K_RENAME_COLUMN).sql(' ').visit(renameColumn).sql(' ')
                       .visit(K_TO).sql(' ').qualify(false, c -> c.visit(renameColumnTo));

                    break;

                case H2:
                case HSQLDB:
                    ctx.visit(K_ALTER_COLUMN).sql(' ');

                    if (ifExistsColumn && supportsIfExistsColumnRename(ctx))
                        ctx.visit(K_IF_EXISTS).sql(' ');

                    ctx.qualify(false, c -> c.visit(renameColumn)).sql(' ')
                       .visit(K_RENAME_TO).sql(' ').qualify(false, c -> c.visit(renameColumnTo));

                    break;

                case FIREBIRD:
                    ctx.visit(K_ALTER_COLUMN).sql(' ').qualify(false, c -> c.visit(renameColumn)).sql(' ')
                       .visit(K_TO).sql(' ').qualify(false, c -> c.visit(renameColumnTo));

                    break;

















                default:
                    ctx.visit(K_RENAME_COLUMN).sql(' ');

                    if (ifExistsColumn && supportsIfExistsColumnRename(ctx))
                        ctx.visit(K_IF_EXISTS).sql(' ');

                    ctx.qualify(false, c -> c.visit(renameColumn)).sql(' ')
                       .visit(K_TO).sql(' ').qualify(false, c -> c.visit(renameColumnTo));

                    break;
            }

            ctx.end(ALTER_TABLE_RENAME_COLUMN);
        }
        else if (renameIndex != null) {
            ctx.start(ALTER_TABLE_RENAME_INDEX)
               .visit(K_RENAME_INDEX).sql(' ').qualify(false, c -> c.visit(renameIndex)).sql(' ')
               .visit(K_TO).sql(' ').qualify(false, c -> c.visit(renameIndexTo))
               .end(ALTER_TABLE_RENAME_INDEX);
        }
        else if (renameConstraint != null) {
            ctx.start(ALTER_TABLE_RENAME_CONSTRAINT);
            ctx.data(DATA_CONSTRAINT_REFERENCE, true, c1 -> {
                if (family == HSQLDB)
                    c1.visit(K_ALTER_CONSTRAINT).sql(' ').qualify(false, c2 -> c2.visit(renameConstraint)).sql(' ')
                      .visit(K_RENAME_TO).sql(' ').qualify(false, c2 -> c2.visit(renameConstraintTo));
                else
                    c1.visit( K_RENAME_CONSTRAINT).sql(' ')
                      .qualify(false, c2 -> c2.visit(renameConstraint)).sql(' ')
                      .visit(K_TO).sql(' ').qualify(false, c2 -> c2.visit(renameConstraintTo));
            });

            ctx.end(ALTER_TABLE_RENAME_CONSTRAINT);
        }
        else if (add != null) {
            boolean multiAdd = REQUIRE_REPEAT_ADD_ON_MULTI_ALTER.contains(ctx.dialect());
            boolean parens = !multiAdd ;
            boolean comma = true ;

            ctx.start(ALTER_TABLE_ADD)
               .visit(addColumnKeyword(ctx))
               .sql(' ');

            if (parens)
                ctx.sql('(');

            boolean indent = !multiAdd && add.size() > 1;

            if (indent)
                ctx.formatIndentStart()
                   .formatNewLine();

            for (int i = 0; i < add.size(); i++) {
                if (i > 0) {
                    ctx.sql(comma ? "," : "").formatSeparator();

                    if (multiAdd)
                        ctx.visit(addColumnKeyword(ctx)).sql(' ');
                }

                TableElement part = add.get(i);

                // [#19032] We already use DATA_CONSTRAINT_REFERENCE to declare qualified constraint references,
                //          so constraints shouldn't be unqualified here.
                if (part instanceof Field<?> f) {
                    ctx.qualify(false, c -> c.visit(Tools.uncollate(part))).sql(' ');
                    toSQLDDLTypeDeclarationForAddition(ctx, table, f.getDataType());
                    CreateTableImpl.acceptColumnComment(ctx, f);
                }
                else
                    ctx.visit(Tools.uncollate(part));
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
            ctx.start(ALTER_TABLE_ADD)
               .visit(addColumnKeyword(ctx)).sql(' ');

            if (ifNotExistsColumn && supportsIfNotExistsColumn(ctx))
                ctx.visit(K_IF_NOT_EXISTS).sql(' ');






            ctx.qualify(false, c -> c.visit(Tools.uncollate(addColumn))).sql(' ');
            toSQLDDLTypeDeclarationForAddition(ctx, table, addColumnType);
            CreateTableImpl.acceptColumnComment(ctx, addColumn);
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

        else if (alterConstraint != null) {
            ctx.start(ALTER_TABLE_ALTER);
            ctx.data(DATA_CONSTRAINT_REFERENCE, true, c -> {
                switch (family) {






                    default:
                        ctx.visit(K_ALTER);
                        break;
                }

                ctx.sql(' ').visit(K_CONSTRAINT).sql(' ').visit(alterConstraint);
                AbstractConstraint.acceptEnforced(ctx, alterConstraintEnforced);
            });

            ctx.end(ALTER_TABLE_ALTER);
        }

        else if (changeColumnFrom != null) {
            ctx.start(ALTER_TABLE_ALTER);

            switch (family) {

                case MARIADB:
                case MYSQL:
                default:
                    ctx.visit(K_CHANGE_COLUMN);

                    if (ifExistsColumn && supportsIfExistsColumnAlter(ctx))
                        ctx.sql(' ').visit(K_IF_EXISTS);

                    ctx.sql(' ').qualify(false, c -> c.visit(changeColumnFrom));
                    ctx.sql(' ').qualify(false, c -> c.visit(changeColumnTo));
                    ctx.sql(' ');
                    acceptColumnType(ctx, table, changeColumnType);
                    break;
            }

            ctx.end(ALTER_TABLE_ALTER);
        }
        else if (alterColumn != null) {
            ctx.start(ALTER_TABLE_ALTER);

            switch (family) {






























                case CUBRID:
                case MARIADB:
                case MYSQL: {

                    // MySQL's CHANGE COLUMN clause has a mandatory RENAMING syntax...
                    boolean change = alterColumnDefault == null && !alterColumnDropDefault;

                    if (change)
                        ctx.visit(K_CHANGE_COLUMN);
                    else
                        ctx.visit(K_ALTER_COLUMN);

                    if (ifExistsColumn && supportsIfExistsColumnAlter(ctx))
                        ctx.sql(' ').visit(K_IF_EXISTS);

                    if (change)
                        ctx.sql(' ').qualify(false, c -> c.visit(alterColumn));

                    break;
                }

                case CLICKHOUSE:
                    ctx.visit(K_MODIFY).sql(' ').visit(K_COLUMN);

                    if (ifExistsColumn && supportsIfExistsColumnAlter(ctx))
                        ctx.sql(' ').visit(K_IF_EXISTS);

                    break;






                case TRINO:
                    ctx.visit(K_ALTER_COLUMN);
                    break;

                default:
                    ctx.visit(K_ALTER);

                    if (ifExistsColumn && supportsIfExistsColumnAlter(ctx))
                        ctx.sql(' ').visit(K_IF_EXISTS);

                    break;
            }






            ctx.sql(' ');
            ctx.qualify(false, c -> c.visit(alterColumn));

            if (alterColumnType != null) {
                switch (family) {


                    case DERBY:
                    case DUCKDB:
                    case TRINO:
                        ctx.sql(' ').visit(K_SET_DATA_TYPE);
                        break;




                    case FIREBIRD:
                    case POSTGRES:
                    case YUGABYTEDB:
                        ctx.sql(' ').visit(K_TYPE);
                        break;
                }

                ctx.sql(' ');
                acceptColumnType(ctx, table, alterColumnType);
            }
            else if (alterColumnDefault != null) {
                ctx.start(ALTER_TABLE_ALTER_DEFAULT);

                switch (family) {









                    default:
                        ctx.sql(' ').visit(K_SET_DEFAULT);
                        break;
                }

                ctx.sql(' ');

                if (NO_SUPPORT_DEFAULT_DATETIME_LITERAL_PREFIX.contains(ctx.dialect()) && alterColumnDefault.getDataType().isDateTime())
                    ctx.data(DATA_OMIT_DATETIME_LITERAL_PREFIX, true, c -> c.visit(alterColumnDefault));
                else
                    ctx.visit(alterColumnDefault);

                ctx.end(ALTER_TABLE_ALTER_DEFAULT);
            }
            else if (alterColumnDropDefault) {
                ctx.start(ALTER_TABLE_ALTER_DEFAULT);

                switch (family) {








                    // MySQL supports DROP DEFAULT, but it does not work correctly:
                    // https://bugs.mysql.com/bug.php?id=81010
                    // Same for MariaDB

                    case MARIADB:
                    case MYSQL:
                        ctx.sql(' ').visit(K_SET_DEFAULT).sql(' ').visit(K_NULL);
                        break;

                    case CLICKHOUSE:
                        ctx.sql(' ').visit(K_REMOVE).sql(' ').visit(K_DEFAULT);
                        break;

                    default:
                        ctx.sql(' ').visit(K_DROP_DEFAULT);
                        break;
                }

                ctx.end(ALTER_TABLE_ALTER_DEFAULT);
            }
            else if (alterColumnSetIdentity != null) {
                switch (ctx.family()) {








                    case POSTGRES:
                    case YUGABYTEDB:
                        ctx.sql(' ').visit(K_ADD).sql(' ').visit(K_GENERATED).sql(' ').visit(K_BY).sql(' ').visit(K_DEFAULT).sql(' ').visit(K_AS).sql(' ').visit(K_IDENTITY);
                        break;

                    case H2:
                        ctx.sql(' ').visit(K_SET).sql(' ').visit(K_GENERATED).sql(' ').visit(K_BY).sql(' ').visit(K_DEFAULT);
                        break;

                    default:
                        ctx.sql(' ').visit(K_SET).sql(' ').visit(K_GENERATED).sql(' ').visit(K_BY).sql(' ').visit(K_DEFAULT).sql(' ').visit(K_AS).sql(' ').visit(K_IDENTITY);
                        break;
                }
            }
            else if (alterColumnDropIdentity) {
                switch (ctx.family()) {






                    default:
                        ctx.sql(' ').visit(K_DROP).sql(' ').visit(K_IDENTITY);
                        break;
                }
            }
            else if (alterColumnNullability != null) {
                ctx.start(ALTER_TABLE_ALTER_NULL);

                switch (ctx.family()) {







                    // [#18043] Assuming users provide explicit data types of the existing column, we can reference it again


                    case MARIADB:
                    case MYSQL:
                        ctx.sql(' ');
                        toSQLDDLTypeDeclaration(ctx, alterColumn.getDataType());
                        ctx.sql(' ').visit(alterColumnNullability.nullable() ? K_NULL : K_NOT_NULL);
                        break;

                    default:
                        ctx.sql(' ').visit(alterColumnNullability.nullable() ? K_DROP_NOT_NULL : K_SET_NOT_NULL);
                        break;
                }

                ctx.end(ALTER_TABLE_ALTER_NULL);
            }






            ctx.end(ALTER_TABLE_ALTER);
        }
        else if (dropColumns != null) {
            ctx.start(ALTER_TABLE_DROP);

            if (REQUIRE_REPEAT_DROP_ON_MULTI_ALTER.contains(ctx.dialect())) {
                String separator = "";

                for (Field<?> dropColumn : dropColumns) {
                    ctx.sql(separator);

                    acceptDropColumn(ctx);
                    if (ifExistsColumn && supportsIfExistsColumn(ctx))
                        ctx.sql(' ').visit(K_IF_EXISTS);

                    ctx.sql(' ').qualify(false, c -> c.visit(dropColumn));







                    separator = ", ";
                }
            }
            else {
                acceptDropColumn(ctx);
                if (ifExistsColumn && supportsIfExistsColumn(ctx))
                    ctx.sql(' ').visit(K_IF_EXISTS);










                ctx.sql(' ');
                ctx.qualify(false, c -> c.visit(dropColumns));





            }

            acceptCascade(ctx);





            ctx.end(ALTER_TABLE_DROP);
        }
        else if (dropConstraint != null) {
            ctx.start(ALTER_TABLE_DROP);
            ctx.data(DATA_CONSTRAINT_REFERENCE, true, c -> {
                if (dropConstraintType == FOREIGN_KEY && NO_SUPPORT_DROP_CONSTRAINT.contains(c.dialect())) {
                    c.visit(K_DROP).sql(' ').visit(K_FOREIGN_KEY)
                     .sql(' ')
                     .visit(dropConstraint);
                }
                else if (dropConstraintType == PRIMARY_KEY && NO_SUPPORT_DROP_CONSTRAINT.contains(c.dialect()) || AbstractConstraint.NO_SUPPORT_NAMED_PK.contains(c.dialect())) {
                    c.visit(K_DROP).sql(' ').visit(K_PRIMARY_KEY);
                }
                else {

                    // [#9382] In some dialects, unnamed UNIQUE constraints can be
                    //         dropped by dropping their declarations.
                    c.visit(dropConstraint.getUnqualifiedName().empty() ? K_DROP : K_DROP_CONSTRAINT).sql(' ');

                    if (ifExistsConstraint && !NO_SUPPORT_IF_EXISTS_CONSTRAINT.contains(c.dialect()))
                        c.visit(K_IF_EXISTS).sql(' ');

                    c.visit(dropConstraint);
                }

                acceptCascade(c);
            });

            ctx.end(ALTER_TABLE_DROP);
        }
        else if (dropConstraintType == PRIMARY_KEY) {
            ctx.start(ALTER_TABLE_DROP);
            ctx.visit(K_DROP).sql(' ').visit(K_PRIMARY_KEY);
            ctx.end(ALTER_TABLE_DROP);
        }
    }

    private static final void acceptColumnType(Context<?> ctx, Table<?> table, DataType<?> type) {
        toSQLDDLTypeDeclaration(ctx, type);
        toSQLDDLTypeDeclarationIdentityBeforeNull(ctx, table, type);

        // [#3805] Some databases cannot change the type and the NOT NULL constraint in a single statement
        if (!NO_SUPPORT_ALTER_TYPE_AND_NULL.contains(ctx.dialect())) {
            switch (type.nullability()) {
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

        toSQLDDLTypeDeclarationIdentityAfterNull(ctx, table, type);
    }

    private final boolean unqualifyRenameTo(Context<?> ctx) {
        return NO_SUPPORT_RENAME_QUALIFIED_TABLE.contains(ctx.dialect())
            && renameTo.getQualifiedName().qualified()

            // [#10234] Omit qualification only for same-schema qualified renames
            && renameTo.getQualifiedName().qualifier().equals(table.getQualifiedName().qualifier());
    }

    private final Keyword addColumnKeyword(Context<?> ctx) {
        switch (ctx.family()) {


            case CLICKHOUSE:
            case TRINO:
                return K_ADD_COLUMN;

            default:
                return K_ADD;
        }
    }

    private final void acceptCascade(Context<?> ctx) {
        switch (ctx.family()) {
            case H2:





                acceptCascade(ctx, dropCascade);
                break;












            default:
                acceptCascade(ctx, dropCascade);
                break;
        }
    }

    private final void acceptFirstBeforeAfter(Context<?> ctx) {
        if (addFirst && ctx.family() != FIREBIRD)
            ctx.sql(' ').visit(K_FIRST);
        else if (addBefore != null)
            ctx.sql(' ').visit(K_BEFORE).sql(' ').qualify(false, c -> c.visit(addBefore));
        else if (addAfter != null)
            ctx.sql(' ').visit(K_AFTER).sql(' ').qualify(false, c -> c.visit(addAfter));
    }

    private final void acceptDropColumn(Context<?> ctx) {
        switch (ctx.family()) {














            case CLICKHOUSE:
            case TRINO:
                ctx.visit(K_DROP_COLUMN);
                break;

            default:
                ctx.visit(K_DROP);
                break;
        }
    }






















































































    private final void alterColumnTypeAndNullabilityInBlock(Context<?> ctx) {
        begin(ctx, c1 -> {






            accept1(c1);







            c1.sql(';').formatSeparator();

            switch (c1.family()) {
































                case POSTGRES:
                case YUGABYTEDB: {
                    AlterTableAlterStep<?> step = c1.dsl().alterTable(table).alterColumn(alterColumn);
                    c1.visit(alterColumnType.nullable() ? step.dropNotNull() : step.setNotNull())
                       .sql(';');
                    break;
                }
            }
        });
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
