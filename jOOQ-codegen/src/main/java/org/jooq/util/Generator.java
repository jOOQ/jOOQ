/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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
     * Whether global object references should be generated
     */
    boolean generateGlobalObjectReferences();

    /**
     * Whether global object references should be generated
     */
    void setGenerateGlobalObjectReferences(boolean generateGlobalObjectReferences);

    /**
     * The target directory
     */
    String getTargetDirectory();

    /**
     * Initialise the target directory
     */
    void setTargetDirectory(String directory);

    /**
     * @return Get the target package for the current configuration
     */
    String getTargetPackage();

    /**
     * Initialise the target package name
     */
    void setTargetPackage(String packageName);

}
