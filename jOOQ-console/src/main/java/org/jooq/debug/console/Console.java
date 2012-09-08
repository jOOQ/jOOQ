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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jooq.debug.Debugger;
import org.jooq.debug.DebuggerRegistry;
import org.jooq.debug.LocalDebugger;
import org.jooq.debug.console.misc.JSedRegExBuilder;
import org.jooq.debug.console.remote.ClientDebugger;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings({"serial", "hiding"})
public class Console extends JFrame {

    private Debugger debugger;
    private JTabbedPane mainTabbedPane;
    private EditorsPane editorsPane;


    public Console(DatabaseDescriptor editorDatabaseDescriptor, boolean isShowingLoggingTab, boolean isShowingDebugger) {
        debugger = new LocalDebugger(editorDatabaseDescriptor);
        // Local debugger registration is managed by the console since it hides the debugger.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                DebuggerRegistry.add(debugger);
            }
            @Override
            public void windowClosed(WindowEvent e) {
                DebuggerRegistry.remove(debugger);
            }
        });
        init(isShowingLoggingTab, isShowingDebugger);
    }

    public Console(final Debugger debugger, boolean isShowingLoggingTab, boolean isShowingDebugger) {
        this.debugger = debugger;
        // Local debugger registration is handled externally if needed (e.g.: remote client must not be registered).
    	init(isShowingLoggingTab, isShowingDebugger);
    }

    private void init(boolean isShowingLoggingTab, boolean isShowingDebugger) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    	JMenuBar menuBar = new JMenuBar();
    	JMenu fileMenu = new JMenu("File");
    	fileMenu.setMnemonic('F');
    	JMenuItem regExpEditorMenuItem = new JMenuItem("Reg. Exp. Editor");
    	regExpEditorMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(Console.this, "Reg. Exp Editor");
                JPanel contentPane = new JPanel(new BorderLayout());
                contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                contentPane.add(new JSedRegExBuilder("/foo/bar/gi", "axFooax\nbxfoobx"), BorderLayout.CENTER);
                dialog.add(contentPane, BorderLayout.CENTER);
                dialog.setSize(600, 400);
                dialog.setVisible(true);
                dialog.setLocationRelativeTo(Console.this);
            }
        });
    	fileMenu.add(regExpEditorMenuItem);
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
                JTabbedPane tabbedPane = new JTabbedPane();
                JPanel aboutPane = new JPanel(new GridBagLayout());
                aboutPane.setOpaque(false);
                aboutPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                aboutPane.add(new JLabel("jOOQ library: "), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                aboutPane.add(new JLabel("Lukas Eder"), new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                aboutPane.add(new JLabel("jOOQ Console: "), new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
                aboutPane.add(new JLabel("Christopher Deckers"), new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
                aboutPane.add(new JLabel("License: "), new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
                aboutPane.add(new JLabel("Apache License, Version 2.0"), new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
                aboutPane.add(new JLabel("Web site: "), new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
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
                aboutPane.add(siteLabel, new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
                tabbedPane.addTab("About", aboutPane);
                JPanel disclaimerPane = new JPanel(new BorderLayout());
                disclaimerPane.setBorder(BorderFactory.createEmptyBorder(2, 5, 5, 5));
                disclaimerPane.setOpaque(false);
                JTextArea textArea = new JTextArea(
                        "This software is provided by the copyright holders and contributors \"as is\"" +
                        " and any express or implied warranties, including, but not limited to, the" +
                        " implied warranties of merchantability and fitness for a particular purpose" +
                        " are disclaimed. In no event shall the copyright owner or contributors be" +
                        " liable for any direct, indirect, incidental, special, exemplary, or" +
                        " consequential damages (including, but not limited to, procurement of" +
                        " substitute goods or services; loss of use, data, or profits; or business" +
                        " interruption) however caused and on any theory of liability, whether in" +
                        " contract, strict liability, or tort (including negligence or otherwise)" +
                        " arising in any way out of the use of this software, even if advised of the" +
                        " possibility of such damage."
                );
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                disclaimerPane.add(new JScrollPane(textArea));
                tabbedPane.addTab("Disclaimer", disclaimerPane);
                contentPane.add(tabbedPane, BorderLayout.CENTER);
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
                aboutDialog.setSize(500, 400);
                aboutDialog.setLocationRelativeTo(Console.this);
                aboutDialog.setVisible(true);
            }
        });
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        mainTabbedPane = new JTabbedPane();
        String title = "jOOQ Console";
//        if(editorDatabaseDescriptor != null) {
//        	String schemaName = editorDatabaseDescriptor.getSchema().getName();
//        	if(schemaName != null && schemaName.length() != 0) {
//        		title += " - " + schemaName;
//        	}
//        }
        setTitle(title);
        if(debugger.isExecutionSupported()) {
        	addEditorTab();
        }
        if(isShowingLoggingTab) {
            addLoggerTab();
        }
        if(isShowingDebugger) {
            addDebuggerTab();
        }
        contentPane.add(mainTabbedPane, BorderLayout.CENTER);
        setLocationByPlatform(true);
        setSize(800, 600);
        addNotify();
        if(debugger.isExecutionSupported()) {
        	editorsPane.adjustDefaultFocus();
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
        setContentPane(contentPane);
    }

    private void performCleanup() {
        if(sqlLoggerPane != null) {
            sqlLoggerPane.setLogging(false);
        }
        if(editorsPane != null) {
            editorsPane.performCleanup();
        }
    }

    private DebuggerPane sqlDebuggerPane;

    private void addDebuggerTab() {
        sqlDebuggerPane = new DebuggerPane(debugger);
        mainTabbedPane.addTab("Debugger", sqlDebuggerPane);

    }

    private LoggerPane sqlLoggerPane;

	private void addLoggerTab() {
		sqlLoggerPane = new LoggerPane(debugger);
		mainTabbedPane.addTab("Logger", sqlLoggerPane);
	}

    public static void openConsole(DatabaseDescriptor databaseDescriptor, boolean isLoggingActive) {
        Console sqlConsoleFrame = new Console(databaseDescriptor, true, true);
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

    private void addEditorTab() {
        editorsPane = new EditorsPane(debugger, true);
        editorsPane.setBorder(BorderFactory.createEmptyBorder(2, 5, 5, 5));
        mainTabbedPane.addTab("Editor", editorsPane);
    }

    public static void main(String[] args) {
    	if(args.length < 2) {
    	    System.out.println("Please specify IP and port of a running RemoteDebuggerServer");
    		System.out.println("Usage: Console <ip> <port>");
    		return;
    	}
    	final Debugger debugger;
    	try {
    	    debugger = new ClientDebugger(args[0], Integer.parseInt(args[1]));
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
				Console sqlConsoleFrame = new Console(debugger, true, true);
				sqlConsoleFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				sqlConsoleFrame.setVisible(true);
			}
		});
	}

}
