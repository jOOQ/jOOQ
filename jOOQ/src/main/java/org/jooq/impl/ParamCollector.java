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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.tools.StringUtils;

/**
 * A stub {@link BindContext} that acts as a collector of {@link Param}
 * {@link QueryPart}'s
 *
 * @author Lukas Eder
 */
class ParamCollector extends AbstractBindContext {

    final Map<String, Param<?>> result           = new LinkedHashMap<String, Param<?>>();

    ParamCollector(Configuration configuration) {
        super(configuration);
    }

    @Override
    public final PreparedStatement statement() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final void bindInternal(QueryPartInternal internal) {
        if (internal instanceof Param) {
            Param<?> param = (Param<?>) internal;
            String i = String.valueOf(nextIndex());

            if (StringUtils.isBlank(param.getParamName())) {
                result.put(i, param);
            }
            else {
                result.put(param.getParamName(), param);
            }
        }
        else {
            super.bindInternal(internal);
        }
    }

    @Override
    protected final BindContext bindValue0(Object value, Class<?> type) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
