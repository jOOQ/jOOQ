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

import static org.jooq.Name.Quoted.MIXED;
// ...
import static org.jooq.impl.Tools.EMPTY_NAME;
import static org.jooq.impl.Tools.EMPTY_STRING;
import static org.jooq.impl.Tools.stringLiteral;

import org.jooq.Context;
import org.jooq.Name;
// ...

/**
 * The default implementation for a qualified SQL identifier.
 *
 * @author Lukas Eder
 */
final class QualifiedName extends AbstractName {

    final Name            qualifier;
    final UnqualifiedName last;

    QualifiedName(Name qualifier, UnqualifiedName last) {
        this.qualifier = qualifier;
        this.last = last;
    }















    @Override
    public final void accept(Context<?> ctx) {














        // [#3437] Fully qualify this field only if allowed in the current context
        if (ctx.qualify() && qualifier != null)
            ctx.visit(qualifier).sql('.').visit(last);
        else
            ctx.visit(last);
    }

    @Override
    public final String first() {
        return qualifier != null ? qualifier.first() : last.name;
    }

    @Override
    public final String last() {
        return last.name;
    }

    @Override
    public final boolean empty() {
        return last.empty();
    }

    @Override
    public final boolean qualified() {
        return qualifier != null;
    }

    @Override
    public final boolean qualifierQualified() {
        return qualifier != null && qualifier.qualified();
    }

    @Override
    public final Name qualifier() {
        return qualifier;
    }

    @Override
    public final Name unqualifiedName() {
        return last;
    }

    @Override
    public final Quoted quoted() {
        Quoted result = qualifier != null ? qualifier.quoted() : null;
        return result == null || result == last.quoted() ? last.quoted() : MIXED;
    }

    @Override
    public final Name quotedName() {
        return qualifier.quotedName().append(last.quotedName());
    }

    @Override
    public final Name unquotedName() {
        return qualifier.unquotedName().append(last.unquotedName());
    }

    @Override
    public final String[] getName() {
        if (empty())
            return EMPTY_STRING;

        int i = 1;
        Name q = qualifier;
        while (q != null) {
            q = q.qualifier();
            i++;
        }

        String[] name = new String[i];
        name[--i] = last.name;
        q = qualifier;
        while (q != null) {
            name[--i] = q.last();
            q = q.qualifier();
        }

        return name;
    }

    @Override
    public final Name[] parts() {
        if (empty())
            return EMPTY_NAME;

        int i = 1;
        Name q = qualifier;
        while (q != null) {
            q = q.qualifier();
            i++;
        }

        Name[] parts = new Name[i];
        parts[--i] = last;
        q = qualifier;
        while (q != null) {
            parts[--i] = q.unqualifiedName();
            q = q.qualifier();
        }

        return parts;
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

            h = 31 * h + hashCode0(last);

            Name q = qualifier;
            while (q != null) {
                h = 31 * h + hashCode0(q.unqualifiedName());
                q = q.qualifier();
            }

            hash = h;
        }

        return hash;
    }

    static int hashCode0(Name n1) {
        return n1 instanceof UnqualifiedName u ? hashCode0(u) : n1.hashCode();
    }

    static int hashCode0(UnqualifiedName n1) {
        return n1.name.hashCode();
    }

    static int hashCode0(Name n1, Name n2) {
        int h = 1;

        if (!n1.empty()) h = 31 * h + hashCode0(n1);
        if (!n2.empty()) h = 31 * h + hashCode0(n2);

        return h;
    }

    static int hashCode0(Name n1, Name n2, Name n3) {
        int h = 1;

        if (!n1.empty()) h = 31 * h + hashCode0(n1);
        if (!n2.empty()) h = 31 * h + hashCode0(n2);
        if (!n3.empty()) h = 31 * h + hashCode0(n3);

        return h;
    }

    static int hashCode0(Name n1, Name n2, Name n3, Name n4) {
        int h = 1;

        if (!n1.empty()) h = 31 * h + hashCode0(n1);
        if (!n2.empty()) h = 31 * h + hashCode0(n2);
        if (!n3.empty()) h = 31 * h + hashCode0(n3);
        if (!n4.empty()) h = 31 * h + hashCode0(n4);

        return h;
    }
}
