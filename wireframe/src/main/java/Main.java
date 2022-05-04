import exceptions.MatrixOpException;
import utils.Matrix;
import view.WindowController;

public class Main {


    public static void main(String[] args) {
        WindowController mainFrame = new WindowController();



        Matrix array1 = new Matrix(new float[]{3, 3, 2, 2, 1, 1}, 2, 3);
        Matrix array2 = new Matrix(new float[]{3, 3, 2, 2, 1, 1, 0, 0}, 4, 2);

        try {

            System.out.println( array1.multiply(array2).getData()[0]);
        } catch (MatrixOpException e) {
            e.printStackTrace();
        }

        mainFrame.pack();
        mainFrame.setVisible(true);
    }


}
