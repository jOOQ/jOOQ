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

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.RandomAccessFile
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Lukas Eder
 */
abstract class Generators {
	
	def file(String className) {
        if (className.contains("xtend")) {
            return new File("./../jOOQ-xtend/src/main/xtend/" + className.replace(".", "/") + ".xtend");
        }
        else if (className.contains("scala")) {
            return new File("./../jOOQ-scala/src/main/scala/" + className.replace(".", "/") + ".scala");
        }
        else if (className.contains("lambda")) {
            return new File("./../../jOOL/src/main/java/" + className.replace(".", "/") + ".java");
        }
		else {
			return new File("./../jOOQ/src/main/java/" + className.replace(".", "/") + ".java");
		}
	}
	
	def read(String className) {
		return read(file(className))
	}
	
	def read(File file) {
        val f = new RandomAccessFile(file, "r");
	    try {
            val contents = Util::newByteArray(f.length);
            f.readFully(contents);
            return new String(contents);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            f.close;
        }
	}
	
	def insert(String className, CharSequence contents, String section) {
		val result = new StringBuilder();
		val original = read(className);
		val start = "// [jooq-tools] START [" + section + "]";
		val end = "// [jooq-tools] END [" + section + "]"
		
		result.append(original.substring(0, original.indexOf(start) + start.length + 1))
		result.append(contents);
		result.append("\n");
		result.append(original.substring(original.indexOf(end)));
		
		write(className, result, section);
	}

	def write(String className, CharSequence contents) {
		write(className, contents, null);
	}
	
    def write(String className, CharSequence contents, String section) {
        val file = file(className);
        write(file, contents, section);
    }
        
    def write(File file, CharSequence contents) {
        write(file, contents, null);
    }
    
    def write(File file, CharSequence contents, String section) {
        file.getParentFile().mkdirs();
    
        try {
            val i = totalWrites.incrementAndGet;
            System::out.println(i + ": Writing " + file + (if (section != null) (" (section: " + section + ")") else ""));
            val fw = new FileWriter(file);
            // It's hard to enforce unix line separators in Xtend
            fw.append(contents.toString().replace("\r\n", "\n"));
            fw.flush();
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static AtomicInteger totalWrites = new AtomicInteger(0);

    def first(int degree) {
        switch degree {
            case 1 : "first"
            case 2 : "second"
            case 3 : "third"
            case 4 : "fourth"
            case 5 : "fifth"
            case 6 : "sixth"
            case 7 : "seventh"
            case 8 : "eighth"
            case 9 : "ninth"
            case 10 : "tenth"
            case 11 : "eleventh"
            case 12 : "twelfth"
            case 13 : "thirteenth"
            case 14 : "fourteenth"
            case 15 : "fifteenth"
            case 16 : "sixteenth"
            case 17 : "seventeenth"
            case 18 : "eighteenth"
            case 19 : "ninteenth"
            case 20 : "twentieth"
            case 21 : "twenty-first"
            case 22 : "twenty-second"
            case degree % 10 == 1 : degree + "st"
            case degree % 10 == 2 : degree + "nd"
            case degree % 10 == 3 : degree + "rd"
            default : degree + "th"
        }
    }
    
    def classHeader() {
        '''
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
        '''
    }
    
    def generatedAnnotation() {
        '''
        @Generated("This class was generated using jOOQ-tools")
        '''
    }
   
    def generatedMethod() {
        '''
        @Generated("This method was generated using jOOQ-tools")
        '''
    }
    
    /**
     * A comma-separated list of types
     * <p>
     * <code>T1, T2, .., T[N]</code>
     */
    def TN(int degree) {
        return
        if (degree == 0)
            "Object..."
        else
            TN(1, degree)
    }    
    
    /**
     * A comma-separated list of types
     * <p>
     * <code>T[from], .., T[to]</code>
     */
    def TN(int from, int to) {
        XXXn(from, to, "T")
    }
    
    /**
     * A comma-separated list of identifier references
     * <p>
     * <code>t1, t2, .., t[N]</code>
     */
    def tn(int degree) {
        return
        if (degree == 0)
            "values"
        else
            (1..degree).join(", ", [e | "t" + e])
    }
    
    /**
     * A comma-separated list of identifier references
     * <p>
     * <code>v1, v2, .., v[N]</code>
     */
    def vn(int degree) {
        return
        if (degree == 0)
            "values"
        else
            vn(1, degree)
    }
    
    /**
     * A comma-separated list of identifier references
     * <p>
     * <code>v[from], .., v[to]</code>
     */
    def vn(int from, int to) {
        XXXn(from, to, "v")
    }
    
    /**
     * A comma-separated list of identifier references
     * <p>
     * <code>t1, t2, .., t[N]</code>
     */
    def val_tn(int degree) {
        return
        if (degree == 0)
            "values"
        else
            (1..degree).join(", ", [e | "val(t" + e + ")"])
    }
    
    /** 
     * A comma-separated list of identifier declarations
     * <p>
     * <code>T1 t1, T2 t2, .., T[N] t[N]</code>
     */
    def TN_tn(int degree) {
    	return 
    	if (degree == 0)
    		"Object... values"
    	else
        	(1..degree).join(", ", [e | "T" + e + " t" + e])
    }
    
    /** 
     * A comma-separated list of identifier declarations
     * <p>
     * <code>Object t1, Object t2, .., Object t[N]</code>
     */
    def Object_tn(int degree) {
    	return 
    	if (degree == 0)
    		"Object... values"
    	else
        	(1..degree).join(", ", [e | "Object t" + e])
    }
    
    /** 
     * A comma-separated list of identifier declarations
     * <p>
     * <code>T1 t1, T2 t2, .., T[N] t[N]</code>
     */
    def TN_XXXn(int degree, String XXX) {
        TN_XXXn(1, degree, XXX)
    }
    
    /** 
     * A comma-separated list of identifier declarations
     * <p>
     * <code>T1 t1, T2 t2, .., T[N] t[N]</code>
     */
    def TN_XXXn(int from, int to, String XXX) {
        return
        if (to == 0)
            "Object... " + XXX + "s"
        else
            (from..to).join(", ", [e | "T" + e + " " + XXX + e])
    }
    
    /**
     * A comma-separated list of field declarations
     * <p>
     * <code>Field&lt;T1> t1, Field&lt;T2> t2, .., Field&ltT[N]> t[N]</code>
     */
    def Field_TN_tn(int degree) {
    	return
    	if (degree == 0)
    		"Field<?>... values"
		else
	        (1..degree).join(", ", [e | "Field<T" + e + "> t" + e])
    }
     
    /**
     * A comma-separated list of field declarations
     * <p>
     * <code>Field t1, Field t2, .., Field t[N]</code>
     */
    def Field_tn(int degree) {
        return
        if (degree == 0)
            "Field<?>... values"
        else
            (1..degree).join(", ", [e | "Field t" + e])
    }
        
    /**
     * A comma-separated list of sort field declarations
     * <p>
     * <code>SortField t1, SortField t2, .., SortField t[N]</code>
     */
    def SortField_tn(int degree) {
        return
        if (degree == 0)
            "SortField<?>... values"
        else
            (1..degree).join(", ", [e | "SortField t" + e])
    }
    
    /**
     * A comma-separated list of field declarations
     * <p>
     * <code>Field&lt;T1> t1, Field&lt;T2> t2, .., Field&ltT[N]> t[N]</code>
     */
    def Field_TN_XXXn(int degree, String XXX) {
        return
        if (degree == 0)
            "Field<?>... " + XXX + "s"
        else
            (1..degree).join(", ", [e | "Field<T" + e + "> " + XXX + e])
    }
    
    /**
     * A comma-separated list of field declarations
     * <p>
     * <code>Field&lt;T1> field1, Field&lt;T2> field2, .., Field&ltT[N]> field[N]</code>
     */
    def Field_TN_fieldn(int degree) {
        return
        if (degree == 0)
            "Field<?>... fields"
        else
            Field_TN_XXXn(degree, "field")
    }
         
    /**
     * A comma-separated list of field declarations
     * <p>
     * <code>SelectField&lt;T1> field1, SelectField&lt;T2> field2, .., SelectField&ltT[N]> field[N]</code>
     */
    def SelectField_TN_fieldn(int degree) {
        return
        if (degree == 0)
            "SelectField<?>... fields"
        else
            (1..degree).join(", ", [e | "SelectField<T" + e + "> field" + e])
    }
     
    /**
     * A comma-separated list of field declarations
     * <p>
     * <code>SortField&lt;T1> field1, SortField&lt;T2> field2, .., SortField&ltT[N]> field[N]</code>
     */
    def SortField_TN_fieldn(int degree) {
        return
        if (degree == 0)
            "SortField<?>... fields"
        else
            (1..degree).join(", ", [e | "SortField<T" + e + "> field" + e])
    }
    
    /**
     * A comma-separated list of field references
     * <p>
     * <code>field1, field2, .., field[N]</code>
     */
    def fieldn(int degree) {
        XXXn(degree, "field")
    }
    
    /**
     * A comma-separated list of field references
     * <p>
     * <code>XXX1, XXX2, .., XXX[N]</code>
     */
    def XXXn(int degree, String XXX) {
        return
        if (degree == 0)
            XXX + "s"
        else
            XXXn(1, degree, XXX)
    }
    
    /**
     * A comma-separated list of field references
     * <p>
     * <code>XXX1, XXX2, .., XXX[N]</code>
     */
    def XXXn(int from, int to, String XXX) {
        if (from <= to)
            (from..to).join(", ", [e | XXX + e])
        else
            ""
    }
    
    /**
     * A comma-separated list of field references
     * <p>
     * Unlike {@link #fieldn(int)}, this will return at most 5 fields
     * <p>
     * <code>field1, field2, .., field[N]</code>
     */
    def field1_field2_fieldn(int degree) {
    	XXX1_XXX2_XXXn(degree, "field")
    }
    
    /**
     * A comma-separated list of literals
     * <p>
     * Unlike {@link #fieldn(int)}, this will return at most 5 fields
     * <p>
     * <code>XXX1, XXX2, .., XXX[N]</code>
     */
    def XXX1_XXX2_XXXn(int degree, String XXX) {
    	if (degree <= 5) {
    		return fieldn(degree);
    	}
    	else {
    		return (1..3).join(", ", [e | XXX + e]) +
    		                   ", .., " +
    		       (degree - 1..degree).join(", ", [e | XXX + e])
    	}
    }
    
    /**
     * A comma-separated list of value constructor references
     * <p>
     * <code>val(t1), val(t2), .., val(t[N])</code>
     */
    def Utils_field_tn(int degree) {
        (1..degree).join(", ", [e | "Utils.field(t" + e + ")"])
    }
    
    /**
     * A numeric degree or "N"
     */
    def degreeOrN(int degree) {
    	return degreeOr(degree, "N")
    }
    
    /**
     * A numeric degree or [nothing]
     */
    def degreeOr(int degree) {
    	return degreeOr(degree, "")
    }
    
    /**
     * A numeric degree or [or]
     */
    def degreeOr(int degree, String or) {
    	return if (degree == 0) or else degree
    }
    
    /**
     * The generic type of a class, e.g. <code>""</code> or <code>&lt;T1, T2, T3></code>
     */
    def type(int degree) {
    	type(degree, "")
    }
    
    /**
     * The generic type of a class, e.g. <code>""</code> or <code>&lt;T1, T2, T3></code>
     */
    def type(int degree, String spacer) {
    	'''«IF degree > 0»<«TN(degree)»>«spacer»«ENDIF»'''
    }
    
    /**
     * The generic type suffix of a class, e.g. <code>N</code> or <code>3&lt;T1, T2, T3></code>
     */
    def typeSuffix(int degree) {
        '''«typeSuffixRaw(degree)»«type(degree)»'''
    }

	/**
     * The "raw" generic type suffix of a class, e.g. <code>N</code> or <code>3</code>
     */
    def typeSuffixRaw(int degree) {
        '''«degreeOrN(degree)»'''
    }

	/**
     * The generic type suffix of a record, e.g. <code>""</code> or <code>3&lt;T1, T2, T3></code>
     */
    def recTypeSuffix(int degree) {
        '''«recTypeSuffixRaw(degree)»«type(degree)»'''
    }

	/**
     * The "raw" generic type suffix of a record, e.g. <code>""</code> or <code>3</code>
     */
    def recTypeSuffixRaw(int degree) {
        '''«degreeOr(degree)»'''
    }
}