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
package org.jooq.xtend


/**
 * @author Lukas Eder
 */

import org.jooq.Constants

class InsertDSL extends Generators {
    
    def static void main(String[] args) {
        val insert = new InsertDSL();
        insert.generateInsertSetStep();
        insert.generateInsertValuesStep();
        insert.generateInsertImpl();
    }
    
    def generateInsertSetStep() {
        val out = new StringBuilder();

        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                /**
                 * Set the columns for insert.
                 */
                «generatedAnnotation»
                @Support
                <«TN(degree)»> InsertValuesStep«degree»<R, «TN(degree)»> columns(«Field_TN_fieldn(degree)»);
            ''');
        }

        insert("org.jooq.InsertSetStep", out, "columns");
    }
    
    def generateInsertValuesStep() {
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            val out = new StringBuilder();
            
            out.append('''
            «classHeader»
            package org.jooq;

            import java.util.Collection;

            import javax.annotation.Generated;

            /**
             * This type is used for the {@link Insert}'s DSL API.
             * <p>
             * Example: <code><pre>
             * using(configuration)
             *       .insertInto(table, «field1_field2_fieldn(degree)»)
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
                 * {@link DSLContext#insertInto(Table, «(1..degree).join(", ", [e | 'Field'])»)}
                 */
                @Support
                InsertReturningStep<R> select(Select<? extends Record«degree»<«TN(degree)»>> select);
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
        import java.util.Optional;
        
        import javax.annotation.Generated;
        
        import org.jooq.AttachableInternal;
        import org.jooq.Configuration;
        import org.jooq.Field;
        import org.jooq.FieldLike;
        import org.jooq.Insert;
        import org.jooq.InsertOnDuplicateSetMoreStep;
        import org.jooq.InsertQuery;
        import org.jooq.InsertResultStep;
        import org.jooq.InsertSetMoreStep;
        import org.jooq.InsertSetStep;
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
        «generatedAnnotation»
        class InsertImpl<R extends Record, «TN(Constants::MAX_ROW_DEGREE)»>
            extends AbstractDelegatingQuery<InsertQuery<R>>
            implements
        
            // Cascading interface implementations for Insert behaviour
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
            InsertValuesStep«degree»<R, «TN(degree)»>,
            «ENDFOR»
            InsertValuesStepN<R>,
            InsertSetStep<R>,
            InsertSetMoreStep<R>,
            InsertOnDuplicateSetMoreStep<R>,
            InsertResultStep<R> {
        
            /**
             * Generated UID
             */
            private static final long serialVersionUID = 4222898879771679107L;
        
            private final Table<R>    into;
            private Field<?>[]        fields;
            private boolean           onDuplicateKeyUpdate;
        
            InsertImpl(Configuration configuration, Table<R> into, Collection<? extends Field<?>> fields) {
                super(new InsertQueryImpl<R>(configuration, into));
        
                this.into = into;
                columns(fields);
                this.fields = (fields == null || fields.size() == 0)
                    ? into.fields()
                    : fields.toArray(new Field[fields.size()]);
            }
        
            // -------------------------------------------------------------------------
            // The DSL API
            // -------------------------------------------------------------------------
        
            @Override
            public final InsertImpl select(Select select) {
                getDelegate().setSelect(select);
                return this;
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
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
            
            @Override
            @SuppressWarnings("hiding")
            public final <«TN(degree)»> InsertImpl columns(«Field_TN_fieldn(degree)») {
                return columns(new Field[] { «fieldn(degree)» });
            }
            «ENDFOR»
        
            @Override
            public final InsertImpl columns(Field<?>... f) {
                this.fields = (f == null || f.length == 0) ? into.fields() : f;
                return this;
            }
        
            @Override
            public final InsertImpl columns(Collection<? extends Field<?>> f) {
                return columns(f.toArray(new Field[f.size()]));
            }

            /**
             * Add an empty record with default values.
             */
            @Override
            public final InsertImpl defaultValues() {
                getDelegate().setDefaultValues();
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
                return set(Utils.mapOfChangedValues(record));
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
            
            /* [java-8] */
            @Override
            public final Optional<R> fetchOptional() {
                return Optional.ofNullable(fetchOne());
            }
            /* [/java-8] */
        }
        ''');
        
        write("org.jooq.impl.InsertImpl", out);
    }
}