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
import org.jooq.meta.jaxb.*;

/**
 * Extensions for the jOOQ-meta types, to enable groovy DSL usage.
 */
public class MetaExtensions {


    public static class ConfigurationExtension extends Configuration {
        public void jdbc(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = JdbcExtension.class) Closure<?> closure) {
            JdbcExtension o = new JdbcExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setJdbc(o);
        }
        public void generator(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = GeneratorExtension.class) Closure<?> closure) {
            GeneratorExtension o = new GeneratorExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setGenerator(o);
        }
    }

    public static class JdbcExtension extends Jdbc {

        public void properties(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = PropertyListExtension.class) Closure<?> closure) {
            PropertyListExtension l = new PropertyListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setProperties(l);
        }
    }

    public static class PropertyExtension extends Property {
    }

    public static class GeneratorExtension extends Generator {
        public void strategy(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = StrategyExtension.class) Closure<?> closure) {
            StrategyExtension o = new StrategyExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setStrategy(o);
        }
        public void database(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = DatabaseExtension.class) Closure<?> closure) {
            DatabaseExtension o = new DatabaseExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setDatabase(o);
        }
        public void generate(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = GenerateExtension.class) Closure<?> closure) {
            GenerateExtension o = new GenerateExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setGenerate(o);
        }
        public void target(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = TargetExtension.class) Closure<?> closure) {
            TargetExtension o = new TargetExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setTarget(o);
        }
    }

    public static class StrategyExtension extends Strategy {
        public void matchers(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersExtension.class) Closure<?> closure) {
            MatchersExtension o = new MatchersExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setMatchers(o);
        }
    }

    public static class MatchersExtension extends Matchers {

        public void catalogs(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersCatalogTypeListExtension.class) Closure<?> closure) {
            MatchersCatalogTypeListExtension l = new MatchersCatalogTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setCatalogs(l);
        }

        public void schemas(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersSchemaTypeListExtension.class) Closure<?> closure) {
            MatchersSchemaTypeListExtension l = new MatchersSchemaTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setSchemas(l);
        }

        public void tables(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersTableTypeListExtension.class) Closure<?> closure) {
            MatchersTableTypeListExtension l = new MatchersTableTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setTables(l);
        }

        public void indexes(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersIndexTypeListExtension.class) Closure<?> closure) {
            MatchersIndexTypeListExtension l = new MatchersIndexTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setIndexes(l);
        }

        public void primaryKeys(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersPrimaryKeyTypeListExtension.class) Closure<?> closure) {
            MatchersPrimaryKeyTypeListExtension l = new MatchersPrimaryKeyTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setPrimaryKeys(l);
        }

        public void uniqueKeys(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersUniqueKeyTypeListExtension.class) Closure<?> closure) {
            MatchersUniqueKeyTypeListExtension l = new MatchersUniqueKeyTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setUniqueKeys(l);
        }

        public void foreignKeys(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersForeignKeyTypeListExtension.class) Closure<?> closure) {
            MatchersForeignKeyTypeListExtension l = new MatchersForeignKeyTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setForeignKeys(l);
        }

        public void fields(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersFieldTypeListExtension.class) Closure<?> closure) {
            MatchersFieldTypeListExtension l = new MatchersFieldTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setFields(l);
        }

        public void routines(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersRoutineTypeListExtension.class) Closure<?> closure) {
            MatchersRoutineTypeListExtension l = new MatchersRoutineTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setRoutines(l);
        }

        public void sequences(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersSequenceTypeListExtension.class) Closure<?> closure) {
            MatchersSequenceTypeListExtension l = new MatchersSequenceTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setSequences(l);
        }

        public void enums(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersEnumTypeListExtension.class) Closure<?> closure) {
            MatchersEnumTypeListExtension l = new MatchersEnumTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setEnums(l);
        }

        public void embeddables(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersEmbeddableTypeListExtension.class) Closure<?> closure) {
            MatchersEmbeddableTypeListExtension l = new MatchersEmbeddableTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setEmbeddables(l);
        }

        public void udts(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersUDTTypeListExtension.class) Closure<?> closure) {
            MatchersUDTTypeListExtension l = new MatchersUDTTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setUdts(l);
        }

        public void attributes(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersAttributeTypeListExtension.class) Closure<?> closure) {
            MatchersAttributeTypeListExtension l = new MatchersAttributeTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setAttributes(l);
        }
    }

    public static class MatchersCatalogTypeExtension extends MatchersCatalogType {
        public void catalogClass(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatcherRuleExtension.class) Closure<?> closure) {
            MatcherRuleExtension o = new MatcherRuleExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
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
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setSyntheticObjects(o);
        }

        public void properties(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = PropertyListExtension.class) Closure<?> closure) {
            PropertyListExtension l = new PropertyListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setProperties(l);
        }

        public void comments(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CommentTypeListExtension.class) Closure<?> closure) {
            CommentTypeListExtension l = new CommentTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setComments(l);
        }

        public void catalogs(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CatalogMappingTypeListExtension.class) Closure<?> closure) {
            CatalogMappingTypeListExtension l = new CatalogMappingTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setCatalogs(l);
        }

        public void schemata(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SchemaMappingTypeListExtension.class) Closure<?> closure) {
            SchemaMappingTypeListExtension l = new SchemaMappingTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setSchemata(l);
        }

        public void embeddables(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = EmbeddableDefinitionTypeListExtension.class) Closure<?> closure) {
            EmbeddableDefinitionTypeListExtension l = new EmbeddableDefinitionTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setEmbeddables(l);
        }

        public void customTypes(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CustomTypeListExtension.class) Closure<?> closure) {
            CustomTypeListExtension l = new CustomTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setCustomTypes(l);
        }

        public void enumTypes(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = EnumTypeListExtension.class) Closure<?> closure) {
            EnumTypeListExtension l = new EnumTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setEnumTypes(l);
        }

        public void forcedTypes(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = ForcedTypeListExtension.class) Closure<?> closure) {
            ForcedTypeListExtension l = new ForcedTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setForcedTypes(l);
        }
    }

    public static class SyntheticObjectsTypeExtension extends SyntheticObjectsType {

        public void readonlyColumns(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticReadonlyColumnTypeListExtension.class) Closure<?> closure) {
            SyntheticReadonlyColumnTypeListExtension l = new SyntheticReadonlyColumnTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setReadonlyColumns(l);
        }

        public void readonlyRowids(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticReadonlyRowidTypeListExtension.class) Closure<?> closure) {
            SyntheticReadonlyRowidTypeListExtension l = new SyntheticReadonlyRowidTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setReadonlyRowids(l);
        }

        public void columns(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticColumnTypeListExtension.class) Closure<?> closure) {
            SyntheticColumnTypeListExtension l = new SyntheticColumnTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setColumns(l);
        }

        public void identities(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticIdentityTypeListExtension.class) Closure<?> closure) {
            SyntheticIdentityTypeListExtension l = new SyntheticIdentityTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setIdentities(l);
        }

        public void enums(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticEnumTypeListExtension.class) Closure<?> closure) {
            SyntheticEnumTypeListExtension l = new SyntheticEnumTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setEnums(l);
        }

        public void primaryKeys(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticPrimaryKeyTypeListExtension.class) Closure<?> closure) {
            SyntheticPrimaryKeyTypeListExtension l = new SyntheticPrimaryKeyTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setPrimaryKeys(l);
        }

        public void uniqueKeys(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticUniqueKeyTypeListExtension.class) Closure<?> closure) {
            SyntheticUniqueKeyTypeListExtension l = new SyntheticUniqueKeyTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setUniqueKeys(l);
        }

        public void foreignKeys(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticForeignKeyTypeListExtension.class) Closure<?> closure) {
            SyntheticForeignKeyTypeListExtension l = new SyntheticForeignKeyTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setForeignKeys(l);
        }

        public void views(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticViewTypeListExtension.class) Closure<?> closure) {
            SyntheticViewTypeListExtension l = new SyntheticViewTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setViews(l);
        }

        public void daos(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticDaoTypeListExtension.class) Closure<?> closure) {
            SyntheticDaoTypeListExtension l = new SyntheticDaoTypeListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
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
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
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
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setSchemata(l);
        }
    }

    public static class SchemaMappingTypeExtension extends SchemaMappingType {
    }

    public static class EmbeddableDefinitionTypeExtension extends EmbeddableDefinitionType {

        public void fields(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = EmbeddableFieldListExtension.class) Closure<?> closure) {
            EmbeddableFieldListExtension l = new EmbeddableFieldListExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setFields(l);
        }
    }

    public static class EmbeddableFieldExtension extends EmbeddableField {
    }

    public static class CustomTypeExtension extends CustomType {
        public void lambdaConverter(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = LambdaConverterExtension.class) Closure<?> closure) {
            LambdaConverterExtension o = new LambdaConverterExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
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
        public void property(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = PropertyExtension.class) Closure<?> c) {
            PropertyExtension o = new PropertyExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersCatalogTypeListExtension extends ArrayList<MatchersCatalogType> {
        public void catalog(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersCatalogTypeExtension.class) Closure<?> c) {
            MatchersCatalogTypeExtension o = new MatchersCatalogTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersSchemaTypeListExtension extends ArrayList<MatchersSchemaType> {
        public void schema(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersSchemaTypeExtension.class) Closure<?> c) {
            MatchersSchemaTypeExtension o = new MatchersSchemaTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersTableTypeListExtension extends ArrayList<MatchersTableType> {
        public void table(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersTableTypeExtension.class) Closure<?> c) {
            MatchersTableTypeExtension o = new MatchersTableTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersIndexTypeListExtension extends ArrayList<MatchersIndexType> {
        public void index(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersIndexTypeExtension.class) Closure<?> c) {
            MatchersIndexTypeExtension o = new MatchersIndexTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersPrimaryKeyTypeListExtension extends ArrayList<MatchersPrimaryKeyType> {
        public void primaryKey(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersPrimaryKeyTypeExtension.class) Closure<?> c) {
            MatchersPrimaryKeyTypeExtension o = new MatchersPrimaryKeyTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersUniqueKeyTypeListExtension extends ArrayList<MatchersUniqueKeyType> {
        public void uniqueKey(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersUniqueKeyTypeExtension.class) Closure<?> c) {
            MatchersUniqueKeyTypeExtension o = new MatchersUniqueKeyTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersForeignKeyTypeListExtension extends ArrayList<MatchersForeignKeyType> {
        public void foreignKey(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersForeignKeyTypeExtension.class) Closure<?> c) {
            MatchersForeignKeyTypeExtension o = new MatchersForeignKeyTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersFieldTypeListExtension extends ArrayList<MatchersFieldType> {
        public void field(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersFieldTypeExtension.class) Closure<?> c) {
            MatchersFieldTypeExtension o = new MatchersFieldTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersRoutineTypeListExtension extends ArrayList<MatchersRoutineType> {
        public void routine(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersRoutineTypeExtension.class) Closure<?> c) {
            MatchersRoutineTypeExtension o = new MatchersRoutineTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersSequenceTypeListExtension extends ArrayList<MatchersSequenceType> {
        public void sequence(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersSequenceTypeExtension.class) Closure<?> c) {
            MatchersSequenceTypeExtension o = new MatchersSequenceTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersEnumTypeListExtension extends ArrayList<MatchersEnumType> {
        public void enum_(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersEnumTypeExtension.class) Closure<?> c) {
            MatchersEnumTypeExtension o = new MatchersEnumTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersEmbeddableTypeListExtension extends ArrayList<MatchersEmbeddableType> {
        public void embeddable(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersEmbeddableTypeExtension.class) Closure<?> c) {
            MatchersEmbeddableTypeExtension o = new MatchersEmbeddableTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersUDTTypeListExtension extends ArrayList<MatchersUDTType> {
        public void udt(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersUDTTypeExtension.class) Closure<?> c) {
            MatchersUDTTypeExtension o = new MatchersUDTTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class MatchersAttributeTypeListExtension extends ArrayList<MatchersAttributeType> {
        public void attribute(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MatchersAttributeTypeExtension.class) Closure<?> c) {
            MatchersAttributeTypeExtension o = new MatchersAttributeTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class CommentTypeListExtension extends ArrayList<CommentType> {
        public void comment(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CommentTypeExtension.class) Closure<?> c) {
            CommentTypeExtension o = new CommentTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class CatalogMappingTypeListExtension extends ArrayList<CatalogMappingType> {
        public void catalog(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CatalogMappingTypeExtension.class) Closure<?> c) {
            CatalogMappingTypeExtension o = new CatalogMappingTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class SchemaMappingTypeListExtension extends ArrayList<SchemaMappingType> {
        public void schema(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SchemaMappingTypeExtension.class) Closure<?> c) {
            SchemaMappingTypeExtension o = new SchemaMappingTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class EmbeddableDefinitionTypeListExtension extends ArrayList<EmbeddableDefinitionType> {
        public void embeddable(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = EmbeddableDefinitionTypeExtension.class) Closure<?> c) {
            EmbeddableDefinitionTypeExtension o = new EmbeddableDefinitionTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class CustomTypeListExtension extends ArrayList<CustomType> {
        public void customType(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CustomTypeExtension.class) Closure<?> c) {
            CustomTypeExtension o = new CustomTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class EnumTypeListExtension extends ArrayList<EnumType> {
        public void enumType(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = EnumTypeExtension.class) Closure<?> c) {
            EnumTypeExtension o = new EnumTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class ForcedTypeListExtension extends ArrayList<ForcedType> {
        public void forcedType(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = ForcedTypeExtension.class) Closure<?> c) {
            ForcedTypeExtension o = new ForcedTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class SyntheticReadonlyColumnTypeListExtension extends ArrayList<SyntheticReadonlyColumnType> {
        public void readonlyColumn(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticReadonlyColumnTypeExtension.class) Closure<?> c) {
            SyntheticReadonlyColumnTypeExtension o = new SyntheticReadonlyColumnTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class SyntheticReadonlyRowidTypeListExtension extends ArrayList<SyntheticReadonlyRowidType> {
        public void readonlyRowid(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticReadonlyRowidTypeExtension.class) Closure<?> c) {
            SyntheticReadonlyRowidTypeExtension o = new SyntheticReadonlyRowidTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class SyntheticColumnTypeListExtension extends ArrayList<SyntheticColumnType> {
        public void column(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticColumnTypeExtension.class) Closure<?> c) {
            SyntheticColumnTypeExtension o = new SyntheticColumnTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class SyntheticIdentityTypeListExtension extends ArrayList<SyntheticIdentityType> {
        public void identity(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticIdentityTypeExtension.class) Closure<?> c) {
            SyntheticIdentityTypeExtension o = new SyntheticIdentityTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class SyntheticEnumTypeListExtension extends ArrayList<SyntheticEnumType> {
        public void enum_(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticEnumTypeExtension.class) Closure<?> c) {
            SyntheticEnumTypeExtension o = new SyntheticEnumTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class SyntheticPrimaryKeyTypeListExtension extends ArrayList<SyntheticPrimaryKeyType> {
        public void primaryKey(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticPrimaryKeyTypeExtension.class) Closure<?> c) {
            SyntheticPrimaryKeyTypeExtension o = new SyntheticPrimaryKeyTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class SyntheticUniqueKeyTypeListExtension extends ArrayList<SyntheticUniqueKeyType> {
        public void uniqueKey(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticUniqueKeyTypeExtension.class) Closure<?> c) {
            SyntheticUniqueKeyTypeExtension o = new SyntheticUniqueKeyTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class SyntheticForeignKeyTypeListExtension extends ArrayList<SyntheticForeignKeyType> {
        public void foreignKey(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticForeignKeyTypeExtension.class) Closure<?> c) {
            SyntheticForeignKeyTypeExtension o = new SyntheticForeignKeyTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class SyntheticViewTypeListExtension extends ArrayList<SyntheticViewType> {
        public void view(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticViewTypeExtension.class) Closure<?> c) {
            SyntheticViewTypeExtension o = new SyntheticViewTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class SyntheticDaoTypeListExtension extends ArrayList<SyntheticDaoType> {
        public void dao(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticDaoTypeExtension.class) Closure<?> c) {
            SyntheticDaoTypeExtension o = new SyntheticDaoTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class SyntheticDaoMethodTypeListExtension extends ArrayList<SyntheticDaoMethodType> {
        public void method(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SyntheticDaoMethodTypeExtension.class) Closure<?> c) {
            SyntheticDaoMethodTypeExtension o = new SyntheticDaoMethodTypeExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }

    public static class EmbeddableFieldListExtension extends ArrayList<EmbeddableField> {
        public void field(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = EmbeddableFieldExtension.class) Closure<?> c) {
            EmbeddableFieldExtension o = new EmbeddableFieldExtension();
            c = (Closure<?>) c.clone();
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(o);
            c.call(o);
            add(o);
        }
    }


}