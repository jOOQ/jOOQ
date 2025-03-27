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
        "You don't have to use jOOQ's DSL. You can use all of jOOQ's API with native SQL as well: https://blog.jooq.org/using-java-13-text-blocks-for-plain-sql-with-jooq/",
        "When plain SQL templating is not enough, you can write dialect agnostic CustomQueryPart implementations: https://www.jooq.org/doc/latest/manual/sql-building/queryparts/custom-queryparts/",
        "By default, all user values generate implicit bind values in jOOQ. But you can use DSL.val() explicitly, or create inline values (constants, literals) using DSL.inline(), too: https://www.jooq.org/doc/latest/manual/sql-building/bind-values/",
        "A Field<Boolean> can be turned into a Condition using DSL.condition(Field<Boolean>) for use in WHERE, HAVING, etc.: https://www.jooq.org/doc/latest/manual/sql-building/conditional-expressions/boolean-column/",
        "A Condition can be turned into a Field<Boolean> using DSL.field(Condition) for use in SELECT, GROUP BY, ORDER BY, etc.: https://www.jooq.org/doc/latest/manual/sql-building/conditional-expressions/boolean-column/ (though it might not be necessary: https://blog.jooq.org/a-condition-is-a-field/)",
        "Views (and table valued functions) are the most underrated SQL feature! They work very well together with jOOQ and jOOQ's code generator! https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-table-valued-functions/",
        "Multi tenancy is best implemented in jOOQ using runtime schema and table mapping: https://www.jooq.org/doc/latest/manual/sql-building/dsl-context/custom-settings/settings-render-mapping/",
        "When creating dynamic SQL Conditions, the DSL.trueCondition(), DSL.falseCondition(), DSL.noCondition(): https://blog.jooq.org/create-empty-optional-sql-clauses-with-jooq/",
        "You can nest collections directly in SQL using jOOQ's SQL/XML or SQL/JSON support: https://blog.jooq.org/nesting-collections-with-jooq-3-14s-sql-xml-or-sql-json-support/",
        "jOOQ supports a variety of out-of-the-box export formats, including CSV, XML, JSON, HTML, and Text: https://www.jooq.org/doc/latest/manual/sql-execution/exporting/",
        "Most formats that can be exported can be imported again: https://www.jooq.org/doc/latest/manual/sql-execution/importing/",
        "If you're using the code generator, you can profit from jOOQ's very useful implicit join syntax: https://blog.jooq.org/type-safe-implicit-join-through-path-navigation-in-jooq-3-11/",
        "Synthetic primary keys / foreign keys are very helpful when working with views: https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-synthetic-objects/codegen-database-synthetic-fks/",
        "An 'example' record can be turned into a predicate using the Query By Example (QBE) API, i.e. the DSL.condition(Record) method: https://www.jooq.org/doc/latest/manual/sql-building/conditional-expressions/query-by-example/",
        "When working with jOOQ and JPA, you can generate your jOOQ code also from your JPA entities rather than a live database: https://www.jooq.org/doc/latest/manual/code-generation/codegen-jpa/",
        "The jOOQ code generator doesn't have to connect to a live database. It can reverse engineer your DDL scripts (e.g. the Flyway ones), too, if you're not using anything too fancy vendor specific: https://www.jooq.org/doc/latest/manual/code-generation/codegen-ddl/",
        "The jOOQ code generator can generate code from an XML representation of your schema: https://www.jooq.org/doc/latest/manual/code-generation/codegen-xml/",
        "If you're using Liquibase to migrate your schema, the jOOQ code generator can simulate a Liquibase migration to produce its generated code, rather than connecting to an actual database: https://www.jooq.org/doc/latest/manual/code-generation/codegen-liquibase/",
        "To version control jOOQ generated code or not? There are pros and cons to both approaches: https://www.jooq.org/doc/latest/manual/code-generation/codegen-version-control/",
        "While you don't have to use jOOQ's code generator, there are *lots* of awesome features you're missing out on if you're not using it!",
        "A NoClassDefFoundError or ClassNotFoundException is often a sign that your jOOQ code is generated with a different version of jOOQ than runtime library you're using",
        "The logical order of SELECT operations doesn't match the lexical (syntactic) order of operations. Learn more about this here: https://blog.jooq.org/a-beginners-guide-to-the-true-order-of-sql-operations/",
        "If you must be using OFFSET pagination (you probably shouldn't!), then do it right, and without extra round trips: https://blog.jooq.org/calculating-pagination-metadata-without-extra-roundtrips-in-sql/",
        "Keyset pagination is usually a faster and more useful way to paginate than OFFSET, and jOOQ supports it out of the box via the synthetic SEEK clause: https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/seek-clause/",
        "Want to learn more about SQL? Check out our many blog posts: https://blog.jooq.org/sql",
        "You don't *have to* map SQL results to POJOs if you're consuming XML or JSON in the end. Generate the XML or JSON directly in SQL, instead! https://blog.jooq.org/stop-mapping-stuff-in-your-middleware-use-sqls-xml-or-json-operators-instead/",
        "You can quickly navigate between child and parent records using TableRecord::fetchParent or TableRecord::parent and UpdatableRecord::fetchChildren or UpdatableRecord::children. Beware of N+1 problems, though!",
        "A quick way to create a new Condition from a table is to just write MY_TABLE.where(condition)",
        "jOOQ Queries extend Flow.Publisher, and can thus be used in reactive APIs. Blocking on JDBC by default, but if you configure an R2DBC ConnectionFactory, it's automatically non-blocking!",
        "A ResultQuery<R> extends Iterable<R>, so you can just foreach your queries! See: https://blog.jooq.org/a-hidden-jooq-gem-foreach-loop-over-resultquery/",
        "jOOQ works even better in kotlin! https://blog.jooq.org/10-nice-examples-of-writing-sql-in-kotlin-with-jooq/",
        "Don't want tips? Use the -Dorg.jooq.no-tips=true system property",
        "You don't have to register your Converters or Bindings with the code generator. You can use ad-hoc converters on any Field expression: https://www.jooq.org/doc/latest/manual/sql-execution/fetching/ad-hoc-converter/",
        "There's no easier way to nest collections directly in SQL than using MULTISET, e.g. to collect your to-many relationships: https://www.jooq.org/doc/latest/manual/sql-building/column-expressions/multiset-value-constructor/",
        "Want to aggregate multiple values per group? Use the synthetic MULTISET_AGG aggregate function! https://www.jooq.org/doc/latest/manual/sql-building/column-expressions/aggregate-functions/multiset-agg-function/",
        "You can formally nest records directly in SQL by projecting any ROW type: https://www.jooq.org/doc/latest/manual/sql-building/column-expressions/nested-records/",
        "Need a random value from a group? Use the ANY_VALUE aggregate function, which is supported natively in a few dialects: https://www.jooq.org/doc/latest/manual/sql-building/column-expressions/aggregate-functions/any-value-function/",
        "Using jOOQ's parsers, and can't parse some vendor specific syntax? The ParseListener SPI will help you: https://www.jooq.org/doc/latest/manual/sql-building/sql-parser/sql-parser-listener/",
        "Move logic to your data, not data to your logic, by running procedural code as a stored function: https://www.jooq.org/doc/latest/manual/sql-building/ddl-statements/create-statement/create-function-statement/",
        "The code generator produces Javadoc from table or column comments, but you can override those directly in the code generator: https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-comments/",
        "Want to exclude some columns from your write operations? No problem, specify them as 'readonly' in the code generator: https://www.jooq.org/doc/latest/manual/sql-building/column-expressions/readonly-columns/",
        "Need to frequently calculate the same expressions based on other columns from the same table? Many dialects support computed columns: https://www.jooq.org/doc/latest/manual/sql-building/column-expressions/computed-columns/",
        "You can use Table<R> references as SelectField<R> to project nested TableRecords in any query: https://blog.jooq.org/projecting-type-safe-nested-tablerecords-with-jooq-3-17/",
        "Just like in SQL, a Condition is just a Field<Boolean>, so you can use conditions wherever you can use fields, e.g. in SELECT, GROUP BY, ORDER BY, etc.: https://blog.jooq.org/a-condition-is-a-field/",
        "For dynamic SQL, DSL.noCondition() helps create optional conditions, just like DSL.noField() helps create optional fields in the fluent API.",
        "In order to improve cardinality estimates, it can be valuable to auto-inline bind variables on certain columns, e.g. on enum types: https://www.jooq.org/doc/latest/manual/sql-building/dsl-context/custom-settings/settings-auto-inline-bind-values/",
        "Kotlin users can profit from a variety of convenient extension functions available from the jooq-kotlin module",
        "jOOQ offers out of the box functionality for common scenarios related to audit columns: https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-forced-types/codegen-database-forced-types-audit/",
        "Client side computed columns are very useful if you have redundant behaviour in your queries and data: https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-forced-types/codegen-database-forced-types-computed/",
        "A list of common anti patterns in jOOQ or SQL is available here: https://www.jooq.org/doc/latest/manual/reference/dont-do-this/",
        "In addition to <includes> and <excludes>, you can also use <includesSql> and <excludesSql> to generate regular expressions for the code generator! https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-includes-excludes/",
        "jOOQ can run diagnostics on all the queries passed to it, including if you're using JDBC or R2DBC directly: https://www.jooq.org/doc/latest/manual/sql-execution/diagnostics/",
        "Common SQL patterns can be matched and transformed into more optimal or simpler SQL using pattern transformation, both on jOOQ queries or ordinary JDBC based queries: https://www.jooq.org/doc/latest/manual/sql-building/queryparts/sql-transformation/transform-patterns/",
        "The most common jOOQ types like Field, Condition, Table are summarised in this blog post: https://blog.jooq.org/a-brief-overview-over-the-most-common-jooq-types/",
        "Sometimes, a native query can be the better choice than writing a jOOQ query. Here's when that happens: https://blog.jooq.org/when-to-use-jooq-and-when-to-use-native-sql/",
        "Thinking of building a query with jOOQ and executing it elsewhere? Think again: https://blog.jooq.org/why-you-should-execute-jooq-queries-with-jooq/",
        "You can generate jOOQ code in a single package with package private visibility if this helps. Here's how: https://blog.jooq.org/how-to-generate-package-private-code-with-jooqs-code-generator/",
        "Want to collect a flat representation of tree data into a hierarchy? Use this Collector based approach: https://blog.jooq.org/how-to-turn-a-list-of-flat-elements-into-a-hierarchy-in-java-sql-or-jooq/",
        "When it comes to using Converter or Binding, only the first UNION subquery is relevant. Learn more about this here: https://blog.jooq.org/how-to-use-jooqs-converters-with-union-operations/",
        "Want to upgrade your jOOQ experience? Check out these commercial only features of the jOOQ Professional and Enterprise Editions: https://www.jooq.org/commercial-only-features/",
        "Should you use jOOQ's DAOs? They're convenient, but don't overuse them. SQL is often more powerful: https://blog.jooq.org/to-dao-or-not-to-dao/",
        "Want to hide some legacy columns from your queries? Use jOOQ's hidden column support: https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-hidden-columns/",
        "Different jOOQ editions support multiple JDK versions. Check out the complete list here: https://www.jooq.org/download/support-matrix-jdk",
        "Different jOOQ editions support multiple RDBMS versions. Check out the complete list here: https://www.jooq.org/download/support-matrix",
        "By default, jOOQ's UpdatableRecords track \"touched\" dirty state. You can switch them to track \"modified\" dirty state instead: https://www.jooq.org/doc/latest/manual/sql-building/dsl-context/custom-settings/settings-dirty-tracking/",
        "Your schema may encode ENUM semantics into a column using a CHECK constraint. Turn that into a generated enum, easily: https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-synthetic-objects/codegen-database-synthetic-enums/",
        ""
    };

    static String randomTip() {
        return TIPS[new Random().nextInt(TIPS.length)];
    }
}
