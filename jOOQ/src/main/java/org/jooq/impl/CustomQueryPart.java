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

import static org.jooq.Clause.CUSTOM;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.QueryPart;
import org.jooq.RenderContext;
import org.jooq.exception.DataAccessException;

/**
 * A base class for custom {@link QueryPart} implementations in client code.
 * <p>
 * Client code may provide proper {@link QueryPart} implementations extending
 * this useful base class. All necessary parts of the {@link QueryPart}
 * interface are already implemented. Only these two methods need further
 * implementation:
 * <ul>
 * <li>{@link #toSQL(org.jooq.RenderContext)}</li>
 * <li>{@link #bind(org.jooq.BindContext)}</li>
 * </ul>
 * Refer to those methods' Javadoc for further details about their expected
 * behaviour.
 * <p>
 * Such custom <code>QueryPart</code> implementations can be useful in any of
 * these scenarios:
 * <ul>
 * <li>When reusing a custom <code>QueryPart</code> in other custom
 * <code>QueryPart</code>s, e.g. in {@link CustomCondition}, {@link CustomField}, {@link CustomTable}, etc.</li>
 * <li>When inlining a custom <code>QueryPart</code> in plain SQL methods, such
 * as {@link DSL#condition(String, QueryPart...)},
 * {@link DSL#field(String, QueryPart...)},
 * {@link DSL#table(String, QueryPart...)}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public abstract class CustomQueryPart extends AbstractQueryPart {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -3439681086987884991L;
    private static final Clause[] CLAUSES          = { CUSTOM };

    protected CustomQueryPart() {
    }

    // -------------------------------------------------------------------------
    // Implementation required
    // -------------------------------------------------------------------------

    /**
     * Subclasses must implement this method
     * <hr/>
     * {@inheritDoc}
     */
    @Override
    public abstract void toSQL(RenderContext context);

    // -------------------------------------------------------------------------
    // Implementation optional
    // -------------------------------------------------------------------------

    /**
     * Subclasses may implement this method
     * <hr/>
     * {@inheritDoc}
     */
    @Override
    public void bind(BindContext context) throws DataAccessException {
    }

    // -------------------------------------------------------------------------
    // No further overrides allowed
    // -------------------------------------------------------------------------

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final boolean declaresFields() {
        return super.declaresFields();
    }

    @Override
    public final boolean declaresTables() {
        return super.declaresTables();
    }
}
