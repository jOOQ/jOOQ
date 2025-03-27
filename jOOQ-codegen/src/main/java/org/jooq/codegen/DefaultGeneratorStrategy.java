/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
 *
 *
 *
 */
package org.jooq.codegen;

import static java.util.Arrays.asList;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.codegen.GenerationUtil.convertToIdentifier;
import static org.jooq.codegen.Language.KOTLIN;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// ...
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.CatalogImpl;
import org.jooq.impl.DAOImpl;
import org.jooq.impl.EmbeddableRecordImpl;
import org.jooq.impl.SchemaImpl;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;
import org.jooq.impl.UDTImpl;
import org.jooq.impl.UDTPathTableFieldImpl;
import org.jooq.impl.UDTRecordImpl;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.AttributeDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.ConstraintDefinition;
import org.jooq.meta.Definition;
import org.jooq.meta.DomainDefinition;
import org.jooq.meta.EmbeddableDefinition;
import org.jooq.meta.EnumDefinition;
import org.jooq.meta.EnumLiteralDefinition;
import org.jooq.meta.ForeignKeyDefinition;
import org.jooq.meta.IdentityDefinition;
import org.jooq.meta.IndexDefinition;
import org.jooq.meta.InverseForeignKeyDefinition;
import org.jooq.meta.ManyToManyKeyDefinition;
import org.jooq.meta.PackageDefinition;
import org.jooq.meta.ParameterDefinition;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.SequenceDefinition;
// ...
import org.jooq.meta.SyntheticDaoDefinition;
import org.jooq.meta.TableDefinition;
// ...
import org.jooq.meta.UDTDefinition;
import org.jooq.meta.UniqueKeyDefinition;
// ...
// ...
import org.jooq.tools.StringUtils;

/**
 * The default naming strategy for the {@link JavaGenerator}
 *
 * @author Lukas Eder
 */
public class DefaultGeneratorStrategy extends AbstractGeneratorStrategy {

    String   targetDirectory;
    String   targetPackage;
    Locale   targetLocale                  = Locale.getDefault();
    Language targetLanguage                = Language.JAVA;
    boolean  instanceFields                = true;
    boolean  javaBeansGettersAndSetters    = false;
    boolean  useTableNameForUnambiguousFKs = true;

    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------

    @Override
    public void setInstanceFields(boolean instanceFields) {
        this.instanceFields = instanceFields;
    }

    @Override
    public boolean getInstanceFields() {
        return instanceFields;
    }

    @Override
    public void setJavaBeansGettersAndSetters(boolean javaBeansGettersAndSetters) {
        this.javaBeansGettersAndSetters = javaBeansGettersAndSetters;
    }

    @Override
    public boolean getJavaBeansGettersAndSetters() {
        return javaBeansGettersAndSetters;
    }

    @Override
    public void setUseTableNameForUnambiguousFKs(boolean useTableNameForUnambiguousFKs) {
        this.useTableNameForUnambiguousFKs = useTableNameForUnambiguousFKs;
    }

    @Override
    public boolean getUseTableNameForUnambiguousFKs() {
        return useTableNameForUnambiguousFKs;
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

    @Override
    public Locale getTargetLocale() {
        return targetLocale;
    }

    @Override
    public void setTargetLocale(Locale targetLocale) {
        this.targetLocale = targetLocale;
    }

    @Override
    public Language getTargetLanguage() {
        return targetLanguage;
    }

    @Override
    public void setTargetLanguage(Language targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    // -------------------------------------------------------------------------
    // Strategy methods
    // -------------------------------------------------------------------------

    @Override
    public String getGlobalNamesFileHeader(Definition container, Class<? extends Definition> objectType) {
        return "This file is generated by jOOQ.";
    }

    @Override
    public String getGlobalReferencesFileHeader(Definition container, Class<? extends Definition> objectType) {
        return "This file is generated by jOOQ.";
    }

    @Override
    public String getFileHeader(Definition definition, Mode mode) {
        return "This file is generated by jOOQ.";
    }

    @Override
    public String getJavaEnumLiteral(EnumDefinition definition, String literal) {
        return convertToIdentifier(literal, getTargetLanguage());
    }

    @Override
    public String getJavaIdentifier(Definition definition) {
        String identifier = getFixedJavaIdentifier(definition);

        if (identifier != null)
            return identifier;

        // [#6307] Some databases work with per-table namespacing for indexes, not per-schema namespacing.
        //         In order to have non-ambiguous identifiers, we need to include the table name.
        else if (definition instanceof IndexDefinition && asList(MARIADB, MYSQL).contains(definition.getDatabase().getDialect().family()))
            return ((IndexDefinition) definition).getTable().getOutputName().toUpperCase(targetLocale) + "_" + definition.getOutputName().toUpperCase(targetLocale);

        // [#5538] [#11286] [#12118] The same is true for unique keys, which are really indexes
        else if (definition instanceof UniqueKeyDefinition && asList(SQLITE).contains(definition.getDatabase().getDialect().family()))
            return ((UniqueKeyDefinition) definition).getTable().getOutputName().toUpperCase(targetLocale) + "__" + definition.getOutputName().toUpperCase(targetLocale);

        // [#9758] And then also for foreign keys
        else if (definition instanceof ForeignKeyDefinition && asList(POSTGRES, SQLITE, YUGABYTEDB).contains(definition.getDatabase().getDialect().family()))
            return ((ForeignKeyDefinition) definition).getTable().getOutputName().toUpperCase(targetLocale) + "__" + definition.getOutputName().toUpperCase(targetLocale);

        else if (definition instanceof InverseForeignKeyDefinition && asList(POSTGRES, SQLITE, YUGABYTEDB).contains(definition.getDatabase().getDialect().family()))
            return ((InverseForeignKeyDefinition) definition).getReferencingTable().getOutputName().toUpperCase(targetLocale) + "__" + definition.getOutputName().toUpperCase(targetLocale);

        // [#10481] Embeddables have a defining name (class name) and a referencing name (identifier name, member name).
        else if (definition instanceof EmbeddableDefinition)
            return ((EmbeddableDefinition) definition).getReferencingOutputName().toUpperCase(targetLocale);






        else
            return definition.getOutputName().toUpperCase(targetLocale);
    }
















    String getterSetterSuffix(Definition definition) {

        // [#5354] Please forgive me but this is how it works.
        if (javaBeansGettersAndSetters) {
            String name = getJavaMemberName(definition);

            if (Character.isUpperCase(name.charAt(0)))
                return name;
            if (name.length() > 1 && Character.isUpperCase(name.charAt(1)))
                return name;

            char[] chars = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        }

        // [#10481] Embeddables have a defining name (class name) and a referencing name (identifier name, member name).
        if (definition instanceof EmbeddableDefinition)
            return getJavaClassName0(((EmbeddableDefinition) definition).getReferencingOutputName(), Mode.DEFAULT);
        else
            return getJavaClassName0(definition, Mode.DEFAULT);
    }

    @Override
    public String getJavaSetterName(Definition definition, Mode mode) {
        return "set" + getterSetterSuffix(definition);
    }

    @Override
    public boolean getJavaSetterOverride(Definition definition, Mode mode) {
        return false;
    }

    @Override
    public String getJavaGetterName(Definition definition, Mode mode) {
        return "get" + getterSetterSuffix(definition);
    }

    @Override
    public boolean getJavaGetterOverride(Definition definition, Mode mode) {
        return false;
    }

    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        // [#7148] If table A references table B only once, then B is the ideal name
        //         for the implicit JOIN path. Otherwise, fall back to the foreign key name
        if (getUseTableNameForUnambiguousFKs()) {
            if (definition instanceof ForeignKeyDefinition fk) {
                TableDefinition referenced = fk.getReferencedTable();

                if (fk.getTable().getForeignKeys(referenced).size() == 1)
                    return getJavaMethodName(referenced, mode);
            }
            else if (definition instanceof InverseForeignKeyDefinition fk) {
                TableDefinition referencing = fk.getReferencingTable();

                if (fk.getTable().getInverseForeignKeys(referencing).size() == 1)
                    return getJavaMethodName(referencing, mode);
            }
            else if (definition instanceof ManyToManyKeyDefinition k) {
                TableDefinition t1 = k.getForeignKey1().getReferencedTable();
                TableDefinition t2 = k.getForeignKey2().getReferencedTable();

                if (t1.getManyToManyKeys(t2).size() == 1)
                    return getJavaMethodName(t2, mode);
                else
                    return getJavaClassName0LC(k.getForeignKey2(), Mode.DEFAULT);
            }
        }

        return getJavaClassName0LC(definition, Mode.DEFAULT);
    }

    @Override
    public boolean getJavaMethodOverride(Definition definition, Mode mode) {
        return false;
    }

    @Override
    public String getGlobalNamesJavaClassExtends(Definition container, Class<? extends Definition> objectType) {
        return null;
    }

    @Override
    public String getGlobalReferencesJavaClassExtends(Definition container, Class<? extends Definition> objectType) {
        return null;
    }

    @Override
    public String getJavaClassExtends(Definition definition, Mode mode) {
        if (definition instanceof CatalogDefinition) {
            return CatalogImpl.class.getName();
        }
        else if (definition instanceof SchemaDefinition) {
            return SchemaImpl.class.getName();
        }
        else if (definition instanceof RoutineDefinition) {
            return AbstractRoutine.class.getName();
        }
        else if (definition instanceof TableDefinition t) {
            switch (mode) {
                case DAO:     return DAOImpl.class.getName();
                case RECORD:  return (t.getPrimaryKey() != null ? UpdatableRecordImpl.class : TableRecordImpl.class).getName();
                case DEFAULT: return TableImpl.class.getName();
            }
        }
        else if (definition instanceof UDTDefinition) {
            switch (mode) {
                case PATH:    return UDTPathTableFieldImpl.class.getName();
                case RECORD:  return UDTRecordImpl.class.getName();
                case DEFAULT: return UDTImpl.class.getName();
            }
        }
        else if (definition instanceof EmbeddableDefinition) {
            switch (mode) {
                case RECORD: return EmbeddableRecordImpl.class.getName();
            }
        }

        return null;
    }

    @Override
    public List<String> getGlobalNamesJavaClassImplements(Definition container, Class<? extends Definition> objectType) {
        return new ArrayList<>();
    }

    @Override
    public List<String> getGlobalReferencesJavaClassImplements(Definition container, Class<? extends Definition> objectType) {
        return new ArrayList<>();
    }

    @Override
    public List<String> getJavaClassImplements(Definition definition, Mode mode) {
        return new ArrayList<>();
    }

    @Override
    public String getGlobalNamesJavaClassName(Definition container, Class<? extends Definition> objectType) {
        if (ArrayDefinition.class.isAssignableFrom(objectType))
            return "ArrayNames";
        else if (AttributeDefinition.class.isAssignableFrom(objectType))
            return getJavaClassName(container) + "AttributeNames";
        else if (CatalogDefinition.class.isAssignableFrom(objectType))
            return "CatalogNames";
        else if (ColumnDefinition.class.isAssignableFrom(objectType))
            return getJavaClassName(container) + "ColumnNames";
        else if (DomainDefinition.class.isAssignableFrom(objectType))
            return "DomainNames";
        else if (EmbeddableDefinition.class.isAssignableFrom(objectType))
            return "EmbeddableNames";
        else if (EnumDefinition.class.isAssignableFrom(objectType))
            return "EnumNames";
        else if (EnumLiteralDefinition.class.isAssignableFrom(objectType))
            return getJavaClassName(container) + "LiteralNames";
        else if (ForeignKeyDefinition.class.isAssignableFrom(objectType))
            return "ForeignKeyNames";
        else if (IdentityDefinition.class.isAssignableFrom(objectType))
            return "IdentityNames";
        else if (IndexDefinition.class.isAssignableFrom(objectType))
            return "IndexNames";




        // [#799] UDTDefinition <: PackageDefinition!
        else if (UDTDefinition.class.isAssignableFrom(objectType))
            return "UDTNames";
        else if (PackageDefinition.class.isAssignableFrom(objectType))
            return "PackageNames";
        else if (ParameterDefinition.class.isAssignableFrom(objectType))
            return getJavaClassName(container) + "ParameterNames";




        else if (RoutineDefinition.class.isAssignableFrom(objectType))
            return "RoutineNames";
        else if (SchemaDefinition.class.isAssignableFrom(objectType))
            return "SchemaNames";
        else if (SequenceDefinition.class.isAssignableFrom(objectType))
            return "SequenceNames";
        else if (TableDefinition.class.isAssignableFrom(objectType))
            return "TableNames";






        else if (UniqueKeyDefinition.class.isAssignableFrom(objectType))
            return "UniqueKeyNames";
        else
            return "UnknownTypeNames";
    }
    @Override
    public String getGlobalReferencesJavaClassName(Definition container, Class<? extends Definition> objectType) {
        if (ArrayDefinition.class.isAssignableFrom(objectType))
            return "Arrays";
        else if (AttributeDefinition.class.isAssignableFrom(objectType))
            return getJavaClassName(container);
        else if (CatalogDefinition.class.isAssignableFrom(objectType))
            return getJavaClassName(container);
        else if (ConstraintDefinition.class.isAssignableFrom(objectType))
            return "Keys";
        else if (ColumnDefinition.class.isAssignableFrom(objectType))
            return getJavaClassName(container);
        else if (DomainDefinition.class.isAssignableFrom(objectType))
            return "Domains";
        else if (EmbeddableDefinition.class.isAssignableFrom(objectType))
            return "Embeddables";
        else if (EnumDefinition.class.isAssignableFrom(objectType))
            return "Enums";
        else if (IdentityDefinition.class.isAssignableFrom(objectType))
            return "Identities";
        else if (IndexDefinition.class.isAssignableFrom(objectType))
            return "Indexes";




        else if (PackageDefinition.class.isAssignableFrom(objectType))
            return "UDTs";




        else if (ParameterDefinition.class.isAssignableFrom(objectType))
            return getJavaClassName(container);
        else if (RoutineDefinition.class.isAssignableFrom(objectType))
            return "Routines";
        else if (SchemaDefinition.class.isAssignableFrom(objectType))
            return getJavaClassName(container);
        else if (SequenceDefinition.class.isAssignableFrom(objectType))
            return "Sequences";
        else if (TableDefinition.class.isAssignableFrom(objectType))
            return "Tables";






        else if (UDTDefinition.class.isAssignableFrom(objectType))
            return "UDTs";
        else
            return "UnknownTypes";
    }

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        String name = getFixedJavaClassName(definition);

        if (name != null)
            return name;
        else
            return getJavaClassName0(definition, mode);
    }

    @Override
    public String getGlobalNamesJavaPackageName(Definition container, Class<? extends Definition> objectType) {
        return getGlobalObjectJavaPackageName0(
            container,
            getGlobalReferencesJavaClassName(container, objectType),
            TableDefinition.class.isAssignableFrom(objectType)
            || RoutineDefinition.class.isAssignableFrom(objectType)
            || UDTDefinition.class.isAssignableFrom(objectType)

            || AttributeDefinition.class.isAssignableFrom(objectType)
            || ConstraintDefinition.class.isAssignableFrom(objectType)
            || ColumnDefinition.class.isAssignableFrom(objectType)
            || DomainDefinition.class.isAssignableFrom(objectType)
            || EnumDefinition.class.isAssignableFrom(objectType)
            || IndexDefinition.class.isAssignableFrom(objectType)
            || ParameterDefinition.class.isAssignableFrom(objectType)
            || SequenceDefinition.class.isAssignableFrom(objectType)



            ? "names"
            : ""
        );
    }

    @Override
    public String getGlobalReferencesJavaPackageName(Definition container, Class<? extends Definition> objectType) {
        return getGlobalObjectJavaPackageName0(
            container,
            getGlobalReferencesJavaClassName(container, objectType),
            TableDefinition.class.isAssignableFrom(objectType)
            || RoutineDefinition.class.isAssignableFrom(objectType)
            || UDTDefinition.class.isAssignableFrom(objectType)
            ? "references"
            : null
        );
    }

    private String getGlobalObjectJavaPackageName0(
        Definition container,
        String fileClassName,
        String kotlinSubpackage
    ) {
        String packageName = getJavaPackageName(container);

        // [#799] UDTDefinition <: PackageDefinition!
        if (container instanceof PackageDefinition && !(container instanceof UDTDefinition))
            packageName = packageName + "." + getJavaIdentifier(container).toLowerCase(targetLocale);

        if (targetLanguage == KOTLIN) {
            String className = fileClassName.toLowerCase(targetLocale);

            // Repeat the package name for global references to prevent collisions
            if (kotlinSubpackage != null)
                packageName = packageName + "." + className + "." + kotlinSubpackage;
            else
                packageName = packageName + "." + className;
        }

        return packageName;
    }

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        StringBuilder sb = new StringBuilder();

        sb.append(getTargetPackage());

        // [#2032] In multi-catalog setups, the catalog name goes into the package
        if (definition.getDatabase().getCatalogs().size() > 1) {
            sb.append(".");
            sb.append(getJavaIdentifier(definition.getCatalog()).toLowerCase(targetLocale));
        }

        if (!(definition instanceof CatalogDefinition)) {

            // [#282] In multi-schema setups, the schema name goes into the package
            if (definition.getDatabase().getSchemata().size() > 1) {
                sb.append(".");
                sb.append(getJavaIdentifier(definition.getSchema()).toLowerCase(targetLocale));
            }

            if (!(definition instanceof SchemaDefinition)) {

                // Some definitions have their dedicated subpackages, e.g. "tables", "routines"
                String subPackage = getSubPackage(definition, mode);
                if (!StringUtils.isBlank(subPackage)) {
                    sb.append(".");
                    sb.append(subPackage);
                }

                // Other subpackages
                if (mode == Mode.RECORD)
                    sb.append(".records");
                else if (mode == Mode.RECORD_TYPE)
                    sb.append(".recordtypes");
                else if (mode == Mode.POJO)
                    sb.append(".pojos");
                else if (mode == Mode.DAO)
                    sb.append(".daos");
                else if (mode == Mode.INTERFACE)
                    sb.append(".interfaces");





            }
        }

        return sb.toString();
    }

    @Override
    public String getJavaMemberName(Definition definition, Mode mode) {

        // [#10481] Embeddables have a defining name (class name) and a referencing name (identifier name, member name).
        if (definition instanceof EmbeddableDefinition)
            return getJavaClassName0LC(((EmbeddableDefinition) definition).getReferencingOutputName(), Mode.DEFAULT);
        else
            return getJavaClassName0LC(definition, Mode.DEFAULT);
    }

    @Override
    public boolean getJavaMemberOverride(Definition definition, Mode mode) {
        return false;
    }

    private String getJavaClassName0LC(Definition definition, Mode mode) {
        String result = getJavaClassName0(definition, mode);
        return result.substring(0, 1).toLowerCase(targetLocale) + result.substring(1);
    }

    private String getJavaClassName0LC(String outputName, Mode mode) {
        String result = getJavaClassName0(outputName, mode);
        return result.substring(0, 1).toLowerCase(targetLocale) + result.substring(1);
    }

    private String getJavaClassName0(Definition definition, Mode mode) {
        return getJavaClassName0(definition.getOutputName(), mode);
    }

    private String getJavaClassName0(String outputName, Mode mode) {
        StringBuilder result = new StringBuilder();

        // [#4562] Some characters should be treated like underscore
        result.append(toPascalCase(
            outputName.replace(' ', '_')
                      .replace('-', '_')
                      .replace('.', '_')
        ));

        if (mode == Mode.RECORD)
            result.append("Record");
        else if (mode == Mode.DAO)
            result.append("Dao");
        else if (mode == Mode.INTERFACE)
            result.insert(0, "I");
        else if (mode == Mode.RECORD_TYPE)
            result.append("RecordType");
        else if (mode == Mode.PATH)
            result.append("Path");

        return result.toString();
    }

    private String getSubPackage(Definition definition, Mode mode) {
        if (definition instanceof TableDefinition)
            return "tables";

        // [#2530] Embeddable types
        else if (definition instanceof EmbeddableDefinition)
            return "embeddables";

        // [#799] UDT's are also packages
        else if (definition instanceof UDTDefinition u) {

            // [#330] [#6529] A UDT inside of a package is a PL/SQL RECORD type
            if (u.getPackage() != null)
                return "packages." + getJavaIdentifier(u.getPackage()).toLowerCase(targetLocale) + ".udt";
            else if (mode == Mode.PATH)
                return "udt.paths";
            else
                return "udt";
        }
        else if (definition instanceof PackageDefinition)
            return "packages";
        else if (definition instanceof RoutineDefinition r) {
            if (r.getPackage() instanceof UDTDefinition)
                return "udt." + getJavaIdentifier(r.getPackage()).toLowerCase(targetLocale);
            else if (r.getPackage() != null)
                return "packages." + getJavaIdentifier(r.getPackage()).toLowerCase(targetLocale);
            else
                return "routines";
        }
        else if (definition instanceof EnumDefinition)
            return "enums";
        else if (definition instanceof DomainDefinition)
            return "domains";
        else if (definition instanceof ArrayDefinition a) {

            // [#7125] An array inside of a package is a PL/SQL TABLE type
            if (a.getPackage() != null)
                return "packages." + getJavaIdentifier(a.getPackage()).toLowerCase(targetLocale) + ".udt";
            else
                return "udt";
        }

        else if (definition instanceof SyntheticDaoDefinition)
            return "daos";

        // Default always to the main package
        return "";
    }

    @Override
    public String getOverloadSuffix(Definition definition, Mode mode, String overloadIndex) {
        return overloadIndex;
    }

    String toPascalCase(String name) {
        return StringUtils.toCamelCase(name);
    }

    static Case getCase(String name) {
        if (name.toUpperCase().equals(name))
            return Case.UPPER;
        else if (name.toLowerCase().equals(name))
            return Case.LOWER;
        else
            return Case.MIXED;
    }

    enum Case {
        UPPER,
        LOWER,
        MIXED
    }
}
