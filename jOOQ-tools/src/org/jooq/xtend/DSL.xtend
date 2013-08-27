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

import org.jooq.Constants

class DSL extends Generators {
    
    def static void main(String[] args) {
        val dsl = new DSL();
        dsl.generateRowValue();
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
                 * expression operations can be simulated on all databases. See relevant row
                 * value expression method Javadocs for details.
                 */
                «generatedMethod»
                @Support
                @Transition(
                    name = "ROW",
                    args = "Field+"
                )
                public static <«TN(degree)»> Row«degree»<«TN(degree)»> row(«TN_tn(degree)») {
                    return row(«Utils_field_tn(degree)»);
                }
            ''');
        }

        insert("org.jooq.impl.DSL", out, "row-value");
    }
    
    def generateRowField() {
        val out = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                /**
                 * Create a row value expression of degree <code>«degree»</code>.
                 * <p>
                 * Note: Not all databases support row value expressions, but many row value
                 * expression operations can be simulated on all databases. See relevant row
                 * value expression method Javadocs for details.
                 */
                «generatedMethod»
                @Support
                @Transition(
                    name = "ROW",
                    args = "Field+"
                )
                public static <«TN(degree)»> Row«degree»<«TN(degree)»> row(«Field_TN_tn(degree)») {
                    return new RowImpl(«tn(degree)»);
                }
            ''');
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
                 * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
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
                @Transition(
                    name = "VALUES",
                    args = "Row+"
                )
                public static <«TN(degree)»> Table<Record«degree»<«TN(degree)»>> values(Row«degree»<«TN(degree)»>... rows) {
                    return new Values<Record«degree»<«TN(degree)»>>(rows).as("v", «FOR d : (1..degree) SEPARATOR ', '»"c«d»"«ENDFOR»);
                }
            ''');
        }

        insert("org.jooq.impl.DSL", out, "values");
    }
}