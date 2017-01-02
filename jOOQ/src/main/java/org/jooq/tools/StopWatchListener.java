/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.tools;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;

/**
 * A default {@link ExecuteListener} that just logs events to java.util.logging,
 * log4j, or slf4j using the {@link JooqLogger}
 *
 * @author Lukas Eder
 */
public class StopWatchListener implements ExecuteListener {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7399239846062763212L;

    private final StopWatch watch = new StopWatch();

    @Override
    public void start(ExecuteContext ctx) {
        watch.splitTrace("Initialising");
    }

    @Override
    public void renderStart(ExecuteContext ctx) {
        watch.splitTrace("Rendering query");
    }

    @Override
    public void renderEnd(ExecuteContext ctx) {
        watch.splitTrace("Query rendered");
    }

    @Override
    public void prepareStart(ExecuteContext ctx) {
        watch.splitTrace("Preparing statement");
    }

    @Override
    public void prepareEnd(ExecuteContext ctx) {
        watch.splitTrace("Statement prepared");
    }

    @Override
    public void bindStart(ExecuteContext ctx) {
        watch.splitTrace("Binding variables");
    }

    @Override
    public void bindEnd(ExecuteContext ctx) {
        watch.splitTrace("Variables bound");
    }

    @Override
    public void executeStart(ExecuteContext ctx) {
        watch.splitTrace("Executing query");
    }

    @Override
    public void executeEnd(ExecuteContext ctx) {
        watch.splitDebug("Query executed");
    }

    @Override
    public void outStart(ExecuteContext ctx) {
        watch.splitDebug("Fetching out values");
    }

    @Override
    public void outEnd(ExecuteContext ctx) {
        watch.splitDebug("Out values fetched");
    }

    @Override
    public void fetchStart(ExecuteContext ctx) {
        watch.splitTrace("Fetching results");
    }

    @Override
    public void resultStart(ExecuteContext ctx) {
        watch.splitTrace("Fetching result");
    }

    @Override
    public void recordStart(ExecuteContext ctx) {
        watch.splitTrace("Fetching record");
    }

    @Override
    public void recordEnd(ExecuteContext ctx) {
        watch.splitTrace("Record fetched");
    }

    @Override
    public void resultEnd(ExecuteContext ctx) {
        watch.splitTrace("Result fetched");
    }

    @Override
    public void fetchEnd(ExecuteContext ctx) {
        watch.splitTrace("Results fetched");
    }

    @Override
    public void end(ExecuteContext ctx) {
        watch.splitDebug("Finishing");
    }

    @Override
    public void exception(ExecuteContext ctx) {
        watch.splitDebug("Exception");
    }

    @Override
    public void warning(ExecuteContext ctx) {
        watch.splitDebug("Warning");
    }
}
