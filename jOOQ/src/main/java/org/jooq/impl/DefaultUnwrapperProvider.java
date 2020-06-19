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
 *
 *
 *
 */
package org.jooq.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Wrapper;

import org.jooq.Unwrapper;
import org.jooq.UnwrapperProvider;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;

/**
 * @author Lukas Eder
 */
final class DefaultUnwrapperProvider implements UnwrapperProvider {

    static final Unwrapper INSTANCE = new DefaultUnwrapper();

    static final class DefaultUnwrapper implements Unwrapper {

        /**
         * [#3696] We shouldn't infinitely attempt to unwrap connections.
         */
        private static int              maxUnwrappedConnections = 256;

        /**
         * [#7589] No infinite attempts to unwrap statements, either.
         */
        private static int              maxUnwrappedStatements  = 256;

        @Override
        public <T> T unwrap(Wrapper wrapper, Class<T> iface) {
            if (wrapper instanceof Connection)
                return unwrap((Connection) wrapper, iface);
            else if (wrapper instanceof Statement)
                return unwrap((Statement) wrapper, iface);
            else
                throw new IllegalArgumentException("Cannot unwrap: " + wrapper);
        }

        @SuppressWarnings("unchecked")
        private <T> T unwrap(Connection wrapper, Class<T> iface) {
            Connection result = wrapper;
























































            return (T) result;
        }

        @SuppressWarnings("unchecked")
        private <T> T unwrap(Statement wrapper, Class<T> iface) {
            Statement result = wrapper;














































            return (T) result;
        }
    };

    @Override
    public Unwrapper provide() {
        return INSTANCE;
    }
}
