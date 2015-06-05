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
public class Records extends Generators {
  public static void main(final String[] args) {
    final Records records = new Records();
    records.generateRecords();
    records.generateRecordImpl();
    records.generateRecordInto();
    records.generateResultInto();
  }
  
  public void generateRecords() {
    IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree : _upTo) {
      {
        final StringBuilder out = new StringBuilder();
        StringConcatenation _builder = new StringConcatenation();
        CharSequence _classHeader = this.classHeader();
        _builder.append(_classHeader, "");
        _builder.newLineIfNotEmpty();
        _builder.append("package org.jooq;");
        _builder.newLine();
        _builder.newLine();
        _builder.append("import javax.annotation.Generated;");
        _builder.newLine();
        _builder.newLine();
        _builder.append("/**");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* A model type for a records with degree <code>");
        _builder.append(degree, " ");
        _builder.append("</code>");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("*");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* @see Row");
        _builder.append(degree, " ");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("* @author Lukas Eder");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*/");
        _builder.newLine();
        CharSequence _generatedAnnotation = this.generatedAnnotation();
        _builder.append(_generatedAnnotation, "");
        _builder.newLineIfNotEmpty();
        _builder.append("public interface Record");
        _builder.append(degree, "");
        _builder.append("<");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "");
        _builder.append("> extends Record {");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("// ------------------------------------------------------------------------");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("// Row value expressions");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("// ------------------------------------------------------------------------");
        _builder.newLine();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Get this record\'s fields as a {@link Row");
        _builder.append(degree, "     ");
        _builder.append("}.");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("Row");
        _builder.append(degree, "    ");
        _builder.append("<");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "    ");
        _builder.append("> fieldsRow();");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Get this record\'s values as a {@link Row");
        _builder.append(degree, "     ");
        _builder.append("}.");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("Row");
        _builder.append(degree, "    ");
        _builder.append("<");
        String _TN_2 = this.TN((degree).intValue());
        _builder.append(_TN_2, "    ");
        _builder.append("> valuesRow();");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("// ------------------------------------------------------------------------");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("// Field accessors");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("// ------------------------------------------------------------------------");
        _builder.newLine();
        {
          IntegerRange _upTo_1 = new IntegerRange(1, (degree).intValue());
          for(final Integer d : _upTo_1) {
            _builder.newLine();
            _builder.append("    ");
            _builder.append("/**");
            _builder.newLine();
            _builder.append("    ");
            _builder.append(" ");
            _builder.append("* Get the ");
            String _first = this.first((d).intValue());
            _builder.append(_first, "     ");
            _builder.append(" field.");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append(" ");
            _builder.append("*/");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("Field<T");
            _builder.append(d, "    ");
            _builder.append("> field");
            _builder.append(d, "    ");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.newLine();
        _builder.append("    ");
        _builder.append("// ------------------------------------------------------------------------");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("// Value accessors");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("// ------------------------------------------------------------------------");
        _builder.newLine();
        {
          IntegerRange _upTo_2 = new IntegerRange(1, (degree).intValue());
          for(final Integer d_1 : _upTo_2) {
            _builder.newLine();
            _builder.append("    ");
            _builder.append("/**");
            _builder.newLine();
            _builder.append("    ");
            _builder.append(" ");
            _builder.append("* Get the ");
            String _first_1 = this.first((d_1).intValue());
            _builder.append(_first_1, "     ");
            _builder.append(" value.");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append(" ");
            _builder.append("*/");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("T");
            _builder.append(d_1, "    ");
            _builder.append(" value");
            _builder.append(d_1, "    ");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
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
            _builder.append("* Set the ");
            String _first_2 = this.first((d_2).intValue());
            _builder.append(_first_2, "     ");
            _builder.append(" value.");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append(" ");
            _builder.append("*/");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("Record");
            CharSequence _recTypeSuffix = this.recTypeSuffix((degree).intValue());
            _builder.append(_recTypeSuffix, "    ");
            _builder.append(" value");
            _builder.append(d_2, "    ");
            _builder.append("(T");
            _builder.append(d_2, "    ");
            _builder.append(" value);");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Set all values.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("Record");
        CharSequence _recTypeSuffix_1 = this.recTypeSuffix((degree).intValue());
        _builder.append(_recTypeSuffix_1, "    ");
        _builder.append(" values(");
        String _TN_tn = this.TN_tn((degree).intValue());
        _builder.append(_TN_tn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        out.append(_builder);
        this.write(("org.jooq.Record" + degree), out);
      }
    }
  }
  
  public void generateRecordImpl() {
    final StringBuilder out = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _classHeader = this.classHeader();
    _builder.append(_classHeader, "");
    _builder.newLineIfNotEmpty();
    _builder.append("package org.jooq.impl;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import java.util.Collection;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import javax.annotation.Generated;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import org.jooq.Field;");
    _builder.newLine();
    _builder.append("import org.jooq.Record;");
    _builder.newLine();
    {
      IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree : _upTo) {
        _builder.append("import org.jooq.Record");
        _builder.append(degree, "");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* A general purpose record, typically used for ad-hoc types.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* <p>");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* This type implements both the general-purpose, type-unsafe {@link Record}");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* interface, as well as the more specific, type-safe {@link Record1},");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* {@link Record2} through {@link Record");
    _builder.append(Constants.MAX_ROW_DEGREE, " ");
    _builder.append("} interfaces");
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
    CharSequence _generatedAnnotation = this.generatedAnnotation();
    _builder.append(_generatedAnnotation, "");
    _builder.newLineIfNotEmpty();
    _builder.append("@SuppressWarnings({ \"unchecked\", \"rawtypes\" })");
    _builder.newLine();
    _builder.append("class RecordImpl<");
    String _TN = this.TN(Constants.MAX_ROW_DEGREE);
    _builder.append(_TN, "");
    _builder.append("> extends AbstractRecord");
    _builder.newLineIfNotEmpty();
    _builder.append("implements");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// This record implementation implements all record types. Type-safety is");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// being checked through the type-safe API. No need for further checks here");
    _builder.newLine();
    {
      IntegerRange _upTo_1 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      boolean _hasElements = false;
      for(final Integer degree_1 : _upTo_1) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "    ");
        }
        _builder.append("    ");
        _builder.append("Record");
        _builder.append(degree_1, "    ");
        _builder.append("<");
        String _TN_1 = this.TN((degree_1).intValue());
        _builder.append(_TN_1, "    ");
        _builder.append(">");
        {
          if (((degree_1).intValue() == Constants.MAX_ROW_DEGREE)) {
            _builder.append(" {");
          }
        }
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Generated UID");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private static final long serialVersionUID = -2201346180421463830L;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Create a new general purpose record.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public RecordImpl(Field<?>... fields) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("super(fields);");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Create a new general purpose record.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public RecordImpl(Collection<? extends Field<?>> fields) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("super(fields);");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Create a new general purpose record.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("RecordImpl(RowImpl fields) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("super(fields);");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// ------------------------------------------------------------------------");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// XXX: Type-safe Record APIs");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// ------------------------------------------------------------------------");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public RowImpl<");
    String _TN_2 = this.TN(Constants.MAX_ROW_DEGREE);
    _builder.append(_TN_2, "    ");
    _builder.append("> fieldsRow() {");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("return fields;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public final RowImpl<");
    String _TN_3 = this.TN(Constants.MAX_ROW_DEGREE);
    _builder.append(_TN_3, "    ");
    _builder.append("> valuesRow() {");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("return new RowImpl(Utils.fields(intoArray(), fields.fields()));");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    {
      IntegerRange _upTo_2 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_2 : _upTo_2) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("public final Field<T");
        _builder.append(degree_2, "    ");
        _builder.append("> field");
        _builder.append(degree_2, "    ");
        _builder.append("() {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("    ");
        _builder.append("return fields.field(");
        _builder.append(((degree_2).intValue() - 1), "        ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      IntegerRange _upTo_3 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_3 : _upTo_3) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("public final T");
        _builder.append(degree_3, "    ");
        _builder.append(" value");
        _builder.append(degree_3, "    ");
        _builder.append("() {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("    ");
        _builder.append("return (T");
        _builder.append(degree_3, "        ");
        _builder.append(") getValue(");
        _builder.append(((degree_3).intValue() - 1), "        ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      IntegerRange _upTo_4 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_4 : _upTo_4) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("public final RecordImpl value");
        _builder.append(degree_4, "    ");
        _builder.append("(T");
        _builder.append(degree_4, "    ");
        _builder.append(" value) {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("    ");
        _builder.append("setValue(");
        _builder.append(((degree_4).intValue() - 1), "        ");
        _builder.append(", value);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("    ");
        _builder.append("return this;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      IntegerRange _upTo_5 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_5 : _upTo_5) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("public final RecordImpl values(");
        String _TN_tn = this.TN_tn((degree_5).intValue());
        _builder.append(_TN_tn, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("    ");
        _builder.append("return null;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("}");
    _builder.newLine();
    out.append(_builder);
    this.write("org.jooq.impl.RecordImpl", out);
  }
  
  public void generateRecordInto() {
    final StringBuilder outAPI = new StringBuilder();
    final StringBuilder outImpl = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    {
      IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree : _upTo) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Copy this record into a new record holding only a subset of the previous");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* fields.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* @return The new record");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* @see #into(Record)");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        CharSequence _generatedAnnotation = this.generatedAnnotation();
        _builder.append(_generatedAnnotation, "    ");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("<");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "    ");
        _builder.append("> Record");
        _builder.append(degree, "    ");
        _builder.append("<");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "    ");
        _builder.append("> into(");
        String _Field_TN_fieldn = this.Field_TN_fieldn((degree).intValue());
        _builder.append(_Field_TN_fieldn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    outAPI.append(_builder);
    StringConcatenation _builder_1 = new StringConcatenation();
    {
      IntegerRange _upTo_1 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_1 : _upTo_1) {
        _builder_1.newLine();
        _builder_1.append("    ");
        _builder_1.append("@Override");
        _builder_1.newLine();
        _builder_1.append("    ");
        _builder_1.append("public final <");
        String _TN_2 = this.TN((degree_1).intValue());
        _builder_1.append(_TN_2, "    ");
        _builder_1.append("> Record");
        _builder_1.append(degree_1, "    ");
        _builder_1.append("<");
        String _TN_3 = this.TN((degree_1).intValue());
        _builder_1.append(_TN_3, "    ");
        _builder_1.append("> into(");
        String _Field_TN_fieldn_1 = this.Field_TN_fieldn((degree_1).intValue());
        _builder_1.append(_Field_TN_fieldn_1, "    ");
        _builder_1.append(") {");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("        ");
        _builder_1.append("return (Record");
        _builder_1.append(degree_1, "        ");
        _builder_1.append(") into(new Field[] { ");
        String _fieldn = this.fieldn((degree_1).intValue());
        _builder_1.append(_fieldn, "        ");
        _builder_1.append(" });");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("    ");
        _builder_1.append("}");
        _builder_1.newLine();
      }
    }
    outImpl.append(_builder_1);
    this.insert("org.jooq.Record", outAPI, "into-fields");
    this.insert("org.jooq.impl.AbstractRecord", outImpl, "into-fields");
  }
  
  public void generateResultInto() {
    final StringBuilder outAPI = new StringBuilder();
    final StringBuilder outImpl = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    {
      IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree : _upTo) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Copy all records from this result into a new result with new records");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* holding only a subset of the previous fields.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* @return The new result");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        CharSequence _generatedAnnotation = this.generatedAnnotation();
        _builder.append(_generatedAnnotation, "    ");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("<");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "    ");
        _builder.append("> Result<Record");
        _builder.append(degree, "    ");
        _builder.append("<");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "    ");
        _builder.append(">> into(");
        String _Field_TN_fieldn = this.Field_TN_fieldn((degree).intValue());
        _builder.append(_Field_TN_fieldn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    outAPI.append(_builder);
    StringConcatenation _builder_1 = new StringConcatenation();
    {
      IntegerRange _upTo_1 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_1 : _upTo_1) {
        _builder_1.newLine();
        _builder_1.append("    ");
        _builder_1.append("@Override");
        _builder_1.newLine();
        _builder_1.append("    ");
        _builder_1.append("public final <");
        String _TN_2 = this.TN((degree_1).intValue());
        _builder_1.append(_TN_2, "    ");
        _builder_1.append("> Result<Record");
        _builder_1.append(degree_1, "    ");
        _builder_1.append("<");
        String _TN_3 = this.TN((degree_1).intValue());
        _builder_1.append(_TN_3, "    ");
        _builder_1.append(">> into(");
        String _Field_TN_fieldn_1 = this.Field_TN_fieldn((degree_1).intValue());
        _builder_1.append(_Field_TN_fieldn_1, "    ");
        _builder_1.append(") {");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("        ");
        _builder_1.append("return (Result) into(new Field[] { ");
        String _fieldn = this.fieldn((degree_1).intValue());
        _builder_1.append(_fieldn, "        ");
        _builder_1.append(" });");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("    ");
        _builder_1.append("}");
        _builder_1.newLine();
      }
    }
    outImpl.append(_builder_1);
    this.insert("org.jooq.Result", outAPI, "into-fields");
    this.insert("org.jooq.impl.ResultImpl", outImpl, "into-fields");
  }
}
