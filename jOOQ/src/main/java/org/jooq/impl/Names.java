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

import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.unquotedName;

import org.jooq.Name;
// ...

/**
 * An internal {@link Name} cache.
 *
 * @author Lukas Eder
 */
final class Names {

    static final Name N_ARRAY_TABLE            = name("array_table");
    static final Name N_COLUMN_VALUE           = name("COLUMN_VALUE");

    static final Name N_ABS                    = unquotedName("abs");
    static final Name N_ACOS                   = unquotedName("acos");
    static final Name N_ADD_DAYS               = unquotedName("add_days");
    static final Name N_ADD_HOURS              = unquotedName("add_hours");
    static final Name N_ADD_MINUTES            = unquotedName("add_minutes");
    static final Name N_ADD_MONTHS             = unquotedName("add_months");
    static final Name N_ADD_SECONDS            = unquotedName("add_seconds");
    static final Name N_ADD_YEARS              = unquotedName("add_years");
    static final Name N_ANY                    = unquotedName("any");
    static final Name N_ANY_VALUE              = unquotedName("any_value");
    static final Name N_ARRAY                  = unquotedName("array");
    static final Name N_ARRAY_AGG              = unquotedName("array_agg");
    static final Name N_ARRAY_GET              = unquotedName("array_get");
    static final Name N_ARRAY_LENGTH           = unquotedName("array_length");
    static final Name N_ASC                    = unquotedName("asc");
    static final Name N_ASCII                  = unquotedName("ascii");
    static final Name N_ASCII_CHAR             = unquotedName("ascii_char");
    static final Name N_ASCII_VAL              = unquotedName("ascii_val");
    static final Name N_ASIN                   = unquotedName("asin");
    static final Name N_ATAN                   = unquotedName("atan");
    static final Name N_ATAN2                  = unquotedName("atan2");
    static final Name N_ATN                    = unquotedName("atn");
    static final Name N_ATN2                   = unquotedName("atn2");
    static final Name N_AVG                    = unquotedName("avg");
    static final Name N_BIN_AND                = unquotedName("bin_and");
    static final Name N_BIN_NOT                = unquotedName("bin_not");
    static final Name N_BIN_OR                 = unquotedName("bin_or");
    static final Name N_BIN_SHL                = unquotedName("bin_shl");
    static final Name N_BIN_SHR                = unquotedName("bin_shr");
    static final Name N_BIN_XOR                = unquotedName("bin_xor");
    static final Name N_BITAND                 = unquotedName("bitand");
    static final Name N_BITNOT                 = unquotedName("bitnot");
    static final Name N_BITOR                  = unquotedName("bitor");
    static final Name N_BITSHIFTLEFT           = unquotedName("bitshiftleft");
    static final Name N_BITSHIFTRIGHT          = unquotedName("bitshiftright");
    static final Name N_BITXOR                 = unquotedName("bitxor");
    static final Name N_BIT_AND                = unquotedName("bit_and");
    static final Name N_BIT_AND_AGG            = unquotedName("bit_and_agg");
    static final Name N_BITCOUNT               = unquotedName("bitcount");
    static final Name N_BIT_COUNT              = unquotedName("bit_count");
    static final Name N_BIT_LENGTH             = unquotedName("bit_length");
    static final Name N_BIT_OR                 = unquotedName("bit_or");
    static final Name N_BIT_OR_AGG             = unquotedName("bit_or_agg");
    static final Name N_BIT_XOR                = unquotedName("bit_xor");
    static final Name N_BIT_XOR_AGG            = unquotedName("bit_xor_agg");
    static final Name N_BOOLAND_AGG            = unquotedName("booland_agg");
    static final Name N_BOOLOR_AGG             = unquotedName("boolor_agg");
    static final Name N_BOOL_AND               = unquotedName("bool_and");
    static final Name N_BOOL_OR                = unquotedName("bool_or");
    static final Name N_BYTE_LENGTH            = unquotedName("byte_length");
    static final Name N_CARDINALITY            = unquotedName("cardinality");
    static final Name N_CASE                   = unquotedName("case");
    static final Name N_CAST                   = unquotedName("cast");
    static final Name N_CEIL                   = unquotedName("ceil");
    static final Name N_CEILING                = unquotedName("ceiling");
    static final Name N_CHAR                   = unquotedName("char");
    static final Name N_CHARINDEX              = unquotedName("charindex");
    static final Name N_CHAR_LENGTH            = unquotedName("char_length");
    static final Name N_CHOOSE                 = unquotedName("choose");
    static final Name N_CHR                    = unquotedName("chr");
    static final Name N_COALESCE               = unquotedName("coalesce");
    static final Name N_COLLECT                = unquotedName("collect");
    static final Name N_CONCAT                 = unquotedName("concat");
    static final Name N_CONNECT_BY_IS_CYCLE    = unquotedName("connect_by_iscycle");
    static final Name N_CONNECT_BY_IS_LEAF     = unquotedName("connect_by_isleaf");
    static final Name N_CONNECT_BY_ROOT        = unquotedName("connect_by_root");
    static final Name N_CONVERT                = unquotedName("convert");
    static final Name N_CORR                   = unquotedName("corr");
    static final Name N_COS                    = unquotedName("cos");
    static final Name N_COSH                   = unquotedName("cosh");
    static final Name N_COT                    = unquotedName("cot");
    static final Name N_COTH                   = unquotedName("coth");
    static final Name N_COUNT                  = unquotedName("count");
    static final Name N_COUNTIF                = unquotedName("countif");
    static final Name N_COUNTSET               = unquotedName("countset");
    static final Name N_COUNT_IF               = unquotedName("count_if");
    static final Name N_COVAR_POP              = unquotedName("covar_pop");
    static final Name N_COVAR_SAMP             = unquotedName("covar_samp");
    static final Name N_CUME_DIST              = unquotedName("cume_dist");
    static final Name N_CURRENTUSER            = unquotedName("currentuser");
    static final Name N_CURRENT_BIGDATETIME    = unquotedName("current_bigdatetime");
    static final Name N_CURRENT_CATALOG        = unquotedName("current_catalog");
    static final Name N_CURRENT_DATABASE       = unquotedName("current_database");
    static final Name N_CURRENT_DATE           = unquotedName("current_date");
    static final Name N_CURRENT_SCHEMA         = unquotedName("current_schema");
    static final Name N_CURRENT_TIME           = unquotedName("current_time");
    static final Name N_CURRENT_TIMESTAMP      = unquotedName("current_timestamp");
    static final Name N_CURRENT_USER           = unquotedName("current_user");
    static final Name N_CURRVAL                = unquotedName("currval");
    static final Name N_DATALENGTH             = unquotedName("datalength");
    static final Name N_DATEADD                = unquotedName("dateadd");
    static final Name N_DATEDIFF               = unquotedName("datediff");
    static final Name N_DATEPART               = unquotedName("datepart");
    static final Name N_DATETIME_TRUNC         = unquotedName("datetime_trunc");
    static final Name N_DATE_ADD               = unquotedName("date_add");
    static final Name N_DATE_DIFF              = unquotedName("date_diff");
    static final Name N_DATE_TRUNC             = unquotedName("date_trunc");
    static final Name N_DAYOFWEEK              = unquotedName("dayofweek");
    static final Name N_DAYOFYEAR              = unquotedName("dayofyear");
    static final Name N_DAYS                   = unquotedName("days");
    static final Name N_DAYS_BETWEEN           = unquotedName("days_between");
    static final Name N_DB_NAME                = unquotedName("db_name");
    static final Name N_DECODE                 = unquotedName("decode");
    static final Name N_DEFAULT                = unquotedName("default");
    static final Name N_DEGREES                = unquotedName("degrees");
    static final Name N_DELETING               = unquotedName("deleting");
    static final Name N_DENSE_RANK             = unquotedName("dense_rank");
    static final Name N_DIGITS                 = unquotedName("digits");
    static final Name N_DUAL                   = unquotedName("dual");
    static final Name N_E                      = unquotedName("e");
    static final Name N_EULER                  = unquotedName("e");
    static final Name N_EVERY                  = unquotedName("every");
    static final Name N_EXP                    = unquotedName("exp");
    static final Name N_EXTRACT                = unquotedName("extract");
    static final Name N_FIELD                  = unquotedName("field");
    static final Name N_FLASHBACK              = unquotedName("flashback");
    static final Name N_FLOOR                  = unquotedName("floor");
    static final Name N_FORMAT                 = unquotedName("format");
    static final Name N_FUNCTION               = unquotedName("function");
    static final Name N_GENERATE_ARRAY         = unquotedName("generate_array");
    static final Name N_GENERATE_SERIES        = unquotedName("generate_series");
    static final Name N_GENERATE_UNIQUE        = unquotedName("generate_unique");
    static final Name N_GENERATE_UUID          = unquotedName("generate_uuid");
    static final Name N_GENERATOR              = unquotedName("generator");
    static final Name N_GENGUID                = unquotedName("genguid");
    static final Name N_GEN_ID                 = unquotedName("gen_id");
    static final Name N_GEN_RANDOM_UUID        = unquotedName("gen_random_uuid");
    static final Name N_GEN_UUID               = unquotedName("gen_uuid");
    static final Name N_GETDATE                = unquotedName("getdate");
    static final Name N_GREATEST               = unquotedName("greatest");
    static final Name N_GROUP_CONCAT           = unquotedName("group_concat");
    static final Name N_HASHBYTES              = unquotedName("hashbytes");
    static final Name N_HASH_MD5               = unquotedName("hash_md5");
    static final Name N_HEX                    = unquotedName("hex");
    static final Name N_IF                     = unquotedName("if");
    static final Name N_IFNULL                 = unquotedName("ifnull");
    static final Name N_IIF                    = unquotedName("iif");
    static final Name N_INSERT                 = unquotedName("insert");
    static final Name N_INSERTING              = unquotedName("inserting");
    static final Name N_INSTR                  = unquotedName("instr");
    static final Name N_ISJSON                 = unquotedName("isjson");
    static final Name N_JOIN                   = unquotedName("join");
    static final Name N_JSONB_AGG              = unquotedName("jsonb_agg");
    static final Name N_JSONB_BUILD_ARRAY      = unquotedName("jsonb_build_array");
    static final Name N_JSONB_OBJECT_AGG       = unquotedName("jsonb_object_agg");
    static final Name N_JSONB_PATH_EXISTS      = unquotedName("jsonb_path_exists");
    static final Name N_JSONB_PATH_QUERY_FIRST = unquotedName("jsonb_path_query_first");
    static final Name N_JSON_AGG               = unquotedName("json_agg");
    static final Name N_JSON_ARRAY             = unquotedName("json_array");
    static final Name N_JSON_ARRAYAGG          = unquotedName("json_arrayagg");
    static final Name N_JSON_BUILD_ARRAY       = unquotedName("json_build_array");
    static final Name N_JSON_CONTAINS_PATH     = unquotedName("json_contains_path");
    static final Name N_JSON_EXTRACT           = unquotedName("json_extract");
    static final Name N_JSON_MERGE             = unquotedName("json_merge");
    static final Name N_JSON_MERGE_PRESERVE    = unquotedName("json_merge_preserve");
    static final Name N_JSON_OBJECT            = unquotedName("json_object");
    static final Name N_JSON_OBJECTAGG         = unquotedName("json_objectagg");
    static final Name N_JSON_OBJECT_AGG        = unquotedName("json_object_agg");
    static final Name N_JSON_QUERY             = unquotedName("json_query");
    static final Name N_JSON_QUOTE             = unquotedName("json_quote");
    static final Name N_JSON_TABLE             = unquotedName("json_table");
    static final Name N_JSON_VALID             = unquotedName("json_valid");
    static final Name N_JSON_VALUE             = unquotedName("json_value");
    static final Name N_LCASE                  = unquotedName("lcase");
    static final Name N_LEAST                  = unquotedName("least");
    static final Name N_LEFT                   = unquotedName("left");
    static final Name N_LEN                    = unquotedName("len");
    static final Name N_LENGTH                 = unquotedName("length");
    static final Name N_LENGTHB                = unquotedName("lengthb");
    static final Name N_LEVEL                  = unquotedName("level");
    static final Name N_LIST                   = unquotedName("list");
    static final Name N_LISTAGG                = unquotedName("listagg");
    static final Name N_LN                     = unquotedName("ln");
    static final Name N_LOCATE                 = unquotedName("locate");
    static final Name N_LOCK_TIMEOUT           = unquotedName("lock_timeout");
    static final Name N_LOG                    = unquotedName("log");
    static final Name N_LOG10                  = unquotedName("log10");
    static final Name N_LOGICAL_AND            = unquotedName("logical_and");
    static final Name N_LOGICAL_OR             = unquotedName("logical_or");
    static final Name N_LOGN                   = unquotedName("logn");
    static final Name N_LOWER                  = unquotedName("lower");
    static final Name N_LPAD                   = unquotedName("lpad");
    static final Name N_LSHIFT                 = unquotedName("lshift");
    static final Name N_LTRIM                  = unquotedName("ltrim");
    static final Name N_MAP                    = unquotedName("map");
    static final Name N_MAX                    = unquotedName("max");
    static final Name N_MAXVALUE               = unquotedName("maxvalue");
    static final Name N_MD5                    = unquotedName("md5");
    static final Name N_MEDIAN                 = unquotedName("median");
    static final Name N_MID                    = unquotedName("mid");
    static final Name N_MIN                    = unquotedName("min");
    static final Name N_MINVALUE               = unquotedName("minvalue");
    static final Name N_MOD                    = unquotedName("mod");
    static final Name N_MODE                   = unquotedName("mode");
    static final Name N_MUL                    = unquotedName("mul");
    static final Name N_MULTISET               = unquotedName("multiset");
    static final Name N_MULTISET_AGG           = unquotedName("multiset_agg");
    static final Name N_NANO100_BETWEEN        = unquotedName("nano100_between");
    static final Name N_NEWID                  = unquotedName("newid");
    static final Name N_NEXTVAL                = unquotedName("nextval");
    static final Name N_NOT                    = unquotedName("not");
    static final Name N_NOW                    = unquotedName("now");
    static final Name N_NTILE                  = unquotedName("ntile");
    static final Name N_NULLIF                 = unquotedName("nullif");
    static final Name N_NVL                    = unquotedName("nvl");
    static final Name N_NVL2                   = unquotedName("nvl2");
    static final Name N_OCTET_LENGTH           = unquotedName("octet_length");
    static final Name N_OPENJSON               = unquotedName("openjson");
    static final Name N_OPENXML                = unquotedName("openxml");
    static final Name N_OREPLACE               = unquotedName("oreplace");
    static final Name N_OTRANSLATE             = unquotedName("otranslate");
    static final Name N_OVERLAY                = unquotedName("overlay");
    static final Name N_PERCENTILE_CONT        = unquotedName("percentile_cont");
    static final Name N_PERCENTILE_DISC        = unquotedName("percentile_disc");
    static final Name N_PERCENT_RANK           = unquotedName("percent_rank");
    static final Name N_PI                     = unquotedName("pi");
    static final Name N_PIVOT                  = unquotedName("pivot");
    static final Name N_PLPGSQL                = unquotedName("plpgsql");
    static final Name N_POSITION               = unquotedName("position");
    static final Name N_POWER                  = unquotedName("power");
    static final Name N_PRINTF                 = unquotedName("printf");
    static final Name N_PRIOR                  = unquotedName("prior");
    static final Name N_PRODUCT                = unquotedName("product");
    static final Name N_RADIANS                = unquotedName("radians");
    static final Name N_RAND                   = unquotedName("rand");
    static final Name N_RANDOM                 = unquotedName("random");
    static final Name N_RANDOMBLOB             = unquotedName("randomblob");
    static final Name N_RANDOM_UUID            = unquotedName("random_uuid");
    static final Name N_RANK                   = unquotedName("rank");
    static final Name N_RATIO_TO_REPORT        = unquotedName("ratio_to_report");
    static final Name N_RAWTOHEX               = unquotedName("rawtohex");
    static final Name N_RECORD                 = unquotedName("record");
    static final Name N_REGEXP_REPLACE         = unquotedName("regexp_replace");
    static final Name N_REGEX_REPLACE          = unquotedName("regex_replace");
    static final Name N_REGR_AVGX              = unquotedName("regr_avgx");
    static final Name N_REGR_AVGY              = unquotedName("regr_avgy");
    static final Name N_REGR_COUNT             = unquotedName("regr_count");
    static final Name N_REGR_INTERCEPT         = unquotedName("regr_intercept");
    static final Name N_REGR_R2                = unquotedName("regr_r2");
    static final Name N_REGR_SLOPE             = unquotedName("regr_slope");
    static final Name N_REGR_SXX               = unquotedName("regr_sxx");
    static final Name N_REGR_SXY               = unquotedName("regr_sxy");
    static final Name N_REGR_SYY               = unquotedName("regr_syy");
    static final Name N_REPEAT                 = unquotedName("repeat");
    static final Name N_REPLACE                = unquotedName("replace");
    static final Name N_REPLACE_REGEXPR        = unquotedName("replace_regexpr");
    static final Name N_REPLICATE              = unquotedName("replicate");
    static final Name N_RESULT                 = unquotedName("result");
    static final Name N_REVERSE                = unquotedName("reverse");
    static final Name N_RIGHT                  = unquotedName("right");
    static final Name N_RND                    = unquotedName("rnd");
    static final Name N_ROLLUP                 = unquotedName("rollup");
    static final Name N_ROUND                  = unquotedName("round");
    static final Name N_ROUND_DOWN             = unquotedName("round_down");
    static final Name N_ROW                    = unquotedName("row");
    static final Name N_ROWNUM                 = unquotedName("rownum");
    static final Name N_ROWSFROM               = unquotedName("rowsfrom");
    static final Name N_ROW_NUMBER             = unquotedName("row_number");
    static final Name N_RPAD                   = unquotedName("rpad");
    static final Name N_RSHIFT                 = unquotedName("rshift");
    static final Name N_RTRIM                  = unquotedName("rtrim");
    static final Name N_SCHEMA_NAME            = unquotedName("schema_name");
    static final Name N_SECONDS_BETWEEN        = unquotedName("seconds_between");
    static final Name N_SELECT                 = unquotedName("select");
    static final Name N_SEQ4                   = unquotedName("seq4");
    static final Name N_SEQ8                   = unquotedName("seq8");
    static final Name N_SGN                    = unquotedName("sgn");
    static final Name N_SHIFTLEFT              = unquotedName("shiftleft");
    static final Name N_SHIFTRIGHT             = unquotedName("shiftright");
    static final Name N_SIGN                   = unquotedName("sign");
    static final Name N_SIN                    = unquotedName("sin");
    static final Name N_SINH                   = unquotedName("sinh");
    static final Name N_SPACE                  = unquotedName("space");
    static final Name N_SPLIT                  = unquotedName("split");
    static final Name N_SPLIT_PART             = unquotedName("split_part");
    static final Name N_SQL_TSI_DAY            = unquotedName("sql_tsi_day");
    static final Name N_SQL_TSI_FRAC_SECOND    = unquotedName("sql_tsi_frac_second");
    static final Name N_SQL_TSI_HOUR           = unquotedName("sql_tsi_hour");
    static final Name N_SQL_TSI_MILLI_SECOND   = unquotedName("sql_tsi_milli_second");
    static final Name N_SQL_TSI_MINUTE         = unquotedName("sql_tsi_minute");
    static final Name N_SQL_TSI_MONTH          = unquotedName("sql_tsi_month");
    static final Name N_SQL_TSI_QUARTER        = unquotedName("sql_tsi_quarter");
    static final Name N_SQL_TSI_SECOND         = unquotedName("sql_tsi_second");
    static final Name N_SQL_TSI_WEEK           = unquotedName("sql_tsi_week");
    static final Name N_SQL_TSI_YEAR           = unquotedName("sql_tsi_year");
    static final Name N_SQR                    = unquotedName("sqr");
    static final Name N_SQRT                   = unquotedName("sqrt");
    static final Name N_SQUARE                 = unquotedName("square");
    static final Name N_STANDARD_HASH          = unquotedName("standard_hash");
    static final Name N_STATS_MODE             = unquotedName("stats_mode");
    static final Name N_STDDEV                 = unquotedName("stddev");
    static final Name N_STDDEV_POP             = unquotedName("stddev_pop");
    static final Name N_STDDEV_SAMP            = unquotedName("stddev_samp");
    static final Name N_STDEV                  = unquotedName("stdev");
    static final Name N_STDEVP                 = unquotedName("stdevp");
    static final Name N_STDEV_SAMP             = unquotedName("stdev_samp");
    static final Name N_STRFTIME               = unquotedName("strftime");
    static final Name N_STRING_AGG             = unquotedName("string_agg");
    static final Name N_STRREVERSE             = unquotedName("strreverse");
    static final Name N_STR_REPLACE            = unquotedName("str_replace");
    static final Name N_SUBSTR                 = unquotedName("substr");
    static final Name N_SUBSTRING              = unquotedName("substring");
    static final Name N_SUBSTRING_INDEX        = unquotedName("substring_index");
    static final Name N_SUM                    = unquotedName("sum");
    static final Name N_SYSTEM_RANGE           = unquotedName("system_range");
    static final Name N_SYSTEM_TIME            = unquotedName("system_time");
    static final Name N_SYSUUID                = unquotedName("sysuuid");
    static final Name N_SYS_CONNECT_BY_PATH    = unquotedName("sys_connect_by_path");
    static final Name N_SYS_GUID               = unquotedName("sys_guid");
    static final Name N_T                      = unquotedName("t");
    static final Name N_TAN                    = unquotedName("tan");
    static final Name N_TANH                   = unquotedName("tanh");
    static final Name N_TAU                    = unquotedName("tau");
    static final Name N_TIMESTAMPADD           = unquotedName("timestampadd");
    static final Name N_TIMESTAMPDIFF          = unquotedName("timestampdiff");
    static final Name N_TIMESTAMPSUB           = unquotedName("timestampsub");
    static final Name N_TIMESTAMP_ADD          = unquotedName("timestamp_add");
    static final Name N_TIMESTAMP_DIFF         = unquotedName("timestamp_diff");
    static final Name N_TIMESTAMP_SUB          = unquotedName("timestamp_sub");
    static final Name N_TO_CHAR                = unquotedName("to_char");
    static final Name N_TO_CLOB                = unquotedName("to_clob");
    static final Name N_TO_DATE                = unquotedName("to_date");
    static final Name N_TO_HEX                 = unquotedName("to_hex");
    static final Name N_TO_NUMBER              = unquotedName("to_number");
    static final Name N_TO_TIMESTAMP           = unquotedName("to_timestamp");
    static final Name N_TRANSLATE              = unquotedName("translate");
    static final Name N_TRIM                   = unquotedName("trim");
    static final Name N_TRUNC                  = unquotedName("trunc");
    static final Name N_TRUNCATE               = unquotedName("truncate");
    static final Name N_TRUNCNUM               = unquotedName("truncnum");
    static final Name N_UCASE                  = unquotedName("ucase");
    static final Name N_UNNEST                 = unquotedName("unnest");
    static final Name N_UPDATING               = unquotedName("updating");
    static final Name N_UPPER                  = unquotedName("upper");
    static final Name N_USER                   = unquotedName("user");
    static final Name N_UUID                   = unquotedName("uuid");
    static final Name N_UUID_GENERATE          = unquotedName("uuid_generate");
    static final Name N_UUID_STRING            = unquotedName("uuid_string");
    static final Name N_UUID_TO_CHAR           = unquotedName("uuid_to_char");
    static final Name N_VALUE                  = unquotedName("value");
    static final Name N_VALUES                 = unquotedName("values");
    static final Name N_VAR                    = unquotedName("var");
    static final Name N_VARIANCE               = unquotedName("variance");
    static final Name N_VARIANCE_SAMP          = unquotedName("variance_samp");
    static final Name N_VARP                   = unquotedName("varp");
    static final Name N_VAR_POP                = unquotedName("var_pop");
    static final Name N_VAR_SAMP               = unquotedName("var_samp");
    static final Name N_WEEKDAY                = unquotedName("weekday");
    static final Name N_WIDTH_BUCKET           = unquotedName("width_bucket");
    static final Name N_XMLAGG                 = unquotedName("xmlagg");
    static final Name N_XMLATTRIBUTES          = unquotedName("xmlattributes");
    static final Name N_XMLCOMMENT             = unquotedName("xmlcomment");
    static final Name N_XMLCONCAT              = unquotedName("xmlconcat");
    static final Name N_XMLDOCUMENT            = unquotedName("xmldocument");
    static final Name N_XMLELEMENT             = unquotedName("xmlelement");
    static final Name N_XMLFOREST              = unquotedName("xmlforest");
    static final Name N_XMLPARSE               = unquotedName("xmlparse");
    static final Name N_XMLPI                  = unquotedName("xmlpi");
    static final Name N_XMLQUERY               = unquotedName("xmlquery");
    static final Name N_XMLROOT                = unquotedName("xmlroot");
    static final Name N_XMLSERIALIZE           = unquotedName("xmlserialize");
    static final Name N_XMLTABLE               = unquotedName("xmltable");
    static final Name N_XMLTEXT                = unquotedName("xmltext");
    static final Name N_XPATH                  = unquotedName("xpath");
    static final Name N_ZEROBLOB               = unquotedName("zeroblob");






}
