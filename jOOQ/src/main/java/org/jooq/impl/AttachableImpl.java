/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import static org.jooq.impl.Factory.isStaticFactory;

import java.util.List;

import org.jooq.Attachable;
import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.SchemaMapping;
import org.jooq.Store;

/**
 * A default implementation for mixin of the {@link Attachable} interface
 *
 * @author Lukas Eder
 */
class AttachableImpl implements AttachableInternal {

    /**
     * Generated UID
     */
    private static final long        serialVersionUID = 8769475224067827057L;
    private static final JooqLogger  log              = JooqLogger.getLogger(AttachableImpl.class);

    private Configuration            configuration;
    private final AttachableInternal delegate;

    AttachableImpl(AttachableInternal delegate) {
        this(delegate, null);
    }

    AttachableImpl(AttachableInternal delegate, Configuration configuration) {
        this.delegate = delegate;
        this.configuration = configuration;
    }

    @Override
    public final <I> I internalAPI(Class<I> internalType) throws ClassCastException {
        return internalType.cast(this);
    }

    @Override
    public final void attach(Configuration c) {

        // Static factories or default configurations in QueryParts
        // shouldn't be attached
        if (!isStaticFactory(configuration)

        // On the other hand, Stores (e.g. UDTRecord, ArrayRecord) should
        // always be attached
            || delegate instanceof Store) {

            if (log.isTraceEnabled()) {
                log.trace("Attaching", delegate.getClass().getSimpleName() + " [ " + delegate + " ]");
            }

            configuration = c;
        }

        for (Attachable attachable : getAttachables()) {
            attachable.attach(c);
        }
    }

    @Override
    public final Configuration getConfiguration() {
        return configuration;
    }

    final SQLDialect getDialect() {
        return getConfiguration().getDialect();
    }

    final SchemaMapping getSchemaMapping() {
        return getConfiguration().getSchemaMapping();
    }

    @Override
    public final List<Attachable> getAttachables() {
        return delegate.getAttachables();
    }

    @Override
    public String toString() {
        if (configuration == null) {
            return "[ detached ]";
        }
        else {
            return configuration.toString();
        }
    }
}
