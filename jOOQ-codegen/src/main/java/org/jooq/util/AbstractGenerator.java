/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.util;

import java.io.File;
import java.util.Collections;
import java.util.Set;


/**
 * A common base implementation for {@link Generator} objects
 *
 * @author Lukas Eder
 */
abstract class AbstractGenerator implements Generator {

    boolean                            generateDeprecated             = true;
    boolean                            generateRelations              = true;
    boolean                            generateInstanceFields         = true;
    boolean                            generateGeneratedAnnotation    = true;
    boolean                            useSchemaVersionProvider       = false;
    boolean                            generateRecords                = true;
    boolean                            generatePojos                  = false;
    boolean                            generatePojosEqualsAndHashCode = false;
    boolean                            generateImmutablePojos         = false;
    boolean                            generateInterfaces             = false;
    boolean                            generateDaos                   = false;
    boolean                            generateJPAAnnotations         = false;
    boolean                            generateValidationAnnotations  = false;
    boolean                            generateGlobalObjectReferences = true;
    boolean                            fluentSetters                  = false;

    protected GeneratorStrategyWrapper strategy;

    @Override
    public void setStrategy(GeneratorStrategy strategy) {
        this.strategy = new GeneratorStrategyWrapper(this, strategy);
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
        return generateRelations || generateDaos;
    }

    @Override
    public void setGenerateRelations(boolean generateRelations) {
        this.generateRelations = generateRelations;
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

        // [#3121] The schema version is generated into @Generated annotations
        return generateGeneratedAnnotation || useSchemaVersionProvider;
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
        return generateInterfaces;
    }

    @Override
    public void setGenerateInterfaces(boolean generateInterfaces) {
        this.generateInterfaces = generateInterfaces;
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
    public boolean generateGlobalObjectReferences() {
        return generateGlobalObjectReferences;
    }

    @Override
    public void setGenerateGlobalObjectReferences(boolean generateGlobalObjectReferences) {
        this.generateGlobalObjectReferences = generateGlobalObjectReferences;
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
