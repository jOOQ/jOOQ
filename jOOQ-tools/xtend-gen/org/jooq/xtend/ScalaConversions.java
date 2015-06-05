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

/**
 * @author Lukas Eder
 */
@SuppressWarnings("all")
public class ScalaConversions extends Generators {
  public static void main(final String[] args) {
    final ScalaConversions conversions = new ScalaConversions();
    conversions.generateAsTuple();
    conversions.generateAsMapper();
  }
  
  public void generateAsTuple() {
    final StringBuilder out = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    {
      IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree : _upTo) {
        _builder.newLine();
        _builder.append("  ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("   ");
        _builder.append("* Enrich any {@link org.jooq.Record");
        _builder.append(degree, "   ");
        _builder.append("} with the {@link Tuple");
        _builder.append(degree, "   ");
        _builder.append("} case class");
        _builder.newLineIfNotEmpty();
        _builder.append("   ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("implicit def asTuple");
        _builder.append(degree, "  ");
        _builder.append("[");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "  ");
        _builder.append("](r : Record");
        _builder.append(degree, "  ");
        _builder.append("[");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "  ");
        _builder.append("]): ");
        {
          if (((degree).intValue() == 1)) {
            _builder.append("Tuple");
            _builder.append(degree, "  ");
            _builder.append("[");
            String _TN_2 = this.TN((degree).intValue());
            _builder.append(_TN_2, "  ");
            _builder.append("]");
          } else {
            _builder.append("(");
            String _TN_3 = this.TN((degree).intValue());
            _builder.append(_TN_3, "  ");
            _builder.append(")");
          }
        }
        _builder.append(" = r match {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("case null => null");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("case _ => ");
        {
          if (((degree).intValue() == 1)) {
            _builder.append("Tuple");
            _builder.append(degree, "    ");
          }
        }
        _builder.append("(");
        {
          IntegerRange _upTo_1 = new IntegerRange(1, (degree).intValue());
          boolean _hasElements = false;
          for(final Integer d : _upTo_1) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", "    ");
            }
            _builder.append("r.value");
            _builder.append(d, "    ");
          }
        }
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    out.append(_builder);
    this.insert("org.jooq.scala.Conversions", out, "tuples");
  }
  
  public void generateAsMapper() {
    final StringBuilder out = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    {
      IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree : _upTo) {
        _builder.newLine();
        _builder.append("  ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("   ");
        _builder.append("* Wrap a Scala <code>Tuple");
        _builder.append(degree, "   ");
        _builder.append(" => E</code> function in a jOOQ <code>RecordMapper</code> type.");
        _builder.newLineIfNotEmpty();
        _builder.append("   ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("implicit def asMapperFromArgList");
        _builder.append(degree, "  ");
        _builder.append("[");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "  ");
        _builder.append(", E](f: (");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "  ");
        _builder.append(") => E): RecordMapper[Record");
        _builder.append(degree, "  ");
        _builder.append("[");
        String _TN_2 = this.TN((degree).intValue());
        _builder.append(_TN_2, "  ");
        _builder.append("], E] = new RecordMapper[Record");
        _builder.append(degree, "  ");
        _builder.append("[");
        String _TN_3 = this.TN((degree).intValue());
        _builder.append(_TN_3, "  ");
        _builder.append("], E] {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("def map(record: Record");
        _builder.append(degree, "    ");
        _builder.append("[");
        String _TN_4 = this.TN((degree).intValue());
        _builder.append(_TN_4, "    ");
        _builder.append("]) = f(");
        String _XXXn = this.XXXn((degree).intValue(), "record.value");
        _builder.append(_XXXn, "    ");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      IntegerRange _upTo_1 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_1 : _upTo_1) {
        _builder.newLine();
        _builder.append("  ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("   ");
        _builder.append("* Wrap a Scala <code>Tuple");
        _builder.append(degree_1, "   ");
        _builder.append(" => E</code> function in a jOOQ <code>RecordMapper</code> type.");
        _builder.newLineIfNotEmpty();
        _builder.append("   ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("implicit def asMapperFromTuple");
        _builder.append(degree_1, "  ");
        _builder.append("[");
        String _TN_5 = this.TN((degree_1).intValue());
        _builder.append(_TN_5, "  ");
        _builder.append(", E](f: ");
        {
          if (((degree_1).intValue() == 1)) {
            _builder.append("Tuple");
            _builder.append(degree_1, "  ");
            _builder.append("[");
            String _TN_6 = this.TN((degree_1).intValue());
            _builder.append(_TN_6, "  ");
            _builder.append("]");
          } else {
            _builder.append("((");
            String _TN_7 = this.TN((degree_1).intValue());
            _builder.append(_TN_7, "  ");
            _builder.append("))");
          }
        }
        _builder.append(" => E): RecordMapper[Record");
        _builder.append(degree_1, "  ");
        _builder.append("[");
        String _TN_8 = this.TN((degree_1).intValue());
        _builder.append(_TN_8, "  ");
        _builder.append("], E] = new RecordMapper[Record");
        _builder.append(degree_1, "  ");
        _builder.append("[");
        String _TN_9 = this.TN((degree_1).intValue());
        _builder.append(_TN_9, "  ");
        _builder.append("], E] {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("def map(record: Record");
        _builder.append(degree_1, "    ");
        _builder.append("[");
        String _TN_10 = this.TN((degree_1).intValue());
        _builder.append(_TN_10, "    ");
        _builder.append("]) = f(");
        {
          if (((degree_1).intValue() == 1)) {
            _builder.append("Tuple");
            _builder.append(degree_1, "    ");
            _builder.append("(");
            String _XXXn_1 = this.XXXn((degree_1).intValue(), "record.value");
            _builder.append(_XXXn_1, "    ");
            _builder.append(")");
          } else {
            _builder.append("(");
            String _XXXn_2 = this.XXXn((degree_1).intValue(), "record.value");
            _builder.append(_XXXn_2, "    ");
            _builder.append(")");
          }
        }
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    out.append(_builder);
    this.insert("org.jooq.scala.Conversions", out, "mapper");
  }
  
  public void generateAsHandler() {
    final StringBuilder out = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    {
      IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree : _upTo) {
        _builder.newLine();
        _builder.append("  ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("   ");
        _builder.append("* Wrap a Scala <code>Tuple");
        _builder.append(degree, "   ");
        _builder.append(" => Unit</code> function in a jOOQ <code>RecordHandler</code> type.");
        _builder.newLineIfNotEmpty();
        _builder.append("   ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("implicit def asHandlerFromArgList");
        _builder.append(degree, "  ");
        _builder.append("[");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "  ");
        _builder.append("](f: (");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "  ");
        _builder.append(") => Unit): RecordHandler[Record");
        _builder.append(degree, "  ");
        _builder.append("[");
        String _TN_2 = this.TN((degree).intValue());
        _builder.append(_TN_2, "  ");
        _builder.append("]] = new RecordHandler[Record");
        _builder.append(degree, "  ");
        _builder.append("[");
        String _TN_3 = this.TN((degree).intValue());
        _builder.append(_TN_3, "  ");
        _builder.append("]] {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("def next(record: Record");
        _builder.append(degree, "    ");
        _builder.append("[");
        String _TN_4 = this.TN((degree).intValue());
        _builder.append(_TN_4, "    ");
        _builder.append("]) = f(");
        String _XXXn = this.XXXn((degree).intValue(), "record.value");
        _builder.append(_XXXn, "    ");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      IntegerRange _upTo_1 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_1 : _upTo_1) {
        _builder.newLine();
        _builder.append("  ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("   ");
        _builder.append("* Wrap a Scala <code>Tuple");
        _builder.append(degree_1, "   ");
        _builder.append(" => Unit</code> function in a jOOQ <code>RecordHandler</code> type.");
        _builder.newLineIfNotEmpty();
        _builder.append("   ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("implicit def asHandlerFromTuple");
        _builder.append(degree_1, "  ");
        _builder.append("[");
        String _TN_5 = this.TN((degree_1).intValue());
        _builder.append(_TN_5, "  ");
        _builder.append(", E](f: Tuple");
        _builder.append(degree_1, "  ");
        _builder.append("[");
        String _TN_6 = this.TN((degree_1).intValue());
        _builder.append(_TN_6, "  ");
        _builder.append("] => E): RecordHandler[Record");
        _builder.append(degree_1, "  ");
        _builder.append("[");
        String _TN_7 = this.TN((degree_1).intValue());
        _builder.append(_TN_7, "  ");
        _builder.append("]] = new RecordHandler[Record");
        _builder.append(degree_1, "  ");
        _builder.append("[");
        String _TN_8 = this.TN((degree_1).intValue());
        _builder.append(_TN_8, "  ");
        _builder.append("]] {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("def next(record: Record");
        _builder.append(degree_1, "    ");
        _builder.append("[");
        String _TN_9 = this.TN((degree_1).intValue());
        _builder.append(_TN_9, "    ");
        _builder.append("]) = f(Tuple");
        _builder.append(degree_1, "    ");
        _builder.append("(");
        String _XXXn_1 = this.XXXn((degree_1).intValue(), "record.value");
        _builder.append(_XXXn_1, "    ");
        _builder.append("))");
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    out.append(_builder);
    this.insert("org.jooq.scala.Conversions", out, "handler");
  }
}
