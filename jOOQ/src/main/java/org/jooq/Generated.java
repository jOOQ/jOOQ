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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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
package org.jooq;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An alternative annotation to replace
 * {@link javax.annotation.processing.Generated} from the JDK, in order to
 * elevate scope to {@link RetentionPolicy#RUNTIME}, keeping an identical
 * structure.
 */
@Documented
@Retention(RUNTIME)
@Target({ PACKAGE, TYPE, METHOD, CONSTRUCTOR, FIELD, LOCAL_VARIABLE, PARAMETER })
public @interface Generated {

    /**
     * The name of the code generator.
     *
     * @see javax.annotation.processing.Generated#value()
     */
    String[] value();

    /**
     * The date the source was generated.
     *
     * @see javax.annotation.processing.Generated#date()
     */
    String date() default "";

    /**
     * Comments that the code generated included.
     *
     * @see javax.annotation.processing.Generated#comments()
     */
    String comments() default "";
}
