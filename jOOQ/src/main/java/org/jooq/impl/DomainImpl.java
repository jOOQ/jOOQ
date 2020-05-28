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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

import org.jooq.Check;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Domain;
import org.jooq.Name;
import org.jooq.Schema;

/**
 * @author Lukas Eder
 */
class DomainImpl<T> extends AbstractTypedNamed<T> implements Domain<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 162853300137140844L;
    private final Schema      schema;
    private final Check<?>[]  checks;

    DomainImpl(Schema schema, Name name, DataType<T> dataType, Check<?>... checks) {
        super(qualify(schema, name), null, dataType);

        this.schema = schema;
        this.checks = checks;
    }

    @Override
    public final Schema getSchema() {
        return schema;
    }

    @Override
    public final List<Check<?>> checks() {
        return unmodifiableList(asList(checks));
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(getUnqualifiedName());
    }
}
