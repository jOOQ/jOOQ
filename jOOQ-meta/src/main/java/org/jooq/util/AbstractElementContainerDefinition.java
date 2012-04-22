/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
                elements = getElements0();
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
        return AbstractDatabase.getDefinition(getElements(), name, ignoreCase);
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
