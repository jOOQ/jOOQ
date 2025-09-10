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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.NOT_IN;
import static org.jooq.DatePart.DAY;
import static org.jooq.DatePart.HOUR;
import static org.jooq.DatePart.MINUTE;
import static org.jooq.DatePart.MONTH;
import static org.jooq.DatePart.SECOND;
import static org.jooq.JSON.json;
import static org.jooq.JSONB.jsonb;
import static org.jooq.JoinType.JOIN;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.VisitListener.onVisitStart;
import static org.jooq.conf.ParseWithMetaLookups.IGNORE_ON_FAILURE;
import static org.jooq.conf.ParseWithMetaLookups.THROW_ON_FAILURE;
import static org.jooq.impl.AbstractName.NO_NAME;
import static org.jooq.impl.Convert.convert;
import static org.jooq.impl.DSL.abs;
import static org.jooq.impl.DSL.acos;
import static org.jooq.impl.DSL.acosh;
import static org.jooq.impl.DSL.acoth;
import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.and;
import static org.jooq.impl.DSL.any;
import static org.jooq.impl.DSL.anyValue;
import static org.jooq.impl.DSL.arrayAgg;
import static org.jooq.impl.DSL.arrayAggDistinct;
import static org.jooq.impl.DSL.arrayAppend;
import static org.jooq.impl.DSL.arrayConcat;
import static org.jooq.impl.DSL.arrayContains;
import static org.jooq.impl.DSL.arrayGet;
import static org.jooq.impl.DSL.arrayOverlap;
import static org.jooq.impl.DSL.arrayPrepend;
import static org.jooq.impl.DSL.arrayRemove;
import static org.jooq.impl.DSL.arrayReplace;
import static org.jooq.impl.DSL.ascii;
import static org.jooq.impl.DSL.asin;
import static org.jooq.impl.DSL.asinh;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.atan;
import static org.jooq.impl.DSL.atanh;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.avgDistinct;
import static org.jooq.impl.DSL.begin;
import static org.jooq.impl.DSL.binaryBitLength;
import static org.jooq.impl.DSL.binaryLength;
import static org.jooq.impl.DSL.binaryListAgg;
import static org.jooq.impl.DSL.binaryListAggDistinct;
import static org.jooq.impl.DSL.binaryLtrim;
import static org.jooq.impl.DSL.binaryMd5;
import static org.jooq.impl.DSL.binaryOctetLength;
import static org.jooq.impl.DSL.binaryOverlay;
import static org.jooq.impl.DSL.binaryRtrim;
import static org.jooq.impl.DSL.binaryTrim;
import static org.jooq.impl.DSL.bitAnd;
import static org.jooq.impl.DSL.bitAndAgg;
import static org.jooq.impl.DSL.bitCount;
import static org.jooq.impl.DSL.bitLength;
import static org.jooq.impl.DSL.bitNand;
import static org.jooq.impl.DSL.bitNandAgg;
import static org.jooq.impl.DSL.bitNor;
import static org.jooq.impl.DSL.bitNorAgg;
import static org.jooq.impl.DSL.bitNot;
import static org.jooq.impl.DSL.bitOr;
import static org.jooq.impl.DSL.bitOrAgg;
import static org.jooq.impl.DSL.bitXNor;
import static org.jooq.impl.DSL.bitXNorAgg;
import static org.jooq.impl.DSL.bitXor;
import static org.jooq.impl.DSL.bitXorAgg;
import static org.jooq.impl.DSL.boolOr;
// ...
import static org.jooq.impl.DSL.cardinality;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.catalog;
import static org.jooq.impl.DSL.ceil;
import static org.jooq.impl.DSL.century;
import static org.jooq.impl.DSL.charLength;
import static org.jooq.impl.DSL.characterSet;
import static org.jooq.impl.DSL.check;
import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.chr;
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
import static org.jooq.impl.DSL.dateAdd;
import static org.jooq.impl.DSL.day;
import static org.jooq.impl.DSL.dayOfWeek;
import static org.jooq.impl.DSL.dayOfYear;
import static org.jooq.impl.DSL.decade;
// ...
import static org.jooq.impl.DSL.defaultValue;
import static org.jooq.impl.DSL.default_;
import static org.jooq.impl.DSL.deg;
import static org.jooq.impl.DSL.denseRank;
import static org.jooq.impl.DSL.digits;
import static org.jooq.impl.DSL.domain;
import static org.jooq.impl.DSL.dual;
import static org.jooq.impl.DSL.epoch;
import static org.jooq.impl.DSL.every;
import static org.jooq.impl.DSL.excluded;
// ...
import static org.jooq.impl.DSL.exists;
// ...
// ...
import static org.jooq.impl.DSL.exp;
import static org.jooq.impl.DSL.extract;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.finalTable;
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
import static org.jooq.impl.DSL.in;
import static org.jooq.impl.DSL.inOut;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.isnull;
import static org.jooq.impl.DSL.isoDayOfWeek;
import static org.jooq.impl.DSL.jsonArray;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonArrayAggDistinct;
import static org.jooq.impl.DSL.jsonExists;
import static org.jooq.impl.DSL.jsonGetAttribute;
import static org.jooq.impl.DSL.jsonGetAttributeAsText;
import static org.jooq.impl.DSL.jsonGetElement;
import static org.jooq.impl.DSL.jsonGetElementAsText;
import static org.jooq.impl.DSL.jsonKeyExists;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonObjectAgg;
import static org.jooq.impl.DSL.jsonTable;
import static org.jooq.impl.DSL.jsonValue;
import static org.jooq.impl.DSL.jsonbArray;
import static org.jooq.impl.DSL.jsonbArrayAgg;
import static org.jooq.impl.DSL.jsonbArrayAggDistinct;
import static org.jooq.impl.DSL.jsonbGetAttribute;
import static org.jooq.impl.DSL.jsonbGetAttributeAsText;
import static org.jooq.impl.DSL.jsonbGetElement;
import static org.jooq.impl.DSL.jsonbGetElementAsText;
import static org.jooq.impl.DSL.jsonbKeyExists;
import static org.jooq.impl.DSL.jsonbObject;
import static org.jooq.impl.DSL.jsonbObjectAgg;
import static org.jooq.impl.DSL.key;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.lag;
import static org.jooq.impl.DSL.lambda;
import static org.jooq.impl.DSL.lastValue;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.lead;
import static org.jooq.impl.DSL.least;
import static org.jooq.impl.DSL.length;
// ...
import static org.jooq.impl.DSL.list;
import static org.jooq.impl.DSL.listAgg;
import static org.jooq.impl.DSL.listAggDistinct;
import static org.jooq.impl.DSL.ln;
import static org.jooq.impl.DSL.log;
import static org.jooq.impl.DSL.log10;
// ...
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.ltrim;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.maxBy;
import static org.jooq.impl.DSL.maxDistinct;
import static org.jooq.impl.DSL.md5;
import static org.jooq.impl.DSL.median;
import static org.jooq.impl.DSL.microsecond;
import static org.jooq.impl.DSL.millennium;
import static org.jooq.impl.DSL.millisecond;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.minBy;
import static org.jooq.impl.DSL.minDistinct;
import static org.jooq.impl.DSL.minute;
import static org.jooq.impl.DSL.mode;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.multisetAgg;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.newTable;
import static org.jooq.impl.DSL.now;
import static org.jooq.impl.DSL.nthValue;
import static org.jooq.impl.DSL.ntile;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.nvl2;
import static org.jooq.impl.DSL.octetLength;
import static org.jooq.impl.DSL.oldTable;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.or;
import static org.jooq.impl.DSL.orderBy;
import static org.jooq.impl.DSL.out;
import static org.jooq.impl.DSL.overlay;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.percentRank;
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
import static org.jooq.impl.DSL.raw;
import static org.jooq.impl.DSL.regexpReplaceAll;
import static org.jooq.impl.DSL.regexpReplaceFirst;
// ...
// ...
import static org.jooq.impl.DSL.reverse;
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
import static org.jooq.impl.DSL.rtrim;
import static org.jooq.impl.DSL.schema;
import static org.jooq.impl.DSL.second;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sequence;
import static org.jooq.impl.DSL.shl;
import static org.jooq.impl.DSL.shr;
import static org.jooq.impl.DSL.sign;
// ...
import static org.jooq.impl.DSL.sin;
import static org.jooq.impl.DSL.sinh;
import static org.jooq.impl.DSL.space;
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.DSL.sqrt;
import static org.jooq.impl.DSL.square;
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.stddevPop;
import static org.jooq.impl.DSL.stddevSamp;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.sumDistinct;
// ...
// ...
import static org.jooq.impl.DSL.systemName;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.tan;
import static org.jooq.impl.DSL.tanh;
import static org.jooq.impl.DSL.time;
import static org.jooq.impl.DSL.timestamp;
import static org.jooq.impl.DSL.timezone;
import static org.jooq.impl.DSL.timezoneHour;
import static org.jooq.impl.DSL.timezoneMinute;
import static org.jooq.impl.DSL.toDate;
import static org.jooq.impl.DSL.toHex;
import static org.jooq.impl.DSL.toTimestamp;
import static org.jooq.impl.DSL.translate;
import static org.jooq.impl.DSL.trim;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DSL.trunc;
import static org.jooq.impl.DSL.tryCast;
import static org.jooq.impl.DSL.unique;
import static org.jooq.impl.DSL.unnest;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.DSL.user;
import static org.jooq.impl.DSL.uuid;
import static org.jooq.impl.DSL.values0;
// ...
import static org.jooq.impl.DSL.varPop;
import static org.jooq.impl.DSL.varSamp;
import static org.jooq.impl.DSL.week;
import static org.jooq.impl.DSL.when;
// ...
import static org.jooq.impl.DSL.widthBucket;
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
import static org.jooq.impl.DSL.xmlserializeContent;
import static org.jooq.impl.DSL.xmlserializeDocument;
import static org.jooq.impl.DSL.xmltable;
import static org.jooq.impl.DSL.xor;
import static org.jooq.impl.DSL.year;
import static org.jooq.impl.DSL.zero;
import static org.jooq.impl.DefaultParseContext.FunctionKeyword.FK_AND;
import static org.jooq.impl.DefaultParseContext.FunctionKeyword.FK_IN;
import static org.jooq.impl.Internal.iadd;
import static org.jooq.impl.Internal.isub;
import static org.jooq.impl.Keywords.K_DELETE;
import static org.jooq.impl.Keywords.K_INSERT;
import static org.jooq.impl.Keywords.K_SELECT;
import static org.jooq.impl.Keywords.K_UPDATE;
import static org.jooq.impl.QOM.JSONOnNull.ABSENT_ON_NULL;
import static org.jooq.impl.QOM.JSONOnNull.NULL_ON_NULL;
// ...
// ...
// ...
// ...
import static org.jooq.impl.QOM.XMLPassingMechanism.BY_REF;
import static org.jooq.impl.QOM.XMLPassingMechanism.BY_VALUE;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.BINARY;
import static org.jooq.impl.SQLDataType.BIT;
import static org.jooq.impl.SQLDataType.BLOB;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.CHAR;
import static org.jooq.impl.SQLDataType.CLOB;
import static org.jooq.impl.SQLDataType.DATE;
import static org.jooq.impl.SQLDataType.DECFLOAT;
import static org.jooq.impl.SQLDataType.DECIMAL;
import static org.jooq.impl.SQLDataType.DOUBLE;
import static org.jooq.impl.SQLDataType.FLOAT;
import static org.jooq.impl.SQLDataType.GEOGRAPHY;
import static org.jooq.impl.SQLDataType.GEOMETRY;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.INTERVAL;
import static org.jooq.impl.SQLDataType.INTERVALDAYTOSECOND;
import static org.jooq.impl.SQLDataType.INTERVALYEARTOMONTH;
import static org.jooq.impl.SQLDataType.JSON;
import static org.jooq.impl.SQLDataType.JSONB;
import static org.jooq.impl.SQLDataType.LONGNVARCHAR;
import static org.jooq.impl.SQLDataType.LONGVARBINARY;
import static org.jooq.impl.SQLDataType.LONGVARCHAR;
import static org.jooq.impl.SQLDataType.NCHAR;
import static org.jooq.impl.SQLDataType.NCLOB;
import static org.jooq.impl.SQLDataType.NUMERIC;
import static org.jooq.impl.SQLDataType.NVARCHAR;
import static org.jooq.impl.SQLDataType.OTHER;
import static org.jooq.impl.SQLDataType.REAL;
import static org.jooq.impl.SQLDataType.SMALLINT;
import static org.jooq.impl.SQLDataType.TIME;
import static org.jooq.impl.SQLDataType.TIMESTAMP;
import static org.jooq.impl.SQLDataType.TIMESTAMPWITHTIMEZONE;
import static org.jooq.impl.SQLDataType.TIMEWITHTIMEZONE;
import static org.jooq.impl.SQLDataType.TINYINT;
import static org.jooq.impl.SQLDataType.VARBINARY;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.SQLDataType.XML;
import static org.jooq.impl.SelectQueryImpl.EMULATE_SELECT_INTO_AS_CTAS;
import static org.jooq.impl.SelectQueryImpl.NO_SUPPORT_FOR_UPDATE_OF_FIELDS;
import static org.jooq.impl.Tools.CONFIG;
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
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.normaliseNameCase;
import static org.jooq.impl.Tools.parseNameCase;
import static org.jooq.impl.Tools.selectQueryImpl;
import static org.jooq.impl.Tools.updateQueryImpl;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_PARSE_ON_CONFLICT;
import static org.jooq.impl.Transformations.transformAppendMissingTableReferences;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
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
import org.jooq.CommentOnRoutineParametersStep;
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
import org.jooq.CreateSequenceAsStep;
import org.jooq.CreateSequenceFlagsStep;
// ...
import org.jooq.CreateTableAsStep;
import org.jooq.CreateTableCommentStep;
import org.jooq.CreateTableElementListStep;
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
import org.jooq.DropIndexOnStep;
import org.jooq.DropSchemaStep;
import org.jooq.DropTableStep;
// ...
import org.jooq.DropTypeStep;
import org.jooq.DropViewStep;
import org.jooq.Field;
import org.jooq.FieldOrRow;
import org.jooq.FieldOrRowOrSelect;
// ...
// ...
import org.jooq.Function1;
import org.jooq.Function2;
import org.jooq.Function3;
import org.jooq.Function4;
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
import org.jooq.Lambda1;
import org.jooq.LanguageContext;
import org.jooq.LikeEscapeStep;
// ...
import org.jooq.Merge;
import org.jooq.MergeMatchedDeleteStep;
import org.jooq.MergeMatchedStep;
import org.jooq.MergeMatchedWhereStep;
import org.jooq.MergeNotMatchedThenStep;
import org.jooq.MergeNotMatchedWhereStep;
import org.jooq.MergeUsingStep;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Name.Quoted;
import org.jooq.OptionallyOrderedAggregateFunction;
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
import org.jooq.SQLDialectCategory;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.SelectCorrelatedSubqueryStep;
import org.jooq.SelectField;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.Sequence;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.Statement;
// ...
import org.jooq.Table;
import org.jooq.TableElement;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOnStep;
import org.jooq.TableOptionalOnStep;
import org.jooq.TableOuterJoinStep;
import org.jooq.TablePartitionByStep;
import org.jooq.TableSampleRepeatableStep;
import org.jooq.TableSampleRowsStep;
import org.jooq.Truncate;
import org.jooq.TruncateCascadeStep;
import org.jooq.TruncateIdentityStep;
import org.jooq.UDTPathField;
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
import org.jooq.XML;
import org.jooq.XMLAggOrderByStep;
import org.jooq.XMLAttributes;
import org.jooq.XMLTableColumnPathStep;
import org.jooq.XMLTableColumnsStep;
import org.jooq.XMLTablePassingStep;
import org.jooq.conf.ParseNameCase;
import org.jooq.conf.ParseSearchSchema;
import org.jooq.conf.ParseUnknownFunctions;
import org.jooq.conf.ParseUnsupportedSyntax;
import org.jooq.conf.ParseWithMetaLookups;
import org.jooq.conf.RenderKeywordCase;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.QOM.DocumentOrContent;
import org.jooq.impl.QOM.JSONOnNull;
import org.jooq.impl.QOM.JoinHint;
import org.jooq.impl.QOM.PrimaryKey;
// ...
import org.jooq.impl.QOM.TableScope;
import org.jooq.impl.QOM.UEmpty;
import org.jooq.impl.QOM.XMLPassingMechanism;
import org.jooq.impl.ScopeStack.Value;
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
final class DefaultParseContext extends AbstractParseContext implements ParseContext {







    static final Set<SQLDialect>         SUPPORTS_HASH_COMMENT_SYNTAX             = SQLDialect.supportedBy(MARIADB, MYSQL);
    static final Set<SQLDialect>         NO_SUPPORT_QUOTED_BUILT_IN_FUNCION_NAMES = SQLDialect.supportedBy(DERBY, FIREBIRD, HSQLDB);

    final Queries parse() {
        return wrap(() -> {
            List<Query> result = new ArrayList<>();
            Query query;
            int p = positionBeforeWhitespace;

            do {
                parseDelimiterSpecifications();

                while (parseDelimiterIf(false))
                    p = positionBeforeWhitespace;

                retainComments(result, p);
                query = patchParsedQuery(parseQuery(false, false));
                if (query == IGNORE.get() || query == IGNORE_NO_DELIMITER.get())
                    continue;
                if (query != null)
                    result.add(query);
            }
            while (parseDelimiterIf(true) && (p = positionBeforeWhitespace) >= 0 && !done());

            if (query != null)
                retainComments(result, p);

            return done("Unexpected token or missing query delimiter", dsl.queries(result));
        });
    }

    private final void retainComments(List<Query> result, int p) {
        if (TRUE.equals(settings().isParseRetainCommentsBetweenQueries()) && p < position) {
            for (int i = p; i < position; i++) {
                if (character(i) != ' ') {
                    result.add(new IgnoreQuery(substring(p, position)));
                    break;
                }
            }
        }
    }

    private static final Pattern P_SEARCH_PATH = Pattern.compile("(?i:select\\s+(pg_catalog\\s*\\.\\s*)?set_config\\s*\\(\\s*'search_path'\\s*,\\s*'([^']*)'\\s*,\\s*\\w+\\s*\\))");

    private final Query patchParsedQuery(Query query) {

        // [#8910] Some statements can be parsed differently when we know we're
        //         parsing them for the DDLDatabase. This method patches these
        //         statements.
        if (isDDLDatabase()) {
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
                        return IGNORE.get();
            }
        }

        return query;
    }

    private boolean isDDLDatabase() {
        return TRUE.equals(configuration().data("org.jooq.ddl.parse-for-ddldatabase"));
    }

    final Query parseQuery0() {
        return wrap(() -> done("Unexpected clause", parseQuery(false, false)));
    }

    final Statement parseStatement0() {
        return wrap(() -> done("Unexpected content", parseStatementAndSemicolonIf()));
    }



















    final ResultQuery<?> parseResultQuery0() {
        return wrap(() -> done("Unexpected content after end of query input", (ResultQuery<?>) parseQuery(true, false)));
    }

    final Select<?> parseSelect0() {
        return wrap(() -> done("Unexpected content after end of query input", (Select<?>) parseQuery(true, true)));
    }

    final Table<?> parseTable0() {
        return wrap(() -> done("Unexpected content after end of table input", parseTable()));
    }

    final Field<?> parseField0() {
        return wrap(() -> done("Unexpected content after end of field input", parseField()));
    }

    final Row parseRow0() {
        return wrap(() -> done("Unexpected content after end of row input", parseRow()));
    }

    final Condition parseCondition0() {
        return wrap(() -> done("Unexpected content after end of condition input", parseCondition()));
    }

    final Name parseName0() {
        return wrap(() -> done("Unexpected content after end of name input", parseName()));
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

        scope.scopeStart();
        boolean previousMetaLookupsForceIgnore = metaLookupsForceIgnore();
        Query result = null;
        LanguageContext previous = languageContext;

        try {
            languageContext = LanguageContext.QUERY;

            switch (characterUpper()) {
                case 'A':
                    if (!parseResultQuery && peekKeyword("ALTER"))
                        return result = metaLookupsForceIgnore(true).parseAlter();
                    else if (!parseResultQuery && peekKeyword("ANALYZE"))
                        throw notImplemented("ANALYZE statement");

                    break;

                case 'B':
                    if (!parseResultQuery && peekKeyword("BEGIN WORK", "BEGIN TRANSACTION", "BEGIN TRAN"))
                        return result = parseStartTransaction();
                    else if (!parseResultQuery && parseKeywordIf("BT"))
                        return dsl.startTransaction();
                    else if (!parseResultQuery && peekKeyword("BEGIN")) {
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
                        return result = metaLookupsForceIgnore(true).parseCreateTable(null);
                    else if (!parseResultQuery && parseKeywordIf("CV"))
                        return result = metaLookupsForceIgnore(true).parseCreateView(false, false);
                    else if (!ignoreProEdition() && peekKeyword("CALL") && requireProEdition())



                        ;
                    else if (!parseResultQuery && peekKeyword("COMMIT"))
                        return result = parseCommit();
                    else if (parseKeywordIf("CONNECT"))
                        throw notImplemented("CONNECT");

                    break;

                case 'D':
                    if (!parseResultQuery && parseKeywordIf("DECLARE GLOBAL TEMPORARY TABLE"))
                        return result = parseCreateTable(TableScope.LOCAL_TEMPORARY);
                    else if (!parseResultQuery && !ignoreProEdition() && peekKeyword("DECLARE") && requireProEdition())
                        return result = parseBlock(true);
                    else if (!parseSelect && (peekKeyword("DELETE", "DEL")))
                        return result = parseDelete(null, parseResultQuery);
                    else if (!parseResultQuery && peekKeyword("DROP"))
                        return result = metaLookupsForceIgnore(true).parseDrop();
                    else if (!parseResultQuery && peekKeyword("DO"))
                        return result = parseDo();
                    else if (!parseResultQuery && peekKeyword("DESC", "DESCRIBE"))
                        throw notImplemented("DESCRIBE statement");

                    break;

                case 'E':
                    if (!parseResultQuery && peekKeyword("EXECUTE BLOCK AS"))
                        return result = parseBlock(true);
                    else if (!parseResultQuery && peekKeyword("EXEC"))
                        return result = parseExec();
                    else if (!ignoreProEdition() && peekKeyword("EXECUTE PROCEDURE") && requireProEdition())



                        ;
                    else if (!parseResultQuery && parseKeywordIf("ET", "END TRANSACTION"))
                        return dsl.commit();
                    else if (!parseResultQuery && peekKeyword("EXPLAIN"))
                        throw notImplemented("EXPLAIN statement");

                    break;

                case 'G':
                    if (!parseResultQuery && peekKeyword("GRANT"))
                        return result = metaLookupsForceIgnore(true).parseGrant();

                    break;

                case 'I':
                    if (!parseSelect && (peekKeyword("INSERT", "INS")))
                        return result = parseInsert(null, parseResultQuery);

                    break;

                case 'L':
                    if (parseKeywordIf("LOAD"))
                        throw notImplemented("LOAD");
                    else if (!parseResultQuery && peekKeyword("LOCK"))
                        throw notImplemented("LOCK statement");

                    break;

                case 'M':
                    if (!parseResultQuery && peekKeyword("MERGE"))
                        return result = parseMerge(null);

                    break;

                case 'O':
                    if (!parseResultQuery && peekKeyword("OPEN"))
                        return result = parseOpen();

                    break;

                case 'P':
                    if (!parseResultQuery && peekKeyword("PREPARE"))
                        throw notImplemented("PREPARE statement");

                    break;

                case 'R':
                    if (!parseResultQuery && peekKeyword("RENAME"))
                        return result = metaLookupsForceIgnore(true).parseRename();
                    else if (!parseResultQuery && peekKeyword("REVOKE"))
                        return result = metaLookupsForceIgnore(true).parseRevoke();
                    else if (parseKeywordIf("REPLACE"))
                        throw notImplemented("REPLACE");
                    else if (!parseResultQuery && peekKeyword("RELEASE"))
                        return result = parseReleaseSavepoint();
                    else if (!parseResultQuery && peekKeyword("ROLLBACK"))
                        return result = parseRollback();
                    else if (!parseResultQuery && peekKeyword("REFRESH"))
                        throw notImplemented("REFRESH statement", "https://github.com/jOOQ/jOOQ/issues/15533");
                    else if (!parseResultQuery && peekKeyword("RESET"))
                        throw notImplemented("RESET statement");

                    break;

                case 'S':
                    if (peekSelect(false))
                        return result = parseSelect();
                    else if (!parseResultQuery && peekKeyword("SET"))
                        return result = parseSet();
                    else if (!parseResultQuery && peekKeyword("SAVE", "SAVEPOINT"))
                        return result = parseSavepoint();
                    else if (!parseResultQuery && peekKeyword("START"))
                        return result = parseStartTransaction();
                    else if (!parseResultQuery && peekKeyword("SHOW"))
                        throw notImplemented("SHOW statement");

                    break;

                case 'T':
                    if (!parseSelect && peekKeyword("TABLE"))
                        return result = parseSelect();
                    else if (!parseResultQuery && peekKeyword("TRUNCATE"))
                        return result = parseTruncate();

                    break;

                case 'U':
                    if (!parseSelect && (peekKeyword("UPDATE", "UPD")))
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
                    if (!ignoreProEdition() && peekKeyword("{ CALL") && requireProEdition())



                        ;

                    break;

                default:
                    break;
            }

            throw exception("Unsupported query type");
        }
        catch (ParserException e) {

            // [#9061] Don't hide this pre-existing exceptions in scopeResolve()
            scope.scopeClear();
            throw e;
        }
        finally {
            scope.scopeEnd(result);
            scope.scopeResolve();
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
            if (parseKeywordIf("FUNCTION"))
                throw notImplemented("WITH FUNCTION");

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
        if (!parseSelect && (peekKeyword("DELETE", "DEL")))
            result = parseDelete(with, false);
        else if (!parseSelect && (peekKeyword("INSERT", "INS")))
            result = parseInsert(with, false);
        else if (!parseSelect && peekKeyword("MERGE"))
            result = parseMerge(with);
        else if (peekSelect(true))
            result = parseSelect(degree, with);
        else if (!parseSelect && (peekKeyword("UPDATE", "UPD")))
            result = parseUpdate(with, false);
        else if ((parseWhitespaceIf() || true) && done())
            throw exception("Missing statement after WITH");
        else
            throw exception("Unsupported statement after WITH");

        while (parens --> 0)
            parse(')');

        return result;
    }

    private final Field<?> parseScalarSubqueryIf() {
        FieldOrRowOrSelect r = parseSubqueryIf();

        if (r instanceof Select<?> s) {
            if (Tools.degree(s) != 1)
                throw exception("Select list must contain exactly one column");

            return field((Select) s);
        }

        return null;
    }

    private final FieldOrRowOrSelect parseSubqueryIf() {
        int p = position();

        try {
            if (peekSelectOrWith(true)) {
                parse('(');
                SelectQueryImpl<Record> select = parseWithOrSelect();
                parse(')');

                return select;
            }
        }
        catch (ParserException e) {

            // TODO: Find a better solution than backtracking, here, which doesn't complete in O(N)
            if (e.getMessage().contains("Token ')' expected"))
                position(p);
            else
                throw e;
        }

        return null;
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
        return parseSelect(degree, with, null);
    }

    private final SelectQueryImpl<Record> parseSelect(Integer degree, WithImpl with, SelectQueryImpl<Record> prefix) {
        scope.scopeStart();
        SelectQueryImpl<Record> result = parseQueryExpressionBody(degree, with, prefix);
        List<SortField<?>> orderBy = null;

        for (Field<?> field : result.getSelect())
            if (aliased(field) != null)
                scope.scope(field);

        if (parseKeywordIf("ORDER")) {
            if (parseProKeywordIf("SIBLINGS BY")) {




            }
            else if (parseKeywordIf("BY"))
                result.addOrderBy(orderBy = parseList(',', c -> c.parseSortField()));
            else
                throw expected("SIBLINGS BY", "BY");
        }

        boolean limit = false;
        boolean for_ = false;
        boolean offset = false;

        if (orderBy != null && parseKeywordIf("SEEK")) {
            boolean before = parseKeywordIf("BEFORE");
            if (!before)
                parseKeywordIf("AFTER");

            List<Field<?>> seek = parseList(',', c -> c.parseField());
            if (seek.size() != orderBy.size())
                throw exception("ORDER BY size (" + orderBy.size() + ") and SEEK size (" + seek.size() + ") must match");

            if (before)
                result.addSeekBefore(seek);
            else
                result.addSeekAfter(seek);

            offset = true;
        }

        while ((!limit && (limit = parseSelectLimit(result, offset)))
            || (!for_ && (for_ = parseSelectFor(result))))
            ;

        if (parseKeywordIf("WITH CHECK OPTION"))
            result.setWithCheckOption();
        else if (parseKeywordIf("WITH READ ONLY"))
            result.setWithReadOnly();

        scope.scopeEnd(result);
        return result;
    }

    private final boolean parseSelectLimit(SelectQueryImpl<Record> result, boolean offset) {
        boolean limit = result.getLimit().isApplicable();

        if (!limit)
            parseLimit(result, !offset);

        return limit;
    }

    private final boolean parseSelectFor(SelectQueryImpl<Record> result) {
        boolean for_;

        forClause:
        if (for_ = parseKeywordIf("FOR")) {
            boolean jsonb;

            if (parseKeywordIf("KEY SHARE"))
                result.setForKeyShare(true);
            else if (parseKeywordIf("NO KEY UPDATE"))
                result.setForNoKeyUpdate(true);
            else if (parseKeywordIf("SHARE"))
                result.setForShare(true);
            else if (parseKeywordIf("UPDATE"))
                result.setForUpdate(true);
            else if (parseProKeywordIf("XML")) {













































            }
            else if (!ignoreProEdition() && (jsonb = parseKeywordIf("JSONB", "JSON")) && requireProEdition()) {





























            }
            else
                throw expected("UPDATE", "NO KEY UPDATE", "SHARE", "KEY SHARE", "XML", "JSON");

            if (parseKeywordIf("OF"))
                if (NO_SUPPORT_FOR_UPDATE_OF_FIELDS.contains(parseDialect()))
                    result.setForUpdateOf(parseList(',', t -> t.parseTable()).toArray(EMPTY_TABLE));
                else
                    result.setForUpdateOf(parseList(',', c -> c.parseField()));

            if (parseKeywordIf("NOWAIT"))
                result.setForUpdateNoWait();
            else if (parseProKeywordIf("WAIT"))



                ;
            else if (parseKeywordIf("SKIP LOCKED"))
                result.setForUpdateSkipLocked();
        }

        return for_;
    }

    private final void parseLimit(SelectQueryImpl<Record> result, boolean allowOffset) {
        boolean offsetStandard = false;
        boolean offsetPostgres = false;

        if (allowOffset && parseKeywordIf("OFFSET")) {
            result.addOffset((Field) parseField());

            if (parseKeywordIf("ROWS", "ROW"))
                offsetStandard = true;

            // Ingres doesn't have a ROWS keyword after offset
            else if (peekKeyword("FETCH"))
                offsetStandard = true;
            else
                offsetPostgres = true;
        }

        if (!offsetStandard && parseKeywordIf("LIMIT")) {
            Field<Long> limit = (Field) parseField();

            if (offsetPostgres) {
                result.addLimit(limit);

                if (parseKeywordIf("PERCENT"))
                    result.setLimitPercent(true);

                if (parseKeywordIf("WITH TIES"))
                    result.setWithTies(true);
            }
            else if (allowOffset && parseIf(',')) {
                result.addLimit(limit, (Field) parseField());
            }
            else {
                if (parseKeywordIf("PERCENT"))
                    result.setLimitPercent(true);

                if (parseKeywordIf("WITH TIES"))
                    result.setWithTies(true);

                if (allowOffset && parseKeywordIf("OFFSET"))
                    result.addLimit((Field) parseField(), limit);
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
                result.addLimit((Field) parseField());

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
            scope.scopeEnd(local);
            scope.scopeStart();

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
            scope.scopeEnd(local);
            scope.scopeStart();

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

    private final <S extends Select<?>> S degreeCheck(int expected, S s) {
        return degreeCheck(expected, s, true);
    }

    private final <S extends Select<?>> S degreeCheck(int expected, S s, boolean throwIfNotMatched) {
        if (expected == 0)
            return s;

        int actual = Tools.degree(s);
        if (actual == 0)
            return s;

        if (expected != actual)
            if (throwIfNotMatched)
                throw exception("Select list must contain " + expected + " columns. Got: " + actual);
            else
                return null;

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
        parseKeywordUndocumentedAlternatives("SELECT", "SEL");
        String hints = parseHints();
        boolean distinct = parseKeywordIf("DISTINCT", "UNIQUE");
        List<Field<?>> distinctOn = null;

        if (distinct) {
            if (parseKeywordIf("ON")) {
                parse('(');
                distinctOn = parseList(',', c -> c.parseField());
                parse(')');
            }
        }
        else
            parseKeywordIf("ALL");

        Field<Long> limit = null;
        Field<Long> offset = null;
        boolean percent = false;
        boolean withTies = false;

        // T-SQL style TOP .. START AT
        try {
            supportArraySubscripts = false;

            if (parseKeywordIf("TOP")) {
                limit = (Field) parseField();
                percent = parseProKeywordIf("PERCENT");

                if (parseKeywordIf("START AT"))
                    offset = (Field) parseField();
                else if (parseKeywordIf("WITH TIES"))
                    withTies = true;
            }

            // Informix style SKIP .. FIRST
            else if (parseKeywordIf("SKIP")) {
                offset = (Field) parseField();

                if (parseKeywordIf("FIRST"))
                    limit = (Field) parseField();
            }
            else if (parseKeywordIf("FIRST")) {
                limit = (Field) parseField();
            }
        }
        finally {
            supportArraySubscripts = true;
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

        if (parseKeywordIf("FROM")) {
            from = parseList(',', ParseContext::parseTable);

            // [#16762] No explicit DUAL tables should be present at the top level, by default
            if (from.size() == 1)
                from.removeIf(t -> t instanceof Dual);
        }


        // [#9061] Register tables in scope as early as possible
        // TODO: Move this into parseTables() so lateral joins can profit from lookups (?)
        if (from != null)
            for (Table<?> table : from)
                scope.scope(table);

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
            if (!parseKeywordIf("ALL") && parseKeywordIf("DISTINCT"))
                result.setGroupByDistinct(true);

            if (parseIf('(', ')', true)) {
                parse(')');
                result.addGroupBy();
            }
            else if (parseKeywordIf("ROLLUP")) {
                parse('(');
                result.addGroupBy(rollup(parseList(',', c -> c.parseField()).toArray(EMPTY_FIELD)));
                parse(')');
            }
            else if (parseKeywordIf("CUBE")) {
                parse('(');
                result.addGroupBy(cube(parseList(',', c -> c.parseField()).toArray(EMPTY_FIELD)));
                parse(')');
            }
            else if (parseKeywordIf("GROUPING SETS")) {
                parse('(');
                List<List<Field<?>>> fieldSets = parseList(',', c -> parseFieldsOrEmptyOptionallyParenthesised(false));
                parse(')');
                result.addGroupBy(groupingSets(fieldSets.toArray((Collection[]) EMPTY_COLLECTION)));
            }
            else {
                groupBy = parseOrdinaryGroupingSets();

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

    private final List<GroupField> parseOrdinaryGroupingSets() {
        List<GroupField> result = new ArrayList<>();

        do {

            // [#14159] Explicit ROW expressions are actual RowAsFields.
            //          Other parenthesised expressions are grouping column reference lists
            if (peekKeyword("ROW")) {
                result.add(parseField());
            }
            else {
                FieldOrRow fr = parseFieldOrRow();

                if (fr instanceof Field<?> f)
                    result.add(f);
                else
                    result.addAll(asList(((Row) fr).fields()));
            }
        }
        while (parseIf(','));

        return result;
    }

    private final boolean parseQueryPrimaryConnectBy(SelectQueryImpl<Record> result) {
        if (parseProKeywordIf("CONNECT BY")) {







            return true;
        }
        else
            return false;
    }

    private final boolean parseQueryPrimaryStartWith(SelectQueryImpl<Record> result) {
        if (parseProKeywordIf("START WITH")) {



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
            ? partitionBy(parseList(',', c -> c.parseField()))
            : null;

        if (parseKeywordIf("ORDER BY"))
            if (orderByAllowed)
                s2 = s1 == null
                    ? orderBy(parseList(',', c -> c.parseSortField()))
                    : s1.orderBy(parseList(',', c -> c.parseSortField()));
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
                else if (asTrue(n = parseUnsignedIntegerLiteral()))
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
            else if (asTrue(n = parseUnsignedIntegerLiteral()))
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
        Field<Long> limit = null;

        // T-SQL style TOP .. START AT
        if (parseKeywordIf("TOP")) {
            limit = (Field) parseField();

            // [#8623] TODO Support this
            // percent = parseKeywordIf("PERCENT") && requireProEdition();
        }

        parseKeywordIf("FROM");
        Table<?> table = scope.scope(parseJoinedTable(() -> peekKeyword(KEYWORD_LOOKUP_IN_DELETE_FROM)));
        DeleteUsingStep<?> s1 = with == null ? dsl.delete(table) : with.delete(table);
        DeleteWhereStep<?> s2 = parseKeywordIf("USING", "FROM") ? s1.using(parseList(',', t -> scope.scope(parseJoinedTable(() -> peekKeyword(KEYWORD_LOOKUP_IN_DELETE_FROM))))) : s1;
        DeleteOrderByStep<?> s3 = parseKeywordIf("ALL")
            ? s2
            : parseKeywordIf("WHERE")
            ? s2.where(parseCondition())
            : s2;
        DeleteLimitStep<?> s4 = parseKeywordIf("ORDER BY") ? s3.orderBy(parseList(',', c -> c.parseSortField())) : s3;
        DeleteReturningStep<?> s5 = (limit != null || parseKeywordIf("LIMIT"))
            ? s4.limit(limit != null ? limit : (Field) parseField())
            : s4;
        return (parseResultQuery ? parseKeyword("RETURNING") : parseKeywordIf("RETURNING"))
            ? s5.returning(parseSelectList())
            : s5;
    }

    private final Query parseInsert(WithImpl with, boolean parseResultQuery) {
        scope.scopeStart();
        parseKeywordUndocumentedAlternatives("INSERT", "INS");
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

        scope.scope(table);

        InsertSetStep<?> s1 = (with == null ? dsl.insertInto(table) : with.insertInto(table));
        Field<?>[] fields = null;

        if (!peekSelectOrWith(true) && parseIf('(') && !parseIf(')')) {
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
            else if (peekSelectOrWith(true)) {
                Field<?>[] f = fields;

                // [#13503] The SELECT in INSERT .. SELECT has its own, independent scope
                returning = onDuplicate = newScope(() -> {
                    Select<?> select = parseWithOrSelect();

                    return (f == null)
                        ? s1.select(select)
                        : s1.columns(f).select(select);
                });
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

                    // Cast is necessary, see https://github.com/eclipse-jdt/eclipse.jdt.core/issues/99
                    InsertOnConflictWhereStep<?> where =
                          parseKeywordIf("ALL TO EXCLUDED")
                        ? onDuplicate.onDuplicateKeyUpdate().setAllToExcluded()
                        : parseKeywordIf("NON KEY TO EXCLUDED")
                        ? onDuplicate.onDuplicateKeyUpdate().setNonKeyToExcluded()
                        : parseKeywordIf("NON PRIMARY KEY TO EXCLUDED")
                        ? onDuplicate.onDuplicateKeyUpdate().setNonPrimaryKeyToExcluded()
                        : parseKeywordIf("NON CONFLICTING KEY TO EXCLUDED")
                        ? onDuplicate.onDuplicateKeyUpdate().setNonConflictingKeyToExcluded()
                        : onDuplicate.onDuplicateKeyUpdate().set((Map<?, ?>) data(DATA_PARSE_ON_CONFLICT, true, c -> c.parseSetClauseList()));

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

                        // Cast is necessary, see https://github.com/eclipse-jdt/eclipse.jdt.core/issues/99
                        InsertOnConflictWhereStep<?> where =
                              parseKeywordIf("ALL TO EXCLUDED")
                            ? doUpdate.doUpdate().setAllToExcluded()
                            : parseKeywordIf("NON KEY TO EXCLUDED")
                            ? doUpdate.doUpdate().setNonKeyToExcluded()
                            : parseKeywordIf("NON PRIMARY KEY TO EXCLUDED")
                            ? doUpdate.doUpdate().setNonPrimaryKeyToExcluded()
                            : parseKeywordIf("NON CONFLICTING KEY TO EXCLUDED")
                            ? doUpdate.doUpdate().setNonConflictingKeyToExcluded()
                            : doUpdate.doUpdate().set((Map<?, ?>) data(DATA_PARSE_ON_CONFLICT, true, c -> c.parseSetClauseList()));

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
            scope.scopeEnd(((InsertImpl) s1).getDelegate());
        }
    }

    private final Query parseUpdate(WithImpl with, boolean parseResultQuery) {
        parseKeywordUndocumentedAlternatives("UPDATE", "UPD");
        Field<Long> limit = null;

        // T-SQL style TOP .. START AT
        if (parseKeywordIf("TOP")) {
            limit = (Field) parseField();

            // [#8623] TODO Support this
            // percent = parseKeywordIf("PERCENT") && requireProEdition();
        }

        Table<?> table = scope.scope(parseJoinedTable(() -> peekKeyword(KEYWORD_LOOKUP_IN_UPDATE_FROM)));
        UpdateSetFirstStep<?> s1 = (with == null ? dsl.update(table) : with.update(table));
        List<Table<?>> from = parseKeywordIf("FROM") ? parseList(',', t -> scope.scope(parseJoinedTable(() -> peekKeyword(KEYWORD_LOOKUP_IN_UPDATE_FROM)))) : null;

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
            ? s2.from(parseList(',', t -> parseJoinedTable(() -> peekKeyword(KEYWORD_LOOKUP_IN_UPDATE_FROM))))
            : s2;
        UpdateOrderByStep<?> s4 = parseKeywordIf("ALL")
            ? s3
            : parseKeywordIf("WHERE")
            ? s3.where(parseCondition())
            : s3;
        UpdateLimitStep<?> s5 = parseKeywordIf("ORDER BY") ? s4.orderBy(parseList(',', c -> c.parseSortField())) : s4;
        UpdateReturningStep<?> s6 = (limit != null || parseKeywordIf("LIMIT"))
            ? s5.limit(limit != null ? limit : (Field) parseField())
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
        MergeUsingStep<?> s1 = (with == null ? dsl.mergeInto(target) : with.mergeInto(target));
        MergeMatchedStep<?> s2 = s1.using(usingTable).on(on);

        for (;;) {
            Map<Field<?>, Object> updateSet;
            Condition updateAnd = null;
            Condition updateWhere = null;
            Condition deleteWhere = null;
            List<Field<?>> insertColumns = null;
            List<Field<?>> insertValues = null;
            Condition insertAnd = null;
            Condition insertWhere = null;

            boolean notMatchedBySource = false;

            if (parseKeywordIf("WHEN MATCHED") || (notMatchedBySource = parseKeywordIf("WHEN NOT MATCHED BY SOURCE"))) {
                update = true;

                if (parseKeywordIf("AND"))
                    updateAnd = parseCondition();

                if (parseKeywordIf("THEN DELETE")) {
                    s2 = notMatchedBySource
                        ? updateAnd != null
                            ? s2.whenNotMatchedBySourceAnd(updateAnd).thenDelete()
                            : s2.whenNotMatchedBySource().thenDelete()
                        : updateAnd != null
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
                        if (notMatchedBySource)
                            s2.whenNotMatchedBySourceAnd(updateAnd).thenUpdate().set(updateSet);
                        else
                            s2.whenMatchedAnd(updateAnd).thenUpdate().set(updateSet);
                    }
                    else {
                        MergeMatchedWhereStep<?> s3 = notMatchedBySource
                            ? s2.whenNotMatchedBySource().thenUpdate().set(updateSet)
                            : s2.whenMatchedThenUpdate().set(updateSet);
                        MergeMatchedDeleteStep<?> s4 = updateWhere != null ? s3.where(updateWhere) : s3;
                        s2 = deleteWhere != null ? s4.deleteWhere(deleteWhere) : s3;
                    }
                }
            }
            else if (parseKeywordIf("WHEN NOT MATCHED")) {
                insert = true;
                boolean byTarget = parseKeywordIf("BY TARGET");

                if (parseKeywordIf("AND"))
                    insertAnd = parseCondition();

                parseKeyword("THEN INSERT");
                parse('(');
                insertColumns = parseUniqueList("identifier", ',', c -> parseFieldName());
                parse(')');
                parseKeyword("VALUES");
                parse('(');
                insertValues = parseList(',', c -> c.parseKeywordIf("DEFAULT") ? default_() : c.parseField());
                parse(')');

                if (insertColumns.size() != insertValues.size())
                    throw exception("Insert column size (" + insertColumns.size() + ") must match values size (" + insertValues.size() + ")");

                if (insertWhere == null && parseKeywordIf("WHERE"))
                    insertWhere = parseCondition();

                MergeNotMatchedThenStep<?> s3 = byTarget
                    ? insertAnd != null
                        ? s2.whenNotMatchedByTargetAnd(insertAnd)
                        : s2.whenNotMatchedByTarget()
                    : insertAnd != null
                        ? s2.whenNotMatchedAnd(insertAnd)
                        : s2.whenNotMatched();
                MergeNotMatchedWhereStep<?> s4 = s3.thenInsert(insertColumns).values(insertValues);
                s2 = (MergeMatchedStep<?>) (insertWhere != null ? s4.where(insertWhere) : s4);
            }
            else
                break;
        }

        if (!update && !insert)
            throw exception("At least one of UPDATE or INSERT clauses is required");

        return s2;
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
            Object value = parseSignedIntegerLiteralIf0(true);
            return dsl.set(name, value != null ? inline(value) : inline(parseStringLiteral()));
        }

        // There are many SET commands in programs like sqlplus, which we'll simply ignore
        else {
            parseUntilEOL();
            return IGNORE_NO_DELIMITER.get();
        }
    }

    private final Query parseSetCatalog() {
        return dsl.setCatalog(parseCatalogName());
    }

    private final Query parseUse() {
        parseKeyword("USE");
        if (parseKeywordIf("SCHEMA"))
            return dsl.setSchema(parseSchemaName());

        parseKeywordIf("DATABASE");
        return dsl.setCatalog(parseCatalogName());
    }

    private final Query parseSetSchema() {
        parseIf('=');
        return peek('\'') ? dsl.setSchema(parseStringLiteral()) : dsl.setSchema(parseSchemaName());
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

        CommentOnIsStep s1 = null;

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
        else if (parseKeywordIf("MATERIALIZED VIEW")) {
            s1 = dsl.commentOnMaterializedView(parseTableName());
        }
        else if (parseProKeywordIf("FUNCTION")) {





        }
        else if (parseProKeywordIf("PROCEDURE")) {





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
            return IGNORE.get();
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
            return IGNORE.get();
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
            return IGNORE.get();
        }
        else if (parseKeywordIf("TRANSFORM FOR")) {
            parseIdentifier();
            parseKeyword("LANGUAGE");
            parseIdentifier();
            parseKeyword("IS");
            parseStringLiteral();
            return IGNORE.get();
        }
        else
            throw unsupportedClause();

        parseKeyword("IS");
        return s1.is(parseStringLiteral());
    }

    private final DDLQuery parseCreate() {
        parseKeyword("CREATE");

        switch (characterUpper()) {
            case 'A':
                if (parseProKeywordIf("ALIAS"))



                    ;
                break;

            case 'C':
                if (parseKeywordIf("CACHED TABLE"))
                    return parseCreateTable(null);

                break;

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
                    return parseCreateView(false, false);
                else if (parseKeywordIf("FORCE MATERIALIZED VIEW"))
                    return parseCreateView(false, true);
                else if (parseKeywordIf("FULLTEXT INDEX") && requireUnsupportedSyntax())
                    return parseCreateIndex(false);
                else if (parseProKeywordIf("FUNCTION"))



                    ;

                break;

            case 'G':
                if (parseKeywordIf("GENERATOR"))
                    return parseCreateSequence();
                else if (parseKeywordIf("GLOBAL TEMP TABLE", "GLOBAL TEMPORARY TABLE"))
                    return parseCreateTable(TableScope.GLOBAL_TEMPORARY);

                break;

            case 'I':
                if (parseKeywordIf("INDEX"))
                    return parseCreateIndex(false);

                break;

            case 'L':
                if (parseKeywordIf("LOCAL TEMP TABLE", "LOCAL TEMPORARY TABLE"))
                    return parseCreateTable(TableScope.LOCAL_TEMPORARY);

                break;

            case 'M':
                if (parseKeywordIf("MEMORY TABLE"))
                    return parseCreateTable(null);
                else if (parseKeywordIf("MATERIALIZED VIEW"))
                    return parseCreateView(false, true);

                break;

            case 'O':
                if (parseKeywordIf("OR")) {
                    parseKeyword("REPLACE", "ALTER");

                    if (parseProKeywordIf("TRIGGER"))



                        ;
                    else if (parseKeywordIf("VIEW", "FORCE VIEW"))
                        return parseCreateView(true, false);
                    else if (parseKeywordIf("MATERIALIZED VIEW"))
                        return parseCreateView(true, true);
                    else if (parseProKeywordIf("FUNCTION"))



                        ;
                    else if (parseKeywordIf("PACKAGE"))
                        throw notImplemented("CREATE PACKAGE", "https://github.com/jOOQ/jOOQ/issues/9190");
                    else if (parseProKeywordIf("PROC", "PROCEDURE"))



                        ;
                    else if (parseProKeywordIf("PUBLIC SYNONYM", "PUBLIC ALIAS"))



                        ;
                    else if (parseProKeywordIf("PRIVATE SYNONYM", "SYNONYM", "ALIAS"))



                        ;
                    else
                        throw expected("ALIAS", "FUNCTION", "PACKAGE", "PROCEDURE", "PRIVATE SYNONYM", "PUBLIC ALIAS", "PUBLIC SYNONYM", "SYNONYM", "TRIGGER", "VIEW");
                }

                break;

            case 'P':
                if (parseKeywordIf("PRIVATE TEMP TABLE", "PRIVATE TEMPORARY TABLE"))
                    return parseCreateTable(TableScope.LOCAL_TEMPORARY);
                else if (parseKeywordIf("PACKAGE"))
                    throw notImplemented("CREATE PACKAGE", "https://github.com/jOOQ/jOOQ/issues/9190");
                else if (parseProKeywordIf("PROC", "PROCEDURE"))



                    ;
                else if (parseProKeywordIf("PUBLIC SYNONYM", "PUBLIC ALIAS"))



                    ;
                else if (parseProKeywordIf("PRIVATE SYNONYM"))



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
                else if (parseProKeywordIf("SYNONYM"))



                    ;

                break;

            case 'T':
                if (parseKeywordIf("TABLE"))
                    return parseCreateTable(null);
                else if (parseKeywordIf("TEMP TABLE", "TEMPORARY TABLE"))
                    return parseCreateTable(TableScope.TEMPORARY);
                else if (parseProKeywordIf("TRIGGER"))



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
                    return parseCreateView(false, false);
                else if (parseKeywordIf("VIRTUAL") && parseKeyword("TABLE"))
                    return parseCreateTable(null);

                break;
        }

        throw expected(
            "ALIAS",
            "FUNCTION",
            "LOCAL TEMPORARY TABLE",
            "GENERATOR",
            "GLOBAL TEMPORARY TABLE",
            "INDEX",
            "OR ALTER",
            "OR REPLACE",
            "PRIVATE SYNONYM",
            "PRIVATE TEMPORARY TABLE",
            "PROCEDURE",
            "PUBLIC ALIAS",
            "PUBLIC SYNONYM",
            "SCHEMA",
            "SEQUENCE",
            "SYNONYM",
            "TABLE",
            "TEMP TABLE",
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
                if (parseProKeywordIf("FUNCTION"))



                    ;

                break;

            case 'I':
                if (parseKeywordIf("INDEX"))
                    return parseAlterIndex();

                break;

            case 'M':
                if (parseKeywordIf("MATERIALIZED VIEW"))
                    return parseAlterView(true);

                break;
            case 'P':
                if (parseKeywordIf("PACKAGE"))
                    throw notImplemented("ALTER PACKAGE", "https://github.com/jOOQ/jOOQ/issues/9190");
                else if (parseProKeywordIf("PROCEDURE"))



                    ;

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
                    return parseAlterView(false);

                break;
        }

        throw expected("DOMAIN", "INDEX", "SCHEMA", "SEQUENCE", "SESSION", "TABLE", "TYPE", "VIEW");
    }

    private final DDLQuery parseDrop() {
        parseKeyword("DROP");

        switch (characterUpper()) {
            case 'A':
                if (parseProKeywordIf("ALIAS"))



                    ;
                break;

            case 'D':
                if (parseKeywordIf("DATABASE"))
                    return parseDropDatabase();
                else if (parseKeywordIf("DOMAIN"))
                    return parseCascadeRestrictIf(
                        parseIfExists(this::parseDomainName, dsl::dropDomainIfExists, dsl::dropDomain),
                        DropDomainCascadeStep::cascade,
                        DropDomainCascadeStep::restrict
                    );

                break;

            case 'E':
                if (parseKeywordIf("EXTENSION"))
                    return parseDropExtension();

                break;

            case 'F':
                if (parseProKeywordIf("FUNCTION"))



                    ;

                break;

            case 'G':
                if (parseKeywordIf("GENERATOR"))
                    return parseDropSequence();
                else if (parseKeywordIf("GLOBAL TEMPORARY TABLE"))
                    return parseDropTable(TableScope.GLOBAL_TEMPORARY);

                break;

            case 'I':
                if (parseKeywordIf("INDEX"))
                    return parseDropIndex();

                break;

            case 'L':
                if (parseKeywordIf("LOCAL TEMPORARY TABLE"))
                    return parseDropTable(TableScope.LOCAL_TEMPORARY);

                break;

            case 'M':
                if (parseKeywordIf("MATERIALIZED VIEW"))
                    return parseDropView(true);

                break;

            case 'P':
                if (parseKeywordIf("PACKAGE"))
                    throw notImplemented("DROP PACKAGE", "https://github.com/jOOQ/jOOQ/issues/9190");
                else if (parseProKeywordIf("PROC", "PROCEDURE"))



                    ;
                else if (parseProKeywordIf("PUBLIC ALIAS", "PUBLIC SYNONYM"))



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
                    return parseCascadeRestrictIf(
                        parseIfExists(this::parseSchemaName, dsl::dropSchemaIfExists, dsl::dropSchema),
                        DropSchemaStep::cascade,
                        DropSchemaStep::restrict
                    );
                else if (parseProKeywordIf("SYNONYM"))



                    ;

                break;

            case 'T':
                if (parseKeywordIf("TABLE"))
                    return parseDropTable(null);
                else if (parseKeywordIf("TEMPORARY TABLE"))
                    return parseDropTable(TableScope.TEMPORARY);
                else if (parseProKeywordIf("TRIGGER"))



                    ;
                else if (parseKeywordIf("TYPE")) {












                    return parseCascadeRestrictIf(
                        parseIfExists(this::parseNames,
                            n -> dsl.dropTypeIfExists(n.toArray(EMPTY_NAME)),
                            n -> dsl.dropType(n.toArray(EMPTY_NAME))
                        ),
                        DropTypeStep::cascade,
                        DropTypeStep::restrict
                    );
                }
                else if (parseKeywordIf("TABLESPACE"))
                    throw notImplemented("DROP TABLESPACE");

                break;

            case 'U':
                if (parseKeywordIf("USER"))
                    throw notImplemented("DROP USER", "https://github.com/jOOQ/jOOQ/issues/10167");

                break;

            case 'V':
                if (parseKeywordIf("VIEW"))
                    return parseDropView(false);

                break;
        }

        throw expected(
            "ALIAS",
            "GENERATOR",
            "FUNCTION",
            "INDEX",
            "PROCEDURE",
            "PUBLIC ALIAS",
            "PUBLIC SYNONYM",
            "SCHEMA",
            "SEQUENCE",
            "SYNONYM",
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
        List<Table<?>> table = parseList(',', ctx -> parseTableName());
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

        return cascade
            ? step2.cascade()
            : restrict
            ? step2.restrict()
            : step2;
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
            if (!ignoreProEdition() && requireProEdition()) {



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





            if (allowDeclareSection && parseProKeywordIf("DECLARE"))



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



































































    private final Query parseStartTransaction() {
        parseKeyword("START", "BEGIN");
        parseKeyword("WORK", "TRAN", "TRANSACTION");
        parseKeywordIf("READ WRITE");
        return dsl.startTransaction();
    }

    private final Query parseSavepoint() {
        if (parseKeywordIf("SAVEPOINT")) {
            Name n = parseIdentifier();
            parseKeywordIf("UNIQUE");
            parseKeywordIf("ON ROLLBACK RETAIN CURSORS");
            return dsl.savepoint(n);
        }

        parseKeyword("SAVE");
        parseKeyword("TRAN", "TRANSACTION");
        return dsl.savepoint(parseIdentifier());
    }

    private final Query parseReleaseSavepoint() {
        parseKeyword("RELEASE");
        parseKeywordIf("TO");
        parseKeywordIf("SAVEPOINT");
        return dsl.releaseSavepoint(parseIdentifier());
    }

    private final Query parseCommit() {
        parseKeyword("COMMIT");
        parseKeywordIf("WORK", "TRAN", "TRANSACTION");
        return dsl.commit();
    }

    private final Query parseRollback() {
        parseKeyword("ROLLBACK");

        if (parseKeywordIf(
                "TRAN",
                "TRANSACTION TO SAVEPOINT",
                "TRANSACTION TO",
                "TRANSACTION",
                "WORK TO SAVEPOINT",
                "TO SAVEPOINT",
                "TO"
        ))
            return dsl.rollback().toSavepoint(parseIdentifier());

        parseKeywordIf("WORK");
        return dsl.rollback();
    }

    private final Block parseDo() {
        parseKeyword("DO");
        return (Block) dsl.parser().parseQuery(parseStringLiteral());
    }

    private final Statement parseStatement() {
        switch (characterUpper()) {
            case 'C':
                if (!ignoreProEdition() && peekKeyword("CALL") && requireProEdition())



                ;
                else if (!ignoreProEdition() && peekKeyword("CONTINUE") && requireProEdition())



                ;

                break;

            case 'D':
                if (!ignoreProEdition() && peekKeyword("DECLARE") && requireProEdition())







                ;
                else if (!ignoreProEdition() && peekKeyword("DEFINE") && requireProEdition())



                ;

                break;

            case 'E':
                if (!ignoreProEdition() && peekKeyword("EXECUTE PROCEDURE", "EXEC") && requireProEdition())



                ;
                if (!ignoreProEdition() && peekKeyword("EXECUTE") && !peekKeyword("EXECUTE BLOCK") && requireProEdition())



                ;
                else if (peekProKeyword("EXIT"))



                ;

                break;

            case 'F':
                if (peekProKeyword("FOR"))



                ;

                break;

            case 'G':
                if (peekProKeyword("GOTO"))



                ;

                break;

            case 'I':
                if (peekProKeyword("IF"))



                ;
                else if (peekProKeyword("ITERATE"))



                ;

                break;

            case 'L':
                if (peekProKeyword("LEAVE"))



                ;
                else if (peekProKeyword("LET"))



                ;
                else if (peekProKeyword("LOOP"))



                ;

                break;

            case 'N':
                if (peekKeyword("NULL"))
                    return parseNullStatement();

                break;

            case 'R':
                if (peekProKeyword("REPEAT"))



                ;
                else if (peekProKeyword("RETURN"))



                ;
                else if (peekProKeyword("RAISE"))



                ;

                break;

            case 'S':
                if (peekProKeyword("SET"))



                ;
                else if (peekProKeyword("SIGNAL"))



                ;

                break;

            case 'W':
                if (peekProKeyword("WHILE"))



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

    private final DDLQuery parseCreateView(boolean orReplace, boolean materialized) {
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

        return (ifNotExists
            ? materialized
                ? dsl.createMaterializedViewIfNotExists(view, fields)
                : dsl.createViewIfNotExists(view, fields)
            : orReplace
            ? materialized
                ? dsl.createOrReplaceMaterializedView(view, fields)
                : dsl.createOrReplaceView(view, fields)
            : materialized
                ? dsl.createMaterializedView(view, fields)
                : dsl.createView(view, fields)
        ).as(select);
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
        return IGNORE.get();
    }

    private final DDLQuery parseDropExtension() {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        parseIdentifiers();
        ifExists = ifExists || parseKeywordIf("IF EXISTS");
        if (!parseKeywordIf("CASCADE"))
            parseKeywordIf("RESTRICT");
        return IGNORE.get();
    }

    private final DDLQuery parseAlterView(boolean materialized) {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Table<?> oldName = parseTableName();
        Field<?>[] fields = EMPTY_FIELD;

        if (parseIf('(')) {
            fields = parseList(',', c -> parseFieldName()).toArray(fields);
            parse(')');
        }

        if (parseKeywordIf("AS")) {
            Select<?> select = parseWithOrSelect();
            int degree = Tools.degree(select);

            if (fields.length > 0 && fields.length != degree)
                throw exception("Select list size (" + degree + ") must match declared field size (" + fields.length + ")");

            if (fields.length == 0)
                return dsl.alterView(oldName).as(select);
            else
                return dsl.alterView(oldName, fields).as(select);
        }
        else if (fields.length > 0)
            throw expected("AS");
        else if (parseKeywordIf("RENAME")) {
            parseKeyword("AS", "TO");
            Table<?> newName = parseTableName();

            return (
                  ifExists
                ? materialized
                    ? dsl.alterMaterializedViewIfExists(oldName)
                    : dsl.alterViewIfExists(oldName)
                : materialized
                    ? dsl.alterMaterializedView(oldName)
                    : dsl.alterView(oldName)
            ).renameTo(newName);
        }
        else if (parseKeywordIf("OWNER TO") && parseUser() != null)
            return IGNORE.get();
        else if (parseKeywordIf("SET"))
            return (materialized
                ? dsl.alterMaterializedView(oldName)
                : dsl.alterView(oldName)).comment(parseOptionsDescription());
        else
            throw expected("AS", "OWNER TO", "RENAME", "SET");
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

    private final DDLQuery parseDropView(boolean materialized) {
        return parseCascadeRestrictIf(
            materialized
                 ? parseIfExists(this::parseTableName, dsl::dropMaterializedViewIfExists, dsl::dropMaterializedView)
                 : parseIfExists(this::parseTableName, dsl::dropViewIfExists, dsl::dropView),
            DropViewStep::cascade,
            DropViewStep::restrict,
            true
        );
    }

    private final DDLQuery parseCreateSequence() {
        boolean ifNotExists = parseKeywordIf("IF NOT EXISTS");
        Sequence<?> schemaName = parseSequenceName();

        CreateSequenceFlagsStep s = ifNotExists
            ? dsl.createSequenceIfNotExists(schemaName)
            : dsl.createSequence(schemaName);

        boolean as = false;
        boolean startWith = false;
        boolean incrementBy = false;
        boolean minvalue = false;
        boolean maxvalue = false;
        boolean cycle = false;
        boolean cache = false;

        for (;;) {
            Field field;
            DataType type = null;

            if (!as && (as |= (parseKeywordIf("AS") && (type = parseDataType()) != null)))
                s = ((CreateSequenceAsStep) s).as(type);
            else if (!startWith && (startWith |= (field = parseSequenceStartWithIf()) != null))
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
            return IGNORE.get();
        }
        else if (parseKeywordIf("OWNED BY") && parseName() != null) {
            return IGNORE.get();
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
                Field<? extends Number> field;

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
        return parseKeywordIf("NO CACHE", "NOCACHE");
    }

    private final Field<? extends Number> parseSequenceCacheIf() {
        return parseKeywordIf("CACHE") && (parseIf("=") || true) ? parseUnsignedIntegerOrBindVariable() : null;
    }

    private final boolean parseSequenceNoCycleIf() {
        return parseKeywordIf("NO CYCLE", "NOCYCLE");
    }

    private final boolean parseSequenceNoMaxvalueIf() {
        return parseKeywordIf("NO MAXVALUE", "NOMAXVALUE");
    }

    private final Field<? extends Number> parseSequenceMaxvalueIf() {
        return parseKeywordIf("MAXVALUE") && (parseIf("=") || true) ? parseSignedIntegerOrBindVariable() : null;
    }

    private final boolean parseSequenceNoMinvalueIf() {
        return parseKeywordIf("NO MINVALUE", "NOMINVALUE");
    }

    private final Field<? extends Number> parseSequenceMinvalueIf() {
        return parseKeywordIf("MINVALUE") && (parseIf("=") || true) ? parseSignedIntegerOrBindVariable() : null;
    }

    private final Field<? extends Number> parseSequenceIncrementByIf() {
        return parseKeywordIf("INCREMENT") && (parseKeywordIf("BY") || parseIf("=") || true) ? parseSignedIntegerOrBindVariable() : null;
    }

    private final Field<? extends Number> parseSequenceStartWithIf() {
        return parseKeywordIf("START") && (parseKeywordIf("WITH") || parseIf("=") || true) ? parseSignedIntegerOrBindVariable() : null;
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




















    private final DDLQuery parseCreateTable(TableScope tableScope) {
        boolean ifNotExists = parseKeywordIf("IF NOT EXISTS");
        Table<?> tableName = DSL.table(parseTableName().getQualifiedName());














        if (parseKeywordIf("USING"))
            parseIdentifier();

        CreateTableOnCommitStep onCommitStep;
        CreateTableCommentStep commentStep;

        List<Field<?>> fields = new ArrayList<>();
        List<Constraint> constraints = new ArrayList<>();
        List<Index> indexes = new ArrayList<>();
        boolean primary = false;
        boolean identity = false;
        boolean hidden = false;
        boolean readonly = false;
        boolean ctas = false;

        if (!peekSelectOrWith(true) && parseIf('(')) {

            columnLoop:
            do {
                int p = position();

                ConstraintTypeStep constraint = parseConstraintNameSpecificationIf();

                if (parsePrimaryKeyClusteredNonClusteredKeywordIf()) {
                    if (primary)
                        throw exception("Duplicate primary key specification");

                    primary = true;
                    PrimaryKeySpecification pk = parsePrimaryKeySpecification(constraint, true);
                    constraints.add(pk.constraint());
                    if (pk.identity()) {
                        PrimaryKey c = (PrimaryKey) pk.constraint();

                        replacement:
                        if (c.$fields().size() == 1) {
                            for (int i = 0; i < fields.size(); i++) {
                                Field<?> f = fields.get(i);

                                if (f.getName().equalsIgnoreCase(c.$fields().get(0).getName())) {
                                    fields.set(i, field(f.getQualifiedName(), f.getDataType().identity(true)));
                                    break replacement;
                                }
                            }

                            throw expected("Column not found: " + c.$fields().get(0).getName());
                        }
                        else
                            throw expected("Single column primary key with inline identity");
                    }

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

                    // [#7348] [#7651] [#9132] [#12712]
                    // Look ahead if the next tokens indicate a MySQL index definition
                    if (parseIf('(') || (
                            parseDataTypeIf(false) == null
                         && parseIdentifierIf() != null
                         && parseUsingIndexTypeIf()
                         && parseIf('('))
                    ) {
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
                boolean skipType = peek(',') || peek(')');

                // If only we had multiple return values or destructuring...
                ParseInlineConstraints inlineConstraints = parseInlineConstraints(
                    fieldName,
                    !skipType ? parseDataType() : SQLDataType.OTHER,
                    constraints,
                    primary,
                    identity,
                    hidden,
                    readonly
                );

                primary = inlineConstraints.primary;
                identity = inlineConstraints.identity;

                fields.add(field(fieldName, inlineConstraints.type, inlineConstraints.fieldComment));
            }
            while (parseIf(','));

            if (fields.isEmpty())
                throw expected("At least one column");

            parse(')');
        }
        else
            ctas = true;

        CreateTableElementListStep elementListStep = ifNotExists
            ? tableScope == TableScope.GLOBAL_TEMPORARY
                ? dsl.createGlobalTemporaryTableIfNotExists(tableName)
                : tableScope == TableScope.LOCAL_TEMPORARY
                ? dsl.createLocalTemporaryTableIfNotExists(tableName)
                : tableScope == TableScope.TEMPORARY
                ? dsl.createTemporaryTableIfNotExists(tableName)
                : dsl.createTableIfNotExists(tableName)
            : tableScope == TableScope.GLOBAL_TEMPORARY
                ? dsl.createGlobalTemporaryTable(tableName)
                : tableScope == TableScope.LOCAL_TEMPORARY
                ? dsl.createLocalTemporaryTable(tableName)
                : tableScope == TableScope.TEMPORARY
                ? dsl.createTemporaryTable(tableName)
                : dsl.createTable(tableName);

        if (!fields.isEmpty())
            elementListStep = elementListStep.columns(fields);

        CreateTableElementListStep constraintStep = constraints.isEmpty()
            ? elementListStep
            : elementListStep.constraints(constraints);
        CreateTableAsStep asStep = indexes.isEmpty()
            ? constraintStep
            : constraintStep.indexes(indexes);

        // [#6133] Historically, the jOOQ API places the ON COMMIT clause after
        // the AS clause, which doesn't correspond to dialect implementations
        Function<CreateTableOnCommitStep, CreateTableCommentStep> onCommit;
        if (tableScope != null && parseKeywordIf("ON COMMIT")) {
            if (parseKeywordIf("DELETE ROWS"))
                onCommit = CreateTableOnCommitStep::onCommitDeleteRows;
            else if (parseKeywordIf("DROP"))
                onCommit = CreateTableOnCommitStep::onCommitDrop;
            else if (parseKeywordIf("PRESERVE ROWS"))
                onCommit = CreateTableOnCommitStep::onCommitPreserveRows;
            else
                throw unsupportedClause();
        }
        else
            onCommit = s -> s;

        // [#12888] To avoid ambiguities with T-SQL's support for statement batches
        //          without statement separators, let's accept MySQL's optional AS
        //          keyword only for empty field lists
        if (parseKeywordIf("AS") || fields.isEmpty() && peekSelectOrWith(true)) {
            boolean previousMetaLookupsForceIgnore = metaLookupsForceIgnore();
            CreateTableWithDataStep withDataStep = asStep.as((Select<Record>) metaLookupsForceIgnore(false).parseQuery(true, true));
            metaLookupsForceIgnore(previousMetaLookupsForceIgnore);
            onCommitStep =
                  parseKeywordIf("WITH DATA")
                ? withDataStep.withData()
                : parseKeywordIf("WITH NO DATA")
                ? withDataStep.withNoData()
                : withDataStep;
        }
        else if (ctas) {
            throw expected("AS, WITH, SELECT, or (");
        }
        else {
            onCommitStep = asStep;

            // [#14631] [#14690] SQLite optional keywords
            if (parseKeywordIf("STRICT", "WITHOUT ROWID") && parseIf(','))
                parseKeyword("STRICT", "WITHOUT ROWID");
        }

        commentStep = onCommit.apply(onCommitStep);

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
                parseEqualOrIsIf();
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

        CreateTableStorageStep storageStep = comment != null
            ? commentStep.comment(comment)
            : commentStep;

        if (storage.size() > 0)
            return storageStep.storage(new SQLConcatenationImpl(storage.toArray(EMPTY_QUERYPART)));
        else
            return storageStep;
    }

    private final boolean parseEqualOrIsIf() {
        if (!parseIf('='))
            parseKeywordIf("IS");

        return true;
    }

    private static final record ParseInlineConstraints(DataType<?> type, Comment fieldComment, boolean primary, boolean identity, boolean hidden, boolean readonly) {}

    private final ParseInlineConstraints parseInlineConstraints(
        Name fieldName,
        DataType<?> type,
        List<? super Constraint> constraints,
        boolean primary,
        boolean identity,
        boolean hidden,
        boolean readonly
    ) {
        boolean nullable = false;
        boolean defaultValue = false;
        boolean computed = false;
        boolean onUpdate = false;
        boolean unique = false;
        Constraint uniqueConstraint = null;
        boolean comment = false;
        boolean compress = false;
        boolean sparse = false;
        Comment fieldComment = null;

        identity |= type.identity();
        hidden |= type.hidden();
        readonly |= type.readonly();

        for (;;) {
            ConstraintTypeStep inlineConstraint = parseConstraintNameSpecificationIf();

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
                else if (parseKeywordIf("NOT HIDDEN", "VISIBLE")) {
                    continue;
                }
                else if (parseKeywordIf("IMPLICITLY HIDDEN", "HIDDEN", "NOT VISIBLE", "INVISIBLE")) {
                    type = type.hidden(true);
                    continue;
                }
                else if (parseProKeywordIf("READONLY")) {




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
                else if (!computed
                        && !ignoreProEdition()
                        && (parseKeywordIf("AS")
                            || parseKeywordIf("COMPUTED") && (parseKeywordIf("BY") || true)
                            || parseKeywordIf("COMPUTE"))
                        && requireProEdition()) {






                }
                else if (!computed && parseProKeywordIf("ALIAS")) {






                }
                else if (!computed && parseProKeywordIf("MATERIALIZED")) {






                }
                else if ((!identity || !computed) && parseKeywordIf("GENERATED")) {
                    boolean always;
                    if (!(always = parseKeywordIf("ALWAYS"))) {
                        parseKeyword("BY DEFAULT");

                        // TODO: Ignored keyword from Oracle
                        parseKeywordIf("ON NULL");
                    }

                    if (always ? parseKeywordIf("AS IDENTITY") : parseKeyword("AS IDENTITY")) {
                        parseIdentityOptionIf();
                        type = type.identity(true);
                        identity = true;
                    }
                    else if (!ignoreProEdition() && parseKeyword("AS") && requireProEdition()) {





                    }

                    defaultValue = true;
                    continue;
                }
            }

            if (!primary && parsePrimaryKeyClusteredNonClusteredKeywordIf()) {
                constraints.add(parseConstraintEnforcementIf(inlineConstraint == null
                    ? primaryKey(fieldName)
                    : inlineConstraint.primaryKey(fieldName)));

                parseUniqueIndexStorageClausesIf();

                // [#13880] Remove all lexically preceding inline UNIQUE KEYs as
                //          soon as a PRIMARY KEY is encountered
                if (uniqueConstraint != null)
                    constraints.remove(uniqueConstraint);

                primary = true;
                unique = true;
                continue;
            }
            else if (parseKeywordIf("UNIQUE")) {
                if (!parseKeywordIf("KEY"))
                    parseKeywordIf("INDEX");

                parseUniqueIndexStorageClausesIf();

                if (!unique)
                    constraints.add(uniqueConstraint = parseConstraintEnforcementIf(inlineConstraint == null
                        ? unique(fieldName)
                        : inlineConstraint.unique(fieldName)));

                unique = true;
                continue;
            }

            if (parseKeywordIf("CHECK")) {
                constraints.add(parseCheckSpecification(inlineConstraint));
                continue;
            }

            if (parseKeywordIf("FOREIGN KEY REFERENCES", "REFERENCES")) {
                constraints.add(parseForeignKeyReferenceSpecification(inlineConstraint, new Field[] { field(fieldName) }));
                continue;
            }

            if (inlineConstraint != null)
                throw expected("CHECK", "DEFAULT", "NOT NULL", "NULL", "PRIMARY KEY", "REFERENCES", "UNIQUE");

            if (!onUpdate) {
                if (parseKeywordIf("ON UPDATE")) {

                    // [#6132] TODO: Support this feature in the jOOQ DDL API
                    parseConcat();
                    onUpdate = true;
                    continue;
                }
            }

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
                    parseEqualOrIsIf();
                    fieldComment = parseComment();
                    comment = true;
                    continue;
                }
                else if (peekKeyword("OPTIONS")) {
                    fieldComment = parseOptionsDescription();
                    comment = true;
                    continue;
                }
            }


            if (!compress) {
                if (parseProKeywordIf("NO COMPRESS")) {




                }
                else if (parseProKeywordIf("COMPRESS")) {











                }
            }

            if (!sparse) {
                if (parseProKeywordIf("SPARSE")) {



                }
            }

            break;
        }

        return new ParseInlineConstraints(type, fieldComment, primary, identity, hidden, readonly);
    }

















    private final void parseIdentityOptionIf() {

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
                else if (parseSignedIntegerLiteralIf0(true) != null) {
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

        if (!parseKeywordIf("ASC"))
            parseKeywordIf("DESC");

        return true;
    }

    private final boolean parseUniqueIndexStorageClausesIf() {




















































        return true;
    }




















    private final DDLQuery parseCreateType() {
        boolean ifNotExists = parseKeywordIf("IF NOT EXISTS");
        Name name = parseName();

        if (parseKeywordIf("AS")) {
            if (parseKeywordIf("ENUM")) {
                List<String> values;
                parse('(');

                if (!parseIf(')')) {
                    values = parseList(',', ParseContext::parseStringLiteral);
                    parse(')');
                }
                else
                    values = new ArrayList<>();

                return (ifNotExists ? dsl.createTypeIfNotExists(name) : dsl.createType(name))
                    .asEnum(values.toArray(EMPTY_STRING));
            }
            else {
                parseKeywordIf("OBJECT", "STRUCT");
                parse('(');
                List<Field<?>> fields = parseList(',', ctx -> DSL.field(parseIdentifier(), parseDataType()));
                parse(')');
                return (ifNotExists ? dsl.createTypeIfNotExists(name) : dsl.createType(name))
                    .as(fields);
            }
        }
        else if (parseKeywordIf("FROM")) {
            return (ifNotExists ? dsl.createDomainIfNotExists(name) : dsl.createDomain(name))
                .as(parseDataType());
        }
        else
            throw expected("AS", "FROM");
    }

    private final Index parseIndexSpecification(Table<?> table) {
        Name name = parseIdentifierIf();
        parseUsingIndexTypeIf();
        return Internal.createIndex(name == null ? NO_NAME : name, table, parseParenthesisedSortSpecification(false).fields(), false);
    }

    private final boolean parseConstraintConflictClauseIf() {
        return parseKeywordIf("ON CONFLICT") && parseKeyword("ROLLBACK", "ABORT", "FAIL", "IGNORE", "REPLACE");
    }

    private final Constraint parseConstraintEnforcementIf(ConstraintEnforcementStep e) {
        boolean onConflict = false;
        boolean deferrable = false;
        boolean initially = false;

        while ((!onConflict && (onConflict = parseConstraintConflictClauseIf()))
            || (!deferrable && (deferrable = parseConstraintDeferrableIf()))
            || (!initially && (initially = parseConstraintInitiallyIf())))
            ;

        if ((parseKeywordIf("ENABLE", "ENFORCED")))
            return e.enforced();
        else if ((parseKeywordIf("DISABLE", "NOT ENFORCED")))
            return e.notEnforced();
        else
            return e;
    }

    private final boolean parseConstraintDeferrableIf() {
        return parseKeywordIf("DEFERRABLE", "NOT DEFERRABLE");
    }

    private final boolean parseConstraintInitiallyIf() {
        return parseKeywordIf("INITIALLY") && parseKeyword("DEFERRED", "IMMEDIATE");
    }

    private static final record PrimaryKeySpecification(Constraint constraint, boolean identity) {}

    private final PrimaryKeySpecification parsePrimaryKeySpecification(ConstraintTypeStep constraint, boolean allowIdentity) {
        parseUsingIndexTypeIf();
        KeyColumnList k = parseKeyColumnList(allowIdentity);

        ConstraintEnforcementStep e = constraint == null
            ? primaryKey(k.fields())
            : constraint.primaryKey(k.fields());

        parseUsingIndexTypeIf();
        parseUniqueIndexStorageClausesIf();
        return new PrimaryKeySpecification(parseConstraintEnforcementIf(e), k.identity());
    }

    private final Constraint parseUniqueSpecification(ConstraintTypeStep constraint) {
        parseUsingIndexTypeIf();

        // [#9246] In MySQL, there's a syntax where the unique constraint looks like an index:
        //         ALTER TABLE t ADD UNIQUE INDEX i (c)
        Name constraintName;
        if (constraint == null && (constraintName = parseIdentifierIf()) != null)
            constraint = constraint(constraintName);

        Field<?>[] fieldNames = parseKeyColumnList(false).fields();

        ConstraintEnforcementStep e = constraint == null
            ? unique(fieldNames)
            : constraint.unique(fieldNames);

        parseUsingIndexTypeIf();
        parseUniqueIndexStorageClausesIf();
        return parseConstraintEnforcementIf(e);
    }

    private static final record KeyColumnList(Field<?>[] fields, boolean identity) {}

    private final KeyColumnList parseKeyColumnList(boolean allowIdentity) {
        SortSpecification s = parseParenthesisedSortSpecification(allowIdentity);
        Field<?>[] fieldNames = new Field[s.fields().length];

        for (int i = 0; i < s.fields().length; i++)
            if (s.fields()[i].$sortOrder() != SortOrder.DESC)
                fieldNames[i] = s.fields()[i].$field();

            // [#7899] TODO: Support this in jOOQ
            else
                throw notImplemented("DESC sorting in constraints");

        return new KeyColumnList(fieldNames, s.identity());
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
        Name constraintName;
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

    private final <O, S extends QueryPart> S parseIfExists(
        Supplier<? extends O> part,
        Function<? super O, ? extends S> stepIfExists,
        Function<? super O, ? extends S> step
    ) {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        O q = part.get();
        ifExists = ifExists || parseKeywordIf("IF EXISTS");

        return ifExists ? stepIfExists.apply(q) : step.apply(q);
    }

    private final <S2 extends QueryPart, S1 extends S2> S2 parseCascadeRestrictIf(
        S1 step,
        Function<? super S1, ? extends S2> stepCascade,
        Function<? super S1, ? extends S2> stepRestrict
    ) {
        return parseCascadeRestrictIf(step, stepCascade, stepRestrict, false);
    }

    private final <S2 extends QueryPart, S1 extends S2> S2 parseCascadeRestrictIf(
        S1 step,
        Function<? super S1, ? extends S2> stepCascade,
        Function<? super S1, ? extends S2> stepRestrict,
        boolean cascadeConstraints
    ) {
        boolean cascade = parseKeywordIf("CASCADE") && (cascadeConstraints && parseKeywordIf("CONSTRAINTS") || true);
        boolean restrict = !cascade && parseKeywordIf("RESTRICT");

        return cascade
            ? stepCascade.apply(step)
            : restrict
            ? stepRestrict.apply(step)
            : step;
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
                    return parseAlterTableAdd(s1, tableName, false);
                else if (parseKeywordIf("ALTER"))
                    if (parseKeywordIf("CONSTRAINT"))
                        return parseAlterTableAlterConstraint(s1);
                    else if ((parseKeywordIf("COLUMN") || true))
                        return parseAlterTableAlterColumn(tableName, s1);

                break;

            case 'C':

                // TODO: support all of the storageLoop from the CREATE TABLE statement
                if (parseKeywordIf("CHANGE")) {
                    parseKeywordIf("COLUMN");
                    return parseAlterTableChangeColumn(s1);
                }
                else if (parseKeywordIf("COMMENT")) {
                    if (parseKeywordIf("COLUMN")) {
                        return dsl.commentOnColumn(tableName.getQualifiedName().append(parseIdentifier()))
                                  .is(parseStringLiteral());
                    }
                    else {
                        parseEqualOrIsIf();
                        return dsl.commentOnTable(tableName).is(parseStringLiteral());
                    }
                }

                break;

            case 'D':
                if (parseKeywordIf("DROP")) {
                    if (parseKeywordIf("CONSTRAINT")) {
                        return parseCascadeRestrictIf(
                            parseIfExists(this::parseIdentifier, s1::dropConstraintIfExists, s1::dropConstraint),
                            AlterTableDropStep::cascade,
                            AlterTableDropStep::restrict
                        );
                    }
                    else if (parseKeywordIf("UNIQUE")) {
                        return parseCascadeRestrictIf(
                            s1.dropUnique(
                                  peek('(')
                                ? unique(parseKeyColumnList(false).fields())
                                : constraint(parseIdentifier())
                            ),
                            AlterTableDropStep::cascade,
                            AlterTableDropStep::restrict
                        );
                    }
                    else if (parseKeywordIf("PRIMARY KEY")) {
                        Name identifier = !peekKeyword("CASCADE", "RESTRICT") ? parseIdentifierIf() : null;
                        return parseCascadeRestrictIf(
                            identifier == null ? s1.dropPrimaryKey() : s1.dropPrimaryKey(identifier),
                            AlterTableDropStep::cascade,
                            AlterTableDropStep::restrict
                        );
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
                            while (parseIf(',')
                                && (parseKeywordIf("DROP") || true) && (parseKeywordIf("COLUMN") || true)
                                || parseKeywordIf("DROP") && (parseKeywordIf("COLUMN") || true)
                            ) {
                                if (fields == null) {
                                    fields = new ArrayList<>();
                                    fields.add(field);
                                }

                                fields.add(parseFieldName());
                            }
                        }

                        if (parens)
                            parse(')');

                        return parseCascadeRestrictIf(
                            fields == null
                                ? ifColumnExists
                                    ? s1.dropColumnIfExists(field)
                                    : s1.dropColumn(field)
                                : s1.dropColumns(fields),
                            AlterTableDropStep::cascade,
                            AlterTableDropStep::restrict
                        );
                    }
                }

                break;

            case 'M':
                if (parseKeywordIf("MODIFY"))
                    if (parseKeywordIf("CONSTRAINT"))
                        return parseAlterTableAlterConstraint(s1);
                    else if (parseKeywordIf("COMMENT"))
                        return s1.comment(parseComment());
                    else if ((parseKeywordIf("COLUMN") || true))
                        return parseAlterTableAlterColumn(tableName, s1);

                break;

            case 'O':
                if (parseKeywordIf("OWNER TO") && parseUser() != null)
                    return IGNORE.get();

                break;

            case 'R':
                if (parseKeywordIf("RENAME")) {
                    if (parseKeywordIf("AS", "TO")) {
                        Table<?> newName = parseTableName();

                        return s1.renameTo(newName);
                    }
                    else if (parseKeywordIf("COLUMN")) {
                        boolean ifExists = parseKeywordIf("IF EXISTS");
                        Name oldName = parseIdentifier();
                        parseKeyword("AS", "TO");
                        Name newName = parseIdentifier();

                        return (ifExists ? s1.renameColumnIfExists(oldName) : s1.renameColumn(oldName)).to(newName);
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

            case 'W':
                if (parseKeywordIf("WITH CHECK ADD"))
                    return parseAlterTableAdd(s1, tableName, true);

                break;
        }

        throw expected("ADD", "ALTER", "CHANGE", "COMMENT", "DROP", "MODIFY", "OWNER TO", "RENAME", "SET", "WITH");
    }

    private final DDLQuery parseAlterTableAdd(AlterTableStep s1, Table<?> tableName, boolean constraintOnly) {
        List<TableElement> list = new ArrayList<>();

        if (!constraintOnly && parseIndexOrKeyIf()) {
            Name name = parseIdentifierIf();

            DDLQuery result = name == null
                ? dsl.createIndex().on(tableName, parseParenthesisedSortSpecification(false).fields())
                : dsl.createIndex(name).on(tableName, parseParenthesisedSortSpecification(false).fields());

            if (parseKeywordIf("TYPE"))
                parseIdentifier();

            return result;
        }

        if (parseIf('(')) {
            do
                parseAlterTableAddFieldsOrConstraints(list, constraintOnly);
            while (parseIf(','));

            parse(')');
        }
        else if (!constraintOnly && (
                 parseKeywordIf("COLUMN IF NOT EXISTS")
              || parseKeywordIf("IF NOT EXISTS"))) {
            return parseAlterTableAddFieldFirstBeforeLast(s1.addColumnIfNotExists(parseAlterTableAddField(null)));
        }
        else {
            do
                parseAlterTableAddFieldsOrConstraints(list, constraintOnly);
            while (
                parseKeywordIf("ADD") ||
                parseIf(',') && (parseKeywordIf("ADD") || !peekKeyword("ALTER", "COMMENT", "DROP", "MODIFY", "OWNER TO", "RENAME"))
            );
        }

        if (list.size() == 1)
            if (list.get(0) instanceof Constraint c)
                return s1.add(c);
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

    private final void parseAlterTableAddFieldsOrConstraints(List<TableElement> list, boolean constraintOnly) {
        ConstraintTypeStep constraint = parseConstraintNameSpecificationIf();

        if (parsePrimaryKeyClusteredNonClusteredKeywordIf())
            list.add(parsePrimaryKeySpecification(constraint, false).constraint());
        else if (parseKeywordIf("UNIQUE") && (parseKeywordIf("KEY", "INDEX") || true))
            list.add(parseUniqueSpecification(constraint));
        else if (parseKeywordIf("FOREIGN KEY"))
            list.add(parseForeignKeySpecification(constraint));
        else if (parseKeywordIf("CHECK"))
            list.add(parseCheckSpecification(constraint));
        else if (constraint != null)
            throw expected("CHECK", "FOREIGN KEY", "PRIMARY KEY", "UNIQUE");
        else if (!constraintOnly && (parseKeywordIf("COLUMN") || true))
            parseAlterTableAddField(list);
    }

    private final ConstraintTypeStep parseConstraintNameSpecificationIf() {
        if (parseKeywordIf("CONSTRAINT") && !peekKeyword("PRIMARY KEY", "UNIQUE", "FOREIGN KEY", "CHECK"))
            return constraint(parseIdentifier());

        return null;
    }

    private final Field<?> parseAlterTableAddField(List<TableElement> list) {

        // The below code is taken from CREATE TABLE, with minor modifications as
        // https://github.com/jOOQ/jOOQ/issues/5317 has not yet been implemented
        // Once implemented, we might be able to factor out the common logic into
        // a new parseXXX() method.

        Name fieldName = parseIdentifier();
        DataType type = parseDataTypeIf(true);
        if (type == null)
            type = SQLDataType.OTHER;

        int p = list == null ? -1 : list.size();

        ParseInlineConstraints inline = parseInlineConstraints(fieldName, type, list, false, false, false, false);
        Field<?> result = field(fieldName, inline.type, inline.fieldComment);

        if (list != null)
            list.add(p, result);

        return result;
    }

    private final DDLQuery parseAlterTableAlterColumn(Table<?> table, AlterTableStep s1) {
        boolean paren = parseIf('(');

        // [#5316] TODO: Support this also for non-renames
        boolean ifExists = !paren && parseKeywordIf("IF EXISTS");
        TableField<?, ?> field = parseFieldName();

        if (!paren)
            if (parseKeywordIf("CONSTRAINT") && parseIdentifier() != null)
                if (parseKeywordIf("NULL"))
                    return (ifExists ? s1.alterIfExists(field) : s1.alter(field)).dropNotNull();
                else if (parseNotNullOptionalEnable())
                    return (ifExists ? s1.alterIfExists(field) : s1.alter(field)).setNotNull();
                else
                    throw expected("NOT NULL", "NULL");
            else if (parseKeywordIf("DROP NOT NULL", "SET NULL", "NULL"))
                return (ifExists ? s1.alterIfExists(field) : s1.alter(field)).dropNotNull();
            else if (parseKeywordIf("DROP DEFAULT"))
                return (ifExists ? s1.alterIfExists(field) : s1.alter(field)).dropDefault();
            else if (parseKeywordIf("DROP IDENTITY"))
                return (ifExists ? s1.alterIfExists(field) : s1.alter(field)).dropIdentity();
            else if (parseKeywordIf("SET NOT NULL") || parseNotNullOptionalEnable())
                return (ifExists ? s1.alterIfExists(field) : s1.alter(field)).setNotNull();
            else if (parseKeywordIf("SET DEFAULT", "DEFAULT"))
                return (ifExists ? s1.alterIfExists(field) : s1.alter(field)).default_((Field) toField(parseConcat()));
            else if (parseKeywordIf(
                    "SET GENERATED BY DEFAULT AS IDENTITY",
                    "SET GENERATED BY DEFAULT",
                    "ADD GENERATED BY DEFAULT AS IDENTITY"))
                return (ifExists ? s1.alterIfExists(field) : s1.alter(field)).setGeneratedByDefaultAsIdentity();
            else if (parseKeywordIf(
                    "SET GENERATED ALWAYS",
                    "ADD GENERATED ALWAYS AS IDENTITY"))
                throw notImplemented("GENERATED ALWAYS AS IDENTITY", "https://github.com/jOOQ/jOOQ/issues/15952");
            else if (peekKeyword("SET OPTIONS") && parseKeywordIf("SET"))
                return dsl.commentOnColumn(field(table.getQualifiedName().append(field.getUnqualifiedName()))).is(parseOptionsDescription());
            else if (parseKeywordIf("TO", "RENAME TO", "RENAME AS"))
                return (ifExists ? s1.renameColumnIfExists(field) : s1.renameColumn(field)).to(parseFieldName());
            else if (parseKeywordIf("TYPE", "SET DATA TYPE"))
                ;

        DataType<?> type = parseDataType();

        if (parseKeywordIf("NULL"))
            type = type.nullable(true);
        else if (parseNotNullOptionalEnable())
            type = type.nullable(false);

        if (paren)
            parse(')');

        return (ifExists ? s1.alterIfExists(field) : s1.alter(field)).set(type);
    }

    private final DDLQuery parseAlterTableChangeColumn(AlterTableStep s1) {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        TableField<?, ?> oldName = parseFieldName();
        TableField<?, ?> newName = parseFieldName();
        DataType<?> type = parseDataType();

        if (parseKeywordIf("NULL"))
            type = type.nullable(true);
        else if (parseNotNullOptionalEnable())
            type = type.nullable(false);

        return (ifExists ? s1.changeIfExists(oldName, newName) : s1.change(oldName, newName)).set(type);
    }

    private final boolean parseNotNullOptionalEnable() {
        return parseKeywordIf("NOT NULL")
            && (parseKeywordIf("ENABLE") || true)
            && (parseConstraintConflictClauseIf() || true);
    }

    private final DDLQuery parseAlterTableAlterConstraint(AlterTableStep s1) {
        requireProEdition();










        throw expected("ENABLE", "ENFORCED", "DISABLE", "NOT ENFORCED");
    }

    private final DDLQuery parseAlterType() {
        AlterTypeStep s1 = parseKeywordIf("IF EXISTS")
            ? dsl.alterTypeIfExists(parseName())
            : dsl.alterType(parseName());

        if (parseKeywordIf("ADD VALUE"))
            return s1.addValue(parseStringLiteral());
        else if (parseKeywordIf("OWNER TO") && parseUser() != null)
            return IGNORE.get();
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
                    boolean ifExists = parseKeywordIf("IF EXISTS");
                    TableField<?, ?> oldName = parseFieldName();
                    parseKeyword("AS", "TO");
                    return ifExists
                        ? dsl.alterTableIfExists(oldName.getTable()).renameColumnIfExists(oldName).to(parseFieldName())
                        : dsl.alterTable(oldName.getTable()).renameColumn(oldName).to(parseFieldName());
                }

                break;

            case 'D':
                if (parseKeywordIf("DATABASE")) {
                    boolean ifExists = parseKeywordIf("IF EXISTS");
                    Catalog oldName = parseCatalogName();
                    parseKeyword("AS", "TO");
                    return ifExists
                        ? dsl.alterDatabaseIfExists(oldName).renameTo(parseCatalogName())
                        : dsl.alterDatabase(oldName).renameTo(parseCatalogName());
                }

                break;

            case 'I':
                if (parseKeywordIf("INDEX")) {
                    boolean ifExists = parseKeywordIf("IF EXISTS");
                    Name oldName = parseIndexName();
                    parseKeyword("AS", "TO");
                    return ifExists
                        ? dsl.alterIndexIfExists(oldName).renameTo(parseIndexName())
                        : dsl.alterIndex(oldName).renameTo(parseIndexName());
                }

                break;

            case 'S':
                if (parseKeywordIf("SCHEMA")) {
                    boolean ifExists = parseKeywordIf("IF EXISTS");
                    Schema oldName = parseSchemaName();
                    parseKeyword("AS", "TO");
                    return ifExists
                        ? dsl.alterSchemaIfExists(oldName).renameTo(parseSchemaName())
                        : dsl.alterSchema(oldName).renameTo(parseSchemaName());
                }
                else if (parseKeywordIf("SEQUENCE")) {
                    boolean ifExists = parseKeywordIf("IF EXISTS");
                    Sequence<?> oldName = parseSequenceName();
                    parseKeyword("AS", "TO");
                    return ifExists
                        ? dsl.alterSequenceIfExists(oldName).renameTo(parseSequenceName())
                        : dsl.alterSequence(oldName).renameTo(parseSequenceName());
                }

                break;

            case 'V':
                if (parseKeywordIf("VIEW")) {
                    boolean ifExists = parseKeywordIf("IF EXISTS");
                    Table<?> oldName = parseTableName();
                    parseKeyword("AS", "TO");
                    return ifExists
                        ? dsl.alterViewIfExists(oldName).renameTo(parseTableName())
                        : dsl.alterView(oldName).renameTo(parseTableName());
                }

                break;
        }

        // If all of the above fails, we can assume we're renaming a table.
        parseKeywordIf("TABLE");
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Table<?> oldName = parseTableName();
        parseKeyword("AS", "TO");
        return ifExists
            ? dsl.alterTableIfExists(oldName).renameTo(parseTableName())
            : dsl.alterTable(oldName).renameTo(parseTableName());
    }

    private final DDLQuery parseDropTable(TableScope tableScope) {
        boolean ifExists = parseKeywordIf("IF EXISTS");
        Table<?> tableName = parseTableName();
        ifExists = ifExists || parseKeywordIf("IF EXISTS");

        return parseCascadeRestrictIf(
            ifExists
                ? tableScope == TableScope.GLOBAL_TEMPORARY
                    ? dsl.dropGlobalTemporaryTableIfExists(tableName)
                    : tableScope == TableScope.LOCAL_TEMPORARY
                    ? dsl.dropLocalTemporaryTableIfExists(tableName)
                    : tableScope == TableScope.TEMPORARY
                    ? dsl.dropTemporaryTableIfExists(tableName)
                    : dsl.dropTableIfExists(tableName)
                : tableScope == TableScope.GLOBAL_TEMPORARY
                    ? dsl.dropGlobalTemporaryTable(tableName)
                    : tableScope == TableScope.LOCAL_TEMPORARY
                    ? dsl.dropLocalTemporaryTable(tableName)
                    : tableScope == TableScope.TEMPORARY
                    ? dsl.dropTemporaryTable(tableName)
                    : dsl.dropTable(tableName),
            DropTableStep::cascade,
            DropTableStep::restrict,
            true
        );
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
            ConstraintTypeStep constraint = parseConstraintNameSpecificationIf();

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
            ConstraintTypeStep constraint = parseConstraintNameSpecificationIf();

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
            if (parseKeywordIf("TO", "AS")) {
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
            return IGNORE.get();
        }
        else
            throw expected("ADD", "DROP", "RENAME", "SET", "OWNER TO");
    }





















    private final DDLQuery parseCreateDatabase() {
        boolean ifNotExists = parseKeywordIf("IF NOT EXISTS");
        Catalog catalogName = parseCatalogName();
        parseMySQLCreateDatabaseFlagsIf();

        return ifNotExists
            ? dsl.createDatabaseIfNotExists(catalogName)
            : dsl.createDatabase(catalogName);
    }

    private final void parseMySQLCreateDatabaseFlagsIf() {
        for (;;) {
            if (parseKeywordIf("DEFAULT CHARACTER SET", "CHARACTER SET") && (parseIf("=") || true))
                parseCharacterSet();
            else if (parseKeywordIf("DEFAULT COLLATE", "COLLATE") && (parseIf("=") || true))
                parseCollation();
            else if (parseKeywordIf("DEFAULT ENCRYPTION", "ENCRYPTION") && (parseIf("=") || true))
                parseCharacterLiteral();
            else
                break;
        }
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
            return IGNORE.get();
        else if (parseAlterDatabaseFlags(true))
            return IGNORE.get();
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
        return parseIfExists(this::parseCatalogName, dsl::dropDatabaseIfExists, dsl::dropDatabase);
    }

    private final DDLQuery parseCreateSchema() {
        boolean ifNotExists = parseKeywordIf("IF NOT EXISTS");
        boolean authorization = parseKeywordIf("AUTHORIZATION");
        Schema schemaName = parseSchemaName();

        if (!authorization && parseKeywordIf("AUTHORIZATION"))
            parseUser();

        parseMySQLCreateDatabaseFlagsIf();
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
            return IGNORE.get();
        else if (parseAlterDatabaseFlags(false))
            return IGNORE.get();
        else
            throw expected("OWNER TO", "RENAME TO");
    }

    private final DDLQuery parseCreateIndex(boolean unique) {
        boolean ifNotExists = parseKeywordIf("IF NOT EXISTS");
        Name indexName = parseIndexNameIf();
        parseUsingIndexTypeIf();
        SortField<?>[] fields = null;
        if (peek('('))
            fields = parseParenthesisedSortSpecification(false).fields();
        parseKeyword("ON");
        Table<?> tableName = parseTableName();
        parseUsingIndexTypeIf();
        if (fields == null)
            fields = parseParenthesisedSortSpecification(false).fields();
        parseUsingIndexTypeIf();
        if (parseKeywordIf("TYPE"))
            parseIdentifier();

        Name[] include = null;
        if (parseKeywordIf("INCLUDE", "COVERING", "STORING")) {
            parse('(');
            include = parseIdentifiers().toArray(EMPTY_NAME);
            parse(')');
        }

        parseKeywordIf("VISIBLE");
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

    private static final record SortSpecification(SortField<?>[] fields, boolean identity) {}

    private SortSpecification parseParenthesisedSortSpecification(boolean allowIdentity) {
        parse('(');
        SortField<?>[] fields = parseList(',', c -> c.parseSortField()).toArray(EMPTY_SORTFIELD);
        boolean identity = fields.length == 1 && allowIdentity && parseKeywordIf("AUTOINCREMENT", "AUTO_INCREMENT");
        parse(')');

        return new SortSpecification(fields, identity);
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
        QueryPart condition = parseXor();

        while (parseKeywordIf("OR"))
            condition = toCondition(condition).or(toCondition(parseXor()));

        return condition;
    }

    private final QueryPart parseXor() {
        QueryPart condition = parseAnd();

        while (parseKeywordIf("XOR"))
            condition = toCondition(condition).xor(toCondition(parseAnd()));

        return condition;
    }

    private final QueryPart parseAnd() {
        QueryPart condition = parseNot();

        while (!forbidden.contains(FK_AND) && parseKeywordIf("AND") || parseCategory() == SQLDialectCategory.MYSQL && parseIf("&&"))
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

        while (parseKeywordIf("NOT") || parseCategory() == SQLDialectCategory.MYSQL && parseIf('!'))
            not++;

        return not;
    }

    private final QueryPart parsePredicate() {
        int p1 = position();
        Condition condition;







        switch (characterUpper()) {
            case 'D':






                break;

            case 'E':
                if (parseFunctionNameIf("EQUAL_NULL"))
                    return parseEqualNull();
                else if (parseKeywordIf("EXISTS"))
                    return exists(parseParenthesised(c -> parseWithOrSelect()));

                break;

            case 'I':






                break;

            case 'J':
                if ((condition = parsePredicateJSONExistsIf()) != null)
                    return condition;

                break;

            case 'P':



















                break;

            case 'R':
                if (parseKeywordIf("REGEXP_LIKE"))
                    return parseFunctionArgs2(Field::likeRegex);

                break;

            case 'S':
                if (parseProFunctionNameIf("ST_CONTAINS", "SDO_CONTAINS")) {



                }
                else if (parseProFunctionNameIf("ST_COVEREDBY", "SDO_COVEREDBY")) {



                }
                else if (parseProFunctionNameIf("ST_COVERS", "SDO_COVERS")) {



                }
                else if (parseProFunctionNameIf("ST_CROSSES")) {



                }
                else if (parseProFunctionNameIf("ST_DISJOINT")) {



                }
                else if (parseProFunctionNameIf("ST_EQUALS", "SDO_EQUAL")) {



                }
                else if (parseProFunctionNameIf("ST_INTERSECTS")) {



                }
                else if (parseProFunctionNameIf("ST_ISCLOSED")) {



                }
                else if (parseProFunctionNameIf("ST_ISEMPTY")) {



                }
                else if (parseProFunctionNameIf("ST_ISRING")) {



                }
                else if (parseProFunctionNameIf("ST_ISSIMPLE")) {



                }
                else if (parseProFunctionNameIf("ST_ISVALID")) {



                }
                else if (parseProFunctionNameIf("ST_OVERLAPS", "SDO_OVERLAPS")) {



                }
                else if (parseProFunctionNameIf("ST_TOUCHES", "SDO_TOUCH")) {



                }
                else if (parseProFunctionNameIf("ST_WITHIN", "SDO_INSIDE")) {



                }

                break;

            case 'U':
                if (parseKeywordIf("UNIQUE"))
                    // javac can't infer this (?)
                    return unique(this.<Select<?>>parseParenthesised(c -> parseWithOrSelect()));







                break;

            case 'X':
                if ((condition = parsePredicateXMLExistsIf()) != null)
                    return condition;

                break;
        }

        boolean notOp = false;
        FieldOrRowOrSelect left = parseConcat();
        Field leftScalar = toField(left, false);
        Select leftSelect = left instanceof Select s ? s : null;
        int p2 = position();
        boolean not = parseKeywordIf("NOT");
        Comparator comp;
        TSQLOuterJoinComparator outer;


        if (!not && !ignoreProEdition() && ((outer = parseTSQLOuterJoinComparatorIf()) != null) && requireProEdition()) {
            Condition result = null;












            return result;
        }
        else if (!not && (comp = parseComparatorIf()) != null) {
            boolean all = parseKeywordIf("ALL");
            boolean any = !all && parseKeywordIf("ANY", "SOME");
            if (all || any)
                parse('(');

            // TODO equal degrees
            Condition result =
                  all
                ? leftScalar != null
                    ? peekSelectOrWith(true)
                        ? leftScalar.compare(comp, DSL.all(parseWithOrSelect(1)))
                        : leftScalar.compare(comp, DSL.all(parseList(',', c -> c.parseField()).toArray(EMPTY_FIELD)))

                    // TODO: Support quantifiers also for rows
                    : new RowSubqueryCondition((Row) left, DSL.all(parseWithOrSelect(((Row) left).size())), comp)

                : any
                ? leftScalar != null
                    ? peekSelectOrWith(true)
                        ? leftScalar.compare(comp, DSL.any(parseWithOrSelect(1)))
                        : leftScalar.compare(comp, DSL.any(parseList(',', c -> c.parseField()).toArray(EMPTY_FIELD)))

                    // TODO: Support quantifiers also for rows
                    : new RowSubqueryCondition((Row) left, DSL.any(parseWithOrSelect(((Row) left).size())), comp)

                : leftScalar != null
                    ? leftScalar.compare(comp, toField(parseConcat()))
                    : AbstractRow.compare((Row) left, comp, parseRow(((Row) left).size(), true));

            if (all || any)
                parse(')');

            return result;
        }
        else if (!not && parseKeywordIf("IS")) {
            not = parseKeywordIf("NOT");

            if (parseKeywordIf("NULL"))
                return not
                    ? leftScalar != null
                        ? leftScalar.isNotNull()
                        : leftSelect != null
                        ? leftSelect.isNotNull()
                        : ((Row) left).isNotNull()
                    : leftScalar != null
                        ? leftScalar.isNull()
                        : leftSelect != null
                        ? leftSelect.isNull()
                        : ((Row) left).isNull();
            else if (leftScalar != null && parseKeywordIf("JSON"))
                return not
                    ? leftScalar.isNotJson()
                    : leftScalar.isJson();
            else if (leftScalar != null && parseKeywordIf("DOCUMENT"))
                return not
                    ? leftScalar.isNotDocument()
                    : leftScalar.isDocument();

            not = parseKeywordIf("DISTINCT FROM") == not;
            if (leftScalar != null) {
                Field right = toField(parseConcat());
                return not ? leftScalar.isNotDistinctFrom(right) : leftScalar.isDistinctFrom(right);
            }
            else if (leftSelect != null) {
                throw notImplementedNonScalarSelectPredicate();
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
            if (leftScalar != null && !peek('(')) {
                result = not
                    ? leftScalar.notIn(parseConcat())
                    : leftScalar.in(parseConcat());
            }
            else {
                parse('(');

                if (leftScalar == null && leftSelect != null)
                    throw notImplementedNonScalarSelectPredicate();

                if (peek(')'))
                    result = not
                        ? leftScalar != null
                            ? leftScalar.notIn(EMPTY_FIELD)
                            : new RowInCondition((Row) left, new QueryPartList<>(), true)
                        : leftScalar != null
                            ? leftScalar.in(EMPTY_FIELD)
                            : new RowInCondition((Row) left, new QueryPartList<>(), false);
                else if (peekSelectOrWith(true))
                    result = not
                        ? leftScalar != null
                            ? leftScalar.notIn(parseWithOrSelect(1))
                            : new RowSubqueryCondition((Row) left, parseWithOrSelect(((Row) left).size()), NOT_IN)
                        : leftScalar != null
                            ? leftScalar.in(parseWithOrSelect(1))
                            : new RowSubqueryCondition((Row) left, parseWithOrSelect(((Row) left).size()), IN);
                else
                    result = not
                        ? leftScalar != null
                            ? leftScalar.notIn(parseList(',', c -> c.parseField()))
                            : new RowInCondition((Row) left, new QueryPartList<>(parseList(',', c -> parseRow(((Row) left).size()))), true)
                        : leftScalar != null
                            ? leftScalar.in(parseList(',', c -> c.parseField()))
                            : new RowInCondition((Row) left, new QueryPartList<>(parseList(',', c -> parseRow(((Row) left).size()))), false);

                parse(')');
            }

            return result;
        }
        else if (parseKeywordIf("BETWEEN")) {
            boolean symmetric = !parseKeywordIf("ASYMMETRIC") && parseKeywordIf("SYMMETRIC");

            if (leftScalar == null && leftSelect != null)
                throw notImplementedNonScalarSelectPredicate();

            FieldOrRowOrSelect r1 = leftScalar != null
                ? parseConcat()
                : parseRow(((Row) left).size());
            parseKeyword("AND");
            FieldOrRowOrSelect r2 = leftScalar != null
                ? parseConcat()
                : parseRow(((Row) left).size());

            return symmetric
                ? not
                    ? leftScalar != null
                        ? leftScalar.notBetweenSymmetric((Field) r1, (Field) r2)
                        : new RowBetweenCondition((Row) left, (Row) r1, not, symmetric, (Row) r2)
                    : leftScalar != null
                        ? leftScalar.betweenSymmetric((Field) r1, (Field) r2)
                        : new RowBetweenCondition((Row) left, (Row) r1, not, symmetric, (Row) r2)
                : not
                    ? leftScalar != null
                        ? leftScalar.notBetween((Field) r1, (Field) r2)
                        : new RowBetweenCondition((Row) left, (Row) r1, not, symmetric, (Row) r2)
                    : leftScalar != null
                        ? leftScalar.between((Field) r1, (Field) r2)
                        : new RowBetweenCondition((Row) left, (Row) r1, not, symmetric, (Row) r2);
        }
        else if (leftScalar != null && (parseKeywordIf("LIKE") || parseOperatorIf("~~") || (notOp = parseOperatorIf("!~~")))) {
            if (parseKeywordIf("ANY")) {
                parse('(');
                if (peekSelectOrWith(true)) {
                    Select<?> select = parseWithOrSelect();
                    parse(')');
                    if (binary(leftScalar))
                        return (not ^ notOp) ? leftScalar.notBinaryLike(any(select)) : leftScalar.binaryLike(any(select));
                    else
                        return parseEscapeClauseIf((not ^ notOp) ? leftScalar.notLike(any(select)) : leftScalar.like(any(select)));
                }
                else {
                    List<Field<?>> fields;
                    if (parseIf(')')) {
                        fields = emptyList();
                    }
                    else {
                        fields = parseList(',', c -> toField(parseConcat()));
                        parse(')');
                    }
                    if (binary(leftScalar))
                        return (not ^ notOp) ? leftScalar.notBinaryLike(any((Field<byte[]>[]) fields.toArray(EMPTY_FIELD))) : leftScalar.binaryLike(any((Field<byte[]>[]) fields.toArray(EMPTY_FIELD)));
                    else
                        return parseEscapeClauseIf((not ^ notOp) ? leftScalar.notLike(any((Field<String>[]) fields.toArray(EMPTY_FIELD))) : leftScalar.like(any((Field<String>[]) fields.toArray(EMPTY_FIELD))));
                }
            }
            else if (parseKeywordIf("ALL")) {
                parse('(');
                if (peekSelectOrWith(true)) {
                    Select<?> select = parseWithOrSelect();
                    parse(')');
                    if (binary(leftScalar))
                        return (not ^ notOp) ? leftScalar.notBinaryLike(all(select)) : leftScalar.binaryLike(all(select));
                    else
                        return parseEscapeClauseIf((not ^ notOp) ? leftScalar.notLike(all(select)) : leftScalar.like(all(select)));
                }
                else {
                    List<Field<?>> fields;
                    if (parseIf(')')) {
                        fields = emptyList();
                    }
                    else {
                        fields = parseList(',', c -> toField(parseConcat()));
                        parse(')');
                    }
                    if (binary(leftScalar))
                        return (not ^ notOp) ? leftScalar.notBinaryLike(all((Field<byte[]>[]) fields.toArray(EMPTY_FIELD))) : leftScalar.binaryLike(all((Field<byte[]>[]) fields.toArray(EMPTY_FIELD)));
                    else
                        return parseEscapeClauseIf((not ^ notOp) ? leftScalar.notLike(all((Field<String>[]) fields.toArray(EMPTY_FIELD))) : leftScalar.like(all((Field<String>[]) fields.toArray(EMPTY_FIELD))));
                }
            }
            else {
                Field right = toField(parseConcat());

                if (binary(leftScalar) || binary(right))
                    return (not ^ notOp) ? leftScalar.notBinaryLike(right) : leftScalar.binaryLike(right);
                else
                    return parseEscapeClauseIf((not ^ notOp) ? leftScalar.notLike(right) : leftScalar.like(right));
            }
        }
        else if (leftScalar != null && (parseKeywordIf("ILIKE") || parseOperatorIf("~~*") || (notOp = parseOperatorIf("!~~*")))) {
            Field right = toField(parseConcat());
            LikeEscapeStep like = (not ^ notOp) ? leftScalar.notLikeIgnoreCase(right) : leftScalar.likeIgnoreCase(right);
            return parseEscapeClauseIf(like);
        }
        else if (leftScalar != null && (parseKeywordIf("REGEXP")
                                        || parseKeywordIf("RLIKE")
                                        || parseKeywordIf("LIKE_REGEX")
                                        || parseOperatorIf("~")
                                        || (notOp = parseOperatorIf("!~")))) {
            Field right = toField(parseConcat());
            return (not ^ notOp)
                    ? leftScalar.notLikeRegex(right)
                    : leftScalar.likeRegex(right);
        }
        else if (leftScalar != null && parseKeywordIf("SIMILAR TO")) {
            Field right = toField(parseConcat());
            LikeEscapeStep like = not ? leftScalar.notSimilarTo(right) : leftScalar.similarTo(right);
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
            position(p2);
            return left;
        }
    }

    private final Condition parseEqualNull() {
        Condition result;

        parse('(');
        FieldOrRowOrSelect left = parseConcat();
        parse(',');

        Field f = toField(left, false);
        if (f != null)
            result = f.isNotDistinctFrom(toField(parseConcat()));
        else
            result = new RowIsDistinctFrom((Row) left, parseRow(((Row) left).size(), true), true);

        parse(')');
        return result;
    }

    private final Condition parsePredicateXMLExistsIf() {
        if (parseKeywordIf("XMLEXISTS")) {
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

        return null;
    }

    private final Condition parsePredicateJSONExistsIf() {
        if (parseKeywordIf("JSON_EXISTS")) {
            parse('(');
            Field json = parseField();
            parse(',');
            Field<String> path = (Field<String>) parseField();
            JSONExists.Behaviour b = parseJSONExistsOnErrorBehaviourIf();
            parse(')');












            return jsonExists(json, path);
        }

        return null;
    }

    private final QueryPart parseEscapeClauseIf(LikeEscapeStep like) {
        return parseKeywordIf("ESCAPE") ? like.escape(parseCharacterLiteral()) : like;
    }

    @Override
    public final Table<?> parseTable() {
        return parseJoinedTable(() -> peekKeyword(KEYWORD_LOOKUP_IN_SELECT_FROM) || (!delimiterRequired && peekKeyword(KEYWORD_LOOKUP_IN_STATEMENTS)));
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
            table instanceof Table<R> t
          ? t
          : dummyAlias
          ? table.asTable("x")
          : table.asTable();
    }

    private final Table<?> parseTableFactor(BooleanSupplier forbiddenKeywords) {

        // [#7982] Postpone turning Select into a Table in case there is an alias
        TableLike<?> result;






        // TODO ONLY ( table primary )
        if (parseFunctionNameIf("OLD TABLE")) {
            parse('(');
            Query query = parseQuery(false, false);
            parse(')');

            if (query instanceof Merge<?> q)
                result = oldTable(q);
            else if (query instanceof Update<?> q)
                result = oldTable(q);
            else if (query instanceof Delete<?> q)
                result = oldTable(q);
            else
                throw expected("UPDATE", "DELETE", "MERGE");
        }
        else if (parseFunctionNameIf("NEW TABLE")) {
            parse('(');
            Query query = parseQuery(false, false);
            parse(')');

            if (query instanceof Merge<?> q)
                result = newTable(q);
            else if (query instanceof Insert<?> q)
                result = newTable(q);
            else if (query instanceof Update<?> q)
                result = newTable(q);
            else
                throw expected("INSERT", "UPDATE", "MERGE");
        }
        else if (parseFunctionNameIf("FINAL TABLE")) {
            parse('(');
            Query query = parseQuery(false, false);
            parse(')');

            if (query instanceof Merge<?> q)
                result = finalTable(q);
            else if (query instanceof Insert<?> q)
                result = finalTable(q);
            else if (query instanceof Update<?> q)
                result = finalTable(q);
            else
                throw expected("INSERT", "UPDATE", "MERGE");
        }
        else if (parseFunctionNameIf("UNNEST", "TABLE")) {
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
                if (!f.getDataType().isArray())
                    f = f.coerce(f.getDataType().array());

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
        else if (parseFunctionNameIf("NUMBERS")) {
            parse('(');
            Field f1 = toField(parseConcat());
            Field f2 = parseIf(',') ? toField(parseConcat()) : null;
            parse(')');

            result = f2 == null
                ? generateSeries(zero(), isub(f1, one()))
                : generateSeries(f1, isub(iadd(f1, f2), one()));
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
        else if (parseFunctionNameIf("OPENJSON") && requireProEdition()) {
            result = null;





















        }
        else if (peekFunctionNameIf("VALUES")) {
            result = parseTableValueConstructor();
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

                // [#18543] We don't really know what the parentheses mean at this point, so
                //          after the fact, if we happen to have parsed what looks like a
                //          "derived table, we could still encounter more set operations or
                //          ORDER BY .. LIMIT clauses
                if (result instanceof Table<?> t) {
                    Select<?> s = Tools.extractSelectFromDerivedTable(t);

                    if (s != null)
                        result = parseSelect(null, null, selectQueryImpl(s));
                }

                parse(')');
            }
        }
        else if ((result = parseTemplateIf(DSL::table)) != null) {}
        else {
            result = parseTableName();

            // TODO Sample clause
        }

        if (parseProKeywordIf("VERSIONS BETWEEN")) {






























        }
        else if (!ignoreProEdition() && parseForPeriodIf() && requireProEdition()) {































        }
        else if (parseProKeywordIf("AS OF")) {










        }
        else if (parseKeywordIf("SAMPLE", "TABLESAMPLE", "USING SAMPLE")) {
            boolean bernoulli = parseKeywordIf("BERNOULLI", "ROW");
            boolean system = !bernoulli && parseKeywordIf("SYSTEM", "BLOCK");
            boolean reservoir = !bernoulli && !system && parseKeywordIf("RESERVOIR");

            parse('(');
            Field<Number> size = parseFieldUnsignedNumericLiteral(Sign.NONE);
            boolean percent = parseKeywordIf("PERCENT");
            boolean rows = !percent && parseKeywordIf("ROWS");
            parse(')');
            Field<Number> seed = null;

            if (parseKeywordIf("REPEATABLE", "SEED")) {
                parse('(');
                seed = parseFieldUnsignedNumericLiteral(Sign.NONE);
                parse(')');
            }

            TableSampleRowsStep<?> s1 =
                  bernoulli
                ? t(result).tablesampleBernoulli(size)
                : system
                ? t(result).tablesampleSystem(size)
                : t(result).tablesample(size);
            TableSampleRepeatableStep<?> s2 =
                  percent
                ? s1.percent()
                : rows
                ? s1.rows()
                : s1;
            result = seed != null ? s2.repeatable(seed) : s2;
        }

        if (parseKeywordIf("WITH ORDINALITY"))
            result = t(result).withOrdinality();

        if (parseProKeywordIf("PIVOT")) {















































        }

        // TODO UNPIVOT
        result = parseCorrelationNameIf(result, forbiddenKeywords);

        int p = position();
        if (!peekKeyword("WITH CHECK OPTION", "WITH READ ONLY") && parseKeywordIf("WITH")) {
            if (!ignoreProEdition() && parseIf('(') && requireProEdition()) {






            }

            // [#10164] Without parens, WITH is part of the next statement in delimiter free statement batches
            else
                position(p);
        }















        else {
            for (;;) {
                if (parseKeywordIf("USE KEY", "USE INDEX")) {
                    if (parseKeywordIf("FOR JOIN"))
                        result = t(result).useIndexForJoin(parseParenthesisedIdentifiers());
                    else if (parseKeywordIf("FOR ORDER BY"))
                        result = t(result).useIndexForOrderBy(parseParenthesisedIdentifiers());
                    else if (parseKeywordIf("FOR GROUP BY"))
                        result = t(result).useIndexForGroupBy(parseParenthesisedIdentifiers());
                    else
                        result = t(result).useIndex(parseParenthesisedIdentifiers());
                }
                else if (parseKeywordIf("FORCE KEY", "FORCE INDEX")) {
                    if (parseKeywordIf("FOR JOIN"))
                        result = t(result).forceIndexForJoin(parseParenthesisedIdentifiers());
                    else if (parseKeywordIf("FOR ORDER BY"))
                        result = t(result).forceIndexForOrderBy(parseParenthesisedIdentifiers());
                    else if (parseKeywordIf("FOR GROUP BY"))
                        result = t(result).forceIndexForGroupBy(parseParenthesisedIdentifiers());
                    else
                        result = t(result).forceIndex(parseParenthesisedIdentifiers());
                }
                else if (parseKeywordIf("IGNORE KEY", "IGNORE INDEX")) {
                    if (parseKeywordIf("FOR JOIN"))
                        result = t(result).ignoreIndexForJoin(parseParenthesisedIdentifiers());
                    else if (parseKeywordIf("FOR ORDER BY"))
                        result = t(result).ignoreIndexForOrderBy(parseParenthesisedIdentifiers());
                    else if (parseKeywordIf("FOR GROUP BY"))
                        result = t(result).ignoreIndexForGroupBy(parseParenthesisedIdentifiers());
                    else
                        result = t(result).ignoreIndex(parseParenthesisedIdentifiers());
                }
                else
                    break;
            }
        }

        return t(result);
    }

    private final boolean parseForPeriodIf() {
        return peekKeyword("FOR")
            && !peekKeyword("FOR JSON")
            && !peekKeyword("FOR KEY SHARE")
            && !peekKeyword("FOR NO KEY UPDATE")
            && !peekKeyword("FOR SHARE")
            && !peekKeyword("FOR UPDATE")
            && !peekKeyword("FOR XML")
            && parseKeyword("FOR");
    }

    private final String[] parseParenthesisedIdentifiers() {
        return parseParenthesised(c -> map(parseIdentifiers(), Name::last, String[]::new));
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
                result = t(result, true).as(alias, columnAliases);
            else
                result = t(result, true).as(alias);
        }

        return result;
    }






































































    private final Row parseTableValueConstructorRow(Integer degree) {
        if (parseKeywordIf("ROW"))
            return parseTuple(degree);

        Field<?> r = null;

        if (degree == null || degree == 1)
            r = parseScalarSubqueryIf();

        if (r != null)
            return row(r);
        else if (peek('('))
            return parseTuple(degree);
        else if (degree == null || degree == 1)
            return row(parseField());
        else
            throw exception("Expected row of degree: " + degree);
    }

    private final Table<?> parseTableValueConstructor() {
        parseKeyword("VALUES");

        List<Row> rows = new ArrayList<>();
        Integer degree = null;
        do {
            Row row = parseTableValueConstructorRow(degree);
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
            fieldsOrRows = parseList(',', c -> c.parseField());

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
        int p = position();
        if (parseProKeywordIf("PARTITION BY")) {








































        }

        Join join = parseJoinTypeIf();

        if (join == null)
            return null;

        Table<?> right = join.type.qualified() ? parseJoinedTable(forbiddenKeywords) : parseLateral(forbiddenKeywords);

        TableOptionalOnStep<?> s0;
        TablePartitionByStep<?> s1;
        TableOnStep<?> s2;
        s2 = s1 = (TablePartitionByStep<?>) (s0 = left.join(right, join.type, join.hint));

        switch (join.type) {
            case LEFT_OUTER_JOIN:
            case FULL_OUTER_JOIN:
            case RIGHT_OUTER_JOIN:
                if (parseProKeywordIf("PARTITION BY")) {





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
                else if (join.type == JOIN)
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
                if (parseKeywordIf("EXCEPT", "EXCLUDE")) {
                    parse('(');
                    result.add(DSL.asterisk().except(parseList(',', c -> parseFieldName()).toArray(EMPTY_FIELD)));
                    parse(')');
                }
                else
                    result.add(DSL.asterisk());
            }
            else if ((qa = parseQualifiedAsteriskIf()) != null) {
                if (parseKeywordIf("EXCEPT", "EXCLUDE")) {
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
                        alias = parseIdentifier(true, false);
                    else if (!peekKeyword(KEYWORD_LOOKUP_IN_SELECT) && (delimiterRequired || !peekKeyword(KEYWORD_LOOKUP_IN_STATEMENTS)))
                        alias = parseIdentifierIf(true, false);
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
            List<Field<?>> result = parseList(',', c -> c.parseField());
            parse(')');
            return result;
        }
    }

    private final List<Field<?>> parseFieldsOrEmptyOptionallyParenthesised(boolean allowUnparenthesisedLists) {
        if (peek('('))
            return parseFieldsOrEmptyParenthesised();




        else if (allowUnparenthesisedLists)
            return parseList(',', c -> c.parseField());
        else
            return asList(parseField());
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
                while (i < chars.length) {
                    if (peekTemplateComment(i)) {
                        position(p);
                        return null;
                    }

                    switch (chars[i]) {
                        case '*':
                            if (i + 1 < chars.length && chars[i + 1] == '/')
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
        else if (part instanceof Condition c)
            return c;
        else if (part instanceof Field f) {
            DataType dataType = f.getDataType();

            if (dataType.isBoolean())
                return condition(f);

            // [#11631] [#12394] Numeric expressions are booleans in MySQL
            else if (dataType.isNumeric())
                return f.ne(zero());

            // [#7266] Support parsing column references as predicates
            else if (dataType.isOther() && (part instanceof TableFieldImpl || part instanceof Val))
                return condition((Field) part);
            else if (dataType.isOther() && part instanceof SQLField)
                return condition(((SQLField) part).delegate);
            else
                throw expected("Boolean field");
        }
        else
            throw expected("Condition");
    }

    private final FieldOrRow toFieldOrRow(QueryPart part) {
        if (part == null)
            return null;
        else if (part instanceof Field<?> f)
            return f;
        else if (part instanceof Select<?> s)
            return DSL.field((Select) degreeCheck(1, s));
        else if (part instanceof Row r)
            return r;
        else
            throw expected("Field or row");
    }

    private final Field<?> toField(QueryPart part) {
        return toField(part, true);
    }

    private final Field<?> toField(QueryPart part, boolean throwIfNonScalar) {
        if (part == null)
            return null;
        else if (part instanceof Field<?> f)
            return f;
        else if (part instanceof Select s)
            if (degreeCheck(1, s, throwIfNonScalar) != null)
                return DSL.field(s);
            else
                return null;
        else if (throwIfNonScalar)
            throw expected("Field");
        else
            return null;
    }

    private final FieldOrRowOrSelect parseConcat() {
        FieldOrRowOrSelect r = parseCollated();

        Field f = toField(r, false);
        if (f != null)
            while (parseIf("||"))
                r = f = concatOperator(f, toField(parseCollated()));

        return r;
    }

    private final Field<?> concatOperator(Field<?> a1, Field<?> a2) {
        if (a1.getDataType().isArray() && a2.getDataType().isArray())
            return DSL.arrayConcat((Field) a1, (Field) a2);
        else if (a1.getDataType().isBinary() && a2.getDataType().isBinary())
            return DSL.binaryConcat((Field) a1, (Field) a2);
        else
            return DSL.concat(a1, a2);
    }

    private final FieldOrRowOrSelect parseCollated() {
        FieldOrRowOrSelect r = parseOp();

        Field f = toField(r, false);
        if (f != null) {
            if (parseKeywordIf("COLLATE"))
                r = f = f.collate(parseCollation());





































        }

        return r;
    }

    private final Field<?> parseFieldNumericOpParenthesised() {
        parse('(');
        Field<?> r = toField(parseOp());
        parse(')');

        return r;
    }

    private final Field<?> parseFieldParenthesised() {
        parse('(');
        Field<?> r = parseField();
        parse(')');

        return r;
    }

    private final <Q extends QueryPart> Q parseFunctionArgs1(Function1<? super Field, ? extends Q> finisher) {
        parse('(');
        Field<?> f1 = parseField();
        parse(')');

        return finisher.apply(f1);
    }

    private final <Q extends QueryPart> Q parseFunctionArgs2(
        Function1<? super Field, ? extends Q> finisher1,
        Function2<? super Field, ? super Field, ? extends Q> finisher2
    ) {
        parse('(');
        Field<?> f1 = parseField();
        Field<?> f2 = parseIf(',') ? parseField() : null;
        parse(')');

        return f2 == null ? finisher1.apply(f1) : finisher2.apply(f1, f2);
    }

    private final <Q extends QueryPart> Q parseFunctionArgs2(Function2<? super Field, ? super Field, ? extends Q> finisher) {
        return parseFunctionArgs2(this::parseField, finisher);
    }

    private final <Q extends QueryPart> Q parseFunctionArgs2(
        Supplier<? extends Field<?>> argument,
        Function2<? super Field, ? super Field, ? extends Q> finisher
    ) {
        parse('(');
        Field<?> f1 = argument.get();
        parse(',');
        Field<?> f2 = argument.get();
        parse(')');

        return finisher.apply(f1, f2);
    }

    private final <Q extends QueryPart> Q parseFunctionArgs3(
        Function2<? super Field, ? super Field, ? extends Q> finisher2,
        Function3<? super Field, ? super Field, ? super Field, ? extends Q> finisher3
    ) {
        parse('(');
        Field<?> f1 = parseField();
        parse(',');
        Field<?> f2 = parseField();
        Field<?> f3 = parseIf(',') ? parseField() : null;
        parse(')');

        return f3 == null ? finisher2.apply(f1, f2) : finisher3.apply(f1, f2, f3);
    }

    private final <Q extends QueryPart> Q parseFunctionArgs3(Function3<? super Field, ? super Field, ? super Field, ? extends Q> finisher) {
        parse('(');
        Field<?> f1 = parseField();
        parse(',');
        Field<?> f2 = parseField();
        parse(',');
        Field<?> f3 = parseField();
        parse(')');

        return finisher.apply(f1, f2, f3);
    }

    private final <Q extends QueryPart> Q parseFunctionArgs4(Function4<? super Field, ? super Field, ? super Field, ? super Field, ? extends Q> finisher) {
        parse('(');
        Field<?> f1 = parseField();
        parse(',');
        Field<?> f2 = parseField();
        parse(',');
        Field<?> f3 = parseField();
        parse(',');
        Field<?> f4 = parseField();
        parse(')');

        return finisher.apply(f1, f2, f3, f4);
    }

    private final boolean parseEmptyParens() {
        return parse('(') && parse(')');
    }

    private final boolean parseEmptyParensOr(Predicate<? super ParseContext> p) {
        return parse('(') && (parseIf(')') || p.test(this) && parse(')'));
    }

    private final boolean parseEmptyParensIf() {
        return parseIf('(') && parse(')') || true;
    }

    // Any numeric operator of low precedence
    // See https://www.postgresql.org/docs/current/sql-syntax-lexical.html#SQL-PRECEDENCE
    private final FieldOrRowOrSelect parseOp() {
        FieldOrRowOrSelect l = parseSum();

        Field f = toField(l, false);
        if (f != null)
            for (;;)
                if (parseIf("<<"))
                    l = f = f.shl(toField(parseSum()));
                else if (parseIf(">>"))
                    l = f = f.shr(toField(parseSum()));
                else if (parseIf("->>")) {
                    Field r = toField(parseSum());

                    // [#10018] We cannot really know reliably whether this is a
                    //          index or attribute access. Let's default to the
                    //          more popular attribute access for now. Also,
                    //          JSONB is likely more popular than JSON.
                    if (r.getDataType().isNumeric())
                        if (f.getDataType().getFromType() == JSON.class)
                            l = f = jsonGetElementAsText(f, r);
                        else
                            l = f = jsonbGetElementAsText(f, r);
                    else
                        if (f.getDataType().getFromType() == JSON.class)
                            l = f = jsonGetAttributeAsText(f, r);
                        else
                            l = f = jsonbGetAttributeAsText(f, r);
                }
                else if (parseIf("->")) {
                    Field r = toField(parseSum());

                    // [#10018] We cannot really know reliably whether this is a
                    //          index or attribute access. Let's default to the
                    //          more popular attribute access for now. Also,
                    //          JSONB is likely more popular than JSON.
                    if (r.getDataType().isNumeric())
                        if (f.getDataType().getFromType() == JSON.class)
                            l = f = jsonGetElement(f, r);
                        else
                            l = f = jsonbGetElement(f, r);
                    else
                        if (f.getDataType().getFromType() == JSON.class)
                            l = f = jsonGetAttribute(f, r);
                        else
                            l = f = jsonbGetAttribute(f, r);
                }
                else if (parseIf("??") || parseIf("?"))
                    if (f.getDataType().getFromType() == JSON.class)
                        return jsonKeyExists(f, (Field) toField(parseSum()));
                    else
                        return jsonbKeyExists(f, (Field) toField(parseSum()));
                else
                    break;

        return l;
    }

    private final FieldOrRowOrSelect parseSum() {
        FieldOrRowOrSelect r = parseFactor();

        Field f = toField(r, false);
        if (f != null)
            for (;;)
                if (parseIf('+'))
                    r = f = parseSumRightOperand(f, true);
                else if (!peek("->") && parseIf('-'))
                    r = f = parseSumRightOperand(f, false);
                else
                    break;

        return r;
    }

    private final Field parseSumRightOperand(FieldOrRowOrSelect r, boolean add) {
        Field rhs = toField(parseFactor());
        DatePart part;

        if (parseProKeywordIf("YEAR", "YEARS"))
            part = DatePart.YEAR;
        else if (parseProKeywordIf("MONTH", "MONTHS"))
            part = DatePart.MONTH;
        else if (parseProKeywordIf("DAY", "DAYS"))
            part = DatePart.DAY;
        else if (parseProKeywordIf("HOUR", "HOURS"))
            part = DatePart.HOUR;
        else if (parseProKeywordIf("MINUTE", "MINUTES"))
            part = DatePart.MINUTE;
        else if (parseProKeywordIf("SECOND", "SECONDS"))
            part = DatePart.SECOND;
        else
            part = null;

        Field lhs = toField(r);














            if (add)
                return lhs.add(rhs);
            else if (lhs.getDataType().isDate() && rhs.getDataType().isDate())
                return DSL.dateDiff(lhs, rhs);
            else if (lhs.getDataType().isTimestamp() && rhs.getDataType().isTimestamp())
                return DSL.timestampDiff(lhs, rhs);
            else
                return lhs.sub(rhs);
    }

    private final FieldOrRowOrSelect parseFactor() {
        FieldOrRowOrSelect r = parseExp();

        Field f = toField(r, false);
        if (f != null)
            for (;;)
                if (!peek("*=") && parseIf('*'))
                    r = f = f.mul(toField(parseExp()));
                else if (parseIf('/'))
                    r = f = f.div(toField(parseExp()));
                else if (parseIf('%'))
                    r = f = f.mod(toField(parseExp()));






                else
                    break;

        return r;
    }

    private final FieldOrRowOrSelect parseExp() {
        FieldOrRowOrSelect r = parseUnaryOps();

        Field f = toField(r, false);
        if (f != null)
            for (;;)
                if (!peek("^=") && parseIf('^') || parseIf("**"))
                    r = f = f.pow(toField(parseUnaryOps()));
                else
                    break;

        return r;
    }

    private final FieldOrRowOrSelect parseUnaryOps() {
        if (parseProKeywordIf("CONNECT_BY_ROOT")) {



        }

        if (parseIf('~'))
            return toField(parseUnaryOps()).bitNot();

        FieldOrRowOrSelect r;
        Sign sign = parseSign();

        if (sign == Sign.NONE)
            r = parseTerm();
        else if (sign == Sign.PLUS)
            r = toField(parseTerm());
        else if ((r = parseFieldUnsignedNumericLiteralIf(Sign.MINUS)) == null)
            r = toField(parseTerm()).neg();

        if (!ignoreProEdition() && parseTokensIf('(', '+', ')') && requireProEdition())



            ;

        // [#7171] Only identifier based field expressions could have been functions
        //         E.g. 'abc' ('xyz') may be some other type of syntax, e.g. from Db2 SIGNAL statements
        int p = position();
        if (r instanceof TableField && parseIf('('))






            throw exception("Unknown function");

        while (parseIf("::"))
            r = cast(toField(r), parseDataType());




        if (peek('[') && possiblyArrayTyped(r) && parseIf('[')) {
            r = arrayGet((Field) toField(r), (Field) parseField());
            parse(']');
        }

        r = parseMethodCallIf(r);
        return r;
    }

    private final boolean possiblyArrayTyped(FieldOrRowOrSelect r) {
        if (!supportArraySubscripts)
            return false;
        else if (r instanceof Row)
            return false;
        else if (r instanceof Field<?> f)
            return f.getDataType().isArray() || f.getDataType().isOther();
        else if (r instanceof Select<?> s) {
            List<Field<?>> l = s.getSelect();

            if (l.size() != 1)
                return false;
            else if (!l.get(0).getDataType().isArray() && !l.get(0).getDataType().isOther())
                return false;
        }

        return true;
    }

    private final FieldOrRowOrSelect parseMethodCallIf(FieldOrRowOrSelect r) {








        return r;
    }

    private final FieldOrRowOrSelect parseMethodCallIf0(FieldOrRowOrSelect r) {






























































































































        return r;
    }

    private final Sign parseSign() {
        Sign sign = Sign.NONE;

        for (;;)
            if (parseIf('+'))
                sign = sign == Sign.NONE ? Sign.PLUS  : sign;

            // [#9447] Semantic comments are not stripped out as whitespace, so ignore them here
            else if (peek('-') && !peek("--") && parseIf('-'))
                sign = sign == Sign.NONE ? Sign.MINUS : sign.invert();
            else
                break;

        return sign;
    }

    private enum Sign {
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

    private final FieldOrRowOrSelect parseTerm() {
        FieldOrRowOrSelect field;
        Object value;



















        char u = characterUpper();





        // [#18480] Allow for quoted built-in function identifiers
        switch (u) {
            case '`':
            case '[':
            case '"':
                u = characterNextUpper();
                break;
        }

        switch (u) {

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
                else if (parseFunctionNameIf("ACOSH"))
                    return acosh((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("ACOTH"))
                    return acoth((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("ASIN"))
                    return asin((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("ASINH"))
                    return asinh((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("ATAN", "ATN"))
                    return atan((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("ATANH"))
                    return atanh((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("ATN2", "ATAN2"))
                    return parseFunctionArgs2(() -> toField(parseOp()), DSL::atan2);

                else if (parseFunctionNameIf("ASCII_CHAR"))
                    return chr((Field) parseFieldParenthesised());

                else if ((field = parseArrayValueConstructorIf()) != null)
                    return field;

                else if (parseFunctionNameIf("ADD_YEARS"))
                    return parseFieldAddDatePart(DatePart.YEAR);
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
                else if (parseFunctionNameIf("ARRAY_APPEND", "arrayPushBack"))
                    return parseFunctionArgs2((f1, f2) -> arrayAppend((Field<Void[]>) f1, (Field<Void>) f2));
                else if (parseFunctionNameIf("ARRAY_CAT", "ARRAY_CONCAT", "arrayConcat"))
                    return parseFunctionArgs2((f1, f2) -> arrayConcat(f1, f2));
                else if (parseFunctionNameIf("ARRAY_CONTAINS"))
                    return parseFunctionArgs2((f1, f2) -> arrayContains((Field<Void[]>) f1, (Field<Void>) f2));
                else if (parseFunctionNameIf("ARRAY_GET", "arrayElement"))
                    return parseFunctionArgs2((f1, f2) -> arrayGet(f1, f2));
                else if (parseFunctionNameIf("ARRAY_FILTER", "arrayFilter"))
                    return parseArrayLambdaFunction(DSL::arrayFilter);
                else if (parseFunctionNameIf("ARRAY_MAP", "arrayMap", "ARRAY_TRANSFORM"))
                    return parseArrayLambdaFunction(DSL::arrayMap);
                else if (parseFunctionNameIf("ARRAY_ALL_MATCH", "arrayAll", "ALL_MATCH"))
                    return parseArrayLambdaFunction(DSL::arrayAllMatch);
                else if (parseFunctionNameIf("ARRAY_ANY_MATCH", "arrayExists", "ANY_MATCH"))
                    return parseArrayLambdaFunction(DSL::arrayAnyMatch);
                else if (parseFunctionNameIf("ARRAY_NONE_MATCH"))
                    return parseArrayLambdaFunction(DSL::arrayNoneMatch);
                else if (parseFunctionNameIf("ARRAY_MAP", "arrayMap", "ARRAY_TRANSFORM"))
                    return parseArrayLambdaFunction(DSL::arrayMap);
                else if (parseFunctionNameIf("ARRAY_OVERLAP", "ARRAYS_OVERLAP"))
                    return parseFunctionArgs2((f1, f2) -> arrayOverlap((Field<Void[]>) f1, (Field<Void[]>) f2));
                else if (parseFunctionNameIf("ARRAY_PREPEND"))
                    return parseFunctionArgs2((f1, f2) -> arrayPrepend((Field<Void>) f1, (Field<Void[]>) f2));
                else if (parseFunctionNameIf("arrayPushFront"))
                    return parseFunctionArgs2((f1, f2) -> arrayPrepend((Field<Void>) f2, (Field<Void[]>) f1));
                else if (parseFunctionNameIf("ARRAY_REMOVE"))
                    return parseFunctionArgs2((f1, f2) -> arrayRemove((Field<Void[]>) f1, (Field<Void>) f2));
                else if (parseFunctionNameIf("ARRAY_REPLACE"))
                    return parseFunctionArgs3((f1, f2, f3) -> arrayReplace((Field<Void[]>) f1, (Field<Void>) f2, (Field<Void>) f3));
                else if (parseFunctionNameIf("ARRAY_TO_STRING"))
                    return parseFunctionArgs3(DSL::arrayToString, DSL::arrayToString);
                else if ((field = parseFieldArrayConstructIf()) != null)
                    return field;
                else if (parseFunctionNameIf("ADD"))
                    return parseFunctionArgs2(Field::add);
                else if (parseFunctionNameIf("AND"))
                    return parseFunctionArgs2((f1, f2) -> and(condition(f1), condition(f2)));

                break;

            case 'B':
                if (parseFunctionNameIf("BIT_LENGTH"))
                    return parseFunctionArgs1(f -> binary(f) ? binaryBitLength(f) : bitLength(f));
                else if (parseFunctionNameIf("BITGET", "BIT_GET", "bitTest"))
                    return parseFunctionArgs2(DSL::bitGet);
                else if (parseFunctionNameIf("BITSET", "BIT_SET"))
                    return parseFunctionArgs3(DSL::bitSet, DSL::bitSet);
                else if (parseFunctionNameIf("BITCOUNT", "BIT_COUNT"))
                    return bitCount((Field) parseFieldNumericOpParenthesised());
                else if (parseKeywordIf("BIT_LSHIFT"))
                    return parseFunctionArgs2(() -> toField(parseOp()), (f1, f2) -> shl(f1, f2));
                else if (parseKeywordIf("BIT_RSHIFT"))
                    return parseFunctionArgs2(() -> toField(parseOp()), (f1, f2) -> shr(f1, f2));
                else if (parseFunctionNameIf("BYTE_LENGTH"))
                    return octetLength((Field) parseFieldParenthesised());
                else if ((field = parseFieldBitwiseFunctionIf()) != null)
                    return field;
                else if (parseFunctionNameIf("BIN_TO_UUID"))
                    return parseFunctionArgs1(DSL::binToUuid);
                else if ((value = parseBitLiteralIf()) != null)
                    return DSL.inline((Boolean) value);
                else if ((field = parseTypedLiteralIf("BIGINT", BIGINT)) != null)
                    return field;
                else if ((field = parseTypedLiteralIf("BOOLEAN", BOOLEAN)) != null)
                    return field;

                break;

            case 'C':
                if ((field = parseFieldConcatIf()) != null)
                    return field;
                else if ((parseFunctionNameIf("CURRENT_CATALOG") && parseEmptyParens()))
                    return currentCatalog();
                else if ((parseFunctionNameIf("CURRENT_DATABASE", "currentDatabase") && parseEmptyParens()))
                    return currentCatalog();
                else if ((parseKeywordIf("CURRENT_SCHEMA", "CURRENT SCHEMA")) && parseEmptyParensIf())
                    return currentSchema();
                else if ((parseKeywordIf("CURRENT_USER", "CURRENT USER", "CURRENTUSER")) && parseEmptyParensIf())
                    return currentUser();
                else if (parseFunctionNameIf("CHR", "CHAR"))
                    return chr((Field) parseFieldParenthesised());

                else if (parseFunctionNameIf("CHARINDEX"))
                    return parseFunctionArgs3(
                        (f1, f2) -> binary(f1, f2) ? DSL.binaryPosition(f2, f1) : DSL.position(f2, f1),
                        (f1, f2, f3) -> binary(f1, f2) ? DSL.binaryPosition(f2, f1, f3) : DSL.position(f2, f1, f3)
                    );
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
                else if (parseFunctionNameIf("CBRT"))
                    return parseFunctionArgs1(DSL::cbrt);
                else if (parseFunctionNameIf("CONTAINS"))
                    return parseFunctionArgs2((f1, f2) -> f1.getDataType().isArray()
                        ? arrayContains((Field<Void[]>) f1, (Field<Void>) f2)
                        : f1.contains(f2)
                    );
                else if ((field = parseNextvalCurrvalIf(SequenceMethod.CURRVAL)) != null)
                    return field;
                else if (parseFunctionNameIf("CENTURY"))
                    return century(parseFieldParenthesised());

                else if (parseKeywordIf("CURRENT_DATE", "CURRENT DATE") && parseEmptyParensIf())
                    return currentDate();
                else if (parseKeywordIf("CURRENT_TIMESTAMP", "CURRENT TIMESTAMP")) {
                    Field<Integer> precision = null;
                    if (parseIf('('))
                        if (!parseIf(')')) {
                            precision = (Field<Integer>) parseField();
                            parse(')');
                        }
                    return precision != null ? currentTimestamp(precision) : currentTimestamp();
                }
                else if (parseKeywordIf("CURRENT_TIME", "CURRENT TIME") && parseEmptyParensIf())
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
                else if (parseProKeywordIf("CONNECT_BY_ISCYCLE")) {



                }
                else if (parseProKeywordIf("CONNECT_BY_ISLEAF")) {



                }




                else if ((field = parseTypedLiteralIf("CHAR", CHAR)) != null)
                    return field;

                break;

            case 'D':
                if ((parseFunctionNameIf("DATABASE") && parseEmptyParens()))
                    return currentCatalog();
                else if ((parseFunctionNameIf("DB_NAME") && parseEmptyParens()))
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
                else if (parseFunctionNameIf("DATE_PART_YEAR"))
                    return year(parseFieldParenthesised());

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

                else if ((field = parseFieldSubstringIf()) != null)
                    return field;
                else if (parseProFunctionNameIf("DBMS_LOB.INSTR")) {



                }
                else if (parseProFunctionNameIf("DBMS_LOB.GETLENGTH")) {



                }
                else if (parseFunctionNameIf("DIV", "DIVIDE"))
                    return parseFunctionArgs2(Field::div);
                else if ((field = parseTypedLiteralIf("DECIMAL", DECIMAL)) != null)
                    return field;
                else if ((field = parseTypedLiteralIf("DECFLOAT", DECFLOAT)) != null)
                    return field;
                else if ((field = parseTypedLiteralIf("DOUBLE PRECISION", DOUBLE)) != null)
                    return field;
                else if ((field = parseTypedLiteralIf("DOUBLE", DOUBLE)) != null)
                    return field;

                break;

            case 'E':

                // [#6704] PostgreSQL E'...' escaped string literals
                if (characterNext() == '\'')
                    return inline(parseStringLiteral());
                else if ((field = parseFieldExtractIf()) != null)
                    return field;
                else if (parseFunctionNameIf("ELEMENT_AT"))
                    return parseFunctionArgs2(DSL::arrayGet);
                else if (parseFunctionNameIf("ENDS_WITH", "ENDSWITH"))
                    return parseFunctionArgs2((f1, f2) -> f1.endsWith(f2));
                else if (parseFunctionNameIf("EXP"))
                    return exp((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("EPOCH"))
                    return epoch(parseFieldParenthesised());
                else if ((field = parseFieldChooseIf()) != null)
                    return field;

                break;

            case 'F':
                if (parseFunctionNameIf("FLOOR"))
                    return floor((Field) parseFieldNumericOpParenthesised());

                else if ((field = parseFieldFirstValueIf()) != null)
                    return field;
                else if ((field = parseFieldFieldIf()) != null)
                    return field;
                else if ((field = parseTypedLiteralIf("FLOAT", FLOAT)) != null)
                    return field;

                break;

            case 'G':
                if (parseKeywordIf("GETDATE") && parseEmptyParens())
                    return currentTimestamp();
                else if (parseFunctionNameIf("GENGUID", "GENERATE_UUID", "GEN_RANDOM_UUID") && parseEmptyParens())
                    return uuid();
                else if (parseFunctionNameIf("generateUUIDv4") && parseEmptyParensOr(c -> c.parseField() != null))
                    return uuid();
                else if (parseFunctionNameIf("GET_BIT", "GETBIT"))
                    return parseFunctionArgs2(DSL::bitGet);

                else if ((field = parseFieldGreatestIf()) != null)
                    return field;
                else if (parseProFunctionNameIf("GROUP_ID") && parseEmptyParens()) {



                }
                else if ((field = parseFieldGroupingIdIf()) != null)
                    return field;
                else if (parseFunctionNameIf("GROUPING"))
                    return grouping(parseFieldParenthesised());
                else if (parseProFunctionNameIf("GEOMETRY::STGEOMFROMWKB", "GEOGRAPHY::STGEOMFROMWKB")) {



                }
                else if (parseProFunctionNameIf("GEOMETRY::STGEOMFROMTEXT", "GEOGRAPHY::STGEOMFROMTEXT")) {



                }
                else if ((field = parseFieldBitwiseFunctionIf()) != null)
                    return field;

                break;

            case 'H':
                if (parseFunctionNameIf("HOUR"))
                    return hour(parseFieldParenthesised());

                else if (parseFunctionNameIf("has"))
                    return parseFunctionArgs2((f1, f2) -> arrayContains((Field<Void[]>) f1, (Field<Void>) f2));
                else if (parseFunctionNameIf("HASH_MD5"))
                    return parseFunctionArgs1(f -> binary(f) ? binaryMd5(f) : md5(f));
                else if (parseFunctionNameIf("HEX"))
                    return toHex((Field) parseFieldParenthesised());

                break;

            case 'I':

                // [#8792] TODO: Support parsing interval expressions
                if ((field = parseFieldIntervalLiteralIf(true)) != null)
                    return field;
                else if (parseFunctionNameIf("ISO_DAY_OF_WEEK"))
                    return isoDayOfWeek(parseFieldParenthesised());
                else if (parseFunctionNameIf("INSTR"))
                    return parseFunctionArgs3(
                        (f1, f2) -> binary(f1, f2) ? DSL.binaryPosition(f1, f2) : DSL.position(f1, f2),
                        (f1, f2, f3) -> binary(f1, f2) ? DSL.binaryPosition(f1, f2, f3) : DSL.position(f1, f2, f3)
                    );
                else if (parseFunctionNameIf("INSERT"))
                    return parseFunctionArgs4(DSL::insert);
                else if (parseFunctionNameIf("IFNULL"))
                    return parseFunctionArgs2((f1, f2) -> ifnull((Field<?>) f1, (Field<?>) f2));
                else if (parseFunctionNameIf("ISJSON"))
                    return parseFunctionArgs1(f -> case_(f.isJson()).when(trueCondition(), one()).when(falseCondition(), zero()));
                else if (parseFunctionNameIf("ISNULL"))
                    return parseFunctionArgs2(f -> f.isNull(), (f1, f2) -> isnull((Field<?>) f1, (Field<?>) f2));
                else if ((field = parseFieldIfIf()) != null)
                    return field;
                else if ((field = parseTypedLiteralIf("INTEGER", INTEGER)) != null)
                    return field;
                else if ((field = parseTypedLiteralIf("INT", INTEGER)) != null)
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
                else if ((field = parseFieldJSONLiteralIf()) != null)
                    return field;
                else if (parseFunctionNameIf("JSON_ARRAY_LENGTH", "JSON_LENGTH", "JSONARRAYLENGTH"))
                    return parseFunctionArgs1(DSL::jsonArrayLength);
                else if (parseFunctionNameIf("JSON_KEYS", "JSONExtractKeys"))
                    return parseFunctionArgs1(DSL::jsonKeys);
                else if (parseFunctionNameIf("JSON_KEY_EXISTS"))
                    return parseFunctionArgs2(DSL::jsonKeyExists);
                else if (parseFunctionNameIf("JSON_INSERT"))
                    return parseFunctionArgs3(DSL::jsonInsert);
                else if (parseFunctionNameIf("JSON_REMOVE"))
                    return parseFunctionArgs2(DSL::jsonRemove);
                else if (parseFunctionNameIf("JSON_REPLACE"))
                    return parseFunctionArgs3(DSL::jsonReplace);
                else if (parseFunctionNameIf("JSON_SET"))
                    return parseFunctionArgs3(DSL::jsonSet);
                else if (parseFunctionNameIf("JSON_VALID"))
                    return parseFunctionArgs1(f -> case_(f.isJson()).when(trueCondition(), one()).when(falseCondition(), zero()));
                else if (parseFunctionNameIf("JSONB_ARRAY_LENGTH"))
                    return parseFunctionArgs1(DSL::jsonbArrayLength);
                else if (parseFunctionNameIf("JSONB_KEYS"))
                    return parseFunctionArgs1(DSL::jsonbKeys);
                else if (parseFunctionNameIf("JSONB_KEY_EXISTS"))
                    return parseFunctionArgs2(DSL::jsonbKeyExists);
                else if (parseFunctionNameIf("JSONB_INSERT"))
                    return parseFunctionArgs3(DSL::jsonbInsert);
                else if (parseFunctionNameIf("JSONB_REMOVE"))
                    return parseFunctionArgs2(DSL::jsonbRemove);
                else if (parseFunctionNameIf("JSONB_REPLACE"))
                    return parseFunctionArgs3(DSL::jsonbReplace);
                else if (parseFunctionNameIf("JSONB_SET"))
                    return parseFunctionArgs3(DSL::jsonbSet);

                break;

            case 'L':
                if (parseFunctionNameIf("LOWER", "LCASE"))
                    return DSL.lower((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("LPAD", "leftPad"))
                    return parseFunctionArgs3(DSL::lpad, DSL::lpad);
                else if (parseFunctionNameIf("LTRIM"))
                    return parseFunctionArgs2(DSL::ltrim, (f1, f2) -> binary(f1, f2) ? binaryLtrim(f1, f2) : ltrim(f1, f2));
                else if (parseFunctionNameIf("LEFT"))
                    return parseFunctionArgs2(DSL::left);
                else if (parseFunctionNameIf("LENGTH", "LEN"))
                    return parseFunctionArgs1(f -> binary(f) ? binaryLength(f) : length(f));
                else if (parseFunctionNameIf("LENGTHB"))
                    return octetLength((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("LIST_CONTAINS"))
                    return parseFunctionArgs2((f1, f2) -> arrayContains((Field<Void[]>) f1, (Field<Void>) f2));
                else if (parseFunctionNameIf("LN", "LOGN"))
                    return ln((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("LOG10"))
                    return log10((Field) parseFieldNumericOpParenthesised());
                else if ((field = parseFieldLogIf()) != null)
                    return field;
                else if ((field = parseFieldLocateIf()) != null)
                    return field;
                else if (parseProKeywordIf("LEVEL")) {



                }
                else if (parseKeywordIf("LSHIFT", "LEFT_SHIFT"))
                    return parseFunctionArgs2(() -> toField(parseOp()), (f1, f2) -> shl(f1, f2));
                else if ((field = parseFieldLeastIf()) != null)
                    return field;
                else if ((field = parseFieldLeadLagIf()) != null)
                    return field;
                else if ((field = parseFieldLastValueIf()) != null)
                    return field;

                break;

            case 'M':
                if (parseFunctionNameIf("MOD", "MODULO"))
                    return parseFunctionArgs2(Field::mod);
                else if (parseFunctionNameIf("MULTIPLY"))
                    return parseFunctionArgs2(Field::mul);
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
                else if (parseFunctionNameIf("MID"))
                    return parseFunctionArgs3(DSL::mid);
                else if (parseFunctionNameIf("MD5"))
                    return parseFunctionArgs1(f -> binary(f) ? binaryMd5(f) : md5(f));

                else if ((field = parseMultisetValueConstructorIf()) != null)
                    return field;

                else if ((field = parseFieldGreatestIf()) != null)
                    return field;
                else if ((field = parseFieldLeastIf()) != null)
                    return field;
                else if ((field = parseFieldDecodeIf()) != null)
                    return field;








                else if (parseFunctionNameIf("MINUS"))
                    return parseFunctionArgs2(Field::sub);

                break;

            case 'N':

                // [#9540] N'...' NVARCHAR literals
                if (characterNext() == '\'')
                    return inline(parseStringLiteral(), NVARCHAR);
                else if ((field = parseFieldNewIdIf()) != null)
                    return field;
                else if (parseFunctionNameIf("NVL2"))
                    return parseFunctionArgs3((f1, f2, f3) -> nvl2((Field<?>) f1, (Field<?>) f2, (Field<?>) f3));
                else if (parseFunctionNameIf("NVL"))
                    return parseFunctionArgs2((f1, f2) -> nvl((Field<?>) f1, (Field<?>) f2));
                else if (parseFunctionNameIf("NULLIF"))
                    return parseFunctionArgs2((f1, f2) -> nullif((Field<?>) f1, (Field<?>) f2));
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
                else if (parseFunctionNameIf("NEG", "NEGATE"))
                    return parseFunctionArgs1(DSL::neg);
                else if (parseFunctionNameIf("NONE_MATCH"))
                    return parseArrayLambdaFunction(DSL::arrayNoneMatch);
                else if ((field = parseTypedLiteralIf("NUMERIC", NUMERIC)) != null)
                    return field;
                else if ((field = parseTypedLiteralIf("NUMBER", NUMERIC)) != null)
                    return field;

                break;

            case 'O':
                if (parseFunctionNameIf("OREPLACE"))
                    return parseFunctionArgs3(DSL::replace, DSL::replace);
                else if ((field = parseFieldOverlayIf()) != null)
                    return field;
                else if ((field = parseFieldTranslateIf()) != null)
                    return field;
                else if (parseFunctionNameIf("OCTET_LENGTH"))
                    return parseFunctionArgs1(f -> binary(f) ? binaryOctetLength(f) : octetLength(f));
                else if ((field = parseFieldObjectConstructIf()) != null)
                    return field;
                else if (parseFunctionNameIf("OBJECT_KEYS"))
                    return parseFunctionArgs1(DSL::jsonKeys);
                else if (parseFunctionNameIf("OR"))
                    return parseFunctionArgs2((f1, f2) -> or(condition(f1), condition(f2)));

                break;

            case 'P':
                if ((field = parseFieldPositionIf()) != null)
                    return field;
                else if ((field = parseFieldPercentRankIf()) != null)
                    return field;
                else if (parseFunctionNameIf("POWER", "POW"))
                    return parseFunctionArgs2(() -> toField(parseOp()), DSL::power);
                else if (parseFunctionNameIf("PI") && parseEmptyParens())
                    return pi();

                else if (parseProKeywordIf("PRIOR")) {



                }
                else if (parseFunctionNameIf("PLUS"))
                    return parseFunctionArgs2(Field::add);

                break;

            case 'Q':
                if (characterNext() == '\'')
                    return inline(parseStringLiteral());

                else if (parseFunctionNameIf("QUARTER"))
                    return quarter(parseFieldParenthesised());






            case 'R':
                if (parseFunctionNameIf("REPLACE", "replaceAll"))
                    return parseFunctionArgs3(DSL::replace, DSL::replace);
                else if ((field = parseFieldRegexpReplaceIf()) != null)
                    return field;
                else if (parseFunctionNameIf("REPEAT", "REPLICATE"))
                    return parseFunctionArgs2(DSL::repeat);
                else if (parseFunctionNameIf("REVERSE"))
                    return reverse((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("RPAD", "rightPad"))
                    return parseFunctionArgs3(DSL::rpad, DSL::rpad);
                else if (parseFunctionNameIf("RTRIM"))
                    return parseFunctionArgs2(DSL::rtrim, (f1, f2) -> binary(f1, f2) ? binaryRtrim(f1, f2) : rtrim(f1, f2));
                else if (parseFunctionNameIf("RIGHT"))
                    return parseFunctionArgs2(DSL::right);
                else if (parseFunctionNameIf("RANDOM_UUID") && parseEmptyParens())
                    return uuid();

                else if (parseFunctionNameIf("ROW_NUMBER", "ROWNUMBER") && parseEmptyParens())
                    return parseWindowFunction(null, null, rowNumber());
                else if ((field = parseFieldRankIf()) != null)
                    return field;
                else if ((field = parseFieldRoundIf()) != null)
                    return field;
                else if (parseProKeywordIf("ROWNUM")) {



                }
                else if (parseFunctionNameIf("RADIANS")
                      || parseFunctionNameIf("RADIAN")
                      || parseFunctionNameIf("RAD"))
                    return rad((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("RAND", "RANDOM") && parseEmptyParens())
                    return rand();

                else if (parseFunctionNameIf("RATIO_TO_REPORT"))
                    return parseFunctionArgs1(f -> parseWindowFunction(null, null, ratioToReport(f)));
                else if (parseKeywordIf("RSHIFT", "RIGHT_SHIFT"))
                    return parseFunctionArgs2(() -> toField(parseOp()), (f1, f2) -> shr(f1, f2));
                else if (parseFunctionNameIf("ROOT"))
                    return parseFunctionArgs2(DSL::sqrt, DSL::root);
                else if (parseFunctionNameIf("ROW"))
                    return parseTuple();
                else if ((field = parseTypedLiteralIf("REAL", REAL)) != null)
                    return field;

                break;

            case 'S':
                if ((field = parseFieldSubstringIf()) != null)
                    return field;
                else if (parseFunctionNameIf("SUBSTRING_INDEX", "substringIndex"))
                    return parseFunctionArgs3(DSL::substringIndex);
                else if (parseFunctionNameIf("SPACE"))
                    return space((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("SPLIT_PART"))
                    return parseFunctionArgs3(DSL::splitPart);
                else if (parseFunctionNameIf("SPLIT", "STRING_TO_ARRAY"))
                    return parseFunctionArgs3(DSL::stringToArray, DSL::stringToArray);
                else if (parseFunctionNameIf("STR_REPLACE"))
                    return parseFunctionArgs3(DSL::replace, DSL::replace);
                else if (parseFunctionNameIf("STARTS_WITH", "STARTSWITH"))
                    return parseFunctionArgs2((f1, f2) -> f1.startsWith(f2));
                else if (parseFunctionNameIf("SCHEMA") && parseEmptyParensIf())
                    return currentSchema();
                else if (parseFunctionNameIf("STRREVERSE"))
                    return reverse((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("STRTOK"))
                    return parseFunctionArgs3(DSL::splitPart);
                else if (parseFunctionNameIf("SYSUUID") && parseEmptyParensIf())
                    return uuid();
                else if (parseFunctionNameIf("SET_BIT", "SETBIT"))
                    return parseFunctionArgs3(DSL::bitSet, DSL::bitSet);

                else if (parseFunctionNameIf("SECOND"))
                    return second(parseFieldParenthesised());
                else if (!ignoreProEdition() && parseFunctionNameIf("SEQ4", "SEQ8") && parseEmptyParens() && requireProEdition()) {



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
                else if (parseKeywordIf("SHL", "SHIFTLEFT"))
                    return parseFunctionArgs2(() -> toField(parseOp()), (f1, f2) -> shl(f1, f2));
                else if (parseKeywordIf("SHR", "SHIFTRIGHT"))
                    return parseFunctionArgs2(() -> toField(parseOp()), (f1, f2) -> shr(f1, f2));
                else if ((field = parseFieldSysConnectByPathIf()) != null)
                    return field;
                else if ((field = parseFieldCastIf()) != null)
                    return field;
                else if (parseProFunctionNameIf("ST_AREA")) {



                }
                else if (parseProFunctionNameIf("SDO_GEOM.SDO_AREA")) {



                }
                else if (parseProFunctionNameIf("ST_ASBINARY", "ST_ASWKB")) {



                }
                else if (parseProFunctionNameIf("ST_ASTEXT")) {



                }
                else if (parseProFunctionNameIf("ST_BOUNDARY")) {



                }
                else if (parseProFunctionNameIf("ST_CENTROID")) {



                }
                else if (parseProFunctionNameIf("SDO_GEOM.SDO_CENTROID")) {



                }
                else if (parseProFunctionNameIf("ST_DIFFERENCE")) {



                }
                else if (parseProFunctionNameIf("SDO_GEOM.SDO_DIFFERENCE")) {



                }
                else if (parseProFunctionNameIf("ST_DIMENSION")) {



                }
                else if (parseProFunctionNameIf("ST_DISTANCE")) {



                }
                else if (parseProFunctionNameIf("SDO_GEOM.SDO_DISTANCE")) {



                }
                else if (parseProFunctionNameIf("ST_ENDPOINT", "SDO_LRS.GEOM_SEGMENT_END_PT")) {



                }
                else if (parseProFunctionNameIf("ST_EXTERIORRING")) {



                }
                else if (parseProFunctionNameIf("ST_GEOMETRYN", "SDO_UTIL.EXTRACT")) {



                }
                else if (parseProFunctionNameIf("ST_GEOMETRYTYPE")) {



                }
                else if (parseProFunctionNameIf("ST_GEOMFROMWKB")) {



                }
                else if (parseProFunctionNameIf("ST_GEOMFROMTEXT", "SDO_GEOMETRY")) {







                }
                else if (parseProFunctionNameIf("ST_INTERIORRINGN")) {



                }
                else if (parseProFunctionNameIf("ST_INTERSECTION")) {



                }
                else if (parseProFunctionNameIf("SDO_GEOM.SDO_INTERSECTION")) {



                }
                else if (parseProFunctionNameIf("ST_LENGTH")) {



                }
                else if (parseProFunctionNameIf("SDO_GEOM.SDO_LENGTH")) {



                }
                else if (parseProFunctionNameIf("ST_NUMINTERIORRING", "ST_NUMINTERIORRINGS", "ST_NINTERIORRINGS")) {



                }
                else if (parseProFunctionNameIf("ST_NUMGEOMETRIES", "SDO_UTIL.GETNUMELEM")) {



                }
                else if (parseProFunctionNameIf("ST_NPOINTS", "ST_NUMPOINTS")) {



                }
                else if (parseProFunctionNameIf("ST_PERIMETER")) {



                }
                else if (parseProFunctionNameIf("ST_POINTN")) {



                }
                else if (parseProFunctionNameIf("ST_SRID")) {



                }
                else if (parseProFunctionNameIf("ST_STARTPOINT", "SDO_LRS.GEOM_SEGMENT_START_PT")) {



                }
                else if (parseProFunctionNameIf("ST_TRANSFORM", "SDO_CS.TRANSFORM")) {



                }
                else if (parseProFunctionNameIf("ST_UNION")) {



                }
                else if (parseProFunctionNameIf("SDO_GEOM.SDO_UNION")) {



                }
                else if (parseProFunctionNameIf("SDO_GEOM.SDO_MIN_MBR_ORDINATE")) {












                }
                else if (parseProFunctionNameIf("SDO_GEOM.SDO_MAX_MBR_ORDINATE")) {












                }
                else if (parseProFunctionNameIf("ST_X")) {



                }
                else if (parseProFunctionNameIf("ST_XMIN")) {



                }
                else if (parseProFunctionNameIf("ST_XMAX")) {



                }
                else if (parseProFunctionNameIf("ST_Y")) {



                }
                else if (parseProFunctionNameIf("ST_YMIN")) {



                }
                else if (parseProFunctionNameIf("ST_YMAX")) {



                }
                else if (parseProFunctionNameIf("ST_Z")) {



                }
                else if (parseProFunctionNameIf("ST_ZMIN")) {



                }
                else if (parseProFunctionNameIf("ST_ZMAX")) {



                }
                else if (parseFunctionNameIf("SUB", "SUBTRACT"))
                    return parseFunctionArgs2(Field::sub);
                else if ((field = parseTypedLiteralIf("SMALLINT", SMALLINT)) != null)
                    return field;

                break;

            case 'T':
                if ((field = parseBooleanValueExpressionIf()) != null)
                    return field;

                else if ((field = parseFieldTrimIf()) != null)
                    return field;
                else if (parseFunctionNameIf("trimBoth"))
                    return parseFunctionArgs1(DSL::trim);
                else if (parseFunctionNameIf("trimLeft"))
                    return parseFunctionArgs1(DSL::ltrim);
                else if (parseFunctionNameIf("trimRight"))
                    return parseFunctionArgs1(DSL::rtrim);
                else if ((field = parseFieldTranslateIf()) != null)
                    return field;
                else if (parseFunctionNameIf("TO_CHAR"))
                    return parseFunctionArgs2(DSL::toChar, DSL::toChar);
                else if (parseFunctionNameIf("TO_HEX"))
                    return toHex((Field) parseFieldParenthesised());

                else if (parseFunctionNameIf("TANH"))
                    return tanh((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("TAN"))
                    return tan((Field) parseFieldNumericOpParenthesised());
                else if (parseFunctionNameIf("TO_NUMBER"))
                    return parseFunctionArgs1(f -> cast(f, NUMERIC));
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
                else if (parseFunctionNameIf("TO_DATE"))
                    return parseFunctionArgs2(f1 -> toDate(f1, inline(settings().getParseDateFormat())), DSL::toDate);
                else if (parseFunctionNameIf("TO_TIMESTAMP"))
                    return parseFunctionArgs2(f1 -> toTimestamp(f1, inline(settings().getParseTimestampFormat())), DSL::toTimestamp);
                else if (parseFunctionNameIf("TIMESTAMPDIFF"))
                    return parseFunctionArgs2((f1, f2) -> DSL.timestampDiff(f1, f2));
                else if ((field = parseFieldTruncIf()) != null)
                    return field;
                else if ((field = parseFieldCastIf()) != null)
                    return field;
                else if (parseFunctionNameIf("TRANSFORM"))
                    return parseArrayLambdaFunction(DSL::arrayMap);
                else if (parseDialect() == SQLITE && parseFunctionNameIf("TOTAL"))
                    return coalesce(
                        parseAggregateFunctionIf(false,
                            (AggregateFunction<?>) parseGeneralSetFunctionIf(ComputationalOperation.SUM)
                        ),
                        inline(BigDecimal.ZERO)
                    );
                else if ((field = parseTypedLiteralIf("TINYINT", TINYINT)) != null)
                    return field;

                break;

            case 'U':
                if (parseFunctionNameIf("UPPER", "UCASE"))
                    return DSL.upper((Field) parseFieldParenthesised());
                else if (parseFunctionNameIf("UUID", "UUID_GENERATE", "UUID_STRING") && parseEmptyParens())
                    return uuid();
                else if (parseFunctionNameIf("UUID_TO_BIN", "UUIDStringToNum"))
                    return parseFunctionArgs1(DSL::uuidToBin);
                else if (parseFunctionNameIf("UUIDNumToString"))
                    return parseFunctionArgs1(DSL::binToUuid);

                else if (parseFunctionNameIf("UNIX_TIMESTAMP"))
                    return epoch(parseFieldParenthesised());
                else if ((field = parseTypedLiteralIf("UUID", SQLDataType.UUID)) != null)
                    return field;

                break;

            case 'V':
                if (TRUE.equals(data(DATA_PARSE_ON_CONFLICT)) && (parseFunctionNameIf("VALUES") || parseFunctionNameIf("VALUE")))
                    return excluded(parseFieldParenthesised());
                else if ((field = parseTypedLiteralIf("VARCHAR", VARCHAR)) != null)
                    return field;

            case 'W':
                if (parseFunctionNameIf("WIDTH_BUCKET", "widthBucket"))
                    return parseFunctionArgs4((f1, f2, f3, f4) -> widthBucket(f1, f2, f3, f4));
                else if (parseFunctionNameIf("WEEK"))
                    return week(parseFieldParenthesised());

                break;

            case 'X':
                if ((value = parseBinaryLiteralIf()) != null)
                    return inline((byte[]) value);

                else if (parseFunctionNameIf("XMLCOMMENT"))
                    return xmlcomment((Field) parseField());
                else if (parseFunctionNameIf("XMLTYPE"))
                    return cast((Field) parseField(), XML);
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
                else if (parseFunctionNameIf("XOR"))
                    return parseFunctionArgs2((f1, f2) -> xor(condition(f1), condition(f2)));

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

                    FieldOrRowOrSelect r = parseSubqueryIf();
                    if (r != null)
                        return r;

                    parse('(');
                    r = parseFieldOrRow();
                    List<Field<?>> list = null;

                    if (r instanceof Field<?> f) {
                        while (parseIf(',')) {
                            if (list == null) {
                                list = new ArrayList<>();
                                list.add(f);
                            }

                            // TODO Allow for nesting ROWs
                            list.add(parseField());
                        }
                    }

                    parse(')');

                    if (list != null)
                        return row(list);
                    else if (peek('.') && r instanceof TableField)
                        return parseUDTPath((TableField<?, ?>) r);




                    else if (peek('.') && r instanceof UDTPathField)
                        return parseUDTPath((UDTPathField<?, ?, ?>) r);
                    else
                        return r;
                }
                finally {
                    forbidden = fk;
                }
        }

        if ((field = parseAggregateFunctionIf()) != null)
            return field;

        else if ((field = parseBooleanValueExpressionIf()) != null)
            return field;

        else if ((field = parseTemplateIf(DSL::field)) != null)
            return field;

        else
            return parseFieldNameOrSequenceExpression();
    }

    private final <T> Field<T> parseTypedLiteralIf(String keyword, DataType<T> type) {
        int p = position();

        if (parseKeywordIf(keyword)) {
            if (peek('\'')) {
                String s = parseStringLiteral();
                return inline(convert(s, type.getType()), type);
            }
            else
                position(p);
        }

        return null;
    }















    private final Field<?> parseUDTPath(TableField<?, ?> tf) {
        return parseUDTPath((UDTPathField<?, ?, ?>) new UDTPathTableFieldImpl<>(
            tf.getUnqualifiedName(),
            tf.getDataType(),
            tf.getTable(),
            null, null, null
        ));
    }

    private final Field<?> parseUDTPath(UDTPathField<?, ?, ?> r) {
        while (parseIf('.'))
            r = new UDTPathFieldImpl<>(parseIdentifier(), OTHER, r.asQualifier(), null, null);

        return r;
    }

    private final <Q extends QueryPart> Q parseTemplateIf(Function<? super SQL, ? extends Q> wrap) {
        boolean raw = false;
        afterWhitespace(position, false, true, icTemplate);

        if (markerStart == -1) {
            raw = true;
            afterWhitespace(position, false, true, icRaw);
        }

        if (markerStart > -1) {
            position(markerStart);

            try {
                String s = substring(markerStart, markerStop);
                return wrap.apply(raw ? raw(s) : sql(s));
            }
            finally {
                position(markerStop);
                parseWhitespaceIf();

                markerStart = -1;
                markerStop = -1;
            }
        }
        else
            return null;
    }





















































    private final Field<?> parseFieldAddDatePart(DatePart part) {
        return parseFunctionArgs2((f1, f2) -> dateAdd(f1, f2, part));
    }

    private final boolean peekSelectOrWith(boolean peekIntoParens) {
        return peekKeyword("WITH", false, peekIntoParens, false) || peekSelect(peekIntoParens);
    }

    private final boolean peekSelect(boolean peekIntoParens) {
        return peekKeyword("SELECT", false, peekIntoParens, false) ||
               peekKeyword("SEL", false, peekIntoParens, false);
    }

    private final Field<?> parseFieldSysConnectByPathIf() {
        if (parseProFunctionNameIf("SYS_CONNECT_BY_PATH")) {








        }

        return null;
    }

    private final Field<?> parseFieldBitwiseFunctionIf() {
        int p = position();

        char c0 = characterUpper();
        char c1 = character(p + 1);
        char c2 = character(p + 2);
        boolean agg = false;

        if (c0 == 'B') {
            if (c1 != 'I' && c1 != 'i')
                return null;
            if (c2 != 'T' && c2 != 't' && c2 != 'N' && c2 != 'n')
                return null;
        }
        else {
            if (c1 != 'r')
                return null;
            if (c2 != 'o')
                return null;
        }

        if (parseKeywordIf("BIT_AND") ||
            parseKeywordIf("BITWISE_AND") ||
            parseKeywordIf("BITAND") ||
            parseKeywordIf("BIN_AND") ||
            (agg = parseKeywordIf("BIT_AND_AGG")) ||
            (agg = parseKeywordIf("BITWISE_AND_AGG")) ||
            (agg = parseKeywordIf("BITAND_AGG")) ||
            (agg = parseKeywordIf("BIN_AND_AGG")) ||
            (agg = parseFunctionNameIf("groupBitAnd"))) {
            parse('(');
            if (parseKeywordIf("DISTINCT", "ALL"))
                agg = true;
            Field<?> x = toField(parseOp());

            if (agg && parse(')') || parseIf(')'))
                return parseAggregateFunctionIf(false, bitAndAgg((Field) x));

            parse(',');
            Field<?> y = toField(parseOp());
            parse(')');

            return bitAnd((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_NAND") ||
            parseKeywordIf("BITNAND") ||
            parseKeywordIf("BIN_NAND") ||
            (agg = parseKeywordIf("BIT_NAND_AGG")) ||
            (agg = parseKeywordIf("BITNAND_AGG")) ||
            (agg = parseKeywordIf("BIN_NAND_AGG")) ||
            (agg = parseFunctionNameIf("groupBitNand"))) {
            parse('(');
            if (parseKeywordIf("DISTINCT", "ALL"))
                agg = true;
            Field<?> x = toField(parseOp());

            if (agg && parse(')') || parseIf(')'))
                return parseAggregateFunctionIf(false, bitNandAgg((Field) x));

            parse(',');
            Field<?> y = toField(parseOp());
            parse(')');

            return bitNand((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_OR") ||
            parseKeywordIf("BITWISE_OR") ||
            parseKeywordIf("BITOR") ||
            parseKeywordIf("BIN_OR") ||
            (agg = parseKeywordIf("BIT_OR_AGG")) ||
            (agg = parseKeywordIf("BITWISE_OR_AGG")) ||
            (agg = parseKeywordIf("BITOR_AGG")) ||
            (agg = parseKeywordIf("BIN_OR_AGG")) ||
            (agg = parseKeywordIf("groupBitOr"))) {
            parse('(');
            if (parseKeywordIf("DISTINCT", "ALL"))
                agg = true;
            Field<?> x = toField(parseOp());

            if (agg && parse(')') || parseIf(')'))
                return parseAggregateFunctionIf(false, bitOrAgg((Field) x));

            parse(',');
            Field<?> y = toField(parseOp());
            parse(')');

            return bitOr((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_NOR") ||
            parseKeywordIf("BITNOR") ||
            parseKeywordIf("BIN_NOR") ||
            (agg = parseKeywordIf("BIT_NOR_AGG")) ||
            (agg = parseKeywordIf("BITNOR_AGG")) ||
            (agg = parseKeywordIf("BIN_NOR_AGG")) ||
            (agg = parseKeywordIf("groupBitNor"))) {
            parse('(');
            if (parseKeywordIf("DISTINCT", "ALL"))
                agg = true;
            Field<?> x = toField(parseOp());

            if (agg && parse(')') || parseIf(')'))
                return parseAggregateFunctionIf(false, bitNorAgg((Field) x));

            parse(',');
            Field<?> y = toField(parseOp());
            parse(')');

            return bitNor((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_XOR") ||
            parseKeywordIf("BITWISE_XOR") ||
            parseKeywordIf("BITXOR") ||
            parseKeywordIf("BIN_XOR") ||
            (agg = parseKeywordIf("BIT_XOR_AGG")) ||
            (agg = parseKeywordIf("BITXOR_AGG")) ||
            (agg = parseKeywordIf("BIN_XOR_AGG")) ||
            (agg = parseKeywordIf("groupBitXor"))) {
            parse('(');
            Field<?> x = toField(parseOp());

            if (agg && parse(')') || parseIf(')'))
                return parseAggregateFunctionIf(false, bitXorAgg((Field) x));

            parse(',');
            Field<?> y = toField(parseOp());
            parse(')');

            return bitXor((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_XNOR") ||
            parseKeywordIf("BITXNOR") ||
            parseKeywordIf("BIN_XNOR") ||
            (agg = parseKeywordIf("BIT_XNOR_AGG")) ||
            (agg = parseKeywordIf("BITXNOR_AGG")) ||
            (agg = parseKeywordIf("BIN_XNOR_AGG")) ||
            (agg = parseKeywordIf("groupBitXnor"))) {
            parse('(');
            Field<?> x = toField(parseOp());

            if (agg && parse(')') || parseIf(')'))
                return parseAggregateFunctionIf(false, bitXNorAgg((Field) x));

            parse(',');
            Field<?> y = toField(parseOp());
            parse(')');

            return bitXNor((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIT_NOT", "BITNOT", "BIN_NOT", "BITWISE_NOT")) {
            parse('(');
            Field<?> x = toField(parseOp());
            parse(')');

            return bitNot((Field) x);
        }
        else if (parseKeywordIf("BIN_SHL", "BITSHIFTLEFT", "BITWISE_LEFT_SHIFT")) {
            parse('(');
            Field<?> x = toField(parseOp());
            parse(',');
            Field<?> y = toField(parseOp());
            parse(')');

            return shl((Field) x, (Field) y);
        }
        else if (parseKeywordIf("BIN_SHR", "BITSHIFTRIGHT", "BITWISE_RIGHT_SHIFT")) {
            parse('(');
            Field<?> x = toField(parseOp());
            parse(',');
            Field<?> y = toField(parseOp());
            parse(')');

            return shr((Field) x, (Field) y);
        }

        return null;
    }

    private final Field<?> parseFieldNewIdIf() {
        if (parseFunctionNameIf("NEWID")) {
            parse('(');
            Long l = parseSignedIntegerLiteralIf();

            if (l != null && l != -1L)
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

    private enum SequenceMethod {
        NEXTVAL,
        CURRVAL
    }

    private final Field<?> parseFieldXMLSerializeIf() {
        if (parseFunctionNameIf("XMLSERIALIZE")) {
            parse('(');
            boolean content = parseKeywordIf("CONTENT");
            if (!content)
                parseKeywordIf("DOCUMENT");

            Field<XML> value = (Field<XML>) parseField();
            parseKeyword("AS");
            DataType<?> type = parseCastDataType(false);
            parse(')');

            return content ? xmlserializeContent(value, type) : xmlserializeDocument(value, type);
        }

        return null;
    }

    private final Field<?> parseFieldXMLConcatIf() {
        if (parseFunctionNameIf("XMLCONCAT")) {
            parse('(');
            List<Field<?>> fields = parseList(',', c -> c.parseField());
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
                return xmlelement(systemName("NAME"));

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
        if (parseProFunctionNameIf("XMLDOCUMENT")) {








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
                field = field.as(parseIdentifier(true, false));

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
            parseKeywordIf("ALL");
            s2 = s1 = xmlagg((Field<XML>) parseField());

            if (parseKeywordIf("ORDER BY"))
                s2 = s1.orderBy(parseList(',', c -> c.parseSortField()));

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
        if (parseProKeywordIf("ERROR"))
            return JSONValue.Behaviour.ERROR;
        else if (parseProKeywordIf("NULL"))
            return JSONValue.Behaviour.NULL;
        else if (parseProKeywordIf("DEFAULT"))
            return JSONValue.Behaviour.DEFAULT;
        else
            return null;
    }

    private final JSONExists.Behaviour parseJSONExistsOnErrorBehaviourIf() {
        if (!ignoreProEdition() && parseKeywordIf("ERROR") && parseKeyword("ON ERROR") && requireProEdition())
            return JSONExists.Behaviour.ERROR;
        else if (!ignoreProEdition() && parseKeywordIf("TRUE") && parseKeyword("ON ERROR") && requireProEdition())
            return JSONExists.Behaviour.TRUE;
        else if (!ignoreProEdition() && parseKeywordIf("FALSE") && parseKeyword("ON ERROR") && requireProEdition())
            return JSONExists.Behaviour.FALSE;
        else if (!ignoreProEdition() && parseKeywordIf("UNKNOWN") && parseKeyword("ON ERROR") && requireProEdition())
            return JSONExists.Behaviour.UNKNOWN;
        else
            return null;
    }

    private final DataType<?> parseJSONReturningIf() {
        return parseKeywordIf("RETURNING") ? parseDataType() : null;
    }

    private final Field<?> parseFieldJSONLiteralIf() {
        boolean jsonb = parseKeywordIf("JSONB");
        boolean json = !jsonb && parseKeywordIf("JSON");

        if (jsonb || json) {
            String s;

            if (parseIf('{')) {
                if (parseIf('}'))
                    return jsonb ? jsonbObject() : jsonObject();

                List<JSONEntry<?>> entries = parseList(',', ctx -> {
                    Field key = parseField();
                    parse(':');
                    return key(key).value(parseField());
                });

                parse('}');
                return jsonb ? jsonbObject(entries) : jsonObject(entries);
            }
            else if (parseIf('[')) {
                if (parseIf(']'))
                    return jsonb ? jsonbArray() : jsonArray();

                List<Field<?>> fields = parseList(',', c -> parseField());
                parse(']');
                return jsonb ? jsonbArray(fields) : jsonArray(fields);
            }
            else if ((s = parseStringLiteralIf()) != null) {
                return jsonb ? inline(jsonb(s)) : inline(json(s));
            }
            else
                throw expected("[", "{", "string literal");
        }

        return null;
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
                result = parseList(',', c -> c.parseField());
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

        if (parseFunctionNameIf("JSON_ARRAYAGG", "JSON_AGG", "JSON_GROUP_ARRAY") || (jsonb = parseFunctionNameIf("JSONB_AGG"))) {
            AggregateFilterStep<?> result;
            JSONArrayAggOrderByStep<?> s1;
            JSONArrayAggNullStep<?> s2;
            JSONArrayAggReturningStep<?> s3;
            JSONOnNull onNull;
            DataType<?> returning;

            parse('(');
            boolean distinct = parseSetQuantifier();
            result = s3 = s2 = s1 = jsonb
                ? distinct ? jsonbArrayAggDistinct(parseField()) : jsonbArrayAgg(parseField())
                : distinct ? jsonArrayAggDistinct(parseField()) : jsonArrayAgg(parseField());

            if (parseKeywordIf("ORDER BY"))
                result = s3 = s2 = s1.orderBy(parseList(',', c -> c.parseSortField()));

            if ((onNull = parseJSONNullTypeIf()) != null)
                result = s3 = onNull == ABSENT_ON_NULL ? s2.absentOnNull() : s2.nullOnNull();

            if ((returning = parseJSONReturningIf()) != null)
                result = s3.returning(returning);

            parse(')');
            return result;
        }

        return null;
    }

    private final Field<?> parseArrayLambdaFunction(Function2<Field, Lambda1, Field> function) {
        parse('(');
        Field f;
        Lambda1 l = parseLambdaIf(c -> c.parseField());

        if (l != null && parse(',')) {
            f = parseField();
        }
        else {
            f = parseField();
            parse(',');
            l = parseLambda(c -> c.parseField());
        }

        parse(')');
        return function.apply(f, l);
    }

    private final Lambda1<?, ?> parseLambda(Function<? super ParseContext, ? extends Field<?>> field) {
        Lambda1<?, ?> l = parseLambdaIf(field);

        if (l == null)
            throw expected("Lambda");

        return l;
    }

    private final Lambda1<?, ?> parseLambdaIf(Function<? super ParseContext, ? extends Field<?>> field) {
        int p = position();
        Name e = parseIdentifierIf();

        if (e != null && parseIf("->"))
            return lambda(field(e), (Field<?>) field.apply(this));
        else
            position(p);

        return null;
    }

    private final Field<?> parseFieldArrayConstructIf() {
        boolean absentOnNull = false;

        if ((parseFunctionNameIf("ARRAY_CONSTRUCT") || (absentOnNull = parseFunctionNameIf("ARRAY_CONSTRUCT_COMPACT"))) && requireProEdition()) {









        }

        return null;
    }

    private final Field<?> parseFieldObjectConstructIf() {
        boolean nullOnNull = false;

        if ((parseFunctionNameIf("OBJECT_CONSTRUCT") || (nullOnNull = parseFunctionNameIf("OBJECT_CONSTRUCT_KEEP_NULL"))) && requireProEdition()) {









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
        AggregateFilterStep<?> result;

        if (parseFunctionNameIf("JSON_OBJECTAGG", "JSON_OBJECT_AGG", "JSON_GROUP_OBJECT") || (jsonb = parseFunctionNameIf("JSONB_OBJECT_AGG"))) {
            JSONObjectAggNullStep<?> s1;
            JSONObjectAggReturningStep<?> s2;
            JSONOnNull onNull;
            DataType<?> returning;

            parse('(');
            parseKeywordIf("ALL");
            result = s2 = s1 = jsonb ? jsonbObjectAgg(parseJSONEntry()) : jsonObjectAgg(parseJSONEntry());

            if ((onNull = parseJSONNullTypeIf()) != null)
                result = s2 = onNull == ABSENT_ON_NULL ? s1.absentOnNull() : s1.nullOnNull();

            if ((returning = parseJSONReturningIf()) != null)
                result = s2.returning(returning);

            parse(')');
            return result;
        }
        else if (parseFunctionNameIf("OBJECT_AGG") && requireProEdition()) {






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
        return parseJSONEntry(true);
    }

    private final JSONEntry<?> parseJSONEntry(boolean supportKeyValue) {
        boolean valueRequired = supportKeyValue && parseKeywordIf("KEY");

        Field<String> key = (Field<String>) parseField();
        if (supportKeyValue && parseKeywordIf("VALUE"))
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
                    fields = parseList(',', c -> c.parseField());
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

    private final Field<?> parseFieldLogIf() {
        if (parseFunctionNameIf("LOG")) {
            parse('(');
            Field f1 = toField(parseOp());
            Field f2 = parseIf(',') ? toField(parseOp()) : null;
            parse(')');

            switch (parseFamily()) {











                case POSTGRES:
                case SQLITE:
                case YUGABYTEDB:
                    return f2 == null ? log10(f1) : log(f2, f1);

                default:
                    return f2 == null ? ln(f1) : log(f2, f1);
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
                    Field<?> arg2 = toField(parseOp());
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
            parse('(');
            Field arg1 = toField(parseOp());
            Field arg2 = parseIf(',') ? toField(parseOp()) : null;
            parse(')');

            return arg2 == null ? round(arg1) : round(arg1, arg2);
        }

        return null;
    }

    private final Field<?> parseFieldLeastIf() {
        if (parseFunctionNameIf("LEAST", "MINVALUE")) {
            parse('(');
            List<Field<?>> fields = parseList(',', c -> c.parseField());
            parse(')');

            return least(fields.get(0), fields.size() > 1 ? fields.subList(1, fields.size()).toArray(EMPTY_FIELD) : EMPTY_FIELD);
        }

        return null;
    }

    private final Field<?> parseFieldGreatestIf() {
        if (parseFunctionNameIf("GREATEST", "MAXVALUE")) {
            parse('(');
            List<Field<?>> fields = parseList(',', c -> c.parseField());
            parse(')');

            return greatest(fields.get(0), fields.size() > 1 ? fields.subList(1, fields.size()).toArray(EMPTY_FIELD) : EMPTY_FIELD);
        }

        return null;
    }

    private final Field<?> parseFieldGroupingIdIf() {
        if (parseProFunctionNameIf("GROUPING_ID")) {








        }

        return null;
    }

    private final Field<?> parseFieldTimestampLiteralIf() {
        int p = position();

        if (parseKeywordIf("TIMESTAMP")) {
            if (parseKeywordIf("WITHOUT TIME ZONE")) {
                return inline(parseTimestampLiteral());
            }
            else if (parseKeywordIf("WITH TIME ZONE")) {
                return inline(parseTimestampTZLiteral());
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
        Timestamp timestamp = Convert.convert(parseStringLiteral(), Timestamp.class);

        if (timestamp == null)
            throw exception("Illegal timestamp literal");

        return timestamp;
    }

    private final OffsetDateTime parseTimestampTZLiteral() {
        OffsetDateTime timestamp = Convert.convert(parseStringLiteral(), OffsetDateTime.class);

        if (timestamp == null)
            throw exception("Illegal timestamp literal");

        return timestamp;
    }

    private final Field<?> parseFieldTimeLiteralIf() {
        int p = position();

        if (parseKeywordIf("TIME")) {
            if (parseKeywordIf("WITHOUT TIME ZONE")) {
                return inline(parseTimeLiteral());
            }
            else if (parseKeywordIf("WITH TIME ZONE")) {
                return inline(parseTimeTZLiteral());
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
        Time time = Convert.convert(parseStringLiteral(), Time.class);

        if (time == null)
            throw exception("Illegal time literal");

        return time;
    }

    private final OffsetTime parseTimeTZLiteral() {
        OffsetTime time = Convert.convert(parseStringLiteral(), OffsetTime.class);

        if (time == null)
            throw exception("Illegal time literal");

        return time;
    }

    private final Field<?> parseFieldIntervalLiteralIf(boolean parseUnknownSyntaxAsIdentifier) {
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

                    if (parseUnknownSyntaxAsIdentifier)
                        return field(parseIdentifier());
                }
            }
        }

        return null;
    }

    private final Field<?> parseFieldMySQLIntervalLiteralIf(BiFunction<? super Field<?>, ? super DatePart, ? extends Field<?>> f) {
        if (parseKeywordIf("INTERVAL")) {
            Field<?> interval = parseField();
            DatePart part = parseIntervalDatePart();
            return f.apply(interval, part);
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
        else if (parseKeywordIf("YEAR_MONTH"))
            return requireNotNull(YearToMonth.yearToMonth(string), message);
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
        else if (parseKeywordIf("DAY_SECOND"))
            return requireNotNull(DayToSecond.dayToSecond(string), message);
        else if (parseKeywordIf("DAY_MINUTE"))
            return requireNotNull(DayToSecond.dayToMinute(string), message);
        else if (parseKeywordIf("DAY_HOUR"))
            return requireNotNull(DayToSecond.dayToHour(string), message);
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
        else if (parseKeywordIf("HOUR_SECOND"))
            return requireNotNull(DayToSecond.hourToSecond(string), message);
        else if (parseKeywordIf("HOUR_MINUTE"))
            return requireNotNull(DayToSecond.hourToMinute(string), message);
        else if (parseIntervalPrecisionKeywordIf("MINUTE"))
            if (parseKeywordIf("TO") && parseIntervalPrecisionKeywordIf("SECOND"))
                return requireNotNull(DayToSecond.minuteToSecond(string), message);
            else
                return requireNotNull(DayToSecond.minute(string), message);
        else if (parseKeywordIf("MINUTE_SECOND"))
            return requireNotNull(DayToSecond.minuteToSecond(string), message);
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
            boolean s = sub;

            parse('(');
            Field<?> d = parseField();

            // [#12025] In the absence of meta data, assume TIMESTAMP
            Field<?> date = d.getDataType().isDateTime() ? d : d.coerce(TIMESTAMP);
            parse(',');

            // [#8792] TODO: Support parsing interval expressions
            Field<?> interval = parseFieldIntervalLiteralIf(false);

            if (interval == null) {
                interval = parseFieldMySQLIntervalLiteralIf((i, p) -> s ? DSL.dateSub((Field) date, (Field) i, p) : DSL.dateAdd((Field) date, (Field) i, p));

                if (interval != null) {
                    parse(')');
                    return interval;
                }
            }
            else {
                parse(')');
                return s ? DSL.dateSub((Field) date, (Field) interval) : DSL.dateAdd((Field) date, (Field) interval);
            }
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
        if (parseFunctionNameIf("DATEPART", "DATE_PART")) {
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
                    parseKeywordIf("WEEK_OF_YEAR") ||
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
            Field<String> result = concat(parseList(',', c -> c.parseField()).toArray(EMPTY_FIELD));
            parse(')');
            return result;
        }

        return null;
    }

    private final Field<?> parseFieldOverlayIf() {
        if (parseFunctionNameIf("OVERLAY")) {
            parse('(');
            Field f1 = parseField();
            parseKeyword("PLACING");
            Field f2 = parseField();
            parseKeyword("FROM");
            Field<Number> f3 = (Field) parseField();
            Field<Number> f4 =
                parseKeywordIf("FOR")
              ? (Field) parseField()




              : null;
            parse(')');

            return f4 == null
                ? binary(f1, f2)
                    ? binaryOverlay(f1, f2, f3)
                    : overlay(f1, f2, f3)
                : binary(f1, f2)
                    ? binaryOverlay(f1, f2, f3, f4)
                    : overlay(f1, f2, f3, f4);
        }

        return null;
    }

    private final boolean binary(Field<?> f1) {
        return f1.getDataType().isBinary();
    }

    private final boolean binary(Field<?> f1, Field<?> f2) {
        return f1.getDataType().isBinary() || f2.getDataType().isBinary();
    }

    private final Field<?> parseFieldPositionIf() {
        if (parseFunctionNameIf("POSITION")) {
            parse('(');
            forbidden.add(FK_IN);
            Field f1 = parseField();

            if (parseIf(',')) {
                Field f2 = parseField();
                Field f3 = parseIf(',') ? parseField() : null;
                parse(')');

                return f3 == null
                     ? DSL.position(f1, f2)
                     : DSL.position(f1, f2, f3);
            }
            else {
                parseKeyword("IN");
                forbidden.remove(FK_IN);
                Field f2 = parseField();
                parse(')');

                return binary(f1, f2)
                     ? DSL.binaryPosition(f2, f1)
                     : DSL.position(f2, f1);
            }
        }

        return null;
    }

    private final Field<?> parseFieldLocateIf() {
        boolean locate = parseFunctionNameIf("LOCATE");
        if (locate || parseFunctionNameIf("LOCATE_IN_STRING")) {
            parse('(');
            Field f1 = parseField();
            parse(',');
            Field f2 = parseField();
            Field<Integer> f3 = (Field) (parseIf(',') ? parseField() : null);
            parse(')');


















            if (locate)
                return f3 == null
                     ? binary(f1, f2)
                         ? DSL.binaryPosition(f2, f1)
                         : DSL.position(f2, f1)
                     : binary(f1, f2)
                         ? DSL.binaryPosition(f2, f1, f3)
                         : DSL.position(f2, f1, f3);
            else
                return f3 == null
                     ? binary(f1, f2)
                         ? DSL.binaryPosition(f1, f2)
                         : DSL.position(f1, f2)
                     : binary(f1, f2)
                         ? DSL.binaryPosition(f1, f2, f3)
                         : DSL.position(f1, f2, f3);
        }

        return null;
    }

    private final Field<?> parseFieldRegexpReplaceIf() {
        boolean all = parseFunctionNameIf("REGEXP_REPLACE_ALL", "replaceRegexpAll");
        boolean first = !all && parseFunctionNameIf("REGEXP_REPLACE_FIRST", "replaceRegexpOne");
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
                    case YUGABYTEDB:
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
        boolean dbmslob = !substr && parseProFunctionNameIf("DBMS_LOB.SUBSTR");

        if (substring || substr) {
            boolean keywords = !substr;
            parse('(');
            Field f1 = parseField();
            if (substr || !(keywords = parseKeywordIf("FROM")))
                parse(',');
            Field f2 = toField(parseOp());
            Field f3 =
                    ((keywords && parseKeywordIf("FOR")) || (!keywords && parseIf(',')))
                ? (Field) toField(parseOp())
                : null;
            parse(')');

            return f3 == null
                ? binary(f1)
                    ? DSL.binarySubstring(f1, f2)
                    : DSL.substring(f1, f2)
                : binary(f1)
                    ? DSL.binarySubstring(f1, f2, f3)
                    : DSL.substring(f1, f2, f3);
        }










        return null;
    }

    private final Field<?> parseFieldTrimIf() {
        if (parseFunctionNameIf("TRIM")) {
            parse('(');
            int p = position();

            boolean leading = parseKeywordIf("LEADING", "L");
            boolean trailing = !leading && parseKeywordIf("TRAILING", "T");
            boolean both = !leading && !trailing && parseKeywordIf("BOTH", "B");

            if (leading || trailing || both) {
                if (parseIf(',') || parseIf(')')) {
                    position(p);
                }
                else if (parseKeywordIf("FROM")) {
                    Field<String> f = (Field) parseField();
                    parse(')');

                    return leading
                         ? ltrim(f)
                         : trailing
                         ? rtrim(f)
                         : trim(f);
                }
            }

            if (parseKeywordIf("FROM")) {
                if (parseIf(',') || parseIf(')')) {
                    position(p);
                }
                else {
                    Field<String> f = (Field) parseField();
                    parse(')');
                    return trim(f);
                }
            }

            Field f1 = parseField();

            if (parseKeywordIf("FROM")) {
                Field f2 = parseField();
                parse(')');

                return leading
                     ? binary(f1, f2)
                         ? binaryLtrim(f2, f1)
                         : ltrim(f2, f1)
                     : trailing
                     ? binary(f1, f2)
                         ? binaryRtrim(f2, f1)
                         : rtrim(f2, f1)
                     : binary(f1, f2)
                         ? binaryTrim(f2, f1)
                         : trim(f2, f1);
            }
            else {
                Field f2 = parseIf(',') ? parseField() : null;
                parse(')');

                return f2 == null
                     ? trim(f1)
                     : binary(f1, f2)
                         ? binaryTrim(f1, f2)
                         : trim(f1, f2);
            }
        }

        return null;
    }

    private final Field<?> parseFieldTranslateIf() {
        if (parseFunctionNameIf("TRANSLATE", "OTRANSLATE")) {





            return parseFunctionArgs3(DSL::translate);
        }

        return null;
    }

    private final Field<?> parseFieldDecodeIf() {
        if (parseFunctionNameIf("DECODE", "DECODE_ORACLE", "MAP")) {
            parse('(');
            List<Field<?>> fields = parseList(',', c -> c.parseField());
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
        if (parseFunctionNameIf("CHOOSE", "ELT")) {
            parse('(');
            Field<Integer> index = (Field<Integer>) parseField();
            parse(',');
            List<Field<?>> fields = parseList(',', c -> c.parseField());
            parse(')');

            return DSL.choose(index, fields.toArray(EMPTY_FIELD));
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

    private final Field<?> parseFieldCoalesceIf() {
        if (parseFunctionNameIf("COALESCE")) {
            parse('(');
            List<Field<?>> fields = parseList(',', c -> c.parseField());
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
            List<Field<?>> f2 = parseList(',', c -> c.parseField());
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
        boolean tryCast = !cast && !coerce && parseFunctionNameIf("TRY_CAST", "SAFE_CAST");

        if (cast || coerce || tryCast) {
            parse('(');
            Field<?> field = parseField();
            parseKeyword("AS");
            DataType<?> type = parseCastDataType(false);

            if (!tryCast)
                tryCast = parseKeywordIf("DEFAULT NULL ON CONVERSION ERROR");

            parse(')');

            return tryCast
                 ? tryCast(field, type)
                 : cast
                 ? cast(field, type)
                 : coerce(field, type);
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
            if (!ignoreProEdition() && parseIf(',') && requireProEdition())
                style = parseUnsignedIntegerLiteral();
            parse(')');

            if (style == null)
                return cast(field, type);




        }

        return null;
    }

    private final Field<?> parseBooleanValueExpressionIf() {
        TruthValue truth = parseTruthValueIf();

        if (truth != null) {
            switch (truth) {
                case T_TRUE:
                    return inline(true);
                case T_FALSE:
                    return inline(false);

                // [#16368] We cannot decide the data type at this point
                case T_NULL:
                    return inline(null, OTHER);
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
            over = filter = parseMinMaxByFunctionIf();
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

        if (keep != null && filter != null && !basic && !ignoreProEdition() && parseKeywordIf("KEEP")) {
            requireProEdition();

















        }
        else if (filter != null && !basic && parseKeywordIf("FILTER")) {
            result = over = parseAggregateFilter(filter);
        }
        else if (filter != null)
            result = filter;
        else
            result = over;

        if (!basic && parseKeywordIf("OVER")) {
            Object nameOrSpecification = parseWindowNameOrSpecification(filter != null);

            if (nameOrSpecification instanceof Name n)
                result = over.over(n);
            else if (nameOrSpecification instanceof WindowSpecification w)
                result = over.over(w);
            else
                result = over.over();
        }

        return result;
    }

    private final Field<?> parseAggregateFilterIf(AggregateFilterStep<?> filter) {
        return parseKeywordIf("FILTER") ? parseAggregateFilter(filter) : filter;
    }

    private final WindowBeforeOverStep<?> parseAggregateFilter(AggregateFilterStep<?> filter) {
        parse('(');
        parseKeyword("WHERE");
        Condition condition = parseCondition();
        parse(')');

        return filter.filterWhere(condition);
    }

    private final Field<?> parseSpecialAggregateFunctionIf() {
        if (parseFunctionNameIf("GROUP_CONCAT")) {
            parse('(');

            GroupConcatOrderByStep s1;
            GroupConcatSeparatorStep s2;
            AggregateFunction<String> s3;

            if (parseKeywordIf("DISTINCT"))
                s1 = DSL.groupConcatDistinct(parseField());
            else if (parseKeywordIf("ALL") || true)
                s1 = DSL.groupConcat(parseField());

            if (parseKeywordIf("ORDER BY"))
                s2 = s1.orderBy(parseList(',', c -> c.parseSortField()));
            else
                s2 = s1;

            if (parseKeywordIf("SEPARATOR"))
                s3 = s2.separator((Field) parseField());
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
            else
                parseKeywordIf("ALL");








            // Hypothetical set function
            List<Field<?>> args = parseList(',', c -> c.parseField());
            parse(')');
            return parseAggregateFilterIf(rank(args).withinGroupOrderBy(parseWithinGroupN()));
        }

        return null;
    }

    private final Field<?> parseFieldDenseRankIf() {
        if (parseFunctionNameIf("DENSE_RANK", "DENSERANK")) {
            parse('(');

            if (parseIf(')'))
                return parseWindowFunction(null, null, denseRank());
            else
                parseKeywordIf("ALL");

            // Hypothetical set function
            List<Field<?>> args = parseList(',', c -> c.parseField());
            parse(')');
            return parseAggregateFilterIf(denseRank(args).withinGroupOrderBy(parseWithinGroupN()));
        }

        return null;
    }

    private final Field<?> parseFieldPercentRankIf() {
        if (parseFunctionNameIf("PERCENT_RANK")) {
            parse('(');

            if (parseIf(')'))
                return parseWindowFunction(null, null, percentRank());
            else
                parseKeywordIf("ALL");

            // Hypothetical set function
            List<Field<?>> args = parseList(',', c -> c.parseField());
            parse(')');
            return parseAggregateFilterIf(percentRank(args).withinGroupOrderBy(parseWithinGroupN()));
        }

        return null;
    }

    private final Field<?> parseFieldCumeDistIf() {
        if (parseFunctionNameIf("CUME_DIST")) {
            parse('(');

            if (parseIf(')'))
                return parseWindowFunction(null, null, cumeDist());
            else
                parseKeywordIf("ALL");

            // Hypothetical set function
            List<Field<?>> args = parseList(',', c -> c.parseField());
            parse(')');
            return parseAggregateFilterIf(cumeDist(args).withinGroupOrderBy(parseWithinGroupN()));
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
        boolean lead = parseFunctionNameIf("LEAD", "leadInFrame");
        boolean lag = !lead && parseFunctionNameIf("LAG", "lagInFrame");

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

        return nameOrSpecification instanceof Name n
            ? s3.over(n)
            : nameOrSpecification instanceof WindowSpecification w
            ? s3.over(w)
            : s3.over();
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
                    return parseBinarySetFunction(DSL::corr);
                else if (parseFunctionNameIf("COVAR_POP", "covarPop"))
                    return parseBinarySetFunction(DSL::covarPop);
                else if (parseFunctionNameIf("COVAR_SAMP", "covarSamp"))
                    return parseBinarySetFunction(DSL::covarSamp);

                break;

            case 'R':
                if (parseFunctionNameIf("REGR_AVGX"))
                    return parseBinarySetFunction(DSL::regrAvgX);
                else if (parseFunctionNameIf("REGR_AVGY"))
                    return parseBinarySetFunction(DSL::regrAvgY);
                else if (parseFunctionNameIf("REGR_COUNT"))
                    return parseBinarySetFunction(DSL::regrCount);
                else if (parseFunctionNameIf("REGR_INTERCEPT"))
                    return parseBinarySetFunction(DSL::regrIntercept);
                else if (parseFunctionNameIf("REGR_R2"))
                    return parseBinarySetFunction(DSL::regrR2);
                else if (parseFunctionNameIf("REGR_SLOPE"))
                    return parseBinarySetFunction(DSL::regrSlope);
                else if (parseFunctionNameIf("REGR_SXX"))
                    return parseBinarySetFunction(DSL::regrSXX);
                else if (parseFunctionNameIf("REGR_SXY"))
                    return parseBinarySetFunction(DSL::regrSXY);
                else if (parseFunctionNameIf("REGR_SYY"))
                    return parseBinarySetFunction(DSL::regrSYY);

                break;
        }

        return null;
    }

    private final AggregateFunction<?> parseBinarySetFunction(BiFunction<? super Field<? extends Number>, ? super Field<? extends Number>, ? extends AggregateFunction<?>> function) {
        parse('(');
        parseKeywordIf("ALL");
        Field<? extends Number> arg1 = (Field) parseField();
        parse(',');
        Field<? extends Number> arg2 = (Field) parseField();
        parse(')');

        return function.apply(arg1, arg2);
    }

    private final AggregateFilterStep<?> parseOrderedSetFunctionIf() {
        OrderedAggregateFunction<?> orderedN;
        OrderedAggregateFunctionOfDeferredType ordered1;
        boolean optionalWithinGroup = false;

        orderedN = parseHypotheticalSetFunctionIf();
        if (orderedN == null) {
            InverseDistributionFunction idf = parseInverseDistributionFunctionIf();

            if (idf != null)
                if (idf.field() != null)
                    return idf.field();
                else
                    orderedN = idf.ordered();
        }

        if (orderedN == null)
            optionalWithinGroup = (orderedN = parseListaggFunctionIf()) != null;
        if (orderedN != null)
            return orderedN.withinGroupOrderBy(parseWithinGroupN(optionalWithinGroup));

        ordered1 = parseModeIf();
        if (ordered1 != null)
            return ordered1.withinGroupOrderBy(parseWithinGroup1());

        return null;
    }

    private final AggregateFilterStep<?> parseMinMaxByFunctionIf() {
        boolean minBy = parseFunctionNameIf("MIN_BY", "ARG_MIN", "argMin");
        boolean maxBy = !minBy && parseFunctionNameIf("MAX_BY", "ARG_MAX", "argMax");

        if (minBy || maxBy) {
            parse('(');
            parseSetQuantifier();
            Field<?> f1 = parseField();
            parse(',');
            Field<?> f2 = parseField();
            List<SortField<?>> sort = parseAggregateOrderByIf();
            parse(')');

            OptionallyOrderedAggregateFunction<?> s1 = minBy ? minBy(f1, f2) : maxBy(f1, f2);
            return sort == null ? s1 : s1.orderBy(sort);
        }

        return null;
    }

    private final List<SortField<?>> parseAggregateOrderBy() {
        List<SortField<?>> sort = parseAggregateOrderByIf();

        if (sort == null)
            throw expected("ORDER BY");

        return sort;
    }

    private final List<SortField<?>> parseAggregateOrderByIf() {
        List<SortField<?>> sort = null;

        if (parseKeywordIf("ORDER BY"))
            sort = parseList(',', c -> c.parseSortField());

        return sort;
    }

    private final AggregateFilterStep<?> parseArrayAggFunctionIf() {
        if (parseKeywordIf("ARRAY_AGG", "groupArray")) {
            parse('(');

            boolean distinct = parseSetQuantifier();
            Field<?> a1 = parseField();
            List<SortField<?>> sort = parseAggregateOrderByIf();
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
            parseKeywordIf("ALL");
            List<Field<?>> fields = parseList(',', c -> c.parseField());
            List<SortField<?>> sort = parseAggregateOrderByIf();
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
        List<SortField<?>> result = parseList(',', c -> c.parseSortField());
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
            ordered = rank(parseList(',', c -> c.parseField()));
            parse(')');
        }
        else if (parseFunctionNameIf("DENSE_RANK")) {
            parse('(');
            ordered = denseRank(parseList(',', c -> c.parseField()));
            parse(')');
        }
        else if (parseFunctionNameIf("PERCENT_RANK")) {
            parse('(');
            ordered = percentRank(parseList(',', c -> c.parseField()));
            parse(')');
        }
        else if (parseFunctionNameIf("CUME_DIST")) {
            parse('(');
            ordered = cumeDist(parseList(',', c -> c.parseField()));
            parse(')');
        }
        else
            ordered = null;

        return ordered;
    }

    private static final record InverseDistributionFunction(OrderedAggregateFunction<BigDecimal> ordered, AggregateFilterStep<?> field) {}

    private final InverseDistributionFunction parseInverseDistributionFunctionIf() {
        if (parseFunctionNameIf("PERCENTILE_CONT"))
            return parseInverseDistributionFunctionIf0(DSL::percentileCont);
        else if (parseFunctionNameIf("PERCENTILE_DISC"))
            return parseInverseDistributionFunctionIf0(DSL::percentileDisc);
        else
            return null;
    }

    private final InverseDistributionFunction parseInverseDistributionFunctionIf0(Function<? super Field, ? extends OrderedAggregateFunction<BigDecimal>> f) {
        parse('(');
        parseKeywordIf("ALL");
        Field f1 = parseField();
        Field f2 = parseIf(',') ? parseField() : null;

        if (f2 != null)
            parseKeywordIf("IGNORE NULLS");

        parse(')');

        return f2 == null
            ? new InverseDistributionFunction(f.apply(f1), null)
            : new InverseDistributionFunction(null, f.apply(f2).withinGroupOrderBy(f1));
    }

    private final OrderedAggregateFunction<?> parseListaggFunctionIf() {
        OrderedAggregateFunction<?> ordered;

        if (parseFunctionNameIf("LISTAGG", "STRING_AGG")) {
            parse('(');
            boolean distinct = parseSetQuantifier();
            Field<?> field = parseField();

            if (parseIf(','))
                ordered = distinct
                    ? binary(field)
                        ? binaryListAggDistinct(field, (Field) parseField())
                        : listAggDistinct(field, (Field) parseField())
                    : binary(field)
                        ? binaryListAgg(field, (Field) parseField())
                        : listAgg(field, (Field) parseField());
            else
                ordered = distinct
                    ? binary(field)
                        ? binaryListAggDistinct(field)
                        : listAggDistinct(field)
                    : binary(field)
                        ? binaryListAgg(field)
                        : listAgg(field);

            if (parseKeywordIf("ORDER BY"))
                ordered.withinGroupOrderBy(parseList(',', c -> c.parseSortField()));

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
        return parseGeneralSetFunctionIf(parseComputationalOperationIf());
    }

    private final Field<?> parseGeneralSetFunctionIf(ComputationalOperation operation) {
        boolean distinct;
        Field arg;

        if (operation == null)
            return null;

        parse('(');

        switch (operation) {
            case ANY:
            case ANY_VALUE:
            case AVG:
            case EVERY:
            case MAX:
            case MIN:
            case SUM:
            case PRODUCT:
                distinct = parseSetQuantifier();
                break;
            default:
                parseKeywordIf("ALL");
                distinct = false;
                break;
        }

        arg = parseField();

        switch (operation) {
            case MAX:
            case MIN: {
                if (!distinct && parseIf(',')) {
                    List<Field<?>> fields = parseList(',', c -> c.parseField());
                    parse(')');

                    return operation == ComputationalOperation.MAX ? greatest(arg, fields.toArray(EMPTY_FIELD)) : least(arg, fields.toArray(EMPTY_FIELD));
                }
            }

            case PRODUCT: {
                if (!distinct && parseIf(',')) {
                    Field<?> result = arg.mul(parseField());
                    parse(')');

                    return result;
                }
            }

            case ANY_VALUE: {
                if (parseKeywordIf("HAVING")) {
                    boolean min = parseKeywordIf("MIN");

                    if (!min)
                        parseKeyword("MAX");

                    Field<?> f = parseField();
                    List<SortField<?>> sort = parseAggregateOrderByIf();
                    parse(')');

                    OptionallyOrderedAggregateFunction<?> s1 = min ? minBy(arg, f) : maxBy(arg, f);
                    return sort == null ? s1 : s1.orderBy(sort);
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
            if (parseIf(')'))
                return count();

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
                        ? parseList(',', c -> c.parseField()).toArray(EMPTY_FIELD)
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
            String first = name.first();
            String last = name.last();

            if ("NEXTVAL".equalsIgnoreCase(last))
                return sequence(name.qualifier()).nextval();
            else if ("CURRVAL".equalsIgnoreCase(last))
                return sequence(name.qualifier()).currval();
            else if (TRUE.equals(data(DATA_PARSE_ON_CONFLICT)) && "EXCLUDED".equalsIgnoreCase(first))
                return excluded(field(name.unqualifiedName()));
        }

        unknownFunctions:
        if (dsl.settings().getParseUnknownFunctions() == ParseUnknownFunctions.IGNORE && peek('(') && !peekTokens('(', '+', ')')) {
            int p = position();
            List<Field<?>> arguments;

            parse('(');
            if (!parseIf(')')) {













                arguments = parseList(',', c -> c.parseField());
                parse(')');
            }
            else
                arguments = new ArrayList<>();

            // [#10107] Completely ignore functions in the DDLDatabase
            return isDDLDatabase()
                 ? inline((Object) null)
                 : function(name, Object.class, arguments);
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
        return collation(parseNameOrStringLiteral());
    }

    private final CharacterSet parseCharacterSet() {
        return characterSet(parseNameOrStringLiteral());
    }

    public final Name parseNameOrStringLiteral() {
        Name result = parseNameIf();

        if (result == null)
            return name(parseStringLiteral());
        else
            return result;
    }

    private final List<Name> parseNames() {
        return parseUniqueList("name", ',', c -> parseName());
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
        if (peek('.') && !peek(".."))
            return parseNameQualified('.', identifier);




        else
            return identifier;
    }

    private final Name parseNameQualified(char separator, Name firstPart) {
        List<Name> result = new ArrayList<>();
        result.add(firstPart);

        while (parseIf(separator))
            result.add(parseIdentifier());

        return DSL.name(result.toArray(EMPTY_NAME));
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
                    return lookupQualifiedAsterisk(positionBeforeName, result == null ? i1 : DSL.name(result.toArray(EMPTY_NAME)));
                }
            }
            while (parseIf('.'));
        }

        position(positionBeforeName);
        return null;
    }

    private final List<Name> parseIdentifiers() {
        return parseUniqueList("identifier", ',', c -> parseIdentifier());
    }

    @Override
    public final Name parseIdentifier() {
        return parseIdentifier(false, false);
    }

    private final Name parseIdentifier(boolean allowAposQuotes, boolean allowPartAsStart) {
        Name result = parseIdentifierIf(allowAposQuotes, allowPartAsStart);

        if (result == null)
            throw expected("Identifier");

        return result;
    }

    @Override
    public final Name parseIdentifierIf() {
        return parseIdentifierIf(false, false);
    }

    private final Name parseIdentifierIf(boolean allowAposQuotes, boolean allowPartAsStart) {
        char quoteEnd = parseQuote(allowAposQuotes);
        boolean quoted = quoteEnd != 0;

        int start = position();
        StringBuilder sb = new StringBuilder();
        char c;
        if (quoted) {
            while ((c = character()) != quoteEnd && hasMore() && positionInc() || character(position + 1) == quoteEnd && hasMore(1) && positionInc(2))
                sb.append(c);
        }
        else {
            if (allowPartAsStart ? isIdentifierPart() : isIdentifierStart()) {
                do {
                    sb.append(character());
                    positionInc();
                }
                while (isIdentifierPart() && hasMore());
            }
        }

        if (position() == start)
            return null;

        String name = normaliseNameCase(configuration(), sb.toString(), quoted, locale);

        if (quoted) {
            if (character() != quoteEnd)
                throw exception("Quoted identifier must terminate in " + quoteEnd + ". Start of identifier: " + StringUtils.abbreviate(sb.toString(), 30));

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

    private final char peekQuote(boolean allowAposQuotes, int p) {
        return peek('"', p) ? '"'
             : peek('`', p) ? '`'
             : peek('[', p) ? ']'
             : allowAposQuotes && peek('\'', p) ? '\''
             : 0;
    }

    private final DataType<?> parseCastDataType(boolean numericOnly) {
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

        return parseDataType(numericOnly);
    }

    @Override
    public final DataType<?> parseDataType() {
        return parseDataType(false);
    }

    private final DataType<?> parseDataType(boolean numericOnly) {
        DataType<?> result = parseDataTypeIf(true, numericOnly);

        if (result == null)
            if (numericOnly)
                throw expected("Numeric data type");
            else
                throw expected("Data type");

        return result;
    }

    private final DataType<?> parseDataTypeIf(boolean parseUnknownTypes) {
        return parseDataTypeIf(parseUnknownTypes, false);
    }

    private final DataType<?> parseDataTypeIf(boolean parseUnknownTypes, boolean numericOnly) {
        DataType<?> result = parseDataTypePrefixIf(parseUnknownTypes, numericOnly);

        if (result != null && !numericOnly) {
            boolean array;

            do {
                array = parseKeywordIf("ARRAY");

                if (parseIf('[')) {
                    parseUnsignedIntegerLiteralIf();
                    parse(']');

                    array = true;
                }

                if (array)
                    result = result.array();
            }
            while (array);
        }

        return result;
    }

    private final DataType<?> parseDataTypePrefixIf(boolean parseUnknownTypes, boolean parseNumericOnly) {
        boolean parseUnknownTypes0 = parseUnknownTypes = parseUnknownTypes && !parseNumericOnly;
        char character = characterUpper();

        if (character == '[' || character == '"' || character == '`')
            character = characterNextUpper();

        switch (character) {
            case 'A':
                if (!parseNumericOnly) {
                    if (parseKeywordOrIdentifierIf("ARRAY")) {
                        if (peek('('))
                            return parseParenthesised(c -> parseDataTypeIf(parseUnknownTypes0).array());
                        else if (peek('<'))
                            return parseParenthesised('<', c -> parseDataTypeIf(parseUnknownTypes0).array(), '>');
                        else
                            return OTHER.array();
                    }
                    else if (parseKeywordIf("AUTO_INCREMENT")) {
                        parseDataTypeIdentityArgsIf();
                        return INTEGER.identity(true);
                    }
                }

                break;

            case 'B':
                if (parseKeywordOrIdentifierIf("BIGINT")) {
                    return parseUnsigned(parseAndIgnoreDataTypeLength(BIGINT));
                }
                else if (!parseNumericOnly) {
                    if (parseKeywordOrIdentifierIf("BIGSERIAL"))
                        return BIGINT.identity(true);
                    else if (parseKeywordOrIdentifierIf("BINARY"))
                        if (parseKeywordIf("VARYING"))
                            return parseDataTypeLength(VARBINARY);
                        else
                            return parseDataTypeLength(BINARY);
                    else if (parseKeywordOrIdentifierIf("BIT"))
                        return parseDataTypeLength(BIT);
                    else if (parseKeywordOrIdentifierIf("BLOB"))
                        if (parseKeywordIf("SUB_TYPE"))
                            if (parseKeywordIf("0", "BINARY"))
                                return parseDataTypeLength(BLOB);
                            else if (parseKeywordIf("1", "TEXT"))
                                return parseDataTypeLength(CLOB);
                            else
                                throw expected("0", "BINARY", "1", "TEXT");
                        else
                            return parseDataTypeLength(BLOB);
                    else if (parseKeywordOrIdentifierIf("BOOLEAN") ||
                             parseKeywordOrIdentifierIf("BOOL"))
                        return BOOLEAN;
                    else if (parseKeywordOrIdentifierIf("BYTEA"))
                        return BLOB;
                }

                break;

            case 'C':
                if (!parseNumericOnly) {
                    if (parseKeywordOrIdentifierIf("CHAR") ||
                        parseKeywordOrIdentifierIf("CHARACTER"))
                        if (parseKeywordIf("VARYING"))
                            return parseDataTypeCollation(parseDataTypeLength(VARCHAR, VARBINARY, () -> parseKeywordIf("FOR BIT DATA")));
                        else if (parseKeywordIf("LARGE OBJECT"))
                            return parseDataTypeCollation(parseDataTypeLength(CLOB));
                        else
                            return parseDataTypeCollation(parseDataTypeLength(CHAR, BINARY, () -> parseKeywordIf("FOR BIT DATA")));

                    // [#5934] [#10291] TODO: support as actual data type as well
                    else if (parseKeywordOrIdentifierIf("CITEXT"))
                        return parseDataTypeCollation(parseAndIgnoreDataTypeLength(CLOB));
                    else if (parseKeywordOrIdentifierIf("CLOB"))
                        return parseDataTypeCollation(parseDataTypeLength(CLOB));
                }

                break;

            case 'D':
                if (!parseNumericOnly && parseKeywordOrIdentifierIf("DATE"))
                    return DATE;
                else if (!parseNumericOnly && parseKeywordOrIdentifierIf("DATETIME"))
                    return parseDataTypePrecisionIf(TIMESTAMP);
                else if (parseKeywordOrIdentifierIf("DECIMAL") ||
                         parseKeywordOrIdentifierIf("DEC"))
                    return parseDataTypePrecisionScaleIf(DECIMAL);
                else if (parseKeywordOrIdentifierIf("DOUBLE PRECISION") ||
                         parseKeywordOrIdentifierIf("DOUBLE"))
                    return parseAndIgnoreDataTypePrecisionScaleIf(DOUBLE);

                break;

            case 'E':
                if (!parseNumericOnly && parseKeywordOrIdentifierIf("ENUM"))
                    return parseDataTypeCollation(parseDataTypeEnum());

                break;

            case 'F':
                if (parseKeywordOrIdentifierIf("FLOAT"))
                    return parseAndIgnoreDataTypePrecisionScaleIf(FLOAT);

                break;

            case 'G':
                if (!parseNumericOnly) {
                    if (!ignoreProEdition()
                        && (parseKeywordOrIdentifierIf("GEOMETRY") || parseKeywordOrIdentifierIf("SDO_GEOMETRY"))
                        && requireProEdition()
                    ) {



                    }
                    else if (!ignoreProEdition() && parseKeywordOrIdentifierIf("GEOGRAPHY") && requireProEdition()) {



                    }
                }

                break;

            case 'I':
                if (parseKeywordOrIdentifierIf("INTEGER") ||
                    parseKeywordOrIdentifierIf("INT") ||
                    parseKeywordOrIdentifierIf("INT4"))
                    return parseUnsigned(parseAndIgnoreDataTypeLength(INTEGER));
                else if (parseKeywordOrIdentifierIf("INT2"))
                    return SMALLINT;
                else if (parseKeywordOrIdentifierIf("INT8"))
                    return BIGINT;
                else if (!parseNumericOnly && parseKeywordIf("INTERVAL")) {
                    if (parseKeywordIf("YEAR")) {
                        parseDataTypePrecisionIf();
                        parseKeyword("TO MONTH");
                        return INTERVALYEARTOMONTH;
                    }
                    else if (parseKeywordIf("DAY")) {
                        parseDataTypePrecisionIf();
                        parseKeyword("TO SECOND");
                        parseDataTypePrecisionIf();
                        return INTERVALDAYTOSECOND;
                    }
                    else
                        return INTERVAL;
                }
                else if (!parseNumericOnly && parseKeywordIf("IDENTITY")) {
                    parseDataTypeIdentityArgsIf();
                    return INTEGER.identity(true);
                }

                break;

            case 'J':
                if (!parseNumericOnly) {
                    if (parseKeywordOrIdentifierIf("JSON"))
                        return JSON;
                    else if (parseKeywordOrIdentifierIf("JSONB"))
                        return JSONB;
                }

                break;

            case 'L':
                if (!parseNumericOnly) {
                    if (parseKeywordOrIdentifierIf("LONGBLOB"))
                        return BLOB;
                    else if (parseKeywordOrIdentifierIf("LONGTEXT"))
                        return parseDataTypeCollation(CLOB);
                    else if (parseKeywordOrIdentifierIf("LONG NVARCHAR"))
                        return parseDataTypeCollation(parseDataTypeLength(LONGNVARCHAR));
                    else if (parseKeywordOrIdentifierIf("LONG VARBINARY") ||
                             parseKeywordOrIdentifierIf("LONGVARBINARY"))
                        return parseDataTypeCollation(parseDataTypeLength(LONGVARBINARY));
                    else if (parseKeywordOrIdentifierIf("LONG VARCHAR") ||
                             parseKeywordOrIdentifierIf("LONGVARCHAR"))
                        return parseDataTypeCollation(parseDataTypeLength(LONGVARCHAR, LONGVARBINARY, () -> parseKeywordIf("FOR BIT DATA")));
                }

                break;

            case 'M':
                if (parseKeywordOrIdentifierIf("MEDIUMINT"))
                    return parseUnsigned(parseAndIgnoreDataTypeLength(INTEGER));
                else if (!parseNumericOnly) {
                    if (parseKeywordOrIdentifierIf("MEDIUMBLOB"))
                        return BLOB;
                    else if (parseKeywordOrIdentifierIf("MEDIUMTEXT"))
                        return parseDataTypeCollation(CLOB);
                }

                break;

            case 'N':
                if (parseKeywordOrIdentifierIf("NUMBER") ||
                    parseKeywordOrIdentifierIf("NUMERIC")) {
                    return parseDataTypePrecisionScaleIf(NUMERIC);
                }
                else if (!parseNumericOnly) {
                    if (parseKeywordIf("NATIONAL CHARACTER") ||
                        parseKeywordIf("NATIONAL CHAR"))
                        if (parseKeywordIf("VARYING"))
                            return parseDataTypeCollation(parseDataTypeLength(NVARCHAR));
                        else if (parseKeywordIf("LARGE OBJECT"))
                            return parseDataTypeCollation(parseDataTypeLength(NCLOB));
                        else
                            return parseDataTypeCollation(parseDataTypeLength(NCHAR));
                    else if (parseKeywordOrIdentifierIf("NCHAR"))
                        if (parseKeywordIf("VARYING"))
                            return parseDataTypeCollation(parseDataTypeLength(NVARCHAR));
                        else if (parseKeywordIf("LARGE OBJECT"))
                            return parseDataTypeCollation(parseDataTypeLength(NCLOB));
                        else
                            return parseDataTypeCollation(parseDataTypeLength(NCHAR));
                    else if (parseKeywordOrIdentifierIf("NCLOB"))
                        return parseDataTypeCollation(parseDataTypeLength(NCLOB));
                    else if (parseKeywordOrIdentifierIf("NVARCHAR") ||
                             parseKeywordOrIdentifierIf("NVARCHAR2"))
                        return parseDataTypeCollation(parseDataTypeLength(NVARCHAR));
                    else if (parseKeywordOrIdentifierIf("NTEXT"))
                        return parseDataTypeCollation(parseAndIgnoreDataTypeLength(NCLOB));
                }

                break;

            case 'O':
                if (!parseNumericOnly && parseKeywordOrIdentifierIf("OTHER"))
                    return OTHER;

                break;

            case 'R':
                if (parseKeywordOrIdentifierIf("REAL"))
                    return parseAndIgnoreDataTypePrecisionScaleIf(REAL);

                break;

            case 'S':
                if (parseKeywordOrIdentifierIf("SMALLINT"))
                    return parseUnsigned(parseAndIgnoreDataTypeLength(SMALLINT));
                else if (!parseNumericOnly) {
                    if (parseKeywordOrIdentifierIf("SERIAL4") ||
                        parseKeywordOrIdentifierIf("SERIAL"))
                        return INTEGER.identity(true);
                    else if (parseKeywordOrIdentifierIf("SERIAL8"))
                        return BIGINT.identity(true);
                    else if (parseKeywordOrIdentifierIf("SET"))
                        return parseDataTypeCollation(parseDataTypeEnum());
                    else if (parseKeywordOrIdentifierIf("SMALLSERIAL") ||
                             parseKeywordOrIdentifierIf("SERIAL2"))
                        return SMALLINT.identity(true);
                    else if (parseKeywordOrIdentifierIf("STRING"))
                        return parseDataTypeCollation(parseDataTypeLength(VARCHAR));
                }

                break;

            case 'T':
                if (parseKeywordOrIdentifierIf("TINYINT"))
                    return parseUnsigned(parseAndIgnoreDataTypeLength(TINYINT));
                else if (!parseNumericOnly) {
                    if (parseKeywordOrIdentifierIf("TEXT"))
                        return parseDataTypeCollation(parseAndIgnoreDataTypeLength(CLOB));
                    else if (parseKeywordOrIdentifierIf("TIMESTAMPTZ"))
                        return parseDataTypePrecisionIf(TIMESTAMPWITHTIMEZONE);
                    else if (parseKeywordOrIdentifierIf("TIMESTAMP")) {
                        Integer precision = parseDataTypePrecisionIf();

                        if (parseKeywordOrIdentifierIf("WITH TIME ZONE"))
                            return precision == null ? TIMESTAMPWITHTIMEZONE : TIMESTAMPWITHTIMEZONE(precision);
                        else if (parseKeywordOrIdentifierIf("WITHOUT TIME ZONE") || true)
                            return precision == null ? TIMESTAMP : TIMESTAMP(precision);
                    }
                    else if (parseKeywordOrIdentifierIf("TIMETZ"))
                        return parseDataTypePrecisionIf(TIMEWITHTIMEZONE);
                    else if (parseKeywordOrIdentifierIf("TIME")) {
                        Integer precision = parseDataTypePrecisionIf();

                        if (parseKeywordOrIdentifierIf("WITH TIME ZONE"))
                            return precision == null ? TIMEWITHTIMEZONE : SQLDataType.TIMEWITHTIMEZONE(precision);
                        else if (parseKeywordOrIdentifierIf("WITHOUT TIME ZONE") || true)
                            return precision == null ? TIME : TIME(precision);
                    }
                    else if (parseKeywordOrIdentifierIf("TINYBLOB"))
                        return BLOB;
                    else if (parseKeywordOrIdentifierIf("TINYTEXT"))
                        return parseDataTypeCollation(CLOB);
                }

                break;

            case 'U':
                if (parseKeywordOrIdentifierIf("UTINYINT"))
                    return SQLDataType.TINYINTUNSIGNED;
                else if (parseKeywordOrIdentifierIf("USMALLINT"))
                    return SQLDataType.SMALLINTUNSIGNED;
                else if (parseKeywordOrIdentifierIf("UINTEGER"))
                    return SQLDataType.INTEGERUNSIGNED;
                else if (parseKeywordOrIdentifierIf("UBIGINT"))
                    return SQLDataType.BIGINTUNSIGNED;
                else if (!parseNumericOnly) {
                    if (parseKeywordOrIdentifierIf("UUID"))
                        return SQLDataType.UUID;
                    else if (parseKeywordOrIdentifierIf("UNIQUEIDENTIFIER"))
                        return SQLDataType.UUID;
                }

                break;

            case 'V':
                if (!parseNumericOnly) {
                    if (parseKeywordOrIdentifierIf("VARCHAR") ||
                        parseKeywordOrIdentifierIf("VARCHAR2") ||
                        // [#5934] [#10291] TODO: support as actual data type as well
                        parseKeywordOrIdentifierIf("VARCHAR_IGNORECASE"))
                        return parseDataTypeCollation(parseDataTypeLength(VARCHAR, VARBINARY, () -> parseKeywordIf("FOR BIT DATA")));
                    else if (parseKeywordOrIdentifierIf("VARBINARY"))
                        return parseDataTypeLength(VARBINARY);
                }

                break;

            case 'X':
                if (!parseNumericOnly && parseKeywordOrIdentifierIf("XML"))
                    return SQLDataType.XML;

                break;

            case 'Y':
                if (!parseNumericOnly && parseKeywordOrIdentifierIf("YEAR"))
                    return parseDataTypeLength(SQLDataType.YEAR);

                break;
        }

        Name name;
        if (parseUnknownTypes && (name = parseNameIf()) != null)
            return parseDataTypeLength(new DefaultDataType(dsl.dialect(), Object.class, name));
        else
            return null;
    }

    private final void parseDataTypeIdentityArgsIf() {
        if (parseIf('(')) {
            parseList(',', c -> c.parseField());
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
        return parseDataTypeLength(in, in, () -> false);
    }

    private final DataType<?> parseDataTypeLength(DataType<?> in, DataType<?> alternative, BooleanSupplier alternativeIfTrue) {
        Integer length = null;

        if (parseIf('(')) {
            if (!parseKeywordIf("MAX"))
                length = asInt(parseUnsignedIntegerLiteral());

            if (in == VARCHAR || in == CHAR)
                if (!parseKeywordIf("BYTE"))
                    parseKeywordIf("CHAR");

            parse(')');
        }

        DataType<?> result = alternativeIfTrue.getAsBoolean() ? alternative : in;
        return length == null ? result : result.length(length);
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
        if (parseKeywordIf("CHARACTER SET", "CHARSET")) {
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
                  + "    return null;\n"
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
                paramName = null;
                break;

            default:
                String prefix = defaultIfNull(settings().getParseNamedParamPrefix(), ":");

                if (parseIf(prefix, false)) {

                    // [#14594] ": param" is a valid placeholder in Oracle
                    if (":".equals(prefix))
                        parseWhitespaceIf();

                    Name identifier = parseIdentifier(false, true);
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
        Param<?> param;

        if (binding instanceof Val<?> v) {
            param = DSL.val0(
                v.getValue(),
                v.getDataType(),
                v.inferredDataType,
                bindIndex,
                paramName
            );
        }
        else if (binding instanceof Field<?> f) {
            return f;
        }
        else
            param = DSL.val0(
                binding,
                DSL.getDataType0((Class<?>) (binding != null ? binding.getClass() : Object.class)),
                true,
                bindIndex,
                paramName
            );

        if (bindParamListener != null)
            bindParams.put(paramName != null ? paramName : ("" + bindIndex), param);

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
            boolean result = !parseIf('0') && parseIf('1');

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
            char c2;

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
                    buffer.write(parseInt("" + c1 + c2, 16));
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
        for (int i = position(); i < chars.length; i++) {
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
        for (int i = position(); i < chars.length; i++) {
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
        for (int i = position(); i < chars.length; i++) {
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
        for (int i = position(); i < chars.length; i++) {
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
                            c1 = (char) parseInt(substring(i + 1, i + 5), 16);
                            i += 4;
                            break;

                        // Unicode character value UTF-32
                        case 'U':
                            sb.appendCodePoint(parseInt(substring(i + 1, i + 9), 16));
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
                                        c1 = (char) parseInt("" + c2 + c3 + c4, 8);
                                    }
                                    else {
                                        c1 = (char) parseInt("" + c2 + c3, 8);
                                    }
                                }
                                else {
                                    c1 = (char) parseInt("" + c2, 8);
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
            int p0 = position();
            parseDigits();

            // [#16330] Support implicit 0 exponents (e.g. 0e or 0.0e as supported by SQL Server)
            String s = substring(p, position());
            if (position() == p0)
                s = s + "0";

            parseWhitespaceIf();
            return sign == Sign.MINUS ? -parseDouble(s) : parseDouble(s);
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

    private final void parseDigits() {
        for (;;) {
            char c = character();

            if (c >= '0' && c <= '9')
                positionInc();
            else
                break;
        }
    }

    private final void parseDigitsOrSign() {
        char s = character();

        if (s == '-' || s == '+')
            positionInc();

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
        Number result = parseSignedIntegerLiteralIf0(false);
        return result == null ? null : result.longValue();
    }

    private final Number parseSignedIntegerLiteralIf0(boolean allowBigInteger) {
        int p = position();
        parseDigitsOrSign();

        if (p == position())
            return null;

        String s = substring(p, position());
        parseWhitespaceIf();

        try {
            return Long.valueOf(s);
        }
        catch (Exception e) {
            if (allowBigInteger)
                return new BigInteger(s);
            else
                throw e;
        }
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

    private final <T> List<T> parseUniqueList(String objectType, char separator, Function<? super ParseContext, ? extends T> element) {
        return parseUniqueList(objectType, c -> c.parseIf(separator), element);
    }

    private final <T> List<T> parseUniqueList(String objectType, String separator, Function<? super ParseContext, ? extends T> element) {
        return parseUniqueList(objectType, c -> c.parseIf(separator), element);
    }

    private final <T> List<T> parseUniqueList(String objectType, Predicate<? super ParseContext> separator, Function<? super ParseContext, ? extends T> element) {
        Set<T> result = new LinkedHashSet<>();

        do
            if (!result.add(element.apply(this)))
                throw exception("Duplicate " + objectType + " encountered: ");
        while (separator.test(this));

        return new ArrayList<>(result);
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

    private final Field<? extends Number> parseCastIntegerOrBindVariable0(
        Supplier<Number> l
    ) {
        Number i = l.get();

        if (i != null)
            return DSL.inline(i);

        Field<?> f = parseBindVariableIf();
        if (f != null)
            return (Field<Number>) f;

        throw expected("Integer or bind variable");
    }

    private final Field<? extends Number> parseCastIntegerOrBindVariable(
        Supplier<Number> l
    ) {
        if (parseFunctionNameIf("CAST")) {
            parse('(');
            Field<?> field = parseCastIntegerOrBindVariable0(l);
            parseKeyword("AS");
            parseCastDataType(true);
            parse(')');

            return (Field<? extends Number>) field;
        }
        else
            return parseCastIntegerOrBindVariable0(l);
    }

    private final Field<? extends Number> parseUnsignedIntegerOrBindVariable() {
        return parseCastIntegerOrBindVariable(() -> parseUnsignedIntegerLiteralIf0(true));
    }

    private final Field<? extends Number> parseSignedIntegerOrBindVariable() {
        return parseCastIntegerOrBindVariable(() -> parseSignedIntegerLiteralIf0(true));
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
        Number result = parseUnsignedIntegerLiteralIf0(false);
        return result == null ? null : result.longValue();
    }

    private final Number parseUnsignedIntegerLiteralIf0(boolean allowBigInteger) {
        int p = position();
        parseDigits();

        if (p == position())
            return null;

        String s = substring(p, position());
        parseWhitespaceIf();

        try {
            return Long.valueOf(s);
        }
        catch (Exception e) {
            if (allowBigInteger)
                return new BigInteger(s);
            else
                throw e;
        }
    }

    private static final record Join(JoinType type, JoinHint hint) {}

    private final Join parseJoinTypeIf() {
        JoinHint hint;

        if (parseKeywordIf("ANTI JOIN"))
            return new Join(JoinType.LEFT_ANTI_JOIN, null);
        else if (parseKeywordIf("CROSS")) {
            if (parseKeywordIf("JOIN"))
                return new Join(JoinType.CROSS_JOIN, null);
            else if (parseKeywordIf("APPLY"))
                return new Join(JoinType.CROSS_APPLY, null);
        }
        else if (parseKeywordIf("INNER") && asTrue(hint = parseJoinHintIf()) && parseKeyword("JOIN"))
            return new Join(JoinType.JOIN, hint);
        else if (parseKeywordIf("JOIN") && asTrue(hint = parseJoinHintIf()))
            return new Join(JoinType.JOIN, hint);
        else if (parseKeywordIf("LEFT")) {
            if (parseKeywordIf("SEMI") && parseKeyword("JOIN"))
                return new Join(JoinType.LEFT_SEMI_JOIN, null);
            else if (parseKeywordIf("ANTI") && parseKeyword("JOIN"))
                return new Join(JoinType.LEFT_ANTI_JOIN, null);
            else if ((parseKeywordIf("OUTER") || true) && asTrue(hint = parseJoinHintIf()) && parseKeyword("JOIN"))
                return new Join(JoinType.LEFT_OUTER_JOIN, hint);
        }
        else if (parseKeywordIf("RIGHT")) {
            if (parseKeywordIf("ANTI JOIN"))
                throw notImplemented("RIGHT ANTI JOIN");
            else if (parseKeywordIf("SEMI JOIN"))
                throw notImplemented("RIGHT SEMI JOIN");
            else if ((parseKeywordIf("OUTER") || true) && asTrue(hint = parseJoinHintIf()) && parseKeyword("JOIN"))
                return new Join(JoinType.RIGHT_OUTER_JOIN, hint);
        }
        else if (parseKeywordIf("FULL") && (parseKeywordIf("OUTER") || true) && asTrue(hint = parseJoinHintIf()) && parseKeyword("JOIN"))
            return new Join(JoinType.FULL_OUTER_JOIN, hint);
        else if (parseKeywordIf("OUTER APPLY"))
            return new Join(JoinType.OUTER_APPLY, null);
        else if (parseKeywordIf("NATURAL")) {
            if (parseKeywordIf("LEFT") && (parseKeywordIf("OUTER") || true) && parseKeyword("JOIN"))
                return new Join(JoinType.NATURAL_LEFT_OUTER_JOIN, null);
            else if (parseKeywordIf("RIGHT") && (parseKeywordIf("OUTER") || true) && parseKeyword("JOIN"))
                return new Join(JoinType.NATURAL_RIGHT_OUTER_JOIN, null);
            else if (parseKeywordIf("FULL") && (parseKeywordIf("OUTER") || true) && parseKeyword("JOIN"))
                return new Join(JoinType.NATURAL_FULL_OUTER_JOIN, null);
            else if ((parseKeywordIf("INNER") || true) && parseKeyword("JOIN"))
                return new Join(JoinType.NATURAL_JOIN, null);
        }
        else if (parseKeywordIf("SEMI JOIN"))
            return new Join(JoinType.LEFT_SEMI_JOIN, null);
        else if (parseKeywordIf("STRAIGHT_JOIN"))
            return new Join(JoinType.STRAIGHT_JOIN, null);

        return null;
        // TODO partitioned join
    }

    private final JoinHint parseJoinHintIf() {
        if (parseKeywordIf("HASH"))
            return JoinHint.HASH;
        else if (parseKeywordIf("LOOP", "LOOKUP"))
            return JoinHint.LOOP;
        else if (parseKeywordIf("MERGE"))
            return JoinHint.MERGE;
        else
            return null;
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
        else if (!intersectOnly && parseKeywordIf("EXCEPT", "MINUS"))
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
                else if (parseFunctionNameIf("ANY_VALUE", "ARBITRARY"))
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
                if (parseFunctionNameIf("MAX", "MAXIMUM"))
                    return ComputationalOperation.MAX;
                else if (parseFunctionNameIf("MEDIAN"))
                    return ComputationalOperation.MEDIAN;
                else if (parseFunctionNameIf("MIN", "MINIMUM"))
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
                else if (parseFunctionNameIf("STDDEV", "STDEVP", "STDDEV_POP", "stddevPop"))
                    return ComputationalOperation.STDDEV_POP;




                else if (parseFunctionNameIf("STDDEV_SAMP", "STDEV", "STDEV_SAMP", "stddevSamp"))
                    return ComputationalOperation.STDDEV_SAMP;

                break;

            case 'V':
                if (parseFunctionNameIf("VAR_POP", "VARIANCE", "VARP", "varPop"))
                    return ComputationalOperation.VAR_POP;




                else if (parseFunctionNameIf("VAR_SAMP", "VARIANCE_SAMP", "VAR", "varSamp"))
                    return ComputationalOperation.VAR_SAMP;

                break;
        }

        return null;
    }

    private final Comparator parseComparatorIf() {










        if (parseIf("==") || parseIf("=") || parseKeywordIf("EQ"))
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

    private enum TSQLOuterJoinComparator {
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

        for (; stop < chars.length; stop++) {
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

    private final boolean peekFunctionNameIf(String name) {
        return peekKeyword(name, false, false, true);
    }

    private final boolean parseProFunctionNameIf(String name) {
        return !ignoreProEdition() && parseFunctionNameIf(name) && requireProEdition();
    }

    private final boolean parseProFunctionNameIf(String name1, String name2) {
        return !ignoreProEdition() && parseFunctionNameIf(name1, name2) && requireProEdition();
    }

    private final boolean parseProFunctionNameIf(String name1, String name2, String name3) {
        return !ignoreProEdition() && parseFunctionNameIf(name1, name2, name3) && requireProEdition();
    }

    private final boolean parseProFunctionNameIf(String... names) {
        return !ignoreProEdition() && parseFunctionNameIf(names) && requireProEdition();
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

        if (chars.length < p + length)
            return false;

        int pos = afterWhitespace(p);

        for (int i = 0; i < length; i++, pos++)
            if (chars[pos] != operator.charAt(i))
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

    private final boolean parseKeywordUndocumentedAlternatives(String keyword, String undocumented1) {
        if (parseKeywordIf(undocumented1))
            return true;
        else
            return parseKeyword(keyword);
    }

    private final boolean parseProKeywordIf(String name) {
        return !ignoreProEdition() && parseKeywordIf(name) && requireProEdition();
    }

    private final boolean parseProKeywordIf(String name1, String name2) {
        return !ignoreProEdition() && parseKeywordIf(name1, name2) && requireProEdition();
    }

    private final boolean parseProKeywordIf(String name1, String name2, String name3) {
        return !ignoreProEdition() && parseKeywordIf(name1, name2, name3) && requireProEdition();
    }

    private final boolean parseProKeywordIf(String... names) {
        return !ignoreProEdition() && parseKeywordIf(names) && requireProEdition();
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

    private final boolean parseKeywordIf(String keyword1, String keyword2, String keyword3, String keyword4) {
        return parseKeywordIf(keyword1) || parseKeywordIf(keyword2) || parseKeywordIf(keyword3) || parseKeywordIf(keyword4);
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
        return Tools.findAny(keywords, k -> parseKeywordIf(k), k -> keyword(k.toLowerCase()));
    }

    private final Keyword parseAndGetKeywordIf(String keyword) {
        if (parseKeywordIf(keyword))
            return keyword(keyword.toLowerCase());

        return null;
    }

    private final boolean peekProKeyword(String... keywords) {
        return !ignoreProEdition() && peekKeyword(keywords) && requireProEdition();
    }

    private final boolean peekProKeyword(String keyword) {
        return !ignoreProEdition() && peekKeyword(keyword) && requireProEdition();
    }

    private final boolean peekProKeyword(String keyword1, String keyword2) {
        return !ignoreProEdition() && peekKeyword(keyword1, keyword2) && requireProEdition();
    }

    private final boolean peekProKeyword(String keyword1, String keyword2, String keyword3) {
        return !ignoreProEdition() && peekKeyword(keyword1, keyword2, keyword3) && requireProEdition();
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
        boolean caseSensitive =
               Character.isLowerCase(keyword.charAt(0))
            || Character.isLowerCase(keyword.charAt(keyword.length() - 1));
        int length = keyword.length();
        int p = position();

        if (chars.length < p + length)
            return false;

        int skip = afterWhitespace(p, peekIntoParens, false, icIgnore) - p;

        // [#18480] Function names are allowed to be quoted
        char quoteEnd = requireFunction ? peekQuote(false, p + skip) : 0;
        boolean quoted = quoteEnd != 0;
        ParseNameCase nameCase = null;

        if (quoted) {
            if (NO_SUPPORT_QUOTED_BUILT_IN_FUNCION_NAMES.contains(parseDialect()))
                return false;

            nameCase = parseNameCase(configuration);
            switch (parseDialect()) {






                default:
                    caseSensitive =
                           nameCase != ParseNameCase.AS_IS
                        && nameCase != ParseNameCase.LOWER;
                    break;
            }

            skip++;
        }

        for (int i = 0; i < length; i++) {
            char c = keyword.charAt(i);

            if (caseSensitive && (
                nameCase == ParseNameCase.LOWER_IF_UNQUOTED ||
                nameCase == ParseNameCase.LOWER))
                c = lower(c);

            int pos = p + i + skip;

            switch (c) {
                case ' ':
                    if (!Character.isWhitespace(character(pos)))
                        return false;

                    skip = skip + (afterWhitespace(pos) - pos - 1);
                    break;

                default:
                    if (caseSensitive) {
                        if (character(pos) != c)
                            return false;
                    }
                    else if (upper(character(pos)) != c)
                        return false;

                    break;
            }
        }

        int pos = p + length + skip;

        if (quoted) {
            if (character(pos) == quoteEnd) {
                pos++;
                skip++;
            }
            else
                return false;
        }

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

    private final boolean peekKeyword(KeywordLookup lookup) {
        int pos = afterWhitespace(position(), false, false, icIgnore);
        int p = lookup.lookup(chars, pos, i -> afterWhitespace(i, false, false, icIgnore));

        if (p == pos)
            return false;

        // [#8806] A keyword that is followed by a period is very likely an identifier
        if (isIdentifierPart(p) || character(p) == '.')
            return false;

        return true;
    }

    @Override
    final int afterWhitespace(int p) {
        return afterWhitespace(p, false, false, icIgnore);
    }

    private static final record IgnoreComment(
        boolean check,
        String start,
        String stop
    ) {}

    private final int afterWhitespace(int p, boolean peekIntoParens, boolean toggleMarkers, IgnoreComment ic) {

        // [#8074] The SQL standard and some implementations (e.g. PostgreSQL,
        //         SQL Server) support nesting block comments
        int p0 = p;
        int blockCommentNestLevel = 0;
        boolean ignoreComment = false;

        loop:
        for (int i = p; i < chars.length; i++) {
            switch (chars[i]) {
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
                    if (i + 1 < chars.length && chars[i + 1] == '*') {
                        i = i + 2;
                        blockCommentNestLevel++;

                        while (i < chars.length) {
                            if (!toggleMarkers && peekTemplateComment(i)) {
                                blockCommentNestLevel = 0;
                                break loop;
                            }
                            else if (!(ignoreComment = peekIgnoreComment(ignoreComment, ic, toggleMarkers, i))) {
                                switch (chars[i]) {
                                    case '/':
                                        if (i + 1 < chars.length && chars[i + 1] == '*') {
                                            i = i + 2;
                                            blockCommentNestLevel++;
                                        }

                                        break;

                                    case '+':
                                        if (!ignoreHints() && i + 1 < chars.length && ((chars[i + 1] >= 'A' && chars[i + 1] <= 'Z') || (chars[i + 1] >= 'a' && chars[i + 1] <= 'z'))) {
                                            blockCommentNestLevel = 0;
                                            break loop;
                                        }

                                        break;

                                    case '*':
                                        if (i + 1 < chars.length && chars[i + 1] == '/') {
                                            p = (i = i + 1) + 1;

                                            if (--blockCommentNestLevel == 0) {
                                                if (toggleMarkers && markerStart > -1) {
                                                    markerStart = p0;
                                                    markerStop = p;
                                                }

                                                continue loop;
                                            }
                                        }

                                        break;
                                }
                            }

                            i++;
                        }
                    }

                    // [#9651] H2 and Snowflake's c-style single line comments
                    else if (i + 1 < chars.length && chars[i + 1] == '/') {
                        i = i + 2;

                        while (i < chars.length) {
                            if (!toggleMarkers && peekTemplateComment(i)) {
                                break loop;
                            }
                            else if (!(ignoreComment = peekIgnoreComment(ignoreComment, ic, toggleMarkers, i))) {
                                switch (chars[i]) {
                                    case '\r':
                                    case '\n': {
                                        p = i + 1;

                                        if (toggleMarkers && markerStart > -1) {
                                            markerStart = p0;
                                            markerStop = p;
                                        }

                                        continue loop;
                                    }
                                }
                            }

                            i++;
                        }

                        p = i;
                    }

                    break loop;

                case '-':
                case '#':
                    if (chars[i] == '-' && i + 1 < chars.length && chars[i + 1] == '-' ||
                        chars[i] == '#' && SUPPORTS_HASH_COMMENT_SYNTAX.contains(parseDialect())) {

                        if (chars[i] == '-')
                            i = i + 2;
                        else
                            i++;

                        while (i < chars.length) {
                            if (!toggleMarkers && peekTemplateComment(i)) {
                                break loop;
                            }
                            else if (!(ignoreComment = peekIgnoreComment(ignoreComment, ic, toggleMarkers, i))) {
                                switch (chars[i]) {
                                    case '\r':
                                    case '\n': {
                                        p = i + 1;

                                        if (toggleMarkers && markerStart > -1) {
                                            markerStart = p0;
                                            markerStop = p;
                                        }

                                        continue loop;
                                    }
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

    private boolean peekTemplateComment(int i) {
        return peekIgnoreComment(false, icTemplate, false, i)
            || peekIgnoreComment(false, icRaw, false, i);
    }

    private final boolean peekIgnoreComment(
        boolean ignoreComment,
        IgnoreComment check,
        boolean toggleMarkers,
        int i
    ) {
        if (check.check()) {
            if (!ignoreComment) {
                if ((ignoreComment = peek(check.start(), i)) && toggleMarkers)
                    markerStart = i;
            }
            else {
                if (!(ignoreComment = !peek(check.stop(), i)) && toggleMarkers)
                    markerStop = i;
            }
        }

        return ignoreComment;
    }

    private enum TruthValue {
        T_TRUE,
        T_FALSE,
        T_NULL
    }

    private enum ComputationalOperation {
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

    private static final KeywordLookup KEYWORD_LOOKUP_IN_STATEMENTS = KeywordLookup.from(KEYWORDS_IN_STATEMENTS);

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

    private static final KeywordLookup KEYWORD_LOOKUP_IN_SELECT = KeywordLookup.from(KEYWORDS_IN_SELECT);

    private static final String[] KEYWORDS_IN_FROM = {
        "ANTI JOIN",
        "CROSS APPLY",
        "CROSS JOIN",
        "FULL JOIN",
        "FULL HASH JOIN",
        "FULL LOOP JOIN",
        "FULL LOOKUP JOIN",
        "FULL MERGE JOIN",
        "FULL OUTER JOIN",
        "FULL OUTER HASH JOIN",
        "FULL OUTER LOOP JOIN",
        "FULL OUTER LOOKUP JOIN",
        "FULL OUTER MERGE JOIN",
        "INNER JOIN",
        "INNER HASH JOIN",
        "INNER LOOP JOIN",
        "INNER LOOKUP JOIN",
        "INNER MERGE JOIN",
        "JOIN",
        "LEFT ANTI JOIN",
        "LEFT JOIN",
        "LEFT HASH JOIN",
        "LEFT LOOP JOIN",
        "LEFT LOOKUP JOIN",
        "LEFT MERGE JOIN",
        "LEFT OUTER JOIN",
        "LEFT OUTER HASH JOIN",
        "LEFT OUTER LOOP JOIN",
        "LEFT OUTER LOOKUP JOIN",
        "LEFT OUTER MERGE JOIN",
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
        "RIGHT HASH JOIN",
        "RIGHT LOOP JOIN",
        "RIGHT LOOKUP JOIN",
        "RIGHT MERGE JOIN",
        "RIGHT OUTER JOIN",
        "RIGHT OUTER HASH JOIN",
        "RIGHT OUTER LOOP JOIN",
        "RIGHT OUTER LOOKUP JOIN",
        "RIGHT OUTER MERGE JOIN",
        "RIGHT SEMI JOIN",
        "SEMI JOIN",
        "STRAIGHT_JOIN",
        "USING"
    };

    private static final KeywordLookup KEYWORD_LOOKUP_IN_FROM = KeywordLookup.from(KEYWORDS_IN_FROM);

    private static final String[] KEYWORDS_IN_SELECT_FROM;

    static {
        Set<String> set = new TreeSet<>(asList(KEYWORDS_IN_FROM));

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
            "FORCE KEY",
            "FORCE INDEX",
            "GROUP BY",
            "HAVING",
            "IGNORE KEY",
            "IGNORE INDEX",
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
            "USE KEY",
            "USE INDEX",
            "WHERE",
            "WINDOW"
        ));

        KEYWORDS_IN_SELECT_FROM = set.toArray(EMPTY_STRING);
    }

    private static final KeywordLookup KEYWORD_LOOKUP_IN_SELECT_FROM = KeywordLookup.from(KEYWORDS_IN_SELECT_FROM);

    private static final String[] KEYWORDS_IN_UPDATE_FROM;

    static {
        Set<String> set = new TreeSet<>(asList(KEYWORDS_IN_FROM));
        set.addAll(asList("FROM", "SET", "WHERE", "ORDER BY", "LIMIT", "RETURNING"));
        KEYWORDS_IN_UPDATE_FROM = set.toArray(EMPTY_STRING);
    }

    private static final KeywordLookup KEYWORD_LOOKUP_IN_UPDATE_FROM = KeywordLookup.from(KEYWORDS_IN_UPDATE_FROM);

    private static final String[] KEYWORDS_IN_DELETE_FROM;

    static {
        Set<String> set = new TreeSet<>(asList(KEYWORDS_IN_FROM));
        set.addAll(asList("FROM", "USING", "ALL", "WHERE", "ORDER BY", "LIMIT", "RETURNING"));
        set.addAll(asList(KEYWORDS_IN_STATEMENTS));
        KEYWORDS_IN_DELETE_FROM = set.toArray(EMPTY_STRING);
    }

    private static final KeywordLookup KEYWORD_LOOKUP_IN_DELETE_FROM = KeywordLookup.from(KEYWORDS_IN_DELETE_FROM);

    private static final String[] PIVOT_KEYWORDS      = {
        "FOR"
    };

    private static final Lazy<DDLQuery> IGNORE              = Lazy.of(() -> new IgnoreQuery());
    private static final Lazy<Query>    IGNORE_NO_DELIMITER = Lazy.of(() -> new IgnoreQuery());

    static final class IgnoreQuery extends AbstractDDLQuery implements UEmpty {
        final String sql;

        IgnoreQuery() {
            this("/* ignored */");
        }

        IgnoreQuery(String sql) {
            this(sql, CONFIG.get());
        }

        IgnoreQuery(String sql, Configuration configuration) {
            super(configuration);

            this.sql = sql;
        }

        @Override
        public void accept(Context<?> ctx) {
            ctx.sql(sql);
        }
    }


    private final Meta                  meta;
    private final ParseWithMetaLookups  metaLookups;
    private boolean                     metaLookupsForceIgnore;
    private final Consumer<Param<?>>    bindParamListener;
    private boolean                     ignoreHints            = true;
    private final Object[]              bindings;
    private int                         bindIndex              = 0;
    private final Map<String, Param<?>> bindParams             = new LinkedHashMap<>();
    private String                      delimiter              = ";";
    private boolean                     delimiterRequired      = false;
    private LanguageContext             languageContext        = LanguageContext.QUERY;
    private EnumSet<FunctionKeyword>    forbidden              = EnumSet.noneOf(FunctionKeyword.class);
    private boolean                     supportArraySubscripts = true;
    private ParseScope                  scope                  = new ParseScope();

    private final IgnoreComment         icIgnore;
    private final IgnoreComment         icTemplate;
    private final IgnoreComment         icRaw;
    private int                         markerStart            = -1;
    private int                         markerStop             = -1;





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
        String chars,
        Object[] bindings
    ) {
        super(dsl, chars, null);

        this.meta = meta;
        this.metaLookups = metaLookups;
        this.bindings = bindings;

        // [#8722] This is an undocumented flag that allows for collecting parameters from the parser
        //         Do not rely on this flag. It will change incompatibly in the future.
        this.bindParamListener = (Consumer<Param<?>>) dsl.configuration().data("org.jooq.parser.param-collector");
        this.delimiterRequired = TRUE.equals(dsl.configuration().data("org.jooq.parser.delimiter-required"));





        this.icIgnore = new IgnoreComment(
            TRUE.equals(dsl.settings().isParseIgnoreComments()),
            dsl.settings().getParseIgnoreCommentStart(),
            dsl.settings().getParseIgnoreCommentStop()
        );
        this.icTemplate = new IgnoreComment(
            TRUE.equals(dsl.settings().isParsePlainSQLTemplateComments()),
            dsl.settings().getParsePlainSQLTemplateCommentStart(),
            dsl.settings().getParsePlainSQLTemplateCommentStop()
        );
        this.icRaw = new IgnoreComment(
            TRUE.equals(dsl.settings().isParseRawSQLComments()),
            dsl.settings().getParseRawSQLCommentStart(),
            dsl.settings().getParseRawSQLCommentStop()
        );

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
    public final SQLDialectCategory parseCategory() {
        return parseDialect().category();
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

    private final boolean ignoreProEdition() {
        return !proEdition() && TRUE.equals(settings().isParseIgnoreCommercialOnlyFeatures());
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

    private final ParserException notImplemented(String feature) {
        return notImplemented(feature, "https://github.com/jOOQ/jOOQ/issues/16487");
    }

    private final ParserException notImplementedNonScalarSelectPredicate() {
        return notImplemented("Non-scalar SELECT predicate", "https://github.com/jOOQ/jOOQ/issues/10176");
    }

    private final ParserException notImplemented(String feature, String link) {
        return init(new ParserException(mark(), feature + " not yet implemented. If you're interested in this feature, please comment on " + link));
    }

    private final ParserException unsupportedClause() {
        return init(new ParserException(mark(), "Unsupported clause"));
    }

    private final Object nextBinding() {
        if (bindIndex++ < bindings.length)
            return bindings[bindIndex - 1];
        else if (bindings.length == 0)
            return null;
        else
            throw exception("No binding provided for bind index " + bindIndex);
    }

    @Override
    public final ParseContext characters(char[] newCharacters) {
        this.chars = newCharacters;
        return this;
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

    private final boolean isIdentifierStart() {
        return isIdentifierStart(character());
    }

    private final boolean isIdentifierStart(int pos) {
        return isIdentifierStart(character(pos));
    }

    private final boolean isIdentifierStart(char character) {
        return Character.isJavaIdentifierStart(character)
           || ((character == '@'
           ||   character == '#')
           &&   character != delimiter.charAt(0));
    }

    @Override
    final boolean done() {
        return super.done() && (bindings.length == 0 || bindings.length == bindIndex);
    }

    private final <Q extends QueryPart> Q done(String message, Q result) {
        if (done())
            return notify(result);
        else
            throw exception(message);
    }

    private final <Q extends QueryPart> Q wrap(Supplier<Q> supplier) {
        ParserException suppressed = null;

        try {
            return supplier.get();
        }
        catch (ParserException e) {
            throw suppressed = e;
        }
        finally {











        }
    }

    private final <Q extends QueryPart> Q notify(Q result) {
        if (bindParamListener != null) {
            final Map<String, Param<?>> params = new LinkedHashMap<>();

            // [#8722]  TODO Replace this by a public SPI
            // [#11054] Use a VisitListener to find actual Params in the expression tree,
            //          which may have more refined DataTypes attached to them, from context
            dsl.configuration().deriveAppending(onVisitStart(ctx -> {
                if (ctx.queryPart() instanceof Param<?> p) {
                    if (!p.isInline()) {
                        String name = p.getParamName();

                        if (name == null)
                            name = "" + ctx.context().peekIndex();

                        if (!params.containsKey(name))
                            params.put(name, p);
                    }
                }
            })).dsl().render(result);

            for (String name : bindParams.keySet())
                bindParamListener.accept(params.get(name));
        }

        return result;
    }

    @SuppressWarnings("unused")
    private final boolean asTrue(Object o) {
        return true;
    }

    private final <T> T newScope(Supplier<T> scoped) {
        ParseScope old = scope;

        try {
            scope = new ParseScope();
            return scoped.get();
        }
        finally {
            scope = old;
        }
    }

    private class ParseScope {
        private boolean                                        scopeClear               = false;
        private final ScopeStack<Name, Table<?>>               tableScope               = new ScopeStack<>();
        private final ScopeStack<Name, Field<?>>               fieldScope               = new ScopeStack<>();
        private final ScopeStack<Name, QualifiedAsteriskProxy> lookupQualifiedAsterisks = new ScopeStack<>();
        private final ScopeStack<Name, FieldProxy<?>>          lookupFields             = new ScopeStack<>();





        private final Table<?> scope(Table<?> table) {
            tableScope.set(table.getQualifiedName(), table);
            return table;
        }

        private final Field<?> scope(Field<?> field) {
            fieldScope.set(field.getQualifiedName(), field);
            return field;
        }

        private final void scopeResolve() {
            if (!lookupFields.isEmpty())
                unknownField(lookupFields.iterator().next());
            if (!lookupQualifiedAsterisks.isEmpty())
                unknownTable(lookupQualifiedAsterisks.iterator().next());
        }

        private final void scopeStart() {
            tableScope.scopeStart();
            fieldScope.scopeStart();
            lookupFields.scopeStart();
            lookupFields.setAll(null);
            lookupQualifiedAsterisks.scopeStart();
            lookupQualifiedAsterisks.setAll(null);
        }

        private final void scopeEnd(Query scopeOwner) {
            List<FieldProxy<?>> fields = new ArrayList<>();

            // [#14372] Avoid looking up tables at a higher scope level
            lookupLoop:
            for (QualifiedAsteriskProxy lookup : scope.lookupQualifiedAsterisks.iterableAtScopeLevel()) {
                for (Table<?> t : scope.tableScope) {

                    // [#15056] TODO: Could there be an ambiguity, as with fields?
                    if (t.getName().equals(lookup.$table().getName())) {
                        lookup.delegate((QualifiedAsteriskImpl) t.asterisk());
                        continue lookupLoop;
                    }
                }

                // [#15056] TODO: Should we support references to higher scopes?
                unknownTable(lookup);
            }

            // [#14372] Avoid looking up fields at a higher scope level
            for (FieldProxy<?> lookup : scope.lookupFields.iterableAtScopeLevel()) {
                Value<Field<?>> found = null;

                for (Field<?> f : scope.fieldScope) {
                    if (f.getName().equals(lookup.getName())) {
                        if (found != null) {
                            position(lookup.position());
                            throw exception("Ambiguous field identifier");
                        }

                        // TODO: Does this instance of "found" really interact with the one below?
                        found = new Value<>(0, f);
                    }
                }

                found = resolveInTableScope(scope.tableScope.valueIterable(), lookup.getQualifiedName(), lookup, found);

                if (found != null && !(found.value() instanceof FieldProxy)) {
                    lookup.delegate((AbstractField) found.value());
                }
                else {
                    lookup.scopeOwner(scopeOwner);
                    fields.add(lookup);
                }
            }

            scope.lookupQualifiedAsterisks.scopeEnd();
            scope.lookupFields.scopeEnd();
            scope.tableScope.scopeEnd();
            scope.fieldScope.scopeEnd();

            for (FieldProxy<?> r : fields)
                if (scope.lookupFields.get(r.getQualifiedName()) == null)
                    if (scope.lookupFields.inScope())
                        scope.lookupFields.set(r.getQualifiedName(), r);
                    else
                        unknownField(r);
        }

        private final void scopeClear() {
            scopeClear = true;
        }

        private final void unknownField(FieldProxy<?> field) {
            if (!scopeClear) {






























                if (metaLookups() == THROW_ON_FAILURE) {
                    position(field.position());
                    throw exception("Unknown field identifier");
                }
            }
        }

        private final void unknownTable(QualifiedAsteriskProxy asterisk) {
            if (!scopeClear) {
                if (metaLookups() == THROW_ON_FAILURE) {
                    position(asterisk.position());
                    throw exception("Unknown table identifier");
                }
            }
        }
    }

    private final Value<Field<?>> resolveInTableScope(Iterable<Value<Table<?>>> tables, Name lookupName, FieldProxy<?> lookup, Value<Field<?>> found) {

        tableScopeLoop:
        for (Value<Table<?>> t : tables) {
            Value<Field<?>> f;

            if (t.value() instanceof JoinTable j) {
                found = resolveInTableScope(
                    asList(
                        new Value<>(t.scopeLevel(), j.lhs),
                        new Value<>(t.scopeLevel(), j.rhs)
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
                    if ((found = Value.of(t.scopeLevel(), t.value().fieldsIncludingHidden().field(lookup.getName()))) != null)
                        break tableScopeLoop;








            }
            else if ((f = Value.of(t.scopeLevel(), t.value().fieldsIncludingHidden().field(lookup.getName()))) != null) {
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
            if (!name.qualified()) {
                for (ParseSearchSchema schema : settings().getParseSearchPath())
                    if ((tables = meta.getTables(name(schema.getCatalog(), schema.getSchema()).append(name))).size() == 1)
                        return tables.get(0);
            }
















        }

        // [#16762] It should always be possible to lookup the DUAL pseudo table
        if (Dual.isDual(name))
            return dual();

        if (metaLookups() == THROW_ON_FAILURE) {
            position(positionBeforeName);
            throw exception("Unknown table identifier");
        }

        return table(name);
    }

    private final QualifiedAsterisk lookupQualifiedAsterisk(int positionBeforeName, Name name) {
        if (metaLookups() == ParseWithMetaLookups.OFF || scope.lookupQualifiedAsterisks.scopeLevel() < 0)
            return table(name).asterisk();

        QualifiedAsteriskProxy asterisk = scope.lookupQualifiedAsterisks.get(name);
        if (asterisk == null)
            scope.lookupQualifiedAsterisks.set(name, asterisk = new QualifiedAsteriskProxy((QualifiedAsteriskImpl) table(name).asterisk(), positionBeforeName));

        return asterisk;
    }

    private final Field<?> lookupField(int positionBeforeName, Name name) {
        if (metaLookups() == ParseWithMetaLookups.OFF || scope.lookupFields.scopeLevel() < 0)
            return field(name);

        FieldProxy<?> field = scope.lookupFields.get(name);
        if (field == null)
            scope.lookupFields.set(name, field = new FieldProxy<>((AbstractField<Object>) field(name), positionBeforeName));

        return field;
    }

    @Override
    public String toString() {
        return mark();
    }

    public final <T> T data(Object key, Object value, Function<? super DefaultParseContext, ? extends T> function) {
        Object previous = data(key, value);

        try {
            return function.apply(this);
        }
        finally {
            if (previous == null)
                data().remove(key);
            else
                data(key, previous);
        }
    }
}