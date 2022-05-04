package tools;

import exceptions.MatrixOpException;
import lombok.Setter;
import utils.Box;
import utils.Matrix;
import utils.Point;
import utils.Point3D;

import static utils.Matrix.DEFAULT_SIZE_FOR_3D;

public class Wireframe {

    private final Point3D pView;
    private Box box;

    @Setter
    private Point3D pCam;
    @Setter
    private int viewHeight;
    @Setter
    private int viewWidth;

    private Matrix rotateMatrix;
    private Matrix perspectiveMatrix;
    private Matrix scaleAndShiftMatrix;


    public Wireframe(int viewWidth, int viewHeight, Point3D camPoint, Point[] bSplinePoints) {
        this.pCam = camPoint;
        this.pView = new Point3D(10, 0, 0);
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        initRotateMatrix();
    }

    public void getNewBox(Point[] bSplinePoints) {
        this.box = new Box(bSplinePoints);
    }

    public void initRotateMatrix() {
        this.rotateMatrix = new Matrix(new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        }, DEFAULT_SIZE_FOR_3D, DEFAULT_SIZE_FOR_3D);
    }

    private Matrix getMatrix2DTo3D(float angle) {
        return new Matrix(new float[]{
                0, (float) Math.cos(Math.toRadians(angle)), 0, 0,
                0, (float) Math.sin(Math.toRadians(angle)), 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 1}, DEFAULT_SIZE_FOR_3D, DEFAULT_SIZE_FOR_3D
        );
    }

    private Matrix getTranslateMatrix(float shiftX, float shiftY, float shiftZ) {
        return new Matrix(new float[]{
                1, 0, 0, shiftX,
                0, 1, 0, shiftY,
                0, 0, 1, shiftZ,
                0, 0, 0, 1
        }, DEFAULT_SIZE_FOR_3D, DEFAULT_SIZE_FOR_3D);


    }

    private Matrix getScaleMatrix(float scaleX, float scaleY, float scaleZ) {
        return new Matrix(new float[]{
                scaleX, 0, 0, 0,
                0, scaleY, 0, 0,
                0, 0, scaleZ, 0,
                0, 0, 0, 1
        }, DEFAULT_SIZE_FOR_3D, DEFAULT_SIZE_FOR_3D);

    }

    private Matrix getNormalizeMatrix(float x) {
        return new Matrix(new float[]{
                1f / x, 0, 0, 0,
                0, 1f / x, 0, 0,
                0, 0, 1f / x, 0,
                0, 0, 0, 1f / x
        }, DEFAULT_SIZE_FOR_3D, DEFAULT_SIZE_FOR_3D);

    }

    private Matrix getPerspectiveMatrix() {
        var xFront = pView.getX() - pCam.getX();
        var xBack = pView.getX() + 2 * box.getWidth();
        return new Matrix(new float[]{
                (xBack + xFront) / (xBack - xFront), 0, 0, (2f * xBack * xFront) / (xFront - xBack),
                0, 2f * xFront / viewWidth, 0, 0,
                0, 0, 2f * xFront / viewHeight, 0,
                1, 0, 0, 0
        }, DEFAULT_SIZE_FOR_3D, DEFAULT_SIZE_FOR_3D);
    }

    public void setRotateMatrix(float dx, float dy) throws MatrixOpException {
        Matrix rotateZ = new Matrix(new float[]{
                (float) Math.cos(Math.toRadians(dx)), (float) -Math.sin(Math.toRadians(dx)), 0, 0,
                (float) Math.sin(Math.toRadians(dx)), (float) Math.cos(Math.toRadians(dx)), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        }, DEFAULT_SIZE_FOR_3D, DEFAULT_SIZE_FOR_3D);

        Matrix rotateY = new Matrix(new float[]{
                (float) Math.cos(Math.toRadians(dy)), 0, (float) Math.sin(Math.toRadians(dy)), 0,
                0, 1, 0, 0,
                (float) -Math.sin(Math.toRadians(dy)), 0, (float) Math.cos(Math.toRadians(dy)), 0,
                0, 0, 0, 1
        }, DEFAULT_SIZE_FOR_3D, DEFAULT_SIZE_FOR_3D);

        rotateMatrix = rotateY.multiply(rotateZ).multiply(rotateMatrix);
    }

    public void setTransformMatrixForBSpline() throws MatrixOpException {
        Matrix centralFigureMatrix = getTranslateMatrix(0, 0, -box.getCenter().getX());
        perspectiveMatrix = getPerspectiveMatrix().multiply(getTranslateMatrix(-pCam.getX(), 0, 0))
                .multiply(getTranslateMatrix(pView.getX() + box.getWidth(), 0, 0))
                .multiply(rotateMatrix)
                .multiply(centralFigureMatrix);
        scaleAndShiftMatrix = getTranslateMatrix(0, viewWidth / 2f, viewHeight / 2f).
                multiply(getScaleMatrix(1, viewWidth / 2f, -viewHeight / 2f));
    }


    public Point getWireframePoint(Point bSplinePoint, float angle) throws MatrixOpException {
        Matrix currVector = new Matrix(new float[]{
                bSplinePoint.getX(), bSplinePoint.getY(), 1, 1
        }, 1, DEFAULT_SIZE_FOR_3D);


        var intermediateRes = perspectiveMatrix.multiply(getMatrix2DTo3D(angle)).multiply(currVector);
        var normalizeScaledMatrix = scaleAndShiftMatrix.multiply(getNormalizeMatrix(intermediateRes.getData()[3]));
        var res2D = normalizeScaledMatrix.multiply(intermediateRes);

        return new Point((int) res2D.getData()[1], (int) res2D.getData()[2]);
    }
}
