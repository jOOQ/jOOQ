package org.jooq.debug.console.misc;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
public class InvisibleSplitPane extends JSplitPane {

    public InvisibleSplitPane() {
       super();
       initialize();
    }

    public InvisibleSplitPane(int newOrientation) {
        super(newOrientation);
        initialize();
    }

    public InvisibleSplitPane(int newOrientation, boolean newContinuousLayout) {
        super(newOrientation, newContinuousLayout);
        initialize();
    }

    public InvisibleSplitPane(int newOrientation, boolean newContinuousLayout, Component newLeftComponent, Component newRightComponent) {
        super(newOrientation, newContinuousLayout, newLeftComponent, newRightComponent);
        initialize();
    }

    public InvisibleSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent) {
        super(newOrientation, newLeftComponent, newRightComponent);
        initialize();
    }

    private void initialize() {
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    @Override
    public void updateUI() {
        setUI(new SplitPaneWithInvisibleDividerUI());
        revalidate();
    }

    private class SplitPaneWithInvisibleDividerUI extends BasicSplitPaneUI {
        @Override
        public BasicSplitPaneDivider createDefaultDivider() {
            return new InvisibleDivider(this);
        }
    }

    private class InvisibleDivider extends BasicSplitPaneDivider {
        public InvisibleDivider( BasicSplitPaneUI ui ) {
            super(ui);
            super.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            setOpaque(false);
        }
        @Override
        public void setBorder(Border border) {
            // ignore
            super.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }
    }

}
