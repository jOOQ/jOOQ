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
package org.jooq;

/**
 * A CSV formatting type, which can be used to configure CSV imports / exports.
 *
 * @author Lukas Eder
 */
public final class TXTFormat {

    final int maxRows;
    final int minColWidth;
    final int maxColWidth;

    public TXTFormat() {
        this(
            Integer.MAX_VALUE,
            4,
            Integer.MAX_VALUE
        );
    }

    private TXTFormat(
        int maxRows,
        int minColWidth,
        int maxColWidth
    ) {
        this.maxRows = maxRows;
        this.minColWidth = minColWidth;
        this.maxColWidth = maxColWidth;
    }

    /**
     * The maximum number of rows to be included in the format, defaulting to all rows.
     */
    public TXTFormat maxRows(int newMaxRows) {
        return new TXTFormat(
            newMaxRows,
            minColWidth,
            maxColWidth
        );
    }

    /**
     * The maximum number of rows to be included in the format, defaulting to all rows.
     */
    public int maxRows() {
        return maxRows;
    }

    /**
     * The minimum column width, defaulting to 4
     */
    public TXTFormat minColWidth(int newMinColWidth) {
        return new TXTFormat(
            maxRows,
            newMinColWidth,
            maxColWidth
        );
    }

    /**
     * The minimum column width, defaulting to 4
     */
    public int minColWidth() {
        return minColWidth;
    }


    /**
     * The minimum column width, defaulting to no limit.
     */
    public TXTFormat maxColWidth(int newMaxColWidth) {
        return new TXTFormat(
            maxRows,
            minColWidth,
            newMaxColWidth
        );
    }

    /**
     * The maximum column width, defaulting to no limit.
     */
    public int maxColWidth() {
        return maxColWidth;
    }
}
