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

import org.jooq.DataType;
import org.jooq.Name;
import org.jooq.Param;
import org.jooq.tools.JooqLogger;

/**
 * A base implementation for {@link Param} handling deprecation warnings, see
 * [#11129]
 *
 * @author Lukas Eder
 */
abstract class AbstractParamX<T> extends AbstractField<T> implements Param<T> {
    private static final JooqLogger log              = JooqLogger.getLogger(AbstractParam.class);

    AbstractParamX(Name name, DataType<T> type) {
        super(name, type);
    }

    // ------------------------------------------------------------------------
    // XXX: Param API
    // ------------------------------------------------------------------------

    @Override
    @Deprecated
    public final void setValue(T value) {
        log.warn("Deprecation", "org.jooq.Param will soon be made immutable. It is recommended to no longer use its deprecated, mutating methods.", new UnsupportedOperationException("Param.setValue"));
        setConverted0(value);
    }

    final void setValue0(T value) {
        setConverted0(value);
    }

    @Override
    @Deprecated
    public final void setConverted(Object value) {
        log.warn("Deprecation", "org.jooq.Param will soon be made immutable. It is recommended to no longer use its deprecated, mutating methods.", new UnsupportedOperationException("Param.setConverted"));
        setConverted0(value);
    }

    abstract void setConverted0(Object value);

    @Override
    @Deprecated
    public final void setInline(boolean inline) {
        log.warn("Deprecation", "org.jooq.Param will soon be made immutable. It is recommended to no longer use its deprecated, mutating methods.", new UnsupportedOperationException("Param.setInline"));
        setInline0(inline);
    }

    abstract void setInline0(boolean inline);
}
