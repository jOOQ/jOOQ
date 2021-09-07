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
package org.jooq;

import static org.jooq.Constants.MINOR_VERSION;

import org.jooq.tools.JooqLogger;

/**
 * Common utilities for {@link ParserCLI}, {@link DiffCLI} and others.
 *
 * @author Lukas Eder
 */
final class CLIUtil {

    static void main(String url, Runnable runnable) {
        JooqLogger.initSimpleFormatter();

        try {
            runnable.run();
        }
        catch (NoClassDefFoundError e) {
            throw new RuntimeException(
                ("" +
                "A class definition could not be found when running the CLI utility.\n" +
                "\n" +
                "This is mostly due to a missing dependency. Make sure you have added the right dependencies\n" +
                "as according to the manual for {url}\n" +
                "").replace("{url}", url.replace("/latest/", "/" + MINOR_VERSION + "/")),
                e
            );
        }
    }
}
