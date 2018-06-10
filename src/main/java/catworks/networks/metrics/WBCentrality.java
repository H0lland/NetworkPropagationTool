package catworks.networks.metrics;

import catworks.networks.IDN;
import java.util.ArrayList;

public class WBCentrality extends InterdependentCentrality {

    public double[] getCentralities(IDN idn) {
        // Initialize array to store centrality values.
        double[] centrality = new double[idn.getNumOfNodes()];

        // Iniitalize data structures to represent IDN.
        int[][] X = idn.getNetwork(0).getIntArrayMatrix();
        int[][] Y = idn.getNetwork(1).getIntArrayMatrix();
        int[][] XY = idn.getInterEdgeMatrix(0, 1);
        int[][] YX = idn.getInterEdgeMatrix(1, 0);
        int n1 = X.length, n2 = Y.length;

        // Step 1: Bridge the network manually.
        int[][] A = new int[n1+n2][n1+n2];
        for (int i = 0; i < n1 + n2; i++) {
            for (int j = 0; j < n1 + n2; j++) {
                if (i < n1 && j < n1)         A[i][j] = X[i][j];
                else if (i < n1 && j >= n1)   A[i][j] = XY[i][j-n1];
                else if (i >= n1 && j < n1)   A[i][j] = YX[i-n1][j];
                else if (i >= n1 && j >= n1)  A[i][j] = Y[i-n1][j-n1];
            }
        }
        double[][] dist = Algorithms.floydWarsall(A);

        // Step 2: Get border nodes in the interdependent network.
        ArrayList<Integer> borderXTemp = new ArrayList<Integer>();
        ArrayList<Integer> borderYTemp = new ArrayList<Integer>();
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < XY[i].length; j++) { // for (int j = 0; j < n1; j++) {
                if (XY[i][j] == 1)
                    if (!borderYTemp.contains(j))
                        borderYTemp.add(j);
            }
        }
        for (int i = 0; i < n2; i++) {
            for (int j = 0; j < YX[i].length; j++) { // for (int j = 0; j < n2; j++) {
                if (YX[i][j] == 1)
                    if (!borderXTemp.contains(j))
                        borderXTemp.add(j);
            }
        }

        // Step 2.1: Convert the ArrayList instances to standard arrays.
        int[] borderX = new int[borderXTemp.size()]; // Nodes in X that have an in-edge from a node in Y.
        int[] borderY = new int[borderYTemp.size()]; // Nodes in Y that have an in-edge from a node in X.
        for (int i = 0; i < borderXTemp.size(); i++) borderX[i] = borderXTemp.get(i);
        for (int i = 0; i < borderYTemp.size(); i++) borderY[i] = borderYTemp.get(i);

        // Step 3.1: Calculate centralities for network `X`.
        double numer = X.length - 1;
        double denom = 0.0;
        for (int x = 0; x < n1; x++) {
            for (int y = 0; y < n1; y++) {
                denom += dist[x][y];
            }
            centrality[x] = numer/denom;
            denom = 0.0;
        }

        numer = borderY.length - 1;
        for (int x = 0; x < n1; x++) {
            for (int z : borderY) {
                denom += dist[x][z+n1];
            }
            centrality[x] += numer/denom;
            denom = 0.0;
        }

        // Step 3.2: Calculate centralities for network `Y`.
        numer = Y.length - 1;
        denom = 0.0;
        for (int x = n1; x < n1 + n2; x++) {
            for (int y = n1; y < n1 + n2; y++) {
                denom += dist[x][y];
            }
            centrality[x] = numer/denom;
            denom = 0.0;
        }

        numer = borderX.length - 1;
        for (int x = 0; x < n1; x++) {
            for (int z : borderX) {
                denom += dist[x][z];
            }
            centrality[x] += numer/denom;
            denom = 0.0;
        }

        // Return the centralities for each node.
        return centrality;
    }

    public int type() {
        return AbstractCentrality.WEIGHTED_BOUNDARY;
    }
    
    @Override
    public String toString() {
        return "Weighted-Boundary";
    }

}