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
package org.jooq;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Require a set of {@link SQLDialect} to be supported by any jOOQ statement in
 * the scope of this annotation.
 * <p>
 * This annotation can be used at the use-site of jOOQ API at any given scope
 * {@link ElementType#PACKAGE}, {@link ElementType#TYPE},
 * {@link ElementType#METHOD} in order to specify that the given scope requires
 * ALL of the supplied {@link SQLDialect} to be supported by all usage of jOOQ
 * API within the scope. For example:
 * <p>
 * <code><pre>
 * // Allow only MYSQL or ORACLE dialect support to be used within the class scope
 * &#64;Allow(MYSQL, ORACLE)
 * public class MySQLAndOracleDAO {
 *
 *     // Allow rule from class applies to this method
 *     public void mysqlAndOracleMethod() {
 *         DSL.using(configuration)
 *            .insertInto(TABLE, TABLE.COLUMN)
 *            .values(1)
 *            // This type checks as it works on both MySQL and Oracle
 *            .onDuplicateKeyUpdate()
 *            .set(TABLE.COLUMN, 2)
 *            .execute();
 *     }
 *
 *     // Refine class Allow rule with additional requirement
 *     &#64;Require(ORACLE)
 *     public void oracleOnlyMethod() {
 *         DSL.using(configuration)
 *            .mergeInto(TABLE)
 *            .using(selectOne())
 *            .on(TABLE.COLUMN.eq(1))
 *            .whenMatchedThenUpdate()
 *            .set(TABLE.COLUMN, 2)
 *            .whenNotMatchedThenInsert(TABLE.COLUMN)
 *            .values(1)
 *            .execute();
 *     }
 * }
 * </pre></code>
 * <p>
 * Type checking for these annotations can be supplied by
 * <code>org.jooq.checker.SQLDialectChecker</code> from the jOOQ-checker module.
 * <p>
 * Type checking for these annotations can be supplied by
 * <code>org.jooq.checker.SQLDialectChecker</code> from the jOOQ-checker module.
 * <h2>Rules:</h2>
 * <ul>
 * <li>In the absence of any {@link Allow} annotation, no jOOQ API usage is
 * allowed.</li>
 * <li>The combination of all {@link Allow} annotations and of the inner-most
 * {@link Require} annotation is applied for any given scope.</li>
 * <li>Nested packages are not creating nested scopes.</li>
 * <li>If a versioned {@link SQLDialect} is required (rather than a
 * {@link SQLDialect#family()}), then the required version, any of its
 * {@link SQLDialect#predecessor()}, or its {@link SQLDialect#family()} are
 * required.</li>
 * </ul>
 *
 * @author Lukas Eder
 * @see Allow
 */
@Target({ METHOD, CONSTRUCTOR, TYPE, PACKAGE })
@Retention(RUNTIME)
@Documented
@Inherited
public @interface Require {

    /**
     * A list of jOOQ {@link SQLDialect} which are required on any jOOQ API
     * method that is annotated with {@link Support}.
     */
    SQLDialect[] value() default {};
}
