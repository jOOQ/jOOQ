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
package org.jooq.codegen.gradle;

import java.util.*;
import groovy.lang.*;
import org.gradle.api.*;
import org.jooq.meta.jaxb.*;

/**
 * Extensions for the jOOQ-meta types, to enable groovy DSL usage.
 */
public class MetaExtensions {

    static void call(Closure<?> closure, Object delegate) {
        closure = (Closure<?>) closure.clone();
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.setDelegate(delegate);

        if (closure.getMaximumNumberOfParameters() == 0)
            closure.call();
        else
            closure.call(delegate);
    }



    public static class ConfigurationExtension extends Configuration {

        public void jdbc(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = JdbcExtension.class) Closure<?> closure) {
            JdbcExtension o = new JdbcExtension();
            call(closure, o);
            setJdbc(o);
        }

        public void jdbc(Action<JdbcExtension> action) {
            JdbcExtension o = new JdbcExtension();
            action.execute(o);
            setJdbc(o);
        }

        public void generator(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = GeneratorExtension.class) Closure<?> closure) {
            GeneratorExtension o = new GeneratorExtension();
            call(closure, o);
            setGenerator(o);
        }

        public void generator(Action<GeneratorExtension> action) {
            GeneratorExtension o = new GeneratorExtension();
            action.execute(o);
            setGenerator(o);
        }
    }

    public static class JdbcExtension extends Jdbc {

        public void properties(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = PropertyListExtension.class) Closure<?> closure) {
            PropertyListExtension l = new PropertyListExtension();
            call(closure, l);
            setProperties(l);
        }

        public void properties(Action<PropertyListExtension> action) {
            PropertyListExtension l = new PropertyListExtension();
            action.execute(l);
            setProperties(l);
        }
    }

    public static class PropertyExtension extends Property {
    }

    public static class GeneratorExtension extends Generator {

        public void strategy(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = StrategyExtension.class) Closure<?> closure) {
            StrategyExtension o = new StrategyExtension();
            call(closure, o);
            setStrategy(o);
        }

        public void strategy(Action<StrategyExtension> action) {
            StrategyExtension o = new StrategyExtension();
            action.execute(o);
            setStrategy(o);
        }

        public void database(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = DatabaseExtension.class) Closure<?> closure) {
            DatabaseExtension o = new DatabaseExtension();
            call(closure, o);
            setDatabase(o);
        }

        public void database(Action<DatabaseExtension> action) {
            DatabaseExtension o = new DatabaseExtension();
            action.execute(o);
            setDatabase(o);
        }

        public void generate(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = GenerateExtension.class) Closure<?> closure) {
            GenerateExtension o = new GenerateExtension();
            call(closure, o);
            setGenerate(o);
        }

        public void generate(Action<GenerateExtension> action) {
            GenerateExtension o = new GenerateExtension();
            action.execute(o);
            setGenerate(o);
        }

        public void target(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = TargetExtension.class) Closure<?> closure) {
            TargetExtension o = new TargetExtension();
            call(closure, o);
            setTarget(o);
        }

        public void target(Action<TargetExtension> action) {
            TargetExtension o = new TargetExtension();
            action.execute(o);
            setTarget(o);
        }
    }

    public static class StrategyExtension extends Strategy {

        public void matchers(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersExtension.class) Closure<?> closure) {
            MatchersExtension o = new MatchersExtension();
            call(closure, o);
            setMatchers(o);
        }

        public void matchers(Action<MatchersExtension> action) {
            MatchersExtension o = new MatchersExtension();
            action.execute(o);
            setMatchers(o);
        }
    }

    public static class MatchersExtension extends Matchers {

        public void catalogs(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersCatalogTypeListExtension.class) Closure<?> closure) {
            MatchersCatalogTypeListExtension l = new MatchersCatalogTypeListExtension();
            call(closure, l);
            setCatalogs(l);
        }

        public void catalogs(Action<MatchersCatalogTypeListExtension> action) {
            MatchersCatalogTypeListExtension l = new MatchersCatalogTypeListExtension();
            action.execute(l);
            setCatalogs(l);
        }

        public void schemas(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersSchemaTypeListExtension.class) Closure<?> closure) {
            MatchersSchemaTypeListExtension l = new MatchersSchemaTypeListExtension();
            call(closure, l);
            setSchemas(l);
        }

        public void schemas(Action<MatchersSchemaTypeListExtension> action) {
            MatchersSchemaTypeListExtension l = new MatchersSchemaTypeListExtension();
            action.execute(l);
            setSchemas(l);
        }

        public void tables(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersTableTypeListExtension.class) Closure<?> closure) {
            MatchersTableTypeListExtension l = new MatchersTableTypeListExtension();
            call(closure, l);
            setTables(l);
        }

        public void tables(Action<MatchersTableTypeListExtension> action) {
            MatchersTableTypeListExtension l = new MatchersTableTypeListExtension();
            action.execute(l);
            setTables(l);
        }

        public void indexes(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersIndexTypeListExtension.class) Closure<?> closure) {
            MatchersIndexTypeListExtension l = new MatchersIndexTypeListExtension();
            call(closure, l);
            setIndexes(l);
        }

        public void indexes(Action<MatchersIndexTypeListExtension> action) {
            MatchersIndexTypeListExtension l = new MatchersIndexTypeListExtension();
            action.execute(l);
            setIndexes(l);
        }

        public void primaryKeys(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersPrimaryKeyTypeListExtension.class) Closure<?> closure) {
            MatchersPrimaryKeyTypeListExtension l = new MatchersPrimaryKeyTypeListExtension();
            call(closure, l);
            setPrimaryKeys(l);
        }

        public void primaryKeys(Action<MatchersPrimaryKeyTypeListExtension> action) {
            MatchersPrimaryKeyTypeListExtension l = new MatchersPrimaryKeyTypeListExtension();
            action.execute(l);
            setPrimaryKeys(l);
        }

        public void uniqueKeys(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersUniqueKeyTypeListExtension.class) Closure<?> closure) {
            MatchersUniqueKeyTypeListExtension l = new MatchersUniqueKeyTypeListExtension();
            call(closure, l);
            setUniqueKeys(l);
        }

        public void uniqueKeys(Action<MatchersUniqueKeyTypeListExtension> action) {
            MatchersUniqueKeyTypeListExtension l = new MatchersUniqueKeyTypeListExtension();
            action.execute(l);
            setUniqueKeys(l);
        }

        public void foreignKeys(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersForeignKeyTypeListExtension.class) Closure<?> closure) {
            MatchersForeignKeyTypeListExtension l = new MatchersForeignKeyTypeListExtension();
            call(closure, l);
            setForeignKeys(l);
        }

        public void foreignKeys(Action<MatchersForeignKeyTypeListExtension> action) {
            MatchersForeignKeyTypeListExtension l = new MatchersForeignKeyTypeListExtension();
            action.execute(l);
            setForeignKeys(l);
        }

        public void fields(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersFieldTypeListExtension.class) Closure<?> closure) {
            MatchersFieldTypeListExtension l = new MatchersFieldTypeListExtension();
            call(closure, l);
            setFields(l);
        }

        public void fields(Action<MatchersFieldTypeListExtension> action) {
            MatchersFieldTypeListExtension l = new MatchersFieldTypeListExtension();
            action.execute(l);
            setFields(l);
        }

        public void routines(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersRoutineTypeListExtension.class) Closure<?> closure) {
            MatchersRoutineTypeListExtension l = new MatchersRoutineTypeListExtension();
            call(closure, l);
            setRoutines(l);
        }

        public void routines(Action<MatchersRoutineTypeListExtension> action) {
            MatchersRoutineTypeListExtension l = new MatchersRoutineTypeListExtension();
            action.execute(l);
            setRoutines(l);
        }

        public void sequences(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersSequenceTypeListExtension.class) Closure<?> closure) {
            MatchersSequenceTypeListExtension l = new MatchersSequenceTypeListExtension();
            call(closure, l);
            setSequences(l);
        }

        public void sequences(Action<MatchersSequenceTypeListExtension> action) {
            MatchersSequenceTypeListExtension l = new MatchersSequenceTypeListExtension();
            action.execute(l);
            setSequences(l);
        }

        public void enums(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersEnumTypeListExtension.class) Closure<?> closure) {
            MatchersEnumTypeListExtension l = new MatchersEnumTypeListExtension();
            call(closure, l);
            setEnums(l);
        }

        public void enums(Action<MatchersEnumTypeListExtension> action) {
            MatchersEnumTypeListExtension l = new MatchersEnumTypeListExtension();
            action.execute(l);
            setEnums(l);
        }

        public void embeddables(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersEmbeddableTypeListExtension.class) Closure<?> closure) {
            MatchersEmbeddableTypeListExtension l = new MatchersEmbeddableTypeListExtension();
            call(closure, l);
            setEmbeddables(l);
        }

        public void embeddables(Action<MatchersEmbeddableTypeListExtension> action) {
            MatchersEmbeddableTypeListExtension l = new MatchersEmbeddableTypeListExtension();
            action.execute(l);
            setEmbeddables(l);
        }

        public void udts(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersUDTTypeListExtension.class) Closure<?> closure) {
            MatchersUDTTypeListExtension l = new MatchersUDTTypeListExtension();
            call(closure, l);
            setUdts(l);
        }

        public void udts(Action<MatchersUDTTypeListExtension> action) {
            MatchersUDTTypeListExtension l = new MatchersUDTTypeListExtension();
            action.execute(l);
            setUdts(l);
        }

        public void attributes(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersAttributeTypeListExtension.class) Closure<?> closure) {
            MatchersAttributeTypeListExtension l = new MatchersAttributeTypeListExtension();
            call(closure, l);
            setAttributes(l);
        }

        public void attributes(Action<MatchersAttributeTypeListExtension> action) {
            MatchersAttributeTypeListExtension l = new MatchersAttributeTypeListExtension();
            action.execute(l);
            setAttributes(l);
        }
    }

    public static class MatchersCatalogTypeExtension extends MatchersCatalogType {

        public void catalogClass(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatcherRuleExtension.class) Closure<?> closure) {
            MatcherRuleExtension o = new MatcherRuleExtension();
            call(closure, o);
            setCatalogClass(o);
        }

        public void catalogClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = new MatcherRuleExtension();
            action.execute(o);
            setCatalogClass(o);
        }
    }

    public static class MatcherRuleExtension extends MatcherRule {
    }

    public static class MatchersSchemaTypeExtension extends MatchersSchemaType {
    }

    public static class MatchersTableTypeExtension extends MatchersTableType {
    }

    public static class MatchersIndexTypeExtension extends MatchersIndexType {
    }

    public static class MatchersPrimaryKeyTypeExtension extends MatchersPrimaryKeyType {
    }

    public static class MatchersUniqueKeyTypeExtension extends MatchersUniqueKeyType {
    }

    public static class MatchersForeignKeyTypeExtension extends MatchersForeignKeyType {
    }

    public static class MatchersFieldTypeExtension extends MatchersFieldType {
    }

    public static class MatchersRoutineTypeExtension extends MatchersRoutineType {
    }

    public static class MatchersSequenceTypeExtension extends MatchersSequenceType {
    }

    public static class MatchersEnumTypeExtension extends MatchersEnumType {
    }

    public static class MatchersEmbeddableTypeExtension extends MatchersEmbeddableType {
    }

    public static class MatchersUDTTypeExtension extends MatchersUDTType {
    }

    public static class MatchersAttributeTypeExtension extends MatchersAttributeType {
    }

    public static class DatabaseExtension extends Database {

        public void syntheticObjects(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticObjectsTypeExtension.class) Closure<?> closure) {
            SyntheticObjectsTypeExtension o = new SyntheticObjectsTypeExtension();
            call(closure, o);
            setSyntheticObjects(o);
        }

        public void syntheticObjects(Action<SyntheticObjectsTypeExtension> action) {
            SyntheticObjectsTypeExtension o = new SyntheticObjectsTypeExtension();
            action.execute(o);
            setSyntheticObjects(o);
        }

        public void properties(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = PropertyListExtension.class) Closure<?> closure) {
            PropertyListExtension l = new PropertyListExtension();
            call(closure, l);
            setProperties(l);
        }

        public void properties(Action<PropertyListExtension> action) {
            PropertyListExtension l = new PropertyListExtension();
            action.execute(l);
            setProperties(l);
        }

        public void comments(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CommentTypeListExtension.class) Closure<?> closure) {
            CommentTypeListExtension l = new CommentTypeListExtension();
            call(closure, l);
            setComments(l);
        }

        public void comments(Action<CommentTypeListExtension> action) {
            CommentTypeListExtension l = new CommentTypeListExtension();
            action.execute(l);
            setComments(l);
        }

        public void catalogs(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CatalogMappingTypeListExtension.class) Closure<?> closure) {
            CatalogMappingTypeListExtension l = new CatalogMappingTypeListExtension();
            call(closure, l);
            setCatalogs(l);
        }

        public void catalogs(Action<CatalogMappingTypeListExtension> action) {
            CatalogMappingTypeListExtension l = new CatalogMappingTypeListExtension();
            action.execute(l);
            setCatalogs(l);
        }

        public void schemata(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SchemaMappingTypeListExtension.class) Closure<?> closure) {
            SchemaMappingTypeListExtension l = new SchemaMappingTypeListExtension();
            call(closure, l);
            setSchemata(l);
        }

        public void schemata(Action<SchemaMappingTypeListExtension> action) {
            SchemaMappingTypeListExtension l = new SchemaMappingTypeListExtension();
            action.execute(l);
            setSchemata(l);
        }

        public void embeddables(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = EmbeddableDefinitionTypeListExtension.class) Closure<?> closure) {
            EmbeddableDefinitionTypeListExtension l = new EmbeddableDefinitionTypeListExtension();
            call(closure, l);
            setEmbeddables(l);
        }

        public void embeddables(Action<EmbeddableDefinitionTypeListExtension> action) {
            EmbeddableDefinitionTypeListExtension l = new EmbeddableDefinitionTypeListExtension();
            action.execute(l);
            setEmbeddables(l);
        }

        public void customTypes(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CustomTypeListExtension.class) Closure<?> closure) {
            CustomTypeListExtension l = new CustomTypeListExtension();
            call(closure, l);
            setCustomTypes(l);
        }

        public void customTypes(Action<CustomTypeListExtension> action) {
            CustomTypeListExtension l = new CustomTypeListExtension();
            action.execute(l);
            setCustomTypes(l);
        }

        public void enumTypes(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = EnumTypeListExtension.class) Closure<?> closure) {
            EnumTypeListExtension l = new EnumTypeListExtension();
            call(closure, l);
            setEnumTypes(l);
        }

        public void enumTypes(Action<EnumTypeListExtension> action) {
            EnumTypeListExtension l = new EnumTypeListExtension();
            action.execute(l);
            setEnumTypes(l);
        }

        public void forcedTypes(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = ForcedTypeListExtension.class) Closure<?> closure) {
            ForcedTypeListExtension l = new ForcedTypeListExtension();
            call(closure, l);
            setForcedTypes(l);
        }

        public void forcedTypes(Action<ForcedTypeListExtension> action) {
            ForcedTypeListExtension l = new ForcedTypeListExtension();
            action.execute(l);
            setForcedTypes(l);
        }
    }

    public static class SyntheticObjectsTypeExtension extends SyntheticObjectsType {

        public void readonlyColumns(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticReadonlyColumnTypeListExtension.class) Closure<?> closure) {
            SyntheticReadonlyColumnTypeListExtension l = new SyntheticReadonlyColumnTypeListExtension();
            call(closure, l);
            setReadonlyColumns(l);
        }

        public void readonlyColumns(Action<SyntheticReadonlyColumnTypeListExtension> action) {
            SyntheticReadonlyColumnTypeListExtension l = new SyntheticReadonlyColumnTypeListExtension();
            action.execute(l);
            setReadonlyColumns(l);
        }

        public void readonlyRowids(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticReadonlyRowidTypeListExtension.class) Closure<?> closure) {
            SyntheticReadonlyRowidTypeListExtension l = new SyntheticReadonlyRowidTypeListExtension();
            call(closure, l);
            setReadonlyRowids(l);
        }

        public void readonlyRowids(Action<SyntheticReadonlyRowidTypeListExtension> action) {
            SyntheticReadonlyRowidTypeListExtension l = new SyntheticReadonlyRowidTypeListExtension();
            action.execute(l);
            setReadonlyRowids(l);
        }

        public void columns(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticColumnTypeListExtension.class) Closure<?> closure) {
            SyntheticColumnTypeListExtension l = new SyntheticColumnTypeListExtension();
            call(closure, l);
            setColumns(l);
        }

        public void columns(Action<SyntheticColumnTypeListExtension> action) {
            SyntheticColumnTypeListExtension l = new SyntheticColumnTypeListExtension();
            action.execute(l);
            setColumns(l);
        }

        public void identities(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticIdentityTypeListExtension.class) Closure<?> closure) {
            SyntheticIdentityTypeListExtension l = new SyntheticIdentityTypeListExtension();
            call(closure, l);
            setIdentities(l);
        }

        public void identities(Action<SyntheticIdentityTypeListExtension> action) {
            SyntheticIdentityTypeListExtension l = new SyntheticIdentityTypeListExtension();
            action.execute(l);
            setIdentities(l);
        }

        public void enums(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticEnumTypeListExtension.class) Closure<?> closure) {
            SyntheticEnumTypeListExtension l = new SyntheticEnumTypeListExtension();
            call(closure, l);
            setEnums(l);
        }

        public void enums(Action<SyntheticEnumTypeListExtension> action) {
            SyntheticEnumTypeListExtension l = new SyntheticEnumTypeListExtension();
            action.execute(l);
            setEnums(l);
        }

        public void primaryKeys(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticPrimaryKeyTypeListExtension.class) Closure<?> closure) {
            SyntheticPrimaryKeyTypeListExtension l = new SyntheticPrimaryKeyTypeListExtension();
            call(closure, l);
            setPrimaryKeys(l);
        }

        public void primaryKeys(Action<SyntheticPrimaryKeyTypeListExtension> action) {
            SyntheticPrimaryKeyTypeListExtension l = new SyntheticPrimaryKeyTypeListExtension();
            action.execute(l);
            setPrimaryKeys(l);
        }

        public void uniqueKeys(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticUniqueKeyTypeListExtension.class) Closure<?> closure) {
            SyntheticUniqueKeyTypeListExtension l = new SyntheticUniqueKeyTypeListExtension();
            call(closure, l);
            setUniqueKeys(l);
        }

        public void uniqueKeys(Action<SyntheticUniqueKeyTypeListExtension> action) {
            SyntheticUniqueKeyTypeListExtension l = new SyntheticUniqueKeyTypeListExtension();
            action.execute(l);
            setUniqueKeys(l);
        }

        public void foreignKeys(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticForeignKeyTypeListExtension.class) Closure<?> closure) {
            SyntheticForeignKeyTypeListExtension l = new SyntheticForeignKeyTypeListExtension();
            call(closure, l);
            setForeignKeys(l);
        }

        public void foreignKeys(Action<SyntheticForeignKeyTypeListExtension> action) {
            SyntheticForeignKeyTypeListExtension l = new SyntheticForeignKeyTypeListExtension();
            action.execute(l);
            setForeignKeys(l);
        }

        public void views(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticViewTypeListExtension.class) Closure<?> closure) {
            SyntheticViewTypeListExtension l = new SyntheticViewTypeListExtension();
            call(closure, l);
            setViews(l);
        }

        public void views(Action<SyntheticViewTypeListExtension> action) {
            SyntheticViewTypeListExtension l = new SyntheticViewTypeListExtension();
            action.execute(l);
            setViews(l);
        }

        public void daos(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticDaoTypeListExtension.class) Closure<?> closure) {
            SyntheticDaoTypeListExtension l = new SyntheticDaoTypeListExtension();
            call(closure, l);
            setDaos(l);
        }

        public void daos(Action<SyntheticDaoTypeListExtension> action) {
            SyntheticDaoTypeListExtension l = new SyntheticDaoTypeListExtension();
            action.execute(l);
            setDaos(l);
        }
    }

    public static class SyntheticReadonlyColumnTypeExtension extends SyntheticReadonlyColumnType {
    }

    public static class SyntheticReadonlyRowidTypeExtension extends SyntheticReadonlyRowidType {
    }

    public static class SyntheticColumnTypeExtension extends SyntheticColumnType {
    }

    public static class SyntheticIdentityTypeExtension extends SyntheticIdentityType {
    }

    public static class SyntheticEnumTypeExtension extends SyntheticEnumType {
    }

    public static class SyntheticPrimaryKeyTypeExtension extends SyntheticPrimaryKeyType {
    }

    public static class SyntheticUniqueKeyTypeExtension extends SyntheticUniqueKeyType {
    }

    public static class SyntheticForeignKeyTypeExtension extends SyntheticForeignKeyType {
    }

    public static class SyntheticViewTypeExtension extends SyntheticViewType {
    }

    public static class SyntheticDaoTypeExtension extends SyntheticDaoType {

        public void methods(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticDaoMethodTypeListExtension.class) Closure<?> closure) {
            SyntheticDaoMethodTypeListExtension l = new SyntheticDaoMethodTypeListExtension();
            call(closure, l);
            setMethods(l);
        }

        public void methods(Action<SyntheticDaoMethodTypeListExtension> action) {
            SyntheticDaoMethodTypeListExtension l = new SyntheticDaoMethodTypeListExtension();
            action.execute(l);
            setMethods(l);
        }
    }

    public static class SyntheticDaoMethodTypeExtension extends SyntheticDaoMethodType {
    }

    public static class CommentTypeExtension extends CommentType {
    }

    public static class CatalogMappingTypeExtension extends CatalogMappingType {

        public void schemata(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SchemaMappingTypeListExtension.class) Closure<?> closure) {
            SchemaMappingTypeListExtension l = new SchemaMappingTypeListExtension();
            call(closure, l);
            setSchemata(l);
        }

        public void schemata(Action<SchemaMappingTypeListExtension> action) {
            SchemaMappingTypeListExtension l = new SchemaMappingTypeListExtension();
            action.execute(l);
            setSchemata(l);
        }
    }

    public static class SchemaMappingTypeExtension extends SchemaMappingType {
    }

    public static class EmbeddableDefinitionTypeExtension extends EmbeddableDefinitionType {

        public void fields(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = EmbeddableFieldListExtension.class) Closure<?> closure) {
            EmbeddableFieldListExtension l = new EmbeddableFieldListExtension();
            call(closure, l);
            setFields(l);
        }

        public void fields(Action<EmbeddableFieldListExtension> action) {
            EmbeddableFieldListExtension l = new EmbeddableFieldListExtension();
            action.execute(l);
            setFields(l);
        }
    }

    public static class EmbeddableFieldExtension extends EmbeddableField {
    }

    public static class CustomTypeExtension extends CustomType {

        public void lambdaConverter(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = LambdaConverterExtension.class) Closure<?> closure) {
            LambdaConverterExtension o = new LambdaConverterExtension();
            call(closure, o);
            setLambdaConverter(o);
        }

        public void lambdaConverter(Action<LambdaConverterExtension> action) {
            LambdaConverterExtension o = new LambdaConverterExtension();
            action.execute(o);
            setLambdaConverter(o);
        }
    }

    public static class LambdaConverterExtension extends LambdaConverter {
    }

    public static class EnumTypeExtension extends EnumType {
    }

    public static class ForcedTypeExtension extends ForcedType {
    }

    public static class GenerateExtension extends Generate {
    }

    public static class TargetExtension extends Target {
    }

    public static class PropertyListExtension extends ArrayList<Property> {
        public void property(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = PropertyExtension.class) Closure<?> closure) {
            PropertyExtension o = new PropertyExtension();
            call(closure, o);
            add(o);
        }

        public void property(Action<PropertyExtension> action) {
            PropertyExtension o = new PropertyExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersCatalogTypeListExtension extends ArrayList<MatchersCatalogType> {
        public void catalog(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersCatalogTypeExtension.class) Closure<?> closure) {
            MatchersCatalogTypeExtension o = new MatchersCatalogTypeExtension();
            call(closure, o);
            add(o);
        }

        public void catalog(Action<MatchersCatalogTypeExtension> action) {
            MatchersCatalogTypeExtension o = new MatchersCatalogTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersSchemaTypeListExtension extends ArrayList<MatchersSchemaType> {
        public void schema(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersSchemaTypeExtension.class) Closure<?> closure) {
            MatchersSchemaTypeExtension o = new MatchersSchemaTypeExtension();
            call(closure, o);
            add(o);
        }

        public void schema(Action<MatchersSchemaTypeExtension> action) {
            MatchersSchemaTypeExtension o = new MatchersSchemaTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersTableTypeListExtension extends ArrayList<MatchersTableType> {
        public void table(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersTableTypeExtension.class) Closure<?> closure) {
            MatchersTableTypeExtension o = new MatchersTableTypeExtension();
            call(closure, o);
            add(o);
        }

        public void table(Action<MatchersTableTypeExtension> action) {
            MatchersTableTypeExtension o = new MatchersTableTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersIndexTypeListExtension extends ArrayList<MatchersIndexType> {
        public void index(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersIndexTypeExtension.class) Closure<?> closure) {
            MatchersIndexTypeExtension o = new MatchersIndexTypeExtension();
            call(closure, o);
            add(o);
        }

        public void index(Action<MatchersIndexTypeExtension> action) {
            MatchersIndexTypeExtension o = new MatchersIndexTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersPrimaryKeyTypeListExtension extends ArrayList<MatchersPrimaryKeyType> {
        public void primaryKey(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersPrimaryKeyTypeExtension.class) Closure<?> closure) {
            MatchersPrimaryKeyTypeExtension o = new MatchersPrimaryKeyTypeExtension();
            call(closure, o);
            add(o);
        }

        public void primaryKey(Action<MatchersPrimaryKeyTypeExtension> action) {
            MatchersPrimaryKeyTypeExtension o = new MatchersPrimaryKeyTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersUniqueKeyTypeListExtension extends ArrayList<MatchersUniqueKeyType> {
        public void uniqueKey(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersUniqueKeyTypeExtension.class) Closure<?> closure) {
            MatchersUniqueKeyTypeExtension o = new MatchersUniqueKeyTypeExtension();
            call(closure, o);
            add(o);
        }

        public void uniqueKey(Action<MatchersUniqueKeyTypeExtension> action) {
            MatchersUniqueKeyTypeExtension o = new MatchersUniqueKeyTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersForeignKeyTypeListExtension extends ArrayList<MatchersForeignKeyType> {
        public void foreignKey(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersForeignKeyTypeExtension.class) Closure<?> closure) {
            MatchersForeignKeyTypeExtension o = new MatchersForeignKeyTypeExtension();
            call(closure, o);
            add(o);
        }

        public void foreignKey(Action<MatchersForeignKeyTypeExtension> action) {
            MatchersForeignKeyTypeExtension o = new MatchersForeignKeyTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersFieldTypeListExtension extends ArrayList<MatchersFieldType> {
        public void field(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersFieldTypeExtension.class) Closure<?> closure) {
            MatchersFieldTypeExtension o = new MatchersFieldTypeExtension();
            call(closure, o);
            add(o);
        }

        public void field(Action<MatchersFieldTypeExtension> action) {
            MatchersFieldTypeExtension o = new MatchersFieldTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersRoutineTypeListExtension extends ArrayList<MatchersRoutineType> {
        public void routine(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersRoutineTypeExtension.class) Closure<?> closure) {
            MatchersRoutineTypeExtension o = new MatchersRoutineTypeExtension();
            call(closure, o);
            add(o);
        }

        public void routine(Action<MatchersRoutineTypeExtension> action) {
            MatchersRoutineTypeExtension o = new MatchersRoutineTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersSequenceTypeListExtension extends ArrayList<MatchersSequenceType> {
        public void sequence(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersSequenceTypeExtension.class) Closure<?> closure) {
            MatchersSequenceTypeExtension o = new MatchersSequenceTypeExtension();
            call(closure, o);
            add(o);
        }

        public void sequence(Action<MatchersSequenceTypeExtension> action) {
            MatchersSequenceTypeExtension o = new MatchersSequenceTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersEnumTypeListExtension extends ArrayList<MatchersEnumType> {
        public void enum_(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersEnumTypeExtension.class) Closure<?> closure) {
            MatchersEnumTypeExtension o = new MatchersEnumTypeExtension();
            call(closure, o);
            add(o);
        }

        public void enum_(Action<MatchersEnumTypeExtension> action) {
            MatchersEnumTypeExtension o = new MatchersEnumTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersEmbeddableTypeListExtension extends ArrayList<MatchersEmbeddableType> {
        public void embeddable(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersEmbeddableTypeExtension.class) Closure<?> closure) {
            MatchersEmbeddableTypeExtension o = new MatchersEmbeddableTypeExtension();
            call(closure, o);
            add(o);
        }

        public void embeddable(Action<MatchersEmbeddableTypeExtension> action) {
            MatchersEmbeddableTypeExtension o = new MatchersEmbeddableTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersUDTTypeListExtension extends ArrayList<MatchersUDTType> {
        public void udt(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersUDTTypeExtension.class) Closure<?> closure) {
            MatchersUDTTypeExtension o = new MatchersUDTTypeExtension();
            call(closure, o);
            add(o);
        }

        public void udt(Action<MatchersUDTTypeExtension> action) {
            MatchersUDTTypeExtension o = new MatchersUDTTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersAttributeTypeListExtension extends ArrayList<MatchersAttributeType> {
        public void attribute(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersAttributeTypeExtension.class) Closure<?> closure) {
            MatchersAttributeTypeExtension o = new MatchersAttributeTypeExtension();
            call(closure, o);
            add(o);
        }

        public void attribute(Action<MatchersAttributeTypeExtension> action) {
            MatchersAttributeTypeExtension o = new MatchersAttributeTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class CommentTypeListExtension extends ArrayList<CommentType> {
        public void comment(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CommentTypeExtension.class) Closure<?> closure) {
            CommentTypeExtension o = new CommentTypeExtension();
            call(closure, o);
            add(o);
        }

        public void comment(Action<CommentTypeExtension> action) {
            CommentTypeExtension o = new CommentTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class CatalogMappingTypeListExtension extends ArrayList<CatalogMappingType> {
        public void catalog(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CatalogMappingTypeExtension.class) Closure<?> closure) {
            CatalogMappingTypeExtension o = new CatalogMappingTypeExtension();
            call(closure, o);
            add(o);
        }

        public void catalog(Action<CatalogMappingTypeExtension> action) {
            CatalogMappingTypeExtension o = new CatalogMappingTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class SchemaMappingTypeListExtension extends ArrayList<SchemaMappingType> {
        public void schema(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SchemaMappingTypeExtension.class) Closure<?> closure) {
            SchemaMappingTypeExtension o = new SchemaMappingTypeExtension();
            call(closure, o);
            add(o);
        }

        public void schema(Action<SchemaMappingTypeExtension> action) {
            SchemaMappingTypeExtension o = new SchemaMappingTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class EmbeddableDefinitionTypeListExtension extends ArrayList<EmbeddableDefinitionType> {
        public void embeddable(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = EmbeddableDefinitionTypeExtension.class) Closure<?> closure) {
            EmbeddableDefinitionTypeExtension o = new EmbeddableDefinitionTypeExtension();
            call(closure, o);
            add(o);
        }

        public void embeddable(Action<EmbeddableDefinitionTypeExtension> action) {
            EmbeddableDefinitionTypeExtension o = new EmbeddableDefinitionTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class CustomTypeListExtension extends ArrayList<CustomType> {
        public void customType(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CustomTypeExtension.class) Closure<?> closure) {
            CustomTypeExtension o = new CustomTypeExtension();
            call(closure, o);
            add(o);
        }

        public void customType(Action<CustomTypeExtension> action) {
            CustomTypeExtension o = new CustomTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class EnumTypeListExtension extends ArrayList<EnumType> {
        public void enumType(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = EnumTypeExtension.class) Closure<?> closure) {
            EnumTypeExtension o = new EnumTypeExtension();
            call(closure, o);
            add(o);
        }

        public void enumType(Action<EnumTypeExtension> action) {
            EnumTypeExtension o = new EnumTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class ForcedTypeListExtension extends ArrayList<ForcedType> {
        public void forcedType(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = ForcedTypeExtension.class) Closure<?> closure) {
            ForcedTypeExtension o = new ForcedTypeExtension();
            call(closure, o);
            add(o);
        }

        public void forcedType(Action<ForcedTypeExtension> action) {
            ForcedTypeExtension o = new ForcedTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticReadonlyColumnTypeListExtension extends ArrayList<SyntheticReadonlyColumnType> {
        public void readonlyColumn(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticReadonlyColumnTypeExtension.class) Closure<?> closure) {
            SyntheticReadonlyColumnTypeExtension o = new SyntheticReadonlyColumnTypeExtension();
            call(closure, o);
            add(o);
        }

        public void readonlyColumn(Action<SyntheticReadonlyColumnTypeExtension> action) {
            SyntheticReadonlyColumnTypeExtension o = new SyntheticReadonlyColumnTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticReadonlyRowidTypeListExtension extends ArrayList<SyntheticReadonlyRowidType> {
        public void readonlyRowid(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticReadonlyRowidTypeExtension.class) Closure<?> closure) {
            SyntheticReadonlyRowidTypeExtension o = new SyntheticReadonlyRowidTypeExtension();
            call(closure, o);
            add(o);
        }

        public void readonlyRowid(Action<SyntheticReadonlyRowidTypeExtension> action) {
            SyntheticReadonlyRowidTypeExtension o = new SyntheticReadonlyRowidTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticColumnTypeListExtension extends ArrayList<SyntheticColumnType> {
        public void column(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticColumnTypeExtension.class) Closure<?> closure) {
            SyntheticColumnTypeExtension o = new SyntheticColumnTypeExtension();
            call(closure, o);
            add(o);
        }

        public void column(Action<SyntheticColumnTypeExtension> action) {
            SyntheticColumnTypeExtension o = new SyntheticColumnTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticIdentityTypeListExtension extends ArrayList<SyntheticIdentityType> {
        public void identity(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticIdentityTypeExtension.class) Closure<?> closure) {
            SyntheticIdentityTypeExtension o = new SyntheticIdentityTypeExtension();
            call(closure, o);
            add(o);
        }

        public void identity(Action<SyntheticIdentityTypeExtension> action) {
            SyntheticIdentityTypeExtension o = new SyntheticIdentityTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticEnumTypeListExtension extends ArrayList<SyntheticEnumType> {
        public void enum_(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticEnumTypeExtension.class) Closure<?> closure) {
            SyntheticEnumTypeExtension o = new SyntheticEnumTypeExtension();
            call(closure, o);
            add(o);
        }

        public void enum_(Action<SyntheticEnumTypeExtension> action) {
            SyntheticEnumTypeExtension o = new SyntheticEnumTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticPrimaryKeyTypeListExtension extends ArrayList<SyntheticPrimaryKeyType> {
        public void primaryKey(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticPrimaryKeyTypeExtension.class) Closure<?> closure) {
            SyntheticPrimaryKeyTypeExtension o = new SyntheticPrimaryKeyTypeExtension();
            call(closure, o);
            add(o);
        }

        public void primaryKey(Action<SyntheticPrimaryKeyTypeExtension> action) {
            SyntheticPrimaryKeyTypeExtension o = new SyntheticPrimaryKeyTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticUniqueKeyTypeListExtension extends ArrayList<SyntheticUniqueKeyType> {
        public void uniqueKey(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticUniqueKeyTypeExtension.class) Closure<?> closure) {
            SyntheticUniqueKeyTypeExtension o = new SyntheticUniqueKeyTypeExtension();
            call(closure, o);
            add(o);
        }

        public void uniqueKey(Action<SyntheticUniqueKeyTypeExtension> action) {
            SyntheticUniqueKeyTypeExtension o = new SyntheticUniqueKeyTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticForeignKeyTypeListExtension extends ArrayList<SyntheticForeignKeyType> {
        public void foreignKey(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticForeignKeyTypeExtension.class) Closure<?> closure) {
            SyntheticForeignKeyTypeExtension o = new SyntheticForeignKeyTypeExtension();
            call(closure, o);
            add(o);
        }

        public void foreignKey(Action<SyntheticForeignKeyTypeExtension> action) {
            SyntheticForeignKeyTypeExtension o = new SyntheticForeignKeyTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticViewTypeListExtension extends ArrayList<SyntheticViewType> {
        public void view(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticViewTypeExtension.class) Closure<?> closure) {
            SyntheticViewTypeExtension o = new SyntheticViewTypeExtension();
            call(closure, o);
            add(o);
        }

        public void view(Action<SyntheticViewTypeExtension> action) {
            SyntheticViewTypeExtension o = new SyntheticViewTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticDaoTypeListExtension extends ArrayList<SyntheticDaoType> {
        public void dao(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticDaoTypeExtension.class) Closure<?> closure) {
            SyntheticDaoTypeExtension o = new SyntheticDaoTypeExtension();
            call(closure, o);
            add(o);
        }

        public void dao(Action<SyntheticDaoTypeExtension> action) {
            SyntheticDaoTypeExtension o = new SyntheticDaoTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticDaoMethodTypeListExtension extends ArrayList<SyntheticDaoMethodType> {
        public void method(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticDaoMethodTypeExtension.class) Closure<?> closure) {
            SyntheticDaoMethodTypeExtension o = new SyntheticDaoMethodTypeExtension();
            call(closure, o);
            add(o);
        }

        public void method(Action<SyntheticDaoMethodTypeExtension> action) {
            SyntheticDaoMethodTypeExtension o = new SyntheticDaoMethodTypeExtension();
            action.execute(o);
            add(o);
        }
    }

    public static class EmbeddableFieldListExtension extends ArrayList<EmbeddableField> {
        public void field(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = EmbeddableFieldExtension.class) Closure<?> closure) {
            EmbeddableFieldExtension o = new EmbeddableFieldExtension();
            call(closure, o);
            add(o);
        }

        public void field(Action<EmbeddableFieldExtension> action) {
            EmbeddableFieldExtension o = new EmbeddableFieldExtension();
            action.execute(o);
            add(o);
        }
    }


}