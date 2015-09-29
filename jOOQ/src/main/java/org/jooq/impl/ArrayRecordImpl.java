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

/* [pro] xx

xxxxxx xxxxxxxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxx
xxxxxx xxxxxxxxxxxxxxxxxxxxxxx

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
    xxxxxxx xxxxx xxxxxxx     xxxxx

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
        xxxxxxxxx x xxx xxxxxxxxxxxxxxx
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
    xxxxxxxxxxx
    xxxxxx xxxxx xxx xxxxx x
        xxxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxx
    x

    xxxxxxxxx
    xxxxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxxxx x
        xxxxxx xxxxx
    x

    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    xxxxxxxxx
    xxxxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxx xxxxxx x
        xx xxxxxx xx xxxxx
            xxxxxxxx
        xxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxxxx xxxxxx xxxxxx xxxxxxxxxxxx x
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxxxx
    x

    xxxxxxxxx
    xxxxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxxxxxxxxxx xxxxxxx xx xxxxxxxxxxx x
        xxxxxxxx
        xxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxxxxxxxxx xxxxxxx xx xxxxx x
        xxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxx xxxxxxxxxxx x
        xxxxxx xxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxx xxxxxxxxx x

        xx xxxxxxx xxxxxxx xxxx xxxx xxxxx xxxx xx xx xxxxxx x xxxxxxxxx xxxxx
        xxxxxx xxxxx
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

        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx
        xxxxxxxxxxxxxxxxxxx

        xxx xx x x xxxxx x
            xxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

            xxxxxxxxx x xx xx
        x

        xxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxxxxxxxxxxxxxxx
    x

    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    xx xxx xxxx xxxxxxx
    xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    xxxxxxxxx
    xxxxxx xxxxx xxx xxxxxx x
        xxxxxx xxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxxxxxxxxxx xx x
        xxxxxx xxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxx xxxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxx xxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxx xxx xxxxxxxxxxx xx x
        xxxxxx xxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxx xx x
        xxxxxx xxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxxxxxxxx xx x
        xxxxxx xxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx xx x
        xxxxxx xxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxxxxxxxxxxxxxx xxxxxxx xx xx x
        xxxxxx xxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxxxxx xxxxxx xxxxxxxxxxxx xxxxxxx xx xx x
        xxxxxx xxxxxxxxxxxxxxxxxx xxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xx x
        xxxxxx xxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xx x
        xxxxxx xxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxx x
        xxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxxxxxxxx xx x
        xxxxxx xxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxx xxxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx x xxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx x xxxxxxx xxxxxx x xxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxx xxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxx xxxxxxx xxxxxx x xxxxxxxx x
        xxxxxxxxxxxxxxx xxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx x xxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxx xxxxxxxxxxxxxx xx x
        xxxxxx xxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxx xxxxxxxxxxxxxxxxxx xx x
        xxxxxx xxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxx xxxxxxxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxx xxxxxxxxxxx xxxxxxxxxx xxx xxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxx
    x
x
xx [/pro] */