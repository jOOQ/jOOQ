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

import java.util.Map;

import org.jooq.BindingSQLContext;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class DefaultBindingSQLContext<U> extends AbstractScope implements BindingSQLContext<U> {

    private final RenderContext render;
    private final U value;
    private final String variable;

    DefaultBindingSQLContext(Configuration configuration, Map<Object, Object> data, RenderContext render, U value) {
        this(configuration, data, render, value, "?");
    }

    DefaultBindingSQLContext(Configuration configuration, Map<Object, Object> data, RenderContext render, U value, String variable) {
        super(configuration, data);

        this.render = render;
        this.value = value;
        this.variable = variable;
    }

    @Override
    public final RenderContext render() {
        return render;
    }

    @Override
    public final U value() {
        return value;
    }

    @Override
    public final String variable() {
        return variable;
    }

    @Override
    public <T> BindingSQLContext<T> convert(Converter<? extends T, ? super U> converter) {
        return new DefaultBindingSQLContext<T>(configuration, data, render, converter.to(value), variable);
    }

    @Override
    public String toString() {
        return "DefaultBindingSQLContext [value=" + value + ", variable=" + variable + "]";
    }
}