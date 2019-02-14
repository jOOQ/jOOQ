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

import static java.util.Arrays.asList;
import static org.checkerframework.javacutil.TreeUtils.elementFromDeclaration;
import static org.checkerframework.javacutil.TreeUtils.elementFromUse;
import static org.checkerframework.javacutil.TreeUtils.enclosingClass;
import static org.checkerframework.javacutil.TreeUtils.enclosingMethod;

import java.io.PrintWriter;
import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import org.jooq.Allow;
import org.jooq.PlainSQL;
import org.jooq.Require;
import org.jooq.SQLDialect;
import org.jooq.Support;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;

/**
 * The actual implementation of the checkers / matchers.
 *
 * @author Lukas Eder
 */
final class Tools {
    static final <T> T checkSQLDialect(
        MethodInvocationTree node,
        Supplier<Element> enclosingSupplier,
        Function<? super String, ? extends T> error,
        Function<? super Printer, ? extends T> print
    ) {
        try {
            ExecutableElement elementFromUse = elementFromUse(node);
            Support support = elementFromUse.getAnnotation(Support.class);

            // In the absence of a @Support annotation, all jOOQ API method calls will type check.
            if (support != null) {
                Element enclosing = enclosingSupplier.get();

                // [#7929] "Empty" @Support annotations expand to all SQLDialects
                EnumSet<SQLDialect> supported = EnumSet.copyOf(
                    support.value().length > 0
                  ? asList(support.value())
                  : asList(SQLDialect.values())
                );

                EnumSet<SQLDialect> allowed = EnumSet.noneOf(SQLDialect.class);
                EnumSet<SQLDialect> required = EnumSet.noneOf(SQLDialect.class);

                boolean evaluateRequire = true;
                while (enclosing != null) {
                    Allow allow = enclosing.getAnnotation(Allow.class);

                    if (allow != null)
                        allowed.addAll(asList(allow.value()));

                    if (evaluateRequire) {
                        Require require = enclosing.getAnnotation(Require.class);

                        if (require != null) {
                            evaluateRequire = false;

                            required.clear();
                            required.addAll(asList(require.value()));
                        }
                    }

                    enclosing = enclosing.getEnclosingElement();
                }

                if (allowed.isEmpty())
                    return error.apply("No jOOQ API usage is allowed at current scope. Use @Allow.");

                boolean allowedFail = true;
                allowedLoop:
                for (SQLDialect a : allowed) {
                    for (SQLDialect s : supported) {
                        if (a.supports(s)) {
                            allowedFail = false;
                            break allowedLoop;
                        }
                    }
                }

                if (allowedFail)
                    return error.apply("The allowed dialects in scope " + allowed + " do not include any of the supported dialects: " + supported);

                boolean requiredFail = false;
                requiredLoop:
                for (SQLDialect r : required) {
                    for (SQLDialect s : supported)
                        if (r.supports(s))
                            continue requiredLoop;

                    requiredFail = true;
                    break requiredLoop;
                }

                if (requiredFail)
                    return error.apply("Not all of the required dialects " + required + " from the current scope are supported " + supported);
            }
        }
        catch (final Exception e) {
            return print.apply(new Printer() {
                @Override
                public void print(PrintWriter t) {
                    e.printStackTrace(t);
                }
            });
        }

        return null;
    }

    static final <T> T checkPlainSQL(
        MethodInvocationTree node,
        Supplier<Element> enclosingSupplier,
        Function<? super String, ? extends T> error,
        Function<? super Printer, ? extends T> print
    ) {
        try {
            ExecutableElement elementFromUse = elementFromUse(node);
            PlainSQL plainSQL = elementFromUse.getAnnotation(PlainSQL.class);

            // In the absence of a @PlainSQL annotation,
            // all jOOQ API method calls will type check.
            if (plainSQL != null) {
                boolean allowed = false;
                Element enclosing = enclosingSupplier.get();

                moveUpEnclosingLoop:
                while (enclosing != null) {
                    if (enclosing.getAnnotation(Allow.PlainSQL.class) != null) {
                        allowed = true;
                        break moveUpEnclosingLoop;
                    }

                    enclosing = enclosing.getEnclosingElement();
                }

                if (!allowed)
                    return error.apply("Plain SQL usage not allowed at current scope. Use @Allow.PlainSQL.");
            }
        }
        catch (final Exception e) {
            return print.apply(new Printer() {
                @Override
                public void print(PrintWriter t) {
                    e.printStackTrace(t);
                }
            });
        }

        return null;
    }

    static Element enclosing(TreePath path) {
        MethodTree enclosingMethod = enclosingMethod(path);

        if (enclosingMethod != null)
            return elementFromDeclaration(enclosingMethod);

        ClassTree enclosingClass = enclosingClass(path);
        return elementFromDeclaration(enclosingClass);
    }

    interface Printer {
        void print(PrintWriter writer);
    }
}
