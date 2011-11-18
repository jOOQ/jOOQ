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
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jooq.impl.TableRecordImpl;
import org.jooq.impl.UDTRecordImpl;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.tools.StringUtils;

/**
 * The default naming strategy for the {@link DefaultGenerator}
 *
 * @author Lukas Eder
 */
@SuppressWarnings("unused")
public class DefaultGeneratorStrategy implements GeneratorStrategy {

    private final Map<Class<?>, Set<String>> reservedColumns = new HashMap<Class<?>, Set<String>>();

    private String                           targetDirectory;
    private String                           targetPackage;
    private String                           tableClassPrefix;
    private String                           tableClassSuffix;
    private String                           recordClassPrefix;
    private String                           recordClassSuffix;
    private String                           scheme;

    private boolean                          instanceFields;

    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------

    @Override
    public void setInstanceFields(boolean instanceFields) {
        this.instanceFields = instanceFields;
    }

    @Override
    public void setMetaClassPrefix(String prefix) {
        this.tableClassPrefix = prefix;
    }

    @Override
    public void setMetaClassSuffix(String suffix) {
        this.tableClassSuffix = suffix;
    }

    @Override
    public void setRecordClassPrefix(String prefix) {
        this.recordClassPrefix = prefix;
    }

    @Override
    public void setRecordClassSuffix(String suffix) {
        this.recordClassSuffix = suffix;
    }

    @Override
    public void setMemberScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String getTargetDirectory() {
        return targetDirectory;
    }

    @Override
    public void setTargetDirectory(String directory) {
        this.targetDirectory = directory;
    }

    @Override
    public String getTargetPackage() {
        return targetPackage;
    }

    @Override
    public void setTargetPackage(String packageName) {
        this.targetPackage = packageName;
    }

    // -------------------------------------------------------------------------
    // Strategy methods
    // -------------------------------------------------------------------------

    @Override
    public String getFileName(Definition definition) {
        return getJavaClassName(definition) + ".java";
    }

    @Override
    public String getFileName(Definition definition, String suffix) {
        return getJavaClassName(definition) + suffix + ".java";
    }

    @Override
    public File getFile(Definition definition) {
        return getFile(definition, "");
    }

    @Override
    public File getFile(Definition definition, String suffix) {
        String dir = getTargetDirectory();
        String pkg = getJavaPackageName(definition, suffix).replaceAll("\\.", "/");
        return new File(dir + "/" + pkg, getFileName(definition, suffix));
    }

    @Override
    public String getJavaIdentifier(Definition definition) {
        return GenerationUtil.convertToJavaIdentifier(definition.getName());
    }

    @Override
    public String getJavaIdentifierUC(Definition definition) {
        String identifier = getJavaIdentifier(definition).toUpperCase();

        // Columns, Attributes, Parameters
        if (definition instanceof ColumnDefinition ||
            definition instanceof AttributeDefinition) {

            TypedElementDefinition<?> e = (TypedElementDefinition<?>) definition;

            if (identifier.equals(getJavaIdentifierUC(e.getContainer()))) {
                return identifier + "_";
            }
        }

        return identifier;
    }

    @Override
    public String getFullJavaIdentifierUC(Definition definition) {
        StringBuilder sb = new StringBuilder();

        // Columns
        if (definition instanceof ColumnDefinition) {
            TypedElementDefinition<?> e = (TypedElementDefinition<?>) definition;

            if (instanceFields) {
                sb.append(getFullJavaIdentifierUC(e.getContainer()));
            }
            else {
                sb.append(getFullJavaClassName(e.getContainer()));
            }
        }

        // Attributes, Parameters
        else if (definition instanceof TypedElementDefinition) {
            TypedElementDefinition<?> e = (TypedElementDefinition<?>) definition;
            sb.append(getFullJavaClassName(e.getContainer()));
        }

        // Table, UDT, Schema, etc
        else {
            sb.append(getFullJavaClassName(definition));
        }

        sb.append(".");
        sb.append(getJavaIdentifierUC(definition));

        return sb.toString();
    }

    @Override
    public String getJavaSetterName(Definition definition) {
        return "set" + disambiguateMethod(definition, getJavaClassName(definition));
    }

    @Override
    public String getJavaGetterName(Definition definition) {
        return "get" + disambiguateMethod(definition, getJavaClassName(definition));
    }

    /**
     * [#182] Method name disambiguation is important to avoid name clashes due
     * to pre-existing getters / setters in super classes
     */
    private String disambiguateMethod(Definition definition, String javaClassName) {
        Set<String> reserved = null;

        if (definition instanceof AttributeDefinition) {
            reserved = reservedColumns(UDTRecordImpl.class);
        }
        else if (definition instanceof ColumnDefinition) {
            if (((ColumnDefinition) definition).getContainer().getMainUniqueKey() != null) {
                reserved = reservedColumns(UpdatableRecordImpl.class);
            }
            else {
                reserved = reservedColumns(TableRecordImpl.class);
            }
        }

        if (reserved != null && reserved.contains(javaClassName)) {
            return javaClassName + "_";
        }

        return javaClassName;
    }


    /**
     * [#182] Find all column names that are reserved because of the extended
     * class hierarchy of a generated class
     */
    private Set<String> reservedColumns(Class<?> clazz) {
        if (clazz == null) {
            return Collections.emptySet();
        }

        Set<String> result = reservedColumns.get(clazz);

        if (result == null) {
            result = new HashSet<String>();
            reservedColumns.put(clazz, result);

            // Recurse up in class hierarchy
            result.addAll(reservedColumns(clazz.getSuperclass()));
            for (Class<?> c : clazz.getInterfaces()) {
                result.addAll(reservedColumns(c));
            }

            for (Method m : clazz.getDeclaredMethods()) {
                String name = m.getName();

                if (name.startsWith("get") && m.getParameterTypes().length == 0) {
                    result.add(name.substring(3));
                }
            }
        }

        return result;
    }

    @Override
    public String getJavaClassName(Definition definition) {
        return getJavaClassName(definition, "");
    }

    @Override
    public String getJavaClassName(Definition definition, String suffix) {
        return getJavaClassName0(definition, suffix);
    }

    @Override
    public String getJavaPackageName(Definition definition) {
        return getJavaPackageName(definition, "");
    }

    @Override
    public String getJavaPackageName(Definition definition, String suffix) {
        StringBuilder sb = new StringBuilder();

        sb.append(getTargetPackage());

        if (!StringUtils.isBlank(getSubPackage(definition))) {
            sb.append(".");
            sb.append(getSubPackage(definition));
        }

        if ("Record".equals(suffix)) {
            sb.append(".records");
        }

        return sb.toString();
    }

    @Override
    public String getJavaClassNameLC(Definition definition) {
        return getJavaClassNameLC(definition, "");
    }

    @Override
    public String getJavaClassNameLC(Definition definition, String suffix) {
        String result = getJavaClassName0(definition, suffix);

        return result.substring(0, 1).toLowerCase() + result.substring(1);
    }

    private String getJavaClassName0(Definition definition, String suffix) {
        StringBuilder result = new StringBuilder();

        String name = GenerationUtil.convertToJavaIdentifier(definition.getName());
        result.append(StringUtils.toCamelCase(name));

        if (!StringUtils.isEmpty(suffix)) {
            result.append(suffix);
        }

        if (!StringUtils.isBlank(definition.getOverload())) {
            result.append(definition.getOverload());
        }

        return result.toString();
    }

    @Override
    public String getFullJavaClassName(Definition definition) {
        return getFullJavaClassName(definition, "");
    }

    @Override
    public String getFullJavaClassName(Definition definition, String suffix) {
        StringBuilder sb = new StringBuilder();

        sb.append(getJavaPackageName(definition, suffix));
        sb.append(".");
        sb.append(getJavaClassName(definition, suffix));

        return sb.toString();
    }

    @Override
    public String getSubPackage(Definition definition) {
        if (definition instanceof MasterDataTableDefinition) {
            return "enums";
        }
        else if (definition instanceof TableDefinition) {
            return "tables";
        }

        // [#799] UDT's are also packages
        else if (definition instanceof UDTDefinition) {
            return "udt";
        }
        else if (definition instanceof PackageDefinition) {
            return "packages";
        }
        else if (definition instanceof RoutineDefinition) {
            RoutineDefinition routine = (RoutineDefinition) definition;

            if (routine.getPackage() instanceof UDTDefinition) {
                return "udt." + getJavaIdentifierUC(routine.getPackage()).toLowerCase();
            }
            else if (routine.getPackage() != null) {
                return "packages." + getJavaIdentifierUC(routine.getPackage()).toLowerCase();
            }
            else {
                return "routines";
            }
        }
        else if (definition instanceof EnumDefinition) {
            return "enums";
        }
        else if (definition instanceof ArrayDefinition) {
            return "udt";
        }

        // Default always to the main package
        return "";
    }


}
