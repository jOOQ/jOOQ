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

import java.util.EventListener;

/**
 * A listener for manipulation events on {@link UpdatableRecord}s.
 * <p>
 * Users may want to centrally inject custom behaviour when manipulating their
 * {@link UpdatableRecord} objects, performing CRUD. This service provider
 * allows to hook in callback method implementations for before or after any of
 * these methods:
 * <ul>
 * <li>{@link UpdatableRecord#store()}</li>
 * <li>{@link UpdatableRecord#insert()}</li>
 * <li>{@link UpdatableRecord#update()}</li>
 * <li>{@link UpdatableRecord#delete()}</li>
 * <li>{@link UpdatableRecord#refresh()}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface RecordListener extends EventListener {

    /**
     * Called before storing an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * prior to storing. Note that modifying the record's primary key value may
     * influence whether storing results in an <code>INSERT</code> or
     * <code>UPDATE</code> statement.
     *
     * @see UpdatableRecord#store()
     */
    void storeStart(RecordContext ctx);

    /**
     * Called after storing an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * after storing. Note that modifying the record's primary key value may
     * influence whether storing results in an <code>INSERT</code> or
     * <code>UPDATE</code> statement.
     *
     * @see UpdatableRecord#store()
     */
    void storeEnd(RecordContext ctx);

    /**
     * Called before inserting an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * prior to inserting.
     *
     * @see UpdatableRecord#insert()
     */
    void insertStart(RecordContext ctx);

    /**
     * Called after inserting an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * after inserting.
     *
     * @see UpdatableRecord#insert()
     */
    void insertEnd(RecordContext ctx);

    /**
     * Called before updating an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * prior to updating.
     *
     * @see UpdatableRecord#update()
     */
    void updateStart(RecordContext ctx);

    /**
     * Called after updating an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * after updating.
     *
     * @see UpdatableRecord#update()
     */
    void updateEnd(RecordContext ctx);

    /**
     * Called before deleting an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * prior to deleting.
     *
     * @see UpdatableRecord#delete()
     */
    void deleteStart(RecordContext ctx);

    /**
     * Called after deleting an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * after deleting.
     *
     * @see UpdatableRecord#delete()
     */
    void deleteEnd(RecordContext ctx);

    /**
     * Called before refreshing an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * prior to refreshing.
     *
     * @see UpdatableRecord#refresh()
     */
    void refreshStart(RecordContext ctx);

    /**
     * Called after refreshing an <code>UpdatableRecord</code>.
     * <p>
     * Implementations are allowed to modify {@link RecordContext#record()}
     * after refreshing.
     *
     * @see UpdatableRecord#refresh()
     */
    void refreshEnd(RecordContext ctx);
}
