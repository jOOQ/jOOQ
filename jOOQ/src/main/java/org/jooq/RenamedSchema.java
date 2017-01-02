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
package org.jooq;

import java.util.List;

import org.jooq.impl.SchemaImpl;

/**
 * A mapped schema
 *
 * @author Lukas Eder
 */
class RenamedSchema extends SchemaImpl {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -3579885830845728730L;

    private final Schema      delegate;

    RenamedSchema(Schema delegate, String rename) {
        super(rename);

        this.delegate = delegate;
    }

    @Override
    public final List<Table<?>> getTables() {
        return delegate.getTables();
    }

    @Override
    public final List<UDT<?>> getUDTs() {
        return delegate.getUDTs();
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        return delegate.getSequences();
    }
}
