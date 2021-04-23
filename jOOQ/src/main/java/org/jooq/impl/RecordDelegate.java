/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.ExecuteType.READ;
import static org.jooq.ExecuteType.WRITE;
import static org.jooq.conf.InvocationOrder.REVERSE;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.LOAD;
import static org.jooq.impl.RecordDelegate.RecordLifecycleType.REFRESH;
import static org.jooq.impl.Tools.attachRecords;
import static org.jooq.impl.Tools.map;

import java.util.Arrays;
import java.util.function.Supplier;

import org.jooq.Configuration;
import org.jooq.ExecuteType;
import org.jooq.Record;
import org.jooq.RecordListener;
import org.jooq.RecordListenerProvider;
import org.jooq.exception.ControlFlowSignal;

/**
 * A stub for {@link Record} objects, abstracting {@link RecordListener}
 * lifecycle handling.
 *
 * @author Lukas Eder
 */
final class RecordDelegate<R extends Record> {

    private final Configuration       configuration;
    private final Supplier<R>         recordSupplier;
    private final Boolean             fetched;
    private final RecordLifecycleType type;

    RecordDelegate(Configuration configuration, Supplier<R> recordSupplier, Boolean fetched) {
        this(configuration, recordSupplier, fetched, LOAD);
    }

    RecordDelegate(Configuration configuration, Supplier<R> recordSupplier, Boolean fetched, RecordLifecycleType type) {
        this.configuration = configuration;
        this.recordSupplier = recordSupplier;
        this.fetched = fetched;
        this.type = type;
    }

    static final <R extends Record> RecordDelegate<R> delegate(
        Configuration configuration,
        R record,
        RecordLifecycleType type
    ) {
        return new RecordDelegate<>(
            configuration,
            () -> record,
            null,
            type
        );
    }

    @SuppressWarnings("unchecked")
    final <E extends Exception> R operate(ThrowingFunction<R, R, E> operation) throws E {
        R record = recordSupplier.get();

        // [#3300] Records that were fetched from the database
        if (fetched != null && record instanceof AbstractRecord)
            ((AbstractRecord) record).fetched = fetched;

        RecordListenerProvider[] providers = null;
        RecordListener[] listeners = null;
        DefaultRecordContext ctx = null;
        E exception = null;

        if (configuration != null) {
            providers = configuration.recordListenerProviders();

            if (providers != null && providers.length > 0) {
                listeners = map(providers, p -> p.provide(), RecordListener[]::new);
                ctx = new DefaultRecordContext(configuration, executeType(), record);
            }
        }

        if (listeners != null) {
            for (RecordListener listener : (
                ctx == null || ctx.settings().getRecordListenerStartInvocationOrder() != REVERSE
                ? Arrays.asList(listeners)
                : Tools.reverseIterable(listeners)
            )) {
                switch (type) {
                    case LOAD:    listener.loadStart(ctx);    break;
                    case REFRESH: listener.refreshStart(ctx); break;
                    case STORE:   listener.storeStart(ctx);   break;
                    case INSERT:  listener.insertStart(ctx);  break;
                    case UPDATE:  listener.updateStart(ctx);  break;
                    case MERGE:   listener.mergeStart(ctx);   break;
                    case DELETE:  listener.deleteStart(ctx);  break;
                    default:
                        throw new IllegalStateException("Type not supported: " + type);
                }
            }
        }

        // [#1684] Do not attach configuration if settings say no
        if (attachRecords(configuration))
            record.attach(configuration);

        if (operation != null) {
            try {
                operation.apply(record);
            }

            // [#2770][#3036] Exceptions must not propagate before listeners receive "end" events
            catch (Exception e) {
                exception = (E) e;

                // Do not propagate these exception types to client code as they're not really "exceptions"
                if (!(e instanceof ControlFlowSignal)) {
                    if (ctx != null)
                        ctx.exception = e;

                    if (listeners != null)
                        for (RecordListener listener : listeners)
                            listener.exception(ctx);
                }
            }
        }

        if (listeners != null) {
            for (RecordListener listener : (
                ctx == null || ctx.settings().getRecordListenerEndInvocationOrder() != REVERSE
                ? Arrays.asList(listeners)
                : Tools.reverseIterable(listeners)
            )) {
                switch (type) {
                    case LOAD:    listener.loadEnd(ctx);    break;
                    case REFRESH: listener.refreshEnd(ctx); break;
                    case STORE:   listener.storeEnd(ctx);   break;
                    case INSERT:  listener.insertEnd(ctx);  break;
                    case UPDATE:  listener.updateEnd(ctx);  break;
                    case MERGE:   listener.mergeEnd(ctx);   break;
                    case DELETE:  listener.deleteEnd(ctx);  break;
                    default:
                        throw new IllegalStateException("Type not supported: " + type);
                }
            }
        }

        if (exception != null)
            throw exception;

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
        MERGE,
        DELETE
    }
}
