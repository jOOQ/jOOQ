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

import org.jooq.Configuration;
import org.jooq.DiagnosticsContext;
import org.jooq.DiagnosticsListener;
import org.jooq.DiagnosticsListenerProvider;

/**
 * @author Lukas Eder
 */
final class DiagnosticsListeners implements DiagnosticsListener {

    final DiagnosticsListener[]     listeners;

    DiagnosticsListeners(DiagnosticsListenerProvider[] providers) {
        listeners = new DiagnosticsListener[providers.length];

        for (int i = 0; i < listeners.length; i++)
            listeners[i] = providers[i].provide();

    }

    static final DiagnosticsListeners get(Configuration configuration) {
        return new DiagnosticsListeners(configuration.diagnosticsListenerProviders());
    }

    @Override
    public final void resultSetTooLarge(DiagnosticsContext ctx) {
        for (DiagnosticsListener listener : listeners)
            listener.resultSetTooLarge(ctx);
    }
}
