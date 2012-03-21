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
 * Common base class for convenience method abstraction
 *
 * @author Lukas Eder
 */
public abstract class AbstractGeneratorStrategy implements GeneratorStrategy {

    // -------------------------------------------------------------------------
    // Strategy methods
    // -------------------------------------------------------------------------

    @Override
    public final String getFileName(Definition definition) {
        return getFileName(definition, Mode.DEFAULT);
    }

    @Override
    public final String getFileName(Definition definition, Mode mode) {
        return getJavaClassName(definition, mode) + ".java";
    }

    @Override
    public final File getFile(Definition definition) {
        return getFile(definition, Mode.DEFAULT);
    }

    @Override
    public final File getFile(Definition definition, Mode mode) {
        String dir = getTargetDirectory();
        String pkg = getJavaPackageName(definition, mode).replaceAll("\\.", "/");
        return new File(dir + "/" + pkg, getFileName(definition, mode));
    }

    @Override
    public final String getFullJavaIdentifier(Definition definition) {
        StringBuilder sb = new StringBuilder();

        // Columns
        if (definition instanceof ColumnDefinition) {
            TypedElementDefinition<?> e = (TypedElementDefinition<?>) definition;

            if (getInstanceFields()) {
                sb.append(getFullJavaIdentifier(e.getContainer()));
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
        sb.append(getJavaIdentifier(definition));

        return sb.toString();
    }

    @Override
    public final String getJavaSetterName(Definition definition) {
        return getJavaSetterName(definition, Mode.DEFAULT);
    }

    @Override
    public final String getJavaGetterName(Definition definition) {
        return getJavaGetterName(definition, Mode.DEFAULT);
    }

    @Override
    public final String getJavaMethodName(Definition definition) {
        return getJavaMethodName(definition, Mode.DEFAULT);
    }

    @Override
    public final String getJavaClassExtends(Definition definition) {
        return getJavaClassExtends(definition, Mode.DEFAULT);
    }

    @Override
    public final List<String> getJavaClassImplements(Definition definition) {
        return getJavaClassImplements(definition, Mode.DEFAULT);
    }

    @Override
    public final String getJavaClassName(Definition definition) {
        return getJavaClassName(definition, Mode.DEFAULT);
    }

    @Override
    public final String getJavaPackageName(Definition definition) {
        return getJavaPackageName(definition, Mode.DEFAULT);
    }

    @Override
    public final String getJavaMemberName(Definition definition) {
        return getJavaMemberName(definition, Mode.DEFAULT);
    }

    @Override
    public final String getFullJavaClassName(Definition definition) {
        return getFullJavaClassName(definition, Mode.DEFAULT);
    }

    @Override
    public final String getFullJavaClassName(Definition definition, Mode mode) {
        StringBuilder sb = new StringBuilder();

        sb.append(getJavaPackageName(definition, mode));
        sb.append(".");
        sb.append(getJavaClassName(definition, mode));

        return sb.toString();
    }

}
