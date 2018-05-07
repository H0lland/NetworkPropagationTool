package catworks;

// Project import statements.
import catworks.networks.*;

// Additional import statement(s).
import java.io.IOException;

public class Main {

    /**
     * For now, this method only demos the SimpleMatrix class in action from EJML.
     * @param String[] args N/A
     */
    public static void main(String[] args) throws Exception, IOException {
        // Run the simulation threads.
        // (new Thread(new SimulationRunner(1))).start();
        // (new Thread(new SimulationRunner(2))).start();
        // (new Thread(new SimulationRunner(3))).start();
        // (new Thread(new SimulationRunner(4))).start();
        // (new Thread(new SimulationRunner(5))).start();
        // (new Thread(new SimulationRunner(6))).start();
        // (new Thread(new SimulationRunner(7))).start();
        // (new Thread(new SimulationRunner(8))).start();
        // (new Thread(new SimulationRunner(9))).start();
        (new Thread(new SimulationRunner(-1))).start(); // NOTE: This is the only disconnected network.

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
