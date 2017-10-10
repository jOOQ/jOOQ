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

import static org.jooq.Clause.CREATE_TABLE;
import static org.jooq.Clause.CREATE_TABLE_AS;
import static org.jooq.Clause.CREATE_TABLE_COLUMNS;
import static org.jooq.Clause.CREATE_TABLE_CONSTRAINTS;
import static org.jooq.Clause.CREATE_TABLE_NAME;
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.insertInto;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_CREATE;
import static org.jooq.impl.Keywords.K_GLOBAL_TEMPORARY;
import static org.jooq.impl.Keywords.K_IF_NOT_EXISTS;
import static org.jooq.impl.Keywords.K_ON_COMMIT_DELETE_ROWS;
import static org.jooq.impl.Keywords.K_ON_COMMIT_DROP;
import static org.jooq.impl.Keywords.K_ON_COMMIT_PRESERVE_ROWS;
import static org.jooq.impl.Keywords.K_TABLE;
import static org.jooq.impl.Keywords.K_TEMPORARY;
import static org.jooq.impl.Keywords.K_WITH_DATA;
import static org.jooq.impl.Keywords.K_WITH_NO_DATA;
import static org.jooq.impl.Tools.begin;
import static org.jooq.impl.Tools.beginExecuteImmediate;
import static org.jooq.impl.Tools.end;
import static org.jooq.impl.Tools.endExecuteImmediate;
import static org.jooq.impl.Tools.DataKey.DATA_SELECT_INTO_TABLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.Context;
import org.jooq.CreateTableAsStep;
import org.jooq.CreateTableColumnStep;
import org.jooq.CreateTableConstraintStep;
import org.jooq.CreateTableFinalStep;
import org.jooq.CreateTableOnCommitStep;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class CreateTableImpl<R extends Record> extends AbstractQuery implements

    // Cascading interface implementations for CREATE TABLE behaviour
    CreateTableAsStep<R>,
    CreateTableColumnStep {

    /**
     * Generated UID
     */
    private static final long                serialVersionUID               = 8904572826501186329L;
    private static final EnumSet<SQLDialect> NO_SUPPORT_IF_NOT_EXISTS       = EnumSet.of(DERBY, FIREBIRD);
    private static final EnumSet<SQLDialect> WRAP_SELECT_IN_PARENS          = EnumSet.of(HSQLDB);
    private static final EnumSet<SQLDialect> SUPPORT_TEMPORARY              = EnumSet.of(MARIADB, MYSQL, POSTGRES);




    private final Table<?>                   table;
    private Select<?>                        select;
    private final List<Field<?>>             columnFields;
    private final List<DataType<?>>          columnTypes;
    private final List<Constraint>           constraints;
    private final boolean                    temporary;
    private final boolean                    ifNotExists;
    private OnCommit                         onCommit;

    CreateTableImpl(Configuration configuration, Table<?> table, boolean temporary, boolean ifNotExists) {
        super(configuration);

        this.table = table;
        this.temporary = temporary;
        this.ifNotExists = ifNotExists;
        this.columnFields = new ArrayList<Field<?>>();
        this.columnTypes = new ArrayList<DataType<?>>();
        this.constraints = new ArrayList<Constraint>();
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final CreateTableOnCommitStep as(Select<? extends R> s) {
        this.select = s;
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final CreateTableColumnStep column(Field<?> field) {
        return column((Field) field, field.getDataType());
    }

    @Override
    public final CreateTableColumnStep columns(Field<?>... fields) {
        return columns(Arrays.asList(fields));
    }

    @Override
    public final CreateTableColumnStep columns(Collection<? extends Field<?>> fields) {
        for (Field<?> field : fields)
            column(field);

        return this;
    }

    @Override
    public final <T> CreateTableColumnStep column(Field<T> field, DataType<T> type) {
        columnFields.add(field);
        columnTypes.add(type);
        return this;
    }

    @Override
    public final CreateTableColumnStep column(Name field, DataType<?> type) {
        columnFields.add(field(field, type));
        columnTypes.add(type);
        return this;
    }

    @Override
    public final CreateTableColumnStep column(String field, DataType<?> type) {
        return column(name(field), type);
    }

    @Override
    public final CreateTableConstraintStep constraint(Constraint c) {
        return constraints(Arrays.asList(c));
    }

    @Override
    public final CreateTableConstraintStep constraints(Constraint... c) {
        return constraints(Arrays.asList(c));
    }

    @Override
    public final CreateTableConstraintStep constraints(Collection<? extends Constraint> c) {
        constraints.addAll(c);
        return this;
    }

    @Override
    public final CreateTableFinalStep onCommitDeleteRows() {
        onCommit = OnCommit.DELETE_ROWS;
        return this;
    }

    @Override
    public final CreateTableFinalStep onCommitPreserveRows() {
        onCommit = OnCommit.PRESERVE_ROWS;
        return this;
    }

    @Override
    public final CreateTableFinalStep onCommitDrop() {
        onCommit = OnCommit.DROP;
        return this;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx)) {
            Tools.beginTryCatch(ctx, DDLStatementType.CREATE_TABLE);
            accept0(ctx);
            Tools.endTryCatch(ctx, DDLStatementType.CREATE_TABLE);
        }
        else {
            accept0(ctx);
        }
    }


    private final void accept0(Context<?> ctx) {
        if (select != null) {








            acceptCreateTableAsSelect(ctx);
        }
        else {
            ctx.start(CREATE_TABLE);
            toSQLCreateTableName(ctx);
            ctx.sql('(')
               .start(CREATE_TABLE_COLUMNS)
               .formatIndentStart()
               .formatNewLine();

            boolean qualify = ctx.qualify();
            ctx.qualify(false);

            for (int i = 0; i < columnFields.size(); i++) {
                DataType<?> type = columnTypes.get(i);

                ctx.visit(columnFields.get(i))
                   .sql(' ');
                Tools.toSQLDDLTypeDeclarationForAddition(ctx, type);

                if (i < columnFields.size() - 1)
                    ctx.sql(',').formatSeparator();
            }

            ctx.qualify(qualify);
            ctx.end(CREATE_TABLE_COLUMNS)
               .start(CREATE_TABLE_CONSTRAINTS);

            if (!constraints.isEmpty())
                for (Constraint constraint : constraints)
                    ctx.sql(',')
                       .formatSeparator()
                       .visit(constraint);

            ctx.end(CREATE_TABLE_CONSTRAINTS)
               .formatIndentEnd()
               .formatNewLine()
               .sql(')');

            toSQLOnCommit(ctx);
            ctx.end(CREATE_TABLE);
        }
    }

    private final void acceptCreateTableAsSelect(Context<?> ctx) {
        ctx.start(CREATE_TABLE);
        toSQLCreateTableName(ctx);
        toSQLOnCommit(ctx);
        ctx.formatSeparator()
           .visit(K_AS);

        if (WRAP_SELECT_IN_PARENS.contains(ctx.family()))
            ctx.sql(" (")
               .formatIndentStart()
               .formatNewLine();
        else
            ctx.formatSeparator();

        ctx.start(CREATE_TABLE_AS)
           .visit(select)
           .end(CREATE_TABLE_AS);

        if (WRAP_SELECT_IN_PARENS.contains(ctx.family())) {
            ctx.formatIndentEnd()
               .formatNewLine()
               .sql(')');

            if (ctx.family() == HSQLDB)
                ctx.sql(' ')
                   .visit(K_WITH_DATA);
        }

        ctx.end(CREATE_TABLE);
    }






































    private final void toSQLCreateTableName(Context<?> ctx) {
        ctx.start(CREATE_TABLE_NAME)
           .visit(K_CREATE)
           .sql(' ');

        if (temporary)
            if (SUPPORT_TEMPORARY.contains(ctx.family()))
                ctx.visit(K_TEMPORARY).sql(' ');
            else
                ctx.visit(K_GLOBAL_TEMPORARY).sql(' ');

        ctx.visit(K_TABLE)
           .sql(' ');

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.visit(K_IF_NOT_EXISTS)
               .sql(' ');

        ctx.visit(table)
           .end(CREATE_TABLE_NAME);
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

    private final void acceptSelectInto(Context<?> ctx) {
        ctx.data(DATA_SELECT_INTO_TABLE, table);
        ctx.visit(select);
        ctx.data().remove(DATA_SELECT_INTO_TABLE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    private enum OnCommit {
        DELETE_ROWS,
        PRESERVE_ROWS,
        DROP;
    }
}
