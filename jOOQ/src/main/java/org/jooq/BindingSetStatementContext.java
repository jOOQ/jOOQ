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

import java.sql.PreparedStatement;

/**
 * A container type for {@link Binding#set(BindingSetStatementContext)}
 * arguments.
 *
 * @author Lukas Eder
 */
public interface BindingSetStatementContext<U> extends Scope {

    /**
     * The {@link PreparedStatement} to which a bind variable should be bound.
     */
    PreparedStatement statement();

    /**
     * The bind variable index at which a bind variable should be bound.
     */
    int index();

    /**
     * The bind value that is being bound.
     */
    U value();

    /**
     * Create a new context from this one using a converter.
     */
    <T> BindingSetStatementContext<T> convert(Converter<? extends T, ? super U> converter);
}
