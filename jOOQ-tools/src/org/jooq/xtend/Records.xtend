/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.xtend

import org.jooq.Constants

/**
 * @author Lukas Eder
 */
class Records extends Generators {
    
    def static void main(String[] args) {
        val records = new Records();
        records.generateRecords();
        records.generateRecordImpl();
    }
    
    def generateRecords() {
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            val out = new StringBuilder();
            
            out.append('''
            «classHeader»
            package org.jooq;

            import javax.annotation.Generated;
            
            /**
             * A model type for a records with degree <code>«degree»</code>
             *
             * @see Row«degree»
             * @author Lukas Eder
             */
            «generatedAnnotation»
            public interface Record«degree»<«TN(degree)»> extends Record {

                // ------------------------------------------------------------------------
                // Row value expressions
                // ------------------------------------------------------------------------
            
                /**
                 * Get this record's fields as a {@link Row«degree»}
                 */
                @Override
                Row«degree»<«TN(degree)»> fieldsRow();
            
                /**
                 * Get this record's values as a {@link Row«degree»}
                 */
                @Override
                Row«degree»<«TN(degree)»> valuesRow();
            
                // ------------------------------------------------------------------------
                // Field accessors
                // ------------------------------------------------------------------------
                «FOR d : (1..degree)»

                /**
                 * Get the «first(d)» field
                 */
                Field<T«d»> field«d»();
                «ENDFOR»

                // ------------------------------------------------------------------------
                // Value accessors
                // ------------------------------------------------------------------------
                «FOR d : (1..degree)»

                /**
                 * Get the «first(d)» value
                 */
                T«d» value«d»();
                «ENDFOR»
            
            }
            ''');
             
            write("org.jooq.Record" + degree, out);
        }
    }
    
    def generateRecordImpl() {
        val out = new StringBuilder();
        
        out.append('''
        «classHeader»
        package org.jooq.impl;
        
        import java.util.Collection;
        
        import javax.annotation.Generated;
        
        import org.jooq.Field;
        import org.jooq.Record;
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        import org.jooq.Record«degree»;
        «ENDFOR»
        
        /**
         * A general purpose record, typically used for ad-hoc types.
         * <p>
         * This type implements both the general-purpose, type-unsafe {@link Record}
         * interface, as well as the more specific, type-safe {@link Record1},
         * {@link Record2} through {@link Record«Constants::MAX_ROW_DEGREE»} interfaces
         *
         * @author Lukas Eder
         */
        «generatedAnnotation»
        @SuppressWarnings({ "unchecked", "rawtypes" })
        class RecordImpl<«TN(Constants::MAX_ROW_DEGREE)»> extends AbstractRecord
        implements
        
            // This record implementation implements all record types. Type-safety is
            // being checked through the type-safe API. No need for further checks here
            «FOR degree : (1..Constants::MAX_ROW_DEGREE) SEPARATOR ','»
            Record«degree»<«TN(degree)»>«IF degree == Constants::MAX_ROW_DEGREE» {«ENDIF»
            «ENDFOR»
        
            /**
             * Generated UID
             */
            private static final long serialVersionUID = -2201346180421463830L;
        
            /**
             * Create a new general purpose record
             */
            public RecordImpl(Field<?>... fields) {
                super(fields);
            }
        
            /**
             * Create a new general purpose record
             */
            public RecordImpl(Collection<? extends Field<?>> fields) {
                super(fields);
            }
                
            // ------------------------------------------------------------------------
            // XXX: Type-safe Record APIs
            // ------------------------------------------------------------------------
        
            @Override
            public RowImpl<«TN(Constants::MAX_ROW_DEGREE)»> fieldsRow() {
                return fields;
            }
        
            @Override
            public final RowImpl<«TN(Constants::MAX_ROW_DEGREE)»> valuesRow() {
                return new RowImpl(Utils.fields(intoArray(), fields.fields()));
            }
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Field<T«degree»> field«degree»() {
                return fields.field(«degree - 1»);
            }
            «ENDFOR»
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

            @Override
            public final T«degree» value«degree»() {
                return (T«degree») getValue(«degree - 1»);
            }
            «ENDFOR»
        }
        ''');
        
        write("org.jooq.impl.RecordImpl", out);
    }
}