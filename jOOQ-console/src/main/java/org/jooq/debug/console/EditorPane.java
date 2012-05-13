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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;

import org.jooq.debug.StatementExecution;
import org.jooq.debug.StatementExecutionMessageResult;
import org.jooq.debug.StatementExecutionResult;
import org.jooq.debug.StatementExecutionResultSetResult;
import org.jooq.debug.StatementExecutor;
import org.jooq.debug.StatementExecutorCreator;
import org.jooq.debug.console.misc.InvisibleSplitPane;
import org.jooq.debug.console.misc.JTableX;
import org.jooq.debug.console.misc.Utils;

import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class EditorPane extends JPanel {

    private static final int MAX_ROW_COUNT = 10000;
    private boolean isUsingMaxRowCount = true;
    private JFormattedTextField displayedRowCountField;

    private SqlTextArea editorTextArea;
    private JPanel southPanel;
    private StatementExecutorCreator statementExecutorCreator;
    private final Object STATEMENT_EXECUTOR_CREATOR_LOCK = new Object();
    private StatementExecutor lastStatementExecutor;
    private JButton startButton;
    private JButton stopButton;

    EditorPane(StatementExecutorCreator statementExecutorCreator) {
        super(new BorderLayout());
        this.statementExecutorCreator = statementExecutorCreator;
        setOpaque(false);
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        JToolBar northWestPanel = new JToolBar();
        northWestPanel.setOpaque(false);
        northWestPanel.setFloatable(false);
        startButton = new JButton(new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/Play16.png")));
        startButton.setOpaque(false);
        startButton.setFocusable(false);
//        startButton.setMargin(new Insets(2, 2, 2, 2));
        startButton.setToolTipText("Run the (selected) text (F5)");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                evaluateInternal();
            }
        });
        northWestPanel.add(startButton);
        stopButton = new JButton(new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/Stop16.png")));
        stopButton.setVisible(false);
        stopButton.setOpaque(false);
        stopButton.setFocusable(false);
//        stopButton.setMargin(new Insets(2, 2, 2, 2));
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeLastExecution();
            }
        });
        northWestPanel.add(stopButton);
        northPanel.add(northWestPanel, BorderLayout.WEST);
        JPanel northEastPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        northEastPanel.setOpaque(false);
        JCheckBox limitCheckBox = new JCheckBox("Parse 10000 rows max", isUsingMaxRowCount);
        limitCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                isUsingMaxRowCount = e.getStateChange() == ItemEvent.SELECTED;
            }
        });
        limitCheckBox.setOpaque(false);
        northEastPanel.add(limitCheckBox);
        northEastPanel.add(Box.createHorizontalStrut(5));
        northEastPanel.add(new JLabel("No display when rows >"));
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        displayedRowCountField = new JFormattedTextField(numberFormat);
        displayedRowCountField.setHorizontalAlignment(JFormattedTextField.RIGHT);
        displayedRowCountField.setValue(100000);
        displayedRowCountField.setColumns(7);
        northEastPanel.add(displayedRowCountField);
        northPanel.add(northEastPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);
        editorTextArea = new SqlTextArea();
        editorTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean isControlDown = e.isControlDown();
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        if(isControlDown) {
                            showCompletion();
                        }
                        break;
                    case KeyEvent.VK_F5:
                        if(startButton.isVisible()) {
                            evaluateInternal();
                        }
                        break;
                    case KeyEvent.VK_ESCAPE:
                        new Thread("SQLConsole - Interruption") {
                            @Override
                            public void run() {
                                closeLastExecution();
                            }
                        }.start();
                        break;
                }
            }
        });
        RTextScrollPane editorTextAreaScrollPane = new RTextScrollPane(editorTextArea);
        southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        JSplitPane verticalSplitPane = new InvisibleSplitPane(JSplitPane.VERTICAL_SPLIT, true, editorTextAreaScrollPane, southPanel);
        verticalSplitPane.setOpaque(false);
        add(verticalSplitPane, BorderLayout.CENTER);
        verticalSplitPane.setDividerLocation(150);
    }

    private void evaluateInternal() {
        String text = editorTextArea.getSelectedText();
        if(text == null || text.length() == 0) {
            text = editorTextArea.getText();
        }
        evaluateInternal(text);
    }

    public void evaluate(final String sql) {
        int caretPosition = editorTextArea.getSelectionStart();
        if(caretPosition == editorTextArea.getSelectionEnd()) {
            String text = editorTextArea.getText();
            if((caretPosition == 0 || text.charAt(caretPosition - 1) == '\n') && (text.length() <= caretPosition || text.charAt(caretPosition) == '\n')) {
                editorTextArea.insert(sql + '\n', caretPosition);
            }
        }
        evaluateInternal(sql);
    }

    public void evaluateInternal(final String sql) {
        southPanel.removeAll();
        southPanel.revalidate();
        southPanel.repaint();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final int maxDisplayedRowCount = ((Number)displayedRowCountField.getValue()).intValue();
                Thread evaluationThread = new Thread("SQLConsole - Evaluation") {
                    @Override
                    public void run() {
                        evaluate_unrestricted_nothread(sql, maxDisplayedRowCount);
                    }
                };
                evaluationThread.start();
            }
        });
    }

    private void evaluate_unrestricted_nothread(final String sql, final int maxDisplayedRowCount) {
        closeLastExecution();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                startButton.setVisible(false);
                stopButton.setVisible(true);
                stopButton.setToolTipText("Query started on " + Utils.formatDateTimeTZ(new Date()));
            }
        });
        StatementExecutor statementExecutor;
        synchronized (STATEMENT_EXECUTOR_CREATOR_LOCK) {
            statementExecutor = statementExecutorCreator.createStatementExecutor();
            lastStatementExecutor = statementExecutor;
        }
        StatementExecution statementExecution;
        try {
            statementExecution = statementExecutor.execute(sql, isUsingMaxRowCount? MAX_ROW_COUNT: Integer.MAX_VALUE, maxDisplayedRowCount);
        } finally {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    startButton.setVisible(true);
                    stopButton.setVisible(false);
                    stopButton.setToolTipText(null);
                }
            });
        }
        final StatementExecutionResult[] results = statementExecution.getResults();
        final long executionDuration = statementExecution.getExecutionDuration();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for(StatementExecutionResult result: results) {
                    if(result instanceof StatementExecutionMessageResult) {
                        StatementExecutionMessageResult messageResult = (StatementExecutionMessageResult)result;
                        setMessage(addResultPane(), messageResult.getMessage(), messageResult.isError());
                    } else if(result instanceof StatementExecutionResultSetResult) {
                        addResultTable(sql, executionDuration, (StatementExecutionResultSetResult)result);
                    } else {
                        throw new IllegalStateException("Unknown result class: " + result.getClass().getName());
                    }
                }
            }
        });
    }

    private void addResultTable(final String sql, long duration, final StatementExecutionResultSetResult resultSetResult) {
        int rowCount = resultSetResult.getRowCount();
        JPanel resultPane = addResultPane();
        final JLabel label = new JLabel(" " + rowCount + " rows");
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        flowLayout.setAlignOnBaseline(true);
        JPanel statusPane = new JPanel(flowLayout);
        statusPane.setOpaque(false);
        if(rowCount <= resultSetResult.getRetainParsedRSDataRowCountThreshold()) {
            final JTableX table = new ResultTable(resultSetResult);
            JTableHeader tableHeader = new JTableHeader(table.getColumnModel()) {
                @Override
                public String getToolTipText(MouseEvent e) {
                    int col = getTable().convertColumnIndexToModel(columnAtPoint(e.getPoint()));
                    return col == 0? null: resultSetResult.getTypeInfos()[col - 1].toString();
                }
            };
            ToolTipManager.sharedInstance().registerComponent(tableHeader);
            table.setTableHeader(tableHeader);
            table.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    if(value != null) {
                        value = "\"" + value + "\"";
                    }
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            });
            final TableCellRenderer booleanRenderer = table.getDefaultRenderer(Boolean.class);
            table.setDefaultRenderer(Boolean.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = null;
                    if(value == null) {
                        c = super.getTableCellRendererComponent(table, " ", isSelected, hasFocus, row, column);
                    } else {
                        c = booleanRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    }
                    if(!isSelected) {
                        c.setBackground(row %2 == 0? UIManager.getColor("Table.background"): JTableX.getTableAlternateRowBackgroundColor());
                    }
                    return c;
                }
            });
            table.setDefaultRenderer(Timestamp.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if(value != null && c instanceof JLabel) {
                        Timestamp timestamp = (Timestamp)value;
                        String s;
                        if(timestamp.getHours() != 0 || timestamp.getMinutes() != 0 || timestamp.getSeconds() != 0) {
                            s = Utils.formatDateTimeGMT((Timestamp)value);
                        } else {
                            s = Utils.formatDateGMT((Timestamp)value);
                        }
                        ((JLabel) c).setText(s);
                    }
                    return c;
                }
            });
            table.setDefaultRenderer(byte[].class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if(value != null && c instanceof JLabel) {
                        byte[] bytes = (byte[])value;
                        ((JLabel) c).setText("Blob (" + bytes.length + " bytes)");
                    }
                    return c;
                }
            });
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setAutoCreateRowSorter(true);
            table.getRowSorter().setSortKeys(Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
            table.setColumnSelectionAllowed(true);
            table.applyMinimumAndPreferredColumnSizes(200);
            resultPane.add(new JScrollPane(table), BorderLayout.CENTER);
            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    int selectedRowCount = table.getSelectedRowCount();
                    label.setText(" " + resultSetResult.getRowData().length + " rows" + (selectedRowCount == 0? "": " - " + selectedRowCount + " selected rows"));
                }
            });
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int row = table.rowAtPoint(e.getPoint());
                    int column = table.columnAtPoint(e.getPoint());
                    if(!table.isCellSelected(row, column)) {
                        table.changeSelection(row, column, false, false);
                    }
                    maybeShowPopup(e);
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    maybeShowPopup(e);
                }
                private void maybeShowPopup(MouseEvent e) {
                    if(!e.isPopupTrigger()) {
                        return;
                    }
                    boolean isEditable = resultSetResult.isEditable();
                    JPopupMenu menu = new JPopupMenu();
                    int selectedRowCount = table.getSelectedRowCount();
                    int selectedColumnCount = table.getSelectedColumnCount();
                    boolean isAddingSeparator = false;
                    // Here to add custom menus.
                    if(isEditable) {
                        if(isAddingSeparator) {
                            isAddingSeparator = false;
                            menu.addSeparator();
                        }
                        JMenuItem insertNullMenuItem = new JMenuItem("Insert NULL");
                        insertNullMenuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int row = table.getSelectedRow();
                                int column = table.getSelectedColumn();
                                table.setValueAt(null, row, column);
                            }
                        });
                        insertNullMenuItem.setEnabled(selectedRowCount == 1 && selectedColumnCount == 1 && table.convertColumnIndexToModel(table.getSelectedColumn()) != 0);
                        menu.add(insertNullMenuItem);
                        menu.addSeparator();
                        JMenuItem deleteRowMenuItem = new JMenuItem("Delete " + (selectedRowCount > 1? selectedRowCount + " rows": "row"));
                        deleteRowMenuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int[] selectedRows = table.getSelectedRows();
                                for(int i=selectedRows.length-1; i>=0; i--) {
                                    selectedRows[i] = table.convertRowIndexToModel(selectedRows[i]);
                                }
                                Arrays.sort(selectedRows);
                                for(int i=selectedRows.length-1; i>=0; i--) {
                                    int row = selectedRows[i];
                                    boolean isSuccess = resultSetResult.deleteRow(row);
                                    if(isSuccess) {
                                        ((AbstractTableModel)table.getModel()).fireTableRowsDeleted(row, row);
                                    }
                                }
                            }
                        });
                        deleteRowMenuItem.setEnabled(selectedRowCount > 0);
                        menu.add(deleteRowMenuItem);
                    }
                    if(menu.getComponentCount() > 0) {
                        menu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });
            final JPanel filterPane = new JPanel(flowLayout);
            filterPane.setOpaque(false);
            final JToggleButton filterToggleButton = new JToggleButton(new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/Search16.png")));
            filterToggleButton.setOpaque(false);
            filterToggleButton.setToolTipText("Filter (" + KeyEvent.getKeyModifiersText(InputEvent.CTRL_MASK) + "+" + KeyEvent.getKeyText(KeyEvent.VK_F) + ")");
            filterToggleButton.setMargin(new Insets(0, 0, 0, 0));
            filterPane.add(filterToggleButton);
            final JTextField searchField = new JTextField(7);
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void removeUpdate(DocumentEvent e) {
                    adjustFilter();
                }
                @Override
                public void insertUpdate(DocumentEvent e) {
                    adjustFilter();
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    adjustFilter();
                }
                private void adjustFilter() {
                    setFilter(table, searchField.getText());
                }
            });
            searchField.setVisible(false);
            filterPane.add(searchField);
            filterToggleButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    boolean isActive = e.getStateChange() == ItemEvent.SELECTED;
                    searchField.setVisible(isActive);
                    if(isActive) {
                        searchField.requestFocus();
                    } else {
                        searchField.setText("");
                        table.requestFocus();
                    }
                    filterPane.revalidate();
                    filterPane.repaint();
                }
            });
            searchField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        e.consume();
                        filterToggleButton.setSelected(false);
                    } else if(e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {
                        filterToggleButton.setSelected(false);
                    }
                }
            });
            table.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {
                        e.consume();
                        filterToggleButton.setSelected(true);
                        searchField.requestFocus();
                        searchField.selectAll();
                    }
                }
            });
            statusPane.add(filterPane);
        }
        JPanel southResultPanel = new JPanel(new BorderLayout());
        southResultPanel.setOpaque(false);
        if(isUsingMaxRowCount && rowCount == MAX_ROW_COUNT) {
            label.setForeground(Color.RED);
        }
        statusPane.add(label);
        southResultPanel.add(statusPane, BorderLayout.WEST);
        southResultPanel.add(new JLabel(Utils.formatDuration(duration) + " - " + Utils.formatDuration(resultSetResult.getResultSetParsingDuration())), BorderLayout.EAST);
        resultPane.add(southResultPanel, BorderLayout.SOUTH);
        southResultPanel.setToolTipText(sql);
        resultPane.revalidate();
        resultPane.repaint();
    }

    private String filter;

    public void setFilter(JTable table, String filter) {
        if(filter != null && filter.length() == 0) {
            this.filter = null;
            ((TableRowSorter<?>)table.getRowSorter()).setRowFilter(null);
            return;
        }
        this.filter = filter;
        ((TableRowSorter<?>)table.getRowSorter()).setRowFilter(new RowFilter<Object, Object>() {
            @Override
            public boolean include(javax.swing.RowFilter.Entry<? extends Object, ? extends Object> entry) {
                String lcFilter = EditorPane.this.filter.toLowerCase();
                for (int i = entry.getValueCount() - 1; i >= 0; i--) {
                    if (entry.getStringValue(i).toLowerCase().contains(lcFilter)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private JPanel addResultPane() {
        JPanel resultPane = new JPanel(new BorderLayout());
        resultPane.setOpaque(false);
        int componentCount = southPanel.getComponentCount();
        if(componentCount == 0) {
            southPanel.add(resultPane, BorderLayout.CENTER);
            southPanel.revalidate();
            southPanel.repaint();
        } else {
            JTabbedPane tabbedPane;
            Component component0 = southPanel.getComponent(0);
            if(component0 instanceof JTabbedPane) {
                tabbedPane = (JTabbedPane)component0;
            } else {
                JPanel query1Panel = (JPanel)component0;
                southPanel.remove(0);
                tabbedPane = new JTabbedPane();
                tabbedPane.addTab("Query 1", query1Panel);
                southPanel.add(tabbedPane, BorderLayout.CENTER);
                southPanel.revalidate();
                southPanel.repaint();
            }
            tabbedPane.addTab("Query " + (tabbedPane.getTabCount() + 1), resultPane);
        }
        return resultPane;
    }

    void closeLastExecution() {
        synchronized (STATEMENT_EXECUTOR_CREATOR_LOCK) {
            if(lastStatementExecutor != null) {
                lastStatementExecutor.stopExecution();
                lastStatementExecutor = null;
            }
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        // TODO: if we want to recycle screens, should we do this?
        closeLastExecution();
    }

    private void setMessage(JPanel messageContentPanel, String message, boolean isError) {
        JTextArea textArea = new JTextArea(message);
        Font editorFont = textArea.getFont().deriveFont(UIManager.getFont("TextField.font").getSize2D());
        textArea.setFont(new Font("Monospaced", editorFont.getStyle(), editorFont.getSize()));
        textArea.setEditable(false);
        if(isError) {
            textArea.setForeground(Color.RED);
        }
        messageContentPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        messageContentPanel.revalidate();
        messageContentPanel.repaint();
    }

    private void showCompletion() {
        try {
            int caretPosition = editorTextArea.getCaretPosition();
            int wordStart = getWordStart();
            CompletionCandidate[] words = getFilteredWords(wordStart, editorTextArea.getText(wordStart, caretPosition - wordStart));
            if(words.length == 0) {
                return;
            }
            final JPopupMenu w = new JPopupMenu();
            final JList jList = new JList(words);
            jList.setCellRenderer(new DefaultListCellRenderer() {
            	private ImageIcon tableIcon = new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/Table16.png"));
            	private ImageIcon tableColumnIcon = new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/TableColumn16.png"));
            	private ImageIcon sqlIcon = new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/SQL16.png"));
                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if(c instanceof JLabel) {
                        Icon icon;
                        switch(((CompletionCandidate)value).getKeyWordType()) {
                            case TABLE: icon = tableIcon; break;
                            case TABLE_COlUMN: icon = tableColumnIcon; break;
                            default: icon = sqlIcon; break;
                        }
                        ((JLabel)c).setIcon(icon);
                    }
                    return c;
                }
            });
            jList.setSelectedIndex(0);
            jList.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch(e.getKeyCode()) {
                        case KeyEvent.VK_PAGE_UP:
                        case KeyEvent.VK_PAGE_DOWN:
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_HOME:
                        case KeyEvent.VK_END:
                        case KeyEvent.VK_ESCAPE:
                            return;
                        case KeyEvent.VK_ENTER:
                            editorTextArea.replaceRange(((CompletionCandidate)jList.getSelectedValue()).toString(), getWordStart(), editorTextArea.getCaretPosition());
                            w.setVisible(false);
                            return;
                    }
                    editorTextArea.dispatchEvent(new KeyEvent(editorTextArea, e.getID(), e.getWhen(), e.getModifiers(), e.getKeyCode(), e.getKeyChar(), e.getKeyLocation()));
                    if(e.getKeyChar() != KeyEvent.CHAR_UNDEFINED || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        adjustListContent();
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    switch(e.getKeyCode()) {
                        case KeyEvent.VK_PAGE_UP:
                        case KeyEvent.VK_PAGE_DOWN:
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_HOME:
                        case KeyEvent.VK_END:
                        case KeyEvent.VK_ESCAPE:
                        case KeyEvent.VK_ENTER:
                            return;
                    }
                    editorTextArea.dispatchEvent(new KeyEvent(editorTextArea, e.getID(), e.getWhen(), e.getModifiers(), e.getKeyCode(), e.getKeyChar(), e.getKeyLocation()));
                    if(e.getKeyChar() != KeyEvent.CHAR_UNDEFINED || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        adjustListContent();
                    }
                }
                @Override
                public void keyTyped(KeyEvent e) {
                    switch(e.getKeyCode()) {
                        case KeyEvent.VK_PAGE_UP:
                        case KeyEvent.VK_PAGE_DOWN:
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_HOME:
                        case KeyEvent.VK_END:
                        case KeyEvent.VK_ESCAPE:
                        case KeyEvent.VK_ENTER:
                            return;
                    }
                    editorTextArea.dispatchEvent(new KeyEvent(editorTextArea, e.getID(), e.getWhen(), e.getModifiers(), e.getKeyCode(), e.getKeyChar(), e.getKeyLocation()));
                    if(e.getKeyChar() != KeyEvent.CHAR_UNDEFINED || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        adjustListContent();
                    }
                }
                private void adjustListContent() {
                    try {
                        int wordStart = getWordStart();
                        CompletionCandidate[] words = getFilteredWords(wordStart, editorTextArea.getText(wordStart, editorTextArea.getCaretPosition() - wordStart));
                        if(words.length == 0) {
                            w.setVisible(false);
                            return;
                        }
                        jList.setListData(words);
                        jList.setSelectedIndex(0);
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }
            });
            jList.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                        editorTextArea.replaceRange(((CompletionCandidate)jList.getSelectedValue()).toString(), getWordStart(), editorTextArea.getCaretPosition());
                        w.setVisible(false);
                    }
                }
            });
            w.add(new JScrollPane(jList), BorderLayout.CENTER);
            w.setPreferredSize(new Dimension(200, 200));
            Rectangle position = editorTextArea.modelToView(caretPosition);
            w.show(editorTextArea, position.x + position.width, position.y + position.height);
            jList.requestFocus();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private int getWordStart() {
        int caretPosition = editorTextArea.getCaretPosition();
        int wordStart = caretPosition;
        while(wordStart > 0) {
            try {
                char c = editorTextArea.getText(wordStart - 1, 1).charAt(0);
                if(!Character.isLetterOrDigit(c) && c!= '_') {
                    break;
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            wordStart--;
        }
        return wordStart;
    }

    private final class ResultTableModel extends AbstractTableModel {

        private final StatementExecutionResultSetResult resultSetResult;

        private ResultTableModel(StatementExecutionResultSetResult resultSetResult) {
            this.resultSetResult = resultSetResult;
        }

        @Override
        public String getColumnName(int column) {
            return column == 0? "": resultSetResult.getColumnNames()[column - 1].toString();
        }

        @Override
        public int getRowCount() {
            return resultSetResult.getRowData().length;
        }

        @Override
        public int getColumnCount() {
            return resultSetResult.getColumnNames().length + 1;
        }

        @Override
        public Object getValueAt(int row, int col) {
            return col == 0? row + 1: resultSetResult.getRowData()[row][col - 1];
        }

        @Override
        public void setValueAt(Object o, int row, int col) {
            resultSetResult.setValueAt(o, row, col - 1);
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col > 0 && resultSetResult.isEditable();
        }

        @Override
        public Class<?> getColumnClass(int col) {
            return col == 0? Integer.class: resultSetResult.getColumnClasses()[col - 1];
        }
    }

    private final class ResultTable extends JTableX {
        private ResultTable(StatementExecutionResultSetResult resultSetResult) {
            super(new ResultTableModel(resultSetResult));
        }

        @Override
        public TableCellEditor getCellEditor(int row, int column) {
            TableCellEditor editor = super.getCellEditor(row, column);
            if(editor instanceof DefaultCellEditor) {
                DefaultCellEditor defaultEditor = (DefaultCellEditor) editor;
                defaultEditor.setClickCountToStart(2);
            }
            return editor;
        }
    }

    private static enum KeyWordType {
        DYNAMIC_STATEMENT,
        TABLE,
        TABLE_COlUMN,
        KEYWORD,
    }

    private static class CompletionCandidate {
        private KeyWordType keyWordType;
        private String word;
        public CompletionCandidate(KeyWordType keyWordType, String word) {
            this.keyWordType = keyWordType;
            this.word = word;
        }
        public KeyWordType getKeyWordType() {
            return keyWordType;
        }
        @Override
        public String toString() {
            return word;
        }
    }

    private CompletionCandidate[] getFilteredWords(int wordStart, String word) {
        List<CompletionCandidate> candidateList = new ArrayList<CompletionCandidate>();
        // Here can add more candidates depending on magic word start.
        if(candidateList.isEmpty()) {
            StatementExecutor statementExecutor = statementExecutorCreator.createStatementExecutor();
            for(String s: statementExecutor.getTableNames()) {
                candidateList.add(new CompletionCandidate(KeyWordType.TABLE, s));
            }
            for(String s: statementExecutor.getTableColumnNames()) {
                candidateList.add(new CompletionCandidate(KeyWordType.TABLE_COlUMN, s));
            }
            statementExecutor.stopExecution();
            for(String s: new String[] {
                    "ALTER",
                    "ASC",
                    "BY",
                    "DESC",
                    "DROP",
                    "FROM",
                    "IN",
                    "INNER",
                    "INSERT",
                    "INTO",
                    "JOIN",
                    "LEFT",
                    "NOT",
                    "ORDER",
                    "SELECT",
                    "UPDATE",
                    "WHERE",
                    "AND",
                    "OR",
                    "EXISTS",
                    "HAVING",
                    "TOP",
                    "GROUP",
                    "SET",
            }) {
                candidateList.add(new CompletionCandidate(KeyWordType.KEYWORD, s));
            }
        }
        word = word.toUpperCase(Locale.ENGLISH);
        List<CompletionCandidate> filteredCompletionCandidateList = new ArrayList<CompletionCandidate>();
        for(CompletionCandidate completionCandidate: candidateList) {
            if(completionCandidate.toString().toLowerCase(Locale.ENGLISH).startsWith(word.toLowerCase(Locale.ENGLISH))) {
                filteredCompletionCandidateList.add(completionCandidate);
            }
        }
        Collections.sort(filteredCompletionCandidateList, new Comparator<CompletionCandidate>() {
            @Override
            public int compare(CompletionCandidate o1, CompletionCandidate o2) {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.toString(), o2.toString());
            }
        });
        return filteredCompletionCandidateList.toArray(new CompletionCandidate[0]);
    }

    public void adjustDefaultFocus() {
        editorTextArea.requestFocusInWindow();
    }

}
