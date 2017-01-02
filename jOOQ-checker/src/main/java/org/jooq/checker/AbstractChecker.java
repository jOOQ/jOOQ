/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.checker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.checkerframework.framework.source.Result;
import org.checkerframework.framework.source.SourceChecker;

/**
 * Common base class for checkers.
 *
 * @author Lukas Eder
 */
abstract class AbstractChecker extends SourceChecker {

    void error(Object node, String message) {
        getChecker().report(Result.failure(message, node), node);
    }

    void warn(Object node, String message) {
        getChecker().report(Result.warning(message, node), node);
    }

    void print(Printer printer) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("error.txt"))){
            writer.println("This is probably a bug in jOOQ-checker.");
            writer.println("Please report this bug here: https://github.com/jOOQ/jOOQ/issues/new");
            writer.println("---------------------------------------------------------------------");

            printer.print(writer);
        }
        catch (IOException ignore) {}
    }

    interface Printer {
        void print(PrintWriter writer);
    }
}
