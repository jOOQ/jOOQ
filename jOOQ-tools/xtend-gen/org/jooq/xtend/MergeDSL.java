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
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jooq.Constants;
import org.jooq.xtend.Generators;

@SuppressWarnings("all")
public class MergeDSL extends Generators {
  public static void main(final String[] args) {
    final MergeDSL merge = new MergeDSL();
    merge.generateMergeNotMatchedStep();
    merge.generateMergeNotMatchedValuesStep();
    merge.generateMergeKeyStep();
    merge.generateMergeValuesStep();
    merge.generateMergeImplImplements();
    merge.generateMergeImplValues();
    merge.generateMergeImplWhenNotMatchedThenInsert();
  }
  
  public void generateMergeNotMatchedStep() {
    final StringBuilder out = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _classHeader = this.classHeader();
    _builder.append(_classHeader, "");
    _builder.newLineIfNotEmpty();
    _builder.append("package org.jooq;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.CUBRID;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.DB2;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.HSQLDB;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.ORACLE;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.SQLSERVER;");
    _builder.newLine();
    _builder.append("import static org.jooq.SQLDialect.SYBASE;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import java.util.Collection;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import javax.annotation.Generated;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* This type is used for the {@link Merge}\'s DSL API.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* <p>");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Example: <code><pre>");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* DSLContext create = DSL.using(configuration);");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* create.mergeInto(table)");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*       .using(select)");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*       .on(condition)");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*       .whenMatchedThenUpdate()");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*       .set(field1, value1)");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*       .set(field2, value2)");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*       .whenNotMatchedThenInsert(field1, field2)");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*       .values(value1, value2)");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*       .execute();");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* </pre></code>");
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
    _builder.append("public interface MergeNotMatchedStep<R extends Record> extends MergeFinalStep<R> {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* <code>MERGE</code> statement.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* <p>");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Unlike the {@link #whenNotMatchedThenInsert(Field...)} and");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* {@link #whenNotMatchedThenInsert(Collection)} methods, this will give");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* access to a MySQL-like API allowing for");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* <code>INSERT SET a = x, b = y</code> syntax.");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("@Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("MergeNotMatchedSetStep<R> whenNotMatchedThenInsert();");
    _builder.newLine();
    {
      IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree : _upTo) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("    ");
        _builder.append(" ");
        _builder.append("* Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the");
        _builder.newLine();
        _builder.append("    ");
        _builder.append(" ");
        _builder.append("* <code>MERGE</code> statement");
        _builder.newLine();
        _builder.append("    ");
        _builder.append(" ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("<");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "    ");
        _builder.append("> MergeNotMatchedValuesStep");
        _builder.append(degree, "    ");
        _builder.append("<R, ");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "    ");
        _builder.append("> whenNotMatchedThenInsert(");
        String _Field_TN_fieldn = this.Field_TN_fieldn((degree).intValue());
        _builder.append(_Field_TN_fieldn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* <code>MERGE</code> statement");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("@Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("MergeNotMatchedValuesStepN<R> whenNotMatchedThenInsert(Field<?>... fields);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* Add the <code>WHEN MATCHED THEN UPDATE</code> clause to the");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("* <code>MERGE</code> statement");
    _builder.newLine();
    _builder.append("     ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("@Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("MergeNotMatchedValuesStepN<R> whenNotMatchedThenInsert(Collection<? extends Field<?>> fields);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    out.append(_builder);
    this.write("org.jooq.MergeNotMatchedStep", out);
  }
  
  public void generateMergeNotMatchedValuesStep() {
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
        _builder.append("import static org.jooq.SQLDialect.CUBRID;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.DB2;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.HSQLDB;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.ORACLE;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.SQLSERVER;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.SYBASE;");
        _builder.newLine();
        _builder.newLine();
        _builder.append("import java.util.Collection;");
        _builder.newLine();
        _builder.newLine();
        _builder.append("import javax.annotation.Generated;");
        _builder.newLine();
        _builder.newLine();
        _builder.append("/**");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* This type is used for the {@link Merge}\'s DSL API.");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* Example: <code><pre>");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* DSLContext create = DSL.using(configuration);");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* create.mergeInto(table)");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*       .using(select)");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*       .on(condition)");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*       .whenMatchedThenUpdate()");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*       .set(field1, value1)");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*       .set(field2, value2)");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*       .whenNotMatchedThenInsert(field1, field2)");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*       .values(value1, value2)");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*       .execute();");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* </pre></code>");
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
        _builder.append("public interface MergeNotMatchedValuesStep");
        _builder.append(degree, "");
        _builder.append("<R extends Record, ");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "");
        _builder.append("> {");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Set <code>VALUES</code> for <code>INSERT</code> in the <code>MERGE</code>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* statement\'s <code>WHEN NOT MATCHED THEN INSERT</code> clause.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("MergeNotMatchedWhereStep<R> values(");
        String _TN_XXXn = this.TN_XXXn((degree).intValue(), "value");
        _builder.append(_TN_XXXn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Set <code>VALUES</code> for <code>INSERT</code> in the <code>MERGE</code>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* statement\'s <code>WHEN NOT MATCHED THEN INSERT</code> clause.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("MergeNotMatchedWhereStep<R> values(");
        String _Field_TN_XXXn = this.Field_TN_XXXn((degree).intValue(), "value");
        _builder.append(_Field_TN_XXXn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Set <code>VALUES</code> for <code>INSERT</code> in the <code>MERGE</code>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* statement\'s <code>WHEN NOT MATCHED THEN INSERT</code> clause.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("MergeNotMatchedWhereStep<R> values(Collection<?> values);");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        out.append(_builder);
        this.write(("org.jooq.MergeNotMatchedValuesStep" + degree), out);
      }
    }
  }
  
  public void generateMergeKeyStep() {
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
        _builder.append("import static org.jooq.SQLDialect.CUBRID;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.DB2;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.H2;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.HSQLDB;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.ORACLE;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.SQLSERVER;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.SYBASE;");
        _builder.newLine();
        _builder.newLine();
        _builder.append("import java.util.Collection;");
        _builder.newLine();
        _builder.newLine();
        _builder.append("import javax.annotation.Generated;");
        _builder.newLine();
        _builder.newLine();
        _builder.append("/**");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* This type is used for the H2-specific variant of the {@link Merge}\'s DSL API.");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* Example: <code><pre>");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* DSLContext create = DSL.using(configuration);");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* create.mergeInto(table, ");
        String _field1_field2_fieldn = this.field1_field2_fieldn((degree).intValue());
        _builder.append(_field1_field2_fieldn, " ");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("*       .key(id)");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*       .values(");
        String _XXX1_XXX2_XXXn = this.XXX1_XXX2_XXXn((degree).intValue(), "value");
        _builder.append(_XXX1_XXX2_XXXn, " ");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("*       .execute();");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* </pre></code>");
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
        _builder.append("public interface MergeKeyStep");
        _builder.append(degree, "");
        _builder.append("<R extends Record, ");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "");
        _builder.append("> extends MergeValuesStep");
        _builder.append(degree, "");
        _builder.append("<R, ");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "");
        _builder.append("> {");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Specify an optional <code>KEY</code> clause.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Use this optional clause in order to override using the underlying");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <code>PRIMARY KEY</code>.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("MergeValuesStep");
        _builder.append(degree, "    ");
        _builder.append("<R, ");
        String _TN_2 = this.TN((degree).intValue());
        _builder.append(_TN_2, "    ");
        _builder.append("> key(Field<?>... keys);");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Specify an optional <code>KEY</code> clause.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Use this optional clause in order to override using the underlying");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <code>PRIMARY KEY</code>.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("MergeValuesStep");
        _builder.append(degree, "    ");
        _builder.append("<R, ");
        String _TN_3 = this.TN((degree).intValue());
        _builder.append(_TN_3, "    ");
        _builder.append("> key(Collection<? extends Field<?>> keys);");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        out.append(_builder);
        this.write(("org.jooq.MergeKeyStep" + degree), out);
      }
    }
  }
  
  public void generateMergeValuesStep() {
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
        _builder.append("import static org.jooq.SQLDialect.CUBRID;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.DB2;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.H2;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.HSQLDB;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.ORACLE;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.SQLSERVER;");
        _builder.newLine();
        _builder.append("import static org.jooq.SQLDialect.SYBASE;");
        _builder.newLine();
        _builder.newLine();
        _builder.append("import java.util.Collection;");
        _builder.newLine();
        _builder.newLine();
        _builder.append("import javax.annotation.Generated;");
        _builder.newLine();
        _builder.newLine();
        _builder.append("/**");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* This type is used for the H2-specific variant of the {@link Merge}\'s DSL API.");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* Example: <code><pre>");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* using(configuration)");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*       .mergeInto(table, ");
        String _field1_field2_fieldn = this.field1_field2_fieldn((degree).intValue());
        _builder.append(_field1_field2_fieldn, " ");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("*       .key(id)");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*       .values(");
        String _XXX1_XXX2_XXXn = this.XXX1_XXX2_XXXn((degree).intValue(), "value");
        _builder.append(_XXX1_XXX2_XXXn, " ");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("*       .execute();");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* </pre></code>");
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
        _builder.append("public interface MergeValuesStep");
        _builder.append(degree, "");
        _builder.append("<R extends Record, ");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "");
        _builder.append("> {");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Specify a <code>VALUES</code> clause");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("Merge<R> values(");
        String _TN_XXXn = this.TN_XXXn((degree).intValue(), "value");
        _builder.append(_TN_XXXn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Specify a <code>VALUES</code> clause");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("Merge<R> values(");
        String _Field_TN_XXXn = this.Field_TN_XXXn((degree).intValue(), "value");
        _builder.append(_Field_TN_XXXn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Specify a <code>VALUES</code> clause");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("Merge<R> values(Collection<?> values);");
        _builder.newLine();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Use a <code>SELECT</code> statement as the source of values for the");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <code>MERGE</code> statement");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <p>");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* This variant of the <code>MERGE .. SELECT</code> statement expects a");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* select returning exactly as many fields as specified previously in the");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* <code>INTO</code> clause:");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* {@link DSLContext#mergeInto(Table, ");
        IntegerRange _upTo_1 = new IntegerRange(1, (degree).intValue());
        final Function1<Integer, CharSequence> _function = (Integer e) -> {
          return "Field";
        };
        String _join = IterableExtensions.<Integer>join(_upTo_1, ", ", _function);
        _builder.append(_join, "     ");
        _builder.append(")}");
        _builder.newLineIfNotEmpty();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("Merge<R> select(Select<? extends Record");
        _builder.append(degree, "    ");
        _builder.append("<");
        String _TN_1 = this.TN((degree).intValue());
        _builder.append(_TN_1, "    ");
        _builder.append(">> select);");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        out.append(_builder);
        this.write(("org.jooq.MergeValuesStep" + degree), out);
      }
    }
  }
  
  public void generateMergeImplImplements() {
    final StringBuilder outKeyStep = new StringBuilder();
    final StringBuilder outNotMatchedValuesStep = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    {
      IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree : _upTo) {
        _builder.append("    ");
        _builder.append("MergeKeyStep");
        _builder.append(degree, "    ");
        _builder.append("<R, ");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "    ");
        _builder.append(">,");
        _builder.newLineIfNotEmpty();
      }
    }
    outKeyStep.append(_builder);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.newLine();
    {
      IntegerRange _upTo_1 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_1 : _upTo_1) {
        _builder_1.append("    ");
        _builder_1.append("MergeNotMatchedValuesStep");
        _builder_1.append(degree_1, "    ");
        _builder_1.append("<R, ");
        String _TN_1 = this.TN((degree_1).intValue());
        _builder_1.append(_TN_1, "    ");
        _builder_1.append(">,");
        _builder_1.newLineIfNotEmpty();
      }
    }
    outNotMatchedValuesStep.append(_builder_1);
    this.insert("org.jooq.impl.MergeImpl", outKeyStep, "implementsKeyStep");
    this.insert("org.jooq.impl.MergeImpl", outNotMatchedValuesStep, "implementsNotMatchedValuesStep");
  }
  
  public void generateMergeImplValues() {
    final StringBuilder out = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    {
      IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree : _upTo) {
        _builder.append("    ");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("public final MergeImpl values(");
        String _TN_XXXn = this.TN_XXXn((degree).intValue(), "value");
        _builder.append(_TN_XXXn, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("    ");
        _builder.append("return values(new Object[] { ");
        String _XXXn = this.XXXn((degree).intValue(), "value");
        _builder.append(_XXXn, "        ");
        _builder.append(" });");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
      }
    }
    {
      IntegerRange _upTo_1 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree_1 : _upTo_1) {
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("public final MergeImpl values(");
        String _Field_TN_XXXn = this.Field_TN_XXXn((degree_1).intValue(), "value");
        _builder.append(_Field_TN_XXXn, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("    ");
        _builder.append("return values(new Field[] { ");
        String _XXXn_1 = this.XXXn((degree_1).intValue(), "value");
        _builder.append(_XXXn_1, "        ");
        _builder.append(" });");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
      }
    }
    out.append(_builder);
    this.insert("org.jooq.impl.MergeImpl", out, "values");
  }
  
  public void generateMergeImplWhenNotMatchedThenInsert() {
    final StringBuilder out = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    {
      IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
      for(final Integer degree : _upTo) {
        _builder.append("    ");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@SuppressWarnings(\"hiding\")");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("public final <");
        String _TN = this.TN((degree).intValue());
        _builder.append(_TN, "    ");
        _builder.append("> MergeImpl whenNotMatchedThenInsert(");
        String _Field_TN_fieldn = this.Field_TN_fieldn((degree).intValue());
        _builder.append(_Field_TN_fieldn, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("    ");
        _builder.append("return whenNotMatchedThenInsert(new Field[] { ");
        String _fieldn = this.fieldn((degree).intValue());
        _builder.append(_fieldn, "        ");
        _builder.append(" });");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
      }
    }
    out.append(_builder);
    this.insert("org.jooq.impl.MergeImpl", out, "whenNotMatchedThenInsert");
  }
}
