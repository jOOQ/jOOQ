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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jooq.debug.Breakpoint;
import org.jooq.debug.Debugger;
import org.jooq.debug.console.misc.CheckBoxNode;
import org.jooq.debug.console.misc.CheckBoxNodeEditor;
import org.jooq.debug.console.misc.CheckBoxNodeRenderer;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class DebuggerPane extends JPanel {

    private Debugger debugger;
    private DefaultMutableTreeNode rootNode;
    private JTree breakpointTree;
    private DefaultTreeModel breakpointTreeModel;
    private JPanel eastPane;

    public DebuggerPane(Debugger debugger) {
        super(new BorderLayout());
        this.debugger = debugger;
        JPanel westPane = new JPanel(new BorderLayout());
        JPanel breakpointAddPane = new JPanel(new GridBagLayout());
        final JTextField addBreakpointTextField = new JTextField(7);
        breakpointAddPane.add(addBreakpointTextField, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        final JButton addBreakpointButton = new JButton("Add");
        addBreakpointButton.setEnabled(false);
        breakpointAddPane.add(addBreakpointButton, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
        addBreakpointTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                adjustStates();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                adjustStates();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                adjustStates();
            }
            private void adjustStates() {
                // TODO: restrict to unique names?
                boolean isEnabled = addBreakpointTextField.getText().length() > 0;
                addBreakpointButton.setEnabled(isEnabled);
            }
        });
        ActionListener addBreakpointActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = addBreakpointTextField.getText();
                addBreakpointTextField.setText("");
                addBreakpoint(name);
            }
        };
        addBreakpointTextField.addActionListener(addBreakpointActionListener);
        addBreakpointButton.addActionListener(addBreakpointActionListener);
        westPane.add(breakpointAddPane, BorderLayout.NORTH);
        rootNode = new DefaultMutableTreeNode();
        breakpointTreeModel = new DefaultTreeModel(rootNode) {
            @Override
            public void valueForPathChanged(TreePath path, Object newValue) {
                if(newValue instanceof CheckBoxNode) {
                    CheckBoxNode node = (CheckBoxNode)path.getLastPathComponent();
                    node.setSelected(((CheckBoxNode) newValue).isSelected());
                    super.valueForPathChanged(path, node.getUserObject());
                }
            }
        };
        breakpointTree = new JTree(breakpointTreeModel);
        breakpointTree.setRootVisible(false);
        breakpointTree.setCellRenderer(new CheckBoxNodeRenderer());
        breakpointTree.setCellEditor(new CheckBoxNodeEditor(breakpointTree));
        breakpointTree.setEditable(true);
        westPane.add(new JScrollPane(breakpointTree), BorderLayout.CENTER);
        JPanel breakpointRemovePane = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        final JButton removeBreakpointButton = new JButton("Remove");
        removeBreakpointButton.setEnabled(false);
        removeBreakpointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath[] paths = breakpointTree.getSelectionPaths();
                boolean isValid = paths != null && paths.length > 0;
                if(isValid) {
                    for(int i=0; i<paths.length; i++) {
                        if(!(paths[i].getLastPathComponent() instanceof CheckBoxNode)) {
                            isValid = false;
                            break;
                        }
                    }
                }
                if(isValid) {
                    breakpointTree.cancelEditing();
                    for(int i=0; i<paths.length; i++) {
                        CheckBoxNode childNode = (CheckBoxNode)paths[i].getLastPathComponent();
                        int index = rootNode.getIndex(childNode);
                        rootNode.remove(index);
                        breakpointTreeModel.nodesWereRemoved(rootNode, new int[] {index}, new Object[] {childNode});
                    }
                    commitBreakpoints();
                }
            }
        });
        breakpointTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath[] paths = breakpointTree.getSelectionPaths();
                boolean isValid = paths != null && paths.length > 0;
                if(isValid) {
                    for(int i=0; i<paths.length; i++) {
                        if(!(paths[i].getLastPathComponent() instanceof CheckBoxNode)) {
                            isValid = false;
                            break;
                        }
                    }
                }
                removeBreakpointButton.setEnabled(isValid);
                processTreeSelection();
            }
        });
        breakpointRemovePane.add(removeBreakpointButton);
        westPane.add(breakpointRemovePane, BorderLayout.SOUTH);
        eastPane = new JPanel(new BorderLayout());
        add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, westPane, eastPane), BorderLayout.CENTER);
    }

    private static int nextID = 1;

    private void addBreakpoint(String name) {
        breakpointTree.cancelEditing();
        Breakpoint breakpoint = new Breakpoint(nextID++, null, true, null, null, null);
        CheckBoxNode breakpointNode = new CheckBoxNode(breakpoint, name, true);
        rootNode.add(breakpointNode);
        breakpointTreeModel.nodesWereInserted(rootNode, new int[] {rootNode.getIndex(breakpointNode)});
        breakpointTree.expandPath(new TreePath(rootNode));
        commitBreakpoints();
    }

    void modifyBreakpoint(Breakpoint breakpoint) {
        int childCount = rootNode.getChildCount();
        for(int i=0; i<childCount; i++) {
            CheckBoxNode checkBoxNode = (CheckBoxNode)rootNode.getChildAt(i);
            Breakpoint breakpoint_ = (Breakpoint)checkBoxNode.getUserObject();
            if(breakpoint_.getID() == breakpoint.getID()) {
                checkBoxNode.setUserObject(breakpoint);
                commitBreakpoints();
                break;
            }
        }
    }

    private void commitBreakpoints() {
        List<Breakpoint> breakpointList = new ArrayList<Breakpoint>();
        int childCount = rootNode.getChildCount();
        for(int i=0; i<childCount; i++) {
            CheckBoxNode node = (CheckBoxNode)rootNode.getChildAt(i);
            if(node.isSelected()) {
                breakpointList.add((Breakpoint)node.getUserObject());
            }
        }
        debugger.setBreakpoints(breakpointList.toArray(new Breakpoint[0]));
    }

    private void processTreeSelection() {
        TreePath[] paths = breakpointTree.getSelectionPaths();
        // TODO: commit last value.
        eastPane.removeAll();
        if(paths != null && paths.length == 1) {
            Object o = ((DefaultMutableTreeNode)paths[0].getLastPathComponent()).getUserObject();
            if(o instanceof Breakpoint) {
                eastPane.add(new BreakpointEditor(this, (Breakpoint)o));
            }
        }
        eastPane.revalidate();
        eastPane.repaint();
    }

}
