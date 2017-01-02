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
package org.jooq.academy.section4;

import java.util.Arrays;

import org.jooq.SQLDialect;
import org.jooq.academy.tools.Tools;
import org.jooq.impl.DSL;

import org.junit.Test;

public class Example_4_2_SQLDialect {

    @Test
    public void run() {
        Tools.title("Generate SELECT 1 FROM DUAL for all SQL dialect families");
        Arrays.stream(SQLDialect.families())
              .map(dialect -> String.format("%15s : ", dialect) + DSL.using(dialect).render(DSL.selectOne()))
              .forEach(System.out::println);
    }
}
