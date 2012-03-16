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
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jooq.Record;
import org.jooq.Table;
import org.jooq.debug.console.remote.RemoteDebuggerClient;

/**
 * @author Christopher Deckers
 */
public class Console extends JFrame {

    private JTabbedPane mainTabbedPane;
    private JTabbedPane editorTabbedPane;

    public Console(final DatabaseDescriptor editorDatabaseDescriptor, boolean isShowingLoggingTab) {
    	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	JMenuBar menuBar = new JMenuBar();
    	JMenu fileMenu = new JMenu("File");
    	fileMenu.setMnemonic('F');
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic('x');
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    performCleanup();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                switch(getDefaultCloseOperation()) {
                    case HIDE_ON_CLOSE:
                        setVisible(false);
                        break;
                    case DISPOSE_ON_CLOSE:
                        dispose();
                        break;
                    case EXIT_ON_CLOSE:
                        System.exit(0);
                        break;
                    case DO_NOTHING_ON_CLOSE:
                    default:
                        break;
                }
            }
        });
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
    	JMenu helpMenu = new JMenu("Help");
    	helpMenu.setMnemonic('H');
    	JMenuItem aboutMenuItem = new JMenuItem("About");
    	aboutMenuItem.setMnemonic('A');
    	aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JDialog aboutDialog = new JDialog(Console.this, "About jOOQ Console", ModalityType.APPLICATION_MODAL);
                aboutDialog.setResizable(false);
                Container contentPane = aboutDialog.getContentPane();
                JPanel centerPane = new JPanel(new GridBagLayout());
                centerPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                centerPane.add(new JLabel("jOOQ library: "), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                centerPane.add(new JLabel("Lukas Eder"), new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                centerPane.add(new JLabel("jOOQ Console: "), new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
                centerPane.add(new JLabel("Christopher Deckers"), new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
                centerPane.add(new JLabel("License: "), new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
                centerPane.add(new JLabel("Apache License, Version 2.0"), new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
                centerPane.add(new JLabel("Web site: "), new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
                JLabel siteLabel = new JLabel("http://www.jooq.org");
                siteLabel.setForeground(Color.BLUE);
                Map<TextAttribute, Object> attributeMap = new HashMap<TextAttribute, Object>();
                attributeMap.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
                siteLabel.setFont(siteLabel.getFont().deriveFont(attributeMap));
                siteLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                siteLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        try {
                            Desktop.getDesktop().browse(new URI("http://www.jooq.org"));
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                centerPane.add(siteLabel, new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
                contentPane.add(centerPane, BorderLayout.CENTER);
                JPanel southPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        aboutDialog.dispose();
                    }
                });
                southPane.add(okButton);
                contentPane.add(southPane, BorderLayout.SOUTH);
                aboutDialog.pack();
                aboutDialog.setLocationRelativeTo(Console.this);
                aboutDialog.setVisible(true);
            }
        });
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        mainTabbedPane = new JTabbedPane();
        String title = "jOOQ Console";
        if(editorDatabaseDescriptor != null) {
        	String schemaName = editorDatabaseDescriptor.getSchema().getName();
        	if(schemaName != null && schemaName.length() != 0) {
        		title += " - " + schemaName;
        	}
        }
        setTitle(title);
        if(editorDatabaseDescriptor != null) {
        	addEditorTab(editorDatabaseDescriptor);
        }
        if(isShowingLoggingTab) {
            addLoggerTab();
        }
        getContentPane().add(mainTabbedPane, BorderLayout.CENTER);
        setLocationByPlatform(true);
        setSize(800, 600);
        addNotify();
        if(editorDatabaseDescriptor != null) {
        	getFocusedEditorPane().adjustDefaultFocus();
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    performCleanup();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void performCleanup() {
        if(sqlLoggerPane != null) {
            sqlLoggerPane.setLogging(false);
        }
        if(editorTabbedPane != null) {
            for(int i=editorTabbedPane.getTabCount()-2; i>=0; i--) {
                ((EditorPane)editorTabbedPane.getComponentAt(i)).closeConnection();
            }
        }
    }

    private LoggerPane sqlLoggerPane;

	private void addLoggerTab() {
		sqlLoggerPane = new LoggerPane();
		mainTabbedPane.addTab("Logger", sqlLoggerPane);
	}

	private void addEditorTab(final DatabaseDescriptor databaseDescriptor) {
		JPanel editorsPane = new JPanel(new BorderLayout());
        final String[] tableNames = getTableNames(databaseDescriptor);
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
        editorTabbedPane = new JTabbedPane();
        editorTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        editorTabbedPane.addTab("New...", new JPanel());
        editorTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(!isAdjusting && editorTabbedPane.getSelectedIndex() == editorTabbedPane.getTabCount() - 1) {
                    addSQLEditorPane(databaseDescriptor);
                }
            }
        });
        JPanel tableNamePane = new JPanel(new BorderLayout());
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
        tableNamePane.add(tableNameFilterTextField, BorderLayout.NORTH);
        tableNamePane.add(new JScrollPane(tableNamesJList), BorderLayout.CENTER);
        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, tableNamePane, editorTabbedPane);
        addSQLEditorPane(databaseDescriptor);
        editorsPane.add(horizontalSplitPane, BorderLayout.CENTER);
        mainTabbedPane.addTab("Editor", editorsPane);
	}

    public static void openConsole(DatabaseDescriptor databaseDescriptor, boolean isLoggingActive) {
    	Console sqlConsoleFrame = new Console(databaseDescriptor, true);
    	sqlConsoleFrame.setLoggingActive(isLoggingActive);
    	sqlConsoleFrame.setVisible(true);
    }

    public void setLoggingActive(boolean isLoggingActive) {
        if(sqlLoggerPane != null) {
            sqlLoggerPane.setLogging(isLoggingActive);
            // Logger is the last tab.
            mainTabbedPane.setSelectedIndex(mainTabbedPane.getTabCount() - 1);
        }
    }

    private boolean isAdjusting;
    private int contextCount = 1;

    private void addSQLEditorPane(DatabaseDescriptor databaseDescriptor) {
        isAdjusting = true;
        int index = editorTabbedPane.getTabCount() - 1;
        final EditorPane sqlEditorPane = new EditorPane(databaseDescriptor);
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
                            ((EditorPane)editorTabbedPane.getComponentAt(i)).closeConnection();
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

    private EditorPane getFocusedEditorPane() {
        return (EditorPane)editorTabbedPane.getSelectedComponent();
    }

    private static String[] getTableNames(DatabaseDescriptor databaseDescriptor) {
    	List<Table<?>> tableList = databaseDescriptor.getSchema().getTables();
        List<String> tableNameList = new ArrayList<String>();
        for(Table<? extends Record> table: tableList) {
        	String tableName = table.getName();
        	tableNameList.add(tableName);
        }
        Collections.sort(tableNameList, String.CASE_INSENSITIVE_ORDER);
        return tableNameList.toArray(new String[0]);
    }

    public static void main(String[] args) {
    	if(args.length < 2) {
    	    System.out.println("Please specify IP and port of a running RemoteDebuggerServer");
    		System.out.println("Usage: Console <ip> <port>");
    		return;
    	}
    	try {
    		new RemoteDebuggerClient(args[0], Integer.parseInt(args[1]));
    	} catch(Exception e) {
    		e.printStackTrace();
    		return;
    	}
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
            public void run() {
				Console sqlConsoleFrame = new Console(null, true);
				sqlConsoleFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				sqlConsoleFrame.setVisible(true);
			}
		});
	}

}
