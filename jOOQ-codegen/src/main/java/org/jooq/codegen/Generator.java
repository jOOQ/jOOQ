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
 *
 *
 *
 */

package org.jooq.codegen;

import java.io.Serializable;

import org.jooq.meta.Database;
import org.jooq.meta.jaxb.GeneratedAnnotationType;

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
     * Whether table-valued functions should be generated as tables.
     */
    boolean generateTableValuedFunctions();

    /**
     * Whether table-valued functions should be generated as tables.
     */
    void setGenerateTableValuedFunctions(boolean generateTableValuedFunctions);

    /**
     * Whether instance fields should be generated (as opposed to static fields)
     */
    boolean generateInstanceFields();

    /**
     * Whether instance fields should be generated (as opposed to static fields)
     */
    void setGenerateInstanceFields(boolean generateInstanceFields);

    /**
     * Whether the {@link javax.annotation.Generated} or {@link javax.annotation.processing.Generated} annotation should be generated
     */
    boolean generateGeneratedAnnotation();

    /**
     * Whether the {@link javax.annotation.Generated} or {@link javax.annotation.processing.Generated} annotation should be generated
     */
    void setGenerateGeneratedAnnotation(boolean generateGeneratedAnnotation);

    /**
     * Whether the {@link javax.annotation.Generated} or {@link javax.annotation.processing.Generated} annotation should be generated
     */
    GeneratedAnnotationType generateGeneratedAnnotationType();

    /**
     * Whether the {@link javax.annotation.Generated} or {@link javax.annotation.processing.Generated} annotation should be generated
     */
    void setGenerateGeneratedAnnotationType(GeneratedAnnotationType generateGeneratedAnnotationType);

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
     * Which type of Nullable annotation should be generated.
     */
    String generatedNullableAnnotationType();

    /**
     * Which type of Nullable annotation should be generated.
     */
    void setGeneratedNullableAnnotationType(String generatedNullableAnnotationType);

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
     * Whether POJO's should be generated in addition to records
     */
    boolean generatePojos();

    /**
     * Whether POJO's should be generated in addition to records
     */
    void setGeneratePojos(boolean generatePojos);

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
     * Whether SQL comments on tables should be generated as Javadoc.
     */
    boolean generateCommentsOnTables();

    /**
     * Whether SQL comments on tables should be generated as Javadoc.
     */
    void setGenerateCommentsOnTables(boolean commentsOnTables);

    /**
     * Whether SQL comments on UDTs should be generated as Javadoc.
     */
    boolean generateCommentsOnUDTs();

    /**
     * Whether SQL comments on UDTs should be generated as Javadoc.
     */
    void setGenerateCommentsOnUDTs(boolean commentsOnUDTs);

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
     * Whether getters and setters should be generated JavaBeans style (or jOOQ style).
     */
    boolean generateJavaBeansGettersAndSetters();

    /**
     * Whether getters and setters should be generated JavaBeans style (or jOOQ style).
     */
    void setGenerateJavaBeansGettersAndSetters(boolean javaBeansGettersAndSetters);

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
     * Whether wrapper types for primary keys should be generated.
     */
    boolean generatePrimaryKeyTypes();

    /**
     * Whether wrapper types for primary keys should be generated.
     */
    void setGeneratePrimaryKeyTypes(boolean generatePrimaryKeyTypes);

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
}
