/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.test._.testcases;

import java.sql.Date;

import org.jooq.Loader;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.jOOQAbstractTest;

/**
 * @author Johannes Buehler
 * @author Lukas Eder
 */
public class CsvLoaderTests<
    A extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
    AP,
    B extends UpdatableRecord<B>,
    S extends UpdatableRecord<S> & Record1<String>,
    B2S extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
    BS extends UpdatableRecord<BS>,
    L extends TableRecord<L> & Record2<String, String>,
    X extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D extends UpdatableRecord<D>,
    T extends UpdatableRecord<T>,
    U extends TableRecord<U>,
    UU extends UpdatableRecord<UU>,
    I extends TableRecord<I>,
    IPK extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends AbstractLoaderTests<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> {

    public CsvLoaderTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    @Override
    protected Loader<A> createLoaderWithRollbackOnDuplicateKeys() throws java.io.IOException {
        Loader<A> loader;
        String csv = "\"ID\",\"First Qualifier\",\"Last Qualifier\"\r" +
                "8,Hermann,Hesse\n" +
                "1,\"Max\",Frisch\n" +
                "2,Friedrich,Dürrenmatt";
        loader =
        create().loadInto(TAuthor())
                .commitAll()
                .onDuplicateKeyError()
                .onErrorAbort()
                .loadCSV(csv)
                .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
                .execute();
        return loader;
    }

    @Override
    protected Loader<A> createLoaderUpdatingDuplicateRecords() throws java.io.IOException {
        Loader<A> loader;
        String csv = "\"ID\",\"First Qualifier\",\"Last Qualifier\"\r" +
                "1,Hermann,Hesse\n" +
                "7,\"Max\",Frisch";
        loader =
        create().loadInto(TAuthor())
                .onDuplicateKeyUpdate()
                .loadCSV(csv)
                .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
                .execute();
        return loader;
    }

    @Override
    protected Loader<A> createLoaderButDontLoadAllColumns() throws java.io.IOException {
        Loader<A> loader;
        String csv = "\"ID\",ignore,\"First Qualifier\",\"Last Qualifier\"\r" +
                "5,asdf,{null},Hesse\n" +
                "6,asdf,\"\",Frisch";
        loader =
        create().loadInto(TAuthor())
                .loadCSV(csv)
                .fields(TAuthor_ID(), null, TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
                .nullString("{null}")
                .execute();
        return loader;
    }

    @Override
    protected Loader<A> createLoaderWithDifferentNulls() throws java.io.IOException {

        String csv = "####Some Data####\n" +
                "\"ID\",\"Last Qualifier\"\r" +
                "3,\"\",Hesse\n" +
                "4,,Frisch";
        Loader<A> execute =
        create().loadInto(TAuthor())
                .loadCSV(csv)
                .fields(TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
                .quote('"')
                .separator(',')
                .ignoreRows(2)
                .execute();
        return execute;
    }

    @Override
    protected Loader<A> createLoaderIgnoringDuplicateRecords() throws java.io.IOException {
        Loader<A> loader;
        String csv = "1,\"Kafka\"\n" +
                "2,Frisch";
        loader =
        create().loadInto(TAuthor())
                .onDuplicateKeyIgnore()
                .onErrorAbort()
                .loadCSV(
                        csv)
                .fields(TAuthor_ID(), TAuthor_LAST_NAME())
                .ignoreRows(0)
                .execute();

        return loader;
    }

    @Override
    protected Loader<A> createLoaderAbortingOnDuplicateRecords() throws java.io.IOException {
        String csv = "1;'Kafka'\n" +
                "2;Frisch";

        Loader<A> loader =
        create().loadInto(TAuthor())
                .onDuplicateKeyError()
                .onErrorAbort()
                .loadCSV(csv)
                .fields(TAuthor_ID(), TAuthor_LAST_NAME())
                .quote('\'')
                .separator(';')
                .ignoreRows(0)
                .execute();

        return loader;
    }

    @Override
    protected Loader<A> createLoaderIgnoringConstraintViolationOnLAST_NAME() throws java.io.IOException {
        Loader<A> loader;
        String csv = "3\n" +
                "4";
        loader =
        create().loadInto(TAuthor())
                .onErrorIgnore()
                .loadCSV(
                        csv)
                .fields(TAuthor_ID())
                .ignoreRows(0)
                .execute();
        return loader;
    }

    @Override
    protected Loader<A> createLoaderCommittingAndIgnoringDuplicates() throws java.io.IOException {
        Loader<A> loader;

        String csv = "\"ID\",\"First Qualifier\",\"Last Qualifier\"\r" +
                "8,Hermann,Hesse\n" +
                "1,\"Max\",Frisch\n" +
                "2,Friedrich,Dürrenmatt";
        loader =
        create().loadInto(TAuthor())
                .commitAll()
                .onDuplicateKeyIgnore()
                .onErrorAbort()
                .loadCSV(
                        csv)
                .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
                .execute();
        return loader;
    }

    @Override
    protected Loader<A> createLoaderAbortingOnConstraintViolationOnLAST_NAME() throws java.io.IOException {
        String csv = "3\n" +
                "4";
        Loader<A> execute =
        create().loadInto(TAuthor())
                .loadCSV(csv)
                .fields(TAuthor_ID())
                .ignoreRows(0)
                .execute();

        return execute;
    }

    @Override
    protected Loader<A> createForEmptyFile() throws java.io.IOException {
        return
        create().loadInto(TAuthor())
                .loadCSV("")
                .fields(TAuthor_ID())
                .execute();
    }
}
