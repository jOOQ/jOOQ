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
public class BetweenAndSteps extends Generators {
  public static void main(final String[] args) {
    final BetweenAndSteps steps = new BetweenAndSteps();
    steps.generateBetweenAndSteps();
    steps.generateBetweenAndStepsImpl();
  }
  
  public void generateBetweenAndSteps() {
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
        _builder.append("* An intermediate DSL type for the construction of a <code>BETWEEN</code>");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* predicate.");
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
        CharSequence _generatedAnnotation = this.generatedAnnotation();
        _builder.append(_generatedAnnotation, "");
        _builder.newLineIfNotEmpty();
        _builder.append("public interface BetweenAndStep");
        _builder.append(degree, "");
        _builder.append("<");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "");
        _builder.append("> {");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Create a condition to check this field against some bounds");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("Condition and(");
        String _Field_TN_XXXn = this.Field_TN_XXXn((degree).intValue(), "maxValue");
        _builder.append(_Field_TN_XXXn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Create a condition to check this field against some bounds");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("Condition and(");
        String _TN_XXXn = this.TN_XXXn((degree).intValue(), "maxValue");
        _builder.append(_TN_XXXn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Create a condition to check this field against some bounds");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("Condition and(Row");
        _builder.append(degree, "    ");
        _builder.append("<");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "    ");
        _builder.append("> maxValue);");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Create a condition to check this field against some bounds");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("Condition and(Record");
        _builder.append(degree, "    ");
        _builder.append("<");
        String _TN_2 = this.TN((degree).intValue());
        _builder.append(_TN_2, "    ");
        _builder.append("> maxValue);");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        out.append(_builder);
        this.write(("org.jooq.BetweenAndStep" + degree), out);
      }
    }
  }
  
  public void generateBetweenAndStepsImpl() {
    final StringBuilder out = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _classHeader = this.classHeader();
    _builder.append(_classHeader, "");
    _builder.newLineIfNotEmpty();
    _builder.append("package org.jooq.impl;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import static java.util.Arrays.asList;");
    _builder.newLine();
    _builder.append("import static org.jooq.Clause.CONDITION;");
    _builder.newLine();
    _builder.append("import static org.jooq.Clause.CONDITION_BETWEEN;");
    _builder.newLine();
    _builder.append("import static org.jooq.Clause.CONDITION_BETWEEN_SYMMETRIC;");
    _builder.newLine();
    _builder.append("import static org.jooq.Clause.CONDITION_NOT_BETWEEN;");
    _builder.newLine();
    _builder.append("import static org.jooq.Clause.CONDITION_NOT_BETWEEN_SYMMETRIC;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.ACCESS;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.ASE;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.CUBRID;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.DB2;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.DERBY;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.FIREBIRD;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.H2;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.HANA;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.INFORMIX;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.INGRES;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.MARIADB;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.MYSQL;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.ORACLE;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.REDSHIFT;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.SQLITE;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.SQLSERVER;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.SYBASE;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.VERTICA;");
    _builder.newLine();
    _builder.append("import static org.jooq.impl.DSL.row;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import javax.annotation.Generated;");
    _builder.newLine();
    _builder.newLine();
    {
      IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree : _upTo) {
        _builder.append("import org.jooq.BetweenAndStep");
        _builder.append(degree, "");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("import org.jooq.BetweenAndStepN;");
    _builder.newLine();
    _builder.append("import org.jooq.BindContext;");
    _builder.newLine();
    _builder.append("import org.jooq.Clause;");
    _builder.newLine();
    _builder.append("import org.jooq.Condition;");
    _builder.newLine();
    _builder.append("import org.jooq.Configuration;");
    _builder.newLine();
    _builder.append("import org.jooq.Context;");
    _builder.newLine();
    _builder.append("import org.jooq.Field;");
    _builder.newLine();
    _builder.append("import org.jooq.QueryPartInternal;");
    _builder.newLine();
    _builder.append("import org.jooq.Record;");
    _builder.newLine();
    {
      IntegerRange _upTo_1 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_1 : _upTo_1) {
        _builder.append("import org.jooq.Record");
        _builder.append(degree_1, "");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("import org.jooq.RenderContext;");
    _builder.newLine();
    _builder.append("import org.jooq.Row;");
    _builder.newLine();
    {
      IntegerRange _upTo_2 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_2 : _upTo_2) {
        _builder.append("import org.jooq.Row");
        _builder.append(degree_2, "");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("import org.jooq.RowN;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/**");
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
    _builder.append("@SuppressWarnings({ \"rawtypes\", \"unchecked\" })");
    _builder.newLine();
    _builder.append("class RowBetweenCondition<");
    String _TN = this.TN(Constants.MAX_ROW_DEGREE);
    _builder.append(_TN, "");
    _builder.append("> extends AbstractCondition");
    _builder.newLineIfNotEmpty();
    _builder.append("implements");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// This BetweenAndStep implementation implements all types. Type-safety is");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// being checked through the type-safe API. No need for further checks here");
    _builder.newLine();
    {
      IntegerRange _upTo_3 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_3 : _upTo_3) {
        _builder.append("    ");
        _builder.append("BetweenAndStep");
        _builder.append(degree_3, "    ");
        _builder.append("<");
        String _TN_1 = this.TN((degree_3).intValue());
        _builder.append(_TN_1, "    ");
        _builder.append(">,");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("    ");
    _builder.append("BetweenAndStepN {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private static final long     serialVersionUID              = -4666251100802237878L;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private static final Clause[] CLAUSES_BETWEEN               = { CONDITION, CONDITION_BETWEEN };");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private static final Clause[] CLAUSES_BETWEEN_SYMMETRIC     = { CONDITION, CONDITION_BETWEEN_SYMMETRIC };");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private static final Clause[] CLAUSES_NOT_BETWEEN           = { CONDITION, CONDITION_NOT_BETWEEN };");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private static final Clause[] CLAUSES_NOT_BETWEEN_SYMMETRIC = { CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC };");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private final boolean         symmetric;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private final boolean         not;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private final Row             row;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private final Row             minValue;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private Row                   maxValue;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("RowBetweenCondition(Row row, Row minValue, boolean not, boolean symmetric) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("this.row = row;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("this.minValue = minValue;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("this.not = not;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("this.symmetric = symmetric;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// ------------------------------------------------------------------------");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// XXX: BetweenAndStep API");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// ------------------------------------------------------------------------");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public final Condition and(Field f) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("if (maxValue == null) {");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("return and(row(f));");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("else {");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("return super.and(f);");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    {
      IntegerRange _upTo_4 = new IntegerRange(2, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_4 : _upTo_4) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("public final Condition and(");
        String _Field_TN_tn = this.Field_TN_tn((degree_4).intValue());
        _builder.append(_Field_TN_tn, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("    ");
        _builder.append("return and(row(");
        String _tn = this.tn((degree_4).intValue());
        _builder.append(_tn, "        ");
        _builder.append("));");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.newLine();
    _builder.append("    ");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public final Condition and(Field<?>... fields) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("return and(row(fields));");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    {
      IntegerRange _upTo_5 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_5 : _upTo_5) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("public final Condition and(");
        String _TN_tn = this.TN_tn((degree_5).intValue());
        _builder.append(_TN_tn, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("    ");
        _builder.append("return and(row(");
        String _tn_1 = this.tn((degree_5).intValue());
        _builder.append(_tn_1, "        ");
        _builder.append("));");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.newLine();
    _builder.append("    ");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public final Condition and(Object... values) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("return and(row(values));");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    {
      IntegerRange _upTo_6 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_6 : _upTo_6) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("public final Condition and(Row");
        _builder.append(degree_6, "    ");
        _builder.append("<");
        String _TN_2 = this.TN((degree_6).intValue());
        _builder.append(_TN_2, "    ");
        _builder.append("> r) {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("    ");
        _builder.append("this.maxValue = r;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("    ");
        _builder.append("return this;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.newLine();
    _builder.append("    ");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public final Condition and(RowN r) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("this.maxValue = r;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("return this;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    {
      IntegerRange _upTo_7 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_7 : _upTo_7) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("public final Condition and(Record");
        _builder.append(degree_7, "    ");
        _builder.append("<");
        String _TN_3 = this.TN((degree_7).intValue());
        _builder.append(_TN_3, "    ");
        _builder.append("> record) {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("    ");
        _builder.append("return and(record.valuesRow());");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.newLine();
    _builder.append("    ");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public final Condition and(Record record) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("RowN r = new RowImpl(Utils.fields(record.intoArray(), record.fields()));");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("return and(r);");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// ------------------------------------------------------------------------");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// XXX: QueryPart API");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// ------------------------------------------------------------------------");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public final void accept(Context<?> ctx) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("delegate(ctx.configuration()).accept(ctx);");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public final Clause[] clauses(Context<?> ctx) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("return delegate(ctx.configuration()).clauses(ctx);");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private final QueryPartInternal delegate(Configuration configuration) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("// These casts are safe for RowImpl");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("RowN r = (RowN) row;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("RowN min = (RowN) minValue;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("RowN max = (RowN) maxValue;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("// These dialects don\'t support the SYMMETRIC keyword at all");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("if (symmetric && asList(ACCESS, ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HANA, INFORMIX, INGRES, MARIADB, MYSQL, ORACLE, REDSHIFT, SQLITE, SQLSERVER, SYBASE, VERTICA).contains(configuration.family())) {");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("return not");
    _builder.newLine();
    _builder.append("                ");
    _builder.append("? (QueryPartInternal) r.notBetween(min, max).and(r.notBetween(max, min))");
    _builder.newLine();
    _builder.append("                ");
    _builder.append(": (QueryPartInternal) r.between(min, max).or(r.between(max, min));");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("// These dialects either don\'t support row value expressions, or they");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("// Can\'t handle row value expressions with the BETWEEN predicate");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("else if (row.size() > 1 && asList(ACCESS, CUBRID, DERBY, FIREBIRD, HANA, INFORMIX, INGRES, MARIADB, MYSQL, ORACLE, SQLITE, SQLSERVER, SYBASE).contains(configuration.family())) {");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("Condition result = r.ge(min).and(r.le(max));");
    _builder.newLine();
    _builder.newLine();
    _builder.append("            ");
    _builder.append("if (not) {");
    _builder.newLine();
    _builder.append("                ");
    _builder.append("result = result.not();");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("            ");
    _builder.append("return (QueryPartInternal) result;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("else {");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("return new Native();");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("private class Native extends AbstractQueryPart {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("* Generated UID");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("private static final long serialVersionUID = 2915703568738921575L;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("public final void toSQL(RenderContext context) {");
    _builder.newLine();
    _builder.append("                           ");
    _builder.append("context.visit(row);");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("if (not)       context.sql(\" \").keyword(\"not\");");
    _builder.newLine();
    _builder.append("                           ");
    _builder.append("context.sql(\" \").keyword(\"between\");");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("if (symmetric) context.sql(\" \").keyword(\"symmetric\");");
    _builder.newLine();
    _builder.append("                           ");
    _builder.append("context.sql(\" \").visit(minValue);");
    _builder.newLine();
    _builder.append("                           ");
    _builder.append("context.sql(\" \").keyword(\"and\");");
    _builder.newLine();
    _builder.append("                           ");
    _builder.append("context.sql(\" \").visit(maxValue);");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("public final void bind(BindContext context) {");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("context.visit(row).visit(minValue).visit(maxValue);");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("        ");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("public final Clause[] clauses(Context<?> ctx) {");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("return not ? symmetric ? CLAUSES_NOT_BETWEEN_SYMMETRIC");
    _builder.newLine();
    _builder.append("                                   ");
    _builder.append(": CLAUSES_NOT_BETWEEN");
    _builder.newLine();
    _builder.append("                       ");
    _builder.append(": symmetric ? CLAUSES_BETWEEN_SYMMETRIC");
    _builder.newLine();
    _builder.append("                                   ");
    _builder.append(": CLAUSES_BETWEEN;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    out.append(_builder);
    this.write("org.jooq.impl.RowBetweenCondition", out);
  }
}
