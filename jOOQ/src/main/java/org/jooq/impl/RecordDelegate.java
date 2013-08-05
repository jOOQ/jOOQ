/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
