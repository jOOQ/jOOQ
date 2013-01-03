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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jooq.debug.Breakpoint;
import org.jooq.debug.BreakpointHit;
import org.jooq.debug.BreakpointHitHandler;
import org.jooq.debug.Debugger;
import org.jooq.debug.console.misc.CheckBoxNode;
import org.jooq.debug.console.misc.CheckBoxNodeEditor;
import org.jooq.debug.console.misc.CheckBoxNodeRenderer;
import org.jooq.debug.console.misc.InvisibleSplitPane;
import org.jooq.debug.console.misc.TreeDataTip;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings({"serial", "hiding"})
public class DebuggerPane extends JPanel {

    private final ImageIcon BREAKPOINT_ON_ICON = new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/BreakpointOn16.png"));
    private final ImageIcon BREAKPOINT_OFF_ICON = new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/BreakpointOff16.png"));
    private final ImageIcon BREAKPOINT_HIT_BEFORE_ICON = new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/BreakpointHit16.png"));
    private final ImageIcon BREAKPOINT_HIT_AFTER_ICON = new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/BreakpointHitAfter16.png"));
    private final ImageIcon STACK_TRACE_ELEMENT_ICON = new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/StackTraceElement16.png"));

    private Debugger debugger;
    private DefaultMutableTreeNode rootNode;
    private JTree breakpointTree;
    private DefaultTreeModel breakpointTreeModel;
    private JPanel eastPane;

    public DebuggerPane(Debugger debugger) {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(2, 5, 5, 5));
        setOpaque(false);
        this.debugger = debugger;
        JPanel westPane = new JPanel(new BorderLayout());
        westPane.setBorder(BorderFactory.createTitledBorder("Breakpoints"));
        westPane.setOpaque(false);
        JPanel breakpointAddPane = new JPanel(new GridBagLayout());
        breakpointAddPane.setOpaque(false);
        breakpointAddPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
        breakpointAddPane.add(new JLabel("Name "), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        final JTextField addBreakpointTextField = new JTextField(7);
        breakpointAddPane.add(addBreakpointTextField, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
        final JButton addBreakpointButton = new JButton("Add");
        addBreakpointButton.setOpaque(false);
        addBreakpointButton.setEnabled(false);
        breakpointAddPane.add(addBreakpointButton, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
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
                if(name.length() == 0) {
                    return;
                }
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
                    boolean isSelected = ((CheckBoxNode) newValue).isSelected();
                    node.setSelected(isSelected);
                    Breakpoint breakpoint = (Breakpoint)node.getUserObject();
                    super.valueForPathChanged(path, breakpoint);
                    if(isSelected) {
                        DebuggerPane.this.debugger.setBreakpointHitHandler(breakpointHitHandler);
                        DebuggerPane.this.debugger.addBreakpoint(breakpoint);
                    } else {
                        DebuggerPane.this.debugger.removeBreakpoint(breakpoint);
                        if(DebuggerPane.this.debugger.getBreakpoints() == null) {
                            DebuggerPane.this.debugger.setBreakpointHitHandler(null);
                        }
                    }
                }
            }
        };
        breakpointTree = new JTree(breakpointTreeModel);
        breakpointTree.setRootVisible(false);
        breakpointTree.setShowsRootHandles(true);
        breakpointTree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                if(c instanceof JLabel) {
                    Icon icon = null;
                    if(value instanceof CheckBoxNode) {
                        icon = ((CheckBoxNode) value).isSelected()? BREAKPOINT_ON_ICON: BREAKPOINT_OFF_ICON;
                    } else if(value instanceof BreakpointHitNode) {
                        if(((BreakpointHitNode) value).getUserObject().isBeforeExecution()) {
                            icon = BREAKPOINT_HIT_BEFORE_ICON;
                        } else {
                            icon = BREAKPOINT_HIT_AFTER_ICON;
                        }
                    } else if(value instanceof StackTraceElementNode) {
                        icon = STACK_TRACE_ELEMENT_ICON;
                    }
                    ((JLabel) c).setIcon(icon);
                }
                return c;
            }
        });
        // Order matters because we want the editor to have the old cell renderer as a base.
        breakpointTree.setCellEditor(new CheckBoxNodeEditor(breakpointTree));
        breakpointTree.setCellRenderer(new CheckBoxNodeRenderer(breakpointTree));
        breakpointTree.setEditable(true);
        TreeDataTip.activate(breakpointTree);
        JScrollPane breakpointTreeScrollPane = new JScrollPane(breakpointTree);
        breakpointTreeScrollPane.setPreferredSize(new Dimension(200, 200));
        westPane.add(breakpointTreeScrollPane, BorderLayout.CENTER);
        JPanel breakpointRemovePane = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        breakpointRemovePane.setOpaque(false);
        breakpointRemovePane.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        final JButton removeBreakpointButton = new JButton("Remove");
        removeBreakpointButton.setOpaque(false);
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
                    List<Breakpoint> breakpointList = new ArrayList<Breakpoint>();
                    List<BreakpointHitNode> breakpointHitNodeList = new ArrayList<DebuggerPane.BreakpointHitNode>();
                    // TODO: list of after exec
                    breakpointTree.cancelEditing();
                    for(int i=0; i<paths.length; i++) {
                        CheckBoxNode childNode = (CheckBoxNode)paths[i].getLastPathComponent();
                        breakpointList.add((Breakpoint)childNode.getUserObject());
                        int childCount = childNode.getChildCount();
                        for(int j=0; j<childCount; j++) {
                            TreeNode node = childNode.getChildAt(j);
                            if(node instanceof BreakpointHitNode) {
                                breakpointHitNodeList.add((BreakpointHitNode)node);
                            }
                        }
                        int index = rootNode.getIndex(childNode);
                        rootNode.remove(index);
                        breakpointTreeModel.nodesWereRemoved(rootNode, new int[] {index}, new Object[] {childNode});
                    }
                    for(Breakpoint breakpoint: breakpointList) {
                        DebuggerPane.this.debugger.removeBreakpoint(breakpoint);
                    }
                    if(DebuggerPane.this.debugger.getBreakpoints() == null) {
                        DebuggerPane.this.debugger.setBreakpointHitHandler(null);
                    }
                    for(BreakpointHitNode node: breakpointHitNodeList) {
                        synchronized (node) {
                            node.proceed();
                        }
                    }
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
        breakpointTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) {
                    int row = breakpointTree.getRowForLocation(e.getX(), e.getY());
                    if(row >= 0 && !breakpointTree.isRowSelected(row)) {
                        breakpointTree.setSelectionRow(row);
                    }
                }
                maybeShowPopup(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }
            private void maybeShowPopup(MouseEvent e) {
                if(e.isPopupTrigger()) {
                    TreePath[] selectionPaths = breakpointTree.getSelectionPaths();
                    if(selectionPaths != null && selectionPaths.length > 0) {
                        JPopupMenu popupMenu = new JPopupMenu();
                        {
                            final List<BreakpointHitNode> nodeList = new ArrayList<DebuggerPane.BreakpointHitNode>();
                            for(TreePath selectionPath: selectionPaths) {
                                Object o = selectionPath.getLastPathComponent();
                                if(!(o instanceof BreakpointHitNode)) {
                                    nodeList.clear();
                                    break;
                                }
                                nodeList.add((BreakpointHitNode)o);
                            }
                            if(!nodeList.isEmpty()) {
                                JMenuItem proceedMenuItem = new JMenuItem("Proceed");
                                proceedMenuItem.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        for(BreakpointHitNode node: nodeList) {
                                            proceedBreakpointHit(node);
                                        }
                                    }
                                });
                                popupMenu.add(proceedMenuItem);
                            }
                        }
                        if(selectionPaths.length == 1) {
                            Object o = selectionPaths[0].getLastPathComponent();
                            if(o instanceof BreakpointHitNode) {
                                final BreakpointHit breakpointHit = ((BreakpointHitNode) o).getUserObject();
                                JMenuItem copyStackToClipboardMenuItem = new JMenuItem("Copy Call Stack to Clipboard");
                                copyStackToClipboardMenuItem.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        StringWriter sw = new StringWriter();
                                        Throwable throwable = new Exception("Statement Stack trace");
                                        throwable.setStackTrace(breakpointHit.getStackTrace());
                                        throwable.printStackTrace(new PrintWriter(sw));
                                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                                        clipboard.setContents(new StringSelection(sw.toString()), null);
                                    }
                                });
                                popupMenu.add(copyStackToClipboardMenuItem);
                                JMenuItem dumpStackToConsoleMenuItem = new JMenuItem("Dump Call Stack");
                                dumpStackToConsoleMenuItem.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        Throwable throwable = new Exception("Statement Stack trace");
                                        throwable.setStackTrace(breakpointHit.getStackTrace());
                                        throwable.printStackTrace();
                                    }
                                });
                                popupMenu.add(dumpStackToConsoleMenuItem);
                            }
                        }
                        if(popupMenu.getComponentCount() > 0) {
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                }
            }
        });
        breakpointRemovePane.add(removeBreakpointButton);
        westPane.add(breakpointRemovePane, BorderLayout.SOUTH);
        eastPane = new JPanel(new BorderLayout());
        eastPane.setOpaque(false);
        add(new InvisibleSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, westPane, eastPane), BorderLayout.CENTER);
    }

    private void addBreakpoint(String name) {
        breakpointTree.cancelEditing();
        Breakpoint breakpoint = new Breakpoint();
        CheckBoxNode breakpointNode = new CheckBoxNode(breakpoint, name, true);
        rootNode.add(breakpointNode);
        breakpointTreeModel.nodesWereInserted(rootNode, new int[] { rootNode.getIndex(breakpointNode) });
        breakpointTree.expandPath(new TreePath(rootNode));
        debugger.addBreakpoint(breakpoint);
        DebuggerPane.this.debugger.setBreakpointHitHandler(breakpointHitHandler);
        breakpointTree.setSelectionPath(new TreePath(new Object[] { rootNode, breakpointNode }));
    }

    void modifyBreakpoint(Breakpoint breakpoint) {
        int childCount = rootNode.getChildCount();

        for (int i = 0; i < childCount; i++) {
            CheckBoxNode checkBoxNode = (CheckBoxNode) rootNode.getChildAt(i);
            Breakpoint b = (Breakpoint) checkBoxNode.getUserObject();

            if (b.equals(breakpoint)) {
                checkBoxNode.setUserObject(breakpoint);

                if (checkBoxNode.isSelected()) {
                    debugger.addBreakpoint(breakpoint);
                }

                break;
            }
        }
    }

    private class StackTraceElementNode extends DefaultMutableTreeNode {
        private String name;
        public StackTraceElementNode(StackTraceElement stackTraceElement) {
            super(stackTraceElement);
            String className = stackTraceElement.getClassName();
            className = className.substring(className.lastIndexOf('.') + 1);
            String fileName = stackTraceElement.getFileName();
            int lineNumber = stackTraceElement.getLineNumber();
            String fileInfo = fileName != null && lineNumber >= 0? fileName + ":" + lineNumber: "";
            name = className + '.' + stackTraceElement.getMethodName() + "(" + fileInfo + ")";
        }
        @Override
        public String toString() {
            return name;
        }
    }

    class BreakpointHitNode extends DefaultMutableTreeNode {
        public BreakpointHitNode(BreakpointHit breakpointHit) {
            super(breakpointHit);
            StackTraceElement[] stackTrace = breakpointHit.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                add(new StackTraceElementNode(stackTraceElement));
            }
        }
        @Override
        public BreakpointHit getUserObject() {
            return (BreakpointHit)super.getUserObject();
        }
        private boolean isLocked = true;
        public void proceed() {
            synchronized (this) {
                isLocked = false;
                notifyAll();
            }
        }
        public boolean isLocked() {
            synchronized (this) {
                return isLocked;
            }
        }
        @Override
        public String toString() {
            return "Thread [" + getUserObject().getThreadName() + "]";
        }
    }

    void proceedBreakpointHit(BreakpointHitNode breakpointHitNode) {
        CheckBoxNode parentNode = (CheckBoxNode)breakpointHitNode.getParent();
        int index = parentNode.getIndex(breakpointHitNode);
        parentNode.remove(index);
        breakpointTreeModel.nodesWereRemoved(parentNode, new int[] {index}, new Object[] {breakpointHitNode});
        breakpointHitNode.proceed();
    }

    private BreakpointHitHandler breakpointHitHandler = new BreakpointHitHandler() {
        @Override
        public void processBreakpointBeforeExecutionHit(final BreakpointHit breakpointHit) {
            if(SwingUtilities.isEventDispatchThread()) {
                new IllegalStateException("Breakpoint triggered from UI thread: cannot break because the debugger needs a live UI thread!").printStackTrace();
                return;
            }
            handleBreakpoint(breakpointHit);
        }
        @Override
        public void processBreakpointAfterExecutionHit(BreakpointHit breakpointHit) {
            if(SwingUtilities.isEventDispatchThread()) {
                new IllegalStateException("Breakpoint triggered from UI thread: cannot break because the debugger needs a live UI thread!").printStackTrace();
                return;
            }
            handleBreakpoint(breakpointHit);
        }
        private void handleBreakpoint(final BreakpointHit breakpointHit) {
            final BreakpointHitNode node = new BreakpointHitNode(breakpointHit);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int childCount = rootNode.getChildCount();
                    boolean isFound = false;
                    for(int i=0; i<childCount; i++) {
                        CheckBoxNode checkBoxNode = (CheckBoxNode)rootNode.getChildAt(i);
                        Breakpoint breakpoint = (Breakpoint)checkBoxNode.getUserObject();
                        if(breakpoint.getID().equals(breakpointHit.getBreakpointID())) {
                            int index = checkBoxNode.getChildCount();
                            checkBoxNode.add(node);
                            breakpointTreeModel.nodesWereInserted(checkBoxNode, new int[] {index});
                            breakpointTreeModel.nodeChanged(checkBoxNode);
                            breakpointTree.expandPath(new TreePath(new Object[] {rootNode, checkBoxNode}));
                            breakpointTree.repaint();
                            isFound = true;
                            break;
                        }
                    }
                    if(!isFound) {
                        node.proceed();
                    }
                }
            });
            synchronized (node) {
                while(node.isLocked()) {
                    try {
                        node.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    };

    private void processTreeSelection() {
        TreePath[] paths = breakpointTree.getSelectionPaths();
        // TODO: commit last value.
        eastPane.removeAll();
        if(paths != null && paths.length == 1) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)paths[0].getLastPathComponent();
            if(node instanceof CheckBoxNode) {
                Object o = node.getUserObject();
                JPanel contentPane = new JPanel(new BorderLayout());
                contentPane.setBorder(BorderFactory.createTitledBorder("Breakpoint configuration"));
                contentPane.setOpaque(false);
                BreakpointEditor c = new BreakpointEditor(this, (Breakpoint)o);
                contentPane.add(c);
                eastPane.add(contentPane);
            } else if(node instanceof BreakpointHitNode) {
                JPanel contentPane = new JPanel(new BorderLayout());
                contentPane.setBorder(BorderFactory.createTitledBorder("Breakpoint hit details"));
                contentPane.setOpaque(false);
                BreakpointHitEditor c = new BreakpointHitEditor(debugger, this, (BreakpointHitNode)node);
                contentPane.add(c);
                eastPane.add(contentPane);
            }
        }
        eastPane.revalidate();
        eastPane.repaint();
    }

}
