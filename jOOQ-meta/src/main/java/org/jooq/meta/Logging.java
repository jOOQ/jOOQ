/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
package org.jooq.meta;

import java.util.function.Supplier;

import org.jooq.meta.jaxb.OnError;
import org.jooq.tools.JooqLogger;

/**
 * A utility that helps executing code or logging messages in the context of a
 * specific error handling strategy, such as {@link Database#onError()}.
 *
 * @author Lukas Eder
 */
public final class Logging {

    private static final JooqLogger log = JooqLogger.getLogger(Logging.class);

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Throwable;
    }

    public static final void run(OnError onError, ThrowingRunnable runnable) {
        run(onError, runnable, () -> "Error in code generator");
    }

    public static final void run(OnError onError, ThrowingRunnable runnable, Supplier<String> message) {
        try {
            runnable.run();
        }
        catch (Throwable e) {
            if (onError == null)
                onError = OnError.FAIL;

            switch (onError) {
                case SILENT:
                    break;

                case LOG:
                    log.warn(message.get(), e);
                    break;

                case FAIL:
                    if (e instanceof RuntimeException r)
                        throw r;
                    else
                        throw new GeneratorException(message.get(), e);
            }
        }
    }

    public static final void log(OnError onError, Supplier<String> message) {
        log(onError, message, null);
    }

    public static final void log(OnError onError, Supplier<String> message, Throwable e) {
        if (onError == null)
            onError = OnError.FAIL;

        switch (onError) {
            case SILENT:
                break;

            case LOG:
                if (e != null)
                    log.warn(message.get(), e);
                else
                    log.warn(message.get());

                break;

            case FAIL:
                if (e instanceof RuntimeException r)
                    throw r;
                else if (e != null)
                    throw new GeneratorException(message.get(), e);
                else
                    throw new GeneratorException(message.get());
        }
    }
}
