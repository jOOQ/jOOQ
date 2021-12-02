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
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.NOT_IN;
import static org.jooq.DatePart.*;
import static org.jooq.JoinType.JOIN;
import static org.jooq.SQLDialect.*;
import static org.jooq.VisitListener.onVisitStart;
import static org.jooq.conf.ParseWithMetaLookups.IGNORE_ON_FAILURE;
import static org.jooq.conf.ParseWithMetaLookups.THROW_ON_FAILURE;
import static org.jooq.conf.SettingsTools.parseLocale;
import static org.jooq.impl.AbstractName.NO_NAME;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DefaultParseContext.FunctionKeyword.FK_AND;
import static org.jooq.impl.DefaultParseContext.FunctionKeyword.FK_IN;
import static org.jooq.impl.JSONOnNull.ABSENT_ON_NULL;
import static org.jooq.impl.JSONOnNull.NULL_ON_NULL;
import static org.jooq.impl.Keywords.K_DELETE;
import static org.jooq.impl.Keywords.K_INSERT;
import static org.jooq.impl.Keywords.K_SELECT;
import static org.jooq.impl.Keywords.K_UPDATE;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.SelectQueryImpl.EMULATE_SELECT_INTO_AS_CTAS;
import static org.jooq.impl.SelectQueryImpl.NO_SUPPORT_FOR_UPDATE_OF_FIELDS;
import static org.jooq.impl.Tools.CTX;
import static org.jooq.impl.Tools.EMPTY_BYTE;
import static org.jooq.impl.Tools.EMPTY_COLLECTION;
import static org.jooq.impl.Tools.EMPTY_COMMON_TABLE_EXPRESSION;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.EMPTY_NAME;
import static org.jooq.impl.Tools.EMPTY_OBJECT;
import static org.jooq.impl.Tools.EMPTY_QUERYPART;
import static org.jooq.impl.Tools.EMPTY_ROW;
import static org.jooq.impl.Tools.EMPTY_SORTFIELD;
import static org.jooq.impl.Tools.EMPTY_STRING;
import static org.jooq.impl.Tools.EMPTY_TABLE;
import static org.jooq.impl.Tools.aliased;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.asInt;
import static org.jooq.impl.Tools.deleteQueryImpl;
import static org.jooq.impl.Tools.findAny;
import static org.jooq.impl.Tools.normaliseNameCase;
import static org.jooq.impl.Tools.selectQueryImpl;
import static org.jooq.impl.Tools.updateQueryImpl;
import static org.jooq.impl.Transformations.transformAppendMissingTableReferences;
import static org.jooq.impl.XMLPassingMechanism.BY_REF;
import static org.jooq.impl.XMLPassingMechanism.BY_VALUE;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.AggregateFilterStep;
import org.jooq.AggregateFunction;
import org.jooq.AlterDatabaseStep;
import org.jooq.AlterDomainDropConstraintCascadeStep;
import org.jooq.AlterDomainRenameConstraintStep;
import org.jooq.AlterDomainStep;
import org.jooq.AlterIndexStep;
import org.jooq.AlterSchemaStep;
import org.jooq.AlterSequenceFlagsStep;
import org.jooq.AlterSequenceStep;
import org.jooq.AlterTableAddStep;
import org.jooq.AlterTableDropStep;
import org.jooq.AlterTableStep;
import org.jooq.AlterTypeStep;
import org.jooq.ArrayAggOrderByStep;
import org.jooq.Block;
import org.jooq.CaseConditionStep;
import org.jooq.CaseValueStep;
import org.jooq.CaseWhenStep;
import org.jooq.Catalog;
import org.jooq.CharacterSet;
import org.jooq.Collation;
import org.jooq.Comment;
import org.jooq.CommentOnIsStep;
import org.jooq.CommonTableExpression;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.ConstraintEnforcementStep;
import org.jooq.ConstraintForeignKeyOnStep;
import org.jooq.ConstraintTypeStep;
import org.jooq.Context;
import org.jooq.CreateDomainConstraintStep;
import org.jooq.CreateDomainDefaultStep;
// ...
// ...
// ...
import org.jooq.CreateIndexIncludeStep;
import org.jooq.CreateIndexStep;
import org.jooq.CreateIndexWhereStep;
// ...
// ...
// ...
import org.jooq.CreateSequenceFlagsStep;
import org.jooq.CreateTableColumnStep;
import org.jooq.CreateTableCommentStep;
import org.jooq.CreateTableConstraintStep;
import org.jooq.CreateTableOnCommitStep;
import org.jooq.CreateTableStorageStep;
import org.jooq.CreateTableWithDataStep;
// ...
// ...
// ...
// ...
// ...
import org.jooq.DDLQuery;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.DatePart;
// ...
import org.jooq.DeleteLimitStep;
import org.jooq.DeleteOrderByStep;
import org.jooq.DeleteReturningStep;
import org.jooq.DeleteUsingStep;
import org.jooq.DeleteWhereStep;
import org.jooq.DerivedColumnList;
import org.jooq.Domain;
import org.jooq.DropDomainCascadeStep;
import org.jooq.DropIndexCascadeStep;
import org.jooq.DropIndexOnStep;
import org.jooq.DropSchemaStep;
import org.jooq.DropTableStep;
import org.jooq.DropTypeStep;
import org.jooq.Field;
import org.jooq.FieldOrConstraint;
import org.jooq.FieldOrRow;
// ...
// ...
import org.jooq.GrantOnStep;
import org.jooq.GrantToStep;
import org.jooq.GrantWithGrantOptionStep;
import org.jooq.GroupConcatOrderByStep;
import org.jooq.GroupConcatSeparatorStep;
import org.jooq.GroupField;
// ...
import org.jooq.Index;
import org.jooq.InsertOnConflictDoUpdateStep;
import org.jooq.InsertOnConflictWhereIndexPredicateStep;
import org.jooq.InsertOnConflictWhereStep;
import org.jooq.InsertOnDuplicateStep;
import org.jooq.InsertReturningStep;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStepN;
import org.jooq.JSONArrayAggNullStep;
import org.jooq.JSONArrayAggOrderByStep;
import org.jooq.JSONArrayAggReturningStep;
import org.jooq.JSONArrayNullStep;
import org.jooq.JSONArrayReturningStep;
import org.jooq.JSONEntry;
import org.jooq.JSONObjectAggNullStep;
import org.jooq.JSONObjectAggReturningStep;
import org.jooq.JSONObjectNullStep;
import org.jooq.JSONObjectReturningStep;
import org.jooq.JSONTableColumnPathStep;
import org.jooq.JSONTableColumnsStep;
import org.jooq.JSONValueDefaultStep;
import org.jooq.JSONValueOnStep;
import org.jooq.JoinType;
import org.jooq.Keyword;
// ...
import org.jooq.LanguageContext;
import org.jooq.LikeEscapeStep;
// ...
import org.jooq.Merge;
import org.jooq.MergeMatchedDeleteStep;
import org.jooq.MergeMatchedStep;
import org.jooq.MergeMatchedWhereStep;
import org.jooq.MergeUsingStep;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Name.Quoted;
import org.jooq.OrderedAggregateFunction;
import org.jooq.OrderedAggregateFunctionOfDeferredType;
import org.jooq.Param;
import org.jooq.ParamMode;
import org.jooq.Parameter;
import org.jooq.ParseContext;
// ...
import org.jooq.Parser;
// ...
// ...
import org.jooq.Privilege;
// ...
import org.jooq.QualifiedAsterisk;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.RevokeFromStep;
import org.jooq.RevokeOnStep;
import org.jooq.Row;
import org.jooq.Row2;
import org.jooq.SQL;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.Sequence;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.Statement;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOnStep;
import org.jooq.TableOptionalOnStep;
import org.jooq.TablePartitionByStep;
import org.jooq.Truncate;
import org.jooq.TruncateCascadeStep;
import org.jooq.TruncateIdentityStep;
import org.jooq.UpdateFromStep;
import org.jooq.UpdateLimitStep;
import org.jooq.UpdateOrderByStep;
import org.jooq.UpdateReturningStep;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateWhereStep;
import org.jooq.User;
// ...
// ...
import org.jooq.WindowBeforeOverStep;
import org.jooq.WindowDefinition;
import org.jooq.WindowFromFirstLastStep;
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowOverStep;
import org.jooq.WindowSpecification;
import org.jooq.WindowSpecificationExcludeStep;
import org.jooq.WindowSpecificationOrderByStep;
import org.jooq.WindowSpecificationRowsAndStep;
import org.jooq.WindowSpecificationRowsStep;
import org.jooq.XML;
import org.jooq.XMLAggOrderByStep;
import org.jooq.XMLAttributes;
import org.jooq.XMLTableColumnPathStep;
import org.jooq.XMLTableColumnsStep;
import org.jooq.XMLTablePassingStep;
import org.jooq.conf.ParseSearchSchema;
import org.jooq.conf.ParseUnknownFunctions;
import org.jooq.conf.ParseUnsupportedSyntax;
import org.jooq.conf.ParseWithMetaLookups;
import org.jooq.conf.RenderKeywordCase;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.ScopeStack.Value;
import org.jooq.impl.XMLParse.DocumentOrContent;
import org.jooq.tools.StringUtils;
import org.jooq.tools.reflect.Reflect;
import org.jooq.types.DayToSecond;
import org.jooq.types.Interval;
import org.jooq.types.YearToMonth;
import org.jooq.types.YearToSecond;

/**
 * @author Lukas Eder
 */
final class ParserImpl implements Parser {

    private final DSLContext           dsl;
    private final ParseWithMetaLookups metaLookups;
    private final Meta                 meta;

    ParserImpl(Configuration configuration) {
        this.dsl = DSL.using(configuration);
        this.metaLookups = configuration.settings().getParseWithMetaLookups();
        this.meta = metaLookups == IGNORE_ON_FAILURE || metaLookups == THROW_ON_FAILURE ? dsl.meta() : null;
    }

    // -------------------------------------------------------------------------
    // XXX: Top level parsing
    // -------------------------------------------------------------------------

    private final DefaultParseContext ctx(String sql, Object... bindings) {
        return new DefaultParseContext(dsl, meta, metaLookups, sql, bindings);
    }

    @Override
    public final Queries parse(String sql) {
        return parse(sql, EMPTY_OBJECT);
    }

    @Override
    public final Queries parse(String sql, Object... bindings) {
        return ctx(sql, bindings).parse();
    }

    @Override
    public final Query parseQuery(String sql) {
        return parseQuery(sql, EMPTY_OBJECT);
    }

    @Override
    public final Query parseQuery(String sql, Object... bindings) {
        return ctx(sql, bindings).parseQuery0();
    }

    @Override
    public final Statement parseStatement(String sql) {
        return parseStatement(sql, EMPTY_OBJECT);
    }

    @Override
    public final Statement parseStatement(String sql, Object... bindings) {
        return ctx(sql, bindings).parseStatementAndSemicolonIf();
    }

















    @Override
    public final ResultQuery<?> parseResultQuery(String sql) {
        return parseResultQuery(sql, EMPTY_OBJECT);
    }

    @Override
    public final ResultQuery<?> parseResultQuery(String sql, Object... bindings) {
        return ctx(sql, bindings).parseResultQuery0();
    }

    @Override
    public final Select<?> parseSelect(String sql) {
        return parseSelect(sql, EMPTY_OBJECT);
    }

    @Override
    public final Select<?> parseSelect(String sql, Object... bindings) {
        return ctx(sql, bindings).parseSelect0();
    }

    @Override
    public final Table<?> parseTable(String sql) {
        return parseTable(sql, EMPTY_OBJECT);
    }

    @Override
    public final Table<?> parseTable(String sql, Object... bindings) {
        return ctx(sql, bindings).parseTable0();
    }

    @Override
    public final Field<?> parseField(String sql) {
        return parseField(sql, EMPTY_OBJECT);
    }

    @Override
    public final Field<?> parseField(String sql, Object... bindings) {
        return ctx(sql, bindings).parseField0();
    }

    @Override
    public final Row parseRow(String sql) {
        return parseRow(sql, EMPTY_OBJECT);
    }

    @Override
    public final Row parseRow(String sql, Object... bindings) {
        return ctx(sql, bindings).parseRow0();
    }

    @Override
    public final Condition parseCondition(String sql) {
        return parseCondition(sql, EMPTY_OBJECT);
    }

    @Override
    public final Condition parseCondition(String sql, Object... bindings) {
        return ctx(sql, bindings).parseCondition0();
    }

    @Override
    public final Name parseName(String sql) {
        return parseName(sql, EMPTY_OBJECT);
    }

    @Override
    public final Name parseName(String sql, Object... bindings) {
        return ctx(sql, bindings).parseName0();
    }
}

@SuppressWarnings({ "rawtypes", "unchecked" })
final class DefaultParseContext extends AbstractScope implements ParseContext {







    static final Set<SQLDialect>         SUPPORTS_HASH_COMMENT_SYNTAX  = SQLDialect.supportedBy(MARIADB, MYSQL);

    final Queries parse() {
        List<Query> result = new ArrayList<>();
        Query query;

        do {
            parseDelimiterSpecifications();
            while (parseDelimiterIf(false));

            query = patchParsedQuery(parseQuery(false, false));
            if (query == IGNORE || query == IGNORE_NO_DELIMITER)
                continue;
            if (query != null)
                result.add(query);
        }
        while (parseDelimiterIf(true) && !done());

        return done("Unexpected token or missing query delimiter", dsl.queries(result));
    }

    private static final Pattern P_SEARCH_PATH = Pattern.compile("(?i:select\\s+(pg_catalog\\s*\\.\\s*)?set_config\\s*\\(\\s*'search_path'\\s*,\\s*'([^']*)'\\s*,\\s*\\w+\\s*\\))");

    private final Query patchParsedQuery(Query query) {

        // [#8910] Some statements can be parsed differently when we know we're
        //         parsing them for the DDLDatabase. This method patches these
        //         statements.
        if (TRUE.equals(configuration().data("org.jooq.ddl.parse-for-ddldatabase"))) {
            if (query instanceof Select) {
                String string =
                configuration().deriveSettings(s -> s
                    .withRenderFormatted(false)
                    .withRenderKeywordCase(RenderKeywordCase.LOWER)
                    .withRenderNameCase(RenderNameCase.LOWER)
                    .withRenderQuotedNames(RenderQuotedNames.NEVER)
                    .withRenderSchema(false))
                    .dsl()
                    .render(query);

                // [#8910] special treatment for PostgreSQL pg_dump's curious
                //         usage of the SET SCHEMA command
                Matcher matcher = P_SEARCH_PATH.matcher(string);
                String schema;
                if (matcher.find())
                    if (!StringUtils.isBlank(schema = matcher.group(2)))
                        return configuration().dsl().setSchema(schema);
                    else
                        return IGNORE;
            }
        }

        return query;
    }

    final Query parseQuery0() {
        return done("Unexpected clause", parseQuery(false, false));
    }

    final Statement parseStatement0() {
        return this.done("Unexpected content", parseStatementAndSemicolonIf());
    }

















    final ResultQuery<?> parseResultQuery0() {
        return done("Unexpected content after end of query input", (ResultQuery<?>) parseQuery(true, false));
    }

    final Select<?> parseSelect0() {
        return done("Unexpected content after end of query input", (Select<?>) parseQuery(true, true));
    }

    final Table<?> parseTable0() {
        return done("Unexpected content after end of table input", parseTable());
    }

    final Field<?> parseField0() {
        return done("Unexpected content after end of field input", parseField());
    }

    final Row parseRow0() {
        return done("Unexpected content after end of row input", parseRow());
    }

    final Condition parseCondition0() {
        return done("Unexpected content after end of condition input", parseCondition());
    }

    final Name parseName0() {
        return done("Unexpected content after end of name input", parseName());
    }

























































    private final void parseDelimiterSpecifications() {
        while (parseKeywordIf("DELIMITER"))
            delimiter(parseUntilEOL().trim());
    }

    private final boolean parseDelimiterIf(boolean optional) {
        if (parseIf(delimiter()))
            return true;

        if (peekKeyword("GO")) {
            positionInc(2);
            String line = parseUntilEOLIf();

            if (line != null && !"".equals(line.trim()))
                throw exception("GO must be only token on line");

            parseWhitespaceIf();
            return true;
        }

        return optional;
    }

    private final Query parseQuery(boolean parseResultQuery, boolean parseSelect) {
        if (done())
            return null;

        scopeStart();
        boolean previousMetaLookupsForceIgnore = metaLookupsForceIgnore();
        Query result = null;
        LanguageContext previous = languageContext;

        try {
            languageContext = LanguageContext.QUERY;

            switch (characterUpper()) {
                case 'A':
                    if (!parseResultQuery && peekKeyword("ALTER"))
                        return result = metaLookupsForceIgnore(true).parseAlter();

                    break;

                case 'B':
                    if (!parseResultQuery && peekKeyword("BEGIN")) {
                        languageContext = previous;
                        return result = parseBlock(false);
                    }

                    break;

                case 'C':
                    if (!parseResultQuery && peekKeyword("CREATE"))
                        return result = metaLookupsForceIgnore(true).parseCreate();
                    else if (!parseResultQuery && peekKeyword("COMMENT ON"))
                        return result = metaLookupsForceIgnore(true).parseCommentOn();
                    else if (!parseResultQuery && parseKeywordIf("CT"))
                        return result = metaLookupsForceIgnore(true).parseCreateTable(false);
                    else if (!parseResultQuery && parseKeywordIf("CV"))
                        return result = metaLookupsForceIgnore(true).parseCreateView(false);
                    else if (peekKeyword("CALL") && requireProEdition())



                        ;
                    else if (parseKeywordIf("COMMIT"))
                        throw notImplemented("COMMIT");
                    else if (parseKeywordIf("CONNECT"))
                        throw notImplemented("CONNECT");

                    break;

                case 'D':
                    if (!parseResultQuery && peekKeyword("DECLARE") && requireProEdition())
                        return result = parseBlock(true);
                    else if (!parseSelect && (peekKeyword("DELETE") || peekKeyword("DEL")))
                        return result = parseDelete(null, parseResultQuery);
                    else if (!parseResultQuery && peekKeyword("DROP"))
                        return result = metaLookupsForceIgnore(true).parseDrop();
                    else if (!parseResultQuery && peekKeyword("DO"))
                        return result = parseDo();

                    break;

                case 'E':
                    if (!parseResultQuery && peekKeyword("EXECUTE BLOCK AS"))
                        return result = parseBlock(true);
                    else if (!parseResultQuery && peekKeyword("EXEC"))
                        return result = parseExec();
                    else if (peekKeyword("EXECUTE PROCEDURE") && requireProEdition())



                        ;

                    break;

                case 'G':
                    if (!parseResultQuery && peekKeyword("GRANT"))
                        return result = metaLookupsForceIgnore(true).parseGrant();

                    break;

                case 'I':
                    if (!parseSelect && (peekKeyword("INSERT") || peekKeyword("INS")))
                        return result = parseInsert(null, parseResultQuery);

                    break;

                case 'L':
                    if (parseKeywordIf("LOAD"))
                        throw notImplemented("LOAD");

                    break;

                case 'M':
                    if (!parseResultQuery && peekKeyword("MERGE"))
                        return result = parseMerge(null);

                    break;

                case 'O':
                    if (!parseResultQuery && peekKeyword("OPEN"))
                        return result = parseOpen();

                    break;

                case 'R':
                    if (!parseResultQuery && peekKeyword("RENAME"))
                        return result = metaLookupsForceIgnore(true).parseRename();
                    else if (!parseResultQuery && peekKeyword("REVOKE"))
                        return result = metaLookupsForceIgnore(true).parseRevoke();
                    else if (parseKeywordIf("REPLACE"))
                        throw notImplemented("REPLACE");
                    else if (parseKeywordIf("ROLLBACK"))
                        throw notImplemented("ROLLBACK");

                    break;

                case 'S':
                    if (peekSelect(false))
                        return result = parseSelect();
                    else if (!parseResultQuery && peekKeyword("SET"))
                        return result = parseSet();
                    else if (parseKeywordIf("SAVEPOINT"))
                        throw notImplemented("SAVEPOINT");

                    break;

                case 'T':
                    if (!parseSelect && peekKeyword("TABLE"))
                        return result = parseSelect();
                    else if (!parseResultQuery && peekKeyword("TRUNCATE"))
                        return result = parseTruncate();

                    break;

                case 'U':
                    if (!parseSelect && (peekKeyword("UPDATE") || peekKeyword("UPD")))
                        return result = parseUpdate(null, parseResultQuery);
                    else if (!parseResultQuery && peekKeyword("USE"))
                        return result = parseUse();
                    else if (parseKeywordIf("UPSERT"))
                        throw notImplemented("UPSERT");

                    break;

                case 'V':
                    if (!parseSelect && peekKeyword("VALUES"))
                        return result = parseSelect();

                case 'W':
                    if (peekKeyword("WITH"))
                        return result = parseWith(parseSelect);

                    break;

                case '(':

                    // TODO are there other possible statement types?
                    if (peekKeyword("WITH", false, true, false))
                        return result = parseWith(true);
                    else
                        return result = parseSelect();

                case '{':
                    if (peekKeyword("{ CALL") && requireProEdition())



                        ;

                    break;

                default:
                    break;
            }

            throw exception("Unsupported query type");
        }
        catch (ParserException e) {

            // [#9061] Don't hide this pre-existing exceptions in scopeResolve()
            scopeClear();
            throw e;
        }
        finally {
            scopeEnd(result);
            scopeResolve();
            metaLookupsForceIgnore(previousMetaLookupsForceIgnore);
            languageContext = previous;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Statement parsing
    // -----------------------------------------------------------------------------------------------------------------

    private final Query parseWith(boolean parseSelect) {
        return parseWith(parseSelect, null);
    }

    private final Query parseWith(boolean parseSelect, Integer degree) {
        int parens = 0;
        while (parseIf('('))
            parens++;

        parseKeyword("WITH");
        boolean recursive = parseKeywordIf("RECURSIVE");

        List<CommonTableExpression<?>> cte = new ArrayList<>();
        do {
            Name name = parseIdentifier();
            DerivedColumnList dcl = null;

            if (parseIf('(')) {
                List<Name> columnNames = parseIdentifiers();
                parse(')');
                dcl = name.fields(columnNames.toArray(EMPTY_NAME));
            }

            parseKeyword("AS");
            boolean materialized = parseKeywordIf("MATERIALIZED");
            boolean notMaterialized = !materialized && parseKeywordIf("NOT MATERIALIZED");
            parse('(');
            ResultQuery<?> resultQuery = (ResultQuery<?>) parseQuery(true, false);
            parse(')');

            cte.add(dcl != null
                ? materialized
                    ? dcl.asMaterialized(resultQuery)
                    : notMaterialized
                    ? dcl.asNotMaterialized(resultQuery)
                    : dcl.as(resultQuery)
                : materialized
                    ? name.asMaterialized(resultQuery)
                    : notMaterialized
                    ? name.asNotMaterialized(resultQuery)
                    : name.as(resultQuery)
            );
        }
        while (parseIf(','));

        // TODO Better model API for WITH clause
        WithImpl with = (WithImpl) new WithImpl(dsl.configuration(), recursive).with(cte.toArray(EMPTY_COMMON_TABLE_EXPRESSION));
        Query result;
        if (!parseSelect && (peekKeyword("DELETE") || peekKeyword("DEL")))
            result = parseDelete(with, false);
        else if (!parseSelect && (peekKeyword("INSERT") || peekKeyword("INS")))
            result = parseInsert(with, false);
        else if (!parseSelect && peekKeyword("MERGE"))
            result = parseMerge(with);
        else if (peekSelect(true))
            result = parseSelect(degree, with);
        else if (!parseSelect && (peekKeyword("UPDATE") || peekKeyword("UPD")))
            result = parseUpdate(with, false);
        else if ((parseWhitespaceIf() || true) && done())
            throw exception("Missing statement after WITH");
        else
            throw exception("Unsupported statement after WITH");

        while (parens --> 0)
            parse(')');

        return result;
    }

    private final SelectQueryImpl<Record> parseWithOrSelect() {
        return parseWithOrSelect(null);
    }

    private final SelectQueryImpl<Record> parseWithOrSelect(Integer degree) {
        return peekKeyword("WITH") ? (SelectQueryImpl<Record>) parseWith(true, degree) : parseSelect(degree, null);
    }

    private final SelectQueryImpl<Record> parseSelect() {
        return parseSelect(null, null);
    }

    private final SelectQueryImpl<Record> parseSelect(Integer degree, WithImpl with) {
        scopeStart();
        SelectQueryImpl<Record> result = parseQueryExpressionBody(degree, with, null);
        List<SortField<?>> orderBy = null;

        for (Field<?> field : result.getSelect())
            if (aliased(field) != null)
                scope(field);

        if (parseKeywordIf("ORDER")) {
            if (parseKeywordIf("SIBLINGS BY") && requireProEdition()) {




            }
            else if (parseKeywordIf("BY"))
                result.addOrderBy(orderBy = parseList(',', ParseContext::parseSortField));
            else
                throw expected("SIBLINGS BY", "BY");
        }

        if (orderBy != null && parseKeywordIf("SEEK")) {
            boolean before = parseKeywordIf("BEFORE");
            if (!before)
                parseKeywordIf("AFTER");

            List<Field<?>> seek = parseList(',', ParseContext::parseField);
            if (seek.size() != orderBy.size())
                throw exception("ORDER BY size (" + orderBy.size() + ") and SEEK size (" + seek.size() + ") must match");

            if (before)
                result.addSeekBefore(seek);
            else
                result.addSeekAfter(seek);

            if (!result.getLimit().isApplicable())
                parseLimit(result, false);
        }
        else if (!result.getLimit().isApplicable()) {
            parseLimit(result, true);
        }

        forClause:
        if (parseKeywordIf("FOR")) {
            boolean jsonb;

            if (parseKeywordIf("KEY SHARE"))
                result.setForKeyShare(true);
            else if (parseKeywordIf("NO KEY UPDATE"))
                result.setForNoKeyUpdate(true);
            else if (parseKeywordIf("SHARE"))
                result.setForShare(true);
            else if (parseKeywordIf("UPDATE"))
                result.setForUpdate(true);
            else if (parseKeywordIf("XML") && requireProEdition()) {









































            }
            else if ((jsonb = parseKeywordIf("JSONB") || parseKeywordIf("JSON")) && requireProEdition()) {





























            }
            else
                throw expected("UPDATE", "NO KEY UPDATE", "SHARE", "KEY SHARE", "XML", "JSON");

            if (parseKeywordIf("OF"))
                if (NO_SUPPORT_FOR_UPDATE_OF_FIELDS.contains(parseDialect()))
                    result.setForUpdateOf(parseList(',', ParseContext::parseTable).toArray(EMPTY_TABLE));
                else
                    result.setForUpdateOf(parseList(',', ParseContext::parseField));

            if (parseKeywordIf("NOWAIT"))
                result.setForUpdateNoWait();
            else if (parseKeywordIf("WAIT") && requireProEdition())



                ;
            else if (parseKeywordIf("SKIP LOCKED"))
                result.setForUpdateSkipLocked();
        }

        scopeEnd(result);
        return result;
    }

    private final void parseLimit(SelectQueryImpl<Record> result, boolean offset) {
        boolean offsetStandard = false;
        boolean offsetPostgres = false;

        if (offset && parseKeywordIf("OFFSET")) {
            result.addOffset(requireParam(parseParenthesisedUnsignedIntegerOrBindVariable()));

            if (parseKeywordIf("ROWS") || parseKeywordIf("ROW"))
                offsetStandard = true;

            // Ingres doesn't have a ROWS keyword after offset
            else if (peekKeyword("FETCH"))
                offsetStandard = true;
            else
                offsetPostgres = true;
        }

        if (!offsetStandard && parseKeywordIf("LIMIT")) {
            Param<Long> limit = requireParam(parseParenthesisedUnsignedIntegerOrBindVariable());

            if (offsetPostgres) {
                result.addLimit(limit);

                if (parseKeywordIf("PERCENT"))
                    result.setLimitPercent(true);

                if (parseKeywordIf("WITH TIES"))
                    result.setWithTies(true);
            }
            else if (offset && parseIf(',')) {
                result.addLimit(limit, requireParam(parseParenthesisedUnsignedIntegerOrBindVariable()));
            }
            else {
                if (parseKeywordIf("PERCENT"))
                    result.setLimitPercent(true);

                if (parseKeywordIf("WITH TIES"))
                    result.setWithTies(true);

                if (offset && parseKeywordIf("OFFSET"))
                    result.addLimit(requireParam(parseParenthesisedUnsignedIntegerOrBindVariable()), limit);
                else
                    result.addLimit(limit);
            }
        }
        else if (!offsetPostgres && parseKeywordIf("FETCH")) {
            parseAndGetKeyword("FIRST", "NEXT");

            if (parseAndGetKeywordIf("ROW", "ROWS") != null) {
                result.addLimit(inline(1L));
            }
            else {
                result.addLimit(requireParam(parseParenthesisedUnsignedIntegerOrBindVariable()));

                if (parseKeywordIf("PERCENT"))
                    result.setLimitPercent(true);

                parseAndGetKeyword("ROW", "ROWS");
            }

            if (parseKeywordIf("WITH TIES"))
                result.setWithTies(true);
            else
                parseKeyword("ONLY");
        }
        else if (!offsetStandard && !offsetPostgres && parseKeywordIf("ROWS")) {
            Long from = parseUnsignedIntegerLiteral();

            if (parseKeywordIf("TO")) {
                Long to = parseUnsignedIntegerLiteral();
                result.addLimit(to - from);
                result.addOffset(from - 1);
            }
            else {
                result.addLimit(from);
            }
        }
    }

    private final SelectQueryImpl<Record> parseQueryExpressionBody(Integer degree, WithImpl with, SelectQueryImpl<Record> prefix) {
        SelectQueryImpl<Record> lhs = parseQueryTerm(degree, with, prefix);
        SelectQueryImpl<Record> local = lhs;

        CombineOperator combine;
        while ((combine = parseCombineOperatorIf(false)) != null) {
            scopeEnd(local);
            scopeStart();

            if (degree == null)
                degree = Tools.degree(lhs);

            SelectQueryImpl<Record> rhs = local = degreeCheck(degree, parseQueryTerm(degree, null, null));
            switch (combine) {
                case UNION:
                    lhs = lhs.union(rhs);
                    break;
                case UNION_ALL:
                    lhs = lhs.unionAll(rhs);
                    break;
                case EXCEPT:
                    lhs = lhs.except(rhs);
                    break;
                case EXCEPT_ALL:
                    lhs = lhs.exceptAll(rhs);
                    break;
                default:
                    throw internalError();
            }
        }

        return lhs;
    }

    private final SelectQueryImpl<Record> parseQueryTerm(Integer degree, WithImpl with, SelectQueryImpl<Record> prefix) {
        SelectQueryImpl<Record> lhs = prefix != null ? prefix : parseQueryPrimary(degree, with);
        SelectQueryImpl<Record> local = lhs;

        CombineOperator combine;
        while ((combine = parseCombineOperatorIf(true)) != null) {
            scopeEnd(local);
            scopeStart();

            if (degree == null)
                degree = Tools.degree(lhs);

            SelectQueryImpl<Record> rhs = local = degreeCheck(degree, parseQueryPrimary(degree, null));
            switch (combine) {
                case INTERSECT:
                    lhs = lhs.intersect(rhs);
                    break;
                case INTERSECT_ALL:
                    lhs = lhs.intersectAll(rhs);
                    break;
                default:
                    throw internalError();
            }
        }

        return lhs;
    }

    private SelectQueryImpl<Record> degreeCheck(int expected, SelectQueryImpl<Record> s) {
        if (expected == 0)
            return s;

        int actual = Tools.degree(s);
        if (actual == 0)
            return s;

        if (expected != actual)
            throw exception("Select list must contain " + expected + " columns. Got: " + actual);

        return s;
    }

    private final SelectQueryImpl<Record> parseQueryPrimary(Integer degree, WithImpl with) {
        if (parseIf('(')) {
            SelectQueryImpl<Record> result = parseSelect(degree, with);
            parse(')');
            return result;
        }

        if (peekKeyword("VALUES"))
            return (SelectQueryImpl<Record>) dsl.selectQuery(parseTableValueConstructor());
        else if (peekKeyword("TABLE"))
            return (SelectQueryImpl<Record>) dsl.selectQuery(parseExplicitTable());

        ignoreHints(false);
        parseKeyword("SELECT", "SEL");
        String hints = parseHints();
        boolean distinct = parseKeywordIf("DISTINCT") || parseKeywordIf("UNIQUE");
        List<Field<?>> distinctOn = null;

        if (distinct) {
            if (parseKeywordIf("ON")) {
                parse('(');
                distinctOn = parseList(',', ParseContext::parseField);
                parse(')');
            }
        }
        else
            parseKeywordIf("ALL");

        Param<Long> limit = null;
        Param<Long> offset = null;
        boolean percent = false;
        boolean withTies = false;

        // T-SQL style TOP .. START AT
        if (parseKeywordIf("TOP")) {
            limit = requireParam(parseParenthesisedUnsignedIntegerOrBindVariable());
            percent = parseKeywordIf("PERCENT") && requireProEdition();

            if (parseKeywordIf("START AT"))
                offset = requireParam(parseParenthesisedUnsignedIntegerOrBindVariable());
            else if (parseKeywordIf("WITH TIES"))
                withTies = true;
        }

        // Informix style SKIP .. FIRST
        else if (parseKeywordIf("SKIP")) {
            offset = requireParam(parseParenthesisedUnsignedIntegerOrBindVariable());

            if (parseKeywordIf("FIRST"))
                limit = requireParam(parseParenthesisedUnsignedIntegerOrBindVariable());
        }
        else if (parseKeywordIf("FIRST")) {
            limit = requireParam(parseParenthesisedUnsignedIntegerOrBindVariable());
        }

        List<SelectFieldOrAsterisk> select = parseSelectList();

        degreeCheck:
        if (degree != null && !degree.equals(0) && !degree.equals(select.size())) {
            for (SelectFieldOrAsterisk s : select)
                if (!(s instanceof Field<?>))
                    break degreeCheck;

            throw exception("Select list must contain " + degree + " columns. Got: " + select.size());
        }

        Table<?> intoTable = null;



        List<Table<?>> from = null;

        if (parseKeywordIf("INTO")) {
            if (proEdition()) {





















            }
            else
                intoTable = parseTableName();
        }

        if (parseKeywordIf("FROM"))
            from = parseList(',', ParseContext::parseTable);

        // TODO is there a better way?
        if (from != null && from.size() == 1 && from.get(0).getName().equalsIgnoreCase("dual"))
            from = null;

        // [#9061] Register tables in scope as early as possible
        // TODO: Move this into parseTables() so lateral joins can profit from lookups (?)
        if (from != null)
            for (Table<?> table : from)
                scope(table);

        SelectQueryImpl<Record> result = new SelectQueryImpl<>(dsl.configuration(), with);

        if (hints != null)
            result.addHint(hints);

        if (distinct)
            result.setDistinct(distinct);

        if (distinctOn != null)
            result.addDistinctOn(distinctOn);

        if (!select.isEmpty())
            result.addSelect(select);

        if (intoTable != null)
            result.setInto(intoTable);






        if (from != null)
            result.addFrom(from);

        // [#10638] [#11403] Oracle and Teradata seem to support (but not document)
        //                   arbitrary ordering between these clauses
        boolean where = false;
        boolean connectBy = false;
        boolean startWith = false;
        boolean groupBy = false;
        boolean having = false;

        while ((!where && (where = parseQueryPrimaryWhere(result)))
            || (!connectBy && (connectBy = parseQueryPrimaryConnectBy(result)))
            || (!startWith && (startWith = parseQueryPrimaryStartWith(result)))
            || (!groupBy && (groupBy = parseQueryPrimaryGroupBy(result)))
            || (!having && (having = parseQueryPrimaryHaving(result))))
            ;

        if (startWith && !connectBy)
            throw expected("CONNECT BY");

        if (parseKeywordIf("WINDOW"))
            result.addWindow(parseWindowDefinitions());

        if (parseKeywordIf("QUALIFY"))
            result.addQualify(parseCondition());

        if (limit != null)
            if (offset != null)
                result.addLimit(offset, limit);
            else
                result.addLimit(limit);

        if (percent)



            ;

        if (withTies)
            result.setWithTies(true);

        return result;
    }

    private final boolean parseQueryPrimaryWhere(SelectQueryImpl<Record> result) {
        if (parseKeywordIf("WHERE")) {
            result.addConditions(parseCondition());
            return true;
        }
        else
            return false;
    }

    private final boolean parseQueryPrimaryHaving(SelectQueryImpl<Record> result) {
        if (parseKeywordIf("HAVING")) {
            result.addHaving(parseCondition());
            return true;
        }
        else
            return false;
    }

    private final boolean parseQueryPrimaryGroupBy(SelectQueryImpl<Record> result) {
        List<GroupField> groupBy;

        if (parseKeywordIf("GROUP BY")) {
            if (parseIf('(')) {
                parse(')');
                result.addGroupBy();
            }
            else if (parseKeywordIf("ROLLUP")) {
                parse('(');
                result.addGroupBy(rollup(parseList(',', ParseContext::parseField).toArray(EMPTY_FIELD)));
                parse(')');
            }
            else if (parseKeywordIf("CUBE")) {
                parse('(');
                result.addGroupBy(cube(parseList(',', ParseContext::parseField).toArray(EMPTY_FIELD)));
                parse(')');
            }
            else if (parseKeywordIf("GROUPING SETS")) {
                parse('(');
                List<List<Field<?>>> fieldSets = parseList(',', c -> parseFieldsOrEmptyParenthesised());
                parse(')');
                result.addGroupBy(groupingSets(fieldSets.toArray((Collection[]) EMPTY_COLLECTION)));
            }
            else {
                groupBy = (List) parseList(',', ParseContext::parseField);

                if (parseKeywordIf("WITH ROLLUP"))
                    result.addGroupBy(rollup(groupBy.toArray(EMPTY_FIELD)));
                else
                    result.addGroupBy(groupBy);
            }

            return true;
        }
        else
            return false;
    }

    private final boolean parseQueryPrimaryConnectBy(SelectQueryImpl<Record> result) {
        if (parseKeywordIf("CONNECT BY") && requireProEdition()) {







            return true;
        }
        else
            return false;
    }

    private final boolean parseQueryPrimaryStartWith(SelectQueryImpl<Record> result) {
        if (parseKeywordIf("START WITH") && requireProEdition()) {



            return true;
        }
        else
            return false;
    }

    private final List<WindowDefinition> parseWindowDefinitions() {
        return parseList(',', c -> {
            Name name = parseIdentifier();
            parseKeyword("AS");
            parse('(');
            WindowDefinition result = name.as(parseWindowSpecificationIf(null, true));
            parse(')');
            return result;
        });
    }

    private final WindowSpecification parseWindowSpecificationIf(Name windowName, boolean orderByAllowed) {
        final WindowSpecificationOrderByStep s1;
        final WindowSpecificationRowsStep s2;
        final WindowSpecificationRowsAndStep s3;
        final WindowSpecificationExcludeStep s4;
        final WindowSpecification result;

        s1 = windowName != null
            ? windowName.as()
            : parseKeywordIf("PARTITION BY")
            ? partitionBy(parseList(',', ParseContext::parseField))
            : null;

        if (parseKeywordIf("ORDER BY"))
            if (orderByAllowed)
                s2 = s1 == null
                    ? orderBy(parseList(',', ParseContext::parseSortField))
                    : s1.orderBy(parseList(',', ParseContext::parseSortField));
            else
                throw exception("ORDER BY not allowed");
        else
            s2 = s1;

        boolean rows = parseKeywordIf("ROWS");
        boolean range = !rows && parseKeywordIf("RANGE");
        boolean groups = !rows && !range && parseKeywordIf("GROUPS");

        if ((rows || range || groups) && !orderByAllowed)
            throw exception("ROWS, RANGE, or GROUPS not allowed");

        if (rows || range || groups) {
            Long n;

            if (parseKeywordIf("BETWEEN")) {
                if (parseKeywordIf("UNBOUNDED"))
                    if (parseKeywordIf("PRECEDING"))
                        s3 = s2 == null
                            ?     rows
                                ? rowsBetweenUnboundedPreceding()
                                : range
                                ? rangeBetweenUnboundedPreceding()
                                : groupsBetweenUnboundedPreceding()
                            :     rows
                                ? s2.rowsBetweenUnboundedPreceding()
                                : range
                                ? s2.rangeBetweenUnboundedPreceding()
                                : s2.groupsBetweenUnboundedPreceding();
                    else if (parseKeywordIf("FOLLOWING"))
                        s3 = s2 == null
                            ?     rows
                                ? rowsBetweenUnboundedFollowing()
                                : range
                                ? rangeBetweenUnboundedFollowing()
                                : groupsBetweenUnboundedFollowing()
                            :     rows
                                ? s2.rowsBetweenUnboundedFollowing()
                                : range
                                ? s2.rangeBetweenUnboundedFollowing()
                                : s2.groupsBetweenUnboundedFollowing();
                    else
                        throw expected("FOLLOWING", "PRECEDING");
                else if (parseKeywordIf("CURRENT ROW"))
                    s3 = s2 == null
                        ?     rows
                            ? rowsBetweenCurrentRow()
                            : range
                            ? rangeBetweenCurrentRow()
                            : groupsBetweenCurrentRow()
                        :     rows
                            ? s2.rowsBetweenCurrentRow()
                            : range
                            ? s2.rangeBetweenCurrentRow()
                            : s2.groupsBetweenCurrentRow();
                else if ((n = parseUnsignedIntegerLiteralIf()) != null)
                    if (parseKeywordIf("PRECEDING"))
                        s3 = s2 == null
                            ?     rows
                                ? rowsBetweenPreceding(n.intValue())
                                : range
                                ? rangeBetweenPreceding(n.intValue())
                                : groupsBetweenPreceding(n.intValue())
                            :     rows
                                ? s2.rowsBetweenPreceding(n.intValue())
                                : range
                                ? s2.rangeBetweenPreceding(n.intValue())
                                : s2.groupsBetweenPreceding(n.intValue());
                    else if (parseKeywordIf("FOLLOWING"))
                        s3 = s2 == null
                            ?     rows
                                ? rowsBetweenFollowing(n.intValue())
                                : range
                                ? rangeBetweenFollowing(n.intValue())
                                : groupsBetweenFollowing(n.intValue())
                            :     rows
                                ? s2.rowsBetweenFollowing(n.intValue())
                                : range
                                ? s2.rangeBetweenFollowing(n.intValue())
                                : s2.groupsBetweenFollowing(n.intValue());
                    else
                        throw expected("FOLLOWING", "PRECEDING");
                else
                    throw expected("CURRENT ROW", "UNBOUNDED", "integer literal");

                parseKeyword("AND");

                if (parseKeywordIf("UNBOUNDED"))
                    if (parseKeywordIf("PRECEDING"))
                        s4 =  s3.andUnboundedPreceding();
                    else if (parseKeywordIf("FOLLOWING"))
                        s4 =  s3.andUnboundedFollowing();
                    else
                        throw expected("FOLLOWING", "PRECEDING");
                else if (parseKeywordIf("CURRENT ROW"))
                    s4 =  s3.andCurrentRow();
                else if ((n = parseUnsignedIntegerLiteral()) != null)
                    if (parseKeywordIf("PRECEDING"))
                        s4 =  s3.andPreceding(n.intValue());
                    else if (parseKeywordIf("FOLLOWING"))
                        s4 =  s3.andFollowing(n.intValue());
                    else
                        throw expected("FOLLOWING", "PRECEDING");
                else
                    throw expected("CURRENT ROW", "UNBOUNDED", "integer literal");
            }
            else if (parseKeywordIf("UNBOUNDED"))
                if (parseKeywordIf("PRECEDING"))
                    s4 = s2 == null
                        ?     rows
                            ? rowsUnboundedPreceding()
                            : range
                            ? rangeUnboundedPreceding()
                            : groupsUnboundedPreceding()
                        :     rows
                            ? s2.rowsUnboundedPreceding()
                            : range
                            ? s2.rangeUnboundedPreceding()
                            : s2.groupsUnboundedPreceding();
                else if (parseKeywordIf("FOLLOWING"))
                    s4 = s2 == null
                        ?     rows
                            ? rowsUnboundedFollowing()
                            : range
                            ? rangeUnboundedFollowing()
                            : groupsUnboundedFollowing()
                        :     rows
                            ? s2.rowsUnboundedFollowing()
                            : range
                            ? s2.rangeUnboundedFollowing()
                            : s2.groupsUnboundedFollowing();
                else
                    throw expected("FOLLOWING", "PRECEDING");
            else if (parseKeywordIf("CURRENT ROW"))
                s4 = s2 == null
                    ?     rows
                        ? rowsCurrentRow()
                        : range
                        ? rangeCurrentRow()
                        : groupsCurrentRow()
                    :     rows
                        ? s2.rowsCurrentRow()
                        : range
                        ? s2.rangeCurrentRow()
                        : s2.groupsCurrentRow();
            else if ((n = parseUnsignedIntegerLiteral()) != null)
                if (parseKeywordIf("PRECEDING"))
                    s4 = s2 == null
                        ?     rows
                            ? rowsPreceding(n.intValue())
                            : range
                            ? rangePreceding(n.intValue())
                            : groupsPreceding(n.intValue())
                        :     rows
                            ? s2.rowsPreceding(n.intValue())
                            : range
                            ? s2.rangePreceding(n.intValue())
                            : s2.groupsPreceding(n.intValue());
                else if (parseKeywordIf("FOLLOWING"))
                    s4 = s2 == null
                        ?     rows
                            ? rowsFollowing(n.intValue())
                            : range
                            ? rangeFollowing(n.intValue())
                            : groupsFollowing(n.intValue())
                        :     rows
                            ? s2.rowsFollowing(n.intValue())
                            : range
                            ? s2.rangeFollowing(n.intValue())
                            : s2.groupsFollowing(n.intValue());
                else
                    throw expected("FOLLOWING", "PRECEDING");
            else
                throw expected("BETWEEN", "CURRENT ROW", "UNBOUNDED", "integer literal");

            if (parseKeywordIf("EXCLUDE"))
                if (parseKeywordIf("CURRENT ROW"))
                    result = s4.excludeCurrentRow();
                else if (parseKeywordIf("TIES"))
                    result = s4.excludeTies();
                else if (parseKeywordIf("GROUP"))
                    result = s4.excludeGroup();
                else if (parseKeywordIf("NO OTHERS"))
                    result = s4.excludeNoOthers();
                else
                    throw expected("CURRENT ROW", "TIES", "GROUP", "NO OTHERS");
            else
                result = s4;
        }
        else
            result = s2;

        if (result != null)
            return result;
        else if (windowName != null)
            return null;
        else if ((windowName = parseIdentifierIf()) != null)
            return parseWindowSpecificationIf(windowName, orderByAllowed);
        else
            return null;
    }

    private final Query parseDelete(WithImpl with, boolean parseResultQuery) {
        parseKeyword("DELETE", "DEL");
        Param<Long> limit = null;

        // T-SQL style TOP .. START AT
        if (parseKeywordIf("TOP")) {
            limit = requireParam(parseParenthesisedUnsignedIntegerOrBindVariable());

            // [#8623] TODO Support this
            // percent = parseKeywordIf("PERCENT") && requireProEdition();
        }

        parseKeywordIf("FROM");
        Table<?> table = parseTable(() -> peekKeyword(KEYWORDS_IN_DELETE_FROM));

        scope(table);

        DeleteUsingStep<?> s1 = with == null ? dsl.delete(table) : with.delete(table);
        DeleteWhereStep<?> s2 = parseKeywordIf("USING", "FROM") ? s1.using(parseList(',', t -> parseTable(() -> peekKeyword(KEYWORDS_IN_DELETE_FROM)))) : s1;
        DeleteOrderByStep<?> s3 = parseKeywordIf("WHERE") ? s2.where(parseCondition()) : s2;
        DeleteLimitStep<?> s4 = parseKeywordIf("ORDER BY") ? s3.orderBy(parseList(',', ParseContext::parseSortField)) : s3;
        DeleteReturningStep<?> s5 = (limit != null || parseKeywordIf("LIMIT"))
            ? s4.limit(limit != null ? limit : requireParam(parseParenthesisedUnsignedIntegerOrBindVariable()))
            : s4;
        return (parseResultQuery ? parseKeyword("RETURNING") : parseKeywordIf("RETURNING"))
            ? s5.returning(parseSelectList())
            : s5;
    }

    private final Query parseInsert(WithImpl with, boolean parseResultQuery) {
        scopeStart();
        parseKeyword("INSERT", "INS");
        parseKeywordIf("INTO");
        Table<?> table = parseTableNameIf();
        if (table == null)
            table = table(parseSelect());

        Name alias;
        if (parseKeywordIf("AS"))
            table = table.as(parseIdentifier());
        else if (!peekKeyword("DEFAULT VALUES", "SEL", "SELECT", "SET", "VALUES")
            && (alias = parseIdentifierIf()) != null)
            table = table.as(alias);

        scope(table);

        InsertSetStep<?> s1 = (with == null ? dsl.insertInto(table) : with.insertInto(table));
        Field<?>[] fields = null;

        if (!peekSelectOrWith(true) && parseIf('(')) {
            fields = parseList(',', c -> parseField()).toArray(EMPTY_FIELD);
            parse(')');
        }

        InsertOnDuplicateStep<?> onDuplicate;
        InsertReturningStep<?> returning;

        try {
            // [#11821] The Teradata INSERT INTO t (1, 2) syntax can be recognised:
            //          When there are non-references fields
            boolean hasExpressions = anyMatch(fields, f -> !(f instanceof TableField));

            if (hasExpressions || parseKeywordIf("VALUES")) {
                List<List<Field<?>>> allValues = new ArrayList<>();

                if (hasExpressions) {
                    allValues.add(asList(fields));
                    fields = null;
                }

                valuesLoop:
                do {
                    if (hasExpressions && !parseIf(','))
                        break valuesLoop;

                    parse('(');

                    // [#6936] MySQL treats an empty VALUES() clause as the same thing as the standard DEFAULT VALUES
                    if (fields == null && parseIf(')'))
                        break valuesLoop;

                    List<Field<?>> values = parseList(',', c -> c.parseKeywordIf("DEFAULT") ? default_() : c.parseField());

                    if (fields != null && fields.length != values.size())
                        throw exception("Insert field size (" + fields.length + ") must match values size (" + values.size() + ")");

                    allValues.add(values);
                    parse(')');
                }
                while (parseIf(','));

                InsertValuesStepN<?> step2 = (fields != null)
                    ? s1.columns(fields)
                    : (InsertValuesStepN<?>) s1;

                for (List<Field<?>> values : allValues)
                    step2 = step2.values(values);

                returning = onDuplicate = step2;
            }
            else if (parseKeywordIf("SET")) {
                Map<Field<?>, Object> map = parseSetClauseList();

                returning = onDuplicate =  s1.set(map);
            }
            else if (peekSelectOrWith(true)){

                // [#10954] These are moved into the INSERT .. SELECT clause handling. They should not be necessary here
                //          either, but it seems we currently don't correctly implement nesting scopes?
                scopeEnd(null);
                scopeStart();

                Select<?> select = parseWithOrSelect();

                returning = onDuplicate = (fields == null)
                    ? s1.select(select)
                    : s1.columns(fields).select(select);
            }
            else if (parseKeywordIf("DEFAULT VALUES")) {
                if (fields != null)
                    throw notImplemented("DEFAULT VALUES without INSERT field list");
                else
                    returning = onDuplicate = s1.defaultValues();
            }
            else
                throw expected("DEFAULT VALUES", "WITH", "SELECT", "SET", "VALUES");

            if (parseKeywordIf("ON")) {
                if (parseKeywordIf("DUPLICATE KEY UPDATE")) {
                    parseKeywordIf("SET");

                    InsertOnConflictWhereStep<?> where = onDuplicate.onDuplicateKeyUpdate().set(parseSetClauseList());

                    if (parseKeywordIf("WHERE"))
                        returning = where.where(parseCondition());
                    else
                        returning = where;
                }
                else if (parseKeywordIf("DUPLICATE KEY IGNORE")) {
                    returning = onDuplicate.onDuplicateKeyIgnore();
                }
                else if (parseKeywordIf("CONFLICT")) {
                    InsertOnConflictDoUpdateStep<?> doUpdate;

                    if (parseKeywordIf("ON CONSTRAINT")) {
                        doUpdate = onDuplicate.onConflictOnConstraint(parseName());
                    }
                    else if (parseIf('(')) {
                        InsertOnConflictWhereIndexPredicateStep<?> where = onDuplicate.onConflict(parseList(',', c -> parseFieldName()));
                        parse(')');

                        doUpdate = parseKeywordIf("WHERE")
                            ? where.where(parseCondition())
                            : where;
                    }
                    else {
                        doUpdate = onDuplicate.onConflict();
                    }

                    parseKeyword("DO");
                    if (parseKeywordIf("NOTHING")) {
                        returning = doUpdate.doNothing();
                    }
                    else if (parseKeywordIf("UPDATE SET")) {
                        InsertOnConflictWhereStep<?> where = doUpdate.doUpdate().set(parseSetClauseList());

                        if (parseKeywordIf("WHERE"))
                            returning = where.where(parseCondition());
                        else
                            returning = where;
                    }
                    else
                        throw expected("NOTHING", "UPDATE");
                }
                else
                    throw expected("CONFLICT", "DUPLICATE");
            }

            return (parseResultQuery ? parseKeyword("RETURNING") : parseKeywordIf("RETURNING"))
                ? returning.returning(parseSelectList())
                : returning;
        }
        finally {
            scopeEnd(((InsertImpl) s1).getDelegate());
        }
    }

    private final Query parseUpdate(WithImpl with, boolean parseResultQuery) {
        parseKeyword("UPDATE", "UPD");
        Param<Long> limit = null;

        // T-SQL style TOP .. START AT
        if (parseKeywordIf("TOP")) {
            limit = requireParam(parseParenthesisedUnsignedIntegerOrBindVariable());

            // [#8623] TODO Support this
            // percent = parseKeywordIf("PERCENT") && requireProEdition();
        }

        Table<?> table = parseTable(() -> peekKeyword(KEYWORDS_IN_UPDATE_FROM));

        scope(table);

        UpdateSetFirstStep<?> s1 = (with == null ? dsl.update(table) : with.update(table));
        List<Table<?>> from = parseKeywordIf("FROM") ? parseList(',', t -> parseTable(() -> peekKeyword(KEYWORDS_IN_UPDATE_FROM))) : null;

        parseKeyword("SET");
        UpdateFromStep<?> s2;

        if (peek('(')) {
            Row row = parseRow();
            parse('=');

            // TODO Can we extract a public API for this?
            if (peekSelectOrWith(true))
                ((UpdateImpl<?>) s1).getDelegate().addValues0(row, parseWithOrSelect(row.size()));
            else
                ((UpdateImpl<?>) s1).getDelegate().addValues0(row, parseRow(row.size()));

            s2 = (UpdateFromStep<?>) s1;
        }
        else {
            Map<Field<?>, Object> map = parseSetClauseList();
            s2 = s1.set(map);
        }

        UpdateWhereStep<?> s3 = from != null
            ? s2.from(from)
            : parseKeywordIf("FROM")
            ? s2.from(parseList(',', t -> parseTable(() -> peekKeyword(KEYWORDS_IN_UPDATE_FROM))))
            : s2;
        UpdateOrderByStep<?> s4 = parseKeywordIf("WHERE") ? s3.where(parseCondition()) : s3;
        UpdateLimitStep<?> s5 = parseKeywordIf("ORDER BY") ? s4.orderBy(parseList(',', ParseContext::parseSortField)) : s4;
        UpdateReturningStep<?> s6 = (limit != null || parseKeywordIf("LIMIT"))
            ? s5.limit(limit != null ? limit : requireParam(parseParenthesisedUnsignedIntegerOrBindVariable()))
            : s5;
        return (parseResultQuery ? parseKeyword("RETURNING") : parseKeywordIf("RETURNING"))
            ? s6.returning(parseSelectList())
            : s6;
    }

    private final Map<Field<?>, Object> parseSetClauseList() {
        Map<Field<?>, Object> map = new LinkedHashMap<>();

        do {
            Field<?> field = parseFieldName();

            if (map.containsKey(field))
                throw exception("Duplicate column in set clause list: " + field);

            parse('=');

            Field<?> value = parseKeywordIf("DEFAULT") ? default_() : parseField();
            map.put(field,  value);
        }
        while (parseIf(','));

        return map;
    }

    private final Merge<?> parseMerge(WithImpl with) {
        parseKeyword("MERGE");
        parseKeywordIf("INTO");
        Table<?> target = parseTableName();

        if (parseKeywordIf("AS") || !peekKeyword("USING"))
            target = target.as(parseIdentifier());

        parseKeyword("USING");
        Table<?> table = null;
        Select<?> using = null;

        if (parseIf('(')) {
            using = parseSelect();
            parse(')');
        }
        else {
            table = parseTableName();
        }

        TableLike<?> usingTable = parseCorrelationNameIf(table != null ? table : using, () -> peekKeyword("ON"));
        parseKeyword("ON");
        Condition on = parseCondition();
        boolean update = false;
        boolean insert = false;
        Field<?>[] insertColumns = null;
        List<Field<?>> insertValues = null;
        Condition insertWhere = null;
        Map<Field<?>, Object> updateSet = null;
        Condition updateAnd = null;
        Condition updateWhere = null;
        Condition deleteWhere = null;

        MergeUsingStep<?> s1 = (with == null ? dsl.mergeInto(target) : with.mergeInto(target));
        MergeMatchedStep<?> s2 = s1.using(usingTable).on(on);

        for (;;) {
            if (parseKeywordIf("WHEN MATCHED")) {
                update = true;

                if (parseKeywordIf("AND"))
                    updateAnd = parseCondition();

                if (parseKeywordIf("THEN DELETE")) {
                    s2 = updateAnd != null
                       ? s2.whenMatchedAnd(updateAnd).thenDelete()
                       : s2.whenMatchedThenDelete();
                }
                else {
                    parseKeyword("THEN UPDATE SET");
                    updateSet = parseSetClauseList();

                    if (updateAnd == null && parseKeywordIf("WHERE"))
                        updateWhere = parseCondition();

                    if (updateAnd == null && parseKeywordIf("DELETE WHERE"))
                        deleteWhere = parseCondition();

                    if (updateAnd != null) {
                        s2.whenMatchedAnd(updateAnd).thenUpdate().set(updateSet);
                    }
                    else {
                        MergeMatchedWhereStep<?> s3 = s2.whenMatchedThenUpdate().set(updateSet);
                        MergeMatchedDeleteStep<?> s4 = updateWhere != null ? s3.where(updateWhere) : s3;
                        s2 = deleteWhere != null ? s4.deleteWhere(deleteWhere) : s3;
                    }
                }
            }
            else if (!insert && (insert = parseKeywordIf("WHEN NOT MATCHED"))) {
                if (parseKeywordIf("AND"))
                    insertWhere = parseCondition();

                parseKeyword("THEN INSERT");
                parse('(');
                insertColumns = Tools.fieldsByName(parseIdentifiers().toArray(EMPTY_NAME));
                parse(')');
                parseKeyword("VALUES");
                parse('(');
                insertValues = parseList(',', c -> c.parseKeywordIf("DEFAULT") ? default_() : c.parseField());
                parse(')');

                if (insertColumns.length != insertValues.size())
                    throw exception("Insert column size (" + insertColumns.length + ") must match values size (" + insertValues.size() + ")");

                if (insertWhere == null && parseKeywordIf("WHERE"))
                    insertWhere = parseCondition();
            }
            else
                break;
        }

        if (!update && !insert)
            throw exception("At least one of UPDATE or INSERT clauses is required");

        // TODO support multi clause MERGE
        // TODO support DELETE
        Merge<?> s3 = insert
            ? insertWhere != null
                ? s2.whenNotMatchedThenInsert(insertColumns).values(insertValues).where(insertWhere)
                : s2.whenNotMatchedThenInsert(insertColumns).values(insertValues)
            : s2;

        return s3;
    }

    private final Query parseOpen() {
        parseKeyword("OPEN");
        parseKeyword("SCHEMA");

        return parseSetSchema();
    }

    private final Query parseSet() {
        parseKeyword("SET");

        if (parseKeywordIf("CATALOG"))
            return parseSetCatalog();
        else if (parseKeywordIf("CURRENT SCHEMA"))
            return parseSetSchema();
        else if (parseKeywordIf("CURRENT SQLID"))
            return parseSetSchema();
        else if (parseKeywordIf("GENERATOR"))
            return parseSetGenerator();
        else if (parseKeywordIf("SCHEMA"))
            return parseSetSchema();
        else if (parseKeywordIf("SEARCH_PATH"))
            return parseSetSearchPath();
        else
            return parseSetCommand();
    }

    private final Query parseSetCommand() {
        if (TRUE.equals(settings().isParseSetCommands())) {
            Name name = parseIdentifier();

            // TODO: [#9780] Are there any possible syntaxes and data types?
            parseIf('=');
            Object value = parseSignedIntegerLiteralIf();
            return dsl.set(name, value != null ? inline(value) : inline(parseStringLiteral()));
        }

        // There are many SET commands in programs like sqlplus, which we'll simply ignore
        else {
            parseUntilEOL();
            return IGNORE_NO_DELIMITER;
        }
    }

    private final Query parseSetCatalog() {
        return dsl.setCatalog(parseCatalogName());
    }

    private final Query parseUse() {
        parseKeyword("USE");
        parseKeywordIf("DATABASE");
        return dsl.setCatalog(parseCatalogName());
    }

    private final Query parseSetSchema() {
        parseIf('=');
        return dsl.setSchema(parseSchemaName());
    }

    private final Query parseSetSearchPath() {
        if (!parseIf('='))
            parseKeyword("TO");

        Schema schema = null;

        do {
            Schema s = parseSchemaName();
            if (schema == null)
                schema = s;
        }
        while (parseIf(','));

        return dsl.setSchema(schema);
    }

    private final DDLQuery parseCommentOn() {
        parseKeyword("COMMENT ON");

        CommentOnIsStep s1;

        if (parseKeywordIf("COLUMN")) {
            s1 = dsl.commentOnColumn(parseFieldName());
        }
        else if (parseKeywordIf("TABLE")) {
            Table<?> table = parseTableName();

            if (parseIf('(')) {
                s1 = dsl.commentOnColumn(table.getQualifiedName().append(parseIdentifier()));
                parseKeyword("IS");
                DDLQuery s2 = s1.is(parseStringLiteral());
                parse(')');
                return s2;
            }
            else
                s1 = dsl.commentOnTable(table);
        }
        else if (parseKeywordIf("VIEW")) {
            s1 = dsl.commentOnView(parseTableName());
        }

        // Ignored no-arg object comments
        // https://www.postgresql.org/docs/10/static/sql-comment.html
        // https://docs.oracle.com/database/121/SQLRF/statements_4010.htm
        else if (parseAndGetKeywordIf(
            "ACCESS METHOD",
            "AUDIT POLICY",
            "COLLATION",
            "CONVERSION",
            "DATABASE",
            "DOMAIN",
            "EDITION",
            "EXTENSION",
            "EVENT TRIGGER",
            "FOREIGN DATA WRAPPER",
            "FOREIGN TABLE",
            "INDEX",
            "INDEXTYPE",
            "LANGUAGE",
            "LARGE OBJECT",
            "MATERIALIZED VIEW",
            "MINING MODEL",
            "OPERATOR",
            "PROCEDURAL LANGUAGE",
            "PUBLICATION",
            "ROLE",
            "SCHEMA",
            "SEQUENCE",
            "SERVER",
            "STATISTICS",
            "SUBSCRIPTION",
            "TABLESPACE",
            "TEXT SEARCH CONFIGURATION",
            "TEXT SEARCH DICTIONARY",
            "TEXT SEARCH PARSER",
            "TEXT SEARCH TEMPLATE",
            "TYPE",
            "VIEW"
        ) != null) {
            parseIdentifier();
            parseKeyword("IS");
            parseStringLiteral();
            return IGNORE;
        }

        // TODO: (PostgreSQL)
        // AGGREGATE, CAST, FUNCTION, OPERATOR, OPERATOR CLASS, OPERATOR FAMILY

        // Ignored object comments with arguments
        // https://www.postgresql.org/docs/10/static/sql-comment.html
        else if (parseKeywordIf("CONSTRAINT")) {
            parseIdentifier();
            parseKeyword("ON");
            parseKeywordIf("DOMAIN");
            parseName();
            parseKeyword("IS");
            parseStringLiteral();
            return IGNORE;
        }
        else if (parseAndGetKeywordIf(
            "POLICY",
            "RULE",
            "TRIGGER"
        ) != null) {
            parseIdentifier();
            parseKeyword("ON");
            parseIdentifier();
            parseKeyword("IS");
            parseStringLiteral();
            return IGNORE;
        }
        else if (parseKeywordIf("TRANSFORM FOR")) {
            parseIdentifier();
            parseKeyword("LANGUAGE");
            parseIdentifier();
            parseKeyword("IS");
            parseStringLiteral();
            return IGNORE;
        }
        else
            throw unsupportedClause();

        parseKeyword("IS");
        return s1.is(parseStringLiteral());
    }

    private final DDLQuery parseCreate() {
        parseKeyword("CREATE");

        switch (characterUpper()) {
            case 'D':
                if (parseKeywordIf("DATABASE"))
                    return parseCreateDatabase();
                else if (parseKeywordIf("DOMAIN"))
                    return parseCreateDomain();

                break;

            case 'E':
                if (parseKeywordIf("EXTENSION"))
                    return parseCreateExtension();

                break;

            case 'F':
                if (parseKeywordIf("FORCE VIEW"))
                    return parseCreateView(false);
                else if (parseKeywordIf("FULLTEXT INDEX") && requireUnsupportedSyntax())
                    return parseCreateIndex(false);
                else if (parseKeywordIf("FUNCTION") && requireProEdition())



                    ;

                break;

            case 'G':
                if (parseKeywordIf("GENERATOR"))
                    return parseCreateSequence();
                else if (parseKeywordIf("GLOBAL TEMP TABLE", "GLOBAL TEMPORARY TABLE"))
                    return parseCreateTable(true);

                break;

            case 'I':
                if (parseKeywordIf("INDEX"))
                    return parseCreateIndex(false);

                break;

            case 'O':
                if (parseKeywordIf("OR")) {
                    parseKeyword("REPLACE", "ALTER");

                    if (parseKeywordIf("TRIGGER") && requireProEdition())



                        ;
                    else if (parseKeywordIf("VIEW", "FORCE VIEW"))
                        return parseCreateView(true);
                    else if (parseKeywordIf("FUNCTION") && requireProEdition())



                        ;
                    else if (parseKeywordIf("PACKAGE"))
                        throw notImplemented("CREATE PACKAGE", "https://github.com/jOOQ/jOOQ/issues/9190");
                    else if (parseKeywordIf("PROC", "PROCEDURE") && requireProEdition())



                        ;
                    else
                        throw expected("FUNCTION", "PACKAGE", "PROCEDURE", "TRIGGER", "VIEW");
                }

                break;

            case 'P':
                if (parseKeywordIf("PACKAGE"))
                    throw notImplemented("CREATE PACKAGE", "https://github.com/jOOQ/jOOQ/issues/9190");
                else if (parseKeywordIf("PROC", "PROCEDURE") && requireProEdition())



                    ;

                break;

            case 'R':
                if (parseKeywordIf("ROLE"))
                    throw notImplemented("CREATE ROLE", "https://github.com/jOOQ/jOOQ/issues/10167");

                break;

            case 'S':
                if (parseKeywordIf("SCHEMA"))
                    return parseCreateSchema();
                else if (parseKeywordIf("SEQUENCE"))
                    return parseCreateSequence();
                else if (parseKeywordIf("SPATIAL INDEX") && requireUnsupportedSyntax())
                    return parseCreateIndex(false);
                else if (parseKeywordIf("SYNONYM"))
                    throw notImplemented("CREATE SYNONYM", "https://github.com/jOOQ/jOOQ/issues/9574");

                break;

            case 'T':
                if (parseKeywordIf("TABLE"))
                    return parseCreateTable(false);
                else if (parseKeywordIf("TEMP TABLE", "TEMPORARY TABLE"))
                    return parseCreateTable(true);
                else if (parseKeywordIf("TRIGGER") && requireProEdition())



                    ;
                else if (parseKeywordIf("TYPE"))
                    return parseCreateType();
                else if (parseKeywordIf("TABLESPACE"))
                    throw notImplemented("CREATE TABLESPACE");

                break;

            case 'U':
                if (parseKeywordIf("UNIQUE INDEX"))
                    return parseCreateIndex(true);
                else if (parseKeywordIf("USER"))
                    throw notImplemented("CREATE USER", "https://github.com/jOOQ/jOOQ/issues/10167");

                break;

            case 'V':
                if (parseKeywordIf("VIEW"))
                    return parseCreateView(false);
                else if (parseKeywordIf("VIRTUAL") && parseKeyword("TABLE"))
                    return parseCreateTable(false);

                break;
        }

        throw expected(
            "FUNCTION",
            "GENERATOR",
            "GLOBAL TEMPORARY TABLE",
            "INDEX",
            "OR ALTER",
            "OR REPLACE",
            "PROCEDURE",
            "SCHEMA",
            "SEQUENCE",
            "TABLE",
            "TEMPORARY TABLE",
            "TRIGGER",
            "TYPE",
            "UNIQUE INDEX",
            "VIEW"
        );
    }

    private final Query parseAlter() {
        parseKeyword("ALTER");

        switch (characterUpper()) {
            case 'D':
                if (parseKeywordIf("DATABASE"))
                    return parseAlterDatabase();
                else if (parseKeywordIf("DOMAIN"))
                    return parseAlterDomain();

                break;

            case 'E':
                if (parseKeywordIf("EXTENSION"))
                    throw notImplemented("ALTER EXTENSION");

                break;

            case 'F':
                if (parseKeywordIf("FUNCTION"))
                    throw notImplemented("ALTER FUNCTION", "https://github.com/jOOQ/jOOQ/issues/9190");

                break;

            case 'I':
                if (parseKeywordIf("INDEX"))
                    return parseAlterIndex();

                break;

            case 'P':
                if (parseKeywordIf("PACKAGE"))
                    throw notImplemented("ALTER PACKAGE", "https://github.com/jOOQ/jOOQ/issues/9190");
                else if (parseKeywordIf("PROCEDURE"))
                    throw notImplemented("ALTER PROCEDURE", "https://github.com/jOOQ/jOOQ/issues/9190");

                break;

            case 'R':
                if (parseKeywordIf("ROLE"))
                    throw notImplemented("ALTER ROLE", "https://github.com/jOOQ/jOOQ/issues/10167");

                break;

            case 'S':
                if (parseKeywordIf("SCHEMA"))
                    return parseAlterSchema();
                else if (parseKeywordIf("SEQUENCE"))
                    return parseAlterSequence();
                else if (parseKeywordIf("SESSION"))
                    return parseAlterSession();
                else if (parseKeywordIf("SYNONYM"))
                    throw notImplemented("ALTER SYNONYM", "https://github.com/jOOQ/jOOQ/issues/9574");

                break;

            case 'T':
                if (parseKeywordIf("TABLE"))
                    return parseAlterTable();
                else if (parseKeywordIf("TYPE"))
                    return parseAlterType();
                else if (parseKeywordIf("TABLESPACE"))
                    throw notImplemented("ALTER TABLESPACE");
                else if (parseKeywordIf("TRIGGER"))
                    throw notImplemented("ALTER TRIGGER", "https://github.com/jOOQ/jOOQ/issues/6956");

                break;

            case 'U':
                if (parseKeywordIf("USER"))
                    throw notImplemented("ALTER USER", "https://github.com/jOOQ/jOOQ/issues/10167");

                break;

            case 'V':
                if (parseKeywordIf("VIEW"))
                    return parseAlterView();

                break;
        }

        throw expected("DOMAIN", "INDEX", "SCHEMA", "SEQUENCE", "SESSION", "TABLE", "TYPE", "VIEW");
    }

    private final DDLQuery parseDrop() {
        parseKeyword("DROP");

        switch (characterUpper()) {
            case 'D':
                if (parseKeywordIf("DATABASE"))
                    return parseDropDatabase();
                else if (parseKeywordIf("DOMAIN"))
                    return parseDropDomain();

                break;

            case 'E':
                if (parseKeywordIf("EXTENSION"))
                    return parseDropExtension();

                break;

            case 'F':
                if (parseKeywordIf("FUNCTION") && requireProEdition())



                    ;

                break;

            case 'G':
                if (parseKeywordIf("GENERATOR"))
                    return parseDropSequence();

                break;

            case 'I':
                if (parseKeywordIf("INDEX"))
                    return parseDropIndex();

                break;

            case 'P':
                if (parseKeywordIf("PACKAGE"))
                    throw notImplemented("DROP PACKAGE", "https://github.com/jOOQ/jOOQ/issues/9190");
                else if (parseKeywordIf("PROC", "PROCEDURE") && requireProEdition())



                    ;

                break;

            case 'R':
                if (parseKeywordIf("ROLE"))
                    throw notImplemented("DROP ROLE", "https://github.com/jOOQ/jOOQ/issues/10167");

                break;

            case 'S':
                if (parseKeywordIf("SEQUENCE"))
                    return parseDropSequence();
                else if (parseKeywordIf("SCHEMA"))
                    return parseDropSchema();

                break;

            case 'T':
                if (parseKeywordIf("TABLE"))
                    return parseDropTable(false);
                else if (parseKeywordIf("TEMPORARY TABLE"))
                    return parseDropTable(true);
                else if (parseKeywordIf("TRIGGER") && requireProEdition())



                    ;
                else if (parseKeywordIf("TYPE"))
                    return parseDropType();
                else if (parseKeywordIf("TABLESPACE"))
                    throw notImplemented("DROP TABLESPACE");

                break;

            case 'U':
                if (parseKeywordIf("USER"))
                    throw notImplemented("DROP USER", "https://github.com/jOOQ/jOOQ/issues/10167");

                break;

            case 'V':
                if (parseKeywordIf("VIEW"))
                    return parseDropView();

                break;
        }

        throw expected(
            "GENERATOR",
            "FUNCTION",
            "INDEX",
            "PROCEDURE",
            "SCHEMA",
            "SEQUENCE",
            "TABLE",
            "TEMPORARY TABLE",
            "TRIGGER",
            "TYPE",
            "VIEW"
        );
    }

    private final Truncate<?> parseTruncate() {
        parseKeyword("TRUNCATE");
        parseKeywordIf("TABLE");
        Table<?> table = parseTableName();
        boolean continueIdentity = parseKeywordIf("CONTINUE IDENTITY");
        boolean restartIdentity = !continueIdentity && parseKeywordIf("RESTART IDENTITY");
        boolean cascade = parseKeywordIf("CASCADE");
        boolean restrict = !cascade && parseKeywordIf("RESTRICT");

        TruncateIdentityStep<?> step1 = dsl.truncate(table);
        TruncateCascadeStep<?> step2 =
              continueIdentity
            ? step1.continueIdentity()
            : restartIdentity
            ? step1.restartIdentity()
            : step1;

        Truncate<?> step3 =
              cascade
            ? step2.cascade()
            : restrict
            ? step2.restrict()
            : step2;

        return step3;
    }

    private final DDLQuery parseGrant() {
        parseKeyword("GRANT");
        Privilege privilege = parsePrivilege();
        List<Privilege> privileges = null;

        while (parseIf(',')) {
            if (privileges == null) {
                privileges = new ArrayList<>();
                privileges.add(privilege);
            }

            privileges.add(parsePrivilege());
        }

        parseKeyword("ON");
        parseKeywordIf("TABLE");
        Table<?> table = parseTableName();

        parseKeyword("TO");
        User user = parseKeywordIf("PUBLIC") ? null : parseUser();

        GrantOnStep s1 = privileges == null ? dsl.grant(privilege) : dsl.grant(privileges);
        GrantToStep s2 = s1.on(table);
        GrantWithGrantOptionStep s3 = user == null ? s2.toPublic() : s2.to(user);

        return parseKeywordIf("WITH GRANT OPTION")
            ? s3.withGrantOption()
            : s3;
    }

    private final DDLQuery parseRevoke() {
        parseKeyword("REVOKE");
        boolean grantOptionFor = parseKeywordIf("GRANT OPTION FOR");
        Privilege privilege = parsePrivilege();
        List<Privilege> privileges = null;

        while (parseIf(',')) {
            if (privileges == null) {
                privileges = new ArrayList<>();
                privileges.add(privilege);
            }

            privileges.add(parsePrivilege());
        }

        parseKeyword("ON");
        parseKeywordIf("TABLE");
        Table<?> table = parseTableName();

        RevokeOnStep s1 = grantOptionFor
            ? privileges == null
                ? dsl.revokeGrantOptionFor(privilege)
                : dsl.revokeGrantOptionFor(privileges)
            : privileges == null
                ? dsl.revoke(privilege)
                : dsl.revoke(privileges);

        parseKeyword("FROM");
        User user = parseKeywordIf("PUBLIC") ? null : parseUser();

        RevokeFromStep s2 = s1.on(table);
        return user == null ? s2.fromPublic() : s2.from(user);
    }

    private final Query parseExec() {
        if (parseKeywordIf("EXEC SP_RENAME")) {
            if (parseKeywordIf("@OBJNAME"))
                parse('=');
            Name oldName = dsl.parser().parseName(parseStringLiteral());

            parse(',');
            if (parseKeywordIf("@NEWNAME"))
                parse('=');

            Name newName = dsl.parser().parseName(parseStringLiteral());
            String objectType = "TABLE";
            if (parseIf(',')) {
                if (parseKeywordIf("@OBJTYPE"))
                    parse('=');

                if (!parseKeywordIf("NULL"))
                    objectType = parseStringLiteral();
            }

            if ("TABLE".equalsIgnoreCase(objectType))
                return dsl.alterTable(oldName).renameTo(newName.unqualifiedName());
            else if ("INDEX".equalsIgnoreCase(objectType))
                return dsl.alterIndex(oldName).renameTo(newName.unqualifiedName());
            else if ("COLUMN".equalsIgnoreCase(objectType))
                return dsl.alterTable(oldName.qualifier()).renameColumn(oldName.unqualifiedName()).to(newName.unqualifiedName());
            else
                throw exception("Unsupported object type: " + objectType);
        }
        else {
            if (requireProEdition()) {



            }

            throw unsupportedClause();
        }
    }

    private final Block parseBlock(boolean allowDeclareSection) {
        LanguageContext previous = languageContext;

        try {
            if (languageContext == LanguageContext.QUERY)
                languageContext = LanguageContext.BLOCK;

            List<Statement> statements = new ArrayList<>();





            if (allowDeclareSection && parseKeywordIf("DECLARE") && requireProEdition())



                ;
            else
                parseKeywordIf("EXECUTE BLOCK AS");

            parseKeyword("BEGIN");
            parseKeywordIf("ATOMIC", "NOT ATOMIC");
            statements.addAll(parseStatementsAndPeek("END"));
            parseKeyword("END");



            parseIf(';');





            return dsl.begin(statements);
        }
        finally {
            languageContext = previous;
        }
    }

    private final void parseSemicolonAfterNonBlocks(Statement result) {
        if (!(result instanceof Block))
            parseIf(';');
        else if (result instanceof BlockImpl && !((BlockImpl) result).alwaysWrapInBeginEnd)
            parseIf(';');
    }

    private final Statement parseStatementAndSemicolon() {
        Statement result = parseStatementAndSemicolonIf();

        if (result == null)
            throw expected("Statement");

        return result;
    }

    final Statement parseStatementAndSemicolonIf() {
        Statement result = parseStatement();
        parseSemicolonAfterNonBlocks(result);
        return result;
    }

    private final List<Statement> parseStatements(boolean peek, String... keywords) {
        List<Statement> statements = new ArrayList<>();

        for (;;) {
            if (peek && peekKeyword(keywords) || !peek && parseKeywordIf(keywords))
                break;

            Statement parsed;
            Statement stored;




            stored = parsed = parseStatement();

            if (parsed == null)
                break;






            statements.add(stored);
            parseSemicolonAfterNonBlocks(parsed);
        }

        return statements;
    }

    private final List<Statement> parseStatementsAndPeek(String... keywords) {
        return parseStatements(true, keywords);
    }

    private final List<Statement> parseStatementsAndKeyword(String... keywords) {
        return parseStatements(false, keywords);
    }



































































    private final Block parseDo() {
        parseKeyword("DO");
        return (Block) dsl.parser().parseQuery(parseStringLiteral());
    }

    private final Statement parseStatement() {
        switch (characterUpper()) {
            case 'C':
                if (peekKeyword("CALL") && requireProEdition())



                ;
                else if (peekKeyword("CONTINUE") && requireProEdition())



                ;

                break;

            case 'D':
                if (peekKeyword("DECLARE") && requireProEdition())







                ;
                else if (peekKeyword("DEFINE") && requireProEdition())



                ;

                break;

            case 'E':
                if (peekKeyword("EXECUTE PROCEDURE", "EXEC") && requireProEdition())



                ;
                if (peekKeyword("EXECUTE") && !peekKeyword("EXECUTE BLOCK") && requireProEdition())



                ;
                else if (peekKeyword("EXIT") && requireProEdition())



                ;

                break;

            case 'F':
                if (peekKeyword("FOR") && requireProEdition())



                ;

                break;

            case 'G':
                if (peekKeyword("GOTO") && requireProEdition())



                ;

                break;

            case 'I':
                if (peekKeyword("IF") && requireProEdition())



                ;
                else if (peekKeyword("ITERATE") && requireProEdition())



                ;

                break;

            case 'L':
                if (peekKeyword("LEAVE") && requireProEdition())



                ;
                else if (peekKeyword("LET") && requireProEdition())



                ;
                else if (peekKeyword("LOOP") && requireProEdition())



                ;

                break;

            case 'N':
                if (peekKeyword("NULL"))
                    return parseNullStatement();

                break;

            case 'R':
                if (peekKeyword("REPEAT") && requireProEdition())



                ;
                else if (peekKeyword("RETURN") && requireProEdition())



                ;
                else if (peekKeyword("RAISE") && requireProEdition())



                ;

                break;

            case 'S':
                if (peekKeyword("SET") && requireProEdition())



                ;
                else if (peekKeyword("SIGNAL") && requireProEdition())



                ;

                break;

            case 'W':
                if (peekKeyword("WHILE") && requireProEdition())



                ;

                break;
        }











        return parseQuery(false, false);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Statement parsing
    // -----------------------------------------------------------------------------------------------------------------

    private final Statement parseNullStatement() {
        parseKeyword("NULL");
        return new NullStatement();
    }



























































































































































































































































































































































































































































    // -----------------------------------------------------------------------------------------------------------------
    // Statement clause parsing
    // -----------------------------------------------------------------------------------------------------------------

    private final Privilege parsePrivilege() {
        if (parseKeywordIf("SELECT"))
            return privilege(K_SELECT);
        else if (parseKeywordIf("INSERT"))
            return privilege(K_INSERT);
        else if (parseKeywordIf("UPDATE"))
            return privilege(K_UPDATE);
        else if (parseKeywordIf("DELETE"))
            return privilege(K_DELETE);
        else
            throw expected("DELETE", "INSERT", "SELECT", "UPDATE");
    }

    private final User parseUser() {
        return user(parseName());
    }

    private final DDLQuery parseCreateView(boolean orReplace) {
        boolean ifNotExists = !orReplace && parseKeywordIf("IF NOT EXISTS");
        Table<?> view = parseTableName();
        Field<?>[] fields = EMPTY_FIELD;

        if (parseIf('(')) {
            fields = parseList(',', c -> parseFieldName()).toArray(fields);
            parse(')');
        }

        parseKeyword("AS");
        Select<?> select = parseWithOrSelect();
        int degree = Tools.degree(select);

        if (fields.length > 0 && fields.length != degree)
            throw exception("Select list size (" + degree + ") must match declared field size (" + fields.length + ")");

        return ifNotExists
            ? dsl.createViewIfNotExists(view, fields).as(select)
            : orReplace
            ? dsl.createOrReplaceView(view, fields).as(select)
            : dsl.createView(view, fields).as(select);
    }

    private final DDLQuery parseCreateExtension() {
        parseKeywordIf("IF NOT EXISTS");
        parseIdentifier();
        parseKeywordIf("WITH");
        if (parseKeywordIf("SCHEMA"))
            parseIdentifier();
        if (parseKeywordIf("VERSION"))
            if (parseIdentifierIf() == null)
                parseStringLiteral();
        if (parseKeywordIf("FROM"))
            if (parseIdentifierIf() == null)
                parseStringLiteral();
        parseKeywordIf("CASCADE");
        return IGNORE;
    }

    private final DDLQuery parseDropExtension() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        parseIdentifiers();
        ifExists = ifExists || parseKeywordIf("IF EXISTS");
        if (!parseKeywordIf("CASCADE"))
            parseKeywordIf("RESTRICT");
        return IGNORE;
    }

    private final DDLQuery parseAlterView() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Table<?> oldName = parseTableName();

        if (parseKeywordIf("RENAME")) {
            parseKeyword("AS", "TO");
            Table<?> newName = parseTableName();

            return ifExists
                ? dsl.alterViewIfExists(oldName).renameTo(newName)
                : dsl.alterView(oldName).renameTo(newName);
        }
        else if (parseKeywordIf("OWNER TO") && parseUser() != null)
            return IGNORE;
        else if (parseKeywordIf("SET"))
            return dsl.alterView(oldName).comment(parseOptionsDescription());
        else
            throw expected("OWNER TO", "RENAME", "SET");
    }

    private final Comment parseOptionsDescription() {
        parseKeyword("OPTIONS");
        parse('(');
        parseKeyword("DESCRIPTION");
        parse('=');
        Comment comment = parseComment();
        parse(')');

        return comment;
    }

    private final DDLQuery parseDropView() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Table<?> tableName = parseTableName();
        ifExists = ifExists || parseKeywordIf("IF EXISTS");

        return ifExists
            ? dsl.dropViewIfExists(tableName)
            : dsl.dropView(tableName);
    }

    private final DDLQuery parseCreateSequence() {
        boolean ifNotExists = parseKeywordIf("IF NOT EXISTS");
        Sequence<?> schemaName = parseSequenceName();

        CreateSequenceFlagsStep s = ifNotExists
            ? dsl.createSequenceIfNotExists(schemaName)
            : dsl.createSequence(schemaName);

        boolean startWith = false;
        boolean incrementBy = false;
        boolean minvalue = false;
        boolean maxvalue = false;
        boolean cycle = false;
        boolean cache = false;

        for (;;) {
            Field<Long> field;

            if (!startWith && (startWith |= (field = parseSequenceStartWithIf()) != null))
                s = s.startWith(field);
            else if (!incrementBy && (incrementBy |= (field = parseSequenceIncrementByIf()) != null))
                s = s.incrementBy(field);
            else if (!minvalue && (minvalue |= (field = parseSequenceMinvalueIf()) != null))
                s = s.minvalue(field);
            else if (!minvalue && (minvalue |= parseSequenceNoMinvalueIf()))
                s = s.noMinvalue();
            else if (!maxvalue && (maxvalue |= (field = parseSequenceMaxvalueIf()) != null))
                s = s.maxvalue(field);
            else if (!maxvalue && (maxvalue |= parseSequenceNoMaxvalueIf()))
                s = s.noMaxvalue();
            else if (!cycle && (cycle |= parseKeywordIf("CYCLE")))
                s = s.cycle();
            else if (!cycle && (cycle |= parseSequenceNoCycleIf()))
                s = s.noCycle();
            else if (!cache && (cache |= (field = parseSequenceCacheIf()) != null))
                s = s.cache(field);
            else if (!cache && (cache |= parseSequenceNoCacheIf()))
                s = s.noCache();
            else
                break;
        }

        return s;
    }

    private final DDLQuery parseAlterSequence() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Sequence<?> sequenceName = parseSequenceName();

        AlterSequenceStep s = ifExists
            ? dsl.alterSequenceIfExists(sequenceName)
            : dsl.alterSequence(sequenceName);

        if (parseKeywordIf("RENAME")) {
            parseKeyword("AS", "TO");
            return s.renameTo(parseSequenceName());
        }
        else if (parseKeywordIf("OWNER TO") && parseUser() != null) {
            return IGNORE;
        }
        else {
            boolean found = false;
            boolean restart = false;
            boolean startWith = false;
            boolean incrementBy = false;
            boolean minvalue = false;
            boolean maxvalue = false;
            boolean cycle = false;
            boolean cache = false;

            AlterSequenceFlagsStep s1 = s;
            while (true) {
                Field<Long> field;

                if (!startWith && (startWith |= (field = parseSequenceStartWithIf()) != null))
                    s1 = s1.startWith(field);
                else if (!incrementBy && (incrementBy |= (field = parseSequenceIncrementByIf()) != null))
                    s1 = s1.incrementBy(field);
                else if (!minvalue && (minvalue |= (field = parseSequenceMinvalueIf()) != null))
                    s1 = s1.minvalue(field);
                else if (!minvalue && (minvalue |= parseSequenceNoMinvalueIf()))
                    s1 = s1.noMinvalue();
                else if (!maxvalue && (maxvalue |= (field = parseSequenceMaxvalueIf()) != null))
                    s1 = s1.maxvalue(field);
                else if (!maxvalue && (maxvalue |= parseSequenceNoMaxvalueIf()))
                    s1 = s1.noMaxvalue();
                else if (!cycle && (cycle |= parseKeywordIf("CYCLE")))
                    s1 = s1.cycle();
                else if (!cycle && (cycle |= parseSequenceNoCycleIf()))
                    s1 = s1.noCycle();
                else if (!cache && (cache |= (field = parseSequenceCacheIf()) != null))
                    s1 = s1.cache(field);
                else if (!cache && (cache |= parseSequenceNoCacheIf()))
                    s1 = s1.noCache();
                else if (!restart && (restart |= parseKeywordIf("RESTART"))) {
                    if (parseKeywordIf("WITH"))
                        s1 = s1.restartWith(parseUnsignedIntegerOrBindVariable());
                    else
                        s1 = s1.restart();
                }
                else
                    break;

                found = true;
            }

            if (!found)
                throw expected(
                    "CACHE",
                    "CYCLE",
                    "INCREMENT BY",
                    "MAXVALUE",
                    "MINVALUE",
                    "NO CACHE",
                    "NO CYCLE",
                    "NO MAXVALUE",
                    "NO MINVALUE",
                    "OWNER TO",
                    "RENAME TO",
                    "RESTART",
                    "START WITH"
                );

            return s1;
        }
    }

    private final boolean parseSequenceNoCacheIf() {
        return parseKeywordIf("NO CACHE") || parseKeywordIf("NOCACHE");
    }

    private final Field<Long> parseSequenceCacheIf() {
        return parseKeywordIf("CACHE") && (parseIf("=") || true) ? parseUnsignedIntegerOrBindVariable() : null;
    }

    private final boolean parseSequenceNoCycleIf() {
        return parseKeywordIf("NO CYCLE") || parseKeywordIf("NOCYCLE");
    }

    private final boolean parseSequenceNoMaxvalueIf() {
        return parseKeywordIf("NO MAXVALUE") || parseKeywordIf("NOMAXVALUE");
    }

    private final Field<Long> parseSequenceMaxvalueIf() {
        return parseKeywordIf("MAXVALUE") && (parseIf("=") || true) ? parseUnsignedIntegerOrBindVariable() : null;
    }

    private final boolean parseSequenceNoMinvalueIf() {
        return parseKeywordIf("NO MINVALUE") || parseKeywordIf("NOMINVALUE");
    }

    private final Field<Long> parseSequenceMinvalueIf() {
        return parseKeywordIf("MINVALUE") && (parseIf("=") || true) ? parseUnsignedIntegerOrBindVariable() : null;
    }

    private final Field<Long> parseSequenceIncrementByIf() {
        return parseKeywordIf("INCREMENT") && (parseKeywordIf("BY") || parseIf("=") || true) ? parseUnsignedIntegerOrBindVariable() : null;
    }

    private final Field<Long> parseSequenceStartWithIf() {
        return parseKeywordIf("START") && (parseKeywordIf("WITH") || parseIf("=") || true) ? parseUnsignedIntegerOrBindVariable() : null;
    }

    private final Query parseAlterSession() {
        parseKeyword("SET CURRENT_SCHEMA");
        parse('=');
        return dsl.setSchema(parseSchemaName());
    }

    private final DDLQuery parseSetGenerator() {
        Sequence<?> sequenceName = parseSequenceName();
        parseKeyword("TO");
        return dsl.alterSequence((Sequence) sequenceName).restartWith(parseUnsignedIntegerLiteral());
    }

    private final DDLQuery parseDropSequence() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Sequence<?> sequenceName = parseSequenceName();
        ifExists = ifExists || parseKeywordIf("IF EXISTS");
        parseKeywordIf("RESTRICT");

        return ifExists
            ? dsl.dropSequenceIfExists(sequenceName)
            : dsl.dropSequence(sequenceName);
    }

    private final DDLQuery parseCreateTable(boolean temporary) {
        boolean ifNotExists = parseKeywordIf("IF NOT EXISTS");
        Table<?> tableName = DSL.table(parseTableName().getQualifiedName());

        if (parseKeywordIf("USING"))
            parseIdentifier();

        CreateTableCommentStep commentStep;
        CreateTableStorageStep storageStep;

        List<Field<?>> fields = new ArrayList<>();
        List<Constraint> constraints = new ArrayList<>();
        List<Index> indexes = new ArrayList<>();
        boolean primary = false;
        boolean identity = false;

        // Three valued boolean:
        // null: Possibly CTAS
        // true: Definitely CTAS
        // false: Definitely not CTAS
        Boolean ctas = null;

        if (parseIf('(')) {

            columnLoop:
            do {
                int p = position();

                ConstraintTypeStep constraint = parseConstraintNameSpecification();

                if (parsePrimaryKeyClusteredNonClusteredKeywordIf()) {
                    if (primary)
                        throw exception("Duplicate primary key specification");

                    primary = true;
                    constraints.add(parsePrimaryKeySpecification(constraint));
                    continue columnLoop;
                }
                else if (parseKeywordIf("UNIQUE")) {
                    if (!parseKeywordIf("KEY"))
                        parseKeywordIf("INDEX");

                    // [#9132] Avoid parsing "using" as an identifier
                    parseUsingIndexTypeIf();

                    // [#7268] MySQL has some legacy syntax where an index name
                    //         can override a constraint name
                    Name index = parseIdentifierIf();
                    if (index != null)
                        constraint = constraint(index);

                    constraints.add(parseUniqueSpecification(constraint));
                    continue columnLoop;
                }
                else if (parseKeywordIf("FOREIGN KEY")) {
                    constraints.add(parseForeignKeySpecification(constraint));
                    continue columnLoop;
                }
                else if (parseKeywordIf("CHECK")) {
                    constraints.add(parseCheckSpecification(constraint));
                    continue columnLoop;
                }
                else if (constraint == null && parseIndexOrKeyIf()) {
                    parseUsingIndexTypeIf();

                    int p2 = position();

                    // [#7348] [#7651] [#9132] Look ahead if the next tokens
                    // indicate a MySQL index definition
                    if (parseIf('(') || (parseIdentifierIf() != null
                                && parseUsingIndexTypeIf()
                                && parseIf('('))) {
                        position(p2);
                        indexes.add(parseIndexSpecification(tableName));

                        parseUsingIndexTypeIf();
                        continue columnLoop;
                    }
                    else {
                        position(p);
                    }
                }
                else if (constraint != null)
                    throw expected("CHECK", "CONSTRAINT", "FOREIGN KEY", "INDEX", "KEY", "PRIMARY KEY", "UNIQUE");

                Name fieldName = parseIdentifier();

                if (ctas == null)
                    ctas = peek(',') || peek(')');

                // If only we had multiple return values or destructuring...
                ParseInlineConstraints inlineConstraints = parseInlineConstraints(
                    fieldName,
                    !TRUE.equals(ctas) ? parseDataType() : SQLDataType.OTHER,
                    constraints,
                    primary,
                    identity
                );

                primary = inlineConstraints.primary;
                identity = inlineConstraints.identity;

                if (ctas)
                    fields.add(field(fieldName));
                else
                    fields.add(field(fieldName, inlineConstraints.type, inlineConstraints.fieldComment));
            }
            while (parseIf(','));

            if (fields.isEmpty())
                throw expected("At least one column");

            parse(')');
        }
        else
            ctas = true;

        CreateTableColumnStep columnStep = ifNotExists
            ? temporary
                ? dsl.createTemporaryTableIfNotExists(tableName)
                : dsl.createTableIfNotExists(tableName)
            : temporary
                ? dsl.createTemporaryTable(tableName)
                : dsl.createTable(tableName);

        if (!fields.isEmpty())
            columnStep = columnStep.columns(fields);

        if (TRUE.equals(ctas) && parseKeyword("AS") ||
           !FALSE.equals(ctas) && parseKeywordIf("AS")) {
            boolean previousMetaLookupsForceIgnore = metaLookupsForceIgnore();
            CreateTableWithDataStep withDataStep = columnStep.as((Select<Record>) metaLookupsForceIgnore(false).parseQuery(true, true));
            metaLookupsForceIgnore(previousMetaLookupsForceIgnore);
            commentStep =
                  parseKeywordIf("WITH DATA")
                ? withDataStep.withData()
                : parseKeywordIf("WITH NO DATA")
                ? withDataStep.withNoData()
                : withDataStep;
        }
        else {
            CreateTableConstraintStep constraintStep = constraints.isEmpty()
                ? columnStep
                : columnStep.constraints(constraints);
            CreateTableOnCommitStep onCommitStep = indexes.isEmpty()
                ? constraintStep
                : constraintStep.indexes(indexes);

            // [#6133] TODO Support this also with CTAS
            if (temporary && parseKeywordIf("ON COMMIT")) {
                if (parseKeywordIf("DELETE ROWS"))
                    commentStep = onCommitStep.onCommitDeleteRows();
                else if (parseKeywordIf("DROP"))
                    commentStep = onCommitStep.onCommitDrop();
                else if (parseKeywordIf("PRESERVE ROWS"))
                    commentStep = onCommitStep.onCommitPreserveRows();
                else
                    throw unsupportedClause();
            }
            else
                commentStep = onCommitStep;
        }

        storageStep = commentStep;

        List<SQL> storage = new ArrayList<>();
        Comment comment = null;

        storageLoop:
        for (boolean first = true;; first = false) {
            boolean optional = first || !parseIf(',');
            Keyword keyword = null;

            // MySQL storage clauses (see: https://dev.mysql.com/doc/refman/5.7/en/create-table.html)
            if ((keyword = parseAndGetKeywordIf("AUTO_INCREMENT")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseFieldUnsignedNumericLiteral(Sign.NONE)));
            }
            else if ((keyword = parseAndGetKeywordIf("AVG_ROW_LENGTH")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseFieldUnsignedNumericLiteral(Sign.NONE)));
            }
            else if ((keyword = parseAndGetKeywordIf("CHARACTER SET")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseIdentifier()));
            }
            else if ((keyword = parseAndGetKeywordIf("DEFAULT CHARACTER SET")) != null
                  || (keyword = parseAndGetKeywordIf("DEFAULT CHARSET")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseIdentifier()));
            }
            else if ((keyword = parseAndGetKeywordIf("CHECKSUM")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseZeroOne()));
            }
            else if ((keyword = parseAndGetKeywordIf("COLLATE")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseIdentifier()));
            }
            else if ((keyword = parseAndGetKeywordIf("DEFAULT COLLATE")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseIdentifier()));
            }

            // [#10164] In a statement batch, this could already be the next statement
            else if (!peekKeyword("COMMENT ON") && parseKeywordIf("COMMENT")) {
                if (!parseIf('='))
                    parseKeywordIf("IS");
                comment = parseComment();
            }
            else if (peekKeyword("OPTIONS")) {
                comment = parseOptionsDescription();
            }
            else if ((keyword = parseAndGetKeywordIf("COMPRESSION")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseStringLiteral()));
            }
            else if ((keyword = parseAndGetKeywordIf("CONNECTION")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseStringLiteral()));
            }
            else if ((keyword = parseAndGetKeywordIf("DATA DIRECTORY")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseStringLiteral()));
            }
            else if ((keyword = parseAndGetKeywordIf("INDEX DIRECTORY")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseStringLiteral()));
            }
            else if ((keyword = parseAndGetKeywordIf("DELAY_KEY_WRITE")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseZeroOne()));
            }
            else if ((keyword = parseAndGetKeywordIf("ENCRYPTION")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseStringLiteral()));
            }
            else if ((keyword = parseAndGetKeywordIf("ENGINE")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseIdentifier()));
            }
            else if ((keyword = parseAndGetKeywordIf("INSERT_METHOD")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseAndGetKeyword("NO", "FIRST", "LAST")));
            }
            else if ((keyword = parseAndGetKeywordIf("KEY_BLOCK_SIZE")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseFieldUnsignedNumericLiteral(Sign.NONE)));
            }
            else if ((keyword = parseAndGetKeywordIf("MAX_ROWS")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseFieldUnsignedNumericLiteral(Sign.NONE)));
            }
            else if ((keyword = parseAndGetKeywordIf("MIN_ROWS")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseFieldUnsignedNumericLiteral(Sign.NONE)));
            }
            else if ((keyword = parseAndGetKeywordIf("PACK_KEYS")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseZeroOneDefault()));
            }
            else if ((keyword = parseAndGetKeywordIf("PASSWORD")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseStringLiteral()));
            }
            else if ((keyword = parseAndGetKeywordIf("ROW_FORMAT")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseAndGetKeyword("DEFAULT", "DYNAMIC", "FIXED", "COMPRESSED", "REDUNDANT", "COMPACT")));
            }
            else if ((keyword = parseAndGetKeywordIf("STATS_AUTO_RECALC")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseZeroOneDefault()));
            }
            else if ((keyword = parseAndGetKeywordIf("STATS_PERSISTENT")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseZeroOneDefault()));
            }
            else if ((keyword = parseAndGetKeywordIf("STATS_SAMPLE_PAGES")) != null) {
                parseIf('=');
                storage.add(sql("{0} {1}", keyword, parseFieldUnsignedNumericLiteral(Sign.NONE)));
            }
            else if ((keyword = parseAndGetKeywordIf("TABLESPACE")) != null) {
                storage.add(sql("{0} {1}", keyword, parseIdentifier()));

                if ((keyword = parseAndGetKeywordIf("STORAGE")) != null)
                    storage.add(sql("{0} {1}", keyword, parseAndGetKeyword("DISK", "MEMORY", "DEFAULT")));
            }
            else if ((keyword = parseAndGetKeywordIf("UNION")) != null) {
                parseIf('=');
                parse('(');
                storage.add(sql("{0} ({1})", keyword, list(parseIdentifiers())));
                parse(')');
            }
            else if (optional)
                break storageLoop;
            else
                throw expected("storage clause after ','");
        }

        if (comment != null)
            storageStep = commentStep.comment(comment);

        if (storage.size() > 0)
            return storageStep.storage(new SQLConcatenationImpl(storage.toArray(EMPTY_QUERYPART)));
        else
            return storageStep;
    }

    private static final /* record */ class ParseInlineConstraints { private final DataType<?> type; private final Comment fieldComment; private final boolean primary; private final boolean identity; public ParseInlineConstraints(DataType<?> type, Comment fieldComment, boolean primary, boolean identity) { this.type = type; this.fieldComment = fieldComment; this.primary = primary; this.identity = identity; } public DataType<?> type() { return type; } public Comment fieldComment() { return fieldComment; } public boolean primary() { return primary; } public boolean identity() { return identity; } @Override public boolean equals(Object o) { if (!(o instanceof ParseInlineConstraints)) return false; ParseInlineConstraints other = (ParseInlineConstraints) o; if (!java.util.Objects.equals(this.type, other.type)) return false; if (!java.util.Objects.equals(this.fieldComment, other.fieldComment)) return false; if (!java.util.Objects.equals(this.primary, other.primary)) return false; if (!java.util.Objects.equals(this.identity, other.identity)) return false; return true; } @Override public int hashCode() { return java.util.Objects.hash(this.type, this.fieldComment, this.primary, this.identity); } @Override public String toString() { return new StringBuilder("ParseInlineConstraints[").append("type=").append(this.type).append(", fieldComment=").append(this.fieldComment).append(", primary=").append(this.primary).append(", identity=").append(this.identity).append("]").toString(); } }

    private final ParseInlineConstraints parseInlineConstraints(
        Name fieldName,
        DataType<?> type,
        List<? super Constraint> constraints,
        boolean primary,
        boolean identity
    ) {
        boolean nullable = false;
        boolean defaultValue = false;
        boolean onUpdate = false;
        boolean unique = false;
        boolean comment = false;
        Comment fieldComment = null;

        identity |= type.identity();

        for (;;) {
            if (!nullable) {
                if (parseKeywordIf("NULL")) {
                    type = type.nullable(true);
                    nullable = true;
                    continue;
                }
                else if (parseNotNullOptionalEnable()) {
                    type = type.nullable(false);
                    nullable = true;
                    continue;
                }
            }

            if (!defaultValue) {
                if (!identity && parseKeywordIf("IDENTITY")) {
                    if (parseIf('(')) {
                        parseSignedIntegerLiteral();
                        parse(',');
                        parseSignedIntegerLiteral();
                        parse(')');
                    }

                    type = type.identity(true);
                    defaultValue = true;
                    identity = true;
                    continue;
                }
                else if (parseKeywordIf("DEFAULT")) {

                    // [#10963] Special case nextval('<id>_seq'::regclass)
                    if (parseSerialIf()) {
                        type = type.identity(true);
                    }
                    else {

                        // TODO: [#10116] Support this clause also in the jOOQ API
                        parseKeywordIf("ON NULL");

                        type = type.defaultValue((Field) toField(parseConcat()));

                        // TODO: [#10115] Support this clause also in the jOOQ API
                        parseKeywordIf("WITH VALUES");

                        defaultValue = true;
                    }

                    continue;
                }
                else if (!identity && parseKeywordIf("GENERATED")) {
                    if (!parseKeywordIf("ALWAYS")) {
                        parseKeyword("BY DEFAULT");

                        // TODO: Ignored keyword from Oracle
                        parseKeywordIf("ON NULL");
                    }

                    parseKeyword("AS IDENTITY");

                    // TODO: Ignored identity options from Oracle
                    if (parseIf('(')) {
                        boolean identityOption = false;

                        for (;;) {
                            if (identityOption)
                                parseIf(',');

                            if (parseKeywordIf("START WITH")) {
                                if (!parseKeywordIf("LIMIT VALUE"))
                                    parseUnsignedIntegerOrBindVariable();
                                identityOption = true;
                                continue;
                            }
                            else if (parseKeywordIf("INCREMENT BY")
                                  || parseKeywordIf("MAXVALUE")
                                  || parseKeywordIf("MINVALUE")
                                  || parseKeywordIf("CACHE")) {
                                parseUnsignedIntegerOrBindVariable();
                                identityOption = true;
                                continue;
                            }
                            else if (parseKeywordIf("NOMAXVALUE")
                                  || parseKeywordIf("NOMINVALUE")
                                  || parseKeywordIf("CYCLE")
                                  || parseKeywordIf("NOCYCLE")
                                  || parseKeywordIf("NOCACHE")
                                  || parseKeywordIf("ORDER")
                                  || parseKeywordIf("NOORDER")) {
                                identityOption = true;
                                continue;
                            }

                            if (identityOption)
                                break;
                            else
                                throw unsupportedClause();
                        }

                        parse(')');
                    }

                    type = type.identity(true);
                    defaultValue = true;
                    identity = true;
                    continue;
                }
            }

            if (!onUpdate) {
                if (parseKeywordIf("ON UPDATE")) {

                    // [#6132] TODO: Support this feature in the jOOQ DDL API
                    parseConcat();
                    onUpdate = true;
                    continue;
                }
            }

            ConstraintTypeStep inlineConstraint = parseConstraintNameSpecification();

            if (!unique) {
                if (!primary && parsePrimaryKeyClusteredNonClusteredKeywordIf()) {
                    if (!parseKeywordIf("CLUSTERED"))
                        parseKeywordIf("NONCLUSTERED");

                    constraints.add(parseConstraintEnforcementIf(inlineConstraint == null
                        ? primaryKey(fieldName)
                        : inlineConstraint.primaryKey(fieldName)));
                    primary = true;
                    unique = true;
                    continue;
                }
                else if (parseKeywordIf("UNIQUE")) {
                    if (!parseKeywordIf("KEY"))
                        parseKeywordIf("INDEX");

                    constraints.add(parseConstraintEnforcementIf(inlineConstraint == null
                        ? unique(fieldName)
                        : inlineConstraint.unique(fieldName)));
                    unique = true;
                    continue;
                }
            }

            if (parseKeywordIf("CHECK")) {
                constraints.add(parseCheckSpecification(inlineConstraint));
                continue;
            }

            if (parseKeywordIf("REFERENCES")) {
                constraints.add(parseForeignKeyReferenceSpecification(inlineConstraint, new Field[] { field(fieldName) }));
                continue;
            }

            if (!nullable) {
                if (parseKeywordIf("NULL")) {
                    type = type.nullable(true);
                    nullable = true;
                    continue;
                }
                else if (parseNotNullOptionalEnable()) {
                    type = type.nullable(false);
                    nullable = true;
                    continue;
                }
            }

            if (inlineConstraint != null)
                throw expected("CHECK", "NOT NULL", "NULL", "PRIMARY KEY", "REFERENCES", "UNIQUE");

            if (!identity) {
                if (parseKeywordIf("AUTO_INCREMENT") ||
                    parseKeywordIf("AUTOINCREMENT")) {
                    type = type.identity(true);
                    identity = true;
                    continue;
                }
            }

            if (!comment) {

                // [#10164] In a statement batch, this could already be the next statement
                if (!peekKeyword("COMMENT ON") && parseKeywordIf("COMMENT")) {
                    if (!parseIf('='))
                        parseKeywordIf("IS");
                    fieldComment = parseComment();
                    continue;
                }
                else if (peekKeyword("OPTIONS")) {
                    fieldComment = parseOptionsDescription();
                    continue;
                }
            }

            break;
        }

        return new ParseInlineConstraints(type, fieldComment, primary, identity);
    }

    private final boolean parseSerialIf() {
        int i = position();

        String s;
        if (parseFunctionNameIf("NEXTVAL")
            && parseIf('(')
            && ((s = parseStringLiteralIf()) != null)
            && s.toLowerCase().endsWith("_seq")
            && parseIf("::")
            && parseKeywordIf("REGCLASS")
            && parseIf(')'))
            return true;

        position(i);
        return false;
    }

    private final boolean parsePrimaryKeyClusteredNonClusteredKeywordIf() {
        if (!parseKeywordIf("PRIMARY KEY"))
            return false;

        if (!parseKeywordIf("CLUSTERED"))
            parseKeywordIf("NONCLUSTERED");

        return true;
    }

    private final DDLQuery parseCreateType() {
        Name name = parseName();
        parseKeyword("AS ENUM");
        List<String> values = new ArrayList<>();
        parse('(');

        if (!parseIf(')')) {
            values = parseList(',', ParseContext::parseStringLiteral);
            parse(')');
        }
        else
            values = new ArrayList<>();

        return dsl.createType(name).asEnum(values);
    }

    private final Index parseIndexSpecification(Table<?> table) {
        Name name = parseIdentifierIf();
        parseUsingIndexTypeIf();
        return Internal.createIndex(name == null ? NO_NAME : name, table, parseParenthesisedSortSpecification(), false);
    }

    private final Constraint parseConstraintEnforcementIf(ConstraintEnforcementStep e) {
        boolean deferrable = parseConstraintDeferrableIf();
        parseConstraintInitiallyIf();
        if (!deferrable)
            parseConstraintDeferrableIf();

        if ((parseKeywordIf("ENABLE") || parseKeywordIf("ENFORCED")))
            return e.enforced();
        else if ((parseKeywordIf("DISABLE") || parseKeywordIf("NOT ENFORCED")))
            return e.notEnforced();
        else
            return e;
    }

    private final boolean parseConstraintDeferrableIf() {
        return parseKeywordIf("DEFERRABLE") || parseKeywordIf("NOT DEFERRABLE");
    }

    private final boolean parseConstraintInitiallyIf() {
        return parseKeywordIf("INITIALLY") && parseKeyword("DEFERRED", "IMMEDIATE");
    }

    private final Constraint parsePrimaryKeySpecification(ConstraintTypeStep constraint) {
        parseUsingIndexTypeIf();
        Field<?>[] fieldNames = parseKeyColumnList();

        ConstraintEnforcementStep e = constraint == null
            ? primaryKey(fieldNames)
            : constraint.primaryKey(fieldNames);

        parseUsingIndexTypeIf();
        return parseConstraintEnforcementIf(e);
    }

    private final Constraint parseUniqueSpecification(ConstraintTypeStep constraint) {
        parseUsingIndexTypeIf();

        // [#9246] In MySQL, there's a syntax where the unique constraint looks like an index:
        //         ALTER TABLE t ADD UNIQUE INDEX i (c)
        Name constraintName;
        if (constraint == null && (constraintName = parseIdentifierIf()) != null)
            constraint = constraint(constraintName);

        Field<?>[] fieldNames = parseKeyColumnList();

        ConstraintEnforcementStep e = constraint == null
            ? unique(fieldNames)
            : constraint.unique(fieldNames);

        parseUsingIndexTypeIf();
        return parseConstraintEnforcementIf(e);
    }

    private final Field<?>[] parseKeyColumnList() {
        SortField<?>[] fieldExpressions = parseParenthesisedSortSpecification();
        Field<?>[] fieldNames = new Field[fieldExpressions.length];

        for (int i = 0; i < fieldExpressions.length; i++)
            if (fieldExpressions[i].getOrder() != SortOrder.DESC)
                fieldNames[i] = ((SortFieldImpl<?>) fieldExpressions[i]).getField();

            // [#7899] TODO: Support this in jOOQ
            else
                throw notImplemented("DESC sorting in constraints");

        return fieldNames;
    }

    private final Constraint parseCheckSpecification(ConstraintTypeStep constraint) {
        boolean parens = parseIf('(');
        Condition condition = parseCondition();
        if (parens)
            parse(')');

        ConstraintEnforcementStep e = constraint == null
            ? check(condition)
            : constraint.check(condition);

        return parseConstraintEnforcementIf(e);
    }

    private final Constraint parseForeignKeySpecification(ConstraintTypeStep constraint) {
        Name constraintName = null;
        if ((constraintName = parseIdentifierIf()) != null)
            if (constraint == null)
                constraint = constraint(constraintName);

        parse('(');
        Field<?>[] referencing = parseList(',', c -> parseFieldName()).toArray(EMPTY_FIELD);
        parse(')');
        parseKeyword("REFERENCES");

        return parseForeignKeyReferenceSpecification(constraint, referencing);
    }

    private final Constraint parseForeignKeyReferenceSpecification(ConstraintTypeStep constraint, Field<?>[] referencing) {
        Table<?> referencedTable = parseTableName();
        Field<?>[] referencedFields = EMPTY_FIELD;

        if (parseIf('(')) {
            referencedFields = parseList(',', c -> parseFieldName()).toArray(EMPTY_FIELD);
            parse(')');

            if (referencing.length != referencedFields.length)
                throw exception("Number of referencing columns (" + referencing.length + ") must match number of referenced columns (" + referencedFields.length + ")");
        }

        ConstraintForeignKeyOnStep e = constraint == null
            ? foreignKey(referencing).references(referencedTable, referencedFields)
            : constraint.foreignKey(referencing).references(referencedTable, referencedFields);

        boolean onDelete = false;
        boolean onUpdate = false;
        while ((!onDelete || !onUpdate) && parseKeywordIf("ON")) {
            if (!onDelete && parseKeywordIf("DELETE")) {
                onDelete = true;

                if (parseKeywordIf("CASCADE"))
                    e = e.onDeleteCascade();
                else if (parseKeywordIf("NO ACTION"))
                    e = e.onDeleteNoAction();
                else if (parseKeywordIf("RESTRICT"))
                    e = e.onDeleteRestrict();
                else if (parseKeywordIf("SET DEFAULT"))
                    e = e.onDeleteSetDefault();
                else if (parseKeywordIf("SET NULL"))
                    e = e.onDeleteSetNull();
                else
                    throw expected("CASCADE", "NO ACTION", "RESTRICT", "SET DEFAULT", "SET NULL");
            }
            else if (!onUpdate && parseKeywordIf("UPDATE")) {
                onUpdate = true;

                if (parseKeywordIf("CASCADE"))
                    e = e.onUpdateCascade();
                else if (parseKeywordIf("NO ACTION"))
                    e = e.onUpdateNoAction();
                else if (parseKeywordIf("RESTRICT"))
                    e = e.onUpdateRestrict();
                else if (parseKeywordIf("SET DEFAULT"))
                    e = e.onUpdateSetDefault();
                else if (parseKeywordIf("SET NULL"))
                    e = e.onUpdateSetNull();
                else
                    throw expected("CASCADE", "NO ACTION", "RESTRICT", "SET DEFAULT", "SET NULL");
            }
            else
                throw expected("DELETE", "UPDATE");
        }

        return parseConstraintEnforcementIf(e);
    }

    private static final Set<String> ALTER_KEYWORDS = new HashSet<>(Arrays.asList("ADD", "ALTER", "COMMENT", "DROP", "MODIFY", "RENAME"));

    private final DDLQuery parseAlterTable() {
        boolean ifTableExists = parseKeywordIf("IF EXISTS");
        Table<?> tableName;

        if (peekKeyword("ONLY")) {

            // [#7751] ONLY is only supported by PostgreSQL. In other RDBMS, it
            //         corresponds to a table name.
            Name only = parseIdentifier();
            int p = position();

            if ((tableName = parseTableNameIf()) == null || (
                    !tableName.getQualifiedName().qualified()
                &&  tableName.getUnqualifiedName().quoted() == Quoted.UNQUOTED
                &&  ALTER_KEYWORDS.contains(tableName.getName().toUpperCase()))) {
                tableName = table(only);
                position(p);
            }
        }
        else {
            tableName = parseTableName();
        }

        AlterTableStep s1 = ifTableExists
            ? dsl.alterTableIfExists(tableName)
            : dsl.alterTable(tableName);

        switch (characterUpper()) {
            case 'A':
                if (parseKeywordIf("ADD"))
                    return parseAlterTableAdd(s1, tableName);
                else if (parseKeywordIf("ALTER"))
                    if (parseKeywordIf("CONSTRAINT"))
                        return parseAlterTableAlterConstraint(s1);
                    else if ((parseKeywordIf("COLUMN") || true))
                        return parseAlterTableAlterColumn(s1);

                break;

            case 'C':

                // TODO: support all of the storageLoop from the CREATE TABLE statement
                if (parseKeywordIf("COMMENT")) {
                    if (!parseIf('='))
                        parseKeywordIf("IS");
                    return dsl.commentOnTable(tableName).is(parseStringLiteral());
                }

                break;

            case 'D':
                if (parseKeywordIf("DROP")) {
                    if (parseKeywordIf("CONSTRAINT")) {
                        return parseCascadeRestrictIf(parseKeywordIf("IF EXISTS")
                            ? s1.dropConstraintIfExists(parseIdentifier())
                            : s1.dropConstraint(parseIdentifier()));
                    }
                    else if (parseKeywordIf("UNIQUE")) {
                        return parseCascadeRestrictIf(s1.dropUnique(
                              peek('(')
                            ? unique(parseKeyColumnList())
                            : constraint(parseIdentifier())));
                    }
                    else if (parseKeywordIf("PRIMARY KEY")) {
                        Name identifier = parseIdentifierIf();
                        return parseCascadeRestrictIf(identifier == null ? s1.dropPrimaryKey() : s1.dropPrimaryKey(identifier));
                    }
                    else if (parseKeywordIf("FOREIGN KEY")) {
                        return s1.dropForeignKey(parseIdentifier());
                    }
                    else if (parseKeywordIf("INDEX")
                          || parseKeywordIf("KEY")) {
                        return dsl.dropIndex(parseIdentifier()).on(tableName);
                    }
                    else {
                        parseKeywordIf("COLUMN");
                        boolean ifColumnExists = parseKeywordIf("IF EXISTS");
                        boolean parens = parseIf('(');
                        Field<?> field = parseFieldName();
                        List<Field<?>> fields = null;

                        if (!ifColumnExists) {
                            while (parseIf(',') || parseKeywordIf("DROP") && (parseKeywordIf("COLUMN") || true)) {
                                if (fields == null) {
                                    fields = new ArrayList<>();
                                    fields.add(field);
                                }

                                fields.add(parseFieldName());
                            }
                        }

                        if (parens)
                            parse(')');

                        return parseCascadeRestrictIf(fields == null
                            ? ifColumnExists
                                ? s1.dropColumnIfExists(field)
                                : s1.dropColumn(field)
                            : s1.dropColumns(fields)
                        );
                    }
                }

                break;

            case 'M':
                if (parseKeywordIf("MODIFY"))
                    if (parseKeywordIf("CONSTRAINT"))
                        return parseAlterTableAlterConstraint(s1);
                    else if ((parseKeywordIf("COLUMN") || true))
                        return parseAlterTableAlterColumn(s1);

                break;

            case 'O':
                if (parseKeywordIf("OWNER TO") && parseUser() != null)
                    return IGNORE;

                break;

            case 'R':
                if (parseKeywordIf("RENAME")) {
                    if (parseKeywordIf("AS") || parseKeywordIf("TO")) {
                        Table<?> newName = parseTableName();

                        return s1.renameTo(newName);
                    }
                    else if (parseKeywordIf("COLUMN")) {
                        Name oldName = parseIdentifier();
                        parseKeyword("AS", "TO");
                        Name newName = parseIdentifier();

                        return s1.renameColumn(oldName).to(newName);
                    }
                    else if (parseKeywordIf("INDEX")) {
                        Name oldName = parseIdentifier();
                        parseKeyword("AS", "TO");
                        Name newName = parseIdentifier();

                        return s1.renameIndex(oldName).to(newName);
                    }
                    else if (parseKeywordIf("CONSTRAINT")) {
                        Name oldName = parseIdentifier();
                        parseKeyword("AS", "TO");
                        Name newName = parseIdentifier();

                        return s1.renameConstraint(oldName).to(newName);
                    }
                }

                break;

            case 'S':
                if (parseKeywordIf("SET"))
                    return s1.comment(parseOptionsDescription());

                break;
        }

        throw expected("ADD", "ALTER", "COMMENT", "DROP", "MODIFY", "OWNER TO", "RENAME", "SET");
    }

    private final DDLQuery parseCascadeRestrictIf(AlterTableDropStep step) {
        boolean cascade = parseKeywordIf("CASCADE");
        boolean restrict = !cascade && parseKeywordIf("RESTRICT");

        return cascade
            ? step.cascade()
            : restrict
            ? step.restrict()
            : step;
    }

    private final DDLQuery parseAlterTableAdd(AlterTableStep s1, Table<?> tableName) {
        List<FieldOrConstraint> list = new ArrayList<>();

        if (parseIndexOrKeyIf()) {
            Name name = parseIdentifierIf();

            return name == null
                ? dsl.createIndex().on(tableName, parseParenthesisedSortSpecification())
                : dsl.createIndex(name).on(tableName, parseParenthesisedSortSpecification());
        }

        if (parseIf('(')) {
            do
                parseAlterTableAddFieldsOrConstraints(list);
            while (parseIf(','));

            parse(')');
        }
        else if (parseKeywordIf("COLUMN IF NOT EXISTS")
              || parseKeywordIf("IF NOT EXISTS")) {
            return parseAlterTableAddFieldFirstBeforeLast(s1.addColumnIfNotExists(parseAlterTableAddField(null)));
        }
        else {
            do
                parseAlterTableAddFieldsOrConstraints(list);
            while (
                parseKeywordIf("ADD") ||
                parseIf(',') && (parseKeywordIf("ADD") || !peekKeyword("ALTER", "COMMENT", "DROP", "MODIFY", "OWNER TO", "RENAME"))
            );
        }

        if (list.size() == 1)
            if (list.get(0) instanceof Constraint)
                return s1.add((Constraint) list.get(0));
            else
                return parseAlterTableAddFieldFirstBeforeLast(s1.add((Field<?>) list.get(0)));
        else
            return parseAlterTableAddFieldFirstBeforeLast(s1.add(list));
    }

    private final DDLQuery parseAlterTableAddFieldFirstBeforeLast(AlterTableAddStep step) {
        if (parseKeywordIf("FIRST"))
            return step.first();
        else if (parseKeywordIf("BEFORE"))
            return step.before(parseFieldName());
        else if (parseKeywordIf("AFTER"))
            return step.after(parseFieldName());
        else
            return step;
    }

    private final boolean parseIndexOrKeyIf() {
        return ((parseKeywordIf("SPATIAL INDEX")
            || parseKeywordIf("SPATIAL KEY")
            || parseKeywordIf("FULLTEXT INDEX")
            || parseKeywordIf("FULLTEXT KEY"))
            && requireUnsupportedSyntax())

            || parseKeywordIf("INDEX")
            || parseKeywordIf("KEY");
    }

    private final void parseAlterTableAddFieldsOrConstraints(List<FieldOrConstraint> list) {
        ConstraintTypeStep constraint = parseConstraintNameSpecification();

        if (parsePrimaryKeyClusteredNonClusteredKeywordIf())
            list.add(parsePrimaryKeySpecification(constraint));
        else if (parseKeywordIf("UNIQUE") && (parseKeywordIf("KEY") || parseKeywordIf("INDEX") || true))
            list.add(parseUniqueSpecification(constraint));
        else if (parseKeywordIf("FOREIGN KEY"))
            list.add(parseForeignKeySpecification(constraint));
        else if (parseKeywordIf("CHECK"))
            list.add(parseCheckSpecification(constraint));
        else if (constraint != null)
            throw expected("CHECK", "FOREIGN KEY", "PRIMARY KEY", "UNIQUE");
        else if (parseKeywordIf("COLUMN") || true)
            parseAlterTableAddField(list);
    }

    private final ConstraintTypeStep parseConstraintNameSpecification() {
        if (parseKeywordIf("CONSTRAINT") && !peekKeyword("PRIMARY KEY", "UNIQUE", "FOREIGN KEY", "CHECK"))
            return constraint(parseIdentifier());

        return null;
    }

    private final Field<?> parseAlterTableAddField(List<FieldOrConstraint> list) {

        // The below code is taken from CREATE TABLE, with minor modifications as
        // https://github.com/jOOQ/jOOQ/issues/5317 has not yet been implemented
        // Once implemented, we might be able to factor out the common logic into
        // a new parseXXX() method.

        Name fieldName = parseIdentifier();
        DataType type = parseDataType();
        int p = list == null ? -1 : list.size();

        ParseInlineConstraints inline = parseInlineConstraints(fieldName, type, list, false, false);
        Field<?> result = field(fieldName, inline.type, inline.fieldComment);

        if (list != null)
            list.add(p, result);

        return result;
    }

    private final DDLQuery parseAlterTableAlterColumn(AlterTableStep s1) {
        boolean paren = parseIf('(');
        TableField<?, ?> field = parseFieldName();

        if (!paren)
            if (parseKeywordIf("CONSTRAINT") && parseIdentifier() != null)
                if (parseKeywordIf("NULL"))
                    return s1.alter(field).dropNotNull();
                else if (parseNotNullOptionalEnable())
                    return s1.alter(field).setNotNull();
                else
                    throw expected("NOT NULL", "NULL");
            else if (parseKeywordIf("DROP NOT NULL") || parseKeywordIf("SET NULL") || parseKeywordIf("NULL"))
                return s1.alter(field).dropNotNull();
            else if (parseKeywordIf("DROP DEFAULT"))
                return s1.alter(field).dropDefault();
            else if (parseKeywordIf("SET NOT NULL") || parseNotNullOptionalEnable())
                return s1.alter(field).setNotNull();
            else if (parseKeywordIf("SET DEFAULT"))
                return s1.alter(field).default_((Field) toField(parseConcat()));
            else if (parseKeywordIf("TO") || parseKeywordIf("RENAME TO") || parseKeywordIf("RENAME AS"))
                return s1.renameColumn(field).to(parseFieldName());
            else if (parseKeywordIf("TYPE") || parseKeywordIf("SET DATA TYPE"))
                ;

        DataType<?> type = parseDataType();

        if (parseKeywordIf("NULL"))
            type = type.nullable(true);
        else if (parseNotNullOptionalEnable())
            type = type.nullable(false);

        if (paren)
            parse(')');

        return s1.alter(field).set(type);
    }

    private final boolean parseNotNullOptionalEnable() {
        return parseKeywordIf("NOT NULL") && (parseKeywordIf("ENABLE") || true);
    }

    private final DDLQuery parseAlterTableAlterConstraint(AlterTableStep s1) {
        requireProEdition();










        throw expected("ENABLE", "ENFORCED", "DISABLE", "NOT ENFORCED");
    }

    private final DDLQuery parseAlterType() {
        AlterTypeStep s1 = dsl.alterType(parseName());


        if (parseKeywordIf("ADD VALUE"))
            return s1.addValue(parseStringLiteral());
        else if (parseKeywordIf("OWNER TO") && parseUser() != null)
            return IGNORE;
        else if (parseKeywordIf("RENAME TO"))
            return s1.renameTo(parseIdentifier());
        else if (parseKeywordIf("RENAME VALUE"))
            return s1.renameValue(parseStringLiteral()).to(parseKeyword("TO") ? parseStringLiteral() : null);
        else if (parseKeywordIf("SET SCHEMA"))
            return s1.setSchema(parseIdentifier());

        throw expected("ADD VALUE", "OWNER TO", "RENAME TO", "RENAME VALUE", "SET SCHEMA");
    }

    private final DDLQuery parseRename() {
        parseKeyword("RENAME");

        switch (characterUpper()) {
            case 'C':
                if (parseKeywordIf("COLUMN")) {
                    TableField<?, ?> oldName = parseFieldName();
                    parseKeyword("AS", "TO");
                    return dsl.alterTable(oldName.getTable()).renameColumn(oldName).to(parseFieldName());
                }

                break;

            case 'D':
                if (parseKeywordIf("DATABASE")) {
                    Catalog oldName = parseCatalogName();
                    parseKeyword("AS", "TO");
                    return dsl.alterDatabase(oldName).renameTo(parseCatalogName());
                }

                break;

            case 'I':
                if (parseKeywordIf("INDEX")) {
                    Name oldName = parseIndexName();
                    parseKeyword("AS", "TO");
                    return dsl.alterIndex(oldName).renameTo(parseIndexName());
                }

                break;

            case 'S':
                if (parseKeywordIf("SCHEMA")) {
                    Schema oldName = parseSchemaName();
                    parseKeyword("AS", "TO");
                    return dsl.alterSchema(oldName).renameTo(parseSchemaName());
                }
                else if (parseKeywordIf("SEQUENCE")) {
                    Sequence<?> oldName = parseSequenceName();
                    parseKeyword("AS", "TO");
                    return dsl.alterSequence(oldName).renameTo(parseSequenceName());
                }

                break;

            case 'V':
                if (parseKeywordIf("VIEW")) {
                    Table<?> oldName = parseTableName();
                    parseKeyword("AS", "TO");
                    return dsl.alterView(oldName).renameTo(parseTableName());
                }

                break;
        }

        // If all of the above fails, we can assume we're renaming a table.
        parseKeywordIf("TABLE");
        Table<?> oldName = parseTableName();
        parseKeyword("AS", "TO");
        return dsl.alterTable(oldName).renameTo(parseTableName());
    }

    private final DDLQuery parseDropTable(boolean temporary) {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Table<?> tableName = parseTableName();
        ifExists = ifExists || parseKeywordIf("IF EXISTS");
        boolean cascade = parseKeywordIf("CASCADE");
        boolean restrict = !cascade && parseKeywordIf("RESTRICT");

        DropTableStep s1;

        s1 = ifExists
           ? dsl.dropTableIfExists(tableName)
           : temporary
           ? dsl.dropTemporaryTable(tableName)
           : dsl.dropTable(tableName);

        return cascade
           ? s1.cascade()
           : restrict
           ? s1.restrict()
           : s1;
    }
















































































































































































































































































































































































    private final DDLQuery parseDropType() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        List<Name> typeNames = parseIdentifiers();
        ifExists = ifExists || parseKeywordIf("IF EXISTS");
        boolean cascade = parseKeywordIf("CASCADE");
        boolean restrict = !cascade && parseKeywordIf("RESTRICT");

        DropTypeStep s1;

        s1 = ifExists
           ? dsl.dropTypeIfExists(typeNames)
           : dsl.dropType(typeNames);

        return cascade
           ? s1.cascade()
           : restrict
           ? s1.restrict()
           : s1;
    }

    private final DDLQuery parseCreateDomain() {
        boolean ifNotExists = parseKeywordIf("IF NOT EXISTS");
        Domain<?> domainName = parseDomainName();
        parseKeyword("AS");
        DataType<?> dataType = parseDataType();

        CreateDomainDefaultStep<?> s1 = ifNotExists
           ? dsl.createDomainIfNotExists(domainName).as(dataType)
           : dsl.createDomain(domainName).as(dataType);

        CreateDomainConstraintStep s2 = parseKeywordIf("DEFAULT")
           ? s1.default_((Field) parseField())
           : s1;

        List<Constraint> constraints = new ArrayList<>();

        constraintLoop:
        for (;;) {
            ConstraintTypeStep constraint = parseConstraintNameSpecification();

            // TODO: NOT NULL constraints
            if (parseKeywordIf("CHECK")) {
                constraints.add(parseCheckSpecification(constraint));
                continue constraintLoop;
            }
            else if (constraint != null)
                throw expected("CHECK", "CONSTRAINT");

            break;
        }

        if (!constraints.isEmpty())
            s2 = s2.constraints(constraints);

        return s2;
    }

    private final DDLQuery parseAlterDomain() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Domain<?> domainName = parseDomainName();

        AlterDomainStep s1 = ifExists
            ? dsl.alterDomainIfExists(domainName)
            : dsl.alterDomain(domainName);

        if (parseKeywordIf("ADD")) {
            ConstraintTypeStep constraint = parseConstraintNameSpecification();

            // TODO: NOT NULL constraints
            if (parseKeywordIf("CHECK"))
                return s1.add(parseCheckSpecification(constraint));
            else
                throw expected("CHECK", "CONSTRAINT");
        }
        else if (parseKeywordIf("DROP CONSTRAINT")) {
            boolean ifConstraintExists = parseKeywordIf("IF EXISTS");
            Constraint constraint = constraint(parseIdentifier());

            AlterDomainDropConstraintCascadeStep s2 = ifConstraintExists
                ? s1.dropConstraintIfExists(constraint)
                : s1.dropConstraint(constraint);

            return parseKeywordIf("CASCADE")
                ? s2.cascade()
                : parseKeywordIf("RESTRICT")
                ? s2.restrict()
                : s2;
        }
        else if (parseKeywordIf("RENAME")) {
            if (parseKeywordIf("TO") || parseKeywordIf("AS")) {
                return s1.renameTo(parseDomainName());
            }
            else if (parseKeywordIf("CONSTRAINT")) {
                boolean ifConstraintExists = parseKeywordIf("IF EXISTS");
                Constraint oldName = constraint(parseIdentifier());

                AlterDomainRenameConstraintStep s2 = ifConstraintExists
                    ? s1.renameConstraintIfExists(oldName)
                    : s1.renameConstraint(oldName);

                parseKeyword("AS", "TO");
                return s2.to(constraint(parseIdentifier()));
            }
            else
                throw expected("CONSTRAINT", "TO", "AS");
        }
        else if (parseKeywordIf("SET DEFAULT"))
            return s1.setDefault(parseField());
        else if (parseKeywordIf("DROP DEFAULT"))
            return s1.dropDefault();
        else if (parseKeywordIf("SET NOT NULL"))
            return s1.setNotNull();
        else if (parseKeywordIf("DROP NOT NULL"))
            return s1.dropNotNull();
        else if (parseKeywordIf("OWNER TO")) {
            parseUser();
            return IGNORE;
        }
        else
            throw expected("ADD", "DROP", "RENAME", "SET", "OWNER TO");
    }

    private final DDLQuery parseDropDomain() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Domain<?> domainName = parseDomainName();
        ifExists = ifExists || parseKeywordIf("IF EXISTS");
        boolean cascade = parseKeywordIf("CASCADE");
        boolean restrict = !cascade && parseKeywordIf("RESTRICT");

        DropDomainCascadeStep s1 = ifExists
            ? dsl.dropDomainIfExists(domainName)
            : dsl.dropDomain(domainName);

        return cascade
            ? s1.cascade()
            : restrict
            ? s1.restrict()
            : s1;
    }

    private final DDLQuery parseCreateDatabase() {
        boolean ifNotExists = parseKeywordIf("IF NOT EXISTS");
        Catalog catalogName = parseCatalogName();

        return ifNotExists
            ? dsl.createDatabaseIfNotExists(catalogName)
            : dsl.createDatabase(catalogName);
    }

    private final DDLQuery parseAlterDatabase() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Catalog catalogName = parseCatalogName();

        AlterDatabaseStep s1 = ifExists
            ? dsl.alterDatabaseIfExists(catalogName)
            : dsl.alterDatabase(catalogName);

        if (parseKeywordIf("RENAME")) {
            parseKeyword("AS", "TO");
            return s1.renameTo(parseCatalogName());
        }
        else if (parseKeywordIf("OWNER TO") && parseUser() != null)
            return IGNORE;
        else if (parseAlterDatabaseFlags(true))
            return IGNORE;
        else
            throw expected("OWNER TO", "RENAME TO");
    }

    private final boolean parseAlterDatabaseFlags(boolean throwOnFail) {
        parseKeywordIf("DEFAULT");

        if (parseCharacterSetSpecificationIf() != null)
            return true;

        if (parseCollateSpecificationIf() != null)
            return true;

        if (parseKeywordIf("ENCRYPTION")) {
            parseIf('=');
            parseStringLiteral();
            return true;
        }

        if (throwOnFail)
            throw expected("CHARACTER SET", "COLLATE", "DEFAULT ENCRYPTION");
        else
            return false;
    }

    private final DDLQuery parseDropDatabase() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Catalog catalogName = parseCatalogName();
        ifExists = ifExists || parseKeywordIf("IF EXISTS");

        return ifExists
            ? dsl.dropDatabaseIfExists(catalogName)
            : dsl.dropDatabase(catalogName);
    }

    private final DDLQuery parseCreateSchema() {
        boolean ifNotExists = parseKeywordIf("IF NOT EXISTS");
        boolean authorization = parseKeywordIf("AUTHORIZATION");
        Schema schemaName = parseSchemaName();

        if (!authorization && parseKeywordIf("AUTHORIZATION"))
            parseUser();

        return ifNotExists
            ? dsl.createSchemaIfNotExists(schemaName)
            : dsl.createSchema(schemaName);
    }

    private final DDLQuery parseAlterSchema() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Schema schemaName = parseSchemaName();
        AlterSchemaStep s1 = ifExists
            ? dsl.alterSchemaIfExists(schemaName)
            : dsl.alterSchema(schemaName);

        if (parseKeywordIf("RENAME")) {
            parseKeyword("AS", "TO");
            return s1.renameTo(parseSchemaName());
        }
        else if (parseKeywordIf("OWNER TO") && parseUser() != null)
            return IGNORE;
        else if (parseAlterDatabaseFlags(false))
            return IGNORE;
        else
            throw expected("OWNER TO", "RENAME TO");
    }

    private final DDLQuery parseDropSchema() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Schema schemaName = parseSchemaName();
        ifExists = ifExists || parseKeywordIf("IF EXISTS");
        boolean cascade = parseKeywordIf("CASCADE");
        boolean restrict = !cascade && parseKeywordIf("RESTRICT");

        DropSchemaStep s1 = ifExists
            ? dsl.dropSchemaIfExists(schemaName)
            : dsl.dropSchema(schemaName);

        return cascade
            ? s1.cascade()
            : restrict
            ? s1.restrict()
            : s1;
    }

    private final DDLQuery parseCreateIndex(boolean unique) {
        boolean ifNotExists = parseKeywordIf("IF NOT EXISTS");
        Name indexName = parseIndexNameIf();
        parseUsingIndexTypeIf();
        SortField<?>[] fields = null;
        if (peek('('))
            fields = parseParenthesisedSortSpecification();
        parseKeyword("ON");
        Table<?> tableName = parseTableName();
        parseUsingIndexTypeIf();
        if (fields == null)
            fields = parseParenthesisedSortSpecification();
        parseUsingIndexTypeIf();

        Name[] include = null;
        if (parseKeywordIf("INCLUDE") || parseKeywordIf("COVERING") || parseKeywordIf("STORING")) {
            parse('(');
            include = parseIdentifiers().toArray(EMPTY_NAME);
            parse(')');
        }

        Condition condition = parseKeywordIf("WHERE")
            ? parseCondition()
            : null;

        boolean excludeNullKeys = condition == null && parseKeywordIf("EXCLUDE NULL KEYS");

        CreateIndexStep s1 = ifNotExists
            ? unique
                ? dsl.createUniqueIndexIfNotExists(indexName)
                : dsl.createIndexIfNotExists(indexName)
            : unique
                ? indexName == null
                    ? dsl.createUniqueIndex()
                    : dsl.createUniqueIndex(indexName)
                : indexName == null
                    ? dsl.createIndex()
                    : dsl.createIndex(indexName);

        CreateIndexIncludeStep s2 = s1.on(tableName, fields);
        CreateIndexWhereStep s3 = include != null
            ? s2.include(include)
            : s2;

        return condition != null
            ? s3.where(condition)
            : excludeNullKeys
            ? s3.excludeNullKeys()
            : s3;
    }

    private SortField<?>[] parseParenthesisedSortSpecification() {
        parse('(');
        SortField<?>[] fields = parseList(',', ParseContext::parseSortField).toArray(EMPTY_SORTFIELD);
        parse(')');

        return fields;
    }

    private final boolean parseUsingIndexTypeIf() {
        if (parseKeywordIf("USING"))
            parseIdentifier();

        return true;
    }

    private final DDLQuery parseAlterIndex() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Name indexName = parseIndexName();
        parseKeyword("RENAME");
        parseKeyword("AS", "TO");
        Name newName = parseIndexName();

        AlterIndexStep s1 = ifExists
            ? dsl.alterIndexIfExists(indexName)
            : dsl.alterIndex(indexName);
        return s1.renameTo(newName);
    }

    private final DDLQuery parseDropIndex() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Name indexName = parseIndexName();
        ifExists = ifExists || parseKeywordIf("IF EXISTS");
        boolean on = parseKeywordIf("ON");
        Table<?> onTable = on ? parseTableName() : null;

        DropIndexOnStep s1;
        DropIndexCascadeStep s2;

        s1 = ifExists
            ? dsl.dropIndexIfExists(indexName)
            : dsl.dropIndex(indexName);

        s2 = on
            ? s1.on(onTable)
            : s1;

        return parseKeywordIf("CASCADE")
            ? s2.cascade()
            : parseKeywordIf("RESTRICT")
            ? s2.restrict()
            : s2;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // QueryPart parsing
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public final Condition parseCondition() {
        return toCondition(parseOr());
    }

    private final QueryPart parseOr() {
        QueryPart condition = parseAnd();

        while (parseKeywordIf("OR"))
            condition = toCondition(condition).or(toCondition(parseAnd()));

        return condition;
    }

    private final QueryPart parseAnd() {
        QueryPart condition = parseNot();

        while (!forbidden.contains(FK_AND) && parseKeywordIf("AND"))
            condition = toCondition(condition).and(toCondition(parseNot()));

        return condition;
    }

    private final QueryPart parseNot() {
        int not = parseNot0();
        QueryPart condition = parsePredicate();

        for (int i = 0; i < not; i++)
            condition = toCondition(condition).not();

        return condition;
    }

    private final int parseNot0() {
        int not = 0;

        while (parseKeywordIf("NOT"))
            not++;

        return not;
    }

    private final QueryPart parsePredicate() {






        if (parseKeywordIf("EXISTS")) {
            parse('(');
            Select<?> select = parseWithOrSelect();
            parse(')');

            return exists(select);
        }
        else if (parseKeywordIf("REGEXP_LIKE")) {
            parse('(');
            Field<?> f1 = parseField();
            parse(',');
            Field<?> f2 = parseField();
            parse(')');

            return f1.likeRegex((Field) f2);
        }
        else if (parseKeywordIf("UNIQUE")) {
            parse('(');
            Select<?> select = parseWithOrSelect();
            parse(')');

            return unique(select);
        }
        else if (parseKeywordIf("JSON_EXISTS")) {
            parse('(');
            Field json = parseField();
            parse(',');
            Field<String> path = (Field<String>) parseField();
            JSONExists.Behaviour b = parseJSONExistsOnErrorBehaviourIf();
            parse(')');












            return jsonExists(json, path);
        }
        else if (parseKeywordIf("XMLEXISTS")) {
            parse('(');
            Field<String> xpath = (Field<String>) parseField();
            XMLPassingMechanism m = parseXMLPassingMechanism();
            Field<XML> xml = (Field<XML>) parseField();
            parse(')');

            if (m == BY_REF)
                return xmlexists(xpath).passingByRef(xml);
            else if (m == BY_VALUE)
                return xmlexists(xpath).passingByValue(xml);
            else
                return xmlexists(xpath).passing(xml);
        }









        else {
            FieldOrRow left;
            Comparator comp;
            TSQLOuterJoinComparator outer;
            boolean not;
            boolean notOp = false;

            left = parseConcat();
            int p = position();
            not = parseKeywordIf("NOT");

            if (!not && ((outer = parseTSQLOuterJoinComparatorIf()) != null) && requireProEdition()) {
                Condition result = null;












                return result;
            }
            else if (!not && (comp = parseComparatorIf()) != null) {
                boolean all = parseKeywordIf("ALL");
                boolean any = !all && (parseKeywordIf("ANY") || parseKeywordIf("SOME"));
                if (all || any)
                    parse('(');

                // TODO equal degrees
                Condition result =
                      all
                    ? left instanceof Field
                        ? peekSelectOrWith(true)
                            ? ((Field) left).compare(comp, DSL.all(parseWithOrSelect(1)))
                            : ((Field) left).compare(comp, DSL.all(parseList(',', ParseContext::parseField).toArray(EMPTY_FIELD)))

                        // TODO: Support quantifiers also for rows
                        : new RowSubqueryCondition((Row) left, DSL.all(parseWithOrSelect(((Row) left).size())), comp)

                    : any
                    ? left instanceof Field
                        ? peekSelectOrWith(true)
                            ? ((Field) left).compare(comp, DSL.any(parseWithOrSelect(1)))
                            : ((Field) left).compare(comp, DSL.any(parseList(',', ParseContext::parseField).toArray(EMPTY_FIELD)))

                        // TODO: Support quantifiers also for rows
                        : new RowSubqueryCondition((Row) left, DSL.any(parseWithOrSelect(((Row) left).size())), comp)

                    : left instanceof Field
                        ? ((Field) left).compare(comp, toField(parseConcat()))
                        : new RowCondition((Row) left, parseRow(((Row) left).size(), true), comp);

                if (all || any)
                    parse(')');

                return result;
            }
            else if (!not && parseKeywordIf("IS")) {
                not = parseKeywordIf("NOT");

                if (parseKeywordIf("NULL"))
                    return not
                        ? left instanceof Field
                            ? ((Field) left).isNotNull()
                            : ((Row) left).isNotNull()
                        : left instanceof Field
                            ? ((Field) left).isNull()
                            : ((Row) left).isNull();
                else if (left instanceof Field && parseKeywordIf("JSON"))
                    return not
                        ? ((Field) left).isNotJson()
                        : ((Field) left).isJson();
                else if (left instanceof Field && parseKeywordIf("DOCUMENT"))
                    return not
                        ? ((Field) left).isNotDocument()
                        : ((Field) left).isDocument();

                not = parseKeywordIf("DISTINCT FROM") == not;
                if (left instanceof Field) {
                    Field right = toField(parseConcat());
                    return not ? ((Field) left).isNotDistinctFrom(right) : ((Field) left).isDistinctFrom(right);
                }
                else {
                    Row right = parseRow(((Row) left).size(), true);
                    return new RowIsDistinctFrom((Row) left, right, not);
                }
            }
            else if (!not && parseIf("@>")) {
                return toField(left).contains((Field) toField(parseConcat()));
            }
            else if (!forbidden.contains(FK_IN) && parseKeywordIf("IN")) {
                Condition result;

                // [#12691] Some dialects support A IN B syntax without parentheses for single element in lists
                if (left instanceof Field && !peek('(')) {
                    result = not
                        ? ((Field) left).notIn(parseField())
                        : ((Field) left).in(parseField());
                }
                else {
                    parse('(');

                    if (peek(')'))
                        result = not
                            ? left instanceof Field
                                ? ((Field) left).notIn(EMPTY_FIELD)
                                : new RowInCondition((Row) left, new QueryPartList<>(), true)
                            : left instanceof Field
                                ? ((Field) left).in(EMPTY_FIELD)
                                : new RowInCondition((Row) left, new QueryPartList<>(), false);
                    else if (peekSelectOrWith(true))
                        result = not
                            ? left instanceof Field
                                ? ((Field) left).notIn(parseWithOrSelect(1))
                                : new RowSubqueryCondition((Row) left, parseWithOrSelect(((Row) left).size()), NOT_IN)
                            : left instanceof Field
                                ? ((Field) left).in(parseWithOrSelect(1))
                                : new RowSubqueryCondition((Row) left, parseWithOrSelect(((Row) left).size()), IN);
                    else
                        result = not
                            ? left instanceof Field
                                ? ((Field) left).notIn(parseList(',', ParseContext::parseField))
                                : new RowInCondition((Row) left, new QueryPartList<>(parseList(',', c -> parseRow(((Row) left).size()))), true)
                            : left instanceof Field
                                ? ((Field) left).in(parseList(',', ParseContext::parseField))
                                : new RowInCondition((Row) left, new QueryPartList<>(parseList(',', c -> parseRow(((Row) left).size()))), false);

                    parse(')');
                }

                return result;
            }
            else if (parseKeywordIf("BETWEEN")) {
                boolean symmetric = !parseKeywordIf("ASYMMETRIC") && parseKeywordIf("SYMMETRIC");
                FieldOrRow r1 = left instanceof Field
                    ? parseConcat()
                    : parseRow(((Row) left).size());
                parseKeyword("AND");
                FieldOrRow r2 = left instanceof Field
                    ? parseConcat()
                    : parseRow(((Row) left).size());

                return symmetric
                    ? not
                        ? left instanceof Field
                            ? ((Field) left).notBetweenSymmetric((Field) r1, (Field) r2)
                            : new RowBetweenCondition((Row) left, (Row) r1, not, symmetric, (Row) r2)
                        : left instanceof Field
                            ? ((Field) left).betweenSymmetric((Field) r1, (Field) r2)
                            : new RowBetweenCondition((Row) left, (Row) r1, not, symmetric, (Row) r2)
                    : not
                        ? left instanceof Field
                            ? ((Field) left).notBetween((Field) r1, (Field) r2)
                            : new RowBetweenCondition((Row) left, (Row) r1, not, symmetric, (Row) r2)
                        : left instanceof Field
                            ? ((Field) left).between((Field) r1, (Field) r2)
                            : new RowBetweenCondition((Row) left, (Row) r1, not, symmetric, (Row) r2);
            }
            else if (left instanceof Field && (parseKeywordIf("LIKE") || parseOperatorIf("~~") || (notOp = parseOperatorIf("!~~")))) {
                if (parseKeywordIf("ANY")) {
                    parse('(');
                    if (peekSelectOrWith(true)) {
                        Select<?> select = parseWithOrSelect();
                        parse(')');
                        LikeEscapeStep result = (not ^ notOp) ? ((Field) left).notLike(any(select)) : ((Field) left).like(any(select));
                        return parseEscapeClauseIf(result);
                    }
                    else {
                        List<Field<?>> fields = null;
                        if (parseIf(')')) {
                            fields = emptyList();
                        }
                        else {
                            fields = parseList(',', c -> toField(parseConcat()));
                            parse(')');
                        }
                        Field<String>[] fieldArray = fields.toArray(new Field[0]);
                        LikeEscapeStep result = (not ^ notOp) ? ((Field<String>) left).notLike(any(fieldArray)) : ((Field<String>) left).like(any(fieldArray));
                        return parseEscapeClauseIf(result);
                    }
                }
                else if (parseKeywordIf("ALL")) {
                    parse('(');
                    if (peekSelectOrWith(true)) {
                        Select<?> select = parseWithOrSelect();
                        parse(')');
                        LikeEscapeStep result = (not ^ notOp) ? ((Field) left).notLike(all(select)) : ((Field) left).like(all(select));
                        return parseEscapeClauseIf(result);
                    }
                    else {
                        List<Field<?>> fields = null;
                        if (parseIf(')')) {
                            fields = emptyList();
                        }
                        else {
                            fields = parseList(',', c -> toField(parseConcat()));
                            parse(')');
                        }
                        Field<String>[] fieldArray = fields.toArray(new Field[0]);
                        LikeEscapeStep result = (not ^ notOp) ? ((Field<String>) left).notLike(all(fieldArray)) : ((Field<String>) left).like(all(fieldArray));
                        return parseEscapeClauseIf(result);
                    }
                }
                else {
                    Field right = toField(parseConcat());
                    LikeEscapeStep like = (not ^ notOp) ? ((Field) left).notLike(right) : ((Field) left).like(right);
                    return parseEscapeClauseIf(like);
                }
            }
            else if (left instanceof Field && (parseKeywordIf("ILIKE") || parseOperatorIf("~~*") || (notOp = parseOperatorIf("!~~*")))) {
                Field right = toField(parseConcat());
                LikeEscapeStep like = (not ^ notOp) ? ((Field) left).notLikeIgnoreCase(right) : ((Field) left).likeIgnoreCase(right);
                return parseEscapeClauseIf(like);
            }
            else if (left instanceof Field && (parseKeywordIf("REGEXP")
                                            || parseKeywordIf("RLIKE")
                                            || parseKeywordIf("LIKE_REGEX")
                                            || parseOperatorIf("~")
                                            || (notOp = parseOperatorIf("!~")))) {
                Field right = toField(parseConcat());
                return (not ^ notOp)
                        ? ((Field) left).notLikeRegex(right)
                        : ((Field) left).likeRegex(right);
            }
            else if (left instanceof Field && parseKeywordIf("SIMILAR TO")) {
                Field right = toField(parseConcat());
                LikeEscapeStep like = not ? ((Field) left).notSimilarTo(right) : ((Field) left).similarTo(right);
                return parseEscapeClauseIf(like);
            }
            else if (left instanceof Row && ((Row) left).size() == 2 && parseKeywordIf("OVERLAPS")) {
                Row leftRow = (Row) left;
                Row rightRow = parseRow(2);

                Row2 leftRow2 = row(leftRow.field(0), leftRow.field(1));
                Row2 rightRow2 = row(rightRow.field(0), rightRow.field(1));

                return leftRow2.overlaps(rightRow2);
            }
            else {
                position(p);
                return left;
            }
        }
    }

    private final QueryPart parseEscapeClauseIf(LikeEscapeStep like) {
        return parseKeywordIf("ESCAPE") ? like.escape(parseCharacterLiteral()) : like;
    }

    @Override
    public final Table<?> parseTable() {
        return parseTable(() -> peekKeyword(KEYWORDS_IN_SELECT_FROM));
    }

    private final Table<?> parseTable(BooleanSupplier forbiddenKeywords) {
        Table<?> result = parseLateral(forbiddenKeywords);

        for (;;) {
            Table<?> joined = parseJoinedTableIf(result, forbiddenKeywords);

            if (joined == null)
                return result;
            else
                result = joined;
        }
    }

    private final Table<?> parseLateral(BooleanSupplier forbiddenKeywords) {
        if (parseKeywordIf("LATERAL"))
            return lateral(parseTableFactor(forbiddenKeywords));
        else
            return parseTableFactor(forbiddenKeywords);
    }

    private final <R extends Record> Table<R> t(TableLike<R> table) {
        return t(table, false);
    }

    private final <R extends Record> Table<R> t(TableLike<R> table, boolean dummyAlias) {
        return
            table instanceof Table
          ? (Table<R>) table
          : dummyAlias
          ? table.asTable("x")
          : table.asTable();
    }

    private final Table<?> parseTableFactor(BooleanSupplier forbiddenKeywords) {

        // [#7982] Postpone turning Select into a Table in case there is an alias
        TableLike<?> result = null;






        // TODO [#5306] Support FINAL TABLE (<data change statement>)
        // TODIO ONLY ( table primary )
        if (parseFunctionNameIf("UNNEST", "TABLE")) {
            parse('(');

            if (parseFunctionNameIf("GENERATOR")) {
                parse('(');
                Field<?> tl = parseFunctionArgumentIf("TIMELIMIT");
                Field<?> rc = parseFunctionArgumentIf("ROWCOUNT");

                if (tl == null)
                    tl = parseFunctionArgumentIf("TIMELIMIT");

                parse(')');
                result = generateSeries(one(), (Field<Integer>) rc);
            }
            else {
                Field<?> f = parseField();

                // Work around a missing feature in unnest()
                if (!f.getType().isArray())
                    f = f.coerce(f.getDataType().getArrayDataType());

                result = unnest(f);
            }

            parse(')');
        }
        else if (parseFunctionNameIf("GENERATE_SERIES", "SYSTEM_RANGE")) {
            parse('(');
            Field from = toField(parseConcat());
            parse(',');
            Field to = toField(parseConcat());

            Field step = parseIf(',')
                ? toField(parseConcat())
                : null;

            parse(')');

            result = step == null
                ? generateSeries(from, to)
                : generateSeries(from, to, step);
        }
        else if (parseFunctionNameIf("JSON_TABLE")) {
            parse('(');

            Field json = parseField();
            parse(',');
            Field path = toField(parseConcat());
            JSONTableColumnsStep s1 = (JSONTableColumnsStep) jsonTable(json, path);
            parseKeyword("COLUMNS");
            parse('(');

            do {
                Name fieldName = parseIdentifier();

                if (parseKeywordIf("FOR ORDINALITY")) {
                    s1 = s1.column(fieldName).forOrdinality();
                }
                else {
                    JSONTableColumnPathStep s2 = s1.column(fieldName, parseDataType());
                    s1 = parseKeywordIf("PATH") ? s2.path(parseStringLiteral()) : s2;
                }
            }
            while (parseIf(','));

            parse(')');
            parse(')');
            result = s1;
        }
        else if (parseFunctionNameIf("XMLTABLE")) {
            parse('(');

            XMLTablePassingStep s1 = xmltable((Field) toField(parseConcat()));
            XMLPassingMechanism m = parseXMLPassingMechanismIf();
            Field<XML> passing = m == null ? null : (Field<XML>) parseField();

            XMLTableColumnsStep s2 = (XMLTableColumnsStep) (
                  m == BY_REF
                ? s1.passingByRef(passing)
                : m == BY_VALUE
                ? s1.passingByValue(passing)
                : m == XMLPassingMechanism.DEFAULT
                ? s1.passing(passing)
                : s1
            );

            parseKeyword("COLUMNS");

            do {
                Name fieldName = parseIdentifier();

                if (parseKeywordIf("FOR ORDINALITY")) {
                    s2 = s2.column(fieldName).forOrdinality();
                }
                else {
                    XMLTableColumnPathStep s3 = s2.column(fieldName, parseDataType());
                    s2 = parseKeywordIf("PATH") ? s3.path(parseStringLiteral()) : s3;
                }
            }
            while (parseIf(','));

            parse(')');
            result = s2;
        }
        else if (parseIf('(')) {

            // A table factor parenthesis can mark the beginning of any of:
            // - A derived table:                     E.g. (select 1)
            // - A derived table with nested set ops: E.g. ((select 1) union (select 2))
            // - A values derived table:              E.g. (values (1))
            // - A joined table:                      E.g. ((a join b on p) right join c on q)
            // - A combination of the above:          E.g. ((a join (select 1) on p) right join (((select 1)) union (select 2)) on q)
            if (peekKeyword("SELECT", "SEL", "WITH")) {
                SelectQueryImpl<Record> select = parseWithOrSelect();
                parse(')');
                result = parseQueryExpressionBody(null, null, select);
            }
            else if (peekKeyword("VALUES")) {
                result = parseTableValueConstructor();
                parse(')');
            }
            else {
                result = parseJoinedTable(forbiddenKeywords);
                parse(')');
            }
        }
        else {
            result = parseTableName();

            // TODO Sample clause
        }

        if (parseKeywordIf("VERSIONS BETWEEN") && requireProEdition()) {






























        }
        else if (peekKeyword("FOR")
            && !peekKeyword("FOR JSON")
            && !peekKeyword("FOR KEY SHARE")
            && !peekKeyword("FOR NO KEY UPDATE")
            && !peekKeyword("FOR SHARE")
            && !peekKeyword("FOR UPDATE")
            && !peekKeyword("FOR XML")
            && parseKeyword("FOR") && requireProEdition()) {

























        }
        else if (parseKeywordIf("AS OF") && requireProEdition()) {










        }

        if (parseKeywordIf("PIVOT") && requireProEdition()) {















































        }

        // TODO UNPIVOT
        result = parseCorrelationNameIf(result, forbiddenKeywords);

        int p = position();
        if (parseKeywordIf("WITH")) {
            if (parseIf('(') && requireProEdition()) {






            }

            // [#10164] Without parens, WITH is part of the next statement in delimiter free statement batches
            else
                position(p);
        }

        return t(result);
    }

    private final Field<?> parseFunctionArgumentIf(String parameterName) {
        if (parseKeywordIf(parameterName) && parse("=>"))
            return parseField();
        else
            return null;
    }

    private final TableLike<?> parseCorrelationNameIf(TableLike<?> result, BooleanSupplier forbiddenKeywords) {
        Name alias = null;
        List<Name> columnAliases = null;

        if (parseKeywordIf("AS"))
            alias = parseIdentifier();
        else if (!forbiddenKeywords.getAsBoolean())
            alias = parseIdentifierIf();

        if (alias != null) {
            if (parseIf('(')) {
                columnAliases = parseIdentifiers();
                parse(')');
            }

            if (columnAliases != null)
                result = t(result, true).as(alias, columnAliases.toArray(EMPTY_NAME));
            else
                result = t(result, true).as(alias);
        }

        return result;
    }

































































    private final Table<?> parseTableValueConstructor() {
        parseKeyword("VALUES");

        List<Row> rows = new ArrayList<>();
        Integer degree = null;
        do {
            parseKeywordIf("ROW");
            Row row = parseTuple(degree);
            rows.add(row);

            if (degree == null)
                degree = row.size();
        }
        while (parseIf(','));
        return values0(rows.toArray(EMPTY_ROW));
    }

    private final Table<?> parseExplicitTable() {
        parseKeyword("TABLE");
        return parseTableName();
    }

    private final Row parseTuple() {
        return parseTuple(null, false);
    }

    private final Row parseTuple(Integer degree) {
        return parseTuple(degree, false);
    }

    private final Row parseTupleIf(Integer degree) {
        return parseTupleIf(degree, false);
    }

    private final Row parseTuple(Integer degree, boolean allowDoubleParens) {
        parse('(');
        List<? extends FieldOrRow> fieldsOrRows;

        if (allowDoubleParens)
            fieldsOrRows = parseList(',', c -> parseFieldOrRow());
        else
            fieldsOrRows = parseList(',', ParseContext::parseField);

        Row row;

        if (fieldsOrRows.size() == 0)
            row = row();
        else if (fieldsOrRows.get(0) instanceof Field)
            row = row(fieldsOrRows);
        else if (fieldsOrRows.size() == 1)
            row = (Row) fieldsOrRows.get(0);
        else
            throw exception("Unsupported row size");

        if (degree != null && row.size() != degree)
            throw exception("Expected row of degree: " + degree + ". Got: " + row.size());

        parse(')');
        return row;
    }

    private final Row parseTupleIf(Integer degree, boolean allowDoubleParens) {
        if (peek('('))
            return parseTuple(degree, allowDoubleParens);

        return null;
    }

    private final Table<?> parseJoinedTable(BooleanSupplier forbiddenKeywords) {
        Table<?> result = parseLateral(forbiddenKeywords);

        for (;;) {
            Table<?> joined = parseJoinedTableIf(result, forbiddenKeywords);

            if (joined == null)
                return result;
            else
                result = joined;
        }
    }

    private final Table<?> parseJoinedTableIf(Table<?> left, BooleanSupplier forbiddenKeywords) {
        JoinType joinType = parseJoinTypeIf();

        if (joinType == null)
            return null;

        Table<?> right = joinType.qualified() ? parseTable(forbiddenKeywords) : parseLateral(forbiddenKeywords);

        TableOptionalOnStep<?> s0;
        TablePartitionByStep<?> s1;
        TableOnStep<?> s2;
        s2 = s1 = (TablePartitionByStep<?>) (s0 = left.join(right, joinType));

        switch (joinType) {
            case LEFT_OUTER_JOIN:
            case FULL_OUTER_JOIN:
            case RIGHT_OUTER_JOIN:
                if (parseKeywordIf("PARTITION BY")) {
                    requireProEdition();






                }

                // No break

            case JOIN:
            case STRAIGHT_JOIN:
            case LEFT_SEMI_JOIN:
            case LEFT_ANTI_JOIN:
                if (parseKeywordIf("ON"))
                    return s2.on(parseCondition());
                else if (parseKeywordIf("USING"))
                    return parseJoinUsing(s2);

                // [#9476] MySQL treats INNER JOIN and CROSS JOIN as the same
                else if (joinType == JOIN)
                    return s0;
                else
                    throw expected("ON", "USING");

            case CROSS_JOIN:

                // [#9476] MySQL treats INNER JOIN and CROSS JOIN as the same
                if (parseKeywordIf("ON"))
                    return left.join(right).on(parseCondition());
                else if (parseKeywordIf("USING"))
                    return parseJoinUsing(left.join(right));

                // No break

            default:
                return s0;
        }
    }

    private final Table<?> parseJoinUsing(TableOnStep<?> join) {
        Table<?> result;

        parse('(');

        if (parseIf(')')) {
            result = join.using();
        }
        else {
            result = join.using(Tools.fieldsByName(parseIdentifiers().toArray(EMPTY_NAME)));
            parse(')');
        }

        return result;
    }

    private final List<SelectFieldOrAsterisk> parseSelectList() {
        List<SelectFieldOrAsterisk> result = new ArrayList<>();

        do {
            QualifiedAsterisk qa;

            if (parseIf('*')) {
                if (parseKeywordIf("EXCEPT")) {
                    parse('(');
                    result.add(DSL.asterisk().except(parseList(',', c -> parseFieldName()).toArray(EMPTY_FIELD)));
                    parse(')');
                }
                else
                    result.add(DSL.asterisk());
            }
            else if ((qa = parseQualifiedAsteriskIf()) != null) {
                if (parseKeywordIf("EXCEPT")) {
                    parse('(');
                    result.add(qa.except(parseList(',', c -> parseFieldName()).toArray(EMPTY_FIELD)));
                    parse(')');
                }
                else
                    result.add(qa);
            }
            else {
                Name alias = null;
                SelectField<?> field = null;














                if (field == null) {
                    field = parseSelectField();

                    if (parseKeywordIf("AS"))
                        alias = parseIdentifier(true);
                    else if (!peekKeyword(KEYWORDS_IN_SELECT) && !peekKeyword(KEYWORDS_IN_STATEMENTS))
                        alias = parseIdentifierIf(true);
                }

                result.add(alias == null ? field : field.as(alias));
            }
        }
        while (parseIf(','));

        return result;
    }

    @Override
    public final SortField<?> parseSortField() {
        Field<?> field = parseField();
        SortField<?> sort;

        if (parseKeywordIf("DESC"))
            sort = field.desc();
        else if (parseKeywordIf("ASC"))
            sort = field.asc();
        else
            sort = field.sortDefault();

        if (parseKeywordIf("NULLS FIRST"))
            sort = sort.nullsFirst();
        else if (parseKeywordIf("NULLS LAST"))
            sort = sort.nullsLast();

        return sort;
    }

    private final List<Field<?>> parseFieldsOrEmptyParenthesised() {
        parse('(');

        if (parseIf(')')) {
            return emptyList();
        }
        else {
            List<Field<?>> result = parseList(',', ParseContext::parseField);
            parse(')');
            return result;
        }
    }

    private final SelectField<?> parseSelectField() {
        return (SelectField<?>) parseFieldOrRow();
    }

    private final Row parseRow() {
        return parseRow(null);
    }

    private final Row parseRowIf() {
        return parseRowIf(null);
    }

    private final Row parseRow(Integer degree) {
        parseFunctionNameIf("ROW");
        return parseTuple(degree);
    }

    private final Row parseRowIf(Integer degree) {
        parseFunctionNameIf("ROW");
        return parseTupleIf(degree);
    }

    private final Row parseRow(Integer degree, boolean allowDoubleParens) {
        parseFunctionNameIf("ROW");
        return parseTuple(degree, allowDoubleParens);
    }

    public final FieldOrRow parseFieldOrRow() {
        return toFieldOrRow(parseOr());
    }

    @Override
    public final Field<?> parseField() {
        return toField(parseOr());
    }

    private final String parseHints() {
        StringBuilder sb = new StringBuilder();

        do {
            int p = position();
            if (parseIf('/', false)) {
                parse('*', false);

                int i = position();

                loop:
                while (i < sql.length) {
                    switch (sql[i]) {
                        case '*':
                            if (i + 1 < sql.length && sql[i + 1] == '/')
                                break loop;
                    }

                    i++;
                }

                position(i + 2);

                if (sb.length() > 0)
                    sb.append(' ');

                sb.append(substring(p, position()));
            }
        }
        while (parseWhitespaceIf());

        ignoreHints(true);
        return sb.length() > 0 ? sb.toString() : null;
    }

    private final Condition toCondition(QueryPart part) {
        if (part == null) {
            return null;
        }
        else if (part instanceof Condition) {
            return (Condition) part;
        }
        else if (part instanceof Field) {
            DataType dataType = ((Field) part).getDataType();
            Class<?> type = dataType.getType();

            if (type == Boolean.class)
                return condition((Field) part);

            // [#11631] [#12394] Numeric expressions are booleans in MySQL
            else if (dataType.isNumeric())
                return ((Field) part).ne(zero());

            // [#7266] Support parsing column references as predicates
            else if (type == Object.class && (part instanceof TableFieldImpl || part instanceof Val))
                return condition((Field) part);
            else
                throw expected("Boolean field");
        }
        else
            throw expected("Condition");
    }

    private final FieldOrRow toFieldOrRow(QueryPart part) {
        if (part == null)
            return null;
        else if (part instanceof Field)
            return (Field) part;
        else if (part instanceof Condition)
            return field((Condition) part);
        else if (part instanceof Row)
            return (Row) part;
        else
            throw expected("Field or row");
    }

    private final Field<?> toField(QueryPart part) {
        if (part == null)
            return null;
        else if (part instanceof Field)
            return (Field) part;
        else if (part instanceof Condition)
            return field((Condition) part);
        else
            throw expected("Field");
    }

    private final FieldOrRow parseConcat() {
        FieldOrRow r = parseCollated();

        if (r instanceof Field)
            while (parseIf("||"))
                r = concat((Field) r, toField(parseCollated()));

        return r;
    }

    private final FieldOrRow parseCollated() {
        FieldOrRow r = parseNumericOp();

        if (r instanceof Field) {
            if (parseKeywordIf("COLLATE"))
                r = ((Field) r).collate(parseCollation());























        }

        return r;
    }

    private final Field<?> parseFieldNumericOpParenthesised() {
        parse('(');
        Field<?> r = toField(parseNumericOp());
        parse(')');
        return r;
    }

    private final Field<?> parseFieldParenthesised() {
        parse('(');
        Field<?> r = toField(parseField());
        parse(')');
        return r;
    }

    private final boolean parseEmptyParens() {
        return parse('(') && parse(')');
    }

    private final boolean parseEmptyParensIf() {
        return parseIf('(') && parse(')') || true;
    }

    // Any numeric operator of low precedence
    // See https://www.postgresql.org/docs/current/sql-syntax-lexical.html#SQL-PRECEDENCE
    private final FieldOrRow parseNumericOp() {
        FieldOrRow r = parseSum();

        if (r instanceof Field)
            for (;;)
                if (parseIf("<<"))
                    r = ((Field) r).shl((Field) parseSum());
                else if (parseIf(">>"))
                    r = ((Field) r).shr((Field) parseSum());
                else
                    break;

        return r;
    }

    private final FieldOrRow parseSum() {
        FieldOrRow r = parseFactor();

        if (r instanceof Field)
            for (;;)
                if (parseIf('+'))
                    r = parseSumRightOperand(r, true);
                else if (parseIf('-'))
                    r = parseSumRightOperand(r, false);
                else
                    break;

        return r;
    }

    private final Field parseSumRightOperand(FieldOrRow r, boolean add) {
        Field rhs = (Field) parseFactor();
        DatePart part;

        if ((parseKeywordIf("YEAR") || parseKeywordIf("YEARS")) && requireProEdition())
            part = DatePart.YEAR;
        else if ((parseKeywordIf("MONTH") || parseKeywordIf("MONTHS")) && requireProEdition())
            part = DatePart.MONTH;
        else if ((parseKeywordIf("DAY") || parseKeywordIf("DAYS")) && requireProEdition())
            part = DatePart.DAY;
        else if ((parseKeywordIf("HOUR") || parseKeywordIf("HOURS")) && requireProEdition())
            part = DatePart.HOUR;
        else if ((parseKeywordIf("MINUTE") || parseKeywordIf("MINUTES")) && requireProEdition())
            part = DatePart.MINUTE;
        else if ((parseKeywordIf("SECOND") || parseKeywordIf("SECONDS")) && requireProEdition())
            part = DatePart.SECOND;
        else
            part = null;

        Field lhs = (Field) r;









            if (add)
                return lhs.add(rhs);
            else if (lhs.getDataType().isDate() && rhs.getDataType().isDate())
                return DSL.dateDiff(lhs, rhs);
            else if (lhs.getDataType().isTimestamp() && rhs.getDataType().isTimestamp())
                return DSL.timestampDiff(lhs, rhs);
            else
                return lhs.sub(rhs);
    }

    private final FieldOrRow parseFactor() {
        FieldOrRow r = parseExp();

        if (r instanceof Field)
            for (;;)
                if (!peek("*=") && parseIf('*'))
                    r = ((Field) r).mul((Field) parseExp());
                else if (parseIf('/'))
                    r = ((Field) r).div((Field) parseExp());
                else if (parseIf('%'))
                    r = ((Field) r).mod((Field) parseExp());






                else
                    break;

        return r;
    }

    private final FieldOrRow parseExp() {
        FieldOrRow r = parseUnaryOps();

        if (r instanceof Field)
            for (;;)
                if (!peek("^=") && parseIf('^') || parseIf("**"))
                    r = ((Field) r).pow(toField(parseUnaryOps()));
                else
                    break;

        return r;
    }

    private final FieldOrRow parseUnaryOps() {
        if (parseKeywordIf("CONNECT_BY_ROOT") && requireProEdition()) {



        }

        FieldOrRow r;
        Sign sign = parseSign();

        if (sign == Sign.NONE)
            r = parseTerm();
        else if (sign == Sign.PLUS)
            r = toField(parseTerm());
        else if ((r = parseFieldUnsignedNumericLiteralIf(Sign.MINUS)) == null)
            r = toField(parseTerm()).neg();

        if (parseTokensIf('(', '+', ')') && requireProEdition())



            ;

        // [#7171] Only identifier based field expressions could have been functions
        //         E.g. 'abc' ('xyz') may be some other type of syntax, e.g. from Db2 SIGNAL statements
        int p = position();
        if (r instanceof TableField && parseIf('('))






            throw exception("Unknown function");

        while (parseIf("::"))
            r = cast(toField(r), parseDataType());




        if (parseIf('[')) {
            r = arrayGet((Field) toField(r), (Field) parseField());
            parse(']');
        }

        return r;
    }

    private final Sign parseSign() {
        Sign sign = Sign.NONE;

        for (;;)
            if (parseIf('+'))
                sign = sign == Sign.NONE ? Sign.PLUS  : sign;
            else if (parseIf('-'))
                sign = sign == Sign.NONE ? Sign.MINUS : sign.invert();
            else
                break;

        return sign;
    }

    private static enum Sign {
        NONE,
        PLUS,
        MINUS;

        final Sign invert() {
            if (this == PLUS)
                return MINUS;
            else if (this == MINUS)
                return PLUS;
            else
                return NONE;
        }
    }

    private final FieldOrRow parseTerm() {
        FieldOrRow field;
        Object value;






        switch (characterUpper()) {

            // [#8821] Known prefixes so far:
            case ':':
            case '@':
            case '?':
                if ((field = parseBindVariableIf()) != null)
                    return field;

                break;









            case '\'':
                return inline(parseStringLiteral());

            case '$':
                if ((field = parseBindVariableIf()) != null)
                    return field;
                else if ((value = parseDollarQuotedStringLiteralIf()) != null)
                    return inline((String) value);

                break;

            case 'A':
                if (parseFunctionNameIf("ABS"))
                    return abs((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("ASC", "ASCII", "ASCII_VAL"))
                    return ascii((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("ACOS"))
                    return acos((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("ASIN"))
                    return asin((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("ATAN", "ATN"))
                    return atan((Field) parseFieldNumericOpParenthesised());
                else if ((field = parseFieldAtan2If()) != null)
                    return field;

                else if (parseFunctionNameIf("ASCII_CHAR"))
                    return chr((Field) parseFieldParenthesised());

                else if ((field = parseArrayValueConstructorIf()) != null)
                    return field;

                else if (parseFunctionNameIf("ADD_YEARS"))
                    return parseFieldAddDatePart(YEAR);
                else if (parseFunctionNameIf("ADD_MONTHS"))
                    return parseFieldAddDatePart(MONTH);
                else if (parseFunctionNameIf("ADD_DAYS"))
                    return parseFieldAddDatePart(DAY);
                else if (parseFunctionNameIf("ADD_HOURS"))
                    return parseFieldAddDatePart(HOUR);
                else if (parseFunctionNameIf("ADD_MINUTES"))
                    return parseFieldAddDatePart(MINUTE);
                else if (parseFunctionNameIf("ADD_SECONDS"))
                    return parseFieldAddDatePart(SECOND);

                else if ((field = parseFieldArrayGetIf()) != null)
                    return field;

                break;

            case 'B':
                if (parseFunctionNameIf("BIT_LENGTH"))
                    return bitLength((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("BITCOUNT", "BIT_COUNT"))
                    return bitCount((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("BYTE_LENGTH"))
                    return octetLength((Field) parseFieldParenthesised());
                else if ((field = parseFieldBitwiseFunctionIf()) != null)
                    return field;

                else if ((value = parseBitLiteralIf()) != null)
                    return DSL.inline((Boolean) value);

                break;

            case 'C':
                if ((field = parseFieldConcatIf()) != null)
                    return field;
                else if ((parseFunctionNameIf("CURRENT_CATALOG") && parseEmptyParens()))
                    return currentCatalog();
                else if ((parseFunctionNameIf("CURRENT_DATABASE") && parseEmptyParens()))
                    return currentCatalog();
                else if ((parseKeywordIf("CURRENT_SCHEMA", "CURRENT SCHEMA")) && parseEmptyParensIf())
                    return currentSchema();
                else if ((parseKeywordIf("CURRENT_USER", "CURRENT USER", "CURRENTUSER")) && parseEmptyParensIf())
                    return currentUser();
                else if (parseFunctionNameIf("CHR", "CHAR"))
                    return chr((Field) parseFieldParenthesised());

                else if ((field = parseFieldCharIndexIf()) != null)
                    return field;
                else if (parseFunctionNameIf("CHAR_LENGTH"))
                    return charLength((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("CARDINALITY"))
                    return cardinality((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("CEILING", "CEIL"))
                    return ceil((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("COSH"))
                    return cosh((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("COS"))
                    return cos((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("COTH"))
                    return coth((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("COT"))
                    return cot((Field) parseFieldNumericOpParenthesised());
                else if ((field = parseNextvalCurrvalIf(SequenceMethod.CURRVAL)) != null)
                    return field;
                else if (parseFunctionNameIf("CENTURY"))
                    return century(parseFieldParenthesised());

                else if ((parseKeywordIf("CURRENT_DATE") || parseKeywordIf("CURRENT DATE")) && parseEmptyParensIf())
                    return currentDate();
                else if (parseKeywordIf("CURRENT_TIMESTAMP") || parseKeywordIf("CURRENT TIMESTAMP")) {
                    Field<Integer> precision = null;
                    if (parseIf('('))
                        if (!parseIf(')')) {
                            precision = (Field<Integer>) parseField();
                            parse(')');
                        }
                    return precision != null ? currentTimestamp(precision) : currentTimestamp();
                }
                else if ((parseKeywordIf("CURRENT_TIME") || parseKeywordIf("CURRENT TIME")) && parseEmptyParensIf())
                    return currentTime();
                else if (parseFunctionNameIf("CURDATE") && parseEmptyParens())
                    return currentDate();
                else if (parseFunctionNameIf("CURTIME") && parseEmptyParens())
                    return currentTime();

                else if ((field = parseFieldCaseIf()) != null)
                    return field;
                else if ((field = parseFieldCastIf()) != null)
                    return field;
                else if ((field = parseFieldCoalesceIf()) != null)
                    return field;
                else if ((field = parseFieldCumeDistIf()) != null)
                    return field;
                else if ((field = parseFieldConvertIf()) != null)
                    return field;
                else if ((field = parseFieldChooseIf()) != null)
                    return field;
                else if (parseKeywordIf("CONNECT_BY_ISCYCLE") && requireProEdition()) {



                }
                else if (parseKeywordIf("CONNECT_BY_ISLEAF") && requireProEdition()) {



                }

                break;

            case 'D':
                if ((parseFunctionNameIf("DB_NAME") && parseEmptyParens()))
                    return currentCatalog();
                else if ((parseFunctionNameIf("DBINFO") && parse('(') && parseStringLiteral("dbname") != null && parse(')')))
                    return currentCatalog();
                else if (parseFunctionNameIf("DIGITS"))
                    return digits((Field) parseFieldParenthesised());

                else if ((field = parseFieldDateLiteralIf()) != null)
                    return field;
                else if ((field = parseFieldDateTruncIf()) != null)
                    return field;
                else if ((field = parseFieldDateAddIf()) != null)
                    return field;
                else if ((field = parseFieldDateDiffIf()) != null)
                    return field;
                else if ((field = parseFieldDatePartIf()) != null)
                    return field;

                else if ((field = parseFieldDenseRankIf()) != null)
                    return field;
                else if (parseFunctionNameIf("DECADE"))
                    return decade(parseFieldParenthesised());
                else if (parseFunctionNameIf("DAY")
                      || parseFunctionNameIf("DAYOFMONTH"))
                    return day(parseFieldParenthesised());
                // DB2 and MySQL support the non-ISO version where weeks go from Sunday = 1 to Saturday = 7
                else if (parseFunctionNameIf("DAYOFWEEK_ISO"))
                    return isoDayOfWeek(parseFieldParenthesised());
                else if (parseFunctionNameIf("DAYOFWEEK")
                      || parseFunctionNameIf("DAY_OF_WEEK"))
                    return dayOfWeek(parseFieldParenthesised());
                else if (parseFunctionNameIf("DAYOFYEAR")
                      || parseFunctionNameIf("DAY_OF_YEAR"))
                    return dayOfYear(parseFieldParenthesised());
                else if (parseFunctionNameIf("DEGREES")
                      || parseFunctionNameIf("DEGREE")
                      || parseFunctionNameIf("DEG"))
                    return deg((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("DATALENGTH"))
                    return octetLength((Field) parseFieldParenthesised());

                else if ((field = parseFieldDecodeIf()) != null)
                    return field;
                else if (parseKeywordIf("DEFAULT"))
                    return default_();

                break;

            case 'E':

                // [#6704] PostgreSQL E'...' escaped string literals
                if (characterNext() == '\'')
                    return inline(parseStringLiteral());

                else if ((field = parseFieldExtractIf()) != null)
                    return field;
                else if (parseFunctionNameIf("EXP"))
                    return exp((Field) parseFieldNumericOpParenthesised());

                else if (parseFunctionNameIf("EPOCH"))
                    return epoch(parseFieldParenthesised());

                break;

            case 'F':
                if (parseFunctionNameIf("FLOOR"))
                    return floor((Field) parseFieldNumericOpParenthesised());

                else if ((field = parseFieldFirstValueIf()) != null)
                    return field;
                else if ((field = parseFieldFieldIf()) != null)
                    return field;

                break;

            case 'G':
                if (parseKeywordIf("GETDATE") && parseEmptyParens())
                    return currentTimestamp();

                else if (parseFunctionNameIf("GENGUID", "GENERATE_UUID", "GEN_RANDOM_UUID") && parseEmptyParens())
                    return uuid();

                else if ((field = parseFieldGreatestIf()) != null)
                    return field;
                else if ((field = parseFieldGroupIdIf()) != null)
                    return field;
                else if ((field = parseFieldGroupingIdIf()) != null)
                    return field;
                else if ((field = parseFieldGroupingIf()) != null)
                    return field;
                else
                    break;

            case 'H':
                if (parseFunctionNameIf("HOUR"))
                    return hour(parseFieldParenthesised());

                else if (parseFunctionNameIf("HASH_MD5"))
                    return md5((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("HEX"))
                    return toHex((Field) parseFieldParenthesised());

                break;

            case 'I':
                if ((field = parseFieldIntervalLiteralIf()) != null)
                    return field;
                else if (parseFunctionNameIf("ISO_DAY_OF_WEEK"))
                    return isoDayOfWeek(parseFieldParenthesised());

                else if ((field = parseFieldInstrIf()) != null)
                    return field;

                else if ((field = parseFieldInsertIf()) != null)
                    return field;

                else if ((field = parseFieldIfnullIf()) != null)
                    return field;
                else if ((field = parseFieldIsnullIf()) != null)
                    return field;
                else if ((field = parseFieldIfIf()) != null)
                    return field;
                else
                    break;

            case 'J':
                if ((field = parseFieldJSONArrayConstructorIf()) != null)
                    return field;
                else if ((field = parseFieldJSONObjectConstructorIf()) != null)
                    return field;
                else if ((field = parseFieldJSONValueIf()) != null)
                    return field;

                break;

            case 'L':
                if (parseFunctionNameIf("LOWER", "LCASE"))
                    return lower((Field) parseFieldParenthesised());
                else if ((field = parseFieldLpadIf()) != null)
                    return field;
                else if ((field = parseFieldLtrimIf()) != null)
                    return field;
                else if ((field = parseFieldLeftIf()) != null)
                    return field;

                else if (parseFunctionNameIf("LENGTH", "LEN"))
                    return length((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("LENGTHB"))
                    return octetLength((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("LN", "LOGN"))
                    return ln((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("LOG10"))
                    return log10((Field) parseFieldNumericOpParenthesised());
                else if ((field = parseFieldLogIf()) != null)
                    return field;
                else if ((field = parseFieldLocateIf()) != null)
                    return field;
                else if (parseKeywordIf("LEVEL") && requireProEdition()) {



                }
                else if ((field = parseFieldShlIf()) != null)
                    return field;

                else if ((field = parseFieldLeastIf()) != null)
                    return field;
                else if ((field = parseFieldLeadLagIf()) != null)
                    return field;
                else if ((field = parseFieldLastValueIf()) != null)
                    return field;

                break;

            case 'M':
                if ((field = parseFieldModIf()) != null)
                    return field;
                else if (parseFunctionNameIf("MICROSECOND"))
                    return microsecond(parseFieldParenthesised());
                else if (parseFunctionNameIf("MILLENNIUM"))
                    return millennium(parseFieldParenthesised());
                else if (parseFunctionNameIf("MILLISECOND"))
                    return millisecond(parseFieldParenthesised());
                else if (parseFunctionNameIf("MINUTE"))
                    return minute(parseFieldParenthesised());
                else if (parseFunctionNameIf("MONTH"))
                    return month(parseFieldParenthesised());

                else if ((field = parseFieldMidIf()) != null)
                    return field;
                else if (parseFunctionNameIf("MD5"))
                    return md5((Field) parseFieldParenthesised());

                else if ((field = parseMultisetValueConstructorIf()) != null)
                    return field;

                else if ((field = parseFieldGreatestIf()) != null)
                    return field;
                else if ((field = parseFieldLeastIf()) != null)
                    return field;
                else if ((field = parseFieldDecodeIf()) != null)
                    return field;

                break;

            case 'N':

                // [#9540] N'...' NVARCHAR literals
                if (characterNext() == '\'')
                    return inline(parseStringLiteral(), NVARCHAR);
                else if ((field = parseFieldNewIdIf()) != null)
                    return field;

                else if ((field = parseFieldNvl2If()) != null)
                    return field;
                else if ((field = parseFieldNvlIf()) != null)
                    return field;
                else if ((field = parseFieldNullifIf()) != null)
                    return field;
                else if ((field = parseFieldNtileIf()) != null)
                    return field;
                else if ((field = parseFieldNthValueIf()) != null)
                    return field;
                else if ((field = parseNextValueIf()) != null)
                    return field;
                else if ((field = parseNextvalCurrvalIf(SequenceMethod.NEXTVAL)) != null)
                    return field;
                else if (parseFunctionNameIf("NOW") && parse('(')) {
                    if (parseIf(')'))
                        return now();
                    Field<Integer> precision = (Field<Integer>) parseField();
                    parse(')');
                    return now(precision);
                }

                break;

            case 'O':
                if ((field = parseFieldReplaceIf()) != null)
                    return field;
                else if ((field = parseFieldOverlayIf()) != null)
                    return field;
                else if ((field = parseFieldTranslateIf()) != null)
                    return field;

                else if (parseFunctionNameIf("OCTET_LENGTH"))
                    return octetLength((Field) parseFieldParenthesised());

                break;

            case 'P':
                if ((field = parseFieldPositionIf()) != null)
                    return field;
                else if ((field = parseFieldPercentRankIf()) != null)
                    return field;
                else if ((field = parseFieldPowerIf()) != null)
                    return field;
                else if (parseFunctionNameIf("PI") && parseEmptyParens())
                    return pi();

                else if (parseKeywordIf("PRIOR") && requireProEdition()) {



                }

                break;

            case 'Q':
                if (characterNext() == '\'')
                    return inline(parseStringLiteral());

                else if (parseFunctionNameIf("QUARTER"))
                    return quarter(parseFieldParenthesised());

            case 'R':
                if ((field = parseFieldReplaceIf()) != null)
                    return field;
                else if ((field = parseFieldRegexpReplaceIf()) != null)
                    return field;
                else if ((field = parseFieldRepeatIf()) != null)
                    return field;
                else if (parseFunctionNameIf("REVERSE"))
                    return reverse((Field) parseFieldParenthesised());
                else if ((field = parseFieldRpadIf()) != null)
                    return field;
                else if ((field = parseFieldRtrimIf()) != null)
                    return field;
                else if ((field = parseFieldRightIf()) != null)
                    return field;
                else if (parseFunctionNameIf("RANDOM_UUID") && parseEmptyParens())
                    return uuid();

                else if ((field = parseFieldRowNumberIf()) != null)
                    return field;
                else if ((field = parseFieldRankIf()) != null)
                    return field;
                else if ((field = parseFieldRoundIf()) != null)
                    return field;
                else if (parseKeywordIf("ROWNUM") && requireProEdition()) {



                }
                else if (parseFunctionNameIf("RADIANS")
                      || parseFunctionNameIf("RADIAN")
                      || parseFunctionNameIf("RAD"))
                    return rad((Field) parseFieldNumericOpParenthesised());
                else if ((field = parseFieldRandIf()) != null)
                    return field;
                else if ((field = parseFieldRatioToReportIf()) != null)
                    return field;
                else if ((field = parseFieldShrIf()) != null)
                    return field;

                else if (parseFunctionNameIf("ROW"))
                    return parseTuple();

                break;

            case 'S':
                if ((field = parseFieldSubstringIf()) != null)
                    return field;
                else if ((field = parseFieldSubstringIndexIf()) != null)
                    return field;
                else if (parseFunctionNameIf("SPACE"))
                    return space((Field) parseFieldParenthesised());
                else if ((field = parseFieldSplitPartIf()) != null)
                    return field;
                else if ((field = parseFieldReplaceIf()) != null)
                    return field;
                else if (parseFunctionNameIf("SCHEMA") && parseIf('(') && parse(')'))
                    return currentSchema();
                else if (parseFunctionNameIf("STRREVERSE"))
                    return reverse((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("SYSUUID") && parseEmptyParensIf())
                    return uuid();

                else if (parseFunctionNameIf("SECOND"))
                    return second(parseFieldParenthesised());
                else if (parseFunctionNameIf("SEQ4", "SEQ8") && parse('(') && parse(')') && requireProEdition()) {



                }
                else if (parseFunctionNameIf("SIGN", "SGN"))
                    return sign((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("SQRT", "SQR"))
                    return sqrt((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("SQUARE"))
                    return square((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("SINH"))
                    return sinh((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("SIN"))
                    return sin((Field) parseFieldNumericOpParenthesised());
                else if ((field = parseFieldShlIf()) != null)
                    return field;
                else if ((field = parseFieldShrIf()) != null)
                    return field;

                else if ((field = parseFieldSysConnectByPathIf()) != null)
                    return field;

                break;

            case 'T':
                if ((field = parseBooleanValueExpressionIf()) != null)
                    return field;

                else if ((field = parseFieldTrimIf()) != null)
                    return field;
                else if ((field = parseFieldTranslateIf()) != null)
                    return field;
                else if ((field = parseFieldToCharIf()) != null)
                    return field;
                else if (parseFunctionNameIf("TO_HEX"))
                    return toHex((Field) parseFieldParenthesised());

                else if (parseFunctionNameIf("TANH"))
                    return tanh((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("TAN"))
                    return tan((Field) parseFieldNumericOpParenthesised());
                else if ((field = parseFieldToNumberIf()) != null)
                    return field;
                else if (parseFunctionNameIf("TIMEZONE_HOUR"))
                    return timezoneHour(parseFieldParenthesised());
                else if (parseFunctionNameIf("TIMEZONE_MINUTE"))
                    return timezoneMinute(parseFieldParenthesised());
                else if (parseFunctionNameIf("TIMEZONE"))
                    return timezone(parseFieldParenthesised());

                else if ((field = parseFieldTimestampLiteralIf()) != null)
                    return field;
                else if ((field = parseFieldTimeLiteralIf()) != null)
                    return field;
                else if ((field = parseFieldToDateIf()) != null)
                    return field;
                else if ((field = parseFieldToTimestampIf()) != null)
                    return field;
                else if ((field = parseFieldTimestampDiffIf()) != null)
                    return field;

                else if ((field = parseFieldTruncIf()) != null)
                    return field;

                break;

            case 'U':
                if (parseFunctionNameIf("UPPER", "UCASE"))
                    return DSL.upper((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("UUID", "UUID_GENERATE", "UUID_STRING") && parseEmptyParens())
                    return uuid();

                else if (parseFunctionNameIf("UNIX_TIMESTAMP"))
                    return epoch(parseFieldParenthesised());

                break;

            case 'W':
                if ((field = parseFieldWidthBucketIf()) != null)
                    return field;
                else if (parseFunctionNameIf("WEEK"))
                    return week(parseFieldParenthesised());

                break;

            case 'X':
                if ((value = parseBinaryLiteralIf()) != null)
                    return inline((byte[]) value);

                else if ((field = parseFieldXMLCommentIf()) != null)
                    return field;
                else if ((field = parseFieldXMLConcatIf()) != null)
                    return field;
                else if ((field = parseFieldXMLElementIf()) != null)
                    return field;
                else if ((field = parseFieldXMLPIIf()) != null)
                    return field;
                else if ((field = parseFieldXMLForestIf()) != null)
                    return field;
                else if ((field = parseFieldXMLParseIf()) != null)
                    return field;
                else if ((field = parseFieldXMLDocumentIf()) != null)
                    return field;
                else if ((field = parseFieldXMLQueryIf()) != null)
                    return field;
                else if ((field = parseFieldXMLSerializeIf()) != null)
                    return field;

                break;

            case 'Y':
                if (parseFunctionNameIf("YEAR"))
                    return year(parseFieldParenthesised());

                break;

            case 'Z':
                if (parseFunctionNameIf("ZEROIFNULL"))
                    return coalesce(parseFieldParenthesised(), zero());

                break;

            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '-':
            case '.':
                if ((field = parseFieldUnsignedNumericLiteralIf(Sign.NONE)) != null)
                    return field;

                break;

            case '{':
                parse('{', false);

                switch (characterUpper()) {
                    case 'D':
                        parseKeyword("D");
                        field = inline(parseDateLiteral());
                        break;

                    case 'F':
                        parseKeyword("FN");

                        // TODO: Limit the supported expressions in this context to the ones specified here:
                        // http://download.oracle.com/otn-pub/jcp/jdbc-4_2-mrel2-eval-spec/jdbc4.2-fr-spec.pdf
                        field = parseTerm();
                        break;

                    case 'T':
                        if (parseKeywordIf("TS")) {
                            field = inline(parseTimestampLiteral());
                        }
                        else {
                            parseKeyword("T");
                            field = inline(parseTimeLiteral());
                        }
                        break;

                    default:
                        throw exception("Unsupported JDBC escape literal");
                }

                parse('}');
                return field;

            case '(':

                // A term parenthesis can mark the beginning of any of:
                // - ROW expression without ROW keyword:        E.g. (1, 2)
                // - Parenthesised field expression:            E.g. (1 + 2)
                // - A correlated subquery:                     E.g. (select 1)
                // - A correlated subquery with nested set ops: E.g. ((select 1) except (select 2))
                // - A combination of the above:                E.g. ((select 1) + 2, ((select 1) except (select 2)) + 2)
                int p = position();
                EnumSet fk = forbidden;

                try {
                    if (!forbidden.isEmpty())
                        forbidden = EnumSet.noneOf(FunctionKeyword.class);

                    try {
                        if (peekSelectOrWith(true)) {
                            parse('(');
                            SelectQueryImpl<Record> select = parseWithOrSelect();
                            parse(')');
                            if (Tools.degree(select) != 1)
                                throw exception("Select list must contain exactly one column");

                            return field((Select) select);
                        }
                    }
                    catch (ParserException e) {

                        // TODO: Find a better solution than backtracking, here, which doesn't complete in O(N)
                        if (e.getMessage().contains("Token ')' expected"))
                            position(p);
                        else
                            throw e;
                    }

                    parse('(');
                    FieldOrRow r = parseFieldOrRow();
                    List<Field<?>> list = null;

                    if (r instanceof Field) {
                        while (parseIf(',')) {
                            if (list == null) {
                                list = new ArrayList<>();
                                list.add((Field) r);
                            }

                            // TODO Allow for nesting ROWs
                            list.add(parseField());
                        }
                    }

                    parse(')');
                    return list != null ? row(list) : r;
                }
                finally {
                    forbidden = fk;
                }
        }

        if ((field = parseAggregateFunctionIf()) != null)
            return field;

        else if ((field = parseBooleanValueExpressionIf()) != null)
            return field;

        else
            return parseFieldNameOrSequenceExpression();
    }

    private final Field<?> parseFieldAddDatePart(DatePart part) {
        parse('(');
        Field<?> arg1 = parseField();
        parse(',');
        Field<?> arg2 = parseField();
        parse(')');

        return DSL.dateAdd((Field) arg1, (Field) arg2, part);
    }

    private final boolean peekSelectOrWith(boolean peekIntoParens) {
        return peekKeyword("WITH", false, peekIntoParens, false) || peekSelect(peekIntoParens);
    }

    private final boolean peekSelect(boolean peekIntoParens) {
        return peekKeyword("SELECT", false, peekIntoParens, false) ||
               peekKeyword("SEL", false, peekIntoParens, false);
    }

    private final Field<?> parseFieldSplitPartIf() {
        if (parseKeywordIf("SPLIT_PART")) {
            parse('(');
            Field<?> f1 = parseField();
            parse(',');
            Field<?> f2 = parseField();
            parse(',');
            Field<?> f3 = parseField();
            parse(')');

            return splitPart((Field) f1, (Field) f2, (Field) f3);
        }

        return null;
    }

    private final Field<?> parseFieldShlIf() {
        if (parseKeywordIf("SHL") || parseKeywordIf("SHIFTLEFT") || parseKeywordIf("LSHIFT")) {
            parse('(');
            Field<?> x = toField(parseNumericOp());
            parse(',');
            Field<?> y = toField(parseNumericOp());
            parse(')');

            return shl((Field) x, (Field) y);
        }

        return null;
    }

    private final Field<?> parseFieldShrIf() {
        if (parseKeywordIf("SHR") || parseKeywordIf("SHIFTRIGHT") || parseKeywordIf("RSHIFT")) {
            parse('(');
            Field<?> x = toField(parseNumericOp());
            parse(',');
            Field<?> y = toField(parseNumericOp());
            parse(')');

            return shr((Field) x, (Field) y);
        }

        return null;
    }

    private final Field<?> parseFieldSysConnectByPathIf() {
        if (parseFunctionNameIf("SYS_CONNECT_BY_PATH") && requireProEdition()) {








        }

        return null;
    }

    private final Field<?> parseFieldBitwiseFunctionIf() {
        int p = position();

        char c1 = character(p + 1);
        char c2 = character(p + 2);
        boolean agg = false;

        if (c1 != 'I' && c1 != 'i')
            return null;
        if (c2 != 'T' && c2 != 't' && c2 != 'N' && c2 != 'n')
            return null;

        if (parseKeywordIf("BIT_AND") ||
            parseKeywordIf("BITAND") ||
            parseKeywordIf("BIN_AND") ||
            (agg = parseKeywordIf("BIT_AND_AGG")) ||
            (agg = parseKeywordIf("BITAND_AGG")) ||
            (agg = parseKeywordIf("BIN_AND_AGG"))) {
            parse('(');
            Field<?> x = toField(parseNumericOp());

            if (agg && parse(')') || parseIf(')'))
                return parseAggregateFunctionIf(false, bitAndAgg((Field) x));

            parse(',');
            Field<?> y = toField(parseNumericOp());
            parse(')');

            return bitAnd((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_NAND") || parseKeywordIf("BITNAND") || parseKeywordIf("BIN_NAND")) {
            parse('(');
            Field<?> x = toField(parseNumericOp());
            parse(',');
            Field<?> y = toField(parseNumericOp());
            parse(')');

            return bitNand((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_OR") ||
            parseKeywordIf("BITOR") ||
            parseKeywordIf("BIN_OR") ||
            (agg = parseKeywordIf("BIT_OR_AGG")) ||
            (agg = parseKeywordIf("BITOR_AGG")) ||
            (agg = parseKeywordIf("BIN_OR_AGG"))) {
            parse('(');
            Field<?> x = toField(parseNumericOp());

            if (agg && parse(')') || parseIf(')'))
                return parseAggregateFunctionIf(false, bitOrAgg((Field) x));

            parse(',');
            Field<?> y = toField(parseNumericOp());
            parse(')');

            return bitOr((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_NOR") || parseKeywordIf("BITNOR") || parseKeywordIf("BIN_NOR")) {
            parse('(');
            Field<?> x = toField(parseNumericOp());
            parse(',');
            Field<?> y = toField(parseNumericOp());
            parse(')');

            return bitNor((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_XOR") ||
            parseKeywordIf("BITXOR") ||
            parseKeywordIf("BIN_XOR") ||
            (agg = parseKeywordIf("BIT_XOR_AGG")) ||
            (agg = parseKeywordIf("BITXOR_AGG")) ||
            (agg = parseKeywordIf("BIN_XOR_AGG"))) {
            parse('(');
            Field<?> x = toField(parseNumericOp());

            if (agg && parse(')') || parseIf(')'))
                return parseAggregateFunctionIf(false, bitXorAgg((Field) x));

            parse(',');
            Field<?> y = toField(parseNumericOp());
            parse(')');

            return bitXor((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_XNOR") || parseKeywordIf("BITXNOR") || parseKeywordIf("BIN_XNOR")) {
            parse('(');
            Field<?> x = toField(parseNumericOp());
            parse(',');
            Field<?> y = toField(parseNumericOp());
            parse(')');

            return bitXNor((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_NOT") || parseKeywordIf("BITNOT") || parseKeywordIf("BIN_NOT")) {
            parse('(');
            Field<?> x = toField(parseNumericOp());
            parse(')');

            return bitNot((Field) x);
        }
        else if (parseKeywordIf("BIN_SHL", "BITSHIFTLEFT")) {
            parse('(');
            Field<?> x = toField(parseNumericOp());
            parse(',');
            Field<?> y = toField(parseNumericOp());
            parse(')');

            return shl((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIN_SHR", "BITSHIFTRIGHT")) {
            parse('(');
            Field<?> x = toField(parseNumericOp());
            parse(',');
            Field<?> y = toField(parseNumericOp());
            parse(')');

            return shr((Field) x, (Field) y);
        }

        return null;
    }

    private final Field<?> parseFieldNewIdIf() {
        if (parseFunctionNameIf("NEWID")) {
            parse('(');
            Long l = parseSignedIntegerLiteralIf();

            if (l != null && l.longValue() != -1L)
                throw expected("No argument or -1 expected");

            parse(')');
            return uuid();
        }

        return null;
    }

    private final Field<?> parseNextValueIf() {
        if (parseKeywordIf("NEXT VALUE FOR"))
            return sequence(parseName()).nextval();

        return null;
    }

    private final Field<?> parseNextvalCurrvalIf(SequenceMethod method) {
        if (parseFunctionNameIf(method.name())) {
            parse('(');

            Name name = parseNameIf();
            Sequence s = name != null
                ? sequence(name)
                : sequence(dsl.parser().parseName(parseStringLiteral()));

            parse(')');

            if (method == SequenceMethod.NEXTVAL)
                return s.nextval();
            else if (method == SequenceMethod.CURRVAL)
                return s.currval();
            else
                throw exception("Only NEXTVAL and CURRVAL methods supported");
        }

        return null;
    }

    private static enum SequenceMethod {
        NEXTVAL,
        CURRVAL;
    }

    private final Field<?> parseFieldXMLCommentIf() {
        if (parseFunctionNameIf("XMLCOMMENT")) {
            parse('(');
            Field<String> comment = (Field<String>) parseField();
            parse(')');

            return xmlcomment(comment);
        }

        return null;
    }

    private final Field<?> parseFieldXMLSerializeIf() {
        if (parseFunctionNameIf("XMLSERIALIZE")) {
            parse('(');
            boolean content = parseKeywordIf("CONTENT");
            if (!content)
                parseKeywordIf("DOCUMENT");

            Field<XML> value = (Field<XML>) parseField();
            parseKeyword("AS");
            DataType<?> type = parseCastDataType();
            parse(')');

            return content ? xmlserializeContent(value, type) : xmlserializeDocument(value, type);
        }

        return null;
    }

    private final Field<?> parseFieldXMLConcatIf() {
        if (parseFunctionNameIf("XMLCONCAT")) {
            parse('(');
            List<Field<?>> fields = parseList(',', ParseContext::parseField);
            parse(')');

            return xmlconcat(fields);
        }

        return null;
    }

    private final Field<?> parseFieldXMLElementIf() {
        if (parseFunctionNameIf("XMLELEMENT")) {
            parse('(');
            parseKeywordIf("NAME");

            if (parseIf(')'))
                return xmlelement(unquotedName("NAME"));

            Name name = parseIdentifier();
            XMLAttributes attr = null;
            List<Field<?>> content = new ArrayList<>();

            while (parseIf(',')) {
                if (attr == null && parseKeywordIf("XMLATTRIBUTES")) {
                    parse('(');
                    List<Field<?>> attrs = parseAliasedXMLContent();
                    parse(')');
                    attr = xmlattributes(attrs);
                }
                else
                    content.add(parseField());
            }
            parse(')');

            return attr == null
                ? xmlelement(name, content)
                : xmlelement(name, attr, content);
        }

        return null;
    }

    private final Field<?> parseFieldXMLDocumentIf() {
        if (parseFunctionNameIf("XMLDOCUMENT") && requireProEdition()) {








        }

        return null;
    }

    private final Field<?> parseFieldXMLPIIf() {
        if (parseFunctionNameIf("XMLPI")) {
            parse('(');
            parseKeyword("NAME");
            Name target = parseIdentifier();
            Field<?> content = parseIf(',') ? parseField() : null;
            parse(')');
            return content == null ? xmlpi(target) : xmlpi(target, content);
        }

        return null;
    }

    private final Field<?> parseFieldXMLForestIf() {
        if (parseFunctionNameIf("XMLFOREST")) {
            parse('(');
            List<Field<?>> content = parseAliasedXMLContent();
            parse(')');

            return xmlforest(content);
        }

        return null;
    }

    private final Field<?> parseFieldXMLParseIf() {
        if (parseFunctionNameIf("XMLPARSE")) {
            parse('(');
            DocumentOrContent documentOrContent;

            if (parseKeywordIf("DOCUMENT"))
                documentOrContent = DocumentOrContent.DOCUMENT;
            else if (parseKeywordIf("CONTENT"))
                documentOrContent = DocumentOrContent.CONTENT;
            else
                throw expected("CONTENT", "DOCUMENT");

            Field<String> xml = (Field<String>) parseField();
            parse(')');

            return documentOrContent == DocumentOrContent.DOCUMENT
                 ? xmlparseDocument(xml)
                 : xmlparseContent(xml);
        }

        return null;
    }

    private final Field<?> parseFieldXMLQueryIf() {
        if (parseFunctionNameIf("XMLQUERY")) {
            parse('(');
            Field<String> xpath = (Field<String>) parseField();
            XMLPassingMechanism m = parseXMLPassingMechanism();
            Field<XML> xml = (Field<XML>) parseField();
            parseKeywordIf("RETURNING CONTENT");
            parse(')');

            if (m == BY_REF)
                return xmlquery(xpath).passingByRef(xml);






            else
                return xmlquery(xpath).passing(xml);
        }

        return null;
    }

    private final XMLPassingMechanism parseXMLPassingMechanism() {
        XMLPassingMechanism result = parseXMLPassingMechanismIf();

        if (result == null)
            throw expected("PASSING");

        return result;
    }

    private final XMLPassingMechanism parseXMLPassingMechanismIf() {
        if (!parseKeywordIf("PASSING"))
            return null;
        else if (!parseKeywordIf("BY"))
            return XMLPassingMechanism.DEFAULT;
        else if (parseKeywordIf("REF"))
            return BY_REF;
        else if (parseKeywordIf("VALUE"))
            return BY_VALUE;
        else
            throw expected("REF", "VALUE");
    }

    private final List<Field<?>> parseAliasedXMLContent() {
        List<Field<?>> result = new ArrayList<>();

        do {
            Field<?> field = parseField();

            if (parseKeywordIf("AS"))
                field = field.as(parseIdentifier(true));

            result.add(field);
        }
        while (parseIf(','));
        return result;
    }

    private final AggregateFilterStep<?> parseXMLAggFunctionIf() {
        if (parseFunctionNameIf("XMLAGG")) {
            XMLAggOrderByStep<?> s1;
            AggregateFilterStep<?> s2;

            parse('(');
            s2 = s1 = xmlagg((Field<XML>) parseField());

            if (parseKeywordIf("ORDER BY"))
                s2 = s1.orderBy(parseList(',', ParseContext::parseSortField));

            parse(')');
            return s2;
        }

        return null;
    }

    private final Field<?> parseFieldJSONValueIf() {
        if (parseFunctionNameIf("JSON_VALUE")) {
            parse('(');
            Field json = parseField();
            parse(',');
            Field<String> path = (Field<String>) parseField();

            JSONValueOnStep<?> s1 = jsonValue(json, path);
            JSONValue.Behaviour behaviour = parseJSONValueBehaviourIf();
































            DataType<?> returning = parseJSONReturningIf();
            parse(')');
            return returning == null ? s1 : s1.returning(returning);
        }

        return null;
    }

    private final JSONValue.Behaviour parseJSONValueBehaviourIf() {
        if (parseKeywordIf("ERROR") && requireProEdition())
            return JSONValue.Behaviour.ERROR;
        else if (parseKeywordIf("NULL") && requireProEdition())
            return JSONValue.Behaviour.NULL;
        else if (parseKeywordIf("DEFAULT") && requireProEdition())
            return JSONValue.Behaviour.DEFAULT;
        else
            return null;
    }

    private final JSONExists.Behaviour parseJSONExistsOnErrorBehaviourIf() {
        if (parseKeywordIf("ERROR") && parseKeyword("ON ERROR") && requireProEdition())
            return JSONExists.Behaviour.ERROR;
        else if (parseKeywordIf("TRUE") && parseKeyword("ON ERROR") && requireProEdition())
            return JSONExists.Behaviour.TRUE;
        else if (parseKeywordIf("FALSE") && parseKeyword("ON ERROR") && requireProEdition())
            return JSONExists.Behaviour.FALSE;
        else if (parseKeywordIf("UNKNOWN") && parseKeyword("ON ERROR") && requireProEdition())
            return JSONExists.Behaviour.UNKNOWN;
        else
            return null;
    }

    private final DataType<?> parseJSONReturningIf() {
        return parseKeywordIf("RETURNING") ? parseDataType() : null;
    }

    private final Field<?> parseFieldJSONArrayConstructorIf() {
        boolean jsonb = false;

        if (parseFunctionNameIf("JSON_ARRAY", "JSON_BUILD_ARRAY") || (jsonb = parseFunctionNameIf("JSONB_BUILD_ARRAY"))) {
            parse('(');
            if (parseIf(')'))
                return jsonb ? jsonbArray() : jsonArray();

            List<Field<?>> result = null;
            JSONOnNull onNull = parseJSONNullTypeIf();
            DataType<?> returning = parseJSONReturningIf();

            if (onNull == null && returning == null) {
                result = parseList(',', ParseContext::parseField);
                onNull = parseJSONNullTypeIf();
                returning = parseJSONReturningIf();
            }

            parse(')');

            JSONArrayNullStep<?> s1 = result == null
                ? jsonb ? jsonbArray() : jsonArray()
                : jsonb ? jsonbArray(result) : jsonArray(result);
            JSONArrayReturningStep<?> s2 = onNull == NULL_ON_NULL
                ? s1.nullOnNull()
                : onNull == ABSENT_ON_NULL
                ? s1.absentOnNull()
                : s1;
            return returning == null ? s2 : s2.returning(returning);
        }

        return null;
    }

    private final AggregateFilterStep<?> parseJSONArrayAggFunctionIf() {
        boolean jsonb = false;

        if (parseFunctionNameIf("JSON_ARRAYAGG", "JSON_AGG") || (jsonb = parseFunctionNameIf("JSONB_AGG"))) {
            AggregateFilterStep<?> result;
            JSONArrayAggOrderByStep<?> s1;
            JSONArrayAggNullStep<?> s2;
            JSONArrayAggReturningStep<?> s3;
            JSONOnNull onNull;
            DataType<?> returning;

            parse('(');
            result = s3 = s2 = s1 = jsonb ? jsonbArrayAgg(parseField()) : jsonArrayAgg(parseField());

            if (parseKeywordIf("ORDER BY"))
                result = s3 = s2 = s1.orderBy(parseList(',', ParseContext::parseSortField));

            if ((onNull = parseJSONNullTypeIf()) != null)
                result = s3 = onNull == ABSENT_ON_NULL ? s2.absentOnNull() : s2.nullOnNull();

            if ((returning = parseJSONReturningIf()) != null)
                result = s3.returning(returning);

            parse(')');
            return result;
        }

        return null;
    }

    private final Field<?> parseFieldJSONObjectConstructorIf() {
        boolean jsonb = false;

        if (parseFunctionNameIf("JSON_OBJECT", "JSON_BUILD_OBJECT") || (jsonb = parseFunctionNameIf("JSONB_BUILD_OBJECT"))) {
            parse('(');
            if (parseIf(')'))
                return jsonb ? jsonbObject() : jsonObject();

            List<JSONEntry<?>> result;
            JSONOnNull onNull = parseJSONNullTypeIf();
            DataType<?> returning = parseJSONReturningIf();

            if (onNull == null && returning == null) {
                result = parseList(',', c -> parseJSONEntry());
                onNull = parseJSONNullTypeIf();
                returning = parseJSONReturningIf();
            }
            else
                result = new ArrayList<>();

            parse(')');

            JSONObjectNullStep<?> s1 = jsonb ? jsonbObject(result) : jsonObject(result);
            JSONObjectReturningStep<?> s2 = onNull == NULL_ON_NULL
                ? s1.nullOnNull()
                : onNull == ABSENT_ON_NULL
                ? s1.absentOnNull()
                : s1;
            return returning == null ? s2 : s2.returning(returning);
        }

        return null;
    }

    private final AggregateFilterStep<?> parseJSONObjectAggFunctionIf() {
        boolean jsonb = false;

        if (parseFunctionNameIf("JSON_OBJECTAGG", "JSON_OBJECT_AGG") || (jsonb = parseFunctionNameIf("JSONB_OBJECT_AGG"))) {
            AggregateFilterStep<?> result;
            JSONObjectAggNullStep<?> s1;
            JSONObjectAggReturningStep<?> s2;
            JSONOnNull onNull;
            DataType<?> returning;

            parse('(');
            result = s2 = s1 = jsonb ? jsonbObjectAgg(parseJSONEntry()) : jsonObjectAgg(parseJSONEntry());

            if ((onNull = parseJSONNullTypeIf()) != null)
                result = s2 = onNull == ABSENT_ON_NULL ? s1.absentOnNull() : s1.nullOnNull();

            if ((returning = parseJSONReturningIf()) != null)
                result = s2.returning(returning);

            parse(')');
            return result;
        }

        return null;
    }

    private final JSONOnNull parseJSONNullTypeIf() {
        if (parseKeywordIf("NULL ON NULL"))
            return NULL_ON_NULL;
        else if (parseKeywordIf("ABSENT ON NULL"))
            return ABSENT_ON_NULL;
        else
            return null;
    }

    private final JSONEntry<?> parseJSONEntry() {
        boolean valueRequired = parseKeywordIf("KEY");

        Field<String> key = (Field<String>) parseField();
        if (parseKeywordIf("VALUE"))
            ;
        else if (valueRequired)
            throw expected("VALUE");
        else
            parse(',');

        return key(key).value(parseField());
    }

    private final Field<?> parseArrayValueConstructorIf() {
        if (parseKeywordIf("ARRAY")) {
            if (parseIf('[')) {
                List<Field<?>> fields;

                if (parseIf(']')) {
                    fields = emptyList();
                }
                else {
                    fields = parseList(',', ParseContext::parseField);
                    parse(']');
                }

                // Prevent "wrong" javac method bind
                return DSL.array((Collection) fields);
            }
            else if (parseIf('(')) {
                SelectQueryImpl select = parseWithOrSelect(1);
                parse(')');

                return DSL.array(select);
            }
            else
                throw expected("[", "(");
        }

        return null;
    }

    private final Field<?> parseMultisetValueConstructorIf() {
        if (parseKeywordIf("MULTISET")) {
            if (parseIf('(')) {
                SelectQueryImpl select = parseWithOrSelect();
                parse(')');

                return DSL.multiset(select);
            }
            else
                throw expected("(");
        }

        return null;
    }

    private final Field<?> parseFieldArrayGetIf() {
        if (parseFunctionNameIf("ARRAY_GET")) {
            parse('(');
            Field f1 = parseField();
            parse(',');
            Field f2 = parseField();
            parse(')');

            return arrayGet(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldAtan2If() {
        if (parseFunctionNameIf("ATN2", "ATAN2")) {
            parse('(');
            Field<?> x = toField(parseNumericOp());
            parse(',');
            Field<?> y = toField(parseNumericOp());
            parse(')');

            return atan2((Field) x, (Field) y);
        }

        return null;
    }

    private final Field<?> parseFieldLogIf() {
        if (parseFunctionNameIf("LOG")) {
            parse('(');
            switch (parseFamily()) {















                default:
                    Field<?> base = toField(parseNumericOp());
                    parse(',');
                    Field<?> value = toField(parseNumericOp());
                    parse(')');
                    return log((Field) value, (Field) base);
            }
        }

        return null;
    }

    private final Field<?> parseFieldTruncIf() {
        boolean forceNumericPrecision = false;

        if (parseFunctionNameIf("TRUNC") || (forceNumericPrecision |= parseFunctionNameIf("TRUNCATE", "TRUNCNUM"))) {
            parse('(');
            Field<?> arg1 = parseField();

            if (forceNumericPrecision && parse(',') || parseIf(',')) {

                String part;
                if (!forceNumericPrecision && (part = parseStringLiteralIf()) != null) {
                    part = part.toUpperCase();

                    DatePart p;
                    if ("YY".equals(part) || "YYYY".equals(part) || "YEAR".equals(part))
                        p = DatePart.YEAR;
                    else if ("MM".equals(part) || "MONTH".equals(part))
                        p = DatePart.MONTH;
                    else if ("DD".equals(part))
                        p = DatePart.DAY;
                    else if ("HH".equals(part))
                        p = DatePart.HOUR;
                    else if ("MI".equals(part))
                        p = DatePart.MINUTE;
                    else if ("SS".equals(part))
                        p = DatePart.SECOND;
                    else
                        throw exception("Unsupported date part");

                    parse(')');
                    return DSL.trunc((Field) arg1, p);
                }
                else {
                    Field<?> arg2 = toField(parseNumericOp());
                    parse(')');
                    return DSL.trunc((Field) arg1, (Field) arg2);
                }
            }

            parse(')');

            // [#10668] Ignore TRUNC() when calling TRUNC(CURRENT_DATE) or TRUNC(SYSDATE) in Oracle
            if (arg1 instanceof CurrentDate)
                return arg1;
            else if (arg1.getDataType().isDateTime())
                return DSL.trunc((Field) arg1, DatePart.DAY);
            else if (arg1.getDataType().isNumeric())
                return DSL.trunc((Field) arg1, inline(0));

            // [#9044] By default, assume historic TRUNC(date) behaviour
            else
                return DSL.trunc((Field) arg1);
        }

        return null;
    }

    private final Field<?> parseFieldRoundIf() {
        if (parseFunctionNameIf("ROUND")) {
            Field arg1 = null;
            Field arg2 = null;

            parse('(');
            arg1 = toField(parseNumericOp());
            if (parseIf(','))
                arg2 = toField(parseNumericOp());

            parse(')');
            return arg2 == null ? round(arg1) : round(arg1, arg2);
        }

        return null;
    }

    private final Field<?> parseFieldPowerIf() {
        if (parseFunctionNameIf("POWER", "POW")) {
            parse('(');
            Field arg1 = toField(parseNumericOp());
            parse(',');
            Field arg2 = toField(parseNumericOp());
            parse(')');
            return DSL.power(arg1, arg2);
        }

        return null;
    }

    private final Field<?> parseFieldModIf() {
        if (parseFunctionNameIf("MOD")) {
            parse('(');
            Field<?> f1 = parseField();
            parse(',');
            Field<?> f2 = parseField();
            parse(')');
            return f1.mod((Field) f2);
        }

        return null;
    }

    private final Field<?> parseFieldWidthBucketIf() {
        if (parseFunctionNameIf("WIDTH_BUCKET")) {
            parse('(');
            Field<?> f1 = parseField();
            parse(',');
            Field<?> f2 = parseField();
            parse(',');
            Field<?> f3 = parseField();
            parse(',');
            Field<?> f4 = parseField();
            parse(')');
            return DSL.widthBucket((Field) f1, (Field) f2, (Field) f3, (Field) f4);
        }

        return null;
    }

    private final Field<?> parseFieldLeastIf() {
        if (parseFunctionNameIf("LEAST", "MINVALUE")) {
            parse('(');
            List<Field<?>> fields = parseList(',', ParseContext::parseField);
            parse(')');

            return least(fields.get(0), fields.size() > 1 ? fields.subList(1, fields.size()).toArray(EMPTY_FIELD) : EMPTY_FIELD);
        }

        return null;
    }

    private final Field<?> parseFieldGreatestIf() {
        if (parseFunctionNameIf("GREATEST", "MAXVALUE")) {
            parse('(');
            List<Field<?>> fields = parseList(',', ParseContext::parseField);
            parse(')');

            return greatest(fields.get(0), fields.size() > 1 ? fields.subList(1, fields.size()).toArray(EMPTY_FIELD) : EMPTY_FIELD);
        }

        return null;
    }

    private final Field<?> parseFieldGroupingIf() {
        if (parseFunctionNameIf("GROUPING")) {
            parse('(');
            Field<?> field = parseField();
            parse(')');

            return grouping(field);
        }

        return null;
    }

    private final Field<?> parseFieldGroupIdIf() {
        if (parseFunctionNameIf("GROUP_ID")) {
            requireProEdition();







        }

        return null;
    }

    private final Field<?> parseFieldGroupingIdIf() {
        if (parseFunctionNameIf("GROUPING_ID") && requireProEdition()) {








        }

        return null;
    }

    private final Field<?> parseFieldTimestampLiteralIf() {
        int p = position();

        if (parseKeywordIf("TIMESTAMP")) {
            if (parseKeywordIf("WITHOUT TIME ZONE")) {
                return inline(parseTimestampLiteral());
            }
            else if (parseIf('(')) {
                Field<?> f = parseField();
                parse(')');
                return timestamp((Field) f);
            }
            else if (peek('\'')) {
                return inline(parseTimestampLiteral());
            }
            else {
                position(p);
                return field(parseIdentifier());
            }
        }

        return null;
    }

    private final Timestamp parseTimestampLiteral() {
        try {
            return Timestamp.valueOf(parseStringLiteral());
        }
        catch (IllegalArgumentException e) {
            throw exception("Illegal timestamp literal");
        }
    }

    private final Field<?> parseFieldTimeLiteralIf() {
        int p = position();

        if (parseKeywordIf("TIME")) {
            if (parseKeywordIf("WITHOUT TIME ZONE")) {
                return inline(parseTimeLiteral());
            }
            else if (parseIf('(')) {
                Field<?> f = parseField();
                parse(')');
                return time((Field) f);
            }
            else if (peek('\'')) {
                return inline(parseTimeLiteral());
            }
            else {
                position(p);
                return field(parseIdentifier());
            }
        }

        return null;
    }

    private final Time parseTimeLiteral() {
        try {
            return Time.valueOf(parseStringLiteral());
        }
        catch (IllegalArgumentException e) {
            throw exception("Illegal time literal");
        }
    }

    private final Field<?> parseFieldIntervalLiteralIf() {
        int p = position();

        if (parseKeywordIf("INTERVAL")) {
            if (peek('\'')) {
                return inline(parseIntervalLiteral());
            }
            else {
                Long interval = parseUnsignedIntegerLiteralIf();

                if (interval != null) {
                    DatePart part = parseIntervalDatePart();
                    long l = interval;
                    int i = asInt(l);

                    switch (part) {
                        case YEAR:
                            return inline(new YearToMonth(i));
                        case QUARTER:
                            return inline(new YearToMonth(0, 3 * i));
                        case MONTH:
                            return inline(new YearToMonth(0, i));
                        case WEEK:
                            return inline(new DayToSecond(7 * i));
                        case DAY:
                            return inline(new DayToSecond(i));
                        case HOUR:
                            return inline(new DayToSecond(0, i));
                        case MINUTE:
                            return inline(new DayToSecond(0, 0, i));
                        case SECOND:
                            return inline(new DayToSecond(0, 0, 0, i));
                        case MILLISECOND:
                            return inline(new DayToSecond(0, 0, 0, asInt(l / 1000), (int) (l % 1000 * 1000000)));
                        case MICROSECOND:
                            return inline(new DayToSecond(0, 0, 0, asInt(l / 1000000), (int) (l % 1000000 * 1000)));
                        case NANOSECOND:
                            return inline(new DayToSecond(0, 0, 0, asInt(l / 1000000000), (int) (l % 1000000000)));
                    }
                }

                else {
                    position(p);
                    return field(parseIdentifier());
                }
            }
        }

        return null;
    }

    private final Interval parsePostgresIntervalLiteralIf() {
        int p = position();

        p:
        if (parseIf('\'')) {
            parseIf('@');

            Number year = null;
            Number month = null;
            Number day = null;
            Number hour = null;
            Number minute = null;
            Number second = null;

            do {
                boolean minus = parseIf('-');
                if (!minus)
                    parseIf('+');

                Number n = parseUnsignedNumericLiteralIf(minus ? Sign.MINUS : Sign.NONE);
                if (n == null)
                    break p;

                switch (characterUpper()) {
                    case 'D':
                        if (parseKeywordIf("D") ||
                            parseKeywordIf("DAY") ||
                            parseKeywordIf("DAYS"))
                            if (day == null)
                                day = n;
                            else
                                throw exception("Day part already defined");

                        break;

                    case 'H':
                        if (parseKeywordIf("H") ||
                            parseKeywordIf("HOUR") ||
                            parseKeywordIf("HOURS"))
                            if (hour == null)
                                hour = n;
                            else
                                throw exception("Hour part already defined");

                        break;

                    case 'M':
                        if (parseKeywordIf("M") ||
                            parseKeywordIf("MIN") ||
                            parseKeywordIf("MINS") ||
                            parseKeywordIf("MINUTE") ||
                            parseKeywordIf("MINUTES"))
                            if (minute == null)
                                minute = n;
                            else
                                throw exception("Minute part already defined");

                        else if (parseKeywordIf("MON") ||
                                 parseKeywordIf("MONS") ||
                                 parseKeywordIf("MONTH") ||
                                 parseKeywordIf("MONTHS"))
                            if (month == null)
                                month = n;
                            else
                                throw exception("Month part already defined");

                        break;

                    case 'S':
                        if (parseKeywordIf("S") ||
                            parseKeywordIf("SEC") ||
                            parseKeywordIf("SECS") ||
                            parseKeywordIf("SECOND") ||
                            parseKeywordIf("SECONDS"))
                            if (second == null)
                                second = n;
                            else
                                throw exception("Second part already defined");

                        break;

                    case 'Y':
                        if (parseKeywordIf("Y") ||
                            parseKeywordIf("YEAR") ||
                            parseKeywordIf("YEARS"))
                            if (year == null)
                                year = n;
                            else
                                throw exception("Year part already defined");

                        break;

                    default:
                        break p;
                }
            }
            while (!parseIf('\''));

            int months = (month == null ? 0 : month.intValue())
                       + (year  == null ? 0 : asInt((long) (year.doubleValue() * 12)));

            double seconds = (month  == null ? 0.0 : ((month.doubleValue() % 1.0) * 30 * 86400))
                           + (day    == null ? 0.0 : ((day.doubleValue() * 86400)))
                           + (hour   == null ? 0.0 : ((hour.doubleValue() * 3600)))
                           + (minute == null ? 0.0 : ((minute.doubleValue() * 60)))
                           + (second == null ? 0.0 : ((second.doubleValue())));

            return new YearToSecond(
                new YearToMonth(0, months),
                new DayToSecond(0, 0, 0, asInt((long) seconds), asInt((long) ((seconds % 1.0) * 1000000000)))
            );
        }

        position(p);
        return null;
    }

    private final boolean parseIntervalPrecisionKeywordIf(String keyword) {
        if (parseKeywordIf(keyword)) {
            if (parseIf('(')) {
                parseUnsignedIntegerLiteral();
                parse(')');
            }

            return true;
        }

        return false;
    }

    private final Interval parseIntervalLiteral() {
        Interval result = parsePostgresIntervalLiteralIf();
        if (result != null)
            return result;

        String string = parseStringLiteral();
        String message = "Illegal interval literal";

        if (parseIntervalPrecisionKeywordIf("YEAR"))
            if (parseKeywordIf("TO") && parseIntervalPrecisionKeywordIf("MONTH"))
                return requireNotNull(YearToMonth.yearToMonth(string), message);
            else
                return requireNotNull(YearToMonth.year(string), message);
        else if (parseIntervalPrecisionKeywordIf("MONTH"))
            return requireNotNull(YearToMonth.month(string), message);
        else if (parseIntervalPrecisionKeywordIf("DAY"))
            if (parseKeywordIf("TO"))
                if (parseIntervalPrecisionKeywordIf("SECOND"))
                    return requireNotNull(DayToSecond.dayToSecond(string), message);
                else if (parseIntervalPrecisionKeywordIf("MINUTE"))
                    return requireNotNull(DayToSecond.dayToMinute(string), message);
                else if (parseIntervalPrecisionKeywordIf("HOUR"))
                    return requireNotNull(DayToSecond.dayToHour(string), message);
                else
                    throw expected("HOUR", "MINUTE", "SECOND");
            else
                return requireNotNull(DayToSecond.day(string), message);
        else if (parseIntervalPrecisionKeywordIf("HOUR"))
            if (parseKeywordIf("TO"))
                if (parseIntervalPrecisionKeywordIf("SECOND"))
                    return requireNotNull(DayToSecond.hourToSecond(string), message);
                else if (parseIntervalPrecisionKeywordIf("MINUTE"))
                    return requireNotNull(DayToSecond.hourToMinute(string), message);
                else
                    throw expected("MINUTE", "SECOND");
            else
                return requireNotNull(DayToSecond.hour(string), message);
        else if (parseIntervalPrecisionKeywordIf("MINUTE"))
            if (parseKeywordIf("TO") && parseIntervalPrecisionKeywordIf("SECOND"))
                return requireNotNull(DayToSecond.minuteToSecond(string), message);
            else
                return requireNotNull(DayToSecond.minute(string), message);
        else if (parseIntervalPrecisionKeywordIf("SECOND"))
            return requireNotNull(DayToSecond.second(string), message);

        DayToSecond ds = DayToSecond.valueOf(string);
        if (ds != null)
            return ds;

        YearToMonth ym = YearToMonth.valueOf(string);

        if (ym != null)
            return ym;

        YearToSecond ys = YearToSecond.valueOf(string);
        if (ys != null)
            return ys;

        throw exception(message);
    }

    private final <T> T requireNotNull(T value, String message) {
        if (value != null)
            return value;
        else
            throw exception(message);
    }

    private final Field<?> parseFieldDateLiteralIf() {
        int p = position();

        if (parseKeywordIf("DATE")) {
            if (parseIf('(')) {
                Field<?> f = parseField();
                parse(')');
                return date((Field) f);
            }
            else if (peek('\'')) {
                return inline(parseDateLiteral());
            }






            else {
                position(p);
                return field(parseIdentifier());
            }
        }

        return null;
    }

    private final Field<?> parseFieldDateTruncIf() {
        if (parseFunctionNameIf("DATE_TRUNC", "DATETIME_TRUNC")) {
            parse('(');

            Field<?> field;
            DatePart part;

            switch (parseFamily()) {







                default:
                    part = parseDatePart();
                    parse(',');
                    field = parseField();
                    break;
            }

            parse(')');
            return trunc(field, part);
        }

        return null;
    }

    private final Field<?> parseFieldDateAddIf() {
        boolean sub = false;

        // SQL Server style
        if (parseFunctionNameIf("DATEADD")) {
            parse('(');
            DatePart part = parseDatePart();
            parse(',');
            Field<Number> interval = (Field<Number>) parseField();
            parse(',');
            Field<Date> date = (Field<Date>) parseField();
            parse(')');

            return DSL.dateAdd(date, interval, part);
        }

        // MySQL style
        else if (parseFunctionNameIf("DATE_ADD") || (sub = parseFunctionNameIf("DATE_SUB"))) {
            parse('(');
            Field<?> d = parseField();

            // [#12025] In the absence of meta data, assume TIMESTAMP
            Field<?> date = d.getDataType().isDateTime() ? d : d.coerce(TIMESTAMP);
            parse(',');

            // [#8792] TODO: Support parsing interval expressions
            Field<?> interval = parseFieldIntervalLiteralIf();
            parse(')');

            return sub ? DSL.dateSub((Field) date, (Field) interval) : DSL.dateAdd((Field) date, (Field) interval);
        }

        return null;
    }

    private final Field<?> parseFieldDateDiffIf() {
        if (parseFunctionNameIf("DATEDIFF")) {
            parse('(');
            DatePart datePart = parseDatePartIf();

            if (datePart != null)
                parse(',');

            Field<Date> d1 = (Field<Date>) parseField();

            if (parseIf(',')) {
                Field<Date> d2 = (Field<Date>) parseField();
                parse(')');

                if (datePart != null)
                    return DSL.dateDiff(datePart, d1, d2);
                else
                    return DSL.dateDiff(d1, d2);
            }

            parse(')');

            if (datePart != null)
                return DSL.dateDiff((Field) field(datePart.toName()), d1);
            else
                throw unsupportedClause();
        }













        return null;
    }

    private final Date parseDateLiteral() {
        try {
            return Date.valueOf(parseStringLiteral());
        }
        catch (IllegalArgumentException e) {
            throw exception("Illegal date literal");
        }
    }

    private final Field<?> parseFieldExtractIf() {
        if (parseFunctionNameIf("EXTRACT")) {
            parse('(');
            DatePart part = parseDatePart();
            parseKeyword("FROM");
            Field<?> field = parseField();
            parse(')');

            return extract(field, part);
        }

        return null;
    }

    private final Field<?> parseFieldDatePartIf() {
        if (parseFunctionNameIf("DATEPART")) {
            parse('(');
            DatePart part = parseDatePart();
            parse(',');
            Field<?> field = parseField();
            parse(')');

            return extract(field, part);
        }

        return null;
    }

    private final DatePart parseDatePart() {
        DatePart result = parseDatePartIf();

        if (result == null)
            throw expected("DatePart");

        return result;
    }

    private final DatePart parseDatePartIf() {
        int p = position();
        boolean string = parseIf('\'');

        DatePart result = parseDatePartIf0();

        if (result == null)
            position(p);
        else if (string)
            parse('\'');

        // [#12645] In PostgreSQL, function based indexes tend to cast the
        //          date part to a type explicitly
        if (parseIf("::"))
            parseDataType();

        return result;
    }

    private final DatePart parseDatePartIf0() {
        char character = characterUpper();

        switch (character) {
            case 'C':
                if (parseKeywordIf("CENTURY") ||
                    parseKeywordIf("CENTURIES"))
                    return DatePart.CENTURY;

                break;

            case 'D':
                if (parseKeywordIf("DAYOFYEAR") ||
                    parseKeywordIf("DAY_OF_YEAR") ||
                    parseKeywordIf("DOY") ||
                    parseKeywordIf("DY"))
                    return DatePart.DAY_OF_YEAR;
                else if (parseKeywordIf("DAY_OF_WEEK") ||
                    parseKeywordIf("DAYOFWEEK") ||
                    parseKeywordIf("DW"))
                    return DatePart.DAY_OF_WEEK;
                else if (parseKeywordIf("DAY") ||
                    parseKeywordIf("DAYS") ||
                    parseKeywordIf("DD") ||
                    parseKeywordIf("D"))
                    return DatePart.DAY;
                else if (parseKeywordIf("DECADE") ||
                    parseKeywordIf("DECADES"))
                    return DatePart.DECADE;

                break;

            case 'E':
                if (parseKeywordIf("EPOCH"))
                    return DatePart.EPOCH;

                break;

            case 'H':
                if (parseKeywordIf("HOUR") ||
                    parseKeywordIf("HOURS") ||
                    parseKeywordIf("HH"))
                    return DatePart.HOUR;

                break;

            case 'I':
                if (parseKeywordIf("ISODOW") ||
                    parseKeywordIf("ISO_DAY_OF_WEEK"))
                    return DatePart.ISO_DAY_OF_WEEK;

            case 'M':
                if (parseKeywordIf("MINUTE") ||
                    parseKeywordIf("MINUTES") ||
                    parseKeywordIf("MI"))
                    return DatePart.MINUTE;
                else if (parseKeywordIf("MILLENNIUM") ||
                    parseKeywordIf("MILLENNIUMS") ||
                    parseKeywordIf("MILLENNIA"))
                    return DatePart.MILLENNIUM;
                else if (parseKeywordIf("MICROSECOND") ||
                    parseKeywordIf("MICROSECONDS") ||
                    parseKeywordIf("MCS"))
                    return DatePart.MICROSECOND;
                else if (parseKeywordIf("MILLISECOND") ||
                    parseKeywordIf("MILLISECONDS") ||
                    parseKeywordIf("MS"))
                    return DatePart.MILLISECOND;
                else if (parseKeywordIf("MONTH") ||
                    parseKeywordIf("MONTHS") ||
                    parseKeywordIf("MM") ||
                    parseKeywordIf("M"))
                    return DatePart.MONTH;

                break;

            case 'N':
                if (parseKeywordIf("N"))
                    return DatePart.MINUTE;
                else if (parseKeywordIf("NANOSECOND") ||
                    parseKeywordIf("NANOSECONDS") ||
                    parseKeywordIf("NS"))
                    return DatePart.NANOSECOND;

                break;

            case 'Q':
                if (parseKeywordIf("QUARTER") ||
                    parseKeywordIf("QUARTERS") ||
                    parseKeywordIf("QQ") ||
                    parseKeywordIf("Q"))
                    return DatePart.QUARTER;

                break;

            case 'S':
                if (parseKeywordIf("SECOND") ||
                    parseKeywordIf("SECONDS") ||
                    parseKeywordIf("SS") ||
                    parseKeywordIf("S"))
                    return DatePart.SECOND;

                break;

            case 'T':
                if (parseKeywordIf("TIMEZONE"))
                    return DatePart.TIMEZONE;
                else if (parseKeywordIf("TIMEZONE_HOUR"))
                    return DatePart.TIMEZONE_HOUR;
                else if (parseKeywordIf("TIMEZONE_MINUTE"))
                    return DatePart.TIMEZONE_MINUTE;

                break;

            case 'W':
                if (parseKeywordIf("WEEK") ||
                    parseKeywordIf("WEEKS") ||
                    parseKeywordIf("WK") ||
                    parseKeywordIf("WW"))
                    return DatePart.WEEK;
                else if (parseKeywordIf("WEEKDAY") ||
                    parseKeywordIf("W"))
                    return DatePart.DAY_OF_WEEK;

                break;

            case 'Y':
                if (parseKeywordIf("YEAR") ||
                    parseKeywordIf("YEARS") ||
                    parseKeywordIf("YYYY") ||
                    parseKeywordIf("YY"))
                    return DatePart.YEAR;
                else if (parseKeywordIf("Y"))
                    return DatePart.DAY_OF_YEAR;

                break;
        }

        return null;
    }

    private final DatePart parseIntervalDatePart() {
        char character = characterUpper();

        switch (character) {
            case 'D':
                if (parseKeywordIf("DAY") ||
                    parseKeywordIf("DAYS"))
                    return DatePart.DAY;

                break;

            case 'H':
                if (parseKeywordIf("HOUR") ||
                    parseKeywordIf("HOURS"))
                    return DatePart.HOUR;

                break;

            case 'M':
                if (parseKeywordIf("MINUTE") ||
                    parseKeywordIf("MINUTES"))
                    return DatePart.MINUTE;
                else if (parseKeywordIf("MICROSECOND") ||
                    parseKeywordIf("MICROSECONDS"))
                    return DatePart.MICROSECOND;
                else if (parseKeywordIf("MILLISECOND") ||
                    parseKeywordIf("MILLISECONDS"))
                    return DatePart.MILLISECOND;
                else if (parseKeywordIf("MONTH") ||
                    parseKeywordIf("MONTHS"))
                    return DatePart.MONTH;

                break;

            case 'N':
                if (parseKeywordIf("NANOSECOND") ||
                    parseKeywordIf("NANOSECONDS"))
                    return DatePart.NANOSECOND;

                break;

            case 'Q':
                if (parseKeywordIf("QUARTER") ||
                    parseKeywordIf("QUARTERS"))
                    return DatePart.QUARTER;

                break;

            case 'S':
                if (parseKeywordIf("SECOND") ||
                    parseKeywordIf("SECONDS"))
                    return DatePart.SECOND;

                break;

            case 'W':
                if (parseKeywordIf("WEEK") ||
                    parseKeywordIf("WEEKS"))
                    return DatePart.WEEK;

                break;

            case 'Y':
                if (parseKeywordIf("YEAR") ||
                    parseKeywordIf("YEARS"))
                    return DatePart.YEAR;

                break;
        }

        throw expected("Interval DatePart");
    }

    private final Field<?> parseFieldConcatIf() {
        if (parseFunctionNameIf("CONCAT")) {
            parse('(');
            Field<String> result = concat(parseList(',', ParseContext::parseField).toArray(EMPTY_FIELD));
            parse(')');
            return result;
        }

        return null;
    }

    private final Field<?> parseFieldInstrIf() {
        if (parseFunctionNameIf("INSTR")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parse(',');
            Field<String> f2 = (Field) parseField();
            Field<Integer> f3 = parseIf(',') ? (Field) parseField() : null;
            parse(')');

            return f3 == null ? DSL.position(f1, f2) : DSL.position(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldCharIndexIf() {
        if (parseFunctionNameIf("CHARINDEX")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parse(',');
            Field<String> f2 = (Field) parseField();
            Field<Integer> f3 = parseIf(',') ? (Field) parseField() : null;
            parse(')');

            return f3 == null ? DSL.position(f2, f1) : DSL.position(f2, f1, f3);
        }

        return null;
    }

    private final Field<?> parseFieldLpadIf() {
        if (parseFunctionNameIf("LPAD")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parse(',');
            Field<Integer> f2 = (Field) parseField();
            Field<String> f3 = parseIf(',') ? (Field) parseField() : null;
            parse(')');

            return f3 == null ? lpad(f1, f2) : lpad(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldRpadIf() {
        if (parseFunctionNameIf("RPAD")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parse(',');
            Field<Integer> f2 = (Field) parseField();
            Field<String> f3 = parseIf(',') ? (Field) parseField() : null;
            parse(')');

            return f3 == null ? rpad(f1, f2) : rpad(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldInsertIf() {
        if (parseFunctionNameIf("INSERT")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parse(',');
            Field<Number> f2 = (Field) parseField();
            parse(',');
            Field<Number> f3 = (Field) parseField();
            parse(',');
            Field<String> f4 = (Field) parseField();
            parse(')');

            return insert(f1, f2, f3, f4);
        }

        return null;
    }

    private final Field<?> parseFieldOverlayIf() {
        if (parseFunctionNameIf("OVERLAY")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parseKeyword("PLACING");
            Field<String> f2 = (Field) parseField();
            parseKeyword("FROM");
            Field<Number> f3 = (Field) parseField();
            Field<Number> f4 =
                parseKeywordIf("FOR")
              ? (Field) parseField()




              : null;
            parse(')');

            return f4 == null ? overlay(f1, f2, f3) : overlay(f1, f2, f3, f4);
        }

        return null;
    }

    private final Field<?> parseFieldPositionIf() {
        if (parseFunctionNameIf("POSITION")) {
            parse('(');
            forbidden.add(FK_IN);
            Field<String> f1 = (Field) parseField();
            parseKeyword("IN");
            forbidden.remove(FK_IN);
            Field<String> f2 = (Field) parseField();
            parse(')');
            return DSL.position(f2, f1);
        }

        return null;
    }

    private final Field<?> parseFieldLocateIf() {
        boolean locate = parseFunctionNameIf("LOCATE");
        if (locate || parseFunctionNameIf("LOCATE_IN_STRING")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parse(',');
            Field<String> f2 = (Field) parseField();
            Field<Integer> f3 = (Field) (parseIf(',') ? parseField() : null);
            parse(')');












            if (locate)
                return f3 == null ? DSL.position(f2, f1) : DSL.position(f2, f1, f3);
            else
                return f3 == null ? DSL.position(f1, f2) : DSL.position(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldRepeatIf() {
        if (parseFunctionNameIf("REPEAT", "REPLICATE")) {
            parse('(');
            Field<String> field = (Field) parseField();
            parse(',');
            Field<Integer> count = (Field) parseField();
            parse(')');
            return DSL.repeat(field, count);
        }

        return null;
    }

    private final Field<?> parseFieldReplaceIf() {
        if (parseFunctionNameIf("REPLACE", "OREPLACE", "STR_REPLACE")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parse(',');
            Field<String> f2 = (Field) parseField();
            Field<String> f3 = parseIf(',')
                ? (Field) parseField()
                : null;
            parse(')');
            return f3 == null
                ? replace(f1, f2)
                : replace(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldRegexpReplaceIf() {
        boolean all = parseFunctionNameIf("REGEXP_REPLACE_ALL");
        boolean first = !all && parseFunctionNameIf("REGEXP_REPLACE_FIRST");
        boolean ifx = !all && !first && parseFunctionNameIf("REGEX_REPLACE");

        if (all || first || ifx || parseFunctionNameIf("REGEXP_REPLACE")) {
            parse('(');
            Field field = parseField();
            parse(',');
            Field pattern = parseField();
            Field replacement = parseIf(',') ? parseField() : null;
            Long i1;
            Long i2;

            if (replacement == null) {
                replacement = inline("");
            }
            else if (ifx) {
                if (parseIf(','))
                    if (1L == parseUnsignedIntegerLiteral())
                        first = true;
                    else
                        throw expected("Only a limit of 1 is currently supported");
            }
            else if (!all && !first) {
                if (parseIf(',')) {
                    String s = parseStringLiteralIf();

                    if (s != null) {
                        if (s.contains("g"))
                            all = true;
                    }
                    else {
                        i1 = parseUnsignedIntegerLiteral();
                        parse(',');
                        i2 = parseUnsignedIntegerLiteral();

                        if (Long.valueOf(1L).equals(i1) && Long.valueOf(1L).equals(i2))
                            all = true;
                        else
                            throw expected("Only start and occurence values of 1 are currently supported");
                    }
                }

                if (!all) switch (parseFamily()) {



                    case POSTGRES:
                        first = true;
                        break;
                }
            }

            parse(')');
            return first
                ? regexpReplaceFirst(field, pattern, replacement)
                : regexpReplaceAll(field, pattern, replacement);
        }
        else if (parseFunctionNameIf("REPLACE_REGEXPR")) {
            parse('(');
            forbidden.add(FK_IN);
            Field pattern = parseField();
            parseKeyword("IN");
            forbidden.remove(FK_IN);
            Field field = parseField();
            Field replacement = parseKeywordIf("WITH") ? parseField() : inline("");
            first = parseKeywordIf("OCCURRENCE") && !parseKeywordIf("ALL") && parse("1");

            parse(')');
            return first
                ? regexpReplaceFirst(field, pattern, replacement)
                : regexpReplaceAll(field, pattern, replacement);
        }

        return null;
    }

    private final Field<?> parseFieldSubstringIf() {
        boolean substring = parseFunctionNameIf("SUBSTRING");
        boolean substr = !substring && parseFunctionNameIf("SUBSTR");

        if (substring || substr) {
            boolean keywords = !substr;
            parse('(');
            Field<String> f1 = (Field) parseField();
            if (substr || !(keywords = parseKeywordIf("FROM")))
                parse(',');
            Field f2 = toField(parseNumericOp());
            Field f3 =
                    ((keywords && parseKeywordIf("FOR")) || (!keywords && parseIf(',')))
                ? (Field) toField(parseNumericOp())
                : null;
            parse(')');

            return f3 == null
                ? DSL.substring(f1, f2)
                : DSL.substring(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldSubstringIndexIf() {
        if (parseFunctionNameIf("SUBSTRING_INDEX")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parse(',');
            Field<String> f2 = (Field) parseField();
            parse(',');
            Field<Integer> f3 = (Field) parseField();
            parse(')');

            return substringIndex(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldTrimIf() {
        if (parseFunctionNameIf("TRIM")) {
            parse('(');
            int p = position();

            boolean leading = parseKeywordIf("LEADING") || parseKeywordIf("L");
            boolean trailing = !leading && (parseKeywordIf("TRAILING") || parseKeywordIf("T"));
            boolean both = !leading && !trailing && (parseKeywordIf("BOTH") || parseKeywordIf("B"));

            if (leading || trailing || both) {
                if (parseIf(',')) {
                    position(p);
                }
                else if (parseIf(')')) {
                    position(p);
                }
                else if (parseKeywordIf("FROM")) {
                    Field<String> f = (Field) parseField();
                    parse(')');

                    return leading ? ltrim(f)
                         : trailing ? rtrim(f)
                         : trim(f);
                }
            }

            Field<String> f1 = (Field) parseField();

            if (parseKeywordIf("FROM")) {
                Field<String> f2 = (Field) parseField();
                parse(')');

                return leading ? ltrim(f2, f1)
                     : trailing ? rtrim(f2, f1)
                     : trim(f2, f1);
            }
            else {
                Field<String> f2 = parseIf(',') ? (Field) parseField() : null;
                parse(')');

                return f2 == null ? trim(f1) : trim(f1, f2);
            }
        }

        return null;
    }

    private final Field<?> parseFieldTranslateIf() {
        if (parseFunctionNameIf("TRANSLATE", "OTRANSLATE")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parse(',');
            Field<String> f2 = (Field) parseField();
            parse(',');
            Field<String> f3 = (Field) parseField();
            parse(')');






            return translate(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldToCharIf() {
        if (parseFunctionNameIf("TO_CHAR")) {
            parse('(');
            Field<?> f1 = parseField();
            Field<String> f2 = (Field) (parseIf(',') ? parseField() : null);
            parse(')');

            return f2 == null ? toChar(f1) : toChar(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldToNumberIf() {
        if (parseFunctionNameIf("TO_NUMBER")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parse(')');
            return cast(f1, SQLDataType.NUMERIC);
        }

        return null;
    }

    private final Field<?> parseFieldToDateIf() {
        if (parseFunctionNameIf("TO_DATE")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            Field<String> f2 = parseIf(',') ? (Field) parseField() : inline(settings().getParseDateFormat());
            parse(')');

            return toDate(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldToTimestampIf() {
        if (parseFunctionNameIf("TO_TIMESTAMP")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            Field<String> f2 = parseIf(',') ? (Field) parseField() : inline(settings().getParseTimestampFormat());
            parse(')');

            return toTimestamp(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldTimestampDiffIf() {
        if (parseFunctionNameIf("TIMESTAMPDIFF")) {
            parse('(');
            Field<Timestamp> ts1 = (Field<Timestamp>) parseField();
            parse(',');
            Field<Timestamp> ts2 = (Field<Timestamp>) parseField();
            parse(')');

            return DSL.timestampDiff(ts1, ts2);
        }

        return null;
    }

    private final Field<?> parseFieldRtrimIf() {
        if (parseFunctionNameIf("RTRIM")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            Field<String> f2 = parseIf(',') ? (Field) parseField() : null;
            parse(')');

            return f2 == null ? rtrim(f1) : rtrim(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldLtrimIf() {
        if (parseFunctionNameIf("LTRIM")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            Field<String> f2 = parseIf(',') ? (Field) parseField() : null;
            parse(')');

            return f2 == null ? ltrim(f1) : ltrim(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldMidIf() {
        if (parseFunctionNameIf("MID")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parse(',');
            Field<? extends Number> f2 = (Field) parseField();
            parse(',');
            Field<? extends Number> f3 = (Field) parseField();
            parse(')');
            return mid(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldLeftIf() {
        if (parseFunctionNameIf("LEFT")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parse(',');
            Field<? extends Number> f2 = (Field) parseField();
            parse(')');
            return left(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldRightIf() {
        if (parseFunctionNameIf("RIGHT")) {
            parse('(');
            Field<String> f1 = (Field) parseField();
            parse(',');
            Field<? extends Number> f2 = (Field) parseField();
            parse(')');
            return right(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldDecodeIf() {
        if (parseFunctionNameIf("DECODE", "MAP")) {
            parse('(');
            List<Field<?>> fields = parseList(',', ParseContext::parseField);
            int size = fields.size();
            if (size < 3)
                throw expected("At least three arguments to DECODE()");

            parse(')');
            return DSL.decode(
                (Field<Object>)   fields.get(0),
                (Field<Object>)   fields.get(1),
                (Field<Object>)   fields.get(2),
                (Field<Object>[]) (size == 3 ? EMPTY_FIELD : fields.subList(3, size).toArray(EMPTY_FIELD))
            );
        }

        return null;
    }

    private final Field<?> parseFieldChooseIf() {
        if (parseFunctionNameIf("CHOOSE")) {
            parse('(');
            Field<Integer> index = (Field<Integer>) parseField();
            parse(',');
            List<Field<?>> fields = parseList(',', ParseContext::parseField);
            parse(')');

            return DSL.choose(index, fields.toArray(EMPTY_FIELD));
        }

        return null;
    }

    private final Field<?> parseFieldIfnullIf() {
        if (parseFunctionNameIf("IFNULL")) {
            parse('(');
            Field<?> f1 = parseField();
            parse(',');
            Field<?> f2 = parseField();
            parse(')');

            return ifnull(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldIsnullIf() {
        if (parseFunctionNameIf("ISNULL")) {
            parse('(');
            Field<?> f1 = parseField();
            Field<?> f2 = parseIf(',') ? parseField() : null;
            parse(')');

            return f2 != null ? isnull(f1, f2) : field(f1.isNull());
        }

        return null;
    }

    private final Field<?> parseFieldIfIf() {
        if (parseFunctionNameIf("IF", "IIF")) {
            parse('(');
            Condition c = parseCondition();
            parse(',');
            Field<?> f1 = parseField();
            parse(',');
            Field<?> f2 = parseField();
            parse(')');

            return iif(c, f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldNvlIf() {
        if (parseFunctionNameIf("NVL")) {
            parse('(');
            Field<?> f1 = parseField();
            parse(',');
            Field<?> f2 = parseField();
            parse(')');

            return nvl(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldNvl2If() {
        if (parseFunctionNameIf("NVL2")) {
            parse('(');
            Field<?> f1 = parseField();
            parse(',');
            Field<?> f2 = parseField();
            parse(',');
            Field<?> f3 = parseField();
            parse(')');

            return nvl2(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldNullifIf() {
        if (parseFunctionNameIf("NULLIF")) {
            parse('(');
            Field<?> f1 = parseField();
            parse(',');
            Field<?> f2 = parseField();
            parse(')');

            return nullif(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldCoalesceIf() {
        if (parseFunctionNameIf("COALESCE")) {
            parse('(');
            List<Field<?>> fields = parseList(',', ParseContext::parseField);
            parse(')');

            Field[] a = EMPTY_FIELD;
            return coalesce(fields.get(0), fields.size() == 1 ? a : fields.subList(1, fields.size()).toArray(a));
        }

        return null;
    }

    private final <T> Field<?> parseFieldFieldIf() {
        if (parseFunctionNameIf("FIELD")) {
            parse('(');
            Field<?> f1 = parseField();
            parse(',');
            List<Field<?>> f2 = parseList(',', ParseContext::parseField);
            parse(')');

            return DSL.field((Field<T>) f1, (Field<T>[]) f2.toArray(EMPTY_FIELD));
        }

        return null;
    }

    private final Field<?> parseFieldCaseIf() {
        if (parseKeywordIf("CASE")) {
            if (parseKeywordIf("WHEN")) {
                CaseConditionStep step = null;
                Field result;

                do {
                    Condition condition = parseCondition();
                    parseKeyword("THEN");
                    Field value = parseField();
                    step = step == null ? when(condition, value) : step.when(condition, value);
                }
                while (parseKeywordIf("WHEN"));

                if (parseKeywordIf("ELSE"))
                    result = step.otherwise(parseField());
                else
                    result = step;

                parseKeyword("END");
                return result;
            }
            else {
                CaseValueStep init = choose(parseField());
                CaseWhenStep step = null;
                Field result;
                parseKeyword("WHEN");

                do {
                    Field when = parseField();
                    parseKeyword("THEN");
                    Field then = parseField();
                    step = step == null ? init.when(when, then) : step.when(when, then);
                }
                while (parseKeywordIf("WHEN"));

                if (parseKeywordIf("ELSE"))
                    result = step.otherwise(parseField());
                else
                    result = step;

                parseKeyword("END");
                return result;
            }
        }

        return null;
    }

    private final Field<?> parseFieldCastIf() {
        boolean cast = parseFunctionNameIf("CAST");
        boolean coerce = !cast && parseFunctionNameIf("COERCE");

        if (cast || coerce) {
            parse('(');
            Field<?> field = parseField();
            parseKeyword("AS");
            DataType<?> type = parseCastDataType();
            parse(')');

            return cast ? cast(field, type) : coerce(field, type);
        }

        return null;
    }

    private final Field<?> parseFieldConvertIf() {
        if (parseFunctionNameIf("CONVERT")) {
            parse('(');
            DataType<?> type = parseDataType();
            parse(',');
            Field<?> field = parseField();
            Long style = null;
            if (parseIf(',') && requireProEdition())
                style = parseUnsignedIntegerLiteral();
            parse(')');

            if (style == null)
                return cast(field, type);




        }

        return null;
    }

    private final Field<Boolean> parseBooleanValueExpressionIf() {
        TruthValue truth = parseTruthValueIf();

        if (truth != null) {
            switch (truth) {
                case T_TRUE:
                    return inline(true);
                case T_FALSE:
                    return inline(false);
                case T_NULL:
                    return inline((Boolean) null);
                default:
                    throw exception("Truth value not supported: " + truth);
            }
        }

        return null;
    }

    private final Field<?> parseAggregateFunctionIf() {
        return parseAggregateFunctionIf(false);
    }

    private final Field<?> parseAggregateFunctionIf(boolean basic) {
        return parseAggregateFunctionIf(basic, null);
    }

    private final Field<?> parseAggregateFunctionIf(boolean basic, AggregateFunction<?> f) {
        AggregateFunction<?> agg = null;
        AggregateFilterStep<?> filter = null;
        WindowBeforeOverStep<?> over = null;
        Object keep = null;
        Field<?> result = null;
        Condition condition = null;

        keep = over = filter = agg = f != null ? f : parseCountIf();
        if (filter == null) {
            Field<?> field = parseGeneralSetFunctionIf();

            if (field != null && !(field instanceof AggregateFunction))
                return field;

            keep = over = filter = agg = (AggregateFunction<?>) field;
        }

        if (filter == null && !basic)
            over = filter = agg = parseBinarySetFunctionIf();
        if (filter == null && !basic)
            over = filter = parseOrderedSetFunctionIf();
        if (filter == null && !basic)
            over = filter = parseArrayAggFunctionIf();
        if (filter == null && !basic)
            over = filter = parseMultisetAggFunctionIf();
        if (filter == null && !basic)
            over = filter = parseXMLAggFunctionIf();
        if (filter == null && !basic)
            over = filter = parseJSONArrayAggFunctionIf();
        if (filter == null && !basic)
            over = filter = parseJSONObjectAggFunctionIf();
        if (filter == null)
            over = parseCountIfIf();

        if (filter == null && over == null)
            if (!basic)
                return parseSpecialAggregateFunctionIf();
            else
                return null;

        if (keep != null && filter != null && !basic && parseKeywordIf("KEEP")) {
            requireProEdition();


















        }
        else if (filter != null && !basic && parseKeywordIf("FILTER")) {
            parse('(');
            parseKeyword("WHERE");
            condition = parseCondition();
            parse(')');

            result = over = filter.filterWhere(condition);
        }
        else if (filter != null)
            result = filter;
        else
            result = over;

        if (!basic && parseKeywordIf("OVER")) {
            Object nameOrSpecification = parseWindowNameOrSpecification(filter != null);

            if (nameOrSpecification instanceof Name)
                result = over.over((Name) nameOrSpecification);
            else if (nameOrSpecification instanceof WindowSpecification)
                result = over.over((WindowSpecification) nameOrSpecification);
            else
                result = over.over();
        }

        return result;
    }

    private final Field<?> parseSpecialAggregateFunctionIf() {
        if (parseFunctionNameIf("GROUP_CONCAT")) {
            parse('(');

            GroupConcatOrderByStep s1;
            GroupConcatSeparatorStep s2;
            AggregateFunction<String> s3;

            if (parseKeywordIf("DISTINCT"))
                s1 = DSL.groupConcatDistinct(parseField());
            else
                s1 = DSL.groupConcat(parseField());

            if (parseKeywordIf("ORDER BY"))
                s2 = s1.orderBy(parseList(',', ParseContext::parseSortField));
            else
                s2 = s1;

            if (parseKeywordIf("SEPARATOR"))
                s3 = s2.separator(parseStringLiteral());
            else
                s3 = s2;

            parse(')');
            return s3;
        }

        return null;
    }

    private final Object parseWindowNameOrSpecification(boolean orderByAllowed) {
        Object result;

        if (parseIf('(')) {
            result = parseWindowSpecificationIf(null, orderByAllowed);
            parse(')');
        }
        else {
            result = parseIdentifier();
        }

        return result;
    }

    private final Field<?> parseFieldRankIf() {
        if (parseFunctionNameIf("RANK")) {
            parse('(');

            if (parseIf(')'))
                return parseWindowFunction(null, null, rank());

            // Hypothetical set function
            List<Field<?>> args = parseList(',', ParseContext::parseField);
            parse(')');
            return rank(args).withinGroupOrderBy(parseWithinGroupN());
        }

        return null;
    }

    private final Field<?> parseFieldDenseRankIf() {
        if (parseFunctionNameIf("DENSE_RANK")) {
            parse('(');

            if (parseIf(')'))
                return parseWindowFunction(null, null, denseRank());

            // Hypothetical set function
            List<Field<?>> args = parseList(',', ParseContext::parseField);
            parse(')');
            return denseRank(args).withinGroupOrderBy(parseWithinGroupN());
        }

        return null;
    }

    private final Field<?> parseFieldPercentRankIf() {
        if (parseFunctionNameIf("PERCENT_RANK")) {
            parse('(');

            if (parseIf(')'))
                return parseWindowFunction(null, null, percentRank());

            // Hypothetical set function
            List<Field<?>> args = parseList(',', ParseContext::parseField);
            parse(')');
            return percentRank(args).withinGroupOrderBy(parseWithinGroupN());
        }

        return null;
    }

    private final Field<?> parseFieldCumeDistIf() {
        if (parseFunctionNameIf("CUME_DIST")) {
            parse('(');

            if (parseIf(')'))
                return parseWindowFunction(null, null, cumeDist());

            // Hypothetical set function
            List<Field<?>> args = parseList(',', ParseContext::parseField);
            parse(')');
            return cumeDist(args).withinGroupOrderBy(parseWithinGroupN());
        }

        return null;
    }

    private final Field<?> parseFieldRandIf() {
        if (parseFunctionNameIf("RAND", "RANDOM")) {
            parse('(');
            parse(')');
            return rand();
        }

        return null;
    }

    private final Field<?> parseFieldRatioToReportIf() {
        if (parseFunctionNameIf("RATIO_TO_REPORT")) {
            parse('(');
            Field<Number> field = (Field<Number>) parseField();
            parse(')');
            return parseWindowFunction(null, null, ratioToReport(field));
        }

        return null;
    }

    private final Field<?> parseFieldRowNumberIf() {
        if (parseFunctionNameIf("ROW_NUMBER")) {
            parse('(');
            parse(')');
            return parseWindowFunction(null, null, rowNumber());
        }

        return null;
    }

    private final Field<?> parseFieldNtileIf() {
        if (parseFunctionNameIf("NTILE")) {
            parse('(');
            int number = asInt(parseUnsignedIntegerLiteral());
            parse(')');
            return parseWindowFunction(null, null, ntile(number));
        }

        return null;
    }

    private final Field<?> parseFieldLeadLagIf() {
        boolean lead = parseFunctionNameIf("LEAD");
        boolean lag = !lead && parseFunctionNameIf("LAG");

        if (lead || lag) {
            parse('(');
            Field<Void> f1 = (Field) parseField();
            Integer f2 = null;
            Field<Void> f3 = null;

            if (parseIf(',')) {
                f2 = asInt(parseUnsignedIntegerLiteral());

                if (parseIf(','))
                    f3 = (Field) parseField();
            }

            WindowIgnoreNullsStep s1 = lead
                ? f2 == null
                    ? lead(f1)
                    : f3 == null
                        ? lead(f1, f2)
                        : lead(f1, f2, f3)
                : f2 == null
                    ? lag(f1)
                    : f3 == null
                        ? lag(f1, f2)
                        : lag(f1, f2, f3);

            WindowOverStep<?> s2 = parseWindowRespectIgnoreNulls(s1, s1);
            parse(')');
            return parseWindowFunction(null, s1, s2);
        }

        return null;
    }

    private final Field<?> parseFieldFirstValueIf() {
        if (parseFunctionNameIf("FIRST_VALUE")) {
            parse('(');
            Field<Void> arg = (Field) parseField();
            WindowIgnoreNullsStep<Void> s1 = firstValue(arg);
            WindowOverStep<?> s2 = parseWindowRespectIgnoreNulls(s1, s1);
            parse(')');
            return parseWindowFunction(null, s1, s2);
        }

        return null;
    }

    private final Field<?> parseFieldLastValueIf() {
        if (parseFunctionNameIf("LAST_VALUE")) {
            parse('(');
            Field<Void> arg = (Field) parseField();
            WindowIgnoreNullsStep<Void> s1 = lastValue(arg);
            WindowOverStep<?> s2 = parseWindowRespectIgnoreNulls(s1, s1);
            parse(')');
            return parseWindowFunction(null, s1, s2);
        }

        return null;
    }

    private final Field<?> parseFieldNthValueIf() {
        if (parseFunctionNameIf("NTH_VALUE")) {
            parse('(');
            Field<?> f1 = parseField();
            parse(',');
            Field<?> f2 = parseField();
            WindowFromFirstLastStep<?> s1 = nthValue(f1, (Field) f2);
            WindowIgnoreNullsStep s2 = parseWindowFromFirstLast(s1, s1);
            WindowOverStep<?> s3 = parseWindowRespectIgnoreNulls(s2, s2);
            parse(')');
            return parseWindowFunction(s1, s2, s3);
        }

        return null;
    }

    private final Field<?> parseWindowFunction(WindowFromFirstLastStep s1, WindowIgnoreNullsStep s2, WindowOverStep<?> s3) {
        s2 = parseWindowFromFirstLast(s1, s2);
        s3 = parseWindowRespectIgnoreNulls(s2, s3);

        parseKeyword("OVER");
        Object nameOrSpecification = parseWindowNameOrSpecification(true);

        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=494897
        Field<?> result = (nameOrSpecification instanceof Name)
            ? s3.over((Name) nameOrSpecification)
            : (nameOrSpecification instanceof WindowSpecification)
            ? s3.over((WindowSpecification) nameOrSpecification)
            : s3.over();

        return result;
    }

    private final WindowOverStep<?> parseWindowRespectIgnoreNulls(WindowIgnoreNullsStep s2, WindowOverStep<?> s3) {
        if (s2 != null)
            if (parseKeywordIf("RESPECT NULLS"))
                s3 = s2.respectNulls();
            else if (parseKeywordIf("IGNORE NULLS"))
                s3 = s2.ignoreNulls();
            else
                s3 = s2;

        return s3;
    }

    private final WindowIgnoreNullsStep parseWindowFromFirstLast(WindowFromFirstLastStep s1, WindowIgnoreNullsStep s2) {
        if (s1 != null)
            if (parseKeywordIf("FROM FIRST"))
                s2 = s1.fromFirst();
            else if (parseKeywordIf("FROM LAST"))
                s2 = s1.fromLast();
            else
                s2 = s1;

        return s2;
    }

    private final AggregateFunction<?> parseBinarySetFunctionIf() {
        switch (characterUpper()) {
            case 'C':
                if (parseFunctionNameIf("CORR"))
                    return parseBindarySetFunction(DSL::corr);
                else if (parseFunctionNameIf("COVAR_POP"))
                    return parseBindarySetFunction(DSL::covarPop);
                else if (parseFunctionNameIf("COVAR_SAMP"))
                    return parseBindarySetFunction(DSL::covarSamp);

                break;

            case 'R':
                if (parseFunctionNameIf("REGR_AVGX"))
                    return parseBindarySetFunction(DSL::regrAvgX);
                else if (parseFunctionNameIf("REGR_AVGY"))
                    return parseBindarySetFunction(DSL::regrAvgY);
                else if (parseFunctionNameIf("REGR_COUNT"))
                    return parseBindarySetFunction(DSL::regrCount);
                else if (parseFunctionNameIf("REGR_INTERCEPT"))
                    return parseBindarySetFunction(DSL::regrIntercept);
                else if (parseFunctionNameIf("REGR_R2"))
                    return parseBindarySetFunction(DSL::regrR2);
                else if (parseFunctionNameIf("REGR_SLOPE"))
                    return parseBindarySetFunction(DSL::regrSlope);
                else if (parseFunctionNameIf("REGR_SXX"))
                    return parseBindarySetFunction(DSL::regrSXX);
                else if (parseFunctionNameIf("REGR_SXY"))
                    return parseBindarySetFunction(DSL::regrSXY);
                else if (parseFunctionNameIf("REGR_SYY"))
                    return parseBindarySetFunction(DSL::regrSYY);

                break;
        }

        return null;
    }

    private final AggregateFunction<?> parseBindarySetFunction(BiFunction<? super Field<? extends Number>, ? super Field<? extends Number>, ? extends AggregateFunction<?>> function) {
        parse('(');
        Field<? extends Number> arg1 = (Field) parseField();
        parse(',');
        Field<? extends Number> arg2 = (Field) parseField();
        parse(')');

        return function.apply(arg1, arg2);
    }

    private final AggregateFilterStep<?> parseOrderedSetFunctionIf() {
        // TODO Listagg set function
        OrderedAggregateFunction<?> orderedN;
        OrderedAggregateFunctionOfDeferredType ordered1;
        boolean optionalWithinGroup = false;

        orderedN = parseHypotheticalSetFunctionIf();
        if (orderedN == null)
            orderedN = parseInverseDistributionFunctionIf();
        if (orderedN == null)
            optionalWithinGroup = (orderedN = parseListaggFunctionIf()) != null;
        if (orderedN != null)
            return orderedN.withinGroupOrderBy(parseWithinGroupN(optionalWithinGroup));

        ordered1 = parseModeIf();
        if (ordered1 != null)
            return ordered1.withinGroupOrderBy(parseWithinGroup1());

        return null;
    }

    private final AggregateFilterStep<?> parseArrayAggFunctionIf() {
        if (parseKeywordIf("ARRAY_AGG")) {
            parse('(');

            boolean distinct = parseKeywordIf("DISTINCT");
            Field<?> a1 = parseField();
            List<SortField<?>> sort = null;

            if (parseKeywordIf("ORDER BY"))
                sort = parseList(',', ParseContext::parseSortField);

            parse(')');

            ArrayAggOrderByStep<?> s1 = distinct
                ? arrayAggDistinct(a1)
                : arrayAgg(a1);

            return sort == null ? s1 : s1.orderBy(sort);
        }

        return null;
    }

    private final AggregateFilterStep<?> parseMultisetAggFunctionIf() {
        if (parseKeywordIf("MULTISET_AGG")) {
            parse('(');

            List<Field<?>> fields = parseList(',', ParseContext::parseField);
            List<SortField<?>> sort = null;

            if (parseKeywordIf("ORDER BY"))
                sort = parseList(',', ParseContext::parseSortField);

            parse(')');
            ArrayAggOrderByStep<?> s1 = multisetAgg(fields);
            return sort == null ? s1 : s1.orderBy(sort);
        }

        return null;
    }

    private final List<SortField<?>> parseWithinGroupN() {
        return parseWithinGroupN(false);
    }

    private final List<SortField<?>> parseWithinGroupN(boolean optional) {
        if (optional) {
            if (!parseKeywordIf("WITHIN GROUP"))
                return emptyList();
        }
        else
            parseKeyword("WITHIN GROUP");

        parse('(');
        parseKeyword("ORDER BY");
        List<SortField<?>> result = parseList(',', ParseContext::parseSortField);
        parse(')');
        return result;
    }

    private final SortField<?> parseWithinGroup1() {
        parseKeyword("WITHIN GROUP");
        parse('(');
        parseKeyword("ORDER BY");
        SortField<?> result = parseSortField();
        parse(')');
        return result;
    }

    private final OrderedAggregateFunction<?> parseHypotheticalSetFunctionIf() {

        // This currently never parses hypothetical set functions, as the function names are already
        // consumed earlier in parseFieldTerm(). We should implement backtracking...
        OrderedAggregateFunction<?> ordered;

        if (parseFunctionNameIf("RANK")) {
            parse('(');
            ordered = rank(parseList(',', ParseContext::parseField));
            parse(')');
        }
        else if (parseFunctionNameIf("DENSE_RANK")) {
            parse('(');
            ordered = denseRank(parseList(',', ParseContext::parseField));
            parse(')');
        }
        else if (parseFunctionNameIf("PERCENT_RANK")) {
            parse('(');
            ordered = percentRank(parseList(',', ParseContext::parseField));
            parse(')');
        }
        else if (parseFunctionNameIf("CUME_DIST")) {
            parse('(');
            ordered = cumeDist(parseList(',', ParseContext::parseField));
            parse(')');
        }
        else
            ordered = null;

        return ordered;
    }

    private final OrderedAggregateFunction<BigDecimal> parseInverseDistributionFunctionIf() {
        OrderedAggregateFunction<BigDecimal> ordered;

        if (parseFunctionNameIf("PERCENTILE_CONT")) {
            parse('(');
            ordered = percentileCont((Field) parseField());
            parse(')');
        }
        else if (parseFunctionNameIf("PERCENTILE_DISC")) {
            parse('(');
            ordered = percentileDisc((Field) parseField());
            parse(')');
        }
        else
            ordered = null;

        return ordered;
    }

    private final OrderedAggregateFunction<String> parseListaggFunctionIf() {
        OrderedAggregateFunction<String> ordered;

        if (parseFunctionNameIf("LISTAGG")) {
            parse('(');
            Field<?> field = parseField();

            if (parseIf(','))
                ordered = listAgg(field, parseStringLiteral());
            else
                ordered = listAgg(field);

            parse(')');
        }
        else
            ordered = null;

        return ordered;
    }

    private final OrderedAggregateFunctionOfDeferredType parseModeIf() {
        OrderedAggregateFunctionOfDeferredType ordered;

        if (parseFunctionNameIf("MODE")) {
            parse('(');
            parse(')');
            ordered = mode();
        }
        else
            ordered = null;

        return ordered;
    }

    private final Field<?> parseGeneralSetFunctionIf() {
        boolean distinct;
        Field arg;
        ComputationalOperation operation = parseComputationalOperationIf();

        if (operation == null)
            return null;

        parse('(');

        switch (operation) {
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case PRODUCT:
                distinct = parseSetQuantifier();
                break;
            default:
                distinct = false;
                break;
        }

        arg = parseField();

        switch (operation) {
            case MAX:
            case MIN: {
                if (!distinct && parseIf(',')) {
                    List<Field<?>> fields = parseList(',', ParseContext::parseField);
                    parse(')');

                    return operation == ComputationalOperation.MAX ? greatest(arg, fields.toArray(EMPTY_FIELD)) : least(arg, fields.toArray(EMPTY_FIELD));
                }
            }
        }

        parse(')');

        switch (operation) {
            case ANY_VALUE:
                return anyValue(arg);
            case AVG:
                return distinct ? avgDistinct(arg) : avg(arg);
            case MAX:
                return distinct ? maxDistinct(arg) : max(arg);
            case MIN:
                return distinct ? minDistinct(arg) : min(arg);
            case SUM:
                return distinct ? sumDistinct(arg) : sum(arg);
            case PRODUCT:
                return distinct ? productDistinct(arg) : product(arg);
            case MEDIAN:
                return median(arg);
            case EVERY:
                return every(arg);
            case ANY:
                return boolOr(arg);
            case STDDEV_POP:
                return stddevPop(arg);
            case STDDEV_SAMP:
                return stddevSamp(arg);
            case VAR_POP:
                return varPop(arg);
            case VAR_SAMP:
                return varSamp(arg);

            default:
                throw exception("Unsupported computational operation");
        }
    }

    private final AggregateFunction<?> parseCountIf() {
        if (parseFunctionNameIf("COUNT")) {
            parse('(');
            boolean distinct = parseSetQuantifier();

            if (parseIf('*') && parse(')'))
                if (distinct)
                    return countDistinct(asterisk());
                else
                    return count();

            Field<?>[] fields = null;
            QualifiedAsterisk asterisk = null;
            Row row = parseRowIf();
            if (row != null)
                fields = row.fields();
            else if ((asterisk = parseQualifiedAsteriskIf()) == null)
                fields = distinct
                        ? parseList(',', ParseContext::parseField).toArray(EMPTY_FIELD)
                        : new Field[] { parseField() };

            parse(')');

            if (distinct)
                if (fields == null)
                    return countDistinct(asterisk);
                else if (fields.length == 1)
                    return countDistinct(fields[0]);
                else
                    return countDistinct(fields);
            else if (fields == null)
                return count(asterisk);
            else
                return count(fields[0]);
        }

        return null;
    }

    private final WindowBeforeOverStep<Integer> parseCountIfIf() {
        if (parseFunctionNameIf("COUNTIF", "COUNT_IF")) {
            parse('(');
            Condition condition = parseCondition();
            parse(')');
            return count().filterWhere(condition);
        }

        return null;
    }

    private final boolean parseSetQuantifier() {
        boolean distinct = parseKeywordIf("DISTINCT");
        if (!distinct)
            parseKeywordIf("ALL");
        return distinct;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Name parsing
    // -----------------------------------------------------------------------------------------------------------------

    private final Domain<?> parseDomainName() {
        return domain(parseName());
    }

    private final Catalog parseCatalogName() {
        return catalog(parseName());
    }

    private final Schema parseSchemaName() {
        return schema(parseName());
    }

    private final Table<?> parseTableName() {
        return lookupTable(position(), parseName());
    }

    private final Table<?> parseTableNameIf() {
        int positionBeforeName = position();
        Name name = parseNameIf();

        if (name == null)
            return null;

        return lookupTable(positionBeforeName, name);
    }

    private final Field<?> parseFieldNameOrSequenceExpression() {
        int positionBeforeName = position();
        Name name = parseName();

        if (name.qualified()) {
            String last = name.last();

            if ("NEXTVAL".equalsIgnoreCase(last))
                return sequence(name.qualifier()).nextval();
            else if ("CURRVAL".equalsIgnoreCase(last))
                return sequence(name.qualifier()).currval();
        }

        unknownFunctions:
        if (dsl.settings().getParseUnknownFunctions() == ParseUnknownFunctions.IGNORE && peek('(') && !peekTokens('(', '+', ')')) {
            int p = position();
            List<Field<?>> arguments;

            parse('(');
            if (!parseIf(')')) {













                arguments = parseList(',', ParseContext::parseField);
                parse(')');
            }
            else
                arguments = new ArrayList<>();

            return function(name, Object.class, arguments.toArray(EMPTY_FIELD));
        }










        return lookupField(positionBeforeName, name);
    }

    private final TableField<?, ?> parseFieldName() {
        return (TableField<?, ?>) lookupField(position, parseName());
    }

    private final Sequence<?> parseSequenceName() {
        return sequence(parseName());
    }

    private final Name parseIndexName() {
        Name result = parseNameIf();

        if (result == null)
            throw expected("Identifier");

        return result;
    }

    private final Name parseIndexNameIf() {
        if (!peekKeyword("ON"))
            return parseNameIf();
        else
            return null;
    }

    private final Collation parseCollation() {
        return collation(parseName());
    }

    private final CharacterSet parseCharacterSet() {
        return characterSet(parseName());
    }

    @Override
    public final Name parseName() {
        Name result = parseNameIf();

        if (result == null)
            throw expected("Identifier");

        return result;
    }

    @Override
    public final Name parseNameIf() {
        Name identifier = parseIdentifierIf();

        if (identifier == null)
            return null;

        // Avoid .. token in indexed for loops:
        // FOR i IN identifier1 .. identifier2 LOOP <...> END LOOP;
        if (peek('.') && !peek("..")) {
            List<Name> result = new ArrayList<>();
            result.add(identifier);

            while (parseIf('.'))
                result.add(parseIdentifier());

            return DSL.name(result.toArray(EMPTY_NAME));
        }
        else
            return identifier;
    }

    private final QualifiedAsterisk parseQualifiedAsteriskIf() {
        int positionBeforeName = position();
        Name i1 = parseIdentifierIf();

        if (i1 == null)
            return null;

        if (parseIf('.')) {
            List<Name> result = null;
            Name i2;

            do {
                if ((i2 = parseIdentifierIf()) != null) {
                    if (result == null) {
                        result = new ArrayList<>();
                        result.add(i1);
                    }

                    result.add(i2);
                }
                else {
                    parse('*');
                    return lookupTable(positionBeforeName, result == null ? i1 : DSL.name(result.toArray(EMPTY_NAME))).asterisk();
                }
            }
            while (parseIf('.'));
        }

        position(positionBeforeName);
        return null;
    }

    private final List<Name> parseIdentifiers() {
        LinkedHashSet<Name> result = new LinkedHashSet<>();

        do
            if (!result.add(parseIdentifier()))
                throw exception("Duplicate identifier encountered");
        while (parseIf(','));
        return new ArrayList<>(result);
    }

    @Override
    public final Name parseIdentifier() {
        return parseIdentifier(false);
    }

    private final Name parseIdentifier(boolean allowAposQuotes) {
        Name result = parseIdentifierIf(allowAposQuotes);

        if (result == null)
            throw expected("Identifier");

        return result;
    }

    @Override
    public final Name parseIdentifierIf() {
        return parseIdentifierIf(false);
    }

    private final Name parseIdentifierIf(boolean allowAposQuotes) {
        char quoteEnd = parseQuote(allowAposQuotes);
        boolean quoted = quoteEnd != 0;

        int start = position();
        StringBuilder sb = new StringBuilder();
        char c;
        if (quoted)
            while ((c = character()) != quoteEnd && hasMore() && positionInc() || character(position + 1) == quoteEnd && hasMore(1) && positionInc(2))
                sb.append(c);
        else
            for (; isIdentifierPart() && hasMore(); positionInc())
                sb.append(character());

        if (position() == start)
            return null;

        String name = normaliseNameCase(configuration(), sb.toString(), quoted, locale);

        if (quoted) {
            if (character() != quoteEnd)
                throw exception("Quoted identifier must terminate in " + quoteEnd);

            positionInc();
            parseWhitespaceIf();
            return DSL.quotedName(name);
        }
        else {
            parseWhitespaceIf();
            return DSL.unquotedName(name);
        }
    }

    private final char parseQuote(boolean allowAposQuotes) {
        return parseIf('"', false) ? '"'
             : parseIf('`', false) ? '`'
             : parseIf('[', false) ? ']'
             : allowAposQuotes && parseIf('\'', false) ? '\''
             : 0;
    }

    private final DataType<?> parseCastDataType() {
        char character = characterUpper();

        switch (character) {
            case 'S':
                if (parseKeywordIf("SIGNED") && (parseKeywordIf("INTEGER") || true))
                    return SQLDataType.BIGINT;

                break;

            case 'U':
                if (parseKeywordIf("UNSIGNED") && (parseKeywordIf("INTEGER") || true))
                    return SQLDataType.BIGINTUNSIGNED;

                break;
        }

        return parseDataType();
    }

    @Override
    public final DataType<?> parseDataType() {
        return parseDataTypeIf(true);
    }

    private final DataType<?> parseDataTypeIf(boolean parseUnknownTypes) {
        DataType<?> result = parseDataTypePrefixIf(parseUnknownTypes);

        if (result != null) {
            boolean array = false;

            if (parseKeywordIf("ARRAY"))
                array = true;

            if (parseIf('[')) {
                parseUnsignedIntegerLiteralIf();
                parse(']');

                array = true;
            }

            if (array)
                result = result.getArrayDataType();
        }

        return result;
    }

    private final DataType<?> parseDataTypePrefixIf(boolean parseUnknownTypes) {
        char character = characterUpper();

        if (character == '[' || character == '"' || character == '`')
            character = characterNextUpper();

        switch (character) {
            case 'A':
                if (parseKeywordOrIdentifierIf("ARRAY"))
                    return SQLDataType.OTHER.getArrayDataType();
                else if (parseKeywordIf("AUTO_INCREMENT")) {
                    parseDataTypeIdentityArgsIf();
                    return SQLDataType.INTEGER.identity(true);
                }

                break;

            case 'B':
                if (parseKeywordOrIdentifierIf("BIGINT"))
                    return parseUnsigned(parseAndIgnoreDataTypeLength(SQLDataType.BIGINT));
                else if (parseKeywordOrIdentifierIf("BIGSERIAL"))
                    return SQLDataType.BIGINT.identity(true);
                else if (parseKeywordOrIdentifierIf("BINARY"))
                    return parseDataTypeLength(SQLDataType.BINARY);
                else if (parseKeywordOrIdentifierIf("BIT"))
                    return parseDataTypeLength(SQLDataType.BIT);
                else if (parseKeywordOrIdentifierIf("BLOB"))
                    if (parseKeywordIf("SUB_TYPE"))
                        if (parseKeywordIf("0", "BINARY"))
                            return parseDataTypeLength(SQLDataType.BLOB);
                        else if (parseKeywordIf("1", "TEXT"))
                            return parseDataTypeLength(SQLDataType.CLOB);
                        else
                            throw expected("0", "BINARY", "1", "TEXT");
                    else
                        return parseDataTypeLength(SQLDataType.BLOB);
                else if (parseKeywordOrIdentifierIf("BOOLEAN") ||
                         parseKeywordOrIdentifierIf("BOOL"))
                    return SQLDataType.BOOLEAN;
                else if (parseKeywordOrIdentifierIf("BYTEA"))
                    return SQLDataType.BLOB;

                break;

            case 'C':
                if (parseKeywordOrIdentifierIf("CHARACTER VARYING"))
                    return parseDataTypeCollation(parseDataTypeLength(SQLDataType.VARCHAR));
                else if (parseKeywordOrIdentifierIf("CHAR") ||
                         parseKeywordOrIdentifierIf("CHARACTER"))
                    return parseDataTypeCollation(parseDataTypeLength(SQLDataType.CHAR));
                // [#5934] [#10291] TODO: support as actual data type as well
                else if (parseKeywordOrIdentifierIf("CITEXT"))
                    return parseDataTypeCollation(parseAndIgnoreDataTypeLength(SQLDataType.CLOB));
                else if (parseKeywordOrIdentifierIf("CLOB"))
                    return parseDataTypeCollation(parseDataTypeLength(SQLDataType.CLOB));

                break;

            case 'D':
                if (parseKeywordOrIdentifierIf("DATE"))
                    return SQLDataType.DATE;
                else if (parseKeywordOrIdentifierIf("DATETIME"))
                    return parseDataTypePrecisionIf(SQLDataType.TIMESTAMP);
                else if (parseKeywordOrIdentifierIf("DECIMAL"))
                    return parseDataTypePrecisionScaleIf(SQLDataType.DECIMAL);
                else if (parseKeywordOrIdentifierIf("DOUBLE PRECISION") ||
                         parseKeywordOrIdentifierIf("DOUBLE"))
                    return parseAndIgnoreDataTypePrecisionScaleIf(SQLDataType.DOUBLE);

                break;

            case 'E':
                if (parseKeywordOrIdentifierIf("ENUM"))
                    return parseDataTypeCollation(parseDataTypeEnum());

                break;

            case 'F':
                if (parseKeywordOrIdentifierIf("FLOAT"))
                    return parseAndIgnoreDataTypePrecisionScaleIf(SQLDataType.FLOAT);

                break;

            case 'I':
                if (parseKeywordOrIdentifierIf("INTEGER") ||
                    parseKeywordOrIdentifierIf("INT") ||
                    parseKeywordOrIdentifierIf("INT4"))
                    return parseUnsigned(parseAndIgnoreDataTypeLength(SQLDataType.INTEGER));
                else if (parseKeywordOrIdentifierIf("INT2"))
                    return SQLDataType.SMALLINT;
                else if (parseKeywordOrIdentifierIf("INT8"))
                    return SQLDataType.BIGINT;
                else if (parseKeywordIf("INTERVAL")) {
                    if (parseKeywordIf("YEAR")) {
                        parseDataTypePrecisionIf();
                        parseKeyword("TO MONTH");
                        return SQLDataType.INTERVALYEARTOMONTH;
                    }
                    else if (parseKeywordIf("DAY")) {
                        parseDataTypePrecisionIf();
                        parseKeyword("TO SECOND");
                        parseDataTypePrecisionIf();
                        return SQLDataType.INTERVALDAYTOSECOND;
                    }
                    else
                        return SQLDataType.INTERVAL;
                }
                else if (parseKeywordIf("IDENTITY")) {
                    parseDataTypeIdentityArgsIf();
                    return SQLDataType.INTEGER.identity(true);
                }

                break;

            case 'J':
                if (parseKeywordOrIdentifierIf("JSON"))
                    return SQLDataType.JSON;
                else if (parseKeywordOrIdentifierIf("JSONB"))
                    return SQLDataType.JSONB;

                break;

            case 'L':
                if (parseKeywordOrIdentifierIf("LONGBLOB"))
                    return SQLDataType.BLOB;
                else if (parseKeywordOrIdentifierIf("LONGTEXT"))
                    return parseDataTypeCollation(SQLDataType.CLOB);
                else if (parseKeywordOrIdentifierIf("LONG NVARCHAR"))
                    return parseDataTypeCollation(parseDataTypeLength(SQLDataType.LONGNVARCHAR));
                else if (parseKeywordOrIdentifierIf("LONG VARBINARY"))
                    return parseDataTypeCollation(parseDataTypeLength(SQLDataType.LONGVARBINARY));
                else if (parseKeywordOrIdentifierIf("LONG VARCHAR"))
                    return parseDataTypeCollation(parseDataTypeLength(SQLDataType.LONGVARCHAR));

                break;

            case 'M':
                if (parseKeywordOrIdentifierIf("MEDIUMBLOB"))
                    return SQLDataType.BLOB;
                else if (parseKeywordOrIdentifierIf("MEDIUMINT"))
                    return parseUnsigned(parseAndIgnoreDataTypeLength(SQLDataType.INTEGER));
                else if (parseKeywordOrIdentifierIf("MEDIUMTEXT"))
                    return parseDataTypeCollation(SQLDataType.CLOB);

                break;

            case 'N':
                if (parseKeywordOrIdentifierIf("NCHAR"))
                    return parseDataTypeCollation(parseDataTypeLength(SQLDataType.NCHAR));
                else if (parseKeywordOrIdentifierIf("NCLOB"))
                    return parseDataTypeCollation(SQLDataType.NCLOB);
                else if (parseKeywordOrIdentifierIf("NUMBER") ||
                         parseKeywordOrIdentifierIf("NUMERIC"))
                    return parseDataTypePrecisionScaleIf(SQLDataType.NUMERIC);
                else if (parseKeywordOrIdentifierIf("NVARCHAR") ||
                         parseKeywordOrIdentifierIf("NVARCHAR2"))
                    return parseDataTypeCollation(parseDataTypeLength(SQLDataType.NVARCHAR));

                break;

            case 'O':
                if (parseKeywordOrIdentifierIf("OTHER"))
                    return SQLDataType.OTHER;

                break;

            case 'R':
                if (parseKeywordOrIdentifierIf("REAL"))
                    return parseAndIgnoreDataTypePrecisionScaleIf(SQLDataType.REAL);

                break;

            case 'S':
                if (parseKeywordOrIdentifierIf("SERIAL4") ||
                    parseKeywordOrIdentifierIf("SERIAL"))
                    return SQLDataType.INTEGER.identity(true);
                else if (parseKeywordOrIdentifierIf("SERIAL8"))
                    return SQLDataType.BIGINT.identity(true);
                else if (parseKeywordOrIdentifierIf("SET"))
                    return parseDataTypeCollation(parseDataTypeEnum());
                else if (parseKeywordOrIdentifierIf("SMALLINT"))
                    return parseUnsigned(parseAndIgnoreDataTypeLength(SQLDataType.SMALLINT));
                else if (parseKeywordOrIdentifierIf("SMALLSERIAL") ||
                         parseKeywordOrIdentifierIf("SERIAL2"))
                    return SQLDataType.SMALLINT.identity(true);

                break;

            case 'T':
                if (parseKeywordOrIdentifierIf("TEXT"))
                    return parseDataTypeCollation(parseAndIgnoreDataTypeLength(SQLDataType.CLOB));
                else if (parseKeywordOrIdentifierIf("TIMESTAMPTZ"))
                    return parseDataTypePrecisionIf(SQLDataType.TIMESTAMPWITHTIMEZONE);
                else if (parseKeywordOrIdentifierIf("TIMESTAMP")) {
                    Integer precision = parseDataTypePrecisionIf();

                    if (parseKeywordOrIdentifierIf("WITH TIME ZONE"))
                        return precision == null ? SQLDataType.TIMESTAMPWITHTIMEZONE : SQLDataType.TIMESTAMPWITHTIMEZONE(precision);
                    else if (parseKeywordOrIdentifierIf("WITHOUT TIME ZONE") || true)
                        return precision == null ? SQLDataType.TIMESTAMP : SQLDataType.TIMESTAMP(precision);
                }
                else if (parseKeywordOrIdentifierIf("TIMETZ"))
                    return parseDataTypePrecisionIf(SQLDataType.TIMEWITHTIMEZONE);
                else if (parseKeywordOrIdentifierIf("TIME")) {
                    Integer precision = parseDataTypePrecisionIf();

                    if (parseKeywordOrIdentifierIf("WITH TIME ZONE"))
                        return precision == null ? SQLDataType.TIMEWITHTIMEZONE : SQLDataType.TIMEWITHTIMEZONE(precision);
                    else if (parseKeywordOrIdentifierIf("WITHOUT TIME ZONE") || true)
                        return precision == null ? SQLDataType.TIME : SQLDataType.TIME(precision);
                }
                else if (parseKeywordOrIdentifierIf("TINYBLOB"))
                    return SQLDataType.BLOB;
                else if (parseKeywordOrIdentifierIf("TINYINT"))
                    return parseUnsigned(parseAndIgnoreDataTypeLength(SQLDataType.TINYINT));
                else if (parseKeywordOrIdentifierIf("TINYTEXT"))
                    return parseDataTypeCollation(SQLDataType.CLOB);

                break;

            case 'U':
                if (parseKeywordOrIdentifierIf("UUID"))
                    return SQLDataType.UUID;
                else if (parseKeywordOrIdentifierIf("UNIQUEIDENTIFIER"))
                    return SQLDataType.UUID;

                break;

            case 'V':
                if (parseKeywordOrIdentifierIf("VARCHAR") ||
                    parseKeywordOrIdentifierIf("VARCHAR2") ||
                    // [#5934] [#10291] TODO: support as actual data type as well
                    parseKeywordOrIdentifierIf("VARCHAR_IGNORECASE"))
                    return parseDataTypeCollation(parseDataTypeLength(SQLDataType.VARCHAR));
                else if (parseKeywordOrIdentifierIf("VARBINARY"))
                    return parseDataTypeLength(SQLDataType.VARBINARY);

                break;

            case 'X':
                if (parseKeywordOrIdentifierIf("XML"))
                    return SQLDataType.XML;

                break;
        }

        if (parseUnknownTypes)
            return new DefaultDataType(dsl.dialect(), Object.class, parseName());
        else
            return null;
    }

    private final void parseDataTypeIdentityArgsIf() {
        if (parseIf('(')) {
            parseList(',', ParseContext::parseField);
            parse(')');
        }
    }

    private final boolean parseKeywordOrIdentifierIf(String keyword) {
        int p = position();
        char quoteEnd = parseQuote(false);
        boolean result = parseKeywordIf(keyword);

        if (!result)
            position(p);
        else if (quoteEnd != 0)
            parse(quoteEnd);

        return result;
    }

    private final DataType<?> parseUnsigned(DataType result) {
        if (parseKeywordIf("UNSIGNED"))
            if (result == SQLDataType.TINYINT)
                return SQLDataType.TINYINTUNSIGNED;
            else if (result == SQLDataType.SMALLINT)
                return SQLDataType.SMALLINTUNSIGNED;
            else if (result == SQLDataType.INTEGER)
                return SQLDataType.INTEGERUNSIGNED;
            else if (result == SQLDataType.BIGINT)
                return SQLDataType.BIGINTUNSIGNED;

        return result;
    }

    private final DataType<?> parseAndIgnoreDataTypeLength(DataType<?> result) {
        if (parseIf('(')) {
            parseUnsignedIntegerLiteral();
            parse(')');
        }

        return result;
    }

    private final DataType<?> parseDataTypeLength(DataType<?> in) {
        DataType<?> result = in;

        if (parseIf('(')) {
            if (!parseKeywordIf("MAX"))
                result = result.length(asInt(parseUnsignedIntegerLiteral()));

            if (in == SQLDataType.VARCHAR || in == SQLDataType.CHAR)
                if (!parseKeywordIf("BYTE"))
                    parseKeywordIf("CHAR");

            parse(')');
        }

        return result;
    }

    private final DataType<?> parseDataTypeCollation(DataType<?> result) {
        CharacterSet cs = parseCharacterSetSpecificationIf();
        if (cs != null)
            result = result.characterSet(cs);

        Collation col = parseCollateSpecificationIf();
        if (col != null)
            result = result.collation(col);

        return result;
    }

    private final CharacterSet parseCharacterSetSpecificationIf() {
        if (parseKeywordIf("CHARACTER SET") || parseKeywordIf("CHARSET")) {
            parseIf('=');
            return parseCharacterSet();
        }

        return null;
    }

    private final Collation parseCollateSpecificationIf() {
        if (parseKeywordIf("COLLATE")) {
            parseIf('=');
            return parseCollation();
        }

        return null;
    }

    private final DataType<?> parseAndIgnoreDataTypePrecisionScaleIf(DataType<?> result) {
        if (parseIf('(')) {
            parseUnsignedIntegerLiteral();

            if (parseIf(','))
                parseUnsignedIntegerLiteral();

            parse(')');
        }

        return result;
    }

    private final Integer parseDataTypePrecisionIf() {
        Integer precision = null;

        if (parseIf('(')) {
            precision = asInt(parseUnsignedIntegerLiteral());
            parse(')');
        }

        return precision;
    }

    private final DataType<?> parseDataTypePrecisionIf(DataType<?> result) {
        if (parseIf('(')) {
            int precision = asInt(parseUnsignedIntegerLiteral());
            result = result.precision(precision);
            parse(')');
        }

        return result;
    }

    private final DataType<?> parseDataTypePrecisionScaleIf(DataType<?> result) {
        if (parseIf('(')) {
            int precision = parseIf('*') ? 38 : asInt(parseUnsignedIntegerLiteral());

            if (parseIf(','))
                result = result.precision(precision, asInt(parseSignedIntegerLiteral()));
            else
                result = result.precision(precision);

            parse(')');
        }

        return result;
    }

    private final DataType<?> parseDataTypeEnum() {
        parse('(');
        List<String> literals = new ArrayList<>();
        int length = 0;

        do {
            String literal = parseStringLiteral();

            if (literal != null)
                length = Math.max(length, literal.length());

            literals.add(literal);
        }
        while (parseIf(','));

        parse(')');

        // [#7025] TODO, replace this by a dynamic enum data type encoding, once available
        String className = "GeneratedEnum" + (literals.hashCode() & 0x7FFFFFF);
        StringBuilder content = new StringBuilder();
        content.append(
                    "package org.jooq.impl;\n"
                  + "enum ").append(className).append(" implements org.jooq.EnumType {\n");

        for (int i = 0; i < literals.size(); i++)
            content.append("  E").append(i).append("(\"").append(literals.get(i).replace("\"", "\\\"")).append("\"),\n");

        content.append(
                    "  ;\n"
                  + "  final String literal;\n"
                  + "  private ").append(className).append("(String literal) { this.literal = literal; }\n"
                  + "  @Override\n"
                  + "  public String getName() {\n"
                  + "    return getClass().getName();\n"
                  + "  }\n"
                  + "  @Override\n"
                  + "  public String getLiteral() {\n"
                  + "    return literal;\n"
                  + "  }\n"
                  + "}");

        return VARCHAR(length).asEnumDataType(Reflect.compile("org.jooq.impl." + className, content.toString()).get());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Literal parsing
    // -----------------------------------------------------------------------------------------------------------------

    private final char parseCharacterLiteral() {
        parse('\'', false);

        char c = character();

        // TODO MySQL string escaping...
        if (c == '\'')
            parse('\'', false);

        positionInc();
        parse('\'');
        return c;
    }

    private final Field<?> parseBindVariableIf() {
        int p = position();
        String paramName;

        switch (character()) {
            case '?':
                parse('?');
                paramName = "" + bindIndex;
                break;

            default:
                String prefix = defaultIfNull(settings().getParseNamedParamPrefix(), ":");

                if (parseIf(prefix, false)) {
                    Name identifier = parseIdentifier();
                    paramName = identifier.last();

                    // [#8821] Avoid conflicts with dollar quoted string literals
                    if ("$".equals(prefix) && paramName.endsWith("$")) {
                        position(p);
                        return null;
                    }

                    break;
                }
                else
                    return null;
        }

        // [#11074] Bindings can be Param or even Field types
        Object binding = nextBinding();

        if (binding instanceof Field)
            return (Field<?>) binding;

        Param<?> param = DSL.param(paramName, binding);

        if (bindParamListener != null)
            bindParams.put(paramName, param);

        return param;
    }

    private final Comment parseComment() {
        return DSL.comment(parseStringLiteral());
    }

    private final String parseStringLiteral(String literal) {
        String value = parseStringLiteral();

        if (!literal.equals(value))
            throw expected("String literal: '" + literal + "'");

        return value;
    }

    @Override
    public final String parseStringLiteral() {
        String result = parseStringLiteralIf();

        if (result == null)
            throw expected("String literal");

        return result;
    }

    @Override
    public final String parseStringLiteralIf() {
        if (parseIf('q', '\'', false) || parseIf('Q', '\'', false))
            return parseOracleQuotedStringLiteral();
        else if (parseIf('e', '\'', false) || parseIf('E', '\'', false))
            return parseUnquotedStringLiteral(true, '\'');
        else if (peek('\''))
            return parseUnquotedStringLiteral(false, '\'');
        else if (parseIf('n', '\'', false) || parseIf('N', '\'', false))
            return parseUnquotedStringLiteral(true, '\'');




        else if (peek('$'))
            return parseDollarQuotedStringLiteralIf();
        else
            return null;
    }

    private final Boolean parseBitLiteralIf() {
        if (parseIf("B'", false) || parseIf("b'", false)) {
            boolean result = false;

            if (!parseIf('0') && parseIf('1'))
                result = true;

            if (parseIf('0') || parseIf('1'))
                throw exception("Currently, only BIT(1) literals are supported");

            parse('\'');
            return result;
        }

        return null;
    }

    private final byte[] parseBinaryLiteralIf() {
        if (parseIf("X'", false) || parseIf("x'", false)) {
            if (parseIf('\''))
                return EMPTY_BYTE;

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            char c1 = 0;
            char c2 = 0;

            do {
                while (hasMore()) {
                    c1 = character();

                    if (c1 == ' ')
                        positionInc();
                    else
                        break;
                }

                c2 = characterNext();

                if (c1 == '\'')
                    break;
                if (c2 == '\'')
                    throw exception("Unexpected token: \"'\"");

                try {
                    buffer.write(Integer.parseInt("" + c1 + c2, 16));
                }
                catch (NumberFormatException e) {
                    throw exception("Illegal character for binary literal");
                }

                positionInc(2);
            }
            while (hasMore());

            if (c1 == '\'') {
                positionInc();
                parseWhitespaceIf();
                return buffer.toByteArray();
            }

            throw exception("Binary literal not terminated");
        }

        return null;
    }

    private final String parseOracleQuotedStringLiteral() {
        parse('\'', false);

        char start = character();
        char end;

        switch (start) {
            case '[' : end = ']'; positionInc(); break;
            case '{' : end = '}'; positionInc(); break;
            case '(' : end = ')'; positionInc(); break;
            case '<' : end = '>'; positionInc(); break;
            case ' ' :
            case '\t':
            case '\r':
            case '\n': throw exception("Illegal quote string character");
            default  : end = start; positionInc(); break;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = position(); i < sql.length; i++) {
            char c = character(i);

            if (c == end)
                if (character(i + 1) == '\'') {
                    position(i + 2);
                    parseWhitespaceIf();
                    return sb.toString();
                }
                else {
                    i++;
                }

            sb.append(c);
        }

        throw exception("Quoted string literal not terminated");
    }

    private final String parseDollarQuotedStringLiteralIf() {
        int previous = position();

        if (!peek('$'))
            return null;
        else
            parse('$');

        int openTokenStart = previous;
        int openTokenEnd = previous;

        int closeTokenStart = -1;
        int closeTokenEnd = -1;

        tokenLoop:
        for (int i = position(); i < sql.length; i++) {
            char c = character(i);

            // "Good enough" approximation of PostgreSQL's syntax requirements
            // for dollar quoted tokens. If formal definition is known, improve.
            // No definition is available from this documentation:
            // https://www.postgresql.org/docs/current/sql-syntax-lexical.html#SQL-SYNTAX-DOLLAR-QUOTING
            if (!Character.isJavaIdentifierPart(c))
                return null;

            openTokenEnd++;

            if (c == '$')
                break tokenLoop;
        }

        position(openTokenEnd + 1);

        literalLoop:
        for (int i = position(); i < sql.length; i++) {
            char c = character(i);

            if (c == '$')
                if (closeTokenStart == -1)
                    closeTokenStart = i;
                else if (openTokenEnd - openTokenStart == (closeTokenEnd = i) - closeTokenStart)
                    break literalLoop;
                else
                    closeTokenStart = closeTokenEnd;
            else if (closeTokenStart > -1 && character(i) != character(i - (closeTokenStart - openTokenStart)))
                closeTokenStart = -1;
        }

        if (closeTokenEnd != -1) {
            position(closeTokenEnd + 1);
            return substring(openTokenEnd + 1, closeTokenStart);
        }

        position(previous);
        return null;
    }

    private final String parseUnquotedStringLiteral(boolean postgresEscaping, char delim) {
        parse(delim, false);

        StringBuilder sb = new StringBuilder();

        characterLoop:
        for (int i = position(); i < sql.length; i++) {
            char c1 = character(i);

            // TODO MySQL string escaping...
            switch (c1) {
                case '\\': {
                    if (!postgresEscaping)
                        break;

                    i++;
                    char c2 = character(i);
                    switch (c2) {

                        // Escaped whitespace characters
                        case 'b':
                            c1 = '\b';
                            break;
                        case 'n':
                            c1 = '\n';
                            break;
                        case 't':
                            c1 = '\t';
                            break;
                        case 'r':
                            c1 = '\r';
                            break;
                        case 'f':
                            c1 = '\f';
                            break;

                        // Hexadecimal byte value
                        case 'x': {
                            char c3 = character(i + 1);
                            char c4 = character(i + 2);

                            int d3;
                            if ((d3 = Character.digit(c3, 16)) != -1) {
                                i++;

                                int d4;
                                if ((d4 = Character.digit(c4, 16)) != -1) {
                                    c1 = (char) (0x10 * d3 + d4);
                                    i++;
                                }
                                else
                                    c1 = (char) d3;
                            }
                            else
                                throw exception("Illegal hexadecimal byte value");

                            break;
                        }

                        // Unicode character value UTF-16
                        case 'u':
                            c1 = (char) Integer.parseInt(new String(sql, i + 1, 4), 16);
                            i += 4;
                            break;

                        // Unicode character value UTF-32
                        case 'U':
                            sb.appendCodePoint(Integer.parseInt(new String(sql, i + 1, 8), 16));
                            i += 8;
                            continue characterLoop;

                        default:

                            // Octal byte value
                            if (Character.digit(c2, 8) != -1) {
                                char c3 = character(i + 1);

                                if (Character.digit(c3, 8) != -1) {
                                    i++;
                                    char c4 = character(i + 1);

                                    if (Character.digit(c4, 8) != -1) {
                                        i++;
                                        c1 = (char) Integer.parseInt("" + c2 + c3 + c4, 8);
                                    }
                                    else {
                                        c1 = (char) Integer.parseInt("" + c2 + c3, 8);
                                    }
                                }
                                else {
                                    c1 = (char) Integer.parseInt("" + c2, 8);
                                }
                            }

                            // All other characters
                            else {
                                c1 = c2;
                            }

                            break;
                    }

                    break;
                }








                case '\'': {
                    if (character(i + 1) != delim) {
                        position(i + 1);
                        parseWhitespaceIf();
                        return sb.toString();
                    }

                    i++;
                    break;
                }
            }

            sb.append(c1);
        }

        throw exception("String literal not terminated");
    }

    private final Field<Number> parseFieldUnsignedNumericLiteral(Sign sign) {
        Field<Number> result = parseFieldUnsignedNumericLiteralIf(sign);

        if (result == null)
            throw expected("Unsigned numeric literal");

        return result;
    }

    private final Field<Number> parseFieldUnsignedNumericLiteralIf(Sign sign) {
        Number r = parseUnsignedNumericLiteralIf(sign);
        return r == null ? null : inline(r);
    }

    private final Number parseUnsignedNumericLiteralIf(Sign sign) {
        int p = position();
        boolean decimal = false;
        parseDigits();

        if (decimal |= parseIf('.', false))
            parseDigits();

        if (p == position())
            return null;

        if (parseIf('e', false) || parseIf('E', false)) {
            parseIf('-', false);
            parseDigits();

            String s = substring(p, position());
            parseWhitespaceIf();
            return sign == Sign.MINUS ? -Double.parseDouble(s) : Double.parseDouble(s);
        }
        else {
            String s = substring(p, position());
            parseWhitespaceIf();

            if (decimal)
                return sign == Sign.MINUS ? new BigDecimal(s).negate() : new BigDecimal(s);

            try {
                return sign == Sign.MINUS ? -Long.valueOf(s) : Long.valueOf(s);
            }
            catch (Exception e1) {
                return sign == Sign.MINUS ? new BigInteger(s).negate() : new BigInteger(s);
            }
        }
    }

    private void parseDigits() {
        for (;;) {
            char c = character();

            if (c >= '0' && c <= '9')
                positionInc();
            else
                break;
        }
    }

    private final Field<Integer> parseZeroOne() {
        if (parseIf('0'))
            return zero();
        else if (parseIf('1'))
            return one();
        else
            throw expected("0 or 1");
    }

    private final Field<Integer> parseZeroOneDefault() {
        if (parseIf('0'))
            return zero();
        else if (parseIf('1'))
            return one();
        else if (parseKeywordIf("DEFAULT"))
            return defaultValue(INTEGER);
        else
            throw expected("0 or 1");
    }

    @Override
    public final Long parseSignedIntegerLiteral() {
        Long result = parseSignedIntegerLiteralIf();

        if (result == null)
            throw expected("Signed integer");

        return result;
    }

    @Override
    public final Long parseSignedIntegerLiteralIf() {
        Sign sign = parseSign();
        Long unsigned;

        if (sign == Sign.MINUS)
            unsigned = parseUnsignedIntegerLiteral();
        else
            unsigned = parseUnsignedIntegerLiteralIf();

        return unsigned == null
             ? null
             : sign == Sign.MINUS
             ? -unsigned
             : unsigned;
    }

    private final <T> Param<T> requireParam(Field<T> field) {
        if (field instanceof Param)
            return (Param<T>) field;
        else
            throw expected("Bind parameter or constant");
    }

    private final <T> List<T> parseList(char separator, Function<? super ParseContext, ? extends T> element) {
        return parseList(c -> c.parseIf(separator), element);
    }

    @Override
    public final <T> List<T> parseList(String separator, Function<? super ParseContext, ? extends T> element) {
        return parseList(c -> c.parseIf(separator), element);
    }

    @Override
    public final <T> List<T> parseList(Predicate<? super ParseContext> separator, Function<? super ParseContext, ? extends T> element) {
        List<T> result = new ArrayList<>();

        do
            result.add(element.apply(this));
        while (separator.test(this));

        return result;
    }

    @Override
    public final <T> T parseParenthesised(Function<? super ParseContext, ? extends T> content) {
        return parseParenthesised('(', content, ')');
    }

    @Override
    public final <T> T parseParenthesised(char open, Function<? super ParseContext, ? extends T> content, char close) {
        parse(open);
        T result = content.apply(this);
        parse(close);

        return result;
    }

    @Override
    public final <T> T parseParenthesised(String open, Function<? super ParseContext, ? extends T> content, String close) {
        parse(open);
        T result = content.apply(this);
        parse(close);

        return result;
    }

    private final Field<Long> parseParenthesisedUnsignedIntegerOrBindVariable() {
        Field<Long> result;

        int parens;
        for (parens = 0; parseIf('('); parens++);
        result = parseUnsignedIntegerOrBindVariable();
        for (; parens > 0 && parse(')'); parens--);

        return result;
    }

    private final Field<Long> parseUnsignedIntegerOrBindVariable() {
        Long i = parseUnsignedIntegerLiteralIf();

        if (i != null)
            return DSL.inline(i);

        Field<?> f = parseBindVariableIf();
        if (f != null)
            return (Field<Long>) f;

        throw expected("Unsigned integer or bind variable");
    }

    @Override
    public final Long parseUnsignedIntegerLiteral() {
        Long result = parseUnsignedIntegerLiteralIf();

        if (result == null)
            throw expected("Unsigned integer literal");

        return result;
    }

    @Override
    public final Long parseUnsignedIntegerLiteralIf() {
        int p = position();
        parseDigits();

        if (p == position())
            return null;

        String s = substring(p, position());
        parseWhitespaceIf();
        return Long.valueOf(s);
    }

    private final JoinType parseJoinTypeIf() {
        if (parseKeywordIf("CROSS")) {
            if (parseKeywordIf("JOIN"))
                return JoinType.CROSS_JOIN;
            else if (parseKeywordIf("APPLY"))
                return JoinType.CROSS_APPLY;
        }
        else if (parseKeywordIf("INNER") && parseKeyword("JOIN"))
            return JoinType.JOIN;
        else if (parseKeywordIf("JOIN"))
            return JoinType.JOIN;
        else if (parseKeywordIf("LEFT")) {
            if (parseKeywordIf("SEMI") && parseKeyword("JOIN"))
                return JoinType.LEFT_SEMI_JOIN;
            else if (parseKeywordIf("ANTI") && parseKeyword("JOIN"))
                return JoinType.LEFT_ANTI_JOIN;
            else if ((parseKeywordIf("OUTER") || true) && parseKeyword("JOIN"))
                return JoinType.LEFT_OUTER_JOIN;
        }
        else if (parseKeywordIf("RIGHT") && (parseKeywordIf("OUTER") || true) && parseKeyword("JOIN"))
            return JoinType.RIGHT_OUTER_JOIN;
        else if (parseKeywordIf("FULL") && (parseKeywordIf("OUTER") || true) && parseKeyword("JOIN"))
            return JoinType.FULL_OUTER_JOIN;
        else if (parseKeywordIf("OUTER APPLY"))
            return JoinType.OUTER_APPLY;
        else if (parseKeywordIf("NATURAL")) {
            if (parseKeywordIf("LEFT") && (parseKeywordIf("OUTER") || true) && parseKeyword("JOIN"))
                return JoinType.NATURAL_LEFT_OUTER_JOIN;
            else if (parseKeywordIf("RIGHT") && (parseKeywordIf("OUTER") || true) && parseKeyword("JOIN"))
                return JoinType.NATURAL_RIGHT_OUTER_JOIN;
            else if (parseKeywordIf("FULL") && (parseKeywordIf("OUTER") || true) && parseKeyword("JOIN"))
                return JoinType.NATURAL_FULL_OUTER_JOIN;
            else if ((parseKeywordIf("INNER") || true) && parseKeyword("JOIN"))
                return JoinType.NATURAL_JOIN;
        }
        else if (parseKeywordIf("STRAIGHT_JOIN"))
            return JoinType.STRAIGHT_JOIN;

        return null;
        // TODO partitioned join
    }

    private final TruthValue parseTruthValueIf() {
        if (parseKeywordIf("TRUE"))
            return TruthValue.T_TRUE;
        else if (parseKeywordIf("FALSE"))
            return TruthValue.T_FALSE;
        else if (parseKeywordIf("NULL"))
            return TruthValue.T_NULL;

        return null;
    }

    private final CombineOperator parseCombineOperatorIf(boolean intersectOnly) {
        if (!intersectOnly && parseKeywordIf("UNION"))
            if (parseKeywordIf("ALL"))
                return CombineOperator.UNION_ALL;
            else if (parseKeywordIf("DISTINCT"))
                return CombineOperator.UNION;
            else
                return CombineOperator.UNION;
        else if (!intersectOnly && (parseKeywordIf("EXCEPT") || parseKeywordIf("MINUS")))
            if (parseKeywordIf("ALL"))
                return CombineOperator.EXCEPT_ALL;
            else if (parseKeywordIf("DISTINCT"))
                return CombineOperator.EXCEPT;
            else
                return CombineOperator.EXCEPT;
        else if (intersectOnly && parseKeywordIf("INTERSECT"))
            if (parseKeywordIf("ALL"))
                return CombineOperator.INTERSECT_ALL;
            else if (parseKeywordIf("DISTINCT"))
                return CombineOperator.INTERSECT;
            else
                return CombineOperator.INTERSECT;

        return null;
    }

    private final ComputationalOperation parseComputationalOperationIf() {
        switch (characterUpper()) {
            case 'A':
                if (parseFunctionNameIf("ANY"))
                    return ComputationalOperation.ANY;
                else if (parseFunctionNameIf("ANY_VALUE"))
                    return ComputationalOperation.ANY_VALUE;
                else if (parseFunctionNameIf("AVG"))
                    return ComputationalOperation.AVG;

                break;

            case 'B':
                if (parseFunctionNameIf("BOOL_AND", "BOOLAND_AGG"))
                    return ComputationalOperation.EVERY;
                else if (parseFunctionNameIf("BOOL_OR", "BOOLOR_AGG"))
                    return ComputationalOperation.ANY;

                break;

            case 'E':
                if (parseFunctionNameIf("EVERY"))
                    return ComputationalOperation.EVERY;

                break;

            case 'L':
                if (parseFunctionNameIf("LOGICAL_AND"))
                    return ComputationalOperation.EVERY;
                else if (parseFunctionNameIf("LOGICAL_OR"))
                    return ComputationalOperation.ANY;

                break;


            case 'M':
                if (parseFunctionNameIf("MAX"))
                    return ComputationalOperation.MAX;
                else if (parseFunctionNameIf("MEDIAN"))
                    return ComputationalOperation.MEDIAN;
                else if (parseFunctionNameIf("MIN"))
                    return ComputationalOperation.MIN;
                else if (parseFunctionNameIf("MUL"))
                    return ComputationalOperation.PRODUCT;

                break;

            case 'P':
                if (parseFunctionNameIf("PRODUCT"))
                    return ComputationalOperation.PRODUCT;

                break;

            case 'S':
                if (parseFunctionNameIf("SUM"))
                    return ComputationalOperation.SUM;
                else if (parseFunctionNameIf("SOME"))
                    return ComputationalOperation.ANY;
                else if (parseFunctionNameIf("STDDEV", "STDEVP", "STDDEV_POP"))
                    return ComputationalOperation.STDDEV_POP;




                else if (parseFunctionNameIf("STDDEV_SAMP", "STDEV", "STDEV_SAMP"))
                    return ComputationalOperation.STDDEV_SAMP;

                break;

            case 'V':
                if (parseFunctionNameIf("VAR_POP", "VARIANCE", "VARP"))
                    return ComputationalOperation.VAR_POP;




                else if (parseFunctionNameIf("VAR_SAMP", "VARIANCE_SAMP", "VAR"))
                    return ComputationalOperation.VAR_SAMP;

                break;
        }

        return null;
    }

    private final Comparator parseComparatorIf() {










        if (parseIf("=") || parseKeywordIf("EQ"))
            return Comparator.EQUALS;
        else if (parseIf("!=") || parseIf("<>") || parseIf("^=") || parseKeywordIf("NE"))
            return Comparator.NOT_EQUALS;
        else if (parseIf(">=") || parseKeywordIf("GE"))
            return Comparator.GREATER_OR_EQUAL;
        else if (parseIf(">") || parseKeywordIf("GT"))
            return Comparator.GREATER;

        // MySQL DISTINCT operator
        else if (parseIf("<=>"))
            return Comparator.IS_NOT_DISTINCT_FROM;
        else if (parseIf("<=") || parseKeywordIf("LE"))
            return Comparator.LESS_OR_EQUAL;
        else if (parseIf("<") || parseKeywordIf("LT"))
            return Comparator.LESS;

        return null;
    }

    private static enum TSQLOuterJoinComparator {
        LEFT, RIGHT
    }

    private final TSQLOuterJoinComparator parseTSQLOuterJoinComparatorIf() {
        if (parseIf("*="))
            return TSQLOuterJoinComparator.LEFT;
        else if (parseIf("=*"))
            return TSQLOuterJoinComparator.RIGHT;
        else
            return null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Other tokens
    // -----------------------------------------------------------------------------------------------------------------

    private final String parseUntilEOL() {
        String result = parseUntilEOLIf();

        if (result == null)
            throw expected("Content before EOL");

        return result;
    }

    private final String parseUntilEOLIf() {
        int start = position();
        int stop = start;

        for (; stop < sql.length; stop++) {
            char c = character(stop);

            if (c == '\r') {
                if (character(stop + 1) == '\n')
                    stop++;

                break;
            }
            else if (c == '\n')
                break;
        }

        if (start == stop)
            return null;

        position(stop);
        parseWhitespaceIf();
        return substring(start, stop);
    }

    private final boolean parseTokens(char... tokens) {
        boolean result = parseTokensIf(tokens);

        if (!result)
            throw expected(new String(tokens));

        return result;
    }

    private final boolean parseTokensIf(char... tokens) {
        int p = position();

        for (char token : tokens) {
            if (!parseIf(token)) {
                position(p);
                return false;
            }
        }

        return true;
    }

    private final boolean peekTokens(char... tokens) {
        int p = position();

        for (char token : tokens) {
            if (!parseIf(token)) {
                position(p);
                return false;
            }
        }

        position(p);
        return true;
    }

    @Override
    public final boolean parse(String string) {
        boolean result = parseIf(string);

        if (!result)
            throw expected(string);

        return result;
    }

    @Override
    public final boolean parseIf(String string) {
        return parseIf(string, true);
    }

    private final boolean parseIf(String string, boolean skipAfterWhitespace) {
        boolean result = peek(string);

        if (result) {
            positionInc(string.length());

            if (skipAfterWhitespace)
                parseWhitespaceIf();
        }

        return result;
    }

    @Override
    public final boolean parse(char c) {
        return parse(c, true);
    }

    private final boolean parse(char c, boolean skipAfterWhitespace) {
        if (!parseIf(c, skipAfterWhitespace))
            throw expected("Token '" + c + "'");

        return true;
    }

    @Override
    public final boolean parseIf(char c) {
        return parseIf(c, true);
    }

    private final boolean parseIf(char c, boolean skipAfterWhitespace) {
        boolean result = peek(c);

        if (result) {
            positionInc();

            if (skipAfterWhitespace)
                parseWhitespaceIf();
        }

        return result;
    }

    private final boolean parseIf(char c, char peek, boolean skipAfterWhitespace) {
        if (character() != c)
            return false;

        if (characterNext() != peek)
            return false;

        positionInc();

        if (skipAfterWhitespace)
            parseWhitespaceIf();

        return true;
    }

    @Override
    public final boolean parseFunctionNameIf(String name) {
        return peekKeyword(name, true, false, true);
    }

    private final boolean parseFunctionNameIf(String name1, String name2) {
        return parseFunctionNameIf(name1) || parseFunctionNameIf(name2);
    }

    private final boolean parseFunctionNameIf(String name1, String name2, String name3) {
        return parseFunctionNameIf(name1) || parseFunctionNameIf(name2) || parseFunctionNameIf(name3);
    }

    @Override
    public final boolean parseFunctionNameIf(String... names) {
        return anyMatch(names, n -> parseFunctionNameIf(n));
    }

    private final boolean parseOperator(String operator) {
        if (!parseOperatorIf(operator))
            throw expected("Operator '" + operator + "'");

        return true;
    }

    private final boolean parseOperatorIf(String operator) {
        return peekOperator(operator, true);
    }

    private final boolean peekOperator(String operator) {
        return peekOperator(operator, false);
    }

    private final boolean peekOperator(String operator, boolean updatePosition) {
        int length = operator.length();
        int p = position();

        if (sql.length < p + length)
            return false;

        int pos = afterWhitespace(p, false);

        for (int i = 0; i < length; i++, pos++)
            if (sql[pos] != operator.charAt(i))
                return false;

        // [#9888] An operator that is followed by a special character is very likely another, more complex operator
        if (isOperatorPart(pos))
            return false;

        if (updatePosition) {
            position(pos);
            parseWhitespaceIf();
        }

        return true;
    }

    @Override
    public final boolean parseKeyword(String keyword) {
        if (!parseKeywordIf(keyword))
            throw expected("Keyword '" + keyword + "'");

        return true;
    }

    private final boolean parseKeyword(String keyword1, String keyword2) {
        if (parseKeywordIf(keyword1, keyword2))
            return true;

        throw expected(keyword1, keyword2);
    }

    private final boolean parseKeyword(String keyword1, String keyword2, String keyword3) {
        if (parseKeywordIf(keyword1, keyword2, keyword3))
            return true;

        throw expected(keyword1, keyword2, keyword3);
    }

    @Override
    public final boolean parseKeywordIf(String keyword) {
        return peekKeyword(keyword, true, false, false);
    }

    private final boolean parseKeywordIf(String keyword1, String keyword2) {
        return parseKeywordIf(keyword1) || parseKeywordIf(keyword2);
    }

    private final boolean parseKeywordIf(String keyword1, String keyword2, String keyword3) {
        return parseKeywordIf(keyword1) || parseKeywordIf(keyword2) || parseKeywordIf(keyword3);
    }

    @Override
    public final boolean parseKeywordIf(String... keywords) {
        return anyMatch(keywords, k -> parseKeywordIf(k));
    }

    @Override
    public final boolean parseKeyword(String... keywords) {
        if (parseKeywordIf(keywords))
            return true;

        throw expected(keywords);
    }

    private final Keyword parseAndGetKeyword(String... keywords) {
        Keyword result = parseAndGetKeywordIf(keywords);

        if (result == null)
            throw expected(keywords);

        return result;
    }

    private final Keyword parseAndGetKeywordIf(String... keywords) {
        return findAny(keywords, k -> parseKeywordIf(k), k -> keyword(k.toLowerCase()));
    }

    private final Keyword parseAndGetKeywordIf(String keyword) {
        if (parseKeywordIf(keyword))
            return keyword(keyword.toLowerCase());

        return null;
    }

    @Override
    public final boolean peek(char c) {
        if (character() != c)
            return false;

        return true;
    }

    @Override
    public final boolean peek(String string) {
        return peek(string, position());
    }

    private final boolean peek(String string, int p) {
        int length = string.length();

        if (sql.length < p + length)
            return false;

        for (int i = 0; i < length; i++)
            if (sql[p + i] != string.charAt(i))
                return false;

        return true;
    }

    @Override
    public final boolean peekKeyword(String... keywords) {
        return anyMatch(keywords, k -> peekKeyword(k));
    }

    @Override
    public final boolean peekKeyword(String keyword) {
        return peekKeyword(keyword, false, false, false);
    }

    private final boolean peekKeyword(String keyword1, String keyword2) {
        return peekKeyword(keyword1) || peekKeyword(keyword2);
    }

    private final boolean peekKeyword(String keyword1, String keyword2, String keyword3) {
        return peekKeyword(keyword1) || peekKeyword(keyword2) || peekKeyword(keyword3);
    }

    private final boolean peekKeyword(String keyword, boolean updatePosition, boolean peekIntoParens, boolean requireFunction) {
        int length = keyword.length();
        int p = position();

        if (sql.length < p + length)
            return false;

        int skip = afterWhitespace(p, peekIntoParens) - p;

        for (int i = 0; i < length; i++) {
            char c = keyword.charAt(i);
            int pos = p + i + skip;

            switch (c) {
                case ' ':
                    if (!Character.isWhitespace(sql[pos]))
                        return false;

                    skip = skip + (afterWhitespace(pos) - pos - 1);
                    break;

                default:
                    if (upper(sql[pos]) != c)
                        return false;

                    break;
            }
        }

        int pos = p + length + skip;

        // [#8806] A keyword that is followed by a period is very likely an identifier
        if (isIdentifierPart(pos) || character(pos) == '.')
            return false;

        if (requireFunction)
            if (character(afterWhitespace(pos)) != '(')
                return false;

        if (updatePosition) {
            positionInc(length + skip);
            parseWhitespaceIf();
        }

        return true;
    }

    private final boolean parseWhitespaceIf() {
        int p = position();
        position(afterWhitespace(p));
        return p != position();
    }

    private final int afterWhitespace(int p) {
        return afterWhitespace(p, false);
    }

    private final int afterWhitespace(int p, boolean peekIntoParens) {

        // [#8074] The SQL standard and some implementations (e.g. PostgreSQL,
        //         SQL Server) support nesting block comments
        int blockCommentNestLevel = 0;
        boolean ignoreComment = false;
        final String ignoreCommentStart = settings().getParseIgnoreCommentStart();
        final String ignoreCommentStop = settings().getParseIgnoreCommentStop();
        final boolean checkIgnoreComment = !FALSE.equals(settings().isParseIgnoreComments());

        loop:
        for (int i = p; i < sql.length; i++) {
            switch (sql[i]) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    p = i + 1;
                    continue loop;

                case '(':
                    if (peekIntoParens)
                        continue loop;
                    else
                        break loop;

                case '/':
                    if (i + 1 < sql.length && sql[i + 1] == '*') {
                        i = i + 2;
                        blockCommentNestLevel++;

                        while (i < sql.length) {
                            if (!(ignoreComment = peekIgnoreComment(ignoreComment, ignoreCommentStart, ignoreCommentStop, checkIgnoreComment, i))) {
                                switch (sql[i]) {
                                    case '/':
                                        if (i + 1 < sql.length && sql[i + 1] == '*') {
                                            i = i + 2;
                                            blockCommentNestLevel++;
                                        }

                                        break;

                                    case '+':
                                        if (!ignoreHints() && i + 1 < sql.length && ((sql[i + 1] >= 'A' && sql[i + 1] <= 'Z') || (sql[i + 1] >= 'a' && sql[i + 1] <= 'z'))) {
                                            blockCommentNestLevel = 0;
                                            break loop;
                                        }

                                        break;

                                    case '*':
                                        if (i + 1 < sql.length && sql[i + 1] == '/') {
                                            p = (i = i + 1) + 1;

                                            if (--blockCommentNestLevel == 0)
                                                continue loop;
                                        }

                                        break;
                                }
                            }

                            i++;
                        }
                    }

                    // [#9651] H2 and Snowflake's c-style single line comments
                    else if (i + 1 < sql.length && sql[i + 1] == '/') {
                        i = i + 2;

                        while (i < sql.length) {
                            if (!(ignoreComment = peekIgnoreComment(ignoreComment, ignoreCommentStart, ignoreCommentStop, checkIgnoreComment, i))) {
                                switch (sql[i]) {
                                    case '\r':
                                    case '\n':
                                        p = i + 1;
                                        continue loop;
                                }
                            }

                            i++;
                        }

                        p = i;
                    }

                    break loop;

                case '-':
                case '#':
                    if (sql[i] == '-' && i + 1 < sql.length && sql[i + 1] == '-' ||
                        sql[i] == '#' && SUPPORTS_HASH_COMMENT_SYNTAX.contains(parseDialect())) {

                        if (sql[i] == '-')
                            i = i + 2;
                        else
                            i++;

                        while (i < sql.length) {
                            if (!(ignoreComment = peekIgnoreComment(ignoreComment, ignoreCommentStart, ignoreCommentStop, checkIgnoreComment, i))) {
                                switch (sql[i]) {
                                    case '\r':
                                    case '\n':
                                        p = i + 1;
                                        continue loop;
                                }
                            }

                            i++;
                        }

                        p = i;
                    }

                    break loop;

                    // TODO MySQL comments require a whitespace after --. Should we deal with this?
                    // TODO Some databases also support # as a single line comment character.

                default:
                    p = i;
                    break loop;
            }
        }

        if (blockCommentNestLevel > 0)
            throw exception("Nested block comment not properly closed");

        return p;
    }

    private final boolean peekIgnoreComment(
        boolean ignoreComment,
        String ignoreCommentStart,
        String ignoreCommentStop,
        boolean checkIgnoreComment,
        int i
    ) {

        if (checkIgnoreComment)
            if (!ignoreComment)
                ignoreComment = peek(ignoreCommentStart, i);
            else
                ignoreComment = !peek(ignoreCommentStop, i);

        return ignoreComment;
    }

    private final char upper(char c) {
        return c >= 'a' && c <= 'z' ? (char) (c - ('a' - 'A')) : c;
    }

    private static enum TruthValue {
        T_TRUE,
        T_FALSE,
        T_NULL;
    }

    private static enum ComputationalOperation {
        ANY_VALUE,
        AVG,
        MAX,
        MIN,
        SUM,
        PRODUCT,
        EVERY,
        ANY,
        SOME,
        COUNT,
        STDDEV_POP,
        STDDEV_SAMP,
        VAR_POP,
        VAR_SAMP,
        MEDIAN,
//        COLLECT,
//        FUSION,
//        INTERSECTION;
    }

    private static final String[] KEYWORDS_IN_STATEMENTS = {
        "ALTER",
        "BEGIN",
        "COMMENT",
        "CREATE",
        "DECLARE",
        "DELETE",
        "DROP",
        "END", // In T-SQL, semicolons are optional, so a T-SQL END clause might appear
        "GO", // The T-SQL statement batch delimiter, not a SELECT keyword
        "GRANT",
        "INSERT",
        "MERGE",
        "RENAME",
        "REVOKE",
        "SELECT",
        "SET",
        "TRUNCATE",
        "UPDATE",
        "USE",
        "VALUES",
        "WITH",
    };

    private static final String[] KEYWORDS_IN_SELECT = {
        "CONNECT BY",
        "EXCEPT",
        "FETCH FIRST",
        "FETCH NEXT",
        "FOR JSON",
        "FOR KEY SHARE",
        "FOR NO KEY UPDATE",
        "FOR SHARE",
        "FOR UPDATE",
        "FOR XML",
        "FROM",
        "GROUP BY",
        "HAVING",
        "INTERSECT",
        "INTO",
        "LIMIT",
        "MINUS",
        "OFFSET",
        "ON",
        "ORDER BY",
        "PARTITION BY",
        "QUALIFY",
        "RETURNING",
        "ROWS",
        "START WITH",
        "UNION",
        "WHERE",
        "WINDOW",
    };

    private static final String[] KEYWORDS_IN_FROM = {
        "CROSS APPLY",
        "CROSS JOIN",
        "FULL JOIN",
        "FULL OUTER JOIN",
        "INNER JOIN",
        "JOIN",
        "LEFT ANTI JOIN",
        "LEFT JOIN",
        "LEFT OUTER JOIN",
        "LEFT SEMI JOIN",
        "NATURAL FULL JOIN",
        "NATURAL FULL OUTER JOIN",
        "NATURAL INNER JOIN",
        "NATURAL JOIN",
        "NATURAL LEFT JOIN",
        "NATURAL LEFT OUTER JOIN",
        "NATURAL RIGHT JOIN",
        "NATURAL RIGHT OUTER JOIN",
        "ON",
        "OUTER APPLY",
        "PARTITION BY",
        "RIGHT ANTI JOIN",
        "RIGHT JOIN",
        "RIGHT OUTER JOIN",
        "RIGHT SEMI JOIN",
        "STRAIGHT_JOIN",
        "USING"
    };

    private static final String[] KEYWORDS_IN_SELECT_FROM;

    static {
        Set<String> set = new TreeSet<>(asList(KEYWORDS_IN_FROM));
        set.addAll(asList(KEYWORDS_IN_STATEMENTS));

        set.addAll(asList(
            "CONNECT BY",
            "CREATE",
            "EXCEPT",
            "FETCH FIRST",
            "FETCH NEXT",
            "FOR JSON",
            "FOR KEY SHARE",
            "FOR NO KEY UPDATE",
            "FOR SHARE",
            "FOR UPDATE",
            "FOR XML",
            "GROUP BY",
            "HAVING",
            "INTERSECT",
            "INTO",
            "LIMIT",
            "MINUS",
            "OFFSET",
            "ORDER BY",
            "QUALIFY",
            "RETURNING",
            "ROWS",
            "START WITH",
            "UNION",
            "WHERE",
            "WINDOW"
        ));

        KEYWORDS_IN_SELECT_FROM = set.toArray(EMPTY_STRING);
    };

    private static final String[] KEYWORDS_IN_UPDATE_FROM;

    static {
        Set<String> set = new TreeSet<>(asList(KEYWORDS_IN_FROM));
        set.addAll(asList("FROM", "SET", "WHERE", "ORDER BY", "LIMIT", "RETURNING"));
        KEYWORDS_IN_UPDATE_FROM = set.toArray(EMPTY_STRING);
    };

    private static final String[] KEYWORDS_IN_DELETE_FROM;

    static {
        Set<String> set = new TreeSet<>(asList(KEYWORDS_IN_FROM));
        set.addAll(asList("FROM", "USING", "WHERE", "ORDER BY", "LIMIT", "RETURNING"));
        set.addAll(asList(KEYWORDS_IN_STATEMENTS));
        KEYWORDS_IN_DELETE_FROM = set.toArray(EMPTY_STRING);
    };

    private static final String[] PIVOT_KEYWORDS      = {
        "FOR"
    };

    private static final DDLQuery                 IGNORE                 = new IgnoreQuery();
    private static final Query                    IGNORE_NO_DELIMITER    = new IgnoreQuery();

    private static final class IgnoreQuery extends AbstractDDLQuery {
        IgnoreQuery() {
            super(CTX.configuration());
        }

        @Override
        public void accept(Context<?> ctx) {
            ctx.sql("/* ignored */");
        }
    }

    private final DSLContext                      dsl;
    private final Locale                          locale;
    private final Meta                            meta;
    private final char[]                          sql;
    private final ParseWithMetaLookups            metaLookups;
    private boolean                               metaLookupsForceIgnore;
    private final Consumer<Param<?>>              bindParamListener;
    private int                                   position               = 0;
    private boolean                               ignoreHints            = true;
    private final Object[]                        bindings;
    private int                                   bindIndex              = 0;
    private final Map<String, Param<?>>           bindParams             = new LinkedHashMap<>();
    private String                                delimiter              = ";";
    private final ScopeStack<Name, Table<?>>      tableScope             = new ScopeStack<>(null);
    private final ScopeStack<Name, Field<?>>      fieldScope             = new ScopeStack<>(null);
    private final ScopeStack<Name, FieldProxy<?>> lookupFields           = new ScopeStack<>(null);
    private boolean                               scopeClear             = false;
    private LanguageContext                       languageContext        = LanguageContext.QUERY;
    private EnumSet<FunctionKeyword>              forbidden       = EnumSet.noneOf(FunctionKeyword.class);







    /**
     * Keywords that can appear as syntactic tokens in functions are forbidden
     * in non-parenthesised expressions passed as function arguments.
     */
    enum FunctionKeyword {
        FK_AND,
        FK_IN
    }

    DefaultParseContext(
        DSLContext dsl,
        Meta meta,
        ParseWithMetaLookups metaLookups,
        String sqlString,
        Object[] bindings
    ) {
        super(dsl.configuration());

        this.dsl = dsl;
        this.locale = parseLocale(dsl.settings());
        this.meta = meta;
        this.metaLookups = metaLookups;
        this.sql = sqlString != null ? sqlString.toCharArray() : new char[0];
        this.bindings = bindings;

        // [#8722] This is an undocumented flag that allows for collecting parameters from the parser
        //         Do not rely on this flag. It will change incompatibly in the future.
        this.bindParamListener = (Consumer<Param<?>>) dsl.configuration().data("org.jooq.parser.param-collector");



        parseWhitespaceIf();
    }

    @Override
    public final SQLDialect parseDialect() {
        SQLDialect result = settings().getParseDialect();

        if (result == null)
            result = SQLDialect.DEFAULT;

        return result;
    }

    @Override
    public final SQLDialect parseFamily() {
        return parseDialect().family();
    }

    @Override
    public final LanguageContext languageContext() {
        return languageContext;
    }

    private final ParseWithMetaLookups metaLookups() {
        if (metaLookupsForceIgnore())
            return ParseWithMetaLookups.OFF;






        else
            return this.metaLookups;
    }

    private final boolean metaLookupsForceIgnore() {
        return this.metaLookupsForceIgnore;
    }

    private final DefaultParseContext metaLookupsForceIgnore(boolean m) {
        this.metaLookupsForceIgnore = m;
        return this;
    }

    private final boolean proEdition() {
        return configuration().commercial();
    }

    private final boolean requireProEdition() {
        if (!proEdition())
            throw exception("Feature only supported in pro edition");

        return true;
    }

    private final boolean requireUnsupportedSyntax() {
        if (dsl.configuration().settings().getParseUnsupportedSyntax() == ParseUnsupportedSyntax.FAIL)
            throw exception("Syntax not supported");

        return true;
    }

    private final String substring(int startPosition, int endPosition) {
        return new String(sql, startPosition, endPosition - startPosition);
    }

    private final ParserException internalError() {
        return exception("Internal Error");
    }

    private final ParserException expected(String object) {
        return init(new ParserException(mark(), object + " expected"));
    }

    private final ParserException expected(String... objects) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < objects.length; i++)
            if (i == 0)
                sb.append(objects[i]);
            // [#10169] Correct application of Oxford comma 🧐
            else if (i == 1 && objects.length == 2)
                sb.append(" or ").append(objects[i]);
            else if (i == objects.length - 1)
                sb.append(", or ").append(objects[i]);
            else
                sb.append(", ").append(objects[i]);

        return init(new ParserException(mark(), sb.toString() + " expected"));
    }

    private final ParserException notImplemented(String feature) {
        return notImplemented(feature, "https://github.com/jOOQ/jOOQ/issues/10171");
    }

    private final ParserException notImplemented(String feature, String link) {
        return init(new ParserException(mark(), feature + " not yet implemented. If you're interested in this feature, please comment on " + link));
    }

    private final ParserException unsupportedClause() {
        return init(new ParserException(mark(), "Unsupported clause"));
    }

    @Override
    public final ParserException exception(String message) {
        return init(new ParserException(mark(), message));
    }

    private final ParserException init(ParserException e) {
        int[] line = line();
        return e.position(position).line(line[0]).column(line[1]);
    }

    private final Object nextBinding() {
        if (bindIndex++ < bindings.length)
            return bindings[bindIndex - 1];
        else if (bindings.length == 0)
            return null;
        else
            throw exception("No binding provided for bind index " + bindIndex);
    }

    private final int[] line() {
        int line = 1;
        int column = 1;

        for (int i = 0; i < position; i++) {
            if (sql[i] == '\r') {
                line++;
                column = 1;

                if (i + 1 < sql.length && sql[i + 1] == '\n')
                    i++;
            }
            else if (sql[i] == '\n') {
                line++;
                column = 1;
            }
            else {
                column++;
            }
        }

        return new int[] { line, column };
    }

    private final char characterUpper() {
        return Character.toUpperCase(character());
    }

    @Override
    public final char character() {
        return character(position);
    }

    @Override
    public final char character(int pos) {
        return pos >= 0 && pos < sql.length ? sql[pos] : ' ';
    }

    private final char characterNextUpper() {
        return Character.toUpperCase(characterNext());
    }

    private final char characterNext() {
        return character(position + 1);
    }

    @Override
    public final int position() {
        return position;
    }

    @Override
    public final boolean position(int newPosition) {
        position = newPosition;
        return true;
    }

    private final boolean positionInc() {
        return positionInc(1);
    }

    private final boolean positionInc(int inc) {
        return position(position + inc);
    }

    private final String delimiter() {
        return delimiter;
    }

    private final void delimiter(String newDelimiter) {
        delimiter = newDelimiter;
    }

    private final boolean ignoreHints() {
        return ignoreHints;
    }

    private final void ignoreHints(boolean newIgnoreHints) {
        ignoreHints = newIgnoreHints;
    }

    private final boolean isOperatorPart(int pos) {
        return isOperatorPart(character(pos));
    }

    private final boolean isOperatorPart(char character) {
        // Obtain all distinct, built-in PostgreSQL operator characters:
        // select distinct regexp_split_to_table(oprname, '') from pg_catalog.pg_operator order by 1;
        switch (character) {
            case '!':
            case '#':
            case '%':
            case '&':
            case '*':
            case '+':
            case '-':
            case '/':
            case ':':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case '^':
            case '|':
            case '~':
                return true;
        }

        return false;
    }

    private final boolean isIdentifierPart() {
        return isIdentifierPart(character());
    }

    private final boolean isIdentifierPart(int pos) {
        return isIdentifierPart(character(pos));
    }

    private final boolean isIdentifierPart(char character) {
        return Character.isJavaIdentifierPart(character)
           || ((character == '@'
           ||   character == '#')
           &&   character != delimiter.charAt(0));
    }

    private final boolean hasMore() {
        return position < sql.length;
    }

    private final boolean hasMore(int offset) {
        return position + offset < sql.length;
    }

    private final boolean done() {
        return position >= sql.length && (bindings.length == 0 || bindings.length == bindIndex);
    }

    private final <Q extends QueryPart> Q done(String message, Q result) {
        if (done())
            return notify(result);
        else
            throw exception(message);
    }

    private final <Q extends QueryPart> Q notify(Q result) {
        if (bindParamListener != null) {
            final Map<String, Param<?>> params = new LinkedHashMap<>();

            // [#8722]  TODO Replace this by a public SPI
            // [#11054] Use a VisitListener to find actual Params in the expression tree,
            //          which may have more refined DataTypes attached to them, from context
            dsl.configuration().deriveAppending(onVisitStart(ctx -> {
                if (ctx.queryPart() instanceof Param) {
                    Param<?> p = (Param<?>) ctx.queryPart();

                    if (!params.containsKey(p.getParamName()))
                        params.put(p.getParamName(), p);
                }
            })).dsl().render(result);

            for (String name : bindParams.keySet())
                bindParamListener.accept(params.get(name));
        }

        return result;
    }

    private final String mark() {
        int[] line = line();
        return "[" + line[0] + ":" + line[1] + "] "
              + (position > 50 ? "..." : "")
              + substring(Math.max(0, position - 50), position)
              + "[*]"
              + substring(position, Math.min(sql.length, position + 80))
              + (sql.length > position + 80 ? "..." : "");
    }

    private final void scope(Table<?> table) {
        tableScope.set(table.getQualifiedName(), table);
    }

    private final void scope(Field<?> field) {
        fieldScope.set(field.getQualifiedName(), field);
    }

    private final void scopeStart() {
        tableScope.scopeStart();
        fieldScope.scopeStart();
        lookupFields.scopeStart();
        lookupFields.setAll(null);
    }

    private final void scopeEnd(Query scopeOwner) {
        List<FieldProxy<?>> retain = new ArrayList<>();

        for (FieldProxy<?> lookup : lookupFields) {
            Value<Field<?>> found = null;

            for (Field<?> f : fieldScope) {
                if (f.getName().equals(lookup.getName())) {
                    if (found != null) {
                        position(lookup.position());
                        throw exception("Ambiguous field identifier");
                    }

                    // TODO: Does this instance of "found" really interact with the one below?
                    found = new Value<>(0, f);
                }
            }

            found = resolveInTableScope(tableScope.valueIterable(), lookup.getQualifiedName(), lookup, found);

            if (found != null && !(found.value() instanceof FieldProxy)) {
                lookup.delegate((AbstractField) found.value());
            }
            else {
                lookup.scopeOwner(scopeOwner);
                retain.add(lookup);
            }
        }

        lookupFields.scopeEnd();
        tableScope.scopeEnd();
        fieldScope.scopeEnd();

        for (FieldProxy<?> r : retain)
            if (lookupFields.get(r.getQualifiedName()) == null)
                if (lookupFields.inScope())
                    lookupFields.set(r.getQualifiedName(), r);
                else
                    unknownField(r);
    }

    private final Value<Field<?>> resolveInTableScope(Iterable<Value<Table<?>>> tables, Name lookupName, FieldProxy<?> lookup, Value<Field<?>> found) {

        tableScopeLoop:
        for (Value<Table<?>> t : tables) {
            Value<Field<?>> f;

            if (t.value() instanceof JoinTable) {
                found = resolveInTableScope(
                    asList(
                        new Value<>(t.scopeLevel(), ((JoinTable) t.value()).lhs),
                        new Value<>(t.scopeLevel(), ((JoinTable) t.value()).rhs)
                    ),
                    lookupName, lookup, found
                );
            }
            else if (lookupName.qualified()) {

                // Additional tests:
                // - More complex search paths
                // - Ambiguities from multiple search paths, when S1.T and S2.T conflict
                // - Test fully qualified column names vs partially qualified column names
                Name q = lookupName.qualifier();
                boolean x = q.qualified();
                if (x && q.equals(t.value().getQualifiedName()) || !x && q.last().equals(t.value().getName()))
                    if ((found = Value.of(t.scopeLevel(), t.value().field(lookup.getName()))) != null)
                        break tableScopeLoop;








            }
            else if ((f = Value.of(t.scopeLevel(), t.value().field(lookup.getName()))) != null) {
                if (found == null || found.scopeLevel() < f.scopeLevel()) {
                    found = f;
                }
                else {
                    position(lookup.position());
                    throw exception("Ambiguous field identifier");
                }
            }
        }

        return found;
    }

    private final void scopeClear() {
        scopeClear = true;
    }

    private final void scopeResolve() {
        if (!lookupFields.isEmpty())
            unknownField(lookupFields.iterator().next());
    }

    private final void unknownField(FieldProxy<?> field) {
        if (!scopeClear) {






























            if (metaLookups() == THROW_ON_FAILURE) {
                position(field.position());
                throw exception("Unknown field identifier");
            }
        }
    }

    private final Table<?> lookupTable(int positionBeforeName, Name name) {
        if (meta != null) {
            List<Table<?>> tables;

            // [#8616] If name is not qualified, names reported by meta must be
            //         unqualified as well
            if (!(tables = meta.getTables(name)).isEmpty())
                for (Table<?> table : tables)
                    if (table.getQualifiedName().qualified() == name.qualified())
                        return tables.get(0);

            // [#8616] If name is not qualified, try the search path as well
            if (!name.qualified())
                for (ParseSearchSchema schema : settings().getParseSearchPath())
                    if ((tables = meta.getTables(name(schema.getCatalog(), schema.getSchema()).append(name))).size() == 1)
                        return tables.get(0);
        }

        if (metaLookups() == THROW_ON_FAILURE) {
            position(positionBeforeName);
            throw exception("Unknown table identifier");
        }

        return table(name);
    }

    private final Field<?> lookupField(int positionBeforeName, Name name) {
        if (metaLookups() == ParseWithMetaLookups.OFF || lookupFields.scopeLevel() < 0)
            return field(name);

        FieldProxy<?> field = lookupFields.get(name);
        if (field == null)
            lookupFields.set(name, field = new FieldProxy<>((AbstractField<Object>) field(name), positionBeforeName));

        return field;
    }

    @Override
    public String toString() {
        return mark();
    }
}