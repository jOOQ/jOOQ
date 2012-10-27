/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import java.util.Arrays;

import org.jooq.Attachable;
import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.Store;
import org.jooq.tools.Convert;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("deprecation")
abstract class AbstractStore<T> implements Store<T>, AttachableInternal {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -2989496800221194411L;

    private Configuration     configuration;

    AbstractStore() {
        this(null);
    }

    AbstractStore(Configuration configuration) {
        this.configuration = configuration;
    }

    // -------------------------------------------------------------------------
    // The Attachable API
    // -------------------------------------------------------------------------

    @Override
    public final void attach(Configuration c) {
        configuration = c;

        for (Attachable attachable : getAttachables()) {
            attachable.attach(c);
        }
    }

    @Override
    public final Configuration getConfiguration() {
        return configuration;
    }

    /**
     * This method is used in generated code!
     */
    protected final Executor create() {
        return new Executor(getConfiguration());
    }

    // -------------------------------------------------------------------------
    // The Store API
    // -------------------------------------------------------------------------

    @Override
    public final T getValue(int index, T defaultValue) {
        final T result = getValue(index);
        return result == null ? defaultValue : result;
    }

    @Override
    public final <Z> Z getValue(int index, Class<? extends Z> type) {
        return Convert.convert(getValue(index), type);
    }

    @Override
    public final <Z> Z getValue(int index, Class<? extends Z> type, Z defaultValue) {
        final Z result = getValue(index, type);
        return result == null ? defaultValue : result;
    }

    // -------------------------------------------------------------------------
    // equals and hashCode
    // -------------------------------------------------------------------------

    @Override
    public int hashCode() {
        int hashCode = 1;

        for (int i = 0; i < size(); i++) {
            final Object obj = getValue(i);
            hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Store) {
            final Store<?> that = (Store<?>) obj;

            if (size() == that.size()) {
                for (int i = 0; i < size(); i++) {
                    final Object thisValue = getValue(i);
                    final Object thatValue = that.getValue(i);

                    // [#1850] Only return false early. In all other cases,
                    // continue checking the remaining fields
                    if (thisValue == null && thatValue == null) {
                        continue;
                    }

                    else if (thisValue == null || thatValue == null) {
                        return false;
                    }

                    // [#985] Compare arrays too.
                    else if (thisValue.getClass().isArray() && thatValue.getClass().isArray()) {

                        // Might be byte[]
                        if (thisValue.getClass() == byte[].class && thatValue.getClass() == byte[].class) {
                            if (!Arrays.equals((byte[]) thisValue, (byte[]) thatValue)) {
                                return false;
                            }
                        }

                        // Other primitive types are not expected
                        else if (!thisValue.getClass().getComponentType().isPrimitive() &&
                                 !thatValue.getClass().getComponentType().isPrimitive()) {
                            if (!Arrays.equals((Object[]) thisValue, (Object[]) thatValue)) {
                                return false;
                            }
                        }

                        else {
                            return false;
                        }
                    }
                    else if (!thisValue.equals(thatValue)) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }
}
