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

import static org.jooq.impl.Names.N_XMLFOREST;

import java.util.Collection;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.XML;

/**
 * @author Lukas Eder
 */
final class XMLForest extends AbstractField<XML> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = 4505809303211506197L;
    private final SelectFieldList<Field<?>> args;

    XMLForest(Collection<? extends Field<?>> args) {
        super(N_XMLFOREST, SQLDataType.XML);

        this.args = new SelectFieldList<>(args);
    }

    @Override
    public final void accept(Context<?> ctx) {
        boolean declareFields = ctx.declareFields();

        ctx.visit(N_XMLFOREST).sql('(')
           .declareFields(true)
           .visit(args)
           .declareFields(declareFields)
           .sql(')');
    }
}
