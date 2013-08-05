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

import org.jooq.Configuration;
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
class RecordStub<R extends Record> {

    private final Configuration configuration;
    private final R             record;

    RecordStub(Configuration configuration, R record) {
        this.configuration = configuration;
        this.record = record;
    }

    final <E extends Exception> R initialise(RecordInitialiser<R, E> initialiser) throws E {
        RecordListenerProvider[] providers = null;
        RecordListener[] listeners = null;
        RecordContext ctx = null;

        if (configuration != null) {
            providers = configuration.recordListenerProviders();

            if (providers != null) {
                listeners = new RecordListener[providers.length];
                ctx = new DefaultRecordContext(configuration, READ, record);

                for (int i = 0; i < providers.length; i++) {
                    listeners[i] = providers[i].provide();
                }
            }
        }

        if (listeners != null) {
            for (RecordListener listener  : listeners) {
                listener.loadStart(ctx);
            }
        }

        if (initialiser != null) {
            initialiser.initialise(record);
        }

        if (listeners != null) {
            for (RecordListener listener  : listeners) {
                listener.loadEnd(ctx);
            }
        }

        return record;
    }
}
