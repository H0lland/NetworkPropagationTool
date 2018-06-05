package catworks.networks.metrics;

import catworks.networks.IDN;
import java.util.ArrayList;

public class ProposalCentrality extends InterdependentCentrality {

    public double[] getCentralities(IDN idn) {
        // Step 1: Get the adjacency matrix representations for networks X and Y.
        final int[][] X = idn.getNetwork(0).getIntArrayMatrix();
        final int[][] Y = idn.getNetwork(1).getIntArrayMatrix();
        int[][] XY = idn.getInterEdgeMatrix(0, 1);
        int[][] YX = idn.getInterEdgeMatrix(1, 0);
        int n1 = X.length, n2 = Y.length;

        // Step 2: Bridge the interdependent network manually.
        int[][] A = new int[n1+n2][n1+n2];
        for (int i = 0; i < n1 + n2; i++) {
            for (int j = 0; j < n1 + n2; j++) {
                if (i < n1 && j < n1)         A[i][j] = X[i][j];
                else if (i < n1 && j >= n1)   A[i][j] = XY[i][j-n1];
                else if (i >= n1 && j < n1)   A[i][j] = YX[i-n1][j];
                else if (i >= n1 && j >= n1)  A[i][j] = Y[i-n1][j-n1];
            }
        }

        // Step 3: Get the distanes among all pairs in the IDN.
        double[][] dist = Algorithms.floydWarsall(A);

        // Step 4: Get the border nodes in the interdependent network.
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
        int[] borderX = new int[borderXTemp.size()]; // Nodes in X that have an in-edge from a node in Y.
        int[] borderY = new int[borderYTemp.size()]; // Nodes in Y that have an in-edge from a node in X.
        for (int i = 0; i < borderXTemp.size(); i++) borderX[i] = borderXTemp.get(i);
        for (int i = 0; i < borderYTemp.size(); i++) borderY[i] = borderYTemp.get(i);

        // Step 5: Calculate centralities for the IDN.
        double[] centralities = new double[idn.getNumOfNodes()];
        double[] degrees = new double[idn.getNumOfNodes()];
        for (int node = 0; node < n1; node++) degrees[node] = degree(A, node);
        for (int node = 0; node < n2; node++) degrees[node+n1] = degree(A, node+n1);

        double sumDegreeX = 0;
        double sumDegreeY = 0;
        for (int node = 0; node < X.length; node++) sumDegreeX += degree(X, node);
        for (int node = 0; node < Y.length; node++) sumDegreeY += degree(Y, node);

        // Step 5.1: Calculate centralities for network X.
        for (int x = 0; x < X.length; x++) {
            double multi = degrees[x] / sumDegreeX;
            double denom = 0.0;
            for (int y : borderX) {
                denom += dist[x][y];
            }
            centralities[x] = multi * 1.0/denom;
        }

        // Step 5.2: Calculate centralities for network Y.
        for (int x = 0; x < Y.length; x++) {
            double multi = degrees[x+n1] / sumDegreeY;
            double denom = 0.0;
            for (int y : borderY) {
                denom += dist[x+n1][y+n1];
            }
            centralities[x+n1] = multi * 1.0/denom;
        }

        // Step 6: Return the results.
        return centralities;
    }

    public int type() {
        return AbstractCentrality.PROPOSAL;
    }

    @Override
    public String toString() {
        return "Proposal";
    }

    private int degree(int[][] adj, int node) {
        int sum = 0;
        for (int i = 0; i < adj.length; i++) {
            sum += (adj[i][node] + adj[node][i]);
        }
        return sum;
    }

}