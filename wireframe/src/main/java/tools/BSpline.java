package tools;

import exceptions.MatrixOpException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.Matrix;
import utils.Point;

import java.util.ArrayList;

@NoArgsConstructor
public class BSpline {

    private static final int DEFAULT_STEP = 100;
    public static final int DEFAULT_SIZE_MATRIX = 4;
    public static final float[] bSplineMatrix = {
            -1f/6, 3f/6, -3f/6, 1f/6,
            3f/6, -6f/6, 3f/6, 0f,
            -3f/6, 0f, 3f/6, 0f,
            1f/6, 4f/6, 1f/6, 0f
    };

    @Getter @Setter
    private int anchorPointsAmount;
    @Getter @Setter
    private int amountSegments;

    @Getter
    private ArrayList<Point> anchorPoints = new ArrayList<>();

    @Getter
    private Point[] bSplinePoints;

    public BSpline(int anchorPointsAmount, int amountSegments) {
        this.anchorPointsAmount = anchorPointsAmount;
        this.amountSegments = amountSegments;
        initBSplineAnchorPoints();
        makeBSpline();
    }

    public void setParamBSpline(int anchorPointsAmount, int amountSegments, ArrayList<Point> anchorPoints) {
        this.anchorPointsAmount = anchorPointsAmount;
        this.amountSegments = amountSegments;
        this.anchorPoints = anchorPoints;
        makeBSpline();
    }

    public void copy(BSpline bSpline) {
        this.anchorPointsAmount = bSpline.anchorPointsAmount;
        this.amountSegments = bSpline.amountSegments;
        for (var point : bSpline.getAnchorPoints()) {
            var copyPoint = new Point();
            copyPoint.copy(point);
            this.anchorPoints.add(copyPoint);
        }
        makeBSpline();
    }

    public void addAnchorPoint() {
        anchorPoints.add(new Point(anchorPoints.get(anchorPoints.size() - 1).getX() + DEFAULT_STEP, 0));
        makeBSpline();
    }

    public void deleteAnchorPoint() {
        anchorPoints.remove(anchorPoints.size() - 1);
        makeBSpline();
    }

    private void initBSplineAnchorPoints() {
        int beginX = 0;
        int beginY = 50 ;
        for (int i = 0; i < anchorPointsAmount; ++i) {
            anchorPoints.add(new Point(beginX + i * DEFAULT_STEP , beginY));
        }
    }

    public void makeBSpline() {
        float beginT = 0;
        Matrix tVector = new Matrix(new float[BSpline.DEFAULT_SIZE_MATRIX], BSpline.DEFAULT_SIZE_MATRIX, 1);
        Matrix bSplineMatrix = new Matrix(BSpline.bSplineMatrix, BSpline.DEFAULT_SIZE_MATRIX, BSpline.DEFAULT_SIZE_MATRIX);
        bSplinePoints = new Point[amountSegments * (anchorPointsAmount - 3) + 1];
        System.out.println(amountSegments + " " + anchorPointsAmount);
        float step = (float) 1 / amountSegments;
        for (int i = 1; i < anchorPointsAmount - 2; ++i) {
            float[] gArrayX = {
                    anchorPoints.get(i - 1).getX(),
                    anchorPoints.get(i).getX(),
                    anchorPoints.get(i + 1).getX(),
                    anchorPoints.get(i + 2).getX()
            };
            float[] gArrayY = {
                    anchorPoints.get(i - 1).getY(),
                    anchorPoints.get(i).getY(),
                    anchorPoints.get(i + 1).getY(),
                    anchorPoints.get(i + 2).getY()
            };
            Matrix gVectorX = new Matrix(gArrayX, 1, BSpline.DEFAULT_SIZE_MATRIX);
            Matrix gVectorY = new Matrix(gArrayY,1, BSpline.DEFAULT_SIZE_MATRIX);
            for (int j = 0; j <= amountSegments; ++j) {
                tVector.fillTMatrix(beginT + j * step);
                try {
                    Point linePoint = new Point();
                    int x = (int)tVector.multiply(bSplineMatrix).multiply(gVectorX).getData()[0];
                    int y = (int)tVector.multiply(bSplineMatrix).multiply(gVectorY).getData()[0];
                    linePoint.setXY(x, y);
                    bSplinePoints[(i - 1) * amountSegments + j] = linePoint;
                } catch (MatrixOpException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
