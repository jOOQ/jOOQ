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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jooq.debug.TextMatcher;
import org.jooq.debug.TextMatcher.TextMatchingType;


/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class TextMatcherPane extends JPanel {

    private JComboBox matcherTypeComboBox;
    private JTextField textField;
    private JCheckBox caseSensitiveCheckBox;

    public TextMatcherPane(TextMatcher textMatcher) {
        super(new GridBagLayout());
        setOpaque(false);
        matcherTypeComboBox = new JComboBox(TextMatchingType.values());
        add(matcherTypeComboBox, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        textField = new JTextField(14);
        add(textField, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
        caseSensitiveCheckBox = new JCheckBox("Case sensitive");
        caseSensitiveCheckBox.setOpaque(false);
        add(caseSensitiveCheckBox, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
        if(textMatcher != null) {
            matcherTypeComboBox.setSelectedItem(textMatcher.getType());
            textField.setText(textMatcher.getText());
            caseSensitiveCheckBox.setSelected(textMatcher.isCaseSensitive());
        }
    }

    public TextMatcher getTextMatcher() {
        return new TextMatcher((TextMatchingType)matcherTypeComboBox.getSelectedItem(), textField.getText(), caseSensitiveCheckBox.isSelected());
    }

    public void setLocked(boolean isLocked) {
        matcherTypeComboBox.setEnabled(!isLocked);
        textField.setEnabled(!isLocked);
        caseSensitiveCheckBox.setEnabled(!isLocked);
    }

}
