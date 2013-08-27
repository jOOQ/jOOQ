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

import java.io.Serializable;
import java.sql.Connection;

import javax.sql.DataSource;

/**
 * An object in jOOQ that can have an underlying {@link Configuration} attached
 * or detached.
 * <p>
 * Detaching an <code>Attachable</code> from its configuration means, that the
 * underlying {@link Connection} or {@link DataSource} is removed. Attaching an
 * <code>Attachable</code> to a new <code>Configuration</code> means, that its
 * underlying <code>Connection</code> or <code>DataSource</code> will be
 * restored.
 * <p>
 * Detaching an <code>Attachable</code> will <b>NOT</b> close the underlying
 * <code>Connection</code> or <code>DataSource</code>!
 * <p>
 * Attachables are also {@link Serializable}. The underlying
 * <code>Connection</code> or <code>DataSource</code> is <code>transient</code>.
 * Serialising an Attachable will always detach it first.
 *
 * @author Lukas Eder
 */
public interface Attachable extends Serializable {

    /**
     * Attach this object to a new {@link Configuration}
     *
     * @param configuration A configuration or <code>null</code>, if you wish to
     *            detach this <code>Attachable</code> from its previous
     *            configuration.
     */
    void attach(Configuration configuration);
}
