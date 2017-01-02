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
