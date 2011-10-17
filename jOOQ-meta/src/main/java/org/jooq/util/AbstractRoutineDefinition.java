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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jooq.impl.JooqLogger;

/**
 * @author Lukas Eder
 */
public abstract class AbstractRoutineDefinition extends AbstractDefinition implements RoutineDefinition {

    private static final JooqLogger     log               = JooqLogger.getLogger(AbstractRoutineDefinition.class);
    private static final String         INOUT             = "(?:(IN|OUT|INOUT)\\s+?)?";
    private static final String         PARAM_NAME        = "(?:(\\S+?)\\s+?)";
    private static final String         PARAM_TYPE        = "([^\\s\\(]+)(?:\\s*\\((\\d+)(?:\\s*,\\s*(\\d+))?\\))?";
    private static final String         PARAMETER         = "(" + INOUT + PARAM_NAME + PARAM_TYPE + ")";

    protected static final Pattern      PARAMETER_PATTERN = Pattern.compile(PARAMETER);
    protected static final Pattern      TYPE_PATTERN      = Pattern.compile(PARAM_TYPE);

    protected List<ParameterDefinition> inParameters;
    protected List<ParameterDefinition> outParameters;
    protected ParameterDefinition       returnValue;
    protected List<ParameterDefinition> allParameters;

    private final PackageDefinition     pkg;

    public AbstractRoutineDefinition(Database database, PackageDefinition pkg, String name, String comment, String overload) {
        super(database, name, comment, overload);

        this.pkg = pkg;
    }

    private void init() {
        inParameters = new ArrayList<ParameterDefinition>();
        outParameters = new ArrayList<ParameterDefinition>();
        allParameters = new ArrayList<ParameterDefinition>();

        try {
            if (returnValue != null) {
                addParameter(InOutDefinition.RETURN, returnValue);
            }

            init0();
        }
        catch (SQLException e) {
            log.error("Error while initialising routine", e);
        }
    }

    protected abstract void init0() throws SQLException;

    @Override
    public final PackageDefinition getPackage() {
        return pkg;
    }

    @Override
    public final List<ParameterDefinition> getInParameters() {
        if (inParameters == null) {
            init();
        }

        return inParameters;
    }

    @Override
    public final List<ParameterDefinition> getOutParameters() {
        if (outParameters == null) {
            init();
        }

        return outParameters;
    }

    @Override
    public final List<ParameterDefinition> getAllParameters() {
        if (allParameters == null) {
            init();
        }

        return allParameters;
    }

    @Override
    public final ParameterDefinition getReturnValue() {
        if (allParameters == null) {
            init();
        }

        return returnValue;
    }

    @Override
    public final DataTypeDefinition getReturnType() {
        if (getReturnValue() != null) {
            return getReturnValue().getType();
        }
        else {
            return new DefaultDataTypeDefinition(getDatabase(), "unknown", 0, 0);
        }
    }

    @Override
    public final boolean isSQLUsable() {
        return getReturnValue() != null && getOutParameters().isEmpty();
    }

    protected final void addParameter(InOutDefinition inOut, ParameterDefinition parameter) {
        allParameters.add(parameter);

        switch (inOut) {
        case IN:
            inParameters.add(parameter);
            break;
        case OUT:
            outParameters.add(parameter);
            break;
        case INOUT:
            inParameters.add(parameter);
            outParameters.add(parameter);
            break;
        case RETURN:
            returnValue = parameter;
            break;
        }
    }
}
