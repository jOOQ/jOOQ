package org.jooq.debug.console.misc;

import java.awt.Component;
import java.awt.Rectangle;
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
@SuppressWarnings({"serial", "hiding"})
public class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {

    private CheckBoxNodeRenderer renderer;
    private JTree tree;

    public CheckBoxNodeEditor(JTree tree) {
        renderer = new CheckBoxNodeRenderer(tree);
        this.tree = tree;
    }

    @Override
    public Object getCellEditorValue() {
        JCheckBox checkbox = renderer.getCheckBoxRenderer();
        return new CheckBoxNode(null, checkbox.getText(), checkbox.isSelected());
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
            if (path != null) {
                Object node = path.getLastPathComponent();
                if(node instanceof CheckBoxNode) {
                    int row = tree.getRowForPath(path);
                    Component editor = renderer.getTreeCellRendererComponent(tree, node, true, false, true, row, true);
                    Rectangle rowBounds = tree.getRowBounds(row);
                    int x = mouseEvent.getX() - rowBounds.x;
                    int y = mouseEvent.getY() - rowBounds.y;
                    // editor always selected / focused
                    if (editor instanceof CheckBoxNodeRenderer.CheckBoxEditor) {
                        editor.setSize(rowBounds.getSize());
                        editor.validate();
                        editor.doLayout();
                        return ((CheckBoxNodeRenderer.CheckBoxEditor)editor).findComponentAt(x, y) instanceof JCheckBox;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row) {
        Component editor = renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);
        // editor always selected / focused
        if (editor instanceof CheckBoxNodeRenderer.CheckBoxEditor) {
            final JCheckBox checkBox = ((CheckBoxNodeRenderer.CheckBoxEditor)editor).getCheckBox();
            editor = new CheckBoxNodeRenderer.CheckBoxEditor(checkBox, renderer.getNonCheckBoxRenderer().getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true));
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent itemEvent) {
                    if (stopCellEditing()) {
                        checkBox.removeItemListener(this);
                        fireEditingStopped();
                    }
                }
            });
        }
        return editor;
    }
}