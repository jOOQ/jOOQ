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

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Ascii extends AbstractFunction<Integer> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    private final Field<?>                string;

    Ascii(Field<?> string) {
        super("ascii", SQLDataType.INTEGER, string);

        this.string = string;
    }

    @Override
    final Field<Integer> getFunction0(Configuration configuration) {
        switch (configuration.family()) {
            /* [pro] xx
            xxxx xxxxxxx
                xxxxxx xxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxx

            xx [/pro] */
            case FIREBIRD:
                return field("{ascii_val}({0})", SQLDataType.INTEGER, string);

            // TODO [#862] [#864] emulate this for some dialects
            /* [pro] xx
            xxxx xxxxxxx
            xx [/pro] */
            case DERBY:
            case SQLITE:

            default:
                return field("{ascii}({0})", SQLDataType.INTEGER, string);
        }
    }
}
