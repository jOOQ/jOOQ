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

import java.util.concurrent.Future;

/**
 * Fetch results asynchronously.
 * <p>
 * This type wraps fetching of records in a {@link java.util.concurrent.Future},
 * such that you can access the actual records at a future instant. This is
 * especially useful when
 * <ul>
 * <li>You want to load heavy data in the background, for instance when the user
 * logs in and accesses a pre-calculated dashboard screen, before they access
 * the heavy data.</li>
 * <li>You want to parallelise several independent OLAP queries before merging
 * all data into a single report</li>
 * <li>...</li>
 * </ul>
 *
 * @deprecated - 3.2.0 - [#2581] - This type will be removed in jOOQ 4.0
 */
@Deprecated
public interface FutureResult<R extends Record> extends Future<Result<R>> {

}
