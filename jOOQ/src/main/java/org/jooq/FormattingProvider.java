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
package org.jooq;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import org.jooq.impl.CallbackFormattingProvider;
import org.jooq.impl.DefaultFormattingProvider;

import org.jetbrains.annotations.NotNull;

/**
 * An SPI that allows for overriding certain formatting defaults.
 * <p>
 * For convenience, consider extending {@link DefaultFormattingProvider} instead
 * of implementing this interface. This will prevent compilation errors in
 * future versions of jOOQ, when this interface might get new methods.
 *
 * @author Lukas Eder
 */
public interface FormattingProvider {

    /**
     * The {@link TXTFormat} to use when calling {@link Formattable#format()}
     * and related methods, defaulting to {@link TXTFormat#DEFAULT}.
     */
    @NotNull
    TXTFormat txtFormat();

    /**
     * The {@link CSVFormat} to use when calling {@link Formattable#formatCSV()}
     * and related methods, defaulting to {@link CSVFormat#DEFAULT}.
     */
    @NotNull
    CSVFormat csvFormat();

    /**
     * The {@link JSONFormat} to use when calling
     * {@link Formattable#formatJSON()} and related methods on results,
     * defaulting to {@link JSONFormat#DEFAULT_FOR_RESULTS}.
     */
    @NotNull
    JSONFormat jsonFormatForResults();

    /**
     * The {@link JSONFormat} to use when calling
     * {@link Formattable#formatJSON()} and related methods on records,
     * defaulting to {@link JSONFormat#DEFAULT_FOR_RECORDS}.
     */
    @NotNull
    JSONFormat jsonFormatForRecords();

    /**
     * The {@link XMLFormat} to use when calling {@link Formattable#formatXML()}
     * and related methods on results, defaulting to
     * {@link XMLFormat#DEFAULT_FOR_RESULTS}.
     */
    @NotNull
    XMLFormat xmlFormatForResults();

    /**
     * The {@link XMLFormat} to use when calling {@link Formattable#formatXML()}
     * and related methods on records, defaulting to
     * {@link XMLFormat#DEFAULT_FOR_RECORDS}.
     */
    @NotNull
    XMLFormat xmlFormatForRecords();

    /**
     * The {@link ChartFormat} to use when calling
     * {@link Formattable#formatChart()} and related methods, defaulting to
     * {@link ChartFormat#DEFAULT}.
     */
    @NotNull
    ChartFormat chartFormat();

    /**
     * The formatting display width in a monospaced font, which may diverge from
     * {@link String#length()} e.g. if the string contains a
     * {@link Character#isIdeographic(int)} character.
     */
    int width(String string);

    /**
     * Create an {@link FormattingProvider} with a {@link #txtFormat()}
     * implementation.
     */
    @NotNull
    public static CallbackFormattingProvider onTxtFormat(Supplier<? extends TXTFormat> newOnTxtFormat) {
        return new CallbackFormattingProvider().onTxtFormat(newOnTxtFormat);
    }

    /**
     * Create an {@link FormattingProvider} with a {@link #csvFormat()}
     * implementation.
     */
    @NotNull
    public static CallbackFormattingProvider onCsvFormat(Supplier<? extends CSVFormat> newOnCsvFormat) {
        return new CallbackFormattingProvider().onCsvFormat(newOnCsvFormat);
    }

    /**
     * Create an {@link FormattingProvider} with a
     * {@link #jsonFormatForResults()} implementation.
     */
    @NotNull
    public static CallbackFormattingProvider onJsonFormatForResults(Supplier<? extends JSONFormat> newOnJsonFormatForResults) {
        return new CallbackFormattingProvider().onJsonFormatForResults(newOnJsonFormatForResults);
    }

    /**
     * Create an {@link FormattingProvider} with a
     * {@link #jsonFormatForRecords()} implementation.
     */
    @NotNull
    public static CallbackFormattingProvider onJsonFormatForRecords(Supplier<? extends JSONFormat> newOnJsonFormatForRecords) {
        return new CallbackFormattingProvider().onJsonFormatForRecords(newOnJsonFormatForRecords);
    }

    /**
     * Create an {@link FormattingProvider} with a
     * {@link #xmlFormatForResults()} implementation.
     */
    @NotNull
    public static CallbackFormattingProvider onXmlFormatForResults(Supplier<? extends XMLFormat> newOnXmlFormatForResults) {
        return new CallbackFormattingProvider().onXmlFormatForResults(newOnXmlFormatForResults);
    }

    /**
     * Create an {@link FormattingProvider} with a {@link #xmlFormatForRecords()}
     * implementation.
     */
    @NotNull
    public static CallbackFormattingProvider onXmlFormatForRecords(Supplier<? extends XMLFormat> newOnXmlFormatForRecords) {
        return new CallbackFormattingProvider().onXmlFormatForRecords(newOnXmlFormatForRecords);
    }

    /**
     * Create an {@link FormattingProvider} with a {@link #chartFormat()}
     * implementation.
     */
    @NotNull
    public static CallbackFormattingProvider onChartFormat(Supplier<? extends ChartFormat> newOnChartFormat) {
        return new CallbackFormattingProvider().onChartFormat(newOnChartFormat);
    }

    /**
     * Create an {@link FormattingProvider} with a {@link #width(String)}
     * implementation.
     */
    @NotNull
    public static CallbackFormattingProvider onWidth(ToIntFunction<? super String> newOnWidth) {
        return new CallbackFormattingProvider().onWidth(newOnWidth);
    }
}
