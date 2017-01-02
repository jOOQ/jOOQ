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
 */
package org.jooq.impl;

import java.sql.ResultSetMetaData;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.Record;

/**
 * A plain SQL query that returns results
 *
 * @author Lukas Eder
 */
final class SQLResultQuery extends AbstractResultQuery<Record> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 1740879770879469220L;

    private final QueryPart   delegate;

    SQLResultQuery(Configuration configuration, QueryPart delegate) {
        super(configuration);

        this.delegate = delegate;
    }

    // ------------------------------------------------------------------------
    // ResultQuery API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        if (delegate instanceof QueryPartInternal) {
            return ((QueryPartInternal) delegate).clauses(ctx);
        }

        return null;
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    protected final Field<?>[] getFields(ResultSetMetaData meta) {
        Configuration configuration = configuration();
        return new MetaDataFieldProvider(configuration, meta).getFields();
    }

    @Override
    final boolean isForUpdate() {
        return false;
    }
}
