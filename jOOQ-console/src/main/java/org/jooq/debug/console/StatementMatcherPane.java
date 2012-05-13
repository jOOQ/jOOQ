/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                             Christopher Deckers, chrriis@gmail.com
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.jooq.debug.SqlQueryType;
import org.jooq.debug.StatementMatcher;
import org.jooq.debug.console.misc.TextMatcher;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class StatementMatcherPane extends JPanel {

    private JCheckBox activeCheckBox;
    private JCheckBox threadNameTextMatcherCheckBox;
    private TextMatcherPane threadNameTextMatcherPane;
    private JCheckBox statementTextMatcherCheckBox;
    private TextMatcherPane statementTextMatcherPane;
    private JCheckBox statementTypeCheckBox;
    private JCheckBox statementTypeSelectCheckBox;
    private JCheckBox statementTypeUpdateCheckBox;
    private JCheckBox statementTypeInsertCheckBox;
    private JCheckBox statementTypeDeleteCheckBox;
    private JCheckBox statementTypeOtherCheckBox;

    public StatementMatcherPane(final StatementMatchersPane statementMatchersPane, StatementMatcher statementMatcher) {
        super(new GridBagLayout());
        setBorder(BorderFactory.createLineBorder(getBackground().darker()));
        if(statementMatcher == null) {
            statementMatcher = new StatementMatcher(null, null, null, true);
        }
        int y = 0;
        JPanel northPane = new JPanel(new GridBagLayout());
        activeCheckBox = new JCheckBox("Active", statementMatcher.isActive());
        activeCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        northPane.add(activeCheckBox, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        JToolBar closeButtonPane = new JToolBar();
        closeButtonPane.setFloatable(false);
        JButton closeButton = new JButton(new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/TabCloseActive14.png")));
        closeButton.setFocusable(false);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statementMatchersPane.removeStatementMatcherPane(StatementMatcherPane.this);
            }
        });
        closeButtonPane.add(closeButton);
        northPane.add(closeButtonPane, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        add(northPane, new GridBagConstraints(0, y, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        y++;
        TextMatcher statementTextMatcher = statementMatcher.getStatementTextMatcher();
        statementTextMatcherCheckBox = new JCheckBox("Statement", statementTextMatcher != null);
        statementTextMatcherCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        add(statementTextMatcherCheckBox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        statementTextMatcherPane = new TextMatcherPane(statementTextMatcher);
        add(statementTextMatcherPane, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        y++;
        Set<SqlQueryType> queryTypeSet = statementMatcher.getQueryTypeSet();
        statementTypeCheckBox = new JCheckBox("Type", queryTypeSet != null);
        statementTypeCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        add(statementTypeCheckBox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        JPanel typesPane = new JPanel(new GridBagLayout());
        statementTypeSelectCheckBox = new JCheckBox("SELECT", queryTypeSet != null && queryTypeSet.contains(SqlQueryType.SELECT));
        typesPane.add(statementTypeSelectCheckBox, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        statementTypeUpdateCheckBox = new JCheckBox("UPDATE", queryTypeSet != null && queryTypeSet.contains(SqlQueryType.UPDATE));
        typesPane.add(statementTypeUpdateCheckBox, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
        statementTypeInsertCheckBox = new JCheckBox("INSERT", queryTypeSet != null && queryTypeSet.contains(SqlQueryType.INSERT));
        typesPane.add(statementTypeInsertCheckBox, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
        statementTypeDeleteCheckBox = new JCheckBox("DELETE", queryTypeSet != null && queryTypeSet.contains(SqlQueryType.DELETE));
        typesPane.add(statementTypeDeleteCheckBox, new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
        statementTypeOtherCheckBox = new JCheckBox("OTHER", queryTypeSet != null && queryTypeSet.contains(SqlQueryType.OTHER));
        typesPane.add(statementTypeOtherCheckBox, new GridBagConstraints(4, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
        add(typesPane, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        y++;
        TextMatcher threadNameTextMatcher = statementMatcher.getThreadNameTextMatcher();
        threadNameTextMatcherCheckBox = new JCheckBox("Thread name", threadNameTextMatcher != null);
        threadNameTextMatcherCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        add(threadNameTextMatcherCheckBox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        threadNameTextMatcherPane = new TextMatcherPane(threadNameTextMatcher);
        add(threadNameTextMatcherPane, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        adjustStates();
    }

    private void adjustStates() {
        boolean isActive = activeCheckBox.isSelected();
        statementTextMatcherCheckBox.setEnabled(isActive);
        statementTypeCheckBox.setEnabled(isActive);
        threadNameTextMatcherCheckBox.setEnabled(isActive);
        statementTextMatcherPane.setLocked(!isActive || !statementTextMatcherCheckBox.isSelected());
        statementTypeSelectCheckBox.setEnabled(isActive && statementTypeCheckBox.isSelected());
        statementTypeUpdateCheckBox.setEnabled(isActive && statementTypeCheckBox.isSelected());
        statementTypeInsertCheckBox.setEnabled(isActive && statementTypeCheckBox.isSelected());
        statementTypeDeleteCheckBox.setEnabled(isActive && statementTypeCheckBox.isSelected());
        statementTypeOtherCheckBox.setEnabled(isActive && statementTypeCheckBox.isSelected());
        threadNameTextMatcherPane.setLocked(!isActive || !threadNameTextMatcherCheckBox.isSelected());
    }

    public StatementMatcher getStatementMatcher() {
        boolean isActive = activeCheckBox.isSelected();
        TextMatcher threadNameTextMatcher = threadNameTextMatcherCheckBox.isSelected()? threadNameTextMatcherPane.getTextMatcher(): null;
        TextMatcher statementTextMatcher = statementTextMatcherCheckBox.isSelected()? statementTextMatcherPane.getTextMatcher(): null;
        Set<SqlQueryType> queryTypeSet;
        if(statementTypeCheckBox.isSelected()) {
            List<SqlQueryType> typeList = new ArrayList<SqlQueryType>();
            if(statementTypeSelectCheckBox.isSelected()) {
                typeList.add(SqlQueryType.SELECT);
            }
            if(statementTypeUpdateCheckBox.isSelected()) {
                typeList.add(SqlQueryType.UPDATE);
            }
            if(statementTypeInsertCheckBox.isSelected()) {
                typeList.add(SqlQueryType.INSERT);
            }
            if(statementTypeDeleteCheckBox.isSelected()) {
                typeList.add(SqlQueryType.DELETE);
            }
            if(statementTypeOtherCheckBox.isSelected()) {
                typeList.add(SqlQueryType.OTHER);
            }
            queryTypeSet = EnumSet.copyOf(typeList);
        } else {
            queryTypeSet = null;
        }
        return new StatementMatcher(threadNameTextMatcher, statementTextMatcher, queryTypeSet, isActive);
    }

}
