/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.util;

import static org.jooq.util.GenerationUtil.convertToJavaIdentifier;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DAO;
import org.jooq.tools.StringUtils;

/**
 * The default naming strategy for the {@link JavaGenerator}
 *
 * @author Lukas Eder
 */
public class DefaultGeneratorStrategy extends AbstractGeneratorStrategy {

    private String                           targetDirectory;
    private String                           targetPackage;
    private boolean                          instanceFields;

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
        return GenerationUtil.convertToJavaIdentifier(definition.getOutputName().toUpperCase());
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
        List<String> result = new ArrayList<String>();

        if (mode == Mode.DAO) {
            TableDefinition table = (TableDefinition) definition;
            List<ColumnDefinition> keyColumns = table.getPrimaryKey().getKeyColumns();

            String name = DAO.class.getName();

            name += "<";
            name += getFullJavaClassName(table, Mode.POJO);
            name += ", ";
            name += keyColumns.size() == 1
                        ? "Void" // keyColumns.get(0).getType()
                        : "Void";
            name += ">";

            result.add(name);
        }

        return result;
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
            sb.append(convertToJavaIdentifier(definition.getSchema().getOutputName()).toLowerCase());
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

        String cc = StringUtils.toCamelCase(definition.getOutputName());
        result.append(GenerationUtil.convertToJavaIdentifier(cc));

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
