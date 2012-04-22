/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                          Christopher Deckers, chrriis@gmail.com
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
package org.jooq.debug.console.misc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * @author Christopher Deckers
 */
public class JTableX extends JTable {

	private static final Color GRID_COLOR = new Color(0xd9d9d9);
    private static final Color EVEN_ROW_COLOR = Color.WHITE;
    private static final Color ODD_ROW_COLOR = getTableAlternateRowBackgroundColor();

    public static Color getTableAlternateRowBackgroundColor() {
        Color defaultBackground = UIManager.getDefaults().getColor("Panel.background");
        Color c = new Color((defaultBackground.getRed() + 0xFF) / 2, (defaultBackground.getGreen() + 0xFF) / 2, (defaultBackground.getBlue() + 0xFF) / 2);
        return c;
    }

    public JTableX(TableModel dm) {
        super(dm);
        init();
    }

    public JTableX(final Object[][] rowData, final Object[] columnNames) {
        super(rowData, columnNames);
        init();
    }

    public JTableX() {
        init();
    }

    private void init() {
        setFillsViewportHeight(true);
        setAutoAdjustingColumn(Integer.MAX_VALUE);
        setRowHeight(getRowHeight() + 3);
        setOpaque(false);
		setGridColor(GRID_COLOR);
	}

    public void applyPreferredColumnSizes(int maxWidth) {
        autoFitTableColumn(this, -1, maxWidth, false);
    }

    public void applyMinimumAndPreferredColumnSizes(int maxWidth) {
        autoFitTableColumn(this, -1, maxWidth, true);
    }

    private static void autoFitTableColumn(JTable table, int columnIndex, int maxWidth, boolean isSettingMinimumSize) {
        TableColumnModel columnModel = table.getColumnModel();
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        int rowCount = table.getRowCount();
        int rowMargin = table.getRowMargin();
        int margin = 20;
        for(int i = columnIndex < 0 ? columnModel.getColumnCount() - 1 : columnIndex; i >= 0; i--) {
            TableColumn column = columnModel.getColumn(i);
            int headerWidth;
            headerWidth = headerRenderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, 0, 0).getPreferredSize().width;
            headerWidth += margin;
            int cellWidth = 0;
            for(int j = 0; j < rowCount; j++) {
                Component comp = table.prepareRenderer(table.getDefaultRenderer(table.getColumnClass(i)), j, i);
                int preferredWidth = comp.getPreferredSize().width;
                preferredWidth += 10;
                cellWidth = Math.max(cellWidth, preferredWidth);
                if(Math.max(headerWidth, cellWidth) + rowMargin >= maxWidth) {
                    break;
                }
            }
            int width = Math.min(Math.max(headerWidth, cellWidth) + rowMargin, maxWidth);
            column.setPreferredWidth(width);
            if(isSettingMinimumSize) {
                column.setMinWidth(width);
            }
            if(columnIndex >= 0) {
                break;
            }
        }

    }

    private int autoAdjustingColumn;

    /**
     * Set the column to auto fit, or -1 to disable it. If the column to auto fit is greater or equal to the number of columns, it will auto fit the last visual column.
     */
    public void setAutoAdjustingColumn(int autoAdjustingColumn) {
        this.autoAdjustingColumn = autoAdjustingColumn;
    }

    public int getAutoAdjustingColumn() {
        return autoAdjustingColumn;
    }

    private boolean isAdjusting;

    public void adjustLastColumn() {
        if(isAdjusting) {
            return;
        }
        isAdjusting = true;
        if(autoAdjustingColumn >= 0 && getAutoResizeMode() == JTable.AUTO_RESIZE_OFF) {
            Container p = getParent();
            if (p instanceof JViewport) {
                Container gp = p.getParent();
                if (gp instanceof JScrollPane) {
                    int columnCount = getColumnModel().getColumnCount();
                    if(columnCount > 0) {
                        int autoFitColumn = autoAdjustingColumn;
                        if(autoFitColumn >= columnCount) {
                            autoFitColumn = columnCount - 1;
                        } else {
                            autoFitColumn = convertColumnIndexToView(autoFitColumn);
                        }
                        TableColumn column = getColumnModel().getColumn(autoFitColumn);
                        int minWidth = column.getMinWidth();
                        column.setMinWidth(0);
                        column.setPreferredWidth(0);
                        column.setWidth(0);
                        int diff = p.getSize().width - getPreferredSize().width;
                        column.setMinWidth(minWidth);
                        diff = Math.max(minWidth, diff);
                        column.setPreferredWidth(diff);
                        column.setWidth(diff);
                    }
                }
            }
        }
        isAdjusting = false;
    }

    private PropertyChangeListener listener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            adjustLastColumn();
        }
    };

    @Override
    public void setModel(TableModel dataModel) {
        for (int i=0; i<getColumnModel().getColumnCount(); i++) {
            getColumnModel().getColumn(i).removePropertyChangeListener(listener);
        }
        super.setModel(dataModel);
        for (int i=0; i<getColumnModel().getColumnCount(); i++) {
            getColumnModel().getColumn(i).addPropertyChangeListener(listener);
        }
    }

    @Override
    protected void configureEnclosingScrollPane() {
    	super.configureEnclosingScrollPane();
    	Container p = getParent();
    	if (p instanceof JViewport) {
    		p.setBackground(EVEN_ROW_COLOR);
    		p.addComponentListener(new ComponentAdapter() {
    			@Override
    			public void componentResized(ComponentEvent e) {
    				adjustLastColumn();
    			}
    		});
    	}
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component component = super.prepareRenderer(renderer, row, column);
        // if the renderer is a JComponent and the given row isn't part of a
        // selection, make the renderer non-opaque so that striped rows show
        // through.
        if (component instanceof JComponent) {
            ((JComponent)component).setOpaque(isCellSelected(row, column));
//            ((JComponent)component).setOpaque(getSelectionModel().isSelectedIndex(row));
        }
        return component;
    }

    @Override
    protected void paintComponent(Graphics g) {
        paintStripedBackground(g);
        super.paintComponent(g);
    }

    private void paintStripedBackground(Graphics g) {
        // get the row index at the top of the clip bounds (the first row
        // to paint).
        int rowAtPoint = rowAtPoint(g.getClipBounds().getLocation());
        // get the y coordinate of the first row to paint. if there are no
        // rows in the table, start painting at the top of the supplied
        // clipping bounds.
        if(rowAtPoint < 0) {
            return;
        }
        int topY = getCellRect(rowAtPoint, 0, true).y;
        // create a counter variable to hold the current row. if there are no
        // rows in the table, start the counter at 0.
        int currentRow = rowAtPoint;
        int rowCount = getRowCount();
        while (currentRow < rowCount && topY < g.getClipBounds().y + g.getClipBounds().height) {
            int bottomY = topY + getRowHeight(currentRow);
            g.setColor(getRowBackgroundColor(currentRow));
            g.fillRect(g.getClipBounds().x, topY, g.getClipBounds().width, bottomY - topY);
            topY = bottomY;
            currentRow ++;
        }
    }

    private Color getRowBackgroundColor(int currentRow) {
        return currentRow % 2 != 0? ODD_ROW_COLOR: EVEN_ROW_COLOR;
    }

}
