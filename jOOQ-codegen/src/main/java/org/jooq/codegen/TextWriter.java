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
package org.jooq.codegen;

import java.io.File;
import java.io.PrintWriter;

/**
 * A wrapper for a {@link PrintWriter}
 * <p>
 * This wrapper adds text printing features to the general
 * {@link GeneratorWriter}
 *
 * @author Lukas Eder
 */
public class TextWriter extends GeneratorWriter<TextWriter> {

    protected TextWriter(File file) {
        this(file, null);
    }

    protected TextWriter(File file, String encoding) {
        super(file, encoding);
    }
}
