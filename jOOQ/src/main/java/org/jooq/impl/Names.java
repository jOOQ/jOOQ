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

import org.jooq.Name;

/**
 * An internal {@link Name} cache.
 *
 * @author Lukas Eder
 */
final class Names {

    static final Name N_ARRAY_TABLE            = DSL.name("array_table");
    static final Name N_COLUMN_VALUE           = DSL.name("COLUMN_VALUE");

    static final Name N_ACOS                   = DSL.unquotedName("acos");
    static final Name N_ARRAY                  = DSL.unquotedName("array");
    static final Name N_ARRAY_AGG              = DSL.unquotedName("array_agg");
    static final Name N_ASCII                  = DSL.unquotedName("ascii");
    static final Name N_ASIN                   = DSL.unquotedName("asin");
    static final Name N_ATAN                   = DSL.unquotedName("atan");
    static final Name N_BIT_COUNT              = DSL.unquotedName("bit_count");
    static final Name N_CASE                   = DSL.unquotedName("case");
    static final Name N_CAST                   = DSL.unquotedName("cast");
    static final Name N_CEIL                   = DSL.unquotedName("ceil");
    static final Name N_CHOOSE                 = DSL.unquotedName("choose");
    static final Name N_COALESCE               = DSL.unquotedName("coalesce");
    static final Name N_COLLECT                = DSL.unquotedName("collect");
    static final Name N_CONCAT                 = DSL.unquotedName("concat");
    static final Name N_CONVERT                = DSL.unquotedName("convert");
    static final Name N_COSH                   = DSL.unquotedName("cosh");
    static final Name N_COT                    = DSL.unquotedName("cot");
    static final Name N_CURRENT_DATE           = DSL.unquotedName("current_date");
    static final Name N_CURRENT_SCHEMA         = DSL.unquotedName("current_schema");
    static final Name N_CURRENT_TIME           = DSL.unquotedName("current_time");
    static final Name N_CURRENT_TIMESTAMP      = DSL.unquotedName("current_timestamp");
    static final Name N_CURRENT_USER           = DSL.unquotedName("current_user");
    static final Name N_CURRVAL                = DSL.unquotedName("currval");
    static final Name N_DATEADD                = DSL.unquotedName("dateadd");
    static final Name N_DATEDIFF               = DSL.unquotedName("datediff");
    static final Name N_DECODE                 = DSL.unquotedName("decode");
    static final Name N_DEGREES                = DSL.unquotedName("degrees");
    static final Name N_DUAL                   = DSL.unquotedName("dual");
    static final Name N_E                      = DSL.unquotedName("e");
    static final Name N_EXP                    = DSL.unquotedName("exp");
    static final Name N_EXTRACT                = DSL.unquotedName("extract");
    static final Name N_FLASHBACK              = DSL.unquotedName("flashback");
    static final Name N_FLOOR                  = DSL.unquotedName("floor");
    static final Name N_FUNCTION               = DSL.unquotedName("function");
    static final Name N_GENERATE_SERIES        = DSL.unquotedName("generate_series");
    static final Name N_GREATEST               = DSL.unquotedName("greatest");
    static final Name N_GROUP_CONCAT           = DSL.unquotedName("group_concat");
    static final Name N_IIF                    = DSL.unquotedName("iif");
    static final Name N_JOIN                   = DSL.unquotedName("join");
    static final Name N_JSON_AGG               = DSL.unquotedName("json_agg");
    static final Name N_JSON_ARRAY             = DSL.unquotedName("json_array");
    static final Name N_JSON_ARRAYAGG          = DSL.unquotedName("json_arrayagg");
    static final Name N_JSON_CONTAINS_PATH     = DSL.unquotedName("json_contains_path");
    static final Name N_JSON_EXTRACT           = DSL.unquotedName("json_extract");
    static final Name N_JSON_OBJECT            = DSL.unquotedName("json_object");
    static final Name N_JSON_OBJECT_AGG        = DSL.unquotedName("json_object_agg");
    static final Name N_JSON_OBJECTAGG         = DSL.unquotedName("json_objectagg");
    static final Name N_JSON_VALUE             = DSL.unquotedName("json_value");
    static final Name N_JSONB_AGG              = DSL.unquotedName("jsonb_agg");
    static final Name N_JSONB_OBJECT           = DSL.unquotedName("jsonb_object");
    static final Name N_JSONB_OBJECT_AGG       = DSL.unquotedName("jsonb_object_agg");
    static final Name N_JSONB_OBJECTAGG        = DSL.unquotedName("jsonb_objectagg");
    static final Name N_JSONB_PATH_EXISTS      = DSL.unquotedName("jsonb_path_exists");
    static final Name N_JSONB_PATH_QUERY_FIRST = DSL.unquotedName("jsonb_path_query_first");
    static final Name N_LEAST                  = DSL.unquotedName("least");
    static final Name N_LEFT                   = DSL.unquotedName("left");
    static final Name N_LIST                   = DSL.unquotedName("list");
    static final Name N_LISTAGG                = DSL.unquotedName("listagg");
    static final Name N_LN                     = DSL.unquotedName("ln");
    static final Name N_LOWER                  = DSL.unquotedName("lower");
    static final Name N_LPAD                   = DSL.unquotedName("lpad");
    static final Name N_LTRIM                  = DSL.unquotedName("ltrim");
    static final Name N_MD5                    = DSL.unquotedName("md5");
    static final Name N_MEDIAN                 = DSL.unquotedName("median");
    static final Name N_MOD                    = DSL.unquotedName("mod");
    static final Name N_MODE                   = DSL.unquotedName("mode");
    static final Name N_NEXTVAL                = DSL.unquotedName("nextval");
    static final Name N_NOT                    = DSL.unquotedName("not");
    static final Name N_NTILE                  = DSL.unquotedName("ntile");
    static final Name N_NULLIF                 = DSL.unquotedName("nullif");
    static final Name N_NVL                    = DSL.unquotedName("nvl");
    static final Name N_NVL2                   = DSL.unquotedName("nvl2");
    static final Name N_OVERLAY                = DSL.unquotedName("overlay");
    static final Name N_PI                     = DSL.unquotedName("pi");
    static final Name N_PIVOT                  = DSL.unquotedName("pivot");
    static final Name N_POSITION               = DSL.unquotedName("position");
    static final Name N_POWER                  = DSL.unquotedName("power");
    static final Name N_PRIOR                  = DSL.unquotedName("prior");
    static final Name N_PRODUCT                = DSL.unquotedName("product");
    static final Name N_RADIANS                = DSL.unquotedName("radians");
    static final Name N_RANDOM                 = DSL.unquotedName("rand");
    static final Name N_REPLACE                = DSL.unquotedName("replace");
    static final Name N_REVERSE                = DSL.unquotedName("reverse");
    static final Name N_RIGHT                  = DSL.unquotedName("right");
    static final Name N_ROLLUP                 = DSL.unquotedName("rollup");
    static final Name N_ROUND                  = DSL.unquotedName("round");
    static final Name N_ROW                    = DSL.unquotedName("row");
    static final Name N_ROW_NUMBER             = DSL.unquotedName("row_number");
    static final Name N_ROWNUM                 = DSL.unquotedName("rownum");
    static final Name N_ROWSFROM               = DSL.unquotedName("rowsfrom");
    static final Name N_RPAD                   = DSL.unquotedName("rpad");
    static final Name N_RTRIM                  = DSL.unquotedName("rtrim");
    static final Name N_SELECT                 = DSL.unquotedName("select");
    static final Name N_SIGN                   = DSL.unquotedName("sign");
    static final Name N_SINH                   = DSL.unquotedName("sinh");
    static final Name N_SPACE                  = DSL.unquotedName("space");
    static final Name N_SQRT                   = DSL.unquotedName("sqrt");
    static final Name N_STATS_MODE             = DSL.unquotedName("stats_mode");
    static final Name N_STRING_AGG             = DSL.unquotedName("string_agg");
    static final Name N_SUBSTRING              = DSL.unquotedName("substring");
    static final Name N_SYSTEM_TIME            = DSL.unquotedName("system_time");
    static final Name N_T                      = DSL.unquotedName("t");
    static final Name N_TANH                   = DSL.unquotedName("tanh");
    static final Name N_TIMESTAMPDIFF          = DSL.unquotedName("timestampdiff");
    static final Name N_TRANSLATE              = DSL.unquotedName("translate");
    static final Name N_TRIM                   = DSL.unquotedName("trim");
    static final Name N_TRUNC                  = DSL.unquotedName("trunc");
    static final Name N_UPPER                  = DSL.unquotedName("upper");
    static final Name N_VALUES                 = DSL.unquotedName("values");
    static final Name N_WIDTH_BUCKET           = DSL.unquotedName("width_bucket");
    static final Name N_XMLAGG                 = DSL.unquotedName("xmlagg");
    static final Name N_XMLATTRIBUTES          = DSL.unquotedName("xmlattributes");
    static final Name N_XMLCOMMENT             = DSL.unquotedName("xmlcomment");
    static final Name N_XMLCONCAT              = DSL.unquotedName("xmlconcat");
    static final Name N_XMLDOCUMENT            = DSL.unquotedName("xmldocument");
    static final Name N_XMLELEMENT             = DSL.unquotedName("xmlelement");
    static final Name N_XMLFOREST              = DSL.unquotedName("xmlforest");
    static final Name N_XMLPARSE               = DSL.unquotedName("xmlparse");
    static final Name N_XMLPI                  = DSL.unquotedName("xmlpi");
    static final Name N_XMLQUERY               = DSL.unquotedName("xmlquery");
    static final Name N_XMLROOT                = DSL.unquotedName("xmlroot");
    static final Name N_XMLTABLE               = DSL.unquotedName("xmltable");
    static final Name N_XPATH                  = DSL.unquotedName("xpath");

}
