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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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

import static java.util.Arrays.asList;
import static org.jooq.DDLFlag.CHECK;
import static org.jooq.DDLFlag.COMMENT;
import static org.jooq.DDLFlag.DOMAIN;
import static org.jooq.DDLFlag.FOREIGN_KEY;
import static org.jooq.DDLFlag.INDEX;
import static org.jooq.DDLFlag.PRIMARY_KEY;
import static org.jooq.DDLFlag.SCHEMA;
import static org.jooq.DDLFlag.SEQUENCE;
import static org.jooq.DDLFlag.TABLE;
import static org.jooq.DDLFlag.UNIQUE;
// ...
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.TableOptions.TableType.VIEW;
import static org.jooq.impl.Comparators.KEY_COMP;
import static org.jooq.impl.Comparators.NAMED_COMP;
import static org.jooq.impl.Comparators.TABLE_VIEW_COMP;
import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.Tools.map;
import static org.jooq.tools.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jooq.Check;
import org.jooq.Comment;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.ConstraintEnforcementStep;
import org.jooq.CreateDomainAsStep;
import org.jooq.CreateDomainConstraintStep;
import org.jooq.CreateDomainDefaultStep;
import org.jooq.CreateIndexIncludeStep;
import org.jooq.CreateSequenceFlagsStep;
import org.jooq.CreateTableOnCommitStep;
import org.jooq.CreateViewAsStep;
import org.jooq.DDLExportConfiguration;
import org.jooq.DDLFlag;
import org.jooq.DSLContext;
import org.jooq.Domain;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Key;
import org.jooq.Meta;
import org.jooq.Named;
// ...
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Sequence;
import org.jooq.SortOrder;
import org.jooq.Table;
import org.jooq.TableOptions;
import org.jooq.TableOptions.OnCommit;
import org.jooq.TableOptions.TableType;
import org.jooq.UniqueKey;
import org.jooq.DDLExportConfiguration.InlineForeignKeyConstraints;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
final class DDL {

    private static final JooqLogger      log                             = JooqLogger.getLogger(DDL.class);




    static final Set<SQLDialect>         NO_SUPPORT_ALTER_ADD_CONSTRAINT = SQLDialect.supportedBy(SQLITE);

    private final DSLContext             ctx;
    private final DDLExportConfiguration configuration;

    DDL(DSLContext ctx, DDLExportConfiguration configuration) {
        this.ctx = ctx;
        this.configuration = configuration;
    }

    private final List<Query> createTableOrViewWithInlineConstraints(Table<?> table, Collection<? extends Constraint> constraints) {
        boolean temporary = table.getTableType() == TableType.TEMPORARY;
        boolean view = table.getTableType().isView();
        OnCommit onCommit = table.getOptions().onCommit();

        if (view) {
            List<Query> result = new ArrayList<>();

            result.add(
                applyAs((configuration.createViewIfNotExists()
                        ? ctx.createViewIfNotExists(table, table.fields())
                        : configuration.createOrReplaceView()
                        ? ctx.createOrReplaceView(table, table.fields())
                        : ctx.createView(table, table.fields())), table.getOptions())
            );

            if (!constraints.isEmpty() && configuration.includeConstraintsOnViews())
                result.addAll(alterTableAddConstraints(table));

            return result;
        }

        CreateTableOnCommitStep s0 =
            (configuration.createTableIfNotExists()
                        ? temporary
                            ? ctx.createTemporaryTableIfNotExists(table)
                            : ctx.createTableIfNotExists(table)
                        : temporary
                            ? ctx.createTemporaryTable(table)
                            : ctx.createTable(table))

                // [#14512] We're exporting COMMENT ON COLUMN statements, so
                //          no need to comment columns again here.
                .columns(sortIf(map(table.fields(), f -> !isEmpty(f.getComment()) ? f.comment("") : f), !configuration.respectColumnOrder()))
                .constraints(constraints);

        if (temporary && onCommit != null) {
            switch (onCommit) {
                case DELETE_ROWS:
                    return asList(s0.onCommitDeleteRows());
                case PRESERVE_ROWS:
                    return asList(s0.onCommitPreserveRows());
                case DROP:
                    return asList(s0.onCommitDrop());
                default:
                    throw new IllegalStateException("Unsupported flag: " + onCommit);
            }
        }

        return asList(s0);
    }

    static final Pattern P_CREATE_VIEW = Pattern.compile("^(?i:\\s*\\bcreate\\b.*?\\bview\\b.*?\\bas\\b\\s+)(.*)$");

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private final Query applyAs(CreateViewAsStep q, TableOptions options) {
        if (options.select() != null)
            return q.as(options.select());
        else if (StringUtils.isBlank(options.source()))
            return q.as("");

        try {

            // [#16979] Internal, undocumented flag to prevent
            // [#17013] TODO: Use new Settings instead, once implemented
            Configuration c = ctx.configuration().derive();
            c.data("org.jooq.parser.delimiter-required", true);
            Query[] queries = c.dsl().parser().parse(options.source()).queries();

            if (queries.length > 0) {
                if (queries[0] instanceof CreateViewImpl<?> cv)
                    return q.as(cv.$select());
                else if (queries[0] instanceof Select<?> s)
                    return q.as(s);
                else
                    return q.as("");
            }
            else
                return q.as("");
        }
        catch (ParserException e) {
            log.info("Cannot parse view source: " + options.source(), e);

            // [#15238] If the command prefix is supplied, use a heursitic where the view
            //          body is expected to start after the first "AS" keyword
            if (options.source().trim().toLowerCase().startsWith("create"))
                return q.as(P_CREATE_VIEW.matcher(options.source()).replaceFirst("$1"));
            else
                return q.as(options.source());
        }
    }

    final Query createSequence(Sequence<?> sequence) {
        CreateSequenceFlagsStep result = configuration.createSequenceIfNotExists()
                    ? ctx.createSequenceIfNotExists(sequence)
                    : ctx.createSequence(sequence);

        if (sequence.getStartWith() != null)
            result = result.startWith(sequence.getStartWith());
        else if (configuration.defaultSequenceFlags())
            result = result.startWith(1);

        if (sequence.getIncrementBy() != null)
            result = result.incrementBy(sequence.getIncrementBy());
        else if (configuration.defaultSequenceFlags())
            result = result.incrementBy(1);

        if (sequence.getMinvalue() != null)
            result = result.minvalue(sequence.getMinvalue());
        else if (configuration.defaultSequenceFlags())
            result = result.noMinvalue();

        if (sequence.getMaxvalue() != null)
            result = result.maxvalue(sequence.getMaxvalue());
        else if (configuration.defaultSequenceFlags())
            result = result.noMaxvalue();

        if (sequence.getCycle())
            result = result.cycle();
        else if (configuration.defaultSequenceFlags())
            result = result.noCycle();

        if (sequence.getCache() != null)
            result = result.cache(sequence.getCache());
        else if (configuration.defaultSequenceFlags())
            result = result.noCache();

        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    final Query createDomain(Domain<?> domain) {
        CreateDomainAsStep s1 = configuration.createDomainIfNotExists()
                    ? ctx.createDomainIfNotExists(domain)
                    : ctx.createDomain(domain);

        CreateDomainDefaultStep s2 = s1.as(domain.getDataType());
        CreateDomainConstraintStep s3 = domain.getDataType().defaulted()
            ? s2.default_(domain.getDataType().default_())
            : s2;

        if (domain.getChecks().isEmpty())
            return s3;

        return s3.constraints(map(domain.getChecks(), c -> c.constraint()));
    }

    final List<Query> createTableOrView(Table<?> table) {
        return createTableOrView(table, new HashSet<Table<?>>());
    }

    private final List<Query> createTableOrView(Table<?> table, Set<Table<?>> tablesWithInlineConstraints) {
        List<Query> queries = new ArrayList<>();
        List<Constraint> constraints = new ArrayList<>();

        if (!hasConstraintsUsingIndexes(table)) {
            tablesWithInlineConstraints.add(table);
            constraints.addAll(constraints(table, inlineForeignKeyDefinitions()));
        }

        queries.addAll(createTableOrViewWithInlineConstraints(table, constraints));
        return queries;
    }

    final List<Query> createIndex(Table<?> table) {
        List<Query> result = new ArrayList<>();

        if (configuration.flags().contains(DDLFlag.INDEX))
            for (Index i : sortIf(table.getIndexes(), !configuration.respectIndexOrder()))
                result.add(createIndex(i));

        return result;
    }

    final Query createIndex(Index i) {
        CreateIndexIncludeStep s1 =
            (configuration.createIndexIfNotExists()
                ? i.getUnique()
                    ? ctx.createUniqueIndexIfNotExists(i)
                    : ctx.createIndexIfNotExists(i)
                : i.getUnique()
                    ? ctx.createUniqueIndex(i)
                    : ctx.createIndex(i))
            .on(i.getTable(), i.getFields());

        return i.getWhere() != null ? s1.where(i.getWhere()) : s1;
    }

    final List<Query> createForeignKey(Table<?> table) {
        List<Query> result = new ArrayList<>();

        if (configuration.flags().contains(DDLFlag.FOREIGN_KEY))
            for (Constraint constraint : foreignKeys(table))
                result.add(ctx.alterTable(table).add(constraint));

        return result;
    }

    final List<Query> alterTableAddConstraints(Table<?> table) {
        return alterTableAddConstraints(table, constraints(table, true));
    }

    final List<Query> alterTableAddConstraints(Table<?> table, List<Constraint> constraints) {
        return map(constraints, c -> ctx.alterTable(table).add(c));
    }

    final List<Constraint> constraints(Table<?> table, boolean includeForeignKeys) {
        List<Constraint> result = new ArrayList<>();

        result.addAll(primaryKeys(table));
        result.addAll(uniqueKeys(table));
        if (includeForeignKeys)
            result.addAll(foreignKeys(table));
        result.addAll(checks(table));

        return result;
    }

    final List<Constraint> primaryKeys(Table<?> table) {
        List<Constraint> result = new ArrayList<>();

        if (configuration.flags().contains(PRIMARY_KEY) && (table.getTableType() != VIEW || configuration.includeConstraintsOnViews()))
            for (UniqueKey<?> key : table.getKeys())
                if (key.isPrimary())
                    result.add(enforced(constraint(key.getUnqualifiedName()).primaryKey(key.getFieldsArray()), key.enforced()));

        return result;
    }

    final List<Constraint> uniqueKeys(Table<?> table) {
        List<Constraint> result = new ArrayList<>();

        if (configuration.flags().contains(UNIQUE) && (table.getTableType() != VIEW || configuration.includeConstraintsOnViews()))
            for (UniqueKey<?> key : sortKeysIf(table.getKeys(), !configuration.respectConstraintOrder()))
                if (!key.isPrimary())
                    result.add(enforced(constraint(key.getUnqualifiedName()).unique(key.getFieldsArray()), key.enforced()));

        return result;
    }

    final List<Constraint> foreignKeys(Table<?> table) {
        List<Constraint> result = new ArrayList<>();

        if (configuration.flags().contains(FOREIGN_KEY) && (table.getTableType() != VIEW || configuration.includeConstraintsOnViews()))
            for (ForeignKey<?, ?> key : sortKeysIf(table.getReferences(), !configuration.respectConstraintOrder()))
                result.add(enforced(constraint(key.getUnqualifiedName()).foreignKey(key.getFieldsArray()).references(key.getKey().getTable(), key.getKeyFieldsArray()), key.enforced()));

        return result;
    }

    final List<Constraint> checks(Table<?> table) {
        List<Constraint> result = new ArrayList<>();

        if (configuration.flags().contains(CHECK) && (table.getTableType() != VIEW || configuration.includeConstraintsOnViews()))
            for (Check<?> check : sortIf(table.getChecks(), !configuration.respectConstraintOrder()))
                result.add(enforced(constraint(check.getUnqualifiedName()).check(check.condition()), check.enforced()));

        return result;
    }

    final Queries queries(Table<?>... tables) {
        List<Query> queries = new ArrayList<>();

        for (Table<?> table : tables) {
            if (configuration.flags().contains(TABLE))
                queries.addAll(createTableOrView(table));
            else
                queries.addAll(alterTableAddConstraints(table));

            if (!inlineForeignKeyDefinitions())
                queries.addAll(createForeignKey(table));

            queries.addAll(createIndex(table));
            queries.addAll(commentOn(table));
        }

        return ctx.queries(queries);
    }

    final List<Query> commentOn(Table<?> table) {
        List<Query> result = new ArrayList<>();

        if (configuration.flags().contains(COMMENT)) {
            Comment tComment = table.getCommentPart();

            if (!StringUtils.isEmpty(tComment.getComment()))
                if (table.getTableType().isView())
                    result.add(ctx.commentOnView(table).is(tComment));
                else
                    result.add(ctx.commentOnTable(table).is(tComment));

            for (Field<?> field : sortIf(Arrays.asList(table.fields()), !configuration.respectColumnOrder())) {
                Comment fComment = field.getCommentPart();

                if (!StringUtils.isEmpty(fComment.getComment()))
                    result.add(ctx.commentOnColumn(field).is(fComment));
            }
        }

        return result;
    }

    final Queries queries(Meta meta) {
        List<Query> queries = new ArrayList<>();
        List<Schema> schemas = sortIf(meta.getSchemas(), !configuration.respectSchemaOrder());

        for (Schema schema : schemas)
            if (configuration.flags().contains(SCHEMA) && !schema.getUnqualifiedName().empty())
                if (configuration.createSchemaIfNotExists())
                    queries.add(ctx.createSchemaIfNotExists(schema.getUnqualifiedName()));
                else
                    queries.add(ctx.createSchema(schema.getUnqualifiedName()));

        // [#15291] There exist some corner cases where inline constraints don't work, including when there's an
        //          explicit or implicit CONSTRAINT .. USING INDEX clause where an index has to be created before
        //          the constraint.
        Set<Table<?>> tablesWithInlineConstraints = new HashSet<>();

        if (configuration.flags().contains(TABLE))
            for (Schema schema : schemas)
                for (Table<?> table : sortTablesIf(schema.getTables(), !configuration.respectTableOrder()))
                    queries.addAll(createTableOrView(table, tablesWithInlineConstraints));

        if (configuration.flags().contains(INDEX))
            for (Schema schema : schemas)
                for (Table<?> table : sortIf(schema.getTables(), !configuration.respectTableOrder()))
                    queries.addAll(createIndex(table));

        for (Schema schema : schemas) {
            if (configuration.flags().contains(PRIMARY_KEY))
                for (Table<?> table : sortIf(schema.getTables(), !configuration.respectTableOrder()))
                    if (!tablesWithInlineConstraints.contains(table))
                        for (Constraint constraint : sortIf(primaryKeys(table), !configuration.respectConstraintOrder()))
                            queries.add(ctx.alterTable(table).add(constraint));

            if (configuration.flags().contains(UNIQUE))
                for (Table<?> table : sortIf(schema.getTables(), !configuration.respectTableOrder()))
                    if (!tablesWithInlineConstraints.contains(table))
                        for (Constraint constraint : sortIf(uniqueKeys(table), !configuration.respectConstraintOrder()))
                            queries.add(ctx.alterTable(table).add(constraint));

            if (configuration.flags().contains(CHECK))
                for (Table<?> table : sortIf(schema.getTables(), !configuration.respectTableOrder()))
                    if (!tablesWithInlineConstraints.contains(table))
                        for (Constraint constraint : sortIf(checks(table), !configuration.respectConstraintOrder()))
                            queries.add(ctx.alterTable(table).add(constraint));
        }

        if (configuration.flags().contains(FOREIGN_KEY) && !inlineForeignKeyDefinitions())
            for (Schema schema : schemas)
                for (Table<?> table : sortIf(schema.getTables(), !configuration.respectTableOrder()))
                    for (Constraint constraint : foreignKeys(table))
                        queries.add(ctx.alterTable(table).add(constraint));

        if (configuration.flags().contains(DOMAIN))
            for (Schema schema : schemas)
                for (Domain<?> domain : sortIf(schema.getDomains(), !configuration.respectDomainOrder()))
                    queries.add(createDomain(domain));

        if (configuration.flags().contains(SEQUENCE))
            for (Schema schema : schemas)
                for (Sequence<?> sequence : sortIf(schema.getSequences(), !configuration.respectSequenceOrder()))
                    queries.add(createSequence(sequence));

        if (configuration.flags().contains(COMMENT))
            for (Schema schema : schemas)
                for (Table<?> table : sortIf(schema.getTables(), !configuration.respectTableOrder()))
                    queries.addAll(commentOn(table));

        return ctx.queries(queries);
    }

    private final boolean inlineForeignKeyDefinitions() {
        return configuration.flags().contains(TABLE) && (
            configuration.inlineForeignKeyConstraints() == InlineForeignKeyConstraints.ALWAYS
         || configuration.inlineForeignKeyConstraints() == InlineForeignKeyConstraints.WHEN_NEEDED && NO_SUPPORT_ALTER_ADD_CONSTRAINT.contains(ctx.dialect())
        );
    }

    private final <R extends Record> boolean hasConstraintsUsingIndexes(Table<R> table) {
















        return false;
    }

    private final <K extends Key<?>> List<K> sortKeysIf(List<K> input, boolean sort) {
        if (sort) {
            List<K> result = new ArrayList<>(input);
            result.sort(KEY_COMP);
            result.sort(NAMED_COMP);
            return result;
        }

        return input;
    }

    private final <N extends Named> List<N> sortIf(List<N> input, boolean sort) {
        if (sort) {
            List<N> result = new ArrayList<>(input);
            result.sort(NAMED_COMP);
            return result;
        }

        return input;
    }

    private final <T extends Table<?>> List<T> sortTablesIf(List<T> input, boolean sort) {

        if (sort) {
            List<T> result = new ArrayList<>(input);
            result.sort(NAMED_COMP);

            // [#15326] Tables should always appear before views
            result.sort(TABLE_VIEW_COMP);
            return result;
        }

        return input;
    }

    private final Constraint enforced(ConstraintEnforcementStep check, boolean enforced) {
        return enforced ? check : check.notEnforced();
    }
}
