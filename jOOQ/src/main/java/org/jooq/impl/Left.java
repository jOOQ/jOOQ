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

import static org.jooq.impl.DSL.inline;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.QueryPart;

/**
 * @author Lukas Eder
 */
final class Left extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID = 2200760781944082146L;

    private Field<String>           field;
    private Field<? extends Number> length;

    Left(Field<String> field, Field<? extends Number> length) {
        super("left", field.getDataType());

        this.field = field;
        this.length = length;
    }

    @Override
    final QueryPart getFunction0(Configuration configuration) {
        switch (configuration.family()) {



            case DERBY:
            case SQLITE:
                return DSL.substring(field, inline(1), length);







            case CUBRID:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            default:
                return DSL.field("{left}({0}, {1})", field, length);
        }
    }
}
