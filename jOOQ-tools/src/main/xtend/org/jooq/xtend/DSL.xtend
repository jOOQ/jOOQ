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

import org.jooq.Constants

class DSL extends Generators {
    
    def static void main(String[] args) {
        val dsl = new DSL();
        dsl.generateRowValue();
        dsl.generateRowExpression();
        dsl.generateRowField();
        dsl.generateValues();
    }
    
    def generateRowValue() {
        val out = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                /**
                 * Create a row value expression of degree <code>«degree»</code>.
                 * <p>
                 * Note: Not all databases support row value expressions, but many row value
                 * expression operations can be emulated on all databases. See relevant row
                 * value expression method Javadocs for details.
                 */
                «generatedMethod»
                @Support
                public static <«TN(degree)»> Row«degree»<«TN(degree)»> row(«TN_tn(degree)») {
                    return row(«Utils_field_tn(degree)»);
                }
            ''');
        }

        insert("org.jooq.impl.DSL", out, "row-value");
    }
    
    def generateRowExpression() {
        val out = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                /**
                 * Create a row value expression of degree <code>«degree»</code>.
                 * <p>
                 * Note: Not all databases support row value expressions, but many row value
                 * expression operations can be emulated on all databases. See relevant row
                 * value expression method Javadocs for details.
                 */
                «generatedMethod»
                @Support
                public static <«TN(degree)»> Row«degree»<«TN(degree)»> row(«Field_TN_tn(degree)») {
                    return new RowImpl(«tn(degree)»);
                }
            ''');
        }

        insert("org.jooq.impl.DSL", out, "row-expression");
    }
    
    def generateRowField() {
        val out = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                /**
                 * EXPERIMENTAL: Turn a row value expression of degree <code>«degree»</code> into a {@code Field}.
                 * <p>
                 * Note: Not all databases support row value expressions, but many row value
                 * expression operations can be emulated on all databases. See relevant row
                 * value expression method Javadocs for details.
                 */
                «generatedMethod»
                @Support
                public static <«TN(degree)»> Field<Record«recTypeSuffix(degree)»> field(Row«typeSuffix(degree)» row) {
                    return new RowField<Row«typeSuffix(degree)», Record«recTypeSuffix(degree)»>(row);
                }
            ''');// (Field) field("{0}", SQLDataType.RECORD, row);
        }

        insert("org.jooq.impl.DSL", out, "row-field");
    }
    
    def generateValues() {
        val out = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                /**
                 * Create a <code>VALUES()</code> expression of degree <code>«degree»</code>.
                 * <p>
                 * The <code>VALUES()</code> constructor is a tool supported by some
                 * databases to allow for constructing tables from constant values.
                 * <p>
                 * If a database doesn't support the <code>VALUES()</code> constructor, it
                 * can be emulated using <code>SELECT .. UNION ALL ..</code>. The following
                 * expressions are equivalent:
                 * <p>
                 * <pre><code>
                 * -- Using VALUES() constructor
                 * VALUES(«FOR d : (1..degree) SEPARATOR ', '»val1_«d»«ENDFOR»),
                 *       («FOR d : (1..degree) SEPARATOR ', '»val2_«d»«ENDFOR»),
                 *       («FOR d : (1..degree) SEPARATOR ', '»val3_«d»«ENDFOR»)
                 * AS "v"(«FOR d : (1..degree) SEPARATOR ', '»"c«d»"  «ENDFOR»)
                 *
                 * -- Using UNION ALL
                 * SELECT «FOR d : (1..degree) SEPARATOR ', '»val1_«d» AS "c«d»"«ENDFOR») UNION ALL
                 * SELECT «FOR d : (1..degree) SEPARATOR ', '»val1_«d» AS "c«d»"«ENDFOR») UNION ALL
                 * SELECT «FOR d : (1..degree) SEPARATOR ', '»val1_«d» AS "c«d»"«ENDFOR»)
                 * </code></pre>
                 * <p>
                 * Use {@link Table#as(String, String...)} to rename the resulting table and
                 * its columns.
                 */
                «generatedMethod»
                @Support
                public static <«TN(degree)»> Table<Record«degree»<«TN(degree)»>> values(Row«degree»<«TN(degree)»>... rows) {
                    return new Values<Record«degree»<«TN(degree)»>>(rows).as("v", «FOR d : (1..degree) SEPARATOR ', '»"c«d»"«ENDFOR»);
                }
            ''');
        }

        insert("org.jooq.impl.DSL", out, "values");
    }
}