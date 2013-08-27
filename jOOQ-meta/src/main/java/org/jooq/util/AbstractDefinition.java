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

import java.sql.Connection;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;

/**
 * A base implementation for any type of definition.
 *
 * @author Lukas Eder
 */
public abstract class AbstractDefinition implements Definition {

    private final Database         database;
    private final SchemaDefinition schema;
    private final String           name;
    private final String           comment;
    private final String           overload;

    // [#2238] Some caches for strings that are heavy to calculate in large schemas
    private transient String       qualifiedInputName;
    private transient String       qualifiedOutputName;
    private transient Integer      hashCode;

    public AbstractDefinition(Database database, SchemaDefinition schema, String name) {
        this(database, schema, name, null);
    }

    public AbstractDefinition(Database database, SchemaDefinition schema, String name, String comment) {
        this(database, schema, name, comment, null);
    }

    public AbstractDefinition(Database database, SchemaDefinition schema, String name, String comment, String overload) {
        this.database = database;

        // The subclass constructor cannot pass "this" to the super constructor
        this.schema = (schema == null && this instanceof SchemaDefinition)
            ? (SchemaDefinition) this
            : schema;
        this.name = name;
        this.comment = comment;
        this.overload = overload;
    }

    @Override
    public final String getOverload() {
        return overload;
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
        return comment;
    }

    @Override
    public final String getQualifiedName() {
        return getQualifiedInputName();
    }

    /**
     * Subclasses may override this method
     *
     * {@inheritDoc}
     */
    @Override
    public final String getQualifiedInputName() {
        if (qualifiedInputName == null) {
            StringBuilder sb = new StringBuilder();

            String separator = "";
            for (Definition part : getDefinitionPath()) {
                if (part instanceof SchemaDefinition && ((SchemaDefinition) part).isDefaultSchema()) {
                    continue;
                }

                sb.append(separator);
                sb.append(part.getInputName());

                separator = ".";
            }

            qualifiedInputName = sb.toString();
        }

        return qualifiedInputName;
    }

    /**
     * Subclasses may override this method
     *
     * {@inheritDoc}
     */
    @Override
    public final String getQualifiedOutputName() {
        if (qualifiedOutputName == null) {
            StringBuilder sb = new StringBuilder();

            String separator = "";
            for (Definition part : getDefinitionPath()) {
                if (part instanceof SchemaDefinition && ((SchemaDefinition) part).isDefaultSchema()) {
                    continue;
                }

                sb.append(separator);
                sb.append(part.getOutputName());

                separator = ".";
            }

            qualifiedOutputName = sb.toString();
        }

        return qualifiedOutputName;
    }

    @Override
    public final Database getDatabase() {
        return database;
    }

    protected final Connection getConnection() {
        return database.getConnection();
    }

    @Override
    public final String toString() {
        return getQualifiedName();
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Definition) {
            Definition that = (Definition) obj;
            return that.getQualifiedName().equals(getQualifiedName());
        }

        return false;
    }

    @Override
    public final int hashCode() {
        if (hashCode == null) {
            hashCode = getQualifiedName().hashCode();
        }

        return hashCode;
    }

    protected final DSLContext create() {
        return database.create();
    }

    protected final SQLDialect getDialect() {
        return database.getDialect();
    }
}
