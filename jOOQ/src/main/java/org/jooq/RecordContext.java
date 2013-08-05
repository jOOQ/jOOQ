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
package org.jooq;

import java.util.Map;

/**
 * A context object for {@link Record} manipulation passed to
 * registered {@link RecordListener}'s.
 *
 * @author Lukas Eder
 */
public interface RecordContext {

    /**
     * Get all custom data from this <code>RecordContext</code>.
     * <p>
     * This is custom data that was previously set to the record context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * between events received by a {@link RecordListener}.
     * <p>
     * Unlike {@link Configuration#data()}, these data's lifecycle only matches
     * that of a single record manipulation.
     *
     * @return The custom data. This is never <code>null</code>
     * @see RecordListener
     */
    Map<Object, Object> data();

    /**
     * Get some custom data from this <code>RecordContext</code>.
     * <p>
     * This is custom data that was previously set to the record context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * between events received by an {@link RecordListener}.
     * <p>
     * Unlike {@link Configuration#data()}, these data's lifecycle only matches
     * that of a single query execution.
     *
     * @param key A key to identify the custom data
     * @return The custom data or <code>null</code> if no such data is contained
     *         in this <code>RecordContext</code>
     * @see RecordListener
     */
    Object data(Object key);

    /**
     * Set some custom data to this <code>RecordContext</code>.
     * <p>
     * This is custom data that was previously set to the record context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * between events received by an {@link RecordListener}.
     * <p>
     * Unlike {@link Configuration#data()}, these data's lifecycle only matches
     * that of a single query execution.
     *
     * @param key A key to identify the custom data
     * @param value The custom data or <code>null</code> to unset the custom
     *            data
     * @return The previously set custom data or <code>null</code> if no data
     *         was previously set for the given key
     * @see RecordListener
     */
    Object data(Object key, Object value);

    /**
     * The configuration wrapped by this context.
     */
    Configuration configuration();

    /**
     * The type of database interaction that is being executed.
     * <p>
     * Unlike {@link ExecuteContext#type()}, this can only result in any of
     * these:
     * <ul>
     * <li>{@link ExecuteType#BATCH} when calling
     * {@link DSLContext#batchStore(UpdatableRecord...) batchStore()},
     * {@link DSLContext#batchInsert(UpdatableRecord...) batchInsert()},
     * {@link DSLContext#batchUpdate(UpdatableRecord...) batchUpdate()},
     * {@link DSLContext#batchDelete(UpdatableRecord...) batchDelete()}.</li>
     * <li>{@link ExecuteType#READ} when calling
     * {@link UpdatableRecord#refresh() refresh()}</li>
     * <li>{@link ExecuteType#WRITE} when calling
     * {@link UpdatableRecord#store() store()}, {@link UpdatableRecord#insert()
     * insert()}, {@link UpdatableRecord#update() update()},
     * {@link UpdatableRecord#delete() delete()}.</li>
     * </ul>
     *
     * @see ExecuteType
     */
    ExecuteType type();

    /**
     * The <code>Record</code> that is being manipulated.
     *
     * @return The <code>Record</code> being manipulated. This is never
     *         <code>null</code>
     */
    Record record();

    /**
     * The <code>Record</code>(s) that are being manipulated in batch
     * mode.
     * <p>
     * If a single <code>Record</code> is being manipulated in
     * non-batch mode, this will return an array of length <code>1</code>,
     * containing that <code>Record</code>.
     *
     * @return The <code>Record</code>(s) being manipulated. This is
     *         never <code>null</code>
     */
    Record[] batchRecords();
}
