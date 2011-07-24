/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import java.sql.Connection;

import org.jooq.SQLDialect;
import org.jooq.impl.Factory;

/**
 * A base implementation for any type of definition.
 *
 * @author Lukas Eder
 */
public abstract class AbstractDefinition implements Definition {

    private final Database database;
    private final String   name;
    private final String   comment;
    private final String   overload;

    public AbstractDefinition(Database database, String name) {
        this(database, name, null);
    }

    public AbstractDefinition(Database database, String name, String comment) {
        this(database, name, comment, null);
    }

    public AbstractDefinition(Database database, String name, String comment, String overload) {
        this.database = database;
        this.name = name;
        this.comment = comment;
        this.overload = overload;
    }

    @Override
    public final String getOverload() {
        return overload;
    }

    @Override
    public final String getSchemaName() {
        return database.getSchemaName();
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    @Deprecated
    public final String getNameUC() {
        return name.toUpperCase();
    }

    @Override
    @Deprecated
    public final String getNameLC() {
        return name.toLowerCase();
    }

    @Override
    public final String getComment() {
        return comment;
    }

    @Override
    public String getQualifiedName() {
        return getName();
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
        if (obj instanceof Definition) {
            Definition that = (Definition) obj;
            return that.getQualifiedName().equals(getQualifiedName());
        }

        return false;
    }

    @Override
    public final int hashCode() {
        return getQualifiedName().hashCode();
    }

    protected final Factory create() {
        return database.create();
    }

    protected final SQLDialect getDialect() {
        return create().getDialect();
    }
}
