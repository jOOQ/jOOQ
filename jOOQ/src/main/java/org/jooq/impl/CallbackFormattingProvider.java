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


import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import org.jooq.CSVFormat;
import org.jooq.ChartFormat;
import org.jooq.FormattingProvider;
import org.jooq.JSONFormat;
import org.jooq.TXTFormat;
import org.jooq.VisitContext;
import org.jooq.XMLFormat;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link FormattingProvider} that allows for functional composition.
 * <p>
 * For example: <pre><code>
 * FormattingProvider listener = FormattingProvider
 *   .on(ctx -&gt; something())
 *   .onVisitEnd(ctx -&gt; something());
 * </code></pre>
 *
 * @author Lukas Eder
 */
public final class CallbackFormattingProvider implements FormattingProvider {

    private static final DefaultFormattingProvider DEFAULT = new DefaultFormattingProvider();

    private final Supplier<? extends TXTFormat>    onTxtFormat;
    private final Supplier<? extends CSVFormat>    onCsvFormat;
    private final Supplier<? extends JSONFormat>   onJsonFormatForResults;
    private final Supplier<? extends JSONFormat>   onJsonFormatForRecords;
    private final Supplier<? extends XMLFormat>    onXmlFormatForResults;
    private final Supplier<? extends XMLFormat>    onXmlFormatForRecords;
    private final Supplier<? extends ChartFormat>  onChartFormat;
    private final ToIntFunction<? super String>    onWidth;

    public CallbackFormattingProvider() {
        this(
            DEFAULT::txtFormat,
            DEFAULT::csvFormat,
            DEFAULT::jsonFormatForResults,
            DEFAULT::jsonFormatForRecords,
            DEFAULT::xmlFormatForResults,
            DEFAULT::xmlFormatForRecords,
            DEFAULT::chartFormat,
            DEFAULT::width
        );
    }

    private CallbackFormattingProvider(
        Supplier<? extends TXTFormat> onTxtFormat,
        Supplier<? extends CSVFormat> onCsvFormat,
        Supplier<? extends JSONFormat> onJsonFormatForResults,
        Supplier<? extends JSONFormat> onJsonFormatForRecords,
        Supplier<? extends XMLFormat> onXmlFormatForResults,
        Supplier<? extends XMLFormat> onXmlFormatForRecords,
        Supplier<? extends ChartFormat> onChartFormat,
        ToIntFunction<? super String> onWidth
    ) {
        this.onTxtFormat = onTxtFormat;
        this.onCsvFormat = onCsvFormat;
        this.onJsonFormatForResults = onJsonFormatForResults;
        this.onJsonFormatForRecords = onJsonFormatForRecords;
        this.onXmlFormatForResults = onXmlFormatForResults;
        this.onXmlFormatForRecords = onXmlFormatForRecords;
        this.onChartFormat = onChartFormat;
        this.onWidth = onWidth;
    }

    @NotNull
    @Override
    public final TXTFormat txtFormat() {
        return onTxtFormat.get();
    }

    @NotNull
    @Override
    public final CSVFormat csvFormat() {
        return onCsvFormat.get();
    }

    @NotNull
    @Override
    public final JSONFormat jsonFormatForResults() {
        return onJsonFormatForResults.get();
    }

    @NotNull
    @Override
    public final JSONFormat jsonFormatForRecords() {
        return onJsonFormatForRecords.get();
    }

    @NotNull
    @Override
    public final XMLFormat xmlFormatForResults() {
        return onXmlFormatForResults.get();
    }

    @NotNull
    @Override
    public final XMLFormat xmlFormatForRecords() {
        return onXmlFormatForRecords.get();
    }

    @NotNull
    @Override
    public final ChartFormat chartFormat() {
        return onChartFormat.get();
    }

    @Override
    public final int width(String string) {
        return onWidth.applyAsInt(string);
    }

    @NotNull
    public final CallbackFormattingProvider onTxtFormat(Supplier<? extends TXTFormat> newOnTxtFormat) {
        return new CallbackFormattingProvider(
            newOnTxtFormat,
            onCsvFormat,
            onJsonFormatForResults,
            onJsonFormatForRecords,
            onXmlFormatForResults,
            onXmlFormatForRecords,
            onChartFormat,
            onWidth
        );
    }

    @NotNull
    public final CallbackFormattingProvider onCsvFormat(Supplier<? extends CSVFormat> newOnCsvFormat) {
        return new CallbackFormattingProvider(
            onTxtFormat,
            newOnCsvFormat,
            onJsonFormatForResults,
            onJsonFormatForRecords,
            onXmlFormatForResults,
            onXmlFormatForRecords,
            onChartFormat,
            onWidth
        );
    }

    @NotNull
    public final CallbackFormattingProvider onJsonFormatForResults(Supplier<? extends JSONFormat> newOnJsonFormatForResults) {
        return new CallbackFormattingProvider(
            onTxtFormat,
            onCsvFormat,
            newOnJsonFormatForResults,
            onJsonFormatForRecords,
            onXmlFormatForResults,
            onXmlFormatForRecords,
            onChartFormat,
            onWidth
        );
    }

    @NotNull
    public final CallbackFormattingProvider onJsonFormatForRecords(Supplier<? extends JSONFormat> newOnJsonFormatForRecords) {
        return new CallbackFormattingProvider(
            onTxtFormat,
            onCsvFormat,
            onJsonFormatForResults,
            newOnJsonFormatForRecords,
            onXmlFormatForResults,
            onXmlFormatForRecords,
            onChartFormat,
            onWidth
        );
    }

    @NotNull
    public final CallbackFormattingProvider onXmlFormatForResults(Supplier<? extends XMLFormat> newOnXmlFormatForResults) {
        return new CallbackFormattingProvider(
            onTxtFormat,
            onCsvFormat,
            onJsonFormatForResults,
            onJsonFormatForRecords,
            newOnXmlFormatForResults,
            onXmlFormatForRecords,
            onChartFormat,
            onWidth
        );
    }

    @NotNull
    public final CallbackFormattingProvider onXmlFormatForRecords(Supplier<? extends XMLFormat> newOnXmlFormatForRecords) {
        return new CallbackFormattingProvider(
            onTxtFormat,
            onCsvFormat,
            onJsonFormatForResults,
            onJsonFormatForRecords,
            onXmlFormatForResults,
            newOnXmlFormatForRecords,
            onChartFormat,
            onWidth
        );
    }

    @NotNull
    public final CallbackFormattingProvider onChartFormat(Supplier<? extends ChartFormat> newOnChartFormat) {
        return new CallbackFormattingProvider(
            onTxtFormat,
            onCsvFormat,
            onJsonFormatForResults,
            onJsonFormatForRecords,
            onXmlFormatForResults,
            onXmlFormatForRecords,
            newOnChartFormat,
            onWidth
        );
    }

    @NotNull
    public final CallbackFormattingProvider onWidth(ToIntFunction<? super String> newOnWidth) {
        return new CallbackFormattingProvider(
            onTxtFormat,
            onCsvFormat,
            onJsonFormatForResults,
            onJsonFormatForRecords,
            onXmlFormatForResults,
            onXmlFormatForRecords,
            onChartFormat,
            newOnWidth
        );
    }
}

