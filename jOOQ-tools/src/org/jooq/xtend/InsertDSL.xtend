/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
                Configuration configuration = ((AttachableInternal) getDelegate()).configuration();
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