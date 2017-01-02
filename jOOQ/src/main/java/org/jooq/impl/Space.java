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
package org.jooq.impl;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.QueryPart;

final class Space extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -4239524454814412161L;

    private final Field<Integer> count;

    Space(Field<Integer> count) {
        super("space", SQLDataType.VARCHAR, count);

        this.count = count;
    }

    @Override
    final QueryPart getFunction0(Configuration configuration) {
        switch (configuration.family()) {








            case DERBY:
            case FIREBIRD:
            case HSQLDB:
            case POSTGRES:
            case SQLITE:
                return DSL.repeat(DSL.inline(" "), count);









            case CUBRID:
            case MARIADB:
            case MYSQL:
            case H2:
            default:
                return DSL.field("{space}({0})", getDataType(), count);
        }
    }

}
