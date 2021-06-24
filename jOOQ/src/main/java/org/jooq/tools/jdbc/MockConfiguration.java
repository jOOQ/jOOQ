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
package org.jooq.tools.jdbc;

import java.sql.Connection;
import java.time.Clock;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Function;

import javax.sql.DataSource;

import org.jooq.CacheProvider;
import org.jooq.CharsetProvider;
import org.jooq.CommitProvider;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.ConverterProvider;
import org.jooq.DSLContext;
import org.jooq.DiagnosticsListenerProvider;
import org.jooq.ExecuteListenerProvider;
import org.jooq.ExecutorProvider;
import org.jooq.MetaProvider;
import org.jooq.MigrationListenerProvider;
// ...
// ...
import org.jooq.RecordListenerProvider;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordUnmapper;
import org.jooq.RecordUnmapperProvider;
import org.jooq.SQLDialect;
import org.jooq.TransactionListenerProvider;
import org.jooq.TransactionProvider;
import org.jooq.Unwrapper;
import org.jooq.UnwrapperProvider;
import org.jooq.VisitListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.impl.AbstractConfiguration;
import org.jooq.impl.DefaultDSLContext;

import io.r2dbc.spi.ConnectionFactory;

/**
 * A mock configuration.
 * <p>
 * This {@link Configuration} wraps a delegate <code>Configuration</code> and
 * wraps all {@link ConnectionProvider} references in
 * {@link MockConnectionProvider}.
 *
 * @author Lukas Eder
 */
public class MockConfiguration extends AbstractConfiguration {

    private final Configuration    delegate;
    private final MockDataProvider provider;

    public MockConfiguration(Configuration delegate, MockDataProvider provider) {
        this.delegate = delegate;
        this.provider = provider;
    }

    @Override
    public DSLContext dsl() {
        return new DefaultDSLContext(this);
    }

    @Override
    public Map<Object, Object> data() {
        return delegate.data();
    }

    @Override
    public Object data(Object key) {
        return delegate.data(key);
    }

    @Override
    public Object data(Object key, Object value) {
        return delegate.data(key, value);
    }

    @Override
    public Clock clock() {
        return delegate.clock();
    }

    @Override
    public ConnectionProvider connectionProvider() {
        return new MockConnectionProvider(delegate.connectionProvider(), provider);
    }

    @Override
    public ConnectionProvider interpreterConnectionProvider() {
        return new MockConnectionProvider(delegate.interpreterConnectionProvider(), provider);
    }

    @Override
    public ConnectionProvider systemConnectionProvider() {
        return new MockConnectionProvider(delegate.systemConnectionProvider(), provider);
    }

    @Override
    public ConnectionFactory connectionFactory() {
        throw new UnsupportedOperationException("TODO: Add support for MockConnectionFactory");
    }

    @Override
    public MetaProvider metaProvider() {
        return delegate.metaProvider();
    }

    @Override
    public CommitProvider commitProvider() {
        return delegate.commitProvider();
    }

    @Override
    public ExecutorProvider executorProvider() {
        return delegate.executorProvider();
    }

    @Override
    public CacheProvider cacheProvider() {
        return delegate.cacheProvider();
    }

    @Override
    public TransactionProvider transactionProvider() {
        return delegate.transactionProvider();
    }

    @Override
    public RecordMapperProvider recordMapperProvider() {
        return delegate.recordMapperProvider();
    }

    @Override
    public RecordUnmapperProvider recordUnmapperProvider() {
        return delegate.recordUnmapperProvider();
    }

    @Override
    public RecordListenerProvider[] recordListenerProviders() {
        return delegate.recordListenerProviders();
    }

    @Override
    public ExecuteListenerProvider[] executeListenerProviders() {
        return delegate.executeListenerProviders();
    }

    @Override
    public MigrationListenerProvider[] migrationListenerProviders() {
        return delegate.migrationListenerProviders();
    }

    @Override
    public VisitListenerProvider[] visitListenerProviders() {
        return delegate.visitListenerProviders();
    }

    @Override
    public TransactionListenerProvider[] transactionListenerProviders() {
        return delegate.transactionListenerProviders();
    }

    @Override
    public DiagnosticsListenerProvider[] diagnosticsListenerProviders() {
        return delegate.diagnosticsListenerProviders();
    }











    @Override
    public UnwrapperProvider unwrapperProvider() {
        return delegate.unwrapperProvider();
    }

    @Override
    public CharsetProvider charsetProvider() {
        return delegate.charsetProvider();
    }

    @Override
    public ConverterProvider converterProvider() {
        return delegate.converterProvider();
    }

    @Override
    public org.jooq.SchemaMapping schemaMapping() {
        return delegate.schemaMapping();
    }

    @Override
    public SQLDialect dialect() {
        return delegate.dialect();
    }

    @Override
    public SQLDialect family() {
        return delegate.family();
    }

    @Override
    public Settings settings() {
        return delegate.settings();
    }

    @Override
    public Configuration set(Clock newClock) {
        delegate.set(newClock);
        return this;
    }

    @Override
    public Configuration set(ConnectionProvider newConnectionProvider) {
        delegate.set(newConnectionProvider);
        return this;
    }

    @Override
    public Configuration set(MetaProvider newMetaProvider) {
        delegate.set(newMetaProvider);
        return this;
    }

    @Override
    public Configuration set(CommitProvider newCommitProvider) {
        delegate.set(newCommitProvider);
        return this;
    }

    @Override
    public Configuration set(Connection newConnection) {
        delegate.set(newConnection);
        return this;
    }

    @Override
    public Configuration set(DataSource newDataSource) {
        delegate.set(newDataSource);
        return this;
    }

    @Override
    public Configuration set(ConnectionFactory newConnectionFactory) {
        delegate.set(newConnectionFactory);
        return this;
    }

    @Override
    public Configuration set(Executor newExecutor) {
        delegate.set(newExecutor);
        return this;
    }

    @Override
    public Configuration set(ExecutorProvider newExecutorProvider) {
        delegate.set(newExecutorProvider);
        return this;
    }

    @Override
    public Configuration set(CacheProvider newCacheProvider) {
        delegate.set(newCacheProvider);
        return this;
    }

    @Override
    public Configuration set(TransactionProvider newTransactionProvider) {
        delegate.set(newTransactionProvider);
        return this;
    }

    @Override
    public Configuration set(RecordMapper<?, ?> newRecordMapper) {
        delegate.set(newRecordMapper);
        return this;
    }

    @Override
    public Configuration set(RecordMapperProvider newRecordMapperProvider) {
        delegate.set(newRecordMapperProvider);
        return this;
    }

    @Override
    public Configuration set(RecordUnmapper<?, ?> newRecordUnmapper) {
        delegate.set(newRecordUnmapper);
        return this;
    }

    @Override
    public Configuration set(RecordUnmapperProvider newRecordUnmapperProvider) {
        delegate.set(newRecordUnmapperProvider);
        return this;
    }

    @Override
    public Configuration set(RecordListenerProvider... newRecordListenerProviders) {
        delegate.set(newRecordListenerProviders);
        return this;
    }

    @Override
    public Configuration set(ExecuteListenerProvider... newExecuteListenerProviders) {
        delegate.set(newExecuteListenerProviders);
        return this;
    }

    @Override
    public Configuration set(MigrationListenerProvider... newMigrationListenerProviders) {
        delegate.set(newMigrationListenerProviders);
        return this;
    }

    @Override
    public Configuration set(VisitListenerProvider... newVisitListenerProviders) {
        delegate.set(newVisitListenerProviders);
        return this;
    }

    @Override
    public Configuration set(TransactionListenerProvider... newTransactionListenerProviders) {
        delegate.set(newTransactionListenerProviders);
        return this;
    }

    @Override
    public Configuration set(DiagnosticsListenerProvider... newDiagnosticsListenerProviders) {
        delegate.set(newDiagnosticsListenerProviders);
        return this;
    }












    @Override
    public Configuration set(Unwrapper newUnwrapper) {
        delegate.set(newUnwrapper);
        return this;
    }

    @Override
    public Configuration set(UnwrapperProvider newUnwrapperProvider) {
        delegate.set(newUnwrapperProvider);
        return this;
    }

    @Override
    public Configuration set(CharsetProvider newCharsetProvider) {
        delegate.set(newCharsetProvider);
        return this;
    }

    @Override
    public Configuration set(ConverterProvider newConverterProvider) {
        delegate.set(newConverterProvider);
        return this;
    }

    @Override
    public Configuration set(SQLDialect newDialect) {
        delegate.set(newDialect);
        return this;
    }

    @Override
    public Configuration set(Settings newSettings) {
        delegate.set(newSettings);
        return this;
    }

    @Override
    public Configuration derive() {
        return new MockConfiguration(delegate.derive(), provider);
    }

    @Override
    public Configuration derive(Clock newClock) {
        return new MockConfiguration(delegate.derive(newClock), provider);
    }

    @Override
    public Configuration derive(Connection newConnection) {
        return new MockConfiguration(delegate.derive(newConnection), provider);
    }

    @Override
    public Configuration derive(DataSource newDataSource) {
        return new MockConfiguration(delegate.derive(newDataSource), provider);
    }

    @Override
    public Configuration derive(ConnectionFactory newConnectionFactory) {
        return new MockConfiguration(delegate.derive(newConnectionFactory), provider);
    }

    @Override
    public Configuration derive(ConnectionProvider newConnectionProvider) {
        return new MockConfiguration(delegate.derive(newConnectionProvider), provider);
    }

    @Override
    public Configuration derive(MetaProvider newMetaProvider) {
        return new MockConfiguration(delegate.derive(newMetaProvider), provider);
    }

    @Override
    public Configuration derive(CommitProvider newCommitProvider) {
        return new MockConfiguration(delegate.derive(newCommitProvider), provider);
    }

    @Override
    public Configuration derive(Executor newExecutor) {
        return new MockConfiguration(delegate.derive(newExecutor), provider);
    }

    @Override
    public Configuration derive(ExecutorProvider newExecutorProvider) {
        return new MockConfiguration(delegate.derive(newExecutorProvider), provider);
    }

    @Override
    public Configuration derive(CacheProvider newCacheProvider) {
        return new MockConfiguration(delegate.derive(newCacheProvider), provider);
    }

    @Override
    public Configuration derive(TransactionProvider newTransactionProvider) {
        return new MockConfiguration(delegate.derive(newTransactionProvider), provider);
    }

    @Override
    public Configuration derive(RecordMapper<?, ?> newRecordMapper) {
        return new MockConfiguration(delegate.derive(newRecordMapper), provider);
    }

    @Override
    public Configuration derive(RecordMapperProvider newRecordMapperProvider) {
        return new MockConfiguration(delegate.derive(newRecordMapperProvider), provider);
    }

    @Override
    public Configuration derive(RecordUnmapper<?, ?> newRecordUnmapper) {
        return new MockConfiguration(delegate.derive(newRecordUnmapper), provider);
    }

    @Override
    public Configuration derive(RecordUnmapperProvider newRecordUnmapperProvider) {
        return new MockConfiguration(delegate.derive(newRecordUnmapperProvider), provider);
    }

    @Override
    public Configuration derive(RecordListenerProvider... newRecordListenerProviders) {
        return new MockConfiguration(delegate.derive(newRecordListenerProviders), provider);
    }

    @Override
    public Configuration derive(ExecuteListenerProvider... newExecuteListenerProviders) {
        return new MockConfiguration(delegate.derive(newExecuteListenerProviders), provider);
    }

    @Override
    public Configuration derive(MigrationListenerProvider... newMigrationListenerProviders) {
        return new MockConfiguration(delegate.derive(newMigrationListenerProviders), provider);
    }

    @Override
    public Configuration derive(VisitListenerProvider... newVisitListenerProviders) {
        return new MockConfiguration(delegate.derive(newVisitListenerProviders), provider);
    }

    @Override
    public Configuration derive(TransactionListenerProvider... newTransactionListenerProviders) {
        return new MockConfiguration(delegate.derive(newTransactionListenerProviders), provider);
    }

    @Override
    public Configuration derive(DiagnosticsListenerProvider... newDiagnosticsListenerProviders) {
        return new MockConfiguration(delegate.derive(newDiagnosticsListenerProviders), provider);
    }











    @Override
    public Configuration derive(Unwrapper newUnwrapper) {
        return new MockConfiguration(delegate.derive(newUnwrapper), provider);
    }

    @Override
    public Configuration derive(UnwrapperProvider newUnwrapperProvider) {
        return new MockConfiguration(delegate.derive(newUnwrapperProvider), provider);
    }

    @Override
    public Configuration derive(CharsetProvider newCharsetProvider) {
        return new MockConfiguration(delegate.derive(newCharsetProvider), provider);
    }

    @Override
    public Configuration derive(ConverterProvider newConverterProvider) {
        return new MockConfiguration(delegate.derive(newConverterProvider), provider);
    }

    @Override
    public Configuration derive(SQLDialect newDialect) {
        return new MockConfiguration(delegate.derive(newDialect), provider);
    }

    @Override
    public Configuration derive(Settings newSettings) {
        return new MockConfiguration(delegate.derive(newSettings), provider);
    }

    @Override
    public Configuration deriveSettings(Function<? super Settings, ? extends Settings> newSettings) {
        return new MockConfiguration(delegate.deriveSettings(newSettings), provider);
    }
}
