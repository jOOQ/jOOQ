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
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.debug.console.misc.JTableX;
import org.jooq.debug.console.misc.Utils;

import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * @author Christopher Deckers
 */
public class EditorPane extends JPanel {

    private static final int MAX_ROW_COUNT = 10000;
    private boolean isUsingMaxRowCount = true;
    private JFormattedTextField displayedRowCountField;

    private DatabaseDescriptor databaseDescriptor;
    private SqlTextArea editorTextArea;
    private JPanel southPanel = new JPanel(new BorderLayout());
    private boolean isDBEditable;
    private JButton startButton;
    private JButton stopButton;

    EditorPane(DatabaseDescriptor databaseDescriptor) {
        super(new BorderLayout());
        this.databaseDescriptor = databaseDescriptor;
        this.isDBEditable = !databaseDescriptor.isReadOnly();
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
                evaluationThread = null;
                closeConnection();
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
//        editorTextArea = new JTextArea();
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
                                if(evaluationThread != null) {
                                    evaluationThread = null;
                                    closeConnection();
                                }
                            }
                        }.start();
                        break;
                }
            }
        });
        RTextScrollPane editorTextAreaScrollPane = new RTextScrollPane(editorTextArea);
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, editorTextAreaScrollPane, southPanel);
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
                evaluate_(sql);
            }
        });
    }

    private void evaluate_(String sql) {
    	// Here could pre process magic commands.
        boolean isAllowed = true;
        if(!isDBEditable) {
            String simplifiedSql = sql.replaceAll("'[^']*'", "");
            Matcher matcher = Pattern.compile("[a-zA-Z_0-9\\$]+").matcher(simplifiedSql);
            boolean isFirst = true;
            while(matcher.find()) {
                String word = simplifiedSql.substring(matcher.start(), matcher.end()).toUpperCase(Locale.ENGLISH);
                if(isFirst && !word.equals("SELECT")) {
                    isAllowed = false;
                    break;
                }
                isFirst = false;
                for(String keyword: new String[] {
                        "INSERT",
                        "UPDATE",
                        "DELETE",
                        "ALTER",
                        "DROP",
                        "CREATE",
                        "EXEC",
                        "EXECUTE",
                }) {
                    if(word.equals(keyword)) {
                        isAllowed = false;
                        break;
                    }
                }
            }
        }
        if(!isAllowed) {
            setMessage(southPanel, "It is only possible to execute SELECT queries.", true);
        } else {
            evaluate_unrestricted(sql);
        }
        southPanel.revalidate();
        southPanel.repaint();
    }

    private static class TypeInfo {

        private String columnName;
        private int precision;
        private int scale;
        private int nullable = ResultSetMetaData.columnNullableUnknown;

        TypeInfo(ResultSetMetaData metaData, int column) {
            try {
                columnName = metaData.getColumnTypeName(column);
                precision = metaData.getPrecision(column);
                scale = metaData.getScale(column);
                nullable = metaData.isNullable(column);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(columnName);
            if(precision != 0) {
                sb.append(" (" + precision + (scale != 0? ", " + scale: "") + ")");
            }
            if(nullable != ResultSetMetaData.columnNullableUnknown) {
                sb.append(nullable == ResultSetMetaData.columnNoNulls? " not null": " null");
            }
            return sb.toString();
        }

    }

    private volatile Connection conn;
    private volatile Statement stmt;

    private volatile Thread evaluationThread;

    private void evaluate_unrestricted(final String sql) {
        final int maxDisplayedRowCount = ((Number)displayedRowCountField.getValue()).intValue();
        evaluationThread = new Thread("SQLConsole - Evaluation") {
            @Override
            public void run() {
                evaluate_unrestricted_nothread(sql, maxDisplayedRowCount);
                evaluationThread = null;
            }
        };
        evaluationThread.start();
    }

    private void evaluate_unrestricted_nothread(final String sql, final int maxDisplayedRowCount) {
        closeConnection();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                startButton.setVisible(false);
                stopButton.setVisible(true);
                stopButton.setToolTipText("Query started on " + Utils.formatDateTimeTZ(new Date()));
            }
        });
        try {
            conn = databaseDescriptor.createConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            final long start = System.currentTimeMillis();
            if(evaluationThread != Thread.currentThread()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setMessage(addResultPane(), "Interrupted by user after " + Utils.formatDuration(System.currentTimeMillis() - start), true);
                    }
                });
                return;
            }
            boolean executeResult;
            try {
                executeResult = stmt.execute(sql);
            } catch(SQLException e) {
                if(evaluationThread != Thread.currentThread()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            setMessage(addResultPane(), "Interrupted by user after " + Utils.formatDuration(System.currentTimeMillis() - start), true);
                        }
                    });
                    return;
                }
                throw e;
            }
            final long duration = System.currentTimeMillis() - start;
            if(evaluationThread != Thread.currentThread()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setMessage(addResultPane(), "Interrupted by user after " + Utils.formatDuration(duration), true);
                    }
                });
                return;
            }
            do {
                if(executeResult) {
                    final ResultSet rs = stmt.getResultSet();
                    ResultSetMetaData metaData = rs.getMetaData();
                    // The first column is the line count
                    final String[] columnNames = new String[metaData.getColumnCount() + 1];
                    final int[] columnTypes = new int[columnNames.length];
                    final TypeInfo[] typeInfos = new TypeInfo[columnNames.length];
                    final Class<?>[] columnClasses = new Class[columnNames.length];
                    columnNames[0] = "";
                    columnClasses[0] = Integer.class;
                    for(int i=1; i<columnNames.length; i++) {
                        columnNames[i] = metaData.getColumnName(i);
                        if(columnNames[i] == null || columnNames[i].length() == 0) {
                            columnNames[i] = " ";
                        }
                        typeInfos[i] = new TypeInfo(metaData, i);
                        int type = metaData.getColumnType(i);
                        columnTypes[i] = type;
                        switch(type) {
                            case Types.CLOB:
                                columnClasses[i] = String.class;
                                break;
                            case Types.BLOB:
                                columnClasses[i] = byte[].class;
                                break;
                            default:
                                String columnClassName = metaData.getColumnClassName(i);
                                if(columnClassName == null) {
                                    System.err.println("Unknown SQL Type for \"" + columnNames[i] + "\" in SQLEditorPane: " + metaData.getColumnTypeName(i));
                                    columnClasses[i] = Object.class;
                                } else {
                                    columnClasses[i] = Class.forName(columnClassName);
                                }
                                break;
                        }
                    }
                    if(evaluationThread != Thread.currentThread()) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                setMessage(addResultPane(), "Interrupted by user after " + Utils.formatDuration(duration), true);
                            }
                        });
                        return;
                    }
                    final List<Object[]> rowDataList = new ArrayList<Object[]>();
                    int rowCount = 0;
                    long rsStart = System.currentTimeMillis();
                    while(rs.next() && (!isUsingMaxRowCount || rowCount < MAX_ROW_COUNT)) {
                        if(evaluationThread != Thread.currentThread()) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    setMessage(addResultPane(), "Interrupted by user after " + Utils.formatDuration(duration), true);
                                }
                            });
                            return;
                        }
                        rowCount++;
                        Object[] rowData = new Object[columnNames.length];
                        rowData[0] = rowCount;
                        for(int i=1; i<columnNames.length; i++) {
                            switch(columnTypes[i]) {
                                case Types.CLOB: {
                                    Clob clob = rs.getClob(i);
                                    if(clob != null) {
                                        StringWriter stringWriter = new StringWriter();
                                        char[] chars = new char[1024];
                                        Reader reader = new BufferedReader(clob.getCharacterStream());
                                        for(int count; (count=reader.read(chars))>=0; ) {
                                            stringWriter.write(chars, 0, count);
                                        }
                                        rowData[i] = stringWriter.toString();
                                    } else {
                                        rowData[i] = null;
                                    }
                                    break;
                                }
                                case Types.BLOB: {
                                    Blob blob = rs.getBlob(i);
                                    if(blob != null) {
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        byte[] bytes = new byte[1024];
                                        InputStream in = new BufferedInputStream(blob.getBinaryStream());
                                        for(int count; (count=in.read(bytes))>=0; ) {
                                            baos.write(bytes, 0, count);
                                        }
                                        rowData[i] = baos.toByteArray();
                                    } else {
                                        rowData[i] = null;
                                    }
                                    break;
                                }
                                default:
                                	Object object = rs.getObject(i);
                                	if(object != null) {
                                		String className = object.getClass().getName();
                                		if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
                                			object = rs.getTimestamp(i);
                                		}
                                		// Probably something to do for oracle.sql.DATE
                                	}
                                	rowData[i] = object;
                                	break;
                            }
                        }
                        if(rowCount <= maxDisplayedRowCount) {
                            rowDataList.add(rowData);
                        } else if(rowCount == maxDisplayedRowCount + 1) {
                            rowDataList.clear();
                        }
                    }
                    final long rsDuration = System.currentTimeMillis() - rsStart;
                    final int rowCount_ = rowCount;
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            addResultTable(sql, duration, rs, columnNames, typeInfos, columnClasses, rowDataList, rowCount_, rsDuration, maxDisplayedRowCount);
                        }
                    });
                } else {
                    final int updateCount = stmt.getUpdateCount();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            setMessage(addResultPane(), Utils.formatDuration(duration) + "> " + updateCount + " row(s) affected.", false);
                        }
                    });
                }
                if(databaseDescriptor.getSQLDialect() == SQLDialect.SQLSERVER) {
                    try {
                        executeResult = stmt.getMoreResults(Statement.KEEP_CURRENT_RESULT);
                    } catch(Exception e) {
                        executeResult = stmt.getMoreResults();
                    }
                } else {
                    executeResult = false;
                }
            } while(executeResult || stmt.getUpdateCount() != -1);
        } catch(Exception e) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            final String message = stringWriter.toString();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setMessage(addResultPane(), message, true);
                }
            });
        } finally {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    startButton.setVisible(true);
                    stopButton.setVisible(false);
                    stopButton.setToolTipText(null);
                }
            });
            if(!isDBEditable) {
                closeConnection();
            }
        }
    }

    private void addResultTable(final String sql, long duration, final ResultSet rs, final String[] columnNames, final TypeInfo[] typeInfos, final Class<?>[] columnClasses, final List<Object[]> rowDataList, int rowCount, long rsDuration, int maxDisplayedRowCount) {
        JPanel resultPane = addResultPane();
        final JLabel label = new JLabel(" " + rowCount + " rows");
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        flowLayout.setAlignOnBaseline(true);
        JPanel statusCountPane = new JPanel(flowLayout);
        if(rowCount <= maxDisplayedRowCount) {
            final JTableX table = new JTableX(new AbstractTableModel() {
                @Override
                public String getColumnName(int column) {
                    return columnNames[column].toString();
                }
                @Override
                public int getRowCount() {
                    return rowDataList.size();
                }
                @Override
                public int getColumnCount() {
                    return columnNames.length;
                }
                @Override
                public Object getValueAt(int row, int col) {
                    return rowDataList.get(row)[col];
                }
                @Override
                public void setValueAt(Object o, int row, int col) {
                    if(Utils.equals(o, rowDataList.get(row)[col])) {
                        return;
                    }
                    int dbRow = (Integer)rowDataList.get(row)[0];
                    try {
                        rs.absolute(dbRow);
                        rs.updateObject(col, o);
                        rs.updateRow();
                        rowDataList.get(row)[col] = o;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        try {
                            rs.cancelRowUpdates();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    try {
                        if(rs.getConcurrency() == ResultSet.CONCUR_UPDATABLE) {
                            return isDBEditable && columnIndex > 0;
                        }
                    } catch (SQLException e) {
                    }
                    return false;
                }
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnClasses[columnIndex];
                }
            }) {
                @Override
                public TableCellEditor getCellEditor(int row, int column) {
                    TableCellEditor editor = super.getCellEditor(row, column);
                    if(editor instanceof DefaultCellEditor) {
                        DefaultCellEditor defaultEditor = (DefaultCellEditor) editor;
                        defaultEditor.setClickCountToStart(2);
                    }
                    return editor;
                }
            };
            JTableHeader tableHeader = new JTableHeader(table.getColumnModel()) {
                @Override
                public String getToolTipText(MouseEvent e) {
                    int col = getTable().convertColumnIndexToModel(columnAtPoint(e.getPoint()));
                    return col == 0? null: typeInfos[col].toString();
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
                    label.setText(" " + rowDataList.size() + " rows" + (selectedRowCount == 0? "": " - " + selectedRowCount + " selected rows"));
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
                    boolean isEditable = false;
                    try {
                        if(rs.getConcurrency() == ResultSet.CONCUR_UPDATABLE) {
                            isEditable = isDBEditable;
                        }
                    } catch (SQLException ex) {
                        isEditable = false;
                    }
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
                                    int dbRow = (Integer)rowDataList.get(row)[0];
                                    try {
                                        rs.absolute(dbRow);
                                        rs.deleteRow();
                                        rowDataList.remove(row);
                                        ((AbstractTableModel)table.getModel()).fireTableRowsDeleted(row, row);
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
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
            final JToggleButton filterToggleButton = new JToggleButton(new ImageIcon(getClass().getResource("/org/jooq/debug/console/resources/Search16.png")));
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
            statusCountPane.add(filterPane);
        }
        JPanel southPanel = new JPanel(new BorderLayout());
        if(isUsingMaxRowCount && rowCount == MAX_ROW_COUNT) {
            label.setForeground(Color.RED);
        }
        statusCountPane.add(label);
        southPanel.add(statusCountPane, BorderLayout.WEST);
        southPanel.add(new JLabel(Utils.formatDuration(duration) + " - " + Utils.formatDuration(rsDuration)), BorderLayout.EAST);
        resultPane.add(southPanel, BorderLayout.SOUTH);
        southPanel.setToolTipText(sql);
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

    void closeConnection() {
        if (conn != null) {
            if (stmt != null) {
                try {
                    stmt.cancel();
                } catch (Exception e) {
                }
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            stmt = null;
            try {
                conn.close();
            } catch (Exception e) {
            }
            conn = null;
        }
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
            for(String s: getTableNames()) {
                candidateList.add(new CompletionCandidate(KeyWordType.TABLE, s));
            }
            for(String s: getTableColumnNames()) {
                candidateList.add(new CompletionCandidate(KeyWordType.TABLE_COlUMN, s));
            }
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

    private String[] getTableNames() {
        List<String> tableNameList = new ArrayList<String>();
        for(Table<? extends Record> table: databaseDescriptor.getSchema().getTables()) {
        	String tableName = table.getName();
        	tableNameList.add(tableName);
        }
        Collections.sort(tableNameList, String.CASE_INSENSITIVE_ORDER);
        return tableNameList.toArray(new String[0]);
    }

    private String[] getTableColumnNames() {
        Set<String> columnNameSet = new HashSet<String>();
        for(Table<?> table: databaseDescriptor.getSchema().getTables()) {
        	for(Field<?> field: table.getFields()) {
        		String columnName = field.getName();
        		columnNameSet.add(columnName);
        	}
        }
        String[] columnNames = columnNameSet.toArray(new String[0]);
        Arrays.sort(columnNames, String.CASE_INSENSITIVE_ORDER);
		return columnNames;
    }

    public void adjustDefaultFocus() {
        editorTextArea.requestFocusInWindow();
    }

}
