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
public class With extends Generators {
  public static void main(final String[] args) {
    final With ctx = new With();
    ctx.generateSelect();
    ctx.generateSelectDistinct();
    ctx.generateInsert();
    ctx.generateMerge();
  }
  
  public void generateSelect() {
    final StringBuilder outWithStep = new StringBuilder();
    final StringBuilder outWithImpl = new StringBuilder();
    IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree : _upTo) {
      {
        String fieldOrRow = ("Row" + degree);
        if (((degree).intValue() == 1)) {
          fieldOrRow = "Field";
        }
        StringConcatenation _builder = new StringConcatenation();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Create a new DSL select statement.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* This is the same as {@link #select(Field...)}, except that it");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* declares additional record-level typesafety, which is needed by");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* {@link ");
        _builder.append(fieldOrRow, "     ");
        _builder.append("#in(Select)}, {@link ");
        _builder.append(fieldOrRow, "     ");
        _builder.append("#equal(Select)} and other predicate");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("* building methods taking subselect arguments.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* This creates an attached, renderable and executable <code>SELECT</code>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* statement from this {@link DSLContext}. If you don\'t need to render or");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* execute this <code>SELECT</code> statement (e.g. because you want to");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* create a subselect), consider using the static");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* {@link DSL#select(");
        {
          IntegerRange _upTo_1 = new IntegerRange(1, (degree).intValue());
          boolean _hasElements = false;
          for(final Integer d : _upTo_1) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", "     ");
            }
            _builder.append("SelectField");
          }
        }
        _builder.append(")} instead.");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Example: <code><pre>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* using(configuration)");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*       .with(name(\"t\").fields(");
        String _XXX1_XXX2_XXXn = this.XXX1_XXX2_XXXn((degree).intValue(), "f");
        _builder.append(_XXX1_XXX2_XXXn, "     ");
        _builder.append(").as(subselect))");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("*       .select(");
        String _field1_field2_fieldn = this.field1_field2_fieldn((degree).intValue());
        _builder.append(_field1_field2_fieldn, "     ");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("*       .from(table1)");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*       .join(table2).on(field1.equal(field2))");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*       .where(field1.greaterThan(100))");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*       .orderBy(field2);");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* </pre></code>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* @see DSL#selectDistinct(SelectField...)");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* @see #selectDistinct(SelectField...)");
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
        _builder.append("<");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "    ");
        _builder.append("> SelectSelectStep<Record");
        _builder.append(degree, "    ");
        _builder.append("<");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "    ");
        _builder.append(">> select(");
        String _SelectField_TN_fieldn = this.SelectField_TN_fieldn((degree).intValue());
        _builder.append(_SelectField_TN_fieldn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        outWithStep.append(_builder);
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.newLine();
        _builder_1.append("    ");
        CharSequence _generatedMethod_1 = this.generatedMethod();
        _builder_1.append(_generatedMethod_1, "    ");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("    ");
        _builder_1.append("@Override");
        _builder_1.newLine();
        _builder_1.append("    ");
        _builder_1.append("public final <");
        String _TN_2 = this.TN((degree).intValue());
        _builder_1.append(_TN_2, "    ");
        _builder_1.append("> SelectSelectStep<Record");
        _builder_1.append(degree, "    ");
        _builder_1.append("<");
        String _TN_3 = this.TN((degree).intValue());
        _builder_1.append(_TN_3, "    ");
        _builder_1.append(">> select(");
        String _SelectField_TN_fieldn_1 = this.SelectField_TN_fieldn((degree).intValue());
        _builder_1.append(_SelectField_TN_fieldn_1, "    ");
        _builder_1.append(") {");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("        ");
        _builder_1.append("return (SelectSelectStep) select(new SelectField[] { ");
        String _fieldn = this.fieldn((degree).intValue());
        _builder_1.append(_fieldn, "        ");
        _builder_1.append(" });");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("    ");
        _builder_1.append("}");
        _builder_1.newLine();
        outWithImpl.append(_builder_1);
      }
    }
    this.insert("org.jooq.WithStep", outWithStep, "select");
    this.insert("org.jooq.impl.WithImpl", outWithImpl, "select");
  }
  
  public void generateSelectDistinct() {
    final StringBuilder outWithStep = new StringBuilder();
    final StringBuilder outWithImpl = new StringBuilder();
    IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree : _upTo) {
      {
        String fieldOrRow = ("Row" + degree);
        if (((degree).intValue() == 1)) {
          fieldOrRow = "Field";
        }
        StringConcatenation _builder = new StringConcatenation();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Create a new DSL select statement.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* This is the same as {@link #selectDistinct(Field...)}, except that it");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* declares additional record-level typesafety, which is needed by");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* {@link ");
        _builder.append(fieldOrRow, "     ");
        _builder.append("#in(Select)}, {@link ");
        _builder.append(fieldOrRow, "     ");
        _builder.append("#equal(Select)} and other predicate");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("* building methods taking subselect arguments.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* This creates an attached, renderable and executable <code>SELECT</code>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* statement from this {@link DSLContext}. If you don\'t need to render or");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* execute this <code>SELECT</code> statement (e.g. because you want to");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* create a subselect), consider using the static");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* {@link DSL#selectDistinct(");
        {
          IntegerRange _upTo_1 = new IntegerRange(1, (degree).intValue());
          boolean _hasElements = false;
          for(final Integer d : _upTo_1) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", "     ");
            }
            _builder.append("SelectField");
          }
        }
        _builder.append(")} instead.");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Example: <code><pre>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* using(configuration)");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*       .with(name(\"t\").fields(");
        String _XXX1_XXX2_XXXn = this.XXX1_XXX2_XXXn((degree).intValue(), "f");
        _builder.append(_XXX1_XXX2_XXXn, "     ");
        _builder.append(").as(subselect))");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("*       .selectDistinct(");
        String _field1_field2_fieldn = this.field1_field2_fieldn((degree).intValue());
        _builder.append(_field1_field2_fieldn, "     ");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("*       .from(table1)");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*       .join(table2).on(field1.equal(field2))");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*       .where(field1.greaterThan(100))");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*       .orderBy(field2);");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* </pre></code>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* @see DSL#selectDistinct(SelectField...)");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* @see #selectDistinct(SelectField...)");
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
        _builder.append("<");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "    ");
        _builder.append("> SelectSelectStep<Record");
        _builder.append(degree, "    ");
        _builder.append("<");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "    ");
        _builder.append(">> selectDistinct(");
        String _SelectField_TN_fieldn = this.SelectField_TN_fieldn((degree).intValue());
        _builder.append(_SelectField_TN_fieldn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        outWithStep.append(_builder);
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.newLine();
        _builder_1.append("    ");
        CharSequence _generatedMethod_1 = this.generatedMethod();
        _builder_1.append(_generatedMethod_1, "    ");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("    ");
        _builder_1.append("@Override");
        _builder_1.newLine();
        _builder_1.append("    ");
        _builder_1.append("public final <");
        String _TN_2 = this.TN((degree).intValue());
        _builder_1.append(_TN_2, "    ");
        _builder_1.append("> SelectSelectStep<Record");
        _builder_1.append(degree, "    ");
        _builder_1.append("<");
        String _TN_3 = this.TN((degree).intValue());
        _builder_1.append(_TN_3, "    ");
        _builder_1.append(">> selectDistinct(");
        String _SelectField_TN_fieldn_1 = this.SelectField_TN_fieldn((degree).intValue());
        _builder_1.append(_SelectField_TN_fieldn_1, "    ");
        _builder_1.append(") {");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("        ");
        _builder_1.append("return (SelectSelectStep) selectDistinct(new SelectField[] { ");
        String _fieldn = this.fieldn((degree).intValue());
        _builder_1.append(_fieldn, "        ");
        _builder_1.append(" });");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("    ");
        _builder_1.append("}");
        _builder_1.newLine();
        outWithImpl.append(_builder_1);
      }
    }
    this.insert("org.jooq.WithStep", outWithStep, "selectDistinct");
    this.insert("org.jooq.impl.WithImpl", outWithImpl, "selectDistinct");
  }
  
  public void generateInsert() {
    final StringBuilder outWithStep = new StringBuilder();
    final StringBuilder outWithImpl = new StringBuilder();
    IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree : _upTo) {
      {
        StringConcatenation _builder = new StringConcatenation();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Create a new DSL insert statement.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Example: <code><pre>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* using(configuration)");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*       .with(name(\"t\").fields(");
        String _XXX1_XXX2_XXXn = this.XXX1_XXX2_XXXn((degree).intValue(), "f");
        _builder.append(_XXX1_XXX2_XXXn, "     ");
        _builder.append(").as(subselect))");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("*       .insertInto(table, ");
        String _field1_field2_fieldn = this.field1_field2_fieldn((degree).intValue());
        _builder.append(_field1_field2_fieldn, "     ");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("*       .values(");
        String _XXX1_XXX2_XXXn_1 = this.XXX1_XXX2_XXXn((degree).intValue(), "valueA");
        _builder.append(_XXX1_XXX2_XXXn_1, "     ");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("*       .values(");
        String _XXX1_XXX2_XXXn_2 = this.XXX1_XXX2_XXXn((degree).intValue(), "valueB");
        _builder.append(_XXX1_XXX2_XXXn_2, "     ");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("*       .onDuplicateKeyUpdate()");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*       .set(field1, value1)");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*       .set(field2, value2)");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*       .execute();");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* </pre></code>");
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
        _builder.append("<R extends Record, ");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "    ");
        _builder.append("> InsertValuesStep");
        _builder.append(degree, "    ");
        _builder.append("<R, ");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "    ");
        _builder.append("> insertInto(Table<R> into, ");
        String _Field_TN_fieldn = this.Field_TN_fieldn((degree).intValue());
        _builder.append(_Field_TN_fieldn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        outWithStep.append(_builder);
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.newLine();
        _builder_1.append("    ");
        CharSequence _generatedMethod_1 = this.generatedMethod();
        _builder_1.append(_generatedMethod_1, "    ");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("    ");
        _builder_1.append("@Override");
        _builder_1.newLine();
        _builder_1.append("    ");
        _builder_1.append("public <R extends Record, ");
        String _TN_2 = this.TN((degree).intValue());
        _builder_1.append(_TN_2, "    ");
        _builder_1.append("> InsertValuesStep");
        _builder_1.append(degree, "    ");
        _builder_1.append("<R, ");
        String _TN_3 = this.TN((degree).intValue());
        _builder_1.append(_TN_3, "    ");
        _builder_1.append("> insertInto(Table<R> into, ");
        String _Field_TN_fieldn_1 = this.Field_TN_fieldn((degree).intValue());
        _builder_1.append(_Field_TN_fieldn_1, "    ");
        _builder_1.append(") {");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("        ");
        _builder_1.append("return new InsertImpl(configuration, into, Arrays.asList(new Field[] { ");
        String _fieldn = this.fieldn((degree).intValue());
        _builder_1.append(_fieldn, "        ");
        _builder_1.append(" }));");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("    ");
        _builder_1.append("}");
        _builder_1.newLine();
        outWithImpl.append(_builder_1);
      }
    }
    this.insert("org.jooq.WithStep", outWithStep, "insert");
    this.insert("org.jooq.impl.WithImpl", outWithImpl, "insert");
  }
  
  public void generateMerge() {
    final StringBuilder outWithStep = new StringBuilder();
    final StringBuilder outWithImpl = new StringBuilder();
    IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree : _upTo) {
      {
        StringConcatenation _builder = new StringConcatenation();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Create a new DSL merge statement (H2-specific syntax).");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* This statement is available from DSL syntax only. It is known to be");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* supported in some way by any of these dialects:");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <table border=\"1\">");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <tr>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <td>H2</td>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <td>H2 natively supports this special syntax</td>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <td><a href= \"www.h2database.com/html/grammar.html#merge\"");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* >www.h2database.com/html/grammar.html#merge</a></td>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* </tr>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <tr>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <td>These databases can simulate the H2-specific MERGE statement using a");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* standard SQL MERGE statement, without restrictions</td>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* </tr>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* </table>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        CharSequence _generatedMethod = this.generatedMethod();
        _builder.append(_generatedMethod, "    ");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("@Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("<R extends Record, ");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "    ");
        _builder.append("> MergeKeyStep");
        _builder.append(degree, "    ");
        _builder.append("<R, ");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "    ");
        _builder.append("> mergeInto(Table<R> table, ");
        String _Field_TN_fieldn = this.Field_TN_fieldn((degree).intValue());
        _builder.append(_Field_TN_fieldn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        outWithStep.append(_builder);
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.newLine();
        _builder_1.append("    ");
        CharSequence _generatedMethod_1 = this.generatedMethod();
        _builder_1.append(_generatedMethod_1, "    ");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("    ");
        _builder_1.append("@Override");
        _builder_1.newLine();
        _builder_1.append("    ");
        _builder_1.append("public <R extends Record, ");
        String _TN_2 = this.TN((degree).intValue());
        _builder_1.append(_TN_2, "    ");
        _builder_1.append("> MergeKeyStep");
        _builder_1.append(degree, "    ");
        _builder_1.append("<R, ");
        String _TN_3 = this.TN((degree).intValue());
        _builder_1.append(_TN_3, "    ");
        _builder_1.append("> mergeInto(Table<R> table, ");
        String _Field_TN_fieldn_1 = this.Field_TN_fieldn((degree).intValue());
        _builder_1.append(_Field_TN_fieldn_1, "    ");
        _builder_1.append(") {");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("        ");
        _builder_1.append("return new MergeImpl(configuration, table, Arrays.asList(");
        String _fieldn = this.fieldn((degree).intValue());
        _builder_1.append(_fieldn, "        ");
        _builder_1.append("));");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("    ");
        _builder_1.append("}");
        _builder_1.newLine();
        outWithImpl.append(_builder_1);
      }
    }
    this.insert("org.jooq.WithStep", outWithStep, "merge");
    this.insert("org.jooq.impl.WithImpl", outWithImpl, "merge");
  }
}
