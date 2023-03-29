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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import static com.sun.source.util.TreePath.getPath;

import org.jooq.Require;
import org.jooq.SQLDialect;
import org.jooq.Support;

import org.checkerframework.framework.source.SourceVisitor;

import com.sun.source.tree.MethodInvocationTree;

/**
 * A checker to compare {@link SQLDialect} from a use-site {@link Require}
 * annotation with a declaration-site {@link Support} annotation.
 *
 * @author Lukas Eder
 */
public class SQLDialectChecker extends AbstractChecker {

    @Override
    protected SourceVisitor<Void, Void> createSourceVisitor() {
        return new SourceVisitor<Void, Void>(getSourceChecker()) {

            @Override
            public Void visitMethodInvocation(MethodInvocationTree node, Void p) {
                Tools.checkSQLDialect(
                    node,
                    () -> Tools.enclosing(getPath(root, node)),
                    message -> error(node, message),
                    printer -> print(printer)
                );

                return super.visitMethodInvocation(node, p);
            }
        };
    }
}
