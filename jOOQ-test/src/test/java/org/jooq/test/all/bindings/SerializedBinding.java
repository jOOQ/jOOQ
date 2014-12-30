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
package org.jooq.test.all.bindings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
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
import org.jooq.impl.DSL;

@SuppressWarnings("serial")
public class SerializedBinding implements Binding<byte[], Serializable> {

    @Override
    public Converter<byte[], Serializable> converter() {
        return new Converter<byte[], Serializable>() {

            @Override
            public Serializable from(byte[] t) {
                if (t == null)
                    return null;

                Serializable data;
                ByteArrayInputStream bis = new ByteArrayInputStream(t);
                try {
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    data = (Serializable) ois.readObject();
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }

                return data;
            }

            @Override
            public byte[] to(Serializable u) {
                if (u == null)
                    return null;

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(u);
                    oos.flush();
                    oos.close();
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }

                return bos.toByteArray();
            }

            @Override
            public Class<byte[]> fromType() {
                return byte[].class;
            }

            @Override
            public Class<Serializable> toType() {
                return Serializable.class;
            }
        };
    }

    @Override
    public void sql(BindingSQLContext<Serializable> ctx) throws SQLException {
        ctx.render().visit(DSL.val(ctx.convert(converter()).value()));
    }

    @Override
    public void register(BindingRegisterContext<Serializable> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARBINARY);
    }

    @Override
    public void set(BindingSetStatementContext<Serializable> ctx) throws SQLException {
        ctx.statement().setBytes(ctx.index(), ctx.convert(converter()).value());
    }

    @Override
    public void set(BindingSetSQLOutputContext<Serializable> ctx) throws SQLException {
        ctx.output().writeBytes(ctx.convert(converter()).value());
    }

    @Override
    public void get(BindingGetResultSetContext<Serializable> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getBytes(ctx.index()));
    }

    @Override
    public void get(BindingGetStatementContext<Serializable> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getBytes(ctx.index()));
    }

    @Override
    public void get(BindingGetSQLInputContext<Serializable> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.input().readBytes());
    }
}
