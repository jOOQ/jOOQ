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
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.NOT_IN;
import static org.jooq.JoinType.JOIN;
// ...
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
import static org.jooq.impl.DSL.arrayGet;
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
import static org.jooq.impl.DSL.cardinality;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.catalog;
import static org.jooq.impl.DSL.ceil;
import static org.jooq.impl.DSL.century;
import static org.jooq.impl.DSL.charLength;
import static org.jooq.impl.DSL.characterSet;
import static org.jooq.impl.DSL.check;
import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.coerce;
import static org.jooq.impl.DSL.collation;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.condition;
// ...
// ...
// ...
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
import static org.jooq.impl.DSL.currentCatalog;
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
import static org.jooq.impl.DSL.domain;
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
import static org.jooq.impl.DSL.insert;
import static org.jooq.impl.DSL.isnull;
import static org.jooq.impl.DSL.isoDayOfWeek;
import static org.jooq.impl.DSL.jsonExists;
import static org.jooq.impl.DSL.jsonTable;
import static org.jooq.impl.DSL.jsonValue;
import static org.jooq.impl.DSL.key;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.lag;
import static org.jooq.impl.DSL.lastValue;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.lead;
import static org.jooq.impl.DSL.least;
import static org.jooq.impl.DSL.left;
import static org.jooq.impl.DSL.length;
// ...
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
import static org.jooq.impl.DSL.primaryKey;
// ...
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
import static org.jooq.impl.DSL.regexpReplaceAll;
import static org.jooq.impl.DSL.regexpReplaceFirst;
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
// ...
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
import static org.jooq.impl.DSL.splitPart;
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.DSL.sqrt;
import static org.jooq.impl.DSL.stddevPop;
import static org.jooq.impl.DSL.stddevSamp;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.sumDistinct;
// ...
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.tan;
import static org.jooq.impl.DSL.tanh;
import static org.jooq.impl.DSL.time;
import static org.jooq.impl.DSL.timestamp;
import static org.jooq.impl.DSL.timezone;
import static org.jooq.impl.DSL.timezoneHour;
import static org.jooq.impl.DSL.timezoneMinute;
import static org.jooq.impl.DSL.toChar;
import static org.jooq.impl.DSL.toDate;
import static org.jooq.impl.DSL.toTimestamp;
import static org.jooq.impl.DSL.translate;
import static org.jooq.impl.DSL.trim;
import static org.jooq.impl.DSL.trunc;
import static org.jooq.impl.DSL.unique;
import static org.jooq.impl.DSL.unnest;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.DSL.user;
import static org.jooq.impl.DSL.values0;
// ...
import static org.jooq.impl.DSL.varPop;
import static org.jooq.impl.DSL.varSamp;
import static org.jooq.impl.DSL.week;
import static org.jooq.impl.DSL.when;
// ...
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlattributes;
import static org.jooq.impl.DSL.xmlcomment;
import static org.jooq.impl.DSL.xmlconcat;
// ...
import static org.jooq.impl.DSL.xmlelement;
import static org.jooq.impl.DSL.xmlexists;
import static org.jooq.impl.DSL.xmlforest;
import static org.jooq.impl.DSL.xmlparseContent;
import static org.jooq.impl.DSL.xmlparseDocument;
import static org.jooq.impl.DSL.xmlpi;
import static org.jooq.impl.DSL.xmlquery;
import static org.jooq.impl.DSL.xmltable;
import static org.jooq.impl.DSL.year;
import static org.jooq.impl.DSL.zero;
import static org.jooq.impl.JSONNull.JSONNullType.ABSENT_ON_NULL;
import static org.jooq.impl.JSONNull.JSONNullType.NULL_ON_NULL;
import static org.jooq.impl.Keywords.K_DELETE;
import static org.jooq.impl.Keywords.K_INSERT;
import static org.jooq.impl.Keywords.K_SELECT;
import static org.jooq.impl.Keywords.K_UPDATE;
import static org.jooq.impl.ParserContext.Type.A;
import static org.jooq.impl.ParserContext.Type.B;
import static org.jooq.impl.ParserContext.Type.D;
import static org.jooq.impl.ParserContext.Type.J;
import static org.jooq.impl.ParserContext.Type.N;
import static org.jooq.impl.ParserContext.Type.S;
import static org.jooq.impl.ParserContext.Type.X;
import static org.jooq.impl.ParserContext.Type.Y;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.NVARCHAR;
import static org.jooq.impl.Tools.EMPTY_BYTE;
import static org.jooq.impl.Tools.EMPTY_COLLECTION;
import static org.jooq.impl.Tools.EMPTY_COMMON_TABLE_EXPRESSION;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.EMPTY_NAME;
import static org.jooq.impl.Tools.EMPTY_OBJECT;
import static org.jooq.impl.Tools.EMPTY_QUERYPART;
import static org.jooq.impl.Tools.EMPTY_ROW;
import static org.jooq.impl.Tools.EMPTY_SORTFIELD;
import static org.jooq.impl.Tools.aliased;
import static org.jooq.impl.Tools.normaliseNameCase;
import static org.jooq.impl.XMLPassingMechanism.BY_REF;
import static org.jooq.impl.XMLPassingMechanism.BY_VALUE;

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
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.AggregateFilterStep;
import org.jooq.AggregateFunction;
import org.jooq.AlterDatabaseStep;
import org.jooq.AlterDomainDropConstraintCascadeStep;
import org.jooq.AlterDomainRenameConstraintStep;
import org.jooq.AlterDomainStep;
import org.jooq.AlterIndexFinalStep;
import org.jooq.AlterIndexStep;
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
import org.jooq.CreateDomainConstraintStep;
import org.jooq.CreateDomainDefaultStep;
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
import org.jooq.Domain;
import org.jooq.DropDomainCascadeStep;
import org.jooq.DropIndexCascadeStep;
import org.jooq.DropIndexFinalStep;
import org.jooq.DropIndexOnStep;
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
import org.jooq.InsertOnConflictWhereIndexPredicateStep;
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
import org.jooq.JSONTableColumnPathStep;
import org.jooq.JSONTableColumnsStep;
import org.jooq.JSONValueDefaultStep;
import org.jooq.JSONValueOnStep;
import org.jooq.JoinType;
import org.jooq.Keyword;
// ...
import org.jooq.LikeEscapeStep;
// ...
import org.jooq.Merge;
import org.jooq.MergeFinalStep;
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
import org.jooq.VisitContext;
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
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.impl.JSONNull.JSONNullType;
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

    private final ParserContext ctx(String sql, Object... bindings) {
        return new ParserContext(dsl, meta, metaLookups, sql, bindings);
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
        return ctx(sql, bindings).parseStatementAndSemicolon();
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
final class ParserContext {








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
                String s =
                configuration().derive(SettingsTools.clone(configuration().settings())
                    .withRenderFormatted(false)
                    .withRenderKeywordCase(RenderKeywordCase.LOWER)
                    .withRenderNameCase(RenderNameCase.LOWER)
                    .withRenderQuotedNames(RenderQuotedNames.NEVER)
                    .withRenderSchema(false))
                    .dsl()
                    .render(query);

                // [#8910] special treatment for PostgreSQL pg_dump's curious
                //         usage of the SET SCHEMA command
                Matcher matcher = P_SEARCH_PATH.matcher(s);
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

    public final Query parseQuery0() {
        return done("Unexpected clause", parseQuery(false, false));
    }

    final Statement parseStatement0() {
        return this.done("Unexpected content", parseStatementAndSemicolon());
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
        try {
            switch (characterUpper()) {
                case 'A':
                    if (!parseResultQuery && peekKeyword("ALTER"))
                        return metaLookupsForceIgnore(true).parseAlter();

                    break;

                case 'B':
                    if (!parseResultQuery && peekKeyword("BEGIN"))
                        return parseBlock();

                    break;

                case 'C':
                    if (!parseResultQuery && peekKeyword("CREATE"))
                        return metaLookupsForceIgnore(true).parseCreate();
                    else if (!parseResultQuery && peekKeyword("COMMENT ON"))
                        return metaLookupsForceIgnore(true).parseCommentOn();
                    else if (parseKeywordIf("CALL"))
                        throw notImplemented("CALL");
                    else if (parseKeywordIf("COMMIT"))
                        throw notImplemented("COMMIT");
                    else if (parseKeywordIf("CONNECT"))
                        throw notImplemented("CONNECT");

                    break;

                case 'D':
                    if (!parseResultQuery && peekKeyword("DECLARE") && requireProEdition())
                        return parseBlock();
                    else if (!parseResultQuery && (peekKeyword("DELETE") || peekKeyword("DEL")))
                        return parseDelete(null);
                    else if (!parseResultQuery && peekKeyword("DROP"))
                        return metaLookupsForceIgnore(true).parseDrop();
                    else if (!parseResultQuery && peekKeyword("DO"))
                        return parseDo();

                    break;

                case 'E':
                    if (!parseResultQuery && peekKeyword("EXECUTE BLOCK AS BEGIN"))
                        return parseBlock();
                    else if (!parseResultQuery && peekKeyword("EXEC"))
                        return parseExec();

                    break;

                case 'G':
                    if (!parseResultQuery && peekKeyword("GRANT"))
                        return metaLookupsForceIgnore(true).parseGrant();

                    break;

                case 'I':
                    if (!parseResultQuery && (peekKeyword("INSERT") || peekKeyword("INS")))
                        return parseInsert(null);

                    break;

                case 'L':
                    if (parseKeywordIf("LOAD"))
                        throw notImplemented("LOAD");

                    break;

                case 'M':
                    if (!parseResultQuery && peekKeyword("MERGE"))
                        return parseMerge(null);

                    break;

                case 'R':
                    if (!parseResultQuery && peekKeyword("RENAME"))
                        return metaLookupsForceIgnore(true).parseRename();
                    else if (!parseResultQuery && peekKeyword("REVOKE"))
                        return metaLookupsForceIgnore(true).parseRevoke();
                    else if (parseKeywordIf("REPLACE"))
                        throw notImplemented("REPLACE");
                    else if (parseKeywordIf("ROLLBACK"))
                        throw notImplemented("ROLLBACK");

                    break;

                case 'S':
                    if (peekSelect(false))
                        return parseSelect();
                    else if (!parseResultQuery && peekKeyword("SET"))
                        return parseSet();
                    else if (parseKeywordIf("SAVEPOINT"))
                        throw notImplemented("SAVEPOINT");

                    break;

                case 'T':
                    if (!parseResultQuery && peekKeyword("TRUNCATE"))
                        return parseTruncate();

                    break;

                case 'U':
                    if (!parseResultQuery && (peekKeyword("UPDATE") || peekKeyword("UPD")))
                        return parseUpdate(null);
                    else if (!parseResultQuery && peekKeyword("USE"))
                        return parseUse();
                    else if (parseKeywordIf("UPSERT"))
                        throw notImplemented("UPSERT");

                    break;

                case 'V':
                    if (!parseSelect && peekKeyword("VALUES"))
                        return parseSelect();

                case 'W':
                    if (peekKeyword("WITH"))
                        return parseWith(parseSelect);

                    break;

                case '(':

                    // TODO are there other possible statement types?
                    if (peekKeyword("WITH", false, true, false))
                        return parseWith(true);
                    else
                        return parseSelect();

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
            scopeEnd();
            scopeResolve();
            metaLookupsForceIgnore(previousMetaLookupsForceIgnore);
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
            Select<?> select = parseSelect();
            parse(')');

            cte.add(dcl != null
                ? materialized
                    ? dcl.asMaterialized(select)
                    : notMaterialized
                    ? dcl.asNotMaterialized(select)
                    : dcl.as(select)
                : materialized
                    ? name.asMaterialized(select)
                    : notMaterialized
                    ? name.asNotMaterialized(select)
                    : name.as(select)
            );
        }
        while (parseIf(','));

        // TODO Better model API for WITH clause
        WithImpl with = (WithImpl) new WithImpl(dsl.configuration(), recursive).with(cte.toArray(EMPTY_COMMON_TABLE_EXPRESSION));
        Query result;
        if (!parseSelect && (peekKeyword("DELETE") || peekKeyword("DEL")))
            result = parseDelete(with);
        else if (!parseSelect && (peekKeyword("INSERT") || peekKeyword("INS")))
            result = parseInsert(with);
        else if (!parseSelect && peekKeyword("MERGE"))
            result = parseMerge(with);
        else if (peekSelect(true))
            result = parseSelect(degree, with);
        else if (!parseSelect && (peekKeyword("UPDATE") || peekKeyword("UPD")))
            result = parseUpdate(with);
        else if ((parseWhitespaceIf() || true) && done())
            throw exception("Missing statement after WITH");
        else
            throw exception("Unsupported statement after WITH");

        while (parens --> 0)
            parse(')');

        return result;
    }

    private final Select<?> parseWithOrSelect() {
        return parseWithOrSelect(null);
    }

    private final Select<?> parseWithOrSelect(Integer degree) {
        return peekKeyword("WITH") ? (Select<?>) parseWith(true, degree) : parseSelect(degree, null);
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
                result.addOrderBy(orderBy = parseSortSpecification());
            else
                throw expected("SIBLINGS BY", "BY");
        }

        if (orderBy != null && parseKeywordIf("SEEK")) {
            boolean before = parseKeywordIf("BEFORE");
            if (!before)
                parseKeywordIf("AFTER");

            List<Field<?>> seek = parseFields();
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
                result.setForUpdateOf(parseFields());

            if (parseKeywordIf("NOWAIT"))
                result.setForUpdateNoWait();
            else if (parseKeywordIf("WAIT") && requireProEdition())



                ;
            else if (parseKeywordIf("SKIP LOCKED"))
                result.setForUpdateSkipLocked();
        }

        scopeEnd();
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
            Long from = parseUnsignedInteger();

            if (parseKeywordIf("TO")) {
                Long to = parseUnsignedInteger();
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

        CombineOperator combine;
        while ((combine = parseCombineOperatorIf(false)) != null) {
            scopeEnd();
            scopeStart();

            if (degree == null)
                degree = Tools.degree(lhs);

            SelectQueryImpl<Record> rhs = degreeCheck(degree, parseQueryTerm(degree, null, null));
            switch (combine) {
                case UNION:
                    lhs = (SelectQueryImpl<Record>) lhs.union(rhs);
                    break;
                case UNION_ALL:
                    lhs = (SelectQueryImpl<Record>) lhs.unionAll(rhs);
                    break;
                case EXCEPT:
                    lhs = (SelectQueryImpl<Record>) lhs.except(rhs);
                    break;
                case EXCEPT_ALL:
                    lhs = (SelectQueryImpl<Record>) lhs.exceptAll(rhs);
                    break;
                default:
                    throw internalError();
            }
        }

        return lhs;
    }

    private final SelectQueryImpl<Record> parseQueryTerm(Integer degree, WithImpl with, SelectQueryImpl<Record> prefix) {
        SelectQueryImpl<Record> lhs = prefix != null ? prefix : parseQueryPrimary(degree, with);

        CombineOperator combine;
        while ((combine = parseCombineOperatorIf(true)) != null) {
            scopeEnd();
            scopeStart();

            if (degree == null)
                degree = Tools.degree(lhs);

            SelectQueryImpl<Record> rhs = degreeCheck(degree, parseQueryPrimary(degree, null));
            switch (combine) {
                case INTERSECT:
                    lhs = (SelectQueryImpl<Record>) lhs.intersect(rhs);
                    break;
                case INTERSECT_ALL:
                    lhs = (SelectQueryImpl<Record>) lhs.intersectAll(rhs);
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

        ignoreHints(false);
        if (!parseKeywordIf("SEL"))
            parseKeyword("SELECT");

        String hints = parseHints();
        boolean distinct = parseKeywordIf("DISTINCT") || parseKeywordIf("UNIQUE");
        List<Field<?>> distinctOn = null;

        if (distinct) {
            if (parseKeywordIf("ON")) {
                parse('(');
                distinctOn = parseFields();
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

        Table<?> into = null;
        List<Table<?>> from = null;

        if (parseKeywordIf("INTO"))
            into = parseTableName();

        if (parseKeywordIf("FROM"))
            from = parseTables();

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

        if (into != null)
            result.setInto(into);

        if (from != null)
            result.addFrom(from);

        if (parseKeywordIf("WHERE"))
            result.addConditions(parseCondition());

        // [#10638] Oracle seems to support (but not document) arbitrary ordering
        //          between these clauses
        boolean connectBy = false;
        boolean startWith = false;
        boolean groupBy = false;
        boolean having = false;

        while ((!connectBy && (connectBy = parseQueryPrimaryConnectBy(result)))
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
                result.addGroupBy(rollup(parseFields().toArray(EMPTY_FIELD)));
                parse(')');
            }
            else if (parseKeywordIf("CUBE")) {
                parse('(');
                result.addGroupBy(cube(parseFields().toArray(EMPTY_FIELD)));
                parse(')');
            }
            else if (parseKeywordIf("GROUPING SETS")) {
                List<List<Field<?>>> fieldSets = new ArrayList<>();
                parse('(');
                do {
                    fieldSets.add(parseFieldsOrEmptyParenthesised());
                }
                while (parseIf(','));
                parse(')');
                result.addGroupBy(groupingSets(fieldSets.toArray((Collection[]) EMPTY_COLLECTION)));
            }
            else {
                groupBy = (List) parseFields();

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
        List<WindowDefinition> result = new ArrayList<>();

        do {
            Name name = parseIdentifier();
            parseKeyword("AS");
            parse('(');
            result.add(name.as(parseWindowSpecificationIf(null, true)));
            parse(')');
        }
        while (parseIf(','));

        return result;
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
            ? partitionBy(parseFields())
            : null;

        if (parseKeywordIf("ORDER BY"))
            if (orderByAllowed)
                s2 = s1 == null
                    ? orderBy(parseSortSpecification())
                    : s1.orderBy(parseSortSpecification());
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
                else if ((n = parseUnsignedIntegerIf()) != null)
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
                else if ((n = parseUnsignedInteger()) != null)
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
            else if ((n = parseUnsignedInteger()) != null)
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

    private final Delete<?> parseDelete(WithImpl with) {
        if (!parseKeywordIf("DEL"))
            parseKeyword("DELETE");

        Param<Long> limit = null;

        // T-SQL style TOP .. START AT
        if (parseKeywordIf("TOP")) {
            limit = requireParam(parseParenthesisedUnsignedIntegerOrBindVariable());

            // [#8623] TODO Support this
            // percent = parseKeywordIf("PERCENT") && requireProEdition();
        }

        parseKeywordIf("FROM");
        Table<?> table = parseTableNameIf();
        if (table == null)
            table = table(parseSelect());

        Name alias;
        if (parseKeywordIf("AS"))
            table = table.as(parseIdentifier());
        else if (!peekKeyword("USING", "WHERE", "ORDER BY", "LIMIT", "RETURNING")
            && !peekKeyword(KEYWORDS_IN_STATEMENTS)
            && (alias = parseIdentifierIf()) != null)
            table = table.as(alias);

        scope(table);

        DeleteUsingStep<?> s1 = with == null ? dsl.delete(table) : with.delete(table);
        DeleteWhereStep<?> s2 = parseKeywordIf("USING") ? s1.using(parseTables()) : s1;
        DeleteOrderByStep<?> s3 = parseKeywordIf("WHERE") ? s2.where(parseCondition()) : s2;
        DeleteLimitStep<?> s4 = parseKeywordIf("ORDER BY") ? s3.orderBy(parseSortSpecification()) : s3;
        DeleteReturningStep<?> s5 = (limit != null || parseKeywordIf("LIMIT"))
            ? s4.limit(limit != null ? limit : requireParam(parseParenthesisedUnsignedIntegerOrBindVariable()))
            : s4;
        Delete<?> s6 = parseKeywordIf("RETURNING") ? s5.returning(parseSelectList()) : s5;

        return s6;
    }

    private final Insert<?> parseInsert(WithImpl with) {
        scopeStart();
        if (!parseKeywordIf("INS"))
            parseKeyword("INSERT");

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

        if (parseIf('(')) {
            fields = parseFieldNames().toArray(EMPTY_FIELD);
            parse(')');
        }

        InsertOnDuplicateStep<?> onDuplicate;
        InsertReturningStep<?> returning;

        try {
            if (parseKeywordIf("VALUES")) {
                List<List<Field<?>>> allValues = new ArrayList<>();

                valuesLoop:
                do {
                    parse('(');

                    // [#6936] MySQL treats an empty VALUES() clause as the same thing as the standard DEFAULT VALUES
                    if (fields == null && parseIf(')'))
                        break valuesLoop;

                    List<Field<?>> values = new ArrayList<>();
                    do {
                        Field<?> value = parseKeywordIf("DEFAULT") ? default_() : parseField();
                        values.add(value);
                    }
                    while (parseIf(','));

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
                scopeEnd();
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
                        InsertOnConflictWhereIndexPredicateStep<?> where = onDuplicate.onConflict(parseFieldNames());
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

            if (parseKeywordIf("RETURNING"))
                return returning.returning(parseSelectList());
            else
                return returning;
        }
        finally {
            scopeEnd();
        }
    }

    private final Update<?> parseUpdate(WithImpl with) {
        if (!parseKeywordIf("UPD"))
            parseKeyword("UPDATE");

        Param<Long> limit = null;

        // T-SQL style TOP .. START AT
        if (parseKeywordIf("TOP")) {
            limit = requireParam(parseParenthesisedUnsignedIntegerOrBindVariable());

            // [#8623] TODO Support this
            // percent = parseKeywordIf("PERCENT") && requireProEdition();
        }

        Table<?> table = parseTableNameIf();
        if (table == null)
            table = table(parseSelect());

        if (parseKeywordIf("AS"))
            table = table.as(parseIdentifier());
        else if (!peekKeyword("SET"))
            table = table.as(parseIdentifierIf());

        scope(table);

        UpdateSetFirstStep<?> s1 = (with == null ? dsl.update(table) : with.update(table));

        parseKeyword("SET");

        // TODO Row value expression updates
        Map<Field<?>, Object> map = parseSetClauseList();
        UpdateFromStep<?> s2 = s1.set(map);
        UpdateWhereStep<?> s3 = parseKeywordIf("FROM") ? s2.from(parseTables()) : s2;
        UpdateOrderByStep<?> s4 = parseKeywordIf("WHERE") ? s3.where(parseCondition()) : s3;
        UpdateLimitStep<?> s5 = parseKeywordIf("ORDER BY") ? s4.orderBy(parseSortSpecification()) : s4;
        UpdateReturningStep<?> s6 = (limit != null || parseKeywordIf("LIMIT"))
            ? s5.limit(limit != null ? limit : requireParam(parseParenthesisedUnsignedIntegerOrBindVariable()))
            : s5;
        Update<?> s7 = parseKeywordIf("RETURNING") ? s6.returning(parseSelectList()) : s6;

        return s7;
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

    @SuppressWarnings("null")
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

        TableLike<?> usingTable = (table != null ? table : using);
        if (parseKeywordIf("AS") || !peekKeyword("ON"))
            usingTable = usingTable.asTable(parseIdentifier());

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
                insertValues = new ArrayList<>();
                do {
                    Field<?> value = parseKeywordIf("DEFAULT") ? default_() : parseField();
                    insertValues.add(value);
                }
                while (parseIf(','));
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
        MergeFinalStep<?> s3 = insert
            ? insertWhere != null
                ? s2.whenNotMatchedThenInsert(insertColumns).values(insertValues).where(insertWhere)
                : s2.whenNotMatchedThenInsert(insertColumns).values(insertValues)
            : s2;

        return s3;
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
            Object value = parseSignedIntegerIf();
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

        if (parseKeywordIf("COLUMN"))
            s1 = dsl.commentOnColumn(parseFieldName());
        else if (parseKeywordIf("TABLE"))
            s1 = dsl.commentOnTable(parseTableName());
        else if (parseKeywordIf("VIEW"))
            s1 = dsl.commentOnView(parseTableName());

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
            parseIdentifier();
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
                else if (parseKeywordIf("FUNCTION"))
                    throw notImplemented("CREATE FUNCTION", "https://github.com/jOOQ/jOOQ/issues/9190");

                break;

            case 'G':
                if (parseKeywordIf("GENERATOR"))
                    return parseCreateSequence();
                else if (parseKeywordIf("GLOBAL TEMP TABLE"))
                    return parseCreateTable(true);
                else if (parseKeywordIf("GLOBAL TEMPORARY TABLE"))
                    return parseCreateTable(true);

                break;

            case 'I':
                if (parseKeywordIf("INDEX"))
                    return parseCreateIndex(false);

                break;

            case 'O':
                if (parseKeywordIf("OR REPLACE VIEW"))
                    return parseCreateView(true);
                else if (parseKeywordIf("OR REPLACE FORCE VIEW"))
                    return parseCreateView(true);
                else if (parseKeywordIf("OR ALTER VIEW"))
                    return parseCreateView(true);
                else if (parseKeywordIf("OR REPLACE FUNCTION"))
                    throw notImplemented("CREATE FUNCTION", "https://github.com/jOOQ/jOOQ/issues/9190");
                else if (parseKeywordIf("OR REPLACE PACKAGE"))
                    throw notImplemented("CREATE PACKAGE", "https://github.com/jOOQ/jOOQ/issues/9190");
                else if (parseKeywordIf("OR REPLACE PROCEDURE"))
                    throw notImplemented("CREATE PROCEDURE", "https://github.com/jOOQ/jOOQ/issues/9190");
                else if (parseKeywordIf("OR REPLACE TRIGGER"))
                    throw notImplemented("CREATE TRIGGER", "https://github.com/jOOQ/jOOQ/issues/6956");

                break;

            case 'P':
                if (parseKeywordIf("PACKAGE"))
                    throw notImplemented("CREATE PACKAGE", "https://github.com/jOOQ/jOOQ/issues/9190");
                else if (parseKeywordIf("PROCEDURE"))
                    throw notImplemented("CREATE PROCEDURE", "https://github.com/jOOQ/jOOQ/issues/9190");

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
                else if (parseKeywordIf("TEMP TABLE"))
                    return parseCreateTable(true);
                else if (parseKeywordIf("TEMPORARY TABLE"))
                    return parseCreateTable(true);
                else if (parseKeywordIf("TYPE"))
                    return parseCreateType();
                else if (parseKeywordIf("TABLESPACE"))
                    throw notImplemented("CREATE TABLESPACE");
                else if (parseKeywordIf("TRIGGER"))
                    throw notImplemented("CREATE TRIGGER", "https://github.com/jOOQ/jOOQ/issues/6956");

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
                if (parseKeywordIf("FUNCTION"))
                    throw notImplemented("DROP FUNCTION", "https://github.com/jOOQ/jOOQ/issues/9190");

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
                else if (parseKeywordIf("PROCEDURE"))
                    throw notImplemented("DROP PROCEDURE", "https://github.com/jOOQ/jOOQ/issues/9190");

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
                else if (parseKeywordIf("TYPE"))
                    return parseDropType();
                else if (parseKeywordIf("TABLESPACE"))
                    throw notImplemented("DROP TABLESPACE");
                else if (parseKeywordIf("TRIGGER"))
                    throw notImplemented("DROP TRIGGER", "https://github.com/jOOQ/jOOQ/issues/6956");

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

        throw expected("GENERATOR", "INDEX", "SCHEMA", "SEQUENCE", "TABLE", "TEMPORARY TABLE", "TYPE", "VIEW");
    }

    private final Truncate<?> parseTruncate() {
        parseKeyword("TRUNCATE");
        parseKeyword("TABLE");
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

        TruncateFinalStep<?> step3 =
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
        parseKeyword("EXEC");

        if (parseKeywordIf("SP_RENAME")) {
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
            throw unsupportedClause();
        }
    }

    private final Block parseBlock() {
        List<Statement> statements = new ArrayList<>();





        if (parseKeywordIf("DECLARE") && requireProEdition())



            ;
        else
            parseKeywordIf("EXECUTE BLOCK AS");

        parseKeyword("BEGIN");
        statements.addAll(parseStatements("END"));
        parseKeyword("END");



        parseIf(';');





        return dsl.begin(statements);
    }

    private final void parseSemicolonAfterNonBlocks(Statement result) {
        if (!(result instanceof Block))
            parseIf(';');
        else if (result instanceof BlockImpl && !((BlockImpl) result).alwaysWrapInBeginEnd)
            parseIf(';');
    }

    final Statement parseStatementAndSemicolon() {
        Statement result = parseStatement();
        parseSemicolonAfterNonBlocks(result);
        return result;
    }

    private final List<Statement> parseStatements(String... peek) {
        List<Statement> statements = new ArrayList<>();

        for (;;) {
            Statement parsed;
            Statement stored;




            stored = parsed = parseStatement();

            if (parsed == null)
                break;






            statements.add(stored);
            parseSemicolonAfterNonBlocks(parsed);
            if (peekKeyword(peek))
                break;
        }

        return statements;
    }

























































    private final Block parseDo() {
        parseKeyword("DO");
        String block = parseStringLiteral();
        return (Block) dsl.parser().parseQuery(block);
    }

    private final Statement parseStatement() {
        switch (characterUpper()) {
            case 'C':
                if (peekKeyword("CONTINUE") && requireProEdition())



                ;

                break;

            case 'D':
                if (peekKeyword("DECLARE") && requireProEdition())



                ;

                break;

            case 'E':
                if (peekKeyword("EXIT") && requireProEdition())



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

                break;

            case 'S':
                if (peekKeyword("SET") && requireProEdition())



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
            fields = parseFieldNames().toArray(fields);
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
        parseKeywordIf("IF EXISTS");
        parseIdentifiers();
        if (!parseKeywordIf("CASCADE"))
            parseKeywordIf("RESTRICT");
        return IGNORE;
    }

    private final DDLQuery parseAlterView() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Table<?> oldName = parseTableName();

        if (parseKeywordIf("RENAME")) {
            if (!parseKeywordIf("AS"))
                parseKeyword("TO");
            Table<?> newName = parseTableName();

            return ifExists
                ? dsl.alterViewIfExists(oldName).renameTo(newName)
                : dsl.alterView(oldName).renameTo(newName);
        }
        else if (parseKeywordIf("OWNER TO") && parseUser() != null)
            return IGNORE;
        else
            throw expected("OWNER TO", "RENAME");
    }

    private final DDLQuery parseDropView() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Table<?> tableName = parseTableName();

        DropViewFinalStep s1;

        s1 = ifExists
            ? dsl.dropViewIfExists(tableName)
            : dsl.dropView(tableName);

        return s1;
    }

    private final DDLQuery parseCreateSequence() {
        boolean ifNotExists = parseKeywordIf("IF NOT EXISTS");
        Sequence<?> schemaName = parseSequenceName();

        CreateSequenceFlagsStep s = ifNotExists
            ? dsl.createSequenceIfNotExists(schemaName)
            : dsl.createSequence(schemaName);

        for (;;) {
            Field<Long> field;

            if ((field = parseSequenceStartWithIf()) != null)
                s = s.startWith(field);
            else if ((field = parseSequenceIncrementByIf()) != null)
                s = s.incrementBy(field);
            else if ((field = parseSequenceMinvalueIf()) != null)
                s = s.minvalue(field);
            else if (parseSequenceNoMinvalueIf())
                s = s.noMinvalue();
            else if ((field = parseSequenceMaxvalueIf()) != null)
                s = s.maxvalue(field);
            else if (parseSequenceNoMaxvalueIf())
                s = s.noMaxvalue();
            else if (parseKeywordIf("CYCLE"))
                s = s.cycle();
            else if (parseSequenceNoCycleIf())
                s = s.noCycle();
            else if ((field = parseSequenceCacheIf()) != null)
                s = s.cache(field);
            else if (parseSequenceNoCacheIf()) {
                s = s.noCache();
                continue;
            }
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
            if (!parseKeywordIf("AS"))
                parseKeyword("TO");
            return s.renameTo(parseSequenceName());
        }
        else if (parseKeywordIf("OWNER TO") && parseUser() != null) {
            return IGNORE;
        }
        else {
            boolean found = false;
            AlterSequenceFlagsStep s1 = s;
            while (true) {
                Field<Long> field;

                if ((field = parseSequenceStartWithIf()) != null)
                    s1 = s1.startWith(field);
                else if ((field = parseSequenceIncrementByIf()) != null)
                    s1 = s1.incrementBy(field);
                else if ((field = parseSequenceMinvalueIf()) != null)
                    s1 = s1.minvalue(field);
                else if (parseSequenceNoMinvalueIf())
                    s1 = s1.noMinvalue();
                else if ((field = parseSequenceMaxvalueIf()) != null)
                    s1 = s1.maxvalue(field);
                else if (parseSequenceNoMaxvalueIf())
                    s1 = s1.noMaxvalue();
                else if (parseKeywordIf("CYCLE"))
                    s1 = s1.cycle();
                else if (parseSequenceNoCycleIf())
                    s1 = s1.noCycle();
                else if ((field = parseSequenceCacheIf()) != null)
                    s1 = s1.cache(field);
                else if (parseSequenceNoCacheIf())
                    s1 = s1.noCache();
                else if (parseKeywordIf("RESTART")) {
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
        return dsl.alterSequence((Sequence) sequenceName).restartWith(parseUnsignedInteger());
    }

    private final DDLQuery parseDropSequence() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Sequence<?> sequenceName = parseSequenceName();

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
            else if (!peekKeyword("COMMENT ON") && (keyword = parseAndGetKeywordIf("COMMENT")) != null) {
                parseIf('=');
                comment = parseComment();
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
                        parseSignedInteger();
                        parse(',');
                        parseSignedInteger();
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

                        type = type.defaultValue((Field) toField(parseConcat(null)));

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
                    parseConcat(null);
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
                    fieldComment = parseComment();
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
            do {
                values.add(parseStringLiteral());
            }
            while (parseIf(','));
            parse(')');
        }

        return dsl.createType(name).asEnum(values);
    }

    private final Index parseIndexSpecification(Table<?> table) {
        Name name = parseIdentifierIf();
        parseUsingIndexTypeIf();
        parse('(');
        SortField<?>[] fields = parseSortSpecification().toArray(EMPTY_SORTFIELD);
        parse(')');
        return Internal.createIndex(name == null ? NO_NAME : name, table, fields, false);
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
        parse('(');
        SortField<?>[] fieldExpressions = parseSortSpecification().toArray(EMPTY_SORTFIELD);
        parse(')');

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
        Field<?>[] referencing = parseFieldNames().toArray(EMPTY_FIELD);
        parse(')');
        parseKeyword("REFERENCES");

        return parseForeignKeyReferenceSpecification(constraint, referencing);
    }

    private final Constraint parseForeignKeyReferenceSpecification(ConstraintTypeStep constraint, Field<?>[] referencing) {
        Table<?> referencedTable = parseTableName();
        Field<?>[] referencedFields = EMPTY_FIELD;

        if (parseIf('(')) {
            referencedFields = parseFieldNames().toArray(EMPTY_FIELD);
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
                    parseIf('=');
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
                            while (parseIf(',')) {
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
                        if (!parseKeywordIf("AS"))
                            parseKeyword("TO");
                        Name newName = parseIdentifier();

                        return s1.renameColumn(oldName).to(newName);
                    }
                    else if (parseKeywordIf("INDEX")) {
                        Name oldName = parseIdentifier();
                        if (!parseKeywordIf("AS"))
                            parseKeyword("TO");
                        Name newName = parseIdentifier();

                        return s1.renameIndex(oldName).to(newName);
                    }
                    else if (parseKeywordIf("CONSTRAINT")) {
                        Name oldName = parseIdentifier();
                        if (!parseKeywordIf("AS"))
                            parseKeyword("TO");
                        Name newName = parseIdentifier();

                        return s1.renameConstraint(oldName).to(newName);
                    }
                }

                break;
        }

        throw expected("ADD", "ALTER", "COMMENT", "DROP", "MODIFY", "OWNER TO", "RENAME");
    }

    private final AlterTableFinalStep parseCascadeRestrictIf(AlterTableDropStep step) {
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
            parse('(');
            List<SortField<?>> sort = parseSortSpecification();
            parse(')');

            return name == null
                ? dsl.createIndex().on(tableName, sort)
                : dsl.createIndex(name).on(tableName, sort);
        }

        if (parseIf('(')) {
            do {
                parseAlterTableAddFieldsOrConstraints(list);
            }
            while (parseIf(','));

            parse(')');
        }
        else if (parseKeywordIf("COLUMN IF NOT EXISTS")
              || parseKeywordIf("IF NOT EXISTS")) {
            return parseAlterTableAddFieldFirstBeforeLast(s1.addColumnIfNotExists(parseAlterTableAddField(null)));
        }
        else {
            do {
                parseAlterTableAddFieldsOrConstraints(list);
            }
            while (parseIf(',') && (parseKeywordIf("ADD") || !peekKeyword("ALTER", "COMMENT", "DROP", "MODIFY", "OWNER TO", "RENAME")));
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
                return s1.alter(field).default_((Field) toField(parseConcat(null)));
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
                    if (!parseKeywordIf("AS"))
                        parseKeyword("TO");

                    return dsl.alterTable(oldName.getTable()).renameColumn(oldName).to(parseFieldName());
                }

                break;

            case 'D':
                if (parseKeywordIf("DATABASE")) {
                    Catalog oldName = parseCatalogName();
                    if (!parseKeywordIf("AS"))
                        parseKeyword("TO");

                    return dsl.alterDatabase(oldName).renameTo(parseCatalogName());
                }

                break;

            case 'I':
                if (parseKeywordIf("INDEX")) {
                    Name oldName = parseIndexName();
                    if (!parseKeywordIf("AS"))
                        parseKeyword("TO");

                    return dsl.alterIndex(oldName).renameTo(parseIndexName());
                }

                break;

            case 'S':
                if (parseKeywordIf("SCHEMA")) {
                    Schema oldName = parseSchemaName();
                    if (!parseKeywordIf("AS"))
                        parseKeyword("TO");

                    return dsl.alterSchema(oldName).renameTo(parseSchemaName());
                }
                else if (parseKeywordIf("SEQUENCE")) {
                    Sequence<?> oldName = parseSequenceName();
                    if (!parseKeywordIf("AS"))
                        parseKeyword("TO");

                    return dsl.alterSequence(oldName).renameTo(parseSequenceName());
                }

                break;

            case 'V':
                if (parseKeywordIf("VIEW")) {
                    Table<?> oldName = parseTableName();
                    if (!parseKeywordIf("AS"))
                        parseKeyword("TO");

                    return dsl.alterView(oldName).renameTo(parseTableName());
                }

                break;
        }

        // If all of the above fails, we can assume we're renaming a table.
        parseKeywordIf("TABLE");
        Table<?> oldName = parseTableName();
        if (!parseKeywordIf("AS"))
            parseKeyword("TO");

        return dsl.alterTable(oldName).renameTo(parseTableName());
    }

    private final DDLQuery parseDropTable(boolean temporary) {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Table<?> tableName = parseTableName();
        boolean cascade = parseKeywordIf("CASCADE");
        boolean restrict = !cascade && parseKeywordIf("RESTRICT");

        DropTableStep s1;
        DropTableFinalStep s2;

        s1 = ifExists
           ? dsl.dropTableIfExists(tableName)
           : temporary
           ? dsl.dropTemporaryTable(tableName)
           : dsl.dropTable(tableName);

        s2 = cascade
           ? s1.cascade()
           : restrict
           ? s1.restrict()
           : s1;

        return s2;
    }

    private final DDLQuery parseDropType() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        List<Name> typeNames = parseIdentifiers();
        boolean cascade = parseKeywordIf("CASCADE");
        boolean restrict = !cascade && parseKeywordIf("RESTRICT");

        DropTypeStep s1;
        DropTypeFinalStep s2;

        s1 = ifExists
           ? dsl.dropTypeIfExists(typeNames)
           : dsl.dropType(typeNames);

        s2 = cascade
           ? s1.cascade()
           : restrict
           ? s1.restrict()
           : s1;

        return s2;
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

                if (!parseKeywordIf("TO"))
                    parseKeyword("AS");

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
            if (!parseKeywordIf("AS"))
                parseKeyword("TO");

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
            if (!parseKeywordIf("AS"))
                parseKeyword("TO");

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
        parseKeyword("ON");
        Table<?> tableName = parseTableName();
        parseUsingIndexTypeIf();
        parse('(');
        SortField<?>[] fields = parseSortSpecification().toArray(EMPTY_SORTFIELD);
        parse(')');
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
        CreateIndexFinalStep s4 = condition != null
            ? s3.where(condition)
            : excludeNullKeys
            ? s3.excludeNullKeys()
            : s3;

        return s4;
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
        if (!parseKeywordIf("AS"))
            parseKeyword("TO");
        Name newName = parseIndexName();

        AlterIndexStep s1 = ifExists
            ? dsl.alterIndexIfExists(indexName)
            : dsl.alterIndex(indexName);
        AlterIndexFinalStep s2 = s1.renameTo(newName);
        return s2;

    }

    private final DDLQuery parseDropIndex() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Name indexName = parseIndexName();
        boolean on = parseKeywordIf("ON");
        Table<?> onTable = on ? parseTableName() : null;

        DropIndexOnStep s1;
        DropIndexCascadeStep s2;
        DropIndexFinalStep s3;

        s1 = ifExists
            ? dsl.dropIndexIfExists(indexName)
            : dsl.dropIndex(indexName);

        s2 = on
            ? s1.on(onTable)
            : s1;

        s3 = parseKeywordIf("CASCADE")
            ? s2.cascade()
            : parseKeywordIf("RESTRICT")
            ? s2.restrict()
            : s2;

        return s3;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // QueryPart parsing
    // -----------------------------------------------------------------------------------------------------------------

    private final Condition parseCondition() {
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

        while (parseKeywordIf("AND"))
            condition = toCondition(condition).and(toCondition(parseNot()));

        return condition;
    }

    private final QueryPart parseNot() {
        boolean not = parseKeywordIf("NOT");
        QueryPart condition = parsePredicate();
        return not ? toCondition(condition).not() : condition;
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

            left = parseConcat(null);
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
                            : ((Field) left).compare(comp, DSL.all(parseFields().toArray(EMPTY_FIELD)))

                        // TODO: Support quantifiers also for rows
                        : new RowSubqueryCondition((Row) left, DSL.all(parseWithOrSelect(((Row) left).size())), comp)

                    : any
                    ? left instanceof Field
                        ? peekSelectOrWith(true)
                            ? ((Field) left).compare(comp, DSL.any(parseWithOrSelect(1)))
                            : ((Field) left).compare(comp, DSL.any(parseFields().toArray(EMPTY_FIELD)))

                        // TODO: Support quantifiers also for rows
                        : new RowSubqueryCondition((Row) left, DSL.any(parseWithOrSelect(((Row) left).size())), comp)

                    : left instanceof Field
                        ? ((Field) left).compare(comp, toField(parseConcat(null)))
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

                parseKeyword("DISTINCT FROM");
                if (left instanceof Field) {
                    Field right = toField(parseConcat(null));
                    return not ? ((Field) left).isNotDistinctFrom(right) : ((Field) left).isDistinctFrom(right);
                }
                else {
                    Row right = parseRow(((Row) left).size(), true);
                    return new RowIsDistinctFrom((Row) left, right, not);
                }
            }
            else if (!not && parseIf("@>")) {
                return toField(left).contains((Field) toField(parseConcat(null)));
            }
            else if (parseKeywordIf("IN")) {
                Condition result;

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
                            ? ((Field) left).notIn(parseFields())
                            : new RowInCondition((Row) left, new QueryPartList<>(parseRows(((Row) left).size())), true)
                        : left instanceof Field
                            ? ((Field) left).in(parseFields())
                            : new RowInCondition((Row) left, new QueryPartList<>(parseRows(((Row) left).size())), false);

                parse(')');
                return result;
            }
            else if (parseKeywordIf("BETWEEN")) {
                boolean symmetric = !parseKeywordIf("ASYMMETRIC") && parseKeywordIf("SYMMETRIC");
                FieldOrRow r1 = left instanceof Field
                    ? parseConcat(null)
                    : parseRow(((Row) left).size());
                parseKeyword("AND");
                FieldOrRow r2 = left instanceof Field
                    ? parseConcat(null)
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
                        if (parseIf(')'))
                            fields = Collections.<Field<?>> emptyList();
                        else {
                            fields = new ArrayList<>();
                            do {
                                fields.add(toField(parseConcat(null)));
                            }
                            while (parseIf(','));
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
                        if (parseIf(')'))
                            fields = Collections.<Field<?>> emptyList();
                        else {
                            fields = new ArrayList<>();
                            do {
                                fields.add(toField(parseConcat(null)));
                            }
                            while (parseIf(','));
                            parse(')');
                        }
                        Field<String>[] fieldArray = fields.toArray(new Field[0]);
                        LikeEscapeStep result = (not ^ notOp) ? ((Field<String>) left).notLike(all(fieldArray)) : ((Field<String>) left).like(all(fieldArray));
                        return parseEscapeClauseIf(result);
                    }
                }
                else {
                    Field right = toField(parseConcat(null));
                    LikeEscapeStep like = (not ^ notOp) ? ((Field) left).notLike(right) : ((Field) left).like(right);
                    return parseEscapeClauseIf(like);
                }
            }
            else if (left instanceof Field && (parseKeywordIf("ILIKE") || parseOperatorIf("~~*") || (notOp = parseOperatorIf("!~~*")))) {
                Field right = toField(parseConcat(null));
                LikeEscapeStep like = (not ^ notOp) ? ((Field) left).notLikeIgnoreCase(right) : ((Field) left).likeIgnoreCase(right);
                return parseEscapeClauseIf(like);
            }
            else if (left instanceof Field && (parseKeywordIf("REGEXP")
                                            || parseKeywordIf("RLIKE")
                                            || parseKeywordIf("LIKE_REGEX")
                                            || parseOperatorIf("~")
                                            || (notOp = parseOperatorIf("!~")))) {
                Field right = toField(parseConcat(null));
                return (not ^ notOp)
                        ? ((Field) left).notLikeRegex(right)
                        : ((Field) left).likeRegex(right);
            }
            else if (left instanceof Field && parseKeywordIf("SIMILAR TO")) {
                Field right = toField(parseConcat(null));
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
            else
                return left;
        }
    }

    private final QueryPart parseEscapeClauseIf(LikeEscapeStep like) {
        return parseKeywordIf("ESCAPE") ? like.escape(parseCharacterLiteral()) : like;
    }

    private final List<Table<?>> parseTables() {
        List<Table<?>> result = new ArrayList<>();

        do {
            result.add(parseTable());
        }
        while (parseIf(','));

        return result;
    }

    private final Table<?> parseTable() {
        Table<?> result = parseLateral();

        for (;;) {
            Table<?> joined = parseJoinedTableIf(result);
            if (joined == null)
                return result;
            else
                result = joined;
        }
    }

    private final Table<?> parseLateral() {
        if (parseKeywordIf("LATERAL"))
            return lateral(parseTableFactor());
        else
            return parseTableFactor();
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

    private final Table<?> parseTableFactor() {

        // [#7982] Postpone turning Select into a Table in case there is an alias
        TableLike<?> result = null;

        // TODO [#5306] Support FINAL TABLE (<data change statement>)
        // TOOD ONLY ( table primary )
        if (parseFunctionNameIf("UNNEST") || parseFunctionNameIf("TABLE")) {
            parse('(');
            Field<?> f = parseField(Type.A);

            // Work around a missing feature in unnest()
            if (!f.getType().isArray())
                f = f.coerce(f.getDataType().getArrayDataType());

            result = unnest(f);
            parse(')');
        }
        else if (parseFunctionNameIf("GENERATE_SERIES")) {
            parse('(');
            Field from = toField(parseConcat(Type.N));
            parse(',');
            Field to = toField(parseConcat(Type.N));

            Field step = parseIf(',')
                ? toField(parseConcat(Type.N))
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
            Field path = toField(parseConcat(Type.S));
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

            XMLTablePassingStep s1 = xmltable((Field) toField(parseConcat(Type.S)));
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
            if (peekKeyword("SELECT") || peekKeyword("SEL")) {
                SelectQueryImpl<Record> select = parseSelect();
                parse(')');
                result = parseQueryExpressionBody(null, null, select);
            }
            else if (peekKeyword("VALUES")) {
                result = parseTableValueConstructor();
                parse(')');
            }
            else {
                result = parseJoinedTable();
                parse(')');
            }
        }
        else {
            result = parseTableName();

            // TODO Sample clause
        }

        if (parseKeywordIf("VERSIONS") && requireProEdition()) {





























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

        Name alias = null;
        List<Name> columnAliases = null;

        if (parseKeywordIf("AS"))
            alias = parseIdentifier();
        else if (!peekKeyword(KEYWORDS_IN_FROM) && !peekKeyword(KEYWORDS_IN_STATEMENTS))
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

































































    private final Table<?> parseTableValueConstructor() {
        parseKeyword("VALUES");

        List<Row> rows = new ArrayList<>();
        do {
            rows.add(parseTuple());
        }
        while (parseIf(','));
        return values0(rows.toArray(EMPTY_ROW));
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
            fieldsOrRows = parseFieldsOrRows();
        else
            fieldsOrRows = parseFields();

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

    private final Table<?> parseJoinedTable() {
        Table<?> result = parseLateral();

        for (;;) {
            Table<?> joined = parseJoinedTableIf(result);

            if (joined == null)
                return result;
            else
                result = joined;
        }
    }

    private final Table<?> parseJoinedTableIf(Table<?> left) {
        JoinType joinType = parseJoinTypeIf();

        if (joinType == null)
            return null;

        Table<?> right = joinType.qualified() ? parseTable() : parseLateral();

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
                    result.add(DSL.asterisk().except(parseFieldNames().toArray(EMPTY_FIELD)));
                    parse(')');
                }
                else
                    result.add(DSL.asterisk());
            }
            else if ((qa = parseQualifiedAsteriskIf()) != null) {
                if (parseKeywordIf("EXCEPT")) {
                    parse('(');
                    result.add(qa.except(parseFieldNames().toArray(EMPTY_FIELD)));
                    parse(')');
                }
                else
                    result.add(qa);
            }
            else {
                Name alias = null;
                Field<?> field = null;














                if (field == null) {
                    field = parseField();

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

    private final List<SortField<?>> parseSortSpecification() {
        List<SortField<?>> result = new ArrayList<>();

        do {
            result.add(parseSortField());
        }
        while (parseIf(','));
        return result;
    }

    private final SortField<?> parseSortField() {
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
            return Collections.emptyList();
        }
        else {
            List<Field<?>> result = parseFields();
            parse(')');
            return result;
        }
    }

    private final List<Field<?>> parseFields() {
        List<Field<?>> result = new ArrayList<>();
        do {
            result.add(parseField());
        }
        while (parseIf(','));
        return result;
    }

    private final List<FieldOrRow> parseFieldsOrRows() {
        List<FieldOrRow> result = new ArrayList<>();
        do {
            result.add(parseFieldOrRow());
        }
        while (parseIf(','));
        return result;
    }

    private final Field<?> parseField() {
        return parseField(null);
    }

    private final FieldOrRow parseFieldOrRow() {
        return parseFieldOrRow(null);
    }

    private final Row parseRow() {
        return parseRow(null);
    }

    private final Row parseRowIf() {
        return parseRowIf(null);
    }

    private final List<Row> parseRows(Integer degree) {
        List<Row> result = new ArrayList<>();

        do {
            result.add(parseRow(degree));
        }
        while (parseIf(','));

        return result;
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

    static enum Type {
        A("array"),
        D("date"),
        S("string"),
        N("numeric"),
        B("boolean"),
        Y("binary"),
        J("json"),
        X("xml");

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

    private final FieldOrRow parseFieldOrRow(Type type) {
        if (B.is(type))
            return toFieldOrRow(parseOr());
        else
            return parseConcat(type);
    }

    private final Field<?> parseField(Type type) {
        if (B.is(type))
            return toField(parseOr());
        else
            return toField(parseConcat(type));
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
        if (part == null)
            return null;
        else if (part instanceof Condition)
            return (Condition) part;
        else if (part instanceof Field)
            if (((Field) part).getDataType().getType() == Boolean.class)
                return condition((Field) part);

            // [#7266] Support parsing column references as predicates
            else if (part instanceof TableFieldImpl)
                return condition((Field) part);
            else
                throw expected("Boolean field");
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

    private final FieldOrRow parseConcat(Type type) {
        FieldOrRow r = parseCollated(type);

        if (S.is(type) && r instanceof Field)
            while (parseIf("||"))
                r = concat((Field) r, toField(parseCollated(type)));

        return r;
    }

    private final FieldOrRow parseCollated(Type type) {
        FieldOrRow r = parseNumericOp(type);

        if (S.is(type) && r instanceof Field)
            if (parseKeywordIf("COLLATE"))
                r = ((Field) r).collate(parseCollation());

        return r;
    }

    private final Field<?> parseFieldNumericOpParenthesised() {
        parse('(');
        Field<?> r = toField(parseNumericOp(N));
        parse(')');
        return r;
    }

    private final Field<?> parseFieldParenthesised(Type type) {
        parse('(');
        Field<?> r = toField(parseField(type));
        parse(')');
        return r;
    }

    // Any numeric operator of low precedence
    // See https://www.postgresql.org/docs/current/sql-syntax-lexical.html#SQL-PRECEDENCE
    private final FieldOrRow parseNumericOp(Type type) {
        FieldOrRow r = parseSum(type);

        if (N.is(type) && r instanceof Field)
            for (;;)
                if (parseIf("<<"))
                    r = ((Field) r).shl((Field) parseSum(type));
                else if (parseIf(">>"))
                    r = ((Field) r).shr((Field) parseSum(type));
                else
                    break;

        return r;
    }

    private final FieldOrRow parseSum(Type type) {
        FieldOrRow r = parseFactor(type);

        if (N.is(type) && r instanceof Field)
            for (;;)
                if (parseIf('+'))
                    r = parseSumRightOperand(type, r, true);
                else if (parseIf('-'))
                    r = parseSumRightOperand(type, r, false);
                else
                    break;

        return r;
    }

    private final Field parseSumRightOperand(Type type, FieldOrRow r, boolean add) {
        Field rhs = (Field) parseFactor(type);
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

    private final FieldOrRow parseFactor(Type type) {
        FieldOrRow r = parseExp(type);

        if (N.is(type) && r instanceof Field)
            for (;;)
                if (!peek("*=") && parseIf('*'))
                    r = ((Field) r).mul((Field) parseExp(type));
                else if (parseIf('/'))
                    r = ((Field) r).div((Field) parseExp(type));
                else if (parseIf('%'))
                    r = ((Field) r).mod((Field) parseExp(type));






                else
                    break;

        return r;
    }

    private final FieldOrRow parseExp(Type type) {
        FieldOrRow r = parseUnaryOps(type);

        if (N.is(type) && r instanceof Field)
            for (;;)
                if (!peek("^=") && parseIf('^'))
                    r = ((Field) r).pow(toField(parseUnaryOps(type)));
                else
                    break;

        return r;
    }

    private final FieldOrRow parseUnaryOps(Type type) {
        if (parseKeywordIf("CONNECT_BY_ROOT") && requireProEdition()) {



        }

        FieldOrRow r;
        Sign sign = parseSign();

        if (sign == Sign.NONE)
            r = parseTerm(type);
        else if (sign == Sign.PLUS)
            r = toField(parseTerm(type));
        else if ((r = parseFieldUnsignedNumericLiteralIf(Sign.MINUS)) == null)
            r = toField(parseTerm(type)).neg();

        if (parseTokensIf('(', '+', ')') && requireProEdition())



            ;

        if (parseIf('('))
            throw exception("Unknown function");

        while (parseIf("::"))
            r = cast(toField(r), parseDataType());




        if (parseIf('[')) {
            r = arrayGet((Field) toField(r), (Field) parseField(N));
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

    private final FieldOrRow parseTerm(Type type) {
        FieldOrRow field;
        Object value;

        switch (characterUpper()) {
            case ':':
            case '?':
                return parseBindVariable();









            case '\'':
                return inline(parseStringLiteral());

            case '$':
                if ((value = parseDollarQuotedStringLiteralIf()) != null)
                    return inline((String) value);

                break;

            case 'A':
                if (N.is(type))
                    if (parseFunctionNameIf("ABS"))
                        return abs((Field) parseFieldNumericOpParenthesised());
                    else if (parseFunctionNameIf("ASCII"))
                        return ascii((Field) parseFieldParenthesised(S));
                    else if (parseFunctionNameIf("ACOS"))
                        return acos((Field) parseFieldNumericOpParenthesised());
                    else if (parseFunctionNameIf("ASIN"))
                        return asin((Field) parseFieldNumericOpParenthesised());
                    else if (parseFunctionNameIf("ATAN"))
                        return atan((Field) parseFieldNumericOpParenthesised());
                    else if ((field = parseFieldAtan2If()) != null)
                        return field;

                if (A.is(type))
                    if ((field = parseArrayValueConstructorIf()) != null)
                        return field;

                if ((field = parseFieldArrayGetIf()) != null)
                    return field;

                break;

            case 'B':
                if (N.is(type))
                    if (parseFunctionNameIf("BIT_LENGTH"))
                        return bitLength((Field) parseFieldParenthesised(S));
                    else if (parseFunctionNameIf("BIT_COUNT"))
                        return bitCount((Field) parseFieldNumericOpParenthesised());
                    else if ((field = parseFieldBitwiseFunctionIf()) != null)
                        return field;

                if (B.is(type))
                    if ((value = parseBitLiteralIf()) != null)
                        return DSL.inline((Boolean) value);

                break;

            case 'C':
                if (S.is(type))
                    if ((field = parseFieldConcatIf()) != null)
                        return field;
                    else if ((parseFunctionNameIf("CURRENT_CATALOG") && parse('(') && parse(')')))
                        return currentCatalog();
                    else if ((parseFunctionNameIf("CURRENT_DATABASE") && parse('(') && parse(')')))
                        return currentCatalog();
                    else if ((parseKeywordIf("CURRENT_SCHEMA") || parseKeywordIf("CURRENT SCHEMA")) && (parseIf('(') && parse(')') || true))
                        return currentSchema();
                    else if ((parseKeywordIf("CURRENT_USER") || parseKeywordIf("CURRENT USER")) && (parseIf('(') && parse(')') || true))
                        return currentUser();

                if (N.is(type))
                    if ((field = parseFieldCharIndexIf()) != null)
                        return field;
                    else if (parseFunctionNameIf("CHAR_LENGTH"))
                        return charLength((Field) parseFieldParenthesised(S));
                    else if (parseFunctionNameIf("CARDINALITY"))
                        return cardinality((Field) parseFieldParenthesised(A));
                    else if (parseFunctionNameIf("CEILING") || parseFunctionNameIf("CEIL"))
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
                        return century(parseFieldParenthesised(D));

                if (D.is(type))
                    if ((parseKeywordIf("CURRENT_DATE") || parseKeywordIf("CURRENT DATE")) && (parseIf('(') && parse(')') || true))
                        return currentDate();
                    else if (parseKeywordIf("CURRENT_TIMESTAMP") || parseKeywordIf("CURRENT TIMESTAMP")) {
                        Field<Integer> precision = null;
                        if (parseIf('('))
                            if (!parseIf(')')) {
                                precision = (Field<Integer>) parseField(N);
                                parse(')');
                            }
                        return precision != null ? currentTimestamp(precision) : currentTimestamp();
                    }
                    else if ((parseKeywordIf("CURRENT_TIME") || parseKeywordIf("CURRENT TIME")) && (parseIf('(') && parse(')') || true))
                        return currentTime();
                    else if (parseFunctionNameIf("CURDATE") && parse('(') && parse(')'))
                        return currentDate();
                    else if (parseFunctionNameIf("CURTIME") && parse('(') && parse(')'))
                        return currentTime();

                if ((field = parseFieldCaseIf()) != null)
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
                if (S.is(type))
                    if ((parseFunctionNameIf("DB_NAME") && parse('(') && parse(')')))
                        return currentCatalog();
                    else if ((parseFunctionNameIf("DBINFO") && parse('(') && parseStringLiteral("dbname") != null && parse(')')))
                        return currentCatalog();

                if (D.is(type))
                    if ((field = parseFieldDateLiteralIf()) != null)
                        return field;
                    else if ((field = parseFieldDateTruncIf()) != null)
                        return field;
                    else if ((field = parseFieldDateAddIf()) != null)
                        return field;
                    else if ((field = parseFieldDateDiffIf()) != null)
                        return field;
                    else if ((field = parseFieldDatePartIf()) != null)
                        return field;

                if (N.is(type))
                    if ((field = parseFieldDenseRankIf()) != null)
                        return field;
                    else if (parseFunctionNameIf("DECADE"))
                        return decade(parseFieldParenthesised(D));
                    else if (parseFunctionNameIf("DAY")
                          || parseFunctionNameIf("DAYOFMONTH"))
                        return day(parseFieldParenthesised(D));
                    // DB2 and MySQL support the non-ISO version where weeks go from Sunday = 1 to Saturday = 7
                    else if (parseFunctionNameIf("DAYOFWEEK_ISO"))
                        return isoDayOfWeek(parseFieldParenthesised(D));
                    else if (parseFunctionNameIf("DAYOFWEEK")
                          || parseFunctionNameIf("DAY_OF_WEEK"))
                        return dayOfWeek(parseFieldParenthesised(D));
                    else if (parseFunctionNameIf("DAYOFYEAR")
                          || parseFunctionNameIf("DAY_OF_YEAR"))
                        return dayOfYear(parseFieldParenthesised(D));
                    else if (parseFunctionNameIf("DEGREES")
                          || parseFunctionNameIf("DEGREE")
                          || parseFunctionNameIf("DEG"))
                        return deg((Field) parseFieldNumericOpParenthesised());

                if ((field = parseFieldDecodeIf()) != null)
                    return field;

                break;

            case 'E':

                // [#6704] PostgreSQL E'...' escaped string literals
                if (S.is(type))
                    if (characterNext() == '\'')
                        return inline(parseStringLiteral());

                if (N.is(type))
                    if ((field = parseFieldExtractIf()) != null)
                        return field;
                    else if (parseFunctionNameIf("EXP"))
                        return exp((Field) parseFieldNumericOpParenthesised());

                if (D.is(type))
                    if (parseFunctionNameIf("EPOCH"))
                        return epoch(parseFieldParenthesised(D));

                break;

            case 'F':
                if (N.is(type))
                    if (parseFunctionNameIf("FLOOR"))
                        return floor((Field) parseFieldNumericOpParenthesised());

                if ((field = parseFieldFirstValueIf()) != null)
                    return field;
                else if ((field = parseFieldFieldIf()) != null)
                    return field;

                break;

            case 'G':
                if (D.is(type))
                    if (parseKeywordIf("GETDATE") && parse('(') && parse(')'))
                        return currentTimestamp();

                if ((field = parseFieldGreatestIf()) != null)
                    return field;
                else if (N.is(type) && (field = parseFieldGroupIdIf()) != null)
                    return field;
                else if (N.is(type) && (field = parseFieldGroupingIdIf()) != null)
                    return field;
                else if (N.is(type) && (field = parseFieldGroupingIf()) != null)
                    return field;
                else
                    break;

            case 'H':
                if (N.is(type))
                    if (parseFunctionNameIf("HOUR"))
                        return hour(parseFieldParenthesised(D));

                break;

            case 'I':
                if (D.is(type))
                    if ((field = parseFieldIntervalLiteralIf()) != null)
                        return field;
                    else if (parseFunctionNameIf("ISO_DAY_OF_WEEK"))
                        return isoDayOfWeek(parseFieldParenthesised(D));

                if (N.is(type))
                    if ((field = parseFieldInstrIf()) != null)
                        return field;

                if (S.is(type))
                    if ((field = parseFieldInsertIf()) != null)
                        return field;

                if ((field = parseFieldIfnullIf()) != null)
                    return field;
                else if ((field = parseFieldIsnullIf()) != null)
                    return field;
                else if ((field = parseFieldIfIf()) != null)
                    return field;
                else
                    break;

            case 'J':
                if (J.is(type))
                    if ((field = parseFieldJSONArrayConstructorIf()) != null)
                        return field;
                    else if ((field = parseFieldJSONArrayAggIf()) != null)
                        return field;
                    else if ((field = parseFieldJSONObjectConstructorIf()) != null)
                        return field;
                    else if ((field = parseFieldJSONObjectAggIf()) != null)
                        return field;
                    else if ((field = parseFieldJSONValueIf()) != null)
                        return field;

                break;

            case 'L':
                if (S.is(type))
                    if (parseFunctionNameIf("LOWER") || parseFunctionNameIf("LCASE"))
                        return lower((Field) parseFieldParenthesised(S));
                    else if ((field = parseFieldLpadIf()) != null)
                        return field;
                    else if ((field = parseFieldLtrimIf()) != null)
                        return field;
                    else if ((field = parseFieldLeftIf()) != null)
                        return field;

                if (N.is(type))
                    if (parseFunctionNameIf("LENGTH") || parseFunctionNameIf("LEN"))
                        return length((Field) parseFieldParenthesised(S));
                    else if (parseFunctionNameIf("LN"))
                        return ln((Field) parseFieldNumericOpParenthesised());
                    else if ((field = parseFieldLogIf()) != null)
                        return field;
                    else if (parseKeywordIf("LEVEL") && requireProEdition()) {



                    }
                    else if ((field = parseFieldShlIf()) != null)
                        return field;

                if ((field = parseFieldLeastIf()) != null)
                    return field;
                else if ((field = parseFieldLeadLagIf()) != null)
                    return field;
                else if ((field = parseFieldLastValueIf()) != null)
                    return field;

                break;

            case 'M':
                if (N.is(type))
                    if ((field = parseFieldModIf()) != null)
                        return field;
                    else if (parseFunctionNameIf("MICROSECOND"))
                        return microsecond(parseFieldParenthesised(D));
                    else if (parseFunctionNameIf("MILLENNIUM"))
                        return millennium(parseFieldParenthesised(D));
                    else if (parseFunctionNameIf("MILLISECOND"))
                        return millisecond(parseFieldParenthesised(D));
                    else if (parseFunctionNameIf("MINUTE"))
                        return minute(parseFieldParenthesised(D));
                    else if (parseFunctionNameIf("MONTH"))
                        return month(parseFieldParenthesised(D));

                if (S.is(type))
                    if ((field = parseFieldMidIf()) != null)
                        return field;
                    else if (parseFunctionNameIf("MD5"))
                        return md5((Field) parseFieldParenthesised(S));

                break;

            case 'N':

                // [#9540] N'...' NVARCHAR literals
                if (S.is(type))
                    if (characterNext() == '\'')
                        return inline(parseStringLiteral(), NVARCHAR);

                if ((field = parseFieldNvl2If()) != null)
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
                    Field<Integer> precision = (Field<Integer>) parseField(N);
                    parse(')');
                    return now(precision);
                }

                break;

            case 'O':
                if (S.is(type))
                    if ((field = parseFieldReplaceIf()) != null)
                        return field;
                    else if ((field = parseFieldOverlayIf()) != null)
                        return field;

                if (N.is(type))
                    if (parseFunctionNameIf("OCTET_LENGTH"))
                        return octetLength((Field) parseFieldParenthesised(S));

                break;

            case 'P':
                if (N.is(type))
                    if ((field = parseFieldPositionIf()) != null)
                        return field;
                    else if ((field = parseFieldPercentRankIf()) != null)
                        return field;
                    else if ((field = parseFieldPowerIf()) != null)
                        return field;
                    else if (parseFunctionNameIf("PI") && parse('(') && parse(')'))
                        return pi();

                if (parseKeywordIf("PRIOR") && requireProEdition()) {



                }

                break;

            case 'Q':
                if (S.is(type))
                    if (characterNext() == '\'')
                        return inline(parseStringLiteral());

                if (D.is(type))
                    if (parseFunctionNameIf("QUARTER"))
                        return quarter(parseFieldParenthesised(D));

            case 'R':
                if (S.is(type))
                    if ((field = parseFieldReplaceIf()) != null)
                        return field;
                    else if ((field = parseFieldRegexpReplaceIf()) != null)
                        return field;
                    else if ((field = parseFieldRepeatIf()) != null)
                        return field;
                    else if (parseFunctionNameIf("REVERSE"))
                        return reverse((Field) parseFieldParenthesised(S));
                    else if ((field = parseFieldRpadIf()) != null)
                        return field;
                    else if ((field = parseFieldRtrimIf()) != null)
                        return field;
                    else if ((field = parseFieldRightIf()) != null)
                        return field;

                if (N.is(type))
                    if ((field = parseFieldRowNumberIf()) != null)
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

                if (parseFunctionNameIf("ROW"))
                    return parseTuple();

                break;

            case 'S':
                if (S.is(type))
                    if ((field = parseFieldSubstringIf()) != null)
                        return field;
                    else if (parseFunctionNameIf("SPACE"))
                        return space((Field) parseFieldParenthesised(N));
                    else if ((field = parseFieldSplitPartIf()) != null)
                        return field;
                    else if ((field = parseFieldReplaceIf()) != null)
                        return field;
                    else if (parseFunctionNameIf("SCHEMA") && parseIf('(') && parse(')'))
                        return currentSchema();

                if (N.is(type))
                    if (parseFunctionNameIf("SECOND"))
                        return second(parseFieldParenthesised(D));
                    else if (parseFunctionNameIf("SIGN"))
                        return sign((Field) parseFieldParenthesised(N));
                    else if (parseFunctionNameIf("SQRT") || parseFunctionNameIf("SQR"))
                        return sqrt((Field) parseFieldNumericOpParenthesised());
                    else if (parseFunctionNameIf("SINH"))
                        return sinh((Field) parseFieldNumericOpParenthesised());
                    else if (parseFunctionNameIf("SIN"))
                        return sin((Field) parseFieldNumericOpParenthesised());
                    else if ((field = parseFieldShlIf()) != null)
                        return field;
                    else if ((field = parseFieldShrIf()) != null)
                        return field;

                if ((field = parseFieldSysConnectByPathIf()) != null)
                    return field;

                break;

            case 'T':
                if (B.is(type))
                    if ((field = parseBooleanValueExpressionIf()) != null)
                        return field;

                if (S.is(type))
                    if ((field = parseFieldTrimIf()) != null)
                        return field;
                    else if ((field = parseFieldTranslateIf()) != null)
                        return field;
                    else if ((field = parseFieldToCharIf()) != null)
                        return field;

                if (N.is(type))
                    if (parseFunctionNameIf("TANH"))
                        return tanh((Field) parseFieldNumericOpParenthesised());
                    else if (parseFunctionNameIf("TAN"))
                        return tan((Field) parseFieldNumericOpParenthesised());
                    else if ((field = parseFieldToNumberIf()) != null)
                        return field;
                    else if (parseFunctionNameIf("TIMEZONE_HOUR"))
                        return timezoneHour(parseFieldParenthesised(D));
                    else if (parseFunctionNameIf("TIMEZONE_MINUTE"))
                        return timezoneMinute(parseFieldParenthesised(D));
                    else if (parseFunctionNameIf("TIMEZONE"))
                        return timezone(parseFieldParenthesised(D));

                if (D.is(type))
                    if ((field = parseFieldTimestampLiteralIf()) != null)
                        return field;
                    else if ((field = parseFieldTimeLiteralIf()) != null)
                        return field;
                    else if ((field = parseFieldToDateIf()) != null)
                        return field;
                    else if ((field = parseFieldToTimestampIf()) != null)
                        return field;
                    else if ((field = parseFieldTimestampDiffIf()) != null)
                        return field;

                if (N.is(type) || D.is(type))
                    if ((field = parseFieldTruncIf()) != null)
                        return field;

                break;

            case 'U':
                if (S.is(type))
                    if (parseFunctionNameIf("UPPER")
                        || parseFunctionNameIf("UCASE"))
                        return DSL.upper((Field) parseFieldParenthesised(S));

                if (D.is(type))
                    if (parseFunctionNameIf("UNIX_TIMESTAMP"))
                        return epoch(parseFieldParenthesised(D));

                break;

            case 'W':
                if (N.is(type))
                    if ((field = parseFieldWidthBucketIf()) != null)
                        return field;
                    else if (parseFunctionNameIf("WEEK"))
                        return week(parseFieldParenthesised(D));

                break;

            case 'X':
                if (Y.is(type))
                    if ((value = parseBinaryLiteralIf()) != null)
                        return inline((byte[]) value);

                if (X.is(type))
                    if ((field = parseFieldXMLCommentIf()) != null)
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

                break;

            case 'Y':
                if (N.is(type))
                    if (parseFunctionNameIf("YEAR"))
                        return year(parseFieldParenthesised(D));

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
                        field = parseTerm(type);
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
                try {
                    if (peekSelect(true)) {
                        SelectQueryImpl<Record> select = parseSelect();
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
                FieldOrRow r = parseFieldOrRow(type);
                List<Field<?>> list = null;

                if (r instanceof Field) {
                    while (parseIf(',')) {
                        if (list == null) {
                            list = new ArrayList<>();
                            list.add((Field) r);
                        }

                        // TODO Allow for nesting ROWs
                        list.add(parseField(type));
                    }
                }

                parse(')');
                return list != null ? row(list) : r;
        }

        if ((field = parseAggregateFunctionIf()) != null)
            return field;

        else if ((field = parseBooleanValueExpressionIf()) != null)
            return field;

        else
            return parseFieldNameOrSequenceExpression();
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
            Field<?> f1 = parseField(S);
            parse(',');
            Field<?> f2 = parseField(S);
            parse(',');
            Field<?> f3 = parseField(N);
            parse(')');

            return splitPart((Field) f1, (Field) f2, (Field) f3);
        }

        return null;
    }

    private final Field<?> parseFieldShlIf() {
        if (parseKeywordIf("SHL") || parseKeywordIf("SHIFTLEFT") || parseKeywordIf("LSHIFT")) {
            parse('(');
            Field<?> x = toField(parseNumericOp(N));
            parse(',');
            Field<?> y = toField(parseNumericOp(N));
            parse(')');

            return shl((Field) x, (Field) y);
        }

        return null;
    }

    private final Field<?> parseFieldShrIf() {
        if (parseKeywordIf("SHR") || parseKeywordIf("SHIFTRIGHT") || parseKeywordIf("RSHIFT")) {
            parse('(');
            Field<?> x = toField(parseNumericOp(N));
            parse(',');
            Field<?> y = toField(parseNumericOp(N));
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

        if (c1 != 'I' && c1 != 'i')
            return null;
        if (c2 != 'T' && c2 != 't' && c2 != 'N' && c2 != 'n')
            return null;

        if (parseKeywordIf("BIT_AND") || parseKeywordIf("BITAND") || parseKeywordIf("BIN_AND")) {
            parse('(');
            Field<?> x = toField(parseNumericOp(N));
            parse(',');
            Field<?> y = toField(parseNumericOp(N));
            parse(')');

            return bitAnd((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_NAND") || parseKeywordIf("BITNAND") || parseKeywordIf("BIN_NAND")) {
            parse('(');
            Field<?> x = toField(parseNumericOp(N));
            parse(',');
            Field<?> y = toField(parseNumericOp(N));
            parse(')');

            return bitNand((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_OR") || parseKeywordIf("BITOR") || parseKeywordIf("BIN_OR")) {
            parse('(');
            Field<?> x = toField(parseNumericOp(N));
            parse(',');
            Field<?> y = toField(parseNumericOp(N));
            parse(')');

            return bitOr((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_NOR") || parseKeywordIf("BITNOR") || parseKeywordIf("BIN_NOR")) {
            parse('(');
            Field<?> x = toField(parseNumericOp(N));
            parse(',');
            Field<?> y = toField(parseNumericOp(N));
            parse(')');

            return bitNor((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_XOR") || parseKeywordIf("BITXOR") || parseKeywordIf("BIN_XOR")) {
            parse('(');
            Field<?> x = toField(parseNumericOp(N));
            parse(',');
            Field<?> y = toField(parseNumericOp(N));
            parse(')');

            return bitXor((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_XNOR") || parseKeywordIf("BITXNOR") || parseKeywordIf("BIN_XNOR")) {
            parse('(');
            Field<?> x = toField(parseNumericOp(N));
            parse(',');
            Field<?> y = toField(parseNumericOp(N));
            parse(')');

            return bitXNor((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_NOT") || parseKeywordIf("BITNOT") || parseKeywordIf("BIN_NOT")) {
            parse('(');
            Field<?> x = toField(parseNumericOp(N));
            parse(')');

            return bitNot((Field) x);
        }
        else if (parseKeywordIf("BIN_SHL")) {
            parse('(');
            Field<?> x = toField(parseNumericOp(N));
            parse(',');
            Field<?> y = toField(parseNumericOp(N));
            parse(')');

            return shl((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIN_SHR")) {
            parse('(');
            Field<?> x = toField(parseNumericOp(N));
            parse(',');
            Field<?> y = toField(parseNumericOp(N));
            parse(')');

            return shr((Field) x, (Field) y);
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

    private final Field<?> parseFieldXMLConcatIf() {
        if (parseFunctionNameIf("XMLCONCAT")) {
            parse('(');
            List<Field<?>> fields = parseFields();
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
                s2 = s1.orderBy(parseSortSpecification());

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
































            parse(')');
            return s1;
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

    private final Field<?> parseFieldJSONArrayConstructorIf() {
        if (parseFunctionNameIf("JSON_ARRAY")) {
            parse('(');
            if (parseIf(')'))
                return DSL.jsonArray();

            List<Field<?>> result = null;
            JSONNullType nullType = parseJSONNullTypeIf();

            if (nullType == null) {
                result = parseFields();
                nullType = parseJSONNullTypeIf();
            }

            parse(')');

            JSONArrayNullStep<JSON> a = result == null ? DSL.jsonArray() : DSL.jsonArray(result);
            return nullType == NULL_ON_NULL
                 ? a.nullOnNull()
                 : nullType == ABSENT_ON_NULL
                 ? a.absentOnNull()
                 : a;
        }

        return null;
    }

    private final Field<?> parseFieldJSONArrayAggIf() {
        if (parseFunctionNameIf("JSON_ARRAYAGG")) {
            Field<?> result;
            JSONArrayAggOrderByStep<JSON> s1;
            JSONArrayAggNullStep<JSON> s2;
            JSONNullType nullType;

            parse('(');
            result = s2 = s1 = DSL.jsonArrayAgg(parseField());

            if (parseKeywordIf("ORDER BY"))
                result = s2 = s1.orderBy(parseSortSpecification());

            if ((nullType = parseJSONNullTypeIf()) != null)
                result = nullType == ABSENT_ON_NULL ? s2.absentOnNull() : s2.nullOnNull();

            parse(')');
            return result;
        }

        return null;
    }

    private final Field<?> parseFieldJSONObjectConstructorIf() {
        if (parseFunctionNameIf("JSON_OBJECT")) {
            parse('(');
            if (parseIf(')'))
                return DSL.jsonObject();

            List<JSONEntry<?>> result = new ArrayList<>();
            JSONNullType nullType = parseJSONNullTypeIf();

            if (nullType == null) {
                do {
                    result.add(parseJSONEntry());
                }
                while (parseIf(','));

                nullType = parseJSONNullTypeIf();
            }
            parse(')');

            JSONObjectNullStep<JSON> o = DSL.jsonObject(result);
            return nullType == NULL_ON_NULL
                 ? o.nullOnNull()
                 : nullType == ABSENT_ON_NULL
                 ? o.absentOnNull()
                 : o;
        }

        return null;
    }

    private final Field<?> parseFieldJSONObjectAggIf() {
        if (parseFunctionNameIf("JSON_OBJECTAGG")) {
            Field<?> result;
            JSONObjectAggNullStep<JSON> s1;
            JSONNullType nullType;

            parse('(');
            result = s1 = DSL.jsonObjectAgg(parseJSONEntry());

            if ((nullType = parseJSONNullTypeIf()) != null)
                result = nullType == ABSENT_ON_NULL ? s1.absentOnNull() : s1.nullOnNull();

            parse(')');
            return result;
        }

        return null;
    }

    private final JSONNullType parseJSONNullTypeIf() {
        if (parseKeywordIf("NULL ON NULL"))
            return NULL_ON_NULL;
        else if (parseKeywordIf("ABSENT ON NULL"))
            return ABSENT_ON_NULL;
        else
            return null;
    }

    private final JSONEntry<?> parseJSONEntry() {
        boolean valueRequired = parseKeywordIf("KEY");

        Field<String> key = (Field<String>) parseField(Type.S);
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
            parse('[');

            List<Field<?>> fields;
            if (parseIf(']')) {
                fields = Collections.<Field<?>>emptyList();
            }
            else {
                fields = parseFields();
                parse(']');
            }

            // Prevent "wrong" javac method bind
            return DSL.array((Collection) fields);
        }

        return null;
    }

    private final Field<?> parseFieldArrayGetIf() {
        if (parseFunctionNameIf("ARRAY_GET")) {
            parse('(');
            Field f1 = parseField(A);
            parse(',');
            Field f2 = parseField(N);
            parse(')');

            return arrayGet(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldAtan2If() {
        if (parseFunctionNameIf("ATN2") || parseFunctionNameIf("ATAN2")) {
            parse('(');
            Field<?> x = toField(parseNumericOp(N));
            parse(',');
            Field<?> y = toField(parseNumericOp(N));
            parse(')');

            return atan2((Field) x, (Field) y);
        }

        return null;
    }

    private final Field<?> parseFieldLogIf() {
        if (parseFunctionNameIf("LOG")) {
            parse('(');
            switch (family()) {















                default:
                    Field<?> base = toField(parseNumericOp(N));
                    parse(',');
                    Field<?> value = toField(parseNumericOp(N));
                    parse(')');
                    return log((Field) value, (Field) base);
            }
        }

        return null;
    }

    private final Field<?> parseFieldTruncIf() {
        if (parseFunctionNameIf("TRUNC")) {
            parse('(');
            Field<?> arg1 = parseField();

            if (parseIf(',')) {

                String part;
                if ((part = parseStringLiteralIf()) != null) {
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
                    Field<?> arg2 = toField(parseNumericOp(N));
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
            arg1 = toField(parseNumericOp(N));
            if (parseIf(','))
                arg2 = toField(parseNumericOp(N));

            parse(')');
            return arg2 == null ? round(arg1) : round(arg1, arg2);
        }

        return null;
    }

    private final Field<?> parseFieldPowerIf() {
        if (parseFunctionNameIf("POWER") || parseFunctionNameIf("POW")) {
            parse('(');
            Field arg1 = toField(parseNumericOp(N));
            parse(',');
            Field arg2 = toField(parseNumericOp(N));
            parse(')');
            return DSL.power(arg1, arg2);
        }

        return null;
    }

    private final Field<?> parseFieldModIf() {
        if (parseFunctionNameIf("MOD")) {
            parse('(');
            Field<?> f1 = parseField(N);
            parse(',');
            Field<?> f2 = parseField(N);
            parse(')');
            return f1.mod((Field) f2);
        }

        return null;
    }

    private final Field<?> parseFieldWidthBucketIf() {
        if (parseFunctionNameIf("WIDTH_BUCKET")) {
            parse('(');
            Field<?> f1 = parseField(N);
            parse(',');
            Field<?> f2 = parseField(N);
            parse(',');
            Field<?> f3 = parseField(N);
            parse(',');
            Field<?> f4 = parseField(N);
            parse(')');
            return DSL.widthBucket((Field) f1, (Field) f2, (Field) f3, (Field) f4);
        }

        return null;
    }

    private final Field<?> parseFieldLeastIf() {
        if (parseFunctionNameIf("LEAST")) {
            parse('(');
            List<Field<?>> fields = parseFields();
            parse(')');

            return least(fields.get(0), fields.size() > 1 ? fields.subList(1, fields.size()).toArray(EMPTY_FIELD) : EMPTY_FIELD);
        }

        return null;
    }

    private final Field<?> parseFieldGreatestIf() {
        if (parseFunctionNameIf("GREATEST")) {
            parse('(');
            List<Field<?>> fields = parseFields();
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
                Field<?> f = parseField(S);
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
                Field<?> f = parseField(S);
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
                Long interval = parseUnsignedIntegerIf();

                if (interval != null) {
                    DatePart part = parseIntervalDatePart();
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

        position(p);
        return null;
    }

    private final Interval parseIntervalLiteral() {
        Interval result = parsePostgresIntervalLiteralIf();
        if (result != null)
            return result;

        String string = parseStringLiteral();
        String message = "Illegal interval literal";

        if (parseKeywordIf("YEAR"))
            if (parseKeywordIf("TO") && parseKeyword("MONTH"))
                return requireNotNull(YearToMonth.yearToMonth(string), message);
            else
                return requireNotNull(YearToMonth.year(string), message);
        else if (parseKeywordIf("MONTH"))
            return requireNotNull(YearToMonth.month(string), message);
        else if (parseKeywordIf("DAY"))
            if (parseKeywordIf("TO"))
                if (parseKeywordIf("SECOND"))
                    return requireNotNull(DayToSecond.dayToSecond(string), message);
                else if (parseKeywordIf("MINUTE"))
                    return requireNotNull(DayToSecond.dayToMinute(string), message);
                else if (parseKeywordIf("HOUR"))
                    return requireNotNull(DayToSecond.dayToHour(string), message);
                else
                    throw expected("HOUR", "MINUTE", "SECOND");
            else
                return requireNotNull(DayToSecond.day(string), message);
        else if (parseKeywordIf("HOUR"))
            if (parseKeywordIf("TO"))
                if (parseKeywordIf("SECOND"))
                    return requireNotNull(DayToSecond.hourToSecond(string), message);
                else if (parseKeywordIf("MINUTE"))
                    return requireNotNull(DayToSecond.hourToMinute(string), message);
                else
                    throw expected("MINUTE", "SECOND");
            else
                return requireNotNull(DayToSecond.hour(string), message);
        else if (parseKeywordIf("MINUTE"))
            if (parseKeywordIf("TO") && parseKeyword("SECOND"))
                return requireNotNull(DayToSecond.minuteToSecond(string), message);
            else
                return requireNotNull(DayToSecond.minute(string), message);
        else if (parseKeywordIf("SECOND"))
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
                Field<?> f = parseField(S);
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
        if (parseFunctionNameIf("DATE_TRUNC")) {
            parse('(');
            DatePart part = DatePart.valueOf(parseStringLiteral().toUpperCase());
            parse(',');
            Field<?> field = parseField(D);
            parse(')');

            return trunc(field, part);
        }

        return null;
    }

    private final Field<?> parseFieldDateAddIf() {
        if (parseFunctionNameIf("DATEADD")) {
            parse('(');
            DatePart part = parseDatePart();
            parse(',');
            Field<Number> interval = (Field<Number>) parseField(Type.N);
            parse(',');
            Field<Date> date = (Field<Date>) parseField(Type.D);
            parse(')');

            return DSL.dateAdd(date, interval, part);
        }

        return null;
    }

    private final Field<?> parseFieldDateDiffIf() {
        if (parseFunctionNameIf("DATEDIFF")) {
            parse('(');
            DatePart datePart = parseDatePartIf();

            if (datePart != null)
                parse(',');

            Field<Date> d1 = (Field<Date>) parseField(Type.D);

            if (parseIf(',')) {
                Field<Date> d2 = (Field<Date>) parseField(Type.D);
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
            Field<String> result = concat(parseFields().toArray(EMPTY_FIELD));
            parse(')');
            return result;
        }

        return null;
    }

    private final Field<?> parseFieldInstrIf() {
        if (parseFunctionNameIf("INSTR")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parse(',');
            Field<String> f2 = (Field) parseField(S);
            parse(')');
            return DSL.position(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldCharIndexIf() {
        if (parseFunctionNameIf("CHARINDEX")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parse(',');
            Field<String> f2 = (Field) parseField(S);
            parse(')');
            return DSL.position(f2, f1);
        }

        return null;
    }

    private final Field<?> parseFieldLpadIf() {
        if (parseFunctionNameIf("LPAD")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parse(',');
            Field<Integer> f2 = (Field) parseField(N);
            Field<String> f3 = parseIf(',')
                ? (Field) parseField(S)
                : null;
            parse(')');
            return f3 == null
                ? lpad(f1, f2)
                : lpad(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldRpadIf() {
        if (parseFunctionNameIf("RPAD")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parse(',');
            Field<Integer> f2 = (Field) parseField(N);
            Field<String> f3 = parseIf(',')
                ? (Field) parseField(S)
                : null;
            parse(')');
            return f3 == null
                ? rpad(f1, f2)
                : rpad(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldInsertIf() {
        if (parseFunctionNameIf("INSERT")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parse(',');
            Field<Number> f2 = (Field) parseField(N);
            parse(',');
            Field<Number> f3 = (Field) parseField(N);
            parse(',');
            Field<String> f4 = (Field) parseField(S);
            parse(')');

            return insert(f1, f2, f3, f4);
        }

        return null;
    }

    private final Field<?> parseFieldOverlayIf() {
        if (parseFunctionNameIf("OVERLAY")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parseKeyword("PLACING");
            Field<String> f2 = (Field) parseField(S);
            parseKeyword("FROM");
            Field<Number> f3 = (Field) parseField(N);
            Field<Number> f4 =
                parseKeywordIf("FOR")
              ? (Field) parseField(N)




              : null;
            parse(')');

            return f4 == null ? overlay(f1, f2, f3) : overlay(f1, f2, f3, f4);
        }

        return null;
    }

    private final Field<?> parseFieldPositionIf() {
        if (parseFunctionNameIf("POSITION")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parseKeyword("IN");
            Field<String> f2 = (Field) parseField(S);
            parse(')');
            return DSL.position(f2, f1);
        }

        return null;
    }

    private final Field<?> parseFieldRepeatIf() {
        if (parseFunctionNameIf("REPEAT")) {
            parse('(');
            Field<String> field = (Field) parseField(S);
            parse(',');
            Field<Integer> count = (Field) parseField(N);
            parse(')');
            return DSL.repeat(field, count);
        }

        return null;
    }

    private final Field<?> parseFieldReplaceIf() {
        if (parseFunctionNameIf("REPLACE") ||
            parseFunctionNameIf("OREPLACE") ||
            parseFunctionNameIf("STR_REPLACE")) {

            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parse(',');
            Field<String> f2 = (Field) parseField(S);
            Field<String> f3 = parseIf(',')
                ? (Field) parseField(S)
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
            Field field = parseField(S);
            parse(',');
            Field pattern = parseField(S);
            Field replacement = parseIf(',') ? parseField(S) : null;
            Long i1;
            Long i2;

            if (replacement == null) {
                replacement = inline("");
            }
            else if (ifx) {
                if (parseIf(','))
                    if (1L == parseUnsignedInteger())
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
                        i1 = parseUnsignedInteger();
                        parse(',');
                        i2 = parseUnsignedInteger();

                        if (Long.valueOf(1L).equals(i1) && Long.valueOf(1L).equals(i2))
                            all = true;
                        else
                            throw expected("Only start and occurence values of 1 are currently supported");
                    }
                }

                if (!all) switch (family()) {





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

        return null;
    }

    private final Field<?> parseFieldSubstringIf() {
        boolean substring = parseFunctionNameIf("SUBSTRING");
        boolean substr = !substring && parseFunctionNameIf("SUBSTR");

        if (substring || substr) {
            boolean keywords = !substr;
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            if (substr || !(keywords = parseKeywordIf("FROM")))
                parse(',');
            Field f2 = toField(parseNumericOp(N));
            Field f3 =
                    ((keywords && parseKeywordIf("FOR")) || (!keywords && parseIf(',')))
                ? (Field) toField(parseNumericOp(N))
                : null;
            parse(')');

            return f3 == null
                ? DSL.substring(f1, f2)
                : DSL.substring(f1, f2, f3);
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
                    Field<String> f = (Field) parseField(S);
                    parse(')');

                    return leading ? ltrim(f)
                         : trailing ? rtrim(f)
                         : trim(f);
                }
            }

            Field<String> f1 = (Field) parseField(S);

            if (parseKeywordIf("FROM")) {
                Field<String> f2 = (Field) parseField(S);
                parse(')');

                return leading ? ltrim(f2, f1)
                     : trailing ? rtrim(f2, f1)
                     : trim(f2, f1);
            }
            else {
                Field<String> f2 = parseIf(',') ? (Field) parseField(S) : null;
                parse(')');

                return f2 == null ? trim(f1) : trim(f1, f2);
            }
        }

        return null;
    }

    private final Field<?> parseFieldTranslateIf() {
        if (parseFunctionNameIf("TRANSLATE")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parse(',');
            Field<String> f2 = (Field) parseField(S);
            parse(',');
            Field<String> f3 = (Field) parseField(S);
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
            Field<String> f1 = (Field) parseField(S);
            parse(')');
            return cast(f1, SQLDataType.NUMERIC);
        }

        return null;
    }

    private final Field<?> parseFieldToDateIf() {
        if (parseFunctionNameIf("TO_DATE")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parse(',');
            Field<String> f2 = (Field) parseField(S);
            parse(')');

            return toDate(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldToTimestampIf() {
        if (parseFunctionNameIf("TO_TIMESTAMP")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parse(',');
            Field<String> f2 = (Field) parseField(S);
            parse(')');

            return toTimestamp(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldTimestampDiffIf() {
        if (parseFunctionNameIf("TIMESTAMPDIFF")) {
            parse('(');
            Field<Timestamp> ts1 = (Field<Timestamp>) parseField(Type.D);
            parse(',');
            Field<Timestamp> ts2 = (Field<Timestamp>) parseField(Type.D);
            parse(')');

            return DSL.timestampDiff(ts1, ts2);
        }

        return null;
    }

    private final Field<?> parseFieldRtrimIf() {
        if (parseFunctionNameIf("RTRIM")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            Field<String> f2 = parseIf(',') ? (Field) parseField(S) : null;
            parse(')');

            return f2 == null ? rtrim(f1) : rtrim(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldLtrimIf() {
        if (parseFunctionNameIf("LTRIM")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            Field<String> f2 = parseIf(',') ? (Field) parseField(S) : null;
            parse(')');

            return f2 == null ? ltrim(f1) : ltrim(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldMidIf() {
        if (parseFunctionNameIf("MID")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parse(',');
            Field<? extends Number> f2 = (Field) parseField(N);
            parse(',');
            Field<? extends Number> f3 = (Field) parseField(N);
            parse(')');
            return mid(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldLeftIf() {
        if (parseFunctionNameIf("LEFT")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parse(',');
            Field<? extends Number> f2 = (Field) parseField(N);
            parse(')');
            return left(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldRightIf() {
        if (parseFunctionNameIf("RIGHT")) {
            parse('(');
            Field<String> f1 = (Field) parseField(S);
            parse(',');
            Field<? extends Number> f2 = (Field) parseField(N);
            parse(')');
            return right(f1, f2);
        }

        return null;
    }

    private final Field<?> parseFieldDecodeIf() {
        if (parseFunctionNameIf("DECODE")) {
            parse('(');
            List<Field<?>> fields = parseFields();
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
            Field<Integer> index = (Field<Integer>) parseField(Type.N);
            parse(',');
            List<Field<?>> fields = parseFields();
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
        if (parseFunctionNameIf("IF") || parseFunctionNameIf("IIF")) {
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
            List<Field<?>> fields = parseFields();
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
            List<Field<?>> f2 = parseFields();
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
                style = parseUnsignedInteger();
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
        AggregateFunction<?> agg = null;
        AggregateFilterStep<?> filter = null;
        WindowBeforeOverStep<?> over = null;
        Object keep = null;
        Field<?> result = null;
        Condition condition = null;

        keep = over = filter = agg = parseCountIf();
        if (filter == null)
            keep = over = filter = agg = parseGeneralSetFunctionIf();
        if (filter == null && !basic)
            over = filter = agg = parseBinarySetFunctionIf();
        if (filter == null && !basic)
            over = filter = parseOrderedSetFunctionIf();
        if (filter == null && !basic)
            over = filter = parseArrayAggFunctionIf();
        if (filter == null && !basic)
            over = filter = parseXMLAggFunctionIf();

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
                s2 = s1.orderBy(parseSortSpecification());
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
            List<Field<?>> args = parseFields();
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
            List<Field<?>> args = parseFields();
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
            List<Field<?>> args = parseFields();
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
            List<Field<?>> args = parseFields();
            parse(')');
            return cumeDist(args).withinGroupOrderBy(parseWithinGroupN());
        }

        return null;
    }

    private final Field<?> parseFieldRandIf() {
        if (parseFunctionNameIf("RAND") || parseFunctionNameIf("RANDOM")) {
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
            int number = (int) (long) parseUnsignedInteger();
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
                f2 = (int) (long) parseUnsignedInteger();

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
            int f2 = (int) (long) parseUnsignedInteger();
            WindowFromFirstLastStep<?> s1 = nthValue(f1, f2);
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
        Field<? extends Number> arg1;
        Field<? extends Number> arg2;
        BinarySetFunctionType type = parseBinarySetFunctionTypeIf();

        if (type == null)
            return null;

        parse('(');
        arg1 = (Field) toField(parseNumericOp(N));
        parse(',');
        arg2 = (Field) toField(parseNumericOp(N));
        parse(')');

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
                throw exception("Binary set function not supported: " + type);
        }
    }

    private final AggregateFilterStep<?> parseOrderedSetFunctionIf() {
        // TODO Listagg set function
        OrderedAggregateFunction<?> orderedN;
        OrderedAggregateFunctionOfDeferredType ordered1;

        orderedN = parseHypotheticalSetFunctionIf();
        if (orderedN == null)
            orderedN = parseInverseDistributionFunctionIf();
        if (orderedN == null)
            orderedN = parseListaggFunctionIf();
        if (orderedN != null)
            return orderedN.withinGroupOrderBy(parseWithinGroupN());

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
                sort = parseSortSpecification();

            parse(')');

            ArrayAggOrderByStep<?> s1 = distinct
                ? arrayAggDistinct(a1)
                : arrayAgg(a1);

            return sort == null ? s1 : s1.orderBy(sort);
        }

        return null;
    }

    private final List<SortField<?>> parseWithinGroupN() {
        parseKeyword("WITHIN GROUP");
        parse('(');
        parseKeyword("ORDER BY");
        List<SortField<?>> result = parseSortSpecification();
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
            ordered = rank(parseFields());
            parse(')');
        }
        else if (parseFunctionNameIf("DENSE_RANK")) {
            parse('(');
            ordered = denseRank(parseFields());
            parse(')');
        }
        else if (parseFunctionNameIf("PERCENT_RANK")) {
            parse('(');
            ordered = percentRank(parseFields());
            parse(')');
        }
        else if (parseFunctionNameIf("CUME_DIST")) {
            parse('(');
            ordered = cumeDist(parseFields());
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
            ordered = percentileCont(parseFieldUnsignedNumericLiteral(Sign.NONE));
            parse(')');
        }
        else if (parseFunctionNameIf("PERCENTILE_DISC")) {
            parse('(');
            ordered = percentileDisc(parseFieldUnsignedNumericLiteral(Sign.NONE));
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

    private final AggregateFunction<?> parseGeneralSetFunctionIf() {
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
        parse(')');

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
                        ? parseFields().toArray(EMPTY_FIELD)
                        : new Field[] { parseField() };

            parse(')');

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
        return lookupTable(parseName());
    }

    private final Table<?> parseTableNameIf() {
        Name name = parseNameIf();

        if (name == null)
            return null;

        return lookupTable(name);
    }

    private final Field<?> parseFieldNameOrSequenceExpression() {
        Name name = parseName();

        if (name.qualified()) {
            String last = name.last();

            if ("NEXTVAL".equalsIgnoreCase(last))
                return sequence(name.qualifier()).nextval();
            else if ("CURRVAL".equalsIgnoreCase(last))
                return sequence(name.qualifier()).currval();
        }

        if (dsl.settings().getParseUnknownFunctions() == ParseUnknownFunctions.IGNORE && peek('(') && !peekTokens('(', '+', ')')) {
            List<Field<?>> arguments = new ArrayList<>();

            parse('(');
            if (!parseIf(')')) {
                do {
                    arguments.add(parseField());
                }
                while (parseIf(','));

                parse(')');
            }

            return function(name, Object.class, arguments.toArray(EMPTY_FIELD));
        }
        else {









            return lookupField(name);
        }
    }

    private final TableField<?, ?> parseFieldName() {
        return (TableField<?, ?>) lookupField(parseName());
    }

    private final List<Field<?>> parseFieldNames() {
        List<Field<?>> result = new ArrayList<>();

        do {
            result.add(parseFieldName());
        }
        while (parseIf(','));

        return result;
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

    private final Name parseName() {
        Name result = parseNameIf();

        if (result == null)
            throw expected("Identifier");

        return result;
    }

    private final Name parseNameIf() {
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
        int p = position();
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
                    return lookupTable(result == null ? i1 : DSL.name(result.toArray(EMPTY_NAME))).asterisk();
                }
            }
            while (parseIf('.'));
        }

        position(p);
        return null;
    }

    private final List<Name> parseIdentifiers() {
        LinkedHashSet<Name> result = new LinkedHashSet<>();

        do {
            if (!result.add(parseIdentifier()))
                throw exception("Duplicate identifier encountered");
        }
        while (parseIf(','));
        return new ArrayList<>(result);
    }

    private final Name parseIdentifier() {
        return parseIdentifier(false);
    }

    private final Name parseIdentifier(boolean allowAposQuotes) {
        Name result = parseIdentifierIf(allowAposQuotes);

        if (result == null)
            throw expected("Identifier");

        return result;
    }

    private final Name parseIdentifierIf() {
        return parseIdentifierIf(false);
    }

    private final Name parseIdentifierIf(boolean allowAposQuotes) {
        char quoteEnd = parseQuote(allowAposQuotes);
        boolean quoted = quoteEnd != 0;

        int start = position();
        if (quoted)
            while (character() != quoteEnd && hasMore())
                positionInc();
        else
            while (isIdentifierPart() && hasMore())
                positionInc();

        if (position() == start)
            return null;

        String name = normaliseNameCase(configuration(), substring(start, position()), quoted, locale);

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

    private final DataType<?> parseDataType() {
        DataType<?> result = parseDataTypePrefix();
        boolean array = false;

        if (parseKeywordIf("ARRAY"))
            array = true;

        if (parseIf('[')) {
            parseUnsignedIntegerIf();
            parse(']');

            array = true;
        }

        if (array)
            result = result.getArrayDataType();

        return result;
    }

    private final DataType<?> parseDataTypePrefix() {
        char character = characterUpper();

        if (character == '[' || character == '"' || character == '`')
            character = characterNextUpper();

        switch (character) {
            case 'A':
                if (parseKeywordOrIdentifierIf("ARRAY"))
                    return SQLDataType.OTHER.getArrayDataType();

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
                    else

                    if (parseKeywordOrIdentifierIf("WITHOUT TIME ZONE") || true)
                        return precision == null ? SQLDataType.TIMESTAMP : SQLDataType.TIMESTAMP(precision);
                }

                else if (parseKeywordOrIdentifierIf("TIMETZ"))
                    return parseDataTypePrecisionIf(SQLDataType.TIMEWITHTIMEZONE);

                else if (parseKeywordOrIdentifierIf("TIME")) {
                    Integer precision = parseDataTypePrecisionIf();


                    if (parseKeywordOrIdentifierIf("WITH TIME ZONE"))
                        return precision == null ? SQLDataType.TIMEWITHTIMEZONE : SQLDataType.TIMEWITHTIMEZONE(precision);
                    else

                    if (parseKeywordOrIdentifierIf("WITHOUT TIME ZONE") || true)
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

        return new DefaultDataType(dsl.dialect(), Object.class, parseName());
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
            parseUnsignedInteger();
            parse(')');
        }

        return result;
    }

    private final DataType<?> parseDataTypeLength(DataType<?> in) {
        DataType<?> result = in;

        if (parseIf('(')) {
            if (!parseKeywordIf("MAX"))
                result = result.length((int) (long) parseUnsignedInteger());

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
            parseUnsignedInteger();

            if (parseIf(','))
                parseUnsignedInteger();

            parse(')');
        }

        return result;
    }

    private final Integer parseDataTypePrecisionIf() {
        Integer precision = null;

        if (parseIf('(')) {
            precision = (int) (long) parseUnsignedInteger();
            parse(')');
        }

        return precision;
    }

    private final DataType<?> parseDataTypePrecisionIf(DataType<?> result) {
        if (parseIf('(')) {
            int precision = (int) (long) parseUnsignedInteger();
            result = result.precision(precision);
            parse(')');
        }

        return result;
    }

    private final DataType<?> parseDataTypePrecisionScaleIf(DataType<?> result) {
        if (parseIf('(')) {
            int precision = parseIf('*') ? 38 : (int) (long) parseUnsignedInteger();

            if (parseIf(','))
                result = result.precision(precision, (int) (long) parseSignedInteger());
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

    private final Field<?> parseBindVariable() {

        // [#11074] Bindings can be Param or even Field types
        Object binding = nextBinding();
        String paramName;

        switch (character()) {
            case '?':
                parse('?');
                paramName = "" + bindIndex;
                break;

            case ':':
                parse(':', false);
                Name identifier = parseIdentifier();
                paramName = identifier.last();
                break;

            default:
                throw exception("Illegal bind variable character");
        }

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

    private final String parseStringLiteral() {
        String result = parseStringLiteralIf();

        if (result == null)
            throw expected("String literal");

        return result;
    }

    private final String parseStringLiteralIf() {
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

        if (!parseIf('$'))
            return null;

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
        char c;

        for (;;) {
            c = character();
            if (c >= '0' && c <= '9') {
                positionInc();
            }
            else
                break;
        }

        if (c == '.') {
            positionInc();
        }
        else {
            if (p == position())
                return null;

            String s = substring(p, position());
            parseWhitespaceIf();
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
            c = character();
            if (c >= '0' && c <= '9') {
                positionInc();
            }
            else
                break;
        }

        if (p == position())
            return null;

        String s = substring(p, position());
        parseWhitespaceIf();
        return sign == Sign.MINUS
            ? new BigDecimal(s).negate()
            : new BigDecimal(s);
        // TODO add floating point support
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

    private final Long parseSignedInteger() {
        Long result = parseSignedIntegerIf();

        if (result == null)
            throw expected("Signed integer");

        return result;
    }

    private final Long parseSignedIntegerIf() {
        Sign sign = parseSign();
        Long unsigned;

        if (sign == Sign.MINUS)
            unsigned = parseUnsignedInteger();
        else
            unsigned = parseUnsignedIntegerIf();

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

    private final Field<Long> parseParenthesisedUnsignedIntegerOrBindVariable() {
        Field<Long> result;

        int parens;
        for (parens = 0; parseIf('('); parens++);
        result = parseUnsignedIntegerOrBindVariable();
        for (; parens > 0 && parse(')'); parens--);

        return result;
    }

    private final Field<Long> parseUnsignedIntegerOrBindVariable() {
        Long i = parseUnsignedIntegerIf();
        return i != null ? DSL.inline(i) : (Field<Long>) parseBindVariable();
    }

    private final Long parseUnsignedInteger() {
        Long result = parseUnsignedIntegerIf();

        if (result == null)
            throw expected("Unsigned integer");

        return result;
    }

    private final Long parseUnsignedIntegerIf() {
        int p = position();

        for (;;) {
            char c = character();

            if (c >= '0' && c <= '9')
                positionInc();
            else
                break;
        }

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
        if (parseFunctionNameIf("AVG"))
            return ComputationalOperation.AVG;
        else if (parseFunctionNameIf("MAX"))
            return ComputationalOperation.MAX;
        else if (parseFunctionNameIf("MIN"))
            return ComputationalOperation.MIN;
        else if (parseFunctionNameIf("SUM"))
            return ComputationalOperation.SUM;
        else if (parseFunctionNameIf("PRODUCT"))
            return ComputationalOperation.PRODUCT;
        else if (parseFunctionNameIf("MEDIAN"))
            return ComputationalOperation.MEDIAN;
        else if (parseFunctionNameIf("EVERY") || parseFunctionNameIf("BOOL_AND"))
            return ComputationalOperation.EVERY;
        else if (parseFunctionNameIf("ANY") || parseFunctionNameIf("SOME") || parseFunctionNameIf("BOOL_OR"))
            return ComputationalOperation.ANY;
        else if (parseFunctionNameIf("STDDEV_POP") || parseFunctionNameIf("STDEVP"))
            return ComputationalOperation.STDDEV_POP;
        else if (parseFunctionNameIf("STDDEV_SAMP") || parseFunctionNameIf("STDEV"))
            return ComputationalOperation.STDDEV_SAMP;
        else if (parseFunctionNameIf("VAR_POP"))
            return ComputationalOperation.VAR_POP;
        else if (parseFunctionNameIf("VAR_SAMP"))
            return ComputationalOperation.VAR_SAMP;

        return null;
    }

    private final BinarySetFunctionType parseBinarySetFunctionTypeIf() {

        // TODO speed this up
        for (BinarySetFunctionType type : BinarySetFunctionType.values())
            if (parseFunctionNameIf(type.name()))
                return type;

        return null;
    }

    private final Comparator parseComparatorIf() {
        if (parseIf("="))
            return Comparator.EQUALS;
        else if (parseIf("!=") || parseIf("<>") || parseIf("^="))
            return Comparator.NOT_EQUALS;
        else if (parseIf(">="))
            return Comparator.GREATER_OR_EQUAL;
        else if (parseIf(">"))
            return Comparator.GREATER;

        // MySQL DISTINCT operator
        else if (parseIf("<=>"))
            return Comparator.IS_NOT_DISTINCT_FROM;
        else if (parseIf("<="))
            return Comparator.LESS_OR_EQUAL;
        else if (parseIf("<"))
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

    private final boolean parse(String string) {
        boolean result = parseIf(string);

        if (!result)
            throw expected(string);

        return result;
    }

    private final boolean parseIf(String string) {
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

    private final boolean parse(char c) {
        return parse(c, true);
    }

    private final boolean parse(char c, boolean skipAfterWhitespace) {
        if (!parseIf(c, skipAfterWhitespace))
            throw expected("Token '" + c + "'");

        return true;
    }

    private final boolean parseIf(char c) {
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

    private final boolean parseFunctionNameIf(String name) {
        return peekKeyword(name, true, false, true);
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

    private final boolean parseKeyword(String keyword) {
        if (!parseKeywordIf(keyword))
            throw expected("Keyword '" + keyword + "'");

        return true;
    }

    private final boolean parseKeywordIf(String keyword) {
        return peekKeyword(keyword, true, false, false);
    }

    private final boolean parseKeywordIf(String... keywords) {
        for (String keyword : keywords)
            if (parseKeywordIf(keyword))
                return true;

        return false;
    }

    private final boolean parseKeyword(String... keywords) {
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
        for (String keyword : keywords)
            if (parseKeywordIf(keyword))
                return keyword(keyword.toLowerCase());

        return null;
    }

    private final Keyword parseAndGetKeywordIf(String keyword) {
        if (parseKeywordIf(keyword))
            return keyword(keyword.toLowerCase());

        return null;
    }

    private final boolean peek(char c) {
        if (character() != c)
            return false;

        return true;
    }

    private final boolean peek(String string) {
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

    private final boolean peekKeyword(String... keywords) {
        for (String keyword : keywords)
            if (peekKeyword(keyword))
                return true;

        return false;
    }

    private final boolean peekKeyword(String keyword) {
        return peekKeyword(keyword, false, false, false);
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
                    if (i + 1 < sql.length && sql[i + 1] == '-') {
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

    private static final String[] KEYWORDS_IN_SELECT     = {
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

    private static final String[] KEYWORDS_IN_FROM       = {
        "CONNECT BY",
        "CREATE",
        "CROSS APPLY",
        "CROSS JOIN",
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
        "NATURAL FULL JOIN",
        "NATURAL FULL OUTER JOIN",
        "NATURAL INNER JOIN",
        "NATURAL JOIN",
        "NATURAL LEFT JOIN",
        "NATURAL LEFT OUTER JOIN",
        "NATURAL RIGHT JOIN",
        "NATURAL RIGHT OUTER JOIN",
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
        "ROWS",
        "START WITH",
        "STRAIGHT_JOIN",
        "UNION",
        "USING",
        "WHERE",
        "WINDOW",
    };

    private static final String[] PIVOT_KEYWORDS      = {
        "FOR"
    };

    private static final DDLQuery                 IGNORE                 = Reflect.on(DSL.query("/* ignored */")).as(DDLQuery.class, QueryPartInternal.class);
    private static final Query                    IGNORE_NO_DELIMITER    = Reflect.on(DSL.query("/* ignored */")).as(Query.class, QueryPartInternal.class);
    private static final boolean                  PRO_EDITION            = false ;

    private final DSLContext                      dsl;
    private final Locale                          locale;
    private final Meta                            meta;
    private final char[]                          sql;
    private final ParseWithMetaLookups            metaLookups;
    private boolean                               metaLookupsForceIgnore;
    private final F.A1<Param<?>>                  bindParamListener;
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
        this.sql = sqlString != null ? sqlString.toCharArray() : new char[0];
        this.bindings = bindings;

        // [#8722] This is an undocumented flag that allows for collecting parameters from the parser
        //         Do not rely on this flag. It will change incompatibly in the future.
        this.bindParamListener = (F.A1<Param<?>>) dsl.configuration().data("org.jooq.parser.param-collector");
        parseWhitespaceIf();
    }

    private final Configuration configuration() {
        return dsl.configuration();
    }

    private final Settings settings() {
        return configuration().settings();
    }

    private final SQLDialect dialect() {
        SQLDialect result = settings().getParseDialect();

        if (result == null)
            result = SQLDialect.DEFAULT;

        return result;
    }

    private final SQLDialect family() {
        return dialect().family();
    }

    private final boolean metaLookupsForceIgnore() {
        return this.metaLookupsForceIgnore;
    }

    private final ParserContext metaLookupsForceIgnore(boolean m) {
        this.metaLookupsForceIgnore = m;
        return this;
    }

    private final boolean requireProEdition() {
        if (!PRO_EDITION)
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

    private final ParserException exception(String message) {
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

    private final char character() {
        return character(position);
    }

    private final char characterUpper(int pos) {
        return Character.toUpperCase(character(pos));
    }

    private final char character(int pos) {
        return pos >= 0 && pos < sql.length ? sql[pos] : ' ';
    }

    private final char characterNextUpper() {
        return Character.toUpperCase(characterNext());
    }

    private final char characterNext() {
        return character(position + 1);
    }

    private final int position() {
        return position;
    }

    private final void position(int newPosition) {
        position = newPosition;
    }

    private final void positionInc() {
        positionInc(1);
    }

    private final void positionInc(int inc) {
        position(position + inc);
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

    private final boolean isWhitespace() {
        return Character.isWhitespace(character());
    }

    private final boolean isWhitespace(int pos) {
        return Character.isWhitespace(character(pos));
    }

    private final boolean isOperatorPart() {
        return isOperatorPart(character());
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
            dsl.configuration().derive(new DefaultVisitListener() {
                @Override
                public void visitStart(VisitContext context) {
                    if (context.queryPart() instanceof Param) {
                        Param<?> p = (Param<?>) context.queryPart();

                        if (!params.containsKey(p.getParamName()))
                            params.put(p.getParamName(), p);
                    }
                }
            }).dsl().render(result);

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

    private final void scopeEnd() {
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
            if (found != null)
                lookup.delegate((AbstractField) found.value);
            else
                retain.add(lookup);
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

            if (t.value instanceof JoinTable) {
                found = resolveInTableScope(
                    Arrays.<Value<Table<?>>>asList(
                        new Value<>(t.scopeLevel, ((JoinTable) t.value).lhs),
                        new Value<>(t.scopeLevel, ((JoinTable) t.value).rhs)
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
                if (x && q.equals(t.value.getQualifiedName()) || !x && q.last().equals(t.value.getName()))
                    if ((found = Value.<Field<?>>of(t.scopeLevel, t.value.field(lookup.getName()))) != null)
                        break tableScopeLoop;
            }
            else if ((f = Value.<Field<?>>of(t.scopeLevel, t.value.field(lookup.getName()))) != null) {
                if (found == null || found.scopeLevel < f.scopeLevel) {
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
        if (!scopeClear && !metaLookupsForceIgnore && metaLookups == THROW_ON_FAILURE) {
            position(field.position());
            throw exception("Unknown field identifier");
        }
    }

    private final Table<?> lookupTable(Name name) {
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

    private final Field<?> lookupField(Name name) {
        if (metaLookups == ParseWithMetaLookups.OFF)
            return field(name);

        FieldProxy<?> field = lookupFields.get(name);
        if (field == null)
            lookupFields.set(name, field = new FieldProxy<>((AbstractField<Object>) field(name), position));

        return field;
    }

    @Override
    public String toString() {
        return mark();
    }
}