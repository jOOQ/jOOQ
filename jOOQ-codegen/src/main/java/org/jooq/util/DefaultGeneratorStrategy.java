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

import org.jooq.impl.StringUtils;

/**
 * The default naming strategy for the {@link DefaultGenerator}
 *
 * @author Lukas Eder
 */
@SuppressWarnings("unused")
public class DefaultGeneratorStrategy implements GeneratorStrategy {

    private String targetDirectory;
    private String targetPackage;
    private String tableClassPrefix;
    private String tableClassSuffix;
    private String recordClassPrefix;
    private String recordClassSuffix;
    private String scheme;

    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------

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
    public final String getTargetDirectory() {
        return targetDirectory;
    }

    @Override
    public final void setTargetDirectory(String directory) {
        this.targetDirectory = directory;
    }

    @Override
    public final String getTargetPackage() {
        return targetPackage;
    }

    @Override
    public final void setTargetPackage(String packageName) {
        this.targetPackage = packageName;
    }

    // -------------------------------------------------------------------------
    // Strategy methods
    // -------------------------------------------------------------------------

    @Override
    public final String getFileName(Definition definition) {
        return getJavaClassName(definition) + ".java";
    }

    @Override
    public final String getFileName(Definition definition, String suffix) {
        return getJavaClassName(definition) + suffix + ".java";
    }

    @Override
    public final File getFile(Definition definition) {
        return getFile(definition, "");
    }

    @Override
    public final File getFile(Definition definition, String suffix) {
        String dir = getTargetDirectory();
        String pkg = getJavaPackageName(definition, suffix).replaceAll("\\.", "/");
        return new File(dir + "/" + pkg, getFileName(definition, suffix));
    }

    @Override
    public final String getJavaIdentifier(Definition definition) {
        return GenerationUtil.convertToJavaIdentifier(definition.getName());
    }

    @Override
    public final String getJavaIdentifierUC(Definition definition) {
        return getJavaIdentifier(definition).toUpperCase();
    }

    @Override
    public final String getFullJavaIdentifierUC(Definition definition) {
        StringBuilder sb = new StringBuilder();

        // Columns, Attributes, Parameters
        if (definition instanceof TypedElementDefinition) {
            sb.append(getFullJavaClassName(((TypedElementDefinition<?>) definition).getContainer()));
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
    public final String getJavaClassName(Definition definition) {
        return getJavaClassName(definition, "");
    }

    @Override
    public final String getJavaClassName(Definition definition, String suffix) {
        return getJavaClassName0(definition, suffix);
    }

    @Override
    public final String getJavaPackageName(Definition definition) {
        return getJavaPackageName(definition, "");
    }

    @Override
    public final String getJavaPackageName(Definition definition, String suffix) {
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
    public final String getJavaClassNameLC(Definition definition) {
        return getJavaClassNameLC(definition, "");
    }

    @Override
    public final String getJavaClassNameLC(Definition definition, String suffix) {
        String result = getJavaClassName0(definition, suffix);

        return result.substring(0, 1).toLowerCase() + result.substring(1);
    }

    private final String getJavaClassName0(Definition definition, String suffix) {
        StringBuilder result = new StringBuilder();

        String name = GenerationUtil.convertToJavaIdentifier(definition.getName());
        for (String word : name.split("_")) {

            // Uppercase first letter of a word
            if (word.length() > 0) {

                // #82 - If a word starts with a digit, prevail the
                // underscore to prevent naming clashes
                if (Character.isDigit(word.charAt(0))) {
                    result.append("_");
                }

                result.append(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase());
            }

            // If no letter exists, prevail the underscore (e.g. leading
            // underscores)
            else {
                result.append("_");
            }
        }

        if (!StringUtils.isEmpty(suffix)) {
            result.append(suffix);
        }

        if (!StringUtils.isBlank(definition.getOverload())) {
            result.append(definition.getOverload());
        }

        return result.toString();
    }

    @Override
    public final String getFullJavaClassName(Definition definition) {
        return getFullJavaClassName(definition, "");
    }

    @Override
    public final String getFullJavaClassName(Definition definition, String suffix) {
        StringBuilder sb = new StringBuilder();

        sb.append(getJavaPackageName(definition, suffix));
        sb.append(".");
        sb.append(getJavaClassName(definition, suffix));

        return sb.toString();
    }

    @Override
    public final String getSubPackage(Definition definition) {
        if (definition instanceof MasterDataTableDefinition) {
            return "enums";
        }
        else if (definition instanceof TableDefinition) {
            return "tables";
        }
        else if (definition instanceof PackageDefinition) {
            return "packages";
        }
        else if (definition instanceof CallableDefinition) {
            CallableDefinition callable = (CallableDefinition) definition;

            if (callable.getPackage() != null) {
                return "packages." + getJavaIdentifierUC(callable.getPackage()).toLowerCase();
            }
            else if (definition instanceof FunctionDefinition) {
                return "functions";
            }
            else if (definition instanceof ProcedureDefinition) {
                return "procedures";
            }
        }
        else if (definition instanceof EnumDefinition) {
            return "enums";
        }
        else if (definition instanceof ArrayDefinition) {
            return "udt";
        }
        else if (definition instanceof UDTDefinition) {
            return "udt";
        }

        // Default always to the main package
        return "";
    }


}
