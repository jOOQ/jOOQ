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
package org.jooq.checker;

import static com.google.errorprone.BugPattern.LinkType.NONE;
import static com.google.errorprone.BugPattern.SeverityLevel.ERROR;

import org.jooq.PlainSQL;

import com.google.auto.service.AutoService;
import com.google.errorprone.BugPattern;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.matchers.Description;
import com.sun.source.tree.MethodInvocationTree;

/**
 * A checker to disallow usage of {@link PlainSQL} API, except where allowed
 * explicitly.
 *
 * @author Lukas Eder
 */
@AutoService(BugChecker.class)
@BugPattern(
    name = "SQLDialectMatcher",
    summary = "jOOQ API was used which is not supported by your SQLDialect",
    severity = ERROR,
    linkType = NONE
)
public class SQLDialectMatcher extends AbstractMatcher {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -690695693050288771L;

    @Override
    public Description matchMethodInvocation(MethodInvocationTree node, VisitorState state) {
        return nullSafe(Tools.checkSQLDialect(
            node,
            () -> Tools.enclosing(state.getPath()),
            message -> error(node, message),
            printer -> print(printer)
        ));
    }
}
