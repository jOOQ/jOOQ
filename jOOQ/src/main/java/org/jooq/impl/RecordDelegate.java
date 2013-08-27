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

import static org.jooq.ExecuteType.READ;
import static org.jooq.ExecuteType.WRITE;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.LOAD;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.REFRESH;

import org.jooq.Configuration;
import org.jooq.ExecuteType;
import org.jooq.Record;
import org.jooq.RecordContext;
import org.jooq.RecordListener;
import org.jooq.RecordListenerProvider;

/**
 * A stub for {@link Record} objects, abstracting {@link RecordListener}
 * lifecycle handling.
 *
 * @author Lukas Eder
 */
class RecordDelegate<R extends Record> {

    private final Configuration       configuration;
    private final R                   record;
    private final RecordLifecycleType type;

    RecordDelegate(Configuration configuration, R record) {
        this(configuration, record, LOAD);
    }

    RecordDelegate(Configuration configuration, R record, RecordLifecycleType type) {
        this.configuration = configuration;
        this.record = record;
        this.type = type;
    }

    static final <R extends Record> RecordDelegate<R> delegate(Configuration configuration, R record) {
        return new RecordDelegate<R>(configuration, record);
    }

    static final <R extends Record> RecordDelegate<R> delegate(Configuration configuration, R record, RecordLifecycleType type) {
        return new RecordDelegate<R>(configuration, record, type);
    }

    final <E extends Exception> R operate(RecordOperation<R, E> operation) throws E {
        RecordListenerProvider[] providers = null;
        RecordListener[] listeners = null;
        RecordContext ctx = null;

        if (configuration != null) {
            providers = configuration.recordListenerProviders();

            if (providers != null) {
                listeners = new RecordListener[providers.length];
                ctx = new DefaultRecordContext(configuration, executeType(), record);

                for (int i = 0; i < providers.length; i++) {
                    listeners[i] = providers[i].provide();
                }
            }
        }

        if (listeners != null) {
            for (RecordListener listener  : listeners) {
                switch (type) {
                    case LOAD:    listener.loadStart(ctx);    break;
                    case REFRESH: listener.refreshStart(ctx); break;
                    case STORE:   listener.storeStart(ctx);   break;
                    case INSERT:  listener.insertStart(ctx);  break;
                    case UPDATE:  listener.updateStart(ctx);  break;
                    case DELETE:  listener.deleteStart(ctx);  break;
                    default:
                        throw new IllegalStateException("Type not supported: " + type);
                }
            }
        }

        if (operation != null) {
            operation.operate(record);
        }

        if (listeners != null) {
            for (RecordListener listener  : listeners) {
                switch (type) {
                    case LOAD:    listener.loadEnd(ctx);    break;
                    case REFRESH: listener.refreshEnd(ctx); break;
                    case STORE:   listener.storeEnd(ctx);   break;
                    case INSERT:  listener.insertEnd(ctx);  break;
                    case UPDATE:  listener.updateEnd(ctx);  break;
                    case DELETE:  listener.deleteEnd(ctx);  break;
                    default:
                        throw new IllegalStateException("Type not supported: " + type);
                }
            }
        }

        return record;
    }

    private final ExecuteType executeType() {
        return type == LOAD || type == REFRESH ? READ : WRITE;
    }

    enum RecordLifecycleType {
        LOAD,
        REFRESH,
        STORE,
        INSERT,
        UPDATE,
        DELETE
    }
}
