/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.util;

import java.io.File;

import org.jooq.util.jaxb.License;


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
    boolean                            generateRecords                = true;
    boolean                            generatePojos                  = false;
    boolean                            generateImmutablePojos         = false;
    boolean                            generateInterfaces             = false;
    boolean                            generateDaos                   = false;
    boolean                            generateJPAAnnotations         = false;
    boolean                            generateValidationAnnotations  = false;
    boolean                            generateGlobalObjectReferences = true;

    protected GeneratorStrategyWrapper strategy;
    private License                    license;

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
        return generateGeneratedAnnotation;
    }

    @Override
    public void setGenerateGeneratedAnnotation(boolean generateGeneratedAnnotation) {
        this.generateGeneratedAnnotation = generateGeneratedAnnotation;
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
    public License getLicense() {
        return license;
    }

    @Override
    public void setLicense(License license) {
        this.license = license;
    }

    /**
     * If file is a directory, recursively empty its children.
     * If file is a file, delete it
     */
    protected void empty(File file, String suffix) {
        if (file != null) {
            if (file.isDirectory()) {
                File[] children = file.listFiles();

                if (children != null) {
                    for (File child : children) {
                        empty(child, suffix);
                    }
                }
            } else {
                if (file.getName().endsWith(suffix)) {
                    file.delete();
                }
            }
        }
    }
}
