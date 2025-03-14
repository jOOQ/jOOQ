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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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
import static org.jooq.impl.Tools.EMPTY_NAME;
import static org.jooq.impl.Tools.EMPTY_STRING;
import static org.jooq.impl.Tools.stringLiteral;
import static org.jooq.tools.StringUtils.defaultIfNull;

import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Scope;
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
















        boolean previous = ctx.quote();

        ctx.quote(quoted(ctx));
        ctx.literal(defaultIfNull(name, ""));
        ctx.quote(previous);
    }

    final boolean quoted(Scope ctx) {
        return quoted(SettingsTools.getRenderQuotedNames(ctx.settings()), quoted);
    }

    static final boolean quoted(RenderQuotedNames q, Quoted quoted) {
        return quoted != SYSTEM && (
              q == RenderQuotedNames.ALWAYS
           || q == RenderQuotedNames.EXPLICIT_DEFAULT_QUOTED && (quoted == DEFAULT || quoted == QUOTED)
           || q == RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED && quoted == QUOTED
        );
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
        return empty() ? EMPTY_STRING : new String[] { name };
    }

    @Override
    public final Name[] parts() {
        return empty() ? EMPTY_NAME : new Name[] { this };
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {

        // [#13499] Since QualifiedName and UnqualifiedName can be equal, both
        //          need the same hashCode() computation
        return 31 * 1 + StringUtils.defaultIfNull(name, "").hashCode();
    }
}
