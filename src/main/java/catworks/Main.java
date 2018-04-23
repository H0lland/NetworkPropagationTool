package catworks;

// Project import statements.
import catworks.networks.*;
import catworks.networks.metrics.*;
import catworks.phenomena.*;
import catworks.simulations.*;

// Additional import statement(s).
import java.io.IOException;

public class Main {

    /**
     * For now, this method only demos the SimpleMatrix class in action from EJML.
     * @param String[] args N/A
     */
    public static void main(String[] args) throws Exception, IOException {
        java.util.Scanner input = new java.util.Scanner(System.in);
        System.out.print("Enter simulation number to run (1-18), real-world (-1): ");
        int n = input.nextInt();
        input.close();
        new SimulationRunner(n);

        // IDN idn = new IDN(new ERNetwork(300, 0.02), new ERNetwork(300, 0.02));
        // idn.randomInterEdges(10.0/600);
        // System.out.println(idn.numberOfInterEdges());
    }

    public static double averageDegree(Network network) {
        int[][] matrix = network.getIntArrayMatrix();
        double degree = 0;
        for (int i = 0; i < matrix.length; i++) {
            degree += degree(matrix, i);
        }
        return degree / matrix.length;
    }

    public static int degree(int[][] matrix, int i) {
        int degree = 0;
        for (int j = 0; j < matrix[i].length; j++) {
            degree += matrix[i][j];
        }
        return degree;
    }

}
