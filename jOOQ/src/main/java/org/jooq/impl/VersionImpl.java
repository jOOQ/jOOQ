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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.DSLContext;
import org.jooq.Internal;
import org.jooq.Meta;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Source;
import org.jooq.Version;

/**
 * @author Lukas Eder
 */
@Internal
final class VersionImpl implements Version {

    private final DSLContext    ctx;
    private final String        id;
    private final Meta          meta;
    private final List<Version> parents;

    VersionImpl(DSLContext ctx, String id, Meta meta, Version... parents) {
        this.ctx = ctx;
        this.id = id;
        this.meta = meta != null ? meta : ctx.meta("");
        this.parents = Arrays.asList(parents);
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
    public final List<Version> parents() {
        return Collections.unmodifiableList(parents);
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
        return new VersionImpl(ctx, newId, meta().apply(diff), new Version[] { this });
    }

    @Override
    public final Queries migrateFrom(Version version) {
        Queries result = null;





        // TODO: Provide OSS edition solution here
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
}
