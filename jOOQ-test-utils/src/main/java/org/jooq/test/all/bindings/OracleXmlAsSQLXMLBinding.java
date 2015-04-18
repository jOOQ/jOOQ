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
import java.sql.SQLXML;
import java.sql.SQLXML;
import java.sql.Types;

import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.Converter;

public class OracleXmlAsSQLXMLBinding implements Binding<Object, SQLXML> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 358789452467943117L;

    @Override
    public Converter<Object, SQLXML> converter() {
        return new Converter<Object, SQLXML>() {

            @Override
            public SQLXML from(Object databaseObject) {
                return null;
            }

            @Override
            public Object to(SQLXML userObject) {
                return null;
            }

            @Override
            public Class<Object> fromType() {
                return Object.class;
            }

            @Override
            public Class<SQLXML> toType() {
                return SQLXML.class;
            }
        };
    }

    @Override
    public void sql(BindingSQLContext<SQLXML> ctx) throws SQLException {
        ctx.render().sql("?");
    }

    @Override
    public void register(BindingRegisterContext<SQLXML> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.SQLXML);
    }

    @Override
    public void set(BindingSetStatementContext<SQLXML> ctx) throws SQLException {
        if (ctx.value() == null)
            ctx.statement().setObject(ctx.index(), null);
        else
            ctx.statement().setSQLXML(ctx.index(), ctx.value());
    }

    @Override
    public void set(BindingSetSQLOutputContext<SQLXML> ctx) throws SQLException {
        if (ctx.value() == null)
            ctx.output().writeObject(null);
        else
            ctx.output().writeSQLXML(ctx.value());
    }

    @Override
    public void get(BindingGetResultSetContext<SQLXML> ctx) throws SQLException {
        ctx.value(ctx.resultSet().getSQLXML(ctx.index()));
    }

    @Override
    public void get(BindingGetStatementContext<SQLXML> ctx) throws SQLException {
        ctx.value(ctx.statement().getSQLXML(ctx.index()));
    }

    @Override
    public void get(BindingGetSQLInputContext<SQLXML> ctx) throws SQLException {
        ctx.value(ctx.input().readSQLXML());
    }
}
