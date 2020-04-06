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

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.conf.ParamType.NAMED_OR_INLINED;
import static org.jooq.impl.DSL.catalog;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.index;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.schema;
import static org.jooq.impl.DSL.sequence;
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.zero;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.EMPTY_QUERY;
import static org.jooq.impl.Tools.EMPTY_TABLE;
import static org.jooq.impl.Tools.EMPTY_TABLE_RECORD;
import static org.jooq.impl.Tools.EMPTY_UPDATABLE_RECORD;
import static org.jooq.impl.Tools.blocking;
import static org.jooq.impl.Tools.list;
import static org.jooq.tools.Convert.convert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jooq.AlterIndexOnStep;
import org.jooq.AlterIndexStep;
import org.jooq.AlterSchemaStep;
import org.jooq.AlterSequenceStep;
import org.jooq.AlterTableStep;
import org.jooq.AlterTypeStep;
import org.jooq.AlterViewStep;
import org.jooq.Attachable;
import org.jooq.Batch;
import org.jooq.BatchBindStep;
import org.jooq.BindContext;
import org.jooq.Block;
import org.jooq.Catalog;
import org.jooq.CommentOnIsStep;
import org.jooq.CommonTableExpression;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.ConnectionCallable;
import org.jooq.ConnectionProvider;
import org.jooq.ConnectionRunnable;
import org.jooq.ContextTransactionalCallable;
import org.jooq.ContextTransactionalRunnable;
import org.jooq.CreateIndexStep;
import org.jooq.CreateSchemaFinalStep;
import org.jooq.CreateSequenceFlagsStep;
import org.jooq.CreateTableColumnStep;
import org.jooq.CreateTypeStep;
import org.jooq.CreateViewAsStep;
import org.jooq.Cursor;
import org.jooq.DDLExportConfiguration;
import org.jooq.DDLFlag;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.DeleteQuery;
import org.jooq.DeleteUsingStep;
import org.jooq.DropIndexOnStep;
import org.jooq.DropSchemaStep;
import org.jooq.DropSequenceFinalStep;
import org.jooq.DropTableStep;
import org.jooq.DropTypeStep;
import org.jooq.DropViewFinalStep;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Explain;
import org.jooq.Field;
import org.jooq.GrantOnStep;
import org.jooq.Index;
import org.jooq.InsertQuery;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStep1;
import org.jooq.InsertValuesStep10;
import org.jooq.InsertValuesStep11;
import org.jooq.InsertValuesStep12;
import org.jooq.InsertValuesStep13;
import org.jooq.InsertValuesStep14;
import org.jooq.InsertValuesStep15;
import org.jooq.InsertValuesStep16;
import org.jooq.InsertValuesStep17;
import org.jooq.InsertValuesStep18;
import org.jooq.InsertValuesStep19;
import org.jooq.InsertValuesStep2;
import org.jooq.InsertValuesStep20;
import org.jooq.InsertValuesStep21;
import org.jooq.InsertValuesStep22;
import org.jooq.InsertValuesStep3;
import org.jooq.InsertValuesStep4;
import org.jooq.InsertValuesStep5;
import org.jooq.InsertValuesStep6;
import org.jooq.InsertValuesStep7;
import org.jooq.InsertValuesStep8;
import org.jooq.InsertValuesStep9;
import org.jooq.InsertValuesStepN;
import org.jooq.LoaderOptionsStep;
import org.jooq.MergeKeyStep1;
import org.jooq.MergeKeyStep10;
import org.jooq.MergeKeyStep11;
import org.jooq.MergeKeyStep12;
import org.jooq.MergeKeyStep13;
import org.jooq.MergeKeyStep14;
import org.jooq.MergeKeyStep15;
import org.jooq.MergeKeyStep16;
import org.jooq.MergeKeyStep17;
import org.jooq.MergeKeyStep18;
import org.jooq.MergeKeyStep19;
import org.jooq.MergeKeyStep2;
import org.jooq.MergeKeyStep20;
import org.jooq.MergeKeyStep21;
import org.jooq.MergeKeyStep22;
import org.jooq.MergeKeyStep3;
import org.jooq.MergeKeyStep4;
import org.jooq.MergeKeyStep5;
import org.jooq.MergeKeyStep6;
import org.jooq.MergeKeyStep7;
import org.jooq.MergeKeyStep8;
import org.jooq.MergeKeyStep9;
import org.jooq.MergeKeyStepN;
import org.jooq.MergeUsingStep;
import org.jooq.Meta;
import org.jooq.Migration;
import org.jooq.Name;
import org.jooq.Param;
import org.jooq.Parser;
import org.jooq.Privilege;
// ...
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Record11;
import org.jooq.Record12;
import org.jooq.Record13;
import org.jooq.Record14;
import org.jooq.Record15;
import org.jooq.Record16;
import org.jooq.Record17;
import org.jooq.Record18;
import org.jooq.Record19;
import org.jooq.Record2;
import org.jooq.Record20;
import org.jooq.Record21;
import org.jooq.Record22;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record7;
import org.jooq.Record8;
import org.jooq.Record9;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Results;
import org.jooq.RevokeOnStep;
import org.jooq.RowCountQuery;
import org.jooq.SQL;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.SelectQuery;
import org.jooq.SelectSelectStep;
import org.jooq.SelectWhereStep;
import org.jooq.Sequence;
import org.jooq.Source;
import org.jooq.Statement;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableRecord;
import org.jooq.TransactionProvider;
import org.jooq.TransactionalCallable;
import org.jooq.TransactionalRunnable;
import org.jooq.TruncateIdentityStep;
import org.jooq.UDT;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.UpdateQuery;
import org.jooq.UpdateSetFirstStep;
import org.jooq.Version;
import org.jooq.WithAsStep;
import org.jooq.WithAsStep1;
import org.jooq.WithAsStep10;
import org.jooq.WithAsStep11;
import org.jooq.WithAsStep12;
import org.jooq.WithAsStep13;
import org.jooq.WithAsStep14;
import org.jooq.WithAsStep15;
import org.jooq.WithAsStep16;
import org.jooq.WithAsStep17;
import org.jooq.WithAsStep18;
import org.jooq.WithAsStep19;
import org.jooq.WithAsStep2;
import org.jooq.WithAsStep20;
import org.jooq.WithAsStep21;
import org.jooq.WithAsStep22;
import org.jooq.WithAsStep3;
import org.jooq.WithAsStep4;
import org.jooq.WithAsStep5;
import org.jooq.WithAsStep6;
import org.jooq.WithAsStep7;
import org.jooq.WithAsStep8;
import org.jooq.WithAsStep9;
import org.jooq.WithStep;
import org.jooq.conf.Settings;
import org.jooq.exception.ConfigurationException;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.InvalidResultException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.impl.BatchCRUD.Action;
import org.jooq.tools.csv.CSVReader;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.tools.jdbc.MockCallable;
import org.jooq.tools.jdbc.MockConfiguration;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockRunnable;
import org.jooq.util.xml.jaxb.InformationSchema;

/**
 * A default implementation for {@link DSLContext}.
 * <p>
 * You may use this as a base implementation for custom {@link DSLContext}
 * subtypes preventing potential API breakage when upgrading jOOQ, or to
 * delegate DSL method calls to your custom implementations.
 *
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DefaultDSLContext extends AbstractScope implements DSLContext, Serializable {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 2681360188806309513L;

    // -------------------------------------------------------------------------
    // XXX Constructors
    // -------------------------------------------------------------------------

    public DefaultDSLContext(SQLDialect dialect) {
        this(dialect, null);
    }

    public DefaultDSLContext(SQLDialect dialect, Settings settings) {
        this(new DefaultConfiguration(new NoConnectionProvider(), dialect, settings));
    }

    public DefaultDSLContext(Connection connection, SQLDialect dialect) {
        this(connection, dialect, null);
    }

    public DefaultDSLContext(Connection connection, SQLDialect dialect, Settings settings) {
        this(new DefaultConfiguration(new DefaultConnectionProvider(connection), dialect, settings));
    }

    public DefaultDSLContext(DataSource datasource, SQLDialect dialect) {
        this(datasource, dialect, null);
    }

    public DefaultDSLContext(DataSource datasource, SQLDialect dialect, Settings settings) {
        this(new DefaultConfiguration(new DataSourceConnectionProvider(datasource), dialect, settings));
    }

    public DefaultDSLContext(ConnectionProvider connectionProvider, SQLDialect dialect) {
        this(connectionProvider, dialect, null);
    }

    public DefaultDSLContext(ConnectionProvider connectionProvider, SQLDialect dialect, Settings settings) {
        this(new DefaultConfiguration(connectionProvider, dialect, settings));
    }

    public DefaultDSLContext(Configuration configuration) {
        super(configuration, configuration == null ? null : configuration.data());
    }

    // -------------------------------------------------------------------------
    // XXX AutoCloseable
    // -------------------------------------------------------------------------

    @Override
    public void close() {
        ConnectionProvider cp = configuration().connectionProvider();

        if (cp instanceof DefaultConnectionProvider) {
            DefaultConnectionProvider dcp = (DefaultConnectionProvider) cp;

            if (dcp.finalize) {
                JDBCUtils.safeClose(dcp.connection);
                dcp.connection = null;
            }
        }
    }

    // -------------------------------------------------------------------------
    // XXX Configuration API
    // -------------------------------------------------------------------------

    @Override
    public Schema map(Schema schema) {
        return Tools.getMappedSchema(configuration(), schema);
    }

    @Override
    public <R extends Record> Table<R> map(Table<R> table) {
        return Tools.getMappedTable(configuration(), table);
    }

    // -------------------------------------------------------------------------
    // XXX Convenience methods accessing the underlying Connection
    // -------------------------------------------------------------------------

    @Override
    public Parser parser() {
        return new ParserImpl(configuration());
    }

    @Override
    public Connection parsingConnection() {
        return new ParsingConnection(configuration());
    }

    @Override
    public DataSource parsingDataSource() {
        return new ParsingDataSource(configuration());
    }

    @Override
    public Connection diagnosticsConnection() {
        return new DiagnosticsConnection(configuration());
    }

    @Override
    public DataSource diagnosticsDataSource() {
        return new DiagnosticsDataSource(configuration());
    }

    @Override
    public Version version(String id) {
        return new VersionImpl(this, id, null, new Version[0]);
    }

    @Override
    public Migration migrateTo(Version to) {
        return new MigrationImpl(configuration, to);
    }

    @Override
    public Meta meta() {
        return configuration().metaProvider().provide();
    }

    @Override
    public Meta meta(DatabaseMetaData meta) {
        return new MetaImpl(configuration(), meta);
    }

    @Override
    public Meta meta(Catalog... catalogs) {
        return new CatalogMetaImpl(configuration(), catalogs);
    }

    @Override
    public Meta meta(Schema... schemas) {
        return new SchemaMetaImpl(configuration(), schemas);
    }

    @Override
    public Meta meta(Table<?>... tables) {
        return new TableMetaImpl(configuration(), tables);
    }

    @Override
    public Meta meta(InformationSchema schema) {
        return new InformationSchemaMetaImpl(configuration(), schema);
    }

    @Override
    public Meta meta(String... sources) {
        Source[] s = new Source[sources.length];

        for (int i = 0; i < s.length; i++)
            s[i] = Source.of(sources[i]);

        return new SourceMetaProvider(configuration(), s).provide();
    }

    @Override
    public Meta meta(Source... sources) {
        return new SourceMetaProvider(configuration(), sources).provide();
    }

    @Override
    public Meta meta(Query... queries) {
        return new InterpreterMetaProvider(configuration(), queries).provide();
    }

    @Override
    public InformationSchema informationSchema(Catalog catalog) {
        return InformationSchemaExport.exportCatalogs(configuration(), Arrays.asList(catalog));
    }

    @Override
    public InformationSchema informationSchema(Catalog... catalogs) {
        return InformationSchemaExport.exportCatalogs(configuration(), Arrays.asList(catalogs));
    }

    @Override
    public InformationSchema informationSchema(Schema schema) {
        return InformationSchemaExport.exportSchemas(configuration(), Arrays.asList(schema));
    }

    @Override
    public InformationSchema informationSchema(Schema... schemas) {
        return InformationSchemaExport.exportSchemas(configuration(), Arrays.asList(schemas));
    }

    @Override
    public InformationSchema informationSchema(Table<?> table) {
        return InformationSchemaExport.exportTables(configuration(), Arrays.<Table<?>>asList(table));
    }

    @Override
    public InformationSchema informationSchema(Table<?>... tables) {
        return InformationSchemaExport.exportTables(configuration(), Arrays.<Table<?>>asList(tables));
    }
    // -------------------------------------------------------------------------
    // XXX APIs related to query optimisation
    // -------------------------------------------------------------------------

    @Override
    public Explain explain(Query query) {
        return ExplainQuery.explain(this, query);
    }

    // -------------------------------------------------------------------------
    // XXX APIs for creating scope for transactions, mocking, batching, etc.
    // -------------------------------------------------------------------------

    @Override
    public <T> T transactionResult(final ContextTransactionalCallable<T> transactional) {
        TransactionProvider tp = configuration().transactionProvider();

        if (!(tp instanceof ThreadLocalTransactionProvider))
            throw new ConfigurationException("Cannot use ContextTransactionalCallable with TransactionProvider of type " + tp.getClass());

        return transactionResult0(new TransactionalCallable<T>() {
            @Override
            public T run(Configuration c) throws Throwable {
                return transactional.run();
            }
        }, ((ThreadLocalTransactionProvider) tp).configuration(configuration()), true);
    }

    @Override
    public <T> T transactionResult(TransactionalCallable<T> transactional) {
        return transactionResult0(transactional, configuration(), false);
    }

    private static <T> T transactionResult0(TransactionalCallable<T> transactional, Configuration configuration, boolean threadLocal) {

        // If used in a Java 8 Stream, a transaction should always be executed
        // in a ManagedBlocker context, just in case Stream.parallel() is called

        // The same is true for all asynchronous transactions, which must always
        // run in a ManagedBlocker context.


        return blocking(() -> {


            T result = null;

            DefaultTransactionContext ctx = new DefaultTransactionContext(configuration.derive());
            TransactionProvider provider = ctx.configuration().transactionProvider();
            TransactionListeners listeners = new TransactionListeners(ctx.configuration());
            boolean committed = false;

            try {
                try {
                    listeners.beginStart(ctx);
                    provider.begin(ctx);
                }
                finally {
                    listeners.beginEnd(ctx);
                }

                result = transactional.run(ctx.configuration());

                try {
                    listeners.commitStart(ctx);
                    provider.commit(ctx);
                    committed = true;
                }
                finally {
                    listeners.commitEnd(ctx);
                }
            }

            // [#6608] [#7167] Errors are no longer handled differently
            catch (Throwable cause) {

                // [#8413] Avoid rollback logic if commit was successful (exception in commitEnd())
                if (!committed) {
                    if (cause instanceof Exception)
                        ctx.cause((Exception) cause);
                    else
                        ctx.causeThrowable(cause);

                    listeners.rollbackStart(ctx);
                    try {
                        provider.rollback(ctx);
                    }

                    // [#3718] Use reflection to support also JDBC 4.0
                    catch (Exception suppress) {

                        cause.addSuppressed(suppress);

                    }
                    listeners.rollbackEnd(ctx);
                }

                // [#6608] [#7167] Errors are no longer handled differently
                if (cause instanceof RuntimeException)
                    throw (RuntimeException) cause;
                else if (cause instanceof Error)
                    throw (Error) cause;
                else
                    throw new DataAccessException(committed
                        ? "Exception after commit"
                        : "Rollback caused"
                        , cause
                    );
            }

            return result;


        }, threadLocal).get();

    }

    @Override
    public void transaction(final ContextTransactionalRunnable transactional) {
        transactionResult(new ContextTransactionalCallable<Void>() {
            @Override
            public Void run() throws Throwable {
                transactional.run();
                return null;
            }
        });
    }

    @Override
    public void transaction(final TransactionalRunnable transactional) {
        transactionResult(new TransactionalCallable<Void>() {
            @Override
            public Void run(Configuration c) throws Throwable {
                transactional.run(c);
                return null;
            }
        });
    }



    @Override
    public CompletionStage<Void> transactionAsync(TransactionalRunnable transactional) {
        return transactionAsync(Tools.configuration(configuration()).executorProvider().provide(), transactional);
    }

    @Override
    public CompletionStage<Void> transactionAsync(Executor executor, TransactionalRunnable transactional) {
        if (configuration().transactionProvider() instanceof ThreadLocalTransactionProvider)
            throw new ConfigurationException("Cannot use TransactionalRunnable with ThreadLocalTransactionProvider");

        return ExecutorProviderCompletionStage.of(CompletableFuture.supplyAsync(
            () -> { transaction(transactional); return null; }, executor),
            () -> executor
        );
    }

    @Override
    public <T> CompletionStage<T> transactionResultAsync(TransactionalCallable<T> transactional) {
        return transactionResultAsync(Tools.configuration(configuration()).executorProvider().provide(), transactional);
    }

    @Override
    public <T> CompletionStage<T> transactionResultAsync(Executor executor, TransactionalCallable<T> transactional) {
        if (configuration().transactionProvider() instanceof ThreadLocalTransactionProvider)
            throw new ConfigurationException("Cannot use TransactionalCallable with ThreadLocalTransactionProvider");

        return ExecutorProviderCompletionStage.of(CompletableFuture.supplyAsync(
            () -> transactionResult(transactional), executor),
            () -> executor
        );
    }



    @Override
    public <T> T connectionResult(ConnectionCallable<T> callable) {
        final Connection connection = configuration().connectionProvider().acquire();

        try {
            return callable.run(connection);
        }
        catch (Exception e) {
            throw new DataAccessException("Error while running ConnectionCallable", e);
        }
        finally {
            configuration().connectionProvider().release(connection);
        }
    }

    @Override
    public void connection(final ConnectionRunnable runnable) {
        connectionResult(new ConnectionCallable<Void>() {
            @Override
            public Void run(Connection connection) throws Exception {
                runnable.run(connection);
                return null;
            }
        });
    }

    @Override
    public <T> T mockResult(MockDataProvider provider, MockCallable<T> mockable) {
        try {
            return mockable.run(new MockConfiguration(configuration, provider));
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception cause) {
            throw new DataAccessException("Mock failed", cause);
        }
    }

    @Override
    public void mock(final MockDataProvider provider, final MockRunnable mockable) {
        mockResult(provider, new MockCallable<Void>() {
            @Override
            public Void run(Configuration c) throws Exception {
                mockable.run(c);
                return null;
            }
        });
    }

    // -------------------------------------------------------------------------
    // XXX RenderContext and BindContext accessors
    // -------------------------------------------------------------------------

    @Override
    public RenderContext renderContext() {
        return new DefaultRenderContext(configuration());
    }

    @Override
    public String render(QueryPart part) {
        return renderContext().visit(part).render();
    }

    @Override
    public String renderNamedParams(QueryPart part) {
        return renderContext().paramType(NAMED).visit(part).render();
    }

    @Override
    public String renderNamedOrInlinedParams(QueryPart part) {
        return renderContext().paramType(NAMED_OR_INLINED).visit(part).render();
    }

    @Override
    public String renderInlined(QueryPart part) {
        return renderContext().paramType(INLINED).visit(part).render();
    }

    @Override
    public List<Object> extractBindValues(QueryPart part) {
        ParamCollector collector = new ParamCollector(configuration(), false);
        collector.visit(part);

        List<Object> result = new ArrayList<>(collector.resultList.size());
        for (Entry<String, Param<?>> entry : collector.resultList)
            result.add(entry.getValue().getValue());

        return Collections.unmodifiableList(result);
    }

    @Override
    public Map<String, Param<?>> extractParams(QueryPart part) {
        return extractParams0(part, true);
    }

    final Map<String, Param<?>> extractParams0(QueryPart part, boolean includeInlinedParams) {
        ParamCollector collector = new ParamCollector(configuration(), includeInlinedParams);
        collector.visit(part);
        return Collections.unmodifiableMap(collector.resultFlat);
    }

    @Override
    public Param<?> extractParam(QueryPart part, String name) {
        return extractParams(part).get(name);
    }

    @Override
    public BindContext bindContext(PreparedStatement stmt) {
        return new DefaultBindContext(configuration(), stmt);
    }

    @Override
    @Deprecated
    public int bind(QueryPart part, PreparedStatement stmt) {
        return bindContext(stmt).visit(part).peekIndex();
    }

    // -------------------------------------------------------------------------
    // XXX Attachable and Serializable API
    // -------------------------------------------------------------------------

    @Override
    public void attach(Attachable... attachables) {
        attach(Arrays.asList(attachables));
    }

    @Override
    public void attach(Collection<? extends Attachable> attachables) {
        for (Attachable attachable : attachables) {
            attachable.attach(configuration());
        }
    }

    // -------------------------------------------------------------------------
    // XXX Access to the loader API
    // -------------------------------------------------------------------------

    @Override
    public <R extends Record> LoaderOptionsStep<R> loadInto(Table<R> table) {
        return new LoaderImpl<>(configuration(), table);
    }

    // -------------------------------------------------------------------------
    // XXX: Queries
    // -------------------------------------------------------------------------

    @Override
    public Queries queries(Query... queries) {
        return queries(Arrays.asList(queries));
    }

    @Override
    public Queries queries(Collection<? extends Query> queries) {
        return new QueriesImpl(configuration(), queries);
    }

    @Override
    public Block begin(Statement... statements) {
        return begin(Arrays.asList(statements));
    }

    @Override
    public Block begin(Collection<? extends Statement> statements) {
        return new BlockImpl(configuration(), statements, true);
    }

















    // -------------------------------------------------------------------------
    // XXX Plain SQL API
    // -------------------------------------------------------------------------

    @Override
    public RowCountQuery query(SQL sql) {
        return new SQLQuery(configuration(), sql);
    }

    @Override
    public RowCountQuery query(String sql) {
        return query(sql, new Object[0]);
    }

    @Override
    public RowCountQuery query(String sql, Object... bindings) {
        return query(sql(sql, bindings));
    }

    @Override
    public RowCountQuery query(String sql, QueryPart... parts) {
        return query(sql, (Object[]) parts);
    }

    @Override
    public Result<Record> fetch(SQL sql) {
        return resultQuery(sql).fetch();
    }

    @Override
    public Result<Record> fetch(String sql) {
        return resultQuery(sql).fetch();
    }

    @Override
    public Result<Record> fetch(String sql, Object... bindings) {
        return resultQuery(sql, bindings).fetch();
    }

    @Override
    public Result<Record> fetch(String sql, QueryPart... parts) {
        return resultQuery(sql, parts).fetch();
    }

    @Override
    public Cursor<Record> fetchLazy(SQL sql) {
        return resultQuery(sql).fetchLazy();
    }

    @Override
    public Cursor<Record> fetchLazy(String sql) {
        return resultQuery(sql).fetchLazy();
    }

    @Override
    public Cursor<Record> fetchLazy(String sql, Object... bindings) {
        return resultQuery(sql, bindings).fetchLazy();
    }

    @Override
    public Cursor<Record> fetchLazy(String sql, QueryPart... parts) {
        return resultQuery(sql, parts).fetchLazy();
    }



    @Override
    public CompletionStage<Result<Record>> fetchAsync(SQL sql) {
        return resultQuery(sql).fetchAsync();
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(String sql) {
        return resultQuery(sql).fetchAsync();
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(String sql, Object... bindings) {
        return resultQuery(sql, bindings).fetchAsync();
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(String sql, QueryPart... parts) {
        return resultQuery(sql, parts).fetchAsync();
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(Executor executor, SQL sql) {
        return resultQuery(sql).fetchAsync(executor);
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(Executor executor, String sql) {
        return resultQuery(sql).fetchAsync(executor);
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(Executor executor, String sql, Object... bindings) {
        return resultQuery(sql, bindings).fetchAsync(executor);
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(Executor executor, String sql, QueryPart... parts) {
        return resultQuery(sql, parts).fetchAsync(executor);
    }

    @Override
    public Stream<Record> fetchStream(SQL sql) {
        return resultQuery(sql).stream();
    }

    @Override
    public Stream<Record> fetchStream(String sql) {
        return resultQuery(sql).stream();
    }

    @Override
    public Stream<Record> fetchStream(String sql, Object... bindings) {
        return resultQuery(sql, bindings).stream();
    }

    @Override
    public Stream<Record> fetchStream(String sql, QueryPart... parts) {
        return resultQuery(sql, parts).stream();
    }


    @Override
    public Results fetchMany(SQL sql) {
        return resultQuery(sql).fetchMany();
    }

    @Override
    public Results fetchMany(String sql) {
        return resultQuery(sql).fetchMany();
    }

    @Override
    public Results fetchMany(String sql, Object... bindings) {
        return resultQuery(sql, bindings).fetchMany();
    }

    @Override
    public Results fetchMany(String sql, QueryPart... parts) {
        return resultQuery(sql, parts).fetchMany();
    }

    @Override
    public Record fetchOne(SQL sql) {
        return resultQuery(sql).fetchOne();
    }

    @Override
    public Record fetchOne(String sql) {
        return resultQuery(sql).fetchOne();
    }

    @Override
    public Record fetchOne(String sql, Object... bindings) {
        return resultQuery(sql, bindings).fetchOne();
    }

    @Override
    public Record fetchOne(String sql, QueryPart... parts) {
        return resultQuery(sql, parts).fetchOne();
    }

    @Override
    public Record fetchSingle(SQL sql) {
        return resultQuery(sql).fetchSingle();
    }

    @Override
    public Record fetchSingle(String sql) {
        return resultQuery(sql).fetchSingle();
    }

    @Override
    public Record fetchSingle(String sql, Object... bindings) {
        return resultQuery(sql, bindings).fetchSingle();
    }

    @Override
    public Record fetchSingle(String sql, QueryPart... parts) {
        return resultQuery(sql, parts).fetchSingle();
    }


    @Override
    public Optional<Record> fetchOptional(SQL sql) {
        return Optional.ofNullable(fetchOne(sql));
    }

    @Override
    public Optional<Record> fetchOptional(String sql) {
        return Optional.ofNullable(fetchOne(sql));
    }

    @Override
    public Optional<Record> fetchOptional(String sql, Object... bindings) {
        return Optional.ofNullable(fetchOne(sql, bindings));
    }

    @Override
    public Optional<Record> fetchOptional(String sql, QueryPart... parts) {
        return Optional.ofNullable(fetchOne(sql, parts));
    }


    @Override
    public Object fetchValue(SQL sql) {
        return fetchValue((ResultQuery) resultQuery(sql));
    }

    @Override
    public Object fetchValue(String sql) {
        return fetchValue((ResultQuery) resultQuery(sql));
    }

    @Override
    public Object fetchValue(String sql, Object... bindings) {
        return fetchValue((ResultQuery) resultQuery(sql, bindings));
    }

    @Override
    public Object fetchValue(String sql, QueryPart... parts) {
        return fetchValue((ResultQuery) resultQuery(sql, parts));
    }


    @Override
    public Optional<?> fetchOptionalValue(SQL sql) {
        return Optional.ofNullable(fetchValue(sql));
    }

    @Override
    public Optional<?> fetchOptionalValue(String sql) {
        return Optional.ofNullable(fetchValue(sql));
    }

    @Override
    public Optional<?> fetchOptionalValue(String sql, Object... bindings) {
        return Optional.ofNullable(fetchValue(sql, bindings));
    }

    @Override
    public Optional<?> fetchOptionalValue(String sql, QueryPart... parts) {
        return Optional.ofNullable(fetchValue(sql, parts));
    }


    @Override
    public List<?> fetchValues(SQL sql) {
        return fetchValues((ResultQuery) resultQuery(sql));
    }

    @Override
    public List<?> fetchValues(String sql) {
        return fetchValues((ResultQuery) resultQuery(sql));
    }

    @Override
    public List<?> fetchValues(String sql, Object... bindings) {
        return fetchValues((ResultQuery) resultQuery(sql, bindings));
    }

    @Override
    public List<?> fetchValues(String sql, QueryPart... parts) {
        return fetchValues((ResultQuery) resultQuery(sql, parts));
    }

    @Override
    public int execute(SQL sql) {
        return query(sql).execute();
    }

    @Override
    public int execute(String sql) {
        return query(sql).execute();
    }

    @Override
    public int execute(String sql, Object... bindings) {
        return query(sql, bindings).execute();
    }

    @Override
    public int execute(String sql, QueryPart... parts) {
        return query(sql, (Object[]) parts).execute();
    }

    @Override
    public ResultQuery<Record> resultQuery(SQL sql) {
        return new SQLResultQuery(configuration(), sql);
    }

    @Override
    public ResultQuery<Record> resultQuery(String sql) {
        return resultQuery(sql, new Object[0]);
    }

    @Override
    public ResultQuery<Record> resultQuery(String sql, Object... bindings) {
        return resultQuery(sql(sql, bindings));
    }

    @Override
    public ResultQuery<Record> resultQuery(String sql, QueryPart... parts) {
        return resultQuery(sql, (Object[]) parts);
    }

    // -------------------------------------------------------------------------
    // XXX JDBC convenience methods
    // -------------------------------------------------------------------------

    @Override
    public Result<Record> fetch(ResultSet rs) {
        return fetchLazy(rs).fetch();
    }

    @Override
    public Result<Record> fetch(ResultSet rs, Field<?>... fields) {
        return fetchLazy(rs, fields).fetch();
    }

    @Override
    public Result<Record> fetch(ResultSet rs, DataType<?>... types) {
        return fetchLazy(rs, types).fetch();
    }

    @Override
    public Result<Record> fetch(ResultSet rs, Class<?>... types) {
        return fetchLazy(rs, types).fetch();
    }

    @Override
    public Record fetchOne(ResultSet rs) {
        return Tools.fetchOne(fetchLazy(rs));
    }

    @Override
    public Record fetchOne(ResultSet rs, Field<?>... fields) {
        return Tools.fetchOne(fetchLazy(rs, fields));
    }

    @Override
    public Record fetchOne(ResultSet rs, DataType<?>... types) {
        return Tools.fetchOne(fetchLazy(rs, types));
    }

    @Override
    public Record fetchOne(ResultSet rs, Class<?>... types) {
        return Tools.fetchOne(fetchLazy(rs, types));
    }

    @Override
    public Record fetchSingle(ResultSet rs) {
        return Tools.fetchSingle(fetchLazy(rs));
    }

    @Override
    public Record fetchSingle(ResultSet rs, Field<?>... fields) {
        return Tools.fetchSingle(fetchLazy(rs, fields));
    }

    @Override
    public Record fetchSingle(ResultSet rs, DataType<?>... types) {
        return Tools.fetchSingle(fetchLazy(rs, types));
    }

    @Override
    public Record fetchSingle(ResultSet rs, Class<?>... types) {
        return Tools.fetchSingle(fetchLazy(rs, types));
    }


    @Override
    public Optional<Record> fetchOptional(ResultSet rs) {
        return Optional.ofNullable(fetchOne(rs));
    }

    @Override
    public Optional<Record> fetchOptional(ResultSet rs, Field<?>... fields) {
        return Optional.ofNullable(fetchOne(rs, fields));
    }

    @Override
    public Optional<Record> fetchOptional(ResultSet rs, DataType<?>... types) {
        return Optional.ofNullable(fetchOne(rs, types));
    }

    @Override
    public Optional<Record> fetchOptional(ResultSet rs, Class<?>... types) {
        return Optional.ofNullable(fetchOne(rs, types));
    }


    @Override
    public Object fetchValue(ResultSet rs) {
        return value1((Record1) fetchOne(rs));
    }

    @Override
    public <T> T fetchValue(ResultSet rs, Field<T> field) {
        return (T) value1((Record1) fetchOne(rs, field));
    }

    @Override
    public <T> T fetchValue(ResultSet rs, DataType<T> type) {
        return (T) value1((Record1) fetchOne(rs, type));
    }

    @Override
    public <T> T fetchValue(ResultSet rs, Class<T> type) {
        return (T) value1((Record1) fetchOne(rs, type));
    }


    @Override
    public Optional<?> fetchOptionalValue(ResultSet rs) {
        return Optional.ofNullable(fetchValue(rs));
    }

    @Override
    public <T> Optional<T> fetchOptionalValue(ResultSet rs, Field<T> field) {
        return Optional.ofNullable(fetchValue(rs, field));
    }

    @Override
    public <T> Optional<T> fetchOptionalValue(ResultSet rs, DataType<T> type) {
        return Optional.ofNullable(fetchValue(rs, type));
    }

    @Override
    public <T> Optional<T> fetchOptionalValue(ResultSet rs, Class<T> type) {
        return Optional.ofNullable(fetchValue(rs, type));
    }


    @Override
    public List<?> fetchValues(ResultSet rs) {
        return fetch(rs).getValues(0);
    }

    @Override
    public <T> List<T> fetchValues(ResultSet rs, Field<T> field) {
        return fetch(rs).getValues(field);
    }

    @Override
    public <T> List<T> fetchValues(ResultSet rs, DataType<T> type) {
        return fetch(rs).getValues(0, type.getType());
    }

    @Override
    public <T> List<T> fetchValues(ResultSet rs, Class<T> type) {
        return fetch(rs).getValues(0, type);
    }

    @Override
    public Cursor<Record> fetchLazy(ResultSet rs) {
        try {
            return fetchLazy(rs, new MetaDataFieldProvider(configuration(), rs.getMetaData()).getFields());
        }
        catch (SQLException e) {
            throw new DataAccessException("Error while accessing ResultSet meta data", e);
        }
    }

    @Override
    public Cursor<Record> fetchLazy(ResultSet rs, Field<?>... fields) {
        ExecuteContext ctx = new DefaultExecuteContext(configuration());
        ExecuteListener listener = ExecuteListeners.getAndStart(ctx);

        ctx.resultSet(rs);
        return new CursorImpl<>(ctx, listener, fields, null, false, true);
    }

    @Override
    public Cursor<Record> fetchLazy(ResultSet rs, DataType<?>... types) {
        try {
            Field<?>[] fields = new Field[types.length];
            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();

            for (int i = 0; i < types.length && i < columns; i++) {
                fields[i] = field(meta.getColumnLabel(i + 1), types[i]);
            }

            return fetchLazy(rs, fields);
        }
        catch (SQLException e) {
            throw new DataAccessException("Error while accessing ResultSet meta data", e);
        }
    }

    @Override
    public Cursor<Record> fetchLazy(ResultSet rs, Class<?>... types) {
        return fetchLazy(rs, Tools.dataTypes(types));
    }



    @Override
    public CompletionStage<Result<Record>> fetchAsync(ResultSet rs) {
        return fetchAsync(Tools.configuration(configuration()).executorProvider().provide(), rs);
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(ResultSet rs, Field<?>... fields) {
        return fetchAsync(Tools.configuration(configuration()).executorProvider().provide(), rs, fields);
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(ResultSet rs, DataType<?>... types) {
        return fetchAsync(Tools.configuration(configuration()).executorProvider().provide(), rs, types);
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(ResultSet rs, Class<?>... types) {
        return fetchAsync(Tools.configuration(configuration()).executorProvider().provide(), rs, types);
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(Executor executor, ResultSet rs) {
        return ExecutorProviderCompletionStage.of(
            CompletableFuture.supplyAsync(blocking(() -> fetch(rs)), executor),
            () -> executor
        );
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(Executor executor, ResultSet rs, Field<?>... fields) {
        return ExecutorProviderCompletionStage.of(
            CompletableFuture.supplyAsync(blocking(() -> fetch(rs, fields)), executor),
            () -> executor
        );
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(Executor executor, ResultSet rs, DataType<?>... types) {
        return ExecutorProviderCompletionStage.of(
            CompletableFuture.supplyAsync(blocking(() -> fetch(rs, types)), executor),
            () -> executor
        );
    }

    @Override
    public CompletionStage<Result<Record>> fetchAsync(Executor executor, ResultSet rs, Class<?>... types) {
        return ExecutorProviderCompletionStage.of(
            CompletableFuture.supplyAsync(blocking(() -> fetch(rs, types)), executor),
            () -> executor
        );
    }

    @Override
    public Stream<Record> fetchStream(ResultSet rs) {
        return fetchLazy(rs).stream();
    }

    @Override
    public Stream<Record> fetchStream(ResultSet rs, Field<?>... fields) {
        return fetchLazy(rs, fields).stream();
    }

    @Override
    public Stream<Record> fetchStream(ResultSet rs, DataType<?>... types) {
        return fetchLazy(rs, types).stream();
    }

    @Override
    public Stream<Record> fetchStream(ResultSet rs, Class<?>... types) {
        return fetchLazy(rs, types).stream();
    }


    @Override
    public Result<Record> fetchFromTXT(String string) {
        return fetchFromTXT(string, "{null}");
    }

    @Override
    public Result<Record> fetchFromTXT(String string, String nullLiteral) {
        return fetchFromStringData(Tools.parseTXT(string, nullLiteral));
    }

    @Override
    public Result<Record> fetchFromHTML(String string) {
        return fetchFromStringData(Tools.parseHTML(string));
    }

    @Override
    public Result<Record> fetchFromCSV(String string) {
        return fetchFromCSV(string, true, ',');
    }

    @Override
    public Result<Record> fetchFromCSV(String string, char delimiter) {
        return fetchFromCSV(string, true, delimiter);
    }

    @Override
    public Result<Record> fetchFromCSV(String string, boolean header) {
        return fetchFromCSV(string, header, ',');
    }

    @Override
    public Result<Record> fetchFromCSV(String string, boolean header, char delimiter) {
        CSVReader reader = new CSVReader(new StringReader(string), delimiter);
        List<String[]> list = null;

        try {
            list = reader.readAll();
        }
        catch (IOException e) {
            throw new DataAccessException("Could not read the CSV string", e);
        }
        finally {
            try {
                reader.close();
            }
            catch (IOException ignore) {}
        }

        return fetchFromStringData(list, header);
    }

    @Override
    public Result<Record> fetchFromJSON(String string) {
        return new JSONReader(this).read(string);
    }

    @Override
    public Result<Record> fetchFromXML(String string) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();

            // -----------------------------------------------------------------
            // [JOOX #136] FIX START: Prevent OWASP attack vectors
            try {
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            }
            catch (ParserConfigurationException ignore) {}

            try {
                factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            }
            catch (ParserConfigurationException ignore) {}

            try {
                factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            }
            catch (ParserConfigurationException ignore) {}

            // [#149] Not implemented on Android
            try {
                factory.setXIncludeAware(false);
            }
            catch (UnsupportedOperationException ignore) {}

            // [JOOX #136] FIX END
            // -----------------------------------------------------------------

            SAXParser saxParser = factory.newSAXParser();
            // TODO: Why does the SAXParser replace \r by \n?

            XMLHandler handler = new XMLHandler(this);
            saxParser.parse(new ByteArrayInputStream(string.getBytes()), handler);
            return handler.result;
        }
        catch (Exception e) {
            throw new DataAccessException("Could not read the XML string", e);
        }
    }

    @Override
    public Result<Record> fetchFromStringData(String[]... strings) {
        return fetchFromStringData(list(strings), true);
    }

    @Override
    public Result<Record> fetchFromStringData(List<String[]> strings) {
        return fetchFromStringData(strings, true);
    }

    @Override
    public Result<Record> fetchFromStringData(List<String[]> strings, boolean header) {
        if (strings.size() == 0) {
            return new ResultImpl<>(configuration());
        }
        else {
            List<Field<?>> fields = new ArrayList<>(strings.get(0).length);
            int firstRow = header ? 1 : 0;

            if (header)
                for (String name : strings.get(0))
                    fields.add(field(name(name), String.class));
            else
                for (int i = 0; i < strings.get(0).length; i++)
                    fields.add(field(name("COL" + (i + 1)), String.class));

            Result<Record> result = new ResultImpl<>(configuration(), fields);

            if (strings.size() > firstRow) {
                for (String[] values : strings.subList(firstRow, strings.size())) {
                    RecordImplN record = new RecordImplN(fields);

                    for (int i = 0; i < Math.min(values.length, fields.size()); i++) {
                        record.values[i] = values[i];
                        record.originals[i] = values[i];
                    }

                    result.add(record);
                }
            }

            return result;
        }
    }

    // -------------------------------------------------------------------------
    // XXX Global Query factory
    // -------------------------------------------------------------------------

    @Override
    public WithAsStep with(String alias) {
        return new WithImpl(configuration(), false).with(alias);
    }

    @Override
    public WithAsStep with(String alias, String... fieldAliases) {
        return new WithImpl(configuration(), false).with(alias, fieldAliases);
    }

    @Override
    public WithAsStep with(String alias, Collection<String> fieldAliases) {
        return new WithImpl(configuration(), false).with(alias, fieldAliases);
    }

    @Override
    public WithAsStep with(Name alias) {
        return new WithImpl(configuration(), false).with(alias);
    }

    @Override
    public WithAsStep with(Name alias, Name... fieldAliases) {
        return new WithImpl(configuration(), false).with(alias, fieldAliases);
    }

    @Override
    public WithAsStep with(Name alias, Collection<? extends Name> fieldAliases) {
        return new WithImpl(configuration(), false).with(alias, fieldAliases);
    }


    @Override
    public WithAsStep with(String alias, Function<? super Field<?>, ? extends String> fieldNameFunction) {
        return new WithImpl(configuration(), false).with(alias, fieldNameFunction);
    }

    @Override
    public WithAsStep with(String alias, BiFunction<? super Field<?>, ? super Integer, ? extends String> fieldNameFunction) {
        return new WithImpl(configuration(), false).with(alias, fieldNameFunction);
    }




    @Override
    public WithAsStep1 with(String alias, String fieldAlias1) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1);
    }

    @Override
    public WithAsStep2 with(String alias, String fieldAlias1, String fieldAlias2) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2);
    }

    @Override
    public WithAsStep3 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3);
    }

    @Override
    public WithAsStep4 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4);
    }

    @Override
    public WithAsStep5 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5);
    }

    @Override
    public WithAsStep6 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6);
    }

    @Override
    public WithAsStep7 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7);
    }

    @Override
    public WithAsStep8 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8);
    }

    @Override
    public WithAsStep9 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9);
    }

    @Override
    public WithAsStep10 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10);
    }

    @Override
    public WithAsStep11 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11);
    }

    @Override
    public WithAsStep12 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12);
    }

    @Override
    public WithAsStep13 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13);
    }

    @Override
    public WithAsStep14 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14);
    }

    @Override
    public WithAsStep15 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15);
    }

    @Override
    public WithAsStep16 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16);
    }

    @Override
    public WithAsStep17 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17);
    }

    @Override
    public WithAsStep18 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18);
    }

    @Override
    public WithAsStep19 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19);
    }

    @Override
    public WithAsStep20 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20);
    }

    @Override
    public WithAsStep21 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20, String fieldAlias21) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21);
    }

    @Override
    public WithAsStep22 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20, String fieldAlias21, String fieldAlias22) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21, fieldAlias22);
    }

    @Override
    public WithAsStep1 with(Name alias, Name fieldAlias1) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1);
    }

    @Override
    public WithAsStep2 with(Name alias, Name fieldAlias1, Name fieldAlias2) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2);
    }

    @Override
    public WithAsStep3 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3);
    }

    @Override
    public WithAsStep4 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4);
    }

    @Override
    public WithAsStep5 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5);
    }

    @Override
    public WithAsStep6 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6);
    }

    @Override
    public WithAsStep7 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7);
    }

    @Override
    public WithAsStep8 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8);
    }

    @Override
    public WithAsStep9 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9);
    }

    @Override
    public WithAsStep10 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10);
    }

    @Override
    public WithAsStep11 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11);
    }

    @Override
    public WithAsStep12 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12);
    }

    @Override
    public WithAsStep13 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13);
    }

    @Override
    public WithAsStep14 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14);
    }

    @Override
    public WithAsStep15 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15);
    }

    @Override
    public WithAsStep16 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16);
    }

    @Override
    public WithAsStep17 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17);
    }

    @Override
    public WithAsStep18 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18);
    }

    @Override
    public WithAsStep19 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19);
    }

    @Override
    public WithAsStep20 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20);
    }

    @Override
    public WithAsStep21 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20, Name fieldAlias21) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21);
    }

    @Override
    public WithAsStep22 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20, Name fieldAlias21, Name fieldAlias22) {
        return new WithImpl(configuration(), false).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21, fieldAlias22);
    }



    @Override
    public WithStep with(CommonTableExpression<?>... tables) {
        return new WithImpl(configuration(), false).with(tables);
    }

    @Override
    public WithStep with(Collection<? extends CommonTableExpression<?>> tables) {
        return new WithImpl(configuration(), false).with(tables);
    }

    @Override
    public WithAsStep withRecursive(String alias) {
        return new WithImpl(configuration(), true).with(alias);
    }

    @Override
    public WithAsStep withRecursive(String alias, String... fieldAliases) {
        return new WithImpl(configuration(), true).with(alias, fieldAliases);
    }

    @Override
    public WithAsStep withRecursive(String alias, Collection<String> fieldAliases) {
        return new WithImpl(configuration(), true).with(alias, fieldAliases);
    }

    @Override
    public WithAsStep withRecursive(Name alias) {
        return new WithImpl(configuration(), true).with(alias);
    }

    @Override
    public WithAsStep withRecursive(Name alias, Name... fieldAliases) {
        return new WithImpl(configuration(), true).with(alias, fieldAliases);
    }

    @Override
    public WithAsStep withRecursive(Name alias, Collection<? extends Name> fieldAliases) {
        return new WithImpl(configuration(), true).with(alias, fieldAliases);
    }


    @Override
    public WithAsStep withRecursive(String alias, Function<? super Field<?>, ? extends String> fieldNameFunction) {
        return new WithImpl(configuration(), true).with(alias, fieldNameFunction);
    }

    @Override
    public WithAsStep withRecursive(String alias, BiFunction<? super Field<?>, ? super Integer, ? extends String> fieldNameFunction) {
        return new WithImpl(configuration(), true).with(alias, fieldNameFunction);
    }




    @Override
    public WithAsStep1 withRecursive(String alias, String fieldAlias1) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1);
    }

    @Override
    public WithAsStep2 withRecursive(String alias, String fieldAlias1, String fieldAlias2) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2);
    }

    @Override
    public WithAsStep3 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3);
    }

    @Override
    public WithAsStep4 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4);
    }

    @Override
    public WithAsStep5 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5);
    }

    @Override
    public WithAsStep6 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6);
    }

    @Override
    public WithAsStep7 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7);
    }

    @Override
    public WithAsStep8 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8);
    }

    @Override
    public WithAsStep9 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9);
    }

    @Override
    public WithAsStep10 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10);
    }

    @Override
    public WithAsStep11 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11);
    }

    @Override
    public WithAsStep12 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12);
    }

    @Override
    public WithAsStep13 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13);
    }

    @Override
    public WithAsStep14 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14);
    }

    @Override
    public WithAsStep15 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15);
    }

    @Override
    public WithAsStep16 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16);
    }

    @Override
    public WithAsStep17 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17);
    }

    @Override
    public WithAsStep18 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18);
    }

    @Override
    public WithAsStep19 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19);
    }

    @Override
    public WithAsStep20 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20);
    }

    @Override
    public WithAsStep21 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20, String fieldAlias21) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21);
    }

    @Override
    public WithAsStep22 withRecursive(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20, String fieldAlias21, String fieldAlias22) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21, fieldAlias22);
    }

    @Override
    public WithAsStep1 withRecursive(Name alias, Name fieldAlias1) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1);
    }

    @Override
    public WithAsStep2 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2);
    }

    @Override
    public WithAsStep3 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3);
    }

    @Override
    public WithAsStep4 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4);
    }

    @Override
    public WithAsStep5 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5);
    }

    @Override
    public WithAsStep6 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6);
    }

    @Override
    public WithAsStep7 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7);
    }

    @Override
    public WithAsStep8 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8);
    }

    @Override
    public WithAsStep9 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9);
    }

    @Override
    public WithAsStep10 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10);
    }

    @Override
    public WithAsStep11 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11);
    }

    @Override
    public WithAsStep12 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12);
    }

    @Override
    public WithAsStep13 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13);
    }

    @Override
    public WithAsStep14 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14);
    }

    @Override
    public WithAsStep15 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15);
    }

    @Override
    public WithAsStep16 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16);
    }

    @Override
    public WithAsStep17 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17);
    }

    @Override
    public WithAsStep18 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18);
    }

    @Override
    public WithAsStep19 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19);
    }

    @Override
    public WithAsStep20 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20);
    }

    @Override
    public WithAsStep21 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20, Name fieldAlias21) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21);
    }

    @Override
    public WithAsStep22 withRecursive(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20, Name fieldAlias21, Name fieldAlias22) {
        return new WithImpl(configuration(), true).with(alias, fieldAlias1, fieldAlias2, fieldAlias3, fieldAlias4, fieldAlias5, fieldAlias6, fieldAlias7, fieldAlias8, fieldAlias9, fieldAlias10, fieldAlias11, fieldAlias12, fieldAlias13, fieldAlias14, fieldAlias15, fieldAlias16, fieldAlias17, fieldAlias18, fieldAlias19, fieldAlias20, fieldAlias21, fieldAlias22);
    }



    @Override
    public WithStep withRecursive(CommonTableExpression<?>... tables) {
        return new WithImpl(configuration(), true).with(tables);
    }

    @Override
    public WithStep withRecursive(Collection<? extends CommonTableExpression<?>> tables) {
        return new WithImpl(configuration(), true).with(tables);
    }

    @Override
    public <R extends Record> SelectWhereStep<R> selectFrom(Table<R> table) {
        return new SelectImpl(configuration(), null).from(table);
    }

    @Override
    public SelectWhereStep<Record> selectFrom(Name table) {
        return new SelectImpl(configuration(), null).from(table);
    }

    @Override
    public SelectWhereStep<Record> selectFrom(SQL sql) {
        return new SelectImpl(configuration(), null).from(sql);
    }

    @Override
    public SelectWhereStep<Record> selectFrom(String sql) {
        return new SelectImpl(configuration(), null).from(sql);
    }

    @Override
    public SelectWhereStep<Record> selectFrom(String sql, Object... bindings) {
        return new SelectImpl(configuration(), null).from(sql, bindings);
    }

    @Override
    public SelectWhereStep<Record> selectFrom(String sql, QueryPart... parts) {
        return new SelectImpl(configuration(), null).from(sql, parts);
    }

    @Override
    public SelectSelectStep<Record> select(Collection<? extends SelectFieldOrAsterisk> fields) {
        return new SelectImpl(configuration(), null).select(fields);
    }

    @Override
    public SelectSelectStep<Record> select(SelectFieldOrAsterisk... fields) {
        return new SelectImpl(configuration(), null).select(fields);
    }



    @Override
    public <T1> SelectSelectStep<Record1<T1>> select(SelectField<T1> field1) {
        return (SelectSelectStep) select(new SelectField[] { field1 });
    }

    @Override
    public <T1, T2> SelectSelectStep<Record2<T1, T2>> select(SelectField<T1> field1, SelectField<T2> field2) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2 });
    }

    @Override
    public <T1, T2, T3> SelectSelectStep<Record3<T1, T2, T3>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3 });
    }

    @Override
    public <T1, T2, T3, T4> SelectSelectStep<Record4<T1, T2, T3, T4>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4 });
    }

    @Override
    public <T1, T2, T3, T4, T5> SelectSelectStep<Record5<T1, T2, T3, T4, T5>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> SelectSelectStep<Record6<T1, T2, T3, T4, T5, T6>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7> SelectSelectStep<Record7<T1, T2, T3, T4, T5, T6, T7>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8> SelectSelectStep<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9> SelectSelectStep<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SelectSelectStep<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SelectSelectStep<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SelectSelectStep<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> SelectSelectStep<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> SelectSelectStep<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> SelectSelectStep<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> SelectSelectStep<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> SelectSelectStep<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> SelectSelectStep<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> SelectSelectStep<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> SelectSelectStep<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> SelectSelectStep<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> SelectSelectStep<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21, SelectField<T22> field22) {
        return (SelectSelectStep) select(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }



    @Override
    public SelectSelectStep<Record> selectDistinct(Collection<? extends SelectFieldOrAsterisk> fields) {
        return new SelectImpl(configuration(), null, true).select(fields);
    }

    @Override
    public SelectSelectStep<Record> selectDistinct(SelectFieldOrAsterisk... fields) {
        return new SelectImpl(configuration(), null, true).select(fields);
    }



    @Override
    public <T1> SelectSelectStep<Record1<T1>> selectDistinct(SelectField<T1> field1) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1 });
    }

    @Override
    public <T1, T2> SelectSelectStep<Record2<T1, T2>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2 });
    }

    @Override
    public <T1, T2, T3> SelectSelectStep<Record3<T1, T2, T3>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3 });
    }

    @Override
    public <T1, T2, T3, T4> SelectSelectStep<Record4<T1, T2, T3, T4>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4 });
    }

    @Override
    public <T1, T2, T3, T4, T5> SelectSelectStep<Record5<T1, T2, T3, T4, T5>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> SelectSelectStep<Record6<T1, T2, T3, T4, T5, T6>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7> SelectSelectStep<Record7<T1, T2, T3, T4, T5, T6, T7>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8> SelectSelectStep<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9> SelectSelectStep<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SelectSelectStep<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SelectSelectStep<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SelectSelectStep<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> SelectSelectStep<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> SelectSelectStep<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> SelectSelectStep<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> SelectSelectStep<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> SelectSelectStep<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> SelectSelectStep<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> SelectSelectStep<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> SelectSelectStep<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> SelectSelectStep<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> SelectSelectStep<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21, SelectField<T22> field22) {
        return (SelectSelectStep) selectDistinct(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }



    @Override
    public SelectSelectStep<Record1<Integer>> selectZero() {
        return new SelectImpl(configuration(), null).select(zero().as("zero"));
    }

    @Override
    public SelectSelectStep<Record1<Integer>> selectOne() {
        return new SelectImpl(configuration(), null).select(one().as("one"));
    }

    @Override
    public SelectSelectStep<Record1<Integer>> selectCount() {
        return new SelectImpl(configuration(), null).select(count());
    }

    @Override
    public SelectQuery<Record> selectQuery() {
        return new SelectQueryImpl(configuration(), null);
    }

    @Override
    public <R extends Record> SelectQuery<R> selectQuery(TableLike<R> table) {
        return new SelectQueryImpl<>(configuration(), null, table);
    }

    @Override
    public <R extends Record> InsertQuery<R> insertQuery(Table<R> into) {
        return new InsertQueryImpl<>(configuration(), null, into);
    }

    @Override
    public <R extends Record> InsertSetStep<R> insertInto(Table<R> into) {
        return new InsertImpl(configuration(), null, into, Collections.<Field<?>>emptyList());
    }



    @Override
    public <R extends Record, T1> InsertValuesStep1<R, T1> insertInto(Table<R> into, Field<T1> field1) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1 }));
    }

    @Override
    public <R extends Record, T1, T2> InsertValuesStep2<R, T1, T2> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2 }));
    }

    @Override
    public <R extends Record, T1, T2, T3> InsertValuesStep3<R, T1, T2, T3> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4> InsertValuesStep4<R, T1, T2, T3, T4> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5> InsertValuesStep5<R, T1, T2, T3, T4, T5> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6> InsertValuesStep6<R, T1, T2, T3, T4, T5, T6> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7> InsertValuesStep7<R, T1, T2, T3, T4, T5, T6, T7> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8> InsertValuesStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9> InsertValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> InsertValuesStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> InsertValuesStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> InsertValuesStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> InsertValuesStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> InsertValuesStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> InsertValuesStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> InsertValuesStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> InsertValuesStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> InsertValuesStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> InsertValuesStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> InsertValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> InsertValuesStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 }));
    }

    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> InsertValuesStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 }));
    }



    @Override
    public <R extends Record> InsertValuesStepN<R> insertInto(Table<R> into, Field<?>... fields) {
        return new InsertImpl(configuration(), null, into, Arrays.asList(fields));
    }

    @Override
    public <R extends Record> InsertValuesStepN<R> insertInto(Table<R> into, Collection<? extends Field<?>> fields) {
        return new InsertImpl(configuration(), null, into, fields);
    }

    @Override
    public <R extends Record> UpdateQuery<R> updateQuery(Table<R> table) {
        return new UpdateQueryImpl<>(configuration(), null, table);
    }

    @Override
    public <R extends Record> UpdateSetFirstStep<R> update(Table<R> table) {
        return new UpdateImpl<>(configuration(), null, table);
    }

    @Override
    public <R extends Record> MergeUsingStep<R> mergeInto(Table<R> table) {
        return new MergeImpl(configuration(), null, table);
    }



    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1> MergeKeyStep1<R, T1> mergeInto(Table<R> table, Field<T1> field1) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2> MergeKeyStep2<R, T1, T2> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3> MergeKeyStep3<R, T1, T2, T3> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4> MergeKeyStep4<R, T1, T2, T3, T4> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5> MergeKeyStep5<R, T1, T2, T3, T4, T5> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6> MergeKeyStep6<R, T1, T2, T3, T4, T5, T6> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7> MergeKeyStep7<R, T1, T2, T3, T4, T5, T6, T7> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8> MergeKeyStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9> MergeKeyStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> MergeKeyStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> MergeKeyStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> MergeKeyStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> MergeKeyStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> MergeKeyStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> MergeKeyStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> MergeKeyStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> MergeKeyStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> MergeKeyStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> MergeKeyStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> MergeKeyStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> MergeKeyStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21));
    }

    /**
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated
    @Override
    public <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> MergeKeyStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return new MergeImpl(configuration(), null, table, Arrays.asList(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22));
    }



    @Override
    public <R extends Record> MergeKeyStepN<R> mergeInto(Table<R> table, Field<?>... fields) {
        return mergeInto(table, Arrays.asList(fields));
    }

    @Override
    public <R extends Record> MergeKeyStepN<R> mergeInto(Table<R> table, Collection<? extends Field<?>> fields) {
        return new MergeImpl(configuration(), null, table, fields);
    }

    @Override
    public <R extends Record> DeleteQuery<R> deleteQuery(Table<R> table) {
        return new DeleteQueryImpl<>(configuration(), null, table);
    }

    @Override
    public <R extends Record> DeleteUsingStep<R> delete(Table<R> table) {
        return deleteFrom(table);
    }

    @Override
    public <R extends Record> DeleteUsingStep<R> deleteFrom(Table<R> table) {
        return new DeleteImpl<>(configuration(), null, table);
    }

    // -------------------------------------------------------------------------
    // XXX Batch query execution
    // -------------------------------------------------------------------------

    @Override
    public Batch batch(Query... queries) {
        return new BatchMultiple(configuration(), queries);
    }

    @Override
    public Batch batch(Queries queries) {
        return batch(queries.queries());
    }

    @Override
    public Batch batch(String... queries) {
        Query[] result = new Query[queries.length];

        for (int i = 0; i < queries.length; i++) {
            result[i] = query(queries[i]);
        }

        return batch(result);
    }

    @Override
    public Batch batch(Collection<? extends Query> queries) {
        return batch(queries.toArray(EMPTY_QUERY));
    }

    @Override
    public BatchBindStep batch(Query query) {
        return new BatchSingle(configuration(), query);
    }

    @Override
    public BatchBindStep batch(String sql) {
        return batch(query(sql));
    }

    @Override
    public Batch batch(Query query, Object[]... bindings) {
        return batch(query).bind(bindings);
    }

    @Override
    public Batch batch(String sql, Object[]... bindings) {
        return batch(query(sql), bindings);
    }

    @Override
    public Batch batchStore(UpdatableRecord<?>... records) {
        return new BatchCRUD(configuration(), Action.STORE, records);
    }

    @Override
    public Batch batchStore(Collection<? extends UpdatableRecord<?>> records) {
        return batchStore(records.toArray(EMPTY_UPDATABLE_RECORD));
    }

    @Override
    public Batch batchInsert(TableRecord<?>... records) {
        return new BatchCRUD(configuration(), Action.INSERT, records);
    }

    @Override
    public Batch batchInsert(Collection<? extends TableRecord<?>> records) {
        return batchInsert(records.toArray(EMPTY_TABLE_RECORD));
    }

    @Override
    public Batch batchUpdate(UpdatableRecord<?>... records) {
        return new BatchCRUD(configuration(), Action.UPDATE, records);
    }

    @Override
    public Batch batchUpdate(Collection<? extends UpdatableRecord<?>> records) {
        return batchUpdate(records.toArray(EMPTY_UPDATABLE_RECORD));
    }

    @Override
    public Batch batchDelete(UpdatableRecord<?>... records) {
        return new BatchCRUD(configuration(), Action.DELETE, records);
    }

    @Override
    public Batch batchDelete(Collection<? extends UpdatableRecord<?>> records) {
        return batchDelete(records.toArray(EMPTY_UPDATABLE_RECORD));
    }

    // -------------------------------------------------------------------------
    // XXX DDL Statements from existing meta data
    // -------------------------------------------------------------------------

    @Override
    public Queries ddl(Catalog catalog) {
        return ddl(catalog, new DDLExportConfiguration());
    }

    @Override
    public Queries ddl(Catalog catalog, DDLFlag... flags) {
        return ddl(catalog, new DDLExportConfiguration().flags(flags));
    }

    @Override
    public Queries ddl(Catalog catalog, DDLExportConfiguration exportConfiguration) {
        return meta(catalog).ddl(exportConfiguration);
    }

    @Override
    public Queries ddl(Schema schema) {
        return ddl(schema, new DDLExportConfiguration());
    }

    @Override
    public Queries ddl(Schema schema, DDLFlag... flags) {
        return ddl(schema, new DDLExportConfiguration().flags(flags));
    }

    @Override
    public Queries ddl(Schema schema, DDLExportConfiguration exportConfiguration) {
        return meta(schema).ddl(exportConfiguration);
    }

    @Override
    public Queries ddl(Table<?> table) {
        return ddl(new Table[] { table });
    }

    @Override
    public Queries ddl(Table<?> table, DDLFlag... flags) {
        return ddl(new Table[] { table }, flags);
    }

    @Override
    public Queries ddl(Table<?> table, DDLExportConfiguration exportConfiguration) {
        return ddl(new Table[] { table }, exportConfiguration);
    }

    @Override
    public Queries ddl(Table... tables) {
        return ddl(tables, new DDLExportConfiguration());
    }

    @Override
    public Queries ddl(Table[] tables, DDLFlag... flags) {
        return ddl(tables, new DDLExportConfiguration().flags(flags));
    }

    @Override
    public Queries ddl(Table[] tables, DDLExportConfiguration exportConfiguration) {
        return meta(tables).ddl(exportConfiguration);
    }

    @Override
    public Queries ddl(Collection<? extends Table<?>> tables) {
        return ddl(tables.toArray(EMPTY_TABLE));
    }

    @Override
    public Queries ddl(Collection<? extends Table<?>> tables, DDLFlag... flags) {
        return ddl(tables.toArray(EMPTY_TABLE), flags);
    }

    @Override
    public Queries ddl(Collection<? extends Table<?>> tables, DDLExportConfiguration exportConfiguration) {
        return ddl(tables.toArray(EMPTY_TABLE), exportConfiguration);
    }

    // -------------------------------------------------------------------------
    // XXX DDL Statements
    // -------------------------------------------------------------------------

    @Override
    public RowCountQuery setCatalog(String catalog) {
        return setCatalog(name(catalog));
    }

    @Override
    public RowCountQuery setCatalog(Name catalog) {
        return setCatalog(catalog(catalog));
    }

    @Override
    public RowCountQuery setCatalog(Catalog catalog) {
        return new SetCatalog(configuration(), catalog);
    }

    @Override
    public RowCountQuery setSchema(String schema) {
        return setSchema(name(schema));
    }

    @Override
    public RowCountQuery setSchema(Name schema) {
        return setSchema(schema(schema));
    }

    @Override
    public RowCountQuery setSchema(Schema schema) {
        return new SetSchema(configuration(), schema);
    }

    @Override
    public RowCountQuery set(Name name, Param<?> param) {
        return new SetCommand(configuration(), name, param);
    }

    @Override
    public CommentOnIsStep commentOnTable(String tableName) {
        return commentOnTable(name(tableName));
    }

    @Override
    public CommentOnIsStep commentOnTable(Name tableName) {
        return commentOnTable(table(tableName));
    }

    @Override
    public CommentOnIsStep commentOnTable(Table<?> table) {
        return new CommentOnImpl(configuration(), table, false);
    }

    @Override
    public CommentOnIsStep commentOnView(String viewName) {
        return commentOnView(name(viewName));
    }

    @Override
    public CommentOnIsStep commentOnView(Name viewName) {
        return commentOnView(table(viewName));
    }

    @Override
    public CommentOnIsStep commentOnView(Table<?> view) {
        return new CommentOnImpl(configuration(), view, true);
    }

    @Override
    public CommentOnIsStep commentOnColumn(Name columnName) {
        return commentOnColumn(field(columnName));
    }

    @Override
    public CommentOnIsStep commentOnColumn(Field<?> field) {
        return new CommentOnImpl(configuration(), field);
    }

    @Override
    public CreateViewAsStep<Record> createView(String view, String... fields) {
        return createView(table(name(view)), Tools.fieldsByName(view, fields));
    }

    @Override
    public CreateViewAsStep<Record> createView(Name view, Name... fields) {
        return createView(table(view), Tools.fieldsByName(fields));
    }

    @Override
    public CreateViewAsStep<Record> createView(Table<?> view, Field<?>... fields) {
        return new CreateViewImpl<>(configuration(), view, fields, false, false);
    }


    @Override
    public CreateViewAsStep<Record> createView(String view, Function<? super Field<?>, ? extends String> fieldNameFunction) {
        return createView(table(name(view)), (f, i) -> field(name(fieldNameFunction.apply(f))));
    }

    @Override
    public CreateViewAsStep<Record> createView(String view, BiFunction<? super Field<?>, ? super Integer, ? extends String> fieldNameFunction) {
        return createView(table(name(view)), (f, i) -> field(name(fieldNameFunction.apply(f, i))));
    }

    @Override
    public CreateViewAsStep<Record> createView(Name view, Function<? super Field<?>, ? extends Name> fieldNameFunction) {
        return createView(table(view), (f, i) -> field(fieldNameFunction.apply(f)));
    }

    @Override
    public CreateViewAsStep<Record> createView(Name view, BiFunction<? super Field<?>, ? super Integer, ? extends Name> fieldNameFunction) {
        return createView(table(view), (f, i) -> field(fieldNameFunction.apply(f, i)));
    }

    @Override
    public CreateViewAsStep<Record> createView(Table<?> view, Function<? super Field<?>, ? extends Field<?>> fieldNameFunction) {
        return createView(view, (f, i) -> fieldNameFunction.apply(f));
    }

    @Override
    public CreateViewAsStep<Record> createView(Table<?> view, BiFunction<? super Field<?>, ? super Integer, ? extends Field<?>> fieldNameFunction) {
        return new CreateViewImpl<>(configuration(), view, fieldNameFunction, false, false);
    }


    @Override
    public CreateViewAsStep<Record> createOrReplaceView(String view, String... fields) {
        return createOrReplaceView(table(name(view)), Tools.fieldsByName(view, fields));
    }

    @Override
    public CreateViewAsStep<Record> createOrReplaceView(Name view, Name... fields) {
        return createOrReplaceView(table(view), Tools.fieldsByName(fields));
    }

    @Override
    public CreateViewAsStep<Record> createOrReplaceView(Table<?> view, Field<?>... fields) {
        return new CreateViewImpl<>(configuration(), view, fields, false, true);
    }


    @Override
    public CreateViewAsStep<Record> createOrReplaceView(String view, Function<? super Field<?>, ? extends String> fieldNameFunction) {
        return createOrReplaceView(table(name(view)), (f, i) -> field(name(fieldNameFunction.apply(f))));
    }

    @Override
    public CreateViewAsStep<Record> createOrReplaceView(String view, BiFunction<? super Field<?>, ? super Integer, ? extends String> fieldNameFunction) {
        return createOrReplaceView(table(name(view)), (f, i) -> field(name(fieldNameFunction.apply(f, i))));
    }

    @Override
    public CreateViewAsStep<Record> createOrReplaceView(Name view, Function<? super Field<?>, ? extends Name> fieldNameFunction) {
        return createOrReplaceView(table(view), (f, i) -> field(fieldNameFunction.apply(f)));
    }

    @Override
    public CreateViewAsStep<Record> createOrReplaceView(Name view, BiFunction<? super Field<?>, ? super Integer, ? extends Name> fieldNameFunction) {
        return createOrReplaceView(table(view), (f, i) -> field(fieldNameFunction.apply(f, i)));
    }

    @Override
    public CreateViewAsStep<Record> createOrReplaceView(Table<?> view, Function<? super Field<?>, ? extends Field<?>> fieldNameFunction) {
        return createOrReplaceView(view, (f, i) -> fieldNameFunction.apply(f));
    }

    @Override
    public CreateViewAsStep<Record> createOrReplaceView(Table<?> view, BiFunction<? super Field<?>, ? super Integer, ? extends Field<?>> fieldNameFunction) {
        return new CreateViewImpl<>(configuration(), view, fieldNameFunction, false, true);
    }


    @Override
    public CreateViewAsStep<Record> createViewIfNotExists(String view, String... fields) {
        return createViewIfNotExists(table(name(view)), Tools.fieldsByName(view, fields));
    }

    @Override
    public CreateViewAsStep<Record> createViewIfNotExists(Name view, Name... fields) {
        return createViewIfNotExists(table(view), Tools.fieldsByName(fields));
    }

    @Override
    public CreateViewAsStep<Record> createViewIfNotExists(Table<?> view, Field<?>... fields) {
        return new CreateViewImpl<>(configuration(), view, fields, true, false);
    }


    @Override
    public CreateViewAsStep<Record> createViewIfNotExists(String view, Function<? super Field<?>, ? extends String> fieldNameFunction) {
        return createViewIfNotExists(table(name(view)), (f, i) -> field(name(fieldNameFunction.apply(f))));
    }

    @Override
    public CreateViewAsStep<Record> createViewIfNotExists(String view, BiFunction<? super Field<?>, ? super Integer, ? extends String> fieldNameFunction) {
        return createViewIfNotExists(table(name(view)), (f, i) -> field(name(fieldNameFunction.apply(f, i))));
    }

    @Override
    public CreateViewAsStep<Record> createViewIfNotExists(Name view, Function<? super Field<?>, ? extends Name> fieldNameFunction) {
        return createViewIfNotExists(table(view), (f, i) -> field(fieldNameFunction.apply(f)));
    }

    @Override
    public CreateViewAsStep<Record> createViewIfNotExists(Name view, BiFunction<? super Field<?>, ? super Integer, ? extends Name> fieldNameFunction) {
        return createViewIfNotExists(table(view), (f, i) -> field(fieldNameFunction.apply(f, i)));
    }

    @Override
    public CreateViewAsStep<Record> createViewIfNotExists(Table<?> view, Function<? super Field<?>, ? extends Field<?>> fieldNameFunction) {
        return createViewIfNotExists(view, (f, i) -> fieldNameFunction.apply(f));
    }

    @Override
    public CreateViewAsStep<Record> createViewIfNotExists(Table<?> view, BiFunction<? super Field<?>, ? super Integer, ? extends Field<?>> fieldNameFunction) {
        return new CreateViewImpl<>(configuration(), view, fieldNameFunction, true, false);
    }


    @Override
    public CreateSchemaFinalStep createSchema(String schema) {
        return createSchema(name(schema));
    }

    @Override
    public CreateSchemaFinalStep createSchema(Name schema) {
        return createSchema(schema(schema));
    }

    @Override
    public CreateSchemaFinalStep createSchema(Schema schema) {
        return new CreateSchemaImpl(configuration(), schema, false);
    }

    @Override
    public CreateSchemaFinalStep createSchemaIfNotExists(String schema) {
        return createSchemaIfNotExists(name(schema));
    }

    @Override
    public CreateSchemaFinalStep createSchemaIfNotExists(Name schema) {
        return createSchemaIfNotExists(schema(schema));
    }

    @Override
    public CreateSchemaFinalStep createSchemaIfNotExists(Schema schema) {
        return new CreateSchemaImpl(configuration(), schema, true);
    }

    @Override
    public CreateTableColumnStep createTable(String table) {
        return createTable(name(table));
    }

    @Override
    public CreateTableColumnStep createTable(Name table) {
        return createTable(table(table));
    }

    @Override
    public CreateTableColumnStep createTable(Table<?> table) {
        return new CreateTableImpl(configuration(), table, false, false);
    }

    @Override
    public CreateTableColumnStep createTableIfNotExists(String table) {
        return createTableIfNotExists(name(table));
    }

    @Override
    public CreateTableColumnStep createTableIfNotExists(Name table) {
        return createTableIfNotExists(table(table));
    }

    @Override
    public CreateTableColumnStep createTableIfNotExists(Table<?> table) {
        return new CreateTableImpl(configuration(), table, false, true);
    }

    @Override
    public CreateTableColumnStep createTemporaryTable(String table) {
        return createTemporaryTable(name(table));
    }

    @Override
    public CreateTableColumnStep createTemporaryTable(Name table) {
        return createTemporaryTable(table(table));
    }

    @Override
    public CreateTableColumnStep createTemporaryTable(Table<?> table) {
        return new CreateTableImpl(configuration(), table, true, false);
    }

    @Override
    public CreateTableColumnStep createTemporaryTableIfNotExists(String table) {
        return createTemporaryTableIfNotExists(name(table));
    }

    @Override
    public CreateTableColumnStep createTemporaryTableIfNotExists(Name table) {
        return createTemporaryTableIfNotExists(table(table));
    }

    @Override
    public CreateTableColumnStep createTemporaryTableIfNotExists(Table<?> table) {
        return new CreateTableImpl(configuration(), table, true, true);
    }

    @Override
    public CreateTableColumnStep createGlobalTemporaryTable(String table) {
        return createGlobalTemporaryTable(name(table));
    }

    @Override
    public CreateTableColumnStep createGlobalTemporaryTable(Name table) {
        return createGlobalTemporaryTable(table(table));
    }

    @Override
    public CreateTableColumnStep createGlobalTemporaryTable(Table<?> table) {
        return new CreateTableImpl(configuration(), table, true, false);
    }

    @Override
    public CreateTypeStep createType(String type) {
        return createType(name(type));
    }

    @Override
    public CreateTypeStep createType(Name type) {
        return new CreateTypeImpl(configuration(), type);
    }

    @Override
    public AlterTypeStep alterType(String type) {
        return alterType(name(type));
    }

    @Override
    public AlterTypeStep alterType(Name type) {
        return new AlterTypeImpl(configuration(), type);
    }

    @Override
    public DropTypeStep dropType(String type) {
        return dropType(name(type));
    }

    @Override
    public DropTypeStep dropType(Name type) {
        return dropType(Arrays.asList(type));
    }

    @Override
    public DropTypeStep dropType(String... type) {
        return dropType(Tools.names(type));
    }

    @Override
    public DropTypeStep dropType(Name... type) {
        return dropType(Arrays.asList(type));
    }

    @Override
    public DropTypeStep dropType(Collection<?> type) {
        return new DropTypeImpl(configuration(), type, false);
    }

    @Override
    public DropTypeStep dropTypeIfExists(String type) {
        return dropTypeIfExists(name(type));
    }

    @Override
    public DropTypeStep dropTypeIfExists(Name type) {
        return dropTypeIfExists(Arrays.asList(type));
    }

    @Override
    public DropTypeStep dropTypeIfExists(String... type) {
        return dropTypeIfExists(Tools.names(type));
    }

    @Override
    public DropTypeStep dropTypeIfExists(Name... type) {
        return dropTypeIfExists(Arrays.asList(type));
    }

    @Override
    public DropTypeStep dropTypeIfExists(Collection<?> type) {
        return new DropTypeImpl(configuration(), type, true);
    }

    @Override
    public CreateIndexStep createIndex() {
        return new CreateIndexImpl(configuration(), null, false, false);
    }

    @Override
    public CreateIndexStep createIndex(String index) {
        return createIndex(name(index));
    }

    @Override
    public CreateIndexStep createIndex(Name index) {
        return createIndex(index(index));
    }

    @Override
    public CreateIndexStep createIndex(Index index) {
        return new CreateIndexImpl(configuration(), index, index.getUnique(), false);
    }

    @Override
    public CreateIndexStep createIndexIfNotExists(String index) {
        return createIndexIfNotExists(name(index));
    }

    @Override
    public CreateIndexStep createIndexIfNotExists(Name index) {
        return createIndexIfNotExists(index(index));
    }

    @Override
    public CreateIndexStep createIndexIfNotExists(Index index) {
        return new CreateIndexImpl(configuration(), index, index.getUnique(), true);
    }

    @Override
    public CreateIndexStep createUniqueIndex() {
        return new CreateIndexImpl(configuration(), null, true, false);
    }

    @Override
    public CreateIndexStep createUniqueIndex(String index) {
        return createUniqueIndex(name(index));
    }

    @Override
    public CreateIndexStep createUniqueIndex(Name index) {
        return createUniqueIndex(index(index));
    }

    @Override
    public CreateIndexStep createUniqueIndex(Index index) {
        return new CreateIndexImpl(configuration(), index, true, false);
    }

    @Override
    public CreateIndexStep createUniqueIndexIfNotExists(String index) {
        return createUniqueIndexIfNotExists(name(index));
    }

    @Override
    public CreateIndexStep createUniqueIndexIfNotExists(Name index) {
        return createUniqueIndexIfNotExists(index(index));
    }

    @Override
    public CreateIndexStep createUniqueIndexIfNotExists(Index index) {
        return new CreateIndexImpl(configuration(), index, true, true);
    }

    @Override
    public CreateSequenceFlagsStep createSequence(String sequence) {
        return createSequence(name(sequence));
    }

    @Override
    public CreateSequenceFlagsStep createSequence(Name sequence) {
        return createSequence(sequence(sequence));
    }

    @Override
    public CreateSequenceFlagsStep createSequence(Sequence<?> sequence) {
        return new CreateSequenceImpl(configuration(), sequence, false);
    }

    @Override
    public CreateSequenceFlagsStep createSequenceIfNotExists(String sequence) {
        return createSequenceIfNotExists(name(sequence));
    }

    @Override
    public CreateSequenceFlagsStep createSequenceIfNotExists(Name sequence) {
        return createSequenceIfNotExists(sequence(sequence));
    }

    @Override
    public CreateSequenceFlagsStep createSequenceIfNotExists(Sequence<?> sequence) {
        return new CreateSequenceImpl(configuration(), sequence, true);
    }

    @Override
    public AlterSequenceStep<BigInteger> alterSequence(String sequence) {
        return alterSequence(name(sequence));
    }

    @Override
    public AlterSequenceStep<BigInteger> alterSequence(Name sequence) {
        return alterSequence(sequence(sequence));
    }

    @Override
    public <T extends Number> AlterSequenceStep<T> alterSequence(Sequence<T> sequence) {
        return new AlterSequenceImpl<>(configuration(), sequence);
    }

    @Override
    public AlterSequenceStep<BigInteger> alterSequenceIfExists(String sequence) {
        return alterSequenceIfExists(name(sequence));
    }

    @Override
    public AlterSequenceStep<BigInteger> alterSequenceIfExists(Name sequence) {
        return alterSequenceIfExists(sequence(sequence));
    }

    @Override
    public <T extends Number> AlterSequenceStep<T> alterSequenceIfExists(Sequence<T> sequence) {
        return new AlterSequenceImpl<>(configuration(), sequence, true);
    }

    @Override
    public AlterTableStep alterTable(String table) {
        return alterTable(name(table));
    }

    @Override
    public AlterTableStep alterTable(Name table) {
        return alterTable(table(table));
    }

    @Override
    public AlterTableStep alterTable(Table<?> table) {
        return new AlterTableImpl(configuration(), table);
    }

    @Override
    public AlterTableStep alterTableIfExists(String table) {
        return alterTableIfExists(name(table));
    }

    @Override
    public AlterTableStep alterTableIfExists(Name table) {
        return alterTableIfExists(table(table));
    }

    @Override
    public AlterTableStep alterTableIfExists(Table<?> table) {
        return new AlterTableImpl(configuration(), table, true);
    }

    @Override
    public AlterSchemaStep alterSchema(String schema) {
        return alterSchema(name(schema));
    }

    @Override
    public AlterSchemaStep alterSchema(Name schema) {
        return alterSchema(schema(schema));
    }

    @Override
    public AlterSchemaStep alterSchema(Schema schema) {
        return new AlterSchemaImpl(configuration(), schema);
    }

    @Override
    public AlterSchemaStep alterSchemaIfExists(String schema) {
        return alterSchemaIfExists(name(schema));
    }

    @Override
    public AlterSchemaStep alterSchemaIfExists(Name schema) {
        return alterSchemaIfExists(schema(schema));
    }

    @Override
    public AlterSchemaStep alterSchemaIfExists(Schema schema) {
        return new AlterSchemaImpl(configuration(), schema, true);
    }

    @Override
    public AlterViewStep alterView(String table) {
        return alterView(name(table));
    }

    @Override
    public AlterViewStep alterView(Name table) {
        return alterView(table(table));
    }

    @Override
    public AlterViewStep alterView(Table<?> table) {
        return new AlterViewImpl(configuration(), table);
    }

    @Override
    public AlterViewStep alterViewIfExists(String table) {
        return alterViewIfExists(name(table));
    }

    @Override
    public AlterViewStep alterViewIfExists(Name table) {
        return alterViewIfExists(table(table));
    }

    @Override
    public AlterViewStep alterViewIfExists(Table<?> table) {
        return new AlterViewImpl(configuration(), table, true);
    }

    @Override
    public AlterIndexOnStep alterIndex(String index) {
        return alterIndex(name(index));
    }

    @Override
    public AlterIndexOnStep alterIndex(Name index) {
        return alterIndex(index(index));
    }

    @Override
    public AlterIndexOnStep alterIndex(Index index) {
        return new AlterIndexImpl(configuration(), index);
    }

    @Override
    public AlterIndexStep alterIndexIfExists(String index) {
        return alterIndexIfExists(name(index));
    }

    @Override
    public AlterIndexStep alterIndexIfExists(Name index) {
        return alterIndexIfExists(index(index));
    }

    @Override
    public AlterIndexStep alterIndexIfExists(Index index) {
        return new AlterIndexImpl(configuration(), index, true);
    }

    @Override
    public DropSchemaStep dropSchema(String schema) {
        return dropSchema(name(schema));
    }

    @Override
    public DropSchemaStep dropSchema(Name schema) {
        return dropSchema(schema(schema));
    }

    @Override
    public DropSchemaStep dropSchema(Schema schema) {
        return new DropSchemaImpl(configuration(), schema);
    }

    @Override
    public DropSchemaStep dropSchemaIfExists(String schema) {
        return dropSchemaIfExists(name(schema));
    }

    @Override
    public DropSchemaStep dropSchemaIfExists(Name schema) {
        return dropSchemaIfExists(schema(schema));
    }

    @Override
    public DropSchemaStep dropSchemaIfExists(Schema schema) {
        return new DropSchemaImpl(configuration(), schema, true);
    }

    @Override
    public DropViewFinalStep dropView(String view) {
        return dropView(name(view));
    }

    @Override
    public DropViewFinalStep dropView(Name view) {
        return dropView(table(view));
    }

    @Override
    public DropViewFinalStep dropView(Table<?> view) {
        return new DropViewImpl(configuration(), view);
    }

    @Override
    public DropViewFinalStep dropViewIfExists(String view) {
        return dropViewIfExists(name(view));
    }

    @Override
    public DropViewFinalStep dropViewIfExists(Name view) {
        return dropViewIfExists(table(view));
    }

    @Override
    public DropViewFinalStep dropViewIfExists(Table<?> view) {
        return new DropViewImpl(configuration(), view, true);
    }

    @Override
    public DropTableStep dropTable(String table) {
        return dropTable(name(table));
    }

    @Override
    public DropTableStep dropTable(Name table) {
        return dropTable(table(table));
    }

    @Override
    public DropTableStep dropTable(Table<?> table) {
        return new DropTableImpl(configuration(), table);
    }

    @Override
    public DropTableStep dropTemporaryTable(String table) {
        return dropTemporaryTable(name(table));
    }

    @Override
    public DropTableStep dropTemporaryTable(Name table) {
        return dropTemporaryTable(table(table));
    }

    @Override
    public DropTableStep dropTemporaryTable(Table<?> table) {
        return new DropTableImpl(configuration(), table, false, true);
    }

    @Override
    public DropTableStep dropTemporaryTableIfExists(String table) {
        return dropTemporaryTableIfExists(name(table));
    }

    @Override
    public DropTableStep dropTemporaryTableIfExists(Name table) {
        return dropTemporaryTableIfExists(table(table));
    }

    @Override
    public DropTableStep dropTemporaryTableIfExists(Table<?> table) {
        return new DropTableImpl(configuration(), table, true, true);
    }

    @Override
    public DropTableStep dropTableIfExists(String table) {
        return dropTableIfExists(name(table));
    }

    @Override
    public DropTableStep dropTableIfExists(Name table) {
        return dropTableIfExists(table(table));
    }

    @Override
    public DropTableStep dropTableIfExists(Table<?> table) {
        return new DropTableImpl(configuration(), table, true);
    }

    @Override
    public DropIndexOnStep dropIndex(String index) {
        return dropIndex(name(index));
    }

    @Override
    public DropIndexOnStep dropIndex(Name index) {
        return dropIndex(index(index));
    }

    @Override
    public DropIndexOnStep dropIndex(Index index) {
        return new DropIndexImpl(configuration(), index);
    }

    @Override
    public DropIndexOnStep dropIndexIfExists(String index) {
        return dropIndexIfExists(name(index));
    }

    @Override
    public DropIndexOnStep dropIndexIfExists(Name index) {
        return dropIndexIfExists(index(index));
    }

    @Override
    public DropIndexOnStep dropIndexIfExists(Index index) {
        return new DropIndexImpl(configuration(), index, true);
    }

    @Override
    public DropSequenceFinalStep dropSequence(String sequence) {
        return dropSequence(name(sequence));
    }

    @Override
    public DropSequenceFinalStep dropSequence(Name sequence) {
        return dropSequence(sequence(sequence));
    }

    @Override
    public DropSequenceFinalStep dropSequence(Sequence<?> sequence) {
        return new DropSequenceImpl(configuration(), sequence);
    }

    @Override
    public DropSequenceFinalStep dropSequenceIfExists(String sequence) {
        return dropSequenceIfExists(name(sequence));
    }

    @Override
    public DropSequenceFinalStep dropSequenceIfExists(Name sequence) {
        return dropSequenceIfExists(sequence(sequence));
    }

    @Override
    public DropSequenceFinalStep dropSequenceIfExists(Sequence<?> sequence) {
        return new DropSequenceImpl(configuration(), sequence, true);
    }

    @Override
    public final TruncateIdentityStep<Record> truncate(String table) {
        return truncateTable(table);
    }

    @Override
    public final TruncateIdentityStep<Record> truncate(Name table) {
        return truncateTable(table);
    }

    @Override
    public <R extends Record> TruncateIdentityStep<R> truncate(Table<R> table) {
        return truncateTable(table);
    }

    @Override
    public final TruncateIdentityStep<Record> truncateTable(String table) {
        return truncateTable(name(table));
    }

    @Override
    public final TruncateIdentityStep<Record> truncateTable(Name table) {
        return truncateTable(table(table));
    }

    @Override
    public <R extends Record> TruncateIdentityStep<R> truncateTable(Table<R> table) {
        return new TruncateImpl<>(configuration(), table);
    }

    // -------------------------------------------------------------------------
    // XXX Access control
    // -------------------------------------------------------------------------

    @Override
    public GrantOnStep grant(Privilege privilege) {
        return grant(Arrays.asList(privilege));
    }

    @Override
    public GrantOnStep grant(Privilege... privileges) {
        return grant(Arrays.asList(privileges));
    }

    @Override
    public GrantOnStep grant(Collection<? extends Privilege> privileges) {
        return new GrantImpl(configuration(), privileges);
    }

    @Override
    public RevokeOnStep revoke(Privilege privilege) {
        return revoke(Arrays.asList(privilege));
    }

    @Override
    public RevokeOnStep revoke(Privilege... privileges) {
        return revoke(Arrays.asList(privileges));
    }

    @Override
    public RevokeOnStep revoke(Collection<? extends Privilege> privileges) {
        return new RevokeImpl(configuration(), privileges);
    }

    @Override
    public RevokeOnStep revokeGrantOptionFor(Privilege privilege) {
        return revokeGrantOptionFor(Arrays.asList(privilege));
    }

    @Override
    public RevokeOnStep revokeGrantOptionFor(Privilege... privileges) {
        return revokeGrantOptionFor(Arrays.asList(privileges));
    }

    @Override
    public RevokeOnStep revokeGrantOptionFor(Collection<? extends Privilege> privileges) {
        return new RevokeImpl(configuration(), privileges, true);
    }

    // -------------------------------------------------------------------------
    // XXX Other queries for identites and sequences
    // -------------------------------------------------------------------------

    @Override
    public BigInteger lastID() {
        switch (configuration().family()) {
            case DERBY: {
                Field<BigInteger> field = field("identity_val_local()", BigInteger.class);
                return select(field).fetchOne(field);
            }

            case H2:
            case HSQLDB: {
                Field<BigInteger> field = field("identity()", BigInteger.class);
                return select(field).fetchOne(field);
            }







            case CUBRID:
            case MARIADB:
            case MYSQL: {
                Field<BigInteger> field = field("last_insert_id()", BigInteger.class);
                return select(field).fetchOne(field);
            }

            case SQLITE: {
                Field<BigInteger> field = field("last_insert_rowid()", BigInteger.class);
                return select(field).fetchOne(field);
            }






            case POSTGRES: {
                Field<BigInteger> field = field("lastval()", BigInteger.class);
                return select(field).fetchOne(field);
            }





















            default:
                throw new SQLDialectNotSupportedException("identity functionality not supported by " + configuration().dialect());
        }
    }

    @Override
    public BigInteger nextval(String sequence) {
        return nextval(name(sequence));
    }

    @Override
    public BigInteger nextval(Name sequence) {
        return nextval(sequence(sequence));
    }

    @Override
    public <T extends Number> T nextval(Sequence<T> sequence) {
        Field<T> nextval = sequence.nextval();
        return select(nextval).fetchOne(nextval);
    }

    @Override
    public BigInteger currval(String sequence) {
        return currval(name(sequence));
    }

    @Override
    public BigInteger currval(Name sequence) {
        return currval(sequence(sequence));
    }

    @Override
    public <T extends Number> T currval(Sequence<T> sequence) {
        Field<T> currval = sequence.currval();
        return select(currval).fetchOne(currval);
    }

    // -------------------------------------------------------------------------
    // XXX Global Record factory
    // -------------------------------------------------------------------------

    @Override
    public Record newRecord(Field<?>... fields) {
        return Tools.newRecord(false, RecordImplN.class, fields, configuration()).<RuntimeException>operate(null);
    }

    @Override
    public Record newRecord(Collection<? extends Field<?>> fields) {
        return newRecord(fields.toArray(EMPTY_FIELD));
    }



    @Override
    public <T1> Record1<T1> newRecord(Field<T1> field1) {
        return (Record1) newRecord(new Field[] { field1 });
    }

    @Override
    public <T1, T2> Record2<T1, T2> newRecord(Field<T1> field1, Field<T2> field2) {
        return (Record2) newRecord(new Field[] { field1, field2 });
    }

    @Override
    public <T1, T2, T3> Record3<T1, T2, T3> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return (Record3) newRecord(new Field[] { field1, field2, field3 });
    }

    @Override
    public <T1, T2, T3, T4> Record4<T1, T2, T3, T4> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return (Record4) newRecord(new Field[] { field1, field2, field3, field4 });
    }

    @Override
    public <T1, T2, T3, T4, T5> Record5<T1, T2, T3, T4, T5> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return (Record5) newRecord(new Field[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> Record6<T1, T2, T3, T4, T5, T6> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return (Record6) newRecord(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7> Record7<T1, T2, T3, T4, T5, T6, T7> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return (Record7) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8> Record8<T1, T2, T3, T4, T5, T6, T7, T8> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return (Record8) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9> Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return (Record9) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return (Record10) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return (Record11) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return (Record12) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return (Record13) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return (Record14) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return (Record15) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return (Record16) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return (Record17) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return (Record18) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return (Record19) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return (Record20) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return (Record21) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> newRecord(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return (Record22) newRecord(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }



    @Override
    public <R extends UDTRecord<R>> R newRecord(UDT<R> type) {
        return Tools.newRecord(false, type, configuration()).<RuntimeException>operate(null);
    }

    @Override
    public <R extends Record> R newRecord(Table<R> table) {
        return Tools.newRecord(false, table, configuration()).<RuntimeException>operate(null);
    }

    @Override
    public <R extends Record> R newRecord(Table<R> table, final Object source) {
        return Tools.newRecord(false, table, configuration())
                    .operate(new RecordOperation<R, RuntimeException>() {

            @Override
            public R operate(R record) {
                record.from(source);
                return record;
            }
        });
    }

    @Override
    public <R extends Record> Result<R> newResult(Table<R> table) {
        return new ResultImpl<>(configuration(), table.fields());
    }

    @Override
    public Result<Record> newResult(Field<?>... fields) {
        return new ResultImpl<>(configuration(), fields);
    }

    @Override
    public Result<Record> newResult(Collection<? extends Field<?>> fields) {
        return new ResultImpl<>(configuration(), fields);
    }



    @Override
    public <T1> Result<Record1<T1>> newResult(Field<T1> field1) {
        return (Result) newResult(new Field[] { field1 });
    }

    @Override
    public <T1, T2> Result<Record2<T1, T2>> newResult(Field<T1> field1, Field<T2> field2) {
        return (Result) newResult(new Field[] { field1, field2 });
    }

    @Override
    public <T1, T2, T3> Result<Record3<T1, T2, T3>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return (Result) newResult(new Field[] { field1, field2, field3 });
    }

    @Override
    public <T1, T2, T3, T4> Result<Record4<T1, T2, T3, T4>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4 });
    }

    @Override
    public <T1, T2, T3, T4, T5> Result<Record5<T1, T2, T3, T4, T5>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> Result<Record6<T1, T2, T3, T4, T5, T6>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7> Result<Record7<T1, T2, T3, T4, T5, T6, T7>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8> Result<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9> Result<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Result<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Result<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Result<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Result<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Result<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Result<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Result<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Result<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Result<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Result<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Result<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Result<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Result<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> newResult(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return (Result) newResult(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }



    // -------------------------------------------------------------------------
    // XXX Executing queries
    // -------------------------------------------------------------------------

    @Override
    public <R extends Record> Result<R> fetch(ResultQuery<R> query) {
        final Configuration previous = Tools.getConfiguration(query);

        try {
            query.attach(configuration());
            return query.fetch();
        }
        finally {
            query.attach(previous);
        }
    }

    @Override
    public <R extends Record> Cursor<R> fetchLazy(ResultQuery<R> query) {
        final Configuration previous = Tools.getConfiguration(query);

        try {
            query.attach(configuration());
            return query.fetchLazy();
        }
        finally {
            query.attach(previous);
        }
    }



    @Override
    public <R extends Record> CompletionStage<Result<R>> fetchAsync(ResultQuery<R> query) {
        final Configuration previous = Tools.getConfiguration(query);

        try {
            query.attach(configuration());
            return query.fetchAsync();
        }
        finally {
            query.attach(previous);
        }
    }

    @Override
    public <R extends Record> CompletionStage<Result<R>> fetchAsync(Executor executor, ResultQuery<R> query) {
        final Configuration previous = Tools.getConfiguration(query);

        try {
            query.attach(configuration());
            return query.fetchAsync(executor);
        }
        finally {
            query.attach(previous);
        }
    }

    @Override
    public <R extends Record> Stream<R> fetchStream(ResultQuery<R> query) {
        final Configuration previous = Tools.getConfiguration(query);

        try {
            query.attach(configuration());
            return query.stream();
        }
        finally {
            query.attach(previous);
        }
    }



    @Override
    public <R extends Record> Results fetchMany(ResultQuery<R> query) {
        final Configuration previous = Tools.getConfiguration(query);

        try {
            query.attach(configuration());
            return query.fetchMany();
        }
        finally {
            query.attach(previous);
        }
    }

    @Override
    public <R extends Record> R fetchOne(ResultQuery<R> query) {
        final Configuration previous = Tools.getConfiguration(query);

        try {
            query.attach(configuration());
            return query.fetchOne();
        }
        finally {
            query.attach(previous);
        }
    }

    @Override
    public <R extends Record> R fetchSingle(ResultQuery<R> query) {
        final Configuration previous = Tools.getConfiguration(query);

        try {
            query.attach(configuration());
            return query.fetchSingle();
        }
        finally {
            query.attach(previous);
        }
    }


    @Override
    public <R extends Record> Optional<R> fetchOptional(ResultQuery<R> query) {
        return Optional.ofNullable(fetchOne(query));
    }


    @Override
    public <T> T fetchValue(Table<? extends Record1<T>> table) {
        return fetchValue(selectFrom(table));
    }

    @Override
    public <T, R extends Record1<T>> T fetchValue(ResultQuery<R> query) {
        final Configuration previous = Tools.getConfiguration(query);

        try {
            query.attach(configuration());
            return value1(fetchOne(query));
        }
        finally {
            query.attach(previous);
        }
    }

    @Override
    public <T> T fetchValue(TableField<?, T> field) {
        return fetchValue(select(field).from(field.getTable()));
    }

    @Override
    public <T> T fetchValue(Field<T> field) {
        return fetchValue(select(field));
    }


    @Override
    public <T, R extends Record1<T>> Optional<T> fetchOptionalValue(ResultQuery<R> query) {
        return Optional.ofNullable(fetchValue(query));
    }

    @Override
    public <T> Optional<T> fetchOptionalValue(TableField<?, T> field) {
        return Optional.ofNullable(fetchValue(field));
    }


    @Override
    public <T> List<T> fetchValues(Table<? extends Record1<T>> table) {
        return fetchValues(selectFrom(table));
    }

    @Override
    public <T, R extends Record1<T>> List<T> fetchValues(ResultQuery<R> query) {
        return (List) fetch(query).getValues(0);
    }

    @Override
    public <T> List<T> fetchValues(TableField<?, T> field) {
        return fetchValues(select(field).from(field.getTable()));
    }

    private final <T, R extends Record1<T>> T value1(R record) {
        if (record == null)
            return null;

        if (record.size() != 1)
            throw new InvalidResultException("Record contains more than one value : " + record);

        return record.value1();
    }

    @Override
    public <R extends TableRecord<R>> Result<R> fetchByExample(R example) {
        return selectFrom(example.getTable())
              .where(condition(example))
              .fetch();
    }

    @Override
    public int fetchCount(Select<?> query) {
        return new FetchCount(configuration(), query).fetchOne().value1();
    }

    @Override
    public int fetchCount(Table<?> table) {
        return fetchCount(table, noCondition());
    }

    @Override
    public int fetchCount(Table<?> table, Condition condition) {
        return selectCount().from(table).where(condition).fetchOne(0, int.class);
    }

    @Override
    public int fetchCount(Table<?> table, Condition... conditions) {
        return fetchCount(table, DSL.and(conditions));
    }

    @Override
    public int fetchCount(Table<?> table, Collection<? extends Condition> conditions) {
        return fetchCount(table, DSL.and(conditions));
    }

    @Override
    public boolean fetchExists(Select<?> query) {
        return selectOne().whereExists(query).fetchOne() != null;
    }

    @Override
    public boolean fetchExists(Table<?> table) {
        return fetchExists(table, noCondition());
    }

    @Override
    public boolean fetchExists(Table<?> table, Condition condition) {
        return fetchExists(selectOne().from(table).where(condition));
    }

    @Override
    public boolean fetchExists(Table<?> table, Condition... conditions) {
        return fetchExists(table, DSL.and(conditions));
    }

    @Override
    public boolean fetchExists(Table<?> table, Collection<? extends Condition> conditions) {
        return fetchExists(table, DSL.and(conditions));
    }

    @Override
    public int execute(Query query) {
        final Configuration previous = Tools.getConfiguration(query);

        try {
            query.attach(configuration());
            return query.execute();
        }
        finally {
            query.attach(previous);
        }
    }

    // -------------------------------------------------------------------------
    // XXX Fast querying
    // -------------------------------------------------------------------------

    @Override
    public <R extends Record> Result<R> fetch(Table<R> table) {
        return fetch(table, noCondition());
    }

    @Override
    public <R extends Record> Result<R> fetch(Table<R> table, Condition condition) {
        return selectFrom(table).where(condition).fetch();
    }

    @Override
    public <R extends Record> Result<R> fetch(Table<R> table, Condition... conditions) {
        return fetch(table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> Result<R> fetch(Table<R> table, Collection<? extends Condition> conditions) {
        return fetch(table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> R fetchOne(Table<R> table) {
        return fetchOne(table, noCondition());
    }

    @Override
    public <R extends Record> R fetchOne(Table<R> table, Condition condition) {
        return Tools.fetchOne(fetchLazy(table, condition));
    }

    @Override
    public <R extends Record> R fetchOne(Table<R> table, Condition... conditions) {
        return fetchOne(table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> R fetchOne(Table<R> table, Collection<? extends Condition> conditions) {
        return fetchOne(table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> R fetchSingle(Table<R> table) {
        return fetchSingle(table, noCondition());
    }

    @Override
    public <R extends Record> R fetchSingle(Table<R> table, Condition condition) {
        return Tools.fetchSingle(fetchLazy(table, condition));
    }

    @Override
    public <R extends Record> R fetchSingle(Table<R> table, Condition... conditions) {
        return fetchSingle(table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> R fetchSingle(Table<R> table, Collection<? extends Condition> conditions) {
        return fetchSingle(table, DSL.and(conditions));
    }

    @Override
    public Record fetchSingle(SelectField<?>... fields) throws DataAccessException {
        return fetchSingle(Arrays.asList(fields));
    }

    @Override
    public Record fetchSingle(Collection<? extends SelectField<?>> fields) throws DataAccessException {
        return fetchSingle(DSL.select(fields));
    }



    @Override
    public <T1> Record1<T1> fetchSingle(SelectField<T1> field1) {
        return (Record1<T1>) fetchSingle(new SelectField[] { field1 });
    }

    @Override
    public <T1, T2> Record2<T1, T2> fetchSingle(SelectField<T1> field1, SelectField<T2> field2) {
        return (Record2<T1, T2>) fetchSingle(new SelectField[] { field1, field2 });
    }

    @Override
    public <T1, T2, T3> Record3<T1, T2, T3> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3) {
        return (Record3<T1, T2, T3>) fetchSingle(new SelectField[] { field1, field2, field3 });
    }

    @Override
    public <T1, T2, T3, T4> Record4<T1, T2, T3, T4> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4) {
        return (Record4<T1, T2, T3, T4>) fetchSingle(new SelectField[] { field1, field2, field3, field4 });
    }

    @Override
    public <T1, T2, T3, T4, T5> Record5<T1, T2, T3, T4, T5> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5) {
        return (Record5<T1, T2, T3, T4, T5>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> Record6<T1, T2, T3, T4, T5, T6> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6) {
        return (Record6<T1, T2, T3, T4, T5, T6>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7> Record7<T1, T2, T3, T4, T5, T6, T7> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7) {
        return (Record7<T1, T2, T3, T4, T5, T6, T7>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8> Record8<T1, T2, T3, T4, T5, T6, T7, T8> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8) {
        return (Record8<T1, T2, T3, T4, T5, T6, T7, T8>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9> Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9) {
        return (Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10) {
        return (Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11) {
        return (Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12) {
        return (Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13) {
        return (Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14) {
        return (Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15) {
        return (Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16) {
        return (Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17) {
        return (Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18) {
        return (Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19) {
        return (Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20) {
        return (Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21) {
        return (Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> fetchSingle(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21, SelectField<T22> field22) {
        return (Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>) fetchSingle(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }




    @Override
    public <R extends Record> Optional<R> fetchOptional(Table<R> table) {
        return fetchOptional(table, noCondition());
    }

    @Override
    public <R extends Record> Optional<R> fetchOptional(Table<R> table, Condition condition) {
        return Optional.ofNullable(fetchOne(table, condition));
    }

    @Override
    public <R extends Record> Optional<R> fetchOptional(Table<R> table, Condition... conditions) {
        return fetchOptional(table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> Optional<R> fetchOptional(Table<R> table, Collection<? extends Condition> conditions) {
        return fetchOptional(table, DSL.and(conditions));
    }


    @Override
    public <R extends Record> R fetchAny(Table<R> table) {
        return fetchAny(table, noCondition());
    }

    @Override
    public <R extends Record> R fetchAny(Table<R> table, Condition condition) {
        return selectFrom(table).where(condition).limit(1).fetchOne();
    }

    @Override
    public <R extends Record> R fetchAny(Table<R> table, Condition... conditions) {
        return fetchAny(table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> R fetchAny(Table<R> table, Collection<? extends Condition> conditions) {
        return fetchAny(table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> Cursor<R> fetchLazy(Table<R> table) {
        return fetchLazy(table, noCondition());
    }

    @Override
    public <R extends Record> Cursor<R> fetchLazy(Table<R> table, Condition condition) {
        return selectFrom(table).where(condition).fetchLazy();
    }

    @Override
    public <R extends Record> Cursor<R> fetchLazy(Table<R> table, Condition... conditions) {
        return fetchLazy(table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> Cursor<R> fetchLazy(Table<R> table, Collection<? extends Condition> conditions) {
        return fetchLazy(table, DSL.and(conditions));
    }



    @Override
    public <R extends Record> CompletionStage<Result<R>> fetchAsync(Table<R> table) {
        return fetchAsync(table, noCondition());
    }

    @Override
    public <R extends Record> CompletionStage<Result<R>> fetchAsync(Table<R> table, Condition condition) {
        return selectFrom(table).where(condition).fetchAsync();
    }

    @Override
    public <R extends Record> CompletionStage<Result<R>> fetchAsync(Table<R> table, Condition... conditions) {
        return fetchAsync(table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> CompletionStage<Result<R>> fetchAsync(Table<R> table, Collection<? extends Condition> conditions) {
        return fetchAsync(table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> CompletionStage<Result<R>> fetchAsync(Executor executor, Table<R> table) {
        return fetchAsync(executor, table, noCondition());
    }

    @Override
    public <R extends Record> CompletionStage<Result<R>> fetchAsync(Executor executor, Table<R> table, Condition condition) {
        return selectFrom(table).where(condition).fetchAsync(executor);
    }

    @Override
    public <R extends Record> CompletionStage<Result<R>> fetchAsync(Executor executor, Table<R> table, Condition... conditions) {
        return fetchAsync(executor, table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> CompletionStage<Result<R>> fetchAsync(Executor executor, Table<R> table, Collection<? extends Condition> conditions) {
        return fetchAsync(executor, table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> Stream<R> fetchStream(Table<R> table) {
        return fetchStream(table, noCondition());
    }

    @Override
    public <R extends Record> Stream<R> fetchStream(Table<R> table, Condition condition) {
        return selectFrom(table).where(condition).stream();
    }

    @Override
    public <R extends Record> Stream<R> fetchStream(Table<R> table, Condition... conditions) {
        return fetchStream(table, DSL.and(conditions));
    }

    @Override
    public <R extends Record> Stream<R> fetchStream(Table<R> table, Collection<? extends Condition> conditions) {
        return fetchStream(table, DSL.and(conditions));
    }



    @Override
    public int executeInsert(TableRecord<?> record) {
        InsertQuery insert = insertQuery(record.getTable());
        insert.setRecord(record);
        return insert.execute();
    }

    @Override
    public  int executeUpdate(UpdatableRecord<?> record) {
        UpdateQuery update = updateQuery(record.getTable());
        Tools.addConditions(update, record, record.getTable().getPrimaryKey().getFieldsArray());
        update.setRecord(record);
        return update.execute();
    }

    @Override
    public int executeUpdate(TableRecord<?> record, Condition condition) {
        UpdateQuery update = updateQuery(record.getTable());
        update.addConditions(condition);
        update.setRecord(record);
        return update.execute();
    }

    @Override
    public int executeDelete(UpdatableRecord<?> record) {
        DeleteQuery delete = deleteQuery(record.getTable());
        Tools.addConditions(delete, record, record.getTable().getPrimaryKey().getFieldsArray());
        return delete.execute();
    }

    @Override
    public int executeDelete(TableRecord<?> record, Condition condition) {
        DeleteQuery delete = deleteQuery(record.getTable());
        delete.addConditions(condition);
        return delete.execute();
    }

    // -------------------------------------------------------------------------
    // XXX Static initialisation of dialect-specific data types
    // -------------------------------------------------------------------------

    static {
        // Load all dialect-specific data types
        // TODO [#650] Make this more reliable using a data type registry

        try {
            Class.forName(SQLDataType.class.getName());
        } catch (Exception ignore) {}
    }

    // -------------------------------------------------------------------------
    // XXX Internals
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return configuration().toString();
    }
}
