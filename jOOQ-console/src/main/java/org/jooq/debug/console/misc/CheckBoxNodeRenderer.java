package org.jooq.debug.console.misc;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class CheckBoxNodeRenderer implements TreeCellRenderer {

    private JCheckBox checkBoxRenderer;

    private TreeCellRenderer nonCheckBoxRenderer;

    Color selectionBorderColor, selectionForeground, selectionBackground, textForeground, textBackground;

    protected JCheckBox getCheckBoxRenderer() {
        return checkBoxRenderer;
    }

    public TreeCellRenderer getNonCheckBoxRenderer() {
        return nonCheckBoxRenderer;
    }

    public CheckBoxNodeRenderer(JTree tree) {
        nonCheckBoxRenderer = tree.getCellRenderer();
        checkBoxRenderer = new JCheckBox();
        checkBoxRenderer.setMargin(new Insets(0, 0, 0, 0));
        Font fontValue = UIManager.getFont("Tree.font");
        if (fontValue != null) {
            checkBoxRenderer.setFont(fontValue);
        }
        Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
        checkBoxRenderer.setFocusPainted((booleanValue != null) && (booleanValue.booleanValue()));
        selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
        selectionForeground = UIManager.getColor("Tree.selectionForeground");
        selectionBackground = UIManager.getColor("Tree.selectionBackground");
        textForeground = UIManager.getColor("Tree.textForeground");
        textBackground = UIManager.getColor("Tree.textBackground");
    }

    public static class CheckBoxEditor extends JPanel {
        private JCheckBox checkBoxRenderer;
        public CheckBoxEditor(JCheckBox checkBoxRenderer, Component c) {
            super(new FlowLayout(FlowLayout.LEFT, 0, 0));
            setOpaque(false);
            this.checkBoxRenderer = checkBoxRenderer;
            add(checkBoxRenderer);
            add(c);
        }
        public JCheckBox getCheckBox() {
            return checkBoxRenderer;
        }
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//        Object value = // stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, false);
        Component c = nonCheckBoxRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (value instanceof CheckBoxNode) {
//            checkBoxRenderer.setText(stringValue);
            checkBoxRenderer.setSelected(false);
            checkBoxRenderer.setEnabled(tree.isEnabled());
            checkBoxRenderer.setOpaque(false);
//            if (selected) {
//                checkBoxRenderer.setForeground(selectionForeground);
//                checkBoxRenderer.setBackground(selectionBackground);
//            } else {
//                checkBoxRenderer.setForeground(textForeground);
//                checkBoxRenderer.setBackground(textBackground);
//            }
            CheckBoxNode node = (CheckBoxNode) value;
//            checkBoxRenderer.setText(node.getText());
            checkBoxRenderer.setSelected(node.isSelected());
            c = new CheckBoxEditor(checkBoxRenderer, c);
        }
        return c;
    }

}