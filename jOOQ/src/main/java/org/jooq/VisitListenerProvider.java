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

import org.jooq.impl.DefaultVisitListenerProvider;

/**
 * A provider for {@link VisitListener} instances.
 * <p>
 * In order to facilitate the lifecycle management of <code>VisitListener</code>
 * instances that are provided to a jOOQ {@link Configuration}, clients can
 * implement this API. To jOOQ, it is thus irrelevant, if execute listeners are
 * stateful or stateless, local to a single record or record manipulation, or
 * global to an application.
 *
 * @author Lukas Eder
 * @see VisitListener
 * @see Configuration
 */
public interface VisitListenerProvider {

    /**
     * Provide a <code>VisitListener</code> instance.
     * <p>
     * Implementations are free to choose whether this method returns new
     * instances at every call or whether the same instance is returned
     * repetitively.
     * <p>
     * A <code>VisitListener</code> shall be provided exactly once per
     * <code>Context</code> traversal, i.e. per <code>RenderContext</code> or
     * <code>BindContext</code>.
     *
     * @return A <code>VisitListener</code> instance.
     * @see VisitListener
     * @see VisitContext
     * @see DefaultVisitListenerProvider
     */
    VisitListener provide();
}
