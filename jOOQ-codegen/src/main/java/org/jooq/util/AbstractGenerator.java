/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.util;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.jooq.tools.JooqLogger;


/**
 * A common base implementation for {@link Generator} objects
 *
 * @author Lukas Eder
 */
abstract class AbstractGenerator implements Generator {

    private static final JooqLogger    log                              = JooqLogger.getLogger(AbstractGenerator.class);

    boolean                            generateDeprecated               = true;
    boolean                            generateRelations                = true;
    boolean                            generateInstanceFields           = true;
    boolean                            generateGeneratedAnnotation      = true;
    boolean                            useSchemaVersionProvider         = false;
    boolean                            useCatalogVersionProvider        = false;
    boolean                            generateRoutines                 = true;
    boolean                            generateSequences                = true;
    boolean                            generateUDTs                     = true;
    boolean                            generateTables                   = true;
    boolean                            generateRecords                  = true;
    boolean                            generatePojos                    = false;
    boolean                            generatePojosEqualsAndHashCode   = false;
    boolean                            generatePojosToString            = true;
    boolean                            generateImmutablePojos           = false;
    boolean                            generateInterfaces               = false;
    boolean                            generateImmutableInterfaces      = false;
    boolean                            generateDaos                     = false;
    boolean                            generateJPAAnnotations           = false;
    boolean                            generateValidationAnnotations    = false;
    boolean                            generateSpringAnnotations        = false;
    boolean                            generateQueues                   = true;
    boolean                            generateLinks                    = true;
    boolean                            generateGlobalObjectReferences   = true;
    boolean                            generateGlobalCatalogReferences  = true;
    boolean                            generateGlobalSchemaReferences   = true;
    boolean                            generateGlobalRoutineReferences  = true;
    boolean                            generateGlobalSequenceReferences = true;
    boolean                            generateGlobalTableReferences    = true;
    boolean                            generateGlobalUDTReferences      = true;
    boolean                            generateGlobalQueueReferences    = true;
    boolean                            generateGlobalLinkReferences     = true;
    boolean                            fluentSetters                    = false;
    String                             generateFullyQualifiedTypes      = "";
    boolean                            generateTableValuedFunctions     = false;
    boolean                            generateEmptyCatalogs            = false;
    boolean                            generateEmptySchemas             = false;

    protected GeneratorStrategyWrapper strategy;
    protected String                   targetEncoding                   = "UTF-8";
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
    public boolean generateRelations() {

        // [#2294] When DAOs are generated, relations must be generated, too
        return generateRelations || generateTables || generateDaos;
    }

    @Override
    public void setGenerateRelations(boolean generateRelations) {
        this.generateRelations = generateRelations;
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
    public boolean generateRecords() {

        // [#1280] When DAOs are generated, Records must be generated, too
        return generateRecords || generateDaos;
    }

    @Override
    public void setGenerateRecords(boolean generateRecords) {
        this.generateRecords = generateRecords;
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
    public boolean fluentSetters() {
        return fluentSetters;
    }

    @Override
    public void setFluentSetters(boolean fluentSetters) {
        this.fluentSetters = fluentSetters;
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

    /**
     * If file is a directory, recursively empty its children.
     * If file is a file, delete it.
     */
    protected void empty(File file, String suffix) {
        empty(file, suffix, Collections.<File>emptySet());
    }

    /**
     * If file is a directory, recursively empty its children.
     * If file is a file, delete it, except if it is in the list of files to keep.
     */
    protected void empty(File file, String suffix, Set<File> keep) {
        if (file != null) {
            if (file.isDirectory()) {
                File[] children = file.listFiles();

                if (children != null) {
                    for (File child : children) {
                        empty(child, suffix, keep);
                    }
                }
            } else {
                if (file.getName().endsWith(suffix) && !keep.contains(file)) {
                    file.delete();
                }
            }
        }
    }
}
