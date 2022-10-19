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
package org.jooq.impl;

import static org.jooq.Name.Quoted.DEFAULT;
import static org.jooq.Name.Quoted.QUOTED;
import static org.jooq.Name.Quoted.SYSTEM;
import static org.jooq.Name.Quoted.UNQUOTED;
// ...
import static org.jooq.impl.Tools.stringLiteral;

import java.util.Arrays;

import org.jooq.Context;
import org.jooq.Name;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.SettingsTools;
import org.jooq.tools.StringUtils;

/**
 * The default implementation for an unqualified SQL identifier.
 *
 * @author Lukas Eder
 */
final class UnqualifiedName extends AbstractName {

    final String name;
    final Quoted quoted;

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
        boolean current = quoted != SYSTEM && (
             q == RenderQuotedNames.ALWAYS
          || q == RenderQuotedNames.EXPLICIT_DEFAULT_QUOTED && (quoted == DEFAULT || quoted == QUOTED)
          || q == RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED && quoted == QUOTED
        );

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
    public final boolean empty() {
        return StringUtils.isEmpty(name);
    }

    @Override
    public final boolean qualified() {
        return false;
    }

    @Override
    public final boolean qualifierQualified() {
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

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {

        // [#13499] Since QualifiedName and UnqualifiedName can be equal, both
        //          need the same hashCode() computation
        if (name == null)
            return 0;
        else
            return 31 * 1 + name.hashCode();
    }
}
