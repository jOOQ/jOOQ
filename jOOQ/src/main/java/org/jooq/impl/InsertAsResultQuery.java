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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import java.util.Collection;

import org.jooq.DeleteResultStep;
import org.jooq.InsertResultStep;
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
import org.jooq.ResultQuery;
import org.jooq.SelectFieldOrAsterisk;
// ...
import org.jooq.Update;
import org.jooq.impl.QOM.Insert;
import org.jooq.impl.QOM.InsertReturning;
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * A wrapped DML query ({@link Insert}, {@link Update}, {@link Delete}) that
 * works like a {@link ResultQuery}.
 *
 * @author Lukas Eder
 */
final class InsertAsResultQuery<R extends Record>
extends
    AbstractDMLQueryAsResultQuery<R, InsertQueryImpl<R>>
implements
    InsertResultStep<R>,
    QOM.InsertReturning<R>
{

    InsertAsResultQuery(InsertQueryImpl<R> delegate, boolean returningResult) {
        super(delegate, returningResult);
    }

    @Override
    public final Insert<?> $insert() {
        return getDelegate();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final InsertReturning<R> $insert(Insert<?> newInsert) {
        return new InsertAsResultQuery(Tools.insertQueryImpl(newInsert).copy(i -> i.setReturning($returning())), returningResult);
    }

    @Override
    public final UnmodifiableList<? extends SelectFieldOrAsterisk> $returning() {
        return QOM.unmodifiable(getDelegate().returning);
    }

    @Override
    public final InsertReturning<?> $returning(Collection<? extends SelectFieldOrAsterisk> returning) {
        return new InsertAsResultQuery<>(getDelegate().copy(i -> i.setReturning(returning)), returningResult);
    }


























}