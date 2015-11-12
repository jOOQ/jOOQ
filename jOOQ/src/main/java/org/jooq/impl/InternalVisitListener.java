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

import static java.lang.Boolean.TRUE;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.Utils.DataKey.DATA_OVERRIDE_ALIASES_IN_ORDER_BY;
import static org.jooq.impl.Utils.DataKey.DATA_UNALIAS_ALIASES_IN_ORDER_BY;

import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.VisitContext;
import org.jooq.VisitListener;

/**
 * A {@link VisitListener} used by jOOQ internally, to implement some useful
 * features.
 * <p>
 * Features implemented are:
 * <h3>[#2790] Keep a locally scoped data map, scoped for the current subquery</h3>
 * <p>
 * Sometimes, it is useful to have some information only available while
 * visiting QueryParts in the same context of the current subquery, e.g. when
 * communicating between SELECT and WINDOW clause, as is required to emulate
 * [#531].
 * </p>
 *
 * @author Lukas Eder
 */
class InternalVisitListener extends DefaultVisitListener {

    /* [pro] xx
    xxxxxxxxx
    xxxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxx xxxx x
        xxxxxxxxx xxxx x xxxxxxxxxxxxxxxx

        xx xxxxx xxxx xxxxxxxxxxxxxx xxxx xx xxxxxx xxxx xxx xxx xxxxx xxxxxxxx xx xxx xxxxxx xxxxxx
        xx xxxxx xxxxxxxxxx xxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x

            xx xxxxxxx xxxxxxxx xxx xxxxxx xxxxx xx xxxx x xxxxxxxxx xxxxx xx xxxxxxxxx
            xx xx xxx xxxxxx xxxxxx
            xxxxxxxx xxxxxx x xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

            xx xxxxx xxxxxxx xxx xxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xx xxxxxxx xx xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                xxxxxxxxxx xxxxxxxxxxxxxx x xxxxxxxxxxxx xxxxxxxxxx
                xxxxxxxxxx xxxxxxxxxxxxx x xxxxxxxxxxxx xxxxxxxxxx

                xxx xxxx x x xx x x xxxxxxxxxxxxxxxxxxxxxx xxxx x
                    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    x
                x
            x
        x
    x
    xx [/pro] */
}
