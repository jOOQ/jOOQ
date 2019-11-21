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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.DDLQuery;
import org.jooq.DSLContext;
import org.jooq.Meta;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Source;
import org.jooq.Version;
import org.jooq.exception.DataDefinitionException;

/**
 * @author Lukas Eder
 */
final class VersionImpl implements Version {

    private final DSLContext   ctx;
    private final String       id;
    private final Meta         meta;
    private final List<Parent> parents;

    private VersionImpl(DSLContext ctx, String id, Meta meta, List<Parent> parents) {
        this.ctx = ctx;
        this.id = id;
        this.meta = meta != null ? meta : ctx.meta("");
        this.parents = parents;
    }

    VersionImpl(DSLContext ctx, String id, Meta meta, Version parent, Queries queries) {
        this(ctx, id, meta, Arrays.asList(new Parent((VersionImpl) parent, queries)));
    }

    VersionImpl(DSLContext ctx, String id, Meta meta, Version[] parents) {
        this(ctx, id, meta, wrap(parents));
    }

    private static List<Parent> wrap(Version[] parents) {
        List<Parent> result = new ArrayList<>(parents.length);

        for (Version parent : parents)
            result.add(new Parent((VersionImpl) parent, null));

        return result;
    }

    @Override
    public final String id() {
        return id;
    }

    @Override
    public final Meta meta() {
        return meta;
    }

    @Override
    public final Version root() {
        VersionImpl result = this;

        while (result.parents.size() > 0)
            result = result.parents.get(0).version;

        return result;
    }

    @Override
    public final Version apply(String newId, Query... diff) {
        return apply(newId, ctx.queries(diff));
    }

    @Override
    public final Version apply(String newId, Collection<? extends Query> diff) {
        return apply(newId, ctx.queries(diff));
    }

    @Override
    public final Version apply(String newId, String diff) {
        return apply(newId, ctx.parser().parse(diff));
    }

    @Override
    public final Version apply(String newId, Queries diff) {
        return new VersionImpl(ctx, newId, meta().apply(diff), this, diff);
    }

    @Override
    public final Queries migrateFrom(Version version) {
        VersionImpl subgraph = subgraphTo((VersionImpl) version);











        return subgraph.migrateFrom((VersionImpl) version, ctx.queries());
    }

    private final VersionImpl subgraphTo(VersionImpl ancestor) {
        List<Parent> list = null;

        for (Parent parent : parents) {
            if (parent.version.equals(ancestor)) {
                if (list == null)
                    list = new ArrayList<>();

                list.add(new Parent(new VersionImpl(ctx, parent.version.id, parent.version.meta, Collections.<Parent>emptyList()), parent.queries));
            }
            else {
                VersionImpl p = parent.version.subgraphTo(ancestor);

                if (p != null) {
                    if (list == null)
                        list = new ArrayList<>();

                    list.add(new Parent(p, parent.queries));
                }
            }
        }

        return list == null ? null : new VersionImpl(ctx, id, meta, list);
    }

    private final Queries migrateFrom(VersionImpl ancestor, Queries result) {






        for (Parent parent : parents) {
            result = parent.version.migrateFrom(ancestor, result);






                result = result.concat(parent.queries);
        }

        return result;
    }

























































































    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VersionImpl other = (VersionImpl) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "-- Version: " + id() + "\n" + meta();
    }

    private static final class Parent {
        final VersionImpl version;
        final Queries     queries;

        Parent(VersionImpl version, Queries queries) {
            this.version = version;
            this.queries = queries;
        }

        @Override
        public String toString() {
            return version.toString();
        }
    }
}
