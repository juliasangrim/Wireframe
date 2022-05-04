package utils;

import lombok.Getter;

public class Box {

    @Getter
    private final Point3D center;
    @Getter
    private final float width;
    @Getter
    private final float height;


    public Box(Point[] bSplinePoints) {
        float maxCoordY = bSplinePoints[0].getY();
        float maxCoordX = bSplinePoints[0].getX();
        float minCoordX = bSplinePoints[0].getX();
        for (Point bSplinePoint : bSplinePoints) {
            if (maxCoordY < bSplinePoint.getY()) {
                maxCoordY = bSplinePoint.getY();
            }
            if (maxCoordY < bSplinePoint.getX()) {
                maxCoordY = bSplinePoint.getX();
            }
            if (maxCoordX < bSplinePoint.getX()) {
                maxCoordX = bSplinePoint.getX();
            }
            if (minCoordX > bSplinePoint.getX()) {
                minCoordX = bSplinePoint.getX();
            }
        }
        this.height = maxCoordX - minCoordX;
        this.width = 2 * Math.abs(maxCoordY);
        this.center = new Point3D((maxCoordX + minCoordX) / 2f, 0, 0);
    }
}
