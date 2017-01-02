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

import javax.annotation.Generated;

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
     * Whether foreign key relations should be resolved
     */
    boolean generateRelations();

    /**
     * Whether foreign key relations should be resolved
     */
    void setGenerateRelations(boolean generateRelations);

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
     * Whether the {@link Generated} annotation should be generated
     */
    boolean generateGeneratedAnnotation();

    /**
     * Whether the {@link Generated} annotation should be generated
     */
    void setGenerateGeneratedAnnotation(boolean generateGeneratedAnnotation);

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
     * Whether TableRecords should be generated in addition to tables
     */
    boolean generateRecords();

    /**
     * Whether TableRecords should be generated in addition to tables
     */
    void setGenerateRecords(boolean generateRecords);

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
     * Whether fluent setters should be generated
     */
    boolean fluentSetters();

    /**
     * Whether fluent setters should be generated
     */
    void setFluentSetters(boolean fluentSetters);

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

}
