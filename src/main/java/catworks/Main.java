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
        //(new Thread(new SimulationRunner(1))).start();
        (new Thread(new SimulationRunner(2))).start();
        (new Thread(new SimulationRunner(3))).start();
        /*(new Thread(new SimulationRunner(4))).start();
        (new Thread(new SimulationRunner(5))).start();
        (new Thread(new SimulationRunner(6))).start();
        (new Thread(new SimulationRunner(7))).start();
        (new Thread(new SimulationRunner(8))).start();
        (new Thread(new SimulationRunner(9))).start();*/
//        (new Thread(new SimulationRunner(-1))).start();
    }

    public static void testCentrality(Centrality centrality1, Centrality centrality2) {
        int n = 20;
        double p = 0.1;
        Network net = new ERNetwork(n, p);
        boolean getSortedIndices = true;

        if (getSortedIndices) {
            int[] results1 = net.mostCentralNodes(centrality1, n); 
            int[] results2 = net.mostCentralNodes(centrality2, n);

            System.out.print(centrality1 + " = {");
            for (int i = 0; i < n; i++)
                if (i == n - 1) System.out.print(results1[i] + "}\n");
                else            System.out.print(results1[i] + ", ");

            System.out.print(centrality2 + " = {");
            for (int i = 0; i < n; i++)
                if (i == n - 1) System.out.print(results2[i] + "}\n");
                else            System.out.print(results2[i] + ", ");
        } 
        else {
            double[] results1 = net.getCentralities(centrality1);
            double[] results2 = net.getCentralities(centrality2);

            System.out.print(centrality1 + " = {");
            for (int i = 0; i < n; i++)
                if (i == n - 1) System.out.print(results1[i] + "}\n");
                else            System.out.print(results1[i] + ", ");

            System.out.print(centrality2 + " = {");
            for (int i = 0; i < n; i++)
                if (i == n - 1) System.out.print(results2[i] + "}\n");
                else            System.out.print(results2[i] + ", ");
        }
        
    }

    public static void testInterCentrality(InterdependentCentrality cen1, InterdependentCentrality cen2) {
        int n = 50, m = 3;
        IDN idn = new IDN(new SFNetwork(n, m), new SFNetwork(n, m));
        idn.randomInterEdges(0.12, true);
        boolean getSortedIndices = true;

        if (getSortedIndices) {
            int[] results1 = idn.mostCentralNodes(cen1, n); 
            int[] results2 = idn.mostCentralNodes(cen2, n);

            System.out.print(cen1 + " = {");
            for (int i = 0; i < n; i++)
                if (i == n - 1) System.out.print(results1[i] + "}\n");
                else            System.out.print(results1[i] + ", ");

            System.out.print(cen2 + " = {");
            for (int i = 0; i < n; i++)
                if (i == n - 1) System.out.print(results2[i] + "}\n");
                else            System.out.print(results2[i] + ", ");
        } 
        else {
            double[] results1 = idn.getCentralities(cen1);
            double[] results2 = idn.getCentralities(cen2);

            System.out.print(cen1 + " = {");
            for (int i = 0; i < n; i++)
                if (i == n - 1) System.out.print(results1[i] + "}\n");
                else            System.out.print(results1[i] + ", ");

            System.out.print(cen2 + " = {");
            for (int i = 0; i < n; i++)
                if (i == n - 1) System.out.print(results2[i] + "}\n");
                else            System.out.print(results2[i] + ", ");
        } 
    }

}
