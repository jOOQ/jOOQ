/**
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
package org.jooq.docs

import java.lang.reflect.Method
import java.util.EnumSet
import org.jooq.Field
import org.jooq.QueryPart
import org.jooq.SQLDialect
import org.jooq.Support
import org.jooq.conf.RenderNameStyle
import org.jooq.conf.Settings
import org.jooq.impl.DSL

import org.jooq.conf.StatementType
import java.sql.Date
import java.sql.Timestamp

class Functions {
    static Field<Byte> numberB = DSL::field(DSL::name("number"), typeof(Byte));
    static Field<Integer> number = DSL::field(DSL::name("number"), typeof(Integer));
    static Field<Integer> start = DSL::field(DSL::name("start"), typeof(Integer));
    static Field<Integer> length = DSL::field(DSL::name("length"), typeof(Integer));
    static Field<String> string = DSL::field(DSL::name("string"), typeof(String));
    static Field<String> search = DSL::field(DSL::name("search"), typeof(String));
    static Field<String> replace = DSL::field(DSL::name("replace"), typeof(String));
    static Field<Date> date = DSL::field(DSL::name("date"), typeof(Date));
    static Field<Timestamp> timestamp = DSL::field(DSL::name("timestamp"), typeof(Timestamp));
    static Field<String> expression = DSL::field(DSL::name("expression"), typeof(String));
    static Field<String> e1 = DSL::field(DSL::name("expr1"), typeof(String));
    static Field<String> e2 = DSL::field(DSL::name("expr2"), typeof(String));
    static Field<String> e3 = DSL::field(DSL::name("expr3"), typeof(String));
    static Field<String> e4 = DSL::field(DSL::name("expr4"), typeof(String));
    
    
    
    def static EnumSet<SQLDialect> families() {
        return families(SQLDialect::values());
    }
    
    def static EnumSet<SQLDialect> families(SQLDialect[] dialects) {
        if (dialects == null || dialects.length == 0)
            return families;
            
        val result = EnumSet::noneOf(typeof(SQLDialect));
        
        for (SQLDialect d : dialects) {
            if (d != SQLDialect.SQL99 && d != SQLDialect.DEFAULT) 
                result.add(d.family());
        }
        
        return result;
    }
    
    def static void main(String[] args) {
        System::out.println('''
        <table cellpadding="0" cellspacing="0" border="0">
        «heading("Math functions")»
        «expr("Arc sine function", DSL::asin(number), typeof(DSL).getMethod("asin", typeof(Field)))»
        «expr("Arc cosine function", DSL::acos(number), typeof(DSL).getMethod("acos", typeof(Field)))»
        «expr("Arc tangent function", DSL::atan(number), typeof(DSL).getMethod("atan", typeof(Field)))»
        «expr("Arc tangent 2 function", DSL::atan2(number, number), typeof(DSL).getMethod("atan2", typeof(Field), typeof(Field)))»
        «expr("Sine function", DSL::sin(number), typeof(DSL).getMethod("sin", typeof(Field)))»
        «expr("Cosine function", DSL::cos(number), typeof(DSL).getMethod("cos", typeof(Field)))»
        «expr("Tangent function", DSL::tan(number), typeof(DSL).getMethod("tan", typeof(Field)))»
        «expr("Cotangent function", DSL::cot(number), typeof(DSL).getMethod("cot", typeof(Field)))»
        «expr("Hyperbolic sine function", DSL::sinh(number), typeof(DSL).getMethod("sinh", typeof(Field)))»
        «expr("Hyperbolic cosine function", DSL::cosh(number), typeof(DSL).getMethod("cosh", typeof(Field)))»
        «expr("Hyperbolic tangent function", DSL::tanh(number), typeof(DSL).getMethod("tanh", typeof(Field)))»
        «expr("Hyperbolic cotangent function", DSL::coth(number), typeof(DSL).getMethod("coth", typeof(Field)))»

        «expr("Absolute function", DSL::abs(number), typeof(DSL).getMethod("abs", typeof(Field)))»
        «expr("Round function", DSL::round(number, 2), typeof(DSL).getMethod("round", typeof(Field), typeof(int)))»
        «expr("Trunc function", DSL::trunc(number, 2), typeof(DSL).getMethod("trunc", typeof(Field), typeof(int)))»
        «expr("Floor function", DSL::floor(number), typeof(DSL).getMethod("floor", typeof(Field)))»
        «expr("Ceiling function", DSL::ceil(number), typeof(DSL).getMethod("ceil", typeof(Field)))»
        «expr("Sign function", DSL::sign(number), typeof(DSL).getMethod("sign", typeof(Field)))»

        «expr("Exponential function", DSL::exp(number), typeof(DSL).getMethod("exp", typeof(Field)))»
        «expr("Ln function", DSL::ln(number), typeof(DSL).getMethod("ln", typeof(Field)))»
        «expr("Log function", DSL::log(number, 2), typeof(DSL).getMethod("log", typeof(Field), typeof(int)))»
        «expr("Power function", DSL::power(number, number), typeof(DSL).getMethod("power", typeof(Field), typeof(Field)))»
        «expr("Square root function", DSL::sqrt(number), typeof(DSL).getMethod("sqrt", typeof(Field)))»

        «expr("Random function", DSL::rand(), typeof(DSL).getMethod("rand"))»
        «expr("Radians to degrees conversion", DSL::deg(number), typeof(DSL).getMethod("deg", typeof(Field)))»
        «expr("Degrees to radians conversion", DSL::rad(number), typeof(DSL).getMethod("rad", typeof(Field)))»
        «expr("Euler constant", DSL::e(), typeof(DSL).getMethod("e"))»
        «expr("Pi constant", DSL::pi(), typeof(DSL).getMethod("pi"))»
        
        «heading("Bit functions")»
        «expr("Bit count function", DSL::bitCount(numberB), typeof(DSL).getMethod("bitCount", typeof(Field)))»
        «expr("Bit length function", DSL::bitLength(string), typeof(DSL).getMethod("bitLength", typeof(Field)))»
        «expr("Bit AND operation", DSL::bitAnd(number, number), typeof(DSL).getMethod("bitAnd", typeof(Field), typeof(Field)))»
        «expr("Bit NAND operation", DSL::bitNand(number, number), typeof(DSL).getMethod("bitNand", typeof(Field), typeof(Field)))»
        «expr("Bit NOR operation", DSL::bitNor(number, number), typeof(DSL).getMethod("bitNor", typeof(Field), typeof(Field)))»
        «expr("Bit NOT a value", DSL::bitNot(number), typeof(DSL).getMethod("bitNot", typeof(Field)))»
        «expr("Bit OR operation", DSL::bitOr(number, number), typeof(DSL).getMethod("bitOr", typeof(Field), typeof(Field)))»
        «expr("Bit XNOR operation", DSL::bitXNor(number, number), typeof(DSL).getMethod("bitXNor", typeof(Field), typeof(Field)))»
        «expr("Bit XOR operation", DSL::bitXor(number, number), typeof(DSL).getMethod("bitXor", typeof(Field), typeof(Field)))»
        «expr("Bitwise left shift", DSL::shl(number, number), typeof(DSL).getMethod("shl", typeof(Field), typeof(Field)))»
        «expr("Bitwise right shift", DSL::shr(number, number), typeof(DSL).getMethod("shr", typeof(Field), typeof(Field)))»
        
        «heading("String functions")»
        «expr("Char length function", DSL::charLength(string), typeof(DSL).getMethod("charLength", typeof(Field)))»
        «expr("Length function", DSL::length(string), typeof(DSL).getMethod("length", typeof(Field)))»
        «expr("Octet length function", DSL::octetLength(string), typeof(DSL).getMethod("octetLength", typeof(Field)))»
        «expr("Ascii function", DSL::ascii(string), typeof(DSL).getMethod("ascii", typeof(Field)))»
        «expr("Concat function", DSL::concat(string, string), typeof(DSL).getMethod("concat", typeof(Field[])))»
        «expr("Lower function", DSL::lower(string), typeof(DSL).getMethod("lower", typeof(Field)))»
        «expr("Upper function", DSL::upper(string), typeof(DSL).getMethod("upper", typeof(Field)))»
        «expr("Lpad function", DSL::lpad(string, number, string), typeof(DSL).getMethod("lpad", typeof(Field), typeof(Field), typeof(Field)))»
        «expr("Rpad function", DSL::rpad(string, number, string), typeof(DSL).getMethod("rpad", typeof(Field), typeof(Field), typeof(Field)))»
        «expr("Trim function", DSL::trim(string), typeof(DSL).getMethod("trim", typeof(Field)))»
        «expr("Ltrim function", DSL::ltrim(string), typeof(DSL).getMethod("ltrim", typeof(Field)))»
        «expr("Rtrim function", DSL::rtrim(string), typeof(DSL).getMethod("rtrim", typeof(Field)))»
        «expr("Position function", DSL::position(string, search), typeof(DSL).getMethod("position", typeof(Field), typeof(Field)))»
        «expr("Repeat function", DSL::repeat(string, number), typeof(DSL).getMethod("repeat", typeof(Field), typeof(Field)))»
        «expr("Replace function", DSL::replace(string, search, replace), typeof(DSL).getMethod("replace", typeof(Field), typeof(Field), typeof(Field)))»
        «expr("Substring function", DSL::substring(string, start, length), typeof(DSL).getMethod("substring", typeof(Field), typeof(Field), typeof(Field)))»
        
        «heading("Date and time functions")»
        «expr("Date add function", DSL::dateAdd(date, number), typeof(DSL).getMethod("dateAdd", typeof(Field), typeof(Field)))»
        «expr("Date diff function", DSL::dateDiff(date, date), typeof(DSL).getMethod("dateDiff", typeof(Field), typeof(Field)))»
        «expr("Timestamp add function", DSL::timestampAdd(timestamp, number), typeof(DSL).getMethod("timestampAdd", typeof(Field), typeof(Field)))»
        «expr("Timestamp diff function", DSL::timestampDiff(timestamp, timestamp), typeof(DSL).getMethod("timestampDiff", typeof(Field), typeof(Field)))»
        «expr("Extract the year from a date", DSL::year(date), typeof(DSL).getMethod("year", typeof(Field)))»
        «expr("Extract the month from a date", DSL::month(date), typeof(DSL).getMethod("month", typeof(Field)))»
        «expr("Extract the day from a date", DSL::day(date), typeof(DSL).getMethod("day", typeof(Field)))»
        «expr("Extract the hour from a date", DSL::hour(date), typeof(DSL).getMethod("hour", typeof(Field)))»
        «expr("Extract the minute from a date", DSL::minute(date), typeof(DSL).getMethod("minute", typeof(Field)))»
        «expr("Extract the second from a date", DSL::second(date), typeof(DSL).getMethod("second", typeof(Field)))»
        
        «heading("Other functions")»
        «expr("Coalesce function", DSL::coalesce(e1, e2), typeof(DSL).getMethod("coalesce", typeof(Field), typeof(Field[])))»
        «expr("Decode function", DSL::decode(e1, e2, e3, e4), typeof(DSL).getMethod("decode", typeof(Field), typeof(Field), typeof(Field), typeof(Field[])))»
        «expr("Greatest function", DSL::greatest(e1, e2, e3), typeof(DSL).getMethod("greatest", typeof(Field), typeof(Field[])))»
        «expr("Least function", DSL::least(e1, e2, e3), typeof(DSL).getMethod("least", typeof(Field), typeof(Field[])))»
        «expr("Null if function", DSL::nullif(e1, e2), typeof(DSL).getMethod("nullif", typeof(Field), typeof(Field)))»
        «expr("Nvl function", DSL::nvl(e1, e2), typeof(DSL).getMethod("nvl", typeof(Field), typeof(Field)))»
        «expr("Nvl2 function", DSL::nvl2(e1, e2, e3), typeof(DSL).getMethod("nvl2", typeof(Field), typeof(Field), typeof(Field)))»
        </table>
        ''');
    }
    
    def static heading(String description) {
        return '''
        <tr>
        <td colspan="16">
        <h3>«description»<h3>
        </td>
        </tr>
        <tr>
        <td class="description">Function description</td>
        «FOR d : families»
        <td class="«d.nameLC»">«d.name()»</td>
        «ENDFOR»
        </tr>
        '''
    }
    
    def static expr(String description, QueryPart p, Method method) {
        val support = method.getAnnotation(typeof(Support));
        return '''
        <tr>
        <td class="description">«description»</td>
        «FOR d : families»
        <td class="«d.name()»">«IF families(support.value).contains(d)»<code><pre>«DSL::using(d, new Settings()
                    .withRenderSchema(false)
                    .withRenderNameStyle(RenderNameStyle.AS_IS)
                    .withStatementType(StatementType.STATIC_STATEMENT)
                ).render(p)»</pre></code>«ELSE»&nbsp;«ENDIF»</td>
        «ENDFOR»
        </tr>
        '''
    }
}

