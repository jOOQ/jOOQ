/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.function;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Rollup extends AbstractFunction<Object> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -5820608758939548704L;

    Rollup(Field<?>... fields) {
        super("rollup", SQLDataType.OTHER, fields);
    }

    @Override
    final Field<Object> getFunction0(Configuration configuration) {
        switch (configuration.dialect()) {
            case CUBRID:
            case MARIADB:
            case MYSQL:
                return field("{0} {with rollup}", new QueryPartList<Field<?>>(getArguments()));

            default:
                return function("rollup", Object.class, getArguments());
        }
    }
}
