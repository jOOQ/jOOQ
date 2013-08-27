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
package org.jooq.impl;

import org.jooq.Case;
import org.jooq.CaseConditionStep;
import org.jooq.CaseValueStep;
import org.jooq.Condition;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class CaseImpl implements Case {

    CaseImpl() {
    }

    @Override
    public final <V> CaseValueStep<V> value(V value) {
        return value(Utils.field(value));
    }

    @Override
    public final <V> CaseValueStep<V> value(Field<V> value) {
        return new CaseValueStepImpl<V>(value);
    }

    @Override
    public final <T> CaseConditionStep<T> when(Condition condition, T result) {
        return when(condition, Utils.field(result));
    }

    @Override
    public final <T> CaseConditionStep<T> when(Condition condition, Field<T> result) {
        return new CaseConditionStepImpl<T>(condition, result);
    }
}
