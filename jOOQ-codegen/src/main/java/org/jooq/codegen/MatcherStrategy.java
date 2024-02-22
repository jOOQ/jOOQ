/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
 *
 *
 *
 */
package org.jooq.codegen;

import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.jooq.tools.StringUtils.defaultIfEmpty;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.jooq.meta.AttributeDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.Definition;
import org.jooq.meta.EmbeddableDefinition;
import org.jooq.meta.EnumDefinition;
import org.jooq.meta.ForeignKeyDefinition;
import org.jooq.meta.IndexDefinition;
import org.jooq.meta.InverseForeignKeyDefinition;
import org.jooq.meta.ManyToManyKeyDefinition;
import org.jooq.meta.Patterns;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.SequenceDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.UDTDefinition;
import org.jooq.meta.UniqueKeyDefinition;
import org.jooq.meta.jaxb.*;
import org.jooq.tools.StringUtils;

/**
 * A generator strategy that names objects according to a {@link Matchers}
 * configuration object.
 *
 * @author Lukas Eder
 */
public class MatcherStrategy extends DefaultGeneratorStrategy {

    private final Matchers matchers;
    private final Patterns patterns;

    public MatcherStrategy(Matchers matchers) {
        this(matchers, new Patterns());
    }

    public MatcherStrategy(Matchers matchers, Patterns patterns) {
        this.matchers = matchers;
        this.patterns = patterns;
    }

    public Matchers getMatchers() {
        return matchers;
    }

    public Patterns getPatterns() {
        return patterns;
    }

    /**
     * Take a {@link Definition}, try to match its name or qualified name
     * against an expression.
     */
    private final boolean match(Definition definition, String expression) {
        return match(definition.getName(), expression)
            || match(definition.getQualifiedName(), expression);
    }

    private final boolean match(String name, String expression) {
        return matcher(name, expression) != null;
    }

    private final Matcher matcher(String name, String expression) {
        Pattern p = patterns.pattern(defaultIfEmpty(expression, "^.*$").trim());
        Matcher m = p.matcher(name);
        return m.matches() ? m : null;
    }

    /**
     * Take a {@link Definition}, try to match its name or qualified name
     * against an expression, and apply a rule upon match.
     */
    private final String match(Definition definition, String expression, MatcherRule rule) {
        if (rule != null)
            return match(definition, expression, rule.getExpression(), rule.getTransform());

        return null;
    }

    /**
     * Take a {@link Definition}, try to match its name or qualified name
     * against an expression, and apply a rule upon match.
     */
    private final String match(String name, String expression, MatcherRule rule) {
        if (rule != null)
            return match(name, expression, rule.getExpression(), rule.getTransform());

        return null;
    }

    private final String match(Definition definition, String expression, String ruleExpression) {
        return match(definition, expression, ruleExpression, null);
    }

    private final String match(Definition definition, String expression, String ruleExpression, MatcherTransformType ruleTransformType) {
        String result = match(definition.getName(), expression, ruleExpression, ruleTransformType);

        if (result != null)
            return result;

        return match(definition.getQualifiedName(), expression, ruleExpression, ruleTransformType);
    }

    private final String match(String name, String expression, String ruleExpression, MatcherTransformType ruleTransformType) {
        // [#3734] If users forget to specify the rule's expression but they use
        // a transformer (e.g. PASCAL), we should assume the "default" replacement
        // [#15464] The default for Path classes is generated elsewhere
        if (ruleTransformType != null && ruleExpression == null)
            ruleExpression = "$0";

        if (ruleExpression != null) {
            Matcher m = matcher(name, expression);

            if (m != null)
                return transform(m.replaceAll(ruleExpression), ruleTransformType);
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
                return string.toLowerCase(getTargetLocale());
            case LOWER_FIRST_LETTER:
                return StringUtils.toLC(string);
            case UPPER:
                return string.toUpperCase(getTargetLocale());
            case UPPER_FIRST_LETTER:
                return StringUtils.toUC(string);
            case CAMEL:
                return StringUtils.toCamelCaseLC(string);
            case PASCAL:
                return StringUtils.toCamelCase(string);

            default:
                throw new UnsupportedOperationException("Transform Type not supported : " + transform);
        }
    }

    private final List<MatchersCatalogType> catalogs(Definition definition) {
        if (definition instanceof CatalogDefinition)
            return matchers.getCatalogs();

        return emptyList();
    }

    private final List<MatchersSchemaType> schemas(Definition definition) {
        if (definition instanceof SchemaDefinition)
            return matchers.getSchemas();

        return emptyList();
    }

    private final List<MatchersTableType> tables(Definition definition) {
        if (definition instanceof TableDefinition)
            return matchers.getTables();

        return emptyList();
    }

    private final List<MatchersIndexType> indexes(Definition definition) {
        if (definition instanceof IndexDefinition)
            return matchers.getIndexes();

        return emptyList();
    }

    private final List<MatchersPrimaryKeyType> primaryKeys(Definition definition) {
        if (definition instanceof UniqueKeyDefinition u)
            if (u.isPrimaryKey())
                return matchers.getPrimaryKeys();

        return emptyList();
    }

    private final List<MatchersUniqueKeyType> uniqueKeys(Definition definition) {
        if (definition instanceof UniqueKeyDefinition u)
            if (!u.isPrimaryKey())
                return matchers.getUniqueKeys();

        return emptyList();
    }

    private final List<MatchersForeignKeyType> foreignKeys(Definition definition) {
        if (definition instanceof ForeignKeyDefinition)
            return matchers.getForeignKeys();

        return emptyList();
    }

    private final List<MatchersForeignKeyType> inverseForeignKeys(Definition definition) {
        if (definition instanceof InverseForeignKeyDefinition)
            return matchers.getForeignKeys();

        return emptyList();
    }

    private final List<MatchersForeignKeyType> manyToManyKeys(Definition definition) {
        if (definition instanceof ManyToManyKeyDefinition)
            return matchers.getForeignKeys();

        return emptyList();
    }

    private final List<MatchersFieldType> fields(Definition definition) {
        if (definition instanceof ColumnDefinition)
            return matchers.getFields();

        return emptyList();
    }

    private final List<MatchersRoutineType> routines(Definition definition) {
        if (definition instanceof RoutineDefinition)
            return matchers.getRoutines();

        return emptyList();
    }

    private final List<MatchersSequenceType> sequences(Definition definition) {
        if (definition instanceof SequenceDefinition)
            return matchers.getSequences();

        return emptyList();
    }

    private final List<MatchersEnumType> enums(Definition definition) {
        if (definition instanceof EnumDefinition)
            return matchers.getEnums();

        return emptyList();
    }

    private final List<MatchersEmbeddableType> embeddables(Definition definition) {
        if (definition instanceof EmbeddableDefinition)
            return matchers.getEmbeddables();

        return emptyList();
    }

    private final List<MatchersUDTType> udts(Definition definition) {
        if (definition instanceof UDTDefinition)
            return matchers.getUdts();

        return emptyList();
    }

    private final List<MatchersAttributeType> attributes(Definition definition) {
        if (definition instanceof AttributeDefinition)
            return matchers.getAttributes();

        return emptyList();
    }

    private final List<String> split(String result) {
        return Stream.of(result.split(",")).map(String::trim).collect(toList());
    }

    @Override
    public String getJavaIdentifier(Definition definition) {
        for (MatchersCatalogType catalogs : catalogs(definition)) {
            String result = match(definition, catalogs.getExpression(), catalogs.getCatalogIdentifier());
            if (result != null)
                return result;
        }

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

        for (MatchersIndexType index : indexes(definition)) {
            String result = match(definition, index.getExpression(), index.getKeyIdentifier());
            if (result != null)
                return result;
        }

        for (MatchersPrimaryKeyType primaryKey : primaryKeys(definition)) {
            String result = match(definition, primaryKey.getExpression(), primaryKey.getKeyIdentifier());
            if (result != null)
                return result;
        }

        for (MatchersUniqueKeyType uniqueKey : uniqueKeys(definition)) {
            String result = match(definition, uniqueKey.getExpression(), uniqueKey.getKeyIdentifier());
            if (result != null)
                return result;
        }

        for (MatchersForeignKeyType foreignKey : foreignKeys(definition)) {
            String result = match(definition, foreignKey.getExpression(), foreignKey.getKeyIdentifier());
            if (result != null)
                return result;
        }

        for (MatchersForeignKeyType foreignKey : inverseForeignKeys(definition)) {
            String result = match(definition, foreignKey.getExpression(), foreignKey.getKeyIdentifier());
            if (result != null)
                return result;
        }

        for (MatchersUDTType udt : udts(definition)) {
            String result = match(definition, udt.getExpression(), udt.getUdtIdentifier());
            if (result != null)
                return result;
        }

        for (MatchersAttributeType attribute : attributes(definition)) {
            String result = match(definition, attribute.getExpression(), attribute.getAttributeIdentifier());
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

        for (MatchersAttributeType attribute : attributes(definition)) {
            String result = match(definition, attribute.getExpression(), attribute.getAttributeSetter());
            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaSetterName(definition, mode);
    }

    @Override
    public boolean getJavaSetterOverride(Definition definition, Mode mode) {
        switch (mode) {
            case RECORD:
                return getJavaOverride0(definition,
                    MatchersFieldType::isRecordSetterOverride,
                    MatchersAttributeType::isRecordSetterOverride,
                    () -> super.getJavaMemberOverride(definition, mode)
                );
            case INTERFACE:
                return getJavaOverride0(definition,
                    MatchersFieldType::isInterfaceSetterOverride,
                    MatchersAttributeType::isInterfaceSetterOverride,
                    () -> super.getJavaMemberOverride(definition, mode)
                );
            case POJO:
                return getJavaOverride0(definition,
                    MatchersFieldType::isPojoSetterOverride,
                    MatchersAttributeType::isPojoSetterOverride,
                    () -> super.getJavaMemberOverride(definition, mode)
                );
        }

        return super.getJavaMemberOverride(definition, mode);
    }

    @Override
    public String getJavaGetterName(Definition definition, Mode mode) {
        for (MatchersFieldType fields : fields(definition)) {
            String result = match(definition, fields.getExpression(), fields.getFieldGetter());
            if (result != null)
                return result;
        }

        for (MatchersAttributeType attribute : attributes(definition)) {
            String result = match(definition, attribute.getExpression(), attribute.getAttributeGetter());
            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaGetterName(definition, mode);
    }

    @Override
    public boolean getJavaGetterOverride(Definition definition, Mode mode) {
        switch (mode) {
            case RECORD:
                return getJavaOverride0(definition,
                    MatchersFieldType::isRecordGetterOverride,
                    MatchersAttributeType::isRecordGetterOverride,
                    () -> super.getJavaMemberOverride(definition, mode)
                );
            case INTERFACE:
                return getJavaOverride0(definition,
                    MatchersFieldType::isInterfaceGetterOverride,
                    MatchersAttributeType::isInterfaceGetterOverride,
                    () -> super.getJavaMemberOverride(definition, mode)
                );
            case POJO:
                return getJavaOverride0(definition,
                    MatchersFieldType::isPojoGetterOverride,
                    MatchersAttributeType::isPojoGetterOverride,
                    () -> super.getJavaMemberOverride(definition, mode)
                );
        }

        return super.getJavaMemberOverride(definition, mode);
    }

    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        for (MatchersRoutineType routines : routines(definition)) {
            String result = match(definition, routines.getExpression(), routines.getRoutineMethod());

            if (result != null)
                return result;
        }

        for (MatchersForeignKeyType foreignKeys : foreignKeys(definition)) {
            String result = match(definition, foreignKeys.getExpression(), foreignKeys.getPathMethodName());

            if (result != null)
                return result;
        }

        for (MatchersForeignKeyType inverseForeignKeys : inverseForeignKeys(definition)) {
            String result = match(definition, inverseForeignKeys.getExpression(), inverseForeignKeys.getPathMethodNameInverse());

            if (result != null)
                return result;
        }

        for (MatchersForeignKeyType manyToManyKeys : manyToManyKeys(definition)) {
            String result = match(definition, manyToManyKeys.getExpression(), manyToManyKeys.getPathMethodNameManyToMany());

            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaMethodName(definition, mode);
    }

    @Override
    public String getJavaClassExtends(Definition definition, Mode mode) {
        for (MatchersCatalogType catalogs : catalogs(definition)) {
            String result = match(definition, catalogs.getExpression(), catalogs.getCatalogExtends());

            if (result != null)
                return result;
        }

        for (MatchersSchemaType schemas : schemas(definition)) {
            String result = match(definition, schemas.getExpression(), schemas.getSchemaExtends());

            if (result != null)
                return result;
        }

        for (MatchersTableType tables : tables(definition)) {
            String result = null;

            switch (mode) {
                case POJO:    result = match(definition, tables.getExpression(), tables.getPojoExtends());   break;
                case RECORD:  result = match(definition, tables.getExpression(), tables.getRecordExtends()); break;
                case DAO:     result = match(definition, tables.getExpression(), tables.getDaoExtends());    break;
                case PATH:    result = match(definition, tables.getExpression(), tables.getPathExtends());   break;
                case DEFAULT: result = match(definition, tables.getExpression(), tables.getTableExtends());  break;
            }

            if (result != null)
                return result;
        }

        for (MatchersEmbeddableType embeddables : embeddables(definition)) {
            String result = null;

            switch (mode) {
                case POJO: result = match(definition, embeddables.getExpression(), embeddables.getPojoExtends()); break;
                case RECORD: result = match(definition, embeddables.getExpression(), embeddables.getRecordExtends()); break;
            }

            if (result != null)
                return result;
        }

        for (MatchersUDTType udt : udts(definition)) {
            String result = null;

            switch (mode) {
                case POJO:    result = match(definition, udt.getExpression(), udt.getPojoExtends());   break;
                case RECORD:  result = match(definition, udt.getExpression(), udt.getRecordExtends()); break;
                case PATH:    result = match(definition, udt.getExpression(), udt.getPathExtends());   break;
                case DEFAULT: result = match(definition, udt.getExpression(), udt.getUdtExtends());    break;
            }

            if (result != null)
                return result;
        }

        for (MatchersRoutineType routine : routines(definition)) {
            String result = match(definition, routine.getExpression(), routine.getRoutineExtends());

            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaClassExtends(definition, mode);
    }

    @Override
    public List<String> getJavaClassImplements(Definition definition, Mode mode) {
        for (MatchersCatalogType catalogs : catalogs(definition)) {
            String result = match(definition, catalogs.getExpression(), catalogs.getCatalogImplements());

            if (result != null)
                return split(result);
        }

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
                case PATH:      result = match(definition, tables.getExpression(), tables.getTableImplements());     break;
            }

            if (result != null)
                return split(result);
        }

        for (MatchersEmbeddableType embeddables : embeddables(definition)) {
            String result = null;

            switch (mode) {
                case INTERFACE: result = match(definition, embeddables.getExpression(), embeddables.getInterfaceImplements()); break;
                case POJO:      result = match(definition, embeddables.getExpression(), embeddables.getPojoImplements());      break;
                case RECORD:
                case DEFAULT:   result = match(definition, embeddables.getExpression(), embeddables.getRecordImplements());    break;
            }

            if (result != null)
                return split(result);
        }

        for (MatchersUDTType udt : udts(definition)) {
            String result = null;

            switch (mode) {
                case DEFAULT:   result = match(definition, udt.getExpression(), udt.getUdtImplements());       break;
                case INTERFACE: result = match(definition, udt.getExpression(), udt.getInterfaceImplements()); break;
                case POJO:      result = match(definition, udt.getExpression(), udt.getPojoImplements());      break;
                case RECORD:    result = match(definition, udt.getExpression(), udt.getRecordImplements());    break;
                case PATH:      result = match(definition, udt.getExpression(), udt.getPathImplements());      break;
            }

            if (result != null)
                return split(result);
        }

        for (MatchersRoutineType routines : routines(definition)) {
            String result = match(definition, routines.getExpression(), routines.getRoutineImplements());

            if (result != null)
                return split(result);
        }

        for (MatchersEnumType enums : enums(definition)) {
            String result = match(definition, enums.getExpression(), enums.getEnumImplements());

            if (result != null)
                return split(result);
        }

        // Default to standard behaviour
        return super.getJavaClassImplements(definition, mode);
    }

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        for (MatchersCatalogType catalogs : catalogs(definition)) {
            String result = match(definition, catalogs.getExpression(), catalogs.getCatalogClass());

            if (result != null)
                return result;
        }

        for (MatchersSchemaType schemas : schemas(definition)) {
            String result = match(definition, schemas.getExpression(), schemas.getSchemaClass());

            if (result != null)
                return result;
        }

        for (MatchersTableType tables : tables(definition)) {
            String result = null;

            switch (mode) {
                case DEFAULT:   result = match(definition, tables.getExpression(), tables.getTableClass());             break;
                case DAO:       result = match(definition, tables.getExpression(), tables.getDaoClass());               break;
                case INTERFACE: result = match(definition, tables.getExpression(), tables.getInterfaceClass());         break;
                case POJO:      result = match(definition, tables.getExpression(), tables.getPojoClass());              break;
                case RECORD:    result = match(definition, tables.getExpression(), tables.getRecordClass());            break;
                case PATH:      result = match(definition, tables.getExpression(), pathDefault(tables.getPathClass())); break;
            }

            if (result != null)
                return result;
        }

        for (MatchersEmbeddableType embeddables : embeddables(definition)) {
            String result = null;

            switch (mode) {
                case INTERFACE: result = match(definition, embeddables.getExpression(), embeddables.getInterfaceClass()); break;
                case POJO:      result = match(definition, embeddables.getExpression(), embeddables.getPojoClass());      break;
                case RECORD:
                case DEFAULT:   result = match(definition, embeddables.getExpression(), embeddables.getRecordClass());    break;
            }

            if (result != null)
                return result;
        }

        for (MatchersUDTType udt : udts(definition)) {
            String result = null;

            switch (mode) {
                case DEFAULT:   result = match(definition, udt.getExpression(), udt.getUdtClass());               break;
                case INTERFACE: result = match(definition, udt.getExpression(), udt.getInterfaceClass());         break;
                case POJO:      result = match(definition, udt.getExpression(), udt.getPojoClass());              break;
                case RECORD:    result = match(definition, udt.getExpression(), udt.getRecordClass());            break;
                case PATH:      result = match(definition, udt.getExpression(), udt.getPathClass());              break;
            }

            if (result != null)
                return result;
        }

        for (MatchersRoutineType routines : routines(definition)) {
            String result = match(definition, routines.getExpression(), routines.getRoutineClass());

            if (result != null)
                return result;
        }

        for (MatchersEnumType enums : enums(definition)) {
            String result = match(definition, enums.getExpression(), enums.getEnumClass());

            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaClassName(definition, mode);
    }

    private final MatcherRule pathDefault(MatcherRule rule) {
        if (rule == null)
            return null;

        if (!StringUtils.isBlank(rule.getExpression()))
            return rule;

        MatcherTransformType transform = rule.getTransform();

        if (transform != null)
            return new MatcherRule().withTransform(transform).withExpression("$0_PATH");
        else
            return rule;
    }

    @Override
    public String getJavaEnumLiteral(EnumDefinition definition, String literal) {
        for (MatchersEnumType enums : enums(definition)) {
            String result = match(definition, enums.getExpression(), enums.getEnumClass());

            if (result != null)
                result = match(literal, null, enums.getEnumLiteral());

            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaEnumLiteral(definition, literal);
    }

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        return super.getJavaPackageName(definition, mode);
    }

    @Override
    public String getJavaMemberName(Definition definition, Mode mode) {
        for (MatchersFieldType fields : fields(definition)) {
            String result = null;

            switch (mode) {
                case DAO:     result = match(definition, fields.getExpression(), fields.getDaoMember()); break;
                case DEFAULT:
                default:      result = match(definition, fields.getExpression(), fields.getFieldMember()); break;
            }

            if (result != null)
                return result;
        }

        for (MatchersAttributeType attribute : attributes(definition)) {
            String result = match(definition, attribute.getExpression(), attribute.getAttributeMember());

            if (result != null)
                return result;
        }

        // Default to standard behaviour
        return super.getJavaMemberName(definition, mode);
    }

    private boolean getJavaOverride0(
        Definition definition,
        Function<? super MatchersFieldType, ? extends Boolean> isFieldOverride,
        Function<? super MatchersAttributeType, ? extends Boolean> isAttributeOverride,
        Supplier<? extends Boolean> defaultOverride
    ) {
        for (MatchersFieldType field : fields(definition))
            if (match(definition, field.getExpression()) && TRUE.equals(isFieldOverride.apply(field)))
                return true;

        for (MatchersAttributeType attribute : attributes(definition))
            if (match(definition, attribute.getExpression()) && TRUE.equals(isAttributeOverride.apply(attribute)))
                return true;

        // Default to standard behaviour
        return defaultOverride.get();
    }

    @Override
    public boolean getJavaMemberOverride(Definition definition, Mode mode) {
        switch (mode) {
            case RECORD:
                return getJavaOverride0(definition,
                    MatchersFieldType::isRecordMemberOverride,
                    MatchersAttributeType::isRecordMemberOverride,
                    () -> super.getJavaMemberOverride(definition, mode)
                );
            case INTERFACE:
                return getJavaOverride0(definition,
                    MatchersFieldType::isInterfaceMemberOverride,
                    MatchersAttributeType::isInterfaceMemberOverride,
                    () -> super.getJavaMemberOverride(definition, mode)
                );
            case POJO:
                return getJavaOverride0(definition,
                    MatchersFieldType::isPojoMemberOverride,
                    MatchersAttributeType::isPojoMemberOverride,
                    () -> super.getJavaMemberOverride(definition, mode)
                );
        }

        return super.getJavaMemberOverride(definition, mode);
    }

    @Override
    public String getOverloadSuffix(Definition definition, Mode mode, String overloadIndex) {
        return super.getOverloadSuffix(definition, mode, overloadIndex);
    }
}
