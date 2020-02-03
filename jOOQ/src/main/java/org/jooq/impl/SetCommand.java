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
package org.jooq.impl;

import static org.jooq.impl.Keywords.K_SET;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Param;

/**
 * A <code>SET</code> command.
 *
 * @author Lukas Eder
 */
final class SetCommand extends AbstractRowCountQuery {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6018875346107141474L;

    private final Name        name;
    private final Param<?>    value;

    SetCommand(Configuration configuration, Name name, Param<?> value) {
        super(configuration);

        this.name = name;
        this.value = value;
    }

    final Name     $name()  { return name; }
    final Param<?> $value() { return value; }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(K_SET).sql(' ').visit(name).sql(" = ").visit(value);
    }
}
