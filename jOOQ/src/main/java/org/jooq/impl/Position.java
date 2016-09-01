/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
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

import static org.jooq.impl.DSL.one;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Position extends AbstractFunction<Integer> {

    private static final long             serialVersionUID = 3544690069533526544L;

    private final Field<String>           search;
    private final Field<String>           in;
    private final Field<? extends Number> startIndex;

    Position(Field<String> search, Field<String> in) {
        this(search, in, null);
    }

    Position(Field<String> search, Field<String> in, Field<? extends Number> startIndex) {
        super("position", SQLDataType.INTEGER, search, in, startIndex);

        this.search = search;
        this.in = in;
        this.startIndex = startIndex;
    }

    @Override
    final Field<Integer> getFunction0(Configuration configuration) {
        if (startIndex != null)
            switch (configuration.family()) {





                default:
                    return DSL.position(DSL.substring(in, startIndex), search).add(startIndex).sub(one());
            }
        else
            switch (configuration.family()) {



                case DERBY:
                    return DSL.field("{locate}({0}, {1})", SQLDataType.INTEGER, search, in);














                case SQLITE:
                    return DSL.field("{instr}({0}, {1})", SQLDataType.INTEGER, in, search);

                default:
                    return DSL.field("{position}({0} {in} {1})", SQLDataType.INTEGER, search, in);
            }
    }
}
