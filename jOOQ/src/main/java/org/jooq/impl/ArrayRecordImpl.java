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
package org.jooq.impl;

/* [pro] xx

xxxxxx xxxxxxxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxx

xxxxxx xxxxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxx

xxx
 x x xxxxxx xxxx xxxxx xxx xxxxxx xxxxx xxxxx
 x xxx
 x xxxx xxxx xx xxx xxxx xxxxxxxx xxx xxxxx xx xxx xxxxxxxxx xxxxxxxx
 x
 x xxxxxxx xxxxx xxxx
 xx
xxxxxx xxxxx xxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxxx xxxxxxxxxx xxxxxxxxxxxxxx x

    xxx
     x xxxxxxxxx xxx
     xx
    xxxxxxx xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxx

    xxxxxxx xxxxx xxxxxx      xxxxxxx
    xxxxxxx xxxxx xxxxxxxxxxx xxxxxxxxx
    xxxxxxx xxxxx xxxxxxxxxxx xxxxx
    xxxxxxx xxxxx xxxxxx      xxxxx
    xxxxxxx xxx               xxxxxx

    xxx
     x xxxxxx xx xxxxx xxxxx xxxxxx
     x
     x xxxxxxxxxxx x xxxxx x xxxxxxx x xxx xxx
     x             xxxxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxx
     x             xxxxxxxxxxx xxxxxxx
     xx
    xxxxxxxxxxxxxxxxxxxxxxxxxxx
    xxxxxxxxxxx
    xxxxxxxxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxx xxxxx xxxxxxxxxxx xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxx x
        xxxxxxxxxxxx xxxxx xxxxx xxxxxxxxxxxxx xxx xxxxx xxxxxx
    x

    xxx
     x xxxxxx xx xxxxx xxxxx xxxxxx
     x
     x xxxxxxxxxxx x xxxxx x xxxxxxx x xxx xxx
     x             xxxxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxx xxxxxxxxxxx
     x             xxxxxxxxxxx xxxxxxxx
     xx
    xxxxxxxxxxxxxxxxxxxxxxxxxxx
    xxxxxxxxxxx
    xxxxxxxxx xxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxx xxxxx xxxxxxxxxxx xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxx xxxxxxxxxxxx xx xxxxxxxxxx x
        xxxxxxxxxxxx xxxxx xxxxx xxxxxxxxxx xxxxxx
    x

    xxx
     x xxxxxx xx xxxxx xxxxx xxxxxx
     x
     x xxxxxxxxxxx x xxxxx x xxxxxxx x xxx xxx
     x             xxxxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxx xxxxxxxxxxx
     x             xxxxxxxxxxx xxxxxxxx
     xx
    xxxxxxxxxxxxxxxxxxxxxxxxxxx
    xxxxxxxxxxx
    xxxxxxxxx xxx xx xxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxx xxxxx xxxxxxxxxxx xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxx xxxxxxxxxx xx xxxxxxxx x
        xxxxxxxxxxxx xxxxx xxxxx xxxxxxxxxxxxx xxx xxxxx xxxxxxxxx
    x

    xxx
     x xxxxxx xx xxxxx xxxxx xxxxxx
     x
     x xxxxxxxxxxx x xxxxx x xxxxxxx x xxx xxx
     x             xxxxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxx xxxxxxxxxxx
     x             xxxxxxxxxxx xxxxxxxx
     xx
    xxxxxxxxxxxxxxxxxxxxxxxxxxx
    xxxxxxxxxxx
    xxxxxxxxx xxx xx xxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxx xxxxx xxxxxxxxxxx xxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxx xxxxxxxxxxxx xx xxxxxxxxxx xxxxxxxxxx xx xxxxxxxx x
        xxxxxxxxxxxx xxxxx xxxxx xxxxxxxxxx xxxxxxxxx
    x

    xxx
     x xxxxxx xx xxxxx xxxxx xxxxxx
     xx
    xxxxxxxxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxx xxxxx xxxxxxxxxxx xxxxx x
        xxxxxxxxxxxx xxxxx xxxxx xxxxxxxxxxxxx xxx xxxxx xxxxxx
    x

    xxx
     x xxxxxx xx xxxxx xxxxx xxxxxx
     xx
    xxxxxxxxx xxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxx xxxxx xxxxxxxxxxx xxxxx xxxxxxxxxxxx xx xxxxxxxxxx x
        xxxxxxxxxxxx xxxxx xxxxx xxxxxxxxxx xxxxxx
    x

    xxx
     x xxxxxx xx xxxxx xxxxx xxxxxx
     xx
    xxxxxxxxx xxx xx xxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxx xxxxx xxxxxxxxxxx xxxxx xxxxxxxxxx xx xxxxxxxx x
        xxxxxxxxxxxx xxxxx xxxxx xxxxxxxxxxxxx xxx xxxxx xxxxxxxxx
    x

    xxx
     x xxxxxx xx xxxxx xxxxx xxxxxx
     xx
    xxxxxxxxxxxxxxxxxxx xxxxxxxxxxx xx
    xxxxxxxxx xxx xx xxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxx xxxxx xxxxxxxxxxx xxxxx xxxxxxxxxxxx xx xxxxxxxxxx xxxxxxxxxx xx xxxxxxxx x
        xxxxxxxxxxxx

        xxxxxxxxxxx x xxxxxxx
        xxxxxxxxx x xxxxx
        xxxxxxxxxxxxx x xxxxxxxxx xx xxxx xx xxxxxxx xx xxxx
            x xxxxxxxxxxxxx xxxx
            x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxxxx

        xx xxxxx xxxx xxxx xxxxxxxxxxxxxx
        xxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    xx xxx xxxxxxxxxx xxx
    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    xxxxxxxxx
    xxxxx xxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx x
        xxxxxxxxxxxxxxxx xxxxxx x xxx xxxxxxxxxxxxxxxxxxxxxxxx

        xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxx xx xxxxxxx x xxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxx
            x
        x

        xxxxxx xxxxxxx
    x

    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    xx xxx xxxxxxxxxxx xxx
    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    xxxxxxxxx
    xxxxx x xxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxxxx
    x

    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    xxxxxxxxx
    xxxxxx xxxxx xxx xxxxx x
        xx xxxxxx xx xxxxx x
            xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxx
        x
        xxxx x
            xxxxxx xxxxxx
        x
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxxxx x
        xx xxxxxx xx xxxxx x
            xxxxxx xxxxxxxxxxxxxxxxxxxxxxxx
        x
        xxxx x
            xxxxxx xxxxxxxxxxxxxxxxxxxxx
        x
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxx xxxxxx x
        xxxxxxxxxx x xxxxxx
    x

    xxxxxxxxx
    xxxxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxxx xxxxxx xxxxxx xxxxxxxxxxxx x
        xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx
    x

    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    xxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxx xxxxxxx xx xxxxxxxxxxx x
        xx xxxxxxxxxxx xx xxxxx x
            xxxxx x xxxxx
        x
        xxxx x
            xxxxx x xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxx
        x
    x

    xxxxxxxxx
    xxxxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxxxxxxxx xxxxxxx xx xxxxx x
        xxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxx xxxxxx x
        xxxxxx xxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxx xxxxxxxxx x

        xx xxxxxxx xxxx xxxxxx xx xxxxxxxx xxx xxxx xx xxx xxxxx xxxxxxxxx
        xx xxxxxxx xx xxxxx x
            xxxxxx xxxxxxxxxxxxxxxx x xxx x xxxxx
        x

        xx xxxx xxxxxx xx xxxxxxx xxx xxxx xx xxxxx xxxxxxxxx xxxxxxxxxxxx xxx xxxxxx
        xxxx x
            xxxxxx xxxxx
        x
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxx xxxxxxxxxxxxx x
        xxxxxx xxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxx xxxxxxxxxxxxxx x
        xxxxxx xxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxxx xxxxxxxxxx x
        xxxxxxxxxxxxx xxxxxx x xxx xxxxxxxxxxxxxxxx
        xxxxxx xxxxxxxxx x xxx

        xxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxx

        xx xxxxxx xx xxxxx x
            xxx xx x x xxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxx

                xxxxxxxxx x xx xx
            x
        x

        xxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxx xxxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxx
    x
x
xx [/pro] */