package view;

import lombok.Getter;
import tools.BSpline;

import javax.swing.*;
import java.awt.*;

public class DialogPanel extends JPanel {

    @Getter
    private final BSplinePanel bsplinePanel;
    @Getter
    private final SettingPanel settingPanel;


    public DialogPanel(BSplinePanel bSplinePanel, WireframePanel wireframePanel) {
        this.settingPanel = new SettingPanel(bSplinePanel, wireframePanel);
        this.bsplinePanel = bSplinePanel;

        bSplinePanel.subscribeListener(settingPanel);

        this.setPreferredSize(new Dimension(BSplinePanel.DEFAULT_WIDTH, BSplinePanel.DEFAULT_HEIGHT));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(bsplinePanel.getScrollPane());
        this.add(settingPanel);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
