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
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.Tools.DataKey.DATA_SELECT_INTO_TABLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    private static final long       serialVersionUID = 8904572826501186329L;

    private final Table<?>          table;
    private Select<?>               select;
    private final List<Field<?>>    columnFields;
    private final List<DataType<?>> columnTypes;
    private final List<Constraint>  constraints;
    private final boolean           temporary;
    private final boolean           ifNotExists;
    private OnCommit                onCommit;


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
        return !asList(DERBY, FIREBIRD).contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx)) {
            Tools.executeImmediateBegin(ctx, DDLStatementType.CREATE_TABLE);
            accept0(ctx);
            Tools.executeImmediateEnd(ctx, DDLStatementType.CREATE_TABLE);
        }
        else {
            accept0(ctx);
        }
    }


    private final void accept0(Context<?> ctx) {
        if (select != null) {







            {
                acceptCreateTableAsSelect(ctx);
            }
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
                Tools.toSQLDDLTypeDeclaration(ctx, type);

                // [#5356] Some dialects require the DEFAULT clause prior to the
                //         NULL constraints clause
                if (asList(HSQLDB).contains(ctx.family()))
                    acceptDefault(ctx, type);

                if (type.nullable()) {

                    // [#4321] Not all dialects support explicit NULL type declarations
                    if (!asList(DERBY, FIREBIRD).contains(ctx.family()))
                        ctx.sql(' ').keyword("null");
                }
                else {
                    ctx.sql(' ').keyword("not null");
                }

                if (type.identity()) {

                    // [#5062] H2's (and others') AUTO_INCREMENT flag is syntactically located *after* NULL flags.
                    switch (ctx.family()) {





                        case H2:
                        case MARIADB:
                        case MYSQL:  ctx.sql(' ').keyword("auto_increment"); break;
                    }
                }

                if (!asList(HSQLDB).contains(ctx.family()))
                    acceptDefault(ctx, type);

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

    private void acceptDefault(Context<?> ctx, DataType<?> type) {
        if (type.defaulted())
            ctx.sql(' ').keyword("default").sql(' ').visit(type.defaultValue());
    }

    private final void acceptCreateTableAsSelect(Context<?> ctx) {
        ctx.start(CREATE_TABLE);
        toSQLCreateTableName(ctx);
        toSQLOnCommit(ctx);
        ctx.formatSeparator()
           .keyword("as");

        if (asList(HSQLDB).contains(ctx.family())) {
            ctx.sql(" (")
               .formatIndentStart()
               .formatNewLine();
        }
        else {
            ctx.formatSeparator();
        }

        ctx.start(CREATE_TABLE_AS)
           .visit(select)
           .end(CREATE_TABLE_AS);

        if (asList(HSQLDB).contains(ctx.family())) {
            ctx.formatIndentEnd()
               .formatNewLine()
               .sql(')');

            if (ctx.family() == HSQLDB)
                ctx.sql(' ')
                   .keyword("with data");
        }

        ctx.end(CREATE_TABLE);
    }

    private final void toSQLCreateTableName(Context<?> ctx) {
        ctx.start(CREATE_TABLE_NAME)
           .keyword("create")
           .sql(' ');

        if (temporary)
            if (asList(MARIADB, MYSQL, POSTGRES).contains(ctx.family()))
                ctx.keyword("temporary").sql(' ');
            else
                ctx.keyword("global temporary").sql(' ');

        ctx.keyword("table")
           .sql(' ');

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.keyword("if not exists")
               .sql(' ');

        ctx.visit(table)
           .end(CREATE_TABLE_NAME);
    }

    private final void toSQLOnCommit(Context<?> ctx) {
        if (temporary && onCommit != null) {
            switch (onCommit) {
                case DELETE_ROWS:   ctx.formatSeparator().keyword("on commit delete rows");   break;
                case PRESERVE_ROWS: ctx.formatSeparator().keyword("on commit preserve rows"); break;
                case DROP:          ctx.formatSeparator().keyword("on commit drop");          break;
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
