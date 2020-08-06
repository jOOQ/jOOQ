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

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import org.jooq.Catalog;
import org.jooq.Comment;
import org.jooq.Context;
import org.jooq.Domain;
import org.jooq.Internal;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.UDT;

/**
 * A schema that references a lazy initialisable {@link Schema} singleton, for
 * use in generated code.
 *
 * @author Lukas Eder
 */
@Internal
public final class LazySchema extends AbstractNamed implements Schema {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -2260128711891951254L;

    final Callable<Schema>    callable;
    transient Schema          schema;

    public LazySchema(Name name, Comment comment, Callable<Schema> callable) {
        super(name, comment);

        this.callable = callable;
    }

    private final Schema schema() {
        if (schema == null) {
            try {
                schema = callable.call();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return schema;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(schema());
    }

    // -------------------------------------------------------------------------
    // XXX: Schema API
    // -------------------------------------------------------------------------

    @Override
    public final Catalog getCatalog() {
        return schema().getCatalog();
    }



    @Override
    public final Stream<Table<?>> tableStream() {
        return schema().tableStream();
    }



    @Override
    public final List<Table<?>> getTables() {
        return schema().getTables();
    }

    @Override
    public final Table<?> getTable(String name) {
        return schema().getTable(name);
    }



    @Override
    public final Stream<UDT<?>> udtStream() {
        return schema().udtStream();
    }



    @Override
    public final List<UDT<?>> getUDTs() {
        return schema().getUDTs();
    }

    @Override
    public final UDT<?> getUDT(String name) {
        return schema().getUDT(name);
    }



    @Override
    public final Stream<Domain<?>> domainStream() {
        return schema().domainStream();
    }



    @Override
    public final List<Domain<?>> getDomains() {
        return schema().getDomains();
    }

    @Override
    public final Domain<?> getDomain(String name) {
        return schema().getDomain(name);
    }



    @Override
    public final Stream<Sequence<?>> sequenceStream() {
        return schema().sequenceStream();
    }



    @Override
    public final List<Sequence<?>> getSequences() {
        return schema().getSequences();
    }

    @Override
    public final Sequence<?> getSequence(String name) {
        return schema().getSequence(name);
    }
}
