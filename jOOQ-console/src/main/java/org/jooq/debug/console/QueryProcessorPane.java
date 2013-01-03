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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jooq.debug.QueryProcessor;

import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class QueryProcessorPane extends JPanel {

    private JComboBox processorTypeComboBox;
    private JTextField processorTextField;
    private JScrollPane processorStaticScrollPane;
    private SqlTextArea processorStaticSQLTextArea;

    public QueryProcessorPane(QueryProcessor queryProcessor) {
        super(new GridBagLayout());
        setOpaque(false);
        if(queryProcessor == null) {
            queryProcessor = new QueryProcessor(QueryProcessor.ProcessorExecutionType.STATIC, "");
        }
        processorTypeComboBox = new JComboBox(QueryProcessor.ProcessorExecutionType.values());
        processorTypeComboBox.setSelectedItem(queryProcessor.getType());
        processorTypeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustStates();
            }
        });
        add(processorTypeComboBox, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        boolean isStatic = queryProcessor.getType() == QueryProcessor.ProcessorExecutionType.STATIC;
        processorTextField = new JTextField(isStatic? "": queryProcessor.getText(), 14);
        add(processorTextField, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        processorStaticSQLTextArea = new SqlTextArea();
        if(isStatic) {
            processorStaticSQLTextArea.setText(queryProcessor.getText());
            processorStaticSQLTextArea.setCaretPosition(0);
        }
        processorStaticScrollPane = new RTextScrollPane(processorStaticSQLTextArea);
        processorStaticScrollPane.setPreferredSize(new Dimension(100, 100));
        add(processorStaticScrollPane, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        adjustStates();
    }

    private void adjustStates() {
        boolean isStatic = processorTypeComboBox.getSelectedItem() == QueryProcessor.ProcessorExecutionType.STATIC;
        processorStaticScrollPane.setVisible(isStatic);
        processorTextField.setVisible(!isStatic);
        revalidate();
        repaint();
    }

    public QueryProcessor getQueryProcessor() {
        QueryProcessor.ProcessorExecutionType type = (QueryProcessor.ProcessorExecutionType)processorTypeComboBox.getSelectedItem();
        return new QueryProcessor(type, type == QueryProcessor.ProcessorExecutionType.STATIC? processorStaticSQLTextArea.getText(): processorTextField.getText());
    }

    public void setLocked(boolean isLocked) {
        processorTypeComboBox.setEnabled(!isLocked);
        processorTextField.setEnabled(!isLocked);
    }

}
