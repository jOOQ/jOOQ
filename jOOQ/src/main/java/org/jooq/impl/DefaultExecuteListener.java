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
package org.jooq.impl;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;

/**
 * A publicly available default implementation of {@link ExecuteListener}.
 * <p>
 * Use this to stay compatible with future API changes (i.e. added methods to
 * <code>ExecuteListener</code>)
 *
 * @author Lukas Eder
 */
public class DefaultExecuteListener implements ExecuteListener {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7399239846062763212L;

    @Override
    public void start(ExecuteContext ctx) {}

    @Override
    public void renderStart(ExecuteContext ctx) {}

    @Override
    public void renderEnd(ExecuteContext ctx) {}

    @Override
    public void prepareStart(ExecuteContext ctx) {}

    @Override
    public void prepareEnd(ExecuteContext ctx) {}

    @Override
    public void bindStart(ExecuteContext ctx) {}

    @Override
    public void bindEnd(ExecuteContext ctx) {}

    @Override
    public void executeStart(ExecuteContext ctx) {}

    @Override
    public void executeEnd(ExecuteContext ctx) {}

    @Override
    public void outStart(ExecuteContext ctx) {}

    @Override
    public void outEnd(ExecuteContext ctx) {}

    @Override
    public void fetchStart(ExecuteContext ctx) {}

    @Override
    public void resultStart(ExecuteContext ctx) {}

    @Override
    public void recordStart(ExecuteContext ctx) {}

    @Override
    public void recordEnd(ExecuteContext ctx) {}

    @Override
    public void resultEnd(ExecuteContext ctx) {}

    @Override
    public void fetchEnd(ExecuteContext ctx) {}

    @Override
    public void end(ExecuteContext ctx) {}

    @Override
    public void exception(ExecuteContext ctx) {}

    @Override
    public void warning(ExecuteContext ctx) {}

}
