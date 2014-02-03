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
package org.jooq;

// ...
// ...

import java.util.Collection;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * This type is used for the Oracle <code>PIVOT</code> clause DSL API, pivoting
 * {@link Table} objects to new tables.
 *
 * @author Lukas Eder
 */
@State(
    name = "PivotInStep"
)
public interface PivotInStep<T> {

    /* [pro] xx
    xxx
     x xxxxxxx xxx xxxxxxxxxx xxxxxx xxx xxxxxxxx
     x
     x xxxxxx xxxxxx xxx xxxxxxxx xxxxxx
     x xxxxxxx x xxx xxxxxxx xxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxx xx
    xxxxxxxxxxxxx xxxxxxx xxxxxxxx

    xxx
     x xxxxxxx xxx xxxxxxxxxx xxxxxx xxx xxxxxxxx
     x xxx
     x xxxx xxxxxx xx xxxxxxxxx xxxx xxxxxxxxx xx xxxxxx xxxxxxxxxxxxxxxxxxx
     x xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxx xxxxxxxxx xxx
     x xxxxxx xxxxxxxxxxxxxxx xxxxxxxx
     x
     x xxxxxx xxxxxx xxx xxxxxxxx xxxxxx
     x xxxxxxx x xxx xxxxxxx xxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxx xx
    xxxxxxxxxxxx
        xxxx x xxxxx
        xxxx x xxxxxxxxx
        xx x xxxxxxxxxxxx
    x
    xxxxxxxxxxxxx xxxxxxxxxxxxxx xxxxxxxx

    xxx
     x xxxxxxx xxx xxxxxxxxxx xxxxxx xxx xxxxxxxx
     x xxx
     x xxxx xxxxxx xx xxxxxxxxx xxxx xxxxxxxxx xx xxxxxx xxxxxxxxxxxxxxxxxxx
     x xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxx xxxxxxxxx xxx
     x xxxxxx xxxxxxxxxxxxxxx xxxxxxxx
     x
     x xxxxxx xxxxxx xxx xxxxxxxx xxxxxx
     x xxxxxxx x xxx xxxxxxx xxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxx xx
    xxxxxxxxxxxxx xxxxxxxxxxxxxxx xxxxxxx xxxxxxxxx xxxxxxxx
    xx [/pro] */
}
