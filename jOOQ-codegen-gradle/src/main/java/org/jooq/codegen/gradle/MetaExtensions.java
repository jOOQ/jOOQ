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
        public void jdbc(Closure<?> closure) {
            JdbcExtension o = new JdbcExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setJdbc(o);
        }
        public void generator(Closure<?> closure) {
            GeneratorExtension o = new GeneratorExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setGenerator(o);
        }
    }

    public static class JdbcExtension extends Jdbc {

        public void properties(Closure<?> closure) {
            ArrayList<Property> l = new ArrayList<Property>() {
                public void property(Closure<?> c) {
                    PropertyExtension o = new PropertyExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
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
        public void strategy(Closure<?> closure) {
            StrategyExtension o = new StrategyExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setStrategy(o);
        }
        public void database(Closure<?> closure) {
            DatabaseExtension o = new DatabaseExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setDatabase(o);
        }
        public void generate(Closure<?> closure) {
            GenerateExtension o = new GenerateExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setGenerate(o);
        }
        public void target(Closure<?> closure) {
            TargetExtension o = new TargetExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setTarget(o);
        }
    }

    public static class StrategyExtension extends Strategy {
        public void matchers(Closure<?> closure) {
            MatchersExtension o = new MatchersExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setMatchers(o);
        }
    }

    public static class MatchersExtension extends Matchers {

        public void catalogs(Closure<?> closure) {
            ArrayList<MatchersCatalogType> l = new ArrayList<MatchersCatalogType>() {
                public void catalog(Closure<?> c) {
                    MatchersCatalogTypeExtension o = new MatchersCatalogTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setCatalogs(l);
        }

        public void schemas(Closure<?> closure) {
            ArrayList<MatchersSchemaType> l = new ArrayList<MatchersSchemaType>() {
                public void schema(Closure<?> c) {
                    MatchersSchemaTypeExtension o = new MatchersSchemaTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setSchemas(l);
        }

        public void tables(Closure<?> closure) {
            ArrayList<MatchersTableType> l = new ArrayList<MatchersTableType>() {
                public void table(Closure<?> c) {
                    MatchersTableTypeExtension o = new MatchersTableTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setTables(l);
        }

        public void indexes(Closure<?> closure) {
            ArrayList<MatchersIndexType> l = new ArrayList<MatchersIndexType>() {
                public void index(Closure<?> c) {
                    MatchersIndexTypeExtension o = new MatchersIndexTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setIndexes(l);
        }

        public void primaryKeys(Closure<?> closure) {
            ArrayList<MatchersPrimaryKeyType> l = new ArrayList<MatchersPrimaryKeyType>() {
                public void primaryKey(Closure<?> c) {
                    MatchersPrimaryKeyTypeExtension o = new MatchersPrimaryKeyTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setPrimaryKeys(l);
        }

        public void uniqueKeys(Closure<?> closure) {
            ArrayList<MatchersUniqueKeyType> l = new ArrayList<MatchersUniqueKeyType>() {
                public void uniqueKey(Closure<?> c) {
                    MatchersUniqueKeyTypeExtension o = new MatchersUniqueKeyTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setUniqueKeys(l);
        }

        public void foreignKeys(Closure<?> closure) {
            ArrayList<MatchersForeignKeyType> l = new ArrayList<MatchersForeignKeyType>() {
                public void foreignKey(Closure<?> c) {
                    MatchersForeignKeyTypeExtension o = new MatchersForeignKeyTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setForeignKeys(l);
        }

        public void fields(Closure<?> closure) {
            ArrayList<MatchersFieldType> l = new ArrayList<MatchersFieldType>() {
                public void field(Closure<?> c) {
                    MatchersFieldTypeExtension o = new MatchersFieldTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setFields(l);
        }

        public void routines(Closure<?> closure) {
            ArrayList<MatchersRoutineType> l = new ArrayList<MatchersRoutineType>() {
                public void routine(Closure<?> c) {
                    MatchersRoutineTypeExtension o = new MatchersRoutineTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setRoutines(l);
        }

        public void sequences(Closure<?> closure) {
            ArrayList<MatchersSequenceType> l = new ArrayList<MatchersSequenceType>() {
                public void sequence(Closure<?> c) {
                    MatchersSequenceTypeExtension o = new MatchersSequenceTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setSequences(l);
        }

        public void enums(Closure<?> closure) {
            ArrayList<MatchersEnumType> l = new ArrayList<MatchersEnumType>() {
                public void enum_(Closure<?> c) {
                    MatchersEnumTypeExtension o = new MatchersEnumTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setEnums(l);
        }

        public void embeddables(Closure<?> closure) {
            ArrayList<MatchersEmbeddableType> l = new ArrayList<MatchersEmbeddableType>() {
                public void embeddable(Closure<?> c) {
                    MatchersEmbeddableTypeExtension o = new MatchersEmbeddableTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setEmbeddables(l);
        }

        public void udts(Closure<?> closure) {
            ArrayList<MatchersUDTType> l = new ArrayList<MatchersUDTType>() {
                public void udt(Closure<?> c) {
                    MatchersUDTTypeExtension o = new MatchersUDTTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setUdts(l);
        }

        public void attributes(Closure<?> closure) {
            ArrayList<MatchersAttributeType> l = new ArrayList<MatchersAttributeType>() {
                public void attribute(Closure<?> c) {
                    MatchersAttributeTypeExtension o = new MatchersAttributeTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setAttributes(l);
        }
    }

    public static class MatchersCatalogTypeExtension extends MatchersCatalogType {
        public void catalogClass(Closure<?> closure) {
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
        public void syntheticObjects(Closure<?> closure) {
            SyntheticObjectsTypeExtension o = new SyntheticObjectsTypeExtension();
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(o);
            closure.call(o);
            setSyntheticObjects(o);
        }

        public void properties(Closure<?> closure) {
            ArrayList<Property> l = new ArrayList<Property>() {
                public void property(Closure<?> c) {
                    PropertyExtension o = new PropertyExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setProperties(l);
        }

        public void comments(Closure<?> closure) {
            ArrayList<CommentType> l = new ArrayList<CommentType>() {
                public void comment(Closure<?> c) {
                    CommentTypeExtension o = new CommentTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setComments(l);
        }

        public void catalogs(Closure<?> closure) {
            ArrayList<CatalogMappingType> l = new ArrayList<CatalogMappingType>() {
                public void catalog(Closure<?> c) {
                    CatalogMappingTypeExtension o = new CatalogMappingTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setCatalogs(l);
        }

        public void schemata(Closure<?> closure) {
            ArrayList<SchemaMappingType> l = new ArrayList<SchemaMappingType>() {
                public void schema(Closure<?> c) {
                    SchemaMappingTypeExtension o = new SchemaMappingTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setSchemata(l);
        }

        public void embeddables(Closure<?> closure) {
            ArrayList<EmbeddableDefinitionType> l = new ArrayList<EmbeddableDefinitionType>() {
                public void embeddable(Closure<?> c) {
                    EmbeddableDefinitionTypeExtension o = new EmbeddableDefinitionTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setEmbeddables(l);
        }

        public void customTypes(Closure<?> closure) {
            ArrayList<CustomType> l = new ArrayList<CustomType>() {
                public void customType(Closure<?> c) {
                    CustomTypeExtension o = new CustomTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setCustomTypes(l);
        }

        public void enumTypes(Closure<?> closure) {
            ArrayList<EnumType> l = new ArrayList<EnumType>() {
                public void enumType(Closure<?> c) {
                    EnumTypeExtension o = new EnumTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setEnumTypes(l);
        }

        public void forcedTypes(Closure<?> closure) {
            ArrayList<ForcedType> l = new ArrayList<ForcedType>() {
                public void forcedType(Closure<?> c) {
                    ForcedTypeExtension o = new ForcedTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setForcedTypes(l);
        }
    }

    public static class SyntheticObjectsTypeExtension extends SyntheticObjectsType {

        public void readonlyColumns(Closure<?> closure) {
            ArrayList<SyntheticReadonlyColumnType> l = new ArrayList<SyntheticReadonlyColumnType>() {
                public void readonlyColumn(Closure<?> c) {
                    SyntheticReadonlyColumnTypeExtension o = new SyntheticReadonlyColumnTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setReadonlyColumns(l);
        }

        public void readonlyRowids(Closure<?> closure) {
            ArrayList<SyntheticReadonlyRowidType> l = new ArrayList<SyntheticReadonlyRowidType>() {
                public void readonlyRowid(Closure<?> c) {
                    SyntheticReadonlyRowidTypeExtension o = new SyntheticReadonlyRowidTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setReadonlyRowids(l);
        }

        public void columns(Closure<?> closure) {
            ArrayList<SyntheticColumnType> l = new ArrayList<SyntheticColumnType>() {
                public void column(Closure<?> c) {
                    SyntheticColumnTypeExtension o = new SyntheticColumnTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setColumns(l);
        }

        public void identities(Closure<?> closure) {
            ArrayList<SyntheticIdentityType> l = new ArrayList<SyntheticIdentityType>() {
                public void identity(Closure<?> c) {
                    SyntheticIdentityTypeExtension o = new SyntheticIdentityTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setIdentities(l);
        }

        public void enums(Closure<?> closure) {
            ArrayList<SyntheticEnumType> l = new ArrayList<SyntheticEnumType>() {
                public void enum_(Closure<?> c) {
                    SyntheticEnumTypeExtension o = new SyntheticEnumTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setEnums(l);
        }

        public void primaryKeys(Closure<?> closure) {
            ArrayList<SyntheticPrimaryKeyType> l = new ArrayList<SyntheticPrimaryKeyType>() {
                public void primaryKey(Closure<?> c) {
                    SyntheticPrimaryKeyTypeExtension o = new SyntheticPrimaryKeyTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setPrimaryKeys(l);
        }

        public void uniqueKeys(Closure<?> closure) {
            ArrayList<SyntheticUniqueKeyType> l = new ArrayList<SyntheticUniqueKeyType>() {
                public void uniqueKey(Closure<?> c) {
                    SyntheticUniqueKeyTypeExtension o = new SyntheticUniqueKeyTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setUniqueKeys(l);
        }

        public void foreignKeys(Closure<?> closure) {
            ArrayList<SyntheticForeignKeyType> l = new ArrayList<SyntheticForeignKeyType>() {
                public void foreignKey(Closure<?> c) {
                    SyntheticForeignKeyTypeExtension o = new SyntheticForeignKeyTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setForeignKeys(l);
        }

        public void views(Closure<?> closure) {
            ArrayList<SyntheticViewType> l = new ArrayList<SyntheticViewType>() {
                public void view(Closure<?> c) {
                    SyntheticViewTypeExtension o = new SyntheticViewTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
            closure = (Closure<?>) closure.clone();
            closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            closure.setDelegate(l);
            closure.call(l);
            setViews(l);
        }

        public void daos(Closure<?> closure) {
            ArrayList<SyntheticDaoType> l = new ArrayList<SyntheticDaoType>() {
                public void dao(Closure<?> c) {
                    SyntheticDaoTypeExtension o = new SyntheticDaoTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
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

        public void methods(Closure<?> closure) {
            ArrayList<SyntheticDaoMethodType> l = new ArrayList<SyntheticDaoMethodType>() {
                public void method(Closure<?> c) {
                    SyntheticDaoMethodTypeExtension o = new SyntheticDaoMethodTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
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

        public void schemata(Closure<?> closure) {
            ArrayList<SchemaMappingType> l = new ArrayList<SchemaMappingType>() {
                public void schema(Closure<?> c) {
                    SchemaMappingTypeExtension o = new SchemaMappingTypeExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
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

        public void fields(Closure<?> closure) {
            ArrayList<EmbeddableField> l = new ArrayList<EmbeddableField>() {
                public void field(Closure<?> c) {
                    EmbeddableFieldExtension o = new EmbeddableFieldExtension();
                    c = (Closure<?>) c.clone();
                    c.setResolveStrategy(Closure.DELEGATE_FIRST);
                    c.setDelegate(o);
                    c.call(o);
                    add(o);
                }
            };
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
        public void lambdaConverter(Closure<?> closure) {
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


}