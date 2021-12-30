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

import static org.jooq.impl.Names.N_CUBE;
import static org.jooq.impl.SQLDataType.OTHER;
import static org.jooq.impl.Tools.EMPTY_FIELD_OR_ROW;

import org.jooq.Context;
import org.jooq.FieldOrRow;
import org.jooq.Function1;
import org.jooq.GroupField;
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * @author Lukas Eder
 */
final class Cube extends AbstractField<Object> implements QOM.Cube {

    private QueryPartList<FieldOrRow> arguments;

    Cube(FieldOrRow... arguments) {
        super(N_CUBE, OTHER);

        this.arguments = new QueryPartList<>(arguments);
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(N_CUBE).sql('(').visit(arguments).sql(')');
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final UnmodifiableList<? extends FieldOrRow> $arg1() {
        return QOM.unmodifiable(arguments);
    }

    @Override
    public final Function1<? super UnmodifiableList<? extends FieldOrRow>, ? extends GroupField> constructor() {
        return l -> new Cube(l.toArray(EMPTY_FIELD_OR_ROW));
    }
}
