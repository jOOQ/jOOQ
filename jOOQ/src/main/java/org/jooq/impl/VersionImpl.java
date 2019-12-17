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

import static java.lang.Boolean.TRUE;
import static org.jooq.impl.DSL.createSchema;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.schema;

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
import org.jooq.conf.InterpreterSearchSchema;
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
        this.meta = meta != null ? meta : init(ctx);
        this.parents = parents;
    }

    private static final Meta init(DSLContext ctx) {
        Meta result = ctx.meta("");

        // TODO: Instead of reusing interpreter search path, we should have some dedicated
        //       configuration for this.
        // TODO: Should this be moved in DSLContext.meta()?
        List<InterpreterSearchSchema> searchPath = ctx.settings().getInterpreterSearchPath();
        for (InterpreterSearchSchema schema : searchPath)
            result = result.apply(createSchema(schema(name(schema.getCatalog(), schema.getSchema()))));

        return result;
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
    public final Version apply(String newId, Query... migration) {
        return apply(newId, ctx.queries(migration));
    }

    @Override
    public final Version apply(String newId, Collection<? extends Query> migration) {
        return apply(newId, ctx.queries(migration));
    }

    @Override
    public final Version apply(String newId, String migration) {
        return apply(newId, ctx.parser().parse(migration));
    }

    @Override
    public final Version apply(String newId, Queries migration) {
        return new VersionImpl(ctx, newId, meta().apply(migration), this, migration);
    }

    @Override
    public final Queries migrateTo(Version version) {
        if (equals(version))
            return ctx.queries();

        VersionImpl subgraph = ((VersionImpl) version).subgraphTo(this);

        if (subgraph == null)





                throw new DataDefinitionException("No forward path available between versions " + id() + " and " + version.id() + ". Use Settings.migrationAllowsUndo to enable this feature.");

        return migrateTo(subgraph, ctx.queries());
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

    private final Queries migrateTo(VersionImpl target, Queries result) {
        if (!target.forceApply())
            return meta().migrateTo(target.meta());

        for (Parent parent : target.parents) {
            result = migrateTo(parent.version, result);

            if (!forceApply(parent.queries))
                result = result.concat(parent.version.meta().migrateTo(target.meta()));
            else
                result = result.concat(parent.queries);
        }

        return result;
    }

    private final boolean forceApply() {
        for (Parent parent : parents)
            if (forceApply(parent.queries))
                return true;
            else if (parent.version.forceApply())
                return true;

        return false;
    }

    private static final boolean forceApply(Queries queries) {
        if (queries != null)
            for (Query query : queries.queries())
                if (!(query instanceof DDLQuery))
                    return true;

        return false;
    }

    @Override
    public final Version commit(String newId, String... newMeta) {
        return commit(newId, ctx.meta(newMeta));
    }

    @Override
    public final Version commit(String newId, Source... newMeta) {
        return commit(newId, ctx.meta(newMeta));
    }

    @Override
    public final Version commit(String newId, Meta newMeta) {
        return new VersionImpl(ctx, newId, newMeta, new Version[] { this });
    }

    private static final Version commonAncestor(Version v1, Version v2) {
        if (v1.id().equals(v2.id()))
            return v1;

        // TODO: Find a better solution than the brute force one
        // See e.g. https://en.wikipedia.org/wiki/Lowest_common_ancestor

        Map<Version, Integer> a1 = ancestors(v1, new HashMap<>(), 1);
        Map<Version, Integer> a2 = ancestors(v2, new HashMap<>(), 1);

        Version version = null;
        Integer distance = null;

        for (Entry<Version, Integer> entry : a1.entrySet()) {
            if (a2.containsKey(entry.getKey())) {

                // TODO: What if there are several conflicting paths?
                if (distance == null || distance > entry.getValue()) {
                    version = entry.getKey();
                    distance = entry.getValue();
                }
            }
        }

        if (version == null)
            throw new DataDefinitionException("Versions " + v1.id() + " and " + v2.id() + " do not have a common ancestor");

        return version;
    }

    private static Map<Version, Integer> ancestors(Version v, Map<Version, Integer> result, int distance) {
        VersionImpl current = (VersionImpl) v;
        Integer previous = result.get(current);

        if (previous == null || previous > distance) {
            result.put(current, distance);

            for (Parent parent : current.parents)
                ancestors(parent.version, result, distance + 1);
        }

        return result;
    }

    @Override
    public final Version merge(String newId, Version with) {
        Meta m = commonAncestor(this, with).meta();
        return new VersionImpl(ctx, newId, m.apply(m.migrateTo(meta()).concat(m.migrateTo(with.meta()))), new Version[] { this, with });
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
