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

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation marks all methods and types that allow for producing a
 * {@link QueryPart} that generates "plain SQL" inside of an AST.
 * <p>
 * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must guarantee
 * syntax integrity. You may also create the possibility of malicious SQL
 * injection. Be sure to properly use bind variables and/or escape literals when
 * concatenated into SQL clauses!
 *
 * @author Lukas Eder
 * @see SQL
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RUNTIME)
@Documented
@Inherited
public @interface PlainSQL {

}
