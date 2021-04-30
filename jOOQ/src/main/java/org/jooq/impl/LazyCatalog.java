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
import org.jooq.Context;
import org.jooq.Internal;
import org.jooq.Name;
import org.jooq.Schema;

/**
 * A schema that references a lazy initialisable {@link Catalog} singleton, for
 * use in generated code.
 *
 * @author Lukas Eder
 */
@Internal
public final class LazyCatalog extends AbstractNamed implements Catalog {

    final LazySupplier<Catalog> supplier;
    transient Catalog           catalog;

    public LazyCatalog(Name name, LazySupplier<Catalog> supplier) {
        super(name, CommentImpl.NO_COMMENT);

        this.supplier = supplier;
    }

    private final Catalog catalog() {
        if (catalog == null) {
            try {
                catalog = supplier.get();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return catalog;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(catalog());
    }

    // -------------------------------------------------------------------------
    // XXX: Schema API
    // -------------------------------------------------------------------------

    @Override
    public final List<Schema> getSchemas() {
        return catalog().getSchemas();
    }

    @Override
    public final Schema getSchema(String name) {
        return catalog().getSchema(name);
    }

    @Override
    public final Schema getSchema(Name name) {
        return catalog().getSchema(name);
    }

    @Override
    public final Stream<Schema> schemaStream() {
        return catalog().schemaStream();
    }
}
