/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.util;

import java.util.ArrayList;
import java.util.List;

import org.jooq.tools.StringUtils;
import org.jooq.util.oracle.OracleQueueDefinition;

/**
 * The default naming strategy for the {@link JavaGenerator}
 *
 * @author Lukas Eder
 */
public class DefaultGeneratorStrategy extends AbstractGeneratorStrategy {

    private String  targetDirectory;
    private String  targetPackage;
    private boolean instanceFields = true;

    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------

    @Override
    public final void setInstanceFields(boolean instanceFields) {
        this.instanceFields = instanceFields;
    }

    @Override
    public final boolean getInstanceFields() {
        return instanceFields;
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
    public String getJavaIdentifier(Definition definition) {
        return definition.getOutputName().toUpperCase();
    }

    @Override
    public String getJavaSetterName(Definition definition, Mode mode) {
        return "set" + getJavaClassName0(definition, Mode.DEFAULT);
    }

    @Override
    public String getJavaGetterName(Definition definition, Mode mode) {
        return "get" + getJavaClassName0(definition, Mode.DEFAULT);
    }

    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        return getJavaClassName0LC(definition, Mode.DEFAULT);
    }

    @Override
    public String getJavaClassExtends(Definition definition, Mode mode) {
        return null;
    }

    @Override
    public List<String> getJavaClassImplements(Definition definition, Mode mode) {
        return new ArrayList<String>();
    }

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        return getJavaClassName0(definition, mode);
    }

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        StringBuilder sb = new StringBuilder();

        sb.append(getTargetPackage());

        // [#282] In multi-schema setups, the schema name goes into the package
        if (definition.getDatabase().getSchemata().size() > 1) {
            sb.append(".");
            sb.append(getJavaIdentifier(definition.getSchema()).toLowerCase());
        }

        // Some definitions have their dedicated subpackages, e.g. "tables", "routines"
        if (!StringUtils.isBlank(getSubPackage(definition))) {
            sb.append(".");
            sb.append(getSubPackage(definition));
        }

        // Record are yet in another subpackage
        if (mode == Mode.RECORD) {
            sb.append(".records");
        }

        // POJOs too
        else if (mode == Mode.POJO) {
            sb.append(".pojos");
        }

        // DAOs too
        else if (mode == Mode.DAO) {
            sb.append(".daos");
        }

        // Interfaces too
        else if (mode == Mode.INTERFACE) {
            sb.append(".interfaces");
        }

        /* [pro] */
        else if (definition instanceof OracleQueueDefinition) {
            sb.append(".aq");
        }
        /* [/pro] */

        return sb.toString();
    }

    @Override
    public String getJavaMemberName(Definition definition, Mode mode) {
        return getJavaClassName0LC(definition, mode);
    }

    private String getJavaClassName0LC(Definition definition, Mode mode) {
        String result = getJavaClassName0(definition, mode);
        return result.substring(0, 1).toLowerCase() + result.substring(1);
    }

    private String getJavaClassName0(Definition definition, Mode mode) {
        StringBuilder result = new StringBuilder();

        // [#4562] Some characters should be treated like underscore
        result.append(StringUtils.toCamelCase(
            definition.getOutputName()
                      .replace(' ', '_')
                      .replace('-', '_')
                      .replace('.', '_')
        ));

        if (mode == Mode.RECORD) {
            result.append("Record");
        }
        else if (mode == Mode.DAO) {
            result.append("Dao");
        }
        else if (mode == Mode.INTERFACE) {
            result.insert(0, "I");
        }

        return result.toString();
    }

    private final String getSubPackage(Definition definition) {
        if (definition instanceof TableDefinition) {
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
                return "udt." + getJavaIdentifier(routine.getPackage()).toLowerCase();
            }
            else if (routine.getPackage() != null) {
                return "packages." + getJavaIdentifier(routine.getPackage()).toLowerCase();
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

    @Override
    public String getOverloadSuffix(Definition definition, Mode mode, String overloadIndex) {
        return overloadIndex;
    }
}
