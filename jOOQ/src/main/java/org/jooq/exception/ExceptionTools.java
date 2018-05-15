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
package org.jooq.exception;

/**
 * @author Lukas Eder
 */
public final class ExceptionTools {

    /**
     * Never run infinite loops
     */
    private static int maxCauseLookups = 256;

    /**
     * Find a root cause of a given type, or <code>null</code> if no root cause
     * of that type was found.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T getCause(Throwable t, Class<? extends T> type) {
        Throwable next = t.getCause();
        Throwable prev;

        for (int i = 0; i < maxCauseLookups; i++) {
            if (next == null)
                return null;

            if (type.isInstance(next))
                return (T) next;

            prev = next;
            next = next.getCause();

            // Don't trust exceptions to respect the default behaviour of Throwable.getCause()
            if (prev == next)
                return null;
        }

        return null;
    }

    private ExceptionTools() {}
}
