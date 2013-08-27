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

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.inline;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class FieldCondition extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -9170915951443879057L;
    private final Field<Boolean> field;

    FieldCondition(Field<Boolean> field) {
        this.field = field;
    }

    @Override
    public void toSQL(RenderContext ctx) {
        delegate(ctx.configuration()).toSQL(ctx);
    }

    @Override
    public void bind(BindContext ctx) {
        delegate(ctx.configuration()).bind(ctx);
    }

    private final QueryPartInternal delegate(Configuration configuration) {
        switch (configuration.dialect().family()) {

            // [#2485] These don't work nicely, yet
            case CUBRID:
            case FIREBIRD:

            // These do
            case INGRES:
            case ORACLE:
            case SQLSERVER:
            case SYBASE:
                return (QueryPartInternal) condition("{0} = {1}", field, inline(true));


            // Untested yet
            case ASE:
            case DB2:

            // Native support
            case DERBY:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            default:
                return (QueryPartInternal) field;
        }
    }
}
