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

import org.jooq.impl.JooqLogger;

/**
 * @author Lukas Eder
 */
public abstract class AbstractProcedureDefinition extends AbstractCallableDefinition implements ProcedureDefinition {

    private static final JooqLogger   log = JooqLogger.getLogger(AbstractProcedureDefinition.class);

    private List<ParameterDefinition> inParameters;
    private List<ParameterDefinition> outParameters;
    private ParameterDefinition       returnValue;
    private List<ParameterDefinition> allParameters;

    public AbstractProcedureDefinition(Database database, PackageDefinition pkg, String name, String comment, String overload) {
        super(database, pkg, name, comment, overload);
    }

    private void init() {
        inParameters = new ArrayList<ParameterDefinition>();
        outParameters = new ArrayList<ParameterDefinition>();
        allParameters = new ArrayList<ParameterDefinition>();

        try {
            init0();
        }
        catch (SQLException e) {
            log.error("Error while initialising procedure", e);
        }
    }

    protected abstract void init0() throws SQLException;

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
