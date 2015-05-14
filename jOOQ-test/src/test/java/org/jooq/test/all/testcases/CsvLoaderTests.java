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
import static org.jooq.lambda.tuple.Tuple.tuple;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.Loader;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.lambda.tuple.Tuple4;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.test.all.converters.Boolean_10;
import org.jooq.test.all.converters.Boolean_TF_LC;

/**
 * @author Johannes Buehler
 * @author Lukas Eder
 */
public class CsvLoaderTests<
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

    public CsvLoaderTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testCsvLoaderBulkAll() throws Exception {
        jOOQAbstractTest.reset = false;

        Loader<A> loader;
        String csv = "ID,First Qualifier,Last Qualifier\r" +
                "8,Hermann,Hesse\n" +
                "9,Max,Frisch\n" +
                "10,Friedrich,Dürrenmatt";

        List<Tuple4<Integer, Integer, Integer, Integer>> events = new ArrayList<>();

        BatchListener listener;
        DSLContext create;

        listener = new BatchListener();
        create = create(listener);

        loader =
        create.loadInto(TAuthor())
              .bulkAll()
              .loadCSV(csv)
              .fields(TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
              .onRow(ctx -> events.add(tuple(
                  ctx.executed(),
                  ctx.ignored(),
                  ctx.processed(),
                  ctx.stored()
              )))
              .execute();

        assertEquals(3, events.size());
        assertEquals(asList(
            tuple(0, 0, 1, 0),
            tuple(0, 0, 2, 0),
            tuple(0, 0, 3, 0)
        ), events);

        assertEquals(1, loader.executed());
        assertEquals(3, loader.processed());
        assertEquals(3, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(0, loader.errors().size());
        assertEquals(1, listener.batchCommands.size());
        assertEquals(1, listener.batchCommands.get(0));

        Result<A> result =
        create.selectFrom(TAuthor())
              .where(TAuthor_ID().ge(8))
              .orderBy(TAuthor_ID())
              .fetch();

        assertEquals(3, result.size());
        assertEquals(asList(8, 9, 10), result.getValues(TAuthor_ID()));
        assertEquals(asList("Hermann", "Max", "Friedrich"), result.getValues(TAuthor_FIRST_NAME()));
        assertEquals(asList("Hesse", "Frisch", "Dürrenmatt"), result.getValues(TAuthor_LAST_NAME()));
    }

    public void testCsvLoaderBulkAfter() throws Exception {
        jOOQAbstractTest.reset = false;

        Loader<A> loader;
        String csv = "ID,First Qualifier,Last Qualifier\r" +
                "8,Hermann,Hesse\n" +
                "9,Max,Frisch\n" +
                "10,Friedrich,Dürrenmatt";

        List<Tuple4<Integer, Integer, Integer, Integer>> events = new ArrayList<>();

        BatchListener listener;
        DSLContext create;

        listener = new BatchListener();
        create = create(listener);

        loader =
        create.loadInto(TAuthor())
              .bulkAfter(2)
              .loadCSV(csv)
              .fields(TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
              .onRow(ctx -> events.add(tuple(
                  ctx.executed(),
                  ctx.ignored(),
                  ctx.processed(),
                  ctx.stored()
              )))
              .execute();

        assertEquals(3, events.size());
        assertEquals(asList(
            tuple(0, 0, 1, 0),
            tuple(1, 0, 2, 2),
            tuple(1, 0, 3, 2)
        ), events);

        assertEquals(2, loader.executed());
        assertEquals(3, loader.processed());
        assertEquals(3, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(0, loader.errors().size());
        assertEquals(2, listener.batchCommands.size());
        assertEquals(1, listener.batchCommands.get(0));
        assertEquals(1, listener.batchCommands.get(1));

        Result<A> result =
        create.selectFrom(TAuthor())
              .where(TAuthor_ID().ge(8))
              .orderBy(TAuthor_ID())
              .fetch();

        assertEquals(3, result.size());
        assertEquals(asList(8, 9, 10), result.getValues(TAuthor_ID()));
        assertEquals(asList("Hermann", "Max", "Friedrich"), result.getValues(TAuthor_FIRST_NAME()));
        assertEquals(asList("Hesse", "Frisch", "Dürrenmatt"), result.getValues(TAuthor_LAST_NAME()));
    }

    public void testCsvLoaderBatchAll() throws Exception {
        jOOQAbstractTest.reset = false;

        Loader<A> loader;
        String csv = "ID,First Qualifier,Last Qualifier\r" +
                "8,Hermann,Hesse\n" +
                "9,Max,Frisch\n" +
                "10,Friedrich,Dürrenmatt";

        List<Tuple4<Integer, Integer, Integer, Integer>> events = new ArrayList<>();

        BatchListener listener;
        DSLContext create;

        listener = new BatchListener();
        create = create(listener);

        loader =
        create.loadInto(TAuthor())
              .batchAll()
              .loadCSV(csv)
              .fields(TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
              .onRow(ctx -> events.add(tuple(
                  ctx.executed(),
                  ctx.ignored(),
                  ctx.processed(),
                  ctx.stored()
              )))
              .execute();

        assertEquals(3, events.size());
        assertEquals(asList(
            tuple(0, 0, 1, 0),
            tuple(0, 0, 2, 0),
            tuple(0, 0, 3, 0)
        ), events);


        assertEquals(1, loader.executed());
        assertEquals(3, loader.processed());
        assertEquals(3, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(0, loader.errors().size());
        assertEquals(1, listener.batchCommands.size());
        assertEquals(1, listener.batchCommands.get(0));

        Result<A> result =
        create.selectFrom(TAuthor())
              .where(TAuthor_ID().ge(8))
              .orderBy(TAuthor_ID())
              .fetch();

        assertEquals(3, result.size());
        assertEquals(asList(8, 9, 10), result.getValues(TAuthor_ID()));
        assertEquals(asList("Hermann", "Max", "Friedrich"), result.getValues(TAuthor_FIRST_NAME()));
        assertEquals(asList("Hesse", "Frisch", "Dürrenmatt"), result.getValues(TAuthor_LAST_NAME()));
    }

    public void testCsvLoaderBatchAfter() throws Exception {
        jOOQAbstractTest.reset = false;

        Loader<A> loader;
        String csv = "ID,First Qualifier,Last Qualifier\r" +
                "8,Hermann,Hesse\n" +
                "9,Max,Frisch\n" +
                "10,Friedrich,Dürrenmatt";

        List<Tuple4<Integer, Integer, Integer, Integer>> events = new ArrayList<>();

        BatchListener listener;
        DSLContext create;

        listener = new BatchListener();
        create = create(listener);

        loader =
        create.loadInto(TAuthor())
              .batchAfter(2)
              .loadCSV(csv)
              .fields(TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
              .onRow(ctx -> events.add(tuple(
                  ctx.executed(),
                  ctx.ignored(),
                  ctx.processed(),
                  ctx.stored()
              )))
              .execute();

        assertEquals(3, events.size());
        assertEquals(asList(
            tuple(0, 0, 1, 0),
            tuple(1, 0, 2, 2),
            tuple(1, 0, 3, 2)
        ), events);

        assertEquals(2, loader.executed());
        assertEquals(3, loader.processed());
        assertEquals(3, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(0, loader.errors().size());
        assertEquals(2, listener.batchCommands.size());
        assertEquals(1, listener.batchCommands.get(0));
        assertEquals(1, listener.batchCommands.get(1));

        Result<A> result =
        create.selectFrom(TAuthor())
              .where(TAuthor_ID().ge(8))
              .orderBy(TAuthor_ID())
              .fetch();

        assertEquals(3, result.size());
        assertEquals(asList(8, 9, 10), result.getValues(TAuthor_ID()));
        assertEquals(asList("Hermann", "Max", "Friedrich"), result.getValues(TAuthor_FIRST_NAME()));
        assertEquals(asList("Hesse", "Frisch", "Dürrenmatt"), result.getValues(TAuthor_LAST_NAME()));
    }

    public void testCsvLoaderBatchAndBulk() throws Exception {
        jOOQAbstractTest.reset = false;

        Loader<A> loader;
        String csv = "ID,First Qualifier,Last Qualifier\r" +
                "8,A,A\n" +
                "9,B,B\n" +
                "10,C,C\n" +
                "11,D,D\n" +
                "12,E,E";

        List<Tuple4<Integer, Integer, Integer, Integer>> events = new ArrayList<>();

        BatchListener listener;
        DSLContext create;

        listener = new BatchListener();
        create = create(listener);

        loader =
        create.loadInto(TAuthor())
              .batchAfter(2)
              .bulkAfter(2)
              .loadCSV(csv)
              .fields(TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
              .onRow(ctx -> events.add(tuple(
                  ctx.executed(),
                  ctx.ignored(),
                  ctx.processed(),
                  ctx.stored()
              )))
              .execute();

        assertEquals(5, events.size());
        assertEquals(asList(
            tuple(0, 0, 1, 0),
            tuple(0, 0, 2, 0),
            tuple(0, 0, 3, 0),
            tuple(1, 0, 4, 4),
            tuple(1, 0, 5, 4)
        ), events);

        assertEquals(2, loader.executed());
        assertEquals(5, loader.processed());
        assertEquals(5, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(0, loader.errors().size());
        assertEquals(2, listener.batchCommands.size());
        assertEquals(1, listener.batchCommands.get(0));
        assertEquals(1, listener.batchCommands.get(1));

        Result<A> result =
        create.selectFrom(TAuthor())
              .where(TAuthor_ID().ge(8))
              .orderBy(TAuthor_ID())
              .fetch();

        assertEquals(5, result.size());
        assertEquals(asList(8, 9, 10, 11, 12), result.getValues(TAuthor_ID()));
        assertEquals(asList("A", "B", "C", "D", "E"), result.getValues(TAuthor_FIRST_NAME()));
        assertEquals(asList("A", "B", "C", "D", "E"), result.getValues(TAuthor_LAST_NAME()));
    }

    @SuppressWarnings("serial")
    static class BatchListener extends DefaultExecuteListener {

        List<Integer> batchCommands = new ArrayList<>();

        @Override
        public void executeEnd(ExecuteContext ctx) {
            super.executeEnd(ctx);

            batchCommands.add(ctx.batchRows().length);
        }
    }

    public void testLoaderConverter() throws Exception {
        jOOQAbstractTest.reset = false;

        create().transaction(c -> {
            String csv =
                "1,0,false\n" +
                "2,1,true";

            Loader<BOOL> loader =
            DSL.using(c)
               .loadInto(TBooleans())
               .loadCSV(csv)
               .fields(TBooleans_ID(), TBooleans_BOOLEAN_10(), TBooleans_Boolean_TF_LC())
               .ignoreRows(0)
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
        Loader<A> loader;
        String csv = "\"ID\",\"First Qualifier\",Last Qualifier\r" +
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
