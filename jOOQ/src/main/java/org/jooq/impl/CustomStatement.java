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
package org.jooq.impl;

import java.util.function.Consumer;

import org.jooq.Context;
// ...
import org.jooq.Statement;
import org.jooq.impl.QOM.UEmptyStatement;
import org.jooq.impl.QOM.UOpaque;

/**
 * A base class for custom {@link Statement} implementations in client code.
 * <p>
 * Client code may provide proper {@link Statement} implementations extending
 * this useful base class. All necessary parts of the {@link Statement}
 * interface are already implemented. Only this method needs further
 * implementation: {@link #accept(Context)}.
 * <p>
 * Refer to that methods' Javadoc for further details about their expected
 * behaviour.
 *
 * @author Lukas Eder
 */
@Pro
public abstract class CustomStatement
extends
    AbstractStatement
implements
    UEmptyStatement,
    UOpaque
{
    protected CustomStatement() {}

    /**
     * Create a {@link CustomStatement} from a lambda expression.
     */
    public static final CustomStatement of(Consumer<? super Context<?>> consumer) {
        return new CustomStatement() {
            @Override
            public void accept(Context<?> ctx) {
                consumer.accept(ctx);
            }
        };
    }

    // -------------------------------------------------------------------------
    // Implementation required
    // -------------------------------------------------------------------------

    /**
     * Subclasses must implement this method.
     * <hr>
     * {@inheritDoc}
     */
    @Override
    public abstract void accept(Context<?> ctx);

    // -------------------------------------------------------------------------
    // No further overrides allowed
    // -------------------------------------------------------------------------

    @Override
    public final boolean declaresFields() {
        return super.declaresFields();
    }

    @Override
    public final boolean declaresTables() {
        return super.declaresTables();
    }
}

/* [/pro] */