/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
import java.util.List;

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
     * Whether fields are instance fields (as opposed to static fields)
     */
    void setInstanceFields(boolean instanceFields);

    /**
     * Whether fields are instance fields (as opposed to static fields)
     */
    boolean getInstanceFields();

    /**
     * This is applied to definitions that can result in singleton static and
     * instance members. For instance, the singleton instance of a
     * {@link TableDefinition} is a java identifier
     *
     * @return The Java identifier representing this object, e.g. [my_table]
     */
    String getJavaIdentifier(Definition definition);

    /**
     * This is applied to definitions that can result in singleton static and
     * instance members. For instance, the singleton instance of a
     * {@link TableDefinition} is a java identifier
     *
     * @return The Java identifier representing this object, e.g. [my_table]
     */
    String getFullJavaIdentifier(Definition definition);

    /**
     * This is applied to definitions that can result in setters of a container.
     * For example, the definition could be a {@link ColumnDefinition}, the
     * container a {@link TableDefinition}. Then this would apply to records and
     * POJOs. Also, the definition could be an {@link AttributeDefinition} and
     * the container a {@link UDTDefinition}
     * <p>
     * This is the same as calling
     * <code>getJavaSetterName(definition, Mode.DEFAULT)</code>
     *
     * @return The Java setter method name representing this object, e.g.
     *         [setMyTable]
     */
    String getJavaSetterName(Definition definition);

    /**
     * This is applied to definitions that can result in setters of a container.
     * For example, the definition could be a {@link ColumnDefinition}, the
     * container a {@link TableDefinition}. Then this would apply to records and
     * POJOs. Also, the definition could be an {@link AttributeDefinition} and
     * the container a {@link UDTDefinition}
     *
     * @return The Java setter method name representing this object, e.g.
     *         [setMyTable]
     */
    String getJavaSetterName(Definition definition, Mode mode);

    /**
     * This is applied to definitions that can result in getters of a container.
     * For example, the definition could be a {@link ColumnDefinition}, the
     * container a {@link TableDefinition}. Then this would apply to records and
     * POJOs. Also, the definition could be an {@link AttributeDefinition} and
     * the container a {@link UDTDefinition}
     * <p>
     * This is the same as calling
     * <code>getJavaGetterName(definition, Mode.DEFAULT)</code>
     *
     * @return The Java getter method name representing this object, e.g.
     *         [getMyTable]
     */
    String getJavaGetterName(Definition definition);

    /**
     * This is applied to definitions that can result in getters of a container.
     * For example, the definition could be a {@link ColumnDefinition}, the
     * container a {@link TableDefinition}. Then this would apply to records and
     * POJOs. Also, the definition could be an {@link AttributeDefinition} and
     * the container a {@link UDTDefinition}
     *
     * @return The Java getter method name representing this object, e.g.
     *         [getMyTable]
     */
    String getJavaGetterName(Definition definition, Mode mode);

    /**
     * This is applied to definitions that can result in methods. For example,
     * the definition could be a {@link RoutineDefinition}
     * <p>
     * This is the same as calling
     * <code>getJavaMethodName(definition, Mode.DEFAULT)</code>
     *
     * @return The Java method name representing this object, e.g. [myFunction]
     */
    String getJavaMethodName(Definition definition);

    /**
     * This is applied to definitions that can result in methods. For example,
     * the definition could be a {@link RoutineDefinition}
     *
     * @return The Java method name representing this object, e.g. [myFunction]
     */
    String getJavaMethodName(Definition definition, Mode mode);

    /**
     * This is the same as calling
     * <code>getJavaClassExtends(definition, Mode.DEFAULT)</code>
     *
     * @return The super class name of the Java class representing this object,
     *         e.g. [com.example.AbstractPojo]. If this returns
     *         <code>null</code> or an empty string, then no super class is
     *         extended.
     */
    String getJavaClassExtends(Definition definition);

    /**
     * @return The super class name of the Java class representing this object,
     *         e.g. [com.example.AbstractPojo]. If this returns
     *         <code>null</code> or an empty string, then no super class is
     *         extended.
     */
    String getJavaClassExtends(Definition definition, Mode mode);

    /**
     * This is the same as calling
     * <code>getJavaClassImplements(definition, Mode.DEFAULT)</code>
     *
     * @return The implemented interface names of the Java class name
     *         representing this object, e.g. [com.example.Pojo] If this returns
     *         <code>null</code> or an empty list, then no interfaces are
     *         implemented.
     */
    List<String> getJavaClassImplements(Definition definition);

    /**
     * @return The implemented interface names of the Java class name
     *         representing this object, e.g. [com.example.Pojo]. If this
     *         returns <code>null</code> or an empty list, then no interfaces
     *         are implemented.
     */
    List<String> getJavaClassImplements(Definition definition, Mode mode);

    /**
     * This is the same as calling
     * <code>getJavaClassName(definition, Mode.DEFAULT)</code>
     *
     * @return The Java class name representing this object, e.g. [MyTable]
     */
    String getJavaClassName(Definition definition);

    /**
     * @return The Java class name representing this object, e.g.
     *         [MyTableSuffix]
     */
    String getJavaClassName(Definition definition, Mode mode);

    /**
     * This is the same as calling
     * <code>getJavaPackageName(definition, Mode.DEFAULT)</code>
     *
     * @return The Java package name of this object, e.g. [org.jooq.generated]
     */
    String getJavaPackageName(Definition definition);

    /**
     * @return The Java package name of this object, e.g. [org.jooq.generated]
     */
    String getJavaPackageName(Definition definition, Mode mode);

    /**
     * The "java member name" is applied where a definition is used as a member
     * (for POJOs) or as a method argument (for setters). Example definitions
     * are
     * <ul>
     * <li> {@link ColumnDefinition}</li>
     * <li> {@link ParameterDefinition}</li>
     * <li> {@link AttributeDefinition}</li>
     * </ul>
     * This is the same as calling
     * <code>getJavaMemberName(definition, Mode.DEFAULT)</code>
     *
     * @return The Java class name representing this object, starting with a
     *         lower case character, e.g. [myTable]
     */
    String getJavaMemberName(Definition definition);

    /**
     * The "java member name" is applied where a definition is used as a member
     * (for POJOs) or as a method argument (for setters). Example definitions
     * are
     * <ul>
     * <li> {@link ColumnDefinition}</li>
     * <li> {@link ParameterDefinition}</li>
     * <li> {@link AttributeDefinition}</li>
     * </ul>
     *
     * @return The Java class name representing this object, starting with a
     *         lower case character, e.g. [myTableSuffix]
     */
    String getJavaMemberName(Definition definition, Mode mode);

    /**
     * @return The full Java class name representing this object, e.g.
     *         [org.jooq.generated.MyTable]
     */
    String getFullJavaClassName(Definition definition);

    /**
     * This is the same as calling
     * <code>getFullJavaClassName(definition, Mode.DEFAULT)</code>
     *
     * @return The full Java class name representing this object, e.g.
     *         [org.jooq.generated.MyTable][suffix]
     */
    String getFullJavaClassName(Definition definition, Mode mode);

    /**
     * @return The Java class file name representing this object, e.g.
     *         [MyTable.java]
     */
    String getFileName(Definition definition);

    /**
     * @return The Java class file name representing this object, e.g.
     *         [MyTableSuffix.java]
     */
    String getFileName(Definition definition, Mode mode);

    /**
     * @return The Java class file name representing this object, e.g.
     *         [C:\org\jooq\generated\MyTable.java]
     */
    File getFile(Definition definition);

    /**
     * @return The Java class file name representing this object, e.g.
     *         [C:\org\jooq\generated\MyTableSuffix.java]
     */
    File getFile(Definition definition, Mode mode);

    /**
     * The "mode" by which an artefact should be named
     */
    enum Mode {

        /**
         * The default mode. This is used when any {@link Definition}'s meta
         * type is being rendered.
         */
        DEFAULT,

        /**
         * The record mode. This is used when a {@link TableDefinition} or a
         * {@link UDTDefinition}'s record class is being rendered.
         */
        RECORD,

        /**
         * The pojo mode. This is used when a {@link TableDefinition}'s pojo
         * class is being rendered
         */
        POJO,

        /**
         * The enum mode. This is used when a {@link EnumDefinition}'s class is
         * being rendered
         */
        ENUM,

        /**
         * The factory mode. This is used when a {@link SchemaDefinition}'s
         * factory class is being rendered
         */
        FACTORY,

    }
}
