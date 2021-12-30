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

import static org.jooq.impl.Keywords.K_GROUPING_SETS;
import static org.jooq.impl.Names.N_GROUPING_SETS;
import static org.jooq.impl.SQLDataType.OTHER;
import static org.jooq.impl.Tools.EMPTY_COLLECTION;

import java.util.Collection;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.FieldOrRow;
import org.jooq.Function1;
import org.jooq.GroupField;
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * @author Lukas Eder
 */
final class GroupingSets extends AbstractField<Object> implements QOM.GroupingSets {

    private final QueryPartList<QueryPartList<Field<?>>> fieldSets;

    @SafeVarargs
    GroupingSets(Collection<? extends Field<?>>... fieldSets) {
        super(N_GROUPING_SETS, OTHER);

        this.fieldSets = new QueryPartList<>();

        for (Collection<? extends Field<?>> fieldSet : fieldSets)
            this.fieldSets.add(new QueryPartList<>(fieldSet));
    }

    @Override
    public final void accept(Context<?> ctx) {
        QueryPartList<WrappedList> arg = new QueryPartList<>();

        for (Collection<? extends Field<?>> fieldsSet : fieldSets)
            arg.add(new WrappedList(new QueryPartList<>(fieldsSet)));

        ctx.visit(K_GROUPING_SETS).sql('(').visit(arg).sql(')');
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final UnmodifiableList<? extends UnmodifiableList<? extends FieldOrRow>> $arg1() {
        return QOM.unmodifiable(fieldSets);
    }

    @Override
    public final Function1<? super UnmodifiableList<? extends UnmodifiableList<? extends FieldOrRow>>, ? extends GroupField> constructor() {
        return l -> new GroupingSets((Collection[]) l.toArray(EMPTY_COLLECTION));
    }
}
