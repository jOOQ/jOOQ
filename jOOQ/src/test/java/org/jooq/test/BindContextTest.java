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

import org.jooq.BindContext;
import org.jooq.Context;
import org.jooq.RenderContext;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.CustomCondition;

import org.junit.Test;

/**
 * Some common tests related to {@link RenderContext}
 *
 * @author Lukas Eder
 */
public class BindContextTest extends AbstractTest {

    @Test
    public void testData() {
        BindContext ctx = create.bindContext(statement);
        ctx.data("key", "value");

        TestDataQueryPart part = new TestDataQueryPart();
        ctx.visit(part);
        assertEquals("value", part.value);
    }

    private static class TestDataQueryPart extends CustomCondition {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 2310925428858496140L;

        Object value;

        @Override
        public void accept(Context<?> context) throws DataAccessException {
            value = context.data("key");
        }

    }
}
