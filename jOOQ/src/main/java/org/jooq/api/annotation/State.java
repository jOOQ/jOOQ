/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
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
