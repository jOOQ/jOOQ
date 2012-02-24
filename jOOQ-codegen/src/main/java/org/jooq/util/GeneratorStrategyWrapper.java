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

import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

/**
 * A wrapper for generator strategies preventing some common compilation errors
 * resulting from badly generated source code
 *
 * @author Lukas Eder
 */
class GeneratorStrategyWrapper extends AbstractGeneratorStrategy {

    private static final JooqLogger log = JooqLogger.getLogger(GeneratorStrategyWrapper.class);

    final GeneratorStrategy         delegate;

    GeneratorStrategyWrapper(GeneratorStrategy delegate) {
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
        return convertToJavaIdentifier(delegate.getJavaIdentifier(definition));
    }

    @Override
    public String getJavaSetterName(Definition definition, Mode mode) {
        return convertToJavaIdentifier(delegate.getJavaSetterName(definition, mode));
    }

    @Override
    public String getJavaGetterName(Definition definition, Mode mode) {
        return convertToJavaIdentifier(delegate.getJavaGetterName(definition, mode));
    }

    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        return convertToJavaIdentifier(delegate.getJavaMethodName(definition, mode));
    }

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        String className = convertToJavaIdentifier(delegate.getJavaClassName(definition, mode));

        if (mode == Mode.FACTORY) {
            String alternative = convertToJavaIdentifier(delegate.getJavaClassName(definition, Mode.DEFAULT));

            if (className.equalsIgnoreCase(alternative)) {
                log.warn("Configuration problem", "Factory has the same name as Schema, forcing Factory suffix. Consider correcting your strategy");
                className += "Factory";
            }
        }

        return className;
    }

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
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
}
