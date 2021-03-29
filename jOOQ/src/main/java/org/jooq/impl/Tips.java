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

import java.util.Random;

/**
 * A class holding the random tips of the day tips.
 *
 * @author Lukas Eder
 */
final class Tips {

    private static final String[] TIPS = {
        "Whenever jOOQ doesn't support a native SQL feature, use plain SQL templating: https://www.jooq.org/doc/latest/manual/sql-building/plain-sql-templating/",
        "You can extend jOOQ with alternative syntax using plain SQL templating: https://www.jooq.org/doc/latest/manual/sql-building/plain-sql-templating/",
        "You don't have to use jOOQ's DSL. You can use all of jOOQ's API with native SQL as well: https://blog.jooq.org/2020/03/05/using-java-13-text-blocks-for-plain-sql-with-jooq/",
        "When plain SQL templating is not enough, you can write dialect agnostic CustomQueryPart implementations: https://www.jooq.org/doc/latest/manual/sql-building/queryparts/custom-queryparts/",
        "By default, all user values generate implicit bind values in jOOQ. But you can use DSL.val() explicitly, or create inline values (constants, literals) using DSL.inline(), too: https://www.jooq.org/doc/latest/manual/sql-building/bind-values/",
        "A Field<Boolean> can be turned into a Condition using DSL.condition(Field<Boolean>) for use in WHERE, HAVING, etc.: https://www.jooq.org/doc/latest/manual/sql-building/conditional-expressions/boolean-column/",
        "A Condition can be turned into a Field<Boolean> using DSL.field(Condition) for use in SELECT, GROUP BY, ORDER BY, etc.: https://www.jooq.org/doc/latest/manual/sql-building/conditional-expressions/boolean-column/",
        "Views (and table valued functions) are the most underrated SQL feature! They work very well together with jOOQ and jOOQ's code generator! https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-table-valued-functions/",
        "Multi tenancy is best implemented in jOOQ using runtime schema and table mapping: https://www.jooq.org/doc/latest/manual/sql-building/dsl-context/custom-settings/settings-render-mapping/",
        "When creating dynamic SQL Conditions, the DSL.trueCondition(), DSL.falseCondition(), DSL.noCondition(): https://blog.jooq.org/2020/03/06/create-empty-optional-sql-clauses-with-jooq/",
        "You can nest collections directly in SQL using jOOQ's SQL/XML or SQL/JSON support: https://blog.jooq.org/2020/10/09/nesting-collections-with-jooq-3-14s-sql-xml-or-sql-json-support/",
        "jOOQ supports a variety of out-of-the-box export formats, including CSV, XML, JSON, HTML, and Text: https://www.jooq.org/doc/latest/manual/sql-execution/exporting/",
        "Most formats that can be exported can be imported again: https://www.jooq.org/doc/latest/manual/sql-execution/importing/",
        "If you're using the code generator, you can profit from jOOQ's very useful implicit join syntax: https://blog.jooq.org/2018/02/20/type-safe-implicit-join-through-path-navigation-in-jooq-3-11/",
        "Synthetic primary keys / foreign keys are very helpful when working with views: https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-synthetic-objects/codegen-database-synthetic-fks/",
        "An 'example' record can be turned into a predicate using the Query By Example (QBE) API, i.e. the DSL.condition(Record) method: https://www.jooq.org/doc/latest/manual/sql-building/conditional-expressions/query-by-example/"
    };

    static String randomTip() {
        return TIPS[new Random().nextInt(TIPS.length)];
    }
}
