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
package org.jooq.impl;

import java.sql.Connection;

import org.jooq.ConnectionProvider;

/**
 * An "empty" implementation that is never connected.
 *
 * @author Lukas Eder
 */
public class NoConnectionProvider implements ConnectionProvider {

    /**
     * This method will always return <code>null</code>.
     */
    @Override
    public final Connection acquire() {
        return null;
    }

    /**
     * This method has no effect.
     */
    @Override
    public final void release(Connection connection) {}
}
