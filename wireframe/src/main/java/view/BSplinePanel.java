package view;

import lombok.Getter;
import lombok.Setter;
import tools.BSpline;
import utils.BSplinePanelStatus;
import utils.Point;
import tools.PointListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class BSplinePanel extends JPanel implements MouseMotionListener, MouseListener {
    public final static int DEFAULT_WIDTH = 1368;
    public final static int DEFAULT_HEIGHT = 800;

    private final static int DEFAULT_STEP_PIXELS = 100;
    private final static int DEFAULT_RADIUS = 10;

    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;

    @Getter
    private final JScrollPane scrollPane;

    private int centerX = width / 2;
    private int centerY = height / 2;

    private int amountAnchorPoints = SettingPanel.MIN_ANCHOR_POINTS_AMOUNT;
    private int amountSegments = SettingPanel.MIN_SEGMENTS_AMOUNT;
    private PointListener pointListener;


    @Getter @Setter
    private BSpline bSpline;
    private BSplinePanelStatus status = BSplinePanelStatus.NOTHING;
    @Getter @Setter
    private int indexActivePoint = 0;

    private int lastX = 0;
    private int lastY = 0;

    public BSplinePanel(JScrollPane scrollPane) {
        setSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        this.bSpline = new BSpline(amountAnchorPoints, amountSegments);
        this.scrollPane = scrollPane;
        this.scrollPane.setMaximumSize(new Dimension(width, height));
        this.scrollPane.setViewportView(this);
        this.scrollPane.revalidate();
        this.setBackground(Color.BLACK);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void subscribeListener(PointListener listener) {
        pointListener = listener;
    }

    public void unsubscribeListener() {
        pointListener = null;
    }

    public Point localCoordToGlobal(Point localCoord) {
        return new Point(centerX + localCoord.getX(), centerY - localCoord.getY());
    }

    public void setActivePointX(int x) {
        var activePoint = bSpline.getAnchorPoints().get(indexActivePoint);
        activePoint.setX(x);
        repaint();
    }

    public void setActivePointY(int y) {
        var activePoint = bSpline.getAnchorPoints().get(indexActivePoint);
        activePoint.setY(y);
        repaint();
    }

    public void setAmountAnchorPoints(int amountAnchorPoints) {
        var oldValue = bSpline.getAnchorPointsAmount();
        this.amountAnchorPoints = amountAnchorPoints;
        bSpline.setAnchorPointsAmount(amountAnchorPoints);

        if (oldValue > amountAnchorPoints) {
            bSpline.deleteAnchorPoint();
        } else {
            if (oldValue < amountAnchorPoints) {
                bSpline.addAnchorPoint();
            }
        }
        repaint();
    }

    public void setAmountSegments(int amountSegments) {
        this.amountSegments = amountSegments;
        bSpline.setAmountSegments(amountSegments);
        bSpline.makeBSpline();
        repaint();
    }

    private void drawBSplineAnchorPoints(Graphics2D graphics) {
        graphics.setColor(Color.RED);
        Point oldPoint = null;
        var bSplinePoints = bSpline.getAnchorPoints();
        for (int i = 0; i < bSplinePoints.size(); ++i) {
            var point = bSplinePoints.get(i);
            if (i != indexActivePoint) {
                graphics.drawOval(localCoordToGlobal(point).getX() - DEFAULT_RADIUS, localCoordToGlobal(point).getY() - DEFAULT_RADIUS,
                        2 * DEFAULT_RADIUS, 2 * DEFAULT_RADIUS);
            } else {
                graphics.setColor(Color.GREEN);
                graphics.drawOval(localCoordToGlobal(point).getX() - DEFAULT_RADIUS, localCoordToGlobal(point).getY() - DEFAULT_RADIUS,
                        2 * DEFAULT_RADIUS, 2 * DEFAULT_RADIUS);
                graphics.setColor(Color.RED);
            }
            if (oldPoint != null) {
                graphics.drawLine(localCoordToGlobal(oldPoint).getX(), localCoordToGlobal(oldPoint).getY(),
                        localCoordToGlobal(point).getX(), localCoordToGlobal(point).getY());
            }
            oldPoint = point;
        }
    }

    private void drawBSpline(Graphics2D graphics) {
        graphics.setColor(Color.BLUE);
        var bSplinePoints = bSpline.getBSplinePoints();
        for (int i = 1; i < bSplinePoints.length; ++i) {
            graphics.drawLine(localCoordToGlobal(
                            bSplinePoints[i - 1]).getX(), localCoordToGlobal(bSplinePoints[i - 1]).getY(),
                    localCoordToGlobal(bSplinePoints[i]).getX(), localCoordToGlobal(bSplinePoints[i]).getY()
            );
        }
    }

    private void drawCoordinateField(Graphics2D graphics) {

        graphics.setColor(Color.WHITE);
        graphics.drawLine(0, centerY, width - 1, centerY);
        graphics.drawLine(centerX, 0, centerX, height);
        graphics.setColor(Color.GRAY);

        var amountLine = (height - centerY) / DEFAULT_STEP_PIXELS;
        for (int i = 1; i <= amountLine; ++i) {
            graphics.drawLine(0, centerY + DEFAULT_STEP_PIXELS * i, width - 1, centerY + DEFAULT_STEP_PIXELS * i);
            graphics.drawString(Integer.toString(-DEFAULT_STEP_PIXELS * i), centerX + 10 , centerY + DEFAULT_STEP_PIXELS * i - 10);
        }
        amountLine = centerY;
        for (int i = 1; i <= amountLine; ++i) {
            graphics.drawLine(0, centerY - DEFAULT_STEP_PIXELS * i, width - 1, centerY - DEFAULT_STEP_PIXELS * i);
            graphics.drawString(Integer.toString(DEFAULT_STEP_PIXELS * i), centerX + 10 , centerY - DEFAULT_STEP_PIXELS * i - 10);
        }

        amountLine = (width - centerX) / DEFAULT_STEP_PIXELS;
        for (int i = 1; i <= amountLine; ++i) {
            graphics.drawLine(centerX + DEFAULT_STEP_PIXELS * i,0, centerX + DEFAULT_STEP_PIXELS * i, height - 1);
            graphics.drawString(Integer.toString(DEFAULT_STEP_PIXELS * i),
                    centerX + DEFAULT_STEP_PIXELS * i - 10 * Integer.toString(DEFAULT_STEP_PIXELS * i).length(), centerY + 20);
        }

        amountLine = centerX / DEFAULT_STEP_PIXELS;
        for (int i = 1; i <= amountLine; ++i) {
            graphics.drawLine(centerX -DEFAULT_STEP_PIXELS * i,0, centerX - DEFAULT_STEP_PIXELS * i, height - 1);
            graphics.drawString(Integer.toString(-DEFAULT_STEP_PIXELS * i),
                    centerX - DEFAULT_STEP_PIXELS * i - 10 * Integer.toString(DEFAULT_STEP_PIXELS * i).length(), centerY + 20);
        }

    }

    private void changePanelStatus(int pressedX, int pressedY) {
        var bSplinePoints = bSpline.getAnchorPoints();
        if (status != BSplinePanelStatus.POINT_MOVED) {
            for (int i = 0; i < bSplinePoints.size(); ++i) {

                int x = pressedX - localCoordToGlobal(bSplinePoints.get(i)).getX();
                int y = pressedY - localCoordToGlobal(bSplinePoints.get(i)).getY();

                if (x * x + y * y <= DEFAULT_RADIUS * DEFAULT_RADIUS) {
                    status = BSplinePanelStatus.POINT_MOVED;
                    indexActivePoint = i;
                    break;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        changePanelStatus(e.getX(), e.getY());
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        status = BSplinePanelStatus.NOTHING;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (status == BSplinePanelStatus.POINT_MOVED) {
            bSpline.getAnchorPoints().get(indexActivePoint).setXY(e.getX() - centerX, centerY - e.getY());
            pointListener.changePoint(new Point(e.getX() - centerX, centerY - e.getY()));
            bSpline.makeBSpline();
        } else {
            int dx = lastX - e.getX();
            int dy = lastY - e.getY();
            Dimension panelSize = new Dimension(getWidth(), getHeight());
            java.awt.Point viewPos = scrollPane.getViewport().getViewPosition();
            int maxX = viewPos.x + scrollPane.getViewport().getWidth();
            int maxY = viewPos.y + scrollPane.getViewport().getHeight();

            if (dx < 0 && viewPos.x == 0) {
                width -= dx;
                centerX -= dx;
                lastX -= dx;
            } else if (dx >= 0 && maxX == panelSize.width) {
                width += dx;
            }

            if (dy < 0 && viewPos.y == 0) {
                height -= dy;
                centerY -= dy;
                lastY -= dy;
            } else if (dy >= 0 && maxY == panelSize.height) {
                height += dy;
            }
            scrollPane.getHorizontalScrollBar().setValue(viewPos.x + dx);
            scrollPane.getVerticalScrollBar().setValue(viewPos.y + dy);
        }
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        setSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        drawCoordinateField(g2d);
        drawBSplineAnchorPoints(g2d);
        drawBSpline(g2d);
        scrollPane.revalidate();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
