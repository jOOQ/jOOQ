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
package org.jooq.impl;

import static org.jooq.Clause.CUSTOM;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;

/**
 * A base class for custom {@link Field} implementations in client code.
 * <p>
 * Client code may provide proper {@link Condition} implementations extending
 * this useful base class. All necessary parts of the {@link Condition}
 * interface are already implemented. Only this method needs further
 * implementation: {@link #accept(Context)}.
 * <p>
 * Refer to that methods' Javadoc for further details about their expected
 * behaviour.
 *
 * @author Lukas Eder
 */
public abstract class CustomField<T> extends AbstractField<T> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -1778024624798672262L;
    private static final Clause[] CLAUSES          = { CUSTOM };

    protected CustomField(String name, DataType<T> type) {
        super(name, type);
    }

    // -------------------------------------------------------------------------
    // Implementation required
    // -------------------------------------------------------------------------

    /**
     * Subclasses must implement this method.
     * <hr/>
     * {@inheritDoc}
     */
    @Override
    public abstract void accept(Context<?> ctx);

    // -------------------------------------------------------------------------
    // No further overrides allowed
    // -------------------------------------------------------------------------

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final Field<T> as(String alias) {
        return super.as(alias);
    }

    @Override
    public final Field<T> add(Field<?> value) {
        return super.add(value);
    }

    @Override
    public final Field<T> mul(Field<? extends Number> value) {
        return super.mul(value);
    }

    @Override
    public final boolean declaresFields() {
        return super.declaresFields();
    }

    @Override
    public final boolean declaresTables() {
        return super.declaresTables();
    }
}
