package catworks;

// Project import statements.
import catworks.networks.metrics.*;
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
        // (new Thread(new SimulationRunner(-1))).start();

        // Test between two different centrality metrics.
        testCentrality(new BetweennessCentrality(), new MyBetweennessCentrality());
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

    public static void testCentrality(Centrality centrality1, Centrality centrality2) {
        int n = 20;
        double p = 0.1;
        Network net = new ERNetwork(n, p);
        boolean getSortedIndicies = true;

        Number[] results1, results2;

        if (getSortedIndicies) {
            results1 = net.mostCentralNodes(centrality1, n); 
            results2 = net.mostCentralNodes(centrality2, n);
        } 
        else {
            results1 = net.getCentralities(centrality1);
            results2 = net.getCentralities(centrality2);
        }

        System.out.print(centrality1 + " = {");
        for (int i = 0; i < n; i++) {
            if (i == n - 1)
                System.out.print(results1[i] + "}\n");
            else
                System.out.print(results1[i] + ", ");
        }

        System.out.print(centrality2 + " = {");
        for (int i = 0; i < n; i++) {
            if (i == n - 1)
                System.out.print(results2[i] + "}\n");
            else
                System.out.print(results2[i] + ", ");
        }
    }

}
