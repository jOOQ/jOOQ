/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseEvent;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public class TreeDataTip {

    private TreeDataTip() {}

    public static void activate(final JTree tree) {
        MouseInputListener dataTipListener = new MouseInputAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                showTip(e.getPoint());
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                showTip(e.getPoint());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hideTip();
            }
            @Override
            public void mousePressed(MouseEvent e) {
                hideTip();
            }
            private Popup popup;
            private int currentRow = -1;
            private void hideTip() {
                if(popup != null) {
                    popup.hide();
                    popup = null;
                }
            }
            private void showTip(Point mousePosition) {
                int row = tree.getRowForLocation(mousePosition.x, mousePosition.y);
                if(row != currentRow) {
                    hideTip();
                    currentRow = row;
                    if(row >= 0) {
                        TreePath treePath = tree.getPathForRow(row);
                        TreeCellRenderer renderer = tree.getCellRenderer();
                        boolean isSelected = false;//breakpointTree.isPathSelected(treePath);
                        boolean isExpanded = tree.isExpanded(treePath);
                        boolean hasFocus = false;//tree.hasFocus() && rowIndex == tree.getLeadSelectionRow();
                        Object item = treePath.getLastPathComponent();
                        boolean isLeaf = tree.getModel().isLeaf(item);
                        final JComponent rendererComponent = (JComponent)renderer.getTreeCellRendererComponent(tree, item, isSelected, isExpanded, isLeaf, row, hasFocus);
                        rendererComponent.setFont(tree.getFont());
                        Rectangle visRect = tree.getVisibleRect();
                        Rectangle cellBounds = tree.getPathBounds(treePath);
                        Rectangle visibleCellRectangle = cellBounds.intersection(visRect);
                        if (!visibleCellRectangle.contains(mousePosition)) {
                            return;
                        }
                        Dimension rendCompDim = rendererComponent.getMinimumSize();
                        Rectangle rendCompBounds = new Rectangle(cellBounds.getLocation(), rendCompDim);
                        visRect.x -= 1000;
                        visRect.width += 1000;
                        visRect.y -= 1000;
                        visRect.height += 2000;
                        // We do not care about vertical visibility.
                        rendCompBounds.height = 1;
                        if(cellBounds.contains(rendCompBounds) && visRect.contains(rendCompBounds)) {
                            return;
                        }
                        Dimension preferredSize = rendererComponent.getPreferredSize();
                        Point tipPosition = cellBounds.getLocation();
                        int width = Math.max(cellBounds.width, preferredSize.width);
                        int height = cellBounds.height;//Math.max(cellBounds.height, preferredSize.height); // We don't care about vertical visibility
                        Dimension tipDimension = new Dimension(width, height);
                        final Color backgroundColor = tree.getBackground();
                        class DataTipComponent extends JToolTip {
                            private CellRendererPane rendererPane = new CellRendererPane();
                            private boolean isHeavyWeight;
                            public DataTipComponent() {
                                add(rendererPane);
                                setFocusable(false);
                                setBorder(null);
                                enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK);
                            }
                            @Override
                            public void updateUI() {
                            }
                            @Override
                            public boolean contains(int x, int y) {
                                return isHeavyWeight;
                            }
                            @SuppressWarnings("hiding")
                            @Override
                            public void paintComponent(Graphics g) {
                                // Leave the component's opacity settings as is, just paint the background myself.
                                // This seems to be the only viable solution: DefaultTableCellRenderer overrides isOpaque() and returns
                                // true only if renderer color does not equal parent color. The problem is that rendererPane.paintComponent()
                                // re-parents the renderer.
                                g.setColor(backgroundColor);
                                int width = getWidth();
                                int height = getHeight();
                                g.fillRect(0, 0, width, height);
                                g.setColor(Color.black);
                                g.drawRect(0, 0, width - 1, height - 1);
                                g.setClip(1, 1, width - 2, height - 2);
                                int row = currentRow;
                                TreePath treePath = tree.getPathForRow(row);
                                boolean isSelected = false;//breakpointTree.isPathSelected(treePath);
                                boolean isExpanded = tree.isExpanded(treePath);
                                boolean hasFocus = false;//tree.hasFocus() && rowIndex == tree.getLeadSelectionRow();
                                Object item = treePath.getLastPathComponent();
                                boolean isLeaf = tree.getModel().isLeaf(item);
                                TreeCellRenderer renderer = tree.getCellRenderer();
                                JComponent rendererComponent = (JComponent)renderer.getTreeCellRendererComponent(tree, item, isSelected, isExpanded, isLeaf, row, hasFocus);
                                rendererComponent.setFont(tree.getFont());
                                rendererPane.paintComponent(g, rendererComponent, this, 1, 1, width - 1, height - 1);
                            }
                            public void setHeavyWeight(boolean isHeavyWeight) {
                                this.isHeavyWeight = isHeavyWeight;
                            }
                        }
                        DataTipComponent dataTipComponent = new DataTipComponent();
                        Dimension tipDimensionClipped = new Dimension(tipDimension.width, tipDimension.height);
                        Window windowAncestor = SwingUtilities.getWindowAncestor(tree);
                        GraphicsConfiguration gc = windowAncestor.getGraphicsConfiguration();
                        Rectangle screenBounds = gc.getBounds();
                        // Chrriis: we add the border around, not in
                        Point tipScreenPosition = new Point(tipPosition.x, tipPosition.y);
                        SwingUtilities.convertPointToScreen(tipScreenPosition, tree);
                        Point tipPositionClipped = new Point();
                        tipPositionClipped.x = Math.max(tipScreenPosition.x - 1, screenBounds.x);
                        tipPositionClipped.y = Math.max(tipScreenPosition.y - 1, screenBounds.y);
                        // Chrriis: we add the border around, not in
                        tipDimensionClipped.width = Math.min(screenBounds.x + screenBounds.width - tipPositionClipped.x, tipDimensionClipped.width + 2);
                        tipDimensionClipped.height = Math.min(screenBounds.y + screenBounds.height - tipPositionClipped.y, tipDimensionClipped.height + 2);
                        SwingUtilities.convertPointFromScreen(tipPositionClipped, tree);
                        dataTipComponent.setPreferredSize(tipDimensionClipped);
                        SwingUtilities.convertPointToScreen(tipPosition, tree);
                        PopupFactory popupFactory = PopupFactory.getSharedInstance();
                        popup = popupFactory.getPopup(tree, dataTipComponent, tipPosition.x - 1, tipPosition.y - 1);
                        popup.show();
                        Window componentWindow = SwingUtilities.windowForComponent(tree);
                        Window tipWindow = SwingUtilities.windowForComponent(dataTipComponent);
                        boolean isHeavyWeight = tipWindow != null && tipWindow != componentWindow;
                        dataTipComponent.setHeavyWeight(isHeavyWeight);
                    }
                }
            }
        };
        tree.addMouseListener(dataTipListener);
        tree.addMouseMotionListener(dataTipListener);
    }

}
