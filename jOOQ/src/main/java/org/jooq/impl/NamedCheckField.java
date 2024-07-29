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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq.impl;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.TableField;

/**
 * A marker interface for fields that are capable of generating fields whose
 * name is defined.
 * <p>
 * Unlike {@link NamedField}, which marks fields that have a name defined,
 * unconditionally, this allows for checking whether a {@link Field} has {@link #hasName()}.
 *
 * @author Lukas Eder
 */
interface NamedCheckField<T> extends Field<T> {

    /**
     * Whether the field really has a name defined, e.g. a
     * {@link Field#coerce(DataType)} expression on a {@link TableField}.
     */
    default boolean hasName(Context<?> ctx) {
        return false;
    }
}
