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

package org.jooq.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Package;
import org.jooq.Parameter;
import org.jooq.Schema;
import org.jooq.StoredObject;

/**
 * @author Lukas Eder
 */
abstract class AbstractStoredObject extends AbstractSchemaProviderQueryPart implements StoredObject {

    private static final long                 serialVersionUID = 5478305057107861491L;

    private final Package                     pkg;
    private final List<Parameter<?>>          inParameters;
    private final Map<Parameter<?>, Field<?>> inValues;
    private boolean                           overloaded;

    final AttachableImpl                      attachable;

    AbstractStoredObject(Configuration configuration, String name, Schema schema, Package pkg) {
        super(name, schema);

        this.attachable = new AttachableImpl(this, configuration);
        this.pkg = pkg;
        this.inParameters = new ArrayList<Parameter<?>>();
        this.inValues = new HashMap<Parameter<?>, Field<?>>();
    }

    @Override
    public final void attach(Configuration configuration) {
        attachable.attach(configuration);
    }

    @Override
    public final Package getPackage() {
        return pkg;
    }

    protected final String toSQLQualifiedName(Configuration configuration) {
        StringBuilder sb = new StringBuilder();

        if (getMappedSchema(configuration, getSchema()) != null) {
            sb.append(internal(getMappedSchema(configuration, getSchema())).toSQLReference(configuration, false));
            sb.append(".");
        }

        if (getPackage() != null) {
            sb.append(internal(getPackage()).toSQLReference(configuration, false));
            sb.append(".");
        }

        sb.append(JooqUtil.toSQLLiteral(configuration, getName()));
        return sb.toString();
    }

    @Override
    protected final List<Attachable> getAttachables0() {
        List<Attachable> result = new ArrayList<Attachable>();

        result.addAll(getAttachables(pkg));
        result.addAll(getAttachables(inParameters));
        result.addAll(getAttachables(inValues.keySet()));
        result.addAll(getAttachables(inValues.values()));
        result.addAll(getAttachables1());

        return result;
    }

    protected abstract List<Attachable> getAttachables1();

    @Override
    @Deprecated
    public final int execute(Connection connection) throws SQLException {
        return execute(new Factory(connection, attachable.getDialect(), attachable.getSchemaMapping()));
    }


    @Override
    public int execute(Configuration configuration) throws SQLException {

        // Ensure that all depending Attachables are attached
        attach(configuration);
        return execute();
    }

    protected final Map<Parameter<?>, Field<?>> getInValues() {
        return inValues;
    }

    protected final void setNumber(Parameter<? extends Number> parameter, Number value) {
        setValue(parameter, TypeUtils.convert(value, parameter.getType()));
    }

    protected final void setNumber(Parameter<? extends Number> parameter, Field<? extends Number> value) {
        setField(parameter, value);
    }

    protected final void setValue(Parameter<?> parameter, Object value) {
        setField(parameter, val(value, parameter));
    }

    /*
     * #326 - Avoid overloading setValue()
     */
    protected final void setField(Parameter<?> parameter, Field<?> value) {
        // Be sure null is correctly represented as a null field
        if (value == null) {
            setField(parameter, this.val(null, parameter));
        }

        // Add the field to the in-values
        else {
            inValues.put(parameter, value);
        }
    }

    public final List<Parameter<?>> getInParameters() {
        return Collections.unmodifiableList(inParameters);
    }

    protected void addInParameter(Parameter<?> parameter) {
        inParameters.add(parameter);

        // IN parameters are initialised with null by default
        inValues.put(parameter, val(null, parameter));
    }

    protected final void setOverloaded(boolean overloaded) {
        this.overloaded = overloaded;
    }

    protected final boolean isOverloaded() {
        return overloaded;
    }
}
