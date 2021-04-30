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

import static org.jooq.impl.Tools.map;

import java.io.Serializable;

import org.jooq.DiagnosticsListener;
import org.jooq.DiagnosticsListenerProvider;

/**
 * A default implementation for {@link DiagnosticsListenerProvider}.
 * <p>
 * This implementation just wraps an instance of {@link DiagnosticsListener},
 * always providing the same.
 *
 * @author Lukas Eder
 */
public class DefaultDiagnosticsListenerProvider implements DiagnosticsListenerProvider, Serializable {

    /**
     * The delegate listener.
     */
    private final DiagnosticsListener listener;

    /**
     * Convenience method to construct an array of
     * <code>DefaultDiagnosticsListenerProvider</code> from an array of
     * <code>DiagnosticsListener</code> instances.
     */
    public static DiagnosticsListenerProvider[] providers(DiagnosticsListener... listeners) {
        return map(listeners, DefaultDiagnosticsListenerProvider::new, DiagnosticsListenerProvider[]::new);
    }

    /**
     * Create a new provider instance from an argument listener.
     *
     * @param listener The argument listener.
     */
    public DefaultDiagnosticsListenerProvider(DiagnosticsListener listener) {
        this.listener = listener;
    }

    @Override
    public final DiagnosticsListener provide() {
        return listener;
    }

    @Override
    public String toString() {
        return listener.toString();
    }
}
