/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.util;

import java.io.File;

/**
 * A strategy for naming various artefacts generated from {@link Definition}'s
 *
 * @author Lukas Eder
 */
public interface GeneratorStrategy {

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

    /**
     * @return The Java identifier representing this object, e.g.
     *         [my_table]
     */
    String getJavaIdentifier(Definition definition);

    /**
     * @return The Java identifier representing this object, e.g.
     *         [MY_TABLE]
     */
    String getJavaIdentifierUC(Definition definition);

    /**
     * @return The Java identifier representing this object, e.g.
     *         [org.jooq.generated.MY_TABLE]
     */
    String getFullJavaIdentifierUC(Definition definition);

    /**
     * @return The Java class name representing this object, e.g. [MyTable]
     */
    String getJavaClassName(Definition definition);

    /**
     * @return The Java class name representing this object, e.g.
     *         [MyTableSuffix]
     */
    String getJavaClassName(Definition definition, String suffix);

    /**
     * @return The Java package name of this object, e.g. [org.jooq.generated]
     */
    String getJavaPackageName(Definition definition);

    /**
     * @return The Java package name of this object, e.g. [org.jooq.generated]
     */
    String getJavaPackageName(Definition definition, String suffix);

    /**
     * @return The Java class name representing this object, starting with a
     *         lower case character, e.g. [myTable]
     */
    String getJavaClassNameLC(Definition definition);

    /**
     * @return The Java class name representing this object, starting with a
     *         lower case character, e.g. [myTableSuffix]
     */
    String getJavaClassNameLC(Definition definition, String suffix);

    /**
     * @return The full Java class name representing this object, e.g.
     *         [org.jooq.generated.MyTable]
     */
    String getFullJavaClassName(Definition definition);

    /**
     * @return The full Java class name representing this object, e.g.
     *         [org.jooq.generated.MyTable][suffix]
     */
    String getFullJavaClassName(Definition definition, String suffix);

    /**
     * @return Get the target package's subpackage for this definition
     */
    String getSubPackage(Definition definition);

    /**
     * @return The Java class file name representing this object, e.g.
     *         [MyTable.java]
     */
    String getFileName(Definition definition);

    /**
     * @return The Java class file name representing this object, e.g.
     *         [MyTableSuffix.java]
     */
    String getFileName(Definition definition, String suffix);

    /**
     * @return The Java class file name representing this object, e.g.
     *         [C:\org\jooq\generated\MyTable.java]
     */
    File getFile(Definition definition);

    /**
     * @return The Java class file name representing this object, e.g.
     *         [C:\org\jooq\generated\MyTableSuffix.java]
     */
    File getFile(Definition definition, String suffix);

    /**
     * Set the prefix to be used for table classes
     */
    void setMetaClassPrefix(String prefix);

    /**
     * Set the suffix to be used for table classes
     */
    void setMetaClassSuffix(String suffix);

    /**
     * Set the prefix to be used for record classes
     */
    void setRecordClassPrefix(String prefix);

    /**
     * Set the suffix to be used for record classes
     */
    void setRecordClassSuffix(String suffix);

    /**
     * Set the naming scheme for various objects
     */
    void setMemberScheme(String scheme);
}
