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
 *
 *
 *
 */
package org.jooq.impl;

import org.jooq.CSVFormat;
import org.jooq.ChartFormat;
import org.jooq.FormattingProvider;
import org.jooq.JSONFormat;
import org.jooq.TXTFormat;
import org.jooq.XMLFormat;

/**
 * A default implementation for the {@link FormattingProvider}.
 *
 * @author Lukas Eder
 */
public class DefaultFormattingProvider implements FormattingProvider {

    @Override
    public TXTFormat txtFormat() {
        return TXTFormat.DEFAULT;
    }

    @Override
    public CSVFormat csvFormat() {
        return CSVFormat.DEFAULT;
    }

    @Override
    public JSONFormat jsonFormatForResults() {
        return JSONFormat.DEFAULT_FOR_RESULTS;
    }

    @Override
    public JSONFormat jsonFormatForRecords() {
        return JSONFormat.DEFAULT_FOR_RECORDS;
    }

    @Override
    public XMLFormat xmlFormatForResults() {
        return XMLFormat.DEFAULT_FOR_RESULTS;
    }

    @Override
    public XMLFormat xmlFormatForRecords() {
        return XMLFormat.DEFAULT_FOR_RECORDS;
    }

    @Override
    public ChartFormat chartFormat() {
        return ChartFormat.DEFAULT;
    }

    @Override
    public int width(String string) {

        // [#12275] TODO Calculate string display width in presence of ideographic characters
        return string.length();
    }
}
