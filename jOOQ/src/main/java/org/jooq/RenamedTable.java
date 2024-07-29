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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
package org.jooq;

import static org.jooq.impl.DSL.name;

import org.jooq.impl.TableImpl;

/**
 * A mapped table
 *
 * @author Lukas Eder
 */
class RenamedTable<R extends Record> extends TableImpl<R> {

    RenamedTable(Schema schema, Table<R> delegate, String rename) {
        super(name(rename), schema);

        for (Field<?> field : delegate.fields())
            createField(field.getUnqualifiedName(), field.getDataType(), this);
    }
}
