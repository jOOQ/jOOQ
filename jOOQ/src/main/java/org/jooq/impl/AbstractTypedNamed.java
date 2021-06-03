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

import org.jooq.Binding;
import org.jooq.Comment;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Name;
import org.jooq.Typed;

/**
 * @author Lukas Eder
 */
abstract class AbstractTypedNamed<T> extends AbstractNamed implements Typed<T> {

    private final DataType<T> type;

    AbstractTypedNamed(Name name, Comment comment, DataType<T> type) {
        super(name, comment);

        this.type = type;
    }

    // -------------------------------------------------------------------------
    // XXX: Typed API
    // -------------------------------------------------------------------------

    @Override
    public final Converter<?, T> getConverter() {
        return getDataType().getConverter();
    }

    @Override
    public final Binding<?, T> getBinding() {
        return getDataType().getBinding();
    }

    @Override
    public final Class<T> getType() {
        return getDataType().getType();
    }

    /**
     * [#11959] A workaround to get access to the actual data type of an
     * expression.
     */
    /* non-final */ DataType<?> getExpressionDataType() {
        return getDataType();
    }

    @Override
    public final DataType<T> getDataType() {
        return type;
    }

    @Override
    public final DataType<T> getDataType(Configuration configuration) {
        return getDataType().getDataType(configuration);
    }
}
