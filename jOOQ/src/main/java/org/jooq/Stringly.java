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
package org.jooq;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jooq.impl.DSL;

/**
 * A container for annotations on stringly typed API.
 * <p>
 * When working with SQL, strings are ubiquitous. They can have different
 * meanings, in the jOOQ API. A string can be:
 * <p>
 * <ul>
 * <li>A bind variable (i.e. a {@link org.jooq.Param})</li>
 * <li>A plain SQL template (i.e. a {@link org.jooq.SQL})</li>
 * <li>An identifier (i.e. a {@link org.jooq.Name})</li>
 * <li>A keyword (i.e. a {@link org.jooq.Keyword})</li>
 * <li>A comment (i.e. a {@link org.jooq.Comment}</li>
 * </ul>
 * <p>
 * While the jOOQ API applies conventions consistently, it may not always be
 * clear which convention applies. Keywords are hardly a problem, but beginners
 * often confuse when jOOQ offers plain SQL templates for convenience (arbitrary
 * SQL fragments, usually not case-sensitive, SQL injection prone), or
 * identifiers (often case-sensitive, depending on the dialect).
 * <p>
 * The annotations in this class are used to document each of these stringly
 * typed API elements to make the semantics more clear. For increased clarity,
 * the stringly-typed convenience API can always be avoided in favour of the
 * more strongly-typed API.
 *
 * @see <a href=
 *      "https://blog.jooq.org/2020/04/03/whats-a-string-in-the-jooq-api/">https://blog.jooq.org/2020/04/03/whats-a-string-in-the-jooq-api/</a>
 * @author Lukas Eder
 */
public final class Stringly {

    /**
     * The annotated string type represents a bind variable, i.e. a
     * {@link org.jooq.Param}.
     */
    @Target(PARAMETER)
    @Retention(SOURCE)
    @Documented
    @Inherited
    public @interface Param {
    }

    /**
     * The annotated string type represents a plain SQL template, i.e. a
     * {@link org.jooq.SQL}, which will be wrapped by {@link DSL#sql(String)}.
     */
    @Target(PARAMETER)
    @Retention(SOURCE)
    @Documented
    @Inherited
    public @interface SQL {
    }

    /**
     * The annotated string type represents an identifier, i.e. a
     * {@link org.jooq.Name}, which will be wrapped by {@link DSL#name(String)}.
     */
    @Target(PARAMETER)
    @Retention(SOURCE)
    @Documented
    @Inherited
    public @interface Name {
    }

    /**
     * The annotated string type represents a keyword {@link org.jooq.Keyword},
     * which will be wrapped by {@link DSL#keyword(String)}.
     */
    @Target(PARAMETER)
    @Retention(SOURCE)
    @Documented
    @Inherited
    public @interface Keyword {
    }

    /**
     * The annotated string type represents a keyword {@link org.jooq.Comment},
     * which will be wrapped by {@link DSL#comment(String)}.
     */
    @Target(PARAMETER)
    @Retention(SOURCE)
    @Documented
    @Inherited
    public @interface Comment {
    }
}
