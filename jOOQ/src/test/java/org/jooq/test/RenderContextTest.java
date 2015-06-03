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
package org.jooq.test;

import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;

import org.jooq.Context;
import org.jooq.RenderContext;
import org.jooq.impl.CustomCondition;

import org.junit.Test;

/**
 * Some common tests related to {@link RenderContext}
 *
 * @author Lukas Eder
 */
public class RenderContextTest extends AbstractTest {

    @Test
    public void testParamType() {
        assertEquals(INDEXED, create.renderContext().paramType());
        assertEquals(INDEXED, r_ref().paramType());
        assertEquals(INLINED, r_refI().paramType());
        assertEquals(NAMED, r_refP().paramType());
    }

    @Test
    public void testData() {
        RenderContext ctx = create.renderContext();
        ctx.data("key", "value");

        TestDataQueryPart part = new TestDataQueryPart();
        ctx.render(part);
        assertEquals("value", part.value);
    }

    private static class TestDataQueryPart extends CustomCondition {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 2310925428858496140L;

        Object value;

        @Override
        public void accept(Context<?> context) {
            value = context.data("key");
        }
    }
}
