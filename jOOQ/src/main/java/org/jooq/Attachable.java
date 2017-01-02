/*
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
     * Attach this object to a new {@link Configuration}.
     *
     * @param configuration A configuration or <code>null</code>, if you wish to
     *            detach this <code>Attachable</code> from its previous
     *            configuration.
     */
    void attach(Configuration configuration);

    /**
     * Detach this object from its current {@link Configuration}.
     * <p>
     * This is the same as calling <code>attach(null)</code>.
     */
    void detach();
}
