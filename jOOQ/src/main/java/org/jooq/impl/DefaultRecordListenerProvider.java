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

import java.io.Serializable;

import org.jooq.RecordListener;
import org.jooq.RecordListenerProvider;

/**
 * A default implementation for {@link RecordListenerProvider}.
 * <p>
 * This implementation just wraps an instance of {@link RecordListener}, always
 * providing the same.
 *
 * @author Lukas Eder
 */
public class DefaultRecordListenerProvider implements RecordListenerProvider, Serializable {

    /**
     * Generated UID.
     */
    private static final long    serialVersionUID = -2122007794302549679L;

    /**
     * The delegate list.
     */
    private final RecordListener listener;

    /**
     * Convenience method to construct an array of
     * <code>DefaultRecordListenerProvider</code> from an array of
     * <code>RecordListener</code> instances.
     */
    public static RecordListenerProvider[] providers(RecordListener... listeners) {
        RecordListenerProvider[] result = new RecordListenerProvider[listeners.length];

        for (int i = 0; i < listeners.length; i++) {
            result[i] = new DefaultRecordListenerProvider(listeners[i]);
        }

        return result;
    }

    /**
     * Create a new provider instance from an argument listener.
     *
     * @param listener The argument listener.
     */
    public DefaultRecordListenerProvider(RecordListener listener) {
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final RecordListener provide() {
        return listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return listener.toString();
    }
}
