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

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.jooq.impl.DSL.createSchema;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.schema;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.map;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Configuration;
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
final class VersionImpl extends AbstractNode<Version> implements Version {

    final DSLContext   ctx;
    final Meta         meta;
    final List<Parent> parents;

    private VersionImpl(Configuration configuration, String id, Meta meta, Version root, List<Parent> parents) {
        super(configuration, id, null, root);

        this.ctx = configuration.dsl();
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

    VersionImpl(Configuration configuration, String id, Meta meta, Version root, Version parent, Queries queries) {
        this(configuration, id, meta, root, Arrays.asList(new Parent((VersionImpl) parent, queries)));
    }

    VersionImpl(Configuration configuration, String id, Meta meta, Version root, Version[] parents) {
        this(configuration, id, meta, root, wrap(parents));
    }

    /**
     * Create the root {@link Version}.
     */
    VersionImpl(Configuration configuration, String id) {
        this(configuration, id, null, null, asList());
    }

    private static List<Parent> wrap(Version[] parents) {
        return map(parents, p -> new Parent((VersionImpl) p, null));
    }

    @Override
    public final Meta meta() {
        return meta;
    }

    @Override
    public final List<Version> parents() {
        return new AbstractList<Version>() {
            @Override
            public Version get(int index) {
                return parents.get(index).version;
            }

            @Override
            public int size() {
                return parents.size();
            }
        };
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
        return new VersionImpl(ctx.configuration(), newId, meta().apply(migration), root, this, migration);
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

                list.add(new Parent(new VersionImpl(ctx.configuration(), parent.version.id(), parent.version.meta, root, emptyList()), parent.queries));
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

        return list == null ? null : new VersionImpl(ctx.configuration(), id(), meta, root, list);
    }

    private final Queries migrateTo(VersionImpl target, Queries result) {
        if (!target.forceApply())
            return meta().migrateTo(target.meta());

        for (Parent parent : target.parents) {
            result = migrateTo(parent.version, result);

            if (parent.queries != null)
                result = result.concat(parent.queries);
            else
                result = result.concat(parent.version.meta().migrateTo(target.meta()));
        }

        return result;
    }

    private final boolean forceApply() {
        return anyMatch(parents, p -> p.queries != null || p.version.forceApply());
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
        return new VersionImpl(ctx.configuration(), newId, newMeta, root, new Version[] { this });
    }

    @Override
    public final Version merge(String newId, Version with) {
        Meta m = commonAncestor(with).meta();
        return new VersionImpl(ctx.configuration(), newId, m.apply(m.migrateTo(meta()).concat(m.migrateTo(with.meta()))), root, new Version[] { this, with });
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id().hashCode();
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
        if (!id().equals(other.id()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "-- Version: " + id() + "\n" + meta();
    }

    private static final record Parent(VersionImpl version, Queries queries) {
        @Override
        public String toString() {
            return version.toString();
        }
    }
}
