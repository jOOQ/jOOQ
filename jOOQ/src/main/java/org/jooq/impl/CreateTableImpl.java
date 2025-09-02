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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.ExtendedDataKey.*;
import static org.jooq.impl.Tools.SimpleDataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.impl.QOM.WithOrWithoutData;
import org.jooq.impl.QOM.TableCommitAction;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;



/**
 * The <code>CREATE TABLE</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unused" })
final class CreateTableImpl
extends
    AbstractDDLQuery
implements
    QOM.CreateTable,
    CreateTableElementListStep,
    CreateTableAsStep,
    CreateTableWithDataStep,
    CreateTableOnCommitStep,
    CreateTableCommentStep,
    CreateTableStorageStep,
    CreateTableFinalStep
{

    final Table<?>                                  table;
    final boolean                                   temporary;
    final boolean                                   ifNotExists;
          QueryPartListView<? extends TableElement> tableElements;
          Select<?>                                 select;
          WithOrWithoutData                         withData;
          TableCommitAction                         onCommit;
          Comment                                   comment;
          SQL                                       storage;

    CreateTableImpl(
        Configuration configuration,
        Table<?> table,
        boolean temporary,
        boolean ifNotExists
    ) {
        this(
            configuration,
            table,
            temporary,
            ifNotExists,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    CreateTableImpl(
        Configuration configuration,
        Table<?> table,
        boolean temporary,
        boolean ifNotExists,
        Collection<? extends TableElement> tableElements,
        Select<?> select,
        WithOrWithoutData withData,
        TableCommitAction onCommit,
        Comment comment,
        SQL storage
    ) {
        super(configuration);

        this.table = table;
        this.temporary = temporary;
        this.ifNotExists = ifNotExists;
        this.tableElements = new QueryPartList<>(tableElements);
        this.select = select;
        this.withData = withData;
        this.onCommit = onCommit;
        this.comment = comment;
        this.storage = storage;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final CreateTableImpl tableElements(TableElement... tableElements) {
        return tableElements(Arrays.asList(tableElements));
    }

    @Override
    public final CreateTableImpl tableElements(Collection<? extends TableElement> tableElements) {
        if (this.tableElements == null)
            this.tableElements = new QueryPartList<>(tableElements);
        else
            this.tableElements.addAll((Collection) tableElements);

        return this;
    }

    @Override
    public final CreateTableImpl columns(String... columns) {
        return columns(Tools.fieldsByName(columns));
    }

    @Override
    public final CreateTableImpl columns(Name... columns) {
        return columns(Tools.fieldsByName(columns));
    }

    @Override
    public final CreateTableImpl columns(Field<?>... columns) {
        return columns(Arrays.asList(columns));
    }

    @Override
    public final CreateTableImpl columns(Collection<? extends Field<?>> columns) {
        return tableElements(new QueryPartList<>(columns));
    }

    @Override
    public final CreateTableImpl column(Field<?> column) {
        return tableElements(column);
    }

    @Override
    public final CreateTableImpl column(String field, DataType<?> type) {
        return column(DSL.name(field), type);
    }

    @Override
    public final CreateTableImpl column(Name field, DataType<?> type) {
        return tableElements(DSL.field(field, type));
    }

    @Override
    public final CreateTableImpl column(Field<?> field, DataType<?> type) {
        return tableElements(DSL.field(field.getQualifiedName(), type));
    }

    @Override
    public final CreateTableImpl constraints(Constraint... constraints) {
        return constraints(Arrays.asList(constraints));
    }

    @Override
    public final CreateTableImpl constraints(Collection<? extends Constraint> constraints) {
        return tableElements(new QueryPartList<>(constraints));
    }

    @Override
    public final CreateTableImpl constraint(Constraint constraint) {
        return tableElements(constraint);
    }

    @Override
    public final CreateTableImpl primaryKey(String... fields) {
        return primaryKey(Tools.fieldsByName(fields));
    }

    @Override
    public final CreateTableImpl primaryKey(Name... fields) {
        return primaryKey(Tools.fieldsByName(fields));
    }

    @Override
    public final CreateTableImpl primaryKey(Field<?>... fields) {
        return primaryKey(Arrays.asList(fields));
    }

    @Override
    public final CreateTableImpl primaryKey(Collection<? extends Field<?>> fields) {
        return tableElements(DSL.primaryKey(new QueryPartList<>(fields)));
    }

    @Override
    public final CreateTableImpl unique(String... fields) {
        return unique(Tools.fieldsByName(fields));
    }

    @Override
    public final CreateTableImpl unique(Name... fields) {
        return unique(Tools.fieldsByName(fields));
    }

    @Override
    public final CreateTableImpl unique(Field<?>... fields) {
        return unique(Arrays.asList(fields));
    }

    @Override
    public final CreateTableImpl unique(Collection<? extends Field<?>> fields) {
        return tableElements(DSL.unique(new QueryPartList<>(fields)));
    }

    @Override
    public final CreateTableImpl check(Condition condition) {
        return tableElements(DSL.check(condition));
    }

    @Override
    public final CreateTableImpl indexes(Index... indexes) {
        return indexes(Arrays.asList(indexes));
    }

    @Override
    public final CreateTableImpl indexes(Collection<? extends Index> indexes) {
        return tableElements(new QueryPartList<>(indexes));
    }

    @Override
    public final CreateTableImpl index(Index index) {
        return tableElements(index);
    }

    @Override
    public final CreateTableImpl as(Select<?> select) {
        this.select = select;
        return this;
    }

    @Override
    public final CreateTableImpl withData() {
        this.withData = WithOrWithoutData.WITH_DATA;
        return this;
    }

    @Override
    public final CreateTableImpl withNoData() {
        this.withData = WithOrWithoutData.WITH_NO_DATA;
        return this;
    }

    @Override
    public final CreateTableImpl onCommitDeleteRows() {
        this.onCommit = TableCommitAction.DELETE_ROWS;
        return this;
    }

    @Override
    public final CreateTableImpl onCommitPreserveRows() {
        this.onCommit = TableCommitAction.PRESERVE_ROWS;
        return this;
    }

    @Override
    public final CreateTableImpl onCommitDrop() {
        this.onCommit = TableCommitAction.DROP;
        return this;
    }

    @Override
    public final CreateTableImpl comment(String comment) {
        return comment(DSL.comment(comment));
    }

    @Override
    public final CreateTableImpl comment(Comment comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public final CreateTableImpl storage(SQL storage) {
        this.storage = storage;
        return this;
    }

    @Override
    public final CreateTableImpl storage(String storage, QueryPart... parts) {
        return storage(DSL.sql(storage, parts));
    }

    @Override
    public final CreateTableImpl storage(String storage, Object... bindings) {
        return storage(DSL.sql(storage, bindings));
    }

    @Override
    public final CreateTableImpl storage(String storage) {
        return storage(DSL.sql(storage));
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    static final Set<SQLDialect> NO_SUPPORT_IF_NOT_EXISTS           = SQLDialect.supportedUntil(DERBY, FIREBIRD);
    static final Set<SQLDialect> NO_SUPPORT_WITH_DATA               = SQLDialect.supportedBy(DUCKDB, H2, MARIADB, MYSQL, SQLITE);
    static final Set<SQLDialect> NO_SUPPORT_CTAS_COLUMN_NAMES       = SQLDialect.supportedBy(H2);
    static final Set<SQLDialect> EMULATE_INDEXES_IN_BLOCK           = SQLDialect.supportedBy(FIREBIRD, POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect> EMULATE_SOME_ENUM_TYPES_AS_CHECK   = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, HSQLDB, POSTGRES, SQLITE, YUGABYTEDB);
    static final Set<SQLDialect> EMULATE_STORED_ENUM_TYPES_AS_CHECK = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, HSQLDB, SQLITE);
    static final Set<SQLDialect> REQUIRES_WITH_DATA                 = SQLDialect.supportedBy(HSQLDB);
    static final Set<SQLDialect> WRAP_SELECT_IN_PARENS              = SQLDialect.supportedBy(HSQLDB);
    static final Set<SQLDialect> SUPPORT_TEMPORARY                  = SQLDialect.supportedBy(DUCKDB, MARIADB, MYSQL, POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect> EMULATE_TABLE_COMMENT_IN_BLOCK     = SQLDialect.supportedBy(FIREBIRD, POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect> EMULATE_COLUMN_COMMENT_IN_BLOCK    = SQLDialect.supportedBy(FIREBIRD, POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect> REQUIRE_EXECUTE_IMMEDIATE          = SQLDialect.supportedBy(FIREBIRD);
    static final Set<SQLDialect> NO_SUPPORT_NULLABLE_PRIMARY_KEY    = SQLDialect.supportedBy(MARIADB, MYSQL);
    static final Set<SQLDialect> REQUIRE_NON_PK_COLUMNS             = SQLDialect.supportedBy(IGNITE);





    final QOM.UnmodifiableList<? extends Field<?>> $columns() {
        return QOM.unmodifiable(map(filter(tableElements, e -> e instanceof Field<?>), e -> (Field<?>) e));
    }

    final QOM.UnmodifiableList<? extends Constraint> $constraints() {
        return QOM.unmodifiable(map(filter(tableElements, e -> e instanceof Constraint), e -> (Constraint) e));
    }

    final QOM.UnmodifiableList<? extends Index> $indexes() {
        return QOM.unmodifiable(map(filter(tableElements, e -> e instanceof Index), e -> (Index) e));
    }

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx))
            tryCatch(ctx, DDLStatementType.CREATE_TABLE, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        boolean btc = comment != null && EMULATE_TABLE_COMMENT_IN_BLOCK.contains(ctx.dialect());
        boolean bcc = EMULATE_COLUMN_COMMENT_IN_BLOCK.contains(ctx.dialect()) && anyMatch($columns(), c -> !c.getComment().isEmpty());
        boolean bi = !$indexes().isEmpty() && EMULATE_INDEXES_IN_BLOCK.contains(ctx.dialect());

        if (btc || bcc || bi) {
            begin(ctx, c1 -> {
                executeImmediateIf(REQUIRE_EXECUTE_IMMEDIATE.contains(c1.dialect()), c1, c2 -> accept1(c2));

                if (btc) {
                    c1.formatSeparator();

                    executeImmediateIf(REQUIRE_EXECUTE_IMMEDIATE.contains(ctx.dialect()), c1,
                        c2 -> c2.visit(commentOnTable(table).is(comment))
                    );
                }

                if (bcc) {
                    c1.formatSeparator();

                    for (Field<?> c : $columns()) {
                        if (!c.getComment().isEmpty()) {
                            executeImmediateIf(REQUIRE_EXECUTE_IMMEDIATE.contains(ctx.dialect()), c1,
                                c2 -> c2.visit(commentOnColumn(table.getQualifiedName().append(c.getUnqualifiedName())).is(c.getComment()))
                            );
                        }
                    }
                }

                if (bi) {
                    for (Index index : $indexes()) {
                        c1.formatSeparator();

                        executeImmediateIf(REQUIRE_EXECUTE_IMMEDIATE.contains(c1.dialect()), c1, c2 -> {
                            if ("".equals(index.getName()))
                                c2.visit(createIndex().on(index.getTable(), index.getFields()));
                            else
                                c2.visit(createIndex(index.getUnqualifiedName()).on(index.getTable(), index.getFields()));
                        });
                    }
                }
            });
        }
        else
            accept1(ctx);
    }

    private final void accept1(Context<?> ctx) {
        ctx.start(Clause.CREATE_TABLE);

        if (select != null) {








            acceptCreateTableAsSelect(ctx);
        }
        else {
            toSQLCreateTable(ctx);
            toSQLOnCommit(ctx);
        }

        if (comment != null && !EMULATE_TABLE_COMMENT_IN_BLOCK.contains(ctx.dialect())) {
            ctx.formatSeparator()
               .visit(K_COMMENT).sql(' ');








            ctx.visit(comment);
        }

        // [#7772] This data() value should be available from ctx directly, not only from ctx.configuration()
        if (storage != null && ctx.configuration().data("org.jooq.ddl.ignore-storage-clauses") == null)
            ctx.formatSeparator()
               .visit(storage);

        ctx.end(Clause.CREATE_TABLE);
    }

    private void toSQLCreateTable(Context<?> ctx) {
        toSQLCreateTableName(ctx);

        QOM.UnmodifiableList<? extends Field<?>> columns = $columns();
        if (!columns.isEmpty()
                && (select == null || !NO_SUPPORT_CTAS_COLUMN_NAMES.contains(ctx.dialect()))) {
            ctx.sqlIndentStart(" (")
               .start(Clause.CREATE_TABLE_COLUMNS);

            Field<?> identity = null;
            boolean qualify = ctx.qualify();
            boolean first = true;

            columnLoop:
            for (int i = 0; i < columns.size(); i++) {
                Field<?> field = columns.get(i);
                DataType<?> type = columnType(ctx, field);

                if (type.computedOnClientVirtual(ctx.configuration()))
                    continue columnLoop;

                if (identity == null && type.identity())
                    identity = field;

                if (!first)
                    ctx.sql(',').formatSeparator();

                ctx.qualify(false);
                ctx.visit(Tools.uncollate(field));
                ctx.qualify(qualify);

                if (select == null) {
                    ctx.sql(' ');
                    Tools.toSQLDDLTypeDeclarationForAddition(ctx, type);
                    acceptColumnComment(ctx, field);
                }

                first = false;
            }

            // [#10551] Ignite requires at least one non-PK column.
            toSQLDummyColumns(ctx);

            ctx.end(Clause.CREATE_TABLE_COLUMNS)
               .start(Clause.CREATE_TABLE_CONSTRAINTS);

            for (Constraint constraint : $constraints())

                // [#6841] SQLite has a weird requirement of the PRIMARY KEY keyword being on the column directly,
                //         when there is an identity. Thus, we must not repeat the primary key specification here.
                if (((ConstraintImpl) constraint).supported(ctx) && (ctx.family() != SQLITE || !matchingPrimaryKey(constraint, identity)))
                    ctx.sql(',')
                       .formatSeparator()
                       .visit(constraint);

            if (EMULATE_SOME_ENUM_TYPES_AS_CHECK.contains(ctx.dialect())) {
                for (Field<?> field : $columns()) {
                    DataType<?> type = field.getDataType();

                    if (EnumType.class.isAssignableFrom(type.getType())) {

                        @SuppressWarnings("unchecked")
                        DataType<EnumType> enumType = (DataType<EnumType>) type;

                        if (EMULATE_STORED_ENUM_TYPES_AS_CHECK.contains(ctx.dialect()) || !storedEnumType(enumType)) {
                            List<Field<String>> literals = map(enums(enumType.getType()), e -> inline(e.getLiteral()));

                            ctx.sql(',')
                               .formatSeparator()
                               .visit(DSL.constraint(table.getName() + "_" + field.getName() + "_chk")
                                         .check(((Field) field).in(literals)));
                        }
                    }
                }
            }

            ctx.end(Clause.CREATE_TABLE_CONSTRAINTS);

            if (!$indexes().isEmpty() && !EMULATE_INDEXES_IN_BLOCK.contains(ctx.dialect())) {
                ctx.qualify(false);

                for (Index index : $indexes()) {
                    ctx.sql(',').formatSeparator();

                    if (index.getUnique())
                        ctx.visit(K_UNIQUE).sql(' ');

                    ctx.visit(K_INDEX);

                    if (!"".equals(index.getName()))
                        ctx.sql(' ').visit(index.getUnqualifiedName());

                    ctx.sql(" (")
                       .visit(new SortFieldList(index.getFields()))
                       .sql(')');
                }

                ctx.qualify(qualify);
            }

            ctx.sqlIndentEnd(')');
        }
    }

    static void acceptColumnComment(Context<?> ctx, Field<?> field) {
        if (!field.getComment().isEmpty() && !EMULATE_COLUMN_COMMENT_IN_BLOCK.contains(ctx.dialect()))
            ctx.sql(' ').visit(K_COMMENT).sql(' ').visit(inline(field.getComment()));
    }

    private final void toSQLDummyColumns(Context<?> ctx) {

        // [#10551] [#11268] TODO: Make this behaviour configurable
        if (REQUIRE_NON_PK_COLUMNS.contains(ctx.dialect())) {
            Field<?>[] primaryKeyColumns = primaryKeyColumns();

            if (primaryKeyColumns != null && primaryKeyColumns.length == $columns().size()) {
                ctx.sql(',').formatSeparator();
                ctx.visit(DSL.field(name("dummy")));

                if (select == null) {
                    ctx.sql(' ');
                    Tools.toSQLDDLTypeDeclarationForAddition(ctx, INTEGER);
                }
            }
        }
    }

    private final DataType<?> columnType(Context<?> ctx, Field<?> field) {
        DataType<?> type = field.getDataType();

        if (NO_SUPPORT_NULLABLE_PRIMARY_KEY.contains(ctx.dialect())
                && type.nullability() == Nullability.DEFAULT
                && isPrimaryKey(field))
            type = type.nullable(false);

        return type;
    }

    private final Field<?>[] primaryKeyColumns() {
        return Tools.findAny(
            $constraints(),
            c -> c instanceof ConstraintImpl && ((ConstraintImpl) c).$primaryKey() != null,
            c -> ((ConstraintImpl) c).$primaryKey()
        );
    }

    private final boolean isPrimaryKey(Field<?> field) {
        return anyMatch(primaryKeyColumns(), field::equals);
    }

    private final boolean matchingPrimaryKey(Constraint constraint, Field<?> identity) {
        if (constraint instanceof ConstraintImpl c)
            return c.matchingPrimaryKey(identity);

        return false;
    }

    private final void acceptCreateTableAsSelect(Context<?> ctx) {
        toSQLCreateTable(ctx);
        toSQLOnCommit(ctx);
        ctx.formatSeparator()
           .visit(K_AS);

        if (WRAP_SELECT_IN_PARENS.contains(ctx.dialect()))
            ctx.sqlIndentStart(" (");
        else
            ctx.formatSeparator();

        if (WithOrWithoutData.WITH_NO_DATA == withData && NO_SUPPORT_WITH_DATA.contains(ctx.dialect()))
            ctx.data(DATA_SELECT_NO_DATA, true);

        ctx.start(Clause.CREATE_TABLE_AS);

        if (!$columns().isEmpty() && NO_SUPPORT_CTAS_COLUMN_NAMES.contains(ctx.dialect()))
            ctx.visit(select(asterisk()).from(select.asTable(table(name("t")), $columns())));
        else
            ctx.visit(select);

        ctx.end(Clause.CREATE_TABLE_AS);

        if (WithOrWithoutData.WITH_NO_DATA == withData && NO_SUPPORT_WITH_DATA.contains(ctx.dialect()))
            ctx.data().remove(DATA_SELECT_NO_DATA);

        if (WRAP_SELECT_IN_PARENS.contains(ctx.dialect())) {
            ctx.sqlIndentEnd(')');
        }

        if (WithOrWithoutData.WITH_NO_DATA == withData && !NO_SUPPORT_WITH_DATA.contains(ctx.dialect()))
            ctx.formatSeparator()
               .visit(K_WITH_NO_DATA);
        else if (WithOrWithoutData.WITH_DATA == withData && !NO_SUPPORT_WITH_DATA.contains(ctx.dialect()))
            ctx.formatSeparator()
               .visit(K_WITH_DATA);
        else if (REQUIRES_WITH_DATA.contains(ctx.dialect()))
            ctx.formatSeparator()
               .visit(K_WITH_DATA);
    }



































    private final void toSQLCreateTableName(Context<?> ctx) {
        ctx.start(Clause.CREATE_TABLE_NAME)
           .visit(K_CREATE)
           .sql(' ');

        if (temporary)
            if (SUPPORT_TEMPORARY.contains(ctx.dialect()))
                ctx.visit(K_TEMPORARY).sql(' ');
            else
                ctx.visit(K_GLOBAL_TEMPORARY).sql(' ');

        ctx.visit(K_TABLE)
           .sql(' ');

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.visit(K_IF_NOT_EXISTS)
               .sql(' ');

        ctx.visit(table)
           .end(Clause.CREATE_TABLE_NAME);
    }

    private final void toSQLOnCommit(Context<?> ctx) {
        if (temporary && onCommit != null) {
            switch (onCommit) {
                case DELETE_ROWS:   ctx.formatSeparator().visit(K_ON_COMMIT_DELETE_ROWS);   break;
                case PRESERVE_ROWS: ctx.formatSeparator().visit(K_ON_COMMIT_PRESERVE_ROWS); break;
                case DROP:          ctx.formatSeparator().visit(K_ON_COMMIT_DROP);          break;
            }
        }
    }























    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Table<?> $table() {
        return table;
    }

    @Override
    public final boolean $temporary() {
        return temporary;
    }

    @Override
    public final boolean $ifNotExists() {
        return ifNotExists;
    }

    @Override
    public final QOM.UnmodifiableList<? extends TableElement> $tableElements() {
        return QOM.unmodifiable(tableElements);
    }

    @Override
    public final Select<?> $select() {
        return select;
    }

    @Override
    public final WithOrWithoutData $withData() {
        return withData;
    }

    @Override
    public final TableCommitAction $onCommit() {
        return onCommit;
    }

    @Override
    public final Comment $comment() {
        return comment;
    }

    @Override
    public final SQL $storage() {
        return storage;
    }

    @Override
    public final QOM.CreateTable $table(Table<?> newValue) {
        return $constructor().apply(newValue, $temporary(), $ifNotExists(), $tableElements(), $select(), $withData(), $onCommit(), $comment(), $storage());
    }

    @Override
    public final QOM.CreateTable $temporary(boolean newValue) {
        return $constructor().apply($table(), newValue, $ifNotExists(), $tableElements(), $select(), $withData(), $onCommit(), $comment(), $storage());
    }

    @Override
    public final QOM.CreateTable $ifNotExists(boolean newValue) {
        return $constructor().apply($table(), $temporary(), newValue, $tableElements(), $select(), $withData(), $onCommit(), $comment(), $storage());
    }

    @Override
    public final QOM.CreateTable $tableElements(Collection<? extends TableElement> newValue) {
        return $constructor().apply($table(), $temporary(), $ifNotExists(), newValue, $select(), $withData(), $onCommit(), $comment(), $storage());
    }

    @Override
    public final QOM.CreateTable $select(Select<?> newValue) {
        return $constructor().apply($table(), $temporary(), $ifNotExists(), $tableElements(), newValue, $withData(), $onCommit(), $comment(), $storage());
    }

    @Override
    public final QOM.CreateTable $withData(WithOrWithoutData newValue) {
        return $constructor().apply($table(), $temporary(), $ifNotExists(), $tableElements(), $select(), newValue, $onCommit(), $comment(), $storage());
    }

    @Override
    public final QOM.CreateTable $onCommit(TableCommitAction newValue) {
        return $constructor().apply($table(), $temporary(), $ifNotExists(), $tableElements(), $select(), $withData(), newValue, $comment(), $storage());
    }

    @Override
    public final QOM.CreateTable $comment(Comment newValue) {
        return $constructor().apply($table(), $temporary(), $ifNotExists(), $tableElements(), $select(), $withData(), $onCommit(), newValue, $storage());
    }

    @Override
    public final QOM.CreateTable $storage(SQL newValue) {
        return $constructor().apply($table(), $temporary(), $ifNotExists(), $tableElements(), $select(), $withData(), $onCommit(), $comment(), newValue);
    }

    public final Function9<? super Table<?>, ? super Boolean, ? super Boolean, ? super Collection<? extends TableElement>, ? super Select<?>, ? super WithOrWithoutData, ? super TableCommitAction, ? super Comment, ? super SQL, ? extends QOM.CreateTable> $constructor() {
        return (a1, a2, a3, a4, a5, a6, a7, a8, a9) -> new CreateTableImpl(configuration(), a1, a2, a3, (Collection<? extends TableElement>) a4, a5, a6, a7, a8, a9);
    }

































}
