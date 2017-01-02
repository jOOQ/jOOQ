/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package org.jooq.util;

import java.sql.SQLException;
import java.util.ArrayList;
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
        List<Definition> result = new ArrayList<Definition>();
        result.addAll(getSchema().getDefinitionPath());

        if (pkg != null)
            result.add(pkg);

        result.add(this);
        return result;
    }

    protected void init() {
        inParameters = new ArrayList<ParameterDefinition>();
        outParameters = new ArrayList<ParameterDefinition>();
        allParameters = new ArrayList<ParameterDefinition>();

        try {
            if (returnValue != null) {
                addParameter(InOutDefinition.RETURN, returnValue);
            }

            init0();
        }
        catch (Exception e) {
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
    public /* non-final */ boolean isSQLUsable() {
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
