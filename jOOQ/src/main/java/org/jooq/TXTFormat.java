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

/**
 * A CSV formatting type, which can be used to configure CSV imports / exports.
 *
 * @author Lukas Eder
 */
public final class TXTFormat {

    public static final TXTFormat DEFAULT = new TXTFormat();

    final int                     maxRows;
    final int                     minColWidth;
    final int                     maxColWidth;
    final boolean                 horizontalTableBorder;
    final boolean                 horizontalHeaderBorder;
    final boolean                 horizontalCellBorder;
    final boolean                 verticalTableBorder;
    final boolean                 verticalCellBorder;
    final boolean                 intersectLines;

    public TXTFormat() {
        this(
            Integer.MAX_VALUE,
            4,
            Integer.MAX_VALUE,
            true,
            true,
            false,
            true,
            true,
            true
        );
    }

    private TXTFormat(
        int maxRows,
        int minColWidth,
        int maxColWidth,
        boolean horizontalTableBorder,
        boolean horizontalHeaderBorder,
        boolean horizontalCellBorder,
        boolean verticalTableBorder,
        boolean verticalCellBorder,
        boolean intersectLines
    ) {
        this.maxRows = maxRows;
        this.minColWidth = minColWidth;
        this.maxColWidth = maxColWidth;
        this.horizontalTableBorder = horizontalTableBorder;
        this.horizontalHeaderBorder = horizontalHeaderBorder;
        this.horizontalCellBorder = horizontalCellBorder;
        this.verticalTableBorder = verticalTableBorder;
        this.verticalCellBorder = verticalCellBorder;
        this.intersectLines = intersectLines;
    }

    /**
     * The maximum number of rows to be included in the format, defaulting to all rows.
     */
    public TXTFormat maxRows(int newMaxRows) {
        return new TXTFormat(
            newMaxRows,
            minColWidth,
            maxColWidth,
            horizontalTableBorder,
            horizontalHeaderBorder,
            horizontalCellBorder,
            verticalTableBorder,
            verticalCellBorder,
            intersectLines
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
            maxColWidth,
            horizontalTableBorder,
            horizontalHeaderBorder,
            horizontalCellBorder,
            verticalTableBorder,
            verticalCellBorder,
            intersectLines
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
            newMaxColWidth,
            horizontalTableBorder,
            horizontalHeaderBorder,
            horizontalCellBorder,
            verticalTableBorder,
            verticalCellBorder,
            intersectLines
        );
    }

    /**
     * The maximum column width, defaulting to no limit.
     */
    public int maxColWidth() {
        return maxColWidth;
    }

    /**
     * Whether the horizontal table border (top and bottom line) should be
     * displayed.
     */
    public TXTFormat horizontalTableBorder(boolean newHorizontalTableBorder) {
        return new TXTFormat(
            maxRows,
            minColWidth,
            maxColWidth,
            newHorizontalTableBorder,
            horizontalHeaderBorder,
            horizontalCellBorder,
            verticalTableBorder,
            verticalCellBorder,
            intersectLines
        );
    }

    /**
     * Whether the horizontal table border (top and bottom line) should be
     * displayed.
     */
    public boolean horizontalTableBorder() {
        return horizontalTableBorder;
    }

    /**
     * Whether the horizontal header border (line between header and data cells)
     * should be displayed.
     */
    public TXTFormat horizontalHeaderBorder(boolean newHorizontalHeaderBorder) {
        return new TXTFormat(
            maxRows,
            minColWidth,
            maxColWidth,
            horizontalTableBorder,
            newHorizontalHeaderBorder,
            horizontalCellBorder,
            verticalTableBorder,
            verticalCellBorder,
            intersectLines
        );
    }

    /**
     * Whether the horizontal header border (line between header and data cells)
     * should be displayed.
     */
    public boolean horizontalHeaderBorder() {
        return horizontalHeaderBorder;
    }

    /**
     * Whether the horizontal cell border (line between data cells) should be
     * displayed.
     */
    public TXTFormat horizontalCellBorder(boolean newHorizontalCellBorder) {
        return new TXTFormat(
            maxRows,
            minColWidth,
            maxColWidth,
            horizontalTableBorder,
            horizontalHeaderBorder,
            newHorizontalCellBorder,
            verticalTableBorder,
            verticalCellBorder,
            intersectLines
        );
    }

    /**
     * Whether the horizontal cell border (line between data cells) should be
     * displayed.
     */
    public boolean horizontalCellBorder() {
        return horizontalCellBorder;
    }

    /**
     * Whether the vertical table border (left and right most lines) should be
     * displayed.
     */
    public TXTFormat verticalTableBorder(boolean newVerticalTableBorder) {
        return new TXTFormat(
            maxRows,
            minColWidth,
            maxColWidth,
            horizontalTableBorder,
            horizontalHeaderBorder,
            horizontalCellBorder,
            newVerticalTableBorder,
            verticalCellBorder,
            intersectLines
        );
    }

    /**
     * Whether the vertical table border (left and right most lines) should be
     * displayed.
     */
    public boolean verticalTableBorder() {
        return verticalTableBorder;
    }

    /**
     * Whether the vertical cell borders (lines between data cells) should be
     * displayed.
     */
    public TXTFormat verticalCellBorder(boolean newVerticalCellBorder) {
        return new TXTFormat(
            maxRows,
            minColWidth,
            maxColWidth,
            horizontalTableBorder,
            horizontalHeaderBorder,
            horizontalCellBorder,
            verticalTableBorder,
            newVerticalCellBorder,
            intersectLines
        );
    }

    /**
     * Whether the vertical cell borders (lines between data cells) should be
     * displayed.
     */
    public boolean verticalCellBorder() {
        return verticalCellBorder;
    }

    /**
     * Whether horizontal and vertical lines should be intersected with a
     * <code>'+'</code> symbol.
     */
    public TXTFormat intersectLines(boolean newIntersectLines) {
        return new TXTFormat(
            maxRows,
            minColWidth,
            maxColWidth,
            horizontalTableBorder,
            horizontalHeaderBorder,
            horizontalCellBorder,
            verticalTableBorder,
            verticalCellBorder,
            newIntersectLines
        );
    }

    /**
     * Whether horizontal and vertical lines should be intersected with a
     * <code>'+'</code> symbol.
     */
    public boolean intersectLines() {
        return intersectLines;
    }
}
