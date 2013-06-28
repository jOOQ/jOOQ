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

/**
 * A provider for {@link RecordMapper} instances.
 * <p>
 * In order to inject custom {@link Record} to <code>POJO</code> mapping
 * behaviour, users can supply a custom {@link RecordMapperProvider} to their
 * {@link Configuration} instances. This provider will be used in any of these
 * methods (non-exhaustive list):
 * <h3><code>Cursor</code></h3>
 * <ul>
 * <li> {@link Cursor#fetchInto(Class)}</li>
 * <li> {@link Cursor#fetchOneInto(Class)}</li>
 * </ul>
 * <h3><code>Record</code></h3>
 * <ul>
 * <li> {@link Result#into(Class)}</li>
 * </ul>
 * <h3><code>Result</code></h3>
 * <ul>
 * <li> {@link Result#intoMap(Field, Class)}</li>
 * <li> {@link Result#intoMap(Field[], Class)}</li>
 * <li> {@link Result#intoGroups(Field, Class)}</li>
 * <li> {@link Result#intoGroups(Field[], Class)}</li>
 * <li> {@link Result#into(Class)}</li>
 * </ul>
 * <h3><code>ResultQuery</code></h3>
 * <ul>
 * <li> {@link ResultQuery#fetchMap(Field, Class)}</li>
 * <li> {@link ResultQuery#fetchMap(Field[], Class)}</li>
 * <li> {@link ResultQuery#fetchGroups(Field, Class)}</li>
 * <li> {@link ResultQuery#fetchGroups(Field[], Class)}</li>
 * <li> {@link ResultQuery#fetchInto(Class)}</li>
 * <li> {@link ResultQuery#fetchOneInto(Class)}</li>
 * </ul>
 * <h3><code>DAO</code></h3>
 * <ul>
 * <li>Most {@link DAO} methods make use of any of the above methods</li>
 * </ul>
 * <p>
 * This is a non-public, experimental API type
 *
 * @author Lukas Eder
 * @see RecordMapper
 * @see Configuration
 */
interface RecordMapperProvider {

    <R extends Record, E> RecordMapper<R, E> provide(Class<? extends E> type);
}
