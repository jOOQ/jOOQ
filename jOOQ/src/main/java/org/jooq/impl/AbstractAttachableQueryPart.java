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
        switch (paramType) {
            case INDEXED:
                return create().render(this);
            case INLINED:
                return create().renderInlined(this);
            case NAMED:
                return create().renderNamedParams(this);
            case NAMED_OR_INLINED:
                return create().renderNamedOrInlinedParams(this);
            case FORCE_INDEXED:
                return create().renderContext().paramType(paramType).visit(this).render();
        }

        throw new IllegalArgumentException("ParamType not supported: " + paramType);
    }
}
