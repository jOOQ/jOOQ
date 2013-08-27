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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.jooq.tools.JooqLogger;

/**
 * @author Lukas Eder
 */
public abstract class AbstractRoutineDefinition extends AbstractDefinition implements RoutineDefinition {

    private static final JooqLogger     log               = JooqLogger.getLogger(AbstractRoutineDefinition.class);
    private static final String         INOUT             = "(?i:(IN|OUT|INOUT)\\s+?)?";
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
    private final boolean               aggregate;

    public AbstractRoutineDefinition(SchemaDefinition schema, PackageDefinition pkg, String name, String comment, String overload) {
        this(schema, pkg, name, comment, overload, false);
    }

    public AbstractRoutineDefinition(SchemaDefinition schema, PackageDefinition pkg, String name, String comment, String overload, boolean aggregate) {
        super(schema.getDatabase(), schema, name, comment, overload);

        this.pkg = pkg;
        this.aggregate = aggregate;
    }

    @Override
    public List<Definition> getDefinitionPath() {
        if (pkg != null) {
            return Arrays.<Definition>asList(getSchema(), pkg, this);
        }
        else {
            return Arrays.<Definition>asList(getSchema(), this);
        }
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
            return new DefaultDataTypeDefinition(getDatabase(), getSchema(), "unknown");
        }
    }

    @Override
    public final boolean isSQLUsable() {
        return getReturnValue() != null && getOutParameters().isEmpty();
    }

    @Override
    public final boolean isAggregate() {
        return aggregate;
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
