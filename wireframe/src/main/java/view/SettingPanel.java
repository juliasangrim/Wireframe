package view;

import lombok.Getter;
import lombok.Setter;
import tools.Command;
import utils.Point;
import tools.PointListener;

import javax.swing.*;
import java.awt.*;

public class SettingPanel extends JPanel implements PointListener {

    public final static int MIN_ANCHOR_POINTS_AMOUNT = 4;
    public final static int MAX_ANCHOR_POINTS_AMOUNT = 100;
    private final static int STEP_ANCHOR_POINTS_AMOUNT = 1;

    public final static int MIN_SEGMENTS_AMOUNT = 1;
    public final static int MAX_SEGMENTS_AMOUNT = 15;
    private final static int STEP_SEGMENTS_AMOUNT = 1;

    public final static int MIN_GENERATRIX_AMOUNT = 3;
    public final static int MAX_GENERATRIX_AMOUNT = 50;
    private final static int STEP_GENERATRIX_AMOUNT = 1;

    public final static int MIN_CIRCLE_SEGMENTS_AMOUNT = 1;
    public final static int MAX_CIRCLE_SEGMENTS_AMOUNT = 100;
    private final static int STEP_CIRCLE_SEGMENTS_AMOUNT = 1;

    private final static int STEP_COORD = 1;


    @Getter
    @Setter
    private int anchorPointsAmount = MIN_ANCHOR_POINTS_AMOUNT;
    @Getter
    @Setter
    private int segmentPointAmount = MIN_SEGMENTS_AMOUNT;
    @Getter
    @Setter
    private int genetrixAmount = MIN_GENERATRIX_AMOUNT;
    @Getter
    @Setter
    private int circleSegmentsAmount = MIN_CIRCLE_SEGMENTS_AMOUNT;



    public void setSpinnerValueK(int value) {
        spinnerK.setValue(value);
    }
    public void setSpinnerValueN(int value) {
        spinnerN.setValue(value);
    }
    public void setSpinnerValueM1(int value) {
        spinnerM1.setValue(value);
    }
    public void setSpinnerValueM2(int value) {
        spinnerM2.setValue(value);
    }

    private final JSpinner spinnerX;
    private final JSpinner spinnerY;
    private final JSpinner spinnerK;
    private final JSpinner spinnerN;
    private final JSpinner spinnerM1;
    private final JSpinner spinnerM2;

    private JSpinner makeSpinner(String text, Integer currentValue, Integer min, Integer max, Integer step, Command command) {
        JPanel subPanel = new JPanel();
        subPanel.add(new JLabel(text));
        SpinnerModel model = new SpinnerNumberModel(currentValue, min, max, step);
        JSpinner spinner = new JSpinner(model);
        spinner.addChangeListener(evt -> command.set((int) spinner.getValue()));
        spinner.setPreferredSize(new Dimension(70, 20));
        subPanel.add(spinner);
        add(subPanel);

        return spinner;

    }

    public SettingPanel(BSplinePanel bSplinePanel, WireframePanel wireframePanel) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        spinnerK = makeSpinner("<html><p><center><b>K</b></center></p>\n<small>(amount of anchor points)</small></html>",
                MIN_ANCHOR_POINTS_AMOUNT, MIN_ANCHOR_POINTS_AMOUNT, MAX_ANCHOR_POINTS_AMOUNT, STEP_ANCHOR_POINTS_AMOUNT,
                bSplinePanel::setAmountAnchorPoints);

        spinnerN = makeSpinner("<html><p><center><b>N</b></center></p>\n<small>(amount of segments)</small></html>)",
                MIN_SEGMENTS_AMOUNT, MIN_SEGMENTS_AMOUNT, MAX_SEGMENTS_AMOUNT, STEP_SEGMENTS_AMOUNT,
                bSplinePanel::setAmountSegments);

        spinnerX = makeSpinner("<html><p><center><b>X</b></center></p>\n<small>(x coordinate of anchor point)</small></html>",
                bSplinePanel.getBSpline().getAnchorPoints().get(bSplinePanel.getIndexActivePoint()).getX(), null, null, STEP_COORD,
                bSplinePanel::setActivePointX);

        spinnerY = makeSpinner("<html><p><center><b>Y</b></center></p>\n<small>(y coordinate of anchor point)</small></html>)",
                bSplinePanel.getBSpline().getAnchorPoints().get(bSplinePanel.getIndexActivePoint()).getY(), null, null, STEP_COORD,
                bSplinePanel::setActivePointY);

        spinnerM1 = makeSpinner("<html><p><center><b>M1</b></center></p>\n<small>(amount of generatrix)</small></html>)",
                MIN_GENERATRIX_AMOUNT, MIN_GENERATRIX_AMOUNT, MAX_GENERATRIX_AMOUNT, STEP_GENERATRIX_AMOUNT,
                wireframePanel::setGeneratrixAmount);

        spinnerM2 = makeSpinner("<html><p><center><b>M2</b></center></p>\n<small>(amount of circle segments)</small></html>)",
                MIN_CIRCLE_SEGMENTS_AMOUNT, MIN_CIRCLE_SEGMENTS_AMOUNT, MAX_CIRCLE_SEGMENTS_AMOUNT, STEP_CIRCLE_SEGMENTS_AMOUNT,
                wireframePanel::setCircleSegmentAmount);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
    }

    @Override
    public void changePoint(Point point) {
        spinnerX.setValue(point.getX());
        spinnerY.setValue(point.getY());
    }
}
