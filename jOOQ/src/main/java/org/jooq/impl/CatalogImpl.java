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

import static org.jooq.Clause.CATALOG;
import static org.jooq.Clause.CATALOG_REFERENCE;
import static org.jooq.impl.Tools.getMappedCatalog;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.jooq.Catalog;
import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.tools.StringUtils;

/**
 * A common base class for database catalogs
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public class CatalogImpl extends AbstractNamed implements Catalog {
    private static final Clause[] CLAUSES          = { CATALOG, CATALOG_REFERENCE };
    static final Catalog          DEFAULT_CATALOG  = new CatalogImpl("");

    public CatalogImpl(Name name) {
        this(name, null);
    }

    public CatalogImpl(String name) {
        this(name, null);
    }

    public CatalogImpl(Name name, Comment comment) {
        super(name, comment);
    }

    public CatalogImpl(String name, String comment) {
        super(DSL.name(name), DSL.comment(comment));
    }

    @Override
    public final void accept(Context<?> ctx) {
        Catalog mappedCatalog = getMappedCatalog(ctx, this);
        ctx.visit(mappedCatalog != null ? mappedCatalog.getUnqualifiedName() : getUnqualifiedName());
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final Schema getSchema(String name) {
        return find(name, getSchemas());
    }

    @Override
    public final Schema getSchema(Name name) {
        return find(name, getSchemas());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses should override this method
     */
    @Override
    public List<Schema> getSchemas() {
        return Collections.emptyList();
    }

    @Override
    public final Stream<Schema> schemaStream() {
        return getSchemas().stream();
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        // [#11078] CatalogImpl equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof CatalogImpl)
            return StringUtils.equals(getName(), ((CatalogImpl) that).getName());

        return super.equals(that);
    }
}
