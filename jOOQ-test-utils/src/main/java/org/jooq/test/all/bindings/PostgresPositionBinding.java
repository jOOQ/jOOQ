/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.test.all.bindings;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.Converter;
import org.jooq.impl.DSL;
import org.jooq.test.all.converters.PositionConverter;
import org.jooq.test.all.types.Position;


/**
 * @author Lukas Eder
 * @autho Andreas Ahlenstorf
 * @see <a href=
 *      "https://groups.google.com/d/msg/jooq-user/TBQZCPTCvnk/HJYND9AQs6EJ">
 *      https://groups.google.com/d/msg/jooq-user/TBQZCPTCvnk/HJYND9AQs6EJ</a>
 */
public class PostgresPositionBinding implements Binding<Object, Position> {

    private final Converter<Object, Position> converter = new PositionConverter();

    @Override
    public Converter<Object, Position> converter() {
        return converter;
    }

    @Override
    public void sql(BindingSQLContext<Position> ctx) throws SQLException {
        ctx.render().visit(DSL.sql("?::geography"));
    }

    @Override
    public void set(BindingSetStatementContext<Position> ctx) throws SQLException {
        ctx.statement().setObject(ctx.index(), ctx.convert(converter).value());
    }

    @Override
    public void get(BindingGetResultSetContext<Position> ctx) throws SQLException {
        ctx.convert(converter).value(ctx.resultSet().getObject(ctx.index()));
    }

    @Override
    public void get(BindingGetStatementContext<Position> ctx) throws SQLException {
        ctx.convert(converter).value(ctx.statement().getObject(ctx.index()));
    }

    @Override
    public void get(BindingGetSQLInputContext<Position> bindingGetSQLInputContext) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void set(BindingSetSQLOutputContext<Position> bindingSetSQLOutputContext) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void register(BindingRegisterContext<Position> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}