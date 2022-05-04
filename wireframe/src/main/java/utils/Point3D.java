package utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
public class Point3D {


    @Getter
    @Setter
    private float x;
    @Getter
    @Setter
    private float y;
    @Getter
    @Setter
    private float z;


    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


}
