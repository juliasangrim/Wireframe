package view;

import exceptions.MatrixOpException;
import lombok.Getter;
import lombok.Setter;
import tools.BSpline;
import utils.Point;
import utils.Point3D;
import tools.Wireframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WireframePanel extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener {
    private final static int FULL_ANGLE = 360;
    private final static int DEFAULT_WIDTH = 640;
    private final static int DEFAULT_HEIGHT = 480;

    @Getter
    private int generatrixAmount = SettingPanel.MIN_GENERATRIX_AMOUNT;
    @Getter
    private int circleSegmentAmount = SettingPanel.MIN_CIRCLE_SEGMENTS_AMOUNT;
    private final Point3D camPoint = new Point3D(-10, 0, 0);
    @Getter
    private final Wireframe wireframe;
    @Setter
    private BSpline bSpline;


    private int lastX = 0;
    private int lastY = 0;

    public WireframePanel(BSpline bSpline) {
        this.wireframe = new Wireframe(DEFAULT_WIDTH, DEFAULT_HEIGHT, camPoint, bSpline.getBSplinePoints());
        this.bSpline = bSpline;

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    public void initRotate() {
        wireframe.initRotateMatrix();
        repaint();
    }

    public void setCircleSegmentAmount(int circleSegmentAmount) {
        this.circleSegmentAmount = circleSegmentAmount;
    }

    public void setGeneratrixAmount(int generatrixAmount) {
        this.generatrixAmount = generatrixAmount;
    }

    public void drawFigure(Graphics2D graphics2D) throws MatrixOpException {

        Point[] bSplinePoints = bSpline.getBSplinePoints();
        float stepAngle = (float) FULL_ANGLE / generatrixAmount;
        wireframe.getNewBox(bSplinePoints);
        wireframe.setTransformMatrixForBSpline();
        for (int i = 0; i < generatrixAmount; ++i) {
            Point oldPoint = null;
            var currAngle = stepAngle * i;

            for (Point bSplinePoint : bSplinePoints) {
                Point currPoint = wireframe.getWireframePoint(bSplinePoint, currAngle);
                if (oldPoint != null) {
                    graphics2D.drawLine(oldPoint.getX(), oldPoint.getY(), currPoint.getX(), currPoint.getY());
                }
                oldPoint = currPoint;
            }
        }

        float smallStepAngle = stepAngle / circleSegmentAmount;
        for (int i = 0; i < bSplinePoints.length; i += bSpline.getAmountSegments()) {
            Point oldPoint = null;

            for (int j = 0; j <= generatrixAmount * circleSegmentAmount; ++j) {
                var currAngle = smallStepAngle * j;
                Point currPoint = wireframe.getWireframePoint(bSplinePoints[i], currAngle);
                if (oldPoint != null) {
                    graphics2D.drawLine(oldPoint.getX(), oldPoint.getY(), currPoint.getX(), currPoint.getY());
                }

                oldPoint = currPoint;
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        wireframe.setViewWidth(getWidth());
        wireframe.setViewHeight(getHeight());
        try {
            drawFigure(g2d);
        } catch (MatrixOpException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        try {
            wireframe.setRotateMatrix((lastX - e.getX()) / 5f, (lastY - e.getY()) / 5f);
            lastX = e.getX();
            lastY = e.getY();
            repaint();
        } catch (MatrixOpException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (camPoint.getX() + e.getWheelRotation() * 10 <= 10) {
            camPoint.setX(camPoint.getX() + e.getWheelRotation() * 10);
        }
        System.out.println(camPoint.getX());
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
