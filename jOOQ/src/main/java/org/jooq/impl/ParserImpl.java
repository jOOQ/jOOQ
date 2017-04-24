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

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.jooq.impl.DSL.acos;
import static org.jooq.impl.DSL.ascii;
import static org.jooq.impl.DSL.asin;
import static org.jooq.impl.DSL.atan;
import static org.jooq.impl.DSL.atan2;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.avgDistinct;
import static org.jooq.impl.DSL.bitLength;
import static org.jooq.impl.DSL.boolOr;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.ceil;
import static org.jooq.impl.DSL.charLength;
import static org.jooq.impl.DSL.check;
import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.constraint;
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
import static org.jooq.impl.DSL.deg;
import static org.jooq.impl.DSL.denseRank;
import static org.jooq.impl.DSL.every;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.exp;
import static org.jooq.impl.DSL.extract;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.firstValue;
import static org.jooq.impl.DSL.floor;
import static org.jooq.impl.DSL.foreignKey;
import static org.jooq.impl.DSL.greatest;
import static org.jooq.impl.DSL.grouping;
import static org.jooq.impl.DSL.groupingId;
import static org.jooq.impl.DSL.groupingSets;
import static org.jooq.impl.DSL.hour;
import static org.jooq.impl.DSL.ifnull;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.isnull;
import static org.jooq.impl.DSL.lag;
import static org.jooq.impl.DSL.lastValue;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.lead;
import static org.jooq.impl.DSL.least;
import static org.jooq.impl.DSL.left;
import static org.jooq.impl.DSL.length;
import static org.jooq.impl.DSL.level;
import static org.jooq.impl.DSL.listAgg;
import static org.jooq.impl.DSL.ln;
import static org.jooq.impl.DSL.log;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.lpad;
import static org.jooq.impl.DSL.ltrim;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.maxDistinct;
import static org.jooq.impl.DSL.md5;
import static org.jooq.impl.DSL.median;
import static org.jooq.impl.DSL.mid;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.minDistinct;
import static org.jooq.impl.DSL.minute;
import static org.jooq.impl.DSL.mode;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.nthValue;
import static org.jooq.impl.DSL.ntile;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.nvl2;
import static org.jooq.impl.DSL.octetLength;
import static org.jooq.impl.DSL.orderBy;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.percentRank;
import static org.jooq.impl.DSL.percentileCont;
import static org.jooq.impl.DSL.percentileDisc;
import static org.jooq.impl.DSL.position;
import static org.jooq.impl.DSL.primaryKey;
import static org.jooq.impl.DSL.prior;
import static org.jooq.impl.DSL.rad;
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
import static org.jooq.impl.DSL.regrAvgX;
import static org.jooq.impl.DSL.regrAvgY;
import static org.jooq.impl.DSL.regrCount;
import static org.jooq.impl.DSL.regrIntercept;
import static org.jooq.impl.DSL.regrR2;
import static org.jooq.impl.DSL.regrSXX;
import static org.jooq.impl.DSL.regrSXY;
import static org.jooq.impl.DSL.regrSYY;
import static org.jooq.impl.DSL.regrSlope;
import static org.jooq.impl.DSL.repeat;
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
import static org.jooq.impl.DSL.sign;
import static org.jooq.impl.DSL.sin;
import static org.jooq.impl.DSL.sinh;
import static org.jooq.impl.DSL.space;
import static org.jooq.impl.DSL.sqrt;
import static org.jooq.impl.DSL.stddevPop;
import static org.jooq.impl.DSL.stddevSamp;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.sumDistinct;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.tan;
import static org.jooq.impl.DSL.tanh;
import static org.jooq.impl.DSL.time;
import static org.jooq.impl.DSL.timestamp;
import static org.jooq.impl.DSL.trim;
import static org.jooq.impl.DSL.unique;
import static org.jooq.impl.DSL.values;
import static org.jooq.impl.DSL.varPop;
import static org.jooq.impl.DSL.varSamp;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.DSL.year;
import static org.jooq.impl.ParserImpl.Type.A;
import static org.jooq.impl.ParserImpl.Type.B;
import static org.jooq.impl.ParserImpl.Type.D;
import static org.jooq.impl.ParserImpl.Type.N;
import static org.jooq.impl.ParserImpl.Type.S;
import static org.jooq.impl.ParserImpl.Type.X;
import static org.jooq.impl.Tools.EMPTY_BYTE;
import static org.jooq.impl.Tools.EMPTY_COLLECTION;
import static org.jooq.impl.Tools.EMPTY_COMMON_TABLE_EXPRESSION;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.EMPTY_NAME;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.jooq.AggregateFilterStep;
import org.jooq.AggregateFunction;
import org.jooq.AlterIndexFinalStep;
import org.jooq.AlterIndexStep;
import org.jooq.AlterSchemaFinalStep;
import org.jooq.AlterSchemaStep;
import org.jooq.AlterSequenceStep;
import org.jooq.AlterTableDropStep;
import org.jooq.AlterTableFinalStep;
import org.jooq.AlterTableStep;
import org.jooq.CaseConditionStep;
import org.jooq.CaseValueStep;
import org.jooq.CaseWhenStep;
import org.jooq.CommonTableExpression;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Constraint;
import org.jooq.ConstraintForeignKeyOnStep;
import org.jooq.ConstraintTypeStep;
import org.jooq.CreateIndexFinalStep;
import org.jooq.CreateIndexStep;
import org.jooq.CreateIndexWhereStep;
import org.jooq.CreateTableAsStep;
import org.jooq.CreateTableColumnStep;
import org.jooq.CreateTableConstraintStep;
import org.jooq.CreateTableFinalStep;
import org.jooq.DDLQuery;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.DatePart;
import org.jooq.Delete;
import org.jooq.DeleteReturningStep;
import org.jooq.DeleteWhereStep;
import org.jooq.DerivedColumnList;
import org.jooq.DropIndexFinalStep;
import org.jooq.DropIndexOnStep;
import org.jooq.DropSchemaFinalStep;
import org.jooq.DropSchemaStep;
import org.jooq.DropTableFinalStep;
import org.jooq.DropTableStep;
import org.jooq.DropViewFinalStep;
import org.jooq.Field;
import org.jooq.FieldOrRow;
import org.jooq.GroupField;
import org.jooq.Insert;
import org.jooq.InsertOnConflictDoUpdateStep;
import org.jooq.InsertOnConflictWhereStep;
import org.jooq.InsertOnDuplicateStep;
import org.jooq.InsertReturningStep;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStepN;
import org.jooq.JoinType;
import org.jooq.Merge;
import org.jooq.MergeFinalStep;
import org.jooq.MergeMatchedStep;
import org.jooq.MergeNotMatchedStep;
import org.jooq.Name;
import org.jooq.OrderedAggregateFunction;
import org.jooq.OrderedAggregateFunctionOfDeferredType;
import org.jooq.Param;
import org.jooq.Parser;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.Row2;
import org.jooq.RowN;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Sequence;
import org.jooq.SortField;
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
import org.jooq.UpdateReturningStep;
// ...
import org.jooq.WindowBeforeOverStep;
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowOverStep;
import org.jooq.WindowSpecification;
import org.jooq.WindowSpecificationOrderByStep;
import org.jooq.WindowSpecificationRowsAndStep;
import org.jooq.WindowSpecificationRowsStep;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
class ParserImpl implements Parser {

    private final DSLContext    dsl;

    ParserImpl(Configuration configuration) {
        this.dsl = DSL.using(configuration);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Top level parsing
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public final Queries parse(String sql) {
        ParserContext ctx = new ParserContext(dsl, sql);
        List<Query> result = new ArrayList<Query>();
        do {
            Query query = parseQuery(ctx);
            if (query != null)
                result.add(query);
        }
        while (parseIf(ctx, ";"));

        if (!ctx.done())
            throw ctx.exception("Unexpected content after end of queries input");

        return new QueriesImpl(dsl.configuration(), result);
    }

    @Override
    public final Query parseQuery(String sql) {
        ParserContext ctx = new ParserContext(dsl, sql);
        Query result = parseQuery(ctx);

        if (!ctx.done())
            throw ctx.exception("Unexpected content after end of query input");

        return result;
    }

    @Override
    public final Table<?> parseTable(String sql) {
        ParserContext ctx = new ParserContext(dsl, sql);
        Table<?> result = parseTable(ctx);

        if (!ctx.done())
            throw ctx.exception("Unexpected content after end of table input");

        return result;
    }

    @Override
    public final Field<?> parseField(String sql) {
        ParserContext ctx = new ParserContext(dsl, sql);
        Field<?> result = parseField(ctx);

        if (!ctx.done())
            throw ctx.exception("Unexpected content after end of field input");

        return result;
    }

    @Override
    public final Row parseRow(String sql) {
        ParserContext ctx = new ParserContext(dsl, sql);
        RowN result = parseRow(ctx);

        if (!ctx.done())
            throw ctx.exception("Unexpected content after end of row input");

        return result;
    }

    @Override
    public final Condition parseCondition(String sql) {
        ParserContext ctx = new ParserContext(dsl, sql);
        Condition result = parseCondition(ctx);

        if (!ctx.done())
            throw ctx.exception("Unexpected content after end of condition input");

        return result;
    }

    @Override
    public final Name parseName(String sql) {
        ParserContext ctx = new ParserContext(dsl, sql);
        Name result = parseName(ctx);

        if (!ctx.done())
            throw ctx.exception("Unexpected content after end of name input");

        return result;
    }

    private static final Query parseQuery(ParserContext ctx) {
        if (ctx.done())
            return null;

        parseWhitespaceIf(ctx);
        try {
            switch (ctx.character()) {
                case 'a':
                case 'A':
                    if (peekKeyword(ctx, "ALTER"))
                        return parseAlter(ctx);

                    break;

                case 'c':
                case 'C':
                    if (peekKeyword(ctx, "CREATE"))
                        return parseCreate(ctx);

                    break;

                case 'd':
                case 'D':
                    if (peekKeyword(ctx, "DELETE"))
                        return parseDelete(ctx);
                    else if (peekKeyword(ctx, "DROP"))
                        return parseDrop(ctx);

                    break;

                case 'i':
                case 'I':
                    if (peekKeyword(ctx, "INSERT"))
                        return parseInsert(ctx);

                    break;

                case 'm':
                case 'M':
                    if (peekKeyword(ctx, "MERGE"))
                        return parseMerge(ctx);

                    break;

                case 'r':
                case 'R':
                    if (peekKeyword(ctx, "RENAME"))
                        return parseRename(ctx);

                    break;

                case 's':
                case 'S':
                    if (peekKeyword(ctx, "SELECT"))
                        return parseSelect(ctx);

                    break;

                case 't':
                case 'T':
                    if (peekKeyword(ctx, "TRUNCATE"))
                        return parseTruncate(ctx);

                    break;

                case 'u':
                case 'U':
                    if (peekKeyword(ctx, "UPDATE"))
                        return parseUpdate(ctx);

                    break;

                case 'v':
                case 'V':
                    if (peekKeyword(ctx, "VALUES"))
                        return ctx.dsl.selectFrom(parseTableValueConstructor(ctx));

                case 'w':
                case 'W':
                    if (peekKeyword(ctx, "WITH"))
                        return parseWith(ctx);

                    break;

                case '(':
                    // TODO are there other possible statement types?
                    return parseSelect(ctx);
                default:
                    break;
            }
        }
        finally {
            parseWhitespaceIf(ctx);
            if (!ctx.done() && ctx.character() != ';')
                throw ctx.unexpectedToken();
        }

        throw ctx.exception("Unsupported query type");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Statement parsing
    // -----------------------------------------------------------------------------------------------------------------

    private static final Query parseWith(ParserContext ctx) {
        parseKeyword(ctx, "WITH");

        List<CommonTableExpression<?>> cte = new ArrayList<CommonTableExpression<?>>();
        do {

            Name table = parseIdentifier(ctx);

            // [#6022] Allow unquoted identifiers for columns
            parse(ctx, '(');
            List<Name> columnNames = parseIdentifiers(ctx);
            String[] columns = new String[columnNames.size()];
            for (int i = 0; i < columns.length; i++)
                columns[i] = columnNames.get(i).last();
            parse(ctx, ')');

            DerivedColumnList dcl = table.fields(columns);
            parseKeyword(ctx, "AS");
            parse(ctx, '(');
            cte.add(dcl.as(parseSelect(ctx)));
            parse(ctx, ')');
        }
        while (parseIf(ctx, ','));

        // TODO Better model API for WITH clause
        return parseSelect(ctx, null, (WithImpl) new WithImpl(ctx.dsl.configuration(), false).with(cte.toArray(EMPTY_COMMON_TABLE_EXPRESSION)));

        // TODO Other statements than SELECT
    }

    private static final SelectQueryImpl<Record> parseSelect(ParserContext ctx) {
        return parseSelect(ctx, null, null);
    }

    private static final SelectQueryImpl<Record> parseSelect(ParserContext ctx, Integer degree) {
        return parseSelect(ctx, degree, null);
    }

    private static final SelectQueryImpl<Record> parseSelect(ParserContext ctx, Integer degree, WithImpl with) {
        SelectQueryImpl<Record> result = parseQueryPrimary(ctx, degree, with);
        CombineOperator combine;
        while ((combine = parseCombineOperatorIf(ctx)) != null) {
            if (degree == null)
                degree = result.getSelect().size();

            switch (combine) {
                case UNION:
                    result = (SelectQueryImpl<Record>) result.union(parseQueryPrimary(ctx, degree));
                    break;
                case UNION_ALL:
                    result = (SelectQueryImpl<Record>) result.unionAll(parseQueryPrimary(ctx, degree));
                    break;
                case EXCEPT:
                    result = (SelectQueryImpl<Record>) result.except(parseQueryPrimary(ctx, degree));
                    break;
                case EXCEPT_ALL:
                    result = (SelectQueryImpl<Record>) result.exceptAll(parseQueryPrimary(ctx, degree));
                    break;
                case INTERSECT:
                    result = (SelectQueryImpl<Record>) result.intersect(parseQueryPrimary(ctx, degree));
                    break;
                case INTERSECT_ALL:
                    result = (SelectQueryImpl<Record>) result.intersectAll(parseQueryPrimary(ctx, degree));
                    break;
                default:
                    ctx.unexpectedToken();
                    break;
            }
        }

        if (parseKeywordIf(ctx, "ORDER"))
            if (parseKeywordIf(ctx, "SIBLINGS BY")) {
                result.addOrderBy(parseSortSpecification(ctx));
                result.setOrderBySiblings(true);
            }
            else if (parseKeywordIf(ctx, "BY"))
                result.addOrderBy(parseSortSpecification(ctx));
            else
                throw ctx.unexpectedToken();

        if (!result.getLimit().isApplicable()) {
            boolean offsetStandard = false;
            boolean offsetPostgres = false;

            if (parseKeywordIf(ctx, "OFFSET")) {
                result.addOffset(inline((int) (long) parseUnsignedInteger(ctx)));

                if (parseKeywordIf(ctx, "ROWS") || parseKeywordIf(ctx, "ROW"))
                    offsetStandard = true;

                // Ingres doesn't have a ROWS keyword after offset
                else if (peekKeyword(ctx, "FETCH"))
                    offsetStandard = true;
                else
                    offsetPostgres = true;
            }

            if (!offsetStandard && parseKeywordIf(ctx, "LIMIT")) {
                Param<Integer> limit = inline((int) (long) parseUnsignedInteger(ctx));

                if (!offsetPostgres && parseIf(ctx, ','))
                    result.addLimit(limit, inline((int) (long) parseUnsignedInteger(ctx)));
                else if (!offsetPostgres && parseKeywordIf(ctx, "OFFSET"))
                    result.addLimit(inline((int) (long) parseUnsignedInteger(ctx)), limit);
                else
                    result.addLimit(limit);
            }
            else if (!offsetPostgres && parseKeywordIf(ctx, "FETCH")) {
                if (!parseKeywordIf(ctx, "FIRST") && !parseKeywordIf(ctx, "NEXT"))
                    throw ctx.unexpectedToken();

                result.addLimit(inline((int) (long) parseUnsignedInteger(ctx)));

                if (!parseKeywordIf(ctx, "ROWS ONLY") && !parseKeywordIf(ctx, "ROW ONLY"))
                    throw ctx.unexpectedToken();
            }
        }

        if (parseKeywordIf(ctx, "FOR")) {
            if (parseKeywordIf(ctx, "SHARE")) {
                result.setForShare(true);
            }
            else if (parseKeywordIf(ctx, "UPDATE")) {
                result.setForUpdate(true);

                if (parseKeywordIf(ctx, "OF"))
                    result.setForUpdateOf(parseFields(ctx));

                if (parseKeywordIf(ctx, "NOWAIT"))
                    result.setForUpdateNoWait();




                else if (parseKeywordIf(ctx, "SKIP LOCKED"))
                    result.setForUpdateSkipLocked();
            }
            else
                throw ctx.unexpectedToken();
        }

        return result;
    }

    private static final SelectQueryImpl<Record> parseQueryPrimary(ParserContext ctx, Integer degree) {
        return parseQueryPrimary(ctx, degree, null);
    }

    private static final SelectQueryImpl<Record> parseQueryPrimary(ParserContext ctx, Integer degree, WithImpl with) {
        if (parseIf(ctx, '(')) {
            SelectQueryImpl<Record> result = parseSelect(ctx, degree, with);
            parse(ctx, ')');
            return result;
        }

        parseKeyword(ctx, "SELECT");
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

        Long limit = null;
        Long offset = null;

        // T-SQL style TOP .. START AT
        if (parseKeywordIf(ctx, "TOP")) {
            limit = parseUnsignedInteger(ctx);

            if (parseKeywordIf(ctx, "START AT"))
                offset = parseUnsignedInteger(ctx);
        }

        // Informix style SKIP .. FIRST
        else if (parseKeywordIf(ctx, "SKIP")) {
            offset = parseUnsignedInteger(ctx);

            if (parseKeywordIf(ctx, "FIRST"))
                limit = parseUnsignedInteger(ctx);
        }
        else if (parseKeywordIf(ctx, "FIRST")) {
            limit = parseUnsignedInteger(ctx);
        }

        List<Field<?>> select = parseSelectList(ctx);
        if (degree != null && select.size() != degree)
            throw ctx.exception("Select list must contain " + degree + " columns. Got: " + select.size());

        Table<?> into = null;
        List<Table<?>> from = null;
        Condition startWith = null;
        Condition connectBy = null;
        boolean connectByNoCycle = false;
        Condition where = null;
        List<GroupField> groupBy = null;
        Condition having = null;

        if (parseKeywordIf(ctx, "INTO"))
            into = parseTableName(ctx);

        if (parseKeywordIf(ctx, "FROM"))
            from = parseTables(ctx);

        // TODO is there a better way?
        if (from != null && from.size() == 1 && from.get(0).getName().equalsIgnoreCase("dual"))
            from = null;

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

        if (parseKeywordIf(ctx, "WHERE"))
            where = parseCondition(ctx);

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
                List<List<Field<?>>> fieldSets = new ArrayList<List<Field<?>>>();
                parse(ctx, '(');
                do {
                    parse(ctx, '(');
                    if (parseIf(ctx, ')')) {
                        fieldSets.add(Collections.<Field<?>>emptyList());
                    }
                    else {
                        fieldSets.add(parseFields(ctx));
                        parse(ctx, ')');
                    }
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

        // TODO support WINDOW

        SelectQueryImpl<Record> result = new SelectQueryImpl<Record>(ctx.dsl.configuration(), with);
        if (distinct)
            result.setDistinct(distinct);

        if (distinctOn != null)
            result.addDistinctOn(distinctOn);

        if (select.size() > 0)
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

        if (limit != null)
            if (offset != null)
                result.addLimit((int) (long) offset, (int) (long) limit);
            else
                result.addLimit((int) (long) limit);

        return result;
    }

    private static final Delete<?> parseDelete(ParserContext ctx) {
        parseKeyword(ctx, "DELETE");
        parseKeywordIf(ctx, "FROM");
        Table<?> tableName = parseTableName(ctx);
        boolean where = parseKeywordIf(ctx, "WHERE");
        Condition condition = where ? parseCondition(ctx) : null;

        DeleteWhereStep<?> s1;
        DeleteReturningStep<?> s2;

        s1 = ctx.dsl.delete(tableName);
        s2 = where
            ? s1.where(condition)
            : s1;

        if (parseKeywordIf(ctx, "RETURNING"))
            if (parseIf(ctx, '*'))
                return s2.returning();
            else
                return s2.returning(parseFields(ctx));
        else
            return s2;
    }

    private static final Insert<?> parseInsert(ParserContext ctx) {
        parseKeyword(ctx, "INSERT INTO");
        Table<?> tableName = parseTableName(ctx);
        Field<?>[] fields = null;

        if (parseIf(ctx, '(')) {
            fields = Tools.fieldsByName(parseIdentifiers(ctx).toArray(EMPTY_NAME));
            parse(ctx, ')');
        }

        InsertOnDuplicateStep<?> onDuplicate;
        InsertReturningStep<?> returning;

        if (parseKeywordIf(ctx, "VALUES")) {
            List<List<Field<?>>> allValues = new ArrayList<List<Field<?>>>();

            do {
                parse(ctx, '(');
                List<Field<?>> values = parseFields(ctx);

                if (fields != null && fields.length != values.size())
                    throw ctx.exception("Insert field size (" + fields.length + ") must match values size (" + values.size() + ")");

                allValues.add(values);
                parse(ctx, ')');
            }
            while (parseIf(ctx, ','));

            InsertSetStep<?> step1 = ctx.dsl.insertInto(tableName);
            InsertValuesStepN<?> step2 = (fields != null)
                ? step1.columns(fields)
                : (InsertValuesStepN<?>) step1;

            for (List<Field<?>> values : allValues)
                step2 = step2.values(values);

            returning = onDuplicate = step2;
        }
        else if (parseKeywordIf(ctx, "SET")) {
            Map<Field<?>, Object> map = parseSetClauseList(ctx);

            returning = onDuplicate =  ctx.dsl.insertInto(tableName).set(map);
        }
        else if (peekKeyword(ctx, "SELECT", false, true)){
            SelectQueryImpl<Record> select = parseSelect(ctx);

            returning = onDuplicate = (fields == null)
                ? ctx.dsl.insertInto(tableName).select(select)
                : ctx.dsl.insertInto(tableName).columns(fields).select(select);
        }
        else if (parseKeywordIf(ctx, "DEFAULT VALUES")) {
            if (fields != null)
                throw ctx.notImplemented("DEFAULT VALUES without INSERT field list");
            else
                returning = onDuplicate = ctx.dsl.insertInto(tableName).defaultValues();
        }
        else
            throw ctx.unexpectedToken();

        if (parseKeywordIf(ctx, "ON")) {
            if (parseKeywordIf(ctx, "DUPLICATE KEY UPDATE SET")) {
                returning = onDuplicate.onDuplicateKeyUpdate().set(parseSetClauseList(ctx));
            }
            else if (parseKeywordIf(ctx, "DUPLICATE KEY IGNORE")) {
                returning = onDuplicate.onDuplicateKeyIgnore();
            }
            else if (parseKeywordIf(ctx, "CONFLICT")) {
                parse(ctx, '(');
                InsertOnConflictDoUpdateStep<?> doUpdate = onDuplicate.onConflict(parseFieldNames(ctx));
                parse(ctx, ')');
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
                    throw ctx.unexpectedToken();
            }
            else
                throw ctx.unexpectedToken();
        }

        if (parseKeywordIf(ctx, "RETURNING"))
            if (parseIf(ctx, '*'))
                return returning.returning();
            else
                return returning.returning(parseFields(ctx));
        else
            return returning;
    }

    private static final Update<?> parseUpdate(ParserContext ctx) {
        parseKeyword(ctx, "UPDATE");
        Table<?> tableName = parseTableName(ctx);
        parseKeyword(ctx, "SET");

        // TODO Row value expression updates
        Map<Field<?>, Object> map = parseSetClauseList(ctx);

        // TODO support FROM
        Condition condition = parseKeywordIf(ctx, "WHERE") ? parseCondition(ctx) : null;

        // TODO support RETURNING
        UpdateReturningStep<?> returning = condition == null
            ? ctx.dsl.update(tableName).set(map)
            : ctx.dsl.update(tableName).set(map).where(condition);

        if (parseKeywordIf(ctx, "RETURNING"))
            if (parseIf(ctx, '*'))
                return returning.returning();
            else
                return returning.returning(parseFields(ctx));
        else
            return returning;
    }

    private static final Map<Field<?>, Object> parseSetClauseList(ParserContext ctx) {
        Map<Field<?>, Object> map = new LinkedHashMap<Field<?>, Object>();

        do {
            Field<?> field = parseFieldName(ctx);

            if (map.containsKey(field))
                throw ctx.exception("Duplicate column in set clause list: " + field);

            parse(ctx, '=');
            Field<?> value = parseField(ctx);
            map.put(field,  value);
        }
        while (parseIf(ctx, ','));

        return map;
    }

    private static final Merge<?> parseMerge(ParserContext ctx) {
        parseKeyword(ctx, "MERGE INTO");
        Table<?> target = parseTableName(ctx);
        parseKeyword(ctx, "USING");
        parse(ctx, '(');
        Select<?> using = parseSelect(ctx);
        TableLike<?> usingTable = using;
        parse(ctx, ')');
        if (parseKeywordIf(ctx, "AS"))
            usingTable = DSL.table(using).as(parseIdentifier(ctx));
        parseKeyword(ctx, "ON");
        Condition on = parseCondition(ctx);
        boolean update = false;
        boolean insert = false;
        Field<?>[] insertColumns = null;
        List<Field<?>> insertValues = null;
        Map<Field<?>, Object> updateSet = null;

        for (;;) {
            if (!update && (update = parseKeywordIf(ctx, "WHEN MATCHED THEN UPDATE SET"))) {
                updateSet = parseSetClauseList(ctx);
            }
            else if (!insert && (insert = parseKeywordIf(ctx, "WHEN NOT MATCHED THEN INSERT"))) {
                parse(ctx, '(');
                insertColumns = Tools.fieldsByName(parseIdentifiers(ctx).toArray(EMPTY_NAME));
                parse(ctx, ')');
                parseKeyword(ctx, "VALUES");
                parse(ctx, '(');
                insertValues = parseFields(ctx);
                parse(ctx, ')');

                if (insertColumns.length != insertValues.size())
                    throw ctx.exception("Insert column size (" + insertColumns.length + ") must match values size (" + insertValues.size() + ")");
            }
            else
                break;
        }

        if (!update && !insert)
            throw ctx.exception("At least one of UPDATE or INSERT clauses is required");

        // TODO support WHERE
        // TODO support multi clause MERGE
        // TODO support DELETE

        MergeMatchedStep<?> s1 = ctx.dsl.mergeInto(target).using(usingTable).on(on);
        MergeNotMatchedStep<?> s2 = update ? s1.whenMatchedThenUpdate().set(updateSet) : s1;
        MergeFinalStep<?> s3 = insert ? s2.whenNotMatchedThenInsert(insertColumns).values(insertValues) : s2;

        return s3;
    }

    private static final DDLQuery parseCreate(ParserContext ctx) {
        parseKeyword(ctx, "CREATE");

        if (parseKeywordIf(ctx, "TABLE"))
            return parseCreateTable(ctx, false);
        else if (parseKeywordIf(ctx, "TEMPORARY TABLE"))
            return parseCreateTable(ctx, true);
        else if (parseKeywordIf(ctx, "GLOBAL TEMPORARY TABLE"))
            return parseCreateTable(ctx, true);
        else if (parseKeywordIf(ctx, "INDEX"))
            return parseCreateIndex(ctx, false);
        else if (parseKeywordIf(ctx, "UNIQUE INDEX"))
            return parseCreateIndex(ctx, true);
        else if (parseKeywordIf(ctx, "SCHEMA"))
            return parseCreateSchema(ctx);
        else if (parseKeywordIf(ctx, "SEQUENCE"))
            return parseCreateSequence(ctx);
        else if (parseKeywordIf(ctx, "VIEW"))
            return parseCreateView(ctx);
        else
            throw ctx.unexpectedToken();
    }

    private static final DDLQuery parseAlter(ParserContext ctx) {
        parseKeyword(ctx, "ALTER");

        if (parseKeywordIf(ctx, "TABLE"))
            return parseAlterTable(ctx);
        else if (parseKeywordIf(ctx, "INDEX"))
            return parseAlterIndex(ctx);
        else if (parseKeywordIf(ctx, "SCHEMA"))
            return parseAlterSchema(ctx);
        else if (parseKeywordIf(ctx, "SEQUENCE"))
            return parseAlterSequence(ctx);
        else if (parseKeywordIf(ctx, "VIEW"))
            return parseAlterView(ctx);
        else
            throw ctx.unexpectedToken();
    }

    private static final DDLQuery parseDrop(ParserContext ctx) {
        parseKeyword(ctx, "DROP");

        if (parseKeywordIf(ctx, "TABLE"))
            return parseDropTable(ctx);
        else if (parseKeywordIf(ctx, "INDEX"))
            return parseDropIndex(ctx);
        else if (parseKeywordIf(ctx, "VIEW"))
            return parseDropView(ctx);
        else if (parseKeywordIf(ctx, "SEQUENCE"))
            return parseDropSequence(ctx);
        else if (parseKeywordIf(ctx, "SCHEMA"))
            return parseDropSchema(ctx);
        else
            throw ctx.unexpectedToken();
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

    // -----------------------------------------------------------------------------------------------------------------
    // Statement clause parsing
    // -----------------------------------------------------------------------------------------------------------------

    private static final DDLQuery parseCreateView(ParserContext ctx) {
        boolean ifNotExists = parseKeywordIf(ctx, "IF NOT EXISTS");
        Table<?> view = parseTableName(ctx);
        Field<?>[] fields = EMPTY_FIELD;

        if (parseIf(ctx, '(')) {
            fields = parseFieldNames(ctx).toArray(fields);
            parse(ctx, ')');
        }

        parseKeyword(ctx, "AS");
        Select<?> select = parseSelect(ctx);

        if (fields.length > 0 && fields.length != select.getSelect().size())
            throw ctx.exception("Select list size (" + select.getSelect().size() + ") must match declared field size (" + fields.length + ")");


        return ifNotExists
            ? ctx.dsl.createViewIfNotExists(view, fields).as(select)
            : ctx.dsl.createView(view, fields).as(select);
    }

    private static final DDLQuery parseAlterView(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Table<?> oldName = parseTableName(ctx);
        parseKeyword(ctx, "RENAME TO");
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

        return ifNotExists
            ? ctx.dsl.createSequenceIfNotExists(schemaName)
            : ctx.dsl.createSequence(schemaName);
    }

    private static final DDLQuery parseAlterSequence(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Sequence<?> sequenceName = parseSequenceName(ctx);

        AlterSequenceStep s1 = ifExists
            ? ctx.dsl.alterSequenceIfExists(sequenceName)
            : ctx.dsl.alterSequence(sequenceName);

        if (parseKeywordIf(ctx, "RENAME TO"))
            return s1.renameTo(parseSequenceName(ctx));
        else if (parseKeywordIf(ctx, "RESTART"))
            if (parseKeywordIf(ctx, "WITH"))
                return s1.restartWith(parseUnsignedInteger(ctx));
            else
                return s1.restart();
        else
            throw ctx.unexpectedToken();
    }

    private static final DDLQuery parseDropSequence(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Sequence<?> sequenceName = parseSequenceName(ctx);

        return ifExists
            ? ctx.dsl.dropSequenceIfExists(sequenceName)
            : ctx.dsl.dropSequence(sequenceName);
    }

    private static final DDLQuery parseCreateTable(ParserContext ctx, boolean temporary) {
        boolean ifNotExists = !temporary && parseKeywordIf(ctx, "IF NOT EXISTS");
        Table<?> tableName = parseTableName(ctx);

        // [#5309] TODO: Move this after the column specification
        if (parseKeywordIf(ctx, "AS")) {
            Select<?> select = parseSelect(ctx);

            CreateTableAsStep<Record> s1 = ifNotExists
                ? ctx.dsl.createTableIfNotExists(tableName)
                : temporary
                    ? ctx.dsl.createTemporaryTable(tableName)
                    : ctx.dsl.createTable(tableName);

            CreateTableFinalStep s2 = s1.as(select);
            return s2;
        }
        else {
            List<Field<?>> fields = new ArrayList<Field<?>>();
            List<Constraint> constraints = new ArrayList<Constraint>();
            boolean primary = false;
            boolean noConstraint = true;

            parse(ctx, '(');
            do {
                Name fieldName = parseIdentifier(ctx);
                DataType<?> type = parseDataType(ctx);

                boolean nullable = false;
                boolean defaultValue = false;
                boolean unique = false;
                boolean identity = type.identity();

                for (;;) {
                    if (!nullable) {
                        if (parseKeywordIf(ctx, "NULL")) {
                            type = type.nullable(true);
                            nullable = true;
                            continue;
                        }
                        else if (parseKeywordIf(ctx, "NOT NULL")) {
                            type = type.nullable(false);
                            nullable = true;
                            continue;
                        }
                    }

                    if (!defaultValue) {
                        if (parseKeywordIf(ctx, "DEFAULT")) {

                            // TODO: Ignored keyword from Oracle
                            parseKeywordIf(ctx, "ON NULL");

                            type = type.defaultValue((Field) toField(ctx, parseConcat(ctx, null)));
                            defaultValue = true;
                            identity = true;
                            continue;
                        }
                        else if (parseKeywordIf(ctx, "GENERATED")) {
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
                                    if (parseKeywordIf(ctx, "START WITH")) {
                                        if (!parseKeywordIf(ctx, "LIMIT VALUE"))
                                            parseUnsignedInteger(ctx);
                                        identityOption = true;
                                        continue;
                                    }
                                    else if (parseKeywordIf(ctx, "INCREMENT BY")
                                          || parseKeywordIf(ctx, "MAXVALUE")
                                          || parseKeywordIf(ctx, "MINVALUE")
                                          || parseKeywordIf(ctx, "CACHE")) {
                                        parseUnsignedInteger(ctx);
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
                                        throw ctx.unexpectedToken();
                                }

                                parse(ctx, ')');
                            }

                            type = type.identity(true);
                            defaultValue = true;
                            identity = true;
                            continue;
                        }
                    }

                    if (!unique) {
                        if (parseKeywordIf(ctx, "PRIMARY KEY")) {
                            constraints.add(primaryKey(fieldName));
                            primary = true;
                            unique = true;
                            continue;
                        }
                        else if (parseKeywordIf(ctx, "UNIQUE")) {
                            constraints.add(unique(fieldName));
                            unique = true;
                            continue;
                        }
                    }

                    if (parseKeywordIf(ctx, "CHECK")) {
                        parse(ctx, '(');
                        constraints.add(check(parseCondition(ctx)));
                        parse(ctx, ')');
                        continue;
                    }

                    if (!identity) {
                        if (parseKeywordIf(ctx, "AUTO_INCREMENT") ||
                            parseKeywordIf(ctx, "AUTOINCREMENT")) {
                            type = type.identity(true);
                            identity = true;
                            continue;
                        }
                    }

                    break;
                }

                fields.add(field(fieldName, type));
            }
            while (parseIf(ctx, ',')
               && (noConstraint =
                      !peekKeyword(ctx, "PRIMARY KEY")
                   && !peekKeyword(ctx, "UNIQUE")
                   && !peekKeyword(ctx, "FOREIGN KEY")
                   && !peekKeyword(ctx, "CHECK")
                   && !peekKeyword(ctx, "CONSTRAINT"))
            );

            if (!noConstraint) {
                do {
                    ConstraintTypeStep constraint = null;

                    if (parseKeywordIf(ctx, "CONSTRAINT"))
                        constraint = constraint(parseIdentifier(ctx));

                    if (parseKeywordIf(ctx, "PRIMARY KEY")) {
                        if (primary) {
                            throw ctx.exception("Duplicate primary key specification");
                        }
                        else {
                            primary = true;
                            parse(ctx, '(');
                            Field<?>[] fieldNames = parseFieldNames(ctx).toArray(EMPTY_FIELD);
                            parse(ctx, ')');

                            constraints.add(constraint == null
                                ? primaryKey(fieldNames)
                                : constraint.primaryKey(fieldNames));
                        }
                    }
                    else if (parseKeywordIf(ctx, "UNIQUE")) {
                        parse(ctx, '(');
                        Field<?>[] fieldNames = parseFieldNames(ctx).toArray(EMPTY_FIELD);
                        parse(ctx, ')');

                        constraints.add(constraint == null
                            ? unique(fieldNames)
                            : constraint.unique(fieldNames));
                    }
                    else if (parseKeywordIf(ctx, "FOREIGN KEY")) {
                        parse(ctx, '(');
                        Field<?>[] referencing = parseFieldNames(ctx).toArray(EMPTY_FIELD);
                        parse(ctx, ')');
                        parseKeyword(ctx, "REFERENCES");
                        Table<?> referencedTable = parseTableName(ctx);
                        parse(ctx, '(');
                        Field<?>[] referencedFields = parseFieldNames(ctx).toArray(EMPTY_FIELD);
                        parse(ctx, ')');

                        if (referencing.length != referencedFields.length)
                            throw ctx.exception("Number of referencing columns (" + referencing.length + ") must match number of referenced columns (" + referencedFields.length + ")");

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
                                    throw ctx.unexpectedToken();
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
                                    throw ctx.unexpectedToken();
                            }
                            else
                                throw ctx.unexpectedToken();
                        }

                        constraints.add(e);
                    }
                    else if (parseKeywordIf(ctx, "CHECK")) {
                        parse(ctx, '(');
                        Condition condition = parseCondition(ctx);
                        parse(ctx, ')');

                        constraints.add(constraint == null
                            ? check(condition)
                            : constraint.check(condition));
                    }
                    else {
                        throw ctx.unexpectedToken();
                    }
                }
                while (parseIf(ctx, ','));
            }

            parse(ctx, ')');

            CreateTableAsStep<Record> s1 = ifNotExists
                ? ctx.dsl.createTableIfNotExists(tableName)
                : temporary
                    ? ctx.dsl.createTemporaryTable(tableName)
                    : ctx.dsl.createTable(tableName);
            CreateTableColumnStep s2 = s1.columns(fields);
            CreateTableConstraintStep s3 = constraints.isEmpty()
                ? s2
                : s2.constraints(constraints);
            CreateTableFinalStep s4 = s3;

            if (temporary && parseKeywordIf(ctx, "ON COMMIT")) {
                if (parseKeywordIf(ctx, "DELETE ROWS"))
                    s4 = s3.onCommitDeleteRows();
                else if (parseKeywordIf(ctx, "DROP"))
                    s4 = s3.onCommitDrop();
                else if (parseKeywordIf(ctx, "PRESERVE ROWS"))
                    s4 = s3.onCommitPreserveRows();
                else
                    throw ctx.unexpectedToken();
            }

            return s4;
        }
    }

    private static final DDLQuery parseAlterTable(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Table<?> tableName = parseTableName(ctx);
        parseWhitespaceIf(ctx);

        AlterTableStep s1 = ifExists
            ? ctx.dsl.alterTableIfExists(tableName)
            : ctx.dsl.alterTable(tableName);

        switch (ctx.character()) {
            case 'a':
            case 'A':
                if (parseKeywordIf(ctx, "ADD")) {
                    ConstraintTypeStep constraint = null;
                    if (parseKeywordIf(ctx, "CONSTRAINT"))
                        constraint = constraint(parseIdentifier(ctx));

                    if (parseKeywordIf(ctx, "PRIMARY KEY")) {
                        parse(ctx, '(');
                        Field<?>[] fieldNames = parseFieldNames(ctx).toArray(EMPTY_FIELD);
                        parse(ctx, ')');

                        return constraint == null
                            ? s1.add(primaryKey(fieldNames))
                            : s1.add(constraint.primaryKey(fieldNames));
                    }
                    else if (parseKeywordIf(ctx, "UNIQUE")) {
                        parse(ctx, '(');
                        Field<?>[] fieldNames = parseFieldNames(ctx).toArray(EMPTY_FIELD);
                        parse(ctx, ')');

                        return constraint == null
                            ? s1.add(unique(fieldNames))
                            : s1.add(constraint.unique(fieldNames));
                    }
                    else if (parseKeywordIf(ctx, "FOREIGN KEY")) {
                        parse(ctx, '(');
                        Field<?>[] referencing = parseFieldNames(ctx).toArray(EMPTY_FIELD);
                        parse(ctx, ')');
                        parseKeyword(ctx, "REFERENCES");
                        Table<?> referencedTable = parseTableName(ctx);
                        parse(ctx, '(');
                        Field<?>[] referencedFields = parseFieldNames(ctx).toArray(EMPTY_FIELD);
                        parse(ctx, ')');

                        if (referencing.length != referencedFields.length)
                            throw ctx.exception("Number of referencing columns must match number of referenced columns");

                        return constraint == null
                            ? s1.add(foreignKey(referencing).references(referencedTable, referencedFields))
                            : s1.add(constraint.foreignKey(referencing).references(referencedTable, referencedFields));
                    }
                    else if (parseKeywordIf(ctx, "CHECK")) {
                        parse(ctx, '(');
                        Condition condition = parseCondition(ctx);
                        parse(ctx, ')');

                        return constraint == null
                            ? s1.add(check(condition))
                            : s1.add(constraint.check(condition));
                    }
                    else if (constraint != null) {
                        throw ctx.unexpectedToken();
                    }
                    else {
                        parseKeywordIf(ctx, "COLUMN");

                        // The below code is taken from CREATE TABLE, with minor modifications as
                        // https://github.com/jOOQ/jOOQ/issues/5317 has not yet been implemented
                        // Once implemented, we might be able to factor out the common logic into
                        // a new parseXXX() method.

                        Name fieldName = parseIdentifier(ctx);
                        DataType type = parseDataType(ctx);

                        boolean nullable = false;
                        boolean defaultValue = false;
                        boolean unique = false;

                        for (;;) {
                            if (!nullable) {
                                if (parseKeywordIf(ctx, "NULL")) {
                                    type = type.nullable(true);
                                    nullable = true;
                                    continue;
                                }
                                else if (parseKeywordIf(ctx, "NOT NULL")) {
                                    type = type.nullable(false);
                                    nullable = true;
                                    continue;
                                }
                            }

                            if (!defaultValue) {
                                if (parseKeywordIf(ctx, "DEFAULT")) {
                                    type = type.defaultValue(toField(ctx, parseConcat(ctx, null)));
                                    defaultValue = true;
                                    continue;
                                }
                            }

                            if (!unique) {
                                if (parseKeywordIf(ctx, "PRIMARY KEY")) {
                                    throw ctx.unexpectedToken();
                                }
                                else if (parseKeywordIf(ctx, "UNIQUE")) {
                                    throw ctx.unexpectedToken();
                                }
                            }

                            if (parseKeywordIf(ctx, "CHECK")) {
                                throw ctx.unexpectedToken();
                            }

                            break;
                        }

                        return s1.add(field(fieldName, type), type);
                    }
                }

                break;

            case 'd':
            case 'D':
                if (parseKeywordIf(ctx, "DROP")) {
                    if (parseKeywordIf(ctx, "COLUMN")) {
                        Field<?> field = parseFieldName(ctx);
                        boolean cascade = parseKeywordIf(ctx, "CASCADE");
                        boolean restrict = !cascade && parseKeywordIf(ctx, "RESTRICT");

                        AlterTableDropStep s2 = s1.dropColumn(field);
                        AlterTableFinalStep s3 =
                              cascade
                            ? s2.cascade()
                            : restrict
                            ? s2.restrict()
                            : s2;
                        return s3;
                    }
                    else if (parseKeywordIf(ctx, "CONSTRAINT")) {
                        Name constraint = parseIdentifier(ctx);

                        return s1.dropConstraint(constraint);
                    }
                }

                break;

            case 'r':
            case 'R':
                if (parseKeywordIf(ctx, "RENAME")) {
                    if (parseKeywordIf(ctx, "TO")) {
                        Name newName = parseIdentifier(ctx);

                        return s1.renameTo(newName);
                    }
                    else if (parseKeywordIf(ctx, "COLUMN")) {
                        Name oldName = parseIdentifier(ctx);
                        parseKeyword(ctx, "TO");
                        Name newName = parseIdentifier(ctx);

                        return s1.renameColumn(oldName).to(newName);
                    }
                    else if (parseKeywordIf(ctx, "CONSTRAINT")) {
                        Name oldName = parseIdentifier(ctx);
                        parseKeyword(ctx, "TO");
                        Name newName = parseIdentifier(ctx);

                        return s1.renameConstraint(oldName).to(newName);
                    }
                }

                break;
        }

        throw ctx.unexpectedToken();
    }

    private static final DDLQuery parseRename(ParserContext ctx) {
        parseKeyword(ctx, "RENAME");
        parseWhitespaceIf(ctx);

        switch (ctx.character()) {
            case 'c':
            case 'C':
                if (parseKeywordIf(ctx, "COLUMN")) {
                    TableField<?, ?> oldName = parseFieldName(ctx);
                    parseKeyword(ctx, "TO");
                    Field<?> newName = parseFieldName(ctx);

                    return ctx.dsl.alterTable(oldName.getTable()).renameColumn(oldName).to(newName);
                }

                break;

            case 'i':
            case 'I':
                if (parseKeywordIf(ctx, "INDEX")) {
                    Name oldName = parseIndexName(ctx);
                    parseKeyword(ctx, "TO");
                    Name newName = parseIndexName(ctx);

                    return ctx.dsl.alterIndex(oldName).renameTo(newName);
                }

                break;

            case 's':
            case 'S':
                if (parseKeywordIf(ctx, "SCHEMA")) {
                    Schema oldName = parseSchemaName(ctx);
                    parseKeyword(ctx, "TO");
                    Schema newName = parseSchemaName(ctx);

                    return ctx.dsl.alterSchema(oldName).renameTo(newName);
                }
                else if (parseKeywordIf(ctx, "SEQUENCE")) {
                    Sequence<?> oldName = parseSequenceName(ctx);
                    parseKeyword(ctx, "TO");
                    Sequence<?> newName = parseSequenceName(ctx);

                    return ctx.dsl.alterSequence(oldName).renameTo(newName);
                }

                break;

            case 'v':
            case 'V':
                if (parseKeywordIf(ctx, "VIEW")) {
                    Table<?> oldName = parseTableName(ctx);
                    parseKeyword(ctx, "TO");
                    Table<?> newName = parseTableName(ctx);

                    return ctx.dsl.alterView(oldName).renameTo(newName);
                }

                break;
        }

        // If all of the above fails, we can assume we're renaming a table.
        parseKeywordIf(ctx, "TABLE");
        Table<?> oldName = parseTableName(ctx);
        parseKeyword(ctx, "TO");
        Table<?> newName = parseTableName(ctx);

        return ctx.dsl.alterTable(oldName).renameTo(newName);
    }

    private static final DDLQuery parseDropTable(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Table<?> tableName = parseTableName(ctx);
        boolean cascade = parseKeywordIf(ctx, "CASCADE");
        boolean restrict = !cascade && parseKeywordIf(ctx, "RESTRICT");

        DropTableStep s1;
        DropTableFinalStep s2;

        s1 = ifExists
            ? ctx.dsl.dropTableIfExists(tableName)
            : ctx.dsl.dropTable(tableName);

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

    private static final DDLQuery parseAlterSchema(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Schema schemaName = parseSchemaName(ctx);
        parseKeyword(ctx, "RENAME TO");
        Schema newName = parseSchemaName(ctx);

        AlterSchemaStep s1 = ifExists
            ? ctx.dsl.alterSchemaIfExists(schemaName)
            : ctx.dsl.alterSchema(schemaName);
        AlterSchemaFinalStep s2 = s1.renameTo(newName);
        return s2;
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
        Name indexName = parseIndexName(ctx);
        parseKeyword(ctx, "ON");
        Table<?> tableName = parseTableName(ctx);
        parse(ctx, '(');
        Field<?>[] fieldNames = Tools.fieldsByName(parseIdentifiers(ctx).toArray(EMPTY_NAME));
        parse(ctx, ')');
        Condition condition = parseKeywordIf(ctx, "WHERE")
            ? parseCondition(ctx)
            : null;


        CreateIndexStep s1 = ifNotExists
            ? unique
                ? ctx.dsl.createUniqueIndexIfNotExists(indexName)
                : ctx.dsl.createIndexIfNotExists(indexName)
            : unique
                ? ctx.dsl.createUniqueIndex(indexName)
                : ctx.dsl.createIndex(indexName);
        CreateIndexWhereStep s2 = s1.on(tableName, fieldNames);
        CreateIndexFinalStep s3 = condition != null
            ? s2.where(condition)
            : s2;

        return s3;
    }

    private static final DDLQuery parseAlterIndex(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Name indexName = parseIndexName(ctx);
        parseKeyword(ctx, "RENAME TO");
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
        DropIndexFinalStep s2;

        s1 = ifExists
            ? ctx.dsl.dropIndexIfExists(indexName)
            : ctx.dsl.dropIndex(indexName);

        s2 = on
            ? s1.on(onTable)
            : s1;

        return s2;
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
            Select<?> select = parseSelect(ctx);
            parse(ctx, ')');

            return exists(select);
        }

        else {
            FieldOrRow left;
            Comparator comp;
            boolean not;

            left = parseConcat(ctx, null);
            not = parseKeywordIf(ctx, "NOT");

            if (!not && (comp = parseComparatorIf(ctx)) != null) {
                boolean all = parseKeywordIf(ctx, "ALL");
                boolean any = !all && (parseKeywordIf(ctx, "ANY") || parseKeywordIf(ctx, "SOME"));
                if (all || any)
                    parse(ctx, '(');

                // TODO equal degrees
                Condition result =
                      all
                    ? left instanceof Field
                        ? ((Field) left).compare(comp, DSL.all(parseSelect(ctx, 1)))
                        : ((RowN) left).compare(comp, DSL.all(parseSelect(ctx, ((RowN) left).size())))
                    : any
                    ? left instanceof Field
                        ? ((Field) left).compare(comp, DSL.any(parseSelect(ctx, 1)))
                        : ((RowN) left).compare(comp, DSL.any(parseSelect(ctx, ((RowN) left).size())))
                    : left instanceof Field
                        ? ((Field) left).compare(comp, toField(ctx, parseConcat(ctx, null)))
                        : ((RowN) left).compare(comp, parseRow(ctx, ((RowN) left).size(), true));

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
                            : ((RowN) left).isNotNull()
                        : left instanceof Field
                            ? ((Field) left).isNull()
                            : ((RowN) left).isNotNull();

                parseKeyword(ctx, "DISTINCT FROM");

                // TODO: Support this for ROW as well
                if (((Field) left) == null)
                    throw ctx.notImplemented("DISTINCT predicate for rows");

                Field right = toField(ctx, parseConcat(ctx, null));
                return not ? ((Field) left).isNotDistinctFrom(right) : ((Field) left).isDistinctFrom(right);
            }
            else if (parseKeywordIf(ctx, "IN")) {
                Condition result;

                parse(ctx, '(');
                if (peekKeyword(ctx, "SELECT"))
                    result = not
                        ? left instanceof Field
                            ? ((Field) left).notIn(parseSelect(ctx, 1))
                            : ((RowN) left).notIn(parseSelect(ctx, ((RowN) left).size()))
                        : left instanceof Field
                            ? ((Field) left).in(parseSelect(ctx, 1))
                            : ((RowN) left).in(parseSelect(ctx, ((RowN) left).size()));
                else
                    result = not
                        ? left instanceof Field
                            ? ((Field) left).notIn(parseFields(ctx))
                            : ((RowN) left).notIn(parseRows(ctx, ((RowN) left).size()))
                        : left instanceof Field
                            ? ((Field) left).in(parseFields(ctx))
                            : ((RowN) left).in(parseRows(ctx, ((RowN) left).size()));

                parse(ctx, ')');
                return result;
            }
            else if (parseKeywordIf(ctx, "BETWEEN")) {
                boolean symmetric = parseKeywordIf(ctx, "SYMMETRIC");
                FieldOrRow r1 = left instanceof Field
                    ? parseConcat(ctx, null)
                    : parseRow(ctx, ((RowN) left).size());
                parseKeyword(ctx, "AND");
                FieldOrRow r2 = left instanceof Field
                    ? parseConcat(ctx, null)
                    : parseRow(ctx, ((RowN) left).size());

                return symmetric
                    ? not
                        ? left instanceof Field
                            ? ((Field) left).notBetweenSymmetric((Field) r1, (Field) r2)
                            : ((RowN) left).notBetweenSymmetric((RowN) r1, (RowN) r2)
                        : left instanceof Field
                            ? ((Field) left).betweenSymmetric((Field) r1, (Field) r2)
                            : ((RowN) left).betweenSymmetric((RowN) r1, (RowN) r2)
                    : not
                        ? left instanceof Field
                            ? ((Field) left).notBetween((Field) r1, (Field) r2)
                            : ((RowN) left).notBetween((RowN) r1, (RowN) r2)
                        : left instanceof Field
                            ? ((Field) left).between((Field) r1, (Field) r2)
                            : ((RowN) left).between((RowN) r1, (RowN) r2);
            }
            else if (left instanceof Field && parseKeywordIf(ctx, "LIKE")) {
                Field right = toField(ctx, parseConcat(ctx, null));
                boolean escape = parseKeywordIf(ctx, "ESCAPE");
                char character = escape ? parseCharacterLiteral(ctx) : ' ';
                return escape
                    ? not
                        ? ((Field) left).notLike(right, character)
                        : ((Field) left).like(right, character)
                    : not
                        ? ((Field) left).notLike(right)
                        : ((Field) left).like(right);
            }
            else if (left instanceof RowN && ((RowN) left).size() == 2 && parseKeywordIf(ctx, "OVERLAPS")) {
                return ((Row2) left).overlaps((Row2) parseRow(ctx, 2));
            }
            else
                return left;
        }
    }

    private static final List<Table<?>> parseTables(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        List<Table<?>> result = new ArrayList<Table<?>>();
        do {
            result.add(parseTable(ctx));
        }
        while (parseIf(ctx, ','));
        return result;
    }

    private static final Table<?> parseTable(ParserContext ctx) {
        Table<?> result = parseTableFactor(ctx);

        for (;;) {
            Table<?> joined = parseJoinedTableIf(ctx, result);
            if (joined == null)
                return result;
            else
                result = joined;
        }
    }

    private static final Table<?> parseTableFactor(ParserContext ctx) {
        Table<?> result = null;

        // TODO [#5306] Support FINAL TABLE (<data change statement>)
        // TOOD ONLY ( table primary )
        if (parseKeywordIf(ctx, "LATERAL")) {
            parse(ctx, '(');
            result = lateral(parseSelect(ctx));
            parse(ctx, ')');
        }
        else if (parseKeywordIf(ctx, "UNNEST")) {
            // TODO
            throw ctx.notImplemented("UNNEST");
        }
        else if (parseIf(ctx, '(')) {
            if (peekKeyword(ctx, "SELECT")) {
                result = table(parseSelect(ctx));
                parse(ctx, ')');
            }
            else if (peekKeyword(ctx, "VALUES")) {
                result = parseTableValueConstructor(ctx);
                parse(ctx, ')');
            }
            else {
                int parens = 0;

                while (parseIf(ctx, '('))
                    parens++;

                result = parseJoinedTable(ctx);

                while (parens --> 0)
                    parse(ctx, ')');

                parse(ctx, ')');
                return result;
            }
        }
        else {
            result = parseTableName(ctx);
            // TODO Sample clause
        }


























































































        // TODO UNPIVOT
//        else if (parseKeywordIf(ctx, "UNPIVOT")) {
//
//        }

        Name alias = null;
        List<Name> columnAliases = null;

        if (parseKeywordIf(ctx, "AS"))
            alias = parseIdentifier(ctx);
        else if (!peekKeyword(ctx, SELECT_KEYWORDS))
            alias = parseIdentifierIf(ctx);

        if (alias != null) {
            if (parseIf(ctx, '(')) {
                columnAliases = parseIdentifiers(ctx);
                parse(ctx, ')');
            }

            if (columnAliases != null)
                result = result.as(alias, columnAliases.toArray(EMPTY_NAME));
            else
                result = result.as(alias);
        }

        return result;
    }

    private static final Table<?> parseTableValueConstructor(ParserContext ctx) {
        parseKeyword(ctx, "VALUES");

        List<RowN> rows = new ArrayList<RowN>();
        do {
            rows.add(parseTuple(ctx));
        }
        while (parseIf(ctx, ','));
        return values(rows.toArray(Tools.EMPTY_ROWN));
    }

    private static final RowN parseTuple(ParserContext ctx) {
        return parseTuple(ctx, null, false);
    }

    private static final RowN parseTuple(ParserContext ctx, Integer degree) {
        return parseTuple(ctx, degree, false);
    }

    private static final RowN parseTuple(ParserContext ctx, Integer degree, boolean allowDoubleParens) {
        parse(ctx, '(');
        List<? extends FieldOrRow> fieldsOrRows;

        if (allowDoubleParens)
            fieldsOrRows = parseFieldsOrRows(ctx);
        else
            fieldsOrRows = parseFields(ctx);

        RowN row;

        if (fieldsOrRows.size() == 0)
            row = row();
        else if (fieldsOrRows.get(0) instanceof Field)
            row = row(fieldsOrRows);
        else if (fieldsOrRows.size() == 1)
            row = (RowN) fieldsOrRows.get(0);
        else
            throw ctx.exception("Unsupported row size");

        if (degree != null && row.size() != degree)
            throw ctx.exception("Expected row of degree: " + degree + ". Got: " + row.size());

        parse(ctx, ')');
        return row;
    }

    private static final Table<?> parseJoinedTable(ParserContext ctx) {
        Table<?> result = parseTableFactor(ctx);

        for (int i = 0;; i++) {
            Table<?> joined = parseJoinedTableIf(ctx, result);
            if (joined == null)
                if (i == 0)
                    ctx.unexpectedToken();
                else
                    return result;
            else
                result = joined;
        }
    }

    private static final Table<?> parseJoinedTableIf(ParserContext ctx, Table<?> left) {
        JoinType joinType = parseJoinTypeIf(ctx);

        if (joinType == null)
            return null;

        Table<?> right = joinType.qualified() ? parseTable(ctx) : parseTableFactor(ctx);

        TableOptionalOnStep<?> s0;
        TablePartitionByStep<?> s1;
        TableOnStep<?> s2;
        s2 = s1 = s0 = left.join(right, joinType);

        switch (joinType) {
            case LEFT_OUTER_JOIN:
            case FULL_OUTER_JOIN:
            case RIGHT_OUTER_JOIN:










            case JOIN:
                boolean on = parseKeywordIf(ctx, "ON");

                if (on) {
                    return s2.on(parseCondition(ctx));
                }
                else {
                    parseKeyword(ctx, "USING");
                    parse(ctx, '(');
                    Table result = s2.using(Tools.fieldsByName(parseIdentifiers(ctx).toArray(EMPTY_NAME)));
                    parse(ctx, ')');

                    return result;
                }

            default:
                return s0;
        }
    }

    private static final List<Field<?>> parseSelectList(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        if (parseIf(ctx, '*'))
            return Collections.emptyList();

        // TODO Support qualified asterisk
        List<Field<?>> result = new ArrayList<Field<?>>();
        do {
            if (peekKeyword(ctx, SELECT_KEYWORDS))
                throw ctx.unexpectedToken();

            Field<?> field = parseField(ctx);
            Name alias = null;

            if (parseKeywordIf(ctx, "AS"))
                alias = parseIdentifier(ctx);
            else if (!peekKeyword(ctx, SELECT_KEYWORDS))
                alias = parseIdentifierIf(ctx);

            result.add(alias == null ? field : field.as(alias));
        }
        while (parseIf(ctx, ','));
        return result;
    }

    private static final List<SortField<?>> parseSortSpecification(ParserContext ctx) {
        List<SortField<?>> result = new ArrayList<SortField<?>>();

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
        else if (parseKeywordIf(ctx, "ASC") || true)
            sort = field.asc();

        if (parseKeywordIf(ctx, "NULLS FIRST"))
            sort = sort.nullsFirst();
        else if (parseKeywordIf(ctx, "NULLS LAST"))
            sort = sort.nullsLast();

        return sort;
    }

    private static final List<Field<?>> parseFields(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        List<Field<?>> result = new ArrayList<Field<?>>();
        do {
            result.add(parseField(ctx));
        }
        while (parseIf(ctx, ','));
        return result;
    }

    private static final List<FieldOrRow> parseFieldsOrRows(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        List<FieldOrRow> result = new ArrayList<FieldOrRow>();
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

    private static final RowN parseRow(ParserContext ctx) {
        return parseRow(ctx, null);
    }

    private static final List<RowN> parseRows(ParserContext ctx, Integer degree) {
        List<RowN> result = new ArrayList<RowN>();

        do {
            result.add(parseRow(ctx, degree));
        }
        while (parseIf(ctx, ','));

        return result;
    }

    private static final RowN parseRow(ParserContext ctx, Integer degree) {
        parseKeywordIf(ctx, "ROW");
        RowN row = parseTuple(ctx, degree);
        return row;
    }

    private static final RowN parseRow(ParserContext ctx, Integer degree, boolean allowDoubleParens) {
        parseKeywordIf(ctx, "ROW");
        RowN row = parseTuple(ctx, degree, allowDoubleParens);
        return row;
    }

    static enum Type {
        A("array"),
        D("date"),
        S("string"),
        N("numeric"),
        B("boolean"),
        X("binary");

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

    private static final Condition toCondition(ParserContext ctx, QueryPart part) {
        if (part == null)
            return null;
        else if (part instanceof Condition)
            return (Condition) part;
        else if (part instanceof Field)
            if (((Field) part).getDataType().getType() == Boolean.class)
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
        FieldOrRow r = parseSum(ctx, type);

        if (S.is(type) && r instanceof Field)
            while (parseIf(ctx, "||"))
                r = concat((Field) r, toField(ctx, parseSum(ctx, type)));

        return r;
    }

    private static final Field<?> parseFieldSumParenthesised(ParserContext ctx) {
        parse(ctx, '(');
        Field<?> r = toField(ctx, parseSum(ctx, N));
        parse(ctx, ')');
        return r;
    }

    private static final FieldOrRow parseSum(ParserContext ctx, Type type) {
        FieldOrRow r = parseFactor(ctx, type);

        if (N.is(type) && r instanceof Field)
            for (;;)
                if (parseIf(ctx, '+'))
                    r = ((Field) r).add((Field) parseFactor(ctx, type));
                else if (parseIf(ctx, '-'))
                    r = ((Field) r).sub((Field) parseFactor(ctx, type));
                else
                    break;

        return r;
    }

    private static final FieldOrRow parseFactor(ParserContext ctx, Type type) {
        FieldOrRow r = parseExp(ctx, type);

        if (N.is(type) && r instanceof Field)
            for (;;)
                if (parseIf(ctx, '*'))
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
                if (parseIf(ctx, '^'))
                    r = ((Field) r).pow(toField(ctx, parseUnaryOps(ctx, type)));
                else
                    break;

        return r;
    }

    private static final FieldOrRow parseUnaryOps(ParserContext ctx, Type type) {
        FieldOrRow r;
        Sign sign = parseSign(ctx);

        if (sign == Sign.NONE)
            r = parseTerm(ctx, type);
        else if (sign == Sign.PLUS)
            r = toField(ctx, parseTerm(ctx, type));
        else if ((r = parseFieldUnsignedNumericLiteralIf(ctx, Sign.MINUS)) == null)
            r = toField(ctx, parseTerm(ctx, type)).neg();

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
        parseWhitespaceIf(ctx);

        Field<?> field;
        Object value;

        switch (ctx.character()) {
            case ':':
            case '?':
                return parseBindVariable(ctx);

            case '\'':
                return inline(parseStringLiteral(ctx));

            case 'a':
            case 'A':
                if (N.is(type))
                    if ((field = parseFieldAsciiIf(ctx)) != null)
                        return field;
                    else if (parseKeywordIf(ctx, "ACOS"))
                        return acos((Field) parseFieldSumParenthesised(ctx));
                    else if (parseKeywordIf(ctx, "ASIN"))
                        return asin((Field) parseFieldSumParenthesised(ctx));
                    else if (parseKeywordIf(ctx, "ATAN"))
                        return atan((Field) parseFieldSumParenthesised(ctx));
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

                break;

            case 'c':
            case 'C':
                if (S.is(type))
                    if ((field = parseFieldConcatIf(ctx)) != null)
                        return field;
                    else if (parseKeywordIf(ctx, "CURRENT_SCHEMA"))
                        return currentSchema();
                    else if (parseKeywordIf(ctx, "CURRENT_USER"))
                        return currentUser();

                if (N.is(type))
                    if ((field = parseFieldCharIndexIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldCharLengthIf(ctx)) != null)
                        return field;
                    else if (parseKeywordIf(ctx, "CEILING") || parseKeywordIf(ctx, "CEIL"))
                        return ceil((Field) parseFieldSumParenthesised(ctx));
                    else if (parseKeywordIf(ctx, "COSH"))
                        return cosh((Field) parseFieldSumParenthesised(ctx));
                    else if (parseKeywordIf(ctx, "COS"))
                        return cos((Field) parseFieldSumParenthesised(ctx));
                    else if (parseKeywordIf(ctx, "COTH"))
                        return coth((Field) parseFieldSumParenthesised(ctx));
                    else if (parseKeywordIf(ctx, "COT"))
                        return cot((Field) parseFieldSumParenthesised(ctx));

                if (D.is(type))
                    if (parseKeywordIf(ctx, "CURRENT_TIMESTAMP"))
                        return currentTimestamp();
                    else if (parseKeywordIf(ctx, "CURRENT_TIME"))
                        return currentTime();
                    else if (parseKeywordIf(ctx, "CURRENT_DATE"))
                        return currentDate();

                if ((field = parseFieldCaseIf(ctx)) != null)
                    return field;
                else if ((field = parseCastIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldCoalesceIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldCumeDistIf(ctx)) != null)
                    return field;

                break;

            case 'd':
            case 'D':
                if (D.is(type))
                    if ((field = parseFieldDateLiteralIf(ctx)) != null)
                        return field;

                if (N.is(type))
                    if ((field = parseFieldDenseRankIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldDayIf(ctx)) != null)
                        return field;
                    else if (parseKeywordIf(ctx, "DEGREE") || parseKeywordIf(ctx, "DEG"))
                        return deg((Field) parseFieldSumParenthesised(ctx));

                break;

            case 'e':
            case 'E':
                if (N.is(type))
                    if ((field = parseFieldExtractIf(ctx)) != null)
                        return field;
                    else if (parseKeywordIf(ctx, "EXP"))
                        return exp((Field) parseFieldSumParenthesised(ctx));

                break;

            case 'f':
            case 'F':
                if (N.is(type))
                    if (parseKeywordIf(ctx, "FLOOR"))
                        return floor((Field) parseFieldSumParenthesised(ctx));

                if ((field = parseFieldFirstValueIf(ctx)) != null)
                    return field;

                break;

            case 'g':
            case 'G':
                if ((field = parseFieldGreatestIf(ctx)) != null)
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
                if (N.is(type) && (field = parseFieldInstrIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldIfnullIf(ctx)) != null)
                    return field;
                else if ((field = parseFieldIsnullIf(ctx)) != null)
                    return field;
                else
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
                    else if (parseKeywordIf(ctx, "LN"))
                        return ln((Field) parseFieldSumParenthesised(ctx));
                    else if ((field = parseFieldLogIf(ctx)) != null)
                        return field;
                    else if (parseKeywordIf(ctx, "LEVEL"))
                        return level();

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

                break;

            case 'o':
            case 'O':
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

                if (parseKeywordIf(ctx, "PRIOR"))
                    return prior(toField(ctx, parseConcat(ctx, type)));

                break;

            case 'q':
            case 'Q':
                if (S.is(type))
                    if (ctx.character(ctx.position + 1) == '\'')
                        return inline(parseStringLiteral(ctx));

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
                    else if (parseKeywordIf(ctx, "RADIAN") || parseKeywordIf(ctx, "RAD"))
                        return rad((Field) parseFieldSumParenthesised(ctx));

                if (parseKeywordIf(ctx, "ROW"))
                    return parseTuple(ctx);

                break;

            case 's':
            case 'S':
                if (S.is(type))
                    if ((field = parseFieldSubstringIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldSpaceIf(ctx)) != null)
                        return field;

                if (N.is(type))
                    if ((field = parseFieldSecondIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldSignIf(ctx)) != null)
                        return field;
                    else if (parseKeywordIf(ctx, "SQRT") || parseKeywordIf(ctx, "SQR"))
                        return sqrt((Field) parseFieldSumParenthesised(ctx));
                    else if (parseKeywordIf(ctx, "SINH"))
                        return sinh((Field) parseFieldSumParenthesised(ctx));
                    else if (parseKeywordIf(ctx, "SIN"))
                        return sin((Field) parseFieldSumParenthesised(ctx));

                break;

            case 't':
            case 'T':
                if (B.is(type))
                    if ((field = parseBooleanValueExpressionIf(ctx)) != null)
                        return field;

                if (S.is(type))
                    if ((field = parseFieldTrimIf(ctx)) != null)
                        return field;

                if (N.is(type))
                    if ((field = parseFieldTruncIf(ctx)) != null)
                        return field;
                    else if (parseKeywordIf(ctx, "TANH"))
                        return tanh((Field) parseFieldSumParenthesised(ctx));
                    else if (parseKeywordIf(ctx, "TAN"))
                        return tan((Field) parseFieldSumParenthesised(ctx));

                if (D.is(type))
                    if ((field = parseFieldTimestampLiteralIf(ctx)) != null)
                        return field;
                    else if ((field = parseFieldTimeLiteralIf(ctx)) != null)
                        return field;

                break;

            case 'u':
            case 'U':
                if (S.is(type))
                    if ((field = parseFieldUpperIf(ctx)) != null)
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

            case '(':
                parse(ctx, '(');

                if (peekKeyword(ctx, "SELECT")) {
                    SelectQueryImpl<Record> select = parseSelect(ctx);
                    if (select.getSelect().size() > 1)
                        throw ctx.exception("Select list must contain at least one column");

                    field = field((Select) select);
                    parse(ctx, ')');
                    return field;
                }
                else {
                    FieldOrRow r = parseFieldOrRow(ctx, type);
                    List<Field<?>> list = null;

                    if (r instanceof Field) {
                        while (parseIf(ctx, ',')) {
                            if (list == null) {
                                list = new ArrayList<Field<?>>();
                                list.add((Field) r);
                            }

                            // TODO Allow for nesting ROWs
                            list.add(parseField(ctx, type));
                        }
                    }

                    parse(ctx, ')');
                    return list != null ? row(list) : r;
                }
        }

        if ((field = parseAggregateFunctionIf(ctx)) != null)
            return field;

        else if ((field = parseBooleanValueExpressionIf(ctx)) != null)
            return field;

        else
            return parseFieldName(ctx);
    }

    private static final Field<?> parseArrayValueConstructorIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "ARRAY")) {
            parse(ctx, '[');

            List<? extends Field<? extends Object[]>> fields;
            if (parseIf(ctx, ']')) {
                fields = Collections.emptyList();
            }
            else {
                fields = (List) parseFields(ctx);
                parse(ctx, ']');
            }

            return DSL.array(fields);
        }

        return null;
    }

    private static final Field<?> parseFieldAtan2If(ParserContext ctx) {
        if (parseKeywordIf(ctx, "ATN2") || parseKeywordIf(ctx, "ATAN2")) {
            parse(ctx, '(');
            Field<?> x = toField(ctx, parseSum(ctx, N));
            parse(ctx, ',');
            Field<?> y = toField(ctx, parseSum(ctx, N));
            parse(ctx, ')');

            return atan2((Field) x, (Field) y);
        }

        return null;
    }

    private static final Field<?> parseFieldLogIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "LOG")) {
            parse(ctx, '(');
            Field<?> arg1 = toField(ctx, parseSum(ctx, N));
            parse(ctx, ',');
            long arg2 = parseUnsignedInteger(ctx);
            parse(ctx, ')');
            return log((Field) arg1, (int) arg2);
        }

        return null;
    }

    private static final Field<?> parseFieldTruncIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "TRUNC")) {
            parse(ctx, '(');
            Field<?> arg1 = toField(ctx, parseSum(ctx, N));
            parse(ctx, ',');
            Field<?> arg2 = toField(ctx, parseSum(ctx, N));
            parse(ctx, ')');
            return DSL.trunc((Field) arg1, (Field) arg2);
        }

        return null;
    }

    private static final Field<?> parseFieldRoundIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "ROUND")) {
            Field arg1 = null;
            Integer arg2 = null;

            parse(ctx, '(');
            arg1 = toField(ctx, parseSum(ctx, N));
            if (parseIf(ctx, ','))
                arg2 = (int) (long) parseUnsignedInteger(ctx);

            parse(ctx, ')');
            return arg2 == null ? round(arg1) : round(arg1, arg2);
        }

        return null;
    }

    private static final Field<?> parseFieldPowerIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "POWER") || parseKeywordIf(ctx, "POW")) {
            parse(ctx, '(');
            Field arg1 = toField(ctx, parseSum(ctx, N));
            parse(ctx, ',');
            Field arg2 = toField(ctx, parseSum(ctx, N));
            parse(ctx, ')');
            return DSL.power(arg1, arg2);
        }

        return null;
    }

    private static final Field<?> parseFieldModIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "MOD")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx, N);
            parse(ctx, ',');
            Field<?> f2 = parseField(ctx, N);
            parse(ctx, ')');
            return f1.mod((Field) f2);
        }

        return null;
    }

    private static final Field<?> parseFieldLeastIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "LEAST")) {
            parse(ctx, '(');
            List<Field<?>> fields = parseFields(ctx);
            parse(ctx, ')');

            return least(fields.get(0), fields.size() > 1 ? fields.subList(1, fields.size()).toArray(EMPTY_FIELD) : EMPTY_FIELD);
        }

        return null;
    }

    private static final Field<?> parseFieldGreatestIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "GREATEST")) {
            parse(ctx, '(');
            List<Field<?>> fields = parseFields(ctx);
            parse(ctx, ')');

            return greatest(fields.get(0), fields.size() > 1 ? fields.subList(1, fields.size()).toArray(EMPTY_FIELD) : EMPTY_FIELD);
        }

        return null;
    }

    private static final Field<?> parseFieldGroupingIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "GROUPING")) {
            parse(ctx, '(');
            Field<?> field = parseField(ctx);
            parse(ctx, ')');

            return grouping(field);
        }

        return null;
    }

    private static final Field<?> parseFieldGroupingIdIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "GROUPING_ID")) {
            parse(ctx, '(');
            List<Field<?>> fields = parseFields(ctx);
            parse(ctx, ')');

            return groupingId(fields.toArray(EMPTY_FIELD));
        }

        return null;
    }

    private static final Field<?> parseFieldTimestampLiteralIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "TIMESTAMP")) {
            if (parseKeywordIf(ctx, "WITHOUT TIME ZONE")) {
                return inline(parseTimestampLiteral(ctx));
            }
            else if (parseIf(ctx, '(')) {
                Field<?> f = parseField(ctx, S);
                parse(ctx, ')');
                return timestamp((Field) f);
            }
            else {
                return inline(parseTimestampLiteral(ctx));
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
        if (parseKeywordIf(ctx, "TIME")) {
            if (parseKeywordIf(ctx, "WITHOUT TIME ZONE")) {
                return inline(parseTimeLiteral(ctx));
            }
            else if (parseIf(ctx, '(')) {
                Field<?> f = parseField(ctx, S);
                parse(ctx, ')');
                return time((Field) f);
            }
            else {
                return inline(parseTimeLiteral(ctx));
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

    private static final Field<?> parseFieldDateLiteralIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "DATE")) {
            if (parseIf(ctx, '(')) {
                Field<?> f = parseField(ctx, S);
                parse(ctx, ')');
                return date((Field) f);
            }
            else {
                return inline(parseDateLiteral(ctx));
            }
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
        if (parseKeywordIf(ctx, "EXTRACT")) {
            parse(ctx, '(');
            DatePart part = parseDatePart(ctx);
            parseKeyword(ctx, "FROM");
            Field<?> field = parseField(ctx);
            parse(ctx, ')');

            return extract(field, part);
        }

        return null;
    }

    private static final DatePart parseDatePart(ParserContext ctx) {
        for (DatePart part : DatePart.values())
            if (parseKeywordIf(ctx, part.name()))
                return part;

        throw ctx.unexpectedToken();
    }

    private static final Field<?> parseFieldAsciiIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "ASCII")) {
            parse(ctx, '(');
            Field<?> arg = parseField(ctx, S);
            parse(ctx, ')');
            return ascii((Field) arg);
        }

        return null;
    }

    private static final Field<?> parseFieldConcatIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "CONCAT")) {
            parse(ctx, '(');
            Field<String> result = concat(parseFields(ctx).toArray(EMPTY_FIELD));
            parse(ctx, ')');
            return result;
        }

        return null;
    }

    private static final Field<?> parseFieldInstrIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "INSTR")) {
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
        if (parseKeywordIf(ctx, "CHARINDEX")) {
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
        if (parseKeywordIf(ctx, "LPAD")) {
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
        if (parseKeywordIf(ctx, "RPAD")) {
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

    private static final Field<?> parseFieldPositionIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "POSITION")) {
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
        if (parseKeywordIf(ctx, "REPEAT")) {
            parse(ctx, '(');
            Field<String> field = (Field) parseField(ctx, S);
            parse(ctx, ',');
            Field<Integer> count = (Field) parseField(ctx, N);
            parse(ctx, ')');
            return repeat(field, count);
        }

        return null;
    }

    private static final Field<?> parseFieldReplaceIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "REPLACE")) {
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
        if (parseKeywordIf(ctx, "REVERSE")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return reverse(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldSpaceIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "SPACE")) {
            parse(ctx, '(');
            Field<Integer> f1 = (Field) parseField(ctx, N);
            parse(ctx, ')');
            return space(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldSubstringIf(ParserContext ctx) {
        boolean substring = parseKeywordIf(ctx, "SUBSTRING");
        boolean substr = !substring && parseKeywordIf(ctx, "SUBSTR");

        if (substring || substr) {
            boolean keywords = !substr;
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            if (substr || !(keywords = parseKeywordIf(ctx, "FROM")))
                parse(ctx, ',');
            Field f2 = toField(ctx, parseSum(ctx, N));
            Field f3 =
                    ((keywords && parseKeywordIf(ctx, "FOR")) || (!keywords && parseIf(ctx, ',')))
                ? (Field) toField(ctx, parseSum(ctx, N))
                : null;
            parse(ctx, ')');

            return f3 == null
                ? substring(f1, f2)
                : substring(f1, f2, f3);
        }

        return null;
    }

    private static final Field<?> parseFieldTrimIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "TRIM")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return trim(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldRtrimIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "RTRIM")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return rtrim(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldLtrimIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "LTRIM")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return ltrim(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldMidIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "MID")) {
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
        if (parseKeywordIf(ctx, "LEFT")) {
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
        if (parseKeywordIf(ctx, "RIGHT")) {
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
        if (parseKeywordIf(ctx, "MD5")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return md5(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldLengthIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "LENGTH")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return length(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldCharLengthIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "CHAR_LENGTH")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return charLength(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldBitLengthIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "BIT_LENGTH")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return bitLength(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldOctetLengthIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "OCTET_LENGTH")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return octetLength(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldLowerIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "LOWER") || parseKeywordIf(ctx, "LCASE")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return lower(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldUpperIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "UPPER") || parseKeywordIf(ctx, "UCASE")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return DSL.upper(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldYearIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "YEAR")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return year(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldMonthIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "MONTH")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return month(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldDayIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "DAY")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return day(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldHourIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "HOUR")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return hour(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldMinuteIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "MINUTE")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return minute(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldSecondIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "SECOND")) {
            parse(ctx, '(');
            Field<Timestamp> f1 = (Field) parseField(ctx, D);
            parse(ctx, ')');
            return second(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldSignIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "SIGN")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx, N);
            parse(ctx, ')');
            return sign((Field) f1);
        }

        return null;
    }

    private static final Field<?> parseFieldIfnullIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "IFNULL")) {
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
        if (parseKeywordIf(ctx, "ISNULL")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx);
            parse(ctx, ',');
            Field<?> f2 = parseField(ctx);
            parse(ctx, ')');

            return isnull(f1, f2);
        }

        return null;
    }

    private static final Field<?> parseFieldNvlIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "NVL")) {
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
        if (parseKeywordIf(ctx, "NVL2")) {
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
        if (parseKeywordIf(ctx, "NULLIF")) {
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
        if (parseKeywordIf(ctx, "COALESCE")) {
            parse(ctx, '(');
            List<Field<?>> fields = parseFields(ctx);
            parse(ctx, ')');

            Field[] a = EMPTY_FIELD;
            return coalesce(fields.get(0), fields.size() == 1 ? a : fields.subList(1, fields.size()).toArray(a));
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

    private static final Field<?> parseCastIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "CAST")) {
            parse(ctx, '(');
            Field<?> field = parseField(ctx);
            parseKeyword(ctx, "AS");
            DataType<?> type = parseDataType(ctx);
            parse(ctx, ')');

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
        AggregateFunction<?> agg;
        WindowBeforeOverStep<?> over;
        Object keep = null;
        Field<?> result;
        Condition filter;

        keep = over = agg = parseCountIf(ctx);
        if (agg == null)
            keep = over = agg = parseGeneralSetFunctionIf(ctx);
        if (agg == null && !basic)
            over = agg = parseBinarySetFunctionIf(ctx);
        if (agg == null && !basic)
            over = parseOrderedSetFunctionIf(ctx);

        if (agg == null && over == null)
            return null;





















        if (agg != null && !basic && parseKeywordIf(ctx, "FILTER")) {
            parse(ctx, '(');
            parseKeyword(ctx, "WHERE");
            filter = parseCondition(ctx);
            parse(ctx, ')');

            result = over = agg.filterWhere(filter);
        }
        else if (agg != null)
            result = agg;
        else
            result = over;

        if (!basic && parseKeywordIf(ctx, "OVER")) {
            Object nameOrSpecification = parseWindowNameOrSpecification(ctx, agg != null);

            if (nameOrSpecification instanceof Name)
                result = over.over((Name) nameOrSpecification);
            else if (nameOrSpecification instanceof WindowSpecification)
                result = over.over((WindowSpecification) nameOrSpecification);
            else
                result = over.over();
        }

        return result;
    }

    private static final Object parseWindowNameOrSpecification(ParserContext ctx, boolean orderByAllowed) {
        Object result;

        if (parseIf(ctx, '(')) {
            WindowSpecificationOrderByStep s1 = null;
            WindowSpecificationRowsStep s2 = null;
            WindowSpecificationRowsAndStep s3 = null;

            s1 = parseKeywordIf(ctx, "PARTITION BY")
                ? partitionBy(parseFields(ctx))
                : null;

            s2 = orderByAllowed && parseKeywordIf(ctx, "ORDER BY")
                ? s1 == null
                    ? orderBy(parseSortSpecification(ctx))
                    : s1.orderBy(parseSortSpecification(ctx))
                : s1;

            boolean rows = orderByAllowed && parseKeywordIf(ctx, "ROWS");
            if (rows || (orderByAllowed && parseKeywordIf(ctx, "RANGE"))) {
                if (parseKeywordIf(ctx, "BETWEEN")) {
                    if (parseKeywordIf(ctx, "UNBOUNDED")) {
                        if (parseKeywordIf(ctx, "PRECEDING")) {
                            s3 = s2 == null
                                ? rows
                                    ? rowsBetweenUnboundedPreceding()
                                    : rangeBetweenUnboundedPreceding()
                                : rows
                                    ? s2.rowsBetweenUnboundedPreceding()
                                    : s2.rangeBetweenUnboundedPreceding();
                        }
                        else {
                            parseKeyword(ctx, "FOLLOWING");
                            s3 = s2 == null
                                ? rows
                                    ? rowsBetweenUnboundedFollowing()
                                    : rangeBetweenUnboundedFollowing()
                                : rows
                                    ? s2.rowsBetweenUnboundedFollowing()
                                    : s2.rangeBetweenUnboundedFollowing();
                        }
                    }
                    else if (parseKeywordIf(ctx, "CURRENT ROW")) {
                        s3 = s2 == null
                            ? rows
                                ? rowsBetweenCurrentRow()
                                : rangeBetweenCurrentRow()
                            : rows
                                ? s2.rowsBetweenCurrentRow()
                                : s2.rangeBetweenCurrentRow();
                    }
                    else {
                        int number = (int) (long) parseUnsignedInteger(ctx);

                        if (parseKeywordIf(ctx, "PRECEDING")) {
                            s3 = s2 == null
                                ? rows
                                    ? rowsBetweenPreceding(number)
                                    : rangeBetweenPreceding(number)
                                : rows
                                    ? s2.rowsBetweenPreceding(number)
                                    : s2.rangeBetweenPreceding(number);
                        }
                        else {
                            parseKeyword(ctx, "FOLLOWING");
                            s3 = s2 == null
                                ? rows
                                    ? rowsBetweenFollowing(number)
                                    : rangeBetweenFollowing(number)
                                : rows
                                    ? s2.rowsBetweenFollowing(number)
                                    : s2.rangeBetweenFollowing(number);
                        }
                    }

                    parseKeyword(ctx, "AND");

                    if (parseKeywordIf(ctx, "UNBOUNDED")) {
                        if (parseKeywordIf(ctx, "PRECEDING")) {
                            result = s3.andUnboundedPreceding();
                        }
                        else {
                            parseKeyword(ctx, "FOLLOWING");
                            result = s3.andUnboundedFollowing();
                        }
                    }
                    else if (parseKeywordIf(ctx, "CURRENT ROW")) {
                        result = s3.andCurrentRow();
                    }
                    else {
                        int number = (int) (long) parseUnsignedInteger(ctx);

                        if (parseKeywordIf(ctx, "PRECEDING")) {
                            result = s3.andPreceding(number);
                        }
                        else {
                            parseKeyword(ctx, "FOLLOWING");
                            result = s3.andFollowing(number);
                        }
                    }
                }
                else {
                    if (parseKeywordIf(ctx, "UNBOUNDED")) {
                        if (parseKeywordIf(ctx, "PRECEDING")) {
                            result = s2 == null
                                ? rows
                                    ? rowsUnboundedPreceding()
                                    : rangeUnboundedPreceding()
                                : rows
                                    ? s2.rowsUnboundedPreceding()
                                    : s2.rangeUnboundedPreceding();
                        }
                        else {
                            parseKeyword(ctx, "FOLLOWING");
                            result = s2 == null
                                ? rows
                                    ? rowsUnboundedFollowing()
                                    : rangeUnboundedFollowing()
                                : rows
                                    ? s2.rowsUnboundedFollowing()
                                    : s2.rangeUnboundedFollowing();
                        }
                    }
                    else if (parseKeywordIf(ctx, "CURRENT ROW")) {
                        result = s2 == null
                            ? rows
                                ? rowsCurrentRow()
                                : rangeCurrentRow()
                            : rows
                                ? s2.rowsCurrentRow()
                                : s2.rangeCurrentRow();
                    }
                    else {
                        int number = (int) (long) parseUnsignedInteger(ctx);

                        if (parseKeywordIf(ctx, "PRECEDING")) {
                            result = s2 == null
                                ? rows
                                    ? rowsPreceding(number)
                                    : rangePreceding(number)
                                : rows
                                    ? s2.rowsPreceding(number)
                                    : s2.rangePreceding(number);
                        }
                        else {
                            parseKeyword(ctx, "FOLLOWING");
                            result = s2 == null
                                ? rows
                                    ? rowsFollowing(number)
                                    : rangeFollowing(number)
                                : rows
                                    ? s2.rowsFollowing(number)
                                    : s2.rangeFollowing(number);
                        }
                    }
                }
            }
            else {
                result = s2;
            }

            parse(ctx, ')');
        }
        else {
            result = parseIdentifier(ctx);
        }

        return result;
    }

    private static final Field<?> parseFieldRankIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "RANK")) {
            parse(ctx, '(');

            if (parseIf(ctx, ')'))
                return parseWindowFunction(ctx, null, rank());

            // Hypothetical set function
            List<Field<?>> args = parseFields(ctx);
            parse(ctx, ')');
            AggregateFilterStep<?> result = parseWithinGroupN(ctx, rank(args));
            return result;
        }

        return null;
    }

    private static final Field<?> parseFieldDenseRankIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "DENSE_RANK")) {
            parse(ctx, '(');

            if (parseIf(ctx, ')'))
                return parseWindowFunction(ctx, null, denseRank());

            // Hypothetical set function
            List<Field<?>> args = parseFields(ctx);
            parse(ctx, ')');
            AggregateFilterStep<?> result = parseWithinGroupN(ctx, denseRank(args));
            return result;
        }

        return null;
    }

    private static final Field<?> parseFieldPercentRankIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "PERCENT_RANK")) {
            parse(ctx, '(');

            if (parseIf(ctx, ')'))
                return parseWindowFunction(ctx, null, percentRank());

            // Hypothetical set function
            List<Field<?>> args = parseFields(ctx);
            parse(ctx, ')');
            AggregateFilterStep<?> result = parseWithinGroupN(ctx, percentRank(args));
            return result;
        }

        return null;
    }

    private static final Field<?> parseFieldCumeDistIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "CUME_DIST")) {
            parse(ctx, '(');

            if (parseIf(ctx, ')'))
                return parseWindowFunction(ctx, null, cumeDist());

            // Hypothetical set function
            List<Field<?>> args = parseFields(ctx);
            parse(ctx, ')');
            AggregateFilterStep<?> result = parseWithinGroupN(ctx, cumeDist(args));
            return result;
        }

        return null;
    }

    private static final Field<?> parseFieldRowNumberIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "ROW_NUMBER")) {
            parse(ctx, '(');
            parse(ctx, ')');
            return parseWindowFunction(ctx, null, rowNumber());
        }

        return null;
    }

    private static final Field<?> parseFieldNtileIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "NTILE")) {
            parse(ctx, '(');
            int number = (int) (long) parseUnsignedInteger(ctx);
            parse(ctx, ')');
            return parseWindowFunction(ctx, null, ntile(number));
        }

        return null;
    }

    private static final Field<?> parseFieldLeadLagIf(ParserContext ctx) {
        boolean lead = parseKeywordIf(ctx, "LEAD");
        boolean lag = !lead && parseKeywordIf(ctx, "LAG");

        if (lead || lag) {
            parse(ctx, '(');
            Field<Void> f1 = (Field) parseField(ctx);
            Integer f2 = null;
            Field<Void> f3 = null;

            if (parseIf(ctx, ',')) {
                f2 = (int) (long) parseUnsignedInteger(ctx);

                if (parseIf(ctx, ',')) {
                    f3 = (Field) parseField(ctx);
                }
            }
            parse(ctx, ')');
            return parseWindowFunction(ctx, lead
                ? f2 == null
                    ? lead(f1)
                    : f3 == null
                        ? lead(f1, f2)
                        : lead(f1, f2, f3)
                : f2 == null
                    ? lag(f1)
                    : f3 == null
                        ? lag(f1, f2)
                        : lag(f1, f2, f3), null);
        }

        return null;
    }

    private static final Field<?> parseFieldFirstValueIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "FIRST_VALUE")) {
            parse(ctx, '(');
            Field<Void> arg = (Field) parseField(ctx);
            parse(ctx, ')');
            return parseWindowFunction(ctx, firstValue(arg), null);
        }

        return null;
    }

    private static final Field<?> parseFieldLastValueIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "LAST_VALUE")) {
            parse(ctx, '(');
            Field<Void> arg = (Field) parseField(ctx);
            parse(ctx, ')');
            return parseWindowFunction(ctx, lastValue(arg), null);
        }

        return null;
    }

    private static final Field<?> parseFieldNthValueIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "NTH_VALUE")) {
            parse(ctx, '(');
            Field<?> f1 = parseField(ctx);
            parse(ctx, ',');
            int f2 = (int) (long) parseUnsignedInteger(ctx);
            parse(ctx, ')');
            return parseWindowFunction(ctx, nthValue(f1, f2), null);
        }

        return null;
    }

    private static final Field<?> parseWindowFunction(ParserContext ctx, WindowIgnoreNullsStep s1, WindowOverStep<?> s2) {
        if (s1 != null) {








                s2 = s1;
        }

        parseKeyword(ctx, "OVER");
        Object nameOrSpecification = parseWindowNameOrSpecification(ctx, true);

        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=494897
        Field<?> result = (nameOrSpecification instanceof Name)
            ? s2.over((Name) nameOrSpecification)
            : (nameOrSpecification instanceof WindowSpecification)
            ? s2.over((WindowSpecification) nameOrSpecification)
            : s2.over();

        return result;
    }

    private static final AggregateFunction<?> parseBinarySetFunctionIf(ParserContext ctx) {
        Field<? extends Number> arg1;
        Field<? extends Number> arg2;
        BinarySetFunctionType type = parseBinarySetFunctionTypeIf(ctx);

        if (type == null)
            return null;

        parse(ctx, '(');
        arg1 = (Field) toField(ctx, parseSum(ctx, N));
        parse(ctx, ',');
        arg2 = (Field) toField(ctx, parseSum(ctx, N));
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

    private static final WindowBeforeOverStep<?> parseOrderedSetFunctionIf(ParserContext ctx) {
        // TODO Listagg set function
        OrderedAggregateFunction<?> orderedN;
        OrderedAggregateFunctionOfDeferredType ordered1;

        orderedN = parseHypotheticalSetFunctionIf(ctx);
        if (orderedN == null)
            orderedN = parseInverseDistributionFunctionIf(ctx);
        if (orderedN == null)
            orderedN = parseListaggFunctionIf(ctx);
        if (orderedN != null)
            return parseWithinGroupN(ctx, orderedN);

        ordered1 = parseModeIf(ctx);
        if (ordered1 != null)
            return parseWithinGroup1(ctx, ordered1);

        return null;
    }

    private static final AggregateFilterStep<?> parseWithinGroupN(ParserContext ctx, OrderedAggregateFunction<?> ordered) {
        parseKeyword(ctx, "WITHIN GROUP");
        parse(ctx, '(');
        parseKeyword(ctx, "ORDER BY");
        AggregateFilterStep<?> result = ordered.withinGroupOrderBy(parseSortSpecification(ctx));
        parse(ctx, ')');
        return result;
    }

    private static final AggregateFilterStep<?> parseWithinGroup1(ParserContext ctx, OrderedAggregateFunctionOfDeferredType ordered) {
        parseKeyword(ctx, "WITHIN GROUP");
        parse(ctx, '(');
        parseKeyword(ctx, "ORDER BY");
        AggregateFilterStep<?> result = ordered.withinGroupOrderBy(parseSortField(ctx));
        parse(ctx, ')');
        return result;
    }

    private static final OrderedAggregateFunction<?> parseHypotheticalSetFunctionIf(ParserContext ctx) {

        // This currently never parses hypothetical set functions, as the function names are already
        // consumed earlier in parseFieldTerm(). We should implement backtracking...
        OrderedAggregateFunction<?> ordered;

        if (parseKeywordIf(ctx, "RANK")) {
            parse(ctx, '(');
            ordered = rank(parseFields(ctx));
            parse(ctx, ')');
        }
        else if (parseKeywordIf(ctx, "DENSE_RANK")) {
            parse(ctx, '(');
            ordered = denseRank(parseFields(ctx));
            parse(ctx, ')');
        }
        else if (parseKeywordIf(ctx, "PERCENT_RANK")) {
            parse(ctx, '(');
            ordered = percentRank(parseFields(ctx));
            parse(ctx, ')');
        }
        else if (parseKeywordIf(ctx, "CUME_DIST")) {
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

        if (parseKeywordIf(ctx, "PERCENTILE_CONT")) {
            parse(ctx, '(');
            ordered = percentileCont(parseFieldUnsignedNumericLiteral(ctx, Sign.NONE));
            parse(ctx, ')');
        }
        else if (parseKeywordIf(ctx, "PERCENTILE_DISC")) {
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

        if (parseKeywordIf(ctx, "LISTAGG")) {
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

        if (parseKeywordIf(ctx, "MODE")) {
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
                throw ctx.unexpectedToken();
        }
    }

    private static final AggregateFunction<?> parseCountIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "COUNT")) {
            parse(ctx, '(');
            if (parseIf(ctx, '*')) {
                parse(ctx, ')');
                return count();
            }

            boolean distinct = parseSetQuantifier(ctx);
            List<Field<?>> fields = distinct
                ? parseFields(ctx)
                : Collections.<Field<?>>singletonList(parseField(ctx));
            parse(ctx, ')');

            if (distinct)
                if (fields.size() > 0)
                    return countDistinct(fields.toArray(EMPTY_FIELD));
                else
                    return countDistinct(fields.get(0));
            else
                return count(fields.get(0));
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

    private static final Schema parseSchemaName(ParserContext ctx) {
        return schema(parseName(ctx));
    }

    private static final Table<?> parseTableName(ParserContext ctx) {
        return table(parseName(ctx));
    }

    private static final TableField<?, ?> parseFieldName(ParserContext ctx) {
        return (TableField<?, ?>) field(parseName(ctx));
    }

    private static final List<Field<?>> parseFieldNames(ParserContext ctx) {
        List<Field<?>> result = new ArrayList<Field<?>>();

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
        return parseName(ctx);
    }

    private static final Name parseName(ParserContext ctx) {
        List<Name> result = new ArrayList<Name>();
        result.add(parseIdentifier(ctx));

        while (parseIf(ctx, '.'))
            result.add(parseIdentifier(ctx));

        return result.size() == 1 ? result.get(0) : DSL.name(result.toArray(EMPTY_NAME));
    }

    private static final List<Name> parseIdentifiers(ParserContext ctx) {
        LinkedHashSet<Name> result = new LinkedHashSet<Name>();

        do {
            if (!result.add(parseIdentifier(ctx)))
                throw ctx.exception("Duplicate identifier encountered");
        }
        while (parseIf(ctx, ','));
        return new ArrayList<Name>(result);
    }

    private static final Name parseIdentifier(ParserContext ctx) {
        Name result = parseIdentifierIf(ctx);

        if (result == null)
            throw ctx.expected("Identifier");

        return result;
    }

    private static final Name parseIdentifierIf(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        char quoteEnd =
            parseIf(ctx, '"') ? '"'
          : parseIf(ctx, '`') ? '`'
          : parseIf(ctx, '[') ? ']'
          : 0;

        int start = ctx.position;
        if (quoteEnd != 0)
            while (ctx.character() != quoteEnd && ctx.position < ctx.sql.length)
                ctx.position = ctx.position + 1;
        else
            while (ctx.isIdentifierPart() && ctx.position < ctx.sql.length)
                ctx.position = ctx.position + 1;


        if (ctx.position == start)
            return null;

        String result = new String(ctx.sql, start, ctx.position - start);

        if (quoteEnd != 0) {
            if (ctx.character() != quoteEnd)
                throw ctx.exception("Quoted identifier must terminate in " + quoteEnd);

            ctx.position = ctx.position + 1;
            return DSL.quotedName(result);
        }
        else
            return DSL.unquotedName(result);
    }

    private static final DataType<?> parseDataType(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        switch (ctx.character()) {
            case 'b':
            case 'B':
                if (parseKeywordIf(ctx, "BIGINT UNSIGNED"))
                    return SQLDataType.BIGINTUNSIGNED;
                else if (parseKeywordIf(ctx, "BIGINT"))
                    return SQLDataType.BIGINT;
                else if (parseKeywordIf(ctx, "BINARY"))
                    return parseDataTypeLength(ctx, SQLDataType.BINARY);
                else if (parseKeywordIf(ctx, "BIT"))
                    return SQLDataType.BIT;
                else if (parseKeywordIf(ctx, "BLOB"))
                    return SQLDataType.BLOB;
                else if (parseKeywordIf(ctx, "BOOLEAN"))
                    return SQLDataType.BOOLEAN;
                else
                    throw ctx.unexpectedToken();

            case 'c':
            case 'C':
                if (parseKeywordIf(ctx, "CHAR"))
                    return parseDataTypeLength(ctx, SQLDataType.CHAR);
                else if (parseKeywordIf(ctx, "CLOB"))
                    return parseDataTypeLength(ctx, SQLDataType.CLOB);
                else
                    throw ctx.unexpectedToken();

            case 'd':
            case 'D':
                if (parseKeywordIf(ctx, "DATE"))
                    return SQLDataType.DATE;
                else if (parseKeywordIf(ctx, "DECIMAL"))
                    return parseDataTypePrecisionScale(ctx, SQLDataType.DECIMAL);
                else if (parseKeywordIf(ctx, "DOUBLE PRECISION") ||
                         parseKeywordIf(ctx, "DOUBLE"))
                    return SQLDataType.DOUBLE;
                else
                    throw ctx.unexpectedToken();

            case 'f':
            case 'F':
                if (parseKeywordIf(ctx, "FLOAT"))
                    return SQLDataType.FLOAT;
                else
                    throw ctx.unexpectedToken();

            case 'i':
            case 'I':
                if (parseKeywordIf(ctx, "INTEGER UNSIGNED") ||
                    parseKeywordIf(ctx, "INT UNSIGNED"))
                    return SQLDataType.INTEGERUNSIGNED;
                else if (parseKeywordIf(ctx, "INTEGER") ||
                         parseKeywordIf(ctx, "INT"))
                    return SQLDataType.INTEGER;
                else
                    throw ctx.unexpectedToken();

            case 'l':
            case 'L':
                if (parseKeywordIf(ctx, "LONGBLOB"))
                    return SQLDataType.BLOB;
                else if (parseKeywordIf(ctx, "LONGTEXT"))
                    return SQLDataType.CLOB;
                else if (parseKeywordIf(ctx, "LONG NVARCHAR"))
                    return parseDataTypeLength(ctx, SQLDataType.LONGNVARCHAR);
                else if (parseKeywordIf(ctx, "LONG VARBINARY"))
                    return parseDataTypeLength(ctx, SQLDataType.LONGVARBINARY);
                else if (parseKeywordIf(ctx, "LONG VARCHAR"))
                    return parseDataTypeLength(ctx, SQLDataType.LONGVARCHAR);
                else
                    throw ctx.unexpectedToken();

            case 'm':
            case 'M':
                if (parseKeywordIf(ctx, "MEDIUMBLOB"))
                    return SQLDataType.BLOB;
                else if (parseKeywordIf(ctx, "MEDIUMINT UNSIGNED"))
                    return SQLDataType.INTEGERUNSIGNED;
                else if (parseKeywordIf(ctx, "MEDIUMINT"))
                    return SQLDataType.INTEGER;
                else if (parseKeywordIf(ctx, "MEDIUMTEXT"))
                    return SQLDataType.CLOB;
                else
                    throw ctx.unexpectedToken();

            case 'n':
            case 'N':
                if (parseKeywordIf(ctx, "NCHAR"))
                    return parseDataTypeLength(ctx, SQLDataType.NCHAR);
                else if (parseKeywordIf(ctx, "NCLOB"))
                    return SQLDataType.NCLOB;
                else if (parseKeywordIf(ctx, "NUMBER") ||
                         parseKeywordIf(ctx, "NUMERIC"))
                    return parseDataTypePrecisionScale(ctx, SQLDataType.NUMERIC);
                else if (parseKeywordIf(ctx, "NVARCHAR"))
                    return parseDataTypeLength(ctx, SQLDataType.NVARCHAR);
                else
                    throw ctx.unexpectedToken();

            case 'r':
            case 'R':
                if (parseKeywordIf(ctx, "REAL"))
                    return SQLDataType.REAL;
                else
                    throw ctx.unexpectedToken();

            case 's':
            case 'S':
                if (parseKeywordIf(ctx, "SERIAL4") || parseKeywordIf(ctx, "SERIAL"))
                    return SQLDataType.INTEGER.identity(true);
                else if (parseKeywordIf(ctx, "SERIAL8"))
                    return SQLDataType.BIGINT.identity(true);
                else if (parseKeywordIf(ctx, "SMALLINT UNSIGNED"))
                    return SQLDataType.SMALLINTUNSIGNED;
                else if (parseKeywordIf(ctx, "SMALLINT"))
                    return SQLDataType.SMALLINT;
                else
                    throw ctx.unexpectedToken();

            case 't':
            case 'T':
                if (parseKeywordIf(ctx, "TEXT"))
                    return SQLDataType.CLOB;
                else if (parseKeywordIf(ctx, "TIMESTAMP WITH TIME ZONE") ||
                         parseKeywordIf(ctx, "TIMESTAMPTZ"))
                    return SQLDataType.TIMESTAMPWITHTIMEZONE;

                else if (parseKeywordIf(ctx, "TIMESTAMP"))
                    return SQLDataType.TIMESTAMP;

                else if (parseKeywordIf(ctx, "TIME WITH TIME ZONE") ||
                         parseKeywordIf(ctx, "TIMETZ"))
                    return SQLDataType.TIMEWITHTIMEZONE;

                else if (parseKeywordIf(ctx, "TIME"))
                    return SQLDataType.TIME;

                else if (parseKeywordIf(ctx, "TINYBLOB"))
                    return SQLDataType.BLOB;
                else if (parseKeywordIf(ctx, "TINYINT UNSIGNED"))
                    return SQLDataType.TINYINTUNSIGNED;
                else if (parseKeywordIf(ctx, "TINYINT"))
                    return SQLDataType.TINYINT;
                else if (parseKeywordIf(ctx, "TINYTEXT"))
                    return SQLDataType.CLOB;
                else
                    throw ctx.unexpectedToken();

            case 'u':
            case 'U':
                if (parseKeywordIf(ctx, "UUID"))
                    return SQLDataType.UUID;
                else
                    throw ctx.unexpectedToken();

            case 'v':
            case 'V':
                if (parseKeywordIf(ctx, "VARCHAR") ||
                    parseKeywordIf(ctx, "VARCHAR2") ||
                    parseKeywordIf(ctx, "CHARACTER VARYING"))
                    return parseDataTypeLength(ctx, SQLDataType.VARCHAR);
                else if (parseKeywordIf(ctx, "VARBINARY"))
                    return parseDataTypeLength(ctx, SQLDataType.VARBINARY);
                else
                    throw ctx.unexpectedToken();

        }

        throw ctx.unexpectedToken();
    }

    private static final DataType<?> parseDataTypeLength(ParserContext ctx, DataType<?> result) {
        if (parseIf(ctx, '(')) {
            result = result.length((int) (long) parseUnsignedInteger(ctx));
            parse(ctx, ')');
        }

        return result;
    }

    private static final DataType<?> parseDataTypePrecisionScale(ParserContext ctx, DataType<?> result) {
        if (parseIf(ctx, '(')) {
            int precision = (int) (long) parseUnsignedInteger(ctx);

            if (parseIf(ctx, ','))
                result = result.precision(precision, (int) (long) parseUnsignedInteger(ctx));
            else
                result = result.precision(precision);

            parse(ctx, ')');
        }

        return result;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Literal parsing
    // -----------------------------------------------------------------------------------------------------------------

    private static final char parseCharacterLiteral(ParserContext ctx) {
        parseWhitespaceIf(ctx);
        parse(ctx, '\'');

        char c = ctx.character();

        // TODO MySQL string escaping...
        if (c == '\'')
            parse(ctx, '\'');

        ctx.position = ctx.position + 1;
        parse(ctx, '\'');
        return c;
    }

    private static final Field<?> parseBindVariable(ParserContext ctx) {
        switch (ctx.character()) {
            case '?':
                parse(ctx, '?');
                return DSL.val(null, Object.class);

            case ':':
                parse(ctx, ':');
                return DSL.param(parseIdentifier(ctx).last());

            default:
                throw ctx.exception("Illegal bind variable character");
        }
    }

    private static final String parseStringLiteral(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        if (parseIf(ctx, 'q'))
            return parseOracleQuotedStringLiteral(ctx);
        else
            return parseUnquotedStringLiteral(ctx);
    }

    private static final byte[] parseBinaryLiteralIf(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        if (parseIf(ctx, "X'") || parseIf(ctx, "x'")) {
            if (parseIf(ctx, '\''))
                return EMPTY_BYTE;

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            char c1 = 0;
            char c2 = 0;

            do {
                while (ctx.position < ctx.sql.length) {
                    c1 = ctx.character(ctx.position);

                    if (c1 == ' ')
                        ctx.position = ctx.position + 1;
                    else
                        break;
                }

                c2 = ctx.character(ctx.position + 1);

                if (c1 == '\'')
                    break;
                if (c2 == '\'')
                    throw ctx.unexpectedToken();

                try {
                    buffer.write(Integer.parseInt("" + c1 + c2, 16));
                }
                catch (NumberFormatException e) {
                    throw ctx.exception("Illegal character for binary literal");
                }
            }
            while ((ctx.position = ctx.position + 2) < ctx.sql.length);

            if (c1 == '\'') {
                ctx.position = ctx.position + 1;
                return buffer.toByteArray();
            }

            throw ctx.exception("Binary literal not terminated");
        }

        return null;
    }

    private static final String parseOracleQuotedStringLiteral(ParserContext ctx) {
        parse(ctx, '\'');

        char start = ctx.character();
        char end;

        switch (start) {
            case '!': end = '!'; ctx.position = ctx.position + 1; break;
            case '[': end = ']'; ctx.position = ctx.position + 1; break;
            case '{': end = '}'; ctx.position = ctx.position + 1; break;
            case '(': end = ')'; ctx.position = ctx.position + 1; break;
            case '<': end = '>'; ctx.position = ctx.position + 1; break;
            default:
                throw ctx.exception("Illegal quote string character");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = ctx.position; i < ctx.sql.length; i++) {
            char c = ctx.character(i);

            if (c == end)
                if (ctx.character(i + 1) == '\'') {
                    ctx.position = i + 2;
                    return sb.toString();
                }
                else {
                    i++;
                }

            sb.append(c);
        }

        throw ctx.exception("Quoted string literal not terminated");
    }

    private static final String parseUnquotedStringLiteral(ParserContext ctx) {
        parse(ctx, '\'');

        StringBuilder sb = new StringBuilder();
        for (int i = ctx.position; i < ctx.sql.length; i++) {
            char c = ctx.character(i);

            // TODO MySQL string escaping...
            if (c == '\'')
                if (ctx.character(i + 1) == '\'') {
                    i++;
                }
                else {
                    ctx.position = i + 1;
                    return sb.toString();
                }

            sb.append(c);
        }

        throw ctx.exception("String literal not terminated");
    }

    private static final Field<Number> parseFieldUnsignedNumericLiteral(ParserContext ctx, Sign sign) {
        Field<Number> result = parseFieldUnsignedNumericLiteralIf(ctx, sign);

        if (result == null)
            throw ctx.unexpectedToken();

        return result;
    }

    private static final Field<Number> parseFieldUnsignedNumericLiteralIf(ParserContext ctx, Sign sign) {
        Number r = parseUnsignedNumericLiteralIf(ctx, sign);
        return r == null ? null : inline(r);
    }

    private static final Number parseUnsignedNumericLiteralIf(ParserContext ctx, Sign sign) {
        StringBuilder sb = new StringBuilder();
        char c;

        for (;;) {
            c = ctx.character();
            if (c >= '0' && c <= '9') {
                sb.append(c);
                ctx.position = ctx.position + 1;
            }
            else
                break;
        }

        if (c == '.') {
            sb.append(c);
            ctx.position = ctx.position + 1;
        }
        else {
            if (sb.length() == 0)
                return null;

            try {
                return sign == Sign.MINUS
                    ? -Long.valueOf(sb.toString())
                    : Long.valueOf(sb.toString());
            }
            catch (Exception e1) {
                return sign == Sign.MINUS
                    ? new BigInteger(sb.toString()).negate()
                    : new BigInteger(sb.toString());
            }
        }

        for (;;) {
            c = ctx.character();
            if (c >= '0' && c <= '9') {
                sb.append(c);
                ctx.position = ctx.position + 1;
            }
            else
                break;
        }

        if (sb.length() == 0)
            return null;

        return sign == Sign.MINUS
            ? new BigDecimal(sb.toString()).negate()
            : new BigDecimal(sb.toString());
        // TODO add floating point support
    }

    private static final Long parseSignedInteger(ParserContext ctx) {
        Long result = parseSignedIntegerIf(ctx);

        if (result == null)
            throw ctx.expected("Signed integer");

        return result;
    }

    private static final Long parseSignedIntegerIf(ParserContext ctx) {
        parseWhitespaceIf(ctx);

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

    private static final Long parseUnsignedInteger(ParserContext ctx) {
        Long result = parseUnsignedIntegerIf(ctx);

        if (result == null)
            throw ctx.expected("Unsigned integer");

        return result;
    }

    private static final Long parseUnsignedIntegerIf(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        StringBuilder sb = new StringBuilder();
        char c;

        for (;;) {
            c = ctx.character();
            if (c >= '0' && c <= '9') {
                sb.append(c);
                ctx.position = ctx.position + 1;
            }
            else
                break;
        }

        if (sb.length() == 0)
            return null;

        return Long.valueOf(sb.toString());
    }

    private static final JoinType parseJoinTypeIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "CROSS JOIN"))
            return JoinType.CROSS_JOIN;
        else if (parseKeywordIf(ctx, "CROSS APPLY"))
            return JoinType.CROSS_APPLY;
        else if (parseKeywordIf(ctx, "CROSS JOIN"))
            return JoinType.CROSS_JOIN;
        else if (parseKeywordIf(ctx, "INNER")) {
            parseKeyword(ctx, "JOIN");
            return JoinType.JOIN;
        }
        else if (parseKeywordIf(ctx, "JOIN"))
            return JoinType.JOIN;
        else if (parseKeywordIf(ctx, "LEFT")) {
            parseKeywordIf(ctx, "OUTER");
            parseKeyword(ctx, "JOIN");
            return JoinType.LEFT_OUTER_JOIN;
        }
        else if (parseKeywordIf(ctx, "RIGHT")) {
            parseKeywordIf(ctx, "OUTER");
            parseKeyword(ctx, "JOIN");
            return JoinType.RIGHT_OUTER_JOIN;
        }
        else if (parseKeywordIf(ctx, "FULL")) {
            parseKeywordIf(ctx, "OUTER");
            parseKeyword(ctx, "JOIN");
            return JoinType.FULL_OUTER_JOIN;
        }
        else if (parseKeywordIf(ctx, "OUTER APPLY"))
            return JoinType.OUTER_APPLY;
        else if (parseKeywordIf(ctx, "NATURAL")) {
            if (parseKeywordIf(ctx, "LEFT")) {
                parseKeywordIf(ctx, "OUTER");
                parseKeyword(ctx, "JOIN");
                return JoinType.NATURAL_LEFT_OUTER_JOIN;
            }
            else if (parseKeywordIf(ctx, "RIGHT")) {
                parseKeywordIf(ctx, "OUTER");
                parseKeyword(ctx, "JOIN");
                return JoinType.NATURAL_RIGHT_OUTER_JOIN;
            }
            else if (parseKeywordIf(ctx, "JOIN"))
                return JoinType.NATURAL_JOIN;
        }

        return null;
        // TODO partitioned join
    }

    private static final TruthValue parseTruthValueIf(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        if (parseKeywordIf(ctx, "TRUE"))
            return TruthValue.TRUE;
        else if (parseKeywordIf(ctx, "FALSE"))
            return TruthValue.FALSE;
        else if (parseKeywordIf(ctx, "NULL"))
            return TruthValue.NULL;

        return null;
    }

    private static final CombineOperator parseCombineOperatorIf(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        if (parseKeywordIf(ctx, "UNION"))
            if (parseKeywordIf(ctx, "ALL"))
                return CombineOperator.UNION_ALL;
            else if (parseKeywordIf(ctx, "DISTINCT"))
                return CombineOperator.UNION;
            else
                return CombineOperator.UNION;
        else if (parseKeywordIf(ctx, "EXCEPT") || parseKeywordIf(ctx, "MINUS"))
            if (parseKeywordIf(ctx, "ALL"))
                return CombineOperator.EXCEPT_ALL;
            else if (parseKeywordIf(ctx, "DISTINCT"))
                return CombineOperator.EXCEPT;
            else
                return CombineOperator.EXCEPT;
        else if (parseKeywordIf(ctx, "INTERSECT"))
            if (parseKeywordIf(ctx, "ALL"))
                return CombineOperator.INTERSECT_ALL;
            else if (parseKeywordIf(ctx, "DISTINCT"))
                return CombineOperator.INTERSECT;
            else
                return CombineOperator.INTERSECT;

        return null;
    }

    private static final ComputationalOperation parseComputationalOperationIf(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        if (parseKeywordIf(ctx, "AVG"))
            return ComputationalOperation.AVG;
        else if (parseKeywordIf(ctx, "MAX"))
            return ComputationalOperation.MAX;
        else if (parseKeywordIf(ctx, "MIN"))
            return ComputationalOperation.MIN;
        else if (parseKeywordIf(ctx, "SUM"))
            return ComputationalOperation.SUM;
        else if (parseKeywordIf(ctx, "MEDIAN"))
            return ComputationalOperation.MEDIAN;
        else if (parseKeywordIf(ctx, "EVERY") || parseKeywordIf(ctx, "BOOL_AND"))
            return ComputationalOperation.EVERY;
        else if (parseKeywordIf(ctx, "ANY") || parseKeywordIf(ctx, "SOME") || parseKeywordIf(ctx, "BOOL_OR"))
            return ComputationalOperation.ANY;
        else if (parseKeywordIf(ctx, "STDDEV_POP"))
            return ComputationalOperation.STDDEV_POP;
        else if (parseKeywordIf(ctx, "STDDEV_SAMP"))
            return ComputationalOperation.STDDEV_SAMP;
        else if (parseKeywordIf(ctx, "VAR_POP"))
            return ComputationalOperation.VAR_POP;
        else if (parseKeywordIf(ctx, "VAR_SAMP"))
            return ComputationalOperation.VAR_SAMP;

        return null;
    }

    private static final BinarySetFunctionType parseBinarySetFunctionTypeIf(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        // TODO speed this up
        for (BinarySetFunctionType type : BinarySetFunctionType.values())
            if (parseKeywordIf(ctx, type.name()))
                return type;

        return null;
    }

    private static final Comparator parseComparatorIf(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        if (parseIf(ctx, "="))
            return Comparator.EQUALS;
        else if (parseIf(ctx, "!=") || parseIf(ctx, "<>"))
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

    // -----------------------------------------------------------------------------------------------------------------
    // Other tokens
    // -----------------------------------------------------------------------------------------------------------------

    private static final boolean parseIf(ParserContext ctx, String string) {
        parseWhitespaceIf(ctx);
        int length = string.length();

        ctx.expectedTokens.add(string);
        if (ctx.sql.length < ctx.position + length)
            return false;

        for (int i = 0; i < length; i++) {
            char c = string.charAt(i);
            if (ctx.sql[ctx.position + i] != c)
                return false;
        }

        ctx.position = ctx.position + length;
        ctx.expectedTokens.clear();
        return true;
    }

    private static final boolean parseIf(ParserContext ctx, char c) {
        parseWhitespaceIf(ctx);

        if (ctx.character() != c)
            return false;

        ctx.position = ctx.position + 1;
        return true;
    }

    private static final void parse(ParserContext ctx, char c) {
        if (!parseIf(ctx, c))
            throw ctx.unexpectedToken();
    }

    private static final void parseKeyword(ParserContext ctx, String string) {
        if (!parseKeywordIf(ctx, string))
            throw ctx.unexpectedToken();
    }

    private static final boolean parseKeywordIf(ParserContext ctx, String string) {
        ctx.expectedTokens.add(string);

        if (peekKeyword(ctx, string, true, false))
            ctx.expectedTokens.clear();
        else
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
        return peekKeyword(ctx, keyword, false, false);
    }

    private static final boolean peekKeyword(ParserContext ctx, String keyword, boolean updatePosition, boolean peekIntoParens) {
        parseWhitespaceIf(ctx);
        int length = keyword.length();
        int skip;

        if (ctx.sql.length < ctx.position + length)
            return false;

        // TODO is this correct?
        skipLoop:
        for (skip = 0; ctx.position + skip < ctx.sql.length; skip++) {
            char c = ctx.character(ctx.position + skip);

            switch (c) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    continue skipLoop;

                case '(':
                    if (peekIntoParens)
                        continue skipLoop;
                    else
                        break skipLoop;

                default:
                    break skipLoop;
            }
        }


        for (int i = 0; i < length; i++) {
            char c = keyword.charAt(i);

            switch (c) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    skip = skip + (afterWhitespace(ctx, ctx.position + i + skip) - ctx.position - i - 1);
                    break;

                default:
                    if (upper(ctx.sql[ctx.position + i + skip]) != keyword.charAt(i))
                        return false;

                    break;
            }
        }

        if (ctx.isIdentifierPart(ctx.position + length + skip))
            return false;

        if (updatePosition)
            ctx.position = ctx.position + length + skip;

        return true;
    }

    private static final boolean parseWhitespaceIf(ParserContext ctx) {
        int position = ctx.position;
        ctx.position = afterWhitespace(ctx, ctx.position);
        return position != ctx.position;
    }

    private static final int afterWhitespace(ParserContext ctx, int position) {
        loop:
        for (int i = position; i < ctx.sql.length; i++) {
            switch (ctx.sql[i]) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    position = i + 1;
                    continue loop;

                case '/':
                    if (i + 1 < ctx.sql.length && ctx.sql[i + 1] == '*') {
                        i = i + 2;

                        while (i < ctx.sql.length) {
                            switch (ctx.sql[i]) {
                                case '*':
                                    if (i + 1 < ctx.sql.length && ctx.sql[i + 1] == '/') {
                                        position = i = i + 1;
                                        continue loop;
                                    }

                                    // No break
                                default:
                                    i++;
                            }
                        }
                    }

                    break loop;

                case '-':
                    if (i + 1 < ctx.sql.length && ctx.sql[i + 1] == '-') {
                        i = i + 2;

                        while (i < ctx.sql.length) {
                            switch (ctx.sql[i]) {
                                case '\r':
                                case '\n':
                                    position = i;
                                    continue loop;

                                default:
                                    i++;
                            }
                        }
                    }

                    break loop;

                    // TODO MySQL comments require a whitespace after --. Should we deal with this?
                    // TODO Some databases also support # as a single line comment character.


                // TODO support Oracle-style hints
                default:
                    position = i;
                    break loop;
            }
        }

        return position;
    }

    private static final char upper(char c) {
        return c >= 'a' && c <= 'z' ? (char) (c - ('a' - 'A')) : c;
    }

    public static final void main(String[] args) {
        System.out.println(new ParserImpl(new DefaultConfiguration()).parse(
            "DROP INDEX   y on a.b.c"
        ));
    }

    static final class ParserContext {
        private final DSLContext   dsl;
        private final String       sqlString;
        private final char[]       sql;
        private final List<String> expectedTokens;
        private int                position = 0;

        ParserContext(DSLContext dsl, String sqlString) {
            this.dsl = dsl;
            this.sqlString = sqlString;
            this.sql = sqlString.toCharArray();
            this.expectedTokens = new ArrayList<String>();
        }

        ParserException internalError() {
            return exception("Internal Error");
        }

        ParserException expected(String object) {
            return new ParserException(mark(), object + " expected");
        }

        ParserException notImplemented(String feature) {
            return new ParserException(mark(), feature + " not yet implemented");
        }

        ParserException exception(String message) {
            return new ParserException(mark(), message);
        }

        ParserException unexpectedToken() {
            return new ParserException(mark(), "Expected tokens: " + new TreeSet<String>(expectedTokens));
        }

        char character() {
            return character(position);
        }

        char character(int pos) {
            return pos >= 0 && pos < sql.length ? sql[pos] : ' ';
        }

        boolean isWhitespace() {
            return Character.isWhitespace(character());
        }

        boolean isWhitespace(int pos) {
            return Character.isWhitespace(character(pos));
        }

        boolean isIdentifierPart() {
            return Character.isJavaIdentifierPart(character());
        }

        boolean isIdentifierPart(int pos) {
            return Character.isJavaIdentifierPart(character(pos));
        }

        boolean done() {
            return position >= sql.length;
        }

        String mark() {
            return sqlString.substring(Math.max(0, position - 50), position) + "[*]" + sqlString.substring(position, Math.min(sqlString.length(), position + 80));
        }

        @Override
        public String toString() {
            return mark();
        }
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

    private static final String[] SELECT_KEYWORDS = {
        "CONNECT",
        "CROSS",
        "EXCEPT",
        "FETCH",
        "FOR",
        "FROM",
        "FULL",
        "GROUP BY",
        "HAVING",
        "INNER",
        "INTERSECT",
        "INTO",
        "JOIN",
        "LEFT",
        "LIMIT",
        "MINUS",
        "NATURAL",
        "OFFSET",
        "ON",
        "ORDER BY",
        "OUTER",
        "PARTITION",
        "RETURNING",
        "RIGHT",
        "SELECT",
        "START",
        "UNION",
        "USING",
        "WHERE",
    };

    private static final String[] PIVOT_KEYWORDS = {
        "FOR"
    };
}
