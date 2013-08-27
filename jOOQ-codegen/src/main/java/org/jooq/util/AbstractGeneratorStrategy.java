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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

        // Sequences
        else if (definition instanceof SequenceDefinition) {
            sb.append(getJavaPackageName(definition.getSchema()));
            sb.append(".Sequences");
        }

        // Attributes, Parameters
        else if (definition instanceof TypedElementDefinition) {
            TypedElementDefinition<?> e = (TypedElementDefinition<?>) definition;
            sb.append(getFullJavaClassName(e.getContainer()));
        }

        // Identities
        else if (definition instanceof IdentityDefinition) {
            sb.append(getJavaPackageName(definition.getSchema()));
            sb.append(".Keys");
        }

        // Unique Keys
        else if (definition instanceof UniqueKeyDefinition) {
            sb.append(getJavaPackageName(definition.getSchema()));
            sb.append(".Keys");
        }

        // Foreign Keys
        else if (definition instanceof ForeignKeyDefinition) {
            sb.append(getJavaPackageName(definition.getSchema()));
            sb.append(".Keys");
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

    // -------------------------------------------------------------------------
    // List methods
    // -------------------------------------------------------------------------

    @Override
    public final List<String> getJavaIdentifiers(Collection<? extends Definition> definitions) {
        List<String> result = new ArrayList<String>();

        for (Definition definition : nonNull(definitions)) {
            result.add(getJavaIdentifier(definition));
        }

        return result;
    }

    @Override
    public final List<String> getJavaIdentifiers(Definition... definitions) {
        return getJavaIdentifiers(Arrays.asList(definitions));
    }

    @Override
    public final List<String> getFullJavaIdentifiers(Collection<? extends Definition> definitions) {
        List<String> result = new ArrayList<String>();

        for (Definition definition : nonNull(definitions)) {
            result.add(getFullJavaIdentifier(definition));
        }

        return result;
    }

    @Override
    public final List<String> getFullJavaIdentifiers(Definition... definitions) {
        return getFullJavaIdentifiers(Arrays.asList(definitions));
    }

    private static final <T> List<T> nonNull(Collection<? extends T> collection) {
        List<T> result = new ArrayList<T>();

        for (T t : collection) {
            if (t != null) {
                result.add(t);
            }
        }

        return result;
    }
}