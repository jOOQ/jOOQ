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

/**
 * An internal {@link Name} cache.
 *
 * @author Lukas Eder
 */
final class Names {

    static final Name N_ARRAY_TABLE            = name("array_table");
    static final Name N_COLUMN_VALUE           = name("COLUMN_VALUE");

    static final Name N_ACOS                   = unquotedName("acos");
    static final Name N_ADD_MONTHS             = unquotedName("add_months");
    static final Name N_ARRAY                  = unquotedName("array");
    static final Name N_ARRAY_AGG              = unquotedName("array_agg");
    static final Name N_ASC                    = unquotedName("asc");
    static final Name N_ASCII                  = unquotedName("ascii");
    static final Name N_ASCII_VAL              = unquotedName("ascii_val");
    static final Name N_ASIN                   = unquotedName("asin");
    static final Name N_ATAN                   = unquotedName("atan");
    static final Name N_ATN                    = unquotedName("atn");
    static final Name N_BIT_COUNT              = unquotedName("bit_count");
    static final Name N_BOOL_AND               = unquotedName("bool_and");
    static final Name N_BOOL_OR                = unquotedName("bool_or");
    static final Name N_CASE                   = unquotedName("case");
    static final Name N_CAST                   = unquotedName("cast");
    static final Name N_CEIL                   = unquotedName("ceil");
    static final Name N_CEILING                = unquotedName("ceiling");
    static final Name N_CHARINDEX              = unquotedName("charindex");
    static final Name N_CHOOSE                 = unquotedName("choose");
    static final Name N_CLNG                   = unquotedName("clng");
    static final Name N_COALESCE               = unquotedName("coalesce");
    static final Name N_COLLECT                = unquotedName("collect");
    static final Name N_CONCAT                 = unquotedName("concat");
    static final Name N_CONVERT                = unquotedName("convert");
    static final Name N_COSH                   = unquotedName("cosh");
    static final Name N_COT                    = unquotedName("cot");
    static final Name N_COUNTSET               = unquotedName("countset");
    static final Name N_CURRENT_BIGDATETIME    = unquotedName("current_bigdatetime");
    static final Name N_CURRENT_DATE           = unquotedName("current_date");
    static final Name N_CURRENT_SCHEMA         = unquotedName("current_schema");
    static final Name N_CURRENT_TIME           = unquotedName("current_time");
    static final Name N_CURRENT_TIMESTAMP      = unquotedName("current_timestamp");
    static final Name N_CURRENT_USER           = unquotedName("current_user");
    static final Name N_CURRENTUSER            = unquotedName("currentuser");
    static final Name N_CURRVAL                = unquotedName("currval");
    static final Name N_DATE_ADD               = unquotedName("date_add");
    static final Name N_DATE_DIFF              = unquotedName("date_diff");
    static final Name N_DATE_TRUNC             = unquotedName("date_trunc");
    static final Name N_DATEADD                = unquotedName("dateadd");
    static final Name N_DATEDIFF               = unquotedName("datediff");
    static final Name N_DATEPART               = unquotedName("datepart");
    static final Name N_DAYOFWEEK              = unquotedName("dayofweek");
    static final Name N_DAYS_BETWEEN           = unquotedName("days_between");
    static final Name N_DECODE                 = unquotedName("decode");
    static final Name N_DEGREES                = unquotedName("degrees");
    static final Name N_DUAL                   = unquotedName("dual");
    static final Name N_E                      = unquotedName("e");
    static final Name N_EXP                    = unquotedName("exp");
    static final Name N_EXTRACT                = unquotedName("extract");
    static final Name N_FLASHBACK              = unquotedName("flashback");
    static final Name N_FLOOR                  = unquotedName("floor");
    static final Name N_FUNCTION               = unquotedName("function");
    static final Name N_GEN_ID                 = unquotedName("gen_id");
    static final Name N_GENERATE_SERIES        = unquotedName("generate_series");
    static final Name N_GREATEST               = unquotedName("greatest");
    static final Name N_GROUP_CONCAT           = unquotedName("group_concat");
    static final Name N_HASHBYTES              = unquotedName("hashbytes");
    static final Name N_HEX                    = unquotedName("hex");
    static final Name N_IFNULL                 = unquotedName("ifnull");
    static final Name N_IIF                    = unquotedName("iif");
    static final Name N_INSTR                  = unquotedName("instr");
    static final Name N_JOIN                   = unquotedName("join");
    static final Name N_JSON_AGG               = unquotedName("json_agg");
    static final Name N_JSON_ARRAY             = unquotedName("json_array");
    static final Name N_JSON_ARRAYAGG          = unquotedName("json_arrayagg");
    static final Name N_JSON_CONTAINS_PATH     = unquotedName("json_contains_path");
    static final Name N_JSON_EXTRACT           = unquotedName("json_extract");
    static final Name N_JSON_OBJECT            = unquotedName("json_object");
    static final Name N_JSON_OBJECT_AGG        = unquotedName("json_object_agg");
    static final Name N_JSON_OBJECTAGG         = unquotedName("json_objectagg");
    static final Name N_JSON_VALUE             = unquotedName("json_value");
    static final Name N_JSONB_AGG              = unquotedName("jsonb_agg");
    static final Name N_JSONB_OBJECT           = unquotedName("jsonb_object");
    static final Name N_JSONB_OBJECT_AGG       = unquotedName("jsonb_object_agg");
    static final Name N_JSONB_OBJECTAGG        = unquotedName("jsonb_objectagg");
    static final Name N_JSONB_PATH_EXISTS      = unquotedName("jsonb_path_exists");
    static final Name N_JSONB_PATH_QUERY_FIRST = unquotedName("jsonb_path_query_first");
    static final Name N_LCASE                  = unquotedName("lcase");
    static final Name N_LEAST                  = unquotedName("least");
    static final Name N_LEFT                   = unquotedName("left");
    static final Name N_LEN                    = unquotedName("len");
    static final Name N_LENGTH                 = unquotedName("length");
    static final Name N_LIST                   = unquotedName("list");
    static final Name N_LISTAGG                = unquotedName("listagg");
    static final Name N_LN                     = unquotedName("ln");
    static final Name N_LOCATE                 = unquotedName("locate");
    static final Name N_LOWER                  = unquotedName("lower");
    static final Name N_LPAD                   = unquotedName("lpad");
    static final Name N_LTRIM                  = unquotedName("ltrim");
    static final Name N_MD5                    = unquotedName("md5");
    static final Name N_MEDIAN                 = unquotedName("median");
    static final Name N_MID                    = unquotedName("mid");
    static final Name N_MOD                    = unquotedName("mod");
    static final Name N_MODE                   = unquotedName("mode");
    static final Name N_NEXTVAL                = unquotedName("nextval");
    static final Name N_NOT                    = unquotedName("not");
    static final Name N_NOW                    = unquotedName("now");
    static final Name N_NTILE                  = unquotedName("ntile");
    static final Name N_NULLIF                 = unquotedName("nullif");
    static final Name N_NUMTODSINTERVAL        = unquotedName("numtodsinterval");
    static final Name N_NVL                    = unquotedName("nvl");
    static final Name N_NVL2                   = unquotedName("nvl2");
    static final Name N_OVERLAY                = unquotedName("overlay");
    static final Name N_PI                     = unquotedName("pi");
    static final Name N_PIVOT                  = unquotedName("pivot");
    static final Name N_POSITION               = unquotedName("position");
    static final Name N_POWER                  = unquotedName("power");
    static final Name N_PRIOR                  = unquotedName("prior");
    static final Name N_PRODUCT                = unquotedName("product");
    static final Name N_RADIANS                = unquotedName("radians");
    static final Name N_RAND                   = unquotedName("rand");
    static final Name N_RANDOM                 = unquotedName("rand");
    static final Name N_RATIO_TO_REPORT        = unquotedName("ratio_to_report");
    static final Name N_RAWTOHEX               = unquotedName("rawtohex");
    static final Name N_REPEAT                 = unquotedName("repeat");
    static final Name N_REPLACE                = unquotedName("replace");
    static final Name N_REPLICATE              = unquotedName("replicate");
    static final Name N_REVERSE                = unquotedName("reverse");
    static final Name N_RIGHT                  = unquotedName("right");
    static final Name N_RND                    = unquotedName("rnd");
    static final Name N_ROLLUP                 = unquotedName("rollup");
    static final Name N_ROUND                  = unquotedName("round");
    static final Name N_ROUND_DOWN             = unquotedName("round_down");
    static final Name N_ROW                    = unquotedName("row");
    static final Name N_ROW_NUMBER             = unquotedName("row_number");
    static final Name N_ROWNUM                 = unquotedName("rownum");
    static final Name N_ROWSFROM               = unquotedName("rowsfrom");
    static final Name N_RPAD                   = unquotedName("rpad");
    static final Name N_RTRIM                  = unquotedName("rtrim");
    static final Name N_SCHEMA_NAME            = unquotedName("schema_name");
    static final Name N_SELECT                 = unquotedName("select");
    static final Name N_SGN                    = unquotedName("sgn");
    static final Name N_SIGN                   = unquotedName("sign");
    static final Name N_SINH                   = unquotedName("sinh");
    static final Name N_SPACE                  = unquotedName("space");
    static final Name N_SQR                    = unquotedName("sqr");
    static final Name N_SQRT                   = unquotedName("sqrt");
    static final Name N_STANDARD_HASH          = unquotedName("standard_hash");
    static final Name N_STATS_MODE             = unquotedName("stats_mode");
    static final Name N_STRFTIME               = unquotedName("strftime");
    static final Name N_STRING_AGG             = unquotedName("string_agg");
    static final Name N_STRREVERSE             = unquotedName("strreverse");
    static final Name N_SUBSTR                 = unquotedName("substr");
    static final Name N_SUBSTRING              = unquotedName("substring");
    static final Name N_SYSDATE                = unquotedName("sysdate");
    static final Name N_SYSTEM_TIME            = unquotedName("system_time");
    static final Name N_T                      = unquotedName("t");
    static final Name N_TANH                   = unquotedName("tanh");
    static final Name N_TIMESTAMPADD           = unquotedName("timestampadd");
    static final Name N_TIMESTAMPDIFF          = unquotedName("timestampdiff");
    static final Name N_TO_CHAR                = unquotedName("to_char");
    static final Name N_TO_CLOB                = unquotedName("to_clob");
    static final Name N_TO_DATE                = unquotedName("to_date");
    static final Name N_TO_NUMBER              = unquotedName("to_number");
    static final Name N_TO_TIMESTAMP           = unquotedName("to_timestamp");
    static final Name N_TRANSLATE              = unquotedName("translate");
    static final Name N_TRIM                   = unquotedName("trim");
    static final Name N_TRUNC                  = unquotedName("trunc");
    static final Name N_TRUNCATE               = unquotedName("truncate");
    static final Name N_TRUNCNUM               = unquotedName("truncnum");
    static final Name N_UCASE                  = unquotedName("ucase");
    static final Name N_UPPER                  = unquotedName("upper");
    static final Name N_USER                   = unquotedName("user");
    static final Name N_VALUES                 = unquotedName("values");
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
