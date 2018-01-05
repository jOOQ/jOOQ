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

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DSLContext;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.ResultQuery;
import org.jooq.Results;
import org.jooq.impl.ResultsImpl.ResultOrRowsImpl;

/**
 * @author Lukas Eder
 */
final class QueriesImpl extends AbstractQueryPart implements Queries {

    /**
     * Generated UID
     */
    private static final long                 serialVersionUID = 261452207127914269L;

    private final Collection<? extends Query> queries;
    private Configuration                     configuration;

    QueriesImpl(Configuration configuration, Collection<? extends Query> queries) {
        this.configuration = configuration;
        this.queries = queries;
    }

    // ------------------------------------------------------------------------
    // Access API
    // ------------------------------------------------------------------------

    @Override
    public final Query[] queries() {
        return queries.toArray(EMPTY_QUERY);
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
        ResultsImpl results = new ResultsImpl(configuration());
        DSLContext ctx = DSL.using(configuration());

        for (Query query : this)
            if (query instanceof ResultQuery)
                results.resultsOrRows.addAll(ctx.fetchMany((ResultQuery<?>) query).resultsOrRows());
            else
                results.resultsOrRows.add(new ResultOrRowsImpl(ctx.execute(query)));

        return results;
    }

    @Override
    public final int[] executeBatch() {
        return DSL.using(configuration()).batch(this).execute();
    }

    // ------------------------------------------------------------------------
    // Attachable API
    // ------------------------------------------------------------------------

    @Override
    public final void attach(Configuration c) {
        configuration = c;
    }

    @Override
    public final void detach() {
        attach(null);
    }

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    // ------------------------------------------------------------------------
    // QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        for (Query query : this)
            ctx.visit(query).sql(';').formatNewLine();
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Query query : queries)
            sb.append(query).append(";\n");

        return sb.toString();
    }
}
