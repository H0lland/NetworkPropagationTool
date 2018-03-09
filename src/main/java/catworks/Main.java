package catworks;

import org.ejml.simple.SimpleMatrix;

public class Main {

    /**
     * For now, this method only demos the SimpleMatrix class in action from EJML.
     * @param String[] args N/A
     */
    public static void main(String[] args) {
        // Declare data to fill matrices.
        double[][] data1 = {{.1, .2, .3}, {.4, .5, .6}, {.7, .8, .9}};
		double[][] data2 = {{.7, .8, .9}, {.4, .5, .6}, {.1, .2, .3}};

        // Declare matrices, perform some linear calculations, print result.
        SimpleMatrix H = new SimpleMatrix(data1);
		SimpleMatrix P = new SimpleMatrix(data2);
		SimpleMatrix S = H.mult(P).mult(H.transpose());
		System.out.println(S);

        // Test the ThreeTuple class.
        InterEdge edge = new InterEdge(1, 4, 6);
        System.out.println(edge);
    }

}
