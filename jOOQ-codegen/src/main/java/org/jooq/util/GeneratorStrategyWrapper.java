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

import static org.jooq.util.GenerationUtil.convertToJavaIdentifier;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.Record;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.TableRecordImpl;
import org.jooq.impl.UDTRecordImpl;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.tools.StringUtils;

/**
 * A wrapper for generator strategies preventing some common compilation errors
 * resulting from badly generated source code
 *
 * @author Lukas Eder
 */
class GeneratorStrategyWrapper extends AbstractGeneratorStrategy {

    private final Map<Class<?>, Set<String>> reservedColumns = new HashMap<Class<?>, Set<String>>();

    final Generator                          generator;
    final GeneratorStrategy                  delegate;

    GeneratorStrategyWrapper(Generator generator, GeneratorStrategy delegate) {
        this.generator = generator;
        this.delegate = delegate;
    }

    @Override
    public String getTargetDirectory() {
        return delegate.getTargetDirectory();
    }

    @Override
    public void setTargetDirectory(String directory) {
        delegate.setTargetDirectory(directory);
    }

    @Override
    public String getTargetPackage() {
        return delegate.getTargetPackage();
    }

    @Override
    public void setTargetPackage(String packageName) {
        delegate.setTargetPackage(packageName);
    }

    @Override
    public void setInstanceFields(boolean instanceFields) {
        delegate.setInstanceFields(instanceFields);
    }

    @Override
    public boolean getInstanceFields() {
        return delegate.getInstanceFields();
    }

    @Override
    public String getJavaIdentifier(Definition definition) {

        // [#1473] Identity identifiers should not be renamed by custom strategies
        if (definition instanceof IdentityDefinition) {
            return "IDENTITY_" + getJavaIdentifier(((IdentityDefinition) definition).getColumn().getContainer());
        }

        String identifier = convertToJavaIdentifier(delegate.getJavaIdentifier(definition));

        // [#1212] Don't trust custom strategies and disambiguate identifiers here
        if (definition instanceof ColumnDefinition ||
            definition instanceof AttributeDefinition) {

            TypedElementDefinition<?> e = (TypedElementDefinition<?>) definition;

            if (identifier.equals(getJavaIdentifier(e.getContainer()))) {
                return identifier + "_";
            }
        }

        return identifier;
    }

    @Override
    public String getJavaSetterName(Definition definition, Mode mode) {
        return disambiguateMethod(definition,
            convertToJavaIdentifier(delegate.getJavaSetterName(definition, mode)));
    }

    @Override
    public String getJavaGetterName(Definition definition, Mode mode) {
        return disambiguateMethod(definition,
            convertToJavaIdentifier(delegate.getJavaGetterName(definition, mode)));
    }

    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        String methodName;

        methodName = delegate.getJavaMethodName(definition, mode);
        methodName = overload(definition, mode, methodName);
        methodName = convertToJavaIdentifier(methodName);

        return disambiguateMethod(definition, methodName);
    }

    /**
     * [#1358] Add an overload suffix if needed
     */
    private String overload(Definition definition, Mode mode, String identifier) {
        if (!StringUtils.isBlank(definition.getOverload())) {
            identifier += getOverloadSuffix(definition, mode, definition.getOverload());
        }

        return identifier;
    }

    /**
     * [#182] Method name disambiguation is important to avoid name clashes due
     * to pre-existing getters / setters in super classes
     */
    private String disambiguateMethod(Definition definition, String method) {
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

        // [#1406] Disambiguate also procedure parameters
        else if (definition instanceof ParameterDefinition) {
            reserved = reservedColumns(AbstractRoutine.class);
        }

        if (reserved != null) {
            if (reserved.contains(method)) {
                return method + "_";
            }

            // If this is the setter, check if the getter needed disambiguation
            // This ensures that getters and setters have the same name
            if (method.startsWith("set")) {
                String base = method.substring(3);

                if (reserved.contains("get" + base) || reserved.contains("is" + base)) {
                    return method + "_";
                }
            }
        }

        return method;
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
                    result.add(name);
                }
            }
        }

        return result;
    }

    @Override
    public String getJavaClassExtends(Definition definition, Mode mode) {

        // [#1243] Only POJO mode can accept super classes
        return delegate.getJavaClassExtends(definition, mode);
    }

    @Override
    public List<String> getJavaClassImplements(Definition definition, Mode mode) {

        // [#1243] All generation modes can accept interfaces
        Set<String> result = new LinkedHashSet<String>(delegate.getJavaClassImplements(definition, mode));

        // [#1528] Generated interfaces (implemented by RECORD and POJO) are
        // always Serializable
        if (mode == Mode.INTERFACE) {
            result.add(Serializable.class.getName());
        }

        // [#1528] POJOs only implement Serializable if they don't inherit
        // Serializable from INTERFACE already
        else if (mode == Mode.POJO && !generator.generateInterfaces()) {
            result.add(Serializable.class.getName());
        }

        return new ArrayList<String>(result);
    }

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {

        // [#1150] Intercept Mode.RECORD calls for tables
        if (!generator.generateRecords() && mode == Mode.RECORD && definition instanceof TableDefinition) {
            return Record.class.getSimpleName();
        }

        String className;

        className = delegate.getJavaClassName(definition, mode);
        className = overload(definition, mode, className);
        className = convertToJavaIdentifier(className);

        return className;
    }

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {

        // [#1150] Intercept Mode.RECORD calls for tables
        if (!generator.generateRecords() && mode == Mode.RECORD && definition instanceof TableDefinition) {
            return Record.class.getPackage().getName();
        }

        String[] split = delegate.getJavaPackageName(definition, mode).split("\\.");

        for (int i = 0; i < split.length; i++) {
            split[i] = convertToJavaIdentifier(split[i]);
        }

        return StringUtils.join(split, ".");
    }

    @Override
    public String getJavaMemberName(Definition definition, Mode mode) {
        return convertToJavaIdentifier(delegate.getJavaMemberName(definition, mode));
    }

    @Override
    public String getOverloadSuffix(Definition definition, Mode mode, String overloadIndex) {
        return delegate.getOverloadSuffix(definition, mode, overloadIndex);
    }
}
