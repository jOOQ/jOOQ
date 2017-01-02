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
package org.jooq.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a "state" in the state machine modelled by the DSL API.
 *
 * @author Lukas Eder
 * @deprecated - 3.5.0 - [#3345] - The DSL API annotation is in a prototype
 *             state and will be removed from the 3.x APIs
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Deprecated
public @interface State {

    /**
     * The State name.
     * <p>
     * This describes the name of a state in the state machine modelled by the
     * DSL API. By default, the state name matches the type name of the
     * annotated type.
     */
    String name() default "";

    /**
     * The State's aliases.
     * <p>
     * This describes the alias names of a state in the state machine modelled
     * by the DSL API.
     */
    String[] aliases() default "";

    /**
     * The State's level.
     * <p>
     * This describes whether the state is considered a "terminal state", i.e. a
     * state from which the state transitions can "exit".
     */
    boolean terminal() default false;
}
