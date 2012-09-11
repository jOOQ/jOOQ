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

import org.jooq.tools.debug.Breakpoint;
import org.jooq.tools.debug.QueryType;
import org.jooq.tools.debug.QueryMatcher;
import org.jooq.tools.debug.QueryProcessor;
import org.jooq.tools.debug.TextMatcher;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class BreakpointEditor extends JPanel {

    private static final String BREAK = "Break on match";
    private static final String PROCESS = "Process on match";

    private int id;
    private JCheckBox threadNameTextMatcherCheckBox;
    private TextMatcherPane threadNameTextMatcherPane;
    private JCheckBox hitCountCheckBox;
    private JFormattedTextField hitCountField;
    private JCheckBox statementTextMatcherCheckBox;
    private TextMatcherPane statementTextMatcherPane;
    private JCheckBox statementTypeCheckBox;
    private JCheckBox statementTypeSelectCheckBox;
    private JCheckBox statementTypeUpdateCheckBox;
    private JCheckBox statementTypeInsertCheckBox;
    private JCheckBox statementTypeDeleteCheckBox;
    private JCheckBox statementTypeOtherCheckBox;
    private JComboBox breakpointTypeComboBox;
    private JPanel processorPane;
    private JCheckBox beforeExecutionCheckBox;
    private QueryProcessorPane beforeExecutionProcessorPane;
    private JComboBox executeTypeComboBox;
    private QueryProcessorPane replacementExecutionProcessorPane;
    private JCheckBox afterExecutionCheckBox;
    private QueryProcessorPane afterExecutionProcessorPane;

    public BreakpointEditor(final DebuggerPane debuggerPane, Breakpoint breakpoint) {
        super(new GridBagLayout());
        setOpaque(false);
        QueryMatcher queryMatcher = breakpoint.getStatementMatcher();
        id = breakpoint.getID();
        if(queryMatcher == null) {
            queryMatcher = new QueryMatcher(null, null, null, true);
        }
        int y = 0;
        TextMatcher statementTextMatcher = queryMatcher.getQueryTextMatcher();
        statementTextMatcherCheckBox = new JCheckBox("Statement", statementTextMatcher != null);
        statementTextMatcherCheckBox.setOpaque(false);
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
        Set<QueryType> queryTypeSet = queryMatcher.getQueryTypeSet();
        statementTypeCheckBox = new JCheckBox("Type", queryTypeSet != null);
        statementTypeCheckBox.setOpaque(false);
        statementTypeCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        add(statementTypeCheckBox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        JPanel typesPane = new JPanel(new GridBagLayout());
        typesPane.setOpaque(false);
        statementTypeSelectCheckBox = new JCheckBox("SELECT", queryTypeSet != null && queryTypeSet.contains(QueryType.SELECT));
        statementTypeSelectCheckBox.setOpaque(false);
        typesPane.add(statementTypeSelectCheckBox, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        statementTypeUpdateCheckBox = new JCheckBox("UPDATE", queryTypeSet != null && queryTypeSet.contains(QueryType.UPDATE));
        statementTypeUpdateCheckBox.setOpaque(false);
        typesPane.add(statementTypeUpdateCheckBox, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
        statementTypeInsertCheckBox = new JCheckBox("INSERT", queryTypeSet != null && queryTypeSet.contains(QueryType.INSERT));
        statementTypeInsertCheckBox.setOpaque(false);
        typesPane.add(statementTypeInsertCheckBox, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
        statementTypeDeleteCheckBox = new JCheckBox("DELETE", queryTypeSet != null && queryTypeSet.contains(QueryType.DELETE));
        statementTypeDeleteCheckBox.setOpaque(false);
        typesPane.add(statementTypeDeleteCheckBox, new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
        statementTypeOtherCheckBox = new JCheckBox("OTHER", queryTypeSet != null && queryTypeSet.contains(QueryType.OTHER));
        statementTypeOtherCheckBox.setOpaque(false);
        typesPane.add(statementTypeOtherCheckBox, new GridBagConstraints(4, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
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
        breakpointTypeComboBox = new JComboBox(new Object[] {BREAK, PROCESS});
        breakpointTypeComboBox.setSelectedItem(breakpoint.isBreaking()? BREAK: PROCESS);
        breakpointTypeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        add(breakpointTypeComboBox, new GridBagConstraints(0, y, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        y++;
        processorPane = new JPanel(new GridBagLayout());
        processorPane.setOpaque(false);
        populateProcessorPane(breakpoint);
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

    private void populateProcessorPane(Breakpoint breakpoint) {
        int y = 0;
        QueryProcessor beforeExecutionProcessor = breakpoint.getBeforeExecutionProcessor();
        beforeExecutionCheckBox = new JCheckBox("Execute before: ");
        beforeExecutionCheckBox.setOpaque(false);
        beforeExecutionCheckBox.setSelected(beforeExecutionProcessor != null);
        beforeExecutionCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        processorPane.add(beforeExecutionCheckBox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
        beforeExecutionProcessorPane = new QueryProcessorPane(beforeExecutionProcessor);
        processorPane.add(beforeExecutionProcessorPane, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 0, 0), 0, 0));
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
        replacementExecutionProcessorPane = new QueryProcessorPane(breakpoint.getReplacementExecutionProcessor());
        processorPane.add(replacementExecutionProcessorPane, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        executeTypeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        y++;
        QueryProcessor afterExecutionProcessor = breakpoint.getAfterExecutionProcessor();
        afterExecutionCheckBox = new JCheckBox("Execute after: ");
        afterExecutionCheckBox.setOpaque(false);
        afterExecutionCheckBox.setSelected(afterExecutionProcessor != null);
        afterExecutionCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        processorPane.add(afterExecutionCheckBox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
        afterExecutionProcessorPane = new QueryProcessorPane(afterExecutionProcessor);
        processorPane.add(afterExecutionProcessorPane, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 0, 0), 0, 0));
    }

    private void adjustStates() {
        boolean isActive = true;
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
        hitCountCheckBox.setEnabled(isActive);
        hitCountField.setEnabled(isActive && hitCountCheckBox.isSelected());
        breakpointTypeComboBox.setEnabled(isActive && (statementTextMatcherCheckBox.isSelected() || statementTypeCheckBox.isSelected() || threadNameTextMatcherCheckBox.isSelected() || hitCountCheckBox.isSelected()));
        processorPane.setVisible(isActive && breakpointTypeComboBox.getSelectedItem() == PROCESS);
        beforeExecutionCheckBox.setEnabled(isActive);
        beforeExecutionProcessorPane.setLocked(!isActive || !beforeExecutionCheckBox.isSelected());
        executeTypeComboBox.setEnabled(isActive);
//        doNotExecuteRadioButton;
        replacementExecutionProcessorPane.setLocked(!isActive || executeTypeComboBox.getSelectedIndex() != 1);
        afterExecutionCheckBox.setEnabled(isActive);
        afterExecutionProcessorPane.setLocked(!isActive || !afterExecutionCheckBox.isSelected());
    }

    private QueryMatcher getStatementMatcher() {
        boolean isActive = true;//activeCheckBox.isSelected();
        TextMatcher threadNameTextMatcher = threadNameTextMatcherCheckBox.isSelected()? threadNameTextMatcherPane.getTextMatcher(): null;
        TextMatcher statementTextMatcher = statementTextMatcherCheckBox.isSelected()? statementTextMatcherPane.getTextMatcher(): null;
        Set<QueryType> queryTypeSet;
        if(statementTypeCheckBox.isSelected()) {
            List<QueryType> typeList = new ArrayList<QueryType>();
            if(statementTypeSelectCheckBox.isSelected()) {
                typeList.add(QueryType.SELECT);
            }
            if(statementTypeUpdateCheckBox.isSelected()) {
                typeList.add(QueryType.UPDATE);
            }
            if(statementTypeInsertCheckBox.isSelected()) {
                typeList.add(QueryType.INSERT);
            }
            if(statementTypeDeleteCheckBox.isSelected()) {
                typeList.add(QueryType.DELETE);
            }
            if(statementTypeOtherCheckBox.isSelected()) {
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
        QueryMatcher queryMatcher = getStatementMatcher();
        Integer hitCount = hitCountCheckBox.isSelected()? ((Number)hitCountField.getValue()).intValue(): null;
        boolean isBreaking = breakpointTypeComboBox.getSelectedItem() == BREAK;
        QueryProcessor beforeExecutionProcessor = null;
        QueryProcessor replacementExecutionProcessor = null;
        QueryProcessor afterExecutionProcessor = null;
        if(!isBreaking) {
            beforeExecutionProcessor = beforeExecutionCheckBox.isSelected()? beforeExecutionProcessorPane.getQueryProcessor(): null;
            replacementExecutionProcessor = executeTypeComboBox.getSelectedIndex() == 1? replacementExecutionProcessorPane.getQueryProcessor(): null;
            afterExecutionProcessor = afterExecutionCheckBox.isSelected()? afterExecutionProcessorPane.getQueryProcessor(): null;
        }
        return new Breakpoint(id, hitCount, queryMatcher, isBreaking, beforeExecutionProcessor, replacementExecutionProcessor, afterExecutionProcessor);
    }

}
