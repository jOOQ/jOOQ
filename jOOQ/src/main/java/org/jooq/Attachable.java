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

import java.io.Serializable;
import java.sql.Connection;

import javax.sql.DataSource;

/**
 * An object in jOOQ that can have an underlying {@link Configuration} attached
 * or detached.
 * <p>
 * Detaching an <code>Attachable</code> from its configuration means, that the
 * underlying {@link Connection} or {@link DataSource} is removed. Attaching an
 * <code>Attachable</code> to a new <code>Configuration</code> means, that its
 * underlying <code>Connection</code> or <code>DataSource</code> will be
 * restored.
 * <p>
 * Detaching an <code>Attachable</code> will <b>NOT</b> close the underlying
 * <code>Connection</code> or <code>DataSource</code>!
 * <p>
 * Attachables are also {@link Serializable}. The underlying
 * <code>Connection</code> or <code>DataSource</code> is <code>transient</code>.
 * Serialising an Attachable will always detach it first.
 *
 * @author Lukas Eder
 */
public interface Attachable extends Serializable {

    /**
     * Attach this object to a new {@link Configuration}
     *
     * @param configuration A configuration or <code>null</code>, if you wish to
     *            detach this <code>Attachable</code> from its previous
     *            configuration.
     */
    void attach(Configuration configuration);
}
