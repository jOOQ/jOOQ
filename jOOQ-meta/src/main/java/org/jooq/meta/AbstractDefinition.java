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

package org.jooq.meta;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.name;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.StringUtils.isEmpty;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Name;
// ...
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.meta.jaxb.CommentType;
import org.jooq.tools.StringUtils;

/**
 * A base implementation for any type of definition.
 *
 * @author Lukas Eder
 */
public abstract class AbstractDefinition implements Definition {

    private final Database          database;
    private final SchemaDefinition  schema;
    private final PackageDefinition pkg;
    private final String            name;
    private final String            schemaComment;



    private final String            overload;
    private final String            source;

    // [#2238] Some caches for strings that are heavy to calculate in large schemas
    private transient String        qualifiedInputName;
    private transient String        qualifiedOutputName;
    private transient Name          qualifiedInputNamePart;
    private transient Name          qualifiedOutputNamePart;
    private transient Integer       hashCode;
    private transient List<String>  partiallyQualifiedNames;

    public AbstractDefinition(Database database, SchemaDefinition schema, String name) {
        this(database, schema, name, null);
    }

    public AbstractDefinition(Database database, SchemaDefinition schema, String name, String comment) {
        this(database, schema, name, comment, null);
    }

    public AbstractDefinition(Database database, SchemaDefinition schema, String name, String comment, String overload) {
        this(database, schema, null, name, comment, overload);
    }

    public AbstractDefinition(Database database, SchemaDefinition schema, PackageDefinition pkg, String name, String comment, String overload) {
        this(database, schema, pkg, name, comment, overload, null);
    }

    public AbstractDefinition(Database database, SchemaDefinition schema, PackageDefinition pkg, String name, String comment, String overload, String source) {
        this.database = database;

        // The subclass constructor cannot pass "this" to the super constructor
        this.schema = (schema == null && this instanceof SchemaDefinition)
            ? (SchemaDefinition) this
            : schema;
        this.pkg = pkg;
        this.name = name;
        this.overload = overload;
        this.source = source;
        this.schemaComment = comment;
    }

    @Override
    public List<Definition> getDefinitionPath() {
        List<Definition> result = new ArrayList<>(getSchema().getDefinitionPath());

        if (getPackage() != null)
            result.add(getPackage());

        result.add(this);
        return result;
    }

    @Override
    public final PackageDefinition getPackage() {
        return pkg;
    }

    @Override
    public final String getOverload() {
        return overload;
    }

    @Override
    public /* non-final */ CatalogDefinition getCatalog() {
        return getSchema().getCatalog();
    }

    @Override
    public final SchemaDefinition getSchema() {
        return schema;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String getInputName() {
        return name;
    }

    /**
     * Subclasses may override this method
     *
     * {@inheritDoc}
     */
    @Override
    public String getOutputName() {
        return getInputName();
    }

    @Override
    public final String getComment() {



















        return getSchemaComment();
    }

























    private final String getSchemaComment() {
        return !isEmpty(schemaComment) ? schemaComment : defaultIfNull(getDatabase().getComments().get(this), "");
    }

    @Override
    public final String getQualifiedName() {
        return getQualifiedInputName();
    }

    @Override
    public final String getQualifiedInputName() {
        if (qualifiedInputName == null) {
            StringBuilder sb = new StringBuilder();

            String separator = "";
            for (Definition part : getDefinitionPath()) {

                // [#6855] Some databases (e.g. SQLite) might not have a schema
                String input = part.getInputName();

                if (!StringUtils.isEmpty(input)) {
                    sb.append(separator);
                    sb.append(part.getInputName());

                    separator = ".";
                }
            }

            qualifiedInputName = sb.toString();
        }

        return qualifiedInputName;
    }

    @Override
    public final String getQualifiedOutputName() {
        if (qualifiedOutputName == null) {
            StringBuilder sb = new StringBuilder();

            String separator = "";
            for (Definition part : getDefinitionPath()) {
                if (part instanceof CatalogDefinition && ((CatalogDefinition) part).isDefaultCatalog())
                    continue;
                else if (part instanceof SchemaDefinition && ((SchemaDefinition) part).isDefaultSchema())
                    continue;

                sb.append(separator);
                sb.append(part.getOutputName());

                separator = ".";
            }

            qualifiedOutputName = sb.toString();
        }

        return qualifiedOutputName;
    }

    @Override
    public final Name getQualifiedNamePart() {
        return getQualifiedInputNamePart();
    }

    @Override
    public final Name getQualifiedInputNamePart() {
        if (qualifiedInputNamePart == null) {
            List<String> list = new ArrayList<>();

            for (Definition part : getDefinitionPath())
                list.add(part.getInputName());

            qualifiedInputNamePart = name(list);
        }

        return qualifiedInputNamePart;
    }

    @Override
    public final Name getQualifiedOutputNamePart() {
        if (qualifiedOutputNamePart == null) {
            List<String> list = new ArrayList<>();

            for (Definition part : getDefinitionPath()) {
                if (part instanceof CatalogDefinition && ((CatalogDefinition) part).isDefaultCatalog())
                    continue;
                else if (part instanceof SchemaDefinition && ((SchemaDefinition) part).isDefaultSchema())
                    continue;

                list.add(part.getOutputName());
            }

            qualifiedOutputNamePart = name(list);
        }

        return qualifiedOutputNamePart;
    }

    // [#16567] Avoid re-creating rendering contexts every time we render a name for matching purposes.
    private static final DSLContext CTX = new DefaultConfiguration().dsl();

    @Override
    public final List<String> getPartiallyQualifiedNames() {
        if (partiallyQualifiedNames == null) {
            partiallyQualifiedNames = new ArrayList<>();

            List<Name> parts = Arrays.asList(getQualifiedNamePart().parts());

            for (int i = parts.size() - 1; i >= 0; i--)
                partiallyQualifiedNames.add(CTX.render(DSL.name(parts.subList(i, parts.size()).toArray(new Name[0])).unquotedName()));
        }

        return partiallyQualifiedNames;
    }

    @Override
    public final Database getDatabase() {
        return database;
    }

    protected final Connection getConnection() {
        return database.getConnection();
    }

    @Override
    public String toString() {
        return getQualifiedName();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Definition) {
            Definition that = (Definition) obj;
            return that.getQualifiedName().equals(getQualifiedName());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (hashCode == null)
            hashCode = getQualifiedName().hashCode();

        return hashCode;
    }

    protected final DSLContext create() {
        return database.create();
    }

    protected final DSLContext create(boolean muteExceptions) {
        if (database instanceof AbstractDatabase)
            return ((AbstractDatabase) database).create(muteExceptions);
        else
            return database.create();
    }

    protected final SQLDialect getDialect() {
        return database.getDialect();
    }

    @Override
    public final String getSource() {
        return source != null ? source : getDatabase().getSources().get(this);
    }
}
