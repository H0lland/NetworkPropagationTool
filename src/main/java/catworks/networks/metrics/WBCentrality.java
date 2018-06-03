package catworks.networks.metrics;

import catworks.networks.IDN;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Get the path from a source node to a target node using Dijkstra's algorithm.
     * @param adj    [description]
     * @param source [description]
     * @param target [description]
     * @return [description]
     */
    private double d(int[][] adj, int source, int dest) {
        int n = adj.length;
        ArrayList<Integer> queue = new ArrayList<Integer>();
        double[] dist = new double[n];
        int[] prev = new int[n];

        // Initialize the data structures necessary for computing dijkstra's algorithm to initial values.
        for (int node : range(n)) {
            queue.add(node);
            dist[node] = Double.POSITIVE_INFINITY;
            prev[node] = -1;
        }
        dist[source] = 0.0;

        // Loop through all elements of the queue until you reach the target.
        boolean targetFound = false;
        while (!queue.isEmpty() && !targetFound) {
            int minIndex = min(dist, queue);
            int u = queue.remove(minIndex);

            if (u == dest) {
                targetFound = true;
            }

            for (int v : range(n)) {
                // If there is a neighbor (via some weighted edge).
                if (adj[u][v] > 0) {
                    double alt = dist[u] + adj[u][v];
                    if (alt < dist[v]) {
                        dist[v] = alt;
                        prev[v] = u;
                    }
                }
            }
        }
        return dist[dest];
    }

    /**
     * Find the index of the smallest element in an array among the set of indices we're interested in.
     * @param arr     The array in which we wish to find the minimum value's index.
     * @param indices Set of indices that will be used to search for. If an index in `arr`, does not belong in `indices`,
     *                then we are not concerned with it when finding the minimum value's index (hence, we ignore it).
     * @return        Index of smallest value in `arr`, such that this index is among the set of the provided indices.
     */
    private int min(double[] arr, List<Integer> indices) {
        double[] newArr = new double[indices.size()];
        int i = 0;
        for (Integer index : indices)
            newArr[i++] = arr[index];
        
        double minVal = newArr[0];
        int minIndex = 0;
        for (i = 1; i < newArr.length; i++) {
            if (newArr[i] < minVal) {
                minVal = newArr[i];
                minIndex = i;
            }
        }

        return minIndex;
    }

    /**
     * This method works identically to the one-arg version of Python's native `range` function. It returns an
     * array of ints of length `n` such that each element is incremental.
     * @param n The number of elements to belong to the array.
     * @return  Newly created array of ints, e.g., {0, 1, 2, ..., n-1}.
     */
    private int[] range(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = i;
        return arr;
    }

    @Override
    public String toString() {
        return "Weighted-Boundary";
    }

}