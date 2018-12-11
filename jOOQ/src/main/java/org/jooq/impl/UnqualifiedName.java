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

import static org.jooq.Name.Quoted.DEFAULT;
import static org.jooq.Name.Quoted.QUOTED;
import static org.jooq.Name.Quoted.UNQUOTED;

import org.jooq.Context;
import org.jooq.Name;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.SettingsTools;

/**
 * The default implementation for an unqualified SQL identifier.
 *
 * @author Lukas Eder
 */
final class UnqualifiedName extends AbstractName {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 8562325639223483938L;

    private final String      name;
    private final Quoted     quoted;

    UnqualifiedName(String name) {
        this(name, DEFAULT);
    }

    UnqualifiedName(String name, Quoted quoted) {
        this.name = name;
        this.quoted = quoted;
    }

    @Override
    public final void accept(Context<?> ctx) {
        RenderQuotedNames q = SettingsTools.getRenderQuotedNames(ctx.settings());

        boolean previous = ctx.quote();
        boolean current =
             q == RenderQuotedNames.ALWAYS
          || q == RenderQuotedNames.EXPLICIT_DEFAULT_QUOTED && (quoted == DEFAULT || quoted == QUOTED)
          || q == RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED && quoted == QUOTED;

        ctx.quote(current);
        ctx.literal(name);
        ctx.quote(previous);
    }

    @Override
    public final String first() {
        return name;
    }

    @Override
    public final String last() {
        return name;
    }

    @Override
    public final boolean qualified() {
        return false;
    }

    @Override
    public final Name qualifier() {
        return null;
    }

    @Override
    public final Name unqualifiedName() {
        return this;
    }

    @Override
    public final Quoted quoted() {
        return quoted;
    }

    @Override
    public final Name quotedName() {
        return new UnqualifiedName(name, QUOTED);
    }

    @Override
    public final Name unquotedName() {
        return new UnqualifiedName(name, UNQUOTED);
    }

    @Override
    public final String[] getName() {
        return new String[] { name };
    }

    @Override
    public final Name[] parts() {
        return new Name[] { this };
    }
}
