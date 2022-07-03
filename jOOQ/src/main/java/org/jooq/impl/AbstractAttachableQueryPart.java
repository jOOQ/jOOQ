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
 *
 *
 *
 */

package org.jooq.impl;

import static org.jooq.conf.SettingsTools.getParamType;

import java.util.List;
import java.util.Map;

import org.jooq.AttachableQueryPart;
import org.jooq.Configuration;
import org.jooq.Param;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
abstract class AbstractAttachableQueryPart extends AbstractQueryPart implements AttachableQueryPart {

    private Configuration configuration;

    AbstractAttachableQueryPart(Configuration configuration) {
        this.configuration = configuration;
    }

    // -------------------------------------------------------------------------
    // The Attachable and Attachable internal API
    // -------------------------------------------------------------------------

    @Override
    public final void attach(Configuration c) {
        configuration = c;
    }

    @Override
    public final void detach() {
        attach(null);
    }

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    @NotNull
    final Configuration configurationOrDefault() {
        return Tools.configuration(this);
    }

    @NotNull
    final Configuration configurationOrThrow() {
        return Tools.configurationOrThrow(this);
    }

    // -------------------------------------------------------------------------
    // The AttachableQueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final List<Object> getBindValues() {
        return create().extractBindValues(this);
    }

    @Override
    public final Map<String, Param<?>> getParams() {
        return create().extractParams(this);
    }

    @Override
    public final Param<?> getParam(String name) {
        return create().extractParam(this, name);
    }

    @Override
    public final String getSQL() {
        return getSQL(getParamType(Tools.settings(configuration())));
    }

    @Override
    public final String getSQL(ParamType paramType) {
        return getSQL(paramType, configuration());
    }

    @Override
    public final String getSQL(SQLDialect sqlDialect) {
        return getSQL(getParamType(Tools.settings(configuration())), sqlDialect);
    }

    @Override
    public final String getSQL(ParamType paramType, SQLDialect sqlDialect) {
        Configuration config = configuration();
        if (config instanceof DefaultConfiguration) {
            DefaultConfiguration defaultConfig = new DefaultConfiguration((DefaultConfiguration)config);
            defaultConfig.setSQLDialect(sqlDialect);
            config = defaultConfig;
        }
        return getSQL(paramType, config);
    }

    private String getSQL(ParamType paramType, Configuration configuration) {
        switch (paramType) {
            case INDEXED:
                return create(configuration).render(this);
            case INLINED:
                return create(configuration).renderInlined(this);
            case NAMED:
                return create(configuration).renderNamedParams(this);
            case NAMED_OR_INLINED:
                return create(configuration).renderNamedOrInlinedParams(this);
            case FORCE_INDEXED:
                return create(configuration).renderContext().paramType(paramType).visit(this).render();
        }

        throw new IllegalArgumentException("ParamType not supported: " + paramType);
    }
}
