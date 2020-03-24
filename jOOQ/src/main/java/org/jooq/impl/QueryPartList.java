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

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jooq.QueryPart;

/**
 * @author Lukas Eder
 */
class QueryPartList<T extends QueryPart> extends QueryPartListView<T> implements List<T> {

    private static final long serialVersionUID = -2936922742534009564L;

    QueryPartList() {
        this((Collection<T>) null);
    }

    QueryPartList(T[] wrappedList) {
        this(asList(wrappedList));
    }

    QueryPartList(Collection<? extends T> wrappedList) {
        super(new ArrayList<>());

        // [#4664] Don't allocate the backing array if not necessary!
        if (wrappedList != null && !wrappedList.isEmpty())
            addAll(wrappedList);
    }
}
