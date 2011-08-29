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

import java.util.ArrayList;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.Package;
import org.jooq.Parameter;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.StoredProcedure;

/**
 * A common base class for stored procedures
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public abstract class AbstractStoredProcedure extends AbstractStoredObject implements StoredProcedure {

    private static final long        serialVersionUID = 750586958119197957L;

    private final List<Parameter<?>> allParameters;
    private final List<Parameter<?>> outParameters;
    private Parameter<?>             returnParameter;

    protected AbstractStoredProcedure(SQLDialect dialect, String name, Schema schema, Package pkg) {
        this(Factory.getNewFactory(dialect), name, schema, pkg);
    }

    protected AbstractStoredProcedure(Configuration configuration, String name, Schema schema, Package pkg) {
        super(configuration, name, schema, pkg);

        this.allParameters = new ArrayList<Parameter<?>>();
        this.outParameters = new ArrayList<Parameter<?>>();
    }

    @Override
    protected final List<Attachable> getAttachables1() {
        List<Attachable> result = new ArrayList<Attachable>();

        result.addAll(getAttachables(allParameters));
        result.addAll(getAttachables2());

        return result;
    }

    protected abstract List<Attachable> getAttachables2();

    @Override
    public final List<Parameter<?>> getOutParameters() {
        return outParameters;
    }

    protected final Parameter<?> getReturnParameter() {
        return returnParameter;
    }

    @Override
    public final List<Parameter<?>> getParameters() {
        return allParameters;
    }

    protected void addInOutParameter(Parameter<?> parameter) {
        super.addInParameter(parameter);
        outParameters.add(parameter);
        allParameters.add(parameter);
    }

    @Override
    protected void addInParameter(Parameter<?> parameter) {
        super.addInParameter(parameter);
        allParameters.add(parameter);
    }

    protected void addOutParameter(Parameter<?> parameter) {
        outParameters.add(parameter);
        allParameters.add(parameter);
    }

    protected void setReturnParameter(Parameter<?> parameter) {
        returnParameter = parameter;
        allParameters.add(parameter);
    }

    protected abstract <T> T getValue(Parameter<T> parameter);
}
