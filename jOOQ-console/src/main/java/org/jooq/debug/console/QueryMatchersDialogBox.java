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
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jooq.debug.Debugger;
import org.jooq.debug.console.LoggerPane.LoggerPaneLoggingListener;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class QueryMatchersDialogBox extends JDialog {

    public QueryMatchersDialogBox(Component parent, final Debugger debugger, final LoggerPaneLoggingListener listener) {
        super(SwingUtilities.getWindowAncestor(parent), "Query filters", ModalityType.DOCUMENT_MODAL);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();
        JPanel northPane = new JPanel(new BorderLayout());
        northPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 2, 5));
        northPane.add(new JLabel("Log queries matching any of these filters:"), BorderLayout.WEST);
        contentPane.add(northPane, BorderLayout.NORTH);
        final QueryMatchersPane matchersPane = new QueryMatchersPane(listener.getMatchers());
        matchersPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        contentPane.add(matchersPane, BorderLayout.CENTER);
        JPanel buttonBar = new JPanel(new BorderLayout());
        buttonBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel filterButtonsPane = new JPanel(new GridLayout(1, 2, 2, 0));
        JButton addFilterButton = new JButton("Add filter");
        addFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                matchersPane.addQueryMatcherPane(new QueryMatcherPane(matchersPane, null));
            }
        });
        filterButtonsPane.add(addFilterButton);
        JButton removeAllFiltersButton = new JButton("Remove all");
        removeAllFiltersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                matchersPane.removeAllQueryMatcherPanes();
            }
        });
        filterButtonsPane.add(removeAllFiltersButton);
        buttonBar.add(filterButtonsPane, BorderLayout.WEST);
        JPanel rightButtonsPane = new JPanel(new GridLayout(1, 2, 2, 0));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.setMatchers(matchersPane.getMatchers());
                debugger.setLoggingListener(listener);
                dispose();
            }
        });
        rightButtonsPane.add(okButton);
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.setMatchers(matchersPane.getMatchers());
                debugger.setLoggingListener(listener);
            }
        });
        rightButtonsPane.add(applyButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        rightButtonsPane.add(cancelButton);
        buttonBar.add(rightButtonsPane, BorderLayout.EAST);
        contentPane.add(buttonBar, BorderLayout.SOUTH);
        setSize(600, 400);
        setLocationRelativeTo(SwingUtilities.getWindowAncestor(parent));
    }

}
