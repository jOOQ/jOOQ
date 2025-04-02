/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import java.sql.SQLException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.tools.StringUtils;

/**
 * A stub {@link BindContext} that acts as a collector of {@link Param}
 * {@link QueryPart}'s
 *
 * @author Lukas Eder
 */
final class ParamCollector extends AbstractBindContext {

    final Map<String, Param<?>>         resultFlat = new LinkedHashMap<>();
    final Map<String, List<Param<?>>>   result     = new LinkedHashMap<>();
    final List<Entry<String, Param<?>>> resultList = new ArrayList<>();

    private final boolean               includeInlinedParams;

    ParamCollector(Configuration configuration, boolean includeInlinedParams) {
        super(configuration, null, null);

        this.includeInlinedParams = includeInlinedParams;
    }

    @Override
    protected final void bindInternal(QueryPartInternal internal) {
        if (internal instanceof AbstractParam<?> param) {

            // [#3131] Inlined parameters should not be returned in some contexts
            if (includeInlinedParams || !param.isInline(this)) {
                String i = String.valueOf(nextIndex());
                String paramName = param.getParamName();

                if (StringUtils.isBlank(paramName)) {
                    resultFlat.put(i, param);
                    resultList.add(new SimpleImmutableEntry<>(i, param));
                    result(i).add(param);
                }
                else {
                    resultFlat.put(param.getParamName(), param);
                    resultList.add(new SimpleImmutableEntry<>(param.getParamName(), param));
                    result(param.getParamName()).add(param);
                }
            }
        }
        else {
            super.bindInternal(internal);
        }
    }

    private final List<Param<?>> result(String key) {
        return result.computeIfAbsent(key, k -> new ArrayList<>());
    }

    @Override
    protected final BindContext bindValue0(Object value, Field<?> field) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
