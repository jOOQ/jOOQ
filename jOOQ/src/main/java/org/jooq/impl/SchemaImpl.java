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

import static org.jooq.Clause.SCHEMA;
import static org.jooq.Clause.SCHEMA_REFERENCE;
import static org.jooq.impl.CatalogImpl.DEFAULT_CATALOG;
import static org.jooq.impl.Tools.flatMap;
import static org.jooq.impl.Tools.getMappedCatalog;
import static org.jooq.impl.Tools.getMappedSchema;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.jooq.Catalog;
import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Context;
import org.jooq.Domain;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.UDT;
import org.jooq.UniqueKey;
import org.jooq.tools.StringUtils;

/**
 * A common base class for database schemata
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public class SchemaImpl extends AbstractNamed implements Schema {

    private static final Clause[] CLAUSES        = { SCHEMA, SCHEMA_REFERENCE };
    static final Schema           DEFAULT_SCHEMA = new SchemaImpl("");

    private Catalog               catalog;

    public SchemaImpl(String name) {
        this(name, null);
    }

    public SchemaImpl(String name, Catalog catalog) {
        this(DSL.name(name), catalog);
    }

    public SchemaImpl(String name, Catalog catalog, String comment) {
        this(DSL.name(name), catalog, DSL.comment(comment));
    }

    public SchemaImpl(Name name) {
        this(name, null);
    }

    public SchemaImpl(Name name, Catalog catalog) {
        this(name, catalog, null);
    }

    public SchemaImpl(Name name, Catalog catalog, Comment comment) {
        super(qualify(catalog, name), comment);

        this.catalog = catalog;
    }

    @Override
    public /* non-final */ Catalog getCatalog() {
        if (catalog == null)
            catalog = getQualifiedName().qualified()
                    ? DSL.catalog(getQualifiedName().qualifier())
                    : null;

        return catalog;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ctx.qualifyCatalog()) {
            Catalog mappedCatalog = getMappedCatalog(ctx, getCatalog());

            if (mappedCatalog != null && !"".equals(mappedCatalog.getName()))
                ctx.visit(mappedCatalog).sql('.');
        }

        Schema mappedSchema = getMappedSchema(ctx, this);
        ctx.visit(mappedSchema != null ? mappedSchema.getUnqualifiedName() : getUnqualifiedName());
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final Table<?> getTable(String name) {
        return find(name, getTables());
    }

    @Override
    public final Table<?> getTable(Name name) {
        return find(name, getTables());
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys(String name) {
        return findAll(name, getPrimaryKeys());
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys(Name name) {
        return findAll(name, getPrimaryKeys());
    }

    @Override
    public final List<UniqueKey<?>> getUniqueKeys(String name) {
        return findAll(name, getUniqueKeys());
    }

    @Override
    public final List<UniqueKey<?>> getUniqueKeys(Name name) {
        return findAll(name, getUniqueKeys());
    }

    @Override
    public final List<ForeignKey<?, ?>> getForeignKeys(String name) {
        return findAll(name, getForeignKeys());
    }

    @Override
    public final List<ForeignKey<?, ?>> getForeignKeys(Name name) {
        return findAll(name, getForeignKeys());
    }

    @Override
    public final List<Index> getIndexes(String name) {
        return findAll(name, getIndexes());
    }

    @Override
    public final List<Index> getIndexes(Name name) {
        return findAll(name, getIndexes());
    }

    @Override
    public final UDT<?> getUDT(String name) {
        return find(name, getUDTs());
    }

    @Override
    public final UDT<?> getUDT(Name name) {
        return find(name, getUDTs());
    }

    @Override
    public final Domain<?> getDomain(String name) {
        return find(name, getDomains());
    }

    @Override
    public final Domain<?> getDomain(Name name) {
        return find(name, getDomains());
    }

    @Override
    public final Sequence<?> getSequence(String name) {
        return find(name, getSequences());
    }

    @Override
    public final Sequence<?> getSequence(Name name) {
        return find(name, getSequences());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses should override this method
     */
    @Override
    public List<Table<?>> getTables() {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses may override this method
     */
    @Override
    public List<UniqueKey<?>> getPrimaryKeys() {
        List<UniqueKey<?>> result = new ArrayList<>();

        for (Table<?> table : getTables())
            if (table.getPrimaryKey() != null)
                result.add(table.getPrimaryKey());

        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses may override this method
     */
    @Override
    public List<UniqueKey<?>> getUniqueKeys() {
        return flatMap(getTables(), t -> t.getKeys());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses may override this method
     */
    @Override
    public List<ForeignKey<?, ?>> getForeignKeys() {
        return flatMap(getTables(), t -> t.getReferences());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses may override this method
     */
    @Override
    public List<Index> getIndexes() {
        return flatMap(getTables(), t -> t.getIndexes());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses should override this method
     */
    @Override
    public List<UDT<?>> getUDTs() {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses should override this method
     */
    @Override
    public List<Domain<?>> getDomains() {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses should override this method
     */
    @Override
    public List<Sequence<?>> getSequences() {
        return Collections.emptyList();
    }

    @Override
    public final Stream<Table<?>> tableStream() {
        return getTables().stream();
    }

    @Override
    public final Stream<UniqueKey<?>> primaryKeyStream() {
        return getPrimaryKeys().stream();
    }

    @Override
    public final Stream<UniqueKey<?>> uniqueKeyStream() {
        return getUniqueKeys().stream();
    }

    @Override
    public final Stream<ForeignKey<?, ?>> foreignKeyStream() {
        return getForeignKeys().stream();
    }

    @Override
    public final Stream<Index> indexStream() {
        return getIndexes().stream();
    }

    @Override
    public final Stream<UDT<?>> udtStream() {
        return getUDTs().stream();
    }

    @Override
    public final Stream<Domain<?>> domainStream() {
        return getDomains().stream();
    }

    @Override
    public final Stream<Sequence<?>> sequenceStream() {
        return getSequences().stream();
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        // [#2144] SchemaImpl equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof SchemaImpl) {
            SchemaImpl other = (SchemaImpl) that;
            return

                // [#7172] [#10274] Cannot use getQualifiedName() yet here
                StringUtils.equals(
                    defaultIfNull(getCatalog(), DEFAULT_CATALOG),
                    defaultIfNull(other.getCatalog(), DEFAULT_CATALOG)
                ) &&
                StringUtils.equals(getName(), other.getName());
        }

        return super.equals(that);
    }
}
