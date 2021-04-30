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
import java.util.stream.Stream;

import org.jooq.Catalog;
import org.jooq.Comment;
import org.jooq.Context;
import org.jooq.Domain;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Internal;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.UDT;
import org.jooq.UniqueKey;

/**
 * A schema that references a lazy initialisable {@link Schema} singleton, for
 * use in generated code.
 *
 * @author Lukas Eder
 */
@Internal
public final class LazySchema extends AbstractNamed implements Schema {

    final LazySupplier<Schema> supplier;
    transient Schema           schema;

    public LazySchema(Name name, Comment comment, LazySupplier<Schema> supplier) {
        super(name, comment);

        this.supplier = supplier;
    }

    private final Schema schema() {
        if (schema == null) {
            try {
                schema = supplier.get();
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
    public final List<Table<?>> getTables() {
        return schema().getTables();
    }

    @Override
    public final Table<?> getTable(String name) {
        return schema().getTable(name);
    }

    @Override
    public final Table<?> getTable(Name name) {
        return schema().getTable(name);
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys() {
        return schema().getPrimaryKeys();
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys(String name) {
        return schema().getPrimaryKeys(name);
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys(Name name) {
        return schema().getPrimaryKeys(name);
    }

    @Override
    public final List<UniqueKey<?>> getUniqueKeys() {
        return schema().getUniqueKeys();
    }

    @Override
    public final List<UniqueKey<?>> getUniqueKeys(String name) {
        return schema().getUniqueKeys(name);
    }

    @Override
    public final List<UniqueKey<?>> getUniqueKeys(Name name) {
        return schema().getUniqueKeys(name);
    }

    @Override
    public final List<ForeignKey<?, ?>> getForeignKeys() {
        return schema().getForeignKeys();
    }

    @Override
    public final List<ForeignKey<?, ?>> getForeignKeys(String name) {
        return schema().getForeignKeys(name);
    }

    @Override
    public final List<ForeignKey<?, ?>> getForeignKeys(Name name) {
        return schema().getForeignKeys(name);
    }

    @Override
    public final List<Index> getIndexes() {
        return schema().getIndexes();
    }

    @Override
    public final List<Index> getIndexes(String name) {
        return schema().getIndexes(name);
    }

    @Override
    public final List<Index> getIndexes(Name name) {
        return schema().getIndexes(name);
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
    public final UDT<?> getUDT(Name name) {
        return schema().getUDT(name);
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
    public final Domain<?> getDomain(Name name) {
        return schema().getDomain(name);
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        return schema().getSequences();
    }

    @Override
    public final Sequence<?> getSequence(String name) {
        return schema().getSequence(name);
    }

    @Override
    public final Sequence<?> getSequence(Name name) {
        return schema().getSequence(name);
    }

    @Override
    public final Stream<Table<?>> tableStream() {
        return schema().tableStream();
    }

    @Override
    public final Stream<UniqueKey<?>> primaryKeyStream() {
        return schema().primaryKeyStream();
    }

    @Override
    public final Stream<UniqueKey<?>> uniqueKeyStream() {
        return schema().uniqueKeyStream();
    }

    @Override
    public final Stream<ForeignKey<?, ?>> foreignKeyStream() {
        return schema().foreignKeyStream();
    }

    @Override
    public final Stream<Index> indexStream() {
        return schema().indexStream();
    }

    @Override
    public final Stream<UDT<?>> udtStream() {
        return schema().udtStream();
    }

    @Override
    public final Stream<Domain<?>> domainStream() {
        return schema().domainStream();
    }

    @Override
    public final Stream<Sequence<?>> sequenceStream() {
        return schema().sequenceStream();
    }
}
