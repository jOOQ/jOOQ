/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.explorer;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * {@link HBoxResizer} can be used to add mouse listeners to a {@link Region}
 * and make it resizable by the user by clicking and dragging the border in the
 * same way as a window.
 */
@SuppressWarnings("restriction")
public class HBoxResizer {

    /**
     * The margin around the control that a user can click in to start resizing
     * the region.
     */
    private static final int RESIZE_MARGIN = 5;

    private final HBox       hbox;
    private Region           dragging;
    private double           x;

    private HBoxResizer(HBox hbox) {
        this.hbox = hbox;

        ObservableList<Node> children = hbox.getChildren();
        for (int i = 1; i < children.size(); i++) {
            Region child = (Region) children.get(i);
            Insets margin = HBox.getMargin(child);
            if (margin == null)
                margin = Insets.EMPTY;

            HBox.setMargin(child, new Insets(margin.getTop(), margin.getRight(), margin.getBottom(), margin.getLeft() + RESIZE_MARGIN));
        }
    }

    public static void makeResizable(HBox hbox) {
        final HBoxResizer resizer = new HBoxResizer(hbox);

        hbox.setOnMousePressed(resizer::mousePressed);
        hbox.setOnMouseDragged(resizer::mouseDragged);
        hbox.setOnMouseMoved(resizer::mouseOver);
        hbox.setOnMouseEntered(resizer::mouseOver);
        hbox.setOnMouseExited(resizer::mouseOver);
        hbox.setOnMouseReleased(resizer::mouseReleased);
    }

    protected void mouseReleased(MouseEvent event) {
        dragging = null;
        hbox.setCursor(Cursor.DEFAULT);
    }

    protected void mouseOver(MouseEvent event) {
        if (dragging != null || draggableRegion(event) != null)
            hbox.setCursor(Cursor.E_RESIZE);
        else
            hbox.setCursor(Cursor.DEFAULT);
    }

    protected Region draggableRegion(MouseEvent event) {
        ObservableList<Node> children = hbox.getChildren();

        Region result;
        double width = 0;
        for (int i = 0; i < children.size() - 1; i++) {
            result = (Region) children.get(i);
            width += result.getWidth();

            if (event.getX() >= width && event.getX() <= width + RESIZE_MARGIN)
                return result;

            width +=  RESIZE_MARGIN;
        }

        return null;
    }

    protected void mouseDragged(MouseEvent event) {
        if (dragging == null)
            return;

        double newX = event.getX();
        double newWidth = dragging.getPrefWidth() + (newX - x);
        dragging.setPrefWidth(newWidth);
        x = newX;
    }

    protected void mousePressed(MouseEvent event) {
        dragging = draggableRegion(event);
        x = event.getX();
    }
}