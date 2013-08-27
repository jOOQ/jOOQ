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

import java.util.Arrays;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.RenderContext;
import org.jooq.tools.StringUtils;

/**
 * The default implementation for a SQL identifier
 *
 * @author Lukas Eder
 */
class NameImpl extends AbstractQueryPart implements Name {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 8562325639223483938L;

    private String[]          qualifiedName;

    NameImpl(String[] qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    @Override
    public final void toSQL(RenderContext context) {
        String separator = "";

        for (String name : qualifiedName) {
            if (!StringUtils.isEmpty(name)) {
                context.sql(separator).literal(name);
                separator = ".";
            }
        }
    }

    @Override
    public final void bind(BindContext context) {}

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public final String[] getName() {
        return qualifiedName;
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return Arrays.hashCode(getName());
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        // [#1626] NameImpl equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof NameImpl) {
            return Arrays.equals(getName(), (((NameImpl) that).getName()));
        }

        return super.equals(that);
    }
}
