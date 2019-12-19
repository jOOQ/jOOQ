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

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.jooq.meta.Database;
import org.jooq.meta.jaxb.GeneratedAnnotationType;
import org.jooq.tools.JooqLogger;


/**
 * A common base implementation for {@link Generator} objects
 *
 * @author Lukas Eder
 */
abstract class AbstractGenerator implements Generator {

    private static final JooqLogger    log                                = JooqLogger.getLogger(AbstractGenerator.class);

    boolean                            generateDeprecated                 = true;
    boolean                            generateDeprecationOnUnknownTypes  = true;
    boolean                            generateIndexes                    = true;
    boolean                            generateRelations                  = true;
    boolean                            generateImplicitJoinPathsToOne     = true;
    boolean                            generateInstanceFields             = true;
    boolean                            generateGeneratedAnnotation        = true;
    GeneratedAnnotationType            generateGeneratedAnnotationType    = GeneratedAnnotationType.DETECT_FROM_JDK;
    boolean                            useSchemaVersionProvider           = false;
    boolean                            useCatalogVersionProvider          = false;
    boolean                            generateRoutines                   = true;
    boolean                            generateSequences                  = true;
    boolean                            generateSequenceFlags              = true;
    boolean                            generateUDTs                       = true;
    boolean                            generateTables                     = true;
    boolean                            generateEmbeddables                = true;
    boolean                            generateRecords                    = true;
    boolean                            generateRecordsImplementingRecordN = true;
    boolean                            generatePojos                      = false;
    boolean                            generatePojosEqualsAndHashCode     = false;
    boolean                            generatePojosToString              = true;
    boolean                            generateImmutablePojos             = false;
    boolean                            generateSerializablePojos          = true;
    boolean                            generateInterfaces                 = false;
    boolean                            generateImmutableInterfaces        = false;
    boolean                            generateSerializableInterfaces     = true;
    boolean                            generateDaos                       = false;
    boolean                            generateJPAAnnotations             = false;
    String                             generateJPAVersion                 = "";
    boolean                            generateValidationAnnotations      = false;
    boolean                            generateSpringAnnotations          = false;
    boolean                            generateQueues                     = true;
    boolean                            generateLinks                      = true;
    boolean                            generateKeys                       = true;
    boolean                            generateGlobalObjectReferences     = true;
    boolean                            generateGlobalCatalogReferences    = true;
    boolean                            generateGlobalSchemaReferences     = true;
    boolean                            generateGlobalRoutineReferences    = true;
    boolean                            generateGlobalSequenceReferences   = true;
    boolean                            generateGlobalTableReferences      = true;
    boolean                            generateGlobalUDTReferences        = true;
    boolean                            generateGlobalQueueReferences      = true;
    boolean                            generateGlobalLinkReferences       = true;
    boolean                            generateGlobalKeyReferences        = true;
    boolean                            generateGlobalIndexReferences      = true;
    boolean                            generateJavadoc                    = true;
    boolean                            generateComments                   = true;
    boolean                            generateCommentsOnAttributes       = true;
    boolean                            generateCommentsOnCatalogs         = true;
    boolean                            generateCommentsOnColumns          = true;
    boolean                            generateCommentsOnKeys             = true;
    boolean                            generateCommentsOnLinks            = true;
    boolean                            generateCommentsOnPackages         = true;
    boolean                            generateCommentsOnParameters       = true;
    boolean                            generateCommentsOnQueues           = true;
    boolean                            generateCommentsOnRoutines         = true;
    boolean                            generateCommentsOnSchemas          = true;
    boolean                            generateCommentsOnSequences        = true;
    boolean                            generateCommentsOnTables           = true;
    boolean                            generateCommentsOnUDTs             = true;
    boolean                            generateFluentSetters              = false;
    boolean                            generateJavaBeansGettersAndSetters = false;
    boolean                            generateVarargsSetters             = true;
    String                             generateFullyQualifiedTypes        = "";
    boolean                            generateJavaTimeTypes              = true;
    boolean                            generateTableValuedFunctions       = false;
    boolean                            generateEmptyCatalogs              = false;
    boolean                            generateEmptySchemas               = false;
    boolean                            generatePrimaryKeyTypes            = false;
    String                             generateNewline                    = "\n";
    String                             generateIndentation;

    protected GeneratorStrategyWrapper strategy;
    protected String                   targetEncoding                     = "UTF-8";
    protected boolean                  targetClean                        = true;
    final Language                     language;


    AbstractGenerator(Language language) {
        this.language = language;
    }

    enum Language {
        JAVA, SCALA, XML;
    }

    void logDatabaseParameters(Database db) {
        String url = "";
        try {
            Connection connection = db.getConnection();

            if (connection != null)
                url = connection.getMetaData().getURL();
        }
        catch (SQLException ignore) {}

        log.info("License parameters");
        log.info("----------------------------------------------------------");
        log.info("  Thank you for using jOOQ and jOOQ's code generator");
        log.info("");
        log.info("Database parameters");
        log.info("----------------------------------------------------------");
        log.info("  dialect", db.getDialect());
        log.info("  URL", url);
        log.info("  target dir", getTargetDirectory());
        log.info("  target package", getTargetPackage());
        log.info("  includes", Arrays.asList(db.getIncludes()));
        log.info("  excludes", Arrays.asList(db.getExcludes()));
        log.info("  includeExcludeColumns", db.getIncludeExcludeColumns());
        log.info("----------------------------------------------------------");
    }

    void logGenerationRemarks(Database db) {
        log.info("Generation remarks");
        log.info("----------------------------------------------------------");

        if (contains(db.getIncludes(), ',') && db.getIncluded().isEmpty())
            log.info("  includes", "The <includes/> element takes a Java regular expression, not a comma-separated list. This might be why no objects were included.");

        if (contains(db.getExcludes(), ',') && db.getExcluded().isEmpty())
            log.info("  excludes", "The <excludes/> element takes a Java regular expression, not a comma-separated list. This might be why no objects were excluded.");
    }

    private boolean contains(String[] array, char c) {
        if (array == null)
            return false;

        for (String string : array)
            if (string != null && string.indexOf(c) > -1)
                return true;

        return false;
    }

    @Override
    public void setStrategy(GeneratorStrategy strategy) {
        this.strategy = new GeneratorStrategyWrapper(this, strategy, language);
    }

    @Override
    public GeneratorStrategy getStrategy() {
        return strategy;
    }

    @Override
    public boolean generateDeprecated() {
        return generateDeprecated;
    }

    @Override
    public void setGenerateDeprecated(boolean generateDeprecated) {
        this.generateDeprecated = generateDeprecated;
    }

    @Override
    public boolean generateDeprecationOnUnknownTypes() {
        return generateDeprecationOnUnknownTypes;
    }

    @Override
    public void setGenerateDeprecationOnUnknownTypes(boolean generateDeprecationOnUnknownTypes) {
        this.generateDeprecationOnUnknownTypes = generateDeprecationOnUnknownTypes;
    }

    @Override
    public boolean generateIndexes() {
        return generateIndexes;
    }

    @Override
    public void setGenerateIndexes(boolean generateIndexes) {
        this.generateIndexes = generateIndexes;
    }

    @Override
    public boolean generateRelations() {

        // [#2294] When DAOs are generated, relations must be generated, too
        return generateRelations || generateTables || generateDaos;
    }

    @Override
    public void setGenerateRelations(boolean generateRelations) {
        this.generateRelations = generateRelations;
    }

    @Override
    public boolean generateImplicitJoinPathsToOne() {
        return generateImplicitJoinPathsToOne && generateRelations();
    }

    @Override
    public void setGenerateImplicitJoinPathsToOne(boolean generateImplicitJoinPathsToOne) {
        this.generateImplicitJoinPathsToOne = generateImplicitJoinPathsToOne;
    }

    @Override
    public boolean generateTableValuedFunctions() {
        return generateTableValuedFunctions;
    }

    @Override
    public void setGenerateTableValuedFunctions(boolean generateTableValuedFunctions) {
        this.generateTableValuedFunctions = generateTableValuedFunctions;
    }

    @Override
    public boolean generateInstanceFields() {
        return generateInstanceFields;
    }

    @Override
    public void setGenerateInstanceFields(boolean generateInstanceFields) {
        this.generateInstanceFields = generateInstanceFields;
    }

    @Override
    public boolean generateGeneratedAnnotation() {

        // [#3121] [#4827] The schema and catalog versions are generated into
        //                 @Generated annotations
        return generateGeneratedAnnotation || useSchemaVersionProvider || useCatalogVersionProvider;
    }

    @Override
    public void setGenerateGeneratedAnnotation(boolean generateGeneratedAnnotation) {
        this.generateGeneratedAnnotation = generateGeneratedAnnotation;
    }

    @Override
    public GeneratedAnnotationType generateGeneratedAnnotationType() {
        return generateGeneratedAnnotationType;
    }

    @Override
    public void setGenerateGeneratedAnnotationType(GeneratedAnnotationType generateGeneratedAnnotationType) {
        this.generateGeneratedAnnotationType = generateGeneratedAnnotationType;
    }

    @Override
    public boolean useSchemaVersionProvider() {
        return useSchemaVersionProvider;
    }

    @Override
    public void setUseSchemaVersionProvider(boolean useSchemaVersionProvider) {
        this.useSchemaVersionProvider = useSchemaVersionProvider;
    }

    @Override
    public boolean useCatalogVersionProvider() {
        return useCatalogVersionProvider;
    }

    @Override
    public void setUseCatalogVersionProvider(boolean useCatalogVersionProvider) {
        this.useCatalogVersionProvider = useCatalogVersionProvider;
    }

    @Override
    public boolean generateRoutines() {
        return generateRoutines;
    }

    @Override
    public void setGenerateRoutines(boolean generateRoutines) {
        this.generateRoutines = generateRoutines;
    }

    @Override
    public boolean generateSequences() {
        return generateSequences;
    }

    @Override
    public void setGenerateSequences(boolean generateSequences) {
        this.generateSequences = generateSequences;
    }

    @Override
    public boolean generateSequenceFlags() {
        return generateSequenceFlags;
    }

    @Override
    public void setGenerateSequenceFlags(boolean generateSequenceFlags) {
        this.generateSequenceFlags = generateSequenceFlags;
    }

    @Override
    public boolean generateUDTs() {
        return generateUDTs;
    }

    @Override
    public void setGenerateUDTs(boolean generateUDTs) {
        this.generateUDTs = generateUDTs;
    }

    @Override
    public boolean generateTables() {

        // [#5525] When DAOs or records are generated, tables must be generated, too
        return generateTables || generateRecords || generateDaos;
    }

    @Override
    public void setGenerateTables(boolean generateTables) {
        this.generateTables = generateTables;
    }

    @Override
    public boolean generateEmbeddables() {
        return generateEmbeddables;
    }

    @Override
    public void setGenerateEmbeddables(boolean generateEmbeddables) {
        this.generateEmbeddables = generateEmbeddables;
    }

    @Override
    public boolean generateRecords() {

        // [#1280] When DAOs are generated, Records must be generated, too
        return generateRecords || generateDaos;
    }

    @Override
    public void setGenerateRecords(boolean generateRecords) {
        this.generateRecords = generateRecords;
    }

    @Override
    public boolean generateRecordsImplementingRecordN() {
        return generateRecords() && generateRecordsImplementingRecordN;
    }

    @Override
    public void setGenerateRecordsImplementingRecordN(boolean generateRecordsImplementingRecordN) {
        this.generateRecordsImplementingRecordN = generateRecordsImplementingRecordN;
    }

    @Override
    public boolean generatePojos() {

        // [#1339] When immutable POJOs are generated, POJOs must be generated
        // [#1280] When DAOs are generated, POJOs must be generated, too
        return generatePojos || generateImmutablePojos || generateDaos;
    }

    @Override
    public void setGeneratePojos(boolean generatePojos) {
        this.generatePojos = generatePojos;
    }

    @Override
    public boolean generateImmutablePojos() {
        return generateImmutablePojos;
    }

    @Override
    public void setGenerateImmutablePojos(boolean generateImmutablePojos) {
        this.generateImmutablePojos = generateImmutablePojos;
    }

    @Override
    public boolean generateSerializablePojos() {
        return generateSerializablePojos && generatePojos();
    }

    @Override
    public void setGenerateSerializablePojos(boolean generateSerializablePojos) {
        this.generateSerializablePojos = generateSerializablePojos;
    }

    @Override
    public boolean generateInterfaces() {
        return generateInterfaces || generateImmutableInterfaces;
    }

    @Override
    public void setGenerateInterfaces(boolean generateInterfaces) {
        this.generateInterfaces = generateInterfaces;
    }

    @Override
    public boolean generateImmutableInterfaces() {
        return generateImmutableInterfaces || (generateInterfaces && generateImmutablePojos);
    }

    @Override
    public void setGenerateImmutableInterfaces(boolean generateImmutableInterfaces) {
        this.generateImmutableInterfaces = generateImmutableInterfaces;
    }

    @Override
    public boolean generateSerializableInterfaces() {
        return generateSerializableInterfaces && generateInterfaces();
    }

    @Override
    public void setGenerateSerializableInterfaces(boolean generateSerializableInterfaces) {
        this.generateSerializableInterfaces = generateSerializableInterfaces;
    }

    @Override
    public boolean generateDaos() {
        return generateDaos;
    }

    @Override
    public void setGenerateDaos(boolean generateDaos) {
        this.generateDaos = generateDaos;
    }

    @Override
    public boolean generateJPAAnnotations() {
        return generateJPAAnnotations;
    }

    @Override
    public void setGenerateJPAAnnotations(boolean generateJPAAnnotations) {
        this.generateJPAAnnotations = generateJPAAnnotations;
    }

    @Override
    public String generateJPAVersion() {
        return generateJPAVersion;
    }

    @Override
    public void setGenerateJPAVersion(String generateJPAVersion) {
        this.generateJPAVersion = generateJPAVersion;
    }

    @Override
    public boolean generateValidationAnnotations() {
        return generateValidationAnnotations;
    }

    @Override
    public void setGenerateValidationAnnotations(boolean generateValidationAnnotations) {
        this.generateValidationAnnotations = generateValidationAnnotations;
    }

    @Override
    public boolean generateSpringAnnotations() {
        return generateSpringAnnotations;
    }

    @Override
    public void setGenerateSpringAnnotations(boolean generateSpringAnnotations) {
        this.generateSpringAnnotations = generateSpringAnnotations;
    }

    @Override
    public boolean generateGlobalObjectReferences() {
        return generateGlobalObjectReferences;
    }

    @Override
    public void setGenerateGlobalObjectReferences(boolean generateGlobalObjectReferences) {
        this.generateGlobalObjectReferences = generateGlobalObjectReferences;
    }

    @Override
    public boolean generateGlobalCatalogReferences() {
        return generateGlobalObjectReferences() && generateGlobalCatalogReferences;
    }

    @Override
    public void setGenerateGlobalCatalogReferences(boolean globalCatalogReferences) {
        this.generateGlobalCatalogReferences = globalCatalogReferences;
    }

    @Override
    public boolean generateGlobalSchemaReferences() {
        return generateGlobalObjectReferences() && generateGlobalSchemaReferences;
    }

    @Override
    public void setGenerateGlobalSchemaReferences(boolean globalSchemaReferences) {
        this.generateGlobalSchemaReferences = globalSchemaReferences;
    }

    @Override
    public boolean generateGlobalRoutineReferences() {
        return generateRoutines() && generateGlobalObjectReferences() && generateGlobalRoutineReferences;
    }

    @Override
    public void setGenerateGlobalRoutineReferences(boolean generateGlobalRoutineReferences) {
        this.generateGlobalRoutineReferences = generateGlobalRoutineReferences;
    }

    @Override
    public boolean generateGlobalSequenceReferences() {
        return generateSequences() && generateGlobalObjectReferences() && generateGlobalSequenceReferences;
    }

    @Override
    public void setGenerateGlobalSequenceReferences(boolean generateGlobalSequenceReferences) {
        this.generateGlobalSequenceReferences = generateGlobalSequenceReferences;
    }

    @Override
    public boolean generateGlobalTableReferences() {
        return generateTables() && generateGlobalObjectReferences() && generateGlobalTableReferences;
    }

    @Override
    public void setGenerateGlobalTableReferences(boolean generateGlobalTableReferences) {
        this.generateGlobalTableReferences = generateGlobalTableReferences;
    }

    @Override
    public boolean generateGlobalUDTReferences() {
        return generateUDTs() && generateGlobalObjectReferences() && generateGlobalUDTReferences;
    }

    @Override
    public void setGenerateGlobalUDTReferences(boolean generateGlobalUDTReferences) {
        this.generateGlobalUDTReferences = generateGlobalUDTReferences;
    }

    @Override
    public boolean generateGlobalQueueReferences() {
        return generateQueues() && generateGlobalObjectReferences() && generateGlobalQueueReferences;
    }

    @Override
    public void setGenerateGlobalQueueReferences(boolean globalQueueReferences) {
        this.generateGlobalQueueReferences = globalQueueReferences;
    }

    @Override
    public boolean generateGlobalLinkReferences() {
        return generateLinks() && generateGlobalObjectReferences() && generateGlobalLinkReferences;
    }

    @Override
    public void setGenerateGlobalLinkReferences(boolean globalLinkReferences) {
        this.generateGlobalLinkReferences = globalLinkReferences;
    }

    @Override
    public boolean generateGlobalKeyReferences() {
        return generateKeys() && generateGlobalObjectReferences() && generateGlobalKeyReferences;
    }

    @Override
    public void setGenerateGlobalKeyReferences(boolean globalKeyReferences) {
        this.generateGlobalKeyReferences = globalKeyReferences;
    }

    @Override
    public boolean generateGlobalIndexReferences() {
        return generateIndexes() && generateGlobalObjectReferences() && generateGlobalIndexReferences;
    }

    @Override
    public void setGenerateGlobalIndexReferences(boolean globalIndexReferences) {
        this.generateGlobalIndexReferences = globalIndexReferences;
    }

    @Override
    public boolean generateQueues() {
        return generateQueues;
    }

    @Override
    public void setGenerateQueues(boolean queues) {
        this.generateQueues = queues;
    }

    @Override
    public boolean generateLinks() {
        return generateLinks;
    }

    @Override
    public void setGenerateLinks(boolean links) {
        this.generateLinks = links;
    }

    @Override
    public boolean generateKeys() {
        return generateKeys;
    }

    @Override
    public void setGenerateKeys(boolean keys) {
        this.generateKeys = keys;
    }

    @Override
    public boolean generateJavadoc() {
        return generateJavadoc;
    }

    @Override
    public void setGenerateJavadoc(boolean javadoc) {
        this.generateJavadoc = javadoc;
    }

    @Override
    public boolean generateComments() {
        return generateComments;
    }

    @Override
    public void setGenerateComments(boolean comments) {
        this.generateComments = comments;
    }

    @Override
    public boolean generateCommentsOnAttributes() {
        return generateComments() && generateCommentsOnAttributes;
    }

    @Override
    public void setGenerateCommentsOnAttributes(boolean commentsOnAttributes) {
        this.generateCommentsOnAttributes = commentsOnAttributes;
    }

    @Override
    public boolean generateCommentsOnCatalogs() {
        return generateComments() && generateCommentsOnCatalogs;
    }

    @Override
    public void setGenerateCommentsOnCatalogs(boolean commentsOnCatalogs) {
        this.generateCommentsOnCatalogs = commentsOnCatalogs;
    }

    @Override
    public boolean generateCommentsOnColumns() {
        return generateComments() && generateCommentsOnColumns;
    }

    @Override
    public void setGenerateCommentsOnColumns(boolean commentsOnColumns) {
        this.generateCommentsOnColumns = commentsOnColumns;
    }

    @Override
    public boolean generateCommentsOnKeys() {
        return generateComments() && generateCommentsOnKeys;
    }

    @Override
    public void setGenerateCommentsOnKeys(boolean commentsOnKeys) {
        this.generateCommentsOnKeys = commentsOnKeys;
    }

    @Override
    public boolean generateCommentsOnLinks() {
        return generateComments() && generateCommentsOnLinks;
    }

    @Override
    public void setGenerateCommentsOnLinks(boolean commentsOnLinks) {
        this.generateCommentsOnLinks = commentsOnLinks;
    }

    @Override
    public boolean generateCommentsOnPackages() {
        return generateComments() && generateCommentsOnPackages;
    }

    @Override
    public void setGenerateCommentsOnPackages(boolean commentsOnPackages) {
        this.generateCommentsOnPackages = commentsOnPackages;
    }

    @Override
    public boolean generateCommentsOnParameters() {
        return generateComments() && generateCommentsOnParameters;
    }

    @Override
    public void setGenerateCommentsOnParameters(boolean commentsOnParameters) {
        this.generateCommentsOnParameters = commentsOnParameters;
    }

    @Override
    public boolean generateCommentsOnQueues() {
        return generateComments() && generateCommentsOnQueues;
    }

    @Override
    public void setGenerateCommentsOnQueues(boolean commentsOnQueues) {
        this.generateCommentsOnQueues = commentsOnQueues;
    }

    @Override
    public boolean generateCommentsOnRoutines() {
        return generateComments() && generateCommentsOnRoutines;
    }

    @Override
    public void setGenerateCommentsOnRoutines(boolean commentsOnRoutines) {
        this.generateCommentsOnRoutines = commentsOnRoutines;
    }

    @Override
    public boolean generateCommentsOnSchemas() {
        return generateComments() && generateCommentsOnSchemas;
    }

    @Override
    public void setGenerateCommentsOnSchemas(boolean commentsOnSchemas) {
        this.generateCommentsOnSchemas = commentsOnSchemas;
    }

    @Override
    public boolean generateCommentsOnSequences() {
        return generateComments() && generateCommentsOnSequences;
    }

    @Override
    public void setGenerateCommentsOnSequences(boolean commentsOnSequences) {
        this.generateCommentsOnSequences = commentsOnSequences;
    }

    @Override
    public boolean generateCommentsOnTables() {
        return generateComments() && generateCommentsOnTables;
    }

    @Override
    public void setGenerateCommentsOnTables(boolean commentsOnTables) {
        this.generateCommentsOnTables = commentsOnTables;
    }

    @Override
    public boolean generateCommentsOnUDTs() {
        return generateComments() && generateCommentsOnUDTs;
    }

    @Override
    public void setGenerateCommentsOnUDTs(boolean commentsOnUDTs) {
        this.generateCommentsOnUDTs = commentsOnUDTs;
    }

    @Override
    @Deprecated
    public boolean fluentSetters() {
        return generateFluentSetters();
    }

    @Override
    @Deprecated
    public void setFluentSetters(boolean fluentSetters) {
        setGenerateFluentSetters(fluentSetters);
    }

    @Override
    public boolean generateFluentSetters() {
        return generateFluentSetters;
    }

    @Override
    public void setGenerateFluentSetters(boolean fluentSetters) {
        this.generateFluentSetters = fluentSetters;
    }

    @Override
    public boolean generateJavaBeansGettersAndSetters() {
        return generateJavaBeansGettersAndSetters;
    }

    @Override
    public void setGenerateJavaBeansGettersAndSetters(boolean javaBeansGettersAndSetters) {
        this.generateJavaBeansGettersAndSetters = javaBeansGettersAndSetters;
    }

    @Override
    public boolean generateVarargsSetters() {
        return generateVarargsSetters;
    }

    @Override
    public void setGenerateVarargsSetters(boolean varargsSetters) {
        this.generateVarargsSetters = varargsSetters;
    }

    @Override
    public boolean generatePojosEqualsAndHashCode() {
        return generatePojosEqualsAndHashCode;
    }

    @Override
    public void setGeneratePojosEqualsAndHashCode(boolean generatePojosEqualsAndHashCode) {
        this.generatePojosEqualsAndHashCode = generatePojosEqualsAndHashCode;
    }

    @Override
    public boolean generatePojosToString() {
        return generatePojosToString;
    }

    @Override
    public void setGeneratePojosToString(boolean generatePojosToString) {
        this.generatePojosToString = generatePojosToString;
    }

    @Override
    @Deprecated
    public String fullyQualifiedTypes() {
        return generateFullyQualifiedTypes();
    }

    @Override
    @Deprecated
    public void setFullyQualifiedTypes(String fullyQualifiedTypes) {
        setGenerateFullyQualifiedTypes(fullyQualifiedTypes);
    }

    @Override
    public String generateFullyQualifiedTypes() {
        return generateFullyQualifiedTypes;
    }

    @Override
    public void setGenerateFullyQualifiedTypes(String generateFullyQualifiedTypes) {
        this.generateFullyQualifiedTypes = generateFullyQualifiedTypes;
    }

    @Override
    public boolean generateJavaTimeTypes() {
        return generateJavaTimeTypes;
    }

    @Override
    public void setGenerateJavaTimeTypes(boolean generateJavaTimeTypes) {
        this.generateJavaTimeTypes = generateJavaTimeTypes;
    }

    @Override
    public boolean generateEmptyCatalogs() {
        return generateEmptyCatalogs;
    }

    @Override
    public void setGenerateEmptyCatalogs(boolean generateEmptyCatalogs) {
        this.generateEmptyCatalogs = generateEmptyCatalogs;
    }

    @Override
    public boolean generateEmptySchemas() {
        return generateEmptySchemas;
    }

    @Override
    public void setGenerateEmptySchemas(boolean generateEmptySchemas) {
        this.generateEmptySchemas = generateEmptySchemas;
    }

    @Override
    public boolean generatePrimaryKeyTypes() {
        return generatePrimaryKeyTypes;
    }

    @Override
    public void setGeneratePrimaryKeyTypes(boolean generatePrimaryKeyTypes) {
        this.generatePrimaryKeyTypes = generatePrimaryKeyTypes;
    }

    @Override
    public String generateNewline() {
        return generateNewline;
    }

    @Override
    public void setGenerateNewline(String newline) {
        // [#6234] The character provided in the configuration may either be the
        //         ASCII character itself, or an escaped version of it, such as "\\n"
        this.generateNewline = newline == null ? newline : newline.replace("\\r", "\r").replace("\\n", "\n");
    }

    @Override
    public String generateIndentation() {
        return generateIndentation;
    }

    @Override
    public void setGenerateIndentation(String indentation) {
        this.generateIndentation = indentation;
    }

    // ----

    @Override
    public void setTargetDirectory(String directory) {
        strategy.setTargetDirectory(directory);
    }

    @Override
    public String getTargetDirectory() {
        return strategy.getTargetDirectory();
    }

    @Override
    public void setTargetPackage(String packageName) {
        strategy.setTargetPackage(packageName);
    }

    @Override
    public String getTargetPackage() {
        return strategy.getTargetPackage();
    }

    @Override
    public String getTargetEncoding() {
        return targetEncoding;
    }

    @Override
    public void setTargetEncoding(String encoding) {
        this.targetEncoding = encoding;
    }

    @Override
    public boolean getTargetClean() {
        return targetClean;
    }

    @Override
    public void setTargetClean(boolean clean) {
        this.targetClean = clean;
    }

    /**
     * If file is a directory, recursively empty its children.
     * If file is a file, delete it.
     */
    protected void empty(File file, String suffix) {
        empty(file, suffix, Collections.<File>emptySet(), Collections.<File>emptySet());
    }

    /**
     * If file is a directory, recursively empty its children.
     * If file is a file, delete it, except if it is in the list of files to keep.
     */
    protected void empty(File file, String suffix, Set<File> keep, Set<File> ignore) {
        if (!targetClean)
            return;

        if (file != null) {

            // Just a Murphy's Law safeguard in case a user misconfigures their config...
            if (file.getParentFile() == null) {
                log.warn("WARNING: Root directory configured for code generation. Not deleting anything from previous generations!");
                return;
            }

            // [#5614] Don't go into these directories
            for (File i : ignore)
                if (file.getAbsolutePath().startsWith(i.getAbsolutePath()))
                    return;

            if (file.isDirectory()) {
                File[] children = file.listFiles();

                if (children != null)
                    for (File child : children)
                        empty(child, suffix, keep, ignore);

                File[] childrenAfterDeletion = file.listFiles();

                // [#5556] Delete directory if empty after content was removed.
                //         Useful if a catalog / schema was dropped, or removed from code generation, or renamed
                if (childrenAfterDeletion != null && childrenAfterDeletion.length == 0)
                    file.delete();
            }
            else if (file.getName().endsWith(suffix) && !keep.contains(file)) {
                file.delete();
            }
        }
    }
}
