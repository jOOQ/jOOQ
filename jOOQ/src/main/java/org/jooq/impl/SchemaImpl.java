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

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.jooq.Catalog;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.UDT;
import org.jooq.tools.StringUtils;

/**
 * A common base class for database schemata
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public class SchemaImpl extends AbstractQueryPart implements Schema {

    private static final long     serialVersionUID = -8101463810207566546L;
    private static final Clause[] CLAUSES          = { SCHEMA, SCHEMA_REFERENCE };

    private final Name            name;
    private Catalog               catalog;

    public SchemaImpl(String name) {
        this(name, null);
    }

    public SchemaImpl(String name, Catalog catalog) {
        this(DSL.name(name), catalog);
    }

    public SchemaImpl(Name name) {
        this(name, null);
    }

    public SchemaImpl(Name name, Catalog catalog) {
        this.name = name;
        this.catalog = catalog;
    }

    @Override
    public /* non-final */ Catalog getCatalog() {
        if (catalog == null)
            catalog = name.qualified()
                    ? DSL.catalog(name.qualifier())
                    : null;

        return catalog;
    }

    @Override
    public final String getName() {
        return name.last();
    }

    @Override
    public final Name getQualifiedName() {
        return name;
    }

    @Override
    public final Name getUnqualifiedName() {
        return name.unqualifiedName();
    }

    @Override
    public final void accept(Context<?> ctx) {
        Catalog mappedCatalog = Tools.getMappedCatalog(ctx.configuration(), getCatalog());

        if (ctx.qualifyCatalog() && mappedCatalog != null) {
            ctx.visit(mappedCatalog);
            ctx.sql('.');
        }

        ctx.visit(name.unqualifiedName());
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final Table<?> getTable(String tableName) {
        for (Table<?> table : getTables())
            if (table.getName().equals(tableName))
                return table;

        return null;
    }

    @Override
    public final UDT<?> getUDT(String udtName) {
        for (UDT<?> udt : getUDTs())
            if (udt.getName().equals(udtName))
                return udt;

        return null;
    }

    @Override
    public final Sequence<?> getSequence(String sequenceName) {
        for (Sequence<?> sequence : getSequences())
            if (sequence.getName().equals(sequenceName))
                return sequence;

        return null;
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
    public List<Sequence<?>> getSequences() {
        return Collections.emptyList();
    }


    @Override
    public final Stream<Table<?>> tableStream() {
        return getTables().stream();
    }

    @Override
    public final Stream<UDT<?>> udtStream() {
        return getUDTs().stream();
    }

    @Override
    public final Stream<Sequence<?>> sequenceStream() {
        return getSequences().stream();
    }


    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {

        // [#1938] This is a much more efficient hashCode() implementation
        // compared to that of standard QueryParts
        return getName() != null ? getName().hashCode() : 0;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        // [#2144] SchemaImpl equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof SchemaImpl) {
            SchemaImpl other = (SchemaImpl) that;
            return
                StringUtils.equals(getCatalog(), other.getCatalog()) &&
                StringUtils.equals(getName(), other.getName());
        }

        return super.equals(that);
    }
}
