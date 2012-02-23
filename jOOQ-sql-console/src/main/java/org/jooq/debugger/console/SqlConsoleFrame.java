package org.jooq.debugger.console;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
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
import org.jooq.debugger.console.remote.SqlRemoteQueryDebuggerClient;

/**
 * @author Christopher Deckers
 */
public class SqlConsoleFrame extends JFrame {

    private JTabbedPane mainTabbedPane;
    private JTabbedPane editorTabbedPane;

    public SqlConsoleFrame(final DatabaseDescriptor editorDatabaseDescriptor, boolean isShowingLoggingTab) {
    	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainTabbedPane = new JTabbedPane();
        String title = "SQL Console";
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
                if(sqlLoggerPane != null) {
                    sqlLoggerPane.setLogging(false);
                }
                if(editorTabbedPane != null) {
                	for(int i=editorTabbedPane.getTabCount()-2; i>=0; i--) {
                		((SqlEditorPane)editorTabbedPane.getComponentAt(i)).closeConnection();
                	}
                }
            }
        });
    }
    
    private SqlLoggerPane sqlLoggerPane;

	private void addLoggerTab() {
		sqlLoggerPane = new SqlLoggerPane();
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
    	SqlConsoleFrame sqlConsoleFrame = new SqlConsoleFrame(databaseDescriptor, true);
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
        final SqlEditorPane sqlEditorPane = new SqlEditorPane(databaseDescriptor);
        editorTabbedPane.insertTab("Context " + contextCount++, null, sqlEditorPane, null, index);
        editorTabbedPane.setSelectedIndex(index);
        isAdjusting = false;
        sqlEditorPane.adjustDefaultFocus();
    }

    private SqlEditorPane getFocusedEditorPane() {
        return (SqlEditorPane)editorTabbedPane.getSelectedComponent();
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
    		System.out.println("Usage: <prog> <ip> <port>");
    		return;
    	}
    	try {
    		new SqlRemoteQueryDebuggerClient(args[0], Integer.parseInt(args[1]));
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
			public void run() {
				SqlConsoleFrame sqlConsoleFrame = new SqlConsoleFrame(null, true);
				sqlConsoleFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				sqlConsoleFrame.setVisible(true);
			}
		});
	}
    
}
