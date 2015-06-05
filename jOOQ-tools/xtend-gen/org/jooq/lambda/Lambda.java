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
package org.jooq.lambda;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jooq.xtend.Generators;

@SuppressWarnings("all")
public class Lambda extends Generators {
  public static void main(final String[] args) {
    final Lambda lambda = new Lambda();
    lambda.generateTuple();
    lambda.generateTuples();
    lambda.generateFunctions();
    lambda.generateZipStatic();
  }
  
  public int max() {
    return 8;
  }
  
  public CharSequence copyright() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Copyright (c) 2014-2015, Data Geekery GmbH, contact@datageekery.com");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
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
    _builder.append("*     http://www.apache.org/licenses/LICENSE-2.0");
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
    _builder.append("*/");
    _builder.newLine();
    return _builder;
  }
  
  public void generateZipStatic() {
    final StringBuilder out = new StringBuilder();
    int _max = this.max();
    IntegerRange _upTo = new IntegerRange(2, _max);
    for (final Integer degree : _upTo) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("/**");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* Zip ");
      _builder.append(degree, "     ");
      _builder.append(" streams into one.");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("* <p>");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* <code><pre>");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* // (tuple(1, \"a\"), tuple(2, \"b\"), tuple(3, \"c\"))");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* Seq.of(1, 2, 3).zip(Seq.of(\"a\", \"b\", \"c\"))");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* </pre></code>");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("*/");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("static <");
      String _TN = this.TN((degree).intValue());
      _builder.append(_TN, "    ");
      _builder.append("> Seq<Tuple");
      _builder.append(degree, "    ");
      _builder.append("<");
      String _TN_1 = this.TN((degree).intValue());
      _builder.append(_TN_1, "    ");
      _builder.append(">> zip(");
      IntegerRange _upTo_1 = new IntegerRange(1, (degree).intValue());
      final Function1<Integer, String> _function = (Integer d) -> {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("Stream<T");
        _builder_1.append(d, "");
        _builder_1.append("> s");
        _builder_1.append(d, "");
        return _builder_1.toString();
      };
      Iterable<String> _map = IterableExtensions.<Integer, String>map(_upTo_1, _function);
      String _join = IterableExtensions.join(_map, ", ");
      _builder.append(_join, "    ");
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      _builder.append("        ");
      _builder.append("return zip(");
      String _XXXn = this.XXXn((degree).intValue(), "s");
      _builder.append(_XXXn, "        ");
      _builder.append(", Tuple::tuple);");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      out.append(_builder);
    }
    int _max_1 = this.max();
    IntegerRange _upTo_2 = new IntegerRange(2, _max_1);
    for (final Integer degree_1 : _upTo_2) {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.newLine();
      _builder_1.append("    ");
      _builder_1.append("/**");
      _builder_1.newLine();
      _builder_1.append("     ");
      _builder_1.append("* Zip ");
      _builder_1.append(degree_1, "     ");
      _builder_1.append(" streams into one using a ");
      {
        if (((degree_1).intValue() == 2)) {
          _builder_1.append("{@link BiFunction}");
        } else {
          _builder_1.append("{@link Function");
          _builder_1.append(degree_1, "     ");
          _builder_1.append("}");
        }
      }
      _builder_1.append(" to produce resulting values.");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("     ");
      _builder_1.append("* <p>");
      _builder_1.newLine();
      _builder_1.append("     ");
      _builder_1.append("* <code><pre>");
      _builder_1.newLine();
      _builder_1.append("     ");
      _builder_1.append("* // (\"1:a\", \"2:b\", \"3:c\")");
      _builder_1.newLine();
      _builder_1.append("     ");
      _builder_1.append("* Seq.of(1, 2, 3).zip(Seq.of(\"a\", \"b\", \"c\"), (i, s) -> i + \":\" + s)");
      _builder_1.newLine();
      _builder_1.append("     ");
      _builder_1.append("* </pre></code>");
      _builder_1.newLine();
      _builder_1.append("     ");
      _builder_1.append("*/");
      _builder_1.newLine();
      _builder_1.append("    ");
      _builder_1.append("static <");
      String _TN_2 = this.TN((degree_1).intValue());
      _builder_1.append(_TN_2, "    ");
      _builder_1.append(", R> Seq<R> zip(");
      IntegerRange _upTo_3 = new IntegerRange(1, (degree_1).intValue());
      final Function1<Integer, String> _function_1 = (Integer d) -> {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("Stream<T");
        _builder_2.append(d, "");
        _builder_2.append("> s");
        _builder_2.append(d, "");
        return _builder_2.toString();
      };
      Iterable<String> _map_1 = IterableExtensions.<Integer, String>map(_upTo_3, _function_1);
      String _join_1 = IterableExtensions.join(_map_1, ", ");
      _builder_1.append(_join_1, "    ");
      _builder_1.append(", ");
      {
        if (((degree_1).intValue() == 2)) {
          _builder_1.append("BiFunction");
        } else {
          _builder_1.append("Function");
          _builder_1.append(degree_1, "    ");
        }
      }
      _builder_1.append("<");
      String _TN_3 = this.TN((degree_1).intValue());
      _builder_1.append(_TN_3, "    ");
      _builder_1.append(", R> zipper) {");
      _builder_1.newLineIfNotEmpty();
      {
        IntegerRange _upTo_4 = new IntegerRange(1, (degree_1).intValue());
        for(final Integer d : _upTo_4) {
          _builder_1.append("        ");
          _builder_1.append("final Iterator<T");
          _builder_1.append(d, "        ");
          _builder_1.append("> it");
          _builder_1.append(d, "        ");
          _builder_1.append(" = s");
          _builder_1.append(d, "        ");
          _builder_1.append(".iterator();");
          _builder_1.newLineIfNotEmpty();
        }
      }
      _builder_1.newLine();
      _builder_1.append("        ");
      _builder_1.append("class Zip implements Iterator<R> {");
      _builder_1.newLine();
      _builder_1.append("            ");
      _builder_1.append("@Override");
      _builder_1.newLine();
      _builder_1.append("            ");
      _builder_1.append("public boolean hasNext() {");
      _builder_1.newLine();
      _builder_1.append("                ");
      _builder_1.append("return ");
      {
        IntegerRange _upTo_5 = new IntegerRange(1, (degree_1).intValue());
        boolean _hasElements = false;
        for(final Integer d_1 : _upTo_5) {
          if (!_hasElements) {
            _hasElements = true;
          } else {
            _builder_1.appendImmediate(" && ", "                ");
          }
          _builder_1.append("it");
          _builder_1.append(d_1, "                ");
          _builder_1.append(".hasNext()");
        }
      }
      _builder_1.append(";");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("            ");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("            ");
      _builder_1.append("@Override");
      _builder_1.newLine();
      _builder_1.append("            ");
      _builder_1.append("public R next() {");
      _builder_1.newLine();
      _builder_1.append("                ");
      _builder_1.append("return zipper.apply(");
      {
        IntegerRange _upTo_6 = new IntegerRange(1, (degree_1).intValue());
        boolean _hasElements_1 = false;
        for(final Integer d_2 : _upTo_6) {
          if (!_hasElements_1) {
            _hasElements_1 = true;
          } else {
            _builder_1.appendImmediate(", ", "                ");
          }
          _builder_1.append("it");
          _builder_1.append(d_2, "                ");
          _builder_1.append(".next()");
        }
      }
      _builder_1.append(");");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("            ");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("        ");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("        ");
      _builder_1.append("return seq(new Zip());");
      _builder_1.newLine();
      _builder_1.append("    ");
      _builder_1.append("}");
      _builder_1.newLine();
      out.append(_builder_1);
    }
    this.insert("org.jooq.lambda.Seq", out, "zip-static");
  }
  
  public void generateTuple() {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _copyright = this.copyright();
    _builder.append(_copyright, "");
    _builder.newLineIfNotEmpty();
    _builder.append("package org.jooq.lambda.tuple;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import java.util.List;");
    _builder.newLine();
    _builder.append("import java.util.stream.Collector;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* A tuple.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @author Lukas Eder");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("public interface Tuple extends Iterable<Object> {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Construct a tuple of degree 0.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("static Tuple0 tuple() {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("return new Tuple0();");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    {
      int _max = this.max();
      IntegerRange _upTo = new IntegerRange(1, _max);
      for(final Integer degree : _upTo) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Construct a tuple of degree ");
        _builder.append(degree, "     ");
        _builder.append(".");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("static <");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "    ");
        _builder.append("> Tuple");
        _builder.append(degree, "    ");
        _builder.append("<");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "    ");
        _builder.append("> tuple(");
        String _TN_XXXn = this.TN_XXXn((degree).intValue(), "v");
        _builder.append(_TN_XXXn, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("return new Tuple");
        _builder.append(degree, "        ");
        _builder.append("<>(");
        String _vn = this.vn((degree).intValue());
        _builder.append(_vn, "        ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      int _max_1 = this.max();
      IntegerRange _upTo_1 = new IntegerRange(1, _max_1);
      for(final Integer degree_1 : _upTo_1) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Construct a tuple collector of degree ");
        _builder.append(degree_1, "     ");
        _builder.append(".");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("static <T, ");
        String _XXXn = this.XXXn((degree_1).intValue(), "A");
        _builder.append(_XXXn, "    ");
        _builder.append(", ");
        String _XXXn_1 = this.XXXn((degree_1).intValue(), "D");
        _builder.append(_XXXn_1, "    ");
        _builder.append("> Collector<T, Tuple");
        _builder.append(degree_1, "    ");
        _builder.append("<");
        String _XXXn_2 = this.XXXn((degree_1).intValue(), "A");
        _builder.append(_XXXn_2, "    ");
        _builder.append(">, Tuple");
        _builder.append(degree_1, "    ");
        _builder.append("<");
        String _XXXn_3 = this.XXXn((degree_1).intValue(), "D");
        _builder.append(_XXXn_3, "    ");
        _builder.append(">> collectors(");
        _builder.newLineIfNotEmpty();
        {
          IntegerRange _upTo_2 = new IntegerRange(1, (degree_1).intValue());
          for(final Integer d : _upTo_2) {
            _builder.append("      ");
            {
              if (((d).intValue() > 1)) {
                _builder.append(",");
              } else {
                _builder.append(" ");
              }
            }
            _builder.append(" Collector<T, A");
            _builder.append(d, "      ");
            _builder.append(", D");
            _builder.append(d, "      ");
            _builder.append("> collector");
            _builder.append(d, "      ");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("    ");
        _builder.append(") {");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("return Collector.<T, Tuple");
        _builder.append(degree_1, "        ");
        _builder.append("<");
        String _XXXn_4 = this.XXXn((degree_1).intValue(), "A");
        _builder.append(_XXXn_4, "        ");
        _builder.append(">, Tuple");
        _builder.append(degree_1, "        ");
        _builder.append("<");
        String _XXXn_5 = this.XXXn((degree_1).intValue(), "D");
        _builder.append(_XXXn_5, "        ");
        _builder.append(">>of(");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("() -> tuple(");
        _builder.newLine();
        {
          IntegerRange _upTo_3 = new IntegerRange(1, (degree_1).intValue());
          for(final Integer d_1 : _upTo_3) {
            _builder.append("              ");
            {
              if (((d_1).intValue() > 1)) {
                _builder.append(",");
              } else {
                _builder.append(" ");
              }
            }
            _builder.append(" collector");
            _builder.append(d_1, "              ");
            _builder.append(".supplier().get()");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("            ");
        _builder.append("),");
        _builder.newLine();
        _builder.append("            ");
        _builder.append("(a, t) -> {");
        _builder.newLine();
        {
          IntegerRange _upTo_4 = new IntegerRange(1, (degree_1).intValue());
          for(final Integer d_2 : _upTo_4) {
            _builder.append("                ");
            _builder.append("collector");
            _builder.append(d_2, "                ");
            _builder.append(".accumulator().accept(a.v");
            _builder.append(d_2, "                ");
            _builder.append(", t);");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("            ");
        _builder.append("},");
        _builder.newLine();
        _builder.append("            ");
        _builder.append("(a1, a2) -> tuple(");
        _builder.newLine();
        {
          IntegerRange _upTo_5 = new IntegerRange(1, (degree_1).intValue());
          for(final Integer d_3 : _upTo_5) {
            _builder.append("              ");
            {
              if (((d_3).intValue() > 1)) {
                _builder.append(",");
              } else {
                _builder.append(" ");
              }
            }
            _builder.append(" collector");
            _builder.append(d_3, "              ");
            _builder.append(".combiner().apply(a1.v");
            _builder.append(d_3, "              ");
            _builder.append(", a2.v");
            _builder.append(d_3, "              ");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("            ");
        _builder.append("),");
        _builder.newLine();
        _builder.append("            ");
        _builder.append("a -> tuple(");
        _builder.newLine();
        {
          IntegerRange _upTo_6 = new IntegerRange(1, (degree_1).intValue());
          for(final Integer d_4 : _upTo_6) {
            _builder.append("              ");
            {
              if (((d_4).intValue() > 1)) {
                _builder.append(",");
              } else {
                _builder.append(" ");
              }
            }
            _builder.append(" collector");
            _builder.append(d_4, "              ");
            _builder.append(".finisher().apply(a.v");
            _builder.append(d_4, "              ");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("            ");
        _builder.append(")");
        _builder.newLine();
        _builder.append("        ");
        _builder.append(");");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Create a new range.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("static <T extends Comparable<T>> Range<T> range(T t1, T t2) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("return new Range<>(t1, t2);");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Get an array representation of this tuple.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Object[] array();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Get a list representation of this tuple.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("List<?> list();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* The degree of this tuple.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("int degree();");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    this.write("org.jooq.lambda.tuple.Tuple", _builder);
  }
  
  public void generateTuples() {
    int _max = this.max();
    IntegerRange _upTo = new IntegerRange(0, _max);
    for (final Integer degree : _upTo) {
      StringConcatenation _builder = new StringConcatenation();
      CharSequence _copyright = this.copyright();
      _builder.append(_copyright, "");
      _builder.newLineIfNotEmpty();
      _builder.append("package org.jooq.lambda.tuple;");
      _builder.newLine();
      _builder.newLine();
      _builder.append("import java.io.Serializable;");
      _builder.newLine();
      _builder.append("import java.util.Arrays;");
      _builder.newLine();
      _builder.append("import java.util.Iterator;");
      _builder.newLine();
      _builder.append("import java.util.List;");
      _builder.newLine();
      _builder.append("import java.util.Objects;");
      _builder.newLine();
      {
        if (((degree).intValue() == 2)) {
          _builder.append("import java.util.Optional;");
          _builder.newLine();
        }
      }
      _builder.newLine();
      {
        if (((degree).intValue() != 1)) {
          _builder.append("import org.jooq.lambda.function.Function1;");
          _builder.newLine();
        }
      }
      _builder.append("import org.jooq.lambda.function.Function");
      _builder.append(degree, "");
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append("/**");
      _builder.newLine();
      _builder.append(" ");
      _builder.append("* A tuple of degree ");
      _builder.append(degree, " ");
      _builder.append(".");
      _builder.newLineIfNotEmpty();
      _builder.append(" ");
      _builder.append("*");
      _builder.newLine();
      _builder.append(" ");
      _builder.append("* @author Lukas Eder");
      _builder.newLine();
      _builder.append(" ");
      _builder.append("*/");
      _builder.newLine();
      _builder.append("public class Tuple");
      _builder.append(degree, "");
      {
        if (((degree).intValue() > 0)) {
          _builder.append("<");
          String _TN = this.TN((degree).intValue());
          _builder.append(_TN, "");
          _builder.append(">");
        }
      }
      _builder.append(" implements Tuple, Comparable<Tuple");
      _builder.append(degree, "");
      {
        if (((degree).intValue() > 0)) {
          _builder.append("<");
          String _TN_1 = this.TN((degree).intValue());
          _builder.append(_TN_1, "");
          _builder.append(">");
        }
      }
      _builder.append(">, Serializable, Cloneable {");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("private static final long serialVersionUID = 1L;");
      _builder.newLine();
      {
        if (((degree).intValue() > 0)) {
          _builder.newLine();
          {
            IntegerRange _upTo_1 = new IntegerRange(1, (degree).intValue());
            for(final Integer d : _upTo_1) {
              _builder.append("    ");
              _builder.append("public final T");
              _builder.append(d, "    ");
              _builder.append(" v");
              _builder.append(d, "    ");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
            }
          }
          {
            IntegerRange _upTo_2 = new IntegerRange(1, (degree).intValue());
            for(final Integer d_1 : _upTo_2) {
              _builder.newLine();
              _builder.append("    ");
              _builder.append("public T");
              _builder.append(d_1, "    ");
              _builder.append(" v");
              _builder.append(d_1, "    ");
              _builder.append("() {");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("    ");
              _builder.append("return v");
              _builder.append(d_1, "        ");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("}");
              _builder.newLine();
            }
          }
        }
      }
      {
        if (((degree).intValue() > 0)) {
          _builder.newLine();
          _builder.append("    ");
          _builder.append("public Tuple");
          _builder.append(degree, "    ");
          _builder.append("(Tuple");
          _builder.append(degree, "    ");
          _builder.append("<");
          String _TN_2 = this.TN((degree).intValue());
          _builder.append(_TN_2, "    ");
          _builder.append("> tuple) {");
          _builder.newLineIfNotEmpty();
          {
            IntegerRange _upTo_3 = new IntegerRange(1, (degree).intValue());
            for(final Integer d_2 : _upTo_3) {
              _builder.append("        ");
              _builder.append("this.v");
              _builder.append(d_2, "        ");
              _builder.append(" = tuple.v");
              _builder.append(d_2, "        ");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.append("    ");
          _builder.append("}");
          _builder.newLine();
          _builder.newLine();
          _builder.append("    ");
          _builder.append("public Tuple");
          _builder.append(degree, "    ");
          _builder.append("(");
          String _TN_XXXn = this.TN_XXXn((degree).intValue(), "v");
          _builder.append(_TN_XXXn, "    ");
          _builder.append(") {");
          _builder.newLineIfNotEmpty();
          {
            IntegerRange _upTo_4 = new IntegerRange(1, (degree).intValue());
            for(final Integer d_3 : _upTo_4) {
              _builder.append("        ");
              _builder.append("this.v");
              _builder.append(d_3, "        ");
              _builder.append(" = v");
              _builder.append(d_3, "        ");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.append("    ");
          _builder.append("}");
          _builder.newLine();
        } else {
          _builder.newLine();
          _builder.append("    ");
          _builder.append("public Tuple");
          _builder.append(degree, "    ");
          _builder.append("(Tuple0 tuple) {");
          _builder.newLineIfNotEmpty();
          _builder.append("    ");
          _builder.append("}");
          _builder.newLine();
          _builder.newLine();
          _builder.append("    ");
          _builder.append("public Tuple");
          _builder.append(degree, "    ");
          _builder.append("() {");
          _builder.newLineIfNotEmpty();
          _builder.append("    ");
          _builder.append("}");
          _builder.newLine();
        }
      }
      {
        int _max_1 = this.max();
        boolean _lessThan = ((degree).intValue() < _max_1);
        if (_lessThan) {
          _builder.newLine();
          _builder.append("    ");
          _builder.append("/**");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* Concatenate a value to this tuple.");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("*/");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("public final <T");
          _builder.append(((degree).intValue() + 1), "    ");
          _builder.append("> Tuple");
          _builder.append(((degree).intValue() + 1), "    ");
          _builder.append("<");
          String _TN_3 = this.TN(((degree).intValue() + 1));
          _builder.append(_TN_3, "    ");
          _builder.append("> concat(T");
          _builder.append(((degree).intValue() + 1), "    ");
          _builder.append(" value) {");
          _builder.newLineIfNotEmpty();
          _builder.append("    ");
          _builder.append("    ");
          _builder.append("return new Tuple");
          _builder.append(((degree).intValue() + 1), "        ");
          _builder.append("<>(");
          {
            if (((degree).intValue() > 0)) {
              String _XXXn = this.XXXn((degree).intValue(), "v");
              _builder.append(_XXXn, "        ");
              _builder.append(", ");
            }
          }
          _builder.append("value);");
          _builder.newLineIfNotEmpty();
          _builder.append("    ");
          _builder.append("}");
          _builder.newLine();
          {
            int _max_2 = this.max();
            IntegerRange _upTo_5 = new IntegerRange(((degree).intValue() + 1), _max_2);
            for(final Integer d_4 : _upTo_5) {
              _builder.newLine();
              _builder.append("    ");
              _builder.append("/**");
              _builder.newLine();
              _builder.append("    ");
              _builder.append(" ");
              _builder.append("* Concatenate a tuple to this tuple.");
              _builder.newLine();
              _builder.append("    ");
              _builder.append(" ");
              _builder.append("*/");
              _builder.newLine();
              _builder.append("    ");
              _builder.append("public final <");
              String _TN_4 = this.TN(((degree).intValue() + 1), (d_4).intValue());
              _builder.append(_TN_4, "    ");
              _builder.append("> Tuple");
              _builder.append(d_4, "    ");
              _builder.append("<");
              String _TN_5 = this.TN((d_4).intValue());
              _builder.append(_TN_5, "    ");
              _builder.append("> concat(Tuple");
              _builder.append(((d_4).intValue() - (degree).intValue()), "    ");
              _builder.append("<");
              String _TN_6 = this.TN(((degree).intValue() + 1), (d_4).intValue());
              _builder.append(_TN_6, "    ");
              _builder.append("> tuple) {");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("    ");
              _builder.append("return new Tuple");
              _builder.append(d_4, "        ");
              _builder.append("<>(");
              {
                if (((degree).intValue() > 0)) {
                  String _XXXn_1 = this.XXXn((degree).intValue(), "v");
                  _builder.append(_XXXn_1, "        ");
                  _builder.append(", ");
                }
              }
              String _XXXn_2 = this.XXXn(((d_4).intValue() - (degree).intValue()), "tuple.v");
              _builder.append(_XXXn_2, "        ");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("}");
              _builder.newLine();
            }
          }
        }
      }
      {
        if (((degree).intValue() == 2)) {
          _builder.newLine();
          _builder.append("    ");
          _builder.append("/**");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* Get a tuple with the two attributes swapped.");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("*/");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("public final Tuple2<T2, T1> swap() {");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("    ");
          _builder.append("return new Tuple2<>(v2, v1);");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("}");
          _builder.newLine();
          _builder.newLine();
          _builder.append("    ");
          _builder.append("/**");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* Whether two tuples overlap.");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* <p>");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* <code><pre>");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* // true");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* range(1, 3).overlaps(range(2, 4))");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("*");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* // false");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* range(1, 3).overlaps(range(5, 8))");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* </pre></code>");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("*/");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("public static final <T extends Comparable<T>> boolean overlaps(Tuple2<T, T> left, Tuple2<T, T> right) {");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("    ");
          _builder.append("return left.v1.compareTo(right.v2) <= 0");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("        ");
          _builder.append("&& left.v2.compareTo(right.v1) >= 0;");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("}");
          _builder.newLine();
          _builder.newLine();
          _builder.append("    ");
          _builder.append("/**");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* The intersection of two ranges.");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* <p>");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* <code><pre>");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* // (2, 3)");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* range(1, 3).intersect(range(2, 4))");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("*");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* // none");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* range(1, 3).intersect(range(5, 8))");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* </pre></code>");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("*/");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("public static final <T extends Comparable<T>> Optional<Tuple2<T, T>> intersect(Tuple2<T, T> left, Tuple2<T, T> right) {");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("    ");
          _builder.append("if (overlaps(left, right))");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("        ");
          _builder.append("return Optional.of(new Tuple2<>(");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("            ");
          _builder.append("left.v1.compareTo(right.v1) >= 0 ? left.v1 : right.v1,");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("            ");
          _builder.append("left.v2.compareTo(right.v2) <= 0 ? left.v2 : right.v2");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("        ");
          _builder.append("));");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("    ");
          _builder.append("else");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("        ");
          _builder.append("return Optional.empty();");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("}");
          _builder.newLine();
        }
      }
      _builder.newLine();
      _builder.append("    ");
      _builder.append("/**");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* Apply this tuple as arguments to a function.");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("*/");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public final <R> R map(Function");
      _builder.append(degree, "    ");
      _builder.append("<");
      {
        if (((degree).intValue() > 0)) {
          String _TN_7 = this.TN((degree).intValue());
          _builder.append(_TN_7, "    ");
          _builder.append(", ");
        }
      }
      _builder.append("R> function) {");
      _builder.newLineIfNotEmpty();
      _builder.append("        ");
      _builder.append("return function.apply(this);");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      {
        if (((degree).intValue() > 0)) {
          {
            IntegerRange _upTo_6 = new IntegerRange(1, (degree).intValue());
            for(final Integer d_5 : _upTo_6) {
              _builder.newLine();
              _builder.append("    ");
              _builder.append("/**");
              _builder.newLine();
              _builder.append("    ");
              _builder.append(" ");
              _builder.append("* Apply attribute ");
              _builder.append(d_5, "     ");
              _builder.append(" as argument to a function and return a new tuple with the substituted argument.");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append(" ");
              _builder.append("*/");
              _builder.newLine();
              _builder.append("    ");
              _builder.append("public final <U");
              _builder.append(d_5, "    ");
              _builder.append("> Tuple");
              _builder.append(degree, "    ");
              _builder.append("<");
              String _TN_8 = this.TN(1, ((d_5).intValue() - 1));
              _builder.append(_TN_8, "    ");
              {
                if (((d_5).intValue() > 1)) {
                  _builder.append(", ");
                }
              }
              _builder.append("U");
              _builder.append(d_5, "    ");
              {
                boolean _lessThan_1 = (d_5.compareTo(degree) < 0);
                if (_lessThan_1) {
                  _builder.append(", ");
                }
              }
              String _TN_9 = this.TN(((d_5).intValue() + 1), (degree).intValue());
              _builder.append(_TN_9, "    ");
              _builder.append("> map");
              _builder.append(d_5, "    ");
              _builder.append("(Function1<? super T");
              _builder.append(d_5, "    ");
              _builder.append(", ? extends U");
              _builder.append(d_5, "    ");
              _builder.append("> function) {");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("    ");
              _builder.append("return Tuple.tuple(");
              String _vn = this.vn(1, ((d_5).intValue() - 1));
              _builder.append(_vn, "        ");
              {
                if (((d_5).intValue() > 1)) {
                  _builder.append(", ");
                }
              }
              _builder.append("function.apply(v");
              _builder.append(d_5, "        ");
              _builder.append(")");
              {
                boolean _lessThan_2 = (d_5.compareTo(degree) < 0);
                if (_lessThan_2) {
                  _builder.append(", ");
                }
              }
              String _vn_1 = this.vn(((d_5).intValue() + 1), (degree).intValue());
              _builder.append(_vn_1, "        ");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("}");
              _builder.newLine();
            }
          }
        }
      }
      _builder.newLine();
      _builder.append("    ");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public final Object[] array() {");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("return new Object[] { ");
      {
        if (((degree).intValue() > 0)) {
          String _vn_2 = this.vn((degree).intValue());
          _builder.append(_vn_2, "        ");
        }
      }
      _builder.append(" };");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public final List<?> list() {");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("return Arrays.asList(array());");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("/**");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* The degree of this tuple: ");
      _builder.append(degree, "     ");
      _builder.append(".");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("*/");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public final int degree() {");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("return ");
      _builder.append(degree, "        ");
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("@SuppressWarnings(\"unchecked\")");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public final Iterator<Object> iterator() {");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("return (Iterator<Object>) list().iterator();");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public int compareTo(Tuple");
      _builder.append(degree, "    ");
      {
        if (((degree).intValue() > 0)) {
          _builder.append("<");
          String _TN_10 = this.TN((degree).intValue());
          _builder.append(_TN_10, "    ");
          _builder.append(">");
        }
      }
      _builder.append(" other) {");
      _builder.newLineIfNotEmpty();
      _builder.append("        ");
      _builder.append("int result = 0;");
      _builder.newLine();
      {
        if (((degree).intValue() > 0)) {
          _builder.newLine();
          {
            IntegerRange _upTo_7 = new IntegerRange(1, (degree).intValue());
            for(final Integer d_6 : _upTo_7) {
              _builder.append("        ");
              _builder.append("result = Tuples.compare(v");
              _builder.append(d_6, "        ");
              _builder.append(", other.v");
              _builder.append(d_6, "        ");
              _builder.append("); if (result != 0) return result;");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.newLine();
        }
      }
      _builder.append("        ");
      _builder.append("return result;");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public boolean equals(Object o) {");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("if (this == o)");
      _builder.newLine();
      _builder.append("            ");
      _builder.append("return true;");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("if (!(o instanceof Tuple");
      _builder.append(degree, "        ");
      _builder.append("))");
      _builder.newLineIfNotEmpty();
      _builder.append("            ");
      _builder.append("return false;");
      _builder.newLine();
      {
        if (((degree).intValue() > 0)) {
          _builder.newLine();
          _builder.append("        ");
          _builder.append("@SuppressWarnings({ \"unchecked\", \"rawtypes\" })");
          _builder.newLine();
          _builder.append("        ");
          _builder.append("final Tuple");
          _builder.append(degree, "        ");
          _builder.append("<");
          String _TN_11 = this.TN((degree).intValue());
          _builder.append(_TN_11, "        ");
          _builder.append("> that = (Tuple");
          _builder.append(degree, "        ");
          _builder.append(") o;");
          _builder.newLineIfNotEmpty();
          _builder.newLine();
          {
            IntegerRange _upTo_8 = new IntegerRange(1, (degree).intValue());
            for(final Integer d_7 : _upTo_8) {
              _builder.append("        ");
              _builder.append("if (!Objects.equals(v");
              _builder.append(d_7, "        ");
              _builder.append(", that.v");
              _builder.append(d_7, "        ");
              _builder.append(")) return false;");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.newLine();
      _builder.append("        ");
      _builder.append("return true;");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public int hashCode() {");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("final int prime = 31;");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("int result = 1;");
      _builder.newLine();
      {
        if (((degree).intValue() > 0)) {
          _builder.newLine();
          {
            IntegerRange _upTo_9 = new IntegerRange(1, (degree).intValue());
            for(final Integer d_8 : _upTo_9) {
              _builder.append("        ");
              _builder.append("result = prime * result + ((v");
              _builder.append(d_8, "        ");
              _builder.append(" == null) ? 0 : v");
              _builder.append(d_8, "        ");
              _builder.append(".hashCode());");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.newLine();
        }
      }
      _builder.append("        ");
      _builder.append("return result;");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public String toString() {");
      _builder.newLine();
      {
        if (((degree).intValue() == 0)) {
          _builder.append("        ");
          _builder.append("return \"()\";");
          _builder.newLine();
        } else {
          _builder.append("        ");
          _builder.append("return \"(\"");
          _builder.newLine();
          {
            IntegerRange _upTo_10 = new IntegerRange(1, (degree).intValue());
            for(final Integer d_9 : _upTo_10) {
              _builder.append("        ");
              _builder.append("     ");
              _builder.append("+ ");
              {
                if (((d_9).intValue() > 1)) {
                  _builder.append("\", \" + ");
                } else {
                  _builder.append("       ");
                }
              }
              _builder.append("v");
              _builder.append(d_9, "             ");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.append("        ");
          _builder.append("     ");
          _builder.append("+ \")\";");
          _builder.newLine();
        }
      }
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public Tuple");
      _builder.append(degree, "    ");
      {
        if (((degree).intValue() > 0)) {
          _builder.append("<");
          String _TN_12 = this.TN((degree).intValue());
          _builder.append(_TN_12, "    ");
          _builder.append(">");
        }
      }
      _builder.append(" clone() {");
      _builder.newLineIfNotEmpty();
      _builder.append("        ");
      _builder.append("return new Tuple");
      _builder.append(degree, "        ");
      {
        if (((degree).intValue() > 0)) {
          _builder.append("<>");
        }
      }
      _builder.append("(this);");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this.write(
        ("org.jooq.lambda.tuple.Tuple" + degree), _builder);
    }
  }
  
  public void generateFunctions() {
    int _max = this.max();
    IntegerRange _upTo = new IntegerRange(1, _max);
    for (final Integer degree : _upTo) {
      StringConcatenation _builder = new StringConcatenation();
      CharSequence _copyright = this.copyright();
      _builder.append(_copyright, "");
      _builder.newLineIfNotEmpty();
      _builder.append("package org.jooq.lambda.function;");
      _builder.newLine();
      _builder.newLine();
      {
        if (((degree).intValue() == 1)) {
          _builder.append("import java.util.function.Function;");
          _builder.newLine();
        }
      }
      {
        if (((degree).intValue() == 2)) {
          _builder.append("import java.util.function.BiFunction;");
          _builder.newLine();
        }
      }
      _builder.newLine();
      {
        IntegerRange _upTo_1 = new IntegerRange(1, (degree).intValue());
        for(final Integer d : _upTo_1) {
          _builder.append("import org.jooq.lambda.tuple.Tuple");
          _builder.append(d, "");
          _builder.append(";");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.newLine();
      _builder.append("/**");
      _builder.newLine();
      _builder.append(" ");
      _builder.append("* A function with ");
      _builder.append(degree, " ");
      _builder.append(" arguments");
      _builder.newLineIfNotEmpty();
      _builder.append(" ");
      _builder.append("*");
      _builder.newLine();
      _builder.append(" ");
      _builder.append("* @author Lukas Eder");
      _builder.newLine();
      _builder.append(" ");
      _builder.append("*/");
      _builder.newLine();
      _builder.append("@FunctionalInterface");
      _builder.newLine();
      _builder.append("public interface Function");
      _builder.append(degree, "");
      _builder.append("<");
      String _TN = this.TN((degree).intValue());
      _builder.append(_TN, "");
      _builder.append(", R> ");
      {
        if (((degree).intValue() == 1)) {
          _builder.append("extends Function<T1, R> ");
        } else {
          if (((degree).intValue() == 2)) {
            _builder.append("extends BiFunction<T1, T2, R> ");
          }
        }
      }
      _builder.append("{");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("/**");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* Apply this function to the arguments.");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("*");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* @param args The arguments as a tuple.");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("*/");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("default R apply(Tuple");
      _builder.append(degree, "    ");
      _builder.append("<");
      String _TN_1 = this.TN((degree).intValue());
      _builder.append(_TN_1, "    ");
      _builder.append("> args) {");
      _builder.newLineIfNotEmpty();
      _builder.append("        ");
      _builder.append("return apply(");
      String _XXXn = this.XXXn((degree).intValue(), "args.v");
      _builder.append(_XXXn, "        ");
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("/**");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* Apply this function to the arguments.");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("*/");
      _builder.newLine();
      {
        if (((degree).intValue() <= 2)) {
          _builder.append("    ");
          _builder.append("@Override");
          _builder.newLine();
        }
      }
      _builder.append("    ");
      _builder.append("R apply(");
      String _TN_XXXn = this.TN_XXXn((degree).intValue(), "v");
      _builder.append(_TN_XXXn, "    ");
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      {
        if (((degree).intValue() == 1)) {
          _builder.newLine();
          _builder.append("    ");
          _builder.append("/**");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* Convert this function to a {@link java.util.function.Function}");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("*/");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("default Function<T1, R> toFunction() {");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("    ");
          _builder.append("return this::apply;");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("}");
          _builder.newLine();
          _builder.newLine();
          _builder.append("    ");
          _builder.append("/**");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("* Convert to this function from a {@link java.util.function.Function}");
          _builder.newLine();
          _builder.append("    ");
          _builder.append(" ");
          _builder.append("*/");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("static <T1, R> Function1<T1, R> from(Function<T1, R> function) {");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("    ");
          _builder.append("return function::apply;");
          _builder.newLine();
          _builder.append("    ");
          _builder.append("}");
          _builder.newLine();
        } else {
          if (((degree).intValue() == 2)) {
            _builder.newLine();
            _builder.append("    ");
            _builder.append("/**");
            _builder.newLine();
            _builder.append("    ");
            _builder.append(" ");
            _builder.append("* Convert this function to a {@link java.util.function.BiFunction}");
            _builder.newLine();
            _builder.append("    ");
            _builder.append(" ");
            _builder.append("*/");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("default BiFunction<T1, T2, R> toBiFunction() {");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("    ");
            _builder.append("return this::apply;");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("    ");
            _builder.append("/**");
            _builder.newLine();
            _builder.append("    ");
            _builder.append(" ");
            _builder.append("* Convert to this function to a {@link java.util.function.BiFunction}");
            _builder.newLine();
            _builder.append("    ");
            _builder.append(" ");
            _builder.append("*/");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("static <T1, T2, R> Function2<T1, T2, R> from(BiFunction<T1, T2, R> function) {");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("    ");
            _builder.append("return function::apply;");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
      {
        if (((degree).intValue() > 0)) {
          {
            IntegerRange _upTo_2 = new IntegerRange(1, (degree).intValue());
            for(final Integer d_1 : _upTo_2) {
              _builder.newLine();
              _builder.append("    ");
              _builder.append("/**");
              _builder.newLine();
              _builder.append("    ");
              _builder.append(" ");
              _builder.append("* Partially apply this function to the arguments.");
              _builder.newLine();
              _builder.append("    ");
              _builder.append(" ");
              _builder.append("*/");
              _builder.newLine();
              _builder.append("    ");
              _builder.append("default Function");
              _builder.append(((degree).intValue() - (d_1).intValue()), "    ");
              _builder.append("<");
              {
                if ((((degree).intValue() - (d_1).intValue()) > 0)) {
                  String _TN_2 = this.TN(((d_1).intValue() + 1), (degree).intValue());
                  _builder.append(_TN_2, "    ");
                  _builder.append(", ");
                }
              }
              _builder.append("R> curry(");
              String _TN_XXXn_1 = this.TN_XXXn((d_1).intValue(), "v");
              _builder.append(_TN_XXXn_1, "    ");
              _builder.append(") {");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("    ");
              _builder.append("return (");
              String _XXXn_1 = this.XXXn(((d_1).intValue() + 1), (degree).intValue(), "v");
              _builder.append(_XXXn_1, "        ");
              _builder.append(") -> apply(");
              String _XXXn_2 = this.XXXn((degree).intValue(), "v");
              _builder.append(_XXXn_2, "        ");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("}");
              _builder.newLine();
            }
          }
          {
            IntegerRange _upTo_3 = new IntegerRange(1, (degree).intValue());
            for(final Integer d_2 : _upTo_3) {
              _builder.newLine();
              _builder.append("    ");
              _builder.append("/**");
              _builder.newLine();
              _builder.append("    ");
              _builder.append(" ");
              _builder.append("* Partially apply this function to the arguments.");
              _builder.newLine();
              _builder.append("    ");
              _builder.append(" ");
              _builder.append("*/");
              _builder.newLine();
              _builder.append("    ");
              _builder.append("default Function");
              _builder.append(((degree).intValue() - (d_2).intValue()), "    ");
              _builder.append("<");
              {
                if ((((degree).intValue() - (d_2).intValue()) > 0)) {
                  String _TN_3 = this.TN(((d_2).intValue() + 1), (degree).intValue());
                  _builder.append(_TN_3, "    ");
                  _builder.append(", ");
                }
              }
              _builder.append("R> curry(Tuple");
              _builder.append(d_2, "    ");
              _builder.append("<");
              String _TN_4 = this.TN((d_2).intValue());
              _builder.append(_TN_4, "    ");
              _builder.append("> args) {");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("    ");
              _builder.append("return (");
              String _XXXn_3 = this.XXXn(((d_2).intValue() + 1), (degree).intValue(), "v");
              _builder.append(_XXXn_3, "        ");
              _builder.append(") -> apply(");
              String _XXXn_4 = this.XXXn((d_2).intValue(), "args.v");
              _builder.append(_XXXn_4, "        ");
              {
                if ((((degree).intValue() - (d_2).intValue()) > 0)) {
                  _builder.append(", ");
                  String _XXXn_5 = this.XXXn(((d_2).intValue() + 1), (degree).intValue(), "v");
                  _builder.append(_XXXn_5, "        ");
                }
              }
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("}");
              _builder.newLine();
            }
          }
        }
      }
      _builder.append("}");
      _builder.newLine();
      this.write(
        ("org.jooq.lambda.function.Function" + degree), _builder);
    }
  }
}
