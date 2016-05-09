/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static com.sun.source.util.TreePath.getPath;
import static java.util.Arrays.asList;
import static org.checkerframework.javacutil.TreeUtils.elementFromDeclaration;
import static org.checkerframework.javacutil.TreeUtils.elementFromUse;
import static org.checkerframework.javacutil.TreeUtils.enclosingMethod;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumSet;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import org.jooq.Allow;
import org.jooq.Require;
import org.jooq.SQLDialect;
import org.jooq.Support;

import org.checkerframework.framework.source.Result;
import org.checkerframework.framework.source.SourceChecker;
import org.checkerframework.framework.source.SourceVisitor;

import com.sun.source.tree.MethodInvocationTree;

/**
 * A checker to compare {@link SQLDialect} from a use-site {@link Require}
 * annotation with a declaration-site {@link Support} annotation.
 *
 * @author Lukas Eder
 */
public class SQLDialectChecker extends SourceChecker {

    @Override
    protected SourceVisitor<?, ?> createSourceVisitor() {
        return new SourceVisitor<Void, Void>(this) {

            @Override
            public Void visitMethodInvocation(MethodInvocationTree node, Void p) {
                try {
                    ExecutableElement elementFromUse = elementFromUse(node);
                    Support support = elementFromUse.getAnnotation(Support.class);

                    // In the absence of a @Support annotation, or if no SQLDialect is supplied,
                    // all jOOQ API method calls will type check.
                    if (support != null && support.value().length > 0) {
                        Element enclosing = elementFromDeclaration(enclosingMethod(getPath(root, node)));

                        EnumSet<SQLDialect> supported = EnumSet.copyOf(asList(support.value()));
                        EnumSet<SQLDialect> allowed = EnumSet.noneOf(SQLDialect.class);
                        EnumSet<SQLDialect> required = EnumSet.allOf(SQLDialect.class);
                        EnumSet<SQLDialect> x;

                        while (enclosing != null) {
                            Allow allow = enclosing.getAnnotation(Allow.class);
                            Require require = enclosing.getAnnotation(Require.class);

                            if (allow != null)
                                allowed.addAll(asList(allow.value()));

                            if (require != null)
                                required.retainAll(asList(require.value()));

                            enclosing = enclosing.getEnclosingElement();
                        }

                        if (allowed.isEmpty())
                            error(node, "No jOOQ API usage is allowed at current scope. Use @Allow.");

                        if (required.isEmpty())
                            error(node, "No jOOQ API usage is allowed at current scope due to conflicting @Require specification.");

                        x = EnumSet.copyOf(allowed);
                        x.retainAll(supported);
                        if (x.isEmpty())
                            error(node, "None of the supported dialects (" + supported + ") are allowed in the current scope (" + allowed + ")");

                        if (!supported.containsAll(required))
                            error(node, "Not all of the required dialects (" + required + ") from the current scope are supported (" + supported + ")");
                    }
                }
                catch (final Exception e) {
                    print(new Printer() {
                        @Override
                        public void print(PrintWriter t) {
                            e.printStackTrace(t);
                        }
                    });
                }

                return super.visitMethodInvocation(node, p);
            }
        };
    }

    void error(Object node, String message) {
        getChecker().report(Result.failure(message, node), node);
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
