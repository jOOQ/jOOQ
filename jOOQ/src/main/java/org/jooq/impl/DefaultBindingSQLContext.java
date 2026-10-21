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

import static org.jooq.ContextConverter.scoped;

import java.util.Map;

import org.jooq.BindingSQLContext;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.ConverterContext;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class DefaultBindingSQLContext<U> extends AbstractScope implements BindingSQLContext<U> {

    private final RenderContext render;
    private final Lazy<U>       value;
    private final String        variable;

    DefaultBindingSQLContext(Configuration configuration, Map<Object, Object> data, RenderContext render, U value) {
        this(configuration, data, render, value, "?");
    }

    DefaultBindingSQLContext(Configuration configuration, Map<Object, Object> data, RenderContext render, U value, String variable) {
        this(configuration, data, render, Lazy.of(() -> value), variable);
    }

    private DefaultBindingSQLContext(Configuration configuration, Map<Object, Object> data, RenderContext render, Lazy<U> value, String variable) {
        super(configuration, data);

        this.render = render;
        this.value = value;
        this.variable = variable;
    }

    @Override
    public final ConverterContext converterContext() {
        return render.converterContext();
    }

    @Override
    public final RenderContext render() {
        return render;
    }

    @Override
    public final U value() {
        return value.get();
    }

    @Override
    public final String variable() {
        return variable;
    }

    @Override
    public <T> BindingSQLContext<T> convert(Converter<? extends T, ? super U> converter) {

        // [#10464] Convert lazily. Bindings that only render a bind variable
        //          placeholder never access the value, so they shouldn't
        //          trigger a potentially expensive conversion that will be
        //          repeated anyway when binding the value.
        return new DefaultBindingSQLContext<>(configuration, data, render, Lazy.of(() -> scoped(converter).to(value.get(), converterContext())), variable);
    }

    @Override
    public String toString() {
        return "DefaultBindingSQLContext [value=" + value + ", variable=" + variable + "]";
    }
}