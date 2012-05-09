package org.jooq.debug.console.misc;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

/**
 * @author Christopher Deckers
 */
public class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {

    private CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
    private JTree tree;

    public CheckBoxNodeEditor(JTree tree) {
        this.tree = tree;
    }

    @Override
    public Object getCellEditorValue() {
        JCheckBox checkbox = renderer.getCheckBoxRenderer();
        return new CheckBoxNode(null, checkbox.getText(), checkbox.isSelected());
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        boolean returnValue = false;
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
            if (path != null) {
                Object node = path.getLastPathComponent();
                returnValue = node instanceof CheckBoxNode;
            }
        }
        return returnValue;
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row) {
        Component editor = renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);
        // editor always selected / focused
        if (editor instanceof JCheckBox) {
            ((JCheckBox) editor).addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent itemEvent) {
                    if (stopCellEditing()) {
                        fireEditingStopped();
                    }
                }
            });
        }
        return editor;
    }
}