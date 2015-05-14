/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.test.all.testcases;

import static java.util.Arrays.asList;

import java.sql.Date;

import org.jooq.Loader;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DSL;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.test.all.converters.Boolean_10;
import org.jooq.test.all.converters.Boolean_TF_LC;

/**
 * @author Johannes Buehler
 * @author Lukas Eder
 */
public class RowLoaderTests<
    A    extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S> & Record1<String>,
    B2S  extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L> & Record2<String, String>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    UU   extends UpdatableRecord<UU>,
    CS   extends UpdatableRecord<CS>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends AbstractLoaderTests<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> {

    public RowLoaderTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testLoaderConverter() throws Exception {
        jOOQAbstractTest.reset = false;

        create().transaction(c -> {
            Loader<BOOL> loader =
            DSL.using(c)
               .loadInto(TBooleans())
               .loadArrays(
                   new Object[] { 1, 0, false },
                   new Object[] { 2, 1, true }
               )
               .fields(TBooleans_ID(), TBooleans_BOOLEAN_10(), TBooleans_Boolean_TF_LC())
               .execute();

            assertEquals(0, loader.errors().size());
            assertEquals(2, loader.stored());

            Result<Record3<Integer, Boolean_10, Boolean_TF_LC>> result =
            DSL.using(c)
               .select(TBooleans_ID(), TBooleans_BOOLEAN_10(), TBooleans_Boolean_TF_LC())
               .from(TBooleans())
               .orderBy(TBooleans_ID())
               .fetch();

            assertEquals(2, result.size());
            assertEquals(asList(1, 2), result.getValues(TBooleans_ID()));
            assertEquals(asList(Boolean_10.ZERO, Boolean_10.ONE), result.getValues(TBooleans_BOOLEAN_10()));
            assertEquals(asList(Boolean_TF_LC.FALSE, Boolean_TF_LC.TRUE), result.getValues(TBooleans_Boolean_TF_LC()));
        });
    }

    @Override
    protected Loader<A> createLoaderWithRollbackOnDuplicateKeys() throws java.io.IOException {
        return
        create().loadInto(TAuthor())
                .commitAll()
                .onDuplicateKeyError()
                .onErrorAbort()
                .loadArrays(
                    new Object[] { 8, "Hermann", "Hesse" },
                    new Object[] { 1, "Max", "Frisch" },
                    new Object[] { 2, "Friedrich", "Dürrenmatt" }
                )
                .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
                .execute();
    }

    @Override
    protected Loader<A> createLoaderUpdatingDuplicateRecords() throws java.io.IOException {
        return
        create().loadInto(TAuthor())
                .onDuplicateKeyUpdate()
                .loadArrays(
                    new Object[] { 1, "Hermann", "Hesse" },
                    new Object[] { 7, "Max", "Frisch" }
                )
                .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
                .execute();
    }

    @Override
    protected Loader<A> createLoaderButDontLoadAllColumns() throws java.io.IOException {
        return
        create().loadInto(TAuthor())
                .loadArrays(
                    new Object[] { 5, "asdf", null, "Hesse" },
                    new Object[] { 6, "asdf", "", "Frisch" }
                )
                .fields(TAuthor_ID(), null, TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
                .execute();
    }

    @Override
    protected Loader<A> createLoaderWithDifferentNulls() throws java.io.IOException {
        return
        create().loadInto(TAuthor())
                .loadArrays(
                    new Object[] { 3, "", "Hesse" },
                    new Object[] { 4, "", "Frisch" }
                )
                .fields(TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
                .execute();
    }

    @Override
    protected Loader<A> createLoaderIgnoringDuplicateRecords() throws java.io.IOException {
        return
        create().loadInto(TAuthor())
                .onDuplicateKeyIgnore()
                .onErrorAbort()
                .loadArrays(
                    new Object[] { 1, "Kafka" },
                    new Object[] { 2, "Frisch" }
                )
                .fields(TAuthor_ID(), TAuthor_LAST_NAME())
                .execute();
    }

    @Override
    protected Loader<A> createLoaderAbortingOnDuplicateRecords() throws java.io.IOException {
        return
        create().loadInto(TAuthor())
                .onDuplicateKeyError()
                .onErrorAbort()
                .loadArrays(
                    new Object[] { 1, "Kafka" },
                    new Object[] { 2, "Frisch" }
                )
                .fields(TAuthor_ID(), TAuthor_LAST_NAME())
                .execute();
    }

    @Override
    protected Loader<A> createLoaderIgnoringConstraintViolationOnLAST_NAME() throws java.io.IOException {
        return
        create().loadInto(TAuthor())
                .onErrorIgnore()
                .loadArrays(
                    new Object[] { 3 },
                    new Object[] { 4 }
                )
                .fields(TAuthor_ID())
                .execute();
    }

    @Override
    protected Loader<A> createLoaderCommittingAndIgnoringDuplicates() throws java.io.IOException {
        return
        create().loadInto(TAuthor())
                .commitAll()
                .onDuplicateKeyIgnore()
                .onErrorAbort()
                .loadArrays(
                    new Object[] { 8, "Hermann", "Hesse" },
                    new Object[] { 1, "Max", "Frisch" },
                    new Object[] { 2, "Friedrich", "Dürrenmatt" }
                )
                .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
                .execute();
    }

    @Override
    protected Loader<A> createLoaderAbortingOnConstraintViolationOnLAST_NAME() throws java.io.IOException {
        return
        create().loadInto(TAuthor())
                .loadArrays(
                    new Object[] { 3 },
                    new Object[] { 4 }
                )
                .fields(TAuthor_ID())
                .execute();
    }

    @Override
    protected Loader<A> createForEmptyFile() throws java.io.IOException {
        return
        create().loadInto(TAuthor())
                .loadArrays()
                .fields(TAuthor_ID())
                .execute();
    }
}
