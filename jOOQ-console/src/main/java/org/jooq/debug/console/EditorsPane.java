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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jooq.debug.QueryExecutorCreator;
import org.jooq.debug.console.misc.InvisibleSplitPane;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class EditorsPane extends JPanel {

    private QueryExecutorCreator queryExecutorCreator;
    private JTabbedPane editorTabbedPane;

    public EditorsPane(QueryExecutorCreator queryExecutorCreator, boolean isAddingTableNames) {
        super(new BorderLayout());
        this.queryExecutorCreator = queryExecutorCreator;
        setOpaque(false);
        editorTabbedPane = new JTabbedPane();
        editorTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        editorTabbedPane.addTab("New...", new JPanel());
        editorTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(!isAdjusting && editorTabbedPane.getSelectedIndex() == editorTabbedPane.getTabCount() - 1) {
                    addSQLEditorPane();
                }
            }
        });
        JComponent centerPane;
        if(isAddingTableNames) {
            JPanel tableNamePane = createTablePane();
            JSplitPane horizontalSplitPane = new InvisibleSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, tableNamePane, editorTabbedPane);
            horizontalSplitPane.setOpaque(false);
            centerPane = horizontalSplitPane;
        } else {
            centerPane = editorTabbedPane;
        }
        addSQLEditorPane();
        add(centerPane, BorderLayout.CENTER);
    }

    private JPanel createTablePane() {
        // TODO: have one table pane per execution context name (lazily created).
        final String[] tableNames = queryExecutorCreator.createQueryExecutor("default").getTableNames();
        final JList tableNamesJList = new JList(tableNames);
        tableNamesJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    Object[] values = tableNamesJList.getSelectedValues();
                    if(values.length == 1) {
                        getFocusedEditorPane().evaluate("SELECT * FROM " + values[0]);
                    }
                }
            }
        });
        JPanel tableNamePane = new JPanel(new BorderLayout());
        tableNamePane.setOpaque(false);
        JPanel tableNameFilterPane = new JPanel(new BorderLayout());
        tableNameFilterPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
        tableNameFilterPane.setOpaque(false);
        final JTextField tableNameFilterTextField = new JTextField();
        tableNameFilterTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                filter();
            }
            private void filter() {
                String filterText = tableNameFilterTextField.getText();
                final List<String> tableNameList = new ArrayList<String>(tableNames.length);
                if(filterText == null || filterText.length() == 0) {
                    tableNameList.addAll(Arrays.asList(tableNames));
                } else {
                    String[] filterTexts = filterText.split(" ");
                    for(String tableName: tableNames) {
                        boolean isAccepted = true;
                        int lastIndex = 0;
                        for(int j=0; j<filterTexts.length; j++) {
                            String filter = filterTexts[j];
                            boolean isCaseSensitive = false;
                            for(int i=filter.length()-1; i>=0; i--) {
                                if(Character.isUpperCase(filter.charAt(i))) {
                                    isCaseSensitive = true;
                                    break;
                                }
                            }
                            int index;
                            if(isCaseSensitive) {
                                index = tableName.indexOf(filter, lastIndex);
                            } else {
                                index = tableName.toLowerCase(Locale.ENGLISH).indexOf(filter.toLowerCase(Locale.ENGLISH), lastIndex);
                            }
                            if(index < 0) {
                                isAccepted = false;
                                break;
                            }
                            lastIndex = index + filter.length() + 1;
                        }
                        if(isAccepted) {
                            tableNameList.add(tableName);
                        }
                    }
                }
                tableNamesJList.setModel(new AbstractListModel() {
                    @Override
                    public int getSize() { return tableNameList.size(); }
                    @Override
                    public String getElementAt(int i) { return tableNameList.get(i); }
                });
            }
        });
        tableNameFilterTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListModel model = tableNamesJList.getModel();
                if(model.getSize() >= 1) {
                    Object selectedValue = tableNamesJList.getSelectedValue();
                    getFocusedEditorPane().evaluate("SELECT * FROM " + (selectedValue == null? model.getElementAt(0): selectedValue));
                }
            }
        });
        tableNameFilterTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                    ListModel model = tableNamesJList.getModel();
                    if(model.getSize() >= 1) {
                        tableNamesJList.setSelectedIndex(0);
                        tableNamesJList.requestFocus();
                    }
                }
            }
        });
        tableNamesJList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP: {
                        if(tableNamesJList.getSelectedIndex() == 0) {
                            tableNamesJList.clearSelection();
                            tableNameFilterTextField.requestFocus();
                        }
                        break;
                    }
                    case KeyEvent.VK_ENTER: {
                        Object selectedValue = tableNamesJList.getSelectedValue();
                        if(selectedValue != null) {
                            getFocusedEditorPane().evaluate("SELECT * FROM " + selectedValue);
                        }
                        break;
                    }
                }
            }
        });
        tableNameFilterPane.add(tableNameFilterTextField, BorderLayout.CENTER);
        tableNamePane.add(tableNameFilterPane, BorderLayout.NORTH);
        tableNamePane.add(new JScrollPane(tableNamesJList), BorderLayout.CENTER);
        return tableNamePane;
    }

    void performCleanup() {
        for(int i=editorTabbedPane.getTabCount()-2; i>=0; i--) {
            ((EditorPane)editorTabbedPane.getComponentAt(i)).closeLastExecution();
        }
    }

    void adjustDefaultFocus() {
        getFocusedEditorPane().adjustDefaultFocus();
    }

    private EditorPane getFocusedEditorPane() {
        return (EditorPane)editorTabbedPane.getSelectedComponent();
    }

    private boolean isAdjusting;
    private int contextCount = 1;

    private void addSQLEditorPane() {
        isAdjusting = true;
        int index = editorTabbedPane.getTabCount() - 1;
        final EditorPane sqlEditorPane = new EditorPane(queryExecutorCreator);
        sqlEditorPane.setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 5));
        String title = "Context " + contextCount++;
        editorTabbedPane.insertTab(title, null, sqlEditorPane, null, index);
        final JPanel tabComponent = new JPanel(new BorderLayout());
        tabComponent.setOpaque(false);
        tabComponent.add(new JLabel(title), BorderLayout.CENTER);
        final JLabel closeLabel = new JLabel(new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/TabCloseInactive14.png")));
        closeLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(!closeLabel.contains(e.getPoint())) {
                    return;
                }
                isAdjusting = true;
                if(editorTabbedPane.getTabCount() > 2) {
                    for(int i=editorTabbedPane.getTabCount()-1; i>=0; i--) {
                        if(editorTabbedPane.getTabComponentAt(i) == tabComponent) {
                            ((EditorPane)editorTabbedPane.getComponentAt(i)).closeLastExecution();
                            editorTabbedPane.removeTabAt(i);
                            if(i == editorTabbedPane.getTabCount() - 1) {
                                editorTabbedPane.setSelectedIndex(i - 1);
                            }
                            break;
                        }
                    }
                }
                isAdjusting = false;
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if(editorTabbedPane.getTabCount() > 2) {
                    closeLabel.setIcon(new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/TabCloseActive14.png")));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                closeLabel.setIcon(new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/TabCloseInactive14.png")));
            }
        });
        editorTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = editorTabbedPane.getSelectedIndex();
                Component tabComponent2 = editorTabbedPane.getTabComponentAt(selectedIndex);
                closeLabel.setVisible(tabComponent2 == tabComponent && editorTabbedPane.getTabCount() > 2);
            }
        });
        tabComponent.add(closeLabel, BorderLayout.EAST);
        editorTabbedPane.setTabComponentAt(index, tabComponent);
        editorTabbedPane.setSelectedIndex(index);
        isAdjusting = false;
        sqlEditorPane.adjustDefaultFocus();
    }

}
