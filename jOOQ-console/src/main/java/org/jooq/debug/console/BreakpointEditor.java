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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import org.jooq.debug.Breakpoint;
import org.jooq.debug.QueryMatcher;
import org.jooq.debug.QueryProcessor;
import org.jooq.debug.QueryType;
import org.jooq.debug.TextMatcher;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class BreakpointEditor extends JPanel {

    private static final String       BREAK   = "Break on match";
    private static final String       PROCESS = "Process on match";

    private final JCheckBox           threadNameTextMatcherCheckBox;
    private final TextMatcherPane     threadNameTextMatcherPane;
    private final JCheckBox           hitCountCheckBox;
    private final JFormattedTextField hitCountField;
    private final JCheckBox           queryTextMatcherCheckBox;
    private final TextMatcherPane     queryTextMatcherPane;
    private final JCheckBox           queryTypeCheckBox;
    private final JCheckBox           queryTypeSelectCheckBox;
    private final JCheckBox           queryTypeUpdateCheckBox;
    private final JCheckBox           queryTypeInsertCheckBox;
    private final JCheckBox           queryTypeDeleteCheckBox;
    private final JCheckBox           queryTypeOtherCheckBox;
    private final JComboBox           breakpointTypeCombobox;
    private final JPanel              processorPane;
    private JCheckBox                 beforeCheckbox;
    private QueryProcessorPane        beforePane;
    private JComboBox                 executeTypeComboBox;
    private QueryProcessorPane        replacePane;
    private JCheckBox                 afterCheckbox;
    private QueryProcessorPane        afterPane;

    private final Breakpoint          breakpoint;

    public BreakpointEditor(final DebuggerPane debuggerPane, Breakpoint breakpoint) {
        super(new GridBagLayout());
        setOpaque(false);
        QueryMatcher queryMatcher = breakpoint.getMatcher();
        this.breakpoint = breakpoint;
        if(queryMatcher == null) {
            queryMatcher = new QueryMatcher(null, null, null, true);
        }
        int y = 0;
        TextMatcher statementTextMatcher = queryMatcher.getQueryTextMatcher();
        queryTextMatcherCheckBox = new JCheckBox("Statement", statementTextMatcher != null);
        queryTextMatcherCheckBox.setOpaque(false);
        queryTextMatcherCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        add(queryTextMatcherCheckBox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        queryTextMatcherPane = new TextMatcherPane(statementTextMatcher);
        add(queryTextMatcherPane, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        y++;
        Set<QueryType> queryTypeSet = queryMatcher.getQueryTypeSet();
        queryTypeCheckBox = new JCheckBox("Type", queryTypeSet != null);
        queryTypeCheckBox.setOpaque(false);
        queryTypeCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        add(queryTypeCheckBox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        JPanel typesPane = new JPanel(new GridBagLayout());
        typesPane.setOpaque(false);
        queryTypeSelectCheckBox = new JCheckBox("SELECT", queryTypeSet != null && queryTypeSet.contains(QueryType.SELECT));
        queryTypeSelectCheckBox.setOpaque(false);
        typesPane.add(queryTypeSelectCheckBox, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        queryTypeUpdateCheckBox = new JCheckBox("UPDATE", queryTypeSet != null && queryTypeSet.contains(QueryType.UPDATE));
        queryTypeUpdateCheckBox.setOpaque(false);
        typesPane.add(queryTypeUpdateCheckBox, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
        queryTypeInsertCheckBox = new JCheckBox("INSERT", queryTypeSet != null && queryTypeSet.contains(QueryType.INSERT));
        queryTypeInsertCheckBox.setOpaque(false);
        typesPane.add(queryTypeInsertCheckBox, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
        queryTypeDeleteCheckBox = new JCheckBox("DELETE", queryTypeSet != null && queryTypeSet.contains(QueryType.DELETE));
        queryTypeDeleteCheckBox.setOpaque(false);
        typesPane.add(queryTypeDeleteCheckBox, new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
        queryTypeOtherCheckBox = new JCheckBox("OTHER", queryTypeSet != null && queryTypeSet.contains(QueryType.OTHER));
        queryTypeOtherCheckBox.setOpaque(false);
        typesPane.add(queryTypeOtherCheckBox, new GridBagConstraints(4, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
        add(typesPane, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        y++;
        TextMatcher threadNameTextMatcher = queryMatcher.getThreadNameTextMatcher();
        threadNameTextMatcherCheckBox = new JCheckBox("Thread name", threadNameTextMatcher != null);
        threadNameTextMatcherCheckBox.setOpaque(false);
        threadNameTextMatcherCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        add(threadNameTextMatcherCheckBox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        threadNameTextMatcherPane = new TextMatcherPane(threadNameTextMatcher);
        add(threadNameTextMatcherPane, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        y++;
        Integer hitCount = breakpoint.getHitCount();
        hitCountCheckBox = new JCheckBox("Hit count", hitCount != null);
        hitCountCheckBox.setOpaque(false);
        hitCountCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        add(hitCountCheckBox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        hitCountField = new JFormattedTextField(numberFormat);
        hitCountField.setHorizontalAlignment(JFormattedTextField.RIGHT);
        hitCountField.setValue(hitCount == null? 10: hitCount);
        hitCountField.setColumns(7);
        add(hitCountField, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        y++;
        breakpointTypeCombobox = new JComboBox(new Object[] {BREAK, PROCESS});
        breakpointTypeCombobox.setSelectedItem(breakpoint.isBreaking()? BREAK: PROCESS);
        breakpointTypeCombobox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        add(breakpointTypeCombobox, new GridBagConstraints(0, y, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        y++;
        processorPane = new JPanel(new GridBagLayout());
        processorPane.setOpaque(false);
        populateProcessorPane();
        add(processorPane, new GridBagConstraints(0, y, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 20, 0, 0), 0, 0));
        y++;
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 5));
        buttonPane.setOpaque(false);
        JButton applyButton = new JButton("Apply changes");
        applyButton.setOpaque(false);
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                debuggerPane.modifyBreakpoint(getBreakpoint());
            }
        });
        buttonPane.add(applyButton);
        add(buttonPane, new GridBagConstraints(0, y, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(Box.createGlue(), new GridBagConstraints(0, Short.MAX_VALUE, 1, 1, 0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        adjustStates();
    }

    private void populateProcessorPane() {
        int y = 0;
        QueryProcessor beforeExecutionProcessor = breakpoint.getBeforeExecutionProcessor();
        beforeCheckbox = new JCheckBox("Execute before: ");
        beforeCheckbox.setOpaque(false);
        beforeCheckbox.setSelected(beforeExecutionProcessor != null);
        beforeCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        processorPane.add(beforeCheckbox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
        beforePane = new QueryProcessorPane(beforeExecutionProcessor);
        processorPane.add(beforePane, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 0, 0), 0, 0));
        y++;
        QueryProcessor replacementExecutionProcessor = breakpoint.getReplacementExecutionProcessor();
        executeTypeComboBox = new JComboBox(new String[] {"Execute", "Replace with"});
//        doNotExecuteRadioButton = new JRadioButton("Do not execute");
//        executionButtonGroup.add(doNotExecuteRadioButton);
//        processorPane.add(doNotExecuteRadioButton, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//        y++;
        if(replacementExecutionProcessor != null) {
            executeTypeComboBox.setSelectedIndex(1);
        } else {
            executeTypeComboBox.setSelectedIndex(0);
        }
        processorPane.add(executeTypeComboBox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        replacePane = new QueryProcessorPane(breakpoint.getReplacementExecutionProcessor());
        processorPane.add(replacePane, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        executeTypeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        y++;
        QueryProcessor afterExecutionProcessor = breakpoint.getAfterExecutionProcessor();
        afterCheckbox = new JCheckBox("Execute after: ");
        afterCheckbox.setOpaque(false);
        afterCheckbox.setSelected(afterExecutionProcessor != null);
        afterCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        processorPane.add(afterCheckbox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
        afterPane = new QueryProcessorPane(afterExecutionProcessor);
        processorPane.add(afterPane, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 0, 0), 0, 0));
    }

    private void adjustStates() {
        boolean isActive = true;
        queryTextMatcherCheckBox.setEnabled(isActive);
        queryTypeCheckBox.setEnabled(isActive);
        threadNameTextMatcherCheckBox.setEnabled(isActive);
        queryTextMatcherPane.setLocked(!isActive || !queryTextMatcherCheckBox.isSelected());
        queryTypeSelectCheckBox.setEnabled(isActive && queryTypeCheckBox.isSelected());
        queryTypeUpdateCheckBox.setEnabled(isActive && queryTypeCheckBox.isSelected());
        queryTypeInsertCheckBox.setEnabled(isActive && queryTypeCheckBox.isSelected());
        queryTypeDeleteCheckBox.setEnabled(isActive && queryTypeCheckBox.isSelected());
        queryTypeOtherCheckBox.setEnabled(isActive && queryTypeCheckBox.isSelected());
        threadNameTextMatcherPane.setLocked(!isActive || !threadNameTextMatcherCheckBox.isSelected());
        hitCountCheckBox.setEnabled(isActive);
        hitCountField.setEnabled(isActive && hitCountCheckBox.isSelected());
        breakpointTypeCombobox.setEnabled(isActive && (queryTextMatcherCheckBox.isSelected() || queryTypeCheckBox.isSelected() || threadNameTextMatcherCheckBox.isSelected() || hitCountCheckBox.isSelected()));
        processorPane.setVisible(isActive && breakpointTypeCombobox.getSelectedItem() == PROCESS);
        beforeCheckbox.setEnabled(isActive);
        beforePane.setLocked(!isActive || !beforeCheckbox.isSelected());
        executeTypeComboBox.setEnabled(isActive);
//        doNotExecuteRadioButton;
        replacePane.setLocked(!isActive || executeTypeComboBox.getSelectedIndex() != 1);
        afterCheckbox.setEnabled(isActive);
        afterPane.setLocked(!isActive || !afterCheckbox.isSelected());
    }

    private QueryMatcher getMatcher() {
        boolean isActive = true;//activeCheckBox.isSelected();
        TextMatcher threadNameTextMatcher = threadNameTextMatcherCheckBox.isSelected()? threadNameTextMatcherPane.getTextMatcher(): null;
        TextMatcher statementTextMatcher = queryTextMatcherCheckBox.isSelected()? queryTextMatcherPane.getTextMatcher(): null;
        Set<QueryType> queryTypeSet;
        if(queryTypeCheckBox.isSelected()) {
            List<QueryType> typeList = new ArrayList<QueryType>();
            if(queryTypeSelectCheckBox.isSelected()) {
                typeList.add(QueryType.SELECT);
            }
            if(queryTypeUpdateCheckBox.isSelected()) {
                typeList.add(QueryType.UPDATE);
            }
            if(queryTypeInsertCheckBox.isSelected()) {
                typeList.add(QueryType.INSERT);
            }
            if(queryTypeDeleteCheckBox.isSelected()) {
                typeList.add(QueryType.DELETE);
            }
            if(queryTypeOtherCheckBox.isSelected()) {
                typeList.add(QueryType.OTHER);
            }
            queryTypeSet = EnumSet.copyOf(typeList);
        } else {
            queryTypeSet = null;
        }
        if(threadNameTextMatcher == null && statementTextMatcher == null && queryTypeSet == null) {
            return null;
        }
        return new QueryMatcher(threadNameTextMatcher, statementTextMatcher, queryTypeSet, isActive);
    }

    public Breakpoint getBreakpoint() {
        QueryMatcher matcher = getMatcher();
        Integer hitCount = hitCountCheckBox.isSelected() ? ((Number) hitCountField.getValue()).intValue() : null;
        boolean isBreaking = breakpointTypeCombobox.getSelectedItem() == BREAK;
        QueryProcessor before = null;
        QueryProcessor replace = null;
        QueryProcessor after = null;

        if (!isBreaking) {
            before = beforeCheckbox.isSelected() ? beforePane.getQueryProcessor() : null;
            replace = executeTypeComboBox.getSelectedIndex() == 1 ? replacePane.getQueryProcessor() : null;
            after = afterCheckbox.isSelected() ? afterPane.getQueryProcessor() : null;
        }

        return new Breakpoint(breakpoint.getID(), hitCount, matcher, isBreaking, before, replace, after);
    }
}
