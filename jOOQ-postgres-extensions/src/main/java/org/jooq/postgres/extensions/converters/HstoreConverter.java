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
package org.jooq.postgres.extensions.converters;

import static org.jooq.postgres.extensions.types.Hstore.hstore;

import org.jooq.impl.AbstractConverter;
import org.jooq.postgres.extensions.types.Hstore;

/**
 * A binding for the PostgreSQL <code>hstore</code> data type.
 *
 * @author Dmitry Baev
 * @author Lukas Eder
 */
public class HstoreConverter extends AbstractConverter<Object, Hstore> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -2852275331366617696L;

    public HstoreConverter() {
        super(Object.class, Hstore.class);
    }

    @Override
    public Hstore from(Object t) {
        return t == null ? null : hstore(org.postgresql.util.HStoreConverter.fromString("" + t));
    }

    @Override
    public Object to(Hstore u) {
        return u == null ? null : org.postgresql.util.HStoreConverter.toString(u.data());
    }
}
