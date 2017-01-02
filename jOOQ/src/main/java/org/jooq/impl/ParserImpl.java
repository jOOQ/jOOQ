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
import static org.jooq.exception.SQLStateSubclass.C42000_NO_SUBCLASS;
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
import static org.jooq.impl.DSL.ln;
import static org.jooq.impl.DSL.log;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.lpad;
import static org.jooq.impl.DSL.ltrim;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.maxDistinct;
import static org.jooq.impl.DSL.md5;
import static org.jooq.impl.DSL.mid;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.minDistinct;
import static org.jooq.impl.DSL.minute;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.nthValue;
import static org.jooq.impl.DSL.ntile;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.nvl2;
import static org.jooq.impl.DSL.octetLength;
import static org.jooq.impl.DSL.orderBy;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.percentRank;
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
import static org.jooq.impl.DSL.varPop;
import static org.jooq.impl.DSL.varSamp;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.DSL.year;
import static org.jooq.impl.ParserImpl.Type.B;
import static org.jooq.impl.ParserImpl.Type.D;
import static org.jooq.impl.ParserImpl.Type.N;
import static org.jooq.impl.ParserImpl.Type.S;
import static org.jooq.impl.Tools.EMPTY_COLLECTION;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.EMPTY_STRING;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.jooq.AggregateFunction;
import org.jooq.AlterIndexFinalStep;
import org.jooq.AlterIndexStep;
import org.jooq.AlterSchemaFinalStep;
import org.jooq.AlterSchemaStep;
import org.jooq.AlterTableDropStep;
import org.jooq.AlterTableFinalStep;
import org.jooq.AlterTableStep;
import org.jooq.CaseConditionStep;
import org.jooq.CaseValueStep;
import org.jooq.CaseWhenStep;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Constraint;
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
import org.jooq.DeleteFinalStep;
import org.jooq.DeleteWhereStep;
import org.jooq.DropIndexFinalStep;
import org.jooq.DropIndexOnStep;
import org.jooq.DropSchemaFinalStep;
import org.jooq.DropSchemaStep;
import org.jooq.DropSequenceFinalStep;
import org.jooq.DropTableFinalStep;
import org.jooq.DropTableStep;
import org.jooq.DropViewFinalStep;
import org.jooq.Field;
import org.jooq.GroupField;
import org.jooq.Insert;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStepN;
import org.jooq.JoinType;
import org.jooq.Merge;
import org.jooq.MergeFinalStep;
import org.jooq.MergeMatchedStep;
import org.jooq.MergeNotMatchedStep;
import org.jooq.Name;
import org.jooq.Parser;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Sequence;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOptionalOnStep;
import org.jooq.Truncate;
import org.jooq.TruncateCascadeStep;
import org.jooq.TruncateFinalStep;
import org.jooq.TruncateIdentityStep;
import org.jooq.Update;
import org.jooq.WindowBeforeOverStep;
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowOverStep;
import org.jooq.WindowSpecification;
import org.jooq.WindowSpecificationOrderByStep;
import org.jooq.WindowSpecificationRowsAndStep;
import org.jooq.WindowSpecificationRowsStep;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.SQLStateSubclass;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Deprecated
class ParserImpl implements Parser {

    private final Configuration configuration;
    private final DSLContext    dsl;

    ParserImpl(Configuration configuration) {
        this.configuration = configuration;
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
            result.add(parseQuery(ctx));
        }
        while (parseIf(ctx, ";"));

        if (!ctx.done())
            throw new ParserException(ctx);

        return new QueriesImpl(result);
    }

    @Override
    public final Query parseQuery(String sql) {
        ParserContext ctx = new ParserContext(dsl, sql);
        Query result = parseQuery(ctx);

        if (!ctx.done())
            throw new ParserException(ctx);

        return result;
    }

    @Override
    public final Table<?> parseTable(String sql) {
        ParserContext ctx = new ParserContext(dsl, sql);
        Table<?> result = parseTable(ctx);

        if (!ctx.done())
            throw new ParserException(ctx);

        return result;
    }

    @Override
    public final Field<?> parseField(String sql) {
        ParserContext ctx = new ParserContext(dsl, sql);
        Field<?> result = parseField(ctx);

        if (!ctx.done())
            throw new ParserException(ctx);

        return result;
    }

    @Override
    public final Condition parseCondition(String sql) {
        ParserContext ctx = new ParserContext(dsl, sql);
        Condition result = parseCondition(ctx);

        if (!ctx.done())
            throw new ParserException(ctx);

        return result;
    }

    @Override
    public final Name parseName(String sql) {
        ParserContext ctx = new ParserContext(dsl, sql);
        Name result = parseName(ctx);

        if (!ctx.done())
            throw new ParserException(ctx);

        return result;
    }

    static final Query parseQuery(ParserContext ctx) {
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

        throw ctx.exception();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Statement parsing
    // -----------------------------------------------------------------------------------------------------------------

    private static final SelectQueryImpl<Record> parseSelect(ParserContext ctx) {
        SelectQueryImpl<Record> result = parseQueryPrimary(ctx);
        CombineOperator combine;
        while ((combine = parseCombineOperatorIf(ctx)) != null) {
            switch (combine) {
                case UNION:
                    result = (SelectQueryImpl<Record>) result.union(parseQueryPrimary(ctx));
                    break;
                case UNION_ALL:
                    result = (SelectQueryImpl<Record>) result.unionAll(parseQueryPrimary(ctx));
                    break;
                case EXCEPT:
                    result = (SelectQueryImpl<Record>) result.except(parseQueryPrimary(ctx));
                    break;
                case EXCEPT_ALL:
                    result = (SelectQueryImpl<Record>) result.exceptAll(parseQueryPrimary(ctx));
                    break;
                case INTERSECT:
                    result = (SelectQueryImpl<Record>) result.intersect(parseQueryPrimary(ctx));
                    break;
                case INTERSECT_ALL:
                    result = (SelectQueryImpl<Record>) result.intersectAll(parseQueryPrimary(ctx));
                    break;
                default:
                    ctx.unexpectedToken();
                    break;
            }
        }

        if (parseKeywordIf(ctx, "ORDER BY"))
            result.addOrderBy(parseSortSpecification(ctx));

        if (!result.getLimit().isApplicable()) {
            boolean offsetStandard = false;
            boolean offsetPostgres = false;

            if (parseKeywordIf(ctx, "OFFSET")) {
                result.addOffset((int) (long) parseUnsignedInteger(ctx));

                if (parseKeywordIf(ctx, "ROWS") || parseKeywordIf(ctx, "ROW"))
                    offsetStandard = true;
                else
                    offsetPostgres = true;
            }

            if (!offsetStandard && parseKeywordIf(ctx, "LIMIT")) {
                int limit = (int) (long) parseUnsignedInteger(ctx);

                if (!offsetPostgres && parseIf(ctx, ','))
                    result.addLimit(limit, (int) (long) parseUnsignedInteger(ctx));
                else if (!offsetPostgres && parseKeywordIf(ctx, "OFFSET"))
                    result.addLimit((int) (long) parseUnsignedInteger(ctx), limit);
                else
                    result.addLimit(limit);
            }
            else if (!offsetPostgres && parseKeywordIf(ctx, "FETCH")) {
                if (!parseKeywordIf(ctx, "FIRST") && !parseKeywordIf(ctx, "NEXT"))
                    throw ctx.unexpectedToken();

                result.addLimit((int) (long) parseUnsignedInteger(ctx));

                if (!parseKeywordIf(ctx, "ROWS ONLY") && !parseKeywordIf(ctx, "ROW ONLY"))
                    throw ctx.unexpectedToken();
            }
        }
        // TODO FOR UPDATE, etc.

        return result;
    }

    private static final SelectQueryImpl<Record> parseQueryPrimary(ParserContext ctx) {
        if (parseIf(ctx, '(')) {
            SelectQueryImpl<Record> result = parseSelect(ctx);
            parse(ctx, ')');
            return result;
        }

        parseKeyword(ctx, "SELECT");
        boolean distinct = parseKeywordIf(ctx, "DISTINCT") || parseKeywordIf(ctx, "UNIQUE");
        Long limit = null;
        Long offset = null;

        if (!distinct)
            parseKeywordIf(ctx, "ALL");

        if (parseKeywordIf(ctx, "TOP")) {
            limit = parseUnsignedInteger(ctx);

            if (parseKeywordIf(ctx, "START AT"))
                offset = parseUnsignedInteger(ctx);
        }

        List<Field<?>> select = parseSelectList(ctx);
        List<Table<?>> from = null;
        Condition startWith = null;
        Condition connectBy = null;
        boolean connectByNoCycle = false;
        Condition where = null;
        List<GroupField> groupBy = null;
        Condition having = null;


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

        SelectQueryImpl<Record> result = (SelectQueryImpl<Record>) ctx.dsl.selectQuery();
        if (distinct)
            result.setDistinct(distinct);

        if (select.size() > 0)
            result.addSelect(select);

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

        // TODO Implement returning
        // boolean returning = parseKeywordIf(ctx, "RETURNING");


        DeleteWhereStep<?> s1;
        DeleteFinalStep<?> s2;

        s1 = ctx.dsl.delete(tableName);
        s2 = where
            ? s1.where(condition)
            : s1;

        return s2;
    }

    private static final Insert<?> parseInsert(ParserContext ctx) {
        parseKeyword(ctx, "INSERT INTO");
        Table<?> tableName = parseTableName(ctx);
        Field<?>[] fields = null;

        if (parseIf(ctx, '(')) {
            fields = Tools.fieldsByName(parseIdentifiers(ctx).toArray(EMPTY_STRING));
            parse(ctx, ')');
        }

        if (parseKeywordIf(ctx, "VALUES")) {
            List<List<Field<?>>> allValues = new ArrayList<List<Field<?>>>();

            do {
                parse(ctx, '(');
                List<Field<?>> values = parseFields(ctx);

                if (fields != null && fields.length != values.size())
                    throw ctx.exception();

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

            return step2;
        }
        else if (parseKeywordIf(ctx, "SET")) {
            Map<Field<?>, Object> map = parseSetClauseList(ctx);

            return ctx.dsl.insertInto(tableName).set(map);
        }
        else if (peekKeyword(ctx, "SELECT")){
            SelectQueryImpl<Record> select = parseSelect(ctx);

            return fields == null
                ? ctx.dsl.insertInto(tableName).select(select)
                : ctx.dsl.insertInto(tableName).columns(fields).select(select);
        }
        else if (parseKeywordIf(ctx, "DEFAULT VALUES")) {
            if (fields != null)
                throw ctx.exception();
            else
                return ctx.dsl.insertInto(tableName).defaultValues();
        }

        throw ctx.unexpectedToken();

        // TODO Support RETURNING
        // TODO Support ON DUPLICATE KEY UPDATE
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
        return condition == null
            ? ctx.dsl.update(tableName).set(map)
            : ctx.dsl.update(tableName).set(map).where(condition);
    }

    private static final Map<Field<?>, Object> parseSetClauseList(ParserContext ctx) {
        Map<Field<?>, Object> map = new LinkedHashMap<Field<?>, Object>();

        do {
            Field<?> field = parseFieldName(ctx);

            if (map.containsKey(field))
                throw ctx.exception();

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
        TableLike<?> using = parseSelect(ctx);
        parse(ctx, ')');
        if (parseKeywordIf(ctx, "AS"))
            using = using.asTable(parseIdentifier(ctx));
        parseKeyword(ctx, "ON");
        Condition on = parseCondition(ctx);
        boolean update = false;
        boolean insert = false;
        List<Field<?>> insertColumns = null;
        List<Field<?>> insertValues = null;
        Map<Field<?>, Object> updateSet = null;

        for (;;) {
            if (!update && (update = parseKeywordIf(ctx, "WHEN MATCHED THEN UPDATE SET"))) {
                updateSet = parseSetClauseList(ctx);
            }
            else if (!insert && (insert = parseKeywordIf(ctx, "WHEN NOT MATCHED THEN INSERT"))) {
                parse(ctx, '(');
                insertColumns = Arrays.asList(Tools.fieldsByName(parseIdentifiers(ctx)));
                parse(ctx, ')');
                parseKeyword(ctx, "VALUES");
                parse(ctx, '(');
                insertValues = parseFields(ctx);
                parse(ctx, ')');

                if (insertColumns.size() != insertValues.size())
                    throw ctx.exception();
            }
            else
                break;
        }

        if (!update && !insert)
            throw ctx.exception();

        // TODO support WHERE
        // TODO support multi clause MERGE
        // TODO support DELETE

        MergeMatchedStep<?> s1 = ctx.dsl.mergeInto(target).using(using).on(on);
        MergeNotMatchedStep<?> s2 = update ? s1.whenMatchedThenUpdate().set(updateSet) : s1;
        MergeFinalStep<?> s3 = insert ? s2.whenNotMatchedThenInsert(insertColumns).values(insertValues) : s2;

        return s3;
    }

    private static final DDLQuery parseCreate(ParserContext ctx) {
        parseKeyword(ctx, "CREATE");

        if (parseKeywordIf(ctx, "TABLE"))
            return parseCreateTable(ctx);
        if (parseKeywordIf(ctx, "INDEX"))
            return parseCreateIndex(ctx);
        else if (parseKeywordIf(ctx, "SCHEMA"))
            return parseCreateSchema(ctx);
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
            throw ctx.exception();


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

    private static final DDLQuery parseDropSequence(ParserContext ctx) {
        boolean ifExists = parseKeywordIf(ctx, "IF EXISTS");
        Sequence<?> sequenceName = parseSequenceName(ctx);

        DropSequenceFinalStep s1;

        s1 = ifExists
            ? ctx.dsl.dropSequenceIfExists(sequenceName)
            : ctx.dsl.dropSequence(sequenceName);

        return s1;
    }

    private static final DDLQuery parseCreateTable(ParserContext ctx) {
        boolean ifNotExists = parseKeywordIf(ctx, "IF NOT EXISTS");
        Table<?> tableName = parseTableName(ctx);

        if (parseKeywordIf(ctx, "AS")) {
            Select<?> select = parseSelect(ctx);

            CreateTableAsStep<Record> s1 = ifNotExists
                ? ctx.dsl.createTableIfNotExists(tableName)
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
                String fieldName = parseIdentifier(ctx);
                DataType<?> type = parseDataType(ctx);

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
                            type = type.defaultValue((Field) parseField(ctx));
                            defaultValue = true;
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

                    break;
                }

                fields.add(field(name(fieldName), type));
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
                            throw ctx.exception();
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
                            throw ctx.exception();

                        constraints.add(constraint == null
                            ? foreignKey(referencing).references(referencedTable, referencedFields)
                            : constraint.foreignKey(referencing).references(referencedTable, referencedFields));
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
                : ctx.dsl.createTable(tableName);
            CreateTableColumnStep s2 = s1.columns(fields);
            CreateTableConstraintStep s3 = constraints.isEmpty()
                ? s2
                : s2.constraints(constraints);

            return s3;
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
                            throw ctx.exception();

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

                        String fieldName = parseIdentifier(ctx);
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
                                    type = type.defaultValue(parseField(ctx));
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

                        return s1.add(field(name(fieldName), type), type);
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
                        String constraint = parseIdentifier(ctx);

                        return s1.dropConstraint(constraint);
                    }
                }

                break;

            case 'r':
            case 'R':
                if (parseKeywordIf(ctx, "RENAME")) {
                    if (parseKeywordIf(ctx, "TO")) {
                        String newName = parseIdentifier(ctx);

                        return s1.renameTo(newName);
                    }
                    else if (parseKeywordIf(ctx, "COLUMN")) {
                        String oldName = parseIdentifier(ctx);
                        parseKeyword(ctx, "TO");
                        String newName = parseIdentifier(ctx);

                        return s1.renameColumn(oldName).to(newName);
                    }
                    else if (parseKeywordIf(ctx, "CONSTRAINT")) {
                        String oldName = parseIdentifier(ctx);
                        parseKeyword(ctx, "TO");
                        String newName = parseIdentifier(ctx);

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

                    return ctx.dsl.alterTable(oldName.getTable().getName()).renameColumn(oldName).to(newName);
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

    private static final DDLQuery parseCreateIndex(ParserContext ctx) {
        boolean ifNotExists = parseKeywordIf(ctx, "IF NOT EXISTS");
        Name indexName = parseIndexName(ctx);
        parseKeyword(ctx, "ON");
        Table<?> tableName = parseTableName(ctx);
        parse(ctx, '(');
        Field<?>[] fieldNames = Tools.fieldsByName(parseIdentifiers(ctx));
        parse(ctx, ')');
        Condition condition = parseKeywordIf(ctx, "WHERE")
            ? parseCondition(ctx)
            : null;


        CreateIndexStep s1 = ifNotExists
            ? ctx.dsl.createIndexIfNotExists(indexName)
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

    static final Condition parseCondition(ParserContext ctx) {
        Condition condition = parseBooleanTerm(ctx);

        while (parseKeywordIf(ctx, "OR"))
            condition = condition.or(parseBooleanTerm(ctx));

        return condition;
    }

    private static final Condition parseBooleanTerm(ParserContext ctx) {
        Condition condition = parseBooleanFactor(ctx);

        while (parseKeywordIf(ctx, "AND"))
            condition = condition.and(parseBooleanFactor(ctx));

        return condition;
    }

    private static final Condition parseBooleanFactor(ParserContext ctx) {
        boolean not = parseKeywordIf(ctx, "NOT");
        Condition condition = parseBooleanTest(ctx);
        return not ? condition.not() : condition;
    }

    private static final Condition parseBooleanTest(ParserContext ctx) {
        Condition condition = parseBooleanPrimary(ctx);

        if (parseKeywordIf(ctx, "IS")) {
            Field<Boolean> field = field(condition);

            boolean not = parseKeywordIf(ctx, "NOT");
            TruthValue truth = parseTruthValue(ctx);

            switch (truth) {
                case FALSE:
                    return not ? field.ne(inline(false)) : field.eq(inline(false));
                case TRUE:
                    return not ? field.ne(inline(true)) : field.eq(inline(true));
                case NULL:
                    return not ? field.isNotNull() : field.isNull();
                default:
                    throw ctx.internalError();
            }
        }

        return condition;
    }

    private static final Condition parseBooleanPrimary(ParserContext ctx) {
        if (parseIf(ctx, '(')) {
            Condition result = parseCondition(ctx);
            parse(ctx, ')');
            return result;
        }

        TruthValue truth = parseTruthValueIf(ctx);
        if (truth != null) {
            Comparator comp = parseComparatorIf(ctx);

            switch (truth) {
                case TRUE:
                    return comp == null ? condition(true) : inline(true).compare(comp, (Field<Boolean>) parseField(ctx));
                case FALSE:
                    return comp == null ? condition(false) : inline(false).compare(comp, (Field<Boolean>) parseField(ctx));
                case NULL:
                    return comp == null ? condition((Boolean) null) : inline((Boolean) null).compare(comp, (Field<Boolean>) parseField(ctx));
                default:
                    throw ctx.exception();
            }
        }

        return parsePredicate(ctx);
    }

    private static final Condition parsePredicate(ParserContext ctx) {
        if (parseKeywordIf(ctx, "EXISTS")) {
            parse(ctx, '(');
            Select<?> select = parseSelect(ctx);
            parse(ctx, ')');

            return exists(select);
        }

        else {
            // TODO row value expressions
            Field left;
            Comparator comp;
            boolean not;

            left = parseFieldConcat(ctx, null);
            not = parseKeywordIf(ctx, "NOT");

            if (!not && (comp = parseComparatorIf(ctx)) != null) {
                boolean all = parseKeywordIf(ctx, "ALL");
                boolean any = !all && (parseKeywordIf(ctx, "ANY") || parseKeywordIf(ctx, "SOME"));
                if (all || any)
                    parse(ctx, '(');

                Condition result =
                      all
                    ? left.compare(comp, DSL.all(parseSelect(ctx)))
                    : any
                    ? left.compare(comp, DSL.any(parseSelect(ctx)))
                    : left.compare(comp, parseFieldConcat(ctx, null));

                if (all || any)
                    parse(ctx, ')');

                return result;
            }
            else if (!not && parseKeywordIf(ctx, "IS")) {
                not = parseKeywordIf(ctx, "NOT");

                if (parseKeywordIf(ctx, "NULL"))
                    return not ? left.isNotNull() : left.isNull();

                parseKeyword(ctx, "DISTINCT FROM");
                Field right = parseFieldConcat(ctx, null);
                return not ? left.isNotDistinctFrom(right) : left.isDistinctFrom(right);
            }
            else if (parseKeywordIf(ctx, "IN")) {
                Condition result;

                parse(ctx, '(');
                if (peekKeyword(ctx, "SELECT"))
                    result = not ? left.notIn(parseSelect(ctx)) : left.in(parseSelect(ctx));
                else
                    result = not ? left.notIn(parseFields(ctx)) : left.in(parseFields(ctx));

                parse(ctx, ')');
                return result;
            }
            else if (parseKeywordIf(ctx, "BETWEEN")) {
                boolean symmetric = parseKeywordIf(ctx, "SYMMETRIC");
                Field r1 = parseFieldConcat(ctx, null);
                parseKeyword(ctx, "AND");
                Field r2 = parseFieldConcat(ctx, null);
                return symmetric
                    ? not
                        ? left.notBetweenSymmetric(r1, r2)
                        : left.betweenSymmetric(r1, r2)
                    : not
                        ? left.notBetween(r1, r2)
                        : left.between(r1, r2);
            }
            else if (parseKeywordIf(ctx, "LIKE")) {
                Field right = parseFieldConcat(ctx, null);
                boolean escape = parseKeywordIf(ctx, "ESCAPE");
                char character = escape ? parseCharacterLiteral(ctx) : ' ';
                return escape
                    ? not
                        ? left.notLike(right, character)
                        : left.like(right, character)
                    : not
                        ? left.notLike(right)
                        : left.like(right);
            }
        }

        throw ctx.exception();
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
        return parseTablePrimary(ctx);
        // TODO Support SAMPLE clause
    }

    private static final Table<?> parseTablePrimary(ParserContext ctx) {
        Table<?> result = null;

        // TODO [#5306] Support FINAL TABLE (<data change statement>)
        if (parseKeywordIf(ctx, "LATERAL")) {
            parse(ctx, '(');
            result = lateral(parseSelect(ctx));
            parse(ctx, ')');
        }
        else if (parseKeywordIf(ctx, "UNNEST")) {
            // TODO
            throw ctx.exception();
        }
        else if (parseIf(ctx, '(')) {
            if (peekKeyword(ctx, "SELECT")) {
                result = table(parseSelect(ctx));
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
        }

        String alias = null;
        List<String> columnAliases = null;

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
                result = result.as(alias, columnAliases.toArray(EMPTY_STRING));
            else
                result = result.as(alias);
        }

        return result;
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

        Table<?> right = parseTableFactor(ctx);
        TableOptionalOnStep<?> result1 = left.join(right, joinType);
        Table<?> result2 = result1;

        switch (joinType) {
            case JOIN:
            case LEFT_OUTER_JOIN:
            case FULL_OUTER_JOIN:
            case RIGHT_OUTER_JOIN:
            case OUTER_APPLY: {
                boolean on = parseKeywordIf(ctx, "ON");

                if (on) {
                    result2 = result1.on(parseCondition(ctx));
                }
                else {
                    parseKeyword(ctx, "USING");
                    parse(ctx, '(');
                    result2 = result1.using(Tools.fieldsByName(parseIdentifiers(ctx).toArray(EMPTY_STRING)));
                    parse(ctx, ')');
                }

                break;
            }
        }

        return result2;
    }

    private static final List<Field<?>> parseSelectList(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        if (parseIf(ctx, '*'))
            return Collections.emptyList();

        // TODO Support qualified asterisk
        List<Field<?>> result = new ArrayList<Field<?>>();
        do {
            Field<?> field = parseField(ctx);
            String alias = null;

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

            result.add(sort);
        }
        while (parseIf(ctx, ','));
        return result;
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

    static final Field<?> parseField(ParserContext ctx) {
        return parseField(ctx, null);
    }

    static enum Type {
        D,
        S,
        N,
        B;

        boolean is(Type type) {
            return type == null || type == this;
        }
    }

    private static final Field<?> parseField(ParserContext ctx, Type type) {
        if (B.is(type)) {
            Field<?> r = parseFieldAnd(ctx);
            Condition c = null;
            while (parseKeywordIf(ctx, "OR"))
                c = ((c == null) ? condition((Field) r) : c).or((Field) parseFieldAnd(ctx));

            return c == null ? r : field(c);
        }
        else {
            return parseFieldConcat(ctx, type);
        }
    }

    private static final Field<?> parseFieldConcat(ParserContext ctx, Type type) {
        Field<?> r = parseFieldSum(ctx, type);

        if (S.is(type))
            while (parseIf(ctx, "||"))
                r = concat(r, parseFieldSum(ctx, type));

        return r;
    }

    private static final Field<?> parseFieldSumParenthesised(ParserContext ctx) {
        parse(ctx, '(');
        Field<?> r = parseFieldSum(ctx, N);
        parse(ctx, ')');
        return r;
    }

    private static final Field<?> parseFieldSum(ParserContext ctx, Type type) {
        Field<?> r = parseFieldFactor(ctx, type);

        if (N.is(type))
            for (;;)
                if (parseIf(ctx, '+'))
                    r = r.add(parseFieldFactor(ctx, type));
                else if (parseIf(ctx, '-'))
                    r = r.sub(parseFieldFactor(ctx, type));
                else
                    break;

        return r;
    }

    private static final Field<?> parseFieldFactor(ParserContext ctx, Type type) {
        Field<?> r = parseFieldTerm(ctx, type);

        if (N.is(type))
            for (;;)
                if (parseIf(ctx, '*'))
                    r = r.mul((Field) parseFieldTerm(ctx, type));
                else if (parseIf(ctx, '/'))
                    r = r.div((Field) parseFieldTerm(ctx, type));
                else if (parseIf(ctx, '%'))
                    r = r.mod((Field) parseFieldTerm(ctx, type));
                else
                    break;

        return r;
    }

    private static final Field<?> parseFieldAnd(ParserContext ctx) {
        Field<?> r = parseFieldCondition(ctx);
        Condition c = null;
        while (parseKeywordIf(ctx, "AND"))
            c = ((c == null) ? condition((Field) r) : c).and((Field) parseFieldCondition(ctx));

        return c == null ? r : field(c);
    }

    private static final Field<?> parseFieldCondition(ParserContext ctx) {
        // TODO all predicates also as fields
        return parseFieldConcat(ctx, null);
    }

    private static final Field<?> parseFieldTerm(ParserContext ctx, Type type) {
        parseWhitespaceIf(ctx);

        Field<?> field;
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
                    return prior(parseField(ctx));

                break;

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

            case 'y':
            case 'Y':
                if (N.is(type))
                    if ((field = parseFieldYearIf(ctx)) != null)
                        return field;

                break;

            case '+':
                parse(ctx, '+');

                if (N.is(type))
                    return parseFieldTerm(ctx, type);
                else
                    break;

            case '-':
                parse(ctx, '-');

                if (N.is(type))
                    if ((field = parseFieldUnsignedNumericLiteralIf(ctx, true)) != null)
                        return field;
                    else
                        return parseFieldTerm(ctx, type).neg();

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
                    if ((field = parseFieldUnsignedNumericLiteralIf(ctx, false)) != null)
                        return field;

                break;

            case '(':
                parse(ctx, '(');

                if (peekKeyword(ctx, "SELECT")) {
                    SelectQueryImpl<Record> select = parseSelect(ctx);
                    if (select.getSelect().size() > 1)
                        throw ctx.exception();

                    field = field((Select) select);
                    parse(ctx, ')');
                    return field;
                }
                else {
                    Field<?> r = parseField(ctx, type);
                    parse(ctx, ')');
                    return r;
                }
        }

        if ((field = parseAggregateFunctionIf(ctx)) != null)
            return field;

        else if ((field = parseBooleanValueExpressionIf(ctx)) != null)
            return field;

        else
            return parseFieldName(ctx);
    }

    private static final Field<?> parseFieldAtan2If(ParserContext ctx) {
        if (parseKeywordIf(ctx, "ATN2") || parseKeywordIf(ctx, "ATAN2")) {
            parse(ctx, '(');
            Field<?> x = parseFieldSum(ctx, N);
            parse(ctx, ',');
            Field<?> y = parseFieldSum(ctx, N);
            parse(ctx, ')');

            return atan2((Field) x, (Field) y);
        }

        return null;
    }

    private static final Field<?> parseFieldLogIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "LOG")) {
            parse(ctx, '(');
            Field<?> arg1 = parseFieldSum(ctx, N);
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
            Field<?> arg1 = parseFieldSum(ctx, N);
            parse(ctx, ',');
            Field<?> arg2 = parseFieldSum(ctx, N);
            parse(ctx, ')');
            return DSL.trunc((Field) arg1, (Field) arg2);
        }

        return null;
    }

    private static final Field<?> parseFieldRoundIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "ROUND")) {
            Field<?> arg1 = null;
            Integer arg2 = null;

            parse(ctx, '(');
            arg1 = parseFieldSum(ctx, N);
            if (parseIf(ctx, ','))
                arg2 = (int) (long) parseUnsignedInteger(ctx);

            parse(ctx, ')');
            return arg2 == null ? round((Field) arg1) : round((Field) arg1, arg2);
        }

        return null;
    }

    private static final Field<?> parseFieldPowerIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "POWER") || parseKeywordIf(ctx, "POW")) {
            parse(ctx, '(');
            Field<?> arg1 = parseFieldSum(ctx, N);
            parse(ctx, ',');
            Field<?> arg2 = parseFieldSum(ctx, N);
            parse(ctx, ')');
            return DSL.power((Field) arg1, (Field) arg2);
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

    private static Field<?> parseFieldLeastIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "LEAST")) {
            parse(ctx, '(');
            List<Field<?>> fields = parseFields(ctx);
            parse(ctx, ')');

            return least(fields.get(0), fields.size() > 1 ? fields.subList(1, fields.size()).toArray(EMPTY_FIELD) : EMPTY_FIELD);
        }

        return null;
    }

    private static Field<?> parseFieldGreatestIf(ParserContext ctx) {
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
            throw ctx.exception();
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
            throw ctx.exception();
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
            throw ctx.exception();
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
            Field<String> f1 = (Field) parseFieldConcat(ctx, S);
            if (substr || !(keywords = parseKeywordIf(ctx, "FROM")))
                parse(ctx, ',');
            Field<?> f2 = parseFieldSum(ctx, N);
            Field<?> f3 =
                    ((keywords && parseKeywordIf(ctx, "FOR")) || (!keywords && parseIf(ctx, ',')))
                ? parseFieldSum(ctx, N)
                : null;
            parse(ctx, ')');

            return f3 == null
                ? substring(f1, (Field) f2)
                : substring(f1, (Field) f2, (Field) f3);
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
        if (parseKeywordIf(ctx, "LOWER")) {
            parse(ctx, '(');
            Field<String> f1 = (Field) parseField(ctx, S);
            parse(ctx, ')');
            return lower(f1);
        }

        return null;
    }

    private static final Field<?> parseFieldUpperIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "UPPER")) {
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
                    throw ctx.exception();
            }
        }

        return null;
    }

    private static final Field<?> parseAggregateFunctionIf(ParserContext ctx) {
        AggregateFunction<?> agg;
        WindowBeforeOverStep<?> over;
        Field<?> result;
        Condition filter;

        agg = parseCountIf(ctx);
        if (agg == null)
            agg = parseGeneralSetFunctionIf(ctx);
        if (agg == null)
            agg = parseBinarySetFunctionIf(ctx);

        if (agg == null)
            return null;

        if (parseKeywordIf(ctx, "FILTER")) {
            parse(ctx, '(');
            parseKeyword(ctx, "WHERE");
            filter = parseCondition(ctx);
            parse(ctx, ')');

            result = over = agg.filterWhere(filter);
        }
        else {
            result = over = agg;
        }

        // TODO parse WITHIN GROUP (ORDER BY) where applicable
        if (parseKeywordIf(ctx, "OVER")) {
            Object nameOrSpecification = parseWindowNameOrSpecification(ctx);

            if (nameOrSpecification instanceof Name)
                result = over.over((Name) nameOrSpecification);
            else if (nameOrSpecification instanceof WindowSpecification)
                result = over.over((WindowSpecification) nameOrSpecification);
            else
                result = over.over();
        }

        return result;
    }

    private static Object parseWindowNameOrSpecification(ParserContext ctx) {
        Object result;

        if (parseIf(ctx, '(')) {
            WindowSpecificationOrderByStep s1 = null;
            WindowSpecificationRowsStep s2 = null;
            WindowSpecificationRowsAndStep s3 = null;

            s1 = parseKeywordIf(ctx, "PARTITION BY")
                ? partitionBy(parseFields(ctx))
                : null;

            s2 = parseKeywordIf(ctx, "ORDER BY")
                ? s1 == null
                    ? orderBy(parseSortSpecification(ctx))
                    : s1.orderBy(parseSortSpecification(ctx))
                : s1;

            boolean rows = parseKeywordIf(ctx, "ROWS");
            if (rows || parseKeywordIf(ctx, "RANGE")) {
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
            result = name(parseIdentifier(ctx));
        }

        return result;
    }

    private static final Field<?> parseFieldRankIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "RANK")) {
            parse(ctx, '(');
            parse(ctx, ')');
            return parseWindowFunction(ctx, null, rank());
        }

        return null;
    }

    private static final Field<?> parseFieldDenseRankIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "DENSE_RANK")) {
            parse(ctx, '(');
            parse(ctx, ')');
            return parseWindowFunction(ctx, null, denseRank());
        }

        return null;
    }

    private static final Field<?> parseFieldPercentRankIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "PERCENT_RANK")) {
            parse(ctx, '(');
            parse(ctx, ')');
            return parseWindowFunction(ctx, null, percentRank());
        }

        return null;
    }

    private static final Field<?> parseFieldCumeDistIf(ParserContext ctx) {
        if (parseKeywordIf(ctx, "CUME_DIST")) {
            parse(ctx, '(');
            parse(ctx, ')');
            return parseWindowFunction(ctx, null, cumeDist());
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
        Object nameOrSpecification = parseWindowNameOrSpecification(ctx);

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
        arg1 = (Field) parseFieldSum(ctx, N);
        parse(ctx, ',');
        arg2 = (Field) parseFieldSum(ctx, N);
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
                throw ctx.exception();
        }
    }

    private static final AggregateFunction<?> parseGeneralSetFunctionIf(ParserContext ctx) {
        boolean distinct;
        Field arg;
        ComputationalOperation operation = parseComputationalOperationIf(ctx);

        if (operation == null)
            return null;

        parse(ctx, '(');
        distinct = parseSetQuantifier(ctx);
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

    static final Name parseName(ParserContext ctx) {
        parseWhitespaceIf(ctx);
        if (ctx.done())
            throw ctx.exception();

        List<String> name = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        int i = ctx.position;
        boolean identifierStart = true;
        boolean identifierEnd = false;
        for (;;) {
            char c = ctx.character(i);

            // TODO Quoted identifiers
            if (c == '.') {
                if (identifierStart)
                    throw new ParserException(ctx);

                name.add(sb.toString());
                sb = new StringBuilder();
                identifierStart = true;
                identifierEnd = false;
            }

            // Better way to distinguish identifier parts
            else if (!identifierEnd && Character.isJavaIdentifierPart(c)) {
                sb.append(c);
                identifierStart = false;
            }

            else if (Character.isWhitespace(c)) {
                identifierEnd = !identifierStart;
            }

            else {
                name.add(sb.toString());
                identifierEnd = !identifierStart;
                break;
            }

            if (++i == ctx.sql.length) {
                if (identifierStart)
                    throw ctx.exception();

                name.add(sb.toString());
                identifierEnd = !identifierStart;
                break;
            }
        }

        if (ctx.position == i)
            throw ctx.exception();

        ctx.position = i;
        return DSL.name(name.toArray(EMPTY_STRING));
    }

    private static final List<String> parseIdentifiers(ParserContext ctx) {
        LinkedHashSet<String> result = new LinkedHashSet<String>();

        do {
            if (!result.add(parseIdentifier(ctx)))
                throw ctx.exception();
        }
        while (parseIf(ctx, ','));
        return new ArrayList<String>(result);
    }

    private static final String parseIdentifier(ParserContext ctx) {
        String alias = parseIdentifierIf(ctx);

        if (alias == null)
            throw ctx.exception();

        return alias;
    }

    private static final String parseIdentifierIf(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        int start = ctx.position;
        while (ctx.isIdentifierPart())
            ctx.position = ctx.position + 1;

        if (ctx.position == start)
            return null;

        return new String(ctx.sql, start, ctx.position - start);
    }

    private static final DataType<?> parseDataType(ParserContext ctx) {
        parseWhitespaceIf(ctx);

        switch (ctx.character()) {
            case 'b':
            case 'B':
                if (parseKeywordIf(ctx, "BIGINT"))
                    return SQLDataType.BIGINT;
                else if (parseKeywordIf(ctx, "BINARY"))
                    return parseDataTypeLength(ctx, SQLDataType.BINARY);
                else if (parseKeywordIf(ctx, "BIT"))
                    return SQLDataType.BIT;
                else if (parseKeywordIf(ctx, "BLOB"))
                    return SQLDataType.BLOB;
                else if (parseKeywordIf(ctx, "BOOLEAN"))
                    return SQLDataType.BOOLEAN;

            case 'c':
            case 'C':
                if (parseKeywordIf(ctx, "CHAR"))
                    return parseDataTypeLength(ctx, SQLDataType.CHAR);
                else if (parseKeywordIf(ctx, "CLOB"))
                    return parseDataTypeLength(ctx, SQLDataType.CLOB);

            case 'i':
            case 'I':
                if (parseKeywordIf(ctx, "INT") || parseKeywordIf(ctx, "INTEGER"))
                    return SQLDataType.INTEGER;

            case 'v':
            case 'V':
                if (parseKeywordIf(ctx, "VARCHAR") || parseKeywordIf(ctx, "VARCHAR2") || parseKeywordIf(ctx, "CHARACTER VARYING"))
                    return parseDataTypeLength(ctx, SQLDataType.VARCHAR);
                else if (parseKeywordIf(ctx, "VARBINARY"))
                    return parseDataTypeLength(ctx, SQLDataType.VARBINARY);

                break;

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

    // -----------------------------------------------------------------------------------------------------------------
    // Literal parsing
    // -----------------------------------------------------------------------------------------------------------------

    static final char parseCharacterLiteral(ParserContext ctx) {
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

    static final Field<?> parseBindVariable(ParserContext ctx) {
        switch (ctx.character()) {
            case '?':
                parse(ctx, '?');
                return DSL.val(null, Object.class);

            case ':':
                parse(ctx, ':');
                return DSL.param(parseIdentifier(ctx));

            default:
                throw ctx.exception();
        }
    }

    static final String parseStringLiteral(ParserContext ctx) {
        parseWhitespaceIf(ctx);
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

        throw ctx.exception();
    }

    private static final Field<?> parseFieldUnsignedNumericLiteralIf(ParserContext ctx, boolean minus) {
        Number r = parseUnsignedNumericLiteralIf(ctx, minus);
        return r == null ? null : inline(r);
    }

    private static final Number parseUnsignedNumericLiteralIf(ParserContext ctx, boolean minus) {
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
                return minus
                    ? -Long.valueOf(sb.toString())
                    : Long.valueOf(sb.toString());
            }
            catch (Exception e1) {
                return minus
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

        return minus
            ? new BigDecimal(sb.toString()).negate()
            : new BigDecimal(sb.toString());
        // TODO add floating point support
    }

    private static final Long parseUnsignedInteger(ParserContext ctx) {
        Long result = parseUnsignedIntegerIf(ctx);

        if (result == null)
            throw ctx.exception();

        return result;
    }

    private static final Field<?> parseFieldUnsignedIntegerIf(ParserContext ctx, boolean minus) {
        Long r = parseUnsignedIntegerIf(ctx);
        return r == null
            ? null
            : minus
            ? inline(-r)
            : inline(r);
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

    private static final JoinType parseJoinType(ParserContext ctx) {
        JoinType result = parseJoinTypeIf(ctx);

        if (result == null)
            ctx.unexpectedToken();

        return result;
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
        else if (parseKeywordIf(ctx, "FULL OUTER JOIN"))
            return JoinType.FULL_OUTER_JOIN;
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

    static final TruthValue parseTruthValue(ParserContext ctx) {
        TruthValue result = parseTruthValueIf(ctx);

        if (result == null)
            throw ctx.exception();

        return result;
    }

    static final TruthValue parseTruthValueIf(ParserContext ctx) {
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

    static final void parseKeyword(ParserContext ctx, String string) {
        if (!parseKeywordIf(ctx, string))
            throw ctx.unexpectedToken();
    }

    static final boolean parseKeywordIf(ParserContext ctx, String string) {
        ctx.expectedTokens.add(string);

        if (peekKeyword(ctx, string, true))
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
        return peekKeyword(ctx, keyword, false);
    }

    private static final boolean peekKeyword(ParserContext ctx, String keyword, boolean updatePosition) {
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
                case '(':
                    continue skipLoop;

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

    static final boolean parseWhitespaceIf(ParserContext ctx) {
        int position = ctx.position;
        ctx.position = afterWhitespace(ctx, ctx.position);
        return position != ctx.position;
    }

    private static final int afterWhitespace(ParserContext ctx, int position) {
        loop:
        for (int i = position; i < ctx.sql.length; i++)
            switch (ctx.sql[i]) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    position = i + 1;
                    continue loop;

                // TODO consume comments
                default:
                    break loop;
            }

        return position;
    }

    private static final char upper(char c) {
        return c >= 'a' && c <= 'z' ? (char) (c - ('a' - 'A')) : c;
    }

    public static void main(String[] args) {
        System.out.println(new ParserImpl(new DefaultConfiguration()).parse(
            "DROP INDEX   y on a.b.c"
        ));
    }

    static class ParserContext {
        final DSLContext   dsl;
        final String       sqlString;
        final char[]       sql;
        final List<String> expectedTokens;
        int                position = 0;

        ParserContext(DSLContext dsl, String sqlString) {
            this.dsl = dsl;
            this.sqlString = sqlString;
            this.sql = sqlString.toCharArray();
            this.expectedTokens = new ArrayList<String>();
        }

        ParserException internalError() {
            return new ParserException(this, "Internal Error");
        }

        ParserException exception() {
            return new ParserException(this);
        }

        ParserException unexpectedToken() {
            return new ParserException(this, "Expected tokens: " + new TreeSet<String>(expectedTokens));
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
            return sqlString.substring(0, position) + "[*]" + sqlString.substring(position);
        }

        @Override
        public String toString() {
            return mark();
        }
    }

    static class ParserException extends DataAccessException {

        /**
         * Generated UID
         */
        private static final long   serialVersionUID = -724913199583039157L;
        private final ParserContext ctx;

        public ParserException(ParserContext ctx) {
            this(ctx, null);
        }

        public ParserException(ParserContext ctx, String message) {
            this(ctx, message, C42000_NO_SUBCLASS);
        }

        public ParserException(ParserContext ctx, String message, SQLStateSubclass state) {
            this(ctx, message, state, null);
        }

        public ParserException(ParserContext ctx, String message, SQLStateSubclass state, Throwable cause) {
            super(state + ": " + (message == null ? "" : message + ": ") + ctx.mark(), cause);

            this.ctx = ctx;
        }
    }

    static enum TruthValue {
        TRUE,
        FALSE,
        NULL;
    }

    static enum ComputationalOperation {
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
//        COLLECT,
//        FUSION,
//        INTERSECTION;
    }

    static enum BinarySetFunctionType {
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
        "FULL",
        "FROM",
        "GROUP BY",
        "HAVING",
        "INNER",
        "INTERSECT",
        "JOIN",
        "LEFT",
        "LIMIT",
        "MINUS",
        "NATURAL",
        "OFFSET",
        "ON",
        "ORDER BY",
        "OUTER",
        "RIGHT",
        "SELECT",
        "START",
        "UNION",
        "USING",
        "WHERE",
    };
}
