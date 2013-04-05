/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.impl;

import java.util.List;

import org.jooq.Cursor;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UniqueKey;

/**
 * A base class for custom {@link Table} implementations in client code.
 * <p>
 * Client code may provide proper {@link Table} implementations extending this
 * useful base class. All necessary parts of the {@link Table} interface are
 * already implemented. Only this method needs further implementation:
 * <ul>
 * <li>{@link #getRecordType()}</li>
 * </ul>
 * Refer to this method's Javadoc for further details about its expected
 * behaviour.
 * <p>
 * Use this base class when providing custom tables to any of the following
 * methods:
 * <ul>
 * <li>{@link ResultQuery#fetchInto(Table)}</li>
 * <li>{@link Cursor#fetchInto(Table)}</li>
 * <li>{@link Result#into(Table)}</li>
 * <li>{@link Record#into(Table)}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public abstract class CustomTable<R extends TableRecord<R>> extends TableImpl<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 4300737872863697213L;

    protected CustomTable(String name) {
        super(name);
    }

    protected CustomTable(String name, Schema schema) {
        super(name, schema);
    }

    // -------------------------------------------------------------------------
    // Implementation required
    // -------------------------------------------------------------------------

    /**
     * Subclasses must implement this method
     * <hr/>
     * {@inheritDoc}
     */
    @Override
    public abstract Class<? extends R> getRecordType();

    // -------------------------------------------------------------------------
    // Further overrides allowed
    // -------------------------------------------------------------------------

    @Override
    public Identity<R, ? extends Number> getIdentity() {
        return super.getIdentity();
    }

    @Override
    public UniqueKey<R> getPrimaryKey() {
        return super.getPrimaryKey();
    }

    @Override
    public List<UniqueKey<R>> getKeys() {
        return super.getKeys();
    }

    @Override
    public List<ForeignKey<R, ?>> getReferences() {
        return super.getReferences();
    }

    // -------------------------------------------------------------------------
    // No further overrides allowed
    // -------------------------------------------------------------------------

    @Override
    public final boolean declaresFields() {
        return super.declaresFields();
    }

    @Override
    public final boolean declaresTables() {
        return super.declaresTables();
    }
}
