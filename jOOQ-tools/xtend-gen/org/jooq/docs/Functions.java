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
package org.jooq.docs;

import com.google.common.base.Objects;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.EnumSet;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.SQLDialect;
import org.jooq.Support;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.DSL;
import org.jooq.types.DayToSecond;

@SuppressWarnings("all")
public class Functions {
  private static Field<Byte> numberB = DSL.<Byte>field(DSL.name("number"), Byte.class);
  
  private static Field<Integer> number = DSL.<Integer>field(DSL.name("number"), Integer.class);
  
  private static Field<Integer> start = DSL.<Integer>field(DSL.name("start"), Integer.class);
  
  private static Field<Integer> length = DSL.<Integer>field(DSL.name("length"), Integer.class);
  
  private static Field<String> string = DSL.<String>field(DSL.name("string"), String.class);
  
  private static Field<String> search = DSL.<String>field(DSL.name("search"), String.class);
  
  private static Field<String> replace = DSL.<String>field(DSL.name("replace"), String.class);
  
  private static Field<Date> date = DSL.<Date>field(DSL.name("date"), Date.class);
  
  private static Field<Timestamp> timestamp = DSL.<Timestamp>field(DSL.name("timestamp"), Timestamp.class);
  
  private static Field<String> expression = DSL.<String>field(DSL.name("expression"), String.class);
  
  private static Field<String> e1 = DSL.<String>field(DSL.name("expr1"), String.class);
  
  private static Field<String> e2 = DSL.<String>field(DSL.name("expr2"), String.class);
  
  private static Field<String> e3 = DSL.<String>field(DSL.name("expr3"), String.class);
  
  private static Field<String> e4 = DSL.<String>field(DSL.name("expr4"), String.class);
  
  public static EnumSet<SQLDialect> families() {
    SQLDialect[] _values = SQLDialect.values();
    return Functions.families(_values);
  }
  
  public static EnumSet<SQLDialect> families(final SQLDialect[] dialects) {
    boolean _or = false;
    boolean _equals = Objects.equal(dialects, null);
    if (_equals) {
      _or = true;
    } else {
      int _length = dialects.length;
      boolean _equals_1 = (_length == 0);
      _or = _equals_1;
    }
    if (_or) {
      return Functions.families();
    }
    final EnumSet<SQLDialect> result = EnumSet.<SQLDialect>noneOf(SQLDialect.class);
    for (final SQLDialect d : dialects) {
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(d, SQLDialect.SQL99));
      if (!_notEquals) {
        _and = false;
      } else {
        boolean _notEquals_1 = (!Objects.equal(d, SQLDialect.DEFAULT));
        _and = _notEquals_1;
      }
      if (_and) {
        SQLDialect _family = d.family();
        result.add(_family);
      }
    }
    return result;
  }
  
  public static void main(final String[] args) {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
      _builder.newLine();
      String _heading = Functions.heading("Math functions");
      _builder.append(_heading, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _asin = DSL.asin(Functions.number);
      Method _method = DSL.class.getMethod("asin", Field.class);
      String _expr = Functions.expr("Arc sine function", _asin, _method);
      _builder.append(_expr, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _acos = DSL.acos(Functions.number);
      Method _method_1 = DSL.class.getMethod("acos", Field.class);
      String _expr_1 = Functions.expr("Arc cosine function", _acos, _method_1);
      _builder.append(_expr_1, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _atan = DSL.atan(Functions.number);
      Method _method_2 = DSL.class.getMethod("atan", Field.class);
      String _expr_2 = Functions.expr("Arc tangent function", _atan, _method_2);
      _builder.append(_expr_2, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _atan2 = DSL.atan2(Functions.number, Functions.number);
      Method _method_3 = DSL.class.getMethod("atan2", Field.class, Field.class);
      String _expr_3 = Functions.expr("Arc tangent 2 function", _atan2, _method_3);
      _builder.append(_expr_3, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _sin = DSL.sin(Functions.number);
      Method _method_4 = DSL.class.getMethod("sin", Field.class);
      String _expr_4 = Functions.expr("Sine function", _sin, _method_4);
      _builder.append(_expr_4, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _cos = DSL.cos(Functions.number);
      Method _method_5 = DSL.class.getMethod("cos", Field.class);
      String _expr_5 = Functions.expr("Cosine function", _cos, _method_5);
      _builder.append(_expr_5, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _tan = DSL.tan(Functions.number);
      Method _method_6 = DSL.class.getMethod("tan", Field.class);
      String _expr_6 = Functions.expr("Tangent function", _tan, _method_6);
      _builder.append(_expr_6, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _cot = DSL.cot(Functions.number);
      Method _method_7 = DSL.class.getMethod("cot", Field.class);
      String _expr_7 = Functions.expr("Cotangent function", _cot, _method_7);
      _builder.append(_expr_7, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _sinh = DSL.sinh(Functions.number);
      Method _method_8 = DSL.class.getMethod("sinh", Field.class);
      String _expr_8 = Functions.expr("Hyperbolic sine function", _sinh, _method_8);
      _builder.append(_expr_8, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _cosh = DSL.cosh(Functions.number);
      Method _method_9 = DSL.class.getMethod("cosh", Field.class);
      String _expr_9 = Functions.expr("Hyperbolic cosine function", _cosh, _method_9);
      _builder.append(_expr_9, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _tanh = DSL.tanh(Functions.number);
      Method _method_10 = DSL.class.getMethod("tanh", Field.class);
      String _expr_10 = Functions.expr("Hyperbolic tangent function", _tanh, _method_10);
      _builder.append(_expr_10, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _coth = DSL.coth(Functions.number);
      Method _method_11 = DSL.class.getMethod("coth", Field.class);
      String _expr_11 = Functions.expr("Hyperbolic cotangent function", _coth, _method_11);
      _builder.append(_expr_11, "");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      Field<Integer> _abs = DSL.<Integer>abs(Functions.number);
      Method _method_12 = DSL.class.getMethod("abs", Field.class);
      String _expr_12 = Functions.expr("Absolute function", _abs, _method_12);
      _builder.append(_expr_12, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _round = DSL.<Integer>round(Functions.number, 2);
      Method _method_13 = DSL.class.getMethod("round", Field.class, int.class);
      String _expr_13 = Functions.expr("Round function", _round, _method_13);
      _builder.append(_expr_13, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _trunc = DSL.<Integer>trunc(Functions.number, 2);
      Method _method_14 = DSL.class.getMethod("trunc", Field.class, int.class);
      String _expr_14 = Functions.expr("Trunc function", _trunc, _method_14);
      _builder.append(_expr_14, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _floor = DSL.<Integer>floor(Functions.number);
      Method _method_15 = DSL.class.getMethod("floor", Field.class);
      String _expr_15 = Functions.expr("Floor function", _floor, _method_15);
      _builder.append(_expr_15, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _ceil = DSL.<Integer>ceil(Functions.number);
      Method _method_16 = DSL.class.getMethod("ceil", Field.class);
      String _expr_16 = Functions.expr("Ceiling function", _ceil, _method_16);
      _builder.append(_expr_16, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _sign = DSL.sign(Functions.number);
      Method _method_17 = DSL.class.getMethod("sign", Field.class);
      String _expr_17 = Functions.expr("Sign function", _sign, _method_17);
      _builder.append(_expr_17, "");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      Field<BigDecimal> _exp = DSL.exp(Functions.number);
      Method _method_18 = DSL.class.getMethod("exp", Field.class);
      String _expr_18 = Functions.expr("Exponential function", _exp, _method_18);
      _builder.append(_expr_18, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _ln = DSL.ln(Functions.number);
      Method _method_19 = DSL.class.getMethod("ln", Field.class);
      String _expr_19 = Functions.expr("Ln function", _ln, _method_19);
      _builder.append(_expr_19, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _log = DSL.log(Functions.number, 2);
      Method _method_20 = DSL.class.getMethod("log", Field.class, int.class);
      String _expr_20 = Functions.expr("Log function", _log, _method_20);
      _builder.append(_expr_20, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _power = DSL.power(Functions.number, Functions.number);
      Method _method_21 = DSL.class.getMethod("power", Field.class, Field.class);
      String _expr_21 = Functions.expr("Power function", _power, _method_21);
      _builder.append(_expr_21, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _sqrt = DSL.sqrt(Functions.number);
      Method _method_22 = DSL.class.getMethod("sqrt", Field.class);
      String _expr_22 = Functions.expr("Square root function", _sqrt, _method_22);
      _builder.append(_expr_22, "");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      Field<BigDecimal> _rand = DSL.rand();
      Method _method_23 = DSL.class.getMethod("rand");
      String _expr_23 = Functions.expr("Random function", _rand, _method_23);
      _builder.append(_expr_23, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _deg = DSL.deg(Functions.number);
      Method _method_24 = DSL.class.getMethod("deg", Field.class);
      String _expr_24 = Functions.expr("Radians to degrees conversion", _deg, _method_24);
      _builder.append(_expr_24, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _rad = DSL.rad(Functions.number);
      Method _method_25 = DSL.class.getMethod("rad", Field.class);
      String _expr_25 = Functions.expr("Degrees to radians conversion", _rad, _method_25);
      _builder.append(_expr_25, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _e = DSL.e();
      Method _method_26 = DSL.class.getMethod("e");
      String _expr_26 = Functions.expr("Euler constant", _e, _method_26);
      _builder.append(_expr_26, "");
      _builder.newLineIfNotEmpty();
      Field<BigDecimal> _pi = DSL.pi();
      Method _method_27 = DSL.class.getMethod("pi");
      String _expr_27 = Functions.expr("Pi constant", _pi, _method_27);
      _builder.append(_expr_27, "");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _heading_1 = Functions.heading("Bit functions");
      _builder.append(_heading_1, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _bitCount = DSL.bitCount(Functions.numberB);
      Method _method_28 = DSL.class.getMethod("bitCount", Field.class);
      String _expr_28 = Functions.expr("Bit count function", _bitCount, _method_28);
      _builder.append(_expr_28, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _bitLength = DSL.bitLength(Functions.string);
      Method _method_29 = DSL.class.getMethod("bitLength", Field.class);
      String _expr_29 = Functions.expr("Bit length function", _bitLength, _method_29);
      _builder.append(_expr_29, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _bitAnd = DSL.<Integer>bitAnd(Functions.number, Functions.number);
      Method _method_30 = DSL.class.getMethod("bitAnd", Field.class, Field.class);
      String _expr_30 = Functions.expr("Bit AND operation", _bitAnd, _method_30);
      _builder.append(_expr_30, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _bitNand = DSL.<Integer>bitNand(Functions.number, Functions.number);
      Method _method_31 = DSL.class.getMethod("bitNand", Field.class, Field.class);
      String _expr_31 = Functions.expr("Bit NAND operation", _bitNand, _method_31);
      _builder.append(_expr_31, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _bitNor = DSL.<Integer>bitNor(Functions.number, Functions.number);
      Method _method_32 = DSL.class.getMethod("bitNor", Field.class, Field.class);
      String _expr_32 = Functions.expr("Bit NOR operation", _bitNor, _method_32);
      _builder.append(_expr_32, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _bitNot = DSL.<Integer>bitNot(Functions.number);
      Method _method_33 = DSL.class.getMethod("bitNot", Field.class);
      String _expr_33 = Functions.expr("Bit NOT a value", _bitNot, _method_33);
      _builder.append(_expr_33, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _bitOr = DSL.<Integer>bitOr(Functions.number, Functions.number);
      Method _method_34 = DSL.class.getMethod("bitOr", Field.class, Field.class);
      String _expr_34 = Functions.expr("Bit OR operation", _bitOr, _method_34);
      _builder.append(_expr_34, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _bitXNor = DSL.<Integer>bitXNor(Functions.number, Functions.number);
      Method _method_35 = DSL.class.getMethod("bitXNor", Field.class, Field.class);
      String _expr_35 = Functions.expr("Bit XNOR operation", _bitXNor, _method_35);
      _builder.append(_expr_35, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _bitXor = DSL.<Integer>bitXor(Functions.number, Functions.number);
      Method _method_36 = DSL.class.getMethod("bitXor", Field.class, Field.class);
      String _expr_36 = Functions.expr("Bit XOR operation", _bitXor, _method_36);
      _builder.append(_expr_36, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _shl = DSL.<Integer>shl(Functions.number, Functions.number);
      Method _method_37 = DSL.class.getMethod("shl", Field.class, Field.class);
      String _expr_37 = Functions.expr("Bitwise left shift", _shl, _method_37);
      _builder.append(_expr_37, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _shr = DSL.<Integer>shr(Functions.number, Functions.number);
      Method _method_38 = DSL.class.getMethod("shr", Field.class, Field.class);
      String _expr_38 = Functions.expr("Bitwise right shift", _shr, _method_38);
      _builder.append(_expr_38, "");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _heading_2 = Functions.heading("String functions");
      _builder.append(_heading_2, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _charLength = DSL.charLength(Functions.string);
      Method _method_39 = DSL.class.getMethod("charLength", Field.class);
      String _expr_39 = Functions.expr("Char length function", _charLength, _method_39);
      _builder.append(_expr_39, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _length = DSL.length(Functions.string);
      Method _method_40 = DSL.class.getMethod("length", Field.class);
      String _expr_40 = Functions.expr("Length function", _length, _method_40);
      _builder.append(_expr_40, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _octetLength = DSL.octetLength(Functions.string);
      Method _method_41 = DSL.class.getMethod("octetLength", Field.class);
      String _expr_41 = Functions.expr("Octet length function", _octetLength, _method_41);
      _builder.append(_expr_41, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _ascii = DSL.ascii(Functions.string);
      Method _method_42 = DSL.class.getMethod("ascii", Field.class);
      String _expr_42 = Functions.expr("Ascii function", _ascii, _method_42);
      _builder.append(_expr_42, "");
      _builder.newLineIfNotEmpty();
      Field<String> _concat = DSL.concat(Functions.string, Functions.string);
      Method _method_43 = DSL.class.getMethod("concat", Field[].class);
      String _expr_43 = Functions.expr("Concat function", _concat, _method_43);
      _builder.append(_expr_43, "");
      _builder.newLineIfNotEmpty();
      Field<String> _lower = DSL.lower(Functions.string);
      Method _method_44 = DSL.class.getMethod("lower", Field.class);
      String _expr_44 = Functions.expr("Lower function", _lower, _method_44);
      _builder.append(_expr_44, "");
      _builder.newLineIfNotEmpty();
      Field<String> _upper = DSL.upper(Functions.string);
      Method _method_45 = DSL.class.getMethod("upper", Field.class);
      String _expr_45 = Functions.expr("Upper function", _upper, _method_45);
      _builder.append(_expr_45, "");
      _builder.newLineIfNotEmpty();
      Field<String> _lpad = DSL.lpad(Functions.string, Functions.number, Functions.string);
      Method _method_46 = DSL.class.getMethod("lpad", Field.class, Field.class, Field.class);
      String _expr_46 = Functions.expr("Lpad function", _lpad, _method_46);
      _builder.append(_expr_46, "");
      _builder.newLineIfNotEmpty();
      Field<String> _rpad = DSL.rpad(Functions.string, Functions.number, Functions.string);
      Method _method_47 = DSL.class.getMethod("rpad", Field.class, Field.class, Field.class);
      String _expr_47 = Functions.expr("Rpad function", _rpad, _method_47);
      _builder.append(_expr_47, "");
      _builder.newLineIfNotEmpty();
      Field<String> _trim = DSL.trim(Functions.string);
      Method _method_48 = DSL.class.getMethod("trim", Field.class);
      String _expr_48 = Functions.expr("Trim function", _trim, _method_48);
      _builder.append(_expr_48, "");
      _builder.newLineIfNotEmpty();
      Field<String> _ltrim = DSL.ltrim(Functions.string);
      Method _method_49 = DSL.class.getMethod("ltrim", Field.class);
      String _expr_49 = Functions.expr("Ltrim function", _ltrim, _method_49);
      _builder.append(_expr_49, "");
      _builder.newLineIfNotEmpty();
      Field<String> _rtrim = DSL.rtrim(Functions.string);
      Method _method_50 = DSL.class.getMethod("rtrim", Field.class);
      String _expr_50 = Functions.expr("Rtrim function", _rtrim, _method_50);
      _builder.append(_expr_50, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _position = DSL.position(Functions.string, Functions.search);
      Method _method_51 = DSL.class.getMethod("position", Field.class, Field.class);
      String _expr_51 = Functions.expr("Position function", _position, _method_51);
      _builder.append(_expr_51, "");
      _builder.newLineIfNotEmpty();
      Field<String> _repeat = DSL.repeat(Functions.string, Functions.number);
      Method _method_52 = DSL.class.getMethod("repeat", Field.class, Field.class);
      String _expr_52 = Functions.expr("Repeat function", _repeat, _method_52);
      _builder.append(_expr_52, "");
      _builder.newLineIfNotEmpty();
      Field<String> _replace = DSL.replace(Functions.string, Functions.search, Functions.replace);
      Method _method_53 = DSL.class.getMethod("replace", Field.class, Field.class, Field.class);
      String _expr_53 = Functions.expr("Replace function", _replace, _method_53);
      _builder.append(_expr_53, "");
      _builder.newLineIfNotEmpty();
      Field<String> _substring = DSL.substring(Functions.string, Functions.start, Functions.length);
      Method _method_54 = DSL.class.getMethod("substring", Field.class, Field.class, Field.class);
      String _expr_54 = Functions.expr("Substring function", _substring, _method_54);
      _builder.append(_expr_54, "");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _heading_3 = Functions.heading("Date and time functions");
      _builder.append(_heading_3, "");
      _builder.newLineIfNotEmpty();
      Field<Date> _dateAdd = DSL.dateAdd(Functions.date, Functions.number);
      Method _method_55 = DSL.class.getMethod("dateAdd", Field.class, Field.class);
      String _expr_55 = Functions.expr("Date add function", _dateAdd, _method_55);
      _builder.append(_expr_55, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _dateDiff = DSL.dateDiff(Functions.date, Functions.date);
      Method _method_56 = DSL.class.getMethod("dateDiff", Field.class, Field.class);
      String _expr_56 = Functions.expr("Date diff function", _dateDiff, _method_56);
      _builder.append(_expr_56, "");
      _builder.newLineIfNotEmpty();
      Field<Timestamp> _timestampAdd = DSL.timestampAdd(Functions.timestamp, Functions.number);
      Method _method_57 = DSL.class.getMethod("timestampAdd", Field.class, Field.class);
      String _expr_57 = Functions.expr("Timestamp add function", _timestampAdd, _method_57);
      _builder.append(_expr_57, "");
      _builder.newLineIfNotEmpty();
      Field<DayToSecond> _timestampDiff = DSL.timestampDiff(Functions.timestamp, Functions.timestamp);
      Method _method_58 = DSL.class.getMethod("timestampDiff", Field.class, Field.class);
      String _expr_58 = Functions.expr("Timestamp diff function", _timestampDiff, _method_58);
      _builder.append(_expr_58, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _year = DSL.year(Functions.date);
      Method _method_59 = DSL.class.getMethod("year", Field.class);
      String _expr_59 = Functions.expr("Extract the year from a date", _year, _method_59);
      _builder.append(_expr_59, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _month = DSL.month(Functions.date);
      Method _method_60 = DSL.class.getMethod("month", Field.class);
      String _expr_60 = Functions.expr("Extract the month from a date", _month, _method_60);
      _builder.append(_expr_60, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _day = DSL.day(Functions.date);
      Method _method_61 = DSL.class.getMethod("day", Field.class);
      String _expr_61 = Functions.expr("Extract the day from a date", _day, _method_61);
      _builder.append(_expr_61, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _hour = DSL.hour(Functions.date);
      Method _method_62 = DSL.class.getMethod("hour", Field.class);
      String _expr_62 = Functions.expr("Extract the hour from a date", _hour, _method_62);
      _builder.append(_expr_62, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _minute = DSL.minute(Functions.date);
      Method _method_63 = DSL.class.getMethod("minute", Field.class);
      String _expr_63 = Functions.expr("Extract the minute from a date", _minute, _method_63);
      _builder.append(_expr_63, "");
      _builder.newLineIfNotEmpty();
      Field<Integer> _second = DSL.second(Functions.date);
      Method _method_64 = DSL.class.getMethod("second", Field.class);
      String _expr_64 = Functions.expr("Extract the second from a date", _second, _method_64);
      _builder.append(_expr_64, "");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _heading_4 = Functions.heading("Other functions");
      _builder.append(_heading_4, "");
      _builder.newLineIfNotEmpty();
      Field<String> _coalesce = DSL.<String>coalesce(Functions.e1, Functions.e2);
      Method _method_65 = DSL.class.getMethod("coalesce", Field.class, Field[].class);
      String _expr_65 = Functions.expr("Coalesce function", _coalesce, _method_65);
      _builder.append(_expr_65, "");
      _builder.newLineIfNotEmpty();
      Field<String> _decode = DSL.<String, String>decode(Functions.e1, Functions.e2, Functions.e3, Functions.e4);
      Method _method_66 = DSL.class.getMethod("decode", Field.class, Field.class, Field.class, Field[].class);
      String _expr_66 = Functions.expr("Decode function", _decode, _method_66);
      _builder.append(_expr_66, "");
      _builder.newLineIfNotEmpty();
      Field<String> _greatest = DSL.<String>greatest(Functions.e1, Functions.e2, Functions.e3);
      Method _method_67 = DSL.class.getMethod("greatest", Field.class, Field[].class);
      String _expr_67 = Functions.expr("Greatest function", _greatest, _method_67);
      _builder.append(_expr_67, "");
      _builder.newLineIfNotEmpty();
      Field<String> _least = DSL.<String>least(Functions.e1, Functions.e2, Functions.e3);
      Method _method_68 = DSL.class.getMethod("least", Field.class, Field[].class);
      String _expr_68 = Functions.expr("Least function", _least, _method_68);
      _builder.append(_expr_68, "");
      _builder.newLineIfNotEmpty();
      Field<String> _nullif = DSL.<String>nullif(Functions.e1, Functions.e2);
      Method _method_69 = DSL.class.getMethod("nullif", Field.class, Field.class);
      String _expr_69 = Functions.expr("Null if function", _nullif, _method_69);
      _builder.append(_expr_69, "");
      _builder.newLineIfNotEmpty();
      Field<String> _nvl = DSL.<String>nvl(Functions.e1, Functions.e2);
      Method _method_70 = DSL.class.getMethod("nvl", Field.class, Field.class);
      String _expr_70 = Functions.expr("Nvl function", _nvl, _method_70);
      _builder.append(_expr_70, "");
      _builder.newLineIfNotEmpty();
      Field<String> _nvl2 = DSL.<String>nvl2(Functions.e1, Functions.e2, Functions.e3);
      Method _method_71 = DSL.class.getMethod("nvl2", Field.class, Field.class, Field.class);
      String _expr_71 = Functions.expr("Nvl2 function", _nvl2, _method_71);
      _builder.append(_expr_71, "");
      _builder.newLineIfNotEmpty();
      _builder.append("</table>");
      _builder.newLine();
      System.out.println(_builder);
    } catch (Throwable _e_1) {
      throw Exceptions.sneakyThrow(_e_1);
    }
  }
  
  public static String heading(final String description) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<tr>");
    _builder.newLine();
    _builder.append("<td colspan=\"16\">");
    _builder.newLine();
    _builder.append("<h3>");
    _builder.append(description, "");
    _builder.append("<h3>");
    _builder.newLineIfNotEmpty();
    _builder.append("</td>");
    _builder.newLine();
    _builder.append("</tr>");
    _builder.newLine();
    _builder.append("<tr>");
    _builder.newLine();
    _builder.append("<td class=\"description\">Function description</td>");
    _builder.newLine();
    {
      EnumSet<SQLDialect> _families = Functions.families();
      for(final SQLDialect d : _families) {
        _builder.append("<td class=\"");
        String _nameLC = d.getNameLC();
        _builder.append(_nameLC, "");
        _builder.append("\">");
        String _name = d.name();
        _builder.append(_name, "");
        _builder.append("</td>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</tr>");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static String expr(final String description, final QueryPart p, final Method method) {
    final Support support = method.<Support>getAnnotation(Support.class);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<tr>");
    _builder.newLine();
    _builder.append("<td class=\"description\">");
    _builder.append(description, "");
    _builder.append("</td>");
    _builder.newLineIfNotEmpty();
    {
      EnumSet<SQLDialect> _families = Functions.families();
      for(final SQLDialect d : _families) {
        _builder.append("<td class=\"");
        String _name = d.name();
        _builder.append(_name, "");
        _builder.append("\">");
        {
          SQLDialect[] _value = support.value();
          EnumSet<SQLDialect> _families_1 = Functions.families(_value);
          boolean _contains = _families_1.contains(d);
          if (_contains) {
            _builder.append("<code><pre>");
            Settings _settings = new Settings();
            Settings _withRenderSchema = _settings.withRenderSchema(Boolean.valueOf(false));
            Settings _withRenderNameStyle = _withRenderSchema.withRenderNameStyle(RenderNameStyle.AS_IS);
            Settings _withStatementType = _withRenderNameStyle.withStatementType(StatementType.STATIC_STATEMENT);
            DSLContext _using = DSL.using(d, _withStatementType);
            String _render = _using.render(p);
            _builder.append(_render, "");
            _builder.append("</pre></code>");
          } else {
            _builder.append("&nbsp;");
          }
        }
        _builder.append("</td>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</tr>");
    _builder.newLine();
    return _builder.toString();
  }
}
