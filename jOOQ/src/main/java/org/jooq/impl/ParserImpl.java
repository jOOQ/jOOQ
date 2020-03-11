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
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.NOT_IN;
import static org.jooq.JoinType.JOIN;
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.conf.ParseWithMetaLookups.IGNORE_ON_FAILURE;
import static org.jooq.conf.ParseWithMetaLookups.THROW_ON_FAILURE;
import static org.jooq.conf.SettingsTools.parseLocale;
import static org.jooq.impl.AbstractName.NO_NAME;
import static org.jooq.impl.DSL.abs;
import static org.jooq.impl.DSL.acos;
import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.any;
import static org.jooq.impl.DSL.arrayAgg;
import static org.jooq.impl.DSL.arrayAggDistinct;
import static org.jooq.impl.DSL.ascii;
import static org.jooq.impl.DSL.asin;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.atan;
import static org.jooq.impl.DSL.atan2;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.avgDistinct;
import static org.jooq.impl.DSL.bitAnd;
import static org.jooq.impl.DSL.bitCount;
import static org.jooq.impl.DSL.bitLength;
import static org.jooq.impl.DSL.bitNand;
import static org.jooq.impl.DSL.bitNor;
import static org.jooq.impl.DSL.bitNot;
import static org.jooq.impl.DSL.bitOr;
import static org.jooq.impl.DSL.bitXNor;
import static org.jooq.impl.DSL.bitXor;
import static org.jooq.impl.DSL.boolOr;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.catalog;
import static org.jooq.impl.DSL.ceil;
import static org.jooq.impl.DSL.century;
import static org.jooq.impl.DSL.charLength;
import static org.jooq.impl.DSL.characterSet;
import static org.jooq.impl.DSL.check;
import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.collation;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.connectByIsCycle;
import static org.jooq.impl.DSL.connectByIsLeaf;
import static org.jooq.impl.DSL.connectByRoot;
import static org.jooq.impl.DSL.constraint;
// ...
// ...
import static org.jooq.impl.DSL.cos;
import static org.jooq.impl.DSL.cosh;
import static org.jooq.impl.DSL.cot;
import static org.jooq.impl.DSL.coth;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.countDistinct;
import static org.jooq.impl.DSL.cube;
import static org.jooq.impl.DSL.cumeDist;
import static org.jooq.impl.DSL.currentDate;
import static org.jooq.impl.DSL.currentSchema;
import static org.jooq.impl.DSL.currentTime;
import static org.jooq.impl.DSL.currentTimestamp;
import static org.jooq.impl.DSL.currentUser;
import static org.jooq.impl.DSL.date;
import static org.jooq.impl.DSL.day;
import static org.jooq.impl.DSL.dayOfWeek;
import static org.jooq.impl.DSL.dayOfYear;
import static org.jooq.impl.DSL.decade;
// ...
import static org.jooq.impl.DSL.defaultValue;
import static org.jooq.impl.DSL.default_;
import static org.jooq.impl.DSL.deg;
import static org.jooq.impl.DSL.denseRank;
import static org.jooq.impl.DSL.epoch;
import static org.jooq.impl.DSL.every;
import static org.jooq.impl.DSL.exists;
// ...
// ...
import static org.jooq.impl.DSL.exp;
import static org.jooq.impl.DSL.extract;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.firstValue;
import static org.jooq.impl.DSL.floor;
// ...
import static org.jooq.impl.DSL.foreignKey;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.generateSeries;
import static org.jooq.impl.DSL.greatest;
import static org.jooq.impl.DSL.grouping;
// ...
import static org.jooq.impl.DSL.groupingSets;
import static org.jooq.impl.DSL.groupsBetweenCurrentRow;
import static org.jooq.impl.DSL.groupsBetweenFollowing;
import static org.jooq.impl.DSL.groupsBetweenPreceding;
import static org.jooq.impl.DSL.groupsBetweenUnboundedFollowing;
import static org.jooq.impl.DSL.groupsBetweenUnboundedPreceding;
import static org.jooq.impl.DSL.groupsCurrentRow;
import static org.jooq.impl.DSL.groupsFollowing;
import static org.jooq.impl.DSL.groupsPreceding;
import static org.jooq.impl.DSL.groupsUnboundedFollowing;
import static org.jooq.impl.DSL.groupsUnboundedPreceding;
import static org.jooq.impl.DSL.hour;
// ...
import static org.jooq.impl.DSL.ifnull;
import static org.jooq.impl.DSL.iif;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.isnull;
import static org.jooq.impl.DSL.isoDayOfWeek;
import static org.jooq.impl.DSL.jsonEntry;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.lag;
import static org.jooq.impl.DSL.lastValue;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.lead;
import static org.jooq.impl.DSL.least;
import static org.jooq.impl.DSL.left;
import static org.jooq.impl.DSL.length;
import static org.jooq.impl.DSL.level;
import static org.jooq.impl.DSL.list;
import static org.jooq.impl.DSL.listAgg;
import static org.jooq.impl.DSL.ln;
import static org.jooq.impl.DSL.log;
// ...
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.lpad;
import static org.jooq.impl.DSL.ltrim;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.maxDistinct;
import static org.jooq.impl.DSL.md5;
import static org.jooq.impl.DSL.median;
import static org.jooq.impl.DSL.microsecond;
import static org.jooq.impl.DSL.mid;
import static org.jooq.impl.DSL.millennium;
import static org.jooq.impl.DSL.millisecond;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.minDistinct;
import static org.jooq.impl.DSL.minute;
import static org.jooq.impl.DSL.mode;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.now;
import static org.jooq.impl.DSL.nthValue;
import static org.jooq.impl.DSL.ntile;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.nvl2;
import static org.jooq.impl.DSL.octetLength;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.orderBy;
import static org.jooq.impl.DSL.overlay;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.percentRank;
import static org.jooq.impl.DSL.percentileCont;
import static org.jooq.impl.DSL.percentileDisc;
import static org.jooq.impl.DSL.pi;
import static org.jooq.impl.DSL.position;
import static org.jooq.impl.DSL.primaryKey;
import static org.jooq.impl.DSL.prior;
import static org.jooq.impl.DSL.privilege;
import static org.jooq.impl.DSL.product;
import static org.jooq.impl.DSL.productDistinct;
import static org.jooq.impl.DSL.quarter;
import static org.jooq.impl.DSL.rad;
import static org.jooq.impl.DSL.rand;
import static org.jooq.impl.DSL.rangeBetweenCurrentRow;
import static org.jooq.impl.DSL.rangeBetweenFollowing;
import static org.jooq.impl.DSL.rangeBetweenPreceding;
import static org.jooq.impl.DSL.rangeBetweenUnboundedFollowing;
import static org.jooq.impl.DSL.rangeBetweenUnboundedPreceding;
import static org.jooq.impl.DSL.rangeCurrentRow;
import static org.jooq.impl.DSL.rangeFollowing;
import static org.jooq.impl.DSL.rangePreceding;
import static org.jooq.impl.DSL.rangeUnboundedFollowing;
import static org.jooq.impl.DSL.rangeUnboundedPreceding;
import static org.jooq.impl.DSL.rank;
import static org.jooq.impl.DSL.ratioToReport;
import static org.jooq.impl.DSL.regrAvgX;
import static org.jooq.impl.DSL.regrAvgY;
import static org.jooq.impl.DSL.regrCount;
import static org.jooq.impl.DSL.regrIntercept;
import static org.jooq.impl.DSL.regrR2;
import static org.jooq.impl.DSL.regrSXX;
import static org.jooq.impl.DSL.regrSXY;
import static org.jooq.impl.DSL.regrSYY;
import static org.jooq.impl.DSL.regrSlope;
// ...
import static org.jooq.impl.DSL.replace;
import static org.jooq.impl.DSL.reverse;
import static org.jooq.impl.DSL.right;
import static org.jooq.impl.DSL.rollup;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.rownum;
import static org.jooq.impl.DSL.rowsBetweenCurrentRow;
import static org.jooq.impl.DSL.rowsBetweenFollowing;
import static org.jooq.impl.DSL.rowsBetweenPreceding;
import static org.jooq.impl.DSL.rowsBetweenUnboundedFollowing;
import static org.jooq.impl.DSL.rowsBetweenUnboundedPreceding;
import static org.jooq.impl.DSL.rowsCurrentRow;
import static org.jooq.impl.DSL.rowsFollowing;
import static org.jooq.impl.DSL.rowsPreceding;
import static org.jooq.impl.DSL.rowsUnboundedFollowing;
import static org.jooq.impl.DSL.rowsUnboundedPreceding;
import static org.jooq.impl.DSL.rpad;
import static org.jooq.impl.DSL.rtrim;
import static org.jooq.impl.DSL.schema;
import static org.jooq.impl.DSL.second;
import static org.jooq.impl.DSL.sequence;
import static org.jooq.impl.DSL.shl;
import static org.jooq.impl.DSL.shr;
import static org.jooq.impl.DSL.sign;
import static org.jooq.impl.DSL.sin;
import static org.jooq.impl.DSL.sinh;
import static org.jooq.impl.DSL.space;
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.DSL.sqrt;
import static org.jooq.impl.DSL.stddevPop;
import static org.jooq.impl.DSL.stddevSamp;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.sumDistinct;
import static org.jooq.impl.DSL.sysConnectByPath;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.tan;
import static org.jooq.impl.DSL.tanh;
import static org.jooq.impl.DSL.time;
import static org.jooq.impl.DSL.timestamp;
import static org.jooq.impl.DSL.timezone;
import static org.jooq.impl.DSL.timezoneHour;
import static org.jooq.impl.DSL.timezoneMinute;
import static org.jooq.impl.DSL.toDate;
import static org.jooq.impl.DSL.toTimestamp;
import static org.jooq.impl.DSL.translate;
import static org.jooq.impl.DSL.trim;
import static org.jooq.impl.DSL.trunc;
import static org.jooq.impl.DSL.unique;
import static org.jooq.impl.DSL.unnest;
import static org.jooq.impl.DSL.user;
import static org.jooq.impl.DSL.values0;
// ...
import static org.jooq.impl.DSL.varPop;
import static org.jooq.impl.DSL.varSamp;
import static org.jooq.impl.DSL.week;
import static org.jooq.impl.DSL.when;
// ...
import static org.jooq.impl.DSL.year;
import static org.jooq.impl.DSL.zero;
import static org.jooq.impl.JSONNullClause.ABSENT_ON_NULL;
import static org.jooq.impl.JSONNullClause.NULL_ON_NULL;
import static org.jooq.impl.Keywords.K_DELETE;
import static org.jooq.impl.Keywords.K_INSERT;
import static org.jooq.impl.Keywords.K_SELECT;
import static org.jooq.impl.Keywords.K_UPDATE;
import static org.jooq.impl.ParserImpl.Type.A;
import static org.jooq.impl.ParserImpl.Type.B;
import static org.jooq.impl.ParserImpl.Type.D;
import static org.jooq.impl.ParserImpl.Type.J;
import static org.jooq.impl.ParserImpl.Type.N;
import static org.jooq.impl.ParserImpl.Type.S;
import static org.jooq.impl.ParserImpl.Type.X;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.Tools.EMPTY_BYTE;
import static org.jooq.impl.Tools.EMPTY_COLLECTION;
import static org.jooq.impl.Tools.EMPTY_COMMON_TABLE_EXPRESSION;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.EMPTY_NAME;
import static org.jooq.impl.Tools.EMPTY_QUERYPART;
import static org.jooq.impl.Tools.EMPTY_ROW;
import static org.jooq.impl.Tools.EMPTY_SORTFIELD;
import static org.jooq.impl.Tools.normaliseNameCase;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.AggregateFilterStep;
import org.jooq.AggregateFunction;
import org.jooq.AlterIndexFinalStep;
import org.jooq.AlterIndexStep;
import org.jooq.AlterSchemaFinalStep;
import org.jooq.AlterSchemaStep;
import org.jooq.AlterSequenceFlagsStep;
import org.jooq.AlterSequenceStep;
import org.jooq.AlterTableAddStep;
import org.jooq.AlterTableDropStep;
import org.jooq.AlterTableFinalStep;
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
import org.jooq.CreateIndexFinalStep;
import org.jooq.CreateIndexIncludeStep;
import org.jooq.CreateIndexStep;
import org.jooq.CreateIndexWhereStep;
import org.jooq.CreateSequenceFlagsStep;
import org.jooq.CreateTableColumnStep;
import org.jooq.CreateTableCommentStep;
import org.jooq.CreateTableConstraintStep;
import org.jooq.CreateTableOnCommitStep;
import org.jooq.CreateTableStorageStep;
import org.jooq.CreateTableWithDataStep;
import org.jooq.DDLQuery;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.DatePart;
// ...
import org.jooq.Delete;
import org.jooq.DeleteLimitStep;
import org.jooq.DeleteOrderByStep;
import org.jooq.DeleteReturningStep;
import org.jooq.DeleteUsingStep;
import org.jooq.DeleteWhereStep;
import org.jooq.DerivedColumnList;
import org.jooq.DropIndexCascadeStep;
import org.jooq.DropIndexFinalStep;
import org.jooq.DropIndexOnStep;
import org.jooq.DropSchemaFinalStep;
import org.jooq.DropSchemaStep;
import org.jooq.DropTableFinalStep;
import org.jooq.DropTableStep;
import org.jooq.DropTypeFinalStep;
import org.jooq.DropTypeStep;
import org.jooq.DropViewFinalStep;
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
import org.jooq.Insert;
import org.jooq.InsertOnConflictDoUpdateStep;
import org.jooq.InsertOnConflictWhereStep;
import org.jooq.InsertOnDuplicateStep;
import org.jooq.InsertReturningStep;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStepN;
import org.jooq.JSON;
import org.jooq.JSONArrayAggNullStep;
import org.jooq.JSONArrayAggOrderByStep;
import org.jooq.JSONArrayNullStep;
import org.jooq.JSONEntry;
import org.jooq.JSONObjectAggNullStep;
import org.jooq.JSONObjectNullStep;
import org.jooq.JoinType;
import org.jooq.Keyword;
// ...
import org.jooq.LikeEscapeStep;
// ...
import org.jooq.Merge;
import org.jooq.MergeFinalStep;
import org.jooq.MergeMatchedStep;
import org.jooq.MergeNotMatchedStep;
import org.jooq.MergeUsingStep;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Name.Quoted;
import org.jooq.OrderedAggregateFunction;
import org.jooq.OrderedAggregateFunctionOfDeferredType;
import org.jooq.Param;
import org.jooq.Parameter;
import org.jooq.Parser;
// ...
// ...
import org.jooq.Privilege;
// ...
import org.jooq.QualifiedAsterisk;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
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
import org.jooq.TruncateFinalStep;
import org.jooq.TruncateIdentityStep;
import org.jooq.Update;
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
import org.jooq.conf.ParseSearchSchema;
import org.jooq.conf.ParseUnknownFunctions;
import org.jooq.conf.ParseUnsupportedSyntax;
import org.jooq.conf.ParseWithMetaLookups;
import org.jooq.conf.RenderKeywordCase;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.tools.StringUtils;
import org.jooq.tools.reflect.Reflect;
import org.jooq.types.DayToSecond;
import org.jooq.types.Interval;
import org.jooq.types.YearToMonth;
import org.jooq.types.YearToSecond;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class ParserImpl implements Parser {






    private final DSLContext             dsl;
    private final ParseWithMetaLookups   metaLookups;
    private final Meta                   meta;

    ParserImpl(Configuration configuration) {
        this.dsl = DSL.using(configuration);
        this.metaLookups = configuration.settings().getParseWithMetaLookups();
        this.meta = metaLookups == IGNORE_ON_FAILURE || metaLookups == THROW_ON_FAILURE ? dsl.meta() : null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Top level parsing
    // -----------------------------------------------------------------------------------------------------------------

    private final ParserContext ctx(String sql, Object... bindings) {
        ParserContext ctx = new ParserContext(dsl, meta, metaLookups, sql, bindings);
        parseWhitespaceIf(ctx);
        return ctx;
    }

    @Override
    public final Queries parse(String sql) {
        return parse(sql, new Object[0]);
    }

    @Override
    public final Queries parse(String sql, Object... bindings) {
        ParserContext ctx = ctx(sql, bindings);
        List<Query> result = new ArrayList<>();
        Query query;

        do {
            parseDelimiterSpecifications(ctx);
            while (parseDelimiterIf(ctx));

            query = patchParsedQuery(ctx, parseQuery(ctx, false, false));
            if (query == IGNORE || query == IGNORE_NO_DELIMITER)
                continue;
            if (query != null)
                result.add(query);
        }
        while (query == IGNORE_NO_DELIMITER || parseDelimiterIf(ctx));

        ctx.done("Unexpected token or missing query delimiter");
        return dsl.queries(result);
    }

    private static final Pattern P_SEARCH_PATH = Pattern.compile("(?i:select\\s+(pg_catalog\\s*\\.\\s*)?set_config\\s*\\(\\s*'search_path'\\s*,\\s*'([^']*)'\\s*,\\s*\\w+\\s*\\))");

    private final Query patchParsedQuery(ParserContext ctx, Query query) {

        // [#8910] Some statements can be parsed differently when we know we're
        //         parsing them for the DDLDatabase. This method patches these
        //         statements.
        if (TRUE.equals(ctx.configuration().data("org.jooq.ddl.parse-for-ddldatabase"))) {
            if (query instanceof Select) {
                String sql =
                ctx.configuration().derive(SettingsTools.clone(ctx.configuration().settings())
                    .withRenderFormatted(false)
                    .withRenderKeywordCase(RenderKeywordCase.LOWER)
                    .withRenderNameCase(RenderNameCase.LOWER)
                    .withRenderQuotedNames(RenderQuotedNames.NEVER)
                    .withRenderSchema(false))
                    .dsl()
                    .render(query);

                // [#8910] special treatment for PostgreSQL pg_dump's curious
                //         usage of the SET SCHEMA command
                Matcher matcher = P_SEARCH_PATH.matcher(sql);
                String schema;
                if (matcher.find())
                    if (!StringUtils.isBlank(schema = matcher.group(2)))
                        return ctx.configuration().dsl().setSchema(schema);
                    else
                        return IGNORE;
            }
        }

        return query;
    }

    @Override
    public final Query parseQuery(String sql) {
        return parseQuery(sql, new Object[0]);
    }

    @Override
    public final Query parseQuery(String sql, Object... bindings) {
        ParserContext ctx = ctx(sql, bindings);
        Query result = parseQuery(ctx, false, false);

        ctx.done("Unexpected clause");
        return result;
    }

    @Override
    public final Statement parseStatement(String sql) {
        return parseStatement(sql, new Object[0]);
    }

    @Override
    public final Statement parseStatement(String sql, Object... bindings) {
        ParserContext ctx = ctx(sql, bindings);
        Statement result = parseStatementAndSemicolon(ctx);
        ctx.done("Unexpected content");
        return result;
    }
























    @Override
    public final ResultQuery<?> parseResultQuery(String sql) {
        return parseResultQuery(sql, new Object[0]);
    }

    @Override
    public final ResultQuery<?> parseResultQuery(String sql, Object... bindings) {
        ParserContext ctx = ctx(sql, bindings);
        ResultQuery<?> result = (ResultQuery<?>) parseQuery(ctx, true, false);

        ctx.done("Unexpected content after end of query input");
        return result;
    }

    @Override
    public final Select<?> parseSelect(String sql) {
        return parseSelect(sql, new Object[0]);
    }

    @Override
    public final Select<?> parseSelect(String sql, Object... bindings) {
        ParserContext ctx = ctx(sql, bindings);
        Select<?> result = (Select<?>) parseQuery(ctx, true, true);

        ctx.done("Unexpected content after end of query input");
        return result;
    }

    @Override
    public final Table<?> parseTable(String sql) {
        return parseTable(sql, new Object[0]);
    }

    @Override
    public final Table<?> parseTable(String sql, Object... bindings) {
        ParserContext ctx = ctx(sql, bindings);
        Table<?> result = parseTable(ctx);

        ctx.done("Unexpected content after end of table input");
        return result;
    }

    @Override
    public final Field<?> parseField(String sql) {
        return parseField(sql, new Object[0]);
    }

    @Override
    public final Field<?> parseField(String sql, Object... bindings) {
        ParserContext ctx = ctx(sql, bindings);
        Field<?> result = parseField(ctx);

        ctx.done("Unexpected content after end of field input");
        return result;
    }

    @Override
    public final Row parseRow(String sql) {
        return parseRow(sql, new Object[0]);
    }

    @Override
    public final Row parseRow(String sql, Object... bindings) {
        ParserContext ctx = ctx(sql, bindings);
        Row result = parseRow(ctx);

        ctx.done("Unexpected content after end of row input");
        return result;
    }

    @Override
    public final Condition parseCondition(String sql) {
        return parseCondition(sql, new Object[0]);
    }

    @Override
    public final Condition parseCondition(String sql, Object... bindings) {
        ParserContext ctx = ctx(sql, bindings);
        Condition result = parseCondition(ctx);

        ctx.done("Unexpected content after end of condition input");
        return result;
    }

    @Override
    public final Name parseName(String sql) {
        return parseName(sql, new Object[0]);
    }

    @Override
    public final Name parseName(String sql, Object... bindings) {
        ParserContext ctx = ctx(sql, bindings);
        Name result = parseName(ctx);

        ctx.done("Unexpected content after end of name input");
        return result;
    }






























































    private static final void parseDelimiterSpecifications(ParserContext ctx) {
        while (parseKeywordIf(ctx, "DELIMITER"))
            ctx.delimiter(parseUntilEOL(ctx).trim());
    }

    private static final boolean parseDelimiterIf(ParserContext ctx) {
        if (parseIf(ctx, ctx.delimiter()))
            return true;

        if (peekKeyword(ctx, "GO")) {
            ctx.positionInc(2);
            String line = parseUntilEOLIf(ctx);

            if (line != null && !"".equals(line.trim()))
                throw ctx.exception("GO must be only token on line");

            parseWhitespaceIf(ctx);
            return true;
        }

        return false;
    }

    private static final Query parseQuery(ParserContext ctx, boolean parseResultQuery, boolean parseSelect) {
        if (ctx.done())
            return null;

        boolean metaLookupsForceIgnore = ctx.metaLookupsForceIgnore();
        try {
            switch (ctx.character()) {
                case 'a':
                case 'A':
                    if (!parseResultQuery && peekKeyword(ctx, "ALTER"))
                        return parseAlter(ctx.metaLookupsForceIgnore(true));

                    break;

                case 'b':
                case 'B':
                    if (!parseResultQuery && peekKeyword(ctx, "BEGIN"))
                        return parseBlock(ctx);

                    break;

                case 'c':
                case 'C':
                    if (!parseResultQuery && peekKeyword(ctx, "CREATE"))
                        return parseCreate(ctx.metaLookupsForceIgnore(true));
                    else if (!parseResultQuery && peekKeyword(ctx, "COMMENT ON"))
                        return parseCommentOn(ctx.metaLookupsForceIgnore(true));

                    break;

                case 'd':
                case 'D':
                    if (!parseResultQuery && peekKeyword(ctx, "DECLARE") && ctx.requireProEdition())
                        return parseBlock(ctx);
                    else if (!parseResultQuery && (peekKeyword(ctx, "DELETE") || peekKeyword(ctx, "DEL")))
                        return parseDelete(ctx, null);
                    else if (!parseResultQuery && peekKeyword(ctx, "DROP"))
                        return parseDrop(ctx.metaLookupsForceIgnore(true));
                    else if (!parseResultQuery && peekKeyword(ctx, "DO"))
                        return parseDo(ctx);

                    break;

                case 'e':
                case 'E':
                    if (!parseResultQuery && peekKeyword(ctx, "EXECUTE BLOCK AS BEGIN"))
                        return parseBlock(ctx);
                    else if (!parseResultQuery && peekKeyword(ctx, "EXEC"))
                        return parseExec(ctx);

                    break;

                case 'g':
                case 'G':
                    if (!parseResultQuery && peekKeyword(ctx, "GRANT"))
                        return parseGrant(ctx.metaLookupsForceIgnore(true));

                    break;

                case 'i':
                case 'I':
                    if (!parseResultQuery && (peekKeyword(ctx, "INSERT") || peekKeyword(ctx, "INS")))
                        return parseInsert(ctx, null);

                    break;

                case 'm':
                case 'M':
                    if (!parseResultQuery && peekKeyword(ctx, "MERGE"))
                        return parseMerge(ctx, null);

                    break;

                case 'r':
                case 'R':
                    if (!parseResultQuery && peekKeyword(ctx, "RENAME"))
                        return parseRename(ctx.metaLookupsForceIgnore(true));
                    else if (!parseResultQuery && peekKeyword(ctx, "REVOKE"))
                        return parseRevoke(ctx.metaLookupsForceIgnore(true));

                    break;

                case 's':
                case 'S':
                    if (peekKeyword(ctx, "SELECT") || peekKeyword(ctx, "SEL"))
                        return parseSelect(ctx);
                    else if (!parseResultQuery && peekKeyword(ctx, "SET"))
                        return parseSet(ctx);

                    break;

                case 't':
                case 'T':
                    if (!parseResultQuery && peekKeyword(ctx, "TRUNCATE"))
                        return parseTruncate(ctx);

                    break;

                case 'u':
                case 'U':
                    if (!parseResultQuery && (peekKeyword(ctx, "UPDATE") || peekKeyword(ctx, "UPD")))
                        return parseUpdate(ctx, null);
                    else if (!parseResultQuery && peekKeyword(ctx, "USE"))
                        return parseUse(ctx);

                    break;

                case 'v':
                case 'V':
                    if (!parseSelect && peekKeyword(ctx, "VALUES"))
                        return parseSelect(ctx);

                case 'w':
                case 'W':
                    if (peekKeyword(ctx, "WITH"))
                        return parseWith(ctx, parseSelect);

                    break;

                case '(':

                    // TODO are there other possible statement types?
                    if (peekKeyword(ctx, "WITH", false, true, false))
                        return parseWith(ctx, true);
                    else
                        return parseSelect(ctx);

                default:
                    break;
            }

            throw ctx.exception("Unsupported query type");
        }
        finally {
            ctx.metaLookupsForceIgnore(metaLookupsForceIgnore);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Statement parsing
    // -----------------------------------------------------------------------------------------------------------------

    private static final Query parseWith(ParserContext ctx, boolean parseSelect) {
        return parseWith(ctx, parseSelect, null);
    }

    private static final Query parseWith(ParserContext ctx, boolean parseSelect, Integer degree) {
        int parens = 0;
        while (parseIf(ctx, '('))
            parens++;

        parseKeyword(ctx, "WITH");
        boolean recursive = parseKeywordIf(ctx, "RECURSIVE");

        List<CommonTableExpression<?>> cte = new ArrayList<>();
        do {
            Name table = parseIdentifier(ctx);
            DerivedColumnList dcl = null;

            if (parseIf(ctx, '(')) {
                List<Name> columnNames = parseIdentifiers(ctx);
                parse(ctx, ')');
                dcl = table.fields(columnNames.toArray(EMPTY_NAME));
            }

            parseKeyword(ctx, "AS");
            parse(ctx, '(');
            Select<?> select = parseSelect(ctx);
            parse(ctx, ')');

            cte.add(dcl != null ? dcl.as(select) : table.as(select));
        }
        while (parseIf(ctx, ','));

        // TODO Better model API for WITH clause
        WithImpl with = (WithImpl) new WithImpl(ctx.dsl.configuration(), recursive).with(cte.toArray(EMPTY_COMMON_TABLE_EXPRESSION));
        Query result;
        if (!parseSelect && (peekKeyword(ctx, "DELETE") || peekKeyword(ctx, "DEL")))
            result = parseDelete(ctx, with);
        else if (!parseSelect && (peekKeyword(ctx, "INSERT") || peekKeyword(ctx, "INS")))
            result = parseInsert(ctx, with);
        else if (!parseSelect && peekKeyword(ctx, "MERGE"))
            result = parseMerge(ctx, with);
        else if (peekKeyword(ctx, "SELECT", false, true, false)
              || peekKeyword(ctx, "SEL", false, true, false))
            result = parseSelect(ctx, degree, with);
        else if (!parseSelect && (peekKeyword(ctx, "UPDATE") || peekKeyword(ctx, "UPD")))
            result = parseUpdate(ctx, with);
        else if ((parseWhitespaceIf(ctx) || true) && ctx.done())
            throw ctx.exception("Missing statement after WITH");
        else
            throw ctx.exception("Unsupported statement after WITH");

        while (parens --> 0)
            parse(ctx, ')');

        return result;
    }

    private static final Select<?> parseWithOrSelect(ParserContext ctx) {
        return parseWithOrSelect(ctx, null);
    }

    private static final Select<?> parseWithOrSelect(ParserContext ctx, Integer degree) {
        return peekKeyword(ctx, "WITH") ? (Select<?>) parseWith(ctx, true, degree) : parseSelect(ctx, degree, null);
    }

    private static final SelectQueryImpl<Record> parseSelect(ParserContext ctx) {
        return parseSelect(ctx, null, null);
    }

    private static final SelectQueryImpl<Record> parseSelect(ParserContext ctx, Integer degree) {
        return parseSelect(ctx, degree, null);
    }

    private static final SelectQueryImpl<Record> parseSelect(ParserContext ctx, Integer degree, WithImpl with) {
        SelectQueryImpl<Record> result = parseQueryExpressionBody(ctx, degree, with, null);
        List<SortField<?>> orderBy = null;

        if (parseKeywordIf(ctx, "ORDER")) {
            if (parseKeywordIf(ctx, "SIBLINGS BY")) {
                result.addOrderBy(parseSortSpecification(ctx));
                result.setOrderBySiblings(true);
            }
            else if (parseKeywordIf(ctx, "BY"))
                result.addOrderBy(orderBy = parseSortSpecification(ctx));
            else
                throw ctx.expected("SIBLINGS BY", "BY");
        }

        if (orderBy != null && parseKeywordIf(ctx, "SEEK")) {
            boolean before = parseKeywordIf(ctx, "BEFORE");
            if (!before)
                parseKeywordIf(ctx, "AFTER");

            List<Field<?>> seek = parseFields(ctx);
            if (seek.size() != orderBy.size())
                throw ctx.exception("ORDER BY size (" + orderBy.size() + ") and SEEK size (" + seek.size() + ") must match");

            if (before)
                result.addSeekBefore(seek);
            else
                result.addSeekAfter(seek);

            if (!result.getLimit().isApplicable())
                parseLimit(ctx, result, false);
        }
        else if (!result.getLimit().isApplicable()) {
            parseLimit(ctx, result, true);
        }

        forClause:
        if (parseKeywordIf(ctx, "FOR")) {
            if (parseKeywordIf(ctx, "KEY SHARE"))
                result.setForKeyShare(true);
            else if (parseKeywordIf(ctx, "NO KEY UPDATE"))
                result.setForNoKeyUpdate(true);
            else if (parseKeywordIf(ctx, "SHARE"))
                result.setForShare(true);
            else if (parseKeywordIf(ctx, "UPDATE"))
                result.setForUpdate(true);
            else if (parseKeywordIf(ctx, "XML") && ctx.requireProEdition()) {


































            }
            else if (parseKeywordIf(ctx, "JSON") && ctx.requireProEdition()) {




























            }
            else
                throw ctx.expected("UPDATE", "NO KEY UPDATE", "SHARE", "KEY SHARE", "XML", "JSON");

            if (parseKeywordIf(ctx, "OF"))
                result.setForUpdateOf(parseFields(ctx));

            if (parseKeywordIf(ctx, "NOWAIT"))
                result.setForUpdateNoWait();
            else if (parseKeywordIf(ctx, "WAIT") && ctx.requireProEdition())



                ;
            else if (parseKeywordIf(ctx, "SKIP LOCKED"))
                result.setForUpdateSkipLocked();
        }

        return result;
    }

    private static final void parseLimit(ParserContext ctx, SelectQueryImpl<Record> result, boolean offset) {
        boolean offsetStandard = false;
        boolean offsetPostgres = false;

        if (offset && parseKeywordIf(ctx, "OFFSET")) {
            result.addOffset(parseParenthesisedUnsignedIntegerOrBindVariable(ctx));

            if (parseKeywordIf(ctx, "ROWS") || parseKeywordIf(ctx, "ROW"))
                offsetStandard = true;

            // Ingres doesn't have a ROWS keyword after offset
            else if (peekKeyword(ctx, "FETCH"))
                offsetStandard = true;
            else
                offsetPostgres = true;
        }

        if (!offsetStandard && parseKeywordIf(ctx, "LIMIT")) {
            Param<Long> limit = parseParenthesisedUnsignedIntegerOrBindVariable(ctx);

            if (offsetPostgres) {
                result.addLimit(limit);

                if (parseKeywordIf(ctx, "PERCENT"))
                    result.setLimitPercent(true);

                if (parseKeywordIf(ctx, "WITH TIES"))
                    result.setWithTies(true);
            }
            else if (offset && parseIf(ctx, ',')) {
                result.addLimit(limit, parseParenthesisedUnsignedIntegerOrBindVariable(ctx));
            }
            else {
                if (parseKeywordIf(ctx, "PERCENT"))
                    result.setLimitPercent(true);

                if (parseKeywordIf(ctx, "WITH TIES"))
                    result.setWithTies(true);

                if (offset && parseKeywordIf(ctx, "OFFSET"))
                    result.addLimit(parseParenthesisedUnsignedIntegerOrBindVariable(ctx), limit);
                else
                    result.addLimit(limit);
            }
        }
        else if (!offsetPostgres && parseKeywordIf(ctx, "FETCH")) {
            parseAndGetKeyword(ctx, "FIRST", "NEXT");

            if (parseAndGetKeywordIf(ctx, "ROW", "ROWS") != null) {
                result.addLimit(inline(1L));
            }
            else {
                result.addLimit(parseParenthesisedUnsignedIntegerOrBindVariable(ctx));

                if (parseKeywordIf(ctx, "PERCENT"))
                    result.setLimitPercent(true);

                parseAndGetKeyword(ctx, "ROW", "ROWS");
            }

            if (parseKeywordIf(ctx, "WITH TIES"))
                result.setWithTies(true);
            else
                parseKeyword(ctx, "ONLY");
        }
    }

    private static final SelectQueryImpl<Record> parseQueryExpressionBody(ParserContext ctx, Integer degree, WithImpl with, SelectQueryImpl<Record> prefix) {
        SelectQueryImpl<Record> result = parseQueryTerm(ctx, degree, with, prefix);

        CombineOperator combine;
        while ((combine = parseCombineOperatorIf(ctx, false)) != null) {
            if (degree == null)
                degree = result.getSelect().size();

            switch (combine) {
                case UNION:
                    result = (SelectQueryImpl<Record>) result.union(parseQueryTerm(ctx, degree, null, null));
                    break;
                case UNION_ALL:
                    result = (SelectQueryImpl<Record>) result.unionAll(parseQueryTerm(ctx, degree, null, null));
                    break;
                case EXCEPT:
                    result = (SelectQueryImpl<Record>) result.except(parseQueryTerm(ctx, degree, null, null));
                    break;
                case EXCEPT_ALL:
                    result = (SelectQueryImpl<Record>) result.exceptAll(parseQueryTerm(ctx, degree, null, null));
                    break;
                default:
                    throw ctx.internalError();
            }
        }

        return result;
    }

    private static final SelectQueryImpl<Record> parseQueryTerm(ParserContext ctx, Integer degree, WithImpl with, SelectQueryImpl<Record> prefix) {
        SelectQueryImpl<Record> result = prefix != null ? prefix : parseQueryPrimary(ctx, degree, with);

        CombineOperator combine;
        while ((combine = parseCombineOperatorIf(ctx, true)) != null) {
            if (degree == null)
                degree = result.getSelect().size();

            switch (combine) {
                case INTERSECT:
                    result = (SelectQueryImpl<Record>) result.intersect(parseQueryPrimary(ctx, degree, null));
                    break;
                case INTERSECT_ALL:
                    result = (SelectQueryImpl<Record>) result.intersectAll(parseQueryPrimary(ctx, degree, null));
                    break;
                default:
                    throw ctx.internalError();
            }
        }

        return result;
    }

    private static final SelectQueryImpl<Record> parseQueryPrimary(ParserContext ctx, Integer degree, WithImpl with) {
        if (parseIf(ctx, '(')) {
            SelectQueryImpl<Record> result = parseSelect(ctx, degree, with);
            parse(ctx, ')');
            return result;
        }

        if (peekKeyword(ctx, "VALUES"))
            return (SelectQueryImpl<Record>) ctx.dsl.selectQuery(parseTableValueConstructor(ctx));

        ctx.ignoreHints(false);
        if (!parseKeywordIf(ctx, "SEL"))
            parseKeyword(ctx, "SELECT");

        String hints = parseHints(ctx);
        boolean distinct = parseKeywordIf(ctx, "DISTINCT") || parseKeywordIf(ctx, "UNIQUE");
        List<Field<?>> distinctOn = null;

        if (distinct) {
            if (parseKeywordIf(ctx, "ON")) {
                parse(ctx, '(');
                distinctOn = parseFields(ctx);
                parse(ctx, ')');
            }
        }
        else
            parseKeywordIf(ctx, "ALL");

        Param<Long> limit = null;
        Param<Long> offset = null;
        boolean percent = false;
        boolean withTies = false;

        // T-SQL style TOP .. START AT
        if (parseKeywordIf(ctx, "TOP")) {
            limit = parseParenthesisedUnsignedIntegerOrBindVariable(ctx);
            percent = parseKeywordIf(ctx, "PERCENT") && ctx.requireProEdition();

            if (parseKeywordIf(ctx, "START AT"))
                offset = parseParenthesisedUnsignedIntegerOrBindVariable(ctx);
            else if (parseKeywordIf(ctx, "WITH TIES"))
                withTies = true;
        }

        // Informix style SKIP .. FIRST
        else if (parseKeywordIf(ctx, "SKIP")) {
            offset = parseParenthesisedUnsignedIntegerOrBindVariable(ctx);

            if (parseKeywordIf(ctx, "FIRST"))
                limit = parseParenthesisedUnsignedIntegerOrBindVariable(ctx);
        }
        else if (parseKeywordIf(ctx, "FIRST")) {
            limit = parseParenthesisedUnsignedIntegerOrBindVariable(ctx);
        }

        List<SelectFieldOrAsterisk> select = parseSelectList(ctx);

        degreeCheck:
        if (degree != null && select.size() != degree) {
            for (SelectFieldOrAsterisk s : select)
                if (!(s instanceof Field<?>))
                    break degreeCheck;

            throw ctx.exception("Select list must contain " + degree + " columns. Got: " + select.size());
        }

        Table<?> into = null;
        List<Table<?>> from = null;
        Condition startWith = null;
        Condition connectBy = null;
        boolean connectByNoCycle = false;
        Condition where = null;
        List<GroupField> groupBy = null;
        Condition having = null;
        List<WindowDefinition> windows = null;
        Condition qualify = null;

        if (parseKeywordIf(ctx, "INTO"))
            into = parseTableName(ctx);

        if (parseKeywordIf(ctx, "FROM"))
            from = parseTables(ctx);

        // TODO is there a better way?
        if (from != null && from.size() == 1 && from.get(0).getName().equalsIgnoreCase("dual"))
            from = null;

        if (parseKeywordIf(ctx, "WHERE"))
            where = parseCondition(ctx);

        if (parseKeywordIf(ctx, "START WITH")) {
            startWith = parseCondition(ctx);
            parseKeyword(ctx, "CONNECT BY");
            connectByNoCycle = parseKeywordIf(ctx, "NOCYCLE");
            connectBy = parseCondition(ctx);
        }
        else if (parseKeywordIf(ctx, "CONNECT BY")) {
            connectByNoCycle = parseKeywordIf(ctx, "NOCYCLE");
            connectBy = parseCondition(ctx);

            if (parseKeywordIf(ctx, "START WITH"))
                startWith = parseCondition(ctx);
        }

        if (parseKeywordIf(ctx, "GROUP BY")) {
            if (parseIf(ctx, '(')) {
                parse(ctx, ')');
                groupBy = emptyList();
            }
            else if (parseKeywordIf(ctx, "ROLLUP")) {
                parse(ctx, '(');
                groupBy = singletonList(rollup(parseFields(ctx).toArray(EMPTY_FIELD)));
                parse(ctx, ')');
            }
            else if (parseKeywordIf(ctx, "CUBE")) {
                parse(ctx, '(');
                groupBy = singletonList(cube(parseFields(ctx).toArray(EMPTY_FIELD)));
                parse(ctx, ')');
            }
            else if (parseKeywordIf(ctx, "GROUPING SETS")) {
                List<List<Field<?>>> fieldSets = new ArrayList<>();
                parse(ctx, '(');
                do {
                    fieldSets.add(parseFieldsOrEmptyParenthesised(ctx));
                }
                while (parseIf(ctx, ','));
                parse(ctx, ')');
                groupBy = singletonList(groupingSets(fieldSets.toArray((Collection[]) EMPTY_COLLECTION)));
            }
            else {
                groupBy = (List) parseFields(ctx);

                if (parseKeywordIf(ctx, "WITH ROLLUP"))
                    groupBy = singletonList(rollup(groupBy.toArray(EMPTY_FIELD)));
            }
        }

        if (parseKeywordIf(ctx, "HAVING"))
            having = parseCondition(ctx);

        if (parseKeywordIf(ctx, "WINDOW"))
            windows = parseWindowDefinitions(ctx);

        if (parseKeywordIf(ctx, "QUALIFY"))
            qualify = parseCondition(ctx);

        SelectQueryImpl<Record> result = new SelectQueryImpl<>(ctx.dsl.configuration(), with);
        if (hints != null)
            result.addHint(hints);

        if (distinct)
            result.setDistinct(distinct);

        if (distinctOn != null)
            result.addDistinctOn(distinctOn);

        if (!select.isEmpty())
            result.addSelect(select);

        if (into != null)
            result.setInto(into);

        if (from != null)
            result.addFrom(from);

        if (connectBy != null)
            if (connectByNoCycle)
                result.addConnectByNoCycle(connectBy);
            else
                result.addConnectBy(connectBy);

        if (startWith != null)
            result.setConnectByStartWith(startWith);

        if (where != null)
            result.addConditions(where);

        if (groupBy != null)
            result.addGroupBy(groupBy);

        if (having != null)
            result.addHaving(having);

        if (windows != null)
            result.addWindow(windows);

        if (qualify != null)
            result.addQualify(qualify);

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

    private static final List<WindowDefinition> parseWindowDefinitions(ParserContext ctx) {
        List<WindowDefinition> result = new ArrayList<>();

        do {
            Name name = parseIdentifier(ctx);
            parseKeyword(ctx, "AS");
            parse(ctx, '(');
            result.add(name.as(parseWindowSpecificationIf(ctx, null, true)));
            parse(ctx, ')');
        }
        while (parseIf(ctx, ','));

        return result;
    }

    private static final WindowSpecification parseWindowSpecificationIf(ParserContext ctx, Name windowName, boolean orderByAllowed) {
        final WindowSpecificationOrderByStep s1;
        final WindowSpecificationRowsStep s2;
        final WindowSpecificationRowsAndStep s3;
        final WindowSpecificationExcludeStep s4;
        final WindowSpecification result;

        s1 = windowName != null
            ? windowName.as()
            : parseKeywordIf(ctx, "PARTITION BY")
            ? partitionBy(parseFields(ctx))
            : null;

        if (parseKeywordIf(ctx, "ORDER BY"))
            if (orderByAllowed)
                s2 = s1 == null
                    ? orderBy(parseSortSpecification(ctx))
                    : s1.orderBy(parseSortSpecification(ctx));
            else
                throw ctx.exception("ORDER BY not allowed");
        else
            s2 = s1;

        boolean rows = parseKeywordIf(ctx, "ROWS");
        boolean range = !rows && parseKeywordIf(ctx, "RANGE");
        boolean groups = !rows && !range && parseKeywordIf(ctx, "GROUPS");

        if ((rows || range || groups) && !orderByAllowed)
            throw ctx.exception("ROWS, RANGE, or GROUPS not allowed");

        if (rows || range || groups) {
            Long n;

            if (parseKeywordIf(ctx, "BETWEEN")) {
                if (parseKeywordIf(ctx, "UNBOUNDED"))
                    if (parseKeywordIf(ctx, "PRECEDING"))
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
                    else if (parseKeywordIf(ctx, "FOLLOWING"))
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
                        throw ctx.expected("FOLLOWING", "PRECEDING");
                else if (parseKeywordIf(ctx, "CURRENT ROW"))
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
                else if ((n = parseUnsignedIntegerIf(ctx)) != null)
                    if (parseKeywordIf(ctx, "PRECEDING"))
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
                    else if (parseKeywordIf(ctx, "FOLLOWING"))
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
                        throw ctx.expected("FOLLOWING", "PRECEDING");
                else
                    throw ctx.expected("CURRENT ROW", "UNBOUNDED", "integer literal");

                parseKeyword(ctx, "AND");

                if (parseKeywordIf(ctx, "UNBOUNDED"))
                    if (parseKeywordIf(ctx, "PRECEDING"))
                        s4 =  s3.andUnboundedPreceding();
                    else if (parseKeywordIf(ctx, "FOLLOWING"))
                        s4 =  s3.andUnboundedFollowing();
                    else
                        throw ctx.expected("FOLLOWING", "PRECEDING");
                else if (parseKeywordIf(ctx, "CURRENT ROW"))
                    s4 =  s3.andCurrentRow();
                else if ((n = parseUnsignedInteger(ctx)) != null)
                    if (parseKeywordIf(ctx, "PRECEDING"))
                        s4 =  s3.andPreceding(n.intValue());
                    else if (parseKeywordIf(ctx, "FOLLOWING"))
                        s4 =  s3.andFollowing(n.intValue());
                    else
                        throw ctx.expected("FOLLOWING", "PRECEDING");
                else
                    throw ctx.expected("CURRENT ROW", "UNBOUNDED", "integer literal");
            }
            else if (parseKeywordIf(ctx, "UNBOUNDED"))
                if (parseKeywordIf(ctx, "PRECEDING"))
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
                else if (parseKeywordIf(ctx, "FOLLOWING"))
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
                    throw ctx.expected("FOLLOWING", "PRECEDING");
            else if (parseKeywordIf(ctx, "CURRENT ROW"))
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
            else if ((n = parseUnsignedInteger(ctx)) != null)
                if (parseKeywordIf(ctx, "PRECEDING"))
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
                else if (parseKeywordIf(ctx, "FOLLOWING"))
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
                    throw ctx.expected("FOLLOWING", "PRECEDING");
            else
                throw ctx.expected("BETWEEN", "CURRENT ROW", "UNBOUNDED", "integer literal");

            if (parseKeywordIf(ctx, "EXCLUDE"))
                if (parseKeywordIf(ctx, "CURRENT ROW"))
                    result = s4.excludeCurrentRow();
                else if (parseKeywordIf(ctx, "TIES"))
                    result = s4.excludeTies();
                else if (parseKeywordIf(ctx, "GROUP"))
                    result = s4.excludeGroup();
                else if (parseKeywordIf(ctx, "NO OTHERS"))
                    result = s4.excludeNoOthers();
                else
                    throw ctx.expected("CURRENT ROW", "TIES", "GROUP", "NO OTHERS");
            else
                result = s4;
        }
        else
            result = s2;

        if (result != null)
            return result;
        else if (windowName != null)
            return null;
        else if ((windowName = parseIdentifierIf(ctx)) != null)
            return parseWindowSpecificationIf(ctx, windowName, orderByAllowed);
        else
            return null;
    }

    private static final Delete<?> parseDelete(ParserContext ctx, WithImpl with) {
        if (!parseKeywordIf(ctx, "DEL"))
            parseKeyword(ctx, "DELETE");

        Param<Long> limit = null;

        // T-SQL style TOP .. START AT
        if (parseKeywordIf(ctx, "TOP")) {
            limit = parseParenthesisedUnsignedIntegerOrBindVariable(ctx);

            // [#8623] TODO Support this
            // percent = parseKeywordIf(ctx, "PERCENT") && ctx.requireProEdition();
        }

        parseKeywordIf(ctx, "FROM");
        Table<?> table = parseTableNameIf(ctx);
        if (table == null) {
            table = table(parseSelect(ctx));

            if (parseKeywordIf(ctx, "AS"))
                table = table.as(parseIdentifier(ctx));
            else if (!peekKeyword(ctx, "WHERE", "ORDER BY", "LIMIT", "RETURNING"))
                table = table.as(parseIdentifierIf(ctx));
        }

        DeleteUsingStep<?> s1 = with == null ? ctx.dsl.delete(table) : with.delete(table);
        DeleteWhereStep<?> s2 = parseKeywordIf(ctx, "USING") ? s1.using(parseTables(ctx)) : s1;
        DeleteOrderByStep<?> s3 = parseKeywordIf(ctx, "WHERE") ? s2.where(parseCondition(ctx)) : s2;
        DeleteLimitStep<?> s4 = parseKeywordIf(ctx, "ORDER BY") ? s3.orderBy(parseSortSpecification(ctx)) : s3;
        DeleteReturningStep<?> s5 = (limit != null || parseKeywordIf(ctx, "LIMIT"))
            ? s4.limit(limit != null ? limit : parseParenthesisedUnsignedIntegerOrBindVariable(ctx))
            : s4;
        Delete<?> s6 = parseKeywordIf(ctx, "RETURNING") ? s5.returning(parseSelectList(ctx)) : s5;

        return s6;
    }

    private static final Insert<?> parseInsert(ParserContext ctx, WithImpl with) {
        if (!parseKeywordIf(ctx, "INS"))
            parseKeyword(ctx, "INSERT");

        parseKeywordIf(ctx, "INTO");
        Table<?> table = parseTableNameIf(ctx);
        if (table == null)
            table = table(parseSelect(ctx));

        Name alias;
        if (parseKeywordIf(ctx, "AS"))
            table = table.as(parseIdentifier(ctx));
        else if (!peekKeyword(ctx, "DEFAULT VALUES", "SEL", "SELECT", "SET", "VALUES")
            && (alias = parseIdentifierIf(ctx)) != null)
            table = table.as(alias);

        InsertSetStep<?> s1 = (with == null ? ctx.dsl.insertInto(table) : with.insertInto(table));
        Field<?>[] fields = null;

        if (parseIf(ctx, '(')) {
            fields = Tools.fieldsByName(parseIdentifiers(ctx).toArray(EMPTY_NAME));
            parse(ctx, ')');
        }

        InsertOnDuplicateStep<?> onDuplicate;
        InsertReturningStep<?> returning;

        if (parseKeywordIf(ctx, "VALUES")) {
            List<List<Field<?>>> allValues = new ArrayList<>();

            valuesLoop:
            do {
                parse(ctx, '(');

                // [#6936] MySQL treats an empty VALUES() clause as the same thing as the standard DEFAULT VALUES
                if (fields == null && parseIf(ctx, ')'))
                    break valuesLoop;

                List<Field<?>> values = new ArrayList<>();
                do {
                    Field<?> value = parseKeywordIf(ctx, "DEFAULT") ? default_() : parseField(ctx);
                    values.add(value);
                }
                while (parseIf(ctx, ','));

                if (fields != null && fields.length != values.size())
                    throw ctx.exception("Insert field size (" + fields.length + ") must match values size (" + values.size() + ")");

                allValues.add(values);
                parse(ctx, ')');
            }
            while (parseIf(ctx, ','));

            if (allValues.isEmpty()) {
                returning = onDuplicate = s1.defaultValues();
            }
            else {
                InsertValuesStepN<?> step2 = (fields != null)
                    ? s1.columns(fields)
                    : (InsertValuesStepN<?>) s1;

                for (List<Field<?>> values : allValues)
                    step2 = step2.values(values);

                returning = onDuplicate = step2;
            }
        }
        else if (parseKeywordIf(ctx, "SET")) {
            Map<Field<?>, Object> map = parseSetClauseList(ctx);

            returning = onDuplicate =  s1.set(map);
        }
        else if (peekKeyword(ctx, "SELECT", false, true, false)
              || peekKeyword(ctx, "SEL", false, true, false)){
            SelectQueryImpl<Record> select = parseSelect(ctx);

            returning = onDuplicate = (fields == null)
                ? s1.select(select)
                : s1.columns(fields).select(select);
        }
        else if (parseKeywordIf(ctx, "DEFAULT VALUES")) {
            if (fields != null)
                throw ctx.notImplemented("DEFAULT VALUES without INSERT field list");
            else
                returning = onDuplicate = s1.defaultValues();
        }
        else
            throw ctx.expected("DEFAULT VALUES", "SELECT", "SET", "VALUES");

        if (parseKeywordIf(ctx, "ON")) {
            if (parseKeywordIf(ctx, "DUPLICATE KEY UPDATE")) {
                parseKeywordIf(ctx, "SET");

                InsertOnConflictWhereStep<?> where = onDuplicate.onDuplicateKeyUpdate().set(parseSetClauseList(ctx));

                if (parseKeywordIf(ctx, "WHERE"))
                    returning = where.where(parseCondition(ctx));
                else
                    returning = where;
            }
            else if (parseKeywordIf(ctx, "DUPLICATE KEY IGNORE")) {
                returning = onDuplicate.onDuplicateKeyIgnore();
            }
            else if (parseKeywordIf(ctx, "CONFLICT")) {
                InsertOnConflictDoUpdateStep<?> doUpdate;

                if (parseKeywordIf(ctx, "ON CONSTRAINT")) {
                    doUpdate = onDuplicate.onConflictOnConstraint(parseName(ctx));
                }
                else if (parseIf(ctx, '(')) {
                    doUpdate = onDuplicate.onConflict(parseFieldNames(ctx));
                    parse(ctx, ')');
                }
                else {
                    doUpdate = onDuplicate.onConflict();
                }

                parseKeyword(ctx, "DO");
                if (parseKeywordIf(ctx, "NOTHING")) {
                    returning = doUpdate.doNothing();
                }
                else if (parseKeywordIf(ctx, "UPDATE SET")) {
                    InsertOnConflictWhereStep<?> where = doUpdate.doUpdate().set(parseSetClauseList(ctx));

                    if (parseKeywordIf(ctx, "WHERE"))
                        returning = where.where(parseCondition(ctx));
                    else
                        returning = where;
                }
                else
                    throw ctx.expected("NOTHING", "UPDATE");
            }
            else
                throw ctx.expected("CONFLICT", "DUPLICATE");
        }

        if (parseKeywordIf(ctx, "RETURNING"))
            return returning.returning(parseSelectList(ctx));
        else
            return returning;
    }

    private static final Update<?> parseUpdate(ParserContext ctx, WithImpl with) {
        if (!parseKeywordIf(ctx, "UPD"))
            parseKeyword(ctx, "UPDATE");

        Param<Long> limit = null;

        // T-SQL style TOP .. START AT
        if (parseKeywordIf(ctx, "TOP")) {
            limit = parseParenthesisedUnsignedIntegerOrBindVariable(ctx);

            // [#8623] TODO Support this
            // percent = parseKeywordIf(ctx, "PERCENT") && ctx.requireProEdition();
        }

        Table<?> table = parseTableNameIf(ctx);
        if (table == null)
            table = table(parseSelect(ctx));

        if (parseKeywordIf(ctx, "AS"))
            table = table.as(parseIdentifier(ctx));
        else if (!peekKeyword(ctx, "SET"))
            table = table.as(parseIdentifierIf(ctx));

        UpdateSetFirstStep<?> s1 = (with == null ? ctx.dsl.update(table) : with.update(table));

        parseKeyword(ctx, "SET");

        // TODO Row value expression updates
        Map<Field<?>, Object> map = parseSetClauseList(ctx);
        UpdateFromStep<?> s2 = s1.set(map);
        UpdateWhereStep<?> s3 = parseKeywordIf(ctx, "FROM") ? s2.from(parseTables(ctx)) : s2;
        UpdateOrderByStep<?> s4 = parseKeywordIf(ctx, "WHERE") ? s3.where(parseCondition(ctx)) : s3;
        UpdateLimitStep<?> s5 = parseKeywordIf(ctx, "ORDER BY") ? s4.orderBy(parseSortSpecification(ctx)) : s4;
        UpdateReturningStep<?> s6 = (limit != null || parseKeywordIf(ctx, "LIMIT"))
            ? s5.limit(limit != null ? limit : parseParenthesisedUnsignedIntegerOrBindVariable(ctx))
            : s5;
        Update<?> s7 = parseKeywordIf(ctx, "RETURNING") ? s6.returning(parseSelectList(ctx)) : s6;

        return s7;
    }

    private static final Map<Field<?>, Object> parseSetClauseList(ParserContext ctx) {
        Map<Field<?>, Object> map = new LinkedHashMap<>();

        do {
            Field<?> field = parseFieldName(ctx);

            if (map.containsKey(field))
                throw ctx.exception("Duplicate column in set clause list: " + field);

            parse(ctx, '=');

            Field<?> value = parseKeywordIf(ctx, "DEFAULT") ? default_() : parseField(ctx);
            map.put(field,  value);
        }
        while (parseIf(ctx, ','));

        return map;
    }

    private static final Merge<?> parseMerge(ParserContext ctx, WithImpl with) {
        parseKeyword(ctx, "MERGE");
        parseKeywordIf(ctx, "INTO");
        Table<?> target = parseTableName(ctx);

        if (parseKeywordIf(ctx, "AS") || !peekKeyword(ctx, "USING"))
            target = target.as(parseIdentifier(ctx));

        parseKeyword(ctx, "USING");
        Table<?> table = null;
        Select<?> using = null;

        if (parseIf(ctx, '(')) {
            using = parseSelect(ctx);
            parse(ctx, ')');
        }
        else {
            table = parseTableName(ctx);
        }

        TableLike<?> usingTable = (table != null ? table : using);
        if (parseKeywordIf(ctx, "AS") || !peekKeyword(ctx, "ON"))
            usingTable = (table != null ? table : DSL.table(using)).as(parseIdentifier(ctx));

        parseKeyword(ctx, "ON");
        Condition on = parseCondition(ctx);
        boolean update = false;
        boolean insert = false;
        Field<?>[] insertColumns = null;
        List<Field<?>> insertValues = null;
        Condition insertWhere = null;
        Map<Field<?>, Object> updateSet = null;
        Condition updateWhere = null;

        for (;;) {
            if (!update && (update = parseKeywordIf(ctx, "WHEN MATCHED"))) {
                if (parseKeywordIf(ctx, "AND"))
                    updateWhere = parseCondition(ctx);

                parseKeyword(ctx, "THEN UPDATE SET");
                updateSet = parseSetClauseList(ctx);

                if (updateWhere == null && parseKeywordIf(ctx, "WHERE"))
                    updateWhere = parseCondition(ctx);
            }
            else if (!insert && (insert = parseKeywordIf(ctx, "WHEN NOT MATCHED"))) {
                if (parseKeywordIf(ctx, "AND"))
                    insertWhere = parseCondition(ctx);

                parseKeyword(ctx, "THEN INSERT");
                parse(ctx, '(');
                insertColumns = Tools.fieldsByName(parseIdentifiers(ctx).toArray(EMPTY_NAME));
                parse(ctx, ')');
                parseKeyword(ctx, "VALUES");
                parse(ctx, '(');
                insertValues = new ArrayList<>();
                do {
                    Field<?> value = parseKeywordIf(ctx, "DEFAULT") ? default_() : parseField(ctx);
                    insertValues.add(value);
                }
                while (parseIf(ctx, ','));
                parse(ctx, ')');

                if (insertColumns.length != insertValues.size())
                    throw ctx.exception("Insert column size (" + insertColumns.length + ") must match values size (" + insertValues.size() + ")");

                if (insertWhere == null && parseKeywordIf(ctx, "WHERE"))
                    insertWhere = parseCondition(ctx);
            }
            else
                break;
        }

        if (!update && !insert)
            throw ctx.exception("At least one of UPDATE or INSERT clauses is required");

        // TODO support multi clause MERGE
        // TODO support DELETE

        MergeUsingStep<?> s1 = (with == null ? ctx.dsl.mergeInto(target) : with.mergeInto(target));
        MergeMatchedStep<?> s2 = s1.using(usingTable).on(on);
        MergeNotMatchedStep<?> s3 = update
            ? updateWhere != null
                ? s2.whenMatchedThenUpdate().set(updateSet).where(updateWhere)
                : s2.whenMatchedThenUpdate().set(updateSet)
            : s2;
        MergeFinalStep<?> s4 = insert
            ? insertWhere != null
                ? s3.whenNotMatchedThenInsert(insertColumns).values(insertValues).where(insertWhere)
                : s3.whenNotMatchedThenInsert(insertColumns).values(insertValues)
            : s3;

        return s4;
    }

    private static final Query parseSet(ParserContext ctx) {
        parseKeyword(ctx, "SET");

        if (parseKeywordIf(ctx, "CATALOG"))
            return parseSetCatalog(ctx);
        else if (parseKeywordIf(ctx, "CURRENT SCHEMA"))
            return parseSetSchema(ctx);
        else if (parseKeywordIf(ctx, "CURRENT SQLID"))
            return parseSetSchema(ctx);
        else if (parseKeywordIf(ctx, "GENERATOR"))
            return parseSetGenerator(ctx);
        else if (parseKeywordIf(ctx, "SCHEMA"))
            return parseSetSchema(ctx);
        else if (parseKeywordIf(ctx, "SEARCH_PATH"))
            return parseSetSearchPath(ctx);
        else
            return parseSetCommand(ctx);
    }

    private static final Query parseSetCommand(ParserContext ctx) {
        if (TRUE.equals(ctx.settings().isParseSetCommands())) {
            Name name = parseIdentifier(ctx);

            // TODO: [#9780] Are there any possible syntaxes and data types?
            parseIf(ctx, '=');
            Object value = parseSignedIntegerIf(ctx);
            return ctx.dsl.set(name, value != null ? inline(value) : inline(parseStringLiteral(ctx)));
        }

        // There are many SET commands in programs like sqlplus, which we'll simply ignore
        else {
            parseUntilEOL(ctx);
            return IGNORE_NO_DELIMITER;
        }
    }

    private static final Query parseSetCatalog(ParserContext ctx) {
        return ctx.dsl.setCatalog(parseCatalogName(ctx));
    }

    private static final Query parseUse(ParserContext ctx) {
        parseKeyword(ctx, "USE");
        return ctx.dsl.setCatalog(parseCatalogName(ctx));
    }

    private static final Query parseSetSchema(ParserContext ctx) {
        parseIf(ctx, '=');
        return ctx.dsl.setSchema(parseSchemaName(ctx));
    }

    private static final Query parseSetSearchPath(ParserContext ctx) {
        if (!parseIf(ctx, '='))
            parseKeyword(ctx, "TO");

        Schema schema = null;

        do {
            Schema s = parseSchemaName(ctx);
            if (schema == null)
                schema = s;
        }
        while (parseIf(ctx, ','));

        return ctx.dsl.setSchema(schema);
    }

    private static final DDLQuery parseCommentOn(ParserContext ctx) {
        parseKeyword(ctx, "COMMENT ON");

        CommentOnIsStep s1;

        if (parseKeywordIf(ctx, "COLUMN"))
            s1 = ctx.dsl.commentOnColumn(parseFieldName(ctx));
        else if (parseKeywordIf(ctx, "TABLE"))
            s1 = ctx.dsl.commentOnTable(parseTableName(ctx));
        else if (parseKeywordIf(ctx, "VIEW"))
            s1 = ctx.dsl.commentOnView(parseTableName(ctx));

        // Ignored no-arg object comments
        // https://www.postgresql.org/docs/10/static/sql-comment.html
        // https://docs.oracle.com/database/121/SQLRF/statements_4010.htm
        else if (parseAndGetKeywordIf(ctx,
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
            parseIdentifier(ctx);
            parseKeyword(ctx, "IS");
            parseStringLiteral(ctx);
            return IGNORE;
        }

        // TODO: (PostgreSQL)
        // AGGREGATE, CAST, FUNCTION, OPERATOR, OPERATOR CLASS, OPERATOR FAMILY

        // Ignored object comments with arguments
        // https://www.postgresql.org/docs/10/static/sql-comment.html
        else if (parseKeywordIf(ctx, "CONSTRAINT")) {
            parseIdentifier(ctx);
            parseKeyword(ctx, "ON");
            parseKeywordIf(ctx, "DOMAIN");
            parseIdentifier(ctx);
            parseKeyword(ctx, "IS");
            parseStringLiteral(ctx);
            return IGNORE;
        }
        else if (parseAndGetKeywordIf(ctx,
            "POLICY",
            "RULE",
            "TRIGGER"
        ) != null) {
            parseIdentifier(ctx);
            parseKeyword(ctx, "ON");
            parseIdentifier(ctx);
            parseKeyword(ctx, "IS");
            parseStringLiteral(ctx);
            return IGNORE;
        }
        else if (parseKeywordIf(ctx, "TRANSFORM FOR")) {
            parseIdentifier(ctx);
            parseKeyword(ctx, "LANGUAGE");
            parseIdentifier(ctx);
            parseKeyword(ctx, "IS");
            parseStringLiteral(ctx);
            return IGNORE;
        }
        else
            throw ctx.unsupportedClause();

        parseKeyword(ctx, "IS");
        return s1.is(parseStringLiteral(ctx));
    }

    private static final DDLQuery parseCreate(ParserContext ctx) {
        parseKeyword(ctx, "CREATE");

        if (parseKeywordIf(ctx, "TABLE"))
            return parseCreateTable(ctx, false);
        else if (parseKeywordIf(ctx, "TEMP TABLE"))
            return parseCreateTable(ctx, true);
        else if (parseKeywordIf(ctx, "TEMPORARY TABLE"))
            return parseCreateTable(ctx, true);
        else if (parseKeywordIf(ctx, "TYPE"))
            return parseCreateType(ctx);
        else if (parseKeywordIf(ctx, "GENERATOR"))
            return parseCreateSequence(ctx);
        else if (parseKeywordIf(ctx, "GLOBAL TEMP TABLE"))
            return parseCreateTable(ctx, true);
        else if (parseKeywordIf(ctx, "GLOBAL TEMPORARY TABLE"))
            return parseCreateTable(ctx, true);
        else if (parseKeywordIf(ctx, "INDEX"))
            return parseCreateIndex(ctx, false);
        else if (parseKeywordIf(ctx, "SPATIAL INDEX") && ctx.requireUnsupportedSyntax())
            return parseCreateIndex(ctx, false);
        else if (parseKeywordIf(ctx, "FULLTEXT INDEX") && ctx.requireUnsupportedSyntax())
            return parseCreateIndex(ctx, false);
        else if (parseKeywordIf(ctx, "UNIQUE INDEX"))
            return parseCreateIndex(ctx, true);
        else if (parseKeywordIf(ctx, "SCHEMA"))
            return parseCreateSchema(ctx);
        else if (parseKeywordIf(ctx, "SEQUENCE"))
            return parseCreateSequence(ctx);
        else if (parseKeywordIf(ctx, "OR REPLACE VIEW") || parseKeywordIf(ctx, "OR REPLACE FORCE VIEW"))
            return parseCreateView(ctx, true);
        else if (parseKeywordIf(ctx, "OR ALTER VIEW"))
            return parseCreateView(ctx, true);
        else if (parseKeywordIf(ctx, "VIEW") || parseKeywordIf(ctx, "FORCE VIEW"))
            return parseCreateView(ctx, false);
        else if (parseKeywordIf(ctx, "EXTENSION"))
            return parseCreateExtension(ctx);
        else
            throw ctx.expected(
                "GENERATOR",
                "GLOBAL TEMPORARY TABLE",
                "INDEX",
                "OR ALTER VIEW",
                "OR REPLACE VIEW",
                "SCHEMA",
                "SEQUENCE",
                "TABLE",
                "TEMPORARY TABLE",
                "TYPE",
                "UNIQUE INDEX",
                "VIEW");
    }

    private static final Query parseAlter(ParserContext ctx) {
        parseKeyword(ctx, "ALTER");

        if (parseKeywordIf(ctx, "DATABASE"))
            return parseAlterDatabase(ctx);
        else if (parseKeywordIf(ctx, "DOMAIN"))
            return parseAlterDomain(ctx);
        else if (parseKeywordIf(ctx, "INDEX"))
            return parseAlterIndex(ctx);
        else if (parseKeywordIf(ctx, "SCHEMA"))
            return parseAlterSchema(ctx);
        else if (parseKeywordIf(ctx, "SEQUENCE"))
            return parseAlterSequence(ctx);
        else if (parseKeywordIf(ctx, "SESSION"))
            return parseAlterSession(ctx);
        else if (parseKeywordIf(ctx, "TABLE"))
            return parseAlterTable(ctx);
        else if (parseKeywordIf(ctx, "TYPE"))
            return parseAlterType(ctx);
        else if (parseKeywordIf(ctx, "VIEW"))
            return parseAlterView(ctx);
        else
            throw ctx.expected("DOMAIN", "INDEX", "SCHEMA", "SEQUENCE", "SESSION", "TABLE", "TYPE", "VIEW");
    }

    private static final DDLQuery parseDrop(ParserContext ctx) {
        parseKeyword(ctx, "DROP");

        if (parseKeywordIf(ctx, "TABLE"))
            return parseDropTable(ctx, false);
        else if (parseKeywordIf(ctx, "TEMPORARY TABLE"))
            return parseDropTable(ctx, true);
        else if (parseKeywordIf(ctx, "TYPE"))
            return parseDropType(ctx);
        else if (parseKeywordIf(ctx, "INDEX"))
            return parseDropIndex(ctx);
        else if (parseKeywordIf(ctx, "VIEW"))
            return parseDropView(ctx);
        else if (parseKeywordIf(ctx, "GENERATOR"))
            return parseDropSequence(ctx);
        else if (parseKeywordIf(ctx, "SEQUENCE"))
            return parseDropSequence(ctx);
        else if (parseKeywordIf(ctx, "SCHEMA"))
            return parseDropSchema(ctx);
        else if (parseKeywordIf(ctx, "EXTENSION"))
            return parseDropExtension(ctx);
        else
            throw ctx.expected("GENERATOR", "INDEX", "SCHEMA", "SEQUENCE", "TABLE", "TEMPORARY TABLE", "TYPE", "VIEW");
    }

    private static final Truncate<?> parseTruncate(ParserContext ctx) {
        parseKeyword(ctx, "TRUNCATE");
        parseKeyword(ctx, "TABLE");
        Table<?> table = parseTableName(ctx);
        boolean continueIdentity = parseKeywordIf(ctx, "CONTINUE IDENTITY");
        boolean restartIdentity = !continueIdentity && parseKeywordIf(ctx, "RESTART IDENTITY");
        boolean cascade = parseKeywordIf(ctx, "CASCADE");
        boolean restrict = !cascade && parseKeywordIf(ctx, "RESTRICT");

        TruncateIdentityStep<?> step1 = ctx.dsl.truncate(table);
        TruncateCascadeStep<?> step2 =
              continueIdentity
            ? step1.continueIdentity()
            : restartIdentity
            ? step1.restartIdentity()
            : step1;

        TruncateFinalStep<?> step3 =
              cascade
            ? step2.cascade()
            : restrict
            ? step2.restrict()
            : step2;

        return step3;
    }

    private static final DDLQuery parseGrant(ParserContext ctx) {
        parseKeyword(ctx, "GRANT");
        Privilege privilege = parsePrivilege(ctx);
        List<Privilege> privileges = null;

        while (parseIf(ctx, ',')) {
            if (privileges == null) {
                privileges = new ArrayList<>();
                privileges.add(privilege);
            }

            privileges.add(parsePrivilege(ctx));
        }

        parseKeyword(ctx, "ON");
        parseKeywordIf(ctx, "TABLE");
        Table<?> table = parseTableName(ctx);

        parseKeyword(ctx, "TO");
        User user = parseKeywordIf(ctx, "PUBLIC") ? null : parseUser(ctx);

        GrantOnStep s1 = privileges == null ? ctx.dsl.grant(privilege) : ctx.dsl.grant(privileges);
        GrantToStep s2 = s1.on(table);
        GrantWithGrantOptionStep s3 = user == null ? s2.toPublic() : s2.to(user);

        return parseKeywordIf(ctx, "WITH GRANT OPTION")
            ? s3.withGrantOption()
            : s3;
    }

    private static final DDLQuery parseRevoke(ParserContext ctx) {
        parseKeyword(ctx, "REVOKE");
        boolean grantOptionFor = parseKeywordIf(ctx, "GRANT OPTION FOR");
        Privilege privilege = parsePrivilege(ctx);
        List<Privilege> privileges = null;

        while (parseIf(ctx, ',')) {
            if (privileges == null) {
                privileges = new ArrayList<>();
                privileges.add(privilege);
            }

            privileges.add(parsePrivilege(ctx));
        }

        parseKeyword(ctx, "ON");
        parseKeywordIf(ctx, "TABLE");
        Table<?> table = parseTableName(ctx);

        RevokeOnStep s1 = grantOptionFor
            ? privileges == null
                ? ctx.dsl.revokeGrantOptionFor(privilege)
                : ctx.dsl.revokeGrantOptionFor(privileges)
            : privileges == null
                ? ctx.dsl.revoke(privilege)
                : ctx.dsl.revoke(privileges);

        parseKeyword(ctx, "FROM");
        User user = parseKeywordIf(ctx, "PUBLIC") ? null : parseUser(ctx);

        RevokeFromStep s2 = s1.on(table);
        return user == null ? s2.fromPublic() : s2.from(user);
    }

    private static final Query parseExec(ParserContext ctx) {
        parseKeyword(ctx, "EXEC");

        if (parseKeywordIf(ctx, "SP_RENAME")) {
            if (parseKeywordIf(ctx, "@OBJNAME"))
                parse(ctx, '=');
            Name oldName = ctx.dsl.parser().parseName(parseStringLiteral(ctx));

            parse(ctx, ',');
            if (parseKeywordIf(ctx, "@NEWNAME"))
                parse(ctx, '=');
            Name newName = ctx.dsl.parser().parseName(parseStringLiteral(ctx));

            String objectType = "TABLE";
            if (parseIf(ctx, ',')) {
                if (parseKeywordIf(ctx, "@OBJTYPE"))
                    parse(ctx, '=');

                if (!parseKeywordIf(ctx, "NULL"))
                    objectType = parseStringLiteral(ctx);
            }

            if ("TABLE".equalsIgnoreCase(objectType))
                return ctx.dsl.alterTable(oldName).renameTo(newName.unqualifiedName());
            else if ("INDEX".equalsIgnoreCase(objectType))
                return ctx.dsl.alterIndex(oldName).renameTo(newName.unqualifiedName());
            else if ("COLUMN".equalsIgnoreCase(objectType))
                return ctx.dsl.alterTable(oldName.qualifier()).renameColumn(oldName.unqualifiedName()).to(newName.unqualifiedName());
            else
                throw ctx.exception("Unsupported object type: " + objectType);
        }
        else {
            throw ctx.unsupportedClause();
        }
    }

    private static final Block parseBlock(ParserContext ctx) {
        List<Statement> statements = new ArrayList<>();





        if (parseKeywordIf(ctx, "DECLARE") && ctx.requireProEdition())



            ;
        else
            parseKeywordIf(ctx, "EXECUTE BLOCK AS");

        parseKeyword(ctx, "BEGIN");
        statements.addAll(parseStatements(ctx, "END"));
        parseKeyword(ctx, "END");



        parseIf(ctx, ';');





        return ctx.dsl.begin(statements);
    }

    private static final void parseSemicolonAfterNonBlocks(ParserContext ctx, Statement result) {
        if (!(result instanceof Block))
            parseIf(ctx, ';');
        else if (result instanceof BlockImpl && !((BlockImpl) result).alwaysWrapInBeginEnd)
            parseIf(ctx, ';');
    }

    private static final Statement parseStatementAndSemicolon(ParserContext ctx) {
        Statement result = parseStatement(ctx);
        parseSemicolonAfterNonBlocks(ctx, result);
        return result;
    }

    private static final List<Statement> parseStatements(ParserContext ctx, String... peek) {
        List<Statement> statements = new ArrayList<>();

        for (;;) {
            Statement parsed;
            Statement stored;




            stored = parsed = parseStatement(ctx);

            if (parsed == null)
                break;






            statements.add(stored);
            parseSemicolonAfterNonBlocks(ctx, parsed);
            if (peekKeyword(ctx, peek))
                break;
        }

        return statements;
    }

























































    private static final Block parseDo(ParserContext ctx) {
        parseKeyword(ctx, "DO");
        String block = parseStringLiteral(ctx);
        return (Block) ctx.dsl.parser().parseQuery(block);
    }

    private static final Statement parseStatement(ParserContext ctx) {
        switch (ctx.character()) {
            case 'c':
            case 'C':
                if (peekKeyword(ctx, "CONTINUE") && ctx.requireProEdition())



                ;

                break;

            case 'd':
            case 'D':
                if (peekKeyword(ctx, "DECLARE") && ctx.requireProEdition())



                ;

                break;

            case 'e':
            case 'E':
                if (peekKeyword(ctx, "EXIT") && ctx.requireProEdition())



                ;

                break;

            case 'f':
            case 'F':
                if (peekKeyword(ctx, "FOR") && ctx.requireProEdition())



                ;

                break;

            case 'g':
            case 'G':
                if (peekKeyword(ctx, "GOTO") && ctx.requireProEdition())



                ;

                break;

            case 'i':
            case 'I':
                if (peekKeyword(ctx, "IF") && ctx.requireProEdition())



                ;
                else if (peekKeyword(ctx, "ITERATE") && ctx.requireProEdition())



                ;

                break;

            case 'l':
            case 'L':
                if (peekKeyword(ctx, "LEAVE") && ctx.requireProEdition())



                ;
                else if (peekKeyword(ctx, "LOOP") && ctx.requireProEdition())



                ;

                break;

            case 'n':
            case 'N':
                if (peekKeyword(ctx, "NULL"))
                    return parseNullStatement(ctx);

                break;

            case 'r':
            case 'R':
                if (peekKeyword(ctx, "REPEAT") && ctx.requireProEdition())



                ;

                break;

            case 's':
            case 'S':
                if (peekKeyword(ctx, "SET") && ctx.requireProEdition())



                ;

                break;

            case 'w':
            case 'W':
                if (peekKeyword(ctx, "WHILE") && ctx.requireProEdition())



                ;

                break;
        }







        return parseQuery(ctx, false, false);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Statement parsing
    // -----------------------------------------------------------------------------------------------------------------

    private static final Statement parseNullStatement(ParserContext ctx) {
        parseKeyword(ctx, "NULL");
        return new NullStatement();
    }




















































































































































































































































































    // -----------------------------------------------------------------------------------------------------------------
    // Statement clause parsing
    // -----------------------------------------------------------------------------------------------------------------

    private static final Privilege parsePrivilege(ParserContext ctx) {
        if (parseKeywordIf(ctx, "SELECT"))
            return privilege(K_SELECT);
        else if (parseKeywordIf(ctx, "INSERT"))
            return privilege(K_INSERT);
        else if (parseKeywordIf(ctx, "UPDATE"))
            return privilege(K_UPDATE);
        else if (parseKeywordIf(ctx, "DELETE"))
            return privilege(K_DELETE);
        else
            throw ctx.expected("DELETE", "INSERT", "SELECT", "UPDATE");
    }

    private static final User parseUser(ParserContext ctx) {
        return user(parseName(ctx));
    }

    private static final DDLQuery parseCreateView(ParserContext ctx, boolean orReplace) {
        boolean ifNotExists = !orReplace && parseKeywordIf(ctx, "IF NOT EXISTS");
        Table<?> view = parseTableName(ctx);
        Field<?>[] fields = EMPTY_FIELD;

        if (parseIf(ctx, '(')) {
            fields = parseFieldNames(ctx).toArray(fields);
            parse(ctx, ')');
        }

        parseKeyword(ctx, "AS");
        Select<?> select = parseWithOrSelect(ctx);

        if (fields.length > 0 && fields.length != select.getSelect().size())
            throw ctx.exception("Select list size (" + select.getSelect().size() + ") must match declared field size (" + fields.length + ")");


        return ifNotExists
            ? ctx.dsl.createViewIfNotExists(view, fields).as(select)
            : orReplace
            ? ctx.dsl.createOrReplaceView(view, fields).as(select)
            : ctx.dsl.createView(view, fields).as(select);
    }

    private static final DDLQuery parseCreateExtension(ParserContext ctx) {
        parseKeywordIf(ctx, "IF NOT EXISTS");
        parseIdentifier(ctx);
        parseKeywordIf(ctx, "WITH");
        if (parseKeywordIf(ctx, "SCHEMA"))
            parseIdentifier(ctx);
        if (parseKeywordIf(ctx, "VERSION"))
            if (parseIdentifierIf(ctx) == null)
                parseStringLiteral(ctx);
        if (parseKeywordIf(ctx, "FROM"))
            if (parseIdentifierIf(ctx) == null)
                parseStringLiteral(ctx);
        parseKeywordIf(ctx, "CASCADE");
        return IGNORE;
    }

    private static final DDLQuery parseDropExtension(ParserContext ctx) {
        parseKeywordIf(ctx, "IF EXISTS");
        parseIdentifiers(ctx);
        if (!parseKeywordIf(ctx, "CASCADE"))
            parseKeywordIf(ctx, "RESTRICT");
        return IGNORE;
    }

    private static final DDLQuery parseAlterView(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Table<?> oldName = parseTableName(ctx);
        parseKeyword(ctx, "RENAME");
        if (!parseKeywordIf(ctx, "AS"))
            parseKeyword(ctx, "TO");
        Table<?> newName = parseTableName(ctx);

        return ifExists
            ? ctx.dsl.alterViewIfExists(oldName).renameTo(newName)
            : ctx.dsl.alterView(oldName).renameTo(newName);
    }

    private static final DDLQuery parseDropView(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Table<?> tableName = parseTableName(ctx);

        DropViewFinalStep s1;

        s1 = ifExists
            ? ctx.dsl.dropViewIfExists(tableName)
            : ctx.dsl.dropView(tableName);

        return s1;
    }

    private static final DDLQuery parseCreateSequence(ParserContext ctx) {
        boolean ifNotExists = parseKeywordIf(ctx, "IF NOT EXISTS");
        Sequence<?> schemaName = parseSequenceName(ctx);

        CreateSequenceFlagsStep s = ifNotExists
            ? ctx.dsl.createSequenceIfNotExists(schemaName)
            : ctx.dsl.createSequence(schemaName);

        for (;;) {
            Param<Long> param;

            if ((param = parseSequenceStartWithIf(ctx)) != null)
                s = s.startWith(param);
            else if ((param = parseSequenceIncrementByIf(ctx)) != null)
                s = s.incrementBy(param);
            else if ((param = parseSequenceMinvalueIf(ctx)) != null)
                s = s.minvalue(param);
            else if (parseSequenceNoMinvalueIf(ctx))
                s = s.noMinvalue();
            else if ((param = parseSequenceMaxvalueIf(ctx)) != null)
                s = s.maxvalue(param);
            else if (parseSequenceNoMaxvalueIf(ctx))
                s = s.noMaxvalue();
            else if (parseKeywordIf(ctx, "CYCLE"))
                s = s.cycle();
            else if (parseSequenceNoCycleIf(ctx))
                s = s.noCycle();
            else if ((param = parseSequenceCacheIf(ctx)) != null)
                s = s.cache(param);
            else if (parseSequenceNoCacheIf(ctx)) {
                s = s.noCache();
                continue;
            }
            else
                break;
        }

        return s;
    }

    private static final DDLQuery parseAlterSequence(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Sequence<?> sequenceName = parseSequenceName(ctx);

        AlterSequenceStep s = ifExists
            ? ctx.dsl.alterSequenceIfExists(sequenceName)
            : ctx.dsl.alterSequence(sequenceName);

        if (parseKeywordIf(ctx, "RENAME")) {
            if (!parseKeywordIf(ctx, "AS"))
                parseKeyword(ctx, "TO");
            return s.renameTo(parseSequenceName(ctx));
        }
        else {
            boolean found = false;
            AlterSequenceFlagsStep s1 = s;
            while (true) {
                Param<Long> param;

                if ((param = parseSequenceStartWithIf(ctx)) != null)
                    s1 = s1.startWith(param);
                else if ((param = parseSequenceIncrementByIf(ctx)) != null)
                    s1 = s1.incrementBy(param);
                else if ((param = parseSequenceMinvalueIf(ctx)) != null)
                    s1 = s1.minvalue(param);
                else if (parseSequenceNoMinvalueIf(ctx))
                    s1 = s1.noMinvalue();
                else if ((param = parseSequenceMaxvalueIf(ctx)) != null)
                    s1 = s1.maxvalue(param);
                else if (parseSequenceNoMaxvalueIf(ctx))
                    s1 = s1.noMaxvalue();
                else if (parseKeywordIf(ctx, "CYCLE"))
                    s1 = s1.cycle();
                else if (parseSequenceNoCycleIf(ctx))
                    s1 = s1.noCycle();
                else if ((param = parseSequenceCacheIf(ctx)) != null)
                    s1 = s1.cache(param);
                else if (parseSequenceNoCacheIf(ctx))
                    s1 = s1.noCache();
                else if (parseKeywordIf(ctx, "RESTART")) {
                    if (parseKeywordIf(ctx, "WITH"))
                        s1 = s1.restartWith(parseUnsignedIntegerOrBindVariable(ctx));
                    else
                        s1 = s1.restart();
                }
                else
                    break;
                found = true;
            }
            if (!found)
                throw ctx.expected("CACHE", "CYCLE", "INCREMENT BY", "MAXVALUE", "MINVALUE", "NO CACHE", "NO CYCLE", "NO MAXVALUE", "NO MINVALUE", "RENAME TO", "RESTART", "START WITH");
            return s1;
        }
    }

    private static final boolean parseSequenceNoCacheIf(ParserContext ctx) {
        return parseKeywordIf(ctx, "NO CACHE") || parseKeywordIf(ctx, "NOCACHE");
    }

    private static final Param<Long> parseSequenceCacheIf(ParserContext ctx) {
        return parseKeywordIf(ctx, "CACHE") && (parseIf(ctx, "=") || true) ? parseUnsignedIntegerOrBindVariable(ctx) : null;
    }

    private static final boolean parseSequenceNoCycleIf(ParserContext ctx) {
        return parseKeywordIf(ctx, "NO CYCLE") || parseKeywordIf(ctx, "NOCYCLE");
    }

    private static final boolean parseSequenceNoMaxvalueIf(ParserContext ctx) {
        return parseKeywordIf(ctx, "NO MAXVALUE") || parseKeywordIf(ctx, "NOMAXVALUE");
    }

    private static final Param<Long> parseSequenceMaxvalueIf(ParserContext ctx) {
        return parseKeywordIf(ctx, "MAXVALUE") && (parseIf(ctx, "=") || true) ? parseUnsignedIntegerOrBindVariable(ctx) : null;
    }

    private static final boolean parseSequenceNoMinvalueIf(ParserContext ctx) {
        return parseKeywordIf(ctx, "NO MINVALUE") || parseKeywordIf(ctx, "NOMINVALUE");
    }

    private static final Param<Long> parseSequenceMinvalueIf(ParserContext ctx) {
        return parseKeywordIf(ctx, "MINVALUE") && (parseIf(ctx, "=") || true) ? parseUnsignedIntegerOrBindVariable(ctx) : null;
    }

    private static final Param<Long> parseSequenceIncrementByIf(ParserContext ctx) {
        return parseKeywordIf(ctx, "INCREMENT") && (parseKeywordIf(ctx, "BY") || parseIf(ctx, "=") || true) ? parseUnsignedIntegerOrBindVariable(ctx) : null;
    }

    private static final Param<Long> parseSequenceStartWithIf(ParserContext ctx) {
        return parseKeywordIf(ctx, "START") && (parseKeywordIf(ctx, "WITH") || parseIf(ctx, "=") || true) ? parseUnsignedIntegerOrBindVariable(ctx) : null;
    }

    private static final Query parseAlterSession(ParserContext ctx) {
        parseKeyword(ctx, "SET CURRENT_SCHEMA");
        parse(ctx, '=');
        return ctx.dsl.setSchema(parseSchemaName(ctx));
    }

    private static final DDLQuery parseSetGenerator(ParserContext ctx) {
        Sequence<?> sequenceName = parseSequenceName(ctx);
        parseKeyword(ctx, "TO");
        return ctx.dsl.alterSequence((Sequence) sequenceName).restartWith(parseUnsignedInteger(ctx));
    }

    private static final DDLQuery parseDropSequence(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Sequence<?> sequenceName = parseSequenceName(ctx);

        return ifExists
            ? ctx.dsl.dropSequenceIfExists(sequenceName)
            : ctx.dsl.dropSequence(sequenceName);
    }

    private static final DDLQuery parseCreateTable(ParserContext ctx, boolean temporary) {
        boolean ifNotExists = parseKeywordIf(ctx, "IF NOT EXISTS");
        Table<?> tableName = DSL.table(parseTableName(ctx).getQualifiedName());
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

        if (parseIf(ctx, '(')) {

            columnLoop:
            do {
                int position = ctx.position();

                ConstraintTypeStep constraint = parseConstraintNameSpecification(ctx);

                if (parsePrimaryKeyClusteredNonClusteredKeywordIf(ctx)) {
                    if (primary)
                        throw ctx.exception("Duplicate primary key specification");

                    primary = true;
                    constraints.add(parsePrimaryKeySpecification(ctx, constraint));
                    continue columnLoop;
                }
                else if (parseKeywordIf(ctx, "UNIQUE")) {
                    if (!parseKeywordIf(ctx, "KEY"))
                        parseKeywordIf(ctx, "INDEX");

                    // [#9132] Avoid parsing "using" as an identifier
                    parseUsingBtreeOrHashIf(ctx);

                    // [#7268] MySQL has some legacy syntax where an index name
                    //         can override a constraint name
                    Name index = parseIdentifierIf(ctx);
                    if (index != null)
                        constraint = constraint(index);

                    constraints.add(parseUniqueSpecification(ctx, constraint));
                    continue columnLoop;
                }
                else if (parseKeywordIf(ctx, "FOREIGN KEY")) {
                    constraints.add(parseForeignKeySpecification(ctx, constraint));
                    continue columnLoop;
                }
                else if (parseKeywordIf(ctx, "CHECK")) {
                    constraints.add(parseCheckSpecification(ctx, constraint));
                    continue columnLoop;
                }
                else if (constraint == null && parseIndexOrKeyIf(ctx)) {
                    parseUsingBtreeOrHashIf(ctx);

                    int p2 = ctx.position();

                    // [#7348] [#7651] [#9132] Look ahead if the next tokens
                    // indicate a MySQL index definition
                    if (parseIf(ctx, '(') || (parseIdentifierIf(ctx) != null
                                && parseUsingBtreeOrHashIf(ctx)
                                && parseIf(ctx, '('))) {
                        ctx.position(p2);
                        indexes.add(parseIndexSpecification(ctx, tableName));

                        parseUsingBtreeOrHashIf(ctx);
                        continue columnLoop;
                    }
                    else {
                        ctx.position(position);
                    }
                }
                else if (constraint != null)
                    throw ctx.expected("CHECK", "CONSTRAINT", "FOREIGN KEY", "INDEX", "KEY", "PRIMARY KEY", "UNIQUE");

                Name fieldName = parseIdentifier(ctx);

                if (ctas == null)
                    ctas = peek(ctx, ',') || peek(ctx, ')');

                // If only we had multiple return values or destructuring...
                ParseInlineConstraints inlineConstraints = parseInlineConstraints(
                    ctx,
                    fieldName,
                    !TRUE.equals(ctas) ? parseDataType(ctx) : SQLDataType.OTHER,
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
            while (parseIf(ctx, ','));

            if (fields.isEmpty())
                throw ctx.expected("At least one column");

            parse(ctx, ')');
        }
        else
            ctas = true;

        CreateTableColumnStep columnStep = ifNotExists
            ? temporary
                ? ctx.dsl.createTemporaryTableIfNotExists(tableName)
                : ctx.dsl.createTableIfNotExists(tableName)
            : temporary
                ? ctx.dsl.createTemporaryTable(tableName)
                : ctx.dsl.createTable(tableName);

        if (!fields.isEmpty())
            columnStep = columnStep.columns(fields);

        if (TRUE.equals(ctas) && parseKeyword(ctx, "AS") ||
           !FALSE.equals(ctas) && parseKeywordIf(ctx, "AS")) {
            boolean previousMetaLookupsForceIgnore = ctx.metaLookupsForceIgnore();
            CreateTableWithDataStep withDataStep = columnStep.as((Select<Record>) parseQuery(ctx.metaLookupsForceIgnore(false), true, true));
            ctx.metaLookupsForceIgnore(previousMetaLookupsForceIgnore);
            commentStep =
                  parseKeywordIf(ctx, "WITH DATA")
                ? withDataStep.withData()
                : parseKeywordIf(ctx, "WITH NO DATA")
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
            if (temporary && parseKeywordIf(ctx, "ON COMMIT")) {
                if (parseKeywordIf(ctx, "DELETE ROWS"))
                    commentStep = onCommitStep.onCommitDeleteRows();
                else if (parseKeywordIf(ctx, "DROP"))
                    commentStep = onCommitStep.onCommitDrop();
                else if (parseKeywordIf(ctx, "PRESERVE ROWS"))
                    commentStep = onCommitStep.onCommitPreserveRows();
                else
                    throw ctx.unsupportedClause();
            }
            else
                commentStep = onCommitStep;
        }

        storageStep = commentStep;

        List<SQL> storage = new ArrayList<>();
        Comment comment = null;

        storageLoop:
        for (boolean first = true;; first = false) {
            boolean optional = first || !parseIf(ctx, ',');
            Keyword keyword = null;

            // MySQL storage clauses (see: https://dev.mysql.com/doc/refman/5.7/en/create-table.html)
            if ((keyword = parseAndGetKeywordIf(ctx, "AUTO_INCREMENT")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseFieldUnsignedNumericLiteral(ctx, Sign.NONE)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "AVG_ROW_LENGTH")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseFieldUnsignedNumericLiteral(ctx, Sign.NONE)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "CHARACTER SET")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseIdentifier(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "DEFAULT CHARACTER SET")) != null
                  || (keyword = parseAndGetKeywordIf(ctx, "DEFAULT CHARSET")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseIdentifier(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "CHECKSUM")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseZeroOne(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "COLLATE")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseIdentifier(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "DEFAULT COLLATE")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseIdentifier(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "COMMENT")) != null) {
                parseIf(ctx, '=');
                comment = parseComment(ctx);
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "COMPRESSION")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseStringLiteral(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "CONNECTION")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseStringLiteral(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "DATA DIRECTORY")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseStringLiteral(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "INDEX DIRECTORY")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseStringLiteral(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "DELAY_KEY_WRITE")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseZeroOne(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "ENCRYPTION")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseStringLiteral(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "ENGINE")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseIdentifier(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "INSERT_METHOD")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseAndGetKeyword(ctx, "NO", "FIRST", "LAST")));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "KEY_BLOCK_SIZE")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseFieldUnsignedNumericLiteral(ctx, Sign.NONE)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "MAX_ROWS")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseFieldUnsignedNumericLiteral(ctx, Sign.NONE)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "MIN_ROWS")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseFieldUnsignedNumericLiteral(ctx, Sign.NONE)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "PACK_KEYS")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseZeroOneDefault(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "PASSWORD")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseStringLiteral(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "ROW_FORMAT")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseAndGetKeyword(ctx, "DEFAULT", "DYNAMIC", "FIXED", "COMPRESSED", "REDUNDANT", "COMPACT")));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "STATS_AUTO_RECALC")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseZeroOneDefault(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "STATS_PERSISTENT")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseZeroOneDefault(ctx)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "STATS_SAMPLE_PAGES")) != null) {
                parseIf(ctx, '=');
                storage.add(sql("{0} {1}", keyword, parseFieldUnsignedNumericLiteral(ctx, Sign.NONE)));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "TABLESPACE")) != null) {
                storage.add(sql("{0} {1}", keyword, parseIdentifier(ctx)));

                if ((keyword = parseAndGetKeywordIf(ctx, "STORAGE")) != null)
                    storage.add(sql("{0} {1}", keyword, parseAndGetKeyword(ctx, "DISK", "MEMORY", "DEFAULT")));
            }
            else if ((keyword = parseAndGetKeywordIf(ctx, "UNION")) != null) {
                parseIf(ctx, '=');
                parse(ctx, '(');
                storage.add(sql("{0} ({1})", keyword, list(parseIdentifiers(ctx))));
                parse(ctx, ')');
            }
            else if (optional)
                break storageLoop;
            else
                throw ctx.expected("storage clause after ','");
        }

        if (comment != null)
            storageStep = commentStep.comment(comment);

        if (storage.size() > 0)
            return storageStep.storage(new SQLConcatenationImpl(storage.toArray(EMPTY_QUERYPART)));
        else
            return storageStep;
    }

    private static final class ParseInlineConstraints {
        final DataType<?> type;
        final Comment     fieldComment;
        final boolean     primary;
        final boolean     identity;

        ParseInlineConstraints(DataType<?> type, Comment fieldComment, boolean primary, boolean identity) {
            this.type = type;
            this.fieldComment = fieldComment;
            this.primary = primary;
            this.identity = identity;
        }
    }

    private static final ParseInlineConstraints parseInlineConstraints(
        ParserContext ctx,
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
                if (parseKeywordIf(ctx, "NULL")) {
                    type = type.nullable(true);
                    nullable = true;
                    continue;
                }
                else if (parseKeywordIf(ctx, "NOT NULL") && (parseKeywordIf(ctx, "ENABLE") || true)) {
                    type = type.nullable(false);
                    nullable = true;
                    continue;
                }
            }

            if (!defaultValue) {
                if (!identity && parseKeywordIf(ctx, "IDENTITY")) {
                    if (parseIf(ctx, '(')) {
                        parseSignedInteger(ctx);
                        parse(ctx, ',');
                        parseSignedInteger(ctx);
                        parse(ctx, ')');
                    }

                    type = type.identity(true);
                    defaultValue = true;
                    identity = true;
                    continue;
                }
                else if (parseKeywordIf(ctx, "DEFAULT")) {

                    // TODO: Ignored keyword from Oracle
                    parseKeywordIf(ctx, "ON NULL");

                    type = type.defaultValue((Field) toField(ctx, parseConcat(ctx, null)));
                    defaultValue = true;
                    identity = true;
                    continue;
                }
                else if (!identity && parseKeywordIf(ctx, "GENERATED")) {
                    if (!parseKeywordIf(ctx, "ALWAYS")) {
                        parseKeyword(ctx, "BY DEFAULT");

                        // TODO: Ignored keyword from Oracle
                        parseKeywordIf(ctx, "ON NULL");
                    }

                    parseKeyword(ctx, "AS IDENTITY");

                    // TODO: Ignored identity options from Oracle
                    if (parseIf(ctx, '(')) {
                        boolean identityOption = false;

                        for (;;) {
                            if (identityOption)
                                parseIf(ctx, ',');

                            if (parseKeywordIf(ctx, "START WITH")) {
                                if (!parseKeywordIf(ctx, "LIMIT VALUE"))
                                    parseUnsignedIntegerOrBindVariable(ctx);
                                identityOption = true;
                                continue;
                            }
                            else if (parseKeywordIf(ctx, "INCREMENT BY")
                                  || parseKeywordIf(ctx, "MAXVALUE")
                                  || parseKeywordIf(ctx, "MINVALUE")
                                  || parseKeywordIf(ctx, "CACHE")) {
                                parseUnsignedIntegerOrBindVariable(ctx);
                                identityOption = true;
                                continue;
                            }
                            else if (parseKeywordIf(ctx, "NOMAXVALUE")
                                  || parseKeywordIf(ctx, "NOMINVALUE")
                                  || parseKeywordIf(ctx, "CYCLE")
                                  || parseKeywordIf(ctx, "NOCYCLE")
                                  || parseKeywordIf(ctx, "NOCACHE")
                                  || parseKeywordIf(ctx, "ORDER")
                                  || parseKeywordIf(ctx, "NOORDER")) {
                                identityOption = true;
                                continue;
                            }

                            if (identityOption)
                                break;
                            else
                                throw ctx.unsupportedClause();
                        }

                        parse(ctx, ')');
                    }

                    type = type.identity(true);
                    defaultValue = true;
                    identity = true;
                    continue;
                }
            }

            if (!onUpdate) {
                if (parseKeywordIf(ctx, "ON UPDATE")) {

                    // [#6132] TODO: Support this feature in the jOOQ DDL API
                    parseConcat(ctx, null);
                    onUpdate = true;
                    continue;
                }
            }

            ConstraintTypeStep inlineConstraint = parseConstraintNameSpecification(ctx);

            if (!unique) {
                if (!primary && parsePrimaryKeyClusteredNonClusteredKeywordIf(ctx)) {
                    if (!parseKeywordIf(ctx, "CLUSTERED"))
                        parseKeywordIf(ctx, "NONCLUSTERED");

                    constraints.add(parseConstraintEnforcementIf(ctx, inlineConstraint == null
                        ? primaryKey(fieldName)
                        : inlineConstraint.primaryKey(fieldName)));
                    primary = true;
                    unique = true;
                    continue;
                }
                else if (parseKeywordIf(ctx, "UNIQUE")) {
                    if (!parseKeywordIf(ctx, "KEY"))
                        parseKeywordIf(ctx, "INDEX");

                    constraints.add(parseConstraintEnforcementIf(ctx, inlineConstraint == null
                        ? unique(fieldName)
                        : inlineConstraint.unique(fieldName)));
                    unique = true;
                    continue;
                }
            }

            if (parseKeywordIf(ctx, "CHECK")) {
                constraints.add(parseCheckSpecification(ctx, inlineConstraint));
                continue;
            }

            if (parseKeywordIf(ctx, "REFERENCES")) {
                constraints.add(parseForeignKeyReferenceSpecification(ctx, inlineConstraint, new Field[] { field(fieldName) }));
                continue;
            }

            if (inlineConstraint != null)
                throw ctx.expected("CHECK", "PRIMARY KEY", "REFERENCES", "UNIQUE");

            if (!identity) {
                if (parseKeywordIf(ctx, "AUTO_INCREMENT") ||
                    parseKeywordIf(ctx, "AUTOINCREMENT")) {
                    type = type.identity(true);
                    identity = true;
                    continue;
                }
            }

            if (!comment) {
                if (parseKeywordIf(ctx, "COMMENT")) {
                    fieldComment = parseComment(ctx);
                    continue;
                }
            }

            break;
        }

        return new ParseInlineConstraints(type, fieldComment, primary, identity);
    }

    private static final boolean parsePrimaryKeyClusteredNonClusteredKeywordIf(ParserContext ctx) {
        if (!parseKeywordIf(ctx, "PRIMARY KEY"))
            return false;

        if (!parseKeywordIf(ctx, "CLUSTERED"))
            parseKeywordIf(ctx, "NONCLUSTERED");

        return true;
    }

    private static final DDLQuery parseCreateType(ParserContext ctx) {
        Name name = parseIdentifier(ctx);
        parseKeyword(ctx, "AS ENUM");
        List<String> values = new ArrayList<>();
        parse(ctx, '(');

        if (!parseIf(ctx, ')')) {
            do {
                values.add(parseStringLiteral(ctx));
            }
            while (parseIf(ctx, ','));
            parse(ctx, ')');
        }

        return ctx.dsl.createType(name).asEnum(values);
    }

    private static final Index parseIndexSpecification(ParserContext ctx, Table<?> table) {
        Name name = parseIdentifierIf(ctx);
        parseUsingBtreeOrHashIf(ctx);
        parse(ctx, '(');
        SortField<?>[] fields = parseSortSpecification(ctx).toArray(EMPTY_SORTFIELD);
        parse(ctx, ')');
        return Internal.createIndex(name == null ? NO_NAME : name, table, fields, false);
    }

    private static final Constraint parseConstraintEnforcementIf(ParserContext ctx, ConstraintEnforcementStep e) {
        if ((parseKeywordIf(ctx, "ENABLE") || parseKeywordIf(ctx, "ENFORCED")) && ctx.requireProEdition())



            ;
        else if ((parseKeywordIf(ctx, "DISABLE") || parseKeywordIf(ctx, "NOT ENFORCED")) && ctx.requireProEdition())



            ;

        return e;
    }

    private static final Constraint parsePrimaryKeySpecification(ParserContext ctx, ConstraintTypeStep constraint) {
        parseUsingBtreeOrHashIf(ctx);
        Field<?>[] fieldNames = parseKeyColumnList(ctx);

        ConstraintEnforcementStep e = constraint == null
            ? primaryKey(fieldNames)
            : constraint.primaryKey(fieldNames);

        parseUsingBtreeOrHashIf(ctx);
        return parseConstraintEnforcementIf(ctx, e);
    }

    private static final Constraint parseUniqueSpecification(ParserContext ctx, ConstraintTypeStep constraint) {
        parseUsingBtreeOrHashIf(ctx);
        Field<?>[] fieldNames = parseKeyColumnList(ctx);

        ConstraintEnforcementStep e = constraint == null
            ? unique(fieldNames)
            : constraint.unique(fieldNames);

        parseUsingBtreeOrHashIf(ctx);
        return parseConstraintEnforcementIf(ctx, e);
    }

    private static Field<?>[] parseKeyColumnList(ParserContext ctx) {
        parse(ctx, '(');
        SortField<?>[] fieldExpressions = parseSortSpecification(ctx).toArray(EMPTY_SORTFIELD);
        parse(ctx, ')');

        Field<?>[] fieldNames = new Field[fieldExpressions.length];

        for (int i = 0; i < fieldExpressions.length; i++)
            if (fieldExpressions[i].getOrder() != SortOrder.DESC)
                fieldNames[i] = ((SortFieldImpl<?>) fieldExpressions[i]).getField();

            // [#7899] TODO: Support this in jOOQ
            else
                throw ctx.notImplemented("DESC sorting in constraints");

        return fieldNames;
    }

    private static final Constraint parseCheckSpecification(ParserContext ctx, ConstraintTypeStep constraint) {
        parse(ctx, '(');
        Condition condition = parseCondition(ctx);
        parse(ctx, ')');

        ConstraintEnforcementStep e = constraint == null
            ? check(condition)
            : constraint.check(condition);

        return parseConstraintEnforcementIf(ctx, e);
    }

    private static final Constraint parseForeignKeySpecification(ParserContext ctx, ConstraintTypeStep constraint) {
        Name constraintName = null;
        if ((constraintName = parseIdentifierIf(ctx)) != null)
            if (constraint == null)
                constraint = constraint(constraintName);

        parse(ctx, '(');
        Field<?>[] referencing = parseFieldNames(ctx).toArray(EMPTY_FIELD);
        parse(ctx, ')');
        parseKeyword(ctx, "REFERENCES");

        return parseForeignKeyReferenceSpecification(ctx, constraint, referencing);
    }

    private static final Constraint parseForeignKeyReferenceSpecification(ParserContext ctx, ConstraintTypeStep constraint, Field<?>[] referencing) {
        Table<?> referencedTable = parseTableName(ctx);
        Field<?>[] referencedFields = EMPTY_FIELD;

        if (parseIf(ctx, '(')) {
            referencedFields = parseFieldNames(ctx).toArray(EMPTY_FIELD);
            parse(ctx, ')');

            if (referencing.length != referencedFields.length)
                throw ctx.exception("Number of referencing columns (" + referencing.length + ") must match number of referenced columns (" + referencedFields.length + ")");
        }

        ConstraintForeignKeyOnStep e = constraint == null
            ? foreignKey(referencing).references(referencedTable, referencedFields)
            : constraint.foreignKey(referencing).references(referencedTable, referencedFields);

        boolean onDelete = false;
        boolean onUpdate = false;
        while ((!onDelete || !onUpdate) && parseKeywordIf(ctx, "ON")) {
            if (!onDelete && parseKeywordIf(ctx, "DELETE")) {
                onDelete = true;

                if (parseKeywordIf(ctx, "CASCADE"))
                    e = e.onDeleteCascade();
                else if (parseKeywordIf(ctx, "NO ACTION"))
                    e = e.onDeleteNoAction();
                else if (parseKeywordIf(ctx, "RESTRICT"))
                    e = e.onDeleteRestrict();
                else if (parseKeywordIf(ctx, "SET DEFAULT"))
                    e = e.onDeleteSetDefault();
                else if (parseKeywordIf(ctx, "SET NULL"))
                    e = e.onDeleteSetNull();
                else
                    throw ctx.expected("CASCADE", "NO ACTION", "RESTRICT", "SET DEFAULT", "SET NULL");
            }
            else if (!onUpdate && parseKeywordIf(ctx, "UPDATE")) {
                onUpdate = true;

                if (parseKeywordIf(ctx, "CASCADE"))
                    e = e.onUpdateCascade();
                else if (parseKeywordIf(ctx, "NO ACTION"))
                    e = e.onUpdateNoAction();
                else if (parseKeywordIf(ctx, "RESTRICT"))
                    e = e.onUpdateRestrict();
                else if (parseKeywordIf(ctx, "SET DEFAULT"))
                    e = e.onUpdateSetDefault();
                else if (parseKeywordIf(ctx, "SET NULL"))
                    e = e.onUpdateSetNull();
                else
                    throw ctx.expected("CASCADE", "NO ACTION", "RESTRICT", "SET DEFAULT", "SET NULL");
            }
            else
                throw ctx.expected("DELETE", "UPDATE");
        }

        return parseConstraintEnforcementIf(ctx, e);
    }

    private static final Set<String> ALTER_KEYWORDS = new HashSet<>(Arrays.asList("ADD", "ALTER", "COMMENT", "DROP", "MODIFY", "RENAME"));

    private static final DDLQuery parseAlterTable(ParserContext ctx) {
        boolean ifTableExists = parseKeywordIf(ctx, "IF EXISTS");
        Table<?> tableName;

        if (peekKeyword(ctx, "ONLY")) {

            // [#7751] ONLY is only supported by PostgreSQL. In other RDBMS, it
            //         corresponds to a table name.
            Name only = parseIdentifier(ctx);
            int position = ctx.position();

            if ((tableName = parseTableNameIf(ctx)) == null || (
                    !tableName.getQualifiedName().qualified()
                &&  tableName.getUnqualifiedName().quoted() == Quoted.UNQUOTED
                &&  ALTER_KEYWORDS.contains(tableName.getName().toUpperCase()))) {
                tableName = table(only);
                ctx.position(position);
            }
        }
        else {
            tableName = parseTableName(ctx);
        }

        AlterTableStep s1 = ifTableExists
            ? ctx.dsl.alterTableIfExists(tableName)
            : ctx.dsl.alterTable(tableName);

        switch (ctx.character()) {
            case 'a':
            case 'A':
                if (parseKeywordIf(ctx, "ADD"))
                    return parseAlterTableAdd(ctx, s1, tableName);
                else if (parseKeywordIf(ctx, "ALTER"))
                    if (parseKeywordIf(ctx, "CONSTRAINT"))
                        return parseAlterTableAlterConstraint(ctx, s1);
                    else if ((parseKeywordIf(ctx, "COLUMN") || true))
                        return parseAlterTableAlterColumn(ctx, s1);

                break;

            case 'c':
            case 'C':

                // TODO: support all of the storageLoop from the CREATE TABLE statement
                if (parseKeywordIf(ctx, "COMMENT")) {
                    parseIf(ctx, '=');
                    return ctx.dsl.commentOnTable(tableName).is(parseStringLiteral(ctx));
                }

                break;

            case 'd':
            case 'D':
                if (parseKeywordIf(ctx, "DROP")) {
                    if (parseKeywordIf(ctx, "CONSTRAINT")) {
                        return parseCascadeRestrictIf(ctx, parseKeywordIf(ctx, "IF EXISTS")
                            ? s1.dropConstraintIfExists(parseIdentifier(ctx))
                            : s1.dropConstraint(parseIdentifier(ctx)));
                    }
                    else if (parseKeywordIf(ctx, "UNIQUE")) {
                        return parseCascadeRestrictIf(ctx, s1.dropUnique(
                              peek(ctx, '(')
                            ? unique(parseKeyColumnList(ctx))
                            : constraint(parseIdentifier(ctx))));
                    }
                    else if (parseKeywordIf(ctx, "PRIMARY KEY")) {
                        Name identifier = parseIdentifierIf(ctx);
                        return parseCascadeRestrictIf(ctx, identifier == null ? s1.dropPrimaryKey() : s1.dropPrimaryKey(identifier));
                    }
                    else if (parseKeywordIf(ctx, "FOREIGN KEY")) {
                        return s1.dropForeignKey(parseIdentifier(ctx));
                    }
                    else if (parseKeywordIf(ctx, "INDEX")
                          || parseKeywordIf(ctx, "KEY")) {
                        return ctx.dsl.dropIndex(parseIdentifier(ctx)).on(tableName);
                    }
                    else {
                        parseKeywordIf(ctx, "COLUMN");
                        boolean ifColumnExists = parseKeywordIf(ctx, "IF EXISTS");
                        boolean parens = parseIf(ctx, '(');
                        Field<?> field = parseFieldName(ctx);
                        List<Field<?>> fields = null;

                        if (!ifColumnExists) {
                            while (parseIf(ctx, ',')) {
                                if (fields == null) {
                                    fields = new ArrayList<>();
                                    fields.add(field);
                                }

                                fields.add(parseFieldName(ctx));
                            }
                        }

                        if (parens)
                            parse(ctx, ')');

                        return parseCascadeRestrictIf(ctx, fields == null
                            ? ifColumnExists
                                ? s1.dropColumnIfExists(field)
                                : s1.dropColumn(field)
                            : s1.dropColumns(fields)
                        );
                    }
                }

                break;

            case 'm':
            case 'M':
                if (parseKeywordIf(ctx, "MODIFY"))
                    if (parseKeywordIf(ctx, "CONSTRAINT"))
                        return parseAlterTableAlterConstraint(ctx, s1);
                    else if ((parseKeywordIf(ctx, "COLUMN") || true))
                        return parseAlterTableAlterColumn(ctx, s1);

                break;

            case 'r':
            case 'R':
                if (parseKeywordIf(ctx, "RENAME")) {
                    if (parseKeywordIf(ctx, "AS") || parseKeywordIf(ctx, "TO")) {
                        Table<?> newName = parseTableName(ctx);

                        return s1.renameTo(newName);
                    }
                    else if (parseKeywordIf(ctx, "COLUMN")) {
                        Name oldName = parseIdentifier(ctx);
                        if (!parseKeywordIf(ctx, "AS"))
                            parseKeyword(ctx, "TO");
                        Name newName = parseIdentifier(ctx);

                        return s1.renameColumn(oldName).to(newName);
                    }
                    else if (parseKeywordIf(ctx, "INDEX")) {
                        Name oldName = parseIdentifier(ctx);
                        if (!parseKeywordIf(ctx, "AS"))
                            parseKeyword(ctx, "TO");
                        Name newName = parseIdentifier(ctx);

                        return s1.renameIndex(oldName).to(newName);
                    }
                    else if (parseKeywordIf(ctx, "CONSTRAINT")) {
                        Name oldName = parseIdentifier(ctx);
                        if (!parseKeywordIf(ctx, "AS"))
                            parseKeyword(ctx, "TO");
                        Name newName = parseIdentifier(ctx);

                        return s1.renameConstraint(oldName).to(newName);
                    }
                }

                break;
        }

        throw ctx.expected("ADD", "ALTER", "COMMENT", "DROP", "MODIFY", "RENAME");
    }

    private static final AlterTableFinalStep parseCascadeRestrictIf(ParserContext ctx, AlterTableDropStep step) {
        boolean cascade = parseKeywordIf(ctx, "CASCADE");
        boolean restrict = !cascade && parseKeywordIf(ctx, "RESTRICT");

        return cascade
            ? step.cascade()
            : restrict
            ? step.restrict()
            : step;
    }

    private static final DDLQuery parseAlterTableAdd(ParserContext ctx, AlterTableStep s1, Table<?> tableName) {
        List<FieldOrConstraint> list = new ArrayList<>();

        if (parseIndexOrKeyIf(ctx)) {
            Name name = parseIdentifierIf(ctx);
            parse(ctx, '(');
            List<SortField<?>> sort = parseSortSpecification(ctx);
            parse(ctx, ')');

            return name == null
                ? ctx.dsl.createIndex().on(tableName, sort)
                : ctx.dsl.createIndex(name).on(tableName, sort);
        }

        if (parseIf(ctx, '(')) {
            do {
                parseAlterTableAddFieldsOrConstraints(ctx, list);
            }
            while (parseIf(ctx, ','));

            parse(ctx, ')');
        }
        else if (parseKeywordIf(ctx, "COLUMN IF NOT EXISTS")
              || parseKeywordIf(ctx, "IF NOT EXISTS")) {
            return parseAlterTableAddFieldFirstBeforeLast(ctx, s1.addColumnIfNotExists(parseAlterTableAddField(ctx, null)));
        }
        else {
            do {
                parseAlterTableAddFieldsOrConstraints(ctx, list);
            }
            while (parseIf(ctx, ',') && parseKeyword(ctx, "ADD"));
        }

        if (list.size() == 1)
            if (list.get(0) instanceof Constraint)
                return s1.add((Constraint) list.get(0));
            else
                return parseAlterTableAddFieldFirstBeforeLast(ctx, s1.add((Field<?>) list.get(0)));
        else
            return parseAlterTableAddFieldFirstBeforeLast(ctx, s1.add(list));
    }

    private static final DDLQuery parseAlterTableAddFieldFirstBeforeLast(ParserContext ctx, AlterTableAddStep step) {
        if (parseKeywordIf(ctx, "FIRST"))
            return step.first();
        else if (parseKeywordIf(ctx, "BEFORE"))
            return step.before(parseFieldName(ctx));
        else if (parseKeywordIf(ctx, "AFTER"))
            return step.after(parseFieldName(ctx));
        else
            return step;
    }

    private static final boolean parseIndexOrKeyIf(ParserContext ctx) {
        return ((parseKeywordIf(ctx, "SPATIAL INDEX")
            || parseKeywordIf(ctx, "SPATIAL KEY")
            || parseKeywordIf(ctx, "FULLTEXT INDEX")
            || parseKeywordIf(ctx, "FULLTEXT KEY"))
            && ctx.requireUnsupportedSyntax())

            || parseKeywordIf(ctx, "INDEX")
            || parseKeywordIf(ctx, "KEY");
    }

    private static final void parseAlterTableAddFieldsOrConstraints(ParserContext ctx, List<FieldOrConstraint> list) {
        ConstraintTypeStep constraint = parseConstraintNameSpecification(ctx);

        if (parsePrimaryKeyClusteredNonClusteredKeywordIf(ctx))
            list.add(parsePrimaryKeySpecification(ctx, constraint));
        else if (parseKeywordIf(ctx, "UNIQUE") && (parseKeywordIf(ctx, "KEY") || parseKeywordIf(ctx, "INDEX") || true))
            list.add(parseUniqueSpecification(ctx, constraint));
        else if (parseKeywordIf(ctx, "FOREIGN KEY"))
            list.add(parseForeignKeySpecification(ctx, constraint));
        else if (parseKeywordIf(ctx, "CHECK"))
            list.add(parseCheckSpecification(ctx, constraint));
        else if (constraint != null)
            throw ctx.expected("CHECK", "FOREIGN KEY", "PRIMARY KEY", "UNIQUE");
        else if (parseKeywordIf(ctx, "COLUMN") || true)
            parseAlterTableAddField(ctx, list);
    }

    private static final ConstraintTypeStep parseConstraintNameSpecification(ParserContext ctx) {
        if (parseKeywordIf(ctx, "CONSTRAINT") && !peekKeyword(ctx, "PRIMARY KEY", "UNIQUE", "FOREIGN KEY", "CHECK"))
            return constraint(parseIdentifier(ctx));

        return null;
    }

    private static final Field<?> parseAlterTableAddField(ParserContext ctx, List<FieldOrConstraint> list) {

        // The below code is taken from CREATE TABLE, with minor modifications as
        // https://github.com/jOOQ/jOOQ/issues/5317 has not yet been implemented
        // Once implemented, we might be able to factor out the common logic into
        // a new parseXXX() method.

        Name fieldName = parseIdentifier(ctx);
        DataType type = parseDataType(ctx);
        int position = list == null ? -1 : list.size();

        ParseInlineConstraints inline = parseInlineConstraints(ctx, fieldName, type, list, false, false);
        Field<?> result = field(fieldName, inline.type, inline.fieldComment);

        if (list != null)
            list.add(position, result);

        return result;
    }

    private static final DDLQuery parseAlterTableAlterColumn(ParserContext ctx, AlterTableStep s1) {
        boolean paren = parseIf(ctx, '(');
        TableField<?, ?> field = parseFieldName(ctx);

        if (!paren)
            if (parseKeywordIf(ctx, "DROP NOT NULL") || parseKeywordIf(ctx, "SET NULL") || parseKeywordIf(ctx, "NULL"))
                return s1.alter(field).dropNotNull();
            else if (parseKeywordIf(ctx, "DROP DEFAULT"))
                return s1.alter(field).dropDefault();
            else if (parseKeywordIf(ctx, "SET NOT NULL") || parseKeywordIf(ctx, "NOT NULL"))
                return s1.alter(field).setNotNull();
            else if (parseKeywordIf(ctx, "SET DEFAULT"))
                return s1.alter(field).default_((Field) toField(ctx, parseConcat(ctx, null)));
            else if (parseKeywordIf(ctx, "TO") || parseKeywordIf(ctx, "RENAME TO") || parseKeywordIf(ctx, "RENAME AS"))
                return s1.renameColumn(field).to(parseFieldName(ctx));
            else if (parseKeywordIf(ctx, "TYPE") || parseKeywordIf(ctx, "SET DATA TYPE"))
                ;

        DataType<?> type = parseDataType(ctx);

        if (parseKeywordIf(ctx, "NULL"))
            type = type.nullable(true);
        else if (parseKeywordIf(ctx, "NOT NULL") && (parseKeywordIf(ctx, "ENABLE") || true))
            type = type.nullable(false);

        if (paren)
            parse(ctx, ')');

        return s1.alter(field).set(type);
    }

    private static final DDLQuery parseAlterTableAlterConstraint(ParserContext ctx, AlterTableStep s1) {
        ctx.requireProEdition();










        throw ctx.expected("ENABLE", "ENFORCED", "DISABLE", "NOT ENFORCED");
    }

    private static final DDLQuery parseAlterType(ParserContext ctx) {
        AlterTypeStep s1 = ctx.dsl.alterType(parseName(ctx));


        if (parseKeywordIf(ctx, "ADD VALUE"))
            return s1.addValue(parseStringLiteral(ctx));
        else if (parseKeywordIf(ctx, "RENAME TO"))
            return s1.renameTo(parseIdentifier(ctx));
        else if (parseKeywordIf(ctx, "RENAME VALUE"))
            return s1.renameValue(parseStringLiteral(ctx)).to(parseKeyword(ctx, "TO") ? parseStringLiteral(ctx) : null);
        else if (parseKeywordIf(ctx, "SET SCHEMA"))
            return s1.setSchema(parseIdentifier(ctx));


        throw ctx.expected("ADD VALUE", "RENAME TO", "RENAME VALUE", "SET SCHEMA");
    }

    private static final DDLQuery parseRename(ParserContext ctx) {
        parseKeyword(ctx, "RENAME");

        switch (ctx.character()) {
            case 'c':
            case 'C':
                if (parseKeywordIf(ctx, "COLUMN")) {
                    TableField<?, ?> oldName = parseFieldName(ctx);
                    if (!parseKeywordIf(ctx, "AS"))
                        parseKeyword(ctx, "TO");
                    Field<?> newName = parseFieldName(ctx);

                    return ctx.dsl.alterTable(oldName.getTable()).renameColumn(oldName).to(newName);
                }

                break;

            case 'i':
            case 'I':
                if (parseKeywordIf(ctx, "INDEX")) {
                    Name oldName = parseIndexName(ctx);
                    if (!parseKeywordIf(ctx, "AS"))
                        parseKeyword(ctx, "TO");
                    Name newName = parseIndexName(ctx);

                    return ctx.dsl.alterIndex(oldName).renameTo(newName);
                }

                break;

            case 's':
            case 'S':
                if (parseKeywordIf(ctx, "SCHEMA")) {
                    Schema oldName = parseSchemaName(ctx);
                    if (!parseKeywordIf(ctx, "AS"))
                        parseKeyword(ctx, "TO");
                    Schema newName = parseSchemaName(ctx);

                    return ctx.dsl.alterSchema(oldName).renameTo(newName);
                }
                else if (parseKeywordIf(ctx, "SEQUENCE")) {
                    Sequence<?> oldName = parseSequenceName(ctx);
                    if (!parseKeywordIf(ctx, "AS"))
                        parseKeyword(ctx, "TO");
                    Sequence<?> newName = parseSequenceName(ctx);

                    return ctx.dsl.alterSequence(oldName).renameTo(newName);
                }

                break;

            case 'v':
            case 'V':
                if (parseKeywordIf(ctx, "VIEW")) {
                    Table<?> oldName = parseTableName(ctx);
                    if (!parseKeywordIf(ctx, "AS"))
                        parseKeyword(ctx, "TO");
                    Table<?> newName = parseTableName(ctx);

                    return ctx.dsl.alterView(oldName).renameTo(newName);
                }

                break;
        }

        // If all of the above fails, we can assume we're renaming a table.
        parseKeywordIf(ctx, "TABLE");
        Table<?> oldName = parseTableName(ctx);
        if (!parseKeywordIf(ctx, "AS"))
            parseKeyword(ctx, "TO");
        Table<?> newName = parseTableName(ctx);

        return ctx.dsl.alterTable(oldName).renameTo(newName);
    }

    private static final DDLQuery parseDropTable(ParserContext ctx, boolean temporary) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Table<?> tableName = parseTableName(ctx);
        boolean cascade = parseKeywordIf(ctx, "CASCADE");
        boolean restrict = !cascade && parseKeywordIf(ctx, "RESTRICT");

        DropTableStep s1;
        DropTableFinalStep s2;

        s1 = ifExists
           ? ctx.dsl.dropTableIfExists(tableName)
           : temporary
           ? ctx.dsl.dropTemporaryTable(tableName)
           : ctx.dsl.dropTable(tableName);

        s2 = cascade
           ? s1.cascade()
           : restrict
           ? s1.restrict()
           : s1;

        return s2;
    }

    private static final DDLQuery parseDropType(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        List<Name> typeNames = parseIdentifiers(ctx);
        boolean cascade = parseKeywordIf(ctx, "CASCADE");
        boolean restrict = !cascade && parseKeywordIf(ctx, "RESTRICT");

        DropTypeStep s1;
        DropTypeFinalStep s2;

        s1 = ifExists
           ? ctx.dsl.dropTypeIfExists(typeNames)
           : ctx.dsl.dropType(typeNames);

        s2 = cascade
           ? s1.cascade()
           : restrict
           ? s1.restrict()
           : s1;

        return s2;
    }

    private static final DDLQuery parseCreateSchema(ParserContext ctx) {
        boolean ifNotExists = parseKeywordIf(ctx, "IF NOT EXISTS");
        Schema schemaName = parseSchemaName(ctx);

        return ifNotExists
            ? ctx.dsl.createSchemaIfNotExists(schemaName)
            : ctx.dsl.createSchema(schemaName);
    }

    private static final DDLQuery parseAlterDatabase(ParserContext ctx) {
        parseSchemaName(ctx);
        parseAlterDatabaseFlags(ctx, true);
        return IGNORE;
    }

    private static final boolean parseAlterDatabaseFlags(ParserContext ctx, boolean throwOnFail) {
        parseKeywordIf(ctx, "DEFAULT");

        if (parseCharacterSetSpecificationIf(ctx) != null)
            return true;

        if (parseCollateSpecificationIf(ctx) != null)
            return true;

        if (parseKeywordIf(ctx, "ENCRYPTION")) {
            parseIf(ctx, '=');
            parseStringLiteral(ctx);
            return true;
        }

        if (throwOnFail)
            throw ctx.expected("CHARACTER SET", "COLLATE", "DEFAULT ENCRYPTION");
        else
            return false;
    }

    private static final DDLQuery parseAlterSchema(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Schema schemaName = parseSchemaName(ctx);
        AlterSchemaStep s1 = ifExists
            ? ctx.dsl.alterSchemaIfExists(schemaName)
            : ctx.dsl.alterSchema(schemaName);

        if (parseKeywordIf(ctx, "RENAME")) {
            if (!parseKeywordIf(ctx, "AS"))
                parseKeyword(ctx, "TO");
            Schema newName = parseSchemaName(ctx);
            AlterSchemaFinalStep s2 = s1.renameTo(newName);
            return s2;
        }
        else if (parseKeywordIf(ctx, "OWNER TO")) {
            parseUser(ctx);
            return IGNORE;
        }
        else if (parseAlterDatabaseFlags(ctx, false))
            return IGNORE;
        else
            throw ctx.expected("OWNER TO", "RENAME TO");
    }

    private static final DDLQuery parseDropSchema(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Schema schemaName = parseSchemaName(ctx);
        boolean cascade = parseKeywordIf(ctx, "CASCADE");
        boolean restrict = !cascade && parseKeywordIf(ctx, "RESTRICT");

        DropSchemaStep s1;
        DropSchemaFinalStep s2;

        s1 = ifExists
            ? ctx.dsl.dropSchemaIfExists(schemaName)
            : ctx.dsl.dropSchema(schemaName);

        s2 = cascade
            ? s1.cascade()
            : restrict
            ? s1.restrict()
            : s1;

        return s2;
    }

    private static final DDLQuery parseCreateIndex(ParserContext ctx, boolean unique) {
        boolean ifNotExists = parseKeywordIf(ctx, "IF NOT EXISTS");
        Name indexName = parseIndexNameIf(ctx);
        parseUsingBtreeOrHashIf(ctx);
        parseKeyword(ctx, "ON");
        Table<?> tableName = parseTableName(ctx);
        parse(ctx, '(');
        SortField<?>[] fields = parseSortSpecification(ctx).toArray(EMPTY_SORTFIELD);
        parse(ctx, ')');
        parseUsingBtreeOrHashIf(ctx);

        Name[] include = null;
        if (parseKeywordIf(ctx, "INCLUDE") || parseKeywordIf(ctx, "COVERING") || parseKeywordIf(ctx, "STORING")) {
            parse(ctx, '(');
            include = parseIdentifiers(ctx).toArray(EMPTY_NAME);
            parse(ctx, ')');
        }

        Condition condition = parseKeywordIf(ctx, "WHERE")
            ? parseCondition(ctx)
            : null;

        CreateIndexStep s1 = ifNotExists
            ? unique
                ? ctx.dsl.createUniqueIndexIfNotExists(indexName)
                : ctx.dsl.createIndexIfNotExists(indexName)
            : unique
                ? indexName == null
                    ? ctx.dsl.createUniqueIndex()
                    : ctx.dsl.createUniqueIndex(indexName)
                : indexName == null
                    ? ctx.dsl.createIndex()
                    : ctx.dsl.createIndex(indexName);

        CreateIndexIncludeStep s2 = s1.on(tableName, fields);
        CreateIndexWhereStep s3 = include != null
            ? s2.include(include)
            : s2;
        CreateIndexFinalStep s4 = condition != null
            ? s3.where(condition)
            : s3;

        return s4;
    }

    private static final boolean parseUsingBtreeOrHashIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "USING BTREE") || parseKeywordIf(ctx, "USING HASH"))
            ;

        return true;
    }

    private static final DDLQuery parseAlterDomain(ParserContext ctx) {
        parseIdentifier(ctx);

        // Some known PostgreSQL no-arg ALTER DOMAIN statements:
        // https://www.postgresql.org/docs/current/static/sql-alterdomain.html
        if (parseAndGetKeywordIf(ctx,
            "DROP DEFAULT",
            "DROP NOT NULL",
            "SET NOT NULL"
        ) != null)
            return IGNORE;

        // ALTER DOMAIN statements with arguments:
        else if (parseKeywordIf(ctx, "SET DEFAULT")) {
            parseConcat(ctx, null);
            return IGNORE;
        }
        else if (parseKeywordIf(ctx, "DROP CONSTRAINT")) {
            parseKeywordIf(ctx, "IF EXISTS");
            parseIdentifier(ctx);
            if (parseKeywordIf(ctx, "RESTRICT") || parseKeywordIf(ctx, "CASCADE"));
            return IGNORE;
        }
        else if (parseKeywordIf(ctx, "RENAME CONSTRAINT")) {
            parseIdentifier(ctx);
            if (!parseKeywordIf(ctx, "AS"))
                parseKeyword(ctx, "TO");
            parseIdentifier(ctx);
            return IGNORE;
        }
        else if (parseAndGetKeywordIf(ctx,
            "OWNER TO",
            "RENAME TO",
            "SET SCHEMA",
            "VALIDATE CONSTRAINT"
        ) != null) {
            parseIdentifier(ctx);
            return IGNORE;
        }

        // TODO (PostgreSQL): ADD
        else
            throw ctx.unsupportedClause();
    }

    private static final DDLQuery parseAlterIndex(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Name indexName = parseIndexName(ctx);
        parseKeyword(ctx, "RENAME");
        if (!parseKeywordIf(ctx, "AS"))
            parseKeyword(ctx, "TO");
        Name newName = parseIndexName(ctx);

        AlterIndexStep s1 = ifExists
            ? ctx.dsl.alterIndexIfExists(indexName)
            : ctx.dsl.alterIndex(indexName);
        AlterIndexFinalStep s2 = s1.renameTo(newName);
        return s2;

    }

    private static final DDLQuery parseDropIndex(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Name indexName = parseIndexName(ctx);
        boolean on = parseKeywordIf(ctx, "ON");
        Table<?> onTable = on ? parseTableName(ctx) : null;

        DropIndexOnStep s1;
        DropIndexCascadeStep s2;
        DropIndexFinalStep s3;

        s1 = ifExists
            ? ctx.dsl.dropIndexIfExists(indexName)
            : ctx.dsl.dropIndex(indexName);

        s2 = on
            ? s1.on(onTable)
            : s1;

        s3 = parseKeywordIf(ctx, "CASCADE")
            ? s2.cascade()
            : parseKeywordIf(ctx, "RESTRICT")
            ? s2.restrict()
            : s2;

        return s3;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // QueryPart parsing
    // -----------------------------------------------------------------------------------------------------------------

    private static final Condition parseCondition(ParserContext ctx) {
        return toCondition(ctx, parseOr(ctx));
    }

    private static final QueryPart parseOr(ParserContext ctx) {
        QueryPart condition = parseAnd(ctx);

        while (parseKeywordIf(ctx, "OR"))
            condition = toCondition(ctx, condition).or(toCondition(ctx, parseAnd(ctx)));

        return condition;
    }

    private static final QueryPart parseAnd(ParserContext ctx) {
        QueryPart condition = parseNot(ctx);

        while (parseKeywordIf(ctx, "AND"))
            condition = toCondition(ctx, condition).and(toCondition(ctx, parseNot(ctx)));

        return condition;
    }

    private static final QueryPart parseNot(ParserContext ctx) {
        boolean not = parseKeywordIf(ctx, "NOT");
        QueryPart condition = parsePredicate(ctx);
        return not ? toCondition(ctx, condition).not() : condition;
    }

    private static final QueryPart parsePredicate(ParserContext ctx) {
        if (parseKeywordIf(ctx, "EXISTS")) {
            parse(ctx, '(');
            Select<?> select = parseWithOrSelect(ctx);
            parse(ctx, ')');

            return exists(select);
        }
        else if (parseKeywordIf(ctx, "REGEXP_LIKE")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx);
            parse(ctx, ',');
            Field<?> f2 = parseField(ctx);
            parse(ctx, ')');

            return f1.likeRegex((Field) f2);
        }
        else if (parseKeywordIf(ctx, "UNIQUE")) {
            parse(ctx, '(');
            Select<?> select = parseWithOrSelect(ctx);
            parse(ctx, ')');

            return unique(select);
        }
        else {
            FieldOrRow left;
            Comparator comp;
            TSQLOuterJoinComparator outer;
            boolean not;
            boolean notOp = false;

            left = parseConcat(ctx, null);
            not = parseKeywordIf(ctx, "NOT");


            if (!not && ((outer = parseTSQLOuterJoinComparatorIf(ctx)) != null) && ctx.requireProEdition()) {
                Condition result = null;












                return result;
            }
            else if (!not && (comp = parseComparatorIf(ctx)) != null) {
                boolean all = parseKeywordIf(ctx, "ALL");
                boolean any = !all && (parseKeywordIf(ctx, "ANY") || parseKeywordIf(ctx, "SOME"));
                if (all || any)
                    parse(ctx, '(');

                // TODO equal degrees
                Condition result =
                      all
                    ? left instanceof Field
                        ? ((Field) left).compare(comp, DSL.all(parseWithOrSelect(ctx, 1)))
                        : new RowSubqueryCondition((Row) left, DSL.all(parseWithOrSelect(ctx, ((Row) left).size())), comp)
                    : any
                    ? left instanceof Field
                        ? ((Field) left).compare(comp, DSL.any(parseWithOrSelect(ctx, 1)))
                        : new RowSubqueryCondition((Row) left, DSL.any(parseWithOrSelect(ctx, ((Row) left).size())), comp)
                    : left instanceof Field
                        ? ((Field) left).compare(comp, toField(ctx, parseConcat(ctx, null)))
                        : new RowCondition((Row) left, parseRow(ctx, ((Row) left).size(), true), comp);

                if (all || any)
                    parse(ctx, ')');

                return result;
            }
            else if (!not && parseKeywordIf(ctx, "IS")) {
                not = parseKeywordIf(ctx, "NOT");

                if (parseKeywordIf(ctx, "NULL"))
                    return not
                        ? left instanceof Field
                            ? ((Field) left).isNotNull()
                            : ((Row) left).isNotNull()
                        : left instanceof Field
                            ? ((Field) left).isNull()
                            : ((Row) left).isNull();
                else if (left instanceof Field && parseKeywordIf(ctx, "JSON"))
                    return not
                        ? ((Field) left).isNotJson()
                        : ((Field) left).isJson();

                parseKeyword(ctx, "DISTINCT FROM");
                if (left instanceof Field) {
                    Field right = toField(ctx, parseConcat(ctx, null));
                    return not ? ((Field) left).isNotDistinctFrom(right) : ((Field) left).isDistinctFrom(right);
                }
                else {
                    Row right = parseRow(ctx, ((Row) left).size(), true);
                    return new RowIsDistinctFrom((Row) left, right, not);
                }
            }
            else if (!not && parseIf(ctx, "@>")) {
                return toField(ctx, left).contains((Field) toField(ctx, parseConcat(ctx, null)));
            }
            else if (parseKeywordIf(ctx, "IN")) {
                Condition result;

                parse(ctx, '(');
                if (peek(ctx, ')'))
                    result = not
                        ? left instanceof Field
                            ? ((Field) left).notIn(EMPTY_FIELD)
                            : new RowInCondition((Row) left, new QueryPartList<>(), true)
                        : left instanceof Field
                            ? ((Field) left).in(EMPTY_FIELD)
                            : new RowInCondition((Row) left, new QueryPartList<>(), false);
                else if (peekKeyword(ctx, "SELECT") || peekKeyword(ctx, "SEL") || peekKeyword(ctx, "WITH"))
                    result = not
                        ? left instanceof Field
                            ? ((Field) left).notIn(parseWithOrSelect(ctx, 1))
                            : new RowSubqueryCondition((Row) left, parseWithOrSelect(ctx, ((Row) left).size()), NOT_IN)
                        : left instanceof Field
                            ? ((Field) left).in(parseWithOrSelect(ctx, 1))
                            : new RowSubqueryCondition((Row) left, parseWithOrSelect(ctx, ((Row) left).size()), IN);
                else
                    result = not
                        ? left instanceof Field
                            ? ((Field) left).notIn(parseFields(ctx))
                            : new RowInCondition((Row) left, new QueryPartList<>(parseRows(ctx, ((Row) left).size())), true)
                        : left instanceof Field
                            ? ((Field) left).in(parseFields(ctx))
                            : new RowInCondition((Row) left, new QueryPartList<>(parseRows(ctx, ((Row) left).size())), false);

                parse(ctx, ')');
                return result;
            }
            else if (parseKeywordIf(ctx, "BETWEEN")) {
                boolean symmetric = parseKeywordIf(ctx, "SYMMETRIC");
                FieldOrRow r1 = left instanceof Field
                    ? parseConcat(ctx, null)
                    : parseRow(ctx, ((Row) left).size());
                parseKeyword(ctx, "AND");
                FieldOrRow r2 = left instanceof Field
                    ? parseConcat(ctx, null)
                    : parseRow(ctx, ((Row) left).size());

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
            else if (left instanceof Field && (parseKeywordIf(ctx, "LIKE") || parseOperatorIf(ctx, "~~") || (notOp = parseOperatorIf(ctx, "!~~")))) {
                if (parseKeywordIf(ctx, "ANY")) {
                    parse(ctx, '(');
                    if (peekKeyword(ctx, "SELECT") || peekKeyword(ctx, "SEL") || peekKeyword(ctx, "WITH")) {
                        Select<?> select = parseWithOrSelect(ctx);
                        parse(ctx, ')');
                        LikeEscapeStep result = (not ^ notOp) ? ((Field) left).notLike(any(select)) : ((Field) left).like(any(select));
                        return parseEscapeClauseIf(ctx, result);
                    }
                    else {
                        List<Field<?>> fields = null;
                        if (parseIf(ctx, ')'))
                            fields = Collections.<Field<?>> emptyList();
                        else {
                            fields = new ArrayList<>();
                            do {
                                fields.add(toField(ctx, parseConcat(ctx, null)));
                            }
                            while (parseIf(ctx, ','));
                            parse(ctx, ')');
                        }
                        Field<String>[] fieldArray = fields.toArray(new Field[0]);
                        LikeEscapeStep result = (not ^ notOp) ? ((Field<String>) left).notLike(any(fieldArray)) : ((Field<String>) left).like(any(fieldArray));
                        return parseEscapeClauseIf(ctx, result);
                    }
                }
                else if (parseKeywordIf(ctx, "ALL")) {
                    parse(ctx, '(');
                    if (peekKeyword(ctx, "SELECT") || peekKeyword(ctx, "SEL") || peekKeyword(ctx, "WITH")) {
                        Select<?> select = parseWithOrSelect(ctx);
                        parse(ctx, ')');
                        LikeEscapeStep result = (not ^ notOp) ? ((Field) left).notLike(all(select)) : ((Field) left).like(all(select));
                        return parseEscapeClauseIf(ctx, result);
                    }
                    else {
                        List<Field<?>> fields = null;
                        if (parseIf(ctx, ')'))
                            fields = Collections.<Field<?>> emptyList();
                        else {
                            fields = new ArrayList<>();
                            do {
                                fields.add(toField(ctx, parseConcat(ctx, null)));
                            }
                            while (parseIf(ctx, ','));
                            parse(ctx, ')');
                        }
                        Field<String>[] fieldArray = fields.toArray(new Field[0]);
                        LikeEscapeStep result = (not ^ notOp) ? ((Field<String>) left).notLike(all(fieldArray)) : ((Field<String>) left).like(all(fieldArray));
                        return parseEscapeClauseIf(ctx, result);
                    }
                }
                else {
                    Field right = toField(ctx, parseConcat(ctx, null));
                    LikeEscapeStep like = (not ^ notOp) ? ((Field) left).notLike(right) : ((Field) left).like(right);
                    return parseEscapeClauseIf(ctx, like);
                }
            }
            else if (left instanceof Field && (parseKeywordIf(ctx, "ILIKE") || parseOperatorIf(ctx, "~~*") || (notOp = parseOperatorIf(ctx, "!~~*")))) {
                Field right = toField(ctx, parseConcat(ctx, null));
                LikeEscapeStep like = (not ^ notOp) ? ((Field) left).notLikeIgnoreCase(right) : ((Field) left).likeIgnoreCase(right);
                return parseEscapeClauseIf(ctx, like);
            }
            else if (left instanceof Field && (parseKeywordIf(ctx, "REGEXP")
                                            || parseKeywordIf(ctx, "RLIKE")
                                            || parseKeywordIf(ctx, "LIKE_REGEX")
                                            || parseOperatorIf(ctx, "~")
                                            || (notOp = parseOperatorIf(ctx, "!~")))) {
                Field right = toField(ctx, parseConcat(ctx, null));
                return (not ^ notOp)
                        ? ((Field) left).notLikeRegex(right)
                        : ((Field) left).likeRegex(right);
            }
            else if (left instanceof Field && parseKeywordIf(ctx, "SIMILAR TO")) {
                Field right = toField(ctx, parseConcat(ctx, null));
                LikeEscapeStep like = not ? ((Field) left).notSimilarTo(right) : ((Field) left).similarTo(right);
                return parseEscapeClauseIf(ctx, like);
            }
            else if (left instanceof Row && ((Row) left).size() == 2 && parseKeywordIf(ctx, "OVERLAPS")) {
                Row leftRow = (Row) left;
                Row rightRow = parseRow(ctx, 2);

                Row2 leftRow2 = row(leftRow.field(0), leftRow.field(1));
                Row2 rightRow2 = row(rightRow.field(0), rightRow.field(1));

                return leftRow2.overlaps(rightRow2);
            }
            else
                return left;
        }
    }

    private static final QueryPart parseEscapeClauseIf(ParserContext ctx, LikeEscapeStep like) {
        return parseKeywordIf(ctx, "ESCAPE") ? like.escape(parseCharacterLiteral(ctx)) : like;
    }

    private static final List<Table<?>> parseTables(ParserContext ctx) {
        List<Table<?>> result = new ArrayList<>();

        do {
            result.add(parseTable(ctx));
        }
        while (parseIf(ctx, ','));

        return result;
    }

    private static final Table<?> parseTable(ParserContext ctx) {
        Table<?> result = parseLateral(ctx);

        for (;;) {
            Table<?> joined = parseJoinedTableIf(ctx, result);
            if (joined == null)
                return result;
            else
                result = joined;
        }
    }

    private static final Table<?> parseLateral(ParserContext ctx) {
        if (parseKeywordIf(ctx, "LATERAL"))
            return lateral(parseTableFactor(ctx));
        else
            return parseTableFactor(ctx);
    }

    private static final <R extends Record> Table<R> t(TableLike<R> table) {
        return t(table, false);
    }

    private static final <R extends Record> Table<R> t(TableLike<R> table, boolean dummyAlias) {
        return
            table instanceof Table
          ? (Table<R>) table
          : dummyAlias
          ? table.asTable("x")
          : table.asTable();
    }

    private static final Table<?> parseTableFactor(ParserContext ctx) {

        // [#7982] Postpone turning Select into a Table in case there is an alias
        TableLike<?> result = null;

        // TODO [#5306] Support FINAL TABLE (<data change statement>)
        // TOOD ONLY ( table primary )
        if (parseFunctionNameIf(ctx, "UNNEST")) {
            parse(ctx, '(');
            Field<?> f = parseField(ctx, Type.A);

            // Work around a missing feature in unnest()
            if (!f.getType().isArray())
                f = f.coerce(f.getDataType().getArrayDataType());

            result = unnest(f);
            parse(ctx, ')');
        }
        else if (parseFunctionNameIf(ctx, "GENERATE_SERIES")) {
            parse(ctx, '(');
            Field from = toField(ctx, parseConcat(ctx, Type.N));
            parse(ctx, ',');
            Field to = toField(ctx, parseConcat(ctx, Type.N));

            Field step = parseIf(ctx, ',')
                ? toField(ctx, parseConcat(ctx, Type.N))
                : null;

            parse(ctx, ')');

            result = step == null
                ? generateSeries(from, to)
                : generateSeries(from, to, step);
        }
        else if (parseIf(ctx, '(')) {

            // A table factor parenthesis can mark the beginning of any of:
            // - A derived table:                     E.g. (select 1)
            // - A derived table with nested set ops: E.g. ((select 1) union (select 2))
            // - A values derived table:              E.g. (values (1))
            // - A joined table:                      E.g. ((a join b on p) right join c on q)
            // - A combination of the above:          E.g. ((a join (select 1) on p) right join (((select 1)) union (select 2)) on q)
            if (peekKeyword(ctx, "SELECT") || peekKeyword(ctx, "SEL")) {
                SelectQueryImpl<Record> select = parseSelect(ctx);
                parse(ctx, ')');
                result = parseQueryExpressionBody(ctx, null, null, select);
            }
            else if (peekKeyword(ctx, "VALUES")) {
                result = parseTableValueConstructor(ctx);
                parse(ctx, ')');
            }
            else {
                result = parseJoinedTable(ctx);
                parse(ctx, ')');
            }
        }
        else {
            result = parseTableName(ctx);

            // TODO Sample clause
        }

        if (parseKeywordIf(ctx, "VERSIONS") && ctx.requireProEdition()) {





























        }
        else if (peekKeyword(ctx, "FOR")
            && !peekKeyword(ctx, "FOR JSON")
            && !peekKeyword(ctx, "FOR KEY SHARE")
            && !peekKeyword(ctx, "FOR NO KEY UPDATE")
            && !peekKeyword(ctx, "FOR SHARE")
            && !peekKeyword(ctx, "FOR UPDATE")
            && !peekKeyword(ctx, "FOR XML")
            && parseKeyword(ctx, "FOR") && ctx.requireProEdition()) {
































        }
        else if (parseKeywordIf(ctx, "AS OF") && ctx.requireProEdition()) {










        }

        if (parseKeywordIf(ctx, "PIVOT") && ctx.requireProEdition()) {















































        }

        // TODO UNPIVOT

        Name alias = null;
        List<Name> columnAliases = null;

        if (parseKeywordIf(ctx, "AS"))
            alias = parseIdentifier(ctx);
        else if (!peekKeyword(ctx, KEYWORDS_IN_FROM))
            alias = parseIdentifierIf(ctx);

        if (alias != null) {
            if (parseIf(ctx, '(')) {
                columnAliases = parseIdentifiers(ctx);
                parse(ctx, ')');
            }

            if (columnAliases != null)
                result = t(result, true).as(alias, columnAliases.toArray(EMPTY_NAME));
            else
                result = t(result, true).as(alias);
        }

        if (parseKeywordIf(ctx, "WITH") && ctx.requireProEdition()) {







        }

        return t(result);
    }

































































    private static final Table<?> parseTableValueConstructor(ParserContext ctx) {
        parseKeyword(ctx, "VALUES");

        List<Row> rows = new ArrayList<>();
        do {
            rows.add(parseTuple(ctx));
        }
        while (parseIf(ctx, ','));
        return values0(rows.toArray(EMPTY_ROW));
    }

    private static final Row parseTuple(ParserContext ctx) {
        return parseTuple(ctx, null, false);
    }

    private static final Row parseTuple(ParserContext ctx, Integer degree) {
        return parseTuple(ctx, degree, false);
    }

    private static final Row parseTupleIf(ParserContext ctx, Integer degree) {
        return parseTupleIf(ctx, degree, false);
    }

    private static final Row parseTuple(ParserContext ctx, Integer degree, boolean allowDoubleParens) {
        parse(ctx, '(');
        List<? extends FieldOrRow> fieldsOrRows;

        if (allowDoubleParens)
            fieldsOrRows = parseFieldsOrRows(ctx);
        else
            fieldsOrRows = parseFields(ctx);

        Row row;

        if (fieldsOrRows.size() == 0)
            row = row();
        else if (fieldsOrRows.get(0) instanceof Field)
            row = row(fieldsOrRows);
        else if (fieldsOrRows.size() == 1)
            row = (Row) fieldsOrRows.get(0);
        else
            throw ctx.exception("Unsupported row size");

        if (degree != null && row.size() != degree)
            throw ctx.exception("Expected row of degree: " + degree + ". Got: " + row.size());

        parse(ctx, ')');
        return row;
    }

    private static final Row parseTupleIf(ParserContext ctx, Integer degree, boolean allowDoubleParens) {
        if (peek(ctx, '('))
            return parseTuple(ctx, degree, allowDoubleParens);

        return null;
    }

    private static final Table<?> parseJoinedTable(ParserContext ctx) {
        Table<?> result = parseLateral(ctx);

        for (;;) {
            Table<?> joined = parseJoinedTableIf(ctx, result);

            if (joined == null)
                return result;
            else
                result = joined;
        }
    }

    private static final Table<?> parseJoinedTableIf(ParserContext ctx, Table<?> left) {
        JoinType joinType = parseJoinTypeIf(ctx);

        if (joinType == null)
            return null;

        Table<?> right = joinType.qualified() ? parseTable(ctx) : parseLateral(ctx);

        TableOptionalOnStep<?> s0;
        TablePartitionByStep<?> s1;
        TableOnStep<?> s2;
        s2 = s1 = (TablePartitionByStep<?>) (s0 = left.join(right, joinType));

        switch (joinType) {
            case LEFT_OUTER_JOIN:
            case FULL_OUTER_JOIN:
            case RIGHT_OUTER_JOIN:
                if (parseKeywordIf(ctx, "PARTITION BY")) {
                    ctx.requireProEdition();






                }

                // No break

            case JOIN:
            case STRAIGHT_JOIN:
            case LEFT_SEMI_JOIN:
            case LEFT_ANTI_JOIN:
                if (parseKeywordIf(ctx, "ON"))
                    return s2.on(parseCondition(ctx));
                else if (parseKeywordIf(ctx, "USING"))
                    return parseJoinUsing(ctx, s2);

                // [#9476] MySQL treats INNER JOIN and CROSS JOIN as the same
                else if (joinType == JOIN)
                    return s0;
                else
                    throw ctx.expected("ON", "USING");

            case CROSS_JOIN:

                // [#9476] MySQL treats INNER JOIN and CROSS JOIN as the same
                if (parseKeywordIf(ctx, "ON"))
                    return left.join(right).on(parseCondition(ctx));
                else if (parseKeywordIf(ctx, "USING"))
                    return parseJoinUsing(ctx, left.join(right));

                // No break

            default:
                return s0;
        }
    }

    private static final Table<?> parseJoinUsing(ParserContext ctx, TableOnStep<?> join) {
        parse(ctx, '(');
        Table result = join.using(Tools.fieldsByName(parseIdentifiers(ctx).toArray(EMPTY_NAME)));
        parse(ctx, ')');

        return result;
    }

    private static final List<SelectFieldOrAsterisk> parseSelectList(ParserContext ctx) {
        List<SelectFieldOrAsterisk> result = new ArrayList<>();

        do {
            if (peekKeyword(ctx, KEYWORDS_IN_SELECT))
                throw ctx.exception("Select keywords must be quoted");

            QualifiedAsterisk qa;
            if (parseIf(ctx, '*')) {
                if (parseKeywordIf(ctx, "EXCEPT")) {
                    parse(ctx, '(');
                    result.add(DSL.asterisk().except(parseFieldNames(ctx).toArray(EMPTY_FIELD)));
                    parse(ctx, ')');
                }
                else
                    result.add(DSL.asterisk());
            }
            else if ((qa = parseQualifiedAsteriskIf(ctx)) != null) {
                if (parseKeywordIf(ctx, "EXCEPT")) {
                    parse(ctx, '(');
                    result.add(qa.except(parseFieldNames(ctx).toArray(EMPTY_FIELD)));
                    parse(ctx, ')');
                }
                else
                    result.add(qa);
            }
            else {
                Name alias = null;
                Field<?> field = null;










                if (field == null) {
                    field = parseField(ctx);

                    if (parseKeywordIf(ctx, "AS"))
                        alias = parseIdentifier(ctx, true);
                    else if (!peekKeyword(ctx, KEYWORDS_IN_SELECT))
                        alias = parseIdentifierIf(ctx, true);
                }

                result.add(alias == null ? field : field.as(alias));
            }
        }
        while (parseIf(ctx, ','));

        return result;
    }

    private static final List<SortField<?>> parseSortSpecification(ParserContext ctx) {
        List<SortField<?>> result = new ArrayList<>();

        do {
            result.add(parseSortField(ctx));
        }
        while (parseIf(ctx, ','));
        return result;
    }

    private static final SortField<?> parseSortField(ParserContext ctx) {
        Field<?> field = parseField(ctx);
        SortField<?> sort;

        if (parseKeywordIf(ctx, "DESC"))
            sort = field.desc();
        else if (parseKeywordIf(ctx, "ASC"))
            sort = field.asc();
        else
            sort = field.sortDefault();

        if (parseKeywordIf(ctx, "NULLS FIRST"))
            sort = sort.nullsFirst();
        else if (parseKeywordIf(ctx, "NULLS LAST"))
            sort = sort.nullsLast();

        return sort;
    }

    private static final List<Field<?>> parseFieldsOrEmptyParenthesised(ParserContext ctx) {
        parse(ctx, '(');

        if (parseIf(ctx, ')')) {
            return Collections.emptyList();
        }
        else {
            List<Field<?>> result = parseFields(ctx);
            parse(ctx, ')');
            return result;
        }
    }

    private static final List<Field<?>> parseFields(ParserContext ctx) {
        List<Field<?>> result = new ArrayList<>();
        do {
            result.add(parseField(ctx));
        }
        while (parseIf(ctx, ','));
        return result;
    }

    private static final List<FieldOrRow> parseFieldsOrRows(ParserContext ctx) {
        List<FieldOrRow> result = new ArrayList<>();
        do {
            result.add(parseFieldOrRow(ctx));
        }
        while (parseIf(ctx, ','));
        return result;
    }

    private static final Field<?> parseField(ParserContext ctx) {
        return parseField(ctx, null);
    }

    private static final FieldOrRow parseFieldOrRow(ParserContext ctx) {
        return parseFieldOrRow(ctx, null);
    }

    private static final Row parseRow(ParserContext ctx) {
        return parseRow(ctx, null);
    }

    private static final Row parseRowIf(ParserContext ctx) {
        return parseRowIf(ctx, null);
    }

    private static final List<Row> parseRows(ParserContext ctx, Integer degree) {
        List<Row> result = new ArrayList<>();

        do {
            result.add(parseRow(ctx, degree));
        }
        while (parseIf(ctx, ','));

        return result;
    }

    private static final Row parseRow(ParserContext ctx, Integer degree) {
        parseFunctionNameIf(ctx, "ROW");
        return parseTuple(ctx, degree);
    }

    private static final Row parseRowIf(ParserContext ctx, Integer degree) {
        parseFunctionNameIf(ctx, "ROW");
        return parseTupleIf(ctx, degree);
    }

    private static final Row parseRow(ParserContext ctx, Integer degree, boolean allowDoubleParens) {
        parseFunctionNameIf(ctx, "ROW");
        return parseTuple(ctx, degree, allowDoubleParens);
    }

    static enum Type {
        A("array"),
        D("date"),
        S("string"),
        N("numeric"),
        B("boolean"),
        X("binary"),
        J("json");

        private final String name;

        private Type(String name) {
            this.name = name;
        }

        boolean is(Type type) {
            return type == null || type == this;
        }

        String getName() {
            return name;
        }
    }

    private static final FieldOrRow parseFieldOrRow(ParserContext ctx, Type type) {
        if (B.is(type))
            return toFieldOrRow(ctx, parseOr(ctx));
        else
            return parseConcat(ctx, type);
    }

    private static final Field<?> parseField(ParserContext ctx, Type type) {
        if (B.is(type))
            return toField(ctx, parseOr(ctx));
        else
            return toField(ctx, parseConcat(ctx, type));
    }

    private static final String parseHints(ParserContext ctx) {
        StringBuilder sb = new StringBuilder();

        do {
            int position = ctx.position();
            if (parseIf(ctx, '/', false)) {
                parse(ctx, '*', false);

                int i = ctx.position();

                loop:
                while (i < ctx.sql.length) {
                    switch (ctx.sql[i]) {
                        case '*':
                            if (i + 1 < ctx.sql.length && ctx.sql[i + 1] == '/')
                                break loop;
                    }

                    i++;
                }

                ctx.position(i + 2);

                if (sb.length() > 0)
                    sb.append(' ');

                sb.append(ctx.substring(position, ctx.position()));
            }
        }
        while (parseWhitespaceIf(ctx));

        ctx.ignoreHints(true);
        return sb.length() > 0 ? sb.toString() : null;
    }

    private static final Condition toCondition(ParserContext ctx, QueryPart part) {
        if (part == null)
            return null;
        else if (part instanceof Condition)
            return (Condition) part;
        else if (part instanceof Field)
            if (((Field) part).getDataType().getType() == Boolean.class)
                return condition((Field) part);

            // [#7266] Support parsing column references as predicates
            else if (part instanceof QualifiedField)
                return condition((Field) part);
            else
                throw ctx.expected("Boolean field");
        else
            throw ctx.expected("Condition");
    }

    private static final FieldOrRow toFieldOrRow(ParserContext ctx, QueryPart part) {
        if (part == null)
            return null;
        else if (part instanceof Field)
            return (Field) part;
        else if (part instanceof Condition)
            return field((Condition) part);
        else if (part instanceof Row)
            return (Row) part;
        else
            throw ctx.expected("Field or row");
    }

    private static final Field<?> toField(ParserContext ctx, QueryPart part) {
        if (part == null)
            return null;
        else if (part instanceof Field)
            return (Field) part;
        else if (part instanceof Condition)
            return field((Condition) part);
        else
            throw ctx.expected("Field");
    }

    private static final FieldOrRow parseConcat(ParserContext ctx, Type type) {
        FieldOrRow r = parseCollated(ctx, type);

        if (S.is(type) && r instanceof Field)
            while (parseIf(ctx, "||"))
                r = concat((Field) r, toField(ctx, parseCollated(ctx, type)));

        return r;
    }

    private static final FieldOrRow parseCollated(ParserContext ctx, Type type) {
        FieldOrRow r = parseNumericOp(ctx, type);

        if (S.is(type) && r instanceof Field)
            if (parseKeywordIf(ctx, "COLLATE"))
                r = ((Field) r).collate(parseCollation(ctx));

        return r;
    }

    private static final Field<?> parseFieldNumericOpParenthesised(ParserContext ctx) {
        parse(ctx, '(');
        Field<?> r = toField(ctx, parseNumericOp(ctx, N));
        parse(ctx, ')');
        return r;
    }

    // Any numeric operator of low precedence
    // See https://www.postgresql.org/docs/current/sql-syntax-lexical.html#SQL-PRECEDENCE
    private static final FieldOrRow parseNumericOp(ParserContext ctx, Type type) {
        FieldOrRow r = parseSum(ctx, type);

        if (N.is(type) && r instanceof Field)
            for (;;)
                if (parseIf(ctx, "<<"))
                    r = ((Field) r).shl((Field) parseSum(ctx, type));
                else if (parseIf(ctx, ">>"))
                    r = ((Field) r).shr((Field) parseSum(ctx, type));
                else
                    break;

        return r;
    }

    private static final FieldOrRow parseSum(ParserContext ctx, Type type) {
        FieldOrRow r = parseFactor(ctx, type);

        if (N.is(type) && r instanceof Field)
            for (;;)
                if (parseIf(ctx, '+'))
                    r = parseSumRightOperand(ctx, type, r, true);
                else if (parseIf(ctx, '-'))
                    r = parseSumRightOperand(ctx, type, r, false);
                else
                    break;

        return r;
    }

    private static final Field parseSumRightOperand(ParserContext ctx, Type type, FieldOrRow r, boolean add) {
        Field f = (Field) parseFactor(ctx, type);
        DatePart part;

        if ((parseKeywordIf(ctx, "YEAR") || parseKeywordIf(ctx, "YEARS")) && ctx.requireProEdition())
            part = DatePart.YEAR;
        else if ((parseKeywordIf(ctx, "MONTH") || parseKeywordIf(ctx, "MONTHS")) && ctx.requireProEdition())
            part = DatePart.MONTH;
        else if ((parseKeywordIf(ctx, "DAY") || parseKeywordIf(ctx, "DAYS")) && ctx.requireProEdition())
            part = DatePart.DAY;
        else if ((parseKeywordIf(ctx, "HOUR") || parseKeywordIf(ctx, "HOURS")) && ctx.requireProEdition())
            part = DatePart.HOUR;
        else if ((parseKeywordIf(ctx, "MINUTE") || parseKeywordIf(ctx, "MINUTES")) && ctx.requireProEdition())
            part = DatePart.MINUTE;
        else if ((parseKeywordIf(ctx, "SECOND") || parseKeywordIf(ctx, "SECONDS")) && ctx.requireProEdition())
            part = DatePart.SECOND;
        else
            part = null;









            if (add)
                return ((Field) r).add(f);
            else
                return ((Field) r).sub(f);
    }

    private static final FieldOrRow parseFactor(ParserContext ctx, Type type) {
        FieldOrRow r = parseExp(ctx, type);

        if (N.is(type) && r instanceof Field)
            for (;;)
                if (!peek(ctx, "*=") && parseIf(ctx, '*'))
                    r = ((Field) r).mul((Field) parseExp(ctx, type));
                else if (parseIf(ctx, '/'))
                    r = ((Field) r).div((Field) parseExp(ctx, type));
                else if (parseIf(ctx, '%'))
                    r = ((Field) r).mod((Field) parseExp(ctx, type));






                else
                    break;

        return r;
    }

    private static final FieldOrRow parseExp(ParserContext ctx, Type type) {
        FieldOrRow r = parseUnaryOps(ctx, type);

        if (N.is(type) && r instanceof Field)
            for (;;)
                if (!peek(ctx, "^=") && parseIf(ctx, '^'))
                    r = ((Field) r).pow(toField(ctx, parseUnaryOps(ctx, type)));
                else
                    break;

        return r;
    }

    private static final FieldOrRow parseUnaryOps(ParserContext ctx, Type type) {
        if (parseKeywordIf(ctx, "CONNECT_BY_ROOT"))
            return connectByRoot(toField(ctx, parseTerm(ctx, type)));

        FieldOrRow r;
        Sign sign = parseSign(ctx);

        if (sign == Sign.NONE)
            r = parseTerm(ctx, type);
        else if (sign == Sign.PLUS)
            r = toField(ctx, parseTerm(ctx, type));
        else if ((r = parseFieldUnsignedNumericLiteralIf(ctx, Sign.MINUS)) == null)
            r = toField(ctx, parseTerm(ctx, type)).neg();

        if (parseIf(ctx, "(+)") && ctx.requireProEdition())



            ;

        if (parseIf(ctx, '('))
            throw ctx.exception("Unknown function");

        while (parseIf(ctx, "::"))
            r = cast(toField(ctx, r), parseDataType(ctx));

        return r;
    }

    private static final Sign parseSign(ParserContext ctx) {
        Sign sign = Sign.NONE;

        for (;;)
            if (parseIf(ctx, '+'))
                sign = sign == Sign.NONE ? Sign.PLUS  : sign;
            else if (parseIf(ctx, '-'))
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

    private static final FieldOrRow parseTerm(ParserContext ctx, Type type) {
        FieldOrRow field;
        Object value;

        switch (ctx.character()) {
            case ':':
            case '?':
                return parseBindVariable(ctx);









            case '\'':
                return inline(parseStringLiteral(ctx));

            case '$':
                if ((value = parseDollarQuotedStringLiteralIf(ctx)) != null)
                    return inline((String) value);

                break;

            case 'a':
            case 'A':
                if (N.is(type))
                    if (parseFunctionNameIf(ctx, "ABS"))
                        return abs((Field) parseFieldNumericOpParenthesised(ctx));
                    else if ((field = parseFieldAsciiIf(ctx)) != null)
                        return field;
                    else if (parseFunctionNameIf(ctx, "ACOS"))
                        return acos((Field) parseFieldNumericOpParenthesised(ctx));
                    else if (parseFunctionNameIf(ctx, "ASIN"))
                        return asin((Field) parseFieldNumericOpParenthesised(ctx));
                    else if (parseFunctionNameIf(ctx, "ATAN"))
                        return atan((Field) parseFieldNumericOpParenthesised(ctx));
                    else if ((field = parseFieldAtan2If(ctx)) != null)
                        return field;

                if (A.is(type))
                    if ((field = parseArrayValueConstructorIf(ctx)) != null)
                        return field;

                break;

            case 'b':
            case 'B':
                if (N.is(type))
                    if ((field = parseFieldBitLengthIf(ctx)) != null)
                        return field;
                    else if (parseFunctionNameIf(ctx, "BIT_COUNT"))
                        return bitCount((Field) parseFieldNumericOpParenthesised(ctx));
                    else if ((field = parseFieldBitwiseFunctionIf(ctx)) != null)
                        return field;

                break;

            case 'c':
            case 'C':
                if (S.is(type))
                    if ((field = parseFieldConcatIf(ctx)) != null)
                        return field;
                    else if ((parseKeywordIf(ctx, "CURRENT_SCHEMA") || parseKeywordIf(ctx, "CURRENT SCHEMA")) && (parseIf(ctx, '(') && parse(ctx, ')') || true))
                        return currentSchema();
                    else if ((parseKeywordIf(ctx, "CURRENT_USER") || parseKeywordIf(ctx, "CURRENT USER")) && (parseIf(ctx, '(') && parse(ctx, ')') || true))
                        return currentUser();

                if (N.is(type))
                    if ((field = parseFieldCharIndexIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldCharLengthIf(ctx)) != null)
                        return field;
                    else if (parseFunctionNameIf(ctx, "CEILING") || parseFunctionNameIf(ctx, "CEIL"))
                        return ceil((Field) parseFieldNumericOpParenthesised(ctx));
                    else if (parseFunctionNameIf(ctx, "COSH"))
                        return cosh((Field) parseFieldNumericOpParenthesised(ctx));
                    else if (parseFunctionNameIf(ctx, "COS"))
                        return cos((Field) parseFieldNumericOpParenthesised(ctx));
                    else if (parseFunctionNameIf(ctx, "COTH"))
                        return coth((Field) parseFieldNumericOpParenthesised(ctx));
                    else if (parseFunctionNameIf(ctx, "COT"))
                        return cot((Field) parseFieldNumericOpParenthesised(ctx));
                    else if ((field = parseNextvalCurrvalIf(ctx, SequenceMethod.CURRVAL)) != null)
                        return field;
                    else if ((field = parseFieldCenturyIf(ctx)) != null)
                        return field;

                if (D.is(type))
                    if ((parseKeywordIf(ctx, "CURRENT_DATE") || parseKeywordIf(ctx, "CURRENT DATE")) && (parseIf(ctx, '(') && parse(ctx, ')') || true))
                        return currentDate();
                    else if (parseKeywordIf(ctx, "CURRENT_TIMESTAMP") || parseKeywordIf(ctx, "CURRENT TIMESTAMP")) {
                        Field<Integer> precision = null;
                        if (parseIf(ctx, '('))
                            if (!parseIf(ctx, ')')) {
                                precision = (Field<Integer>) parseField(ctx, N);
                                parse(ctx, ')');
                            }
                        return precision != null ? currentTimestamp(precision) : currentTimestamp();
                    }
                    else if ((parseKeywordIf(ctx, "CURRENT_TIME") || parseKeywordIf(ctx, "CURRENT TIME")) && (parseIf(ctx, '(') && parse(ctx, ')') || true))
                        return currentTime();
                    else if (parseFunctionNameIf(ctx, "CURDATE") && parse(ctx, '(') && parse(ctx, ')'))
                        return currentDate();
                    else if (parseFunctionNameIf(ctx, "CURTIME") && parse(ctx, '(') && parse(ctx, ')'))
                        return currentTime();

                if ((field = parseFieldCaseIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldCastIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldCoalesceIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldCumeDistIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldConvertIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldChooseIf(ctx)) != null)
                    return field;
                else if (parseKeywordIf(ctx, "CONNECT_BY_ISCYCLE"))
                    return connectByIsCycle();
                else if (parseKeywordIf(ctx, "CONNECT_BY_ISLEAF"))
                    return connectByIsLeaf();

                break;

            case 'd':
            case 'D':
                if (D.is(type))
                    if ((field = parseFieldDateLiteralIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldDateTruncIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldDateAddIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldDateDiffIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldDatePartIf(ctx)) != null)
                        return field;

                if (N.is(type))
                    if ((field = parseFieldDenseRankIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldDecadeIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldDayIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldDayOfWeekIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldIsoDayOfWeekIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldDayOfYearIf(ctx)) != null)
                        return field;
                    else if (parseFunctionNameIf(ctx, "DEGREES")
                          || parseFunctionNameIf(ctx, "DEGREE")
                          || parseFunctionNameIf(ctx, "DEG"))
                        return deg((Field) parseFieldNumericOpParenthesised(ctx));

                if ((field = parseFieldDecodeIf(ctx)) != null)
                    return field;

                break;

            case 'e':
            case 'E':

                // [#6704] PostgreSQL E'...' escaped string literals
                if (S.is(type))
                    if (ctx.characterNext() == '\'')
                        return inline(parseStringLiteral(ctx));

                if (N.is(type))
                    if ((field = parseFieldExtractIf(ctx)) != null)
                        return field;
                    else if (parseFunctionNameIf(ctx, "EXP"))
                        return exp((Field) parseFieldNumericOpParenthesised(ctx));

                if (D.is(type))
                    if ((field = parseFieldEpochIf(ctx)) != null)
                        return field;

                break;

            case 'f':
            case 'F':
                if (N.is(type))
                    if (parseFunctionNameIf(ctx, "FLOOR"))
                        return floor((Field) parseFieldNumericOpParenthesised(ctx));

                if ((field = parseFieldFirstValueIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldFieldIf(ctx)) != null)
                    return field;

                break;

            case 'g':
            case 'G':
                if (D.is(type))
                    if (parseKeywordIf(ctx, "GETDATE") && parse(ctx, '(') && parse(ctx, ')'))
                        return currentTimestamp();

                if ((field = parseFieldGreatestIf(ctx)) != null)
                    return field;
                else if (N.is(type) && (field = parseFieldGroupIdIf(ctx)) != null)
                    return field;
                else if (N.is(type) && (field = parseFieldGroupingIdIf(ctx)) != null)
                    return field;
                else if (N.is(type) && (field = parseFieldGroupingIf(ctx)) != null)
                    return field;
                else
                    break;

            case 'h':
            case 'H':
                if (N.is(type))
                    if ((field = parseFieldHourIf(ctx)) != null)
                        return field;

                break;

            case 'i':
            case 'I':
                if (D.is(type))
                    if ((field = parseFieldIntervalLiteralIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldIsoDayOfWeekIf(ctx)) != null)
                        return field;

                if (N.is(type))
                    if ((field = parseFieldInstrIf(ctx)) != null)
                        return field;

                if ((field = parseFieldIfnullIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldIsnullIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldIifIf(ctx)) != null)
                    return field;
                else
                    break;

            case 'j':
            case 'J':
                if (J.is(type))
                    if ((field = parseFieldJSONArrayConstructorIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldJSONArrayAggIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldJSONObjectConstructorIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldJSONObjectAggIf(ctx)) != null)
                        return field;

                break;

            case 'l':
            case 'L':
                if (S.is(type))
                    if ((field = parseFieldLowerIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldLpadIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldLtrimIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldLeftIf(ctx)) != null)
                        return field;

                if (N.is(type))
                    if ((field = parseFieldLengthIf(ctx)) != null)
                        return field;
                    else if (parseFunctionNameIf(ctx, "LN"))
                        return ln((Field) parseFieldNumericOpParenthesised(ctx));
                    else if ((field = parseFieldLogIf(ctx)) != null)
                        return field;
                    else if (parseKeywordIf(ctx, "LEVEL"))
                        return level();
                    else if ((field = parseFieldShlIf(ctx)) != null)
                        return field;

                if ((field = parseFieldLeastIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldLeadLagIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldLastValueIf(ctx)) != null)
                    return field;

                break;

            case 'm':
            case 'M':
                if (N.is(type))
                    if ((field = parseFieldModIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldMonthIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldMinuteIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldMillenniumIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldMillisecondIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldMicrosecondIf(ctx)) != null)
                        return field;

                if (S.is(type))
                    if ((field = parseFieldMidIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldMd5If(ctx)) != null)
                        return field;

                break;

            case 'n':
            case 'N':
                if ((field = parseFieldNvl2If(ctx)) != null)
                    return field;
                else if ((field = parseFieldNvlIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldNullifIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldNtileIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldNthValueIf(ctx)) != null)
                    return field;
                else if ((field = parseNextValueIf(ctx)) != null)
                    return field;
                else if ((field = parseNextvalCurrvalIf(ctx, SequenceMethod.NEXTVAL)) != null)
                    return field;
                else if (parseFunctionNameIf(ctx, "NOW") && parse(ctx, '(')) {
                    if (parseIf(ctx, ')'))
                        return now();
                    Field<Integer> precision = (Field<Integer>) parseField(ctx, N);
                    parse(ctx, ')');
                    return now(precision);
                }

                break;

            case 'o':
            case 'O':
                if (S.is(type))
                    if ((field = parseFieldReplaceIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldOverlayIf(ctx)) != null)
                        return field;

                if (N.is(type))
                    if ((field = parseFieldOctetLengthIf(ctx)) != null)
                        return field;

                break;

            case 'p':
            case 'P':
                if (N.is(type))
                    if ((field = parseFieldPositionIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldPercentRankIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldPowerIf(ctx)) != null)
                        return field;
                    else if (parseFunctionNameIf(ctx, "PI") && parse(ctx, '(') && parse(ctx, ')'))
                        return pi();

                if (parseKeywordIf(ctx, "PRIOR"))
                    return prior(toField(ctx, parseConcat(ctx, type)));

                break;

            case 'q':
            case 'Q':
                if (S.is(type))
                    if (ctx.characterNext() == '\'')
                        return inline(parseStringLiteral(ctx));

                if (D.is(type))
                    if ((field = parseFieldQuarterIf(ctx)) != null)
                        return field;

            case 'r':
            case 'R':
                if (S.is(type))
                    if ((field = parseFieldReplaceIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldRepeatIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldReverseIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldRpadIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldRtrimIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldRightIf(ctx)) != null)
                        return field;

                if (N.is(type))
                    if ((field = parseFieldRowNumberIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldRankIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldRoundIf(ctx)) != null)
                        return field;
                    else if (parseKeywordIf(ctx, "ROWNUM"))
                        return rownum();
                    else if (parseFunctionNameIf(ctx, "RADIANS")
                          || parseFunctionNameIf(ctx, "RADIAN")
                          || parseFunctionNameIf(ctx, "RAD"))
                        return rad((Field) parseFieldNumericOpParenthesised(ctx));
                    else if ((field = parseFieldRandIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldRatioToReportIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldShrIf(ctx)) != null)
                        return field;

                if (parseFunctionNameIf(ctx, "ROW"))
                    return parseTuple(ctx);

                break;

            case 's':
            case 'S':
                if (S.is(type))
                    if ((field = parseFieldSubstringIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldSpaceIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldReplaceIf(ctx)) != null)
                        return field;
                    else if (parseFunctionNameIf(ctx, "SCHEMA") && parseIf(ctx, '(') && parse(ctx, ')'))
                        return currentSchema();

                if (N.is(type))
                    if ((field = parseFieldSecondIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldSignIf(ctx)) != null)
                        return field;
                    else if (parseFunctionNameIf(ctx, "SQRT") || parseFunctionNameIf(ctx, "SQR"))
                        return sqrt((Field) parseFieldNumericOpParenthesised(ctx));
                    else if (parseFunctionNameIf(ctx, "SINH"))
                        return sinh((Field) parseFieldNumericOpParenthesised(ctx));
                    else if (parseFunctionNameIf(ctx, "SIN"))
                        return sin((Field) parseFieldNumericOpParenthesised(ctx));
                    else if ((field = parseFieldShlIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldShrIf(ctx)) != null)
                        return field;

                if ((field = parseFieldSysConnectByPathIf(ctx)) != null)
                    return field;

                break;

            case 't':
            case 'T':
                if (B.is(type))
                    if ((field = parseBooleanValueExpressionIf(ctx)) != null)
                        return field;

                if (S.is(type))
                    if ((field = parseFieldTrimIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldTranslateIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldToCharIf(ctx)) != null)
                        return field;

                if (N.is(type))
                    if (parseFunctionNameIf(ctx, "TANH"))
                        return tanh((Field) parseFieldNumericOpParenthesised(ctx));
                    else if (parseFunctionNameIf(ctx, "TAN"))
                        return tan((Field) parseFieldNumericOpParenthesised(ctx));
                    else if ((field = parseFieldToNumberIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldTimezoneIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldTimezoneHourIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldTimezoneMinuteIf(ctx)) != null)
                        return field;

                if (D.is(type))
                    if ((field = parseFieldTimestampLiteralIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldTimeLiteralIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldToDateIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldToTimestampIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldTimestampDiffIf(ctx)) != null)
                        return field;

                if (N.is(type) || D.is(type))
                    if ((field = parseFieldTruncIf(ctx)) != null)
                        return field;

                break;

            case 'u':
            case 'U':
                if (S.is(type))
                    if ((field = parseFieldUpperIf(ctx)) != null)
                        return field;

                if (D.is(type))
                    if ((field = parseFieldUnixTimestampIf(ctx)) != null)
                        return field;

                break;

            case 'w':
            case 'W':
                if (N.is(type))
                    if ((field = parseFieldWidthBucketIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldWeekIf(ctx)) != null)
                        return field;

                break;

            case 'x':
            case 'X':
                if (X.is(type))
                    if ((value = parseBinaryLiteralIf(ctx)) != null)
                        return inline((byte[]) value);

                break;

            case 'y':
            case 'Y':
                if (N.is(type))
                    if ((field = parseFieldYearIf(ctx)) != null)
                        return field;

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
            case '.':
                if (N.is(type))
                    if ((field = parseFieldUnsignedNumericLiteralIf(ctx, Sign.NONE)) != null)
                        return field;

                break;

            case '{':
                parse(ctx, '{', false);

                switch (ctx.character()) {
                    case 'd':
                    case 'D':
                        parseKeyword(ctx, "D");
                        field = inline(parseDateLiteral(ctx));
                        break;

                    case 'f':
                    case 'F':
                        parseKeyword(ctx, "FN");

                        // TODO: Limit the supported expressions in this context to the ones specified here:
                        // http://download.oracle.com/otn-pub/jcp/jdbc-4_2-mrel2-eval-spec/jdbc4.2-fr-spec.pdf
                        field = parseTerm(ctx, type);
                        break;

                    case 't':
                    case 'T':
                        if (parseKeywordIf(ctx, "TS")) {
                            field = inline(parseTimestampLiteral(ctx));
                        }
                        else {
                            parseKeyword(ctx, "T");
                            field = inline(parseTimeLiteral(ctx));
                        }
                        break;

                    default:
                        throw ctx.exception("Unsupported JDBC escape literal");
                }

                parse(ctx, '}');
                return field;

            case '(':

                // A term parenthesis can mark the beginning of any of:
                // - ROW expression without ROW keyword:        E.g. (1, 2)
                // - Parenthesised field expression:            E.g. (1 + 2)
                // - A correlated subquery:                     E.g. (select 1)
                // - A correlated subquery with nested set ops: E.g. ((select 1) except (select 2))
                // - A combination of the above:                E.g. ((select 1) + 2, ((select 1) except (select 2)) + 2)
                int position = ctx.position();
                try {
                    if (peekKeyword(ctx, "SELECT", false, true, false) ||
                        peekKeyword(ctx, "SEL", false, true, false)) {
                        SelectQueryImpl<Record> select = parseSelect(ctx);
                        if (select.getSelect().size() > 1)
                            throw ctx.exception("Select list must contain at most one column");

                        field = field((Select) select);
                        return field;
                    }
                }
                catch (ParserException e) {

                    // TODO: Find a better solution than backtracking, here, which doesn't complete in O(N)
                    if (e.getMessage().contains("Token ')' expected"))
                        ctx.position(position);
                    else
                        throw e;
                }

                parse(ctx, '(');
                FieldOrRow r = parseFieldOrRow(ctx, type);
                List<Field<?>> list = null;

                if (r instanceof Field) {
                    while (parseIf(ctx, ',')) {
                        if (list == null) {
                            list = new ArrayList<>();
                            list.add((Field) r);
                        }

                        // TODO Allow for nesting ROWs
                        list.add(parseField(ctx, type));
                    }
                }

                parse(ctx, ')');
                return list != null ? row(list) : r;
        }

        if ((field = parseAggregateFunctionIf(ctx)) != null)
            return field;

        else if ((field = parseBooleanValueExpressionIf(ctx)) != null)
            return field;

        else
            return parseFieldNameOrSequenceExpression(ctx);
    }

    private static final Field<?> parseFieldShlIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "SHL") || parseKeywordIf(ctx, "SHIFTLEFT") || parseKeywordIf(ctx, "LSHIFT")) {
            parse(ctx, '(');
            Field<?> x = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ',');
            Field<?> y = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ')');

            return shl((Field) x, (Field) y);
        }

        return null;
    }

    private static final Field<?> parseFieldShrIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "SHR") || parseKeywordIf(ctx, "SHIFTRIGHT") || parseKeywordIf(ctx, "RSHIFT")) {
            parse(ctx, '(');
            Field<?> x = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ',');
            Field<?> y = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ')');

            return shr((Field) x, (Field) y);
        }

        return null;
    }

    private static final Field<?> parseFieldSysConnectByPathIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "SYS_CONNECT_BY_PATH")) {
            parse(ctx, '(');
            Field<?> x = parseField(ctx);
            parse(ctx, ',');
            String y = parseStringLiteral(ctx);
            parse(ctx, ')');
            return sysConnectByPath(x, y);
        }

        return null;
    }

    private static final Field<?> parseFieldBitwiseFunctionIf(ParserContext ctx) {
        int position = ctx.position();

        char c1 = ctx.character(position + 1);
        char c2 = ctx.character(position + 2);

        if (c1 != 'I' && c1 != 'i')
            return null;
        if (c2 != 'T' && c2 != 't' && c2 != 'N' && c2 != 'n')
            return null;

        if (parseKeywordIf(ctx, "BIT_AND") || parseKeywordIf(ctx, "BITAND") || parseKeywordIf(ctx, "BIN_AND")) {
            parse(ctx, '(');
            Field<?> x = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ',');
            Field<?> y = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ')');

            return bitAnd((Field) x, (Field) y);
        }
        else if (parseKeywordIf(ctx, "BIT_NAND") || parseKeywordIf(ctx, "BITNAND") || parseKeywordIf(ctx, "BIN_NAND")) {
            parse(ctx, '(');
            Field<?> x = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ',');
            Field<?> y = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ')');

            return bitNand((Field) x, (Field) y);
        }
        else if (parseKeywordIf(ctx, "BIT_OR") || parseKeywordIf(ctx, "BITOR") || parseKeywordIf(ctx, "BIN_OR")) {
            parse(ctx, '(');
            Field<?> x = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ',');
            Field<?> y = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ')');

            return bitOr((Field) x, (Field) y);
        }
        else if (parseKeywordIf(ctx, "BIT_NOR") || parseKeywordIf(ctx, "BITNOR") || parseKeywordIf(ctx, "BIN_NOR")) {
            parse(ctx, '(');
            Field<?> x = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ',');
            Field<?> y = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ')');

            return bitNor((Field) x, (Field) y);
        }
        else if (parseKeywordIf(ctx, "BIT_XOR") || parseKeywordIf(ctx, "BITXOR") || parseKeywordIf(ctx, "BIN_XOR")) {
            parse(ctx, '(');
            Field<?> x = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ',');
            Field<?> y = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ')');

            return bitXor((Field) x, (Field) y);
        }
        else if (parseKeywordIf(ctx, "BIT_XNOR") || parseKeywordIf(ctx, "BITXNOR") || parseKeywordIf(ctx, "BIN_XNOR")) {
            parse(ctx, '(');
            Field<?> x = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ',');
            Field<?> y = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ')');

            return bitXNor((Field) x, (Field) y);
        }
        else if (parseKeywordIf(ctx, "BIT_NOT") || parseKeywordIf(ctx, "BITNOT") || parseKeywordIf(ctx, "BIN_NOT")) {
            parse(ctx, '(');
            Field<?> x = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ')');

            return bitNot((Field) x);
        }
        else if (parseKeywordIf(ctx, "BIN_SHL")) {
            parse(ctx, '(');
            Field<?> x = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ',');
            Field<?> y = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ')');

            return shl((Field) x, (Field) y);
        }
        else if (parseKeywordIf(ctx, "BIN_SHR")) {
            parse(ctx, '(');
            Field<?> x = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ',');
            Field<?> y = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ')');

            return shr((Field) x, (Field) y);
        }

        return null;
    }

    private static final Field<?> parseNextValueIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "NEXT VALUE FOR"))
            return sequence(parseName(ctx)).nextval();

        return null;
    }

    private static final Field<?> parseNextvalCurrvalIf(ParserContext ctx, SequenceMethod method) {
        if (parseFunctionNameIf(ctx, method.name())) {
            parse(ctx, '(');

            Name name = parseNameIf(ctx);
            Sequence s = name != null
                ? sequence(name)
                : sequence(ctx.dsl.parser().parseName(parseStringLiteral(ctx)));

            parse(ctx, ')');

            if (method == SequenceMethod.NEXTVAL)
                return s.nextval();
            else if (method == SequenceMethod.CURRVAL)
                return s.currval();
            else
                throw ctx.exception("Only NEXTVAL and CURRVAL methods supported");
        }

        return null;
    }

    private static enum SequenceMethod {
        NEXTVAL,
        CURRVAL;
    }

    private static final Field<?> parseFieldJSONArrayConstructorIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "JSON_ARRAY")) {
            parse(ctx, '(');

            List<Field<?>> result = null;
            JSONNullClause nullClause = parseJSONObjectNullClauseIf(ctx);

            if (nullClause == null) {
                result = parseFields(ctx);
                nullClause = parseJSONObjectNullClauseIf(ctx);
            }

            parse(ctx, ')');

            JSONArrayNullStep<JSON> a = result == null ? DSL.jsonArray() : DSL.jsonArray(result);
            return nullClause == NULL_ON_NULL
                 ? a.nullOnNull()
                 : nullClause == ABSENT_ON_NULL
                 ? a.absentOnNull()
                 : a;
        }

        return null;
    }

    private static final Field<?> parseFieldJSONArrayAggIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "JSON_ARRAYAGG")) {
            Field<?> result;
            JSONArrayAggOrderByStep<JSON> s1;
            JSONArrayAggNullStep<JSON> s2;
            JSONNullClause nullClause;

            parse(ctx, '(');
            result = s2 = s1 = DSL.jsonArrayAgg(parseField(ctx));

            if (parseKeywordIf(ctx, "ORDER BY"))
                result = s2 = s1.orderBy(parseSortSpecification(ctx));

            if ((nullClause = parseJSONObjectNullClauseIf(ctx)) != null)
                result = nullClause == ABSENT_ON_NULL ? s2.absentOnNull() : s2.nullOnNull();

            parse(ctx, ')');
            return result;
        }

        return null;
    }

    private static final Field<?> parseFieldJSONObjectConstructorIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "JSON_OBJECT")) {
            parse(ctx, '(');
            if (parseIf(ctx, ')'))
                return DSL.jsonObject();

            List<JSONEntry<?>> result = new ArrayList<>();
            JSONNullClause nullClause = parseJSONObjectNullClauseIf(ctx);

            if (nullClause == null) {
                do {
                    result.add(parseJSONEntry(ctx));
                }
                while (parseIf(ctx, ','));

                nullClause = parseJSONObjectNullClauseIf(ctx);
            }
            parse(ctx, ')');

            JSONObjectNullStep<JSON> o = DSL.jsonObject(result);
            return nullClause == NULL_ON_NULL
                 ? o.nullOnNull()
                 : nullClause == ABSENT_ON_NULL
                 ? o.absentOnNull()
                 : o;
        }

        return null;
    }

    private static final Field<?> parseFieldJSONObjectAggIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "JSON_OBJECTAGG")) {
            Field<?> result;
            JSONObjectAggNullStep<JSON> s1;
            JSONNullClause nullClause;

            parse(ctx, '(');
            result = s1 = DSL.jsonObjectAgg(parseJSONEntry(ctx));

            if ((nullClause = parseJSONObjectNullClauseIf(ctx)) != null)
                result = nullClause == ABSENT_ON_NULL ? s1.absentOnNull() : s1.nullOnNull();

            parse(ctx, ')');
            return result;
        }

        return null;
    }

    private static final JSONNullClause parseJSONObjectNullClauseIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "NULL ON NULL"))
            return NULL_ON_NULL;
        else if (parseKeywordIf(ctx, "ABSENT ON NULL"))
            return ABSENT_ON_NULL;
        else
            return null;
    }

    private static final JSONEntry<?> parseJSONEntry(ParserContext ctx) {
        boolean valueRequired = parseKeywordIf(ctx, "KEY");

        Field<String> key = (Field<String>) parseField(ctx, Type.S);
        if (parseKeywordIf(ctx, "VALUE"))
            ;
        else if (valueRequired)
            throw ctx.expected("VALUE");
        else
            parse(ctx, ',');

        Field<?> value = parseField(ctx);
        return jsonEntry(key, value);
    }

    private static final Field<?> parseArrayValueConstructorIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "ARRAY")) {
            parse(ctx, '[');

            List<Field<?>> fields;
            if (parseIf(ctx, ']')) {
                fields = Collections.<Field<?>>emptyList();
            }
            else {
                fields = parseFields(ctx);
                parse(ctx, ']');
            }

            // Prevent "wrong" javac method bind
            return DSL.array((Collection) fields);
        }

        return null;
    }

    private static final Field<?> parseFieldAtan2If(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "ATN2") || parseFunctionNameIf(ctx, "ATAN2")) {
            parse(ctx, '(');
            Field<?> x = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ',');
            Field<?> y = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ')');

            return atan2((Field) x, (Field) y);
        }

        return null;
    }

    private static final Field<?> parseFieldLogIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "LOG")) {
            parse(ctx, '(');
            switch (ctx.family()) {















                default:
                    Field<?> base = toField(ctx, parseNumericOp(ctx, N));
                    parse(ctx, ',');
                    Field<?> value = toField(ctx, parseNumericOp(ctx, N));
                    parse(ctx, ')');
                    return log((Field) value, (Field) base);
            }
        }

        return null;
    }

    private static final Field<?> parseFieldTruncIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "TRUNC")) {
            parse(ctx, '(');
            Field<?> arg1 = parseField(ctx);
            parse(ctx, ',');

            String part;
            if ((part = parseStringLiteralIf(ctx)) != null) {
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
                    throw ctx.exception("Unsupported date part");

                parse(ctx, ')');
                return DSL.trunc((Field) arg1, p);
            }
            else {
                Field<?> arg2 = toField(ctx, parseNumericOp(ctx, N));
                parse(ctx, ')');
                return DSL.trunc((Field) arg1, (Field) arg2);
            }
        }

        return null;
    }

    private static final Field<?> parseFieldRoundIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "ROUND")) {
            Field arg1 = null;
            Field arg2 = null;

            parse(ctx, '(');
            arg1 = toField(ctx, parseNumericOp(ctx, N));
            if (parseIf(ctx, ','))
                arg2 = toField(ctx, parseNumericOp(ctx, N));

            parse(ctx, ')');
            return arg2 == null ? round(arg1) : round(arg1, arg2);
        }

        return null;
    }

    private static final Field<?> parseFieldPowerIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "POWER") || parseFunctionNameIf(ctx, "POW")) {
            parse(ctx, '(');
            Field arg1 = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ',');
            Field arg2 = toField(ctx, parseNumericOp(ctx, N));
            parse(ctx, ')');
            return DSL.power(arg1, arg2);
        }

        return null;
    }

    private static final Field<?> parseFieldModIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "MOD")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx, N);
            parse(ctx, ',');
            Field<?> f2 = parseField(ctx, N);
            parse(ctx, ')');
            return f1.mod((Field) f2);
        }

        return null;
    }

    private static final Field<?> parseFieldWidthBucketIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "WIDTH_BUCKET")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx, N);
            parse(ctx, ',');
            Field<?> f2 = parseField(ctx, N);
            parse(ctx, ',');
            Field<?> f3 = parseField(ctx, N);
            parse(ctx, ',');
            Field<?> f4 = parseField(ctx, N);
            parse(ctx, ')');
            return DSL.widthBucket((Field) f1, (Field) f2, (Field) f3, (Field) f4);
        }

        return null;
    }

    private static final Field<?> parseFieldLeastIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "LEAST")) {
            parse(ctx, '(');
            List<Field<?>> fields = parseFields(ctx);
            parse(ctx, ')');

            return least(fields.get(0), fields.size() > 1 ? fields.subList(1, fields.size()).toArray(EMPTY_FIELD) : EMPTY_FIELD);
        }

        return null;
    }

    private static final Field<?> parseFieldGreatestIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "GREATEST")) {
            parse(ctx, '(');
            List<Field<?>> fields = parseFields(ctx);
            parse(ctx, ')');

            return greatest(fields.get(0), fields.size() > 1 ? fields.subList(1, fields.size()).toArray(EMPTY_FIELD) : EMPTY_FIELD);
        }

        return null;
    }

    private static final Field<?> parseFieldGroupingIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "GROUPING")) {
            parse(ctx, '(');
            Field<?> field = parseField(ctx);
            parse(ctx, ')');

            return grouping(field);
        }

        return null;
    }

    private static final Field<?> parseFieldGroupIdIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "GROUP_ID")) {
            ctx.requireProEdition();







        }

        return null;
    }

    private static final Field<?> parseFieldGroupingIdIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "GROUPING_ID") && ctx.requireProEdition()) {








        }

        return null;
    }

    private static final Field<?> parseFieldTimestampLiteralIf(ParserContext ctx) {
        int position = ctx.position();

        if (parseKeywordIf(ctx, "TIMESTAMP")) {
            if (parseKeywordIf(ctx, "WITHOUT TIME ZONE")) {
                return inline(parseTimestampLiteral(ctx));
            }
            else if (parseIf(ctx, '(')) {
                Field<?> f = parseField(ctx, S);
                parse(ctx, ')');
                return timestamp((Field) f);
            }
            else if (peek(ctx, '\'')) {
                return inline(parseTimestampLiteral(ctx));
            }
            else {
                ctx.position(position);
                return field(parseIdentifier(ctx));
            }
        }

        return null;
    }

    private static final Timestamp parseTimestampLiteral(ParserContext ctx) {
        try {
            return Timestamp.valueOf(parseStringLiteral(ctx));
        }
        catch (IllegalArgumentException e) {
            throw ctx.exception("Illegal timestamp literal");
        }
    }

    private static final Field<?> parseFieldTimeLiteralIf(ParserContext ctx) {
        int position = ctx.position();

        if (parseKeywordIf(ctx, "TIME")) {
            if (parseKeywordIf(ctx, "WITHOUT TIME ZONE")) {
                return inline(parseTimeLiteral(ctx));
            }
            else if (parseIf(ctx, '(')) {
                Field<?> f = parseField(ctx, S);
                parse(ctx, ')');
                return time((Field) f);
            }
            else if (peek(ctx, '\'')) {
                return inline(parseTimeLiteral(ctx));
            }
            else {
                ctx.position(position);
                return field(parseIdentifier(ctx));
            }
        }

        return null;
    }

    private static final Time parseTimeLiteral(ParserContext ctx) {
        try {
            return Time.valueOf(parseStringLiteral(ctx));
        }
        catch (IllegalArgumentException e) {
            throw ctx.exception("Illegal time literal");
        }
    }

    private static final Field<?> parseFieldIntervalLiteralIf(ParserContext ctx) {
        int position = ctx.position();

        if (parseKeywordIf(ctx, "INTERVAL")) {
            if (peek(ctx, '\'')) {
                return inline(parseIntervalLiteral(ctx));
            }
            else {
                Long interval = parseUnsignedIntegerIf(ctx);

                if (interval != null) {
                    DatePart part = parseIntervalDatePart(ctx);
                    long l = interval;
                    int i = (int) l;

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
                            return inline(new DayToSecond(0, 0, 0, (int) (l / 1000), (int) (l % 1000 * 1000000)));
                        case MICROSECOND:
                            return inline(new DayToSecond(0, 0, 0, (int) (l / 1000000), (int) (l % 1000000 * 1000)));
                        case NANOSECOND:
                            return inline(new DayToSecond(0, 0, 0, (int) (l / 1000000000), (int) (l % 1000000000)));
                    }
                }

                else {
                    ctx.position(position);
                    return field(parseIdentifier(ctx));
                }
            }
        }

        return null;
    }

    private static final Interval parsePostgresIntervalLiteralIf(ParserContext ctx) {
        int position = ctx.position();

        p:
        if (parseIf(ctx, '\'')) {
            parseIf(ctx, '@');

            Number year = null;
            Number month = null;
            Number day = null;
            Number hour = null;
            Number minute = null;
            Number second = null;

            do {

                boolean minus = parseIf(ctx, '-');
                if (!minus)
                    parseIf(ctx, '+');

                Number n = parseUnsignedNumericLiteralIf(ctx, minus ? Sign.MINUS : Sign.NONE);
                if (n == null)
                    break p;

                switch (ctx.character()) {
                    case 'D':
                    case 'd':
                        if (parseKeywordIf(ctx, "D") ||
                            parseKeywordIf(ctx, "DAY") ||
                            parseKeywordIf(ctx, "DAYS"))
                            if (day == null)
                                day = n;
                            else
                                throw ctx.exception("Day part already defined");

                        break;

                    case 'H':
                    case 'h':
                        if (parseKeywordIf(ctx, "H") ||
                            parseKeywordIf(ctx, "HOUR") ||
                            parseKeywordIf(ctx, "HOURS"))
                            if (hour == null)
                                hour = n;
                            else
                                throw ctx.exception("Hour part already defined");

                        break;

                    case 'M':
                    case 'm':
                        if (parseKeywordIf(ctx, "M") ||
                            parseKeywordIf(ctx, "MIN") ||
                            parseKeywordIf(ctx, "MINS") ||
                            parseKeywordIf(ctx, "MINUTE") ||
                            parseKeywordIf(ctx, "MINUTES"))
                            if (minute == null)
                                minute = n;
                            else
                                throw ctx.exception("Minute part already defined");

                        else if (parseKeywordIf(ctx, "MON") ||
                                 parseKeywordIf(ctx, "MONS") ||
                                 parseKeywordIf(ctx, "MONTH") ||
                                 parseKeywordIf(ctx, "MONTHS"))
                            if (month == null)
                                month = n;
                            else
                                throw ctx.exception("Month part already defined");

                        break;

                    case 'S':
                    case 's':
                        if (parseKeywordIf(ctx, "S") ||
                            parseKeywordIf(ctx, "SEC") ||
                            parseKeywordIf(ctx, "SECS") ||
                            parseKeywordIf(ctx, "SECOND") ||
                            parseKeywordIf(ctx, "SECONDS"))
                            if (second == null)
                                second = n;
                            else
                                throw ctx.exception("Second part already defined");

                        break;

                    case 'Y':
                    case 'y':
                        if (parseKeywordIf(ctx, "Y") ||
                            parseKeywordIf(ctx, "YEAR") ||
                            parseKeywordIf(ctx, "YEARS"))
                            if (year == null)
                                year = n;
                            else
                                throw ctx.exception("Year part already defined");

                        break;

                    default:
                        break p;
                }
            }
            while (!parseIf(ctx, '\''));

            int months = (month == null ? 0 : month.intValue())
                       + (year  == null ? 0 : (int) (year.doubleValue() * 12));

            double seconds = (month  == null ? 0.0 : ((month.doubleValue() % 1.0) * 30 * 86400))
                           + (day    == null ? 0.0 : ((day.doubleValue() * 86400)))
                           + (hour   == null ? 0.0 : ((hour.doubleValue() * 3600)))
                           + (minute == null ? 0.0 : ((minute.doubleValue() * 60)))
                           + (second == null ? 0.0 : ((second.doubleValue())));

            return new YearToSecond(
                new YearToMonth(0, months),
                new DayToSecond(0, 0, 0, (int) seconds, (int) ((seconds % 1.0) * 1000000000))
            );
        }

        ctx.position(position);
        return null;
    }

    private static final Interval parseIntervalLiteral(ParserContext ctx) {
        Interval result = parsePostgresIntervalLiteralIf(ctx);
        if (result != null)
            return result;

        String string = parseStringLiteral(ctx);

        try {
            if (parseKeywordIf(ctx, "YEAR"))
                return new YearToMonth(Integer.parseInt(string));
            else if (parseKeywordIf(ctx, "MONTH"))
                return new YearToMonth(0, Integer.parseInt(string));
            else if (parseKeywordIf(ctx, "DAY"))
                return new DayToSecond(Integer.parseInt(string));
            else if (parseKeywordIf(ctx, "HOUR"))
                return new DayToSecond(0, Integer.parseInt(string));
            else if (parseKeywordIf(ctx, "MINUTE"))
                return new DayToSecond(0, 0, Integer.parseInt(string));
            else if (parseKeywordIf(ctx, "SECOND"))
                return new DayToSecond(0, 0, 0, Integer.parseInt(string));
        }
        catch (NumberFormatException e) {
            throw ctx.expected("Unsigned integer");
        }

        DayToSecond ds = DayToSecond.valueOf(string);
        if (ds != null)
            return ds;

        YearToMonth ym = YearToMonth.valueOf(string);

        if (ym != null)
            return ym;

        YearToSecond ys = YearToSecond.valueOf(string);
        if (ys != null)
            return ys;

        throw ctx.exception("Illegal interval literal");
    }

    private static final Field<?> parseFieldDateLiteralIf(ParserContext ctx) {
        int position = ctx.position();

        if (parseKeywordIf(ctx, "DATE")) {
            if (parseIf(ctx, '(')) {
                Field<?> f = parseField(ctx, S);
                parse(ctx, ')');
                return date((Field) f);
            }
            else if (peek(ctx, '\'')) {
                return inline(parseDateLiteral(ctx));
            }
            else {
                ctx.position(position);
                return field(parseIdentifier(ctx));
            }
        }

        return null;
    }

    private static final Field<?> parseFieldDateTruncIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "DATE_TRUNC")) {
            parse(ctx, '(');
            DatePart part = DatePart.valueOf(parseStringLiteral(ctx).toUpperCase());
            parse(ctx, ',');
            Field<?> field = parseField(ctx, D);
            parse(ctx, ')');

            return trunc(field, part);
        }

        return null;
    }

    private static final Field<?> parseFieldDateAddIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "DATEADD")) {
            parse(ctx, '(');
            DatePart part = parseDatePart(ctx);
            parse(ctx, ',');
            Field<Number> interval = (Field<Number>) parseField(ctx, Type.N);
            parse(ctx, ',');
            Field<Date> date = (Field<Date>) parseField(ctx, Type.D);
            parse(ctx, ')');

            return DSL.dateAdd(date, interval, part);
        }

        return null;
    }

    private static final Field<?> parseFieldDateDiffIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "DATEDIFF")) {
            parse(ctx, '(');
            Field<Date> d1 = (Field<Date>) parseField(ctx, Type.D);
            parse(ctx, ',');
            Field<Date> d2 = (Field<Date>) parseField(ctx, Type.D);
            parse(ctx, ')');

            return DSL.dateDiff(d1, d2);
        }

        return null;
    }

    private static final Date parseDateLiteral(ParserContext ctx) {
        try {
            return Date.valueOf(parseStringLiteral(ctx));
        }
        catch (IllegalArgumentException e) {
            throw ctx.exception("Illegal date literal");
        }
    }

    private static final Field<?> parseFieldExtractIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "EXTRACT")) {
            parse(ctx, '(');
            DatePart part = parseDatePart(ctx);
            parseKeyword(ctx, "FROM");
            Field<?> field = parseField(ctx);
            parse(ctx, ')');

            return extract(field, part);
        }

        return null;
    }

    private static final Field<?> parseFieldDatePartIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "DATEPART")) {
            parse(ctx, '(');
            DatePart part = parseDatePart(ctx);
            parse(ctx, ',');
            Field<?> field = parseField(ctx);
            parse(ctx, ')');

            return extract(field, part);
        }

        return null;
    }

    private static final DatePart parseDatePart(ParserContext ctx) {
        char character = ctx.character();

        switch (character) {
            case 'c':
            case 'C':
                if (parseKeywordIf(ctx, "CENTURY"))
                    return DatePart.CENTURY;

                break;

            case 'd':
            case 'D':
                if (parseKeywordIf(ctx, "DAYOFYEAR") ||
                    parseKeywordIf(ctx, "DAY_OF_YEAR") ||
                    parseKeywordIf(ctx, "DOY") ||
                    parseKeywordIf(ctx, "DY"))
                    return DatePart.DAY_OF_YEAR;
                else if (parseKeywordIf(ctx, "DAY_OF_WEEK") ||
                    parseKeywordIf(ctx, "DAYOFWEEK") ||
                    parseKeywordIf(ctx, "DW"))
                    return DatePart.DAY_OF_WEEK;
                else if (parseKeywordIf(ctx, "DAY") ||
                    parseKeywordIf(ctx, "DD") ||
                    parseKeywordIf(ctx, "D"))
                    return DatePart.DAY;
                else if (parseKeywordIf(ctx, "DECADE"))
                    return DatePart.DECADE;

                break;

            case 'e':
            case 'E':
                if (parseKeywordIf(ctx, "EPOCH"))
                    return DatePart.EPOCH;

                break;

            case 'h':
            case 'H':
                if (parseKeywordIf(ctx, "HOUR") ||
                    parseKeywordIf(ctx, "HH"))
                    return DatePart.HOUR;

                break;

            case 'i':
            case 'I':
                if (parseKeywordIf(ctx, "ISODOW") ||
                    parseKeywordIf(ctx, "ISO_DAY_OF_WEEK"))
                    return DatePart.ISO_DAY_OF_WEEK;

            case 'm':
            case 'M':
                if (parseKeywordIf(ctx, "MINUTE") ||
                    parseKeywordIf(ctx, "MI"))
                    return DatePart.MINUTE;
                else if (parseKeywordIf(ctx, "MILLENNIUM"))
                    return DatePart.MILLENNIUM;
                else if (parseKeywordIf(ctx, "MICROSECOND") ||
                    parseKeywordIf(ctx, "MCS"))
                    return DatePart.MICROSECOND;
                else if (parseKeywordIf(ctx, "MILLISECOND") ||
                    parseKeywordIf(ctx, "MS"))
                    return DatePart.MILLISECOND;
                else if (parseKeywordIf(ctx, "MONTH") ||
                    parseKeywordIf(ctx, "MM") ||
                    parseKeywordIf(ctx, "M"))
                    return DatePart.MONTH;

                break;

            case 'n':
            case 'N':
                if (parseKeywordIf(ctx, "N"))
                    return DatePart.MINUTE;
                else if (parseKeywordIf(ctx, "NANOSECOND") ||
                    parseKeywordIf(ctx, "NS"))
                    return DatePart.NANOSECOND;

                break;

            case 'q':
            case 'Q':
                if (parseKeywordIf(ctx, "QUARTER") ||
                    parseKeywordIf(ctx, "QQ") ||
                    parseKeywordIf(ctx, "Q"))
                    return DatePart.QUARTER;

                break;

            case 's':
            case 'S':
                if (parseKeywordIf(ctx, "SECOND") ||
                    parseKeywordIf(ctx, "SS") ||
                    parseKeywordIf(ctx, "S"))
                    return DatePart.SECOND;

                break;

            case 't':
            case 'T':
                if (parseKeywordIf(ctx, "TIMEZONE"))
                    return DatePart.TIMEZONE;
                else if (parseKeywordIf(ctx, "TIMEZONE_HOUR"))
                    return DatePart.TIMEZONE_HOUR;
                else if (parseKeywordIf(ctx, "TIMEZONE_MINUTE"))
                    return DatePart.TIMEZONE_MINUTE;

                break;

            case 'w':
            case 'W':
                if (parseKeywordIf(ctx, "WEEK") ||
                    parseKeywordIf(ctx, "WK") ||
                    parseKeywordIf(ctx, "WW"))
                    return DatePart.WEEK;
                else if (parseKeywordIf(ctx, "WEEKDAY") ||
                    parseKeywordIf(ctx, "W"))
                    return DatePart.DAY_OF_WEEK;

                break;

            case 'y':
            case 'Y':
                if (parseKeywordIf(ctx, "YEAR") ||
                    parseKeywordIf(ctx, "YYYY") ||
                    parseKeywordIf(ctx, "YY"))
                    return DatePart.YEAR;
                else if (parseKeywordIf(ctx, "Y"))
                    return DatePart.DAY_OF_YEAR;

                break;
        }

        throw ctx.expected("DatePart");
    }

    private static final DatePart parseIntervalDatePart(ParserContext ctx) {
        char character = ctx.character();

        switch (character) {
            case 'd':
            case 'D':
                if (parseKeywordIf(ctx, "DAY") ||
                    parseKeywordIf(ctx, "DAYS"))
                    return DatePart.DAY;

                break;

            case 'h':
            case 'H':
                if (parseKeywordIf(ctx, "HOUR") ||
                    parseKeywordIf(ctx, "HOURS"))
                    return DatePart.HOUR;

                break;

            case 'm':
            case 'M':
                if (parseKeywordIf(ctx, "MINUTE") ||
                    parseKeywordIf(ctx, "MINUTES"))
                    return DatePart.MINUTE;
                else if (parseKeywordIf(ctx, "MICROSECOND") ||
                    parseKeywordIf(ctx, "MICROSECONDS"))
                    return DatePart.MICROSECOND;
                else if (parseKeywordIf(ctx, "MILLISECOND") ||
                    parseKeywordIf(ctx, "MILLISECONDS"))
                    return DatePart.MILLISECOND;
                else if (parseKeywordIf(ctx, "MONTH") ||
                    parseKeywordIf(ctx, "MONTHS"))
                    return DatePart.MONTH;

                break;

            case 'n':
            case 'N':
                if (parseKeywordIf(ctx, "NANOSECOND") ||
                    parseKeywordIf(ctx, "NANOSECONDS"))
                    return DatePart.NANOSECOND;

                break;

            case 'q':
            case 'Q':
                if (parseKeywordIf(ctx, "QUARTER") ||
                    parseKeywordIf(ctx, "QUARTERS"))
                    return DatePart.QUARTER;

                break;

            case 's':
            case 'S':
                if (parseKeywordIf(ctx, "SECOND") ||
                    parseKeywordIf(ctx, "SECONDS"))
                    return DatePart.SECOND;

                break;

            case 'w':
            case 'W':
                if (parseKeywordIf(ctx, "WEEK") ||
                    parseKeywordIf(ctx, "WEEKS"))
                    return DatePart.WEEK;

                break;

            case 'y':
            case 'Y':
                if (parseKeywordIf(ctx, "YEAR") ||
                    parseKeywordIf(ctx, "YEARS"))
                    return DatePart.YEAR;

                break;
        }

        throw ctx.expected("Interval DatePart");
    }

    private static final Field<?> parseFieldAsciiIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "ASCII")) {
            parse(ctx, '(');
            Field<?> arg = parseField(ctx, S);
            parse(ctx, ')');
            return ascii((Field) arg);
        }

        return null;
    }

    private static final Field<?> parseFieldConcatIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "CONCAT")) {
            parse(ctx, '(');
            Field<String> result = concat(parseFields(ctx).toArray(EMPTY_FIELD));
            parse(ctx, ')');
            return result;
        }

        return null;
    }

    private static final Field<?> parseFieldInstrIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "INSTR")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<String> f2 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return position(f1, f2);
        }

        return null;
    }

    private static final Field<?> parseFieldCharIndexIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "CHARINDEX")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<String> f2 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return position(f2, f1);
        }

        return null;
    }

    private static final Field<?> parseFieldLpadIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "LPAD")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<Integer> f2 = (Field) parseField(ctx, N);
            Field<String> f3 = parseIf(ctx, ',')
                ? (Field) parseField(ctx, S)
                : null;
            parse(ctx, ')');
            return f3 == null
                ? lpad(f1, f2)
                : lpad(f1, f2, f3);
        }

        return null;
    }

    private static final Field<?> parseFieldRpadIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "RPAD")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<Integer> f2 = (Field) parseField(ctx, N);
            Field<String> f3 = parseIf(ctx, ',')
                ? (Field) parseField(ctx, S)
                : null;
            parse(ctx, ')');
            return f3 == null
                ? rpad(f1, f2)
                : rpad(f1, f2, f3);
        }

        return null;
    }

    private static final Field<?> parseFieldOverlayIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "OVERLAY")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parseKeyword(ctx, "PLACING");
            Field<String> f2 = (Field) parseField(ctx, S);
            parseKeyword(ctx, "FROM");
            Field<Number> f3 = (Field) parseField(ctx, N);
            Field<Number> f4 =
                parseKeywordIf(ctx, "FOR")
              ? (Field) parseField(ctx, N)




              : null;
            parse(ctx, ')');

            return f4 == null ? overlay(f1, f2, f3) : overlay(f1, f2, f3, f4);
        }

        return null;
    }

    private static final Field<?> parseFieldPositionIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "POSITION")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parseKeyword(ctx, "IN");
            Field<String> f2 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return position(f2, f1);
        }

        return null;
    }

    private static final Field<?> parseFieldRepeatIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "REPEAT")) {
            parse(ctx, '(');
            Field<String> field = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<Integer> count = (Field) parseField(ctx, N);
            parse(ctx, ')');
            return DSL.repeat(field, count);
        }

        return null;
    }

    private static final Field<?> parseFieldReplaceIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "REPLACE") ||
            parseFunctionNameIf(ctx, "OREPLACE") ||
            parseFunctionNameIf(ctx, "STR_REPLACE")) {

            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<String> f2 = (Field) parseField(ctx, S);
            Field<String> f3 = parseIf(ctx, ',')
                ? (Field) parseField(ctx, S)
                : null;
            parse(ctx, ')');
            return f3 == null
                ? replace(f1, f2)
                : replace(f1, f2, f3);
        }

        return null;
    }

    private static final Field<?> parseFieldReverseIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "REVERSE")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return reverse(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldSpaceIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "SPACE")) {
            parse(ctx, '(');
            Field<Integer> f1 = (Field) parseField(ctx, N);
            parse(ctx, ')');
            return space(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldSubstringIf(ParserContext ctx) {
        boolean substring = parseFunctionNameIf(ctx, "SUBSTRING");
        boolean substr = !substring && parseFunctionNameIf(ctx, "SUBSTR");

        if (substring || substr) {
            boolean keywords = !substr;
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            if (substr || !(keywords = parseKeywordIf(ctx, "FROM")))
                parse(ctx, ',');
            Field f2 = toField(ctx, parseNumericOp(ctx, N));
            Field f3 =
                    ((keywords && parseKeywordIf(ctx, "FOR")) || (!keywords && parseIf(ctx, ',')))
                ? (Field) toField(ctx, parseNumericOp(ctx, N))
                : null;
            parse(ctx, ')');

            return f3 == null
                ? substring(f1, f2)
                : substring(f1, f2, f3);
        }

        return null;
    }

    private static final Field<?> parseFieldTrimIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "TRIM")) {
            parse(ctx, '(');
            int position = ctx.position();

            boolean leading = parseKeywordIf(ctx, "LEADING") || parseKeywordIf(ctx, "L");
            boolean trailing = !leading && (parseKeywordIf(ctx, "TRAILING") || parseKeywordIf(ctx, "T"));
            boolean both = !leading && !trailing && (parseKeywordIf(ctx, "BOTH") || parseKeywordIf(ctx, "B"));

            if (leading || trailing || both) {
                if (parseIf(ctx, ',')) {
                    ctx.position(position);
                }
                else if (parseIf(ctx, ')')) {
                    ctx.position(position);
                }
                else if (parseKeywordIf(ctx, "FROM")) {
                    Field<String> f = (Field) parseField(ctx, S);
                    parse(ctx, ')');

                    return leading ? ltrim(f)
                         : trailing ? rtrim(f)
                         : trim(f);
                }
            }

            Field<String> f1 = (Field) parseField(ctx, S);

            if (parseKeywordIf(ctx, "FROM")) {
                Field<String> f2 = (Field) parseField(ctx, S);
                parse(ctx, ')');

                return leading ? ltrim(f2, f1)
                     : trailing ? rtrim(f2, f1)
                     : trim(f2, f1);
            }
            else {
                Field<String> f2 = parseIf(ctx, ',') ? (Field) parseField(ctx, S) : null;
                parse(ctx, ')');

                return f2 == null ? trim(f1) : trim(f1, f2);
            }
        }

        return null;
    }

    private static final Field<?> parseFieldTranslateIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "TRANSLATE")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<String> f2 = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<String> f3 = (Field) parseField(ctx, S);
            parse(ctx, ')');






            return translate(f1, f2, f3);
        }

        return null;
    }

    private static final Field<?> parseFieldToCharIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "TO_CHAR")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx);
            parse(ctx, ')');
            return cast(f1, SQLDataType.VARCHAR);
        }

        return null;
    }

    private static final Field<?> parseFieldToNumberIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "TO_NUMBER")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return cast(f1, SQLDataType.NUMERIC);
        }

        return null;
    }

    private static final Field<?> parseFieldToDateIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "TO_DATE")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<String> f2 = (Field) parseField(ctx, S);
            parse(ctx, ')');

            return toDate(f1, f2);
        }

        return null;
    }

    private static final Field<?> parseFieldToTimestampIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "TO_TIMESTAMP")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<String> f2 = (Field) parseField(ctx, S);
            parse(ctx, ')');

            return toTimestamp(f1, f2);
        }

        return null;
    }

    private static final Field<?> parseFieldTimestampDiffIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "TIMESTAMPDIFF")) {
            parse(ctx, '(');
            Field<Timestamp> ts1 = (Field<Timestamp>) parseField(ctx, Type.D);
            parse(ctx, ',');
            Field<Timestamp> ts2 = (Field<Timestamp>) parseField(ctx, Type.D);
            parse(ctx, ')');

            return DSL.timestampDiff(ts1, ts2);
        }

        return null;
    }

    private static final Field<?> parseFieldRtrimIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "RTRIM")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            Field<String> f2 = parseIf(ctx, ',') ? (Field) parseField(ctx, S) : null;
            parse(ctx, ')');

            return f2 == null ? rtrim(f1) : rtrim(f1, f2);
        }

        return null;
    }

    private static final Field<?> parseFieldLtrimIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "LTRIM")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            Field<String> f2 = parseIf(ctx, ',') ? (Field) parseField(ctx, S) : null;
            parse(ctx, ')');

            return f2 == null ? ltrim(f1) : ltrim(f1, f2);
        }

        return null;
    }

    private static final Field<?> parseFieldMidIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "MID")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<? extends Number> f2 = (Field) parseField(ctx, N);
            parse(ctx, ',');
            Field<? extends Number> f3 = (Field) parseField(ctx, N);
            parse(ctx, ')');
            return mid(f1, f2, f3);
        }

        return null;
    }

    private static final Field<?> parseFieldLeftIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "LEFT")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<? extends Number> f2 = (Field) parseField(ctx, N);
            parse(ctx, ')');
            return left(f1, f2);
        }

        return null;
    }

    private static final Field<?> parseFieldRightIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "RIGHT")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<? extends Number> f2 = (Field) parseField(ctx, N);
            parse(ctx, ')');
            return right(f1, f2);
        }

        return null;
    }

    private static final Field<?> parseFieldMd5If(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "MD5")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return md5(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldLengthIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "LENGTH") || parseFunctionNameIf(ctx, "LEN")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return length(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldCharLengthIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "CHAR_LENGTH")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return charLength(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldBitLengthIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "BIT_LENGTH")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return bitLength(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldOctetLengthIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "OCTET_LENGTH")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return octetLength(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldLowerIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "LOWER") || parseFunctionNameIf(ctx, "LCASE")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return lower(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldUpperIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "UPPER") || parseFunctionNameIf(ctx, "UCASE")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return DSL.upper(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldDecodeIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "DECODE")) {
            parse(ctx, '(');
            List<Field<?>> fields = parseFields(ctx);
            int size = fields.size();
            if (size < 3)
                throw ctx.expected("At least three arguments to DECODE()");

            parse(ctx, ')');
            return DSL.decode(
                (Field<Object>)   fields.get(0),
                (Field<Object>)   fields.get(1),
                (Field<Object>)   fields.get(2),
                (Field<Object>[]) (size == 3 ? EMPTY_FIELD : fields.subList(3, size).toArray(EMPTY_FIELD))
            );
        }

        return null;
    }

    private static final Field<?> parseFieldChooseIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "CHOOSE")) {
            parse(ctx, '(');
            Field<Integer> index = (Field<Integer>) parseField(ctx, Type.N);
            parse(ctx, ',');
            List<Field<?>> fields = parseFields(ctx);
            parse(ctx, ')');

            return DSL.choose(index, fields.toArray(EMPTY_FIELD));
        }

        return null;
    }

    private static final Field<?> parseFieldYearIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "YEAR")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return year(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldMonthIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "MONTH")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return month(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldDayIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "DAY")
                || parseFunctionNameIf(ctx, "DAYOFMONTH")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return day(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldDayOfWeekIf(ParserContext ctx) {

        // DB2 and MySQL support the non-ISO version where weeks go from Sunday = 1 to Saturday = 7
        if (parseFunctionNameIf(ctx, "DAYOFWEEK")
                || parseFunctionNameIf(ctx, "DAY_OF_WEEK")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return dayOfWeek(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldIsoDayOfWeekIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "DAYOFWEEK_ISO")
                || parseFunctionNameIf(ctx, "ISO_DAY_OF_WEEK")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return isoDayOfWeek(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldDayOfYearIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "DAYOFYEAR")
                || parseFunctionNameIf(ctx, "DAY_OF_YEAR")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return dayOfYear(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldEpochIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "EPOCH")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return epoch(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldMillenniumIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "MILLENNIUM")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return millennium(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldCenturyIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "CENTURY")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return century(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldDecadeIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "DECADE")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return decade(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldMillisecondIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "MILLISECOND")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return millisecond(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldMicrosecondIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "MICROSECOND")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return microsecond(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldWeekIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "WEEK")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return week(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldTimezoneIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "TIMEZONE")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return timezone(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldTimezoneHourIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "TIMEZONE_HOUR")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return timezoneHour(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldTimezoneMinuteIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "TIMEZONE_MINUTE")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return timezoneMinute(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldUnixTimestampIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "UNIX_TIMESTAMP")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return epoch(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldQuarterIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "QUARTER")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return quarter(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldHourIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "HOUR")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return hour(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldMinuteIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "MINUTE")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return minute(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldSecondIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "SECOND")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return second(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldSignIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "SIGN")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx, N);
            parse(ctx, ')');
            return sign((Field) f1);
        }

        return null;
    }

    private static final Field<?> parseFieldIfnullIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "IFNULL")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx);
            parse(ctx, ',');
            Field<?> f2 = parseField(ctx);
            parse(ctx, ')');

            return ifnull(f1, f2);
        }

        return null;
    }

    private static final Field<?> parseFieldIsnullIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "ISNULL")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx);
            Field<?> f2 = parseIf(ctx, ',') ? parseField(ctx) : null;
            parse(ctx, ')');

            return f2 != null ? isnull(f1, f2) : field(f1.isNull());
        }

        return null;
    }

    private static final Field<?> parseFieldIifIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "IIF")) {
            parse(ctx, '(');
            Condition c = parseCondition(ctx);
            parse(ctx, ',');
            Field<?> f1 = parseField(ctx);
            parse(ctx, ',');
            Field<?> f2 = parseField(ctx);
            parse(ctx, ')');

            return iif(c, f1, f2);
        }

        return null;
    }

    private static final Field<?> parseFieldNvlIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "NVL")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx);
            parse(ctx, ',');
            Field<?> f2 = parseField(ctx);
            parse(ctx, ')');

            return nvl(f1, f2);
        }

        return null;
    }

    private static final Field<?> parseFieldNvl2If(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "NVL2")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx);
            parse(ctx, ',');
            Field<?> f2 = parseField(ctx);
            parse(ctx, ',');
            Field<?> f3 = parseField(ctx);
            parse(ctx, ')');

            return nvl2(f1, f2, f3);
        }

        return null;
    }

    private static final Field<?> parseFieldNullifIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "NULLIF")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx);
            parse(ctx, ',');
            Field<?> f2 = parseField(ctx);
            parse(ctx, ')');

            return nullif(f1, f2);
        }

        return null;
    }

    private static final Field<?> parseFieldCoalesceIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "COALESCE")) {
            parse(ctx, '(');
            List<Field<?>> fields = parseFields(ctx);
            parse(ctx, ')');

            Field[] a = EMPTY_FIELD;
            return coalesce(fields.get(0), fields.size() == 1 ? a : fields.subList(1, fields.size()).toArray(a));
        }

        return null;
    }

    private static final <T, Z> Field<?> parseFieldFieldIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "FIELD")) {
            parse(ctx, '(');

            List<Field<?>> args = new ArrayList<>();

            args.add(parseField(ctx));
            parse(ctx, ',');

            int i = 1;
            do {
                args.add(parseField(ctx));
                args.add(inline(i++));
            }
            while (parseIf(ctx, ','));

            args.add(inline(0));
            parse(ctx, ')');

            return DSL.decode(
                (Field<T>) args.get(0),
                (Field<T>) args.get(1),
                (Field<Z>) args.get(2),
                args.subList(3, args.size()).toArray(EMPTY_FIELD)
            );
        }

        return null;
    }

    private static final Field<?> parseFieldCaseIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "CASE")) {
            if (parseKeywordIf(ctx, "WHEN")) {
                CaseConditionStep step = null;
                Field result;

                do {
                    Condition condition = parseCondition(ctx);
                    parseKeyword(ctx, "THEN");
                    Field value = parseField(ctx);
                    step = step == null ? when(condition, value) : step.when(condition, value);
                }
                while (parseKeywordIf(ctx, "WHEN"));

                if (parseKeywordIf(ctx, "ELSE"))
                    result = step.otherwise(parseField(ctx));
                else
                    result = step;

                parseKeyword(ctx, "END");
                return result;
            }
            else {
                CaseValueStep init = choose(parseField(ctx));
                CaseWhenStep step = null;
                Field result;
                parseKeyword(ctx, "WHEN");

                do {
                    Field when = parseField(ctx);
                    parseKeyword(ctx, "THEN");
                    Field then = parseField(ctx);
                    step = step == null ? init.when(when, then) : step.when(when, then);
                }
                while (parseKeywordIf(ctx, "WHEN"));

                if (parseKeywordIf(ctx, "ELSE"))
                    result = step.otherwise(parseField(ctx));
                else
                    result = step;

                parseKeyword(ctx, "END");
                return result;
            }
        }

        return null;
    }

    private static final Field<?> parseFieldCastIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "CAST")) {
            parse(ctx, '(');
            Field<?> field = parseField(ctx);
            parseKeyword(ctx, "AS");
            DataType<?> type = parseCastDataType(ctx);
            parse(ctx, ')');

            return cast(field, type);
        }

        return null;
    }

    private static final Field<?> parseFieldConvertIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "CONVERT")) {
            parse(ctx, '(');
            DataType<?> type = parseDataType(ctx);
            parse(ctx, ',');
            Field<?> field = parseField(ctx);
            Long style = null;
            if (parseIf(ctx, ',') && ctx.requireProEdition())
                style = parseUnsignedInteger(ctx);
            parse(ctx, ')');

            if (style == null)
                return cast(field, type);




        }

        return null;
    }

    private static final Field<Boolean> parseBooleanValueExpressionIf(ParserContext ctx) {
        TruthValue truth = parseTruthValueIf(ctx);

        if (truth != null) {
            switch (truth) {
                case TRUE:
                    return inline(true);
                case FALSE:
                    return inline(false);
                case NULL:
                    return inline((Boolean) null);
                default:
                    throw ctx.exception("Truth value not supported: " + truth);
            }
        }

        return null;
    }

    private static final Field<?> parseAggregateFunctionIf(ParserContext ctx) {
        return parseAggregateFunctionIf(ctx, false);
    }

    private static final Field<?> parseAggregateFunctionIf(ParserContext ctx, boolean basic) {
        AggregateFunction<?> agg = null;
        AggregateFilterStep<?> filter = null;
        WindowBeforeOverStep<?> over = null;
        Object keep = null;
        Field<?> result = null;
        Condition condition = null;

        keep = over = filter = agg = parseCountIf(ctx);
        if (filter == null)
            keep = over = filter = agg = parseGeneralSetFunctionIf(ctx);
        if (filter == null && !basic)
            over = filter = agg = parseBinarySetFunctionIf(ctx);
        if (filter == null && !basic)
            over = filter = parseOrderedSetFunctionIf(ctx);
        if (filter == null && !basic)
            over = filter = parseArrayAggFunctionIf(ctx);

        if (filter == null && over == null)
            if (!basic)
                return parseSpecialAggregateFunctionIf(ctx);
            else
                return null;

        if (keep != null && filter != null && !basic && parseKeywordIf(ctx, "KEEP")) {
            ctx.requireProEdition();


















        }
        else if (filter != null && !basic && parseKeywordIf(ctx, "FILTER")) {
            parse(ctx, '(');
            parseKeyword(ctx, "WHERE");
            condition = parseCondition(ctx);
            parse(ctx, ')');

            result = over = filter.filterWhere(condition);
        }
        else if (filter != null)
            result = filter;
        else
            result = over;

        if (!basic && parseKeywordIf(ctx, "OVER")) {
            Object nameOrSpecification = parseWindowNameOrSpecification(ctx, filter != null);

            if (nameOrSpecification instanceof Name)
                result = over.over((Name) nameOrSpecification);
            else if (nameOrSpecification instanceof WindowSpecification)
                result = over.over((WindowSpecification) nameOrSpecification);
            else
                result = over.over();
        }

        return result;
    }

    private static final Field<?> parseSpecialAggregateFunctionIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "GROUP_CONCAT")) {
            parse(ctx, '(');

            GroupConcatOrderByStep s1;
            GroupConcatSeparatorStep s2;
            AggregateFunction<String> s3;

            if (parseKeywordIf(ctx, "DISTINCT"))
                s1 = DSL.groupConcatDistinct(parseField(ctx));
            else
                s1 = DSL.groupConcat(parseField(ctx));

            if (parseKeywordIf(ctx, "ORDER BY"))
                s2 = s1.orderBy(parseSortSpecification(ctx));
            else
                s2 = s1;

            if (parseKeywordIf(ctx, "SEPARATOR"))
                s3 = s2.separator(parseStringLiteral(ctx));
            else
                s3 = s2;

            parse(ctx, ')');
            return s3;
        }

        return null;
    }

    private static final Object parseWindowNameOrSpecification(ParserContext ctx, boolean orderByAllowed) {
        Object result;

        if (parseIf(ctx, '(')) {
            result = parseWindowSpecificationIf(ctx, null, orderByAllowed);
            parse(ctx, ')');
        }
        else {
            result = parseIdentifier(ctx);
        }

        return result;
    }

    private static final Field<?> parseFieldRankIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "RANK")) {
            parse(ctx, '(');

            if (parseIf(ctx, ')'))
                return parseWindowFunction(ctx, null, null, rank());

            // Hypothetical set function
            List<Field<?>> args = parseFields(ctx);
            parse(ctx, ')');
            return rank(args).withinGroupOrderBy(parseWithinGroupN(ctx));
        }

        return null;
    }

    private static final Field<?> parseFieldDenseRankIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "DENSE_RANK")) {
            parse(ctx, '(');

            if (parseIf(ctx, ')'))
                return parseWindowFunction(ctx, null, null, denseRank());

            // Hypothetical set function
            List<Field<?>> args = parseFields(ctx);
            parse(ctx, ')');
            return denseRank(args).withinGroupOrderBy(parseWithinGroupN(ctx));
        }

        return null;
    }

    private static final Field<?> parseFieldPercentRankIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "PERCENT_RANK")) {
            parse(ctx, '(');

            if (parseIf(ctx, ')'))
                return parseWindowFunction(ctx, null, null, percentRank());

            // Hypothetical set function
            List<Field<?>> args = parseFields(ctx);
            parse(ctx, ')');
            return percentRank(args).withinGroupOrderBy(parseWithinGroupN(ctx));
        }

        return null;
    }

    private static final Field<?> parseFieldCumeDistIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "CUME_DIST")) {
            parse(ctx, '(');

            if (parseIf(ctx, ')'))
                return parseWindowFunction(ctx, null, null, cumeDist());

            // Hypothetical set function
            List<Field<?>> args = parseFields(ctx);
            parse(ctx, ')');
            return cumeDist(args).withinGroupOrderBy(parseWithinGroupN(ctx));
        }

        return null;
    }

    private static final Field<?> parseFieldRandIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "RAND") || parseFunctionNameIf(ctx, "RANDOM")) {
            parse(ctx, '(');
            parse(ctx, ')');
            return rand();
        }

        return null;
    }

    private static final Field<?> parseFieldRatioToReportIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "RATIO_TO_REPORT")) {
            parse(ctx, '(');
            Field<Number> field = (Field<Number>) parseField(ctx);
            parse(ctx, ')');
            return parseWindowFunction(ctx, null, null, ratioToReport(field));
        }

        return null;
    }

    private static final Field<?> parseFieldRowNumberIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "ROW_NUMBER")) {
            parse(ctx, '(');
            parse(ctx, ')');
            return parseWindowFunction(ctx, null, null, rowNumber());
        }

        return null;
    }

    private static final Field<?> parseFieldNtileIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "NTILE")) {
            parse(ctx, '(');
            int number = (int) (long) parseUnsignedInteger(ctx);
            parse(ctx, ')');
            return parseWindowFunction(ctx, null, null, ntile(number));
        }

        return null;
    }

    private static final Field<?> parseFieldLeadLagIf(ParserContext ctx) {
        boolean lead = parseFunctionNameIf(ctx, "LEAD");
        boolean lag = !lead && parseFunctionNameIf(ctx, "LAG");

        if (lead || lag) {
            parse(ctx, '(');
            Field<Void> f1 = (Field) parseField(ctx);
            Integer f2 = null;
            Field<Void> f3 = null;

            if (parseIf(ctx, ',')) {
                f2 = (int) (long) parseUnsignedInteger(ctx);

                if (parseIf(ctx, ','))
                    f3 = (Field) parseField(ctx);
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

            WindowOverStep<?> s2 = parseWindowRespectIgnoreNulls(ctx, s1, s1);
            parse(ctx, ')');
            return parseWindowFunction(ctx, null, s1, s2);
        }

        return null;
    }

    private static final Field<?> parseFieldFirstValueIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "FIRST_VALUE")) {
            parse(ctx, '(');
            Field<Void> arg = (Field) parseField(ctx);
            WindowIgnoreNullsStep<Void> s1 = firstValue(arg);
            WindowOverStep<?> s2 = parseWindowRespectIgnoreNulls(ctx, s1, s1);
            parse(ctx, ')');
            return parseWindowFunction(ctx, null, s1, s2);
        }

        return null;
    }

    private static final Field<?> parseFieldLastValueIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "LAST_VALUE")) {
            parse(ctx, '(');
            Field<Void> arg = (Field) parseField(ctx);
            WindowIgnoreNullsStep<Void> s1 = lastValue(arg);
            WindowOverStep<?> s2 = parseWindowRespectIgnoreNulls(ctx, s1, s1);
            parse(ctx, ')');
            return parseWindowFunction(ctx, null, s1, s2);
        }

        return null;
    }

    private static final Field<?> parseFieldNthValueIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "NTH_VALUE")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx);
            parse(ctx, ',');
            int f2 = (int) (long) parseUnsignedInteger(ctx);
            WindowFromFirstLastStep<?> s1 = nthValue(f1, f2);
            WindowIgnoreNullsStep s2 = parseWindowFromFirstLast(ctx, s1, s1);
            WindowOverStep<?> s3 = parseWindowRespectIgnoreNulls(ctx, s2, s2);
            parse(ctx, ')');
            return parseWindowFunction(ctx, s1, s2, s3);
        }

        return null;
    }

    private static final Field<?> parseWindowFunction(ParserContext ctx, WindowFromFirstLastStep s1, WindowIgnoreNullsStep s2, WindowOverStep<?> s3) {
        s2 = parseWindowFromFirstLast(ctx, s1, s2);
        s3 = parseWindowRespectIgnoreNulls(ctx, s2, s3);

        parseKeyword(ctx, "OVER");
        Object nameOrSpecification = parseWindowNameOrSpecification(ctx, true);

        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=494897
        Field<?> result = (nameOrSpecification instanceof Name)
            ? s3.over((Name) nameOrSpecification)
            : (nameOrSpecification instanceof WindowSpecification)
            ? s3.over((WindowSpecification) nameOrSpecification)
            : s3.over();

        return result;
    }

    private static final WindowOverStep<?> parseWindowRespectIgnoreNulls(ParserContext ctx, WindowIgnoreNullsStep s2, WindowOverStep<?> s3) {
        if (s2 != null)
            if (parseKeywordIf(ctx, "RESPECT NULLS"))
                s3 = s2.respectNulls();
            else if (parseKeywordIf(ctx, "IGNORE NULLS"))
                s3 = s2.ignoreNulls();
            else
                s3 = s2;

        return s3;
    }

    private static final WindowIgnoreNullsStep parseWindowFromFirstLast(ParserContext ctx, WindowFromFirstLastStep s1, WindowIgnoreNullsStep s2) {
        if (s1 != null)
            if (parseKeywordIf(ctx, "FROM FIRST"))
                s2 = s1.fromFirst();
            else if (parseKeywordIf(ctx, "FROM LAST"))
                s2 = s1.fromLast();
            else
                s2 = s1;

        return s2;
    }

    private static final AggregateFunction<?> parseBinarySetFunctionIf(ParserContext ctx) {
        Field<? extends Number> arg1;
        Field<? extends Number> arg2;
        BinarySetFunctionType type = parseBinarySetFunctionTypeIf(ctx);

        if (type == null)
            return null;

        parse(ctx, '(');
        arg1 = (Field) toField(ctx, parseNumericOp(ctx, N));
        parse(ctx, ',');
        arg2 = (Field) toField(ctx, parseNumericOp(ctx, N));
        parse(ctx, ')');

        switch (type) {
            case REGR_AVGX:
                return regrAvgX(arg1, arg2);
            case REGR_AVGY:
                return regrAvgY(arg1, arg2);
            case REGR_COUNT:
                return regrCount(arg1, arg2);
            case REGR_INTERCEPT:
                return regrIntercept(arg1, arg2);
            case REGR_R2:
                return regrR2(arg1, arg2);
            case REGR_SLOPE:
                return regrSlope(arg1, arg2);
            case REGR_SXX:
                return regrSXX(arg1, arg2);
            case REGR_SXY:
                return regrSXY(arg1, arg2);
            case REGR_SYY:
                return regrSYY(arg1, arg2);
            default:
                throw ctx.exception("Binary set function not supported: " + type);
        }
    }

    private static final AggregateFilterStep<?> parseOrderedSetFunctionIf(ParserContext ctx) {
        // TODO Listagg set function
        OrderedAggregateFunction<?> orderedN;
        OrderedAggregateFunctionOfDeferredType ordered1;

        orderedN = parseHypotheticalSetFunctionIf(ctx);
        if (orderedN == null)
            orderedN = parseInverseDistributionFunctionIf(ctx);
        if (orderedN == null)
            orderedN = parseListaggFunctionIf(ctx);
        if (orderedN != null)
            return orderedN.withinGroupOrderBy(parseWithinGroupN(ctx));

        ordered1 = parseModeIf(ctx);
        if (ordered1 != null)
            return ordered1.withinGroupOrderBy(parseWithinGroup1(ctx));

        return null;
    }

    private static final AggregateFilterStep<?> parseArrayAggFunctionIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "ARRAY_AGG")) {
            parse(ctx, '(');

            boolean distinct = parseKeywordIf(ctx, "DISTINCT");
            Field<?> a1 = parseField(ctx);
            List<SortField<?>> sort = null;

            if (parseKeywordIf(ctx, "ORDER BY"))
                sort = parseSortSpecification(ctx);

            parse(ctx, ')');

            ArrayAggOrderByStep<?> s1 = distinct
                ? arrayAggDistinct(a1)
                : arrayAgg(a1);

            return sort == null ? s1 : s1.orderBy(sort);
        }

        return null;
    }

    private static final List<SortField<?>> parseWithinGroupN(ParserContext ctx) {
        parseKeyword(ctx, "WITHIN GROUP");
        parse(ctx, '(');
        parseKeyword(ctx, "ORDER BY");
        List<SortField<?>> result = parseSortSpecification(ctx);
        parse(ctx, ')');
        return result;
    }

    private static final SortField<?> parseWithinGroup1(ParserContext ctx) {
        parseKeyword(ctx, "WITHIN GROUP");
        parse(ctx, '(');
        parseKeyword(ctx, "ORDER BY");
        SortField<?> result = parseSortField(ctx);
        parse(ctx, ')');
        return result;
    }

    private static final OrderedAggregateFunction<?> parseHypotheticalSetFunctionIf(ParserContext ctx) {

        // This currently never parses hypothetical set functions, as the function names are already
        // consumed earlier in parseFieldTerm(). We should implement backtracking...
        OrderedAggregateFunction<?> ordered;

        if (parseFunctionNameIf(ctx, "RANK")) {
            parse(ctx, '(');
            ordered = rank(parseFields(ctx));
            parse(ctx, ')');
        }
        else if (parseFunctionNameIf(ctx, "DENSE_RANK")) {
            parse(ctx, '(');
            ordered = denseRank(parseFields(ctx));
            parse(ctx, ')');
        }
        else if (parseFunctionNameIf(ctx, "PERCENT_RANK")) {
            parse(ctx, '(');
            ordered = percentRank(parseFields(ctx));
            parse(ctx, ')');
        }
        else if (parseFunctionNameIf(ctx, "CUME_DIST")) {
            parse(ctx, '(');
            ordered = cumeDist(parseFields(ctx));
            parse(ctx, ')');
        }
        else
            ordered = null;

        return ordered;
    }

    private static final OrderedAggregateFunction<BigDecimal> parseInverseDistributionFunctionIf(ParserContext ctx) {
        OrderedAggregateFunction<BigDecimal> ordered;

        if (parseFunctionNameIf(ctx, "PERCENTILE_CONT")) {
            parse(ctx, '(');
            ordered = percentileCont(parseFieldUnsignedNumericLiteral(ctx, Sign.NONE));
            parse(ctx, ')');
        }
        else if (parseFunctionNameIf(ctx, "PERCENTILE_DISC")) {
            parse(ctx, '(');
            ordered = percentileDisc(parseFieldUnsignedNumericLiteral(ctx, Sign.NONE));
            parse(ctx, ')');
        }
        else
            ordered = null;

        return ordered;
    }

    private static final OrderedAggregateFunction<String> parseListaggFunctionIf(ParserContext ctx) {
        OrderedAggregateFunction<String> ordered;

        if (parseFunctionNameIf(ctx, "LISTAGG")) {
            parse(ctx, '(');
            Field<?> field = parseField(ctx);

            if (parseIf(ctx, ','))
                ordered = listAgg(field, parseStringLiteral(ctx));
            else
                ordered = listAgg(field);

            parse(ctx, ')');
        }
        else
            ordered = null;

        return ordered;
    }

    private static final OrderedAggregateFunctionOfDeferredType parseModeIf(ParserContext ctx) {
        OrderedAggregateFunctionOfDeferredType ordered;

        if (parseFunctionNameIf(ctx, "MODE")) {
            parse(ctx, '(');
            parse(ctx, ')');
            ordered = mode();
        }
        else
            ordered = null;

        return ordered;
    }

    private static final AggregateFunction<?> parseGeneralSetFunctionIf(ParserContext ctx) {
        boolean distinct;
        Field arg;
        ComputationalOperation operation = parseComputationalOperationIf(ctx);

        if (operation == null)
            return null;

        parse(ctx, '(');

        switch (operation) {
            case AVG:
            case MAX:
            case MIN:
            case SUM:
            case PRODUCT:
                distinct = parseSetQuantifier(ctx);
                break;
            default:
                distinct = false;
                break;
        }

        arg = parseField(ctx);
        parse(ctx, ')');

        switch (operation) {
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
                throw ctx.exception("Unsupported computational operation");
        }
    }

    private static final AggregateFunction<?> parseCountIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "COUNT")) {
            parse(ctx, '(');
            boolean distinct = parseSetQuantifier(ctx);

            if (parseIf(ctx, '*') && parse(ctx, ')'))
                if (distinct)
                    return countDistinct(asterisk());
                else
                    return count();

            Field<?>[] fields = null;
            QualifiedAsterisk asterisk = null;
            Row row = parseRowIf(ctx);
            if (row != null)
                fields = row.fields();
            else if ((asterisk = parseQualifiedAsteriskIf(ctx)) == null)
                fields = distinct
                        ? parseFields(ctx).toArray(EMPTY_FIELD)
                        : new Field[] { parseField(ctx) };

            parse(ctx, ')');

            if (distinct)
                if (fields == null)
                    return countDistinct(asterisk);
                else if (fields.length > 0)
                    return countDistinct(fields);
                else
                    return countDistinct(fields[0]);
            else if (fields == null)
                return count(asterisk);
            else
                return count(fields[0]);
        }

        return null;
    }

    private static final boolean parseSetQuantifier(ParserContext ctx) {
        boolean distinct = parseKeywordIf(ctx, "DISTINCT");
        if (!distinct)
            parseKeywordIf(ctx, "ALL");
        return distinct;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Name parsing
    // -----------------------------------------------------------------------------------------------------------------

    private static final Catalog parseCatalogName(ParserContext ctx) {
        return catalog(parseName(ctx));
    }

    private static final Schema parseSchemaName(ParserContext ctx) {
        return schema(parseName(ctx));
    }

    private static final Table<?> parseTableName(ParserContext ctx) {
        return ctx.lookupTable(parseName(ctx));
    }

    private static final Table<?> parseTableNameIf(ParserContext ctx) {
        Name name = parseNameIf(ctx);

        if (name == null)
            return null;

        return table(name);
    }

    private static final Field<?> parseFieldNameOrSequenceExpression(ParserContext ctx) {
        Name name = parseName(ctx);

        if (name.qualified()) {
            String last = name.last();

            if ("NEXTVAL".equalsIgnoreCase(last))
                return sequence(name.qualifier()).nextval();
            else if ("CURRVAL".equalsIgnoreCase(last))
                return sequence(name.qualifier()).currval();
        }

        if (ctx.dsl.settings().getParseUnknownFunctions() == ParseUnknownFunctions.IGNORE && parseIf(ctx, '(')) {
            List<Field<?>> arguments = new ArrayList<>();

            if (!parseIf(ctx, ')')) {
                do {
                    arguments.add(parseField(ctx));
                }
                while (parseIf(ctx, ','));

                parse(ctx, ')');
            }

            return function(name, Object.class, arguments.toArray(EMPTY_FIELD));
        }
        else {









            return ctx.lookupField(name);
        }
    }

    private static final TableField<?, ?> parseFieldName(ParserContext ctx) {
        return (TableField<?, ?>) field(parseName(ctx));
    }

    private static final List<Field<?>> parseFieldNames(ParserContext ctx) {
        List<Field<?>> result = new ArrayList<>();

        do {
            result.add(parseFieldName(ctx));
        }
        while (parseIf(ctx, ','));

        return result;
    }

    private static final Sequence<?> parseSequenceName(ParserContext ctx) {
        return sequence(parseName(ctx));
    }

    private static final Name parseIndexName(ParserContext ctx) {
        Name result = parseNameIf(ctx);

        if (result == null)
            throw ctx.expected("Identifier");

        return result;
    }

    private static final Name parseIndexNameIf(ParserContext ctx) {
        if (!peekKeyword(ctx, "ON"))
            return parseNameIf(ctx);
        else
            return null;
    }

    private static final Collation parseCollation(ParserContext ctx) {
        return collation(parseName(ctx));
    }

    private static final CharacterSet parseCharacterSet(ParserContext ctx) {
        return characterSet(parseName(ctx));
    }

    private static final Name parseName(ParserContext ctx) {
        Name result = parseNameIf(ctx);

        if (result == null)
            throw ctx.expected("Identifier");

        return result;
    }

    private static final Name parseNameIf(ParserContext ctx) {
        Name identifier = parseIdentifierIf(ctx);

        if (identifier == null)
            return null;

        // Avoid .. token in indexed for loops:
        // FOR i IN identifier1 .. identifier2 LOOP <...> END LOOP;
        if (peek(ctx, '.') && !peek(ctx, "..")) {
            List<Name> result = new ArrayList<>();
            result.add(identifier);

            while (parseIf(ctx, '.'))
                result.add(parseIdentifier(ctx));

            return DSL.name(result.toArray(EMPTY_NAME));
        }
        else
            return identifier;
    }

    private static final QualifiedAsterisk parseQualifiedAsteriskIf(ParserContext ctx) {
        int position = ctx.position();
        Name i1 = parseIdentifierIf(ctx);

        if (i1 == null)
            return null;

        if (parseIf(ctx, '.')) {
            List<Name> result = null;
            Name i2;

            do {
                if ((i2 = parseIdentifierIf(ctx)) != null) {
                    if (result == null) {
                        result = new ArrayList<>();
                        result.add(i1);
                    }

                    result.add(i2);
                }
                else {
                    parse(ctx, '*');
                    return ctx.lookupTable(result == null ? i1 : DSL.name(result.toArray(EMPTY_NAME))).asterisk();
                }
            }
            while (parseIf(ctx, '.'));
        }

        ctx.position(position);
        return null;
    }

    private static final List<Name> parseIdentifiers(ParserContext ctx) {
        LinkedHashSet<Name> result = new LinkedHashSet<>();

        do {
            if (!result.add(parseIdentifier(ctx)))
                throw ctx.exception("Duplicate identifier encountered");
        }
        while (parseIf(ctx, ','));
        return new ArrayList<>(result);
    }

    private static final Name parseIdentifier(ParserContext ctx) {
        return parseIdentifier(ctx, false);
    }

    private static final Name parseIdentifier(ParserContext ctx, boolean allowAposQuotes) {
        Name result = parseIdentifierIf(ctx, allowAposQuotes);

        if (result == null)
            throw ctx.expected("Identifier");

        return result;
    }

    private static final Name parseIdentifierIf(ParserContext ctx) {
        return parseIdentifierIf(ctx, false);
    }

    private static final Name parseIdentifierIf(ParserContext ctx, boolean allowAposQuotes) {
        char quoteEnd = parseQuote(ctx, allowAposQuotes);
        boolean quoted = quoteEnd != 0;

        int start = ctx.position();
        if (quoted)
            while (ctx.character() != quoteEnd && ctx.hasMore())
                ctx.positionInc();
        else
            while (ctx.isIdentifierPart() && ctx.hasMore())
                ctx.positionInc();

        if (ctx.position() == start)
            return null;

        String name = normaliseNameCase(ctx.configuration(), ctx.substring(start, ctx.position()), quoted, ctx.locale);

        if (quoted) {
            if (ctx.character() != quoteEnd)
                throw ctx.exception("Quoted identifier must terminate in " + quoteEnd);

            ctx.positionInc();
            parseWhitespaceIf(ctx);
            return DSL.quotedName(name);
        }
        else {
            parseWhitespaceIf(ctx);
            return DSL.unquotedName(name);
        }
    }

    private static final char parseQuote(ParserContext ctx, boolean allowAposQuotes) {
        return parseIf(ctx, '"', false) ? '"'
             : parseIf(ctx, '`', false) ? '`'
             : parseIf(ctx, '[', false) ? ']'
             : allowAposQuotes && parseIf(ctx, '\'', false) ? '\''
             : 0;
    }

    private static final DataType<?> parseCastDataType(ParserContext ctx) {
        char character = ctx.character();

        switch (character) {
            case 's':
            case 'S':
                if (parseKeywordIf(ctx, "SIGNED") && (parseKeywordIf(ctx, "INTEGER") || true))
                    return SQLDataType.BIGINT;

                break;

            case 'u':
            case 'U':
                if (parseKeywordIf(ctx, "UNSIGNED") && (parseKeywordIf(ctx, "INTEGER") || true))
                    return SQLDataType.BIGINTUNSIGNED;

                break;
        }

        return parseDataType(ctx);
    }

    private static final DataType<?> parseDataType(ParserContext ctx) {
        DataType<?> result = parseDataTypePrefix(ctx);
        boolean array = false;

        if (parseKeywordIf(ctx, "ARRAY"))
            array = true;

        if (parseIf(ctx, '[')) {
            parseUnsignedIntegerIf(ctx);
            parse(ctx, ']');

            array = true;
        }

        if (array)
            result = result.getArrayDataType();

        return result;
    }

    private static final DataType<?> parseDataTypePrefix(ParserContext ctx) {
        char character = ctx.character();

        if (character == '[' || character == '"' || character == '`')
            character = ctx.characterNext();

        switch (character) {
            case 'a':
            case 'A':
                if (parseKeywordOrIdentifierIf(ctx, "ARRAY"))
                    return SQLDataType.OTHER.getArrayDataType();

                break;

            case 'b':
            case 'B':
                if (parseKeywordOrIdentifierIf(ctx, "BIGINT"))
                    return parseUnsigned(ctx, parseAndIgnoreDataTypeLength(ctx, SQLDataType.BIGINT));
                else if (parseKeywordOrIdentifierIf(ctx, "BIGSERIAL"))
                    return SQLDataType.BIGINT.identity(true);
                else if (parseKeywordOrIdentifierIf(ctx, "BINARY"))
                    return parseDataTypeLength(ctx, SQLDataType.BINARY);
                else if (parseKeywordOrIdentifierIf(ctx, "BIT"))
                    return parseDataTypeLength(ctx, SQLDataType.BIT);
                else if (parseKeywordOrIdentifierIf(ctx, "BLOB"))
                    return parseDataTypeLength(ctx, SQLDataType.BLOB);
                else if (parseKeywordOrIdentifierIf(ctx, "BOOLEAN") ||
                         parseKeywordOrIdentifierIf(ctx, "BOOL"))
                    return SQLDataType.BOOLEAN;
                else if (parseKeywordOrIdentifierIf(ctx, "BYTEA"))
                    return SQLDataType.BLOB;

                break;

            case 'c':
            case 'C':
                if (parseKeywordOrIdentifierIf(ctx, "CHARACTER VARYING"))
                    return parseDataTypeCollation(ctx, parseDataTypeLength(ctx, SQLDataType.VARCHAR));
                else if (parseKeywordOrIdentifierIf(ctx, "CHAR") ||
                         parseKeywordOrIdentifierIf(ctx, "CHARACTER"))
                    return parseDataTypeCollation(ctx, parseDataTypeLength(ctx, SQLDataType.CHAR));
                else if (parseKeywordOrIdentifierIf(ctx, "CLOB"))
                    return parseDataTypeCollation(ctx, parseDataTypeLength(ctx, SQLDataType.CLOB));

                break;

            case 'd':
            case 'D':
                if (parseKeywordOrIdentifierIf(ctx, "DATE"))
                    return SQLDataType.DATE;
                else if (parseKeywordOrIdentifierIf(ctx, "DATETIME"))
                    return parseDataTypePrecision(ctx, SQLDataType.TIMESTAMP);
                else if (parseKeywordOrIdentifierIf(ctx, "DECIMAL"))
                    return parseDataTypePrecisionScale(ctx, SQLDataType.DECIMAL);
                else if (parseKeywordOrIdentifierIf(ctx, "DOUBLE PRECISION") ||
                         parseKeywordOrIdentifierIf(ctx, "DOUBLE"))
                    return parseAndIgnoreDataTypePrecisionScale(ctx, SQLDataType.DOUBLE);

                break;

            case 'e':
            case 'E':
                if (parseKeywordOrIdentifierIf(ctx, "ENUM"))
                    return parseDataTypeCollation(ctx, parseDataTypeEnum(ctx));

                break;

            case 'f':
            case 'F':
                if (parseKeywordOrIdentifierIf(ctx, "FLOAT"))
                    return parseAndIgnoreDataTypePrecisionScale(ctx, SQLDataType.FLOAT);

                break;

            case 'i':
            case 'I':
                if (parseKeywordOrIdentifierIf(ctx, "INTEGER") ||
                    parseKeywordOrIdentifierIf(ctx, "INT") ||
                    parseKeywordOrIdentifierIf(ctx, "INT4"))
                    return parseUnsigned(ctx, parseAndIgnoreDataTypeLength(ctx, SQLDataType.INTEGER));
                else if (parseKeywordOrIdentifierIf(ctx, "INT2"))
                    return SQLDataType.SMALLINT;
                else if (parseKeywordOrIdentifierIf(ctx, "INT8"))
                    return SQLDataType.BIGINT;

                break;

            case 'l':
            case 'L':
                if (parseKeywordOrIdentifierIf(ctx, "LONGBLOB"))
                    return SQLDataType.BLOB;
                else if (parseKeywordOrIdentifierIf(ctx, "LONGTEXT"))
                    return parseDataTypeCollation(ctx, SQLDataType.CLOB);
                else if (parseKeywordOrIdentifierIf(ctx, "LONG NVARCHAR"))
                    return parseDataTypeCollation(ctx, parseDataTypeLength(ctx, SQLDataType.LONGNVARCHAR));
                else if (parseKeywordOrIdentifierIf(ctx, "LONG VARBINARY"))
                    return parseDataTypeCollation(ctx, parseDataTypeLength(ctx, SQLDataType.LONGVARBINARY));
                else if (parseKeywordOrIdentifierIf(ctx, "LONG VARCHAR"))
                    return parseDataTypeCollation(ctx, parseDataTypeLength(ctx, SQLDataType.LONGVARCHAR));

                break;

            case 'm':
            case 'M':
                if (parseKeywordOrIdentifierIf(ctx, "MEDIUMBLOB"))
                    return SQLDataType.BLOB;
                else if (parseKeywordOrIdentifierIf(ctx, "MEDIUMINT"))
                    return parseUnsigned(ctx, parseAndIgnoreDataTypeLength(ctx, SQLDataType.INTEGER));
                else if (parseKeywordOrIdentifierIf(ctx, "MEDIUMTEXT"))
                    return parseDataTypeCollation(ctx, SQLDataType.CLOB);

                break;

            case 'n':
            case 'N':
                if (parseKeywordOrIdentifierIf(ctx, "NCHAR"))
                    return parseDataTypeCollation(ctx, parseDataTypeLength(ctx, SQLDataType.NCHAR));
                else if (parseKeywordOrIdentifierIf(ctx, "NCLOB"))
                    return parseDataTypeCollation(ctx, SQLDataType.NCLOB);
                else if (parseKeywordOrIdentifierIf(ctx, "NUMBER") ||
                         parseKeywordOrIdentifierIf(ctx, "NUMERIC"))
                    return parseDataTypePrecisionScale(ctx, SQLDataType.NUMERIC);
                else if (parseKeywordOrIdentifierIf(ctx, "NVARCHAR") ||
                         parseKeywordOrIdentifierIf(ctx, "NVARCHAR2"))
                    return parseDataTypeCollation(ctx, parseDataTypeLength(ctx, SQLDataType.NVARCHAR));

                break;

            case 'o':
            case 'O':
                if (parseKeywordOrIdentifierIf(ctx, "OTHER"))
                    return SQLDataType.OTHER;

                break;

            case 'r':
            case 'R':
                if (parseKeywordOrIdentifierIf(ctx, "REAL"))
                    return parseAndIgnoreDataTypePrecisionScale(ctx, SQLDataType.REAL);

                break;

            case 's':
            case 'S':
                if (parseKeywordOrIdentifierIf(ctx, "SERIAL4") ||
                    parseKeywordOrIdentifierIf(ctx, "SERIAL"))
                    return SQLDataType.INTEGER.identity(true);
                else if (parseKeywordOrIdentifierIf(ctx, "SERIAL8"))
                    return SQLDataType.BIGINT.identity(true);
                else if (parseKeywordOrIdentifierIf(ctx, "SET"))
                    return parseDataTypeCollation(ctx, parseDataTypeEnum(ctx));
                else if (parseKeywordOrIdentifierIf(ctx, "SMALLINT"))
                    return parseUnsigned(ctx, parseAndIgnoreDataTypeLength(ctx, SQLDataType.SMALLINT));
                else if (parseKeywordOrIdentifierIf(ctx, "SMALLSERIAL") ||
                         parseKeywordOrIdentifierIf(ctx, "SERIAL2"))
                    return SQLDataType.SMALLINT.identity(true);

                break;

            case 't':
            case 'T':
                if (parseKeywordOrIdentifierIf(ctx, "TEXT"))
                    return parseDataTypeCollation(ctx, parseAndIgnoreDataTypeLength(ctx, SQLDataType.CLOB));

                else if (parseKeywordOrIdentifierIf(ctx, "TIMESTAMPTZ"))
                    return parseDataTypePrecision(ctx, SQLDataType.TIMESTAMPWITHTIMEZONE);

                else if (parseKeywordOrIdentifierIf(ctx, "TIMESTAMP")) {
                    Integer precision = parseDataTypePrecision(ctx);


                    if (parseKeywordOrIdentifierIf(ctx, "WITH TIME ZONE"))
                        return precision == null ? SQLDataType.TIMESTAMPWITHTIMEZONE : SQLDataType.TIMESTAMPWITHTIMEZONE(precision);
                    else

                    if (parseKeywordOrIdentifierIf(ctx, "WITHOUT TIME ZONE") || true)
                        return precision == null ? SQLDataType.TIMESTAMP : SQLDataType.TIMESTAMP(precision);
                }

                else if (parseKeywordOrIdentifierIf(ctx, "TIMETZ"))
                    return parseDataTypePrecision(ctx, SQLDataType.TIMEWITHTIMEZONE);

                else if (parseKeywordOrIdentifierIf(ctx, "TIME")) {
                    Integer precision = parseDataTypePrecision(ctx);


                    if (parseKeywordOrIdentifierIf(ctx, "WITH TIME ZONE"))
                        return precision == null ? SQLDataType.TIMEWITHTIMEZONE : SQLDataType.TIMEWITHTIMEZONE(precision);
                    else

                    if (parseKeywordOrIdentifierIf(ctx, "WITHOUT TIME ZONE") || true)
                        return precision == null ? SQLDataType.TIME : SQLDataType.TIME(precision);
                }
                else if (parseKeywordOrIdentifierIf(ctx, "TINYBLOB"))
                    return SQLDataType.BLOB;
                else if (parseKeywordOrIdentifierIf(ctx, "TINYINT"))
                    return parseUnsigned(ctx, parseAndIgnoreDataTypeLength(ctx, SQLDataType.TINYINT));
                else if (parseKeywordOrIdentifierIf(ctx, "TINYTEXT"))
                    return parseDataTypeCollation(ctx, SQLDataType.CLOB);

                break;

            case 'u':
            case 'U':
                if (parseKeywordOrIdentifierIf(ctx, "UUID"))
                    return SQLDataType.UUID;
                else if (parseKeywordOrIdentifierIf(ctx, "UNIQUEIDENTIFIER"))
                    return SQLDataType.UUID;

                break;

            case 'v':
            case 'V':
                if (parseKeywordOrIdentifierIf(ctx, "VARCHAR") ||
                    parseKeywordOrIdentifierIf(ctx, "VARCHAR2"))
                    return parseDataTypeCollation(ctx, parseDataTypeLength(ctx, SQLDataType.VARCHAR));
                else if (parseKeywordOrIdentifierIf(ctx, "VARBINARY"))
                    return parseDataTypeLength(ctx, SQLDataType.VARBINARY);

                break;
        }

        return new DefaultDataType(ctx.dsl.dialect(), Object.class, parseIdentifier(ctx).toString());
    }

    private static final boolean parseKeywordOrIdentifierIf(ParserContext ctx, String keyword) {
        int position = ctx.position();
        char quoteEnd = parseQuote(ctx, false);
        boolean result = parseKeywordIf(ctx, keyword);

        if (!result)
            ctx.position(position);
        else if (quoteEnd != 0)
            parse(ctx, quoteEnd);

        return result;
    }

    private static final DataType<?> parseUnsigned(ParserContext ctx, DataType result) {
        if (parseKeywordIf(ctx, "UNSIGNED"))
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

    private static final DataType<?> parseAndIgnoreDataTypeLength(ParserContext ctx, DataType<?> result) {
        if (parseIf(ctx, '(')) {
            parseUnsignedInteger(ctx);
            parse(ctx, ')');
        }

        return result;
    }

    private static final DataType<?> parseDataTypeLength(ParserContext ctx, DataType<?> in) {
        DataType<?> result = in;

        if (parseIf(ctx, '(')) {
            if (!parseKeywordIf(ctx, "MAX"))
                result = result.length((int) (long) parseUnsignedInteger(ctx));

            if (in == SQLDataType.VARCHAR || in == SQLDataType.CHAR)
                if (!parseKeywordIf(ctx, "BYTE"))
                    parseKeywordIf(ctx, "CHAR");

            parse(ctx, ')');
        }

        return result;
    }

    private static final DataType<?> parseDataTypeCollation(ParserContext ctx, DataType<?> result) {
        CharacterSet cs = parseCharacterSetSpecificationIf(ctx);
        if (cs != null)
            result = result.characterSet(cs);

        Collation col = parseCollateSpecificationIf(ctx);
        if (col != null)
            result = result.collation(col);

        return result;
    }

    private static final CharacterSet parseCharacterSetSpecificationIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "CHARACTER SET") || parseKeywordIf(ctx, "CHARSET")) {
            parseIf(ctx, '=');
            return parseCharacterSet(ctx);
        }

        return null;
    }

    private static final Collation parseCollateSpecificationIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "COLLATE")) {
            parseIf(ctx, '=');
            return parseCollation(ctx);
        }

        return null;
    }

    private static final DataType<?> parseAndIgnoreDataTypePrecisionScale(ParserContext ctx, DataType<?> result) {
        if (parseIf(ctx, '(')) {
            parseUnsignedInteger(ctx);

            if (parseIf(ctx, ','))
                parseUnsignedInteger(ctx);

            parse(ctx, ')');
        }

        return result;
    }

    private static final Integer parseDataTypePrecision(ParserContext ctx) {
        Integer precision = null;

        if (parseIf(ctx, '(')) {
            precision = (int) (long) parseUnsignedInteger(ctx);
            parse(ctx, ')');
        }

        return precision;
    }

    private static final DataType<?> parseDataTypePrecision(ParserContext ctx, DataType<?> result) {
        if (parseIf(ctx, '(')) {
            int precision = (int) (long) parseUnsignedInteger(ctx);
            result = result.precision(precision);
            parse(ctx, ')');
        }

        return result;
    }

    private static final DataType<?> parseDataTypePrecisionScale(ParserContext ctx, DataType<?> result) {
        if (parseIf(ctx, '(')) {
            int precision = parseIf(ctx, '*') ? 38 : (int) (long) parseUnsignedInteger(ctx);

            if (parseIf(ctx, ','))
                result = result.precision(precision, (int) (long) parseSignedInteger(ctx));
            else
                result = result.precision(precision);

            parse(ctx, ')');
        }

        return result;
    }

    private static final DataType<?> parseDataTypeEnum(ParserContext ctx) {
        parse(ctx, '(');
        List<String> literals = new ArrayList<>();
        int length = 0;

        do {
            String literal = parseStringLiteral(ctx);

            if (literal != null)
                length = Math.max(length, literal.length());

            literals.add(literal);
        }
        while (parseIf(ctx, ','));

        parse(ctx, ')');

        // [#7025] TODO, replace this by a dynamic enum data type encoding, once available
        String className = "GeneratedEnum" + (literals.hashCode() & 0x7FFFFFF);

        StringBuilder content = new StringBuilder();
        content.append(
                    "package org.jooq.impl;\n"
                  + "enum ").append(className).append(" implements org.jooq.EnumType {\n");

        for (int i = 0; i < literals.size(); i++) {
            content.append("  E").append(i).append("(\"").append(literals.get(i).replace("\"", "\\\"")).append("\"),\n");
        }

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


        return SQLDataType.VARCHAR(length)

            .asEnumDataType(Reflect.compile("org.jooq.impl." + className, content.toString()).get())

        ;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Literal parsing
    // -----------------------------------------------------------------------------------------------------------------

    private static final char parseCharacterLiteral(ParserContext ctx) {
        parse(ctx, '\'', false);

        char c = ctx.character();

        // TODO MySQL string escaping...
        if (c == '\'')
            parse(ctx, '\'', false);

        ctx.positionInc();
        parse(ctx, '\'');
        return c;
    }

    private static final Param<?> parseBindVariable(ParserContext ctx) {
        switch (ctx.character()) {
            case '?':
                parse(ctx, '?');
                return DSL.val(ctx.nextBinding(), Object.class);

            case ':':
                parse(ctx, ':', false);
                return DSL.param(parseIdentifier(ctx).last(), ctx.nextBinding());

            default:
                throw ctx.exception("Illegal bind variable character");
        }
    }

    private static final Comment parseComment(ParserContext ctx) {
        return DSL.comment(parseStringLiteral(ctx));
    }

    private static final String parseStringLiteral(ParserContext ctx) {
        String result = parseStringLiteralIf(ctx);

        if (result == null)
            throw ctx.expected("String literal");

        return result;
    }

    private static final String parseStringLiteralIf(ParserContext ctx) {
        if (parseIf(ctx, 'q', '\'', false) || parseIf(ctx, 'Q', '\'', false))
            return parseOracleQuotedStringLiteral(ctx);
        else if (parseIf(ctx, 'e', '\'', false) || parseIf(ctx, 'E', '\'', false))
            return parseUnquotedStringLiteral(ctx, true, '\'');
        else if (peek(ctx, '\''))
            return parseUnquotedStringLiteral(ctx, false, '\'');




        else if (peek(ctx, '$'))
            return parseDollarQuotedStringLiteralIf(ctx);
        else
            return null;
    }

    private static final byte[] parseBinaryLiteralIf(ParserContext ctx) {
        if (parseIf(ctx, "X'", false) || parseIf(ctx, "x'", false)) {
            if (parseIf(ctx, '\''))
                return EMPTY_BYTE;

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            char c1 = 0;
            char c2 = 0;

            do {
                while (ctx.hasMore()) {
                    c1 = ctx.character();

                    if (c1 == ' ')
                        ctx.positionInc();
                    else
                        break;
                }

                c2 = ctx.characterNext();

                if (c1 == '\'')
                    break;
                if (c2 == '\'')
                    throw ctx.exception("Unexpected token: \"'\"");

                try {
                    buffer.write(Integer.parseInt("" + c1 + c2, 16));
                }
                catch (NumberFormatException e) {
                    throw ctx.exception("Illegal character for binary literal");
                }

                ctx.positionInc(2);
            }
            while (ctx.hasMore());

            if (c1 == '\'') {
                ctx.positionInc();
                parseWhitespaceIf(ctx);
                return buffer.toByteArray();
            }

            throw ctx.exception("Binary literal not terminated");
        }

        return null;
    }

    private static final String parseOracleQuotedStringLiteral(ParserContext ctx) {
        parse(ctx, '\'', false);

        char start = ctx.character();
        char end;

        switch (start) {
            case '[' : end = ']'; ctx.positionInc(); break;
            case '{' : end = '}'; ctx.positionInc(); break;
            case '(' : end = ')'; ctx.positionInc(); break;
            case '<' : end = '>'; ctx.positionInc(); break;
            case ' ' :
            case '\t':
            case '\r':
            case '\n': throw ctx.exception("Illegal quote string character");
            default  : end = start; ctx.positionInc(); break;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = ctx.position(); i < ctx.sql.length; i++) {
            char c = ctx.character(i);

            if (c == end)
                if (ctx.character(i + 1) == '\'') {
                    ctx.position(i + 2);
                    parseWhitespaceIf(ctx);
                    return sb.toString();
                }
                else {
                    i++;
                }

            sb.append(c);
        }

        throw ctx.exception("Quoted string literal not terminated");
    }

    private static final String parseDollarQuotedStringLiteralIf(ParserContext ctx) {
        int previous = ctx.position();

        if (!parseIf(ctx, '$'))
            return null;

        int openTokenStart = previous;
        int openTokenEnd = previous;

        int closeTokenStart = -1;
        int closeTokenEnd = -1;

        tokenLoop:
        for (int i = ctx.position(); i < ctx.sql.length; i++) {
            char c = ctx.character(i);

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

        ctx.position(openTokenEnd + 1);

        literalLoop:
        for (int i = ctx.position(); i < ctx.sql.length; i++) {
            char c = ctx.character(i);

            if (c == '$')
                if (closeTokenStart == -1)
                    closeTokenStart = i;
                else if (openTokenEnd - openTokenStart == (closeTokenEnd = i) - closeTokenStart)
                    break literalLoop;
                else
                    closeTokenStart = closeTokenEnd;
            else if (closeTokenStart > -1 && ctx.character(i) != ctx.character(i - (closeTokenStart - openTokenStart)))
                closeTokenStart = -1;
        }

        if (closeTokenEnd != -1) {
            ctx.position(closeTokenEnd + 1);
            return ctx.substring(openTokenEnd + 1, closeTokenStart);
        }

        ctx.position(previous);
        return null;
    }

    private static final String parseUnquotedStringLiteral(ParserContext ctx, boolean postgresEscaping, char delimiter) {
        parse(ctx, delimiter, false);

        StringBuilder sb = new StringBuilder();

        characterLoop:
        for (int i = ctx.position(); i < ctx.sql.length; i++) {
            char c1 = ctx.character(i);

            // TODO MySQL string escaping...
            switch (c1) {
                case '\\': {
                    if (!postgresEscaping)
                        break;

                    i++;
                    char c2 = ctx.character(i);
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
                            char c3 = ctx.character(i + 1);
                            char c4 = ctx.character(i + 2);

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
                                throw ctx.exception("Illegal hexadecimal byte value");

                            break;
                        }

                        // Unicode character value UTF-16
                        case 'u':
                            c1 = (char) Integer.parseInt(new String(ctx.sql, i + 1, 4), 16);
                            i += 4;
                            break;

                        // Unicode character value UTF-32
                        case 'U':
                            sb.appendCodePoint(Integer.parseInt(new String(ctx.sql, i + 1, 8), 16));
                            i += 8;
                            continue characterLoop;

                        default:

                            // Octal byte value
                            if (Character.digit(c2, 8) != -1) {
                                char c3 = ctx.character(i + 1);

                                if (Character.digit(c3, 8) != -1) {
                                    i++;
                                    char c4 = ctx.character(i + 1);

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
                    if (ctx.character(i + 1) != delimiter) {
                        ctx.position(i + 1);
                        parseWhitespaceIf(ctx);
                        return sb.toString();
                    }

                    i++;
                    break;
                }
            }

            sb.append(c1);
        }

        throw ctx.exception("String literal not terminated");
    }

    private static final Field<Number> parseFieldUnsignedNumericLiteral(ParserContext ctx, Sign sign) {
        Field<Number> result = parseFieldUnsignedNumericLiteralIf(ctx, sign);

        if (result == null)
            throw ctx.expected("Unsigned numeric literal");

        return result;
    }

    private static final Field<Number> parseFieldUnsignedNumericLiteralIf(ParserContext ctx, Sign sign) {
        Number r = parseUnsignedNumericLiteralIf(ctx, sign);
        return r == null ? null : inline(r);
    }

    private static final Number parseUnsignedNumericLiteralIf(ParserContext ctx, Sign sign) {
        int position = ctx.position();
        char c;

        for (;;) {
            c = ctx.character();
            if (c >= '0' && c <= '9') {
                ctx.positionInc();
            }
            else
                break;
        }

        if (c == '.') {
            ctx.positionInc();
        }
        else {
            if (position == ctx.position())
                return null;

            String s = ctx.substring(position, ctx.position());
            parseWhitespaceIf(ctx);
            try {
                return sign == Sign.MINUS
                    ? -Long.valueOf(s)
                    : Long.valueOf(s);
            }
            catch (Exception e1) {
                return sign == Sign.MINUS
                    ? new BigInteger(s).negate()
                    : new BigInteger(s);
            }
        }

        for (;;) {
            c = ctx.character();
            if (c >= '0' && c <= '9') {
                ctx.positionInc();
            }
            else
                break;
        }

        if (position == ctx.position())
            return null;

        String s = ctx.substring(position, ctx.position());
        parseWhitespaceIf(ctx);
        return sign == Sign.MINUS
            ? new BigDecimal(s).negate()
            : new BigDecimal(s);
        // TODO add floating point support
    }

    private static final Field<Integer> parseZeroOne(ParserContext ctx) {
        if (parseIf(ctx, '0'))
            return zero();
        else if (parseIf(ctx, '1'))
            return one();
        else
            throw ctx.expected("0 or 1");
    }

    private static final Field<Integer> parseZeroOneDefault(ParserContext ctx) {
        if (parseIf(ctx, '0'))
            return zero();
        else if (parseIf(ctx, '1'))
            return one();
        else if (parseKeywordIf(ctx, "DEFAULT"))
            return defaultValue(INTEGER);
        else
            throw ctx.expected("0 or 1");
    }

    private static final Long parseSignedInteger(ParserContext ctx) {
        Long result = parseSignedIntegerIf(ctx);

        if (result == null)
            throw ctx.expected("Signed integer");

        return result;
    }

    private static final Long parseSignedIntegerIf(ParserContext ctx) {
        Sign sign = parseSign(ctx);
        Long unsigned;

        if (sign == Sign.MINUS)
            unsigned = parseUnsignedInteger(ctx);
        else
            unsigned = parseUnsignedIntegerIf(ctx);

        return unsigned == null
             ? null
             : sign == Sign.MINUS
             ? -unsigned
             : unsigned;
    }

    private static final Param<Long> parseParenthesisedUnsignedIntegerOrBindVariable(ParserContext ctx) {
        Param<Long> result;

        int parens;
        for (parens = 0; parseIf(ctx, '('); parens++);
        result = parseUnsignedIntegerOrBindVariable(ctx);
        for (; parens > 0 && parse(ctx, ')'); parens--);

        return result;
    }

    private static final Param<Long> parseUnsignedIntegerOrBindVariable(ParserContext ctx) {
        Long i = parseUnsignedIntegerIf(ctx);
        return i != null ? DSL.inline(i) : (Param<Long>) parseBindVariable(ctx);
    }

    private static final Long parseUnsignedInteger(ParserContext ctx) {
        Long result = parseUnsignedIntegerIf(ctx);

        if (result == null)
            throw ctx.expected("Unsigned integer");

        return result;
    }

    private static final Long parseUnsignedIntegerIf(ParserContext ctx) {
        int position = ctx.position();

        for (;;) {
            char c = ctx.character();

            if (c >= '0' && c <= '9')
                ctx.positionInc();
            else
                break;
        }

        if (position == ctx.position())
            return null;

        String s = ctx.substring(position, ctx.position());
        parseWhitespaceIf(ctx);
        return Long.valueOf(s);
    }

    private static final JoinType parseJoinTypeIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "CROSS")) {
            if (parseKeywordIf(ctx, "JOIN"))
                return JoinType.CROSS_JOIN;
            else if (parseKeywordIf(ctx, "APPLY"))
                return JoinType.CROSS_APPLY;
        }
        else if (parseKeywordIf(ctx, "INNER") && parseKeyword(ctx, "JOIN"))
            return JoinType.JOIN;
        else if (parseKeywordIf(ctx, "JOIN"))
            return JoinType.JOIN;
        else if (parseKeywordIf(ctx, "LEFT")) {
            if (parseKeywordIf(ctx, "SEMI") && parseKeyword(ctx, "JOIN"))
                return JoinType.LEFT_SEMI_JOIN;
            else if (parseKeywordIf(ctx, "ANTI") && parseKeyword(ctx, "JOIN"))
                return JoinType.LEFT_ANTI_JOIN;
            else if ((parseKeywordIf(ctx, "OUTER") || true) && parseKeyword(ctx, "JOIN"))
                return JoinType.LEFT_OUTER_JOIN;
        }
        else if (parseKeywordIf(ctx, "RIGHT") && (parseKeywordIf(ctx, "OUTER") || true) && parseKeyword(ctx, "JOIN"))
            return JoinType.RIGHT_OUTER_JOIN;
        else if (parseKeywordIf(ctx, "FULL") && (parseKeywordIf(ctx, "OUTER") || true) && parseKeyword(ctx, "JOIN"))
            return JoinType.FULL_OUTER_JOIN;
        else if (parseKeywordIf(ctx, "OUTER APPLY"))
            return JoinType.OUTER_APPLY;
        else if (parseKeywordIf(ctx, "NATURAL")) {
            if (parseKeywordIf(ctx, "LEFT") && (parseKeywordIf(ctx, "OUTER") || true) && parseKeyword(ctx, "JOIN"))
                return JoinType.NATURAL_LEFT_OUTER_JOIN;
            else if (parseKeywordIf(ctx, "RIGHT") && (parseKeywordIf(ctx, "OUTER") || true) && parseKeyword(ctx, "JOIN"))
                return JoinType.NATURAL_RIGHT_OUTER_JOIN;
            else if (parseKeywordIf(ctx, "FULL") && (parseKeywordIf(ctx, "OUTER") || true) && parseKeyword(ctx, "JOIN"))
                return JoinType.NATURAL_FULL_OUTER_JOIN;
            else if ((parseKeywordIf(ctx, "INNER") || true) && parseKeyword(ctx, "JOIN"))
                return JoinType.NATURAL_JOIN;
        }
        else if (parseKeywordIf(ctx, "STRAIGHT_JOIN"))
            return JoinType.STRAIGHT_JOIN;

        return null;
        // TODO partitioned join
    }

    private static final TruthValue parseTruthValueIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "TRUE"))
            return TruthValue.TRUE;
        else if (parseKeywordIf(ctx, "FALSE"))
            return TruthValue.FALSE;
        else if (parseKeywordIf(ctx, "NULL"))
            return TruthValue.NULL;

        return null;
    }

    private static final CombineOperator parseCombineOperatorIf(ParserContext ctx, boolean intersectOnly) {
        if (!intersectOnly && parseKeywordIf(ctx, "UNION"))
            if (parseKeywordIf(ctx, "ALL"))
                return CombineOperator.UNION_ALL;
            else if (parseKeywordIf(ctx, "DISTINCT"))
                return CombineOperator.UNION;
            else
                return CombineOperator.UNION;
        else if (!intersectOnly && (parseKeywordIf(ctx, "EXCEPT") || parseKeywordIf(ctx, "MINUS")))
            if (parseKeywordIf(ctx, "ALL"))
                return CombineOperator.EXCEPT_ALL;
            else if (parseKeywordIf(ctx, "DISTINCT"))
                return CombineOperator.EXCEPT;
            else
                return CombineOperator.EXCEPT;
        else if (intersectOnly && parseKeywordIf(ctx, "INTERSECT"))
            if (parseKeywordIf(ctx, "ALL"))
                return CombineOperator.INTERSECT_ALL;
            else if (parseKeywordIf(ctx, "DISTINCT"))
                return CombineOperator.INTERSECT;
            else
                return CombineOperator.INTERSECT;

        return null;
    }

    private static final ComputationalOperation parseComputationalOperationIf(ParserContext ctx) {
        if (parseFunctionNameIf(ctx, "AVG"))
            return ComputationalOperation.AVG;
        else if (parseFunctionNameIf(ctx, "MAX"))
            return ComputationalOperation.MAX;
        else if (parseFunctionNameIf(ctx, "MIN"))
            return ComputationalOperation.MIN;
        else if (parseFunctionNameIf(ctx, "SUM"))
            return ComputationalOperation.SUM;
        else if (parseFunctionNameIf(ctx, "PRODUCT"))
            return ComputationalOperation.PRODUCT;
        else if (parseFunctionNameIf(ctx, "MEDIAN"))
            return ComputationalOperation.MEDIAN;
        else if (parseFunctionNameIf(ctx, "EVERY") || parseFunctionNameIf(ctx, "BOOL_AND"))
            return ComputationalOperation.EVERY;
        else if (parseFunctionNameIf(ctx, "ANY") || parseFunctionNameIf(ctx, "SOME") || parseFunctionNameIf(ctx, "BOOL_OR"))
            return ComputationalOperation.ANY;
        else if (parseFunctionNameIf(ctx, "STDDEV_POP") || parseFunctionNameIf(ctx, "STDEVP"))
            return ComputationalOperation.STDDEV_POP;
        else if (parseFunctionNameIf(ctx, "STDDEV_SAMP") || parseFunctionNameIf(ctx, "STDEV"))
            return ComputationalOperation.STDDEV_SAMP;
        else if (parseFunctionNameIf(ctx, "VAR_POP"))
            return ComputationalOperation.VAR_POP;
        else if (parseFunctionNameIf(ctx, "VAR_SAMP"))
            return ComputationalOperation.VAR_SAMP;

        return null;
    }

    private static final BinarySetFunctionType parseBinarySetFunctionTypeIf(ParserContext ctx) {

        // TODO speed this up
        for (BinarySetFunctionType type : BinarySetFunctionType.values())
            if (parseFunctionNameIf(ctx, type.name()))
                return type;

        return null;
    }

    private static final Comparator parseComparatorIf(ParserContext ctx) {
        if (parseIf(ctx, "="))
            return Comparator.EQUALS;
        else if (parseIf(ctx, "!=") || parseIf(ctx, "<>") || parseIf(ctx, "^="))
            return Comparator.NOT_EQUALS;
        else if (parseIf(ctx, ">="))
            return Comparator.GREATER_OR_EQUAL;
        else if (parseIf(ctx, ">"))
            return Comparator.GREATER;

        // MySQL DISTINCT operator
        else if (parseIf(ctx, "<=>"))
            return Comparator.IS_NOT_DISTINCT_FROM;
        else if (parseIf(ctx, "<="))
            return Comparator.LESS_OR_EQUAL;
        else if (parseIf(ctx, "<"))
            return Comparator.LESS;

        return null;
    }

    private static enum TSQLOuterJoinComparator {
        LEFT, RIGHT
    }

    private static final TSQLOuterJoinComparator parseTSQLOuterJoinComparatorIf(ParserContext ctx) {
        if (parseIf(ctx, "*="))
            return TSQLOuterJoinComparator.LEFT;
        else if (parseIf(ctx, "=*"))
            return TSQLOuterJoinComparator.RIGHT;
        else
            return null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Other tokens
    // -----------------------------------------------------------------------------------------------------------------

    private static final String parseUntilEOL(ParserContext ctx) {
        String result = parseUntilEOLIf(ctx);

        if (result == null)
            throw ctx.expected("Content before EOL");

        return result;
    }

    private static final String parseUntilEOLIf(ParserContext ctx) {
        int start = ctx.position();
        int stop = start;

        for (; stop < ctx.sql.length; stop++) {
            char c = ctx.character(stop);

            if (c == '\r') {
                if (ctx.character(stop + 1) == '\n')
                    stop++;

                break;
            }
            else if (c == '\n')
                break;
        }

        if (start == stop)
            return null;

        ctx.position(stop);
        parseWhitespaceIf(ctx);
        return ctx.substring(start, stop);
    }

    private static final boolean parse(ParserContext ctx, String string) {
        boolean result = parseIf(ctx, string);

        if (!result)
            throw ctx.expected(string);

        return result;
    }

    private static final boolean parseIf(ParserContext ctx, String string) {
        return parseIf(ctx, string, true);
    }

    private static final boolean parseIf(ParserContext ctx, String string, boolean skipAfterWhitespace) {
        boolean result = peek(ctx, string);

        if (result) {
            ctx.positionInc(string.length());

            if (skipAfterWhitespace)
                parseWhitespaceIf(ctx);
        }

        return result;
    }

    private static final boolean parse(ParserContext ctx, char c) {
        return parse(ctx, c, true);
    }

    private static final boolean parse(ParserContext ctx, char c, boolean skipAfterWhitespace) {
        if (!parseIf(ctx, c, skipAfterWhitespace))
            throw ctx.expected("Token '" + c + "'");

        return true;
    }

    private static final boolean parseIf(ParserContext ctx, char c) {
        return parseIf(ctx, c, true);
    }

    private static final boolean parseIf(ParserContext ctx, char c, boolean skipAfterWhitespace) {
        boolean result = peek(ctx, c);

        if (result) {
            ctx.positionInc();

            if (skipAfterWhitespace)
                parseWhitespaceIf(ctx);
        }

        return result;
    }

    private static final boolean parseIf(ParserContext ctx, char c, char peek, boolean skipAfterWhitespace) {
        if (ctx.character() != c)
            return false;

        if (ctx.characterNext() != peek)
            return false;

        ctx.positionInc();

        if (skipAfterWhitespace)
            parseWhitespaceIf(ctx);

        return true;
    }

    private static final boolean parseFunctionNameIf(ParserContext ctx, String string) {
        return peekKeyword(ctx, string, true, false, true);
    }

    private static final boolean parseOperator(ParserContext ctx, String operator) {
        if (!parseOperatorIf(ctx, operator))
            throw ctx.expected("Operator '" + operator + "'");

        return true;
    }

    private static final boolean parseOperatorIf(ParserContext ctx, String operator) {
        return peekOperator(ctx, operator, true);
    }

    private static final boolean peekOperator(ParserContext ctx, String operator) {
        return peekOperator(ctx, operator, false);
    }

    private static final boolean peekOperator(ParserContext ctx, String operator, boolean updatePosition) {
        int length = operator.length();
        int position = ctx.position();

        if (ctx.sql.length < position + length)
            return false;

        int pos = afterWhitespace(ctx, position, false);

        for (int i = 0; i < length; i++, pos++)
            if (ctx.sql[pos] != operator.charAt(i))
                return false;

        // [#9888] An operator that is followed by a special character is very likely another, more complex operator
        if (ctx.isOperatorPart(pos))
            return false;

        if (updatePosition) {
            ctx.position(pos);
            parseWhitespaceIf(ctx);
        }

        return true;
    }

    private static final boolean parseKeyword(ParserContext ctx, String keyword) {
        if (!parseKeywordIf(ctx, keyword))
            throw ctx.expected("Keyword '" + keyword + "'");

        return true;
    }

    private static final boolean parseKeywordIf(ParserContext ctx, String keyword) {
        return peekKeyword(ctx, keyword, true, false, false);
    }

    private static final Keyword parseAndGetKeyword(ParserContext ctx, String... keywords) {
        Keyword result = parseAndGetKeywordIf(ctx, keywords);

        if (result == null)
            throw ctx.expected(keywords);

        return result;
    }

    private static final Keyword parseAndGetKeywordIf(ParserContext ctx, String... keywords) {
        for (String keyword : keywords)
            if (parseKeywordIf(ctx, keyword))
                return keyword(keyword.toLowerCase());

        return null;
    }

    private static final Keyword parseAndGetKeywordIf(ParserContext ctx, String keyword) {
        if (parseKeywordIf(ctx, keyword))
            return keyword(keyword.toLowerCase());

        return null;
    }

    private static final boolean peek(ParserContext ctx, char c) {
        if (ctx.character() != c)
            return false;

        return true;
    }

    private static final boolean peek(ParserContext ctx, String string) {
        return peek(ctx, string, ctx.position());
    }

    private static final boolean peek(ParserContext ctx, String string, int position) {
        int length = string.length();

        if (ctx.sql.length < position + length)
            return false;

        for (int i = 0; i < length; i++)
            if (ctx.sql[position + i] != string.charAt(i))
                return false;

        return true;
    }

    private static final boolean peekKeyword(ParserContext ctx, String... keywords) {
        for (String keyword : keywords)
            if (peekKeyword(ctx, keyword))
                return true;

        return false;
    }

    private static final boolean peekKeyword(ParserContext ctx, String keyword) {
        return peekKeyword(ctx, keyword, false, false, false);
    }

    private static final boolean peekKeyword(ParserContext ctx, String keyword, boolean updatePosition, boolean peekIntoParens, boolean requireFunction) {
        int length = keyword.length();
        int position = ctx.position();

        if (ctx.sql.length < position + length)
            return false;

        int skip = afterWhitespace(ctx, position, peekIntoParens) - position;

        for (int i = 0; i < length; i++) {
            char c = keyword.charAt(i);
            int p = position + i + skip;

            switch (c) {
                case ' ':
                    skip = skip + (afterWhitespace(ctx, p) - p - 1);
                    break;

                default:
                    if (upper(ctx.sql[p]) != c)
                        return false;

                    break;
            }
        }

        int pos = position + length + skip;

        // [#8806] A keyword that is followed by a period is very likely an identifier
        if (ctx.isIdentifierPart(pos) || ctx.character(pos) == '.')
            return false;

        if (requireFunction)
            if (ctx.character(afterWhitespace(ctx, pos)) != '(')
                return false;

        if (updatePosition) {
            ctx.positionInc(length + skip);
            parseWhitespaceIf(ctx);
        }

        return true;
    }

    private static final boolean parseWhitespaceIf(ParserContext ctx) {
        int position = ctx.position();
        ctx.position(afterWhitespace(ctx, position));
        return position != ctx.position();
    }

    private static final int afterWhitespace(ParserContext ctx, int position) {
        return afterWhitespace(ctx, position, false);
    }

    private static final int afterWhitespace(ParserContext ctx, int position, boolean peekIntoParens) {

        // [#8074] The SQL standard and some implementations (e.g. PostgreSQL,
        //         SQL Server) support nesting block comments
        int blockCommentNestLevel = 0;
        PeekIgnoreComment ignoreComment = new PeekIgnoreComment(ctx);

        loop:
        for (int i = position; i < ctx.sql.length; i++) {
            switch (ctx.sql[i]) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    position = i + 1;
                    continue loop;

                case '(':
                    if (peekIntoParens)
                        continue loop;
                    else
                        break loop;

                case '/':
                    if (i + 1 < ctx.sql.length && ctx.sql[i + 1] == '*') {
                        i = i + 2;
                        blockCommentNestLevel++;

                        while (i < ctx.sql.length) {
                            if (!peekIgnoreComment(ctx, ignoreComment, i).ignoreComment) {
                                switch (ctx.sql[i]) {
                                    case '/':
                                        if (i + 1 < ctx.sql.length && ctx.sql[i + 1] == '*') {
                                            i = i + 2;
                                            blockCommentNestLevel++;
                                        }

                                        break;

                                    case '+':
                                        if (!ctx.ignoreHints() && i + 1 < ctx.sql.length && ((ctx.sql[i + 1] >= 'A' && ctx.sql[i + 1] <= 'Z') || (ctx.sql[i + 1] >= 'a' && ctx.sql[i + 1] <= 'z'))) {
                                            blockCommentNestLevel = 0;
                                            break loop;
                                        }

                                        break;

                                    case '*':
                                        if (i + 1 < ctx.sql.length && ctx.sql[i + 1] == '/') {
                                            position = (i = i + 1) + 1;

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
                    else if (i + 1 < ctx.sql.length && ctx.sql[i + 1] == '/') {
                        i = i + 2;

                        while (i < ctx.sql.length) {
                            if (!peekIgnoreComment(ctx, ignoreComment, i).ignoreComment) {
                                switch (ctx.sql[i]) {
                                    case '\r':
                                    case '\n':
                                        position = i + 1;
                                        continue loop;
                                }
                            }

                            i++;
                        }

                        position = i;
                    }

                    break loop;

                case '-':
                    if (i + 1 < ctx.sql.length && ctx.sql[i + 1] == '-') {
                        i = i + 2;

                        while (i < ctx.sql.length) {
                            if (!peekIgnoreComment(ctx, ignoreComment, i).ignoreComment) {
                                switch (ctx.sql[i]) {
                                    case '\r':
                                    case '\n':
                                        position = i + 1;
                                        continue loop;
                                }
                            }

                            i++;
                        }

                        position = i;
                    }

                    break loop;

                    // TODO MySQL comments require a whitespace after --. Should we deal with this?
                    // TODO Some databases also support # as a single line comment character.

                default:
                    position = i;
                    break loop;
            }
        }

        if (blockCommentNestLevel > 0)
            throw ctx.exception("Nested block comment not properly closed");

        return position;
    }

    private static final class PeekIgnoreComment {
        boolean ignoreComment;
        final String ignoreCommentStart;
        final String ignoreCommentStop;
        final boolean checkIgnoreComment;

        PeekIgnoreComment(ParserContext ctx) {
            this.ignoreComment = false;
            this.ignoreCommentStart = ctx.settings().getParseIgnoreCommentStart();
            this.ignoreCommentStop = ctx.settings().getParseIgnoreCommentStop();
            this.checkIgnoreComment = !FALSE.equals(ctx.settings().isParseIgnoreComments());
        }
    }

    private static final PeekIgnoreComment peekIgnoreComment(
        ParserContext ctx,
        PeekIgnoreComment param,
        int i
    ) {

        if (param.checkIgnoreComment)
            if (!param.ignoreComment)
                param.ignoreComment = peek(ctx, param.ignoreCommentStart, i);
            else
                param.ignoreComment = !peek(ctx, param.ignoreCommentStop, i);

        return param;
    }

    private static final char upper(char c) {
        return c >= 'a' && c <= 'z' ? (char) (c - ('a' - 'A')) : c;
    }

    private static enum TruthValue {
        TRUE,
        FALSE,
        NULL;
    }

    private static enum ComputationalOperation {
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
        VAR_SAMP,
        VAR_POP,
        MEDIAN,
//        COLLECT,
//        FUSION,
//        INTERSECTION;
    }

    private static enum BinarySetFunctionType {
//        COVAR_POP,
//        COVAR_SAMP,
//        CORR,
        REGR_SLOPE,
        REGR_INTERCEPT,
        REGR_COUNT,
        REGR_R2,
        REGR_AVGX,
        REGR_AVGY,
        REGR_SXX,
        REGR_SYY,
        REGR_SXY,
    }

    private static final String[] KEYWORDS_IN_SELECT  = {
        "CONNECT BY",
        "END", // In T-SQL, semicolons are optional, so a T-SQL END clause might appear
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
        "GO", // The T-SQL statement batch delimiter, not a SELECT keyword
        "GROUP BY",
        "HAVING",
        "INTERSECT",
        "INTO",
        "LIMIT",
        "MINUS",
        "OFFSET",
        "ORDER BY",
        "PARTITION BY",
        "QUALIFY",
        "RETURNING",
        "START WITH",
        "UNION",
        "WHERE",
        "WINDOW",
        "WITH",
    };

    private static final String[] KEYWORDS_IN_FROM    = {
        "CONNECT BY",
        "CROSS APPLY",
        "CROSS JOIN",
        "END", // In T-SQL, semicolons are optional, so a T-SQL END clause might appear
        "EXCEPT",
        "FETCH FIRST",
        "FETCH NEXT",
        "FOR JSON",
        "FOR KEY SHARE",
        "FOR NO KEY UPDATE",
        "FOR SHARE",
        "FOR UPDATE",
        "FOR XML",
        "FULL JOIN",
        "FULL OUTER JOIN",
        "GO", // The T-SQL statement batch delimiter, not a SELECT keyword
        "GROUP BY",
        "HAVING",
        "INNER JOIN",
        "INTERSECT",
        "INTO",
        "JOIN",
        "LEFT ANTI JOIN",
        "LEFT JOIN",
        "LEFT OUTER JOIN",
        "LEFT SEMI JOIN",
        "LIMIT",
        "MINUS",
        "NATURAL JOIN",
        "NATURAL INNER JOIN",
        "NATURAL LEFT JOIN",
        "NATURAL LEFT OUTER JOIN",
        "NATURAL RIGHT JOIN",
        "NATURAL RIGHT OUTER JOIN",
        "NATURAL FULL JOIN",
        "NATURAL FULL OUTER JOIN",
        "OFFSET",
        "ON",
        "ORDER BY",
        "OUTER APPLY",
        "PARTITION BY",
        "QUALIFY",
        "RETURNING",
        "RIGHT ANTI JOIN",
        "RIGHT JOIN",
        "RIGHT OUTER JOIN",
        "RIGHT SEMI JOIN",
        "SELECT",
        "START WITH",
        "STRAIGHT_JOIN",
        "UNION",
        "USING",
        "WHERE",
        "WINDOW",
        "WITH",
    };

    private static final String[] PIVOT_KEYWORDS      = {
        "FOR"
    };

    private static final DDLQuery IGNORE              = Reflect.on(DSL.query("/* ignored */")).as(DDLQuery.class, QueryPartInternal.class);
    private static final Query    IGNORE_NO_DELIMITER = Reflect.on(DSL.query("/* ignored */")).as(Query.class, QueryPartInternal.class);
}

final class ParserContext {
    private static final boolean          PRO_EDITION     = false ;

    final DSLContext                      dsl;
    final Locale                          locale;
    final Meta                            meta;
    final char[]                          sql;
    private final ParseWithMetaLookups    metaLookups;
    private boolean                       metaLookupsForceIgnore;
    private int                           position        = 0;
    private boolean                       ignoreHints     = true;
    private final Object[]                bindings;
    private int                           bindIndex       = 0;
    private String                        delimiter       = ";";





    ParserContext(
        DSLContext dsl,
        Meta meta,
        ParseWithMetaLookups metaLookups,
        String sqlString,
        Object[] bindings
    ) {
        this.dsl = dsl;
        this.locale = parseLocale(dsl.settings());
        this.meta = meta;
        this.metaLookups = metaLookups;
        this.sql = sqlString.toCharArray();
        this.bindings = bindings;
    }

    Configuration configuration() {
        return dsl.configuration();
    }

    Settings settings() {
        return configuration().settings();
    }

    SQLDialect dialect() {
        SQLDialect result = settings().getParseDialect();

        if (result == null)
            result = SQLDialect.DEFAULT;

        return result;
    }

    SQLDialect family() {
        return dialect().family();
    }

    boolean metaLookupsForceIgnore() {
        return this.metaLookupsForceIgnore;
    }

    ParserContext metaLookupsForceIgnore(boolean m) {
        this.metaLookupsForceIgnore = m;
        return this;
    }

    boolean requireProEdition() {
        if (!PRO_EDITION)
            throw exception("Feature only supported in pro edition");

        return true;
    }

    boolean requireUnsupportedSyntax() {
        if (dsl.configuration().settings().getParseUnsupportedSyntax() == ParseUnsupportedSyntax.FAIL)
            throw exception("Syntax not supported");

        return true;
    }

    String substring(int startPosition, int endPosition) {
        return new String(sql, startPosition, endPosition - startPosition);
    }

    ParserException internalError() {
        return exception("Internal Error");
    }

    ParserException expected(String object) {
        return init(new ParserException(mark(), object + " expected"));
    }

    ParserException expected(String... objects) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < objects.length; i++)
            if (i == 0)
                sb.append(objects[i]);
            else if (i == objects.length - 1)
                sb.append(", or ").append(objects[i]);
            else
                sb.append(", ").append(objects[i]);

        return init(new ParserException(mark(), sb.toString() + " expected"));
    }

    ParserException notImplemented(String feature) {
        return init(new ParserException(mark(), feature + " not yet implemented"));
    }

    ParserException unsupportedClause() {
        return init(new ParserException(mark(), "Unsupported clause"));
    }

    ParserException exception(String message) {
        return init(new ParserException(mark(), message));
    }

    ParserException init(ParserException e) {
        int[] line = line();
        return e.position(position).line(line[0]).column(line[1]);
    }

    Object nextBinding() {
        if (bindIndex < bindings.length)
            return bindings[bindIndex++];
        else if (bindings.length == 0)
            return null;
        else
            throw exception("No binding provided for bind index " + (bindIndex + 1));
    }

    int[] line() {
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

    char character() {
        return character(position);
    }

    char character(int pos) {
        return pos >= 0 && pos < sql.length ? sql[pos] : ' ';
    }

    char characterNext() {
        return character(position + 1);
    }

    int position() {
        return position;
    }

    void position(int newPosition) {
        position = newPosition;
    }

    void positionInc() {
        positionInc(1);
    }

    void positionInc(int inc) {
        position(position + inc);
    }

    String delimiter() {
        return delimiter;
    }

    void delimiter(String newDelimiter) {
        delimiter = newDelimiter;
    }

    boolean ignoreHints() {
        return ignoreHints;
    }

    void ignoreHints(boolean newIgnoreHints) {
        ignoreHints = newIgnoreHints;
    }

    boolean isWhitespace() {
        return Character.isWhitespace(character());
    }

    boolean isWhitespace(int pos) {
        return Character.isWhitespace(character(pos));
    }

    boolean isOperatorPart() {
        return isOperatorPart(character());
    }

    boolean isOperatorPart(int pos) {
        return isOperatorPart(character(pos));
    }

    boolean isOperatorPart(char character) {
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

    boolean isIdentifierPart() {
        return isIdentifierPart(character());
    }

    boolean isIdentifierPart(int pos) {
        return isIdentifierPart(character(pos));
    }

    boolean isIdentifierPart(char character) {
        return Character.isJavaIdentifierPart(character)
           || ((character == '@'
           ||   character == '#')
           &&   character != delimiter.charAt(0));
    }

    boolean hasMore() {
        return position < sql.length;
    }

    boolean done() {
        return position >= sql.length && (bindings.length == 0 || bindings.length == bindIndex);
    }

    boolean done(String message) {
        if (done())
            return true;
        else
            throw exception(message);
    }

    String mark() {
        int[] line = line();
        return "[" + line[0] + ":" + line[1] + "] "
              + (position > 50 ? "..." : "")
              + substring(Math.max(0, position - 50), position)
              + "[*]"
              + substring(position, Math.min(sql.length, position + 80))
              + (sql.length > position + 80 ? "..." : "");
    }

    Table<?> lookupTable(Name name) {
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

        if (!metaLookupsForceIgnore && metaLookups == THROW_ON_FAILURE)
            throw exception("Unknown table identifier");

        return table(name);
    }

    Field<?> lookupField(Name name) {
        if (meta != null) {
            List<Table<?>> tables = meta.getTables(name.qualifier());

            if (tables.size() == 1) {
                Field<?> field = tables.get(0).field(name);

                if (field != null)
                    return field;
            }
        }

        if (!metaLookupsForceIgnore && metaLookups == THROW_ON_FAILURE)
            throw exception("Unknown field identifier");

        return field(name);
    }

    @Override
    public String toString() {
        return mark();
    }
}