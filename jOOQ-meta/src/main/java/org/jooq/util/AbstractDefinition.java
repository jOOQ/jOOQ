/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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
