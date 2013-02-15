/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.xtend


/**
 * @author Lukas Eder
 */
import org.jooq.Constants

class InsertDSL extends Generators {
    
    def static void main(String[] args) {
        val insert = new InsertDSL();
        insert.generateInsertValuesStep();
        insert.generateInsertImpl();
    }
    
    def generateInsertValuesStep() {
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            val out = new StringBuilder();
            
            out.append('''
            «classHeader»
            package org.jooq;

            import java.util.Collection;

            import javax.annotation.Generated;

            import org.jooq.Support;
            import org.jooq.impl.Executor;
            
            /**
             * This type is used for the {@link Insert}'s DSL API.
             * <p>
             * Example: <code><pre>
             * Executor create = new Executor();
             *
             * create.insertInto(table, «field1_field2_fieldn(degree)»)
             *       .values(«XXX1_XXX2_XXXn(degree, "valueA")»)
             *       .values(«XXX1_XXX2_XXXn(degree, "valueB")»)
             *       .onDuplicateKeyUpdate()
             *       .set(field1, value1)
             *       .set(field2, value2)
             *       .execute();
             * </pre></code>
             *
             * @author Lukas Eder
             */
            «generatedAnnotation»
            public interface InsertValuesStep«degree»<R extends Record, «TN(degree)»> extends InsertOnDuplicateStep<R> {
            
                /**
                 * Add values to the insert statement.
                 */
                @Support
                InsertValuesStep«degree»<R, «TN(degree)»> values(«TN_XXXn(degree, "value")»);
            
                /**
                 * Add values to the insert statement.
                 */
                @Support
                InsertValuesStep«degree»<R, «TN(degree)»> values(«Field_TN_XXXn(degree, "value")»);

                /**
                 * Add values to the insert statement.
                 */
                @Support
                InsertValuesStep«degree»<R, «TN(degree)»> values(Collection<?> values);

                /**
                 * Use a <code>SELECT</code> statement as the source of values for the
                 * <code>INSERT</code> statement
                 * <p>
                 * This variant of the <code>INSERT .. SELECT</code> statement expects a
                 * select returning exactly as many fields as specified previously in the
                 * <code>INTO</code> clause:
                 * {@link Executor#insertInto(Table, «(1..degree).join(", ", [e | 'Field'])»)}
                 */
                @Support
                Insert<R> select(Select<? extends Record«degree»<«TN(degree)»>> select);
            }
            ''');
             
            write("org.jooq.InsertValuesStep" + degree, out);
        }
    }
    
    def generateInsertImpl() {
        val out = new StringBuilder();
        
        out.append('''
        «classHeader»
        package org.jooq.impl;
        
        import java.util.Arrays;
        import java.util.Collection;
        import java.util.List;
        import java.util.Map;
        
        import org.jooq.AttachableInternal;
        import org.jooq.Configuration;
        import org.jooq.Field;
        import org.jooq.FieldLike;
        import org.jooq.Insert;
        import org.jooq.InsertOnDuplicateSetMoreStep;
        import org.jooq.InsertQuery;
        import org.jooq.InsertResultStep;
        import org.jooq.InsertSetMoreStep;
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        import org.jooq.InsertValuesStep«degree»;
        «ENDFOR»
        import org.jooq.InsertValuesStepN;
        import org.jooq.Record;
        import org.jooq.Record1;
        import org.jooq.Result;
        import org.jooq.Select;
        import org.jooq.Table;

        /**
         * @author Lukas Eder
         */
        @SuppressWarnings({ "rawtypes", "unchecked" })        
        class InsertImpl<R extends Record, «TN(Constants::MAX_ROW_DEGREE)»>
            extends AbstractDelegatingQuery<InsertQuery<R>>
            implements
        
            // Cascading interface implementations for Insert behaviour
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
            InsertValuesStep«degree»<R, «TN(degree)»>,
            «ENDFOR»
            InsertValuesStepN<R>,
            InsertSetMoreStep<R>,
            InsertOnDuplicateSetMoreStep<R>,
            InsertResultStep<R> {
        
            /**
             * Generated UID
             */
            private static final long serialVersionUID = 4222898879771679107L;
        
            private final Field<?>[]  fields;
            private final Table<R>    into;
            private boolean           onDuplicateKeyUpdate;
        
            InsertImpl(Configuration configuration, Table<R> into, Collection<? extends Field<?>> fields) {
                super(new InsertQueryImpl<R>(configuration, into));
        
                this.into = into;
                this.fields = (fields == null || fields.size() == 0)
                    ? into.fields()
                    : fields.toArray(new Field[fields.size()]);
            }
        
            // -------------------------------------------------------------------------
            // The DSL API
            // -------------------------------------------------------------------------
        
            @Override
            public final Insert<R> select(Select select) {
                Configuration configuration = ((AttachableInternal) getDelegate()).getConfiguration();
                return new InsertSelectQueryImpl<R>(configuration, into, fields, select);
            }
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
            
            @Override
            public final InsertImpl values(«TN_XXXn(degree, "value")») {
                return values(new Object[] { «XXXn(degree, "value")» });
            }
            «ENDFOR»
        
            @Override
            public final InsertImpl values(Object... values) {
                if (fields.length != values.length) {
                    throw new IllegalArgumentException("The number of values must match the number of fields");
                }
        
                getDelegate().newRecord();
                for (int i = 0; i < fields.length; i++) {
                    addValue(getDelegate(), fields[i], values[i]);
                }
        
                return this;
            }
        
            @Override
            public final InsertImpl values(Collection<?> values) {
                return values(values.toArray());
            }
        
            private <T> void addValue(InsertQuery<R> delegate, Field<T> field, Object object) {
        
                // [#1343] Only convert non-jOOQ objects
                if (object instanceof Field) {
                    delegate.addValue(field, (Field<T>) object);
                }
                else if (object instanceof FieldLike) {
                    delegate.addValue(field, ((FieldLike) object).<T>asField());
                }
                else {
                    delegate.addValue(field, field.getDataType().convert(object));
                }
            }
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
            
            @Override
            public final InsertImpl values(«Field_TN_XXXn(degree, "value")») {
                return values(new Field[] { «XXXn(degree, "value")» });
            }
            «ENDFOR»
        
            @Override
            public final InsertImpl values(Field<?>... values) {
                List<Field<?>> values1 = Arrays.asList(values);
                if (fields.length != values1.size()) {
                    throw new IllegalArgumentException("The number of values must match the number of fields");
                }
        
                getDelegate().newRecord();
                for (int i = 0; i < fields.length; i++) {
                    // javac has trouble when inferring Object for T. Use Void instead
                    getDelegate().addValue((Field<Void>) fields[i], (Field<Void>) values1.get(i));
                }
        
                return this;
            }
        
            @Override
            public final InsertImpl onDuplicateKeyUpdate() {
                onDuplicateKeyUpdate = true;
                getDelegate().onDuplicateKeyUpdate(true);
                return this;
            }
        
            @Override
            public final InsertImpl onDuplicateKeyIgnore() {
                getDelegate().onDuplicateKeyIgnore(true);
                return this;
            }
        
            @Override
            public final <T> InsertImpl set(Field<T> field, T value) {
                if (onDuplicateKeyUpdate) {
                    getDelegate().addValueForUpdate(field, value);
                }
                else {
                    getDelegate().addValue(field, value);
                }
        
                return this;
            }
        
            @Override
            public final <T> InsertImpl set(Field<T> field, Field<T> value) {
                if (onDuplicateKeyUpdate) {
                    getDelegate().addValueForUpdate(field, value);
                }
                else {
                    getDelegate().addValue(field, value);
                }
        
                return this;
            }
        
            @Override
            public final <T> InsertImpl set(Field<T> field, Select<? extends Record1<T>> value) {
                return set(field, value.<T>asField());
            }

            @Override
            public final InsertImpl set(Map<? extends Field<?>, ?> map) {
                if (onDuplicateKeyUpdate) {
                    getDelegate().addValuesForUpdate(map);
                }
                else {
                    getDelegate().addValues(map);
                }
        
                return this;
            }

            @Override
            public final InsertImpl set(Record record) {
            	return set(Utils.map(record));
            }

            @Override
            public final InsertImpl newRecord() {
                getDelegate().newRecord();
                return this;
            }
        
            @Override
            public final InsertImpl returning() {
                getDelegate().setReturning();
                return this;
            }
        
            @Override
            public final InsertImpl returning(Field<?>... f) {
                getDelegate().setReturning(f);
                return this;
            }
        
            @Override
            public final InsertImpl returning(Collection<? extends Field<?>> f) {
                getDelegate().setReturning(f);
                return this;
            }
        
            @Override
            public final Result<R> fetch() {
                getDelegate().execute();
                return getDelegate().getReturnedRecords();
            }
        
            @Override
            public final R fetchOne() {
                getDelegate().execute();
                return getDelegate().getReturnedRecord();
            }
        }
        ''');
        
        write("org.jooq.impl.InsertImpl", out);
    }
}