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

xxxxxx xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxx xxxxxxxxxxxxxxxxxxxxxx

xxxxxx xxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

xxx
 x x xxxxxxxxx xxxxx xxxxxx xxxxxxxxxxxxxxx
 x
 x xxxxxxx xxxxx xxxx
 xx
xxxxx xxxxxxxxxxxxxxxx xxxxxxx xxxxxxx xx
xxxxxxx xxxxxxxxxxxxxxxx
xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xx x

    xxx
     x xxxxxxxxx xxx
     xx
    xxxxxxx xxxxxx xxxxx xxxx   xxxxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxx

    xxxxxxx xxxxx xxxxxxxx      xxxxxx
    xxxxxxx xxxxx xxxxxxxxx     xxxxx
    xxxxxxx xxxxx xxxxxxxx      xxxxx
    xxxxxxx xxxxx xxxxxxxxx     xxxxxxxxx
    xxxxxxx xxxxxxxxx           xxxxxxxxx

    xxxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxx xxxxx xxxxxxxx xxxxxxxxx xxxxxxxxxxxxx xxxxx x
        xxxxxxxxxxxxxxxxxxxxxxxx

        xxxxxxxxxx x xxxxxx
        xxxxxxxxx x xxxxx
        xxxxxxxxxxxxx x xxxxxxxx xx xxxx x xxxxxxxx x xxxxxxxxxxxxxxxxxxxx
        xxxxxxxxx x xxxx xx xxxxxxxxxxxxxxxxx x xxxxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxx
    x

    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    xx xxxx xxxxxxxxx xxx
    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxx xxxxx xxxxxx x
        xxxxxx xxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxx xxxxxxxxxxx xxxxxxx xx xxxxxx x
        xxxxxxxx x xxxxxx
        xxxxxx xxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxx xxxxxxxxxxxxx x
        xxxxxxxx x xxxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxx
    x

    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    xx xxxx xxxxx xxx
    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxx xx xxxxxxxxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxx xxxx x
        xxxxxxxxxxxxxxxxx

        xx xxxxx xx xxxxx x
            xxxxxxxxx xx
               xxxxxxxxxxxx xxxx
               xxxxxx xx
               xxxxxxxxxxxx
               xxxxxx xx
               xxxxxxxxxxxxx
        x
        xxxx x
            xxxxxxxxx xx
               xxxxxxxxxxxxxxxxxx xxxxxxxxx
               xxxxxx xx
               xxxxxxxxxxxx
               xxxxxx xx
               xxxxxxxxxxxxxxxx
               xxxxxx xx
               xxxxxxxxxxxxxxx
               xxxxxx xx
               xxxxxxxxxxxxxxxxx
        x
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxxxxxxxxxxx x
        xxxxxx xxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxx xxxxxxxxx xxxxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxxxx xxxxxx xxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxx xxxxxxxxx xxxxxx xxxxxxxxx xxxxxxxxxxxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxxxxxx xxxxxx
    x

    xxxxxxxxx
    xxxxx xxxxxxxxx xxxxxxxxx x
        xxxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xxx
     x xxx xxxxxxxxx xxxxx xxxxxx xxxx
     xx
    xxxx xxxxxxxxxxxxx x
        xxxx xxxxxxxxxx
    x
x
xx [/pro] */