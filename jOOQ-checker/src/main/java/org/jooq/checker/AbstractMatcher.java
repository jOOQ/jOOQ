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
package org.jooq.checker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jooq.checker.Tools.Printer;

import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.MethodInvocationTreeMatcher;
import com.google.errorprone.matchers.Description;
import com.sun.source.tree.Tree;

/**
 * Common base class for matchers.
 *
 * @author Lukas Eder
 */
abstract class AbstractMatcher extends BugChecker implements MethodInvocationTreeMatcher {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -3955673252940475227L;

    Description error(Tree node, String message) {
        return buildDescription(node).setMessage(message).build();
    }

    static Description print(Printer printer) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("error.txt"))){
            writer.println("This is probably a bug in jOOQ-checker.");
            writer.println("If you think this is a bug in jOOQ, please report it here: https://github.com/jOOQ/jOOQ/issues/new");
            writer.println("---------------------------------------------------------------------");

            printer.print(writer);
        }
        catch (IOException ignore) {}

        return null;
    }
}
