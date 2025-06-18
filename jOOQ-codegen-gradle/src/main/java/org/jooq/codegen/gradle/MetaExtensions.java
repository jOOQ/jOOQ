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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
import org.gradle.api.model.ObjectFactory;
import org.jooq.meta.jaxb.*;

import javax.inject.Inject;

/**
 * Extensions for the jOOQ-meta types, to enable groovy DSL usage.
 */
public class MetaExtensions {

    static void call(Closure<?> closure, Object delegate) {
        // Explicit Closure support seems to still be needed in Gradle 8.5
        // For GeneratedClosure types (which is what the gradle/groovy implementation does),
        // it seems that the ClosureBackedAction is instantiated with Closure.OWNER_ONLY,
        // which is a weird and undesirable flag value for most DSLs.
        // See: https://github.com/jOOQ/jOOQ/issues/12985#issuecomment-1845084003
        closure = (Closure<?>) closure.clone();
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.setDelegate(delegate);

        if (closure.getMaximumNumberOfParameters() == 0)
            closure.call();
        else
            closure.call(delegate);
    }



    public static class ConfigurationExtension extends Configuration {

        final ObjectFactory objects;

        @Inject
        public ConfigurationExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void jdbc(Action<JdbcExtension> action) {
            JdbcExtension o = objects.newInstance(JdbcExtension.class, objects);
            action.execute(o);
            setJdbc(o);
        }

        public void generator(Action<GeneratorExtension> action) {
            GeneratorExtension o = objects.newInstance(GeneratorExtension.class, objects);
            action.execute(o);
            setGenerator(o);
        }
    }

    public static class JdbcExtension extends Jdbc {

        final ObjectFactory objects;

        @Inject
        public JdbcExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getAutoCommit() {
            return isAutoCommit();
        }

        public void properties(Action<PropertyListExtension> action) {
            PropertyListExtension l = objects.newInstance(PropertyListExtension.class, objects);
            action.execute(l);
            setProperties(l);
        }
    }

    public static class PropertyExtension extends Property {

        final ObjectFactory objects;

        @Inject
        public PropertyExtension(ObjectFactory objects) {
            this.objects = objects;
        }
    }

    public static class GeneratorExtension extends Generator {

        final ObjectFactory objects;

        @Inject
        public GeneratorExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void strategy(Action<StrategyExtension> action) {
            StrategyExtension o = objects.newInstance(StrategyExtension.class, objects);
            action.execute(o);
            setStrategy(o);
        }

        public void database(Action<DatabaseExtension> action) {
            DatabaseExtension o = objects.newInstance(DatabaseExtension.class, objects);
            action.execute(o);
            setDatabase(o);
        }

        public void generate(Action<GenerateExtension> action) {
            GenerateExtension o = objects.newInstance(GenerateExtension.class, objects);
            action.execute(o);
            setGenerate(o);
        }

        public void target(Action<TargetExtension> action) {
            TargetExtension o = objects.newInstance(TargetExtension.class, objects);
            action.execute(o);
            setTarget(o);
        }
    }

    public static class StrategyExtension extends Strategy {

        final ObjectFactory objects;

        @Inject
        public StrategyExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void matchers(Action<MatchersExtension> action) {
            MatchersExtension o = objects.newInstance(MatchersExtension.class, objects);
            action.execute(o);
            setMatchers(o);
        }
    }

    public static class MatchersExtension extends Matchers {

        final ObjectFactory objects;

        @Inject
        public MatchersExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void catalogs(Action<MatchersCatalogTypeListExtension> action) {
            MatchersCatalogTypeListExtension l = objects.newInstance(MatchersCatalogTypeListExtension.class, objects);
            action.execute(l);
            setCatalogs(l);
        }

        public void schemas(Action<MatchersSchemaTypeListExtension> action) {
            MatchersSchemaTypeListExtension l = objects.newInstance(MatchersSchemaTypeListExtension.class, objects);
            action.execute(l);
            setSchemas(l);
        }

        public void tables(Action<MatchersTableTypeListExtension> action) {
            MatchersTableTypeListExtension l = objects.newInstance(MatchersTableTypeListExtension.class, objects);
            action.execute(l);
            setTables(l);
        }

        public void indexes(Action<MatchersIndexTypeListExtension> action) {
            MatchersIndexTypeListExtension l = objects.newInstance(MatchersIndexTypeListExtension.class, objects);
            action.execute(l);
            setIndexes(l);
        }

        public void primaryKeys(Action<MatchersPrimaryKeyTypeListExtension> action) {
            MatchersPrimaryKeyTypeListExtension l = objects.newInstance(MatchersPrimaryKeyTypeListExtension.class, objects);
            action.execute(l);
            setPrimaryKeys(l);
        }

        public void uniqueKeys(Action<MatchersUniqueKeyTypeListExtension> action) {
            MatchersUniqueKeyTypeListExtension l = objects.newInstance(MatchersUniqueKeyTypeListExtension.class, objects);
            action.execute(l);
            setUniqueKeys(l);
        }

        public void foreignKeys(Action<MatchersForeignKeyTypeListExtension> action) {
            MatchersForeignKeyTypeListExtension l = objects.newInstance(MatchersForeignKeyTypeListExtension.class, objects);
            action.execute(l);
            setForeignKeys(l);
        }

        public void fields(Action<MatchersFieldTypeListExtension> action) {
            MatchersFieldTypeListExtension l = objects.newInstance(MatchersFieldTypeListExtension.class, objects);
            action.execute(l);
            setFields(l);
        }

        public void routines(Action<MatchersRoutineTypeListExtension> action) {
            MatchersRoutineTypeListExtension l = objects.newInstance(MatchersRoutineTypeListExtension.class, objects);
            action.execute(l);
            setRoutines(l);
        }

        public void sequences(Action<MatchersSequenceTypeListExtension> action) {
            MatchersSequenceTypeListExtension l = objects.newInstance(MatchersSequenceTypeListExtension.class, objects);
            action.execute(l);
            setSequences(l);
        }

        public void enums(Action<MatchersEnumTypeListExtension> action) {
            MatchersEnumTypeListExtension l = objects.newInstance(MatchersEnumTypeListExtension.class, objects);
            action.execute(l);
            setEnums(l);
        }

        public void embeddables(Action<MatchersEmbeddableTypeListExtension> action) {
            MatchersEmbeddableTypeListExtension l = objects.newInstance(MatchersEmbeddableTypeListExtension.class, objects);
            action.execute(l);
            setEmbeddables(l);
        }

        public void udts(Action<MatchersUDTTypeListExtension> action) {
            MatchersUDTTypeListExtension l = objects.newInstance(MatchersUDTTypeListExtension.class, objects);
            action.execute(l);
            setUdts(l);
        }

        public void attributes(Action<MatchersAttributeTypeListExtension> action) {
            MatchersAttributeTypeListExtension l = objects.newInstance(MatchersAttributeTypeListExtension.class, objects);
            action.execute(l);
            setAttributes(l);
        }
    }

    public static class MatchersCatalogTypeExtension extends MatchersCatalogType {

        final ObjectFactory objects;

        @Inject
        public MatchersCatalogTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void catalogClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setCatalogClass(o);
        }

        public void catalogIdentifier(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setCatalogIdentifier(o);
        }
    }

    public static class MatcherRuleExtension extends MatcherRule {

        final ObjectFactory objects;

        @Inject
        public MatcherRuleExtension(ObjectFactory objects) {
            this.objects = objects;
        }
    }

    public static class MatchersSchemaTypeExtension extends MatchersSchemaType {

        final ObjectFactory objects;

        @Inject
        public MatchersSchemaTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void schemaClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setSchemaClass(o);
        }

        public void schemaIdentifier(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setSchemaIdentifier(o);
        }
    }

    public static class MatchersTableTypeExtension extends MatchersTableType {

        final ObjectFactory objects;

        @Inject
        public MatchersTableTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void tableClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setTableClass(o);
        }

        public void tableIdentifier(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setTableIdentifier(o);
        }

        public void pathClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setPathClass(o);
        }

        public void pathExtends(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setPathExtends(o);
        }

        public void pathImplements(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setPathImplements(o);
        }

        public void recordClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setRecordClass(o);
        }

        public void interfaceClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setInterfaceClass(o);
        }

        public void daoClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setDaoClass(o);
        }

        public void pojoClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setPojoClass(o);
        }
    }

    public static class MatchersIndexTypeExtension extends MatchersIndexType {

        final ObjectFactory objects;

        @Inject
        public MatchersIndexTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void keyIdentifier(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setKeyIdentifier(o);
        }
    }

    public static class MatchersPrimaryKeyTypeExtension extends MatchersPrimaryKeyType {

        final ObjectFactory objects;

        @Inject
        public MatchersPrimaryKeyTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void keyIdentifier(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setKeyIdentifier(o);
        }
    }

    public static class MatchersUniqueKeyTypeExtension extends MatchersUniqueKeyType {

        final ObjectFactory objects;

        @Inject
        public MatchersUniqueKeyTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void keyIdentifier(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setKeyIdentifier(o);
        }
    }

    public static class MatchersForeignKeyTypeExtension extends MatchersForeignKeyType {

        final ObjectFactory objects;

        @Inject
        public MatchersForeignKeyTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void keyIdentifier(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setKeyIdentifier(o);
        }

        public void pathMethodName(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setPathMethodName(o);
        }

        public void pathMethodNameInverse(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setPathMethodNameInverse(o);
        }

        public void pathMethodNameManyToMany(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setPathMethodNameManyToMany(o);
        }
    }

    public static class MatchersFieldTypeExtension extends MatchersFieldType {

        final ObjectFactory objects;

        @Inject
        public MatchersFieldTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void fieldIdentifier(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setFieldIdentifier(o);
        }

        public void fieldMember(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setFieldMember(o);
        }

        public void fieldSetter(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setFieldSetter(o);
        }

        public void fieldGetter(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setFieldGetter(o);
        }

        public void daoMember(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setDaoMember(o);
        }

        public Boolean getRecordSetterOverride() {
            return isRecordSetterOverride();
        }

        public Boolean getRecordGetterOverride() {
            return isRecordGetterOverride();
        }

        public Boolean getRecordMemberOverride() {
            return isRecordMemberOverride();
        }

        public Boolean getInterfaceSetterOverride() {
            return isInterfaceSetterOverride();
        }

        public Boolean getInterfaceGetterOverride() {
            return isInterfaceGetterOverride();
        }

        public Boolean getInterfaceMemberOverride() {
            return isInterfaceMemberOverride();
        }

        public Boolean getPojoSetterOverride() {
            return isPojoSetterOverride();
        }

        public Boolean getPojoGetterOverride() {
            return isPojoGetterOverride();
        }

        public Boolean getPojoMemberOverride() {
            return isPojoMemberOverride();
        }
    }

    public static class MatchersRoutineTypeExtension extends MatchersRoutineType {

        final ObjectFactory objects;

        @Inject
        public MatchersRoutineTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void routineClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setRoutineClass(o);
        }

        public void routineMethod(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setRoutineMethod(o);
        }
    }

    public static class MatchersSequenceTypeExtension extends MatchersSequenceType {

        final ObjectFactory objects;

        @Inject
        public MatchersSequenceTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void sequenceIdentifier(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setSequenceIdentifier(o);
        }
    }

    public static class MatchersEnumTypeExtension extends MatchersEnumType {

        final ObjectFactory objects;

        @Inject
        public MatchersEnumTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void enumClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setEnumClass(o);
        }

        public void enumLiteral(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setEnumLiteral(o);
        }
    }

    public static class MatchersEmbeddableTypeExtension extends MatchersEmbeddableType {

        final ObjectFactory objects;

        @Inject
        public MatchersEmbeddableTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void recordClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setRecordClass(o);
        }

        public void interfaceClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setInterfaceClass(o);
        }

        public void pojoClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setPojoClass(o);
        }
    }

    public static class MatchersUDTTypeExtension extends MatchersUDTType {

        final ObjectFactory objects;

        @Inject
        public MatchersUDTTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void udtClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setUdtClass(o);
        }

        public void udtIdentifier(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setUdtIdentifier(o);
        }

        public void pathClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setPathClass(o);
        }

        public void recordClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setRecordClass(o);
        }

        public void recordTypeClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setRecordTypeClass(o);
        }

        public void interfaceClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setInterfaceClass(o);
        }

        public void pojoClass(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setPojoClass(o);
        }
    }

    public static class MatchersAttributeTypeExtension extends MatchersAttributeType {

        final ObjectFactory objects;

        @Inject
        public MatchersAttributeTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void attributeIdentifier(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setAttributeIdentifier(o);
        }

        public void attributeMember(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setAttributeMember(o);
        }

        public void attributeSetter(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setAttributeSetter(o);
        }

        public void attributeGetter(Action<MatcherRuleExtension> action) {
            MatcherRuleExtension o = objects.newInstance(MatcherRuleExtension.class, objects);
            action.execute(o);
            setAttributeGetter(o);
        }

        public Boolean getRecordSetterOverride() {
            return isRecordSetterOverride();
        }

        public Boolean getRecordGetterOverride() {
            return isRecordGetterOverride();
        }

        public Boolean getRecordMemberOverride() {
            return isRecordMemberOverride();
        }

        public Boolean getRecordTypeSetterOverride() {
            return isRecordTypeSetterOverride();
        }

        public Boolean getRecordTypeGetterOverride() {
            return isRecordTypeGetterOverride();
        }

        public Boolean getRecordTypeMemberOverride() {
            return isRecordTypeMemberOverride();
        }

        public Boolean getInterfaceSetterOverride() {
            return isInterfaceSetterOverride();
        }

        public Boolean getInterfaceGetterOverride() {
            return isInterfaceGetterOverride();
        }

        public Boolean getInterfaceMemberOverride() {
            return isInterfaceMemberOverride();
        }

        public Boolean getPojoSetterOverride() {
            return isPojoSetterOverride();
        }

        public Boolean getPojoGetterOverride() {
            return isPojoGetterOverride();
        }

        public Boolean getPojoMemberOverride() {
            return isPojoMemberOverride();
        }
    }

    public static class DatabaseExtension extends Database {

        final ObjectFactory objects;

        @Inject
        public DatabaseExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getRegexMatchesPartialQualification() {
            return isRegexMatchesPartialQualification();
        }

        public Boolean getSqlMatchesPartialQualification() {
            return isSqlMatchesPartialQualification();
        }

        public Boolean getIncludeExcludeColumns() {
            return isIncludeExcludeColumns();
        }

        public Boolean getIncludeExcludePackageRoutines() {
            return isIncludeExcludePackageRoutines();
        }

        public Boolean getIncludeTables() {
            return isIncludeTables();
        }

        public Boolean getIncludeEmbeddables() {
            return isIncludeEmbeddables();
        }

        public Boolean getIncludeRoutines() {
            return isIncludeRoutines();
        }

        public Boolean getIncludeTriggerRoutines() {
            return isIncludeTriggerRoutines();
        }

        public Boolean getIncludePackages() {
            return isIncludePackages();
        }

        public Boolean getIncludePackageRoutines() {
            return isIncludePackageRoutines();
        }

        public Boolean getIncludePackageUDTs() {
            return isIncludePackageUDTs();
        }

        public Boolean getIncludePackageConstants() {
            return isIncludePackageConstants();
        }

        public Boolean getIncludeXMLSchemaCollections() {
            return isIncludeXMLSchemaCollections();
        }

        public Boolean getIncludeUDTs() {
            return isIncludeUDTs();
        }

        public Boolean getIncludeDomains() {
            return isIncludeDomains();
        }

        public Boolean getIncludeTriggers() {
            return isIncludeTriggers();
        }

        public Boolean getIncludeSynonyms() {
            return isIncludeSynonyms();
        }

        public Boolean getIncludeSequences() {
            return isIncludeSequences();
        }

        public Boolean getIncludeIndexes() {
            return isIncludeIndexes();
        }

        public Boolean getIncludePrimaryKeys() {
            return isIncludePrimaryKeys();
        }

        public Boolean getIncludeUniqueKeys() {
            return isIncludeUniqueKeys();
        }

        public Boolean getIncludeForeignKeys() {
            return isIncludeForeignKeys();
        }

        public Boolean getIncludeCheckConstraints() {
            return isIncludeCheckConstraints();
        }

        public Boolean getIncludeSystemTables() {
            return isIncludeSystemTables();
        }

        public Boolean getIncludeSystemIndexes() {
            return isIncludeSystemIndexes();
        }

        public Boolean getIncludeSystemCheckConstraints() {
            return isIncludeSystemCheckConstraints();
        }

        public Boolean getIncludeSystemSequences() {
            return isIncludeSystemSequences();
        }

        public Boolean getIncludeSystemUDTs() {
            return isIncludeSystemUDTs();
        }

        public Boolean getIncludeInvisibleColumns() {
            return isIncludeInvisibleColumns();
        }

        public Boolean getInvisibleColumnsAsHidden() {
            return isInvisibleColumnsAsHidden();
        }

        public void syntheticObjects(Action<SyntheticObjectsTypeExtension> action) {
            SyntheticObjectsTypeExtension o = objects.newInstance(SyntheticObjectsTypeExtension.class, objects);
            action.execute(o);
            setSyntheticObjects(o);
        }

        public Boolean getDateAsTimestamp() {
            return isDateAsTimestamp();
        }

        public Boolean getIgnoreProcedureReturnValues() {
            return isIgnoreProcedureReturnValues();
        }

        public Boolean getUnsignedTypes() {
            return isUnsignedTypes();
        }

        public Boolean getIntegerDisplayWidths() {
            return isIntegerDisplayWidths();
        }

        public Boolean getOutputCatalogToDefault() {
            return isOutputCatalogToDefault();
        }

        public Boolean getOutputSchemaToDefault() {
            return isOutputSchemaToDefault();
        }

        public Boolean getReadonlyIdentities() {
            return isReadonlyIdentities();
        }

        public Boolean getReadonlyComputedColumns() {
            return isReadonlyComputedColumns();
        }

        public Boolean getReadonlyNonUpdatableColumns() {
            return isReadonlyNonUpdatableColumns();
        }

        public Boolean getForcedTypesForBuiltinDataTypeExtensions() {
            return isForcedTypesForBuiltinDataTypeExtensions();
        }

        public Boolean getForcedTypesForXMLSchemaCollections() {
            return isForcedTypesForXMLSchemaCollections();
        }

        public Boolean getForceIntegerTypesOnZeroScaleDecimals() {
            return isForceIntegerTypesOnZeroScaleDecimals();
        }

        public Boolean getTableValuedFunctions() {
            return isTableValuedFunctions();
        }

        public Boolean getTableValuedFunctionsAsRoutines() {
            return isTableValuedFunctionsAsRoutines();
        }

        public Boolean getTableValuedFunctionsAsTables() {
            return isTableValuedFunctionsAsTables();
        }

        public Boolean getOracleUseDBAViews() {
            return isOracleUseDBAViews();
        }

        public void properties(Action<PropertyListExtension> action) {
            PropertyListExtension l = objects.newInstance(PropertyListExtension.class, objects);
            action.execute(l);
            setProperties(l);
        }

        public void comments(Action<CommentTypeListExtension> action) {
            CommentTypeListExtension l = objects.newInstance(CommentTypeListExtension.class, objects);
            action.execute(l);
            setComments(l);
        }

        public void catalogs(Action<CatalogMappingTypeListExtension> action) {
            CatalogMappingTypeListExtension l = objects.newInstance(CatalogMappingTypeListExtension.class, objects);
            action.execute(l);
            setCatalogs(l);
        }

        public void schemata(Action<SchemaMappingTypeListExtension> action) {
            SchemaMappingTypeListExtension l = objects.newInstance(SchemaMappingTypeListExtension.class, objects);
            action.execute(l);
            setSchemata(l);
        }

        public void embeddables(Action<EmbeddableDefinitionTypeListExtension> action) {
            EmbeddableDefinitionTypeListExtension l = objects.newInstance(EmbeddableDefinitionTypeListExtension.class, objects);
            action.execute(l);
            setEmbeddables(l);
        }

        public void customTypes(Action<CustomTypeListExtension> action) {
            CustomTypeListExtension l = objects.newInstance(CustomTypeListExtension.class, objects);
            action.execute(l);
            setCustomTypes(l);
        }

        public void enumTypes(Action<EnumTypeListExtension> action) {
            EnumTypeListExtension l = objects.newInstance(EnumTypeListExtension.class, objects);
            action.execute(l);
            setEnumTypes(l);
        }

        public void forcedTypes(Action<ForcedTypeListExtension> action) {
            ForcedTypeListExtension l = objects.newInstance(ForcedTypeListExtension.class, objects);
            action.execute(l);
            setForcedTypes(l);
        }
    }

    public static class SyntheticObjectsTypeExtension extends SyntheticObjectsType {

        final ObjectFactory objects;

        @Inject
        public SyntheticObjectsTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void readonlyColumns(Action<SyntheticReadonlyColumnTypeListExtension> action) {
            SyntheticReadonlyColumnTypeListExtension l = objects.newInstance(SyntheticReadonlyColumnTypeListExtension.class, objects);
            action.execute(l);
            setReadonlyColumns(l);
        }

        public void readonlyRowids(Action<SyntheticReadonlyRowidTypeListExtension> action) {
            SyntheticReadonlyRowidTypeListExtension l = objects.newInstance(SyntheticReadonlyRowidTypeListExtension.class, objects);
            action.execute(l);
            setReadonlyRowids(l);
        }

        public void columns(Action<SyntheticColumnTypeListExtension> action) {
            SyntheticColumnTypeListExtension l = objects.newInstance(SyntheticColumnTypeListExtension.class, objects);
            action.execute(l);
            setColumns(l);
        }

        public void identities(Action<SyntheticIdentityTypeListExtension> action) {
            SyntheticIdentityTypeListExtension l = objects.newInstance(SyntheticIdentityTypeListExtension.class, objects);
            action.execute(l);
            setIdentities(l);
        }

        public void defaults(Action<SyntheticDefaultTypeListExtension> action) {
            SyntheticDefaultTypeListExtension l = objects.newInstance(SyntheticDefaultTypeListExtension.class, objects);
            action.execute(l);
            setDefaults(l);
        }

        public void enums(Action<SyntheticEnumTypeListExtension> action) {
            SyntheticEnumTypeListExtension l = objects.newInstance(SyntheticEnumTypeListExtension.class, objects);
            action.execute(l);
            setEnums(l);
        }

        public void primaryKeys(Action<SyntheticPrimaryKeyTypeListExtension> action) {
            SyntheticPrimaryKeyTypeListExtension l = objects.newInstance(SyntheticPrimaryKeyTypeListExtension.class, objects);
            action.execute(l);
            setPrimaryKeys(l);
        }

        public void uniqueKeys(Action<SyntheticUniqueKeyTypeListExtension> action) {
            SyntheticUniqueKeyTypeListExtension l = objects.newInstance(SyntheticUniqueKeyTypeListExtension.class, objects);
            action.execute(l);
            setUniqueKeys(l);
        }

        public void foreignKeys(Action<SyntheticForeignKeyTypeListExtension> action) {
            SyntheticForeignKeyTypeListExtension l = objects.newInstance(SyntheticForeignKeyTypeListExtension.class, objects);
            action.execute(l);
            setForeignKeys(l);
        }

        public void synonyms(Action<SyntheticSynonymTypeListExtension> action) {
            SyntheticSynonymTypeListExtension l = objects.newInstance(SyntheticSynonymTypeListExtension.class, objects);
            action.execute(l);
            setSynonyms(l);
        }

        public void views(Action<SyntheticViewTypeListExtension> action) {
            SyntheticViewTypeListExtension l = objects.newInstance(SyntheticViewTypeListExtension.class, objects);
            action.execute(l);
            setViews(l);
        }

        public void daos(Action<SyntheticDaoTypeListExtension> action) {
            SyntheticDaoTypeListExtension l = objects.newInstance(SyntheticDaoTypeListExtension.class, objects);
            action.execute(l);
            setDaos(l);
        }
    }

    public static class SyntheticReadonlyColumnTypeExtension extends SyntheticReadonlyColumnType {

        final ObjectFactory objects;

        @Inject
        public SyntheticReadonlyColumnTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }
    }

    public static class SyntheticReadonlyRowidTypeExtension extends SyntheticReadonlyRowidType {

        final ObjectFactory objects;

        @Inject
        public SyntheticReadonlyRowidTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }
    }

    public static class SyntheticColumnTypeExtension extends SyntheticColumnType {

        final ObjectFactory objects;

        @Inject
        public SyntheticColumnTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }
    }

    public static class SyntheticIdentityTypeExtension extends SyntheticIdentityType {

        final ObjectFactory objects;

        @Inject
        public SyntheticIdentityTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }
    }

    public static class SyntheticDefaultTypeExtension extends SyntheticDefaultType {

        final ObjectFactory objects;

        @Inject
        public SyntheticDefaultTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }
    }

    public static class SyntheticEnumTypeExtension extends SyntheticEnumType {

        final ObjectFactory objects;

        @Inject
        public SyntheticEnumTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getLiteralsFromColumnContent() {
            return isLiteralsFromColumnContent();
        }

        public Boolean getLiteralsFromCheckConstraints() {
            return isLiteralsFromCheckConstraints();
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }
    }

    public static class SyntheticPrimaryKeyTypeExtension extends SyntheticPrimaryKeyType {

        final ObjectFactory objects;

        @Inject
        public SyntheticPrimaryKeyTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }
    }

    public static class SyntheticUniqueKeyTypeExtension extends SyntheticUniqueKeyType {

        final ObjectFactory objects;

        @Inject
        public SyntheticUniqueKeyTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }
    }

    public static class SyntheticForeignKeyTypeExtension extends SyntheticForeignKeyType {

        final ObjectFactory objects;

        @Inject
        public SyntheticForeignKeyTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }
    }

    public static class SyntheticSynonymTypeExtension extends SyntheticSynonymType {

        final ObjectFactory objects;

        @Inject
        public SyntheticSynonymTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }
    }

    public static class SyntheticViewTypeExtension extends SyntheticViewType {

        final ObjectFactory objects;

        @Inject
        public SyntheticViewTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }
    }

    public static class SyntheticDaoTypeExtension extends SyntheticDaoType {

        final ObjectFactory objects;

        @Inject
        public SyntheticDaoTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void methods(Action<SyntheticDaoMethodTypeListExtension> action) {
            SyntheticDaoMethodTypeListExtension l = objects.newInstance(SyntheticDaoMethodTypeListExtension.class, objects);
            action.execute(l);
            setMethods(l);
        }
    }

    public static class SyntheticDaoMethodTypeExtension extends SyntheticDaoMethodType {

        final ObjectFactory objects;

        @Inject
        public SyntheticDaoMethodTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }
    }

    public static class CommentTypeExtension extends CommentType {

        final ObjectFactory objects;

        @Inject
        public CommentTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getDeprecated() {
            return isDeprecated();
        }

        public Boolean getIncludeSchemaComment() {
            return isIncludeSchemaComment();
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }
    }

    public static class CatalogMappingTypeExtension extends CatalogMappingType {

        final ObjectFactory objects;

        @Inject
        public CatalogMappingTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getOutputCatalogToDefault() {
            return isOutputCatalogToDefault();
        }

        public void schemata(Action<SchemaMappingTypeListExtension> action) {
            SchemaMappingTypeListExtension l = objects.newInstance(SchemaMappingTypeListExtension.class, objects);
            action.execute(l);
            setSchemata(l);
        }
    }

    public static class SchemaMappingTypeExtension extends SchemaMappingType {

        final ObjectFactory objects;

        @Inject
        public SchemaMappingTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getOutputSchemaToDefault() {
            return isOutputSchemaToDefault();
        }
    }

    public static class EmbeddableDefinitionTypeExtension extends EmbeddableDefinitionType {

        final ObjectFactory objects;

        @Inject
        public EmbeddableDefinitionTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getReplacesFields() {
            return isReplacesFields();
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }

        public void fields(Action<EmbeddableFieldListExtension> action) {
            EmbeddableFieldListExtension l = objects.newInstance(EmbeddableFieldListExtension.class, objects);
            action.execute(l);
            setFields(l);
        }
    }

    public static class EmbeddableFieldExtension extends EmbeddableField {

        final ObjectFactory objects;

        @Inject
        public EmbeddableFieldExtension(ObjectFactory objects) {
            this.objects = objects;
        }
    }

    public static class CustomTypeExtension extends CustomType {

        final ObjectFactory objects;

        @Inject
        public CustomTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getHidden() {
            return isHidden();
        }

        public Boolean getAuditInsertTimestamp() {
            return isAuditInsertTimestamp();
        }

        public Boolean getAuditInsertUser() {
            return isAuditInsertUser();
        }

        public Boolean getAuditUpdateTimestamp() {
            return isAuditUpdateTimestamp();
        }

        public Boolean getAuditUpdateUser() {
            return isAuditUpdateUser();
        }

        public Boolean getGenericConverter() {
            return isGenericConverter();
        }

        public Boolean getAutoConverter() {
            return isAutoConverter();
        }

        public Boolean getEnumConverter() {
            return isEnumConverter();
        }

        public Boolean getXmlConverter() {
            return isXmlConverter();
        }

        public Boolean getJsonConverter() {
            return isJsonConverter();
        }

        public void lambdaConverter(Action<LambdaConverterExtension> action) {
            LambdaConverterExtension o = objects.newInstance(LambdaConverterExtension.class, objects);
            action.execute(o);
            setLambdaConverter(o);
        }

        public Boolean getGenericBinding() {
            return isGenericBinding();
        }
    }

    public static class LambdaConverterExtension extends LambdaConverter {

        final ObjectFactory objects;

        @Inject
        public LambdaConverterExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getNullable() {
            return isNullable();
        }
    }

    public static class EnumTypeExtension extends EnumType {

        final ObjectFactory objects;

        @Inject
        public EnumTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }
    }

    public static class ForcedTypeExtension extends ForcedType {

        final ObjectFactory objects;

        @Inject
        public ForcedTypeExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getHidden() {
            return isHidden();
        }

        public Boolean getAuditInsertTimestamp() {
            return isAuditInsertTimestamp();
        }

        public Boolean getAuditInsertUser() {
            return isAuditInsertUser();
        }

        public Boolean getAuditUpdateTimestamp() {
            return isAuditUpdateTimestamp();
        }

        public Boolean getAuditUpdateUser() {
            return isAuditUpdateUser();
        }

        public Boolean getGenericConverter() {
            return isGenericConverter();
        }

        public Boolean getAutoConverter() {
            return isAutoConverter();
        }

        public Boolean getEnumConverter() {
            return isEnumConverter();
        }

        public Boolean getXmlConverter() {
            return isXmlConverter();
        }

        public Boolean getJsonConverter() {
            return isJsonConverter();
        }

        public void lambdaConverter(Action<LambdaConverterExtension> action) {
            LambdaConverterExtension o = objects.newInstance(LambdaConverterExtension.class, objects);
            action.execute(o);
            setLambdaConverter(o);
        }

        public Boolean getGenericBinding() {
            return isGenericBinding();
        }

        public Boolean getIgnoreUnused() {
            return isIgnoreUnused();
        }
    }

    public static class GenerateExtension extends Generate {

        final ObjectFactory objects;

        @Inject
        public GenerateExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getIndexes() {
            return isIndexes();
        }

        public Boolean getRelations() {
            return isRelations();
        }

        public Boolean getSequenceFlags() {
            return isSequenceFlags();
        }

        public Boolean getUdtPaths() {
            return isUdtPaths();
        }

        public Boolean getImplicitJoinPathsToOne() {
            return isImplicitJoinPathsToOne();
        }

        public Boolean getImplicitJoinPathsToMany() {
            return isImplicitJoinPathsToMany();
        }

        public Boolean getImplicitJoinPathsManyToMany() {
            return isImplicitJoinPathsManyToMany();
        }

        public Boolean getImplicitJoinPathTableSubtypes() {
            return isImplicitJoinPathTableSubtypes();
        }

        public Boolean getImplicitJoinPathUnusedConstructors() {
            return isImplicitJoinPathUnusedConstructors();
        }

        public Boolean getImplicitJoinPathsUseTableNameForUnambiguousFKs() {
            return isImplicitJoinPathsUseTableNameForUnambiguousFKs();
        }

        public Boolean getImplicitJoinPathsAsKotlinProperties() {
            return isImplicitJoinPathsAsKotlinProperties();
        }

        public Boolean getDeprecated() {
            return isDeprecated();
        }

        public Boolean getDeprecationOnUnknownTypes() {
            return isDeprecationOnUnknownTypes();
        }

        public Boolean getInstanceFields() {
            return isInstanceFields();
        }

        public Boolean getGeneratedAnnotation() {
            return isGeneratedAnnotation();
        }

        public Boolean getGeneratedAnnotationDate() {
            return isGeneratedAnnotationDate();
        }

        public Boolean getGeneratedAnnotationJooqVersion() {
            return isGeneratedAnnotationJooqVersion();
        }

        public Boolean getNonnullAnnotation() {
            return isNonnullAnnotation();
        }

        public Boolean getNullableAnnotation() {
            return isNullableAnnotation();
        }

        public Boolean getNullableAnnotationOnWriteOnlyNullableTypes() {
            return isNullableAnnotationOnWriteOnlyNullableTypes();
        }

        public Boolean getConstructorPropertiesAnnotation() {
            return isConstructorPropertiesAnnotation();
        }

        public Boolean getConstructorPropertiesAnnotationOnPojos() {
            return isConstructorPropertiesAnnotationOnPojos();
        }

        public Boolean getConstructorPropertiesAnnotationOnRecords() {
            return isConstructorPropertiesAnnotationOnRecords();
        }

        public Boolean getRoutines() {
            return isRoutines();
        }

        public Boolean getSequences() {
            return isSequences();
        }

        public Boolean getTriggers() {
            return isTriggers();
        }

        public Boolean getSynonyms() {
            return isSynonyms();
        }

        public Boolean getUdts() {
            return isUdts();
        }

        public Boolean getQueues() {
            return isQueues();
        }

        public Boolean getLinks() {
            return isLinks();
        }

        public Boolean getKeys() {
            return isKeys();
        }

        public Boolean getTables() {
            return isTables();
        }

        public Boolean getEmbeddables() {
            return isEmbeddables();
        }

        public Boolean getRecords() {
            return isRecords();
        }

        public Boolean getRecordsImplementingRecordN() {
            return isRecordsImplementingRecordN();
        }

        public Boolean getEnumsAsScalaSealedTraits() {
            return isEnumsAsScalaSealedTraits();
        }

        public Boolean getEnumsAsScalaEnums() {
            return isEnumsAsScalaEnums();
        }

        public Boolean getPojos() {
            return isPojos();
        }

        public Boolean getPojosEqualsAndHashCode() {
            return isPojosEqualsAndHashCode();
        }

        public Boolean getPojosEqualsAndHashCodeIncludePrimaryKeyOnly() {
            return isPojosEqualsAndHashCodeIncludePrimaryKeyOnly();
        }

        public Boolean getPojosToString() {
            return isPojosToString();
        }

        public Boolean getPojosAsJavaRecordClasses() {
            return isPojosAsJavaRecordClasses();
        }

        public Boolean getPojosAsScalaCaseClasses() {
            return isPojosAsScalaCaseClasses();
        }

        public Boolean getPojosAsKotlinDataClasses() {
            return isPojosAsKotlinDataClasses();
        }

        public Boolean getImmutablePojos() {
            return isImmutablePojos();
        }

        public Boolean getSerializablePojos() {
            return isSerializablePojos();
        }

        public Boolean getInterfaces() {
            return isInterfaces();
        }

        public Boolean getImmutableInterfaces() {
            return isImmutableInterfaces();
        }

        public Boolean getSerializableInterfaces() {
            return isSerializableInterfaces();
        }

        public Boolean getDaos() {
            return isDaos();
        }

        public Boolean getJooqVersionReference() {
            return isJooqVersionReference();
        }

        public Boolean getJpaAnnotations() {
            return isJpaAnnotations();
        }

        public Boolean getValidationAnnotations() {
            return isValidationAnnotations();
        }

        public Boolean getSpringAnnotations() {
            return isSpringAnnotations();
        }

        public Boolean getSpringDao() {
            return isSpringDao();
        }

        public Boolean getKotlinSetterJvmNameAnnotationsOnIsPrefix() {
            return isKotlinSetterJvmNameAnnotationsOnIsPrefix();
        }

        public Boolean getKotlinNotNullPojoAttributes() {
            return isKotlinNotNullPojoAttributes();
        }

        public Boolean getKotlinNotNullRecordAttributes() {
            return isKotlinNotNullRecordAttributes();
        }

        public Boolean getKotlinNotNullInterfaceAttributes() {
            return isKotlinNotNullInterfaceAttributes();
        }

        public Boolean getKotlinDefaultedNullablePojoAttributes() {
            return isKotlinDefaultedNullablePojoAttributes();
        }

        public Boolean getKotlinDefaultedNullableRecordAttributes() {
            return isKotlinDefaultedNullableRecordAttributes();
        }

        public Boolean getGlobalObjectNames() {
            return isGlobalObjectNames();
        }

        public Boolean getGlobalObjectReferences() {
            return isGlobalObjectReferences();
        }

        public Boolean getGlobalCatalogReferences() {
            return isGlobalCatalogReferences();
        }

        public Boolean getGlobalSchemaReferences() {
            return isGlobalSchemaReferences();
        }

        public Boolean getGlobalDomainReferences() {
            return isGlobalDomainReferences();
        }

        public Boolean getGlobalTriggerReferences() {
            return isGlobalTriggerReferences();
        }

        public Boolean getGlobalSynonymReferences() {
            return isGlobalSynonymReferences();
        }

        public Boolean getGlobalTableReferences() {
            return isGlobalTableReferences();
        }

        public Boolean getGlobalSequenceReferences() {
            return isGlobalSequenceReferences();
        }

        public Boolean getGlobalUDTReferences() {
            return isGlobalUDTReferences();
        }

        public Boolean getGlobalRoutineReferences() {
            return isGlobalRoutineReferences();
        }

        public Boolean getGlobalQueueReferences() {
            return isGlobalQueueReferences();
        }

        public Boolean getGlobalLinkReferences() {
            return isGlobalLinkReferences();
        }

        public Boolean getGlobalKeyReferences() {
            return isGlobalKeyReferences();
        }

        public Boolean getGlobalIndexReferences() {
            return isGlobalIndexReferences();
        }

        public Boolean getDefaultCatalog() {
            return isDefaultCatalog();
        }

        public Boolean getDefaultSchema() {
            return isDefaultSchema();
        }

        public Boolean getJavadoc() {
            return isJavadoc();
        }

        public Boolean getComments() {
            return isComments();
        }

        public Boolean getCommentsOnCatalogs() {
            return isCommentsOnCatalogs();
        }

        public Boolean getCommentsOnSchemas() {
            return isCommentsOnSchemas();
        }

        public Boolean getCommentsOnTables() {
            return isCommentsOnTables();
        }

        public Boolean getCommentsOnColumns() {
            return isCommentsOnColumns();
        }

        public Boolean getCommentsOnEmbeddables() {
            return isCommentsOnEmbeddables();
        }

        public Boolean getCommentsOnUDTs() {
            return isCommentsOnUDTs();
        }

        public Boolean getCommentsOnAttributes() {
            return isCommentsOnAttributes();
        }

        public Boolean getCommentsOnPackages() {
            return isCommentsOnPackages();
        }

        public Boolean getCommentsOnRoutines() {
            return isCommentsOnRoutines();
        }

        public Boolean getCommentsOnParameters() {
            return isCommentsOnParameters();
        }

        public Boolean getCommentsOnSequences() {
            return isCommentsOnSequences();
        }

        public Boolean getCommentsOnDomains() {
            return isCommentsOnDomains();
        }

        public Boolean getCommentsOnLinks() {
            return isCommentsOnLinks();
        }

        public Boolean getCommentsOnQueues() {
            return isCommentsOnQueues();
        }

        public Boolean getCommentsOnKeys() {
            return isCommentsOnKeys();
        }

        public Boolean getSources() {
            return isSources();
        }

        public Boolean getSourcesOnViews() {
            return isSourcesOnViews();
        }

        public Boolean getFluentSetters() {
            return isFluentSetters();
        }

        public Boolean getJavaBeansGettersAndSetters() {
            return isJavaBeansGettersAndSetters();
        }

        public Boolean getVarargSetters() {
            return isVarargSetters();
        }

        public Boolean getEmptyCatalogs() {
            return isEmptyCatalogs();
        }

        public Boolean getEmptySchemas() {
            return isEmptySchemas();
        }

        public Boolean getJavaTimeTypes() {
            return isJavaTimeTypes();
        }

        public Boolean getSpatialTypes() {
            return isSpatialTypes();
        }

        public Boolean getXmlTypes() {
            return isXmlTypes();
        }

        public Boolean getJsonTypes() {
            return isJsonTypes();
        }

        public Boolean getIntervalTypes() {
            return isIntervalTypes();
        }

        public Boolean getDecfloatTypes() {
            return isDecfloatTypes();
        }

        public Boolean getWhereMethodOverrides() {
            return isWhereMethodOverrides();
        }

        public Boolean getRenameMethodOverrides() {
            return isRenameMethodOverrides();
        }

        public Boolean getAsMethodOverrides() {
            return isAsMethodOverrides();
        }

        public Boolean getHiddenColumnsInRecords() {
            return isHiddenColumnsInRecords();
        }

        public Boolean getHiddenColumnsInPojos() {
            return isHiddenColumnsInPojos();
        }

        public Boolean getHiddenColumnsInInterfaces() {
            return isHiddenColumnsInInterfaces();
        }
    }

    public static class TargetExtension extends Target {

        final ObjectFactory objects;

        @Inject
        public TargetExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public Boolean getClean() {
            return isClean();
        }
    }

    public static class PropertyListExtension extends ArrayList<Property> {

        final ObjectFactory objects;

        @Inject
        public PropertyListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void property(Action<PropertyExtension> action) {
            PropertyExtension o = objects.newInstance(PropertyExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersCatalogTypeListExtension extends ArrayList<MatchersCatalogType> {

        final ObjectFactory objects;

        @Inject
        public MatchersCatalogTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void catalog(Action<MatchersCatalogTypeExtension> action) {
            MatchersCatalogTypeExtension o = objects.newInstance(MatchersCatalogTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersSchemaTypeListExtension extends ArrayList<MatchersSchemaType> {

        final ObjectFactory objects;

        @Inject
        public MatchersSchemaTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void schema(Action<MatchersSchemaTypeExtension> action) {
            MatchersSchemaTypeExtension o = objects.newInstance(MatchersSchemaTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersTableTypeListExtension extends ArrayList<MatchersTableType> {

        final ObjectFactory objects;

        @Inject
        public MatchersTableTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void table(Action<MatchersTableTypeExtension> action) {
            MatchersTableTypeExtension o = objects.newInstance(MatchersTableTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersIndexTypeListExtension extends ArrayList<MatchersIndexType> {

        final ObjectFactory objects;

        @Inject
        public MatchersIndexTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void index(Action<MatchersIndexTypeExtension> action) {
            MatchersIndexTypeExtension o = objects.newInstance(MatchersIndexTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersPrimaryKeyTypeListExtension extends ArrayList<MatchersPrimaryKeyType> {

        final ObjectFactory objects;

        @Inject
        public MatchersPrimaryKeyTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void primaryKey(Action<MatchersPrimaryKeyTypeExtension> action) {
            MatchersPrimaryKeyTypeExtension o = objects.newInstance(MatchersPrimaryKeyTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersUniqueKeyTypeListExtension extends ArrayList<MatchersUniqueKeyType> {

        final ObjectFactory objects;

        @Inject
        public MatchersUniqueKeyTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void uniqueKey(Action<MatchersUniqueKeyTypeExtension> action) {
            MatchersUniqueKeyTypeExtension o = objects.newInstance(MatchersUniqueKeyTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersForeignKeyTypeListExtension extends ArrayList<MatchersForeignKeyType> {

        final ObjectFactory objects;

        @Inject
        public MatchersForeignKeyTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void foreignKey(Action<MatchersForeignKeyTypeExtension> action) {
            MatchersForeignKeyTypeExtension o = objects.newInstance(MatchersForeignKeyTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersFieldTypeListExtension extends ArrayList<MatchersFieldType> {

        final ObjectFactory objects;

        @Inject
        public MatchersFieldTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void field(Action<MatchersFieldTypeExtension> action) {
            MatchersFieldTypeExtension o = objects.newInstance(MatchersFieldTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersRoutineTypeListExtension extends ArrayList<MatchersRoutineType> {

        final ObjectFactory objects;

        @Inject
        public MatchersRoutineTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void routine(Action<MatchersRoutineTypeExtension> action) {
            MatchersRoutineTypeExtension o = objects.newInstance(MatchersRoutineTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersSequenceTypeListExtension extends ArrayList<MatchersSequenceType> {

        final ObjectFactory objects;

        @Inject
        public MatchersSequenceTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void sequence(Action<MatchersSequenceTypeExtension> action) {
            MatchersSequenceTypeExtension o = objects.newInstance(MatchersSequenceTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersEnumTypeListExtension extends ArrayList<MatchersEnumType> {

        final ObjectFactory objects;

        @Inject
        public MatchersEnumTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void enum_(Action<MatchersEnumTypeExtension> action) {
            MatchersEnumTypeExtension o = objects.newInstance(MatchersEnumTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersEmbeddableTypeListExtension extends ArrayList<MatchersEmbeddableType> {

        final ObjectFactory objects;

        @Inject
        public MatchersEmbeddableTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void embeddable(Action<MatchersEmbeddableTypeExtension> action) {
            MatchersEmbeddableTypeExtension o = objects.newInstance(MatchersEmbeddableTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersUDTTypeListExtension extends ArrayList<MatchersUDTType> {

        final ObjectFactory objects;

        @Inject
        public MatchersUDTTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void udt(Action<MatchersUDTTypeExtension> action) {
            MatchersUDTTypeExtension o = objects.newInstance(MatchersUDTTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class MatchersAttributeTypeListExtension extends ArrayList<MatchersAttributeType> {

        final ObjectFactory objects;

        @Inject
        public MatchersAttributeTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void attribute(Action<MatchersAttributeTypeExtension> action) {
            MatchersAttributeTypeExtension o = objects.newInstance(MatchersAttributeTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class CommentTypeListExtension extends ArrayList<CommentType> {

        final ObjectFactory objects;

        @Inject
        public CommentTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void comment(Action<CommentTypeExtension> action) {
            CommentTypeExtension o = objects.newInstance(CommentTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class CatalogMappingTypeListExtension extends ArrayList<CatalogMappingType> {

        final ObjectFactory objects;

        @Inject
        public CatalogMappingTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void catalog(Action<CatalogMappingTypeExtension> action) {
            CatalogMappingTypeExtension o = objects.newInstance(CatalogMappingTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SchemaMappingTypeListExtension extends ArrayList<SchemaMappingType> {

        final ObjectFactory objects;

        @Inject
        public SchemaMappingTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void schema(Action<SchemaMappingTypeExtension> action) {
            SchemaMappingTypeExtension o = objects.newInstance(SchemaMappingTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class EmbeddableDefinitionTypeListExtension extends ArrayList<EmbeddableDefinitionType> {

        final ObjectFactory objects;

        @Inject
        public EmbeddableDefinitionTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void embeddable(Action<EmbeddableDefinitionTypeExtension> action) {
            EmbeddableDefinitionTypeExtension o = objects.newInstance(EmbeddableDefinitionTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class CustomTypeListExtension extends ArrayList<CustomType> {

        final ObjectFactory objects;

        @Inject
        public CustomTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void customType(Action<CustomTypeExtension> action) {
            CustomTypeExtension o = objects.newInstance(CustomTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class EnumTypeListExtension extends ArrayList<EnumType> {

        final ObjectFactory objects;

        @Inject
        public EnumTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void enumType(Action<EnumTypeExtension> action) {
            EnumTypeExtension o = objects.newInstance(EnumTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class ForcedTypeListExtension extends ArrayList<ForcedType> {

        final ObjectFactory objects;

        @Inject
        public ForcedTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void forcedType(Action<ForcedTypeExtension> action) {
            ForcedTypeExtension o = objects.newInstance(ForcedTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticReadonlyColumnTypeListExtension extends ArrayList<SyntheticReadonlyColumnType> {

        final ObjectFactory objects;

        @Inject
        public SyntheticReadonlyColumnTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void readonlyColumn(Action<SyntheticReadonlyColumnTypeExtension> action) {
            SyntheticReadonlyColumnTypeExtension o = objects.newInstance(SyntheticReadonlyColumnTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticReadonlyRowidTypeListExtension extends ArrayList<SyntheticReadonlyRowidType> {

        final ObjectFactory objects;

        @Inject
        public SyntheticReadonlyRowidTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void readonlyRowid(Action<SyntheticReadonlyRowidTypeExtension> action) {
            SyntheticReadonlyRowidTypeExtension o = objects.newInstance(SyntheticReadonlyRowidTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticColumnTypeListExtension extends ArrayList<SyntheticColumnType> {

        final ObjectFactory objects;

        @Inject
        public SyntheticColumnTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void column(Action<SyntheticColumnTypeExtension> action) {
            SyntheticColumnTypeExtension o = objects.newInstance(SyntheticColumnTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticIdentityTypeListExtension extends ArrayList<SyntheticIdentityType> {

        final ObjectFactory objects;

        @Inject
        public SyntheticIdentityTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void identity(Action<SyntheticIdentityTypeExtension> action) {
            SyntheticIdentityTypeExtension o = objects.newInstance(SyntheticIdentityTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticDefaultTypeListExtension extends ArrayList<SyntheticDefaultType> {

        final ObjectFactory objects;

        @Inject
        public SyntheticDefaultTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void default_(Action<SyntheticDefaultTypeExtension> action) {
            SyntheticDefaultTypeExtension o = objects.newInstance(SyntheticDefaultTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticEnumTypeListExtension extends ArrayList<SyntheticEnumType> {

        final ObjectFactory objects;

        @Inject
        public SyntheticEnumTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void enum_(Action<SyntheticEnumTypeExtension> action) {
            SyntheticEnumTypeExtension o = objects.newInstance(SyntheticEnumTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticPrimaryKeyTypeListExtension extends ArrayList<SyntheticPrimaryKeyType> {

        final ObjectFactory objects;

        @Inject
        public SyntheticPrimaryKeyTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void primaryKey(Action<SyntheticPrimaryKeyTypeExtension> action) {
            SyntheticPrimaryKeyTypeExtension o = objects.newInstance(SyntheticPrimaryKeyTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticUniqueKeyTypeListExtension extends ArrayList<SyntheticUniqueKeyType> {

        final ObjectFactory objects;

        @Inject
        public SyntheticUniqueKeyTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void uniqueKey(Action<SyntheticUniqueKeyTypeExtension> action) {
            SyntheticUniqueKeyTypeExtension o = objects.newInstance(SyntheticUniqueKeyTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticForeignKeyTypeListExtension extends ArrayList<SyntheticForeignKeyType> {

        final ObjectFactory objects;

        @Inject
        public SyntheticForeignKeyTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void foreignKey(Action<SyntheticForeignKeyTypeExtension> action) {
            SyntheticForeignKeyTypeExtension o = objects.newInstance(SyntheticForeignKeyTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticSynonymTypeListExtension extends ArrayList<SyntheticSynonymType> {

        final ObjectFactory objects;

        @Inject
        public SyntheticSynonymTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void synonym(Action<SyntheticSynonymTypeExtension> action) {
            SyntheticSynonymTypeExtension o = objects.newInstance(SyntheticSynonymTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticViewTypeListExtension extends ArrayList<SyntheticViewType> {

        final ObjectFactory objects;

        @Inject
        public SyntheticViewTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void view(Action<SyntheticViewTypeExtension> action) {
            SyntheticViewTypeExtension o = objects.newInstance(SyntheticViewTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticDaoTypeListExtension extends ArrayList<SyntheticDaoType> {

        final ObjectFactory objects;

        @Inject
        public SyntheticDaoTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void dao(Action<SyntheticDaoTypeExtension> action) {
            SyntheticDaoTypeExtension o = objects.newInstance(SyntheticDaoTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class SyntheticDaoMethodTypeListExtension extends ArrayList<SyntheticDaoMethodType> {

        final ObjectFactory objects;

        @Inject
        public SyntheticDaoMethodTypeListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void method(Action<SyntheticDaoMethodTypeExtension> action) {
            SyntheticDaoMethodTypeExtension o = objects.newInstance(SyntheticDaoMethodTypeExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }

    public static class EmbeddableFieldListExtension extends ArrayList<EmbeddableField> {

        final ObjectFactory objects;

        @Inject
        public EmbeddableFieldListExtension(ObjectFactory objects) {
            this.objects = objects;
        }

        public void field(Action<EmbeddableFieldExtension> action) {
            EmbeddableFieldExtension o = objects.newInstance(EmbeddableFieldExtension.class, objects);
            action.execute(o);
            add(o);
        }
    }


}