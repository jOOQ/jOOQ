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

import java.util.HashMap;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.ExecuteType;
import org.jooq.Record;
import org.jooq.RecordContext;

/**
 * A default implementation for {@link RecordContext}.
 *
 * @author Lukas Eder
 */
class DefaultRecordContext implements RecordContext {

    private final Configuration           configuration;
    private final HashMap<Object, Object> data;
    private final ExecuteType             type;
    private final Record[]                records;

    DefaultRecordContext(Configuration configuration, ExecuteType type, Record... records) {
        this.configuration = configuration;
        this.type = type;
        this.data = new HashMap<Object, Object>();
        this.records = records;
    }

    @Override
    public final Map<Object, Object> data() {
        return data;
    }

    @Override
    public final Object data(Object key) {
        return data.get(key);
    }

    @Override
    public final Object data(Object key, Object value) {
        return data.put(key, value);
    }

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    @Override
    public final ExecuteType type() {
        return type;
    }

    @Override
    public final Record record() {
        return records != null && records.length > 0 ? records[0] : null;
    }

    @Override
    public final Record[] batchRecords() {
        return records;
    }
}
