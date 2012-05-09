package org.jooq.debug.console.misc;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 * @author Christopher Deckers
 */
public class CheckBoxNode extends DefaultMutableTreeNode {

    private String text;
    private boolean selected;

    public CheckBoxNode(Object o, String text, boolean selected) {
        super(o);
        this.text = text;
        this.selected = selected;
    }

    public String getText() {
        return text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean newValue) {
        selected = newValue;
    }

}
