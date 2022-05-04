package utils;

import exceptions.MatrixOpException;
import lombok.Getter;

public class Matrix {

    public final static int DEFAULT_SIZE_FOR_3D = 4;

    @Getter
    private final float[] data;
    private final int width;
    private final int height;

    public Matrix(float[] array, int width, int height) {
        this.data = array;
        this.height = height;
        this.width = width;
    }

    public Matrix multiply(Matrix secondMatrix) throws MatrixOpException {
        Matrix result;
        if (this.width == secondMatrix.height) {
            result = new Matrix(new float[this.height * secondMatrix.width], secondMatrix.width, this.height);
            for (int y = 0; y < this.height; ++y) {
                for (int x = 0; x < secondMatrix.width; ++x) {
                    result.data[y * secondMatrix.width + x] = 0;
                    for (int k = 0; k < this.width; ++k) {
                        result.data[y * secondMatrix.width + x] += this.data[y * width + k] * secondMatrix.data[k * secondMatrix.width + x];
                    }

                }
            }
        } else throw new MatrixOpException("Size of matrix is not correct for multiply.");

        return result;
    }

    public void fillTMatrix(float t) {
        for (int i = 0; i < width; ++i) {
            data[i] = (float) Math.pow(t, width - i - 1);
        }
    }

}
