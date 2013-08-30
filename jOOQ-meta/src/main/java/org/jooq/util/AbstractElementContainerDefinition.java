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

import static org.jooq.util.AbstractDatabase.fetchedSize;
import static org.jooq.util.AbstractDatabase.filterExcludeInclude;
import static org.jooq.util.AbstractDatabase.getDefinition;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

/**
 * A base implementation for element container definitions
 *
 * @author Lukas Eder
 */
public abstract class AbstractElementContainerDefinition<E extends TypedElementDefinition<?>>
extends AbstractDefinition {

    /**
     * Precision and scale for those dialects that don't formally provide that
     * information in a separate field
     */
    protected static final Pattern  PRECISION_SCALE = Pattern.compile("\\((\\d+)\\s*(?:,\\s*(\\d+))?\\)");
    private static final JooqLogger log             = JooqLogger.getLogger(AbstractElementContainerDefinition.class);

    private List<E>                 elements;

    public AbstractElementContainerDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema.getDatabase(), schema, name, comment);
    }

    @Override
    public final List<Definition> getDefinitionPath() {
        return Arrays.<Definition>asList(getSchema(), this);
    }

    protected final List<E> getElements() {
        if (elements == null) {
            elements = new ArrayList<E>();

            try {
                Database db = getDatabase();
                List<E> e = getElements0();

                // [#2603] Filter exclude / include also for table columns
                if (this instanceof TableDefinition && db.getIncludeExcludeColumns()) {
                    elements = filterExcludeInclude(e, db.getExcludes(), db.getIncludes());
                    log.info("Columns fetched", fetchedSize(e, elements));
                }
                else {
                    elements = e;
                }
            }
            catch (SQLException e) {
                log.error("Error while initialising type", e);
            }
        }

        return elements;
    }

    protected final E getElement(String name) {
        return getElement(name, false);
    }

    protected final E getElement(String name, boolean ignoreCase) {
        return getDefinition(getElements(), name, ignoreCase);
    }

    protected final E getElement(int index) {
        return getElements().get(index);
    }

    protected abstract List<E> getElements0() throws SQLException;

    protected Number parsePrecision(String typeName) {
        if (typeName.contains("(")) {
            Matcher m = PRECISION_SCALE.matcher(typeName);

            if (m.find()) {
                if (!StringUtils.isBlank(m.group(1))) {
                    return Integer.valueOf(m.group(1));
                }
            }
        }

        return 0;
    }

    protected Number parseScale(String typeName) {
        if (typeName.contains("(")) {
            Matcher m = PRECISION_SCALE.matcher(typeName);

            if (m.find()) {
                if (!StringUtils.isBlank(m.group(2))) {
                    return Integer.valueOf(m.group(2));
                }
            }
        }

        return 0;
    }

    protected boolean parseNotNull(String typeName) {
        return typeName.toUpperCase().contains("NOT NULL");
    }
}
