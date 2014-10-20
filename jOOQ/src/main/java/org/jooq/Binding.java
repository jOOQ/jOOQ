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

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

import org.jooq.RenderContext.CastMode;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
public interface Binding<T> extends Serializable {

    String sql     (BindingContext ctx,                                    T value, CastMode castMode, ParamType paramType) throws SQLException;
    void   register(BindingContext ctx, CallableStatement stmt, int index                                                 ) throws SQLException;
    void   set     (BindingContext ctx, PreparedStatement stmt, int index, T value                                        ) throws SQLException;
    void   set     (BindingContext ctx, SQLOutput stream,                  T value                                        ) throws SQLException;
    T      get     (BindingContext ctx, ResultSet rs,           int index                                                 ) throws SQLException;
    T      get     (BindingContext ctx, CallableStatement stmt, int index                                                 ) throws SQLException;
    T      get     (BindingContext ctx, SQLInput stream                                                                   ) throws SQLException;

    public interface BindingContext {
        Configuration configuration();
        SQLDialect dialect();
        SQLDialect family();
    }

    public class DefaultBindingContext implements BindingContext {
        private final Configuration configuration;

        public DefaultBindingContext(Configuration configuration) {
            this.configuration = configuration;
        }

        @Override
        public Configuration configuration() {
            return configuration;
        }

        @Override
        public SQLDialect dialect() {
            return configuration.dialect();
        }

        @Override
        public SQLDialect family() {
            return configuration.dialect().family();
        }
    }
}
