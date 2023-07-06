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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
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

import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.quotedName;
import static org.jooq.impl.DSL.systemName;

import org.jooq.Name;
// ...

/**
 * An internal {@link Name} cache.
 *
 * @author Lukas Eder
 */
final class Names {

    // [#13383] These names must never be unquoted to prevent conflicts with keywords
    static final Name NQ_CASE                       = quotedName("case");
    static final Name NQ_RESULT                     = quotedName("result");
    static final Name NQ_SELECT                     = quotedName("select");

    static final Name N_ARRAY_TABLE                 = name("array_table");
    static final Name N_COLUMN_VALUE                = name("COLUMN_VALUE");

    static final Name N_ADD                         = systemName("add");
    static final Name N_ADD_DAYS                    = systemName("add_days");
    static final Name N_ADD_HOURS                   = systemName("add_hours");
    static final Name N_ADD_MINUTES                 = systemName("add_minutes");
    static final Name N_ADD_MONTHS                  = systemName("add_months");
    static final Name N_ADD_SECONDS                 = systemName("add_seconds");
    static final Name N_ADD_YEARS                   = systemName("add_years");
    static final Name N_ANY                         = systemName("any");
    static final Name N_ARBITRARY                   = systemName("arbitrary");
    static final Name N_ARRAY                       = systemName("array");
    static final Name N_ARRAY_AGG                   = systemName("array_agg");
    static final Name N_ARRAY_CONSTRUCT             = systemName("array_construct");
    static final Name N_ARRAY_CONSTRUCT_COMPACT     = systemName("array_construct_compact");
    static final Name N_BITCOUNT                    = systemName("bitcount");
    static final Name N_BITWISE_AND_AGG             = systemName("bitwise_and_agg");
    static final Name N_BITWISE_OR_AGG              = systemName("bitwise_or_agg");
    static final Name N_BIT_NAND                    = systemName("bit_nand");
    static final Name N_BIT_NOR                     = systemName("bit_nor");
    static final Name N_BIT_XNOR                    = systemName("bit_xnor");
    static final Name N_BOOLAND_AGG                 = systemName("booland_agg");
    static final Name N_BOOLOR_AGG                  = systemName("boolor_agg");
    static final Name N_BYTEA                       = systemName("bytea");
    static final Name N_CAST                        = systemName("cast");
    static final Name N_CHARINDEX                   = systemName("charindex");
    static final Name N_CHOOSE                      = systemName("choose");
    static final Name N_COLLECT                     = systemName("collect");
    static final Name N_CONCAT                      = systemName("concat");
    static final Name N_CONVERT                     = systemName("convert");
    static final Name N_COUNTIF                     = systemName("countif");
    static final Name N_COUNTSET                    = systemName("countset");
    static final Name N_COUNT_IF                    = systemName("count_if");
    static final Name N_CUBE                        = systemName("cube");
    static final Name N_CUME_DIST                   = systemName("cume_dist");
    static final Name N_CURRENTUSER                 = systemName("currentuser");
    static final Name N_CURRENT_BIGDATETIME         = systemName("current_bigdatetime");
    static final Name N_CURRENT_DATABASE            = systemName("current_database");
    static final Name N_CURRENT_DATE                = systemName("current_date");
    static final Name N_CURRENT_TIME                = systemName("current_time");
    static final Name N_CURRENT_TIMESTAMP           = systemName("current_timestamp");
    static final Name N_CURRVAL                     = systemName("currval");
    static final Name N_DATEADD                     = systemName("dateadd");
    static final Name N_DATEDIFF                    = systemName("datediff");
    static final Name N_DATEPART                    = systemName("datepart");
    static final Name N_DATETIME_TRUNC              = systemName("datetime_trunc");
    static final Name N_DATE_DIFF                   = systemName("date_diff");
    static final Name N_DATE_TRUNC                  = systemName("date_trunc");
    static final Name N_DAYOFWEEK                   = systemName("dayofweek");
    static final Name N_DAYOFYEAR                   = systemName("dayofyear");
    static final Name N_DAYS                        = systemName("days");
    static final Name N_DAYS_BETWEEN                = systemName("days_between");
    static final Name N_DB_NAME                     = systemName("db_name");
    static final Name N_DECODE                      = systemName("decode");
    static final Name N_DECODE_ORACLE               = systemName("decode_oracle");
    static final Name N_DEFAULT                     = systemName("default");
    static final Name N_DELETED                     = systemName("deleted");
    static final Name N_DENSE_RANK                  = systemName("dense_rank");
    static final Name N_DIV                         = systemName("div");
    static final Name N_DUAL                        = systemName("dual");
    static final Name N_ELEMENT_AT                  = systemName("element_at");
    static final Name N_ELT                         = systemName("elt");
    static final Name N_EVERY                       = systemName("every");
    static final Name N_EXTRACT                     = systemName("extract");
    static final Name N_FILTER                      = systemName("filter");
    static final Name N_FIRST_VALUE                 = systemName("first_value");
    static final Name N_FLASHBACK                   = systemName("flashback");
    static final Name N_FUNCTION                    = systemName("function");
    static final Name N_GENERATE_ARRAY              = systemName("generate_array");
    static final Name N_GENERATE_SERIES             = systemName("generate_series");
    static final Name N_GENERATE_UNIQUE             = systemName("generate_unique");
    static final Name N_GENERATOR                   = systemName("generator");
    static final Name N_GEN_ID                      = systemName("gen_id");
    static final Name N_GEN_UUID                    = systemName("gen_uuid");
    static final Name N_GETDATE                     = systemName("getdate");
    static final Name N_GREATEST                    = systemName("greatest");
    static final Name N_GROUPING_SETS               = systemName("grouping sets");
    static final Name N_GROUP_CONCAT                = systemName("group_concat");
    static final Name N_HASHBYTES                   = systemName("hashbytes");
    static final Name N_HASH_MD5                    = systemName("hash_md5");
    static final Name N_HEX_TO_BINARY               = systemName("hex_to_binary");
    static final Name N_HEX_TO_INTEGER              = systemName("hex_to_integer");
    static final Name N_IF                          = systemName("if");
    static final Name N_IIF                         = systemName("iif");
    static final Name N_INSERT                      = systemName("insert");
    static final Name N_INSERTED                    = systemName("inserted");
    static final Name N_JOIN                        = systemName("join");
    static final Name N_JSON                        = systemName("json");
    static final Name N_JSONB_AGG                   = systemName("jsonb_agg");
    static final Name N_JSONB_BUILD_ARRAY           = systemName("jsonb_build_array");
    static final Name N_JSONB_OBJECT_AGG            = systemName("jsonb_object_agg");
    static final Name N_JSONB_PATH_EXISTS           = systemName("jsonb_path_exists");
    static final Name N_JSONB_PATH_QUERY_ARRAY      = systemName("jsonb_path_query_array");
    static final Name N_JSONB_PATH_QUERY_FIRST      = systemName("jsonb_path_query_first");
    static final Name N_JSONPATH                    = systemName("jsonpath");
    static final Name N_JSON_AGG                    = systemName("json_agg");
    static final Name N_JSON_ARRAYAGG               = systemName("json_arrayagg");
    static final Name N_JSON_BUILD_ARRAY            = systemName("json_build_array");
    static final Name N_JSON_CONTAINS_PATH          = systemName("json_contains_path");
    static final Name N_JSON_GROUP_ARRAY            = systemName("json_group_array");
    static final Name N_JSON_GROUP_OBJECT           = systemName("json_group_object");
    static final Name N_JSON_MERGE                  = systemName("json_merge");
    static final Name N_JSON_MERGE_PRESERVE         = systemName("json_merge_preserve");
    static final Name N_JSON_OBJECTAGG              = systemName("json_objectagg");
    static final Name N_JSON_OBJECT_AGG             = systemName("json_object_agg");
    static final Name N_JSON_PARSE                  = systemName("json_parse");
    static final Name N_JSON_QUERY                  = systemName("json_query");
    static final Name N_JSON_QUOTE                  = systemName("json_quote");
    static final Name N_JSON_TABLE                  = systemName("json_table");
    static final Name N_JSON_TYPE                   = systemName("json_type");
    static final Name N_JSON_UNQUOTE                = systemName("json_unquote");
    static final Name N_JSON_VALUE                  = systemName("json_value");
    static final Name N_LAG                         = systemName("lag");
    static final Name N_LAST_VALUE                  = systemName("last_value");
    static final Name N_LEAD                        = systemName("lead");
    static final Name N_LEAST                       = systemName("least");
    static final Name N_LIST                        = systemName("list");
    static final Name N_LISTAGG                     = systemName("listagg");
    static final Name N_LOCATE                      = systemName("locate");
    static final Name N_LOCK_TIMEOUT                = systemName("lock_timeout");
    static final Name N_LOGICAL_AND                 = systemName("logical_and");
    static final Name N_LOGICAL_OR                  = systemName("logical_or");
    static final Name N_MAP                         = systemName("map");
    static final Name N_MAP_FROM_ENTRIES            = systemName("map_from_entries");
    static final Name N_MAXVALUE                    = systemName("maxvalue");
    static final Name N_MID                         = systemName("mid");
    static final Name N_MINVALUE                    = systemName("minvalue");
    static final Name N_MOD                         = systemName("mod");
    static final Name N_MODE                        = systemName("mode");
    static final Name N_MUL                         = systemName("mul");
    static final Name N_MULTISET                    = systemName("multiset");
    static final Name N_MULTISET_AGG                = systemName("multiset_agg");
    static final Name N_NANO100_BETWEEN             = systemName("nano100_between");
    static final Name N_NEG                         = systemName("neg");
    static final Name N_NEW                         = systemName("new");
    static final Name N_NEXTVAL                     = systemName("nextval");
    static final Name N_NOT                         = systemName("not");
    static final Name N_NOW                         = systemName("now");
    static final Name N_NTH_VALUE                   = systemName("nth_value");
    static final Name N_NTILE                       = systemName("ntile");
    static final Name N_NULL                        = systemName("null");
    static final Name N_NVL2                        = systemName("nvl2");
    static final Name N_OBJECT_AGG                  = systemName("object_agg");
    static final Name N_OBJECT_CONSTRUCT            = systemName("object_construct");
    static final Name N_OBJECT_CONSTRUCT_KEEP_NULL  = systemName("object_construct_keep_null");
    static final Name N_OFFSET                      = systemName("offset");
    static final Name N_OLD                         = systemName("old");
    static final Name N_OPENJSON                    = systemName("openjson");
    static final Name N_OPENXML                     = systemName("openxml");
    static final Name N_ORDINAL                     = systemName("ordinal");
    static final Name N_OREPLACE                    = systemName("oreplace");
    static final Name N_PARSE_JSON                  = systemName("parse_json");
    static final Name N_PERCENTILE_CONT             = systemName("percentile_cont");
    static final Name N_PERCENTILE_DISC             = systemName("percentile_disc");
    static final Name N_PERCENT_RANK                = systemName("percent_rank");
    static final Name N_PIVOT                       = systemName("pivot");
    static final Name N_PLPGSQL                     = systemName("plpgsql");
    static final Name N_PLUS                        = systemName("plus");
    static final Name N_POWER                       = systemName("power");
    static final Name N_RANDOMBLOB                  = systemName("randomblob");
    static final Name N_RANK                        = systemName("rank");
    static final Name N_RATIO_TO_REPORT             = systemName("ratio_to_report");
    static final Name N_RAWTOHEX                    = systemName("rawtohex");
    static final Name N_RECORD                      = systemName("record");
    static final Name N_REGEXP_REPLACE              = systemName("regexp_replace");
    static final Name N_REGEX_REPLACE               = systemName("regex_replace");
    static final Name N_REPLACE_REGEXPR             = systemName("replace_regexpr");
    static final Name N_RESULT                      = systemName("result");
    static final Name N_RND                         = systemName("rnd");
    static final Name N_ROLLUP                      = systemName("rollup");
    static final Name N_ROUND_DOWN                  = systemName("round_down");
    static final Name N_ROW                         = systemName("row");
    static final Name N_ROWID                       = systemName("rowid");
    static final Name N_ROWSFROM                    = systemName("rowsfrom");
    static final Name N_ROW_NUMBER                  = systemName("row_number");
    static final Name N_SCHEMA_NAME                 = systemName("schema_name");
    static final Name N_SECONDS_BETWEEN             = systemName("seconds_between");
    static final Name N_SEQ4                        = systemName("seq4");
    static final Name N_SEQ8                        = systemName("seq8");
    static final Name N_SHL                         = systemName("shl");
    static final Name N_SHR                         = systemName("shr");
    static final Name N_SPLIT                       = systemName("split");
    static final Name N_SQL_TSI_DAY                 = systemName("sql_tsi_day");
    static final Name N_SQL_TSI_FRAC_SECOND         = systemName("sql_tsi_frac_second");
    static final Name N_SQL_TSI_HOUR                = systemName("sql_tsi_hour");
    static final Name N_SQL_TSI_MILLI_SECOND        = systemName("sql_tsi_milli_second");
    static final Name N_SQL_TSI_MINUTE              = systemName("sql_tsi_minute");
    static final Name N_SQL_TSI_MONTH               = systemName("sql_tsi_month");
    static final Name N_SQL_TSI_QUARTER             = systemName("sql_tsi_quarter");
    static final Name N_SQL_TSI_SECOND              = systemName("sql_tsi_second");
    static final Name N_SQL_TSI_WEEK                = systemName("sql_tsi_week");
    static final Name N_SQL_TSI_YEAR                = systemName("sql_tsi_year");
    static final Name N_STANDARD_HASH               = systemName("standard_hash");
    static final Name N_STATS_MODE                  = systemName("stats_mode");
    static final Name N_STDDEV                      = systemName("stddev");
    static final Name N_STDEV                       = systemName("stdev");
    static final Name N_STDEVP                      = systemName("stdevp");
    static final Name N_STDEV_SAMP                  = systemName("stdev_samp");
    static final Name N_STRFTIME                    = systemName("strftime");
    static final Name N_STRING_AGG                  = systemName("string_agg");
    static final Name N_STRING_SPLIT                = systemName("string_split");
    static final Name N_STR_REPLACE                 = systemName("str_replace");
    static final Name N_ST_NUMINTERIORRINGS         = systemName("st_numinteriorrings");
    static final Name N_SUB                         = systemName("sub");
    static final Name N_SUBSTR                      = systemName("substr");
    static final Name N_SYSTEM_RANGE                = systemName("system_range");
    static final Name N_SYSTEM_TIME                 = systemName("system_time");
    static final Name N_SYSUUID                     = systemName("sysuuid");
    static final Name N_SYS_GUID                    = systemName("sys_guid");
    static final Name N_T                           = systemName("t");
    static final Name N_TIMESTAMPADD                = systemName("timestampadd");
    static final Name N_TIMESTAMPDIFF               = systemName("timestampdiff");
    static final Name N_TIMESTAMPSUB                = systemName("timestampsub");
    static final Name N_TIMESTAMP_DIFF              = systemName("timestamp_diff");
    static final Name N_TIMESTAMP_SUB               = systemName("timestamp_sub");
    static final Name N_TO_CLOB                     = systemName("to_clob");
    static final Name N_TO_NUMBER                   = systemName("to_number");
    static final Name N_TO_VARIANT                  = systemName("to_variant");
    static final Name N_TRUNCATE                    = systemName("truncate");
    static final Name N_TRUNCNUM                    = systemName("truncnum");
    static final Name N_UNNEST                      = systemName("unnest");
    static final Name N_USER                        = systemName("user");
    static final Name N_UUID_TO_CHAR                = systemName("uuid_to_char");
    static final Name N_VALUE                       = systemName("value");
    static final Name N_VALUES                      = systemName("values");
    static final Name N_VAR                         = systemName("var");
    static final Name N_VARIANCE                    = systemName("variance");
    static final Name N_VARIANCE_SAMP               = systemName("variance_samp");
    static final Name N_VARP                        = systemName("varp");
    static final Name N_WEEKDAY                     = systemName("weekday");
    static final Name N_XMLAGG                      = systemName("xmlagg");
    static final Name N_XMLATTRIBUTES               = systemName("xmlattributes");
    static final Name N_XMLELEMENT                  = systemName("xmlelement");
    static final Name N_XMLPARSE                    = systemName("xmlparse");
    static final Name N_XMLQUERY                    = systemName("xmlquery");
    static final Name N_XMLROOT                     = systemName("xmlroot");
    static final Name N_XMLTABLE                    = systemName("xmltable");
    static final Name N_XMLTEXT                     = systemName("xmltext");
    static final Name N_XMLTYPE                     = systemName("xmltype");
    static final Name N_XPATH                       = systemName("xpath");
    static final Name N_ZEROBLOB                    = systemName("zeroblob");



    static final Name N_ABS                         = systemName("abs");
    static final Name N_ACOS                        = systemName("acos");
    static final Name N_ACOSH                       = systemName("acosh");
    static final Name N_ACOTH                       = systemName("acoth");
    static final Name N_ANY_VALUE                   = systemName("any_value");
    static final Name N_ARRAYS_OVERLAP              = systemName("arrays_overlap");
    static final Name N_ARRAY_APPEND                = systemName("array_append");
    static final Name N_ARRAY_CONCAT                = systemName("array_concat");
    static final Name N_ARRAY_GET                   = systemName("array_get");
    static final Name N_ARRAY_LENGTH                = systemName("array_length");
    static final Name N_ARRAY_OVERLAP               = systemName("array_overlap");
    static final Name N_ARRAY_PREPEND               = systemName("array_prepend");
    static final Name N_ARRAY_REMOVE                = systemName("array_remove");
    static final Name N_ARRAY_REPLACE               = systemName("array_replace");
    static final Name N_ASC                         = systemName("asc");
    static final Name N_ASCII                       = systemName("ascii");
    static final Name N_ASCII_CHAR                  = systemName("ascii_char");
    static final Name N_ASCII_VAL                   = systemName("ascii_val");
    static final Name N_ASIN                        = systemName("asin");
    static final Name N_ASINH                       = systemName("asinh");
    static final Name N_ATAN                        = systemName("atan");
    static final Name N_ATAN2                       = systemName("atan2");
    static final Name N_ATANH                       = systemName("atanh");
    static final Name N_ATN                         = systemName("atn");
    static final Name N_ATN2                        = systemName("atn2");
    static final Name N_AVG                         = systemName("avg");
    static final Name N_BIN_AND                     = systemName("bin_and");
    static final Name N_BIN_NOT                     = systemName("bin_not");
    static final Name N_BIN_OR                      = systemName("bin_or");
    static final Name N_BIN_SHL                     = systemName("bin_shl");
    static final Name N_BIN_SHR                     = systemName("bin_shr");
    static final Name N_BIN_XOR                     = systemName("bin_xor");
    static final Name N_BITAND                      = systemName("bitand");
    static final Name N_BITGET                      = systemName("bitget");
    static final Name N_BITNOT                      = systemName("bitnot");
    static final Name N_BITOR                       = systemName("bitor");
    static final Name N_BITSHIFTLEFT                = systemName("bitshiftleft");
    static final Name N_BITSHIFTRIGHT               = systemName("bitshiftright");
    static final Name N_BITWISE_AND                 = systemName("bitwise_and");
    static final Name N_BITWISE_LEFT_SHIFT          = systemName("bitwise_left_shift");
    static final Name N_BITWISE_NOT                 = systemName("bitwise_not");
    static final Name N_BITWISE_OR                  = systemName("bitwise_or");
    static final Name N_BITWISE_RIGHT_SHIFT         = systemName("bitwise_right_shift");
    static final Name N_BITWISE_XOR                 = systemName("bitwise_xor");
    static final Name N_BITXNOR                     = systemName("bitxnor");
    static final Name N_BITXOR                      = systemName("bitxor");
    static final Name N_BIT_AND                     = systemName("bit_and");
    static final Name N_BIT_AND_AGG                 = systemName("bit_and_agg");
    static final Name N_BIT_CHECK                   = systemName("bit_check");
    static final Name N_BIT_COUNT                   = systemName("bit_count");
    static final Name N_BIT_GET                     = systemName("bit_get");
    static final Name N_BIT_LENGTH                  = systemName("bit_length");
    static final Name N_BIT_LSHIFT                  = systemName("bit_lshift");
    static final Name N_BIT_NAND_AGG                = systemName("bit_nand_agg");
    static final Name N_BIT_NOR_AGG                 = systemName("bit_nor_agg");
    static final Name N_BIT_NOT                     = systemName("bit_not");
    static final Name N_BIT_OR                      = systemName("bit_or");
    static final Name N_BIT_OR_AGG                  = systemName("bit_or_agg");
    static final Name N_BIT_RSHIFT                  = systemName("bit_rshift");
    static final Name N_BIT_SET                     = systemName("bit_set");
    static final Name N_BIT_XNOR_AGG                = systemName("bit_xnor_agg");
    static final Name N_BIT_XOR                     = systemName("bit_xor");
    static final Name N_BIT_XOR_AGG                 = systemName("bit_xor_agg");
    static final Name N_BOOL_AND                    = systemName("bool_and");
    static final Name N_BOOL_OR                     = systemName("bool_or");
    static final Name N_BYTE_LENGTH                 = systemName("byte_length");
    static final Name N_CARDINALITY                 = systemName("cardinality");
    static final Name N_CEIL                        = systemName("ceil");
    static final Name N_CEILING                     = systemName("ceiling");
    static final Name N_CHAR                        = systemName("char");
    static final Name N_CHAR_LENGTH                 = systemName("char_length");
    static final Name N_CHECK_JSON                  = systemName("check_json");
    static final Name N_CHR                         = systemName("chr");
    static final Name N_COALESCE                    = systemName("coalesce");
    static final Name N_CONDITION                   = systemName("condition");
    static final Name N_CONNECT_BY_ISCYCLE          = systemName("connect_by_iscycle");
    static final Name N_CONNECT_BY_ISLEAF           = systemName("connect_by_isleaf");
    static final Name N_CONNECT_BY_ROOT             = systemName("connect_by_root");
    static final Name N_CONTAINS                    = systemName("contains");
    static final Name N_CORR                        = systemName("corr");
    static final Name N_COS                         = systemName("cos");
    static final Name N_COSH                        = systemName("cosh");
    static final Name N_COT                         = systemName("cot");
    static final Name N_COTH                        = systemName("coth");
    static final Name N_COUNT                       = systemName("count");
    static final Name N_COVAR_POP                   = systemName("covar_pop");
    static final Name N_COVAR_SAMP                  = systemName("covar_samp");
    static final Name N_CURRENT_CATALOG             = systemName("current_catalog");
    static final Name N_CURRENT_SCHEMA              = systemName("current_schema");
    static final Name N_CURRENT_USER                = systemName("current_user");
    static final Name N_DATALENGTH                  = systemName("datalength");
    static final Name N_DATE_ADD                    = systemName("date_add");
    static final Name N_DEGREES                     = systemName("degrees");
    static final Name N_DELETING                    = systemName("deleting");
    static final Name N_DIGITS                      = systemName("digits");
    static final Name N_E                           = systemName("e");
    static final Name N_EXCLUDED                    = systemName("excluded");
    static final Name N_EXECUTE                     = systemName("execute");
    static final Name N_EXISTS                      = systemName("exists");
    static final Name N_EXP                         = systemName("exp");
    static final Name N_FIELD                       = systemName("field");
    static final Name N_FLOOR                       = systemName("floor");
    static final Name N_FORMAT                      = systemName("format");
    static final Name N_GENERATE_UUID               = systemName("generate_uuid");
    static final Name N_GENGUID                     = systemName("genguid");
    static final Name N_GEN_RANDOM_UUID             = systemName("gen_random_uuid");
    static final Name N_GET                         = systemName("get");
    static final Name N_GETBIT                      = systemName("getbit");
    static final Name N_GET_BIT                     = systemName("get_bit");
    static final Name N_GOTO                        = systemName("goto");
    static final Name N_HEX                         = systemName("hex");
    static final Name N_IFNULL                      = systemName("ifnull");
    static final Name N_INSERTING                   = systemName("inserting");
    static final Name N_INSTR                       = systemName("instr");
    static final Name N_ISJSON                      = systemName("isjson");
    static final Name N_JSONB_ARRAY                 = systemName("jsonb_array");
    static final Name N_JSONB_GET_ATTRIBUTE         = systemName("jsonb_get_attribute");
    static final Name N_JSONB_GET_ATTRIBUTE_AS_TEXT = systemName("jsonb_get_attribute_as_text");
    static final Name N_JSONB_GET_ELEMENT           = systemName("jsonb_get_element");
    static final Name N_JSONB_GET_ELEMENT_AS_TEXT   = systemName("jsonb_get_element_as_text");
    static final Name N_JSONB_INSERT                = systemName("jsonb_insert");
    static final Name N_JSONB_KEYS                  = systemName("jsonb_keys");
    static final Name N_JSONB_OBJECT                = systemName("jsonb_object");
    static final Name N_JSONB_REMOVE                = systemName("jsonb_remove");
    static final Name N_JSONB_REPLACE               = systemName("jsonb_replace");
    static final Name N_JSONB_SET                   = systemName("jsonb_set");
    static final Name N_JSON_ARRAY                  = systemName("json_array");
    static final Name N_JSON_EXTRACT                = systemName("json_extract");
    static final Name N_JSON_GET_ATTRIBUTE          = systemName("json_get_attribute");
    static final Name N_JSON_GET_ATTRIBUTE_AS_TEXT  = systemName("json_get_attribute_as_text");
    static final Name N_JSON_GET_ELEMENT            = systemName("json_get_element");
    static final Name N_JSON_GET_ELEMENT_AS_TEXT    = systemName("json_get_element_as_text");
    static final Name N_JSON_INSERT                 = systemName("json_insert");
    static final Name N_JSON_KEYS                   = systemName("json_keys");
    static final Name N_JSON_MODIFY                 = systemName("json_modify");
    static final Name N_JSON_OBJECT                 = systemName("json_object");
    static final Name N_JSON_REMOVE                 = systemName("json_remove");
    static final Name N_JSON_REPLACE                = systemName("json_replace");
    static final Name N_JSON_SET                    = systemName("json_set");
    static final Name N_JSON_TRANSFORM              = systemName("json_transform");
    static final Name N_JSON_VALID                  = systemName("json_valid");
    static final Name N_LCASE                       = systemName("lcase");
    static final Name N_LEFT                        = systemName("left");
    static final Name N_LEN                         = systemName("len");
    static final Name N_LENGTH                      = systemName("length");
    static final Name N_LENGTHB                     = systemName("lengthb");
    static final Name N_LEVEL                       = systemName("level");
    static final Name N_LN                          = systemName("ln");
    static final Name N_LOCAL_DATE_ADD              = systemName("local_date_add");
    static final Name N_LOCAL_DATE_TIME_ADD         = systemName("local_date_time_add");
    static final Name N_LOG                         = systemName("log");
    static final Name N_LOG10                       = systemName("log10");
    static final Name N_LOGN                        = systemName("logn");
    static final Name N_LOWER                       = systemName("lower");
    static final Name N_LPAD                        = systemName("lpad");
    static final Name N_LSHIFT                      = systemName("lshift");
    static final Name N_LTRIM                       = systemName("ltrim");
    static final Name N_MAP_KEYS                    = systemName("map_keys");
    static final Name N_MAX                         = systemName("max");
    static final Name N_MD5                         = systemName("md5");
    static final Name N_MEDIAN                      = systemName("median");
    static final Name N_MIN                         = systemName("min");
    static final Name N_NEWID                       = systemName("newid");
    static final Name N_NULLIF                      = systemName("nullif");
    static final Name N_NVL                         = systemName("nvl");
    static final Name N_OBJECT_KEYS                 = systemName("object_keys");
    static final Name N_OCTET_LENGTH                = systemName("octet_length");
    static final Name N_OTRANSLATE                  = systemName("otranslate");
    static final Name N_OVERLAY                     = systemName("overlay");
    static final Name N_PI                          = systemName("pi");
    static final Name N_POSITION                    = systemName("position");
    static final Name N_PRINTF                      = systemName("printf");
    static final Name N_PRIOR                       = systemName("prior");
    static final Name N_PRODUCT                     = systemName("product");
    static final Name N_RADIANS                     = systemName("radians");
    static final Name N_RAND                        = systemName("rand");
    static final Name N_RANDOM                      = systemName("random");
    static final Name N_RANDOM_UUID                 = systemName("random_uuid");
    static final Name N_REGR_AVGX                   = systemName("regr_avgx");
    static final Name N_REGR_AVGY                   = systemName("regr_avgy");
    static final Name N_REGR_COUNT                  = systemName("regr_count");
    static final Name N_REGR_INTERCEPT              = systemName("regr_intercept");
    static final Name N_REGR_R2                     = systemName("regr_r2");
    static final Name N_REGR_SLOPE                  = systemName("regr_slope");
    static final Name N_REGR_SXX                    = systemName("regr_sxx");
    static final Name N_REGR_SXY                    = systemName("regr_sxy");
    static final Name N_REGR_SYY                    = systemName("regr_syy");
    static final Name N_REPEAT                      = systemName("repeat");
    static final Name N_REPLACE                     = systemName("replace");
    static final Name N_REPLICATE                   = systemName("replicate");
    static final Name N_RETURN_                     = systemName("return_");
    static final Name N_REVERSE                     = systemName("reverse");
    static final Name N_RIGHT                       = systemName("right");
    static final Name N_ROUND                       = systemName("round");
    static final Name N_ROWNUM                      = systemName("rownum");
    static final Name N_RPAD                        = systemName("rpad");
    static final Name N_RSHIFT                      = systemName("rshift");
    static final Name N_RTRIM                       = systemName("rtrim");
    static final Name N_SETBIT                      = systemName("setbit");
    static final Name N_SET_BIT                     = systemName("set_bit");
    static final Name N_SGN                         = systemName("sgn");
    static final Name N_SHIFTLEFT                   = systemName("shiftleft");
    static final Name N_SHIFTRIGHT                  = systemName("shiftright");
    static final Name N_SIGN                        = systemName("sign");
    static final Name N_SIGNAL_SQLSTATE             = systemName("signal_sqlstate");
    static final Name N_SIN                         = systemName("sin");
    static final Name N_SINH                        = systemName("sinh");
    static final Name N_SPACE                       = systemName("space");
    static final Name N_SPLIT_PART                  = systemName("split_part");
    static final Name N_SQR                         = systemName("sqr");
    static final Name N_SQRT                        = systemName("sqrt");
    static final Name N_SQUARE                      = systemName("square");
    static final Name N_STARTS_WITH                 = systemName("starts_with");
    static final Name N_STDDEV_POP                  = systemName("stddev_pop");
    static final Name N_STDDEV_SAMP                 = systemName("stddev_samp");
    static final Name N_STRREVERSE                  = systemName("strreverse");
    static final Name N_STRTOK                      = systemName("strtok");
    static final Name N_STR_SPLIT                   = systemName("str_split");
    static final Name N_ST_AREA                     = systemName("st_area");
    static final Name N_ST_ASBINARY                 = systemName("st_asbinary");
    static final Name N_ST_ASTEXT                   = systemName("st_astext");
    static final Name N_ST_CENTROID                 = systemName("st_centroid");
    static final Name N_ST_CONTAINS                 = systemName("st_contains");
    static final Name N_ST_CROSSES                  = systemName("st_crosses");
    static final Name N_ST_DIFFERENCE               = systemName("st_difference");
    static final Name N_ST_DISJOINT                 = systemName("st_disjoint");
    static final Name N_ST_DISTANCE                 = systemName("st_distance");
    static final Name N_ST_ENDPOINT                 = systemName("st_endpoint");
    static final Name N_ST_EQUALS                   = systemName("st_equals");
    static final Name N_ST_EXTERIORRING             = systemName("st_exteriorring");
    static final Name N_ST_GEOMETRYN                = systemName("st_geometryn");
    static final Name N_ST_GEOMETRYTYPE             = systemName("st_geometrytype");
    static final Name N_ST_GEOMFROMTEXT             = systemName("st_geomfromtext");
    static final Name N_ST_GEOMFROMWKB              = systemName("st_geomfromwkb");
    static final Name N_ST_INTERIORRINGN            = systemName("st_interiorringn");
    static final Name N_ST_INTERSECTION             = systemName("st_intersection");
    static final Name N_ST_INTERSECTS               = systemName("st_intersects");
    static final Name N_ST_ISCLOSED                 = systemName("st_isclosed");
    static final Name N_ST_ISEMPTY                  = systemName("st_isempty");
    static final Name N_ST_LENGTH                   = systemName("st_length");
    static final Name N_ST_NUMGEOMETRIES            = systemName("st_numgeometries");
    static final Name N_ST_NUMINTERIORRING          = systemName("st_numinteriorring");
    static final Name N_ST_NUMPOINTS                = systemName("st_numpoints");
    static final Name N_ST_OVERLAPS                 = systemName("st_overlaps");
    static final Name N_ST_POINTN                   = systemName("st_pointn");
    static final Name N_ST_SRID                     = systemName("st_srid");
    static final Name N_ST_STARTPOINT               = systemName("st_startpoint");
    static final Name N_ST_TOUCHES                  = systemName("st_touches");
    static final Name N_ST_UNION                    = systemName("st_union");
    static final Name N_ST_WITHIN                   = systemName("st_within");
    static final Name N_ST_X                        = systemName("st_x");
    static final Name N_ST_Y                        = systemName("st_y");
    static final Name N_ST_Z                        = systemName("st_z");
    static final Name N_SUBSTRING                   = systemName("substring");
    static final Name N_SUBSTRING_INDEX             = systemName("substring_index");
    static final Name N_SUFFIX                      = systemName("suffix");
    static final Name N_SUM                         = systemName("sum");
    static final Name N_SYS_CONNECT_BY_PATH         = systemName("sys_connect_by_path");
    static final Name N_TAN                         = systemName("tan");
    static final Name N_TANH                        = systemName("tanh");
    static final Name N_TAU                         = systemName("tau");
    static final Name N_TIMESTAMP_ADD               = systemName("timestamp_add");
    static final Name N_TO_BASE                     = systemName("to_base");
    static final Name N_TO_CHAR                     = systemName("to_char");
    static final Name N_TO_DATE                     = systemName("to_date");
    static final Name N_TO_HEX                      = systemName("to_hex");
    static final Name N_TO_TIMESTAMP                = systemName("to_timestamp");
    static final Name N_TRANSFORM                   = systemName("transform");
    static final Name N_TRANSLATE                   = systemName("translate");
    static final Name N_TRIM                        = systemName("trim");
    static final Name N_TRUNC                       = systemName("trunc");
    static final Name N_UCASE                       = systemName("ucase");
    static final Name N_UNIQUE                      = systemName("unique");
    static final Name N_UPDATING                    = systemName("updating");
    static final Name N_UPPER                       = systemName("upper");
    static final Name N_UUID                        = systemName("uuid");
    static final Name N_UUID_GENERATE               = systemName("uuid_generate");
    static final Name N_UUID_STRING                 = systemName("uuid_string");
    static final Name N_VAR_POP                     = systemName("var_pop");
    static final Name N_VAR_SAMP                    = systemName("var_samp");
    static final Name N_WIDTH_BUCKET                = systemName("width_bucket");
    static final Name N_XMLCOMMENT                  = systemName("xmlcomment");
    static final Name N_XMLCONCAT                   = systemName("xmlconcat");
    static final Name N_XMLDOCUMENT                 = systemName("xmldocument");
    static final Name N_XMLFOREST                   = systemName("xmlforest");
    static final Name N_XMLPI                       = systemName("xmlpi");
    static final Name N_XMLSERIALIZE                = systemName("xmlserialize");
    static final Name N_XMLSERIALIZE_CONTENT        = systemName("xmlserialize_content");
    static final Name N_XOR                         = systemName("xor");








}
