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

/**
 * @author Lukas Eder
 */
@SuppressWarnings("all")
public class Constraint extends Generators {
  public static void main(final String[] args) {
    final Constraint constraint = new Constraint();
    constraint.generateConstraintTypeStepForeignKey();
    constraint.generateConstraintForeignKeyReferencesSteps();
    constraint.generateConstraintImplImplements();
    constraint.generateConstraintImplForeignKey();
  }
  
  public void generateConstraintImplForeignKey() {
    final StringBuilder out = new StringBuilder();
    IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree : _upTo) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("    ");
      CharSequence _generatedMethod = this.generatedMethod();
      _builder.append(_generatedMethod, "    ");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("public final <");
      String _TN = this.TN((degree).intValue());
      _builder.append(_TN, "    ");
      _builder.append("> ConstraintImpl foreignKey(");
      String _Field_TN_fieldn = this.Field_TN_fieldn((degree).intValue());
      _builder.append(_Field_TN_fieldn, "    ");
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      _builder.append("    \t");
      _builder.append("return foreignKey(new Field[] { ");
      String _fieldn = this.fieldn((degree).intValue());
      _builder.append(_fieldn, "    \t");
      _builder.append(" });");
      _builder.newLineIfNotEmpty();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      out.append(_builder);
    }
    IntegerRange _upTo_1 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree_1 : _upTo_1) {
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
      _builder_1.append("public final ConstraintImpl foreignKey(");
      String _XXXn = this.XXXn((degree_1).intValue(), "String field");
      _builder_1.append(_XXXn, "    ");
      _builder_1.append(") {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("    \t");
      _builder_1.append("return foreignKey(new String[] { ");
      String _fieldn_1 = this.fieldn((degree_1).intValue());
      _builder_1.append(_fieldn_1, "    \t");
      _builder_1.append(" });");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("    ");
      _builder_1.append("}");
      _builder_1.newLine();
      out.append(_builder_1);
    }
    IntegerRange _upTo_2 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree_2 : _upTo_2) {
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.newLine();
      _builder_2.append("    ");
      CharSequence _generatedMethod_2 = this.generatedMethod();
      _builder_2.append(_generatedMethod_2, "    ");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append("    ");
      _builder_2.append("@Override");
      _builder_2.newLine();
      _builder_2.append("    ");
      _builder_2.append("public final ConstraintImpl references(Table table, ");
      String _Field_tn = this.Field_tn((degree_2).intValue());
      _builder_2.append(_Field_tn, "    ");
      _builder_2.append(") {");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append("    \t");
      _builder_2.append("return references(table, new Field[] { ");
      String _tn = this.tn((degree_2).intValue());
      _builder_2.append(_tn, "    \t");
      _builder_2.append(" });");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append("    ");
      _builder_2.append("}");
      _builder_2.newLine();
      out.append(_builder_2);
    }
    IntegerRange _upTo_3 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree_3 : _upTo_3) {
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.newLine();
      _builder_3.append("    ");
      CharSequence _generatedMethod_3 = this.generatedMethod();
      _builder_3.append(_generatedMethod_3, "    ");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("    ");
      _builder_3.append("@Override");
      _builder_3.newLine();
      _builder_3.append("    ");
      _builder_3.append("public final ConstraintImpl references(String table, ");
      String _XXXn_1 = this.XXXn((degree_3).intValue(), "String field");
      _builder_3.append(_XXXn_1, "    ");
      _builder_3.append(") {");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("    \t");
      _builder_3.append("return references(table, new String[] { ");
      String _fieldn_2 = this.fieldn((degree_3).intValue());
      _builder_3.append(_fieldn_2, "    \t");
      _builder_3.append(" });");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("    ");
      _builder_3.append("}");
      _builder_3.newLine();
      out.append(_builder_3);
    }
    this.insert("org.jooq.impl.ConstraintImpl", out, "foreignKey");
  }
  
  public void generateConstraintImplImplements() {
    final StringBuilder out = new StringBuilder();
    IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree : _upTo) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("  ");
      _builder.append(", ConstraintForeignKeyReferencesStep");
      _builder.append(degree, "  ");
      _builder.newLineIfNotEmpty();
      out.append(_builder);
    }
    this.insert("org.jooq.impl.ConstraintImpl", out, "implements");
  }
  
  public void generateConstraintTypeStepForeignKey() {
    final StringBuilder out = new StringBuilder();
    IntegerRange _upTo = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree : _upTo) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("/**");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("* Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.");
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
      _builder.append("> ConstraintForeignKeyReferencesStep");
      _builder.append(degree, "    ");
      _builder.append("<");
      String _TN_1 = this.TN((degree).intValue());
      _builder.append(_TN_1, "    ");
      _builder.append("> foreignKey(");
      String _Field_TN_fieldn = this.Field_TN_fieldn((degree).intValue());
      _builder.append(_Field_TN_fieldn, "    ");
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      out.append(_builder);
    }
    IntegerRange _upTo_1 = new IntegerRange(1, Constants.MAX_ROW_DEGREE);
    for (final Integer degree_1 : _upTo_1) {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.newLine();
      _builder_1.append("    ");
      _builder_1.append("/**");
      _builder_1.newLine();
      _builder_1.append("     ");
      _builder_1.append("* Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.");
      _builder_1.newLine();
      _builder_1.append("     ");
      _builder_1.append("*/");
      _builder_1.newLine();
      _builder_1.append("    ");
      CharSequence _generatedMethod_1 = this.generatedMethod();
      _builder_1.append(_generatedMethod_1, "    ");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("    ");
      _builder_1.append("@Support");
      _builder_1.newLine();
      _builder_1.append("    ");
      _builder_1.append("ConstraintForeignKeyReferencesStep");
      _builder_1.append(degree_1, "    ");
      _builder_1.append("<");
      IntegerRange _upTo_2 = new IntegerRange(1, (degree_1).intValue());
      final Function1<Integer, String> _function = (Integer it) -> {
        return "?";
      };
      Iterable<String> _map = IterableExtensions.<Integer, String>map(_upTo_2, _function);
      String _join = IterableExtensions.join(_map, ", ");
      _builder_1.append(_join, "    ");
      _builder_1.append("> foreignKey(");
      String _XXXn = this.XXXn((degree_1).intValue(), "String field");
      _builder_1.append(_XXXn, "    ");
      _builder_1.append(");");
      _builder_1.newLineIfNotEmpty();
      out.append(_builder_1);
    }
    this.insert("org.jooq.ConstraintTypeStep", out, "foreignKey");
  }
  
  public void generateConstraintForeignKeyReferencesSteps() {
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
        _builder.append("* The step in the {@link Constraint} construction DSL API that allows for");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* matching a <code>FOREIGN KEY</code> clause with a <code>REFERENCES</code>");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* clause.");
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
        _builder.append("public interface ConstraintForeignKeyReferencesStep");
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
        _builder.append("* Add a <code>REFERENCES</code> clause to the <code>CONSTRAINT</code>.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("ConstraintForeignKeyOnStep references(String table, ");
        String _XXXn = this.XXXn((degree).intValue(), "String field");
        _builder.append(_XXXn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        _builder.append("    ");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("* Add a <code>REFERENCES</code> clause to the <code>CONSTRAINT</code>.");
        _builder.newLine();
        _builder.append("     ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("@Support");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("ConstraintForeignKeyOnStep references(Table<?> table, ");
        String _Field_TN_fieldn = this.Field_TN_fieldn((degree).intValue());
        _builder.append(_Field_TN_fieldn, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        out.append(_builder);
        this.write(("org.jooq.ConstraintForeignKeyReferencesStep" + degree), out);
      }
    }
  }
}
