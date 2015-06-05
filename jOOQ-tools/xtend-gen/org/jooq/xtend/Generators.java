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
package org.jooq.xtend;

import com.google.common.base.Objects;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jooq.xtend.Util;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("all")
public abstract class Generators {
  public File file(final String className) {
    boolean _contains = className.contains("xtend");
    if (_contains) {
      String _replace = className.replace(".", "/");
      String _plus = ("./../jOOQ-xtend/src/main/xtend/" + _replace);
      String _plus_1 = (_plus + ".xtend");
      return new File(_plus_1);
    } else {
      boolean _contains_1 = className.contains("scala");
      if (_contains_1) {
        String _replace_1 = className.replace(".", "/");
        String _plus_2 = ("./../jOOQ-scala/src/main/scala/" + _replace_1);
        String _plus_3 = (_plus_2 + ".scala");
        return new File(_plus_3);
      } else {
        boolean _contains_2 = className.contains("lambda");
        if (_contains_2) {
          String _replace_2 = className.replace(".", "/");
          String _plus_4 = ("./../../jOOL/src/main/java/" + _replace_2);
          String _plus_5 = (_plus_4 + ".java");
          return new File(_plus_5);
        } else {
          String _replace_3 = className.replace(".", "/");
          String _plus_6 = ("./../jOOQ/src/main/java/" + _replace_3);
          String _plus_7 = (_plus_6 + ".java");
          return new File(_plus_7);
        }
      }
    }
  }
  
  public String read(final String className) {
    File _file = this.file(className);
    return this.read(_file);
  }
  
  public String read(final File file) {
    try {
      final RandomAccessFile f = new RandomAccessFile(file, "r");
      try {
        long _length = f.length();
        final byte[] contents = Util.newByteArray(_length);
        f.readFully(contents);
        return new String(contents);
      } catch (final Throwable _t) {
        if (_t instanceof IOException) {
          final IOException e = (IOException)_t;
          e.printStackTrace();
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      } finally {
        f.close();
      }
      return null;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void insert(final String className, final CharSequence contents, final String section) {
    final StringBuilder result = new StringBuilder();
    final String original = this.read(className);
    final String start = (("// [jooq-tools] START [" + section) + "]");
    final String end = (("// [jooq-tools] END [" + section) + "]");
    int _indexOf = original.indexOf(start);
    int _length = start.length();
    int _plus = (_indexOf + _length);
    int _plus_1 = (_plus + 1);
    String _substring = original.substring(0, _plus_1);
    result.append(_substring);
    result.append(contents);
    result.append("\n");
    int _indexOf_1 = original.indexOf(end);
    String _substring_1 = original.substring(_indexOf_1);
    result.append(_substring_1);
    this.write(className, result, section);
  }
  
  public void write(final String className, final CharSequence contents) {
    this.write(className, contents, null);
  }
  
  public void write(final String className, final CharSequence contents, final String section) {
    final File file = this.file(className);
    this.write(file, contents, section);
  }
  
  public void write(final File file, final CharSequence contents) {
    this.write(file, contents, null);
  }
  
  public void write(final File file, final CharSequence contents, final String section) {
    File _parentFile = file.getParentFile();
    _parentFile.mkdirs();
    try {
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(section, null));
      if (_notEquals) {
        _xifexpression = ((" (section: " + section) + ")");
      } else {
        _xifexpression = "";
      }
      String _plus = (("Writing " + file) + _xifexpression);
      System.out.println(_plus);
      final FileWriter fw = new FileWriter(file);
      String _string = contents.toString();
      String _replace = _string.replace("\r\n", "\n");
      fw.append(_replace);
      fw.flush();
      fw.close();
    } catch (final Throwable _t) {
      if (_t instanceof IOException) {
        final IOException e = (IOException)_t;
        e.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public String first(final int degree) {
    String _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(degree, 1)) {
        _matched=true;
        _switchResult = "first";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 2)) {
        _matched=true;
        _switchResult = "second";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 3)) {
        _matched=true;
        _switchResult = "third";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 4)) {
        _matched=true;
        _switchResult = "fourth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 5)) {
        _matched=true;
        _switchResult = "fifth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 6)) {
        _matched=true;
        _switchResult = "sixth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 7)) {
        _matched=true;
        _switchResult = "seventh";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 8)) {
        _matched=true;
        _switchResult = "eighth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 9)) {
        _matched=true;
        _switchResult = "ninth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 10)) {
        _matched=true;
        _switchResult = "tenth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 11)) {
        _matched=true;
        _switchResult = "eleventh";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 12)) {
        _matched=true;
        _switchResult = "twelfth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 13)) {
        _matched=true;
        _switchResult = "thirteenth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 14)) {
        _matched=true;
        _switchResult = "fourteenth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 15)) {
        _matched=true;
        _switchResult = "fifteenth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 16)) {
        _matched=true;
        _switchResult = "sixteenth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 17)) {
        _matched=true;
        _switchResult = "seventeenth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 18)) {
        _matched=true;
        _switchResult = "eighteenth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 19)) {
        _matched=true;
        _switchResult = "ninteenth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 20)) {
        _matched=true;
        _switchResult = "twentieth";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 21)) {
        _matched=true;
        _switchResult = "twenty-first";
      }
    }
    if (!_matched) {
      if (Objects.equal(degree, 22)) {
        _matched=true;
        _switchResult = "twenty-second";
      }
    }
    if (!_matched) {
      if (((degree % 10) == 1)) {
        _matched=true;
        _switchResult = (Integer.valueOf(degree) + "st");
      }
    }
    if (!_matched) {
      if (((degree % 10) == 2)) {
        _matched=true;
        _switchResult = (Integer.valueOf(degree) + "nd");
      }
    }
    if (!_matched) {
      if (((degree % 10) == 3)) {
        _matched=true;
        _switchResult = (Integer.valueOf(degree) + "rd");
      }
    }
    if (!_matched) {
      _switchResult = (Integer.valueOf(degree) + "th");
    }
    return _switchResult;
  }
  
  public CharSequence classHeader() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* All rights reserved.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* This work is dual-licensed");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* - under the Apache Software License 2.0 (the \"ASL\")");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* - under the jOOQ License and Maintenance Agreement (the \"jOOQ License\")");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* =============================================================================");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* You may choose which license applies to you:");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* - If you\'re using this work with Open Source databases, you may choose");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*   either ASL or jOOQ License.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* - If you\'re using this work with at least one commercial database, you must");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*   choose jOOQ License");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* For more information, please visit http://www.jooq.org/licenses");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Apache Software License 2.0:");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* -----------------------------------------------------------------------------");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Licensed under the Apache License, Version 2.0 (the \"License\");");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* you may not use this file except in compliance with the License.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* You may obtain a copy of the License at");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*  http://www.apache.org/licenses/LICENSE-2.0");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Unless required by applicable law or agreed to in writing, software");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* distributed under the License is distributed on an \"AS IS\" BASIS,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* See the License for the specific language governing permissions and");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* limitations under the License.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* jOOQ License and Maintenance Agreement:");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* -----------------------------------------------------------------------------");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Data Geekery grants the Customer the non-exclusive, timely limited and");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* non-transferable license to install and use the Software under the terms of");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* the jOOQ License and Maintenance Agreement.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* This library is distributed with a LIMITED WARRANTY. See the jOOQ License");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* and Maintenance Agreement for more details: http://www.jooq.org/licensing");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generatedAnnotation() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@Generated(\"This class was generated using jOOQ-tools\")");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generatedMethod() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@Generated(\"This method was generated using jOOQ-tools\")");
    _builder.newLine();
    return _builder;
  }
  
  /**
   * A comma-separated list of types
   * <p>
   * <code>T1, T2, .., T[N]</code>
   */
  public String TN(final int degree) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = "Object...";
    } else {
      _xifexpression = this.TN(1, degree);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of types
   * <p>
   * <code>T[from], .., T[to]</code>
   */
  public String TN(final int from, final int to) {
    return this.XXXn(from, to, "T");
  }
  
  /**
   * A comma-separated list of identifier references
   * <p>
   * <code>t1, t2, .., t[N]</code>
   */
  public String tn(final int degree) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = "values";
    } else {
      IntegerRange _upTo = new IntegerRange(1, degree);
      final Function1<Integer, CharSequence> _function = (Integer e) -> {
        return ("t" + e);
      };
      _xifexpression = IterableExtensions.<Integer>join(_upTo, ", ", _function);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of identifier references
   * <p>
   * <code>v1, v2, .., v[N]</code>
   */
  public String vn(final int degree) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = "values";
    } else {
      _xifexpression = this.vn(1, degree);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of identifier references
   * <p>
   * <code>v[from], .., v[to]</code>
   */
  public String vn(final int from, final int to) {
    return this.XXXn(from, to, "v");
  }
  
  /**
   * A comma-separated list of identifier references
   * <p>
   * <code>t1, t2, .., t[N]</code>
   */
  public String val_tn(final int degree) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = "values";
    } else {
      IntegerRange _upTo = new IntegerRange(1, degree);
      final Function1<Integer, CharSequence> _function = (Integer e) -> {
        return (("val(t" + e) + ")");
      };
      _xifexpression = IterableExtensions.<Integer>join(_upTo, ", ", _function);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of identifier declarations
   * <p>
   * <code>T1 t1, T2 t2, .., T[N] t[N]</code>
   */
  public String TN_tn(final int degree) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = "Object... values";
    } else {
      IntegerRange _upTo = new IntegerRange(1, degree);
      final Function1<Integer, CharSequence> _function = (Integer e) -> {
        return ((("T" + e) + " t") + e);
      };
      _xifexpression = IterableExtensions.<Integer>join(_upTo, ", ", _function);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of identifier declarations
   * <p>
   * <code>Object t1, Object t2, .., Object t[N]</code>
   */
  public String Object_tn(final int degree) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = "Object... values";
    } else {
      IntegerRange _upTo = new IntegerRange(1, degree);
      final Function1<Integer, CharSequence> _function = (Integer e) -> {
        return ("Object t" + e);
      };
      _xifexpression = IterableExtensions.<Integer>join(_upTo, ", ", _function);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of identifier declarations
   * <p>
   * <code>T1 t1, T2 t2, .., T[N] t[N]</code>
   */
  public String TN_XXXn(final int degree, final String XXX) {
    return this.TN_XXXn(1, degree, XXX);
  }
  
  /**
   * A comma-separated list of identifier declarations
   * <p>
   * <code>T1 t1, T2 t2, .., T[N] t[N]</code>
   */
  public String TN_XXXn(final int from, final int to, final String XXX) {
    String _xifexpression = null;
    if ((to == 0)) {
      _xifexpression = (("Object... " + XXX) + "s");
    } else {
      IntegerRange _upTo = new IntegerRange(from, to);
      final Function1<Integer, CharSequence> _function = (Integer e) -> {
        return (((("T" + e) + " ") + XXX) + e);
      };
      _xifexpression = IterableExtensions.<Integer>join(_upTo, ", ", _function);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of field declarations
   * <p>
   * <code>Field&lt;T1> t1, Field&lt;T2> t2, .., Field&ltT[N]> t[N]</code>
   */
  public String Field_TN_tn(final int degree) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = "Field<?>... values";
    } else {
      IntegerRange _upTo = new IntegerRange(1, degree);
      final Function1<Integer, CharSequence> _function = (Integer e) -> {
        return ((("Field<T" + e) + "> t") + e);
      };
      _xifexpression = IterableExtensions.<Integer>join(_upTo, ", ", _function);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of field declarations
   * <p>
   * <code>Field t1, Field t2, .., Field t[N]</code>
   */
  public String Field_tn(final int degree) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = "Field<?>... values";
    } else {
      IntegerRange _upTo = new IntegerRange(1, degree);
      final Function1<Integer, CharSequence> _function = (Integer e) -> {
        return ("Field t" + e);
      };
      _xifexpression = IterableExtensions.<Integer>join(_upTo, ", ", _function);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of sort field declarations
   * <p>
   * <code>SortField t1, SortField t2, .., SortField t[N]</code>
   */
  public String SortField_tn(final int degree) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = "SortField<?>... values";
    } else {
      IntegerRange _upTo = new IntegerRange(1, degree);
      final Function1<Integer, CharSequence> _function = (Integer e) -> {
        return ("SortField t" + e);
      };
      _xifexpression = IterableExtensions.<Integer>join(_upTo, ", ", _function);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of field declarations
   * <p>
   * <code>Field&lt;T1> t1, Field&lt;T2> t2, .., Field&ltT[N]> t[N]</code>
   */
  public String Field_TN_XXXn(final int degree, final String XXX) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = (("Field<?>... " + XXX) + "s");
    } else {
      IntegerRange _upTo = new IntegerRange(1, degree);
      final Function1<Integer, CharSequence> _function = (Integer e) -> {
        return (((("Field<T" + e) + "> ") + XXX) + e);
      };
      _xifexpression = IterableExtensions.<Integer>join(_upTo, ", ", _function);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of field declarations
   * <p>
   * <code>Field&lt;T1> field1, Field&lt;T2> field2, .., Field&ltT[N]> field[N]</code>
   */
  public String Field_TN_fieldn(final int degree) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = "Field<?>... fields";
    } else {
      _xifexpression = this.Field_TN_XXXn(degree, "field");
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of field declarations
   * <p>
   * <code>SelectField&lt;T1> field1, SelectField&lt;T2> field2, .., SelectField&ltT[N]> field[N]</code>
   */
  public String SelectField_TN_fieldn(final int degree) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = "SelectField<?>... fields";
    } else {
      IntegerRange _upTo = new IntegerRange(1, degree);
      final Function1<Integer, CharSequence> _function = (Integer e) -> {
        return ((("SelectField<T" + e) + "> field") + e);
      };
      _xifexpression = IterableExtensions.<Integer>join(_upTo, ", ", _function);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of field declarations
   * <p>
   * <code>SortField&lt;T1> field1, SortField&lt;T2> field2, .., SortField&ltT[N]> field[N]</code>
   */
  public String SortField_TN_fieldn(final int degree) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = "SortField<?>... fields";
    } else {
      IntegerRange _upTo = new IntegerRange(1, degree);
      final Function1<Integer, CharSequence> _function = (Integer e) -> {
        return ((("SortField<T" + e) + "> field") + e);
      };
      _xifexpression = IterableExtensions.<Integer>join(_upTo, ", ", _function);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of field references
   * <p>
   * <code>field1, field2, .., field[N]</code>
   */
  public String fieldn(final int degree) {
    return this.XXXn(degree, "field");
  }
  
  /**
   * A comma-separated list of field references
   * <p>
   * <code>XXX1, XXX2, .., XXX[N]</code>
   */
  public String XXXn(final int degree, final String XXX) {
    String _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = (XXX + "s");
    } else {
      _xifexpression = this.XXXn(1, degree, XXX);
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of field references
   * <p>
   * <code>XXX1, XXX2, .., XXX[N]</code>
   */
  public String XXXn(final int from, final int to, final String XXX) {
    String _xifexpression = null;
    if ((from <= to)) {
      IntegerRange _upTo = new IntegerRange(from, to);
      final Function1<Integer, CharSequence> _function = (Integer e) -> {
        return (XXX + e);
      };
      _xifexpression = IterableExtensions.<Integer>join(_upTo, ", ", _function);
    } else {
      _xifexpression = "";
    }
    return _xifexpression;
  }
  
  /**
   * A comma-separated list of field references
   * <p>
   * Unlike {@link #fieldn(int)}, this will return at most 5 fields
   * <p>
   * <code>field1, field2, .., field[N]</code>
   */
  public String field1_field2_fieldn(final int degree) {
    return this.XXX1_XXX2_XXXn(degree, "field");
  }
  
  /**
   * A comma-separated list of literals
   * <p>
   * Unlike {@link #fieldn(int)}, this will return at most 5 fields
   * <p>
   * <code>XXX1, XXX2, .., XXX[N]</code>
   */
  public String XXX1_XXX2_XXXn(final int degree, final String XXX) {
    if ((degree <= 5)) {
      return this.fieldn(degree);
    } else {
      IntegerRange _upTo = new IntegerRange(1, 3);
      final Function1<Integer, CharSequence> _function = (Integer e) -> {
        return (XXX + e);
      };
      String _join = IterableExtensions.<Integer>join(_upTo, ", ", _function);
      String _plus = (_join + 
        ", .., ");
      IntegerRange _upTo_1 = new IntegerRange((degree - 1), degree);
      final Function1<Integer, CharSequence> _function_1 = (Integer e) -> {
        return (XXX + e);
      };
      String _join_1 = IterableExtensions.<Integer>join(_upTo_1, ", ", _function_1);
      return (_plus + _join_1);
    }
  }
  
  /**
   * A comma-separated list of value constructor references
   * <p>
   * <code>val(t1), val(t2), .., val(t[N])</code>
   */
  public String Utils_field_tn(final int degree) {
    IntegerRange _upTo = new IntegerRange(1, degree);
    final Function1<Integer, CharSequence> _function = (Integer e) -> {
      return (("Utils.field(t" + e) + ")");
    };
    return IterableExtensions.<Integer>join(_upTo, ", ", _function);
  }
  
  /**
   * A numeric degree or "N"
   */
  public Object degreeOrN(final int degree) {
    return this.degreeOr(degree, "N");
  }
  
  /**
   * A numeric degree or [nothing]
   */
  public Object degreeOr(final int degree) {
    return this.degreeOr(degree, "");
  }
  
  /**
   * A numeric degree or [or]
   */
  public Object degreeOr(final int degree, final String or) {
    Object _xifexpression = null;
    if ((degree == 0)) {
      _xifexpression = or;
    } else {
      _xifexpression = Integer.valueOf(degree);
    }
    return ((Comparable<?>)_xifexpression);
  }
  
  /**
   * The generic type of a class, e.g. <code>""</code> or <code>&lt;T1, T2, T3></code>
   */
  public CharSequence type(final int degree) {
    return this.type(degree, "");
  }
  
  /**
   * The generic type of a class, e.g. <code>""</code> or <code>&lt;T1, T2, T3></code>
   */
  public CharSequence type(final int degree, final String spacer) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((degree > 0)) {
        _builder.append("<");
        String _TN = this.TN(degree);
        _builder.append(_TN, "");
        _builder.append(">");
        _builder.append(spacer, "");
      }
    }
    return _builder;
  }
  
  /**
   * The generic type suffix of a class, e.g. <code>N</code> or <code>3&lt;T1, T2, T3></code>
   */
  public CharSequence typeSuffix(final int degree) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _typeSuffixRaw = this.typeSuffixRaw(degree);
    _builder.append(_typeSuffixRaw, "");
    CharSequence _type = this.type(degree);
    _builder.append(_type, "");
    return _builder;
  }
  
  /**
   * The "raw" generic type suffix of a class, e.g. <code>N</code> or <code>3</code>
   */
  public CharSequence typeSuffixRaw(final int degree) {
    StringConcatenation _builder = new StringConcatenation();
    Object _degreeOrN = this.degreeOrN(degree);
    _builder.append(_degreeOrN, "");
    return _builder;
  }
  
  /**
   * The generic type suffix of a record, e.g. <code>""</code> or <code>3&lt;T1, T2, T3></code>
   */
  public CharSequence recTypeSuffix(final int degree) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _recTypeSuffixRaw = this.recTypeSuffixRaw(degree);
    _builder.append(_recTypeSuffixRaw, "");
    CharSequence _type = this.type(degree);
    _builder.append(_type, "");
    return _builder;
  }
  
  /**
   * The "raw" generic type suffix of a record, e.g. <code>""</code> or <code>3</code>
   */
  public CharSequence recTypeSuffixRaw(final int degree) {
    StringConcatenation _builder = new StringConcatenation();
    Object _degreeOr = this.degreeOr(degree);
    _builder.append(_degreeOr, "");
    return _builder;
  }
}
