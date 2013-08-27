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
package org.jooq;

import org.jooq.impl.DefaultRecordMapper;
import org.jooq.impl.DefaultRecordMapperProvider;

/**
 * A provider for {@link RecordMapper} instances.
 * <p>
 * In order to inject custom {@link Record} to <code>POJO</code> mapping
 * behaviour, users can supply a custom {@link RecordMapperProvider} to their
 * {@link Configuration} instances. This provider will be used in any of these
 * methods (non-exhaustive list):
 * <h3><code>Cursor</code></h3>
 * <ul>
 * <li> {@link Cursor#fetchInto(Class)}</li>
 * <li> {@link Cursor#fetchOneInto(Class)}</li>
 * </ul>
 * <h3><code>Record</code></h3>
 * <ul>
 * <li> {@link Record#into(Class)}</li>
 * </ul>
 * <h3><code>Result</code></h3>
 * <ul>
 * <li> {@link Result#intoMap(Field, Class)}</li>
 * <li> {@link Result#intoMap(Field[], Class)}</li>
 * <li> {@link Result#intoGroups(Field, Class)}</li>
 * <li> {@link Result#intoGroups(Field[], Class)}</li>
 * <li> {@link Result#into(Class)}</li>
 * </ul>
 * <h3><code>ResultQuery</code></h3>
 * <ul>
 * <li> {@link ResultQuery#fetchMap(Field, Class)}</li>
 * <li> {@link ResultQuery#fetchMap(Field[], Class)}</li>
 * <li> {@link ResultQuery#fetchGroups(Field, Class)}</li>
 * <li> {@link ResultQuery#fetchGroups(Field[], Class)}</li>
 * <li> {@link ResultQuery#fetchInto(Class)}</li>
 * <li> {@link ResultQuery#fetchOneInto(Class)}</li>
 * </ul>
 * <h3><code>DAO</code></h3>
 * <ul>
 * <li>Most {@link DAO} methods make use of any of the above methods</li>
 * </ul>
 *
 * @author Lukas Eder
 * @see RecordMapper
 * @see Configuration
 */
public interface RecordMapperProvider {

    /**
     * Provide a <code>RecordMapper</code> instance.
     * <p>
     * Implementations are free to choose whether this method returns new
     * instances at every call or whether the same instance is returned
     * repetitively.
     * <p>
     * A <code>RecordMapper</code> instance should be able to map any number of
     * records with the same <code>RecordType</code>. For example, for
     * {@link Record#into(Class)}, <code>provide()</code> and
     * {@link RecordMapper#map(Record)} are called only once. For
     * {@link Result#into(Class)}, <code>provide()</code> is called only once,
     * but {@link RecordMapper#map(Record)} is called several times, once for
     * every <code>Record</code> in the <code>Result</code>.
     *
     * @param recordType The <code>RecordType</code> of records that shall be
     *            mapped by the returned <code>RecordMapper</code>.
     * @param type The user type that was passed into {@link Record#into(Class)}
     *            or any other method.
     * @return A <code>RecordMapper</code> instance.
     * @see RecordMapper
     * @see DefaultRecordMapper
     * @see DefaultRecordMapperProvider
     */
    <R extends Record, E> RecordMapper<R, E> provide(RecordType<R> recordType, Class<? extends E> type);
}
