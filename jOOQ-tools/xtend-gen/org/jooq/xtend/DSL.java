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

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.jooq.Constants;
import org.jooq.xtend.Generators;

@SuppressWarnings("all")
public class DSL extends Generators {
  public static void main(final String[] args) {
    final DSL dsl = new DSL();
    dsl.generateRowValue();
    dsl.generateRowExpression();
    dsl.generateRowField();
    dsl.generateValues();
  }
  
  public void generateRowValue() {
    final StringBuilder out = new StringBuilder();
    IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree : _upTo) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("/**");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* Create a row value expression of degree <code>");
      _builder.append(degree, "     ");
      _builder.append("</code>.");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("* <p>");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* Note: Not all databases support row value expressions, but many row value");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* expression operations can be emulated on all databases. See relevant row");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* value expression method Javadocs for details.");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("*/");
      _builder.newLine();
      _builder.append("    ");
      CharSequence _generatedMethod = this.generatedMethod();
      _builder.append(_generatedMethod, "    ");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("@Support");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public static <");
      String _TN = this.TN((degree).intValue());
      _builder.append(_TN, "    ");
      _builder.append("> Row");
      _builder.append(degree, "    ");
      _builder.append("<");
      String _TN_1 = this.TN((degree).intValue());
      _builder.append(_TN_1, "    ");
      _builder.append("> row(");
      String _TN_tn = this.TN_tn((degree).intValue());
      _builder.append(_TN_tn, "    ");
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      _builder.append("        ");
      _builder.append("return row(");
      String _Utils_field_tn = this.Utils_field_tn((degree).intValue());
      _builder.append(_Utils_field_tn, "        ");
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      out.append(_builder);
    }
    this.insert("org.jooq.impl.DSL", out, "row-value");
  }
  
  public void generateRowExpression() {
    final StringBuilder out = new StringBuilder();
    IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree : _upTo) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("/**");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* Create a row value expression of degree <code>");
      _builder.append(degree, "     ");
      _builder.append("</code>.");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("* <p>");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* Note: Not all databases support row value expressions, but many row value");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* expression operations can be emulated on all databases. See relevant row");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* value expression method Javadocs for details.");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("*/");
      _builder.newLine();
      _builder.append("    ");
      CharSequence _generatedMethod = this.generatedMethod();
      _builder.append(_generatedMethod, "    ");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("@Support");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public static <");
      String _TN = this.TN((degree).intValue());
      _builder.append(_TN, "    ");
      _builder.append("> Row");
      _builder.append(degree, "    ");
      _builder.append("<");
      String _TN_1 = this.TN((degree).intValue());
      _builder.append(_TN_1, "    ");
      _builder.append("> row(");
      String _Field_TN_tn = this.Field_TN_tn((degree).intValue());
      _builder.append(_Field_TN_tn, "    ");
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      _builder.append("        ");
      _builder.append("return new RowImpl(");
      String _tn = this.tn((degree).intValue());
      _builder.append(_tn, "        ");
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      out.append(_builder);
    }
    this.insert("org.jooq.impl.DSL", out, "row-expression");
  }
  
  public void generateRowField() {
    final StringBuilder out = new StringBuilder();
    IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree : _upTo) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("/**");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* EXPERIMENTAL: Turn a row value expression of degree <code>");
      _builder.append(degree, "     ");
      _builder.append("</code> into a {@code Field}.");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("* <p>");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* Note: Not all databases support row value expressions, but many row value");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* expression operations can be emulated on all databases. See relevant row");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* value expression method Javadocs for details.");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("*/");
      _builder.newLine();
      _builder.append("    ");
      CharSequence _generatedMethod = this.generatedMethod();
      _builder.append(_generatedMethod, "    ");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("@Support");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public static <");
      String _TN = this.TN((degree).intValue());
      _builder.append(_TN, "    ");
      _builder.append("> Field<Record");
      CharSequence _recTypeSuffix = this.recTypeSuffix((degree).intValue());
      _builder.append(_recTypeSuffix, "    ");
      _builder.append("> field(Row");
      CharSequence _typeSuffix = this.typeSuffix((degree).intValue());
      _builder.append(_typeSuffix, "    ");
      _builder.append(" row) {");
      _builder.newLineIfNotEmpty();
      _builder.append("        ");
      _builder.append("return new RowField<Row");
      CharSequence _typeSuffix_1 = this.typeSuffix((degree).intValue());
      _builder.append(_typeSuffix_1, "        ");
      _builder.append(", Record");
      CharSequence _recTypeSuffix_1 = this.recTypeSuffix((degree).intValue());
      _builder.append(_recTypeSuffix_1, "        ");
      _builder.append(">(row);");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      out.append(_builder);
    }
    this.insert("org.jooq.impl.DSL", out, "row-field");
  }
  
  public void generateValues() {
    final StringBuilder out = new StringBuilder();
    IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree : _upTo) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("/**");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* Create a <code>VALUES()</code> expression of degree <code>");
      _builder.append(degree, "     ");
      _builder.append("</code>.");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("* <p>");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* The <code>VALUES()</code> constructor is a tool supported by some");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* databases to allow for constructing tables from constant values.");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* <p>");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* If a database doesn\'t support the <code>VALUES()</code> constructor, it");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* can be emulated using <code>SELECT .. UNION ALL ..</code>. The following");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* expressions are equivalent:");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* <p>");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* <pre><code>");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* -- Using VALUES() constructor");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* VALUES(");
      {
        IntegerRange _upTo_1 = new IntegerRange(1, (degree).intValue());
        boolean _hasElements = false;
        for(final Integer d : _upTo_1) {
          if (!_hasElements) {
            _hasElements = true;
          } else {
            _builder.appendImmediate(", ", "     ");
          }
          _builder.append("val1_");
          _builder.append(d, "     ");
        }
      }
      _builder.append("),");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("*       (");
      {
        IntegerRange _upTo_2 = new IntegerRange(1, (degree).intValue());
        boolean _hasElements_1 = false;
        for(final Integer d_1 : _upTo_2) {
          if (!_hasElements_1) {
            _hasElements_1 = true;
          } else {
            _builder.appendImmediate(", ", "     ");
          }
          _builder.append("val2_");
          _builder.append(d_1, "     ");
        }
      }
      _builder.append("),");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("*       (");
      {
        IntegerRange _upTo_3 = new IntegerRange(1, (degree).intValue());
        boolean _hasElements_2 = false;
        for(final Integer d_2 : _upTo_3) {
          if (!_hasElements_2) {
            _hasElements_2 = true;
          } else {
            _builder.appendImmediate(", ", "     ");
          }
          _builder.append("val3_");
          _builder.append(d_2, "     ");
        }
      }
      _builder.append(")");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("* AS \"v\"(");
      {
        IntegerRange _upTo_4 = new IntegerRange(1, (degree).intValue());
        boolean _hasElements_3 = false;
        for(final Integer d_3 : _upTo_4) {
          if (!_hasElements_3) {
            _hasElements_3 = true;
          } else {
            _builder.appendImmediate(", ", "     ");
          }
          _builder.append("\"c");
          _builder.append(d_3, "     ");
          _builder.append("\"  ");
        }
      }
      _builder.append(")");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("*");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* -- Using UNION ALL");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* SELECT ");
      {
        IntegerRange _upTo_5 = new IntegerRange(1, (degree).intValue());
        boolean _hasElements_4 = false;
        for(final Integer d_4 : _upTo_5) {
          if (!_hasElements_4) {
            _hasElements_4 = true;
          } else {
            _builder.appendImmediate(", ", "     ");
          }
          _builder.append("val1_");
          _builder.append(d_4, "     ");
          _builder.append(" AS \"c");
          _builder.append(d_4, "     ");
          _builder.append("\"");
        }
      }
      _builder.append(") UNION ALL");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("* SELECT ");
      {
        IntegerRange _upTo_6 = new IntegerRange(1, (degree).intValue());
        boolean _hasElements_5 = false;
        for(final Integer d_5 : _upTo_6) {
          if (!_hasElements_5) {
            _hasElements_5 = true;
          } else {
            _builder.appendImmediate(", ", "     ");
          }
          _builder.append("val1_");
          _builder.append(d_5, "     ");
          _builder.append(" AS \"c");
          _builder.append(d_5, "     ");
          _builder.append("\"");
        }
      }
      _builder.append(") UNION ALL");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("* SELECT ");
      {
        IntegerRange _upTo_7 = new IntegerRange(1, (degree).intValue());
        boolean _hasElements_6 = false;
        for(final Integer d_6 : _upTo_7) {
          if (!_hasElements_6) {
            _hasElements_6 = true;
          } else {
            _builder.appendImmediate(", ", "     ");
          }
          _builder.append("val1_");
          _builder.append(d_6, "     ");
          _builder.append(" AS \"c");
          _builder.append(d_6, "     ");
          _builder.append("\"");
        }
      }
      _builder.append(")");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("* </code></pre>");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* <p>");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* Use {@link Table#as(String, String...)} to rename the resulting table and");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* its columns.");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("*/");
      _builder.newLine();
      _builder.append("    ");
      CharSequence _generatedMethod = this.generatedMethod();
      _builder.append(_generatedMethod, "    ");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("@Support");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public static <");
      String _TN = this.TN((degree).intValue());
      _builder.append(_TN, "    ");
      _builder.append("> Table<Record");
      _builder.append(degree, "    ");
      _builder.append("<");
      String _TN_1 = this.TN((degree).intValue());
      _builder.append(_TN_1, "    ");
      _builder.append(">> values(Row");
      _builder.append(degree, "    ");
      _builder.append("<");
      String _TN_2 = this.TN((degree).intValue());
      _builder.append(_TN_2, "    ");
      _builder.append(">... rows) {");
      _builder.newLineIfNotEmpty();
      _builder.append("        ");
      _builder.append("return new Values<Record");
      _builder.append(degree, "        ");
      _builder.append("<");
      String _TN_3 = this.TN((degree).intValue());
      _builder.append(_TN_3, "        ");
      _builder.append(">>(rows).as(\"v\", ");
      {
        IntegerRange _upTo_8 = new IntegerRange(1, (degree).intValue());
        boolean _hasElements_7 = false;
        for(final Integer d_7 : _upTo_8) {
          if (!_hasElements_7) {
            _hasElements_7 = true;
          } else {
            _builder.appendImmediate(", ", "        ");
          }
          _builder.append("\"c");
          _builder.append(d_7, "        ");
          _builder.append("\"");
        }
      }
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      out.append(_builder);
    }
    this.insert("org.jooq.impl.DSL", out, "values");
  }
}
