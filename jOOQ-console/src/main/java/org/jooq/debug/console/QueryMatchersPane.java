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
package org.jooq.debug.console;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

import org.jooq.debug.QueryMatcher;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class QueryMatchersPane extends JPanel {

    private static class ScrollablePane extends JPanel implements Scrollable {

        public ScrollablePane() {
            super(new GridLayout(0, 1, 1, 1));
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 100;
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 10;
        }

    }

    private JPanel matcherPanesContainer;

    public QueryMatchersPane(QueryMatcher[] matchers) {
        super(new BorderLayout());
        matcherPanesContainer = new ScrollablePane();
        matcherPanesContainer.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        addDefaultMessageComponent();
        add(new JScrollPane(matcherPanesContainer), BorderLayout.CENTER);
        if (matchers != null) {
            for (QueryMatcher matcher : matchers) {
                addQueryMatcherPane(new QueryMatcherPane(this, matcher));
            }
        }
    }

    public void addQueryMatcherPane(QueryMatcherPane pane) {
        if(!(matcherPanesContainer.getComponent(0) instanceof QueryMatcherPane)) {
            matcherPanesContainer.remove(0);
        }
        matcherPanesContainer.add(pane);
        matcherPanesContainer.revalidate();
        matcherPanesContainer.repaint();
        matcherPanesContainer.scrollRectToVisible(new Rectangle(0, Short.MAX_VALUE, 1, 1));
    }

    void removeAllQueryMatcherPanes() {
        matcherPanesContainer.removeAll();
        addDefaultMessageComponent();
        matcherPanesContainer.revalidate();
        matcherPanesContainer.repaint();
    }

    private void addDefaultMessageComponent() {
        JLabel messageLabel = new JLabel("No query filters, logging everything.");
        messageLabel.setFont(messageLabel.getFont().deriveFont(Font.ITALIC));
        matcherPanesContainer.add(messageLabel);
    }

    void removeQueryMatcherPane(QueryMatcherPane pane) {
        matcherPanesContainer.remove(pane);
        if (matcherPanesContainer.getComponentCount() == 0) {
            addDefaultMessageComponent();
        }
        matcherPanesContainer.revalidate();
        matcherPanesContainer.repaint();
    }

    public QueryMatcher[] getMatchers() {
        if (!(matcherPanesContainer.getComponent(0) instanceof QueryMatcherPane)) {
            return new QueryMatcher[0];
        }
        Component[] components = matcherPanesContainer.getComponents();
        QueryMatcher[] matchers = new QueryMatcher[components.length];
        for (int i = 0; i < components.length; i++) {
            matchers[i] = ((QueryMatcherPane) components[i]).getQueryMatcher();
        }
        return matchers;
    }
}
