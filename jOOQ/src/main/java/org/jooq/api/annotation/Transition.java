/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * Defines a "transition" in the state machine modelled by the DSL API.
 *
 * @author Lukas Eder
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Transition {

    /**
     * The Transition name.
     * <p>
     * This describes the name of a transition in the state machine modelled by
     * the DSL API. By default, the transition name matches the method name of
     * the annotated method.
     */
    String name() default "";

    /**
     * The Transition arguments.
     * <p>
     * This describes the arguments of a transition in the state machine
     * modelled by the DSL API.
     */
    String[] args() default "";

    /**
     * The Transition source state.
     * <p>
     * This describes the source state name of a transition in the state machine
     * modelled by the DSL API. By default, the transition's source state name
     * matches the state name of the declaring type of the annotated method:
     * <ul>
     * <li>If {@link #from()} is defined, that value is taken</li>
     * <li>Otherwise, if the annotated method's
     * {@link Method#getDeclaringClass()} is annotated with {@link State}, that
     * state's {@link State#name()} is taken</li>
     * <li>Otherwise, the {@link Method#getDeclaringClass()} class name is taken
     * </li>
     * </ul>
     */
    String from() default "";

    /**
     * The Transition target state.
     * <p>
     * This describes the target state name of a transition in the state machine
     * modelled by the DSL API. By default, the transition's target state name
     * matches the state name of the return type of the annotated method:
     * <ul>
     * <li>If {@link #to()} is defined, that value is taken</li>
     * <li>Otherwise, if the annotated method's {@link Method#getReturnType()}
     * is annotated with {@link State}, that state's {@link State#name()} is
     * taken</li>
     * <li>Otherwise, the Method#getReturnType() class name is taken</li>
     * </ul>
     */
    String to() default "";
}
