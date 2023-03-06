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
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
// ...
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
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.conf.SettingsTools.renderLocale;
import static org.jooq.impl.CommonTableExpressionList.markTopLevelCteAndAccept;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.Keywords.K_BEGIN;
import static org.jooq.impl.Keywords.K_BULK_COLLECT_INTO;
import static org.jooq.impl.Keywords.K_DECLARE;
import static org.jooq.impl.Keywords.K_END;
import static org.jooq.impl.Keywords.K_FOR;
import static org.jooq.impl.Keywords.K_FORALL;
import static org.jooq.impl.Keywords.K_FROM;
import static org.jooq.impl.Keywords.K_IN;
import static org.jooq.impl.Keywords.K_INTO;
import static org.jooq.impl.Keywords.K_OPEN;
import static org.jooq.impl.Keywords.K_OUTPUT;
import static org.jooq.impl.Keywords.K_RETURNING;
import static org.jooq.impl.Keywords.K_ROWCOUNT;
import static org.jooq.impl.Keywords.K_SELECT;
import static org.jooq.impl.Keywords.K_SQL;
import static org.jooq.impl.Keywords.K_TABLE;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.EMPTY_STRING;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.autoAlias;
import static org.jooq.impl.Tools.flattenCollection;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_EMULATE_BULK_INSERT_RETURNING;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_UNALIAS_ALIASED_EXPRESSIONS;
import static org.jooq.impl.Tools.DataKey.DATA_DML_TARGET_TABLE;
import static org.jooq.impl.Tools.DataKey.DATA_TOP_LEVEL_CTE;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.util.sqlite.SQLiteDSL.rowid;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.jooq.Asterisk;
import org.jooq.Binding;
import org.jooq.CommonTableExpression;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DMLQuery;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Delete;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Insert;
import org.jooq.Name;
import org.jooq.Param;
// ...
import org.jooq.QualifiedAsterisk;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.RowId;
import org.jooq.SQLDialect;
import org.jooq.SchemaMapping;
import org.jooq.Scope;
import org.jooq.Select;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.Update;
import org.jooq.conf.ExecuteWithoutWhere;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.MappedTable;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultUnwrapperProvider.DefaultUnwrapper;
import org.jooq.impl.QOM.ResultOption;
import org.jooq.impl.Tools.BooleanDataKey;
import org.jooq.impl.Tools.DataKey;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.BatchedPreparedStatement;
import org.jooq.tools.jdbc.JDBCUtils;


/**
 * @author Lukas Eder
 */
abstract class AbstractDMLQuery<R extends Record> extends AbstractRowCountQuery implements DMLQuery<R> {

    private static final JooqLogger              log                                    = JooqLogger.getLogger(AbstractQuery.class);

    private static final Set<SQLDialect>         NO_SUPPORT_INSERT_ALIASED_TABLE        = SQLDialect.supportedBy(DERBY, FIREBIRD, H2, MARIADB, MYSQL);
    private static final Set<SQLDialect>         NATIVE_SUPPORT_INSERT_RETURNING        = SQLDialect.supportedBy(FIREBIRD, MARIADB, POSTGRES, YUGABYTEDB);
    private static final Set<SQLDialect>         NATIVE_SUPPORT_UPDATE_RETURNING        = SQLDialect.supportedBy(FIREBIRD, POSTGRES, YUGABYTEDB);
    private static final Set<SQLDialect>         NATIVE_SUPPORT_DELETE_RETURNING        = SQLDialect.supportedBy(FIREBIRD, MARIADB, POSTGRES, YUGABYTEDB);
    private static final Set<SQLDialect>         NATIVE_SUPPORT_DATA_CHANGE_DELTA_TABLE = SQLDialect.supportedBy(H2);
    private static final Set<SQLDialect>         NO_SUPPORT_FETCHING_KEYS               = SQLDialect.supportedBy(IGNITE);










    private final WithImpl                       with;
    private final Table<R>                       table;
    final SelectFieldList<SelectFieldOrAsterisk> returning;
    final List<Field<?>>                         returningResolvedAsterisks;
    Result<Record>                               returnedResult;
    Result<R>                                    returned;

    AbstractDMLQuery(Configuration configuration, WithImpl with, Table<R> table) {
        super(configuration);

        this.with = with;
        this.table = table;
        this.returning = new SelectFieldList<>();
        this.returningResolvedAsterisks = new ArrayList<>();
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    // @Override
    public final void setReturning() {
        setReturning(table.fields());
    }

    // @Override
    public final void setReturning(Identity<R, ?> identity) {
        if (identity != null)
            setReturning(identity.getField());
    }

    // @Override
    public final void setReturning(SelectFieldOrAsterisk... fields) {
        setReturning(Arrays.asList(fields));
    }

    // @Override
    public final void setReturning(Collection<? extends SelectFieldOrAsterisk> fields) {
        returning.clear();
        returning.addAll(fields.isEmpty() ? Arrays.asList(table.fields()) : fields);

        returningResolvedAsterisks.clear();
        for (SelectFieldOrAsterisk s : returning)
            if (s instanceof Field)
                returningResolvedAsterisks.add((Field<?>) s);
            else if (s instanceof QualifiedAsterisk)
                returningResolvedAsterisks.addAll(Arrays.asList(((QualifiedAsterisk) s).qualifier().fields()));
            else if (s instanceof Asterisk)
                returningResolvedAsterisks.addAll(Arrays.asList(table.fields()));
            else if (s instanceof Row)
                returningResolvedAsterisks.add(new RowField<>((Row) s));
            else
                throw new UnsupportedOperationException("Type not supported: " + s);
    }

    // @Override
    public final R getReturnedRecord() {
        if (getReturnedRecords().isEmpty())
            return null;

        return getReturnedRecords().get(0);
    }

    // @Override
    @SuppressWarnings("unchecked")
    public final Result<R> getReturnedRecords() {
        if (returned == null) {

            // [#3682] Plain SQL tables do not have any fields
            if (table.fields().length > 0) {

                // [#7479] [#7475] Warn users about potential API misuse
                warnOnAPIMisuse();
                returned = getResult().into(table);
            }
            else {
                returned = (Result<R>) getResult();
            }
        }

        return returned;
    }

    private final void warnOnAPIMisuse() {
        for (Field<?> field : getResult().fields())
            if (table.field(field) == null)
                log.warn("API misuse", "Column " + field + " has been requested through the returning() clause, which is not present in table " + table + ". Use StoreQuery.getResult() or the returningResult() clause instead.");
    }

    final Table<R> table() {
        return table;
    }

    final Table<?> table(Context<?> ctx) {



























        // [#8382] [#8384] Table might be aliased and dialect doesn't like that
        if (NO_SUPPORT_INSERT_ALIASED_TABLE.contains(ctx.dialect()) && this instanceof Insert)
            return defaultIfNull(Tools.aliased(table()), table());
        else
            return table();
    }

    // @Override
    public final Result<?> getResult() {
        if (returnedResult == null)
            returnedResult = new ResultImpl<>(configuration(), returningResolvedAsterisks);

        return returnedResult;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        WithImpl w = with;

        ctx.scopeStart()
           .data(DATA_DML_TARGET_TABLE, table);





















        if (w != null)
            ctx.visit(w);
        else
            markTopLevelCteAndAccept(ctx, c -> {});

        boolean previousDeclareFields = ctx.declareFields();

        if (NATIVE_SUPPORT_DATA_CHANGE_DELTA_TABLE.contains(ctx.dialect())
                && !returning.isEmpty()
                && !TRUE.equals(ctx.data(BooleanDataKey.DATA_RENDERING_DATA_CHANGE_DELTA_TABLE))) {
            ctx.data(BooleanDataKey.DATA_RENDERING_DATA_CHANGE_DELTA_TABLE,
                true,
                c -> c.visit(select(returning).from(
                    new DataChangeDeltaTable<>(this instanceof Delete ? ResultOption.OLD : ResultOption.FINAL, this).as(table().getUnqualifiedName())
                ))
            );
        }







































































































































































































































































        else
            accept0(ctx);






        ctx.data().remove(DATA_DML_TARGET_TABLE);
        ctx.scopeEnd();
    }















































































    abstract void accept0(Context<?> ctx);

    /**
     * [#6771] Handle the case where a statement is executed without a WHERE clause.
     */
    void executeWithoutWhere(String message, ExecuteWithoutWhere executeWithoutWhere) {
        switch (executeWithoutWhere) {
            case IGNORE:
                break;
            case LOG_DEBUG:
                if (log.isDebugEnabled())
                    log.debug(message, "A statement is executed without WHERE clause");
                break;
            case LOG_INFO:
                if (log.isInfoEnabled())
                    log.info(message, "A statement is executed without WHERE clause");
                break;
            case LOG_WARN:
                log.warn(message, "A statement is executed without WHERE clause");
                break;
            case THROW:
                throw new DataAccessException("A statement is executed without WHERE clause");
        }
    }























































    final void toSQLReturning(Context<?> ctx) {
        if (!returning.isEmpty()) {
            // Other dialects don't render a RETURNING clause, but
            // use JDBC's Statement.RETURN_GENERATED_KEYS mode instead

            if (nativeSupportReturning(ctx)) {
                boolean qualify = ctx.qualify();
                boolean unqualify = ctx.family() == MARIADB;

                if (unqualify)
                    ctx.qualify(false);

                ctx.formatSeparator()
                   .visit(K_RETURNING)
                   .sql(' ')
                   .declareFields(true, c -> c.visit(

                       // Firebird doesn't support asterisks at all here
                       // MariaDB doesn't support qualified asterisks: https://jira.mariadb.org/browse/MDEV-23178
                       c.family() == FIREBIRD || c.family() == MARIADB
                     ? new SelectFieldList<>(returningResolvedAsterisks)
                     : returning
                   ));

                if (unqualify)
                    ctx.qualify(qualify);
            }
        }
    }

    final boolean nativeSupportReturning(Scope ctx) {
        return this instanceof Insert && NATIVE_SUPPORT_INSERT_RETURNING.contains(ctx.dialect())
            || this instanceof Update && NATIVE_SUPPORT_UPDATE_RETURNING.contains(ctx.dialect())
            || this instanceof Delete && NATIVE_SUPPORT_DELETE_RETURNING.contains(ctx.dialect());
    }

    final boolean nativeSupportReturningOrDataChangeDeltaTable(Scope ctx) {
        return NATIVE_SUPPORT_DATA_CHANGE_DELTA_TABLE.contains(ctx.dialect()) || nativeSupportReturning(ctx);
    }

    @Override
    protected final void prepare(ExecuteContext ctx) throws SQLException {
        prepare0(ctx);
        Tools.setFetchSize(ctx, 0);
    }

    private final void prepare0(ExecuteContext ctx) throws SQLException {
        Connection connection = ctx.connection();

        // Normal statement preparing if no values should be returned
        if (returning.isEmpty())
            super.prepare(ctx);

        // Column stores and other NoSQL/NewSQL DBMS don't seem support fetching generated keys
        else if (NO_SUPPORT_FETCHING_KEYS.contains(ctx.dialect()))
            super.prepare(ctx);

        else if (nativeSupportReturningOrDataChangeDeltaTable(ctx))
            super.prepare(ctx);










        else {
            switch (ctx.family()) {







                // SQLite will select last_insert_rowid() after the INSER
                case SQLITE:
                case CUBRID:
                    super.prepare(ctx);
                    break;

                // Some dialects can only return AUTO_INCREMENT values
                // Other values have to be fetched in a second step
                // [#1260] TODO CUBRID supports this, but there's a JDBC bug








                case DERBY:

                // [#9212] Older MariaDB versions that don't support RETURNING
                //         yet, or UPDATE .. RETURNING
                case MARIADB:
                case MYSQL:
                    if (ctx.statement() == null)
                        ctx.statement(connection.prepareStatement(ctx.sql(), Statement.RETURN_GENERATED_KEYS));
                    break;

                // The default is to return all requested fields directly













                case HSQLDB:
                default: {
                    if (ctx.statement() == null) {
                        RenderNameCase style = SettingsTools.getRenderNameCase(configuration().settings());

                        // [#2845] Field names should be passed to JDBC in the case
                        // imposed by the user. For instance, if the user uses
                        // PostgreSQL generated case-insensitive Fields (default to lower case)
                        // and wants to query HSQLDB (default to upper case), they may choose
                        // to overwrite casing using RenderNameCase.
                        ctx.statement(connection.prepareStatement(ctx.sql(), map(flattenCollection(returningResolvedAsterisks, false, true),
                              style == RenderNameCase.UPPER
                            ? f -> f.getName().toUpperCase(renderLocale(configuration().settings()))
                            : style == RenderNameCase.LOWER
                            ? f -> f.getName().toLowerCase(renderLocale(configuration().settings()))
                            : f -> f.getName()
                        ).toArray(EMPTY_STRING)));
                    }

                    break;
                }
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected final int execute(ExecuteContext ctx, ExecuteListener listener) throws SQLException {
        returned = null;
        returnedResult = null;

        if (returning.isEmpty()) {
            return super.execute(ctx, listener);
        }
        // Column stores don't seem support fetching generated keys
        else if (NO_SUPPORT_FETCHING_KEYS.contains(ctx.dialect())) {
            return super.execute(ctx, listener);
        }
        else {
            int result = 1;
            ResultSet rs;
            switch (ctx.family()) {

                // SQLite can select _rowid_ after the insert
                case SQLITE: {
                    listener.executeStart(ctx);
                    result = executeImmediate(ctx.statement()).executeUpdate();
                    ctx.rows(result);
                    listener.executeEnd(ctx);

                    DSLContext create = ctx.dsl();
                    returnedResult =
                    create.select(returning)
                          .from(table)
                          .where(rowid().eq(DSL.field("last_insert_rowid()", rowid().getDataType())))
                          .fetch();

                    returnedResult.attach(((DefaultExecuteContext) ctx).originalConfiguration());
                    return result;
                }











                case CUBRID: {
                    listener.executeStart(ctx);
                    result = executeImmediate(ctx.statement()).executeUpdate();
                    ctx.rows(result);
                    listener.executeEnd(ctx);

                    selectReturning(
                        ((DefaultExecuteContext) ctx).originalConfiguration(),
                        ctx.configuration(),
                        ctx.dsl().lastID()
                    );

                    return result;
                }
















                // Some dialects can only retrieve "identity" (AUTO_INCREMENT) values
                // Additional values have to be fetched explicitly
                // [#1260] TODO CUBRID supports this, but there's a JDBC bug






                case DERBY:
                case MYSQL: {
                    return executeReturningGeneratedKeysFetchAdditionalRows(ctx, listener);
                }

                case MARIADB: {
                    if (!nativeSupportReturning(ctx))
                        return executeReturningGeneratedKeysFetchAdditionalRows(ctx, listener);

                    rs = executeReturningQuery(ctx, listener);
                    break;
                }

                // [#9676] H2 2.0.202 has introduced support for data change delta tables
                case H2:





                    rs = executeReturningQuery(ctx, listener);
                    break;







                // Firebird and Postgres can execute the INSERT .. RETURNING
                // clause like a select clause. JDBC support is not implemented
                // in the Postgres JDBC driver
                case FIREBIRD:
                case POSTGRES:
                case YUGABYTEDB: {
                    rs = executeReturningQuery(ctx, listener);
                    break;
                }

                // These dialects have full JDBC support















































































                case HSQLDB:
                default: {
                    rs = executeReturningGeneratedKeys(ctx, listener);
                    break;
                }
            }

            ExecuteContext ctx2 = new DefaultExecuteContext(((DefaultExecuteContext) ctx).originalConfiguration());
            ExecuteListener listener2 = ExecuteListeners.getAndStart(ctx2);

            ctx2.resultSet(rs);
            returnedResult = new CursorImpl<>(ctx2, listener2, returningResolvedAsterisks.toArray(EMPTY_FIELD), null, false, true).fetch();

            // [#5366] HSQLDB currently doesn't support fetching updated records in UPDATE statements.
            // [#5408] Other dialects may fall through the switch above (PostgreSQL, Firebird, Oracle) and must
            //         execute this logic
            if (!returnedResult.isEmpty() || ctx.family() != HSQLDB) {
                result = returnedResult.size();
                ctx.rows(result);
            }

            return result;
        }
    }

    /**
     * Make sure a {@link PreparedStatement}, which may be a
     * {@link BatchedPreparedStatement}, is executed immediately, not batched.
     */
    private final PreparedStatement executeImmediate(PreparedStatement s) throws SQLException {
        if (DefaultUnwrapper.isWrapperFor(s, BatchedPreparedStatement.class))
            s.unwrap(BatchedPreparedStatement.class).setExecuteImmediate(true);

        return s;
    }

    private final ResultSet executeReturningGeneratedKeys(ExecuteContext ctx, ExecuteListener listener) throws SQLException {
        listener.executeStart(ctx);
        int result = executeImmediate(ctx.statement()).executeUpdate();
        ctx.rows(result);
        listener.executeEnd(ctx);

        return ctx.statement().getGeneratedKeys();
    }

    private final int executeReturningGeneratedKeysFetchAdditionalRows(ExecuteContext ctx, ExecuteListener listener) throws SQLException {
        ResultSet rs;

        listener.executeStart(ctx);
        int result = executeImmediate(ctx.statement()).executeUpdate();
        ctx.rows(result);
        listener.executeEnd(ctx);

        try {
            rs = ctx.statement().getGeneratedKeys();
        }
        catch (SQLException e) {







            throw e;
        }

        try {
            List<Object> list = new ArrayList<>();

            // Some JDBC drivers seem to illegally return null
            // from getGeneratedKeys() sometimes
            if (rs != null)
                while (rs.next())
                    list.add(rs.getObject(1));

            selectReturning(
                ((DefaultExecuteContext) ctx).originalConfiguration(),
                ctx.configuration(),
                list.toArray()
            );

            return result;
        }
        finally {
            JDBCUtils.safeClose(rs);
        }
    }

    private final ResultSet executeReturningQuery(ExecuteContext ctx, ExecuteListener listener) throws SQLException {
        listener.executeStart(ctx);
        ResultSet rs = ctx.statement().executeQuery();
        listener.executeEnd(ctx);

        return rs;
    }

    /**
     * Get the returning record in those dialects that do not support fetching
     * arbitrary fields from JDBC's {@link Statement#getGeneratedKeys()} method.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final void selectReturning(
        Configuration originalConfiguration,
        Configuration derivedConfiguration,
        Object... values
    ) {
        if (values != null && values.length > 0) {
            Field<Object> returnIdentity = (Field<Object>) returnedIdentity();

            if (returnIdentity != null) {
                DataType<Object> type = returnIdentity.getDataType();
                Object[] ids = map(values, v -> type.convert(v), Object[]::new);

                // Only the IDENTITY value was requested. No need for an
                // additional query
                if (returningResolvedAsterisks.size() == 1 && new FieldsImpl<>(returningResolvedAsterisks).field(returnIdentity) != null) {
                    AbstractRow<? extends AbstractRecord> fields = (AbstractRow<AbstractRecord>) Tools.row0(returningResolvedAsterisks.toArray(EMPTY_FIELD));

                    for (final Object id : ids) {
                        ((Result) getResult()).add(
                        Tools.newRecord(
                                true,
                                AbstractRecord.class,
                                fields,
                                originalConfiguration)
                             .operate(record -> {
                                record.values[0] = id;
                                record.originals[0] = id;

                                return record;
                            }));
                    }
                }

                // Other values are requested, too. Run another query
                else {
                    returnedResult =
                    derivedConfiguration.dsl()
                                        .select(returning)
                                        .from(table)

                                        // [#5050] [#9946] Table.getIdentity() doesn't produce aliased fields yet
                                        .where(table.field(returnIdentity).in(ids))
                                        .fetch();

                    returnedResult.attach(originalConfiguration);
                }
            }
        }
    }

    private final Field<?> returnedIdentity() {
        return table.getIdentity() != null
            ? table.getIdentity().getField()
            : Tools.findAny(returningResolvedAsterisks, f -> f.getDataType().identity());
    }

    public final Field<?>[] getFields(ResultSetMetaData rs) throws SQLException {
        return returningResolvedAsterisks.toArray(EMPTY_FIELD);
    }

    public final Class<? extends Record> getRecordType() {
        return Tools.recordType(returningResolvedAsterisks.size());
    }
}
