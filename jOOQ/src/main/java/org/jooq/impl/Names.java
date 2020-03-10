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

    static final Name N_ACOS              = DSL.name("acos");
    static final Name N_ARRAY             = DSL.name("array");
    static final Name N_ARRAY_TABLE       = DSL.name("array_table");
    static final Name N_ASCII             = DSL.name("ascii");
    static final Name N_ASIN              = DSL.name("asin");
    static final Name N_ATAN              = DSL.name("atan");
    static final Name N_BIT_COUNT         = DSL.name("bit_count");
    static final Name N_CASE              = DSL.name("case");
    static final Name N_CAST              = DSL.name("cast");
    static final Name N_CEIL              = DSL.name("ceil");
    static final Name N_CHOOSE            = DSL.name("choose");
    static final Name N_COALESCE          = DSL.name("coalesce");
    static final Name N_COLUMN_VALUE      = DSL.name("COLUMN_VALUE");
    static final Name N_CONCAT            = DSL.name("concat");
    static final Name N_CONVERT           = DSL.name("convert");
    static final Name N_COSH              = DSL.name("cosh");
    static final Name N_COT               = DSL.name("cot");
    static final Name N_CURRENT_DATE      = DSL.name("current_date");
    static final Name N_CURRENT_SCHEMA    = DSL.name("current_schema");
    static final Name N_CURRENT_TIME      = DSL.name("current_time");
    static final Name N_CURRENT_TIMESTAMP = DSL.name("current_timestamp");
    static final Name N_CURRENT_USER      = DSL.name("current_user");
    static final Name N_CURRVAL           = DSL.name("currval");
    static final Name N_DATEADD           = DSL.name("dateadd");
    static final Name N_DATEDIFF          = DSL.name("datediff");
    static final Name N_DEGREES           = DSL.name("degrees");
    static final Name N_DUAL              = DSL.name("dual");
    static final Name N_EXTRACT           = DSL.name("extract");
    static final Name N_FLASHBACK         = DSL.name("flashback");
    static final Name N_FLOOR             = DSL.name("floor");
    static final Name N_FUNCTION          = DSL.name("function");
    static final Name N_GENERATE_SERIES   = DSL.name("generate_series");
    static final Name N_GROUP_CONCAT      = DSL.unquotedName("group_concat");
    static final Name N_IIF               = DSL.name("iif");
    static final Name N_JOIN              = DSL.name("join");
    static final Name N_JSON_ARRAY        = DSL.name("json_array");
    static final Name N_JSON_OBJECT       = DSL.name("json_object");
    static final Name N_LEFT              = DSL.name("left");
    static final Name N_LIST              = DSL.unquotedName("list");
    static final Name N_LISTAGG           = DSL.unquotedName("listagg");
    static final Name N_LOWER             = DSL.name("lower");
    static final Name N_LPAD              = DSL.name("lpad");
    static final Name N_LTRIM             = DSL.name("ltrim");
    static final Name N_MD5               = DSL.name("md5");
    static final Name N_MEDIAN            = DSL.name("median");
    static final Name N_MOD               = DSL.name("mod");
    static final Name N_NEXTVAL           = DSL.name("nextval");
    static final Name N_NOT               = DSL.name("not");
    static final Name N_NTILE             = DSL.unquotedName("ntile");
    static final Name N_NULLIF            = DSL.name("nullif");
    static final Name N_NVL               = DSL.name("nvl");
    static final Name N_NVL2              = DSL.name("nvl2");
    static final Name N_OVERLAY           = DSL.name("overlay");
    static final Name N_PIVOT             = DSL.name("pivot");
    static final Name N_POSITION          = DSL.name("position");
    static final Name N_POWER             = DSL.name("power");
    static final Name N_PRIOR             = DSL.name("prior");
    static final Name N_PRODUCT           = DSL.unquotedName("product");
    static final Name N_RANDOM            = DSL.name("rand");
    static final Name N_REVERSE           = DSL.name("reverse");
    static final Name N_RIGHT             = DSL.name("right");
    static final Name N_ROLLUP            = DSL.name("rollup");
    static final Name N_ROW               = DSL.name("row");
    static final Name N_ROWNUM            = DSL.unquotedName("rownum");
    static final Name N_ROW_NUMBER        = DSL.unquotedName("row_number");
    static final Name N_ROWSFROM          = DSL.name("rowsfrom");
    static final Name N_RPAD              = DSL.name("rpad");
    static final Name N_RTRIM             = DSL.name("rtrim");
    static final Name N_SELECT            = DSL.name("select");
    static final Name N_SIGN              = DSL.name("sign");
    static final Name N_SINH              = DSL.name("sinh");
    static final Name N_SPACE             = DSL.name("space");
    static final Name N_SQRT              = DSL.name("sqrt");
    static final Name N_STRING_AGG        = DSL.unquotedName("string_agg");
    static final Name N_SUBSTRING         = DSL.name("substring");
    static final Name N_SYSTEM_TIME       = DSL.unquotedName("system_time");
    static final Name N_T                 = DSL.name("t");
    static final Name N_TANH              = DSL.name("tanh");
    static final Name N_TIMESTAMPDIFF     = DSL.name("timestampdiff");
    static final Name N_TRIM              = DSL.name("trim");
    static final Name N_TRUNC             = DSL.name("trunc");
    static final Name N_UPPER             = DSL.name("upper");
    static final Name N_VALUES            = DSL.name("values");
    static final Name N_WIDTH_BUCKET      = DSL.name("width_bucket");

}
