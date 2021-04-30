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

import static org.jooq.impl.Tools.EMPTY_QUERY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.jooq.Block;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DSLContext;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.ResultQuery;
import org.jooq.Results;
import org.jooq.impl.ResultsImpl.ResultOrRowsImpl;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class QueriesImpl extends AbstractAttachableQueryPart implements Queries {

    private final Collection<? extends Query> queries;

    QueriesImpl(Configuration configuration, Collection<? extends Query> queries) {
        super(configuration);

        this.queries = queries;
    }

    // ------------------------------------------------------------------------
    // Access API
    // ------------------------------------------------------------------------

    @Override
    public final Queries concat(Queries other) {
        Query[] array = other.queries();
        List<Query> list = new ArrayList<>(queries.size() + array.length);
        list.addAll(queries);
        list.addAll(Arrays.asList(array));
        return new QueriesImpl(configuration(), list);
    }

    @Override
    public final Query[] queries() {
        return queries.toArray(EMPTY_QUERY);
    }

    @Override
    public final Block block() {
        return configurationOrDefault().dsl().begin(queries);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Iterator<Query> iterator() {
        return (Iterator) queries.iterator();
    }

    @Override
    public final Stream<Query> stream() {
        return queryStream();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Stream<Query> queryStream() {
        return (Stream) queries.stream();
    }

    // ------------------------------------------------------------------------
    // Execution API
    // ------------------------------------------------------------------------

    @Override
    public final Results fetchMany() {
        Configuration c = configurationOrThrow();
        ResultsImpl results = new ResultsImpl(c);
        DSLContext ctx = c.dsl();

        for (Query query : this)
            if (query instanceof ResultQuery)
                results.resultsOrRows.addAll(ctx.fetchMany((ResultQuery<?>) query).resultsOrRows());
            else
                results.resultsOrRows.add(new ResultOrRowsImpl(ctx.execute(query)));

        return results;
    }

    @Override
    public final int[] executeBatch() {
        return configurationOrThrow().dsl().batch(this).execute();
    }

    // ------------------------------------------------------------------------
    // QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        boolean first = true;

        for (Query query : this) {
            if (first)
                first = false;
            else
                ctx.formatSeparator();

            ctx.visit(query).sql(';');
        }
    }

    // ------------------------------------------------------------------------
    // Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return queries.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof QueriesImpl))
            return false;

        return queries.equals(((QueriesImpl) obj).queries);
    }
}
