/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.test.all.converters;

import static java.util.stream.Collectors.toCollection;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jooq.Converter;

public class StringArrayToDateListConverter implements Converter<String[], List<Date>> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -8818832266588869677L;

    @Override
    public List<Date> from(String[] t) {
        if (t == null)
            return null;

        return Arrays.stream(t)
                     .map(Date::valueOf)
                     .collect(toCollection(ArrayList::new));
    }

    @Override
    public String[] to(List<Date> u) {
        if (u == null)
            return null;

        return u.stream()
                .map(Object::toString)
                .toArray(String[]::new);
    }

    @Override
    public Class<String[]> fromType() {
        return String[].class;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Class<List<Date>> toType() {
        return (Class) List.class;
    }
}
