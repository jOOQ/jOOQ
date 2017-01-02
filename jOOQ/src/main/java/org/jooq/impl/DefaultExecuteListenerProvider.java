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
 */
package org.jooq.impl;

import java.io.Serializable;

import org.jooq.ExecuteListener;
import org.jooq.ExecuteListenerProvider;

/**
 * A default implementation for {@link ExecuteListenerProvider}.
 * <p>
 * This implementation just wraps an instance of {@link ExecuteListener}, always
 * providing the same.
 *
 * @author Lukas Eder
 */
public class DefaultExecuteListenerProvider implements ExecuteListenerProvider, Serializable {

    /**
     * Generated UID.
     */
    private static final long     serialVersionUID = -2122007794302549679L;

    /**
     * The delegate listener.
     */
    private final ExecuteListener listener;

    /**
     * Convenience method to construct an array of
     * <code>DefaultExecuteListenerProvider</code> from an array of
     * <code>ExecuteListener</code> instances.
     */
    public static ExecuteListenerProvider[] providers(ExecuteListener... listeners) {
        ExecuteListenerProvider[] result = new ExecuteListenerProvider[listeners.length];

        for (int i = 0; i < listeners.length; i++)
            result[i] = new DefaultExecuteListenerProvider(listeners[i]);

        return result;
    }

    /**
     * Create a new provider instance from an argument listener.
     *
     * @param listener The argument listener.
     */
    public DefaultExecuteListenerProvider(ExecuteListener listener) {
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ExecuteListener provide() {
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
