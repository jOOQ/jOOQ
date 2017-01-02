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

import java.io.File;
import java.util.Collection;
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
     * This is applied to definitions that can result in reference static and
     * instance members. For instance, the reference instance of a
     * {@link TableDefinition} is a java identifier
     *
     * @return The Java identifier representing this object, e.g. [my_table]
     */
    String getJavaIdentifier(Definition definition);

    /**
     * @see #getJavaIdentifier(Definition)
     */
    List<String> getJavaIdentifiers(Collection<? extends Definition> definitions);

    /**
     * @see #getJavaIdentifier(Definition)
     */
    List<String> getJavaIdentifiers(Definition... definitions);

    /**
     * This is applied to definitions that can result in reference static and
     * instance members. For instance, the reference instance of a
     * {@link TableDefinition} is a java identifier
     *
     * @return The Java identifier representing this object, e.g. [my_table]
     */
    String getFullJavaIdentifier(Definition definition);

    /**
     * @see #getFullJavaIdentifier(Definition)
     */
    List<String> getFullJavaIdentifiers(Collection<? extends Definition> definitions);

    /**
     * @see #getFullJavaIdentifier(Definition)
     */
    List<String> getFullJavaIdentifiers(Definition... definitions);

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
     * <li>{@link ColumnDefinition}</li>
     * <li>{@link ParameterDefinition}</li>
     * <li>{@link AttributeDefinition}</li>
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
     * <li>{@link ColumnDefinition}</li>
     * <li>{@link ParameterDefinition}</li>
     * <li>{@link AttributeDefinition}</li>
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
     * @return The directory containing all Java objects, e.g.
     *         [C:\org\jooq\generated]
     */
    File getFileRoot();

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
     * @return The Java class file name representing this object, e.g.
     *         [C:\org\jooq\generated\fileName]
     */
    File getFile(String fileName);

    /**
     * @return The Java class file header, e.g. <code><pre>
     * This file is generated by jOOQ.
     * </pre></code>
     */
    String getFileHeader(Definition definition);

    /**
     * @return The Java class file header, e.g. <code><pre>
     * This file is generated by jOOQ.
     * </pre></code>
     */
    String getFileHeader(Definition definition, Mode mode);

    /**
     * @return The overload suffix to be applied when generating overloaded
     *         routine artefacts, e.g.
     *         <code>"_OverloadIndex_" + overloadIndex</code>
     */
    String getOverloadSuffix(Definition definition, Mode mode, String overloadIndex);

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
         * the interface mode. This is used when a {@link TableDefinition}'s
         * interface is being rendered
         */
        INTERFACE,

        /**
         * The dao mode. This is used when a {@link TableDefinition}'s dao class
         * is being rendered
         */
        DAO,

        /**
         * The enum mode. This is used when a {@link EnumDefinition}'s class is
         * being rendered
         */
        ENUM,

        /**
         * The domain mode. This is used when a {@link DomainDefinition}'s class
         * is being rendered
         */
        DOMAIN

    }
}
