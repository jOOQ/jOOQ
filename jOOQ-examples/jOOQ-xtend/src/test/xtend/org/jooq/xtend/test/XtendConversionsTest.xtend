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
package org.jooq.xtend.test

import java.sql.DriverManager
import static extension org.jooq.xtend.Conversions.*

import org.jooq.impl.DSL
import org.jooq.Record2
import org.jooq.Select

import static org.jooq.impl.DSL.*

/**
 * @author Lukas Eder
 */
class XtendConversionsTest {

    def static void main(String[] args) {
        Class::forName("org.h2.Driver")
        using(DriverManager::getConnection("jdbc:h2:~/test", "sa", ""))

        val r = row(inline(1), inline("a"))
        val s = (select(inline(2), inline("b"))) as Select<Record2<Integer, String>>
        // val c = r > s
        org.jooq.xtend.Conversions::operator_greaterThan(r, r)
    }
}