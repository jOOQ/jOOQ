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

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import org.jooq.debug.console.misc.Utils;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.Token;

/**
 * @author Christopher Deckers
 */
public class SqlTextArea extends RSyntaxTextArea {

    public SqlTextArea() {
        setTabSize(2);
        setTabsEmulated(true);
        setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
        setMarkOccurrences(true);
        setAnimateBracketMatching(false);
        setAutoIndentEnabled(true);
        setCurrentLineHighlightColor(new Color(232, 242, 254));
        setMarkOccurrencesColor(new Color(220, 220, 220));
        setMatchedBracketBGColor(null);
        setMatchedBracketBorderColor(new Color(192, 192, 192));
        getActionMap().put("copy", new RSyntaxTextAreaEditorKit.CopyAsRtfAction());
        Font editorFont = getFont().deriveFont(UIManager.getFont("TextField.font").getSize2D());
        SyntaxScheme syntaxScheme = getSyntaxScheme();
        syntaxScheme.setStyle(Token.SEPARATOR, new Style(new Color(200, 0, 0), null));
        syntaxScheme.setStyle(Token.RESERVED_WORD, new Style(Color.BLUE, null, editorFont));
        setFont(new Font("Monospaced", editorFont.getStyle(), editorFont.getSize()));
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean isControlDown = e.isControlDown();
                boolean isShiftDown = e.isShiftDown();
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_P:
                        if(isControlDown && isShiftDown) {
                            int position = RSyntaxUtilities.getMatchingBracketPosition(SqlTextArea.this);
                            if(position >= 0) {
                                setCaretPosition(position + 1);
                            }
                        }
                        break;
                    case KeyEvent.VK_F:
                        if(isControlDown && isShiftDown) {
                            formatSelection();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public JPopupMenu getPopupMenu() {
        boolean isEditable = isEditable();
        JPopupMenu popupMenu = new JPopupMenu();
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        JMenuItem copyClipboardMenuItem = new JMenuItem("Copy");
        copyClipboardMenuItem.setEnabled(getSelectionStart() < getSelectionEnd());
        copyClipboardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyAsRtf();
            }
        });
        popupMenu.add(copyClipboardMenuItem);
        JMenuItem pasteClipboardMenuItem = new JMenuItem("Paste");
        pasteClipboardMenuItem.setEnabled(false);
        if(isEditable && clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            try {
                final String data = (String)clipboard.getData(DataFlavor.stringFlavor);
                if(data != null && data.length() > 0) {
                    pasteClipboardMenuItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            replaceSelection(data);
                        }
                    });
                    pasteClipboardMenuItem.setEnabled(true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        popupMenu.add(pasteClipboardMenuItem);
        popupMenu.addSeparator();
        JMenuItem formatMenuItem = new JMenuItem("Format");
        formatMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
        formatMenuItem.setEnabled(isEditable && getSelectionStart() < getSelectionEnd());
        formatMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formatSelection();
            }
        });
        popupMenu.add(formatMenuItem);
        if(popupMenu.getComponentCount() > 0) {
            return popupMenu;
        }
        return null;
    }

    private void formatSelection() {
        String text = getSelectedText();
        String newText = Utils.getFormattedSql(text);
        if(!Utils.equals(text, newText)) {
            replaceSelection(newText);
        }
    }

}
