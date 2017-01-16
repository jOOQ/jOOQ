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
    public final File getFileRoot() {
        String dir = getTargetDirectory();
        String pkg = getTargetPackage().replaceAll("\\.", "/");
        return new File(dir + "/" + pkg);
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
    public final File getFile(String fileName) {
        String dir = getTargetDirectory();
        String pkg = getTargetPackage().replaceAll("\\.", "/");
        return new File(dir + "/" + pkg, fileName);
    }

    @Override
    public final String getFileHeader(Definition definition) {
        return getFileHeader(definition, Mode.DEFAULT);
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

    /**
     * [#4168] [#5783] Some identifiers must not be modified by custom strategies.
     */
    final String getFixedJavaIdentifier(Definition definition) {

        // [#1473] Identity identifiers should not be renamed by custom strategies
        if (definition instanceof IdentityDefinition)
            return "IDENTITY_" + getJavaIdentifier(((IdentityDefinition) definition).getColumn().getContainer());

        // [#2032] Intercept default Catalog
        else if (definition instanceof CatalogDefinition && ((CatalogDefinition) definition).isDefaultCatalog())
            return "DEFAULT_CATALOG";

        // [#2089] Intercept default schema
        else if (definition instanceof SchemaDefinition && ((SchemaDefinition) definition).isDefaultSchema())
            return "DEFAULT_SCHEMA";

        else
            return null;
    }

    /**
     * [#4168] [#5783] Some class names must not be modified by custom strategies.
     */
    final String getFixedJavaClassName(Definition definition) {

        // [#2032] Intercept default catalog
        if (definition instanceof CatalogDefinition && ((CatalogDefinition) definition).isDefaultCatalog())
            return "DefaultCatalog";

        // [#2089] Intercept default schema
        else if (definition instanceof SchemaDefinition && ((SchemaDefinition) definition).isDefaultSchema())
            return "DefaultSchema";

        else
            return null;
    }
}