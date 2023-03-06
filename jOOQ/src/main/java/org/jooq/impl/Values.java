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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import static org.jooq.Clause.TABLE_VALUES;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.Keywords.K_MULTISET;
import static org.jooq.impl.Keywords.K_ROW;
import static org.jooq.impl.Keywords.K_STRUCT;
import static org.jooq.impl.Keywords.K_TABLE;
import static org.jooq.impl.Keywords.K_UNNEST;
import static org.jooq.impl.Keywords.K_VALUES;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.SubqueryCharacteristics.DERIVED_TABLE;
import static org.jooq.impl.Tools.EMPTY_ROW;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.isEmpty;
import static org.jooq.impl.Tools.isVal;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.visitSubquery;

import java.util.Set;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.conf.ParamType;
import org.jooq.impl.QOM.UnmodifiableList;


/**
 * An implementation for the <code>VALUES(…)</code> table constructor
 *
 * @author Lukas Eder
 */
final class Values<R extends Record>
extends
    AbstractAutoAliasTable<R>
implements
    QOM.Values<R>
{

    static final Set<SQLDialect>    NO_SUPPORT_VALUES              = SQLDialect.supportedUntil(FIREBIRD, MARIADB);
    static final Set<SQLDialect>    REQUIRE_ROWTYPE_CAST           = SQLDialect.supportedBy(DERBY, FIREBIRD);
    static final Set<SQLDialect>    REQUIRE_ROWTYPE_CAST_FIRST_ROW = SQLDialect.supportedBy(POSTGRES);
    static final Set<SQLDialect>    NO_SUPPORT_PARENTHESES         = SQLDialect.supportedBy();

    private final Row[]             rows;
    private transient DataType<?>[] types;

    Values(Row[] rows) {
        this(rows, name("v"), fieldAliases(degree(rows)));
    }

    Values(Row[] rows, Name alias, Name[] fieldAliases) {
        super(alias, fieldAliases);

        this.rows = assertNotEmpty(rows);
    }

    private static Name[] fieldAliases(int degree) {
        Name[] result = new Name[degree];

        for (int i = 0; i < result.length; i++)
            result[i] = name("c" + (i + 1));

        return result;
    }

    private static final int degree(Row[] rows) {
        return isEmpty(rows) ? 0 : rows[0].size();
    }

    static final Row[] assertNotEmpty(Row[] rows) {
        if (isEmpty(rows))
            throw new IllegalArgumentException("Cannot create a VALUES() constructor with an empty set of rows");

        return rows;
    }

    @Override
    final Values<R> construct(Name newAlias, Name[] newFieldAliases) {
        return new Values<R>(rows, newAlias, newFieldAliases);
    }

    // -------------------------------------------------------------------------
    // XXX: Table API
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Class<? extends R> getRecordType() {
        // TODO: [#4695] Calculate the correct Record[B] type
        return (Class<? extends R>) RecordImplN.class;
    }

    @Override
    final FieldsImpl<R> fields0() {
        return new FieldsImpl<>(map(fieldAliases, (n, i) -> DSL.field(alias.append(n), rows[0].dataType(i))));
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    private final DataType<?>[] rowType() {
        if (types == null) {
            types = new DataType[rows[0].size()];

            typeLoop:
            for (int i = 0; i < types.length; i++) {
                types[i] = rows[0].dataType(i);

                if (types[i].getType() == Object.class) {
                    for (int j = 1; j < rows.length; j++) {
                        DataType<?> type = rows[j].dataType(i);

                        if (type.getType() != Object.class) {
                            types[i] = type;
                            continue typeLoop;
                        }
                    }
                }
            }
        }

        return types;
    }

    private final Field<?>[] castToRowType(Field<?>[] fields) {
        Field<?>[] result = new Field[fields.length];

        for (int i = 0; i < result.length; i++) {
            DataType<?> type = rowType()[i];
            result[i] = fields[i].getDataType().equals(type) ? fields[i] : fields[i].cast(type);
        }

        return result;
    }

    private final Row castNullLiteralToRowType(Context<?> ctx, Row row) {
        if (anyMatch(row.fields(), f -> rendersNullLiteral(ctx, f))) {
            Field<?>[] result = new Field[row.size()];

            for (int i = 0; i < result.length; i++)
                if (rendersNullLiteral(ctx, row.field(i)) && rowType()[i].getType() != Object.class)
                    result[i] = row.field(i).cast(rowType()[i]);
                else
                    result[i] = row.field(i);

            return row(result);
        }
        else
            return row;
    }

    private final boolean rendersNullLiteral(Context<?> ctx, Field<?> field) {
        return isVal(field) && ((Val<?>) field).getValue() == null && ((Val<?>) field).isInline(ctx)
            || field instanceof NullCondition;
    }

    @Override
    public final void accept(Context<?> ctx) {

        // [#915] Emulate VALUES(..) with SELECT .. UNION ALL SELECT ..
        // for those dialects that do not support a VALUES() constructor
        if (NO_SUPPORT_VALUES.contains(ctx.dialect())) {
            Select<Record> selects = null;
            boolean cast = REQUIRE_ROWTYPE_CAST.contains(ctx.dialect());

            for (Row row : rows) {
                Select<Record> select = DSL.select(cast ? castToRowType(row.fields()) : row.fields());

                if (selects == null)
                    selects = select;
                else
                    selects = selects.unionAll(select);
            }

            visitSubquery(ctx, selects, DERIVED_TABLE, true);
        }







        // [#915] Native support of VALUES(..)
        else {
            ctx.start(TABLE_VALUES);

            if (!NO_SUPPORT_PARENTHESES.contains(ctx.dialect()))
                ctx.sqlIndentStart('(');















            ctx.visit(K_VALUES);

            if (rows.length > 1)
                ctx.formatIndentStart()
                   .formatSeparator();
            else
                ctx.sql(' ');

            for (int i = 0; i < rows.length; i++) {
                if (i > 0)
                    ctx.sql(',')
                       .formatSeparator();

                if (ctx.family() == MYSQL)
                    ctx.visit(K_ROW).sql(" ");





                // [#11015] NULL literals of known type should be cast in PostgreSQL in the first row
                if (i == 0 && REQUIRE_ROWTYPE_CAST_FIRST_ROW.contains(ctx.dialect()))
                    ctx.visit(castNullLiteralToRowType(ctx, rows[i]));

                // [#11015] Or in Derby in any other row, too
                else if (REQUIRE_ROWTYPE_CAST.contains(ctx.dialect()))
                    ctx.visit(castNullLiteralToRowType(ctx, rows[i]));
                else
                    ctx.visit(rows[i]);
            }

            if (rows.length > 1)
                ctx.formatIndentEnd()
                   .formatNewLine();











            if (!NO_SUPPORT_PARENTHESES.contains(ctx.dialect()))
                ctx.sqlIndentEnd(')');

            ctx.end(TABLE_VALUES);
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Function1<? super UnmodifiableList<? extends Row>, ? extends QOM.Values<R>> $constructor() {
        return r -> new Values<>(r.toArray(EMPTY_ROW));
    }

    @Override
    public final UnmodifiableList<? extends Row> $arg1() {
        return QOM.unmodifiable(rows);
    }
}
