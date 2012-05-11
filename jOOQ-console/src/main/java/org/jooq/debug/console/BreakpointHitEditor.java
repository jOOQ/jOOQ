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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import org.jooq.debug.BreakpointBeforeExecutionHit;
import org.jooq.debug.BreakpointBeforeExecutionHit.ExecutionType;
import org.jooq.debug.console.DebuggerPane.BreakpointBeforeExecutionHitNode;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class BreakpointHitEditor extends JPanel {

    private JCheckBox replaceStatementCheckBox;
    private JScrollPane replacementSQLTextAreaScrollPane;

    public BreakpointHitEditor(final DebuggerPane debuggerPane, final BreakpointBeforeExecutionHitNode breakpointHitNode) {
        super(new GridBagLayout());
        setOpaque(false);
        final BreakpointBeforeExecutionHit breakpointHit = (BreakpointBeforeExecutionHit)breakpointHitNode.getUserObject();
        int y = 0;
        add(new JLabel("Statement:"), new GridBagConstraints(0, y++, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        SqlTextArea sqlTextArea = new SqlTextArea();
        sqlTextArea.setText(breakpointHit.getSql() + "\n");
        sqlTextArea.setCaretPosition(0);
        add(new JScrollPane(sqlTextArea), new GridBagConstraints(0, y++, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        replaceStatementCheckBox = new JCheckBox("Replace with statement");
        replaceStatementCheckBox.setOpaque(false);
        replaceStatementCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        add(replaceStatementCheckBox, new GridBagConstraints(0, y++, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
        final SqlTextArea replacementSQLTextArea = new SqlTextArea();
        replacementSQLTextAreaScrollPane = new JScrollPane(replacementSQLTextArea);
        add(replacementSQLTextAreaScrollPane, new GridBagConstraints(0, y++, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 20, 0, 0), 0, 0));
        JPanel executionTypePane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        // For now, this choice is not exposed.
        executionTypePane.setVisible(false);
        executionTypePane.setOpaque(false);
        ButtonGroup executionTypeGroup = new ButtonGroup();
        final JRadioButton executeTypeNoneRadioButton = new JRadioButton("Execute");
        executeTypeNoneRadioButton.setOpaque(false);
        executeTypeNoneRadioButton.setSelected(true);
        executionTypeGroup.add(executeTypeNoneRadioButton);
        executionTypePane.add(executeTypeNoneRadioButton);
        JRadioButton executeTypeBreakRadioButton = new JRadioButton("Execute and break");
        executeTypeBreakRadioButton.setOpaque(false);
        executionTypeGroup.add(executeTypeBreakRadioButton);
        executionTypePane.add(executeTypeBreakRadioButton);
        add(executionTypePane, new GridBagConstraints(0, y++, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPane.setOpaque(false);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JButton applyButton = new JButton("Proceed");
        applyButton.setOpaque(false);
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                breakpointHit.setExecutionType(executeTypeNoneRadioButton.isSelected()? ExecutionType.RUN: ExecutionType.STEP_THROUGH, replaceStatementCheckBox.isSelected()? replacementSQLTextArea.getText(): null);
                debuggerPane.proceedBreakpointHit(breakpointHitNode);
            }
        });
        buttonPane.add(applyButton);
        add(buttonPane, new GridBagConstraints(0, y, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        adjustStates();
    }

    private void adjustStates() {
        replacementSQLTextAreaScrollPane.setVisible(replaceStatementCheckBox.isSelected());
        revalidate();
        repaint();
    }

}
