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

import java.io.Serializable;
import java.util.Locale;

import org.jooq.Condition;
import org.jooq.Constants;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Path;
// ...
import org.jooq.Spatial;
import org.jooq.Table;
import org.jooq.XML;
import org.jooq.impl.DAOImpl;
import org.jooq.meta.Database;
import org.jooq.meta.Definition;
import org.jooq.meta.ForeignKeyDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.jaxb.GeneratedAnnotationType;
import org.jooq.meta.jaxb.GeneratedSerialVersionUID;
import org.jooq.meta.jaxb.GeneratedTextBlocks;
import org.jooq.meta.jaxb.VisibilityModifier;
import org.jooq.types.Interval;

/**
 * The Generator provides a basic interface for java code generation
 *
 * @author Lukas Eder
 */
public interface Generator {

    /**
     * Do the code generation
     */
    void generate(Database database);

    /**
     * Set a naming strategy to this generator
     */
    void setStrategy(GeneratorStrategy strategy);

    /**
     * Get this generator's underlying naming strategy
     */
    GeneratorStrategy getStrategy();

    /**
     * Whether deprecated code should be generated
     */
    boolean generateDeprecated();

    /**
     * Whether deprecated code should be generated
     */
    void setGenerateDeprecated(boolean generateDeprecated);

    /**
     * Whether deprecation should be generated on unknown types.
     */
    boolean generateDeprecationOnUnknownTypes();

    /**
     * Whether deprecation should be generated on unknown types.
     */
    void setGenerateDeprecationOnUnknownTypes(boolean generateDeprecationOnUnknownTypes);

    /**
     * Whether indexes should be generated.
     */
    boolean generateIndexes();

    /**
     * Whether indexes should be generated.
     */
    void setGenerateIndexes(boolean generateIndexes);

    /**
     * Whether foreign key relations should be resolved
     */
    boolean generateRelations();

    /**
     * Whether foreign key relations should be resolved
     */
    void setGenerateRelations(boolean generateRelations);

    /**
     * Whether to generate UDT path expressions on tables and UDTs.
     */
    boolean generateUDTPaths();

    /**
     * Whether to generate UDT path expressions on tables and UDTs.
     */
    void setGenerateUDTPaths(boolean generateUDTPaths);

    /**
     * Whether implicit join path constructors on generated tables for outgoing
     * foreign key relationships (to-one relationships) should be generated.
     */
    boolean generateImplicitJoinPathsToOne();

    /**
     * Whether implicit join path constructors on generated tables for outgoing
     * foreign key relationships (to-one relationships) should be generated.
     */
    void setGenerateImplicitJoinPathsToOne(boolean generateImplicitJoinPathsToOne);

    /**
     * Whether implicit join path constructors on generated tables for incoming
     * foreign key relationships (to-many relationships) should be generated.
     */
    boolean generateImplicitJoinPathsToMany();

    /**
     * Whether implicit join path constructors on generated tables for incoming
     * foreign key relationships (to-many relationships) should be generated.
     */
    void setGenerateImplicitJoinPathsToMany(boolean generateImplicitJoinPathsToMany);

    /**
     * Whether to generate implicit join path table subtypes implementing
     * {@link Path} for increased JOIN convenience.
     */
    boolean generateImplicitJoinPathTableSubtypes();

    /**
     * Whether to generate implicit join path table subtypes implementing
     * {@link Path} for increased JOIN convenience.
     */
    void setGenerateImplicitJoinPathTableSubtypes(boolean generateImplicitJoinPathTableSubtypes);

    /**
     * Whether implicit join path constructors should also be generated if there
     * isn't any outgoing or incoming foreign key relationship.
     */
    boolean generateImplicitJoinPathUnusedConstructors();

    /**
     * Whether implicit join path constructors should also be generated if there
     * isn't any outgoing or incoming foreign key relationship.
     */
    void setGenerateImplicitJoinPathUnusedConstructors(boolean generateImplicitJoinPathUnusedConstructors);

    /**
     * Whether implicit join path constructors should be offered as properties
     * in Kotlin.
     */
    boolean generateImplicitJoinPathsAsKotlinProperties();

    /**
     * Whether implicit join path constructors should be offered as properties
     * in Kotlin.
     */
    void setGenerateImplicitJoinPathsAsKotlinProperties(boolean generateImplicitJoinPathsAsKotlinProperties);

    /**
     * Whether table-valued functions should be generated as tables.
     */
    boolean generateTableValuedFunctions();

    /**
     * Whether table-valued functions should be generated as tables.
     */
    void setGenerateTableValuedFunctions(boolean generateTableValuedFunctions);

    /**
     * Whether instance fields should be generated (as opposed to static fields).
     */
    boolean generateInstanceFields();

    /**
     * Whether instance fields should be generated (as opposed to static
     * fields).
     */
    void setGenerateInstanceFields(boolean generateInstanceFields);

    /**
     * Whether a {@code Generated} annotation should be generated.
     */
    boolean generateGeneratedAnnotation();

    /**
     * The {@link VisibilityModifier} that should be used in generated code.
     */
    void setGenerateVisibilityModifier(VisibilityModifier generateVisibilityModifier);

    /**
     * The {@link VisibilityModifier} that should be used in generated code.
     */
    VisibilityModifier generateVisibilityModifier();

    /**
     * Whether a {@code Generated} annotation should be generated.
     */
    void setGenerateGeneratedAnnotation(boolean generateGeneratedAnnotation);

    /**
     * Whether a {@code Generated} annotation should be generated.
     */
    GeneratedAnnotationType generateGeneratedAnnotationType();

    /**
     * Whether a {@code Generated} annotation should be generated.
     */
    void setGenerateGeneratedAnnotationType(GeneratedAnnotationType generateGeneratedAnnotationType);

    /**
     * Whether the {@code Generated} annotation should include the <code>date</code> attribute.
     */
    boolean generateGeneratedAnnotationDate();

    /**
     * Whether the {@code Generated} annotation should include the <code>date</code> attribute.
     */
    void setGenerateGeneratedAnnotationDate(boolean generateGeneratedAnnotationDate);

    /**
     * Whether the {@code Generated} annotation should include the jOOQ version.
     */
    boolean generateGeneratedAnnotationJooqVersion();

    /**
     * Whether the {@code Generated} annotation should include the jOOQ version.
     */
    void setGenerateGeneratedAnnotationJooqVersion(boolean generateGeneratedAnnotationJooqVersion);

    /**
     * Whether Nonnull annotations should be generated.
     * <p>
     * In SQL and by consequence in jOOQ, non-nullability cannot be guaranteed
     * statically. There may still be some cases (e.g. after unions, outer
     * joins, etc.) where a normally non-null value turns out to be null!
     */
    boolean generateNonnullAnnotation();

    /**
     * Whether Nonnull annotations should be generated.
     * <p>
     * In SQL and by consequence in jOOQ, non-nullability cannot be guaranteed
     * statically. There may still be some cases (e.g. after unions, outer
     * joins, etc.) where a normally non-null value turns out to be null!
     */
    void setGenerateNonnullAnnotation(boolean generateNonnullAnnotation);

    /**
     * Which type of Nonnull annotation should be generated.
     */
    String generatedNonnullAnnotationType();

    /**
     * Which type of Nonnull annotation should be generated.
     */
    void setGeneratedNonnullAnnotationType(String generatedNonnullAnnotationType);

    /**
     * Whether Nullable annotations should be generated.
     * <p>
     * Unlike {@link #generateNonnullAnnotation()}, nullability can be
     * guaranteed as in SQL, and by consequence in jOOQ, every column expression
     * can be made nullable using some SQL operation.
     */
    boolean generateNullableAnnotation();

    /**
     * Whether Nullable annotations should be generated.
     * <p>
     * Unlike {@link #generateNonnullAnnotation()}, nullability can be
     * guaranteed as in SQL, and by consequence in jOOQ, every column expression
     * can be made nullable using some SQL operation.
     */
    void setGenerateNullableAnnotation(boolean generateNullableAnnotation);

    /**
     * Whether Nullable annotations should be generated on write-only nullable
     * types (e.g. defaulted, non-null).
     * <p>
     * Unlike {@link #generateNonnullAnnotation()}, nullability can be
     * guaranteed as in SQL, and by consequence in jOOQ, every column expression
     * can be made nullable using some SQL operation.
     */
    boolean generateNullableAnnotationOnWriteOnlyNullableTypes();

    /**
     * Whether Nullable annotations should be generated on write-only nullable
     * types (e.g. defaulted, non-null).
     * <p>
     * Unlike {@link #generateNonnullAnnotation()}, nullability can be
     * guaranteed as in SQL, and by consequence in jOOQ, every column expression
     * can be made nullable using some SQL operation.
     */
    void setGenerateNullableAnnotationOnWriteOnlyNullableTypes(boolean generateNullableAnnotationOnWriteOnlyNullableTypes);

    /**
     * Which type of Nullable annotation should be generated.
     */
    String generatedNullableAnnotationType();

    /**
     * Which type of Nullable annotation should be generated.
     */
    void setGeneratedNullableAnnotationType(String generatedNullableAnnotationType);

    /**
     * Whether the <code>ConstructorProperties</code> annotation should be generated.
     */
    void setGenerateConstructorPropertiesAnnotation(boolean generateConstructorPropertiesAnnotation);

    /**
     * Whether the <code>ConstructorProperties</code> annotation should be generated.
     */
    boolean generateConstructorPropertiesAnnotation();

    /**
     * Whether the <code>ConstructorProperties</code> annotation should be generated on POJOs.
     */
    void setGenerateConstructorPropertiesAnnotationOnPojos(boolean generateConstructorPropertiesAnnotationOnPojos);

    /**
     * Whether the <code>ConstructorProperties</code> annotation should be generated on POJOs.
     */
    boolean generateConstructorPropertiesAnnotationOnPojos();

    /**
     * Whether the <code>ConstructorProperties</code> annotation should be generated on records.
     */
    void setGenerateConstructorPropertiesAnnotationOnRecords(boolean generateConstructorPropertiesAnnotationOnRecords);

    /**
     * Whether the <code>ConstructorProperties</code> annotation should be generated on records.
     */
    boolean generateConstructorPropertiesAnnotationOnRecords();


    boolean useSchemaVersionProvider();
    void setUseSchemaVersionProvider(boolean useSchemaVersionProvider);
    boolean useCatalogVersionProvider();
    void setUseCatalogVersionProvider(boolean useCatalogVersionProvider);

    /**
     * Whether Routines should be generated.
     */
    boolean generateRoutines();

    /**
     * Whether Routines should be generated.
     */
    void setGenerateRoutines(boolean generateRoutines);















    /**
     * Whether Sequences should be generated.
     */
    boolean generateSequences();

    /**
     * Whether Sequences should be generated.
     */
    void setGenerateSequences(boolean generateSequences);

    /**
     * Whether sequence flags should be generated.
     */
    boolean generateSequenceFlags();

    /**
     * Whether sequence flags should be generated.
     */
    void setGenerateSequenceFlags(boolean generateSequenceFlags);

    /**
     * Whether UDTs should be generated.
     */
    boolean generateUDTs();

    /**
     * Whether UDTs should be generated.
     */
    void setGenerateUDTs(boolean generateUDTs);

    /**
     * Whether Tables should be generated
     */
    boolean generateTables();

    /**
     * Whether Tables should be generated
     */
    void setGenerateTables(boolean generateTables);

    /**
     * Whether embeddable types should be generated
     */
    boolean generateEmbeddables();

    /**
     * Whether embeddable types should be generated
     */
    void setGenerateEmbeddables(boolean generateEmbeddables);

    /**
     * Whether TableRecords should be generated in addition to tables
     */
    boolean generateRecords();

    /**
     * Whether TableRecords should be generated in addition to tables
     */
    void setGenerateRecords(boolean generateRecords);

    /**
     * Whether TableRecords should be generated in addition to tables, which implement Record[N] types
     */
    boolean generateRecordsImplementingRecordN();

    /**
     * Whether TableRecords should be generated in addition to tables, which implement Record[N] types
     */
    void setGenerateRecordsImplementingRecordN(boolean generateRecordsImplementingRecordN);

    /**
     * Whether enums generated as Scala sealed traits by the
     * {@link ScalaGenerator}.
     *
     * @deprecated - [#10998] - 3.15.0 - This is maintained for backwards
     *             compatibility only. Do not reuse this feature, if possible
     */
    @Deprecated
    boolean generateEnumsAsScalaSealedTraits();

    /**
     * Whether enums generated as Scala sealed traits by the
     * {@link ScalaGenerator}.
     *
     * @deprecated - [#10998] - 3.15.0 - This is maintained for backwards
     *             compatibility only. Do not reuse this feature, if possible
     */
    @Deprecated
    void setGenerateEnumsAsScalaSealedTraits(boolean generateEnumsAsScalaSealedTraits);

    /**
     * Whether POJO's should be generated in addition to records
     */
    boolean generatePojos();

    /**
     * Whether POJO's should be generated in addition to records
     */
    void setGeneratePojos(boolean generatePojos);

    /**
     * Whether POJOs should be generated as Java records by the
     * {@link JavaGenerator}.
     */
    boolean generatePojosAsJavaRecordClasses();

    /**
     * Whether POJOs should be generated as Java records by the
     * {@link JavaGenerator}.
     */
    void setGeneratePojosAsJavaRecordClasses(boolean pojosAsJavaRecordClasses);

    /**
     * Whether POJOs should be generated as Scala case classes by the
     * {@link ScalaGenerator}.
     */
    boolean generatePojosAsScalaCaseClasses();

    /**
     * Whether POJOs should be generated as Scala case classes by the
     * {@link ScalaGenerator}.
     */
    void setGeneratePojosAsScalaCaseClasses(boolean pojosAsScalaCaseClasses);

    /**
     * Whether POJOs should be generated as Kotlin data classes by the
     * {@link KotlinGenerator}.
     */
    boolean generatePojosAsKotlinDataClasses();

    /**
     * Whether POJOs should be generated as Kotlin data classes by the
     * {@link KotlinGenerator}.
     */
    void setGeneratePojosAsKotlinDataClasses(boolean pojosAsKotlinDataClasses);

    /**
     * Whether immutable POJO's should be generated in addition to records
     */
    boolean generateImmutablePojos();

    /**
     * Whether immutable POJO's should be generated in addition to records
     */
    void setGenerateImmutablePojos(boolean generateImmutablePojos);

    /**
     * Whether generated POJO's should be {@link Serializable}.
     */
    boolean generateSerializablePojos();

    /**
     * Whether generated POJO's should be {@link Serializable}.
     */
    void setGenerateSerializablePojos(boolean generateSerializablePojos);

    /**
     * Whether interfaces should be generated to be implemented by records and
     * POJO's
     */
    boolean generateInterfaces();

    /**
     * Whether interfaces should be generated to be implemented by records and
     * POJO's
     */
    void setGenerateInterfaces(boolean generateInterfaces);

    /**
     * Whether immutable interfaces should be generated in addition to records
     */
    boolean generateImmutableInterfaces();

    /**
     * Whether immutable interfaces should be generated in addition to records
     */
    void setGenerateImmutableInterfaces(boolean generateImmutableInterfaces);

    /**
     * Whether generated interfaces should extend {@link Serializable}.
     */
    boolean generateSerializableInterfaces();

    /**
     * Whether generated interfaces should extend {@link Serializable}.
     */
    void setGenerateSerializableInterfaces(boolean generateSerializableInterfaces);

    /**
     * Whether DAO's should be generated in addition to pojos
     */
    boolean generateDaos();

    /**
     * Whether DAO's should be generated in addition to pojos
     */
    void setGenerateDaos(boolean generateDaos);

    /**
     * Whether generated objects should reference the runtime jOOQ version in
     * {@link Constants}, to help debug code generator / runtime version
     * mismatches.
     */
    boolean generateJooqVersionReference();

    /**
     * Whether generated objects should reference the runtime jOOQ version in
     * {@link Constants}, to help debug code generator / runtime version
     * mismatches.
     */
    void setGenerateJooqVersionReference(boolean generateJooqVersionReference);

    /**
     * Whether POJO's and records should be annotated with JPA annotations
     */
    boolean generateJPAAnnotations();

    /**
     * Whether POJO's and records should be annotated with JPA annotations
     */
    void setGenerateJPAAnnotations(boolean generateJPAAnnotations);

    /**
     * The minimum JPA version that is supported by generated code (latest version if empty).
     */
    String generateJPAVersion();

    /**
     * The minimum JPA version that is supported by generated code (latest version if empty).
     */
    void setGenerateJPAVersion(String generateJPAVersion);

    /**
     * Whether POJO's and records should be annotated with JSR-303 validation
     * annotations
     */
    boolean generateValidationAnnotations();

    /**
     * Whether POJO's and records should be annotated with JSR-303 validation
     * annotations
     */
    void setGenerateValidationAnnotations(boolean generateValidationAnnotations);

    /**
     * Whether DAOs should be annotated with useful spring annotations such as
     * <code>@Repository</code> or <code>@Autowired</code>
     */
    boolean generateSpringAnnotations();

    /**
     * Whether DAOs should be annotated with useful spring annotations such as
     * <code>@Repository</code> or <code>@Autowired</code>
     */
    void setGenerateSpringAnnotations(boolean generateSpringAnnotations);

    /**
     * Whether a Spring specific {@link DAOImpl} subclass should be generated,
     * which may contain Spring specific stuff, such as the
     * <code>@Transactional</code> annotation (if
     * {@link #generateSpringAnnotations()} is set).
     */
    boolean generateSpringDao();

    /**
     * Whether a Spring specific {@link DAOImpl} subclass should be generated,
     * which may contain Spring specific stuff, such as the
     * <code>@Transactional</code> annotation (if
     * {@link #generateSpringAnnotations()} is set).
     */
    void setGenerateSpringDao(boolean generateSpringDao);

    /**
     * Whether kotlin mutable properties should be annotated with
     * <code>set:JvmName</code> as a workaround for problems occurring when
     * kotlin produces <code>setX()</code> instead of <code>setIsX()</code>
     * setters for an <code>isX</code> property.
     */
    boolean generateKotlinSetterJvmNameAnnotationsOnIsPrefix();

    /**
     * Whether kotlin mutable properties should be annotated with
     * <code>set:JvmName</code> as a workaround for problems occurring when
     * kotlin produces <code>setX()</code> instead of <code>setIsX()</code>
     * setters for an <code>isX</code> property.
     */
    void setGenerateKotlinSetterJvmNameAnnotationsOnIsPrefix(boolean generateKotlinSetterJvmNameAnnotationsOnIsPrefix);

    /**
     * Generate non-nullable types on POJO attributes, where column is not null.
     */
    boolean generateKotlinNotNullPojoAttributes();

    /**
     * Generate non-nullable types on POJO attributes, where column is not null.
     */
    void setGenerateKotlinNotNullPojoAttributes(boolean generateKotlinNotNullPojoAttributes);

    /**
     * Generate non-nullable types on Record attributes, where column is not
     * null.
     */
    boolean generateKotlinNotNullRecordAttributes();

    /**
     * Generate non-nullable types on Record attributes, where column is not
     * null.
     */
    void setGenerateKotlinNotNullRecordAttributes(boolean generateKotlinNotNullRecordAttributes);

    /**
     * Generate non-nullable types on interface attributes, where column is not
     * null.
     */
    boolean generateKotlinNotNullInterfaceAttributes();

    /**
     * Generate non-nullable types on interface attributes, where column is not null.
     */
    void setGenerateKotlinNotNullInterfaceAttributes(boolean generateKotlinNotNullInterfaceAttributes);

    /**
     * Generate defaulted nullable POJO attributes.
     */
    boolean generateKotlinDefaultedNullablePojoAttributes();

    /**
     * Generate defaulted nullable POJO attributes.
     */
    void setGenerateKotlinDefaultedNullablePojoAttributes(boolean generateKotlinDefaultedNullablePojoAttributes);

    /**
     * Generate defaulted nullable Record attributes.
     */
    boolean generateKotlinDefaultedNullableRecordAttributes();

    /**
     * Generate defaulted nullable Record attributes.
     */
    void setGenerateKotlinDefaultedNullableRecordAttributes(boolean generateKotlinDefaultedNullableRecordAttributes);

    /**
     * The type of <code>serialVersionUID</code> that should be generated.
     */
    GeneratedSerialVersionUID generatedSerialVersionUID();

    /**
     * The type of <code>serialVersionUID</code> that should be generated.
     */
    void setGenerateGeneratedSerialVersionUID(GeneratedSerialVersionUID generatedSerialVersionUID);

    /**
     * The maximum number of members per initialiser, to prevent reaching the
     * 64kb byte code per method limit in generated code.
     */
    int maxMembersPerInitialiser();

    /**
     * The maximum number of members per initialiser, to prevent reaching the
     * 64kb byte code per method limit in generated code.
     */
    void setMaxMembersPerInitialiser(int maxMembersPerInitialiser);

    /**
     * Whether global object names should be generated
     */
    boolean generateGlobalObjectNames();

    /**
     * Whether global object names should be generated
     */
    void setGenerateGlobalObjectNames(boolean generateGlobalObjectNames);

    /**
     * Whether global object references should be generated
     */
    boolean generateGlobalObjectReferences();

    /**
     * Whether global object references should be generated
     */
    void setGenerateGlobalObjectReferences(boolean generateGlobalObjectReferences);

    /**
     * Whether global catalog references should be generated
     */
    boolean generateGlobalCatalogReferences();

    /**
     * Whether global catalog references should be generated
     */
    void setGenerateGlobalCatalogReferences(boolean globalCatalogReferences);

    /**
     * Whether global schema references should be generated
     */
    boolean generateGlobalSchemaReferences();

    /**
     * Whether global schema references should be generated
     */
    void setGenerateGlobalSchemaReferences(boolean globalSchemaReferences);

    /**
     * Whether global routine references should be generated
     */
    boolean generateGlobalRoutineReferences();

    /**
     * Whether global routine references should be generated
     */
    void setGenerateGlobalRoutineReferences(boolean globalRoutineReferences);

    /**
     * Whether global sequence references should be generated
     */
    boolean generateGlobalSequenceReferences();

    /**
     * Whether global sequence references should be generated
     */
    void setGenerateGlobalSequenceReferences(boolean globalSequenceReferences);

    /**
     * Whether global table references should be generated
     */
    boolean generateGlobalTableReferences();

    /**
     * Whether global table references should be generated
     */
    void setGenerateGlobalTableReferences(boolean globalTableReferences);

    /**
     * Whether global UDT references should be generated
     */
    boolean generateGlobalUDTReferences();

    /**
     * Whether global UDT references should be generated
     */
    void setGenerateGlobalUDTReferences(boolean globalUDTReferences);

    /**
     * Whether global domain references should be generated
     */
    boolean generateGlobalDomainReferences();

    /**
     * Whether global domain references should be generated
     */
    void setGenerateGlobalDomainReferences(boolean globalDomainReferences);

















    /**
     * Whether global queue references should be generated
     */
    boolean generateGlobalQueueReferences();

    /**
     * Whether global queue references should be generated
     */
    void setGenerateGlobalQueueReferences(boolean globalQueueReferences);

    /**
     * Whether global link references should be generated
     */
    boolean generateGlobalLinkReferences();

    /**
     * Whether global link references should be generated
     */
    void setGenerateGlobalLinkReferences(boolean globalLinkReferences);

    /**
     * Whether global key references should be generated
     */
    boolean generateGlobalKeyReferences();

    /**
     * Whether global key references should be generated
     */
    void setGenerateGlobalKeyReferences(boolean globalKeyReferences);

    /**
     * Whether global index references should be generated
     */
    boolean generateGlobalIndexReferences();

    /**
     * Whether global index references should be generated
     */
    void setGenerateGlobalIndexReferences(boolean globalIndexReferences);

    /**
     * Whether default catalog instances should be generated
     */
    boolean generateDefaultCatalog();

    /**
     * Whether default catalog instances should be generated
     */
    void setGenerateDefaultCatalog(boolean defaultCatalog);

    /**
     * Whether default schema instances should be generated
     */
    boolean generateDefaultSchema();

    /**
     * Whether default schema instances should be generated
     */
    void setGenerateDefaultSchema(boolean defaultSchema);

    /**
     * Whether any Javadoc should be generated.
     */
    boolean generateJavadoc();

    /**
     * Whether any Javadoc should be generated.
     */
    void setGenerateJavadoc(boolean javadoc);

    /**
     * Whether any SQL comments should be generated as Javadoc.
     */
    boolean generateComments();

    /**
     * Whether any SQL comments should be generated as Javadoc.
     */
    void setGenerateComments(boolean comments);

    /**
     * Whether SQL comments on attributes should be generated as Javadoc.
     */
    boolean generateCommentsOnAttributes();

    /**
     * Whether SQL comments on attributes should be generated as Javadoc.
     */
    void setGenerateCommentsOnAttributes(boolean commentsOnAttributes);

    /**
     * Whether SQL comments on catalogs should be generated as Javadoc.
     */
    boolean generateCommentsOnCatalogs();

    /**
     * Whether SQL comments on catalogs should be generated as Javadoc.
     */
    void setGenerateCommentsOnCatalogs(boolean commentsOnCatalogs);

    /**
     * Whether SQL comments on columns should be generated as Javadoc.
     */
    boolean generateCommentsOnColumns();

    /**
     * Whether SQL comments on columns should be generated as Javadoc.
     */
    void setGenerateCommentsOnColumns(boolean commentsOnColumns);

    /**
     * Whether SQL comments on keys should be generated as Javadoc.
     */
    boolean generateCommentsOnKeys();

    /**
     * Whether SQL comments on keys should be generated as Javadoc.
     */
    void setGenerateCommentsOnKeys(boolean commentsOnKeys);

    /**
     * Whether SQL comments on links should be generated as Javadoc.
     */
    boolean generateCommentsOnLinks();

    /**
     * Whether SQL comments on links should be generated as Javadoc.
     */
    void setGenerateCommentsOnLinks(boolean commentsOnLinks);

    /**
     * Whether SQL comments on packages should be generated as Javadoc.
     */
    boolean generateCommentsOnPackages();

    /**
     * Whether SQL comments on packages should be generated as Javadoc.
     */
    void setGenerateCommentsOnPackages(boolean commentsOnPackages);

    /**
     * Whether SQL comments on parameters should be generated as Javadoc.
     */
    boolean generateCommentsOnParameters();

    /**
     * Whether SQL comments on parameters should be generated as Javadoc.
     */
    void setGenerateCommentsOnParameters(boolean commentsOnParameters);

    /**
     * Whether SQL comments on queues should be generated as Javadoc.
     */
    boolean generateCommentsOnQueues();

    /**
     * Whether SQL comments on queues should be generated as Javadoc.
     */
    void setGenerateCommentsOnQueues(boolean commentsOnQueues);

    /**
     * Whether SQL comments on routines should be generated as Javadoc.
     */
    boolean generateCommentsOnRoutines();

    /**
     * Whether SQL comments on routines should be generated as Javadoc.
     */
    void setGenerateCommentsOnRoutines(boolean commentsOnRoutines);

    /**
     * Whether SQL comments on schemas should be generated as Javadoc.
     */
    boolean generateCommentsOnSchemas();

    /**
     * Whether SQL comments on schemas should be generated as Javadoc.
     */
    void setGenerateCommentsOnSchemas(boolean commentsOnSchemas);

    /**
     * Whether SQL comments on sequences should be generated as Javadoc.
     */
    boolean generateCommentsOnSequences();

    /**
     * Whether SQL comments on sequences should be generated as Javadoc.
     */
    void setGenerateCommentsOnSequences(boolean commentsOnSequences);

    /**
     * Whether SQL comments on domains should be generated as Javadoc.
     */
    boolean generateCommentsOnDomains();

    /**
     * Whether SQL comments on domains should be generated as Javadoc.
     */
    void setGenerateCommentsOnDomains(boolean commentsOnDomains);

    /**
     * Whether SQL comments on tables should be generated as Javadoc.
     */
    boolean generateCommentsOnTables();

    /**
     * Whether SQL comments on tables should be generated as Javadoc.
     */
    void setGenerateCommentsOnTables(boolean commentsOnTables);

    /**
     * Whether SQL comments on embeddables should be generated as Javadoc.
     */
    boolean generateCommentsOnEmbeddables();

    /**
     * Whether SQL comments on embeddables should be generated as Javadoc.
     */
    void setGenerateCommentsOnEmbeddables(boolean commentsOnEmbeddables);

    /**
     * Whether SQL comments on UDTs should be generated as Javadoc.
     */
    boolean generateCommentsOnUDTs();

    /**
     * Whether SQL comments on UDTs should be generated as Javadoc.
     */
    void setGenerateCommentsOnUDTs(boolean commentsOnUDTs);

    /**
     * Whether source code should be generated on any object type.
     */
    boolean generateSources();

    /**
     * Whether source code should be generated on any object type.
     */
    void setGenerateSources(boolean sourcesOnViews);

    /**
     * Whether source code should be generated on views.
     */
    boolean generateSourcesOnViews();

    /**
     * Whether source code should be generated on views.
     */
    void setGenerateSourcesOnViews(boolean sourcesOnViews);

    /**
     * Whether queue related code should be generated
     */
    boolean generateQueues();

    /**
     * Whether queue related code should be generated
     */
    void setGenerateQueues(boolean queues);

    /**
     * Whether link related code should be generated
     */
    boolean generateLinks();

    /**
     * Whether link related code should be generated
     */
    void setGenerateLinks(boolean links);

    /**
     * Whether key related code should be generated
     */
    boolean generateKeys();

    /**
     * Whether key related code should be generated
     */
    void setGenerateKeys(boolean keys);

    /**
     * Whether fluent setters should be generated
     *
     * @deprecated - Use {@link #generateFluentSetters()} instead.
     */
    @Deprecated
    boolean fluentSetters();

    /**
     * Whether fluent setters should be generated
     *
     * @deprecated - Use {@link #setGenerateFluentSetters(boolean)} instead.
     */
    @Deprecated
    void setFluentSetters(boolean fluentSetters);

    /**
     * Whether fluent setters should be generated
     */
    boolean generateFluentSetters();

    /**
     * Whether fluent setters should be generated
     */
    void setGenerateFluentSetters(boolean fluentSetters);

    /**
     * Whether getters and setters should be generated JavaBeans style (or jOOQ
     * style).
     */
    boolean generateJavaBeansGettersAndSetters();

    /**
     * Whether getters and setters should be generated JavaBeans style (or jOOQ
     * style).
     */
    void setGenerateJavaBeansGettersAndSetters(boolean javaBeansGettersAndSetters);

    /**
     * Whether names of unambiguous {@link ForeignKeyDefinition} should be based
     * on the referenced {@link TableDefinition}.
     * <p>
     * When a child table has only one {@link ForeignKeyDefinition} towards a
     * parent table, then that path is "unambiguous." In that case, some
     * {@link GeneratorStrategy} implementations may choose to use the parent
     * table's {@link TableDefinition} for implementations of
     * {@link GeneratorStrategy#getJavaMethodName(Definition)}, instead of the
     * {@link ForeignKeyDefinition}, e.g. for implicit join paths.
     * <p>
     * This flag allows for turning off this default behaviour.
     */
    boolean generateUseTableNameForUnambiguousFKs();

    /**
     * Whether names of unambiguous {@link ForeignKeyDefinition} should be based
     * on the referenced {@link TableDefinition}.
     * <p>
     * When a child table has only one {@link ForeignKeyDefinition} towards a
     * parent table, then that path is "unambiguous." In that case, some
     * {@link GeneratorStrategy} implementations may choose to use the parent
     * table's {@link TableDefinition} for implementations of
     * {@link GeneratorStrategy#getJavaMethodName(Definition)}, instead of the
     * {@link ForeignKeyDefinition}, e.g. for implicit join paths.
     * <p>
     * This flag allows for turning off this default behaviour.
     */
    void setGenerateUseTableNameForUnambiguousFKs(boolean useTableNameForUnambiguousFKs);

    /**
     * Whether varargs setters should be generated for array types.
     */
    boolean generateVarargsSetters();

    /**
     * Whether varargs setters should be generated for array types.
     */
    void setGenerateVarargsSetters(boolean varargsSetters);

    /**
     * Whether <code>equals()</code> and <code>hashCode()</code> methods should
     * be generated on POJOs
     */
    boolean generatePojosEqualsAndHashCode();

    /**
     * Whether <code>equals()</code> and <code>hashCode()</code> methods should
     * be generated on POJOs
     */
    void setGeneratePojosEqualsAndHashCode(boolean generatePojosEqualsAndHashCode);

    /**
     * Whether a <code>toString()</code> method should be generated on POJOs
     */
    boolean generatePojosToString();

    /**
     * Whether a <code>toString()</code> method should be generated on POJOs
     */
    void setGeneratePojosToString(boolean generatePojosToString);

    /**
     * A regular expression matching all the types in generated code that should
     * be fully qualified.
     *
     * @deprecated - Use {@link #generateFullyQualifiedTypes()} instead.
     */
    @Deprecated
    String fullyQualifiedTypes();

    /**
     * A regular expression matching all the types in generated code that should
     * be fully qualified.
     *
     * @deprecated - Use {@link #setGenerateFullyQualifiedTypes(String)}
     *             instead.
     */
    @Deprecated
    void setFullyQualifiedTypes(String fullyQualifiedTypes);

    /**
     * A regular expression matching all the types in generated code that should
     * be fully qualified.
     */
    String generateFullyQualifiedTypes();

    /**
     * A regular expression matching all the types in generated code that should
     * be fully qualified.
     */
    void setGenerateFullyQualifiedTypes(String generateFullyQualifiedTypes);

    /**
     * A flag indicating whether Java 8's java.time types should be used by the
     * source code generator, rather than JDBC's java.sql types.
     * <p>
     * This flag is ignored in the commercial Java 6 distribution of jOOQ 3.9+
     */
    boolean generateJavaTimeTypes();

    /**
     * A flag indicating whether Java 8's java.time types should be used by the
     * source code generator, rather than JDBC's java.sql types.
     * <p>
     * This flag is ignored in the commercial Java 6 distribution of jOOQ 3.9+
     */
    void setGenerateJavaTimeTypes(boolean generateJavaTimeTypes);

    /**
     * A flag indicating whether the {@link Spatial} type support should be
     * enabled.
     */
    boolean generateSpatialTypes();

    /**
     * A flag indicating whether the {@link Spatial} type support should be
     * enabled.
     */
    void setGenerateSpatialTypes(boolean generateSpatialTypes);

    /**
     * A flag indicating whether the {@link XML} type support should be enabled.
     */
    boolean generateXmlTypes();

    /**
     * A flag indicating whether the {@link XML} type support should be enabled.
     */
    void setGenerateXmlTypes(boolean generateXmlTypes);

    /**
     * A flag indicating whether the {@link JSON} and {@link JSONB} type support
     * should be enabled.
     */
    boolean generateJsonTypes();

    /**
     * A flag indicating whether the {@link JSON} and {@link JSONB} type support
     * should be enabled.
     */
    void setGenerateJsonTypes(boolean generateJsonTypes);

    /**
     * A flag indicating whether the {@link Interval} type support should be
     * enabled.
     */
    boolean generateIntervalTypes();

    /**
     * A flag indicating whether the {@link Interval} type support should be
     * enabled.
     */
    void setGenerateIntervalTypes(boolean generateJsonTypes);

    /**
     * Whether empty catalogs should still be generated.
     */
    boolean generateEmptyCatalogs();

    /**
     * Whether empty catalogs should still be generated.
     */
    void setGenerateEmptyCatalogs(boolean generateEmptyCatalogs);

    /**
     * Whether empty schemas should still be generated.
     */
    boolean generateEmptySchemas();

    /**
     * Whether empty schemas should still be generated.
     */
    void setGenerateEmptySchemas(boolean generateEmptySchemas);

    /**
     * The newline character(s) to be used in generated code.
     */
    String generateNewline();

    /**
     * The newline character(s) to be used in generated code.
     */
    void setGenerateNewline(String newline);

    /**
     * The indentation character(s) to be used in generated code.
     */
    String generateIndentation();

    /**
     * The indentation character(s) to be used in generated code.
     */
    void setGenerateIndentation(String indentation);

    /**
     * The print margin for Javadoc and other block comments to be used in generated code.
     */
    int generatePrintMarginForBlockComment();

    /**
     * The print margin for Javadoc and other block comments to be used in generated code.
     */
    void setGeneratePrintMarginForBlockComment(int printMarginForBlockComment);

    /**
     * Whether to generate String in text block format.
     */
    GeneratedTextBlocks generateTextBlocks();

    /**
     * Whether to generate String in text block format.
     */
    void setGenerateTextBlocks(GeneratedTextBlocks textBlocks);

    /**
     * Whether to generate overrides for {@link Table#where(Condition)} and
     * related overloads.
     */
    boolean generateWhereMethodOverrides();

    /**
     * Whether to generate overrides for {@link Table#where(Condition)} and
     * related overloads.
     */
    void setGenerateWhereMethodOverrides(boolean whereMethodOverrides);

    /**
     * Whether to generate overrides for {@link Table#rename(Name)} and related
     * overloads (see <a href=
     * "https://github.com/jOOQ/jOOQ/issues/13937">https://github.com/jOOQ/jOOQ/issues/13937</a>).
     */
    boolean generateRenameMethodOverrides();

    /**
     * Whether to generate overrides for {@link Table#rename(Name)} and related
     * overloads (see <a href=
     * "https://github.com/jOOQ/jOOQ/issues/13937">https://github.com/jOOQ/jOOQ/issues/13937</a>).
     */
    void setGenerateRenameMethodOverrides(boolean renameMethodOverrides);

    /**
     * Whether to generate overrides for {@link Table#as(Name)} and related
     * overloads.
     */
    boolean generateAsMethodOverrides();

    /**
     * Whether to generate overrides for {@link Table#as(Name)} and related
     * overloads.
     */
    void setGenerateAsMethodOverrides(boolean asMethodOverrides);

    /**
     * The target directory
     */
    String getTargetDirectory();

    /**
     * Initialise the target directory
     */
    void setTargetDirectory(String directory);

    /**
     * The target encoding
     */
    String getTargetEncoding();

    /**
     * Initialise the target encoding
     */
    void setTargetEncoding(String encoding);

    /**
     * @return Get the target package for the current configuration
     */
    String getTargetPackage();

    /**
     * Initialise the target package name
     */
    void setTargetPackage(String packageName);

    /**
     * Whether the target package should be cleaned to contain only generated code after a generation run.
     */
    boolean getTargetClean();

    /**
     * Whether the target package should be cleaned to contain only generated code after a generation run.
     */
    void setTargetClean(boolean clean);

    /**
     * The target locale.
     */
    Locale getTargetLocale();

    /**
     *The target locale.
     */
    void setTargetLocale(Locale targetLocale);
}
