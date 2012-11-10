/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import org.jooq.Constants

/**
 * @author Lukas Eder
 */
class Records extends Generators {
    
    def static void main(String[] args) {
        val records = new Records();
        records.generateRecordClasses();
        records.generateRecordImpl();
    }
    
    def generateRecordClasses() {
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
                Row«degree»<«TN(degree)»> fieldsRow();
            
                /**
                 * Get this record's values as a {@link Row«degree»}
                 */
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
        
        import static org.jooq.impl.Factory.vals;
        
        import java.util.List;
        
        import javax.annotation.Generated;
        
        import org.jooq.Field;
        import org.jooq.FieldProvider;
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
             * Create a new general purpos record
             */
            public RecordImpl(FieldProvider fields) {
                super(fields);
            }
        
            // ------------------------------------------------------------------------
            // XXX: Type-safe Record APIs
            // ------------------------------------------------------------------------
        
            @Override
            public RowImpl<«TN(Constants::MAX_ROW_DEGREE)»> fieldsRow() {
                return new RowImpl(getFields());
            }
        
            @Override
            public final RowImpl<«TN(Constants::MAX_ROW_DEGREE)»> valuesRow() {
                List<Field<?>> fields = getFields();
                return new RowImpl(vals(intoArray(), fields.toArray(new Field[fields.size()])));
            }
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Field<T«degree»> field«degree»() {
                return (Field<T«degree»>) getField(«degree - 1»);
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