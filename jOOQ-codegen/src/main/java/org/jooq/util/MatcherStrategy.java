/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.util;

import static java.util.Collections.emptyList;
import static java.util.regex.Pattern.compile;
import static org.jooq.tools.StringUtils.defaultIfEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.tools.StringUtils;
import org.jooq.util.jaxb.MatcherRule;
import org.jooq.util.jaxb.MatcherTransformType;
import org.jooq.util.jaxb.Matchers;
import org.jooq.util.jaxb.MatchersFieldType;
import org.jooq.util.jaxb.MatchersRoutineType;
import org.jooq.util.jaxb.MatchersSchemaType;
import org.jooq.util.jaxb.MatchersSequenceType;
import org.jooq.util.jaxb.MatchersTableType;

/**
 * A generator strategy that names objects according to a {@link Matchers}
 * configuration object.
 *
 * @author Lukas Eder
 */
public class MatcherStrategy extends DefaultGeneratorStrategy {

    private final Matchers matchers;

    public MatcherStrategy(Matchers matchers) {
        this.matchers = matchers;
    }

    /**
     * Take a {@link Definition}, try to match its name or qualified name
     * against an expression, and apply a rule upon match.
     */
    private final String match(Definition definition, String expression, MatcherRule rule) {
        if (rule != null) {
            return match(definition, expression, rule.getExpression(), rule.getTransform());
        }

        return null;
    }

    private final String match(Definition definition, String expression, String ruleExpression) {
        return match(definition, expression, ruleExpression, null);
    }

    private final String match(Definition definition, String expression, String ruleExpression, MatcherTransformType ruleTransformType) {
        // [#3734] If users forget to specify the rule's expression but they use
        // a transformer (e.g. PASCAL), we should assume the "default" replacement
        if (ruleTransformType != null && ruleExpression == null)
            ruleExpression = "$0";

        if (ruleExpression != null) {
            Pattern p = compile(defaultIfEmpty(expression, "^.*$").trim());
            Matcher m = p.matcher(definition.getName());

            if (m.matches()) {
                return transform(m.replaceAll(ruleExpression), ruleTransformType);
            }

            m = p.matcher(definition.getQualifiedName());

            if (m.matches()) {
                return transform(m.replaceAll(ruleExpression), ruleTransformType);
            }
        }

        return null;
    }

    private final String transform(String string, MatcherTransformType transform) {
        if (transform == null)
            return string;

        switch (transform) {
            case AS_IS:
                return string;
            case LOWER:
                return string.toLowerCase();
            case UPPER:
                return string.toUpperCase();
            case CAMEL:
                return StringUtils.toCamelCaseLC(string);
            case PASCAL:
                return StringUtils.toCamelCase(string);

            default:
                throw new UnsupportedOperationException("Transform Type not supported : " + transform);
        }
    }

    private final List<MatchersSchemaType> schemas(Definition definition) {
        if (definition instanceof SchemaDefinition) {
            return matchers.getSchemas();
        }

        return emptyList();
    }

    private final List<MatchersTableType> tables(Definition definition) {
        if (definition instanceof TableDefinition) {
            return matchers.getTables();
        }

        return emptyList();
    }

    private final List<MatchersFieldType> fields(Definition definition) {
        if (definition instanceof ColumnDefinition) {
            return matchers.getFields();
        }

        return emptyList();
    }

    private final List<MatchersRoutineType> routines(Definition definition) {
        if (definition instanceof RoutineDefinition) {
            return matchers.getRoutines();
        }

        return emptyList();
    }

    private final List<MatchersSequenceType> sequences(Definition definition) {
        if (definition instanceof SequenceDefinition) {
            return matchers.getSequences();
        }

        return emptyList();
    }

    private final List<String> split(String result) {
        List<String> list = new ArrayList<String>();

        for (String string : result.split(",")) {
            list.add(string.trim());
        }

        return list;
    }

    @Override
    public String getJavaIdentifier(Definition definition) {
        for (MatchersSchemaType schemas : schemas(definition)) {
            String result = match(definition, schemas.getExpression(), schemas.getSchemaIdentifier());
            if (result != null)
                return result;
        }

        for (MatchersTableType tables : tables(definition)) {
            String result = match(definition, tables.getExpression(), tables.getTableIdentifier());
            if (result != null)
                return result;
        }

        for (MatchersFieldType fields : fields(definition)) {
            String result = match(definition, fields.getExpression(), fields.getFieldIdentifier());
            if (result != null)
                return result;
        }

        for (MatchersSequenceType sequences : sequences(definition)) {
            String result = match(definition, sequences.getExpression(), sequences.getSequenceIdentifier());
            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaIdentifier(definition);
    }

    @Override
    public String getJavaSetterName(Definition definition, Mode mode) {
        for (MatchersFieldType fields : fields(definition)) {
            String result = match(definition, fields.getExpression(), fields.getFieldSetter());
            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaSetterName(definition, mode);
    }

    @Override
    public String getJavaGetterName(Definition definition, Mode mode) {
        for (MatchersFieldType fields : fields(definition)) {
            String result = match(definition, fields.getExpression(), fields.getFieldGetter());
            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaGetterName(definition, mode);
    }

    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        for (MatchersRoutineType routines : routines(definition)) {
            String result = match(definition, routines.getExpression(), routines.getRoutineMethod());
            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaMethodName(definition, mode);
    }

    @Override
    public String getJavaClassExtends(Definition definition, Mode mode) {
        for (MatchersTableType tables : tables(definition)) {
            String result = null;

            switch (mode) {
                case POJO: result = match(definition, tables.getExpression(), tables.getPojoExtends()); break;
            }

            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaClassExtends(definition, mode);
    }

    @Override
    public List<String> getJavaClassImplements(Definition definition, Mode mode) {
        for (MatchersSchemaType schemas : schemas(definition)) {
            String result = match(definition, schemas.getExpression(), schemas.getSchemaImplements());
            if (result != null)
                return split(result);
        }

        for (MatchersTableType tables : tables(definition)) {
            String result = null;

            switch (mode) {
                case DEFAULT:   result = match(definition, tables.getExpression(), tables.getTableImplements());     break;
                case DAO:       result = match(definition, tables.getExpression(), tables.getDaoImplements());       break;
                case INTERFACE: result = match(definition, tables.getExpression(), tables.getInterfaceImplements()); break;
                case POJO:      result = match(definition, tables.getExpression(), tables.getPojoImplements());      break;
                case RECORD:    result = match(definition, tables.getExpression(), tables.getRecordImplements());    break;
            }

            if (result != null) {
                return split(result);
            }
        }

        for (MatchersRoutineType routines : routines(definition)) {
            String result = match(definition, routines.getExpression(), routines.getRoutineImplements());
            if (result != null)
                return split(result);
        }

        // Default to standard behaviour
        return super.getJavaClassImplements(definition, mode);
    }

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        for (MatchersSchemaType schemas : schemas(definition)) {
            String result = match(definition, schemas.getExpression(), schemas.getSchemaClass());
            if (result != null)
                return result;
        }

        for (MatchersTableType tables : tables(definition)) {
            String result = null;

            switch (mode) {
                case DEFAULT:   result = match(definition, tables.getExpression(), tables.getTableClass());     break;
                case DAO:       result = match(definition, tables.getExpression(), tables.getDaoClass());       break;
                case INTERFACE: result = match(definition, tables.getExpression(), tables.getInterfaceClass()); break;
                case POJO:      result = match(definition, tables.getExpression(), tables.getPojoClass());      break;
                case RECORD:    result = match(definition, tables.getExpression(), tables.getRecordClass());    break;
            }

            if (result != null)
                return result;
        }

        for (MatchersRoutineType routines : routines(definition)) {
            String result = match(definition, routines.getExpression(), routines.getRoutineClass());
            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaClassName(definition, mode);
    }

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        return super.getJavaPackageName(definition, mode);
    }

    @Override
    public String getJavaMemberName(Definition definition, Mode mode) {
        for (MatchersFieldType fields : fields(definition)) {
            String result = match(definition, fields.getExpression(), fields.getFieldMember());
            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaMemberName(definition, mode);
    }

    @Override
    public String getOverloadSuffix(Definition definition, Mode mode, String overloadIndex) {
        return super.getOverloadSuffix(definition, mode, overloadIndex);
    }
}
