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
import static org.jooq.Name.Quoted.MIXED;
// ...
import static org.jooq.impl.Tools.allMatch;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.stringLiteral;

import org.jooq.Context;
import org.jooq.Name;
import org.jooq.tools.StringUtils;

/**
 * The default implementation for a qualified SQL identifier.
 *
 * @author Lukas Eder
 */
final class QualifiedName extends AbstractName {

    private final UnqualifiedName[] qualifiedName;

    QualifiedName(String[] qualifiedName) {
        this(qualifiedName, DEFAULT);
    }

    QualifiedName(String[] qualifiedName, Quoted quoted) {
        this(names(qualifiedName, quoted));
    }

    QualifiedName(Name[] qualifiedName) {
        this(last(nonEmpty(qualifiedName)));
    }

    private QualifiedName(UnqualifiedName[] qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    private static final UnqualifiedName[] names(String[] qualifiedName, Quoted quoted) {
        return map(nonEmpty(qualifiedName), s -> new UnqualifiedName(s, quoted), UnqualifiedName[]::new);
    }

    private static final UnqualifiedName[] last(Name[] qualifiedName) {
        if (qualifiedName instanceof UnqualifiedName[] u)
            return u;

        UnqualifiedName[] result = new UnqualifiedName[qualifiedName.length];

        for (int i = 0; i < qualifiedName.length; i++)
            if (qualifiedName[i] instanceof QualifiedName q) {
                result[i] = q.qualifiedName[q.qualifiedName.length - 1];
            }
            else if (qualifiedName[i] instanceof UnqualifiedName u)
                result[i] = u;
            else
                result[i] = new UnqualifiedName(qualifiedName[i].last());

        return result;
    }

    private static final String[] nonEmpty(String[] qualifiedName) {
        String[] result;
        int nulls = 0;

        for (String name : qualifiedName)
            if (StringUtils.isEmpty(name))
                nulls++;

        if (nulls > 0) {
            result = new String[qualifiedName.length - nulls];

            for (int i = qualifiedName.length - 1; i >= 0; i--)
                if (StringUtils.isEmpty(qualifiedName[i]))
                    nulls--;
                else
                    result[i - nulls] = qualifiedName[i];
        }
        else {
            result = qualifiedName;
        }

        return result;
    }

    private static final Name[] nonEmpty(Name[] names) {
        Name[] result;
        int nulls = 0;

        for (Name name : names)
            if (name == null || name.equals(NO_NAME))
                nulls++;

        if (nulls > 0) {
            result = new Name[names.length - nulls];

            for (int i = names.length - 1; i >= 0; i--)
                if (names[i] == null || names[i].equals(NO_NAME))
                    nulls--;
                else
                    result[i - nulls] = names[i];
        }
        else {
            result = names;
        }

        return result;
    }

    @Override
    public final void accept(Context<?> ctx) {















        // [#3437] Fully qualify this field only if allowed in the current context
        if (ctx.qualify()) {
            String separator = "";

            for (UnqualifiedName name : qualifiedName) {
                ctx.sql(separator).visit(name);
                separator = ".";
            }
        }
        else {
            ctx.visit(qualifiedName[qualifiedName.length - 1]);
        }
    }

    @Override
    public final String first() {
        return qualifiedName.length > 0 ? qualifiedName[0].last() : null;
    }

    @Override
    public final String last() {
        return qualifiedName.length > 0 ? qualifiedName[qualifiedName.length - 1].last() : null;
    }

    @Override
    public final boolean empty() {
        return allMatch(qualifiedName, n -> n.empty());
    }

    @Override
    public final boolean qualified() {
        return qualifiedName.length > 1;
    }

    @Override
    public final boolean qualifierQualified() {
        return qualifiedName.length > 2;
    }

    @Override
    public final Name qualifier() {
        if (qualifiedName.length <= 1)
            return null;
        if (qualifiedName.length == 2)
            return qualifiedName[0];

        UnqualifiedName[] qualifier = new UnqualifiedName[qualifiedName.length - 1];
        System.arraycopy(qualifiedName, 0, qualifier, 0, qualifier.length);
        return new QualifiedName(qualifier);
    }

    @Override
    public final Name unqualifiedName() {
        if (qualifiedName.length == 0)
            return this;
        else
            return qualifiedName[qualifiedName.length - 1];
    }

    @Override
    public final Quoted quoted() {
        Quoted result = null;

        for (UnqualifiedName name : qualifiedName)
            if (result == null)
                result = name.quoted();
            else if (result != name.quoted())
                return MIXED;

        return result == null ? DEFAULT : result;
    }

    @Override
    public final Name quotedName() {
        return new QualifiedName(map(qualifiedName, n -> n.quotedName(), Name[]::new));
    }

    @Override
    public final Name unquotedName() {
        return new QualifiedName(map(qualifiedName, n -> n.unquotedName(), Name[]::new));
    }

    @Override
    public final String[] getName() {
        return map(qualifiedName, n -> n.last(), String[]::new);
    }

    @Override
    public final Name[] parts() {
        return qualifiedName.clone();
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    // The assumption is that race conditions for the assignment are
    // acceptable because the computation is idempotent, so memory
    // barriers or synchronization aren't necessary.
    private transient Integer hash;

    @Override
    public int hashCode() {
        if (hash == null) {
            int h = 1;

            for (int i = 0; i < qualifiedName.length; i++) {
                UnqualifiedName n = qualifiedName[i];

                if (n.name == null)
                    h = 31 * h + 0;
                else
                    h = 31 * h + n.name.hashCode();
            }

            hash = h;
        }

        return hash;
    }
}
