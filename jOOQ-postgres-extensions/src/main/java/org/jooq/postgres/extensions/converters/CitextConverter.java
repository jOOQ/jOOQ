/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import org.jooq.impl.AbstractConverter;

/**
 * A converter for the PostgreSQL <code>citext</code> data type.
 *
 * @author Lukas Eder
 */
public class CitextConverter extends AbstractConverter<Object, String> {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public CitextConverter() {

        // [#17958] citext is really a String type, though we need to continue declaring Object
        //          as that's what generated code is expecting.
        super((Class) String.class, String.class);
    }

    @Override
    public String from(Object t) {
        return t == null ? null : t.toString();
    }

    @Override
    public Object to(String u) {
        return u;
    }
}
