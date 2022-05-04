package utils;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Point {

    @Getter
    private int x;
    @Getter
    private int y;


    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void copy(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}
