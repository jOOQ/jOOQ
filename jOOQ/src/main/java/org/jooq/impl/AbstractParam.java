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

import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_VALUE;
import static org.jooq.conf.ParamType.INLINED;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Param;
import org.jooq.RenderContext;

/**
 * A base implementation for {@link Param}
 *
 * @author Lukas Eder
 */
abstract class AbstractParam<T> extends AbstractField<T> implements Param<T> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 1311856649676227970L;
    private static final Clause[] CLAUSES          = { FIELD, FIELD_VALUE };

    private final String      paramName;
    T                         value;
    private boolean           inline;

    AbstractParam(T value, DataType<T> type) {
        this(value, type, null);
    }

    AbstractParam(T value, DataType<T> type, String paramName) {
        super(name(value, paramName), type);

        this.paramName = paramName;
        this.value = value;
    }

    /**
     * A utility method that generates a field name.
     * <p>
     * <ul>
     * <li>If <code>paramName != null</code>, take <code>paramName</code></li>
     * <li>Otherwise, take the string value of <code>value</code></li>
     * </ul>
     */
    private static String name(Object value, String paramName) {
        return paramName == null ? String.valueOf(value) : paramName;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    // ------------------------------------------------------------------------
    // XXX: Param API
    // ------------------------------------------------------------------------

    @Override
    public final void setValue(T value) {
        setConverted(value);
    }

    @Override
    public final void setConverted(Object value) {
        this.value = getDataType().convert(value);
    }

    @Override
    public final T getValue() {
        return value;
    }

    @Override
    public final String getParamName() {
        return paramName;
    }

    @Override
    public final void setInline(boolean inline) {
        this.inline = inline;
    }

    @Override
    public final boolean isInline() {
        return inline;
    }

    final boolean isInline(RenderContext context) {
        return isInline() || context.paramType() == INLINED;
    }
}
