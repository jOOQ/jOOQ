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

import java.text.DecimalFormat;

import org.jetbrains.annotations.NotNull;

/**
 * A CSV formatting type, which can be used to configure chart exports.
 * <p>
 * The type is immutable, meaning calls to setters like {@link #width(int)} do
 * not modify the original reference, but return a new one instead.
 *
 * @author Lukas Eder
 */
public final class ChartFormat {

    public static final ChartFormat DEFAULT      = new ChartFormat();

    final Output                    output;
    final Type                      type;
    final Display                   display;
    final int                       width;
    final int                       height;
    final int                       category;
    final boolean                   categoryAsText;
    final int[]                     values;
    final char[]                    shades;
    final boolean                   showHorizontalLegend;
    final boolean                   showVerticalLegend;
    final String                    newline;
    final DecimalFormat             numericFormat;
    final DecimalFormat             percentFormat;

    public ChartFormat() {
        this(
            Output.ASCII,
            Type.AREA,
            Display.STACKED,
            80,
            25,
            0,
            true,
            new int[] { 1 },
            new char[] { '█', '▓', '▒', '░' },
            true,
            true,
            "\n",
            new DecimalFormat("###,###.00"),
            new DecimalFormat("##0.00'%'")
        );
    }

    private ChartFormat(
        Output output,
        Type type,
        Display display,
        int width,
        int height,
        int category,
        boolean categoryAsText,
        int[] values,
        char[] shades,
        boolean showHorizontalLegend,
        boolean showVerticalLegend,
        String newline,
        DecimalFormat numericFormat,
        DecimalFormat percentFormat
    ) {
        this.output = output;
        this.type = type;
        this.display = display;
        this.width = width;
        this.height = height;
        this.category = category;
        this.categoryAsText = categoryAsText;
        this.values = values;
        this.shades = shades;
        this.showHorizontalLegend = showHorizontalLegend;
        this.showVerticalLegend = showVerticalLegend;
        this.newline = newline;
        this.numericFormat = numericFormat;
        this.percentFormat = percentFormat;
    }

    /**
     * The new output format, defaulting to {@link Output#ASCII}.
     */
    @NotNull
    public ChartFormat output(Output newOutput) {
        return new ChartFormat(
            newOutput,
            type,
            display,
            width,
            height,
            category,
            categoryAsText,
            values,
            shades,
            showHorizontalLegend,
            showVerticalLegend,
            newline,
            numericFormat,
            percentFormat
        );
    }

    /**
     * The output format.
     */
    @NotNull
    public Output output() {
        return output;
    }

    /**
     * The new chart type, defaulting to {@link Type#AREA}.
     */
    @NotNull
    public ChartFormat type(Type newType) {
        return new ChartFormat(
            output,
            newType,
            display,
            width,
            height,
            category,
            categoryAsText,
            values,
            shades,
            showHorizontalLegend,
            showVerticalLegend,
            newline,
            numericFormat,
            percentFormat
        );
    }

    @NotNull
    public Type type() {
        return type;
    }

    /**
     * The new display format, defaulting to {@link Display#STACKED}.
     */
    @NotNull
    public ChartFormat display(Display newDisplay) {
        return new ChartFormat(
            output,
            type,
            newDisplay,
            width,
            height,
            category,
            categoryAsText,
            values,
            shades,
            showHorizontalLegend,
            showVerticalLegend,
            newline,
            numericFormat,
            percentFormat
        );
    }

    /**
     * The display format.
     */
    @NotNull
    public Display display() {
        return display;
    }

    /**
     * The new chart dimensions, defaulting to <code>80 x 25</code>.
     */
    @NotNull
    public ChartFormat dimensions(int newWidth, int newHeight) {
        return new ChartFormat(
            output,
            type,
            display,
            newWidth,
            newHeight,
            category,
            categoryAsText,
            values,
            shades,
            showHorizontalLegend,
            showVerticalLegend,
            newline,
            numericFormat,
            percentFormat
        );
    }

    /**
     * The new chart width, defaulting to <code>80</code>.
     */
    @NotNull
    public ChartFormat width(int newWidth) {
        return dimensions(newWidth, height);
    }

    /**
     * The chart width.
     */
    public int width() {
        return width;
    }

    /**
     * The new chart height, defaulting to <code>25</code>.
     */
    @NotNull
    public ChartFormat height(int newHeight) {
        return dimensions(width, newHeight);
    }

    /**
     * The chart height.
     */
    public int height() {
        return height;
    }

    /**
     * The new category source column number, defaulting to <code>0</code>.
     */
    @NotNull
    public ChartFormat category(int newCategory) {
        return new ChartFormat(
            output,
            type,
            display,
            width,
            height,
            newCategory,
            categoryAsText,
            values,
            shades,
            showHorizontalLegend,
            showVerticalLegend,
            newline,
            numericFormat,
            percentFormat
        );
    }

    /**
     * The category source column number.
     */
    public int category() {
        return category;
    }

    /**
     * The new category as text value, defaulting to <code>true</code>.
     */
    @NotNull
    public ChartFormat categoryAsText(boolean newCategoryAsText) {
        return new ChartFormat(
            output,
            type,
            display,
            width,
            height,
            category,
            newCategoryAsText,
            values,
            shades,
            showHorizontalLegend,
            showVerticalLegend,
            newline,
            numericFormat,
            percentFormat
        );
    }

    /**
     * The category as text value.
     */
    public boolean categoryAsText() {
        return categoryAsText;
    }

    /**
     * The new value source column numbers, defaulting to <code>{ 1 }</code>.
     */
    @NotNull
    public ChartFormat values(int... newValues) {
        return new ChartFormat(
            output,
            type,
            display,
            width,
            height,
            category,
            categoryAsText,
            newValues,
            shades,
            showHorizontalLegend,
            showVerticalLegend,
            newline,
            numericFormat,
            percentFormat
        );
    }

    /**
     * The value source column numbers.
     */
    public int @NotNull [] values() {
        return values;
    }

    /**
     * The new column shades, defaulting to <code>{ 'X' }</code>.
     */
    @NotNull
    public ChartFormat shades(char... newShades) {
        return new ChartFormat(
            output,
            type,
            display,
            width,
            height,
            category,
            categoryAsText,
            values,
            newShades,
            showHorizontalLegend,
            showVerticalLegend,
            newline,
            numericFormat,
            percentFormat
        );
    }

    /**
     * The value column shades.
     */
    public char @NotNull [] shades() {
        return shades;
    }

    /**
     * Whether to show legends, defaulting to <code>true</code>.
     */
    @NotNull
    public ChartFormat showLegends(boolean newShowHorizontalLegend, boolean newShowVerticalLegend) {
        return new ChartFormat(
            output,
            type,
            display,
            width,
            height,
            category,
            categoryAsText,
            values,
            shades,
            newShowHorizontalLegend,
            newShowVerticalLegend,
            newline,
            numericFormat,
            percentFormat
        );
    }

    /**
     * Whether to show the horizontal legend, defaulting to <code>true</code>.
     */
    @NotNull
    public ChartFormat showHorizontalLegend(boolean newShowHorizontalLegend) {
        return showLegends(newShowHorizontalLegend, showVerticalLegend);
    }

    /**
     * Whether to show the horizontal legend.
     */
    public boolean showHorizontalLegend() {
        return showHorizontalLegend;
    }

    /**
     * Whether to show the vertical legend, defaulting to <code>true</code>.
     */
    @NotNull
    public ChartFormat showVerticalLegend(boolean newShowVerticalLegend) {
        return showLegends(showHorizontalLegend, newShowVerticalLegend);
    }

    /**
     * Whether to show the vertical legend.
     */
    public boolean showVerticalLegend() {
        return showVerticalLegend;
    }

    /**
     * The new newline character, defaulting to <code>\n</code>.
     */
    @NotNull
    public ChartFormat newline(String newNewline) {
        return new ChartFormat(
            output,
            type,
            display,
            width,
            height,
            category,
            categoryAsText,
            values,
            shades,
            showHorizontalLegend,
            showVerticalLegend,
            newNewline,
            numericFormat,
            percentFormat
        );
    }

    /**
     * The newline character.
     */
    @NotNull
    public String newline() {
        return newline;
    }

    /**
     * The new numeric format, defaulting to <code>###,###.00</code>.
     */
    @NotNull
    public ChartFormat numericFormat(DecimalFormat newNumericFormat) {
        return new ChartFormat(
            output,
            type,
            display,
            width,
            height,
            category,
            categoryAsText,
            values,
            shades,
            showHorizontalLegend,
            showVerticalLegend,
            newline,
            newNumericFormat,
            percentFormat
        );
    }

    /**
     * The numeric format.
     */
    @NotNull
    public DecimalFormat numericFormat() {
        return numericFormat;
    }

    /**
     * The new numeric format for percentages, defaulting to <code>###.00'%'</code>.
     */
    @NotNull
    public ChartFormat percentFormat(DecimalFormat newPercentFormat) {
        return new ChartFormat(
            output,
            type,
            display,
            width,
            height,
            category,
            categoryAsText,
            values,
            shades,
            showHorizontalLegend,
            showVerticalLegend,
            newline,
            numericFormat,
            newPercentFormat
        );
    }

    /**
     * The numeric format for percentages.
     */
    @NotNull
    public DecimalFormat percentFormat() {
        return percentFormat;
    }

    /**
     * The chart output format.
     */
    public enum Output {

        /**
         * An ASCII chart.
         */
        ASCII,

//        /**
//         * An ANSI escape sequenced chart.
//         */
//        ANSI,
//
//        /**
//         * An SVG chart.
//         */
//        SVG,

    }

    public static enum Type {

        /**
         * An area chart.
         */
        AREA,
    }

    public static enum Display {

        /**
         * The areas are located in front of one another.
         */
        @SuppressWarnings("hiding")
        DEFAULT,

        /**
         * The areas are stacked on top of one another.
         */
        STACKED,

        /**
         * The areas stack up to 100%.
         */
        HUNDRED_PERCENT_STACKED
    }
}
