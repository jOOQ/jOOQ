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
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.POSTGRES;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.WindowDefinition;
import org.jooq.WindowSpecification;

/**
 * @author Lukas Eder
 */
final class WindowDefinitionImpl extends AbstractQueryPart implements WindowDefinition {

    /**
     * Generated UID
     */
    private static final long         serialVersionUID = -7779419148766154430L;

    private final Name                name;
    private final WindowSpecification window;

    WindowDefinitionImpl(Name name, WindowSpecification window) {
        this.name = name;
        this.window = window;
    }

    final Name getName() {
        return name;
    }

    @Override
    public final void accept(Context<?> ctx) {

        // In the WINDOW clause, always declare window definitions
        if (ctx.declareWindows()) {
            ctx.visit(name)
               .sql(' ')
               .keyword("as")
               .sql(" (")
               .visit(window)
               .sql(')');
        }

        // Outside the WINDOW clause, only few dialects actually support
        // referencing WINDOW definitions
        else if (asList(ctx.family()).contains(POSTGRES)) {
            ctx.visit(name);
        }

        // When emulating, just repeat the window specification
        else {
            ctx.visit(window);
        }
    }

    @Override
    public final boolean declaresWindows() {
        return true;
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
