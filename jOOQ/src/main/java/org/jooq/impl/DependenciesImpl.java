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

import static java.util.Collections.emptySet;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.Dependencies;
import org.jooq.Meta;
import org.jooq.Queries;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableOptions;
// ...
import org.jooq.tools.JooqLogger;

/**
 * @author Lukas Eder
 */
final class DependenciesImpl implements Dependencies {

























































        return emptySet();
    }

    @Override
    public final Set<Table<?>> viewTablesTransitive(Table<?> view) {
        return viewTablesTransitive.computeIfAbsent(view, v -> {
            Set<Table<?>> result = new LinkedHashSet<>();















            return result;
        });
    }















































}

