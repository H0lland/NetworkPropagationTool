package catworks.networks.metrics;

import catworks.networks.IDN;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of Path-Degree centrality -- a novel approach to calculating centralities for interdependent networks.
 * This centrality metric takes advantage of interdependent links to calculate centralities.
 */
public class PathDegreeCentrality extends InterdependentCentrality {

    private static double kx = 0.3,  ky = 0.3;  // Intra-edge link weights.
    private static double kxy = 0.4, kyx = 0.4; // Inter-edge link weights.

    public double[] getCentralities(IDN idn) {
        // Verify that the idn only has two networks, since PathDegree
        // only works with IDNs comprised of two networks.
        if (idn.getNumOfNetworks() != 2) {
            throw new IllegalArgumentException("IDN must only be comprised of TWO networks.");
        }

        // Initialize variables needed for later computation.
        int[][] Ax  = idn.getNetwork(0).getIntArrayMatrix();
        int[][] Ay  = idn.getNetwork(1).getIntArrayMatrix();
        int[][] Axy = idn.getInterEdgeMatrix(0, 1);
        int[][] Ayx = idn.getInterEdgeMatrix(1, 0);
        int n1 = Ax.length;
        int n2 = Ay.length;

        // Step 1: Bridge the network manually.
        int[][] A = new int[n1+n2][n1+n2];
        for (int i = 0; i < n1 + n2; i++) {
            for (int j = 0; j < n1 + n2; j++) {
                if (i < n1 && j < n1)         A[i][j] = Ax[i][j];
                else if (i < n1 && j >= n1)   A[i][j] = Axy[i][j-n1];
                else if (i >= n1 && j < n1)   A[i][j] = Ayx[i-n1][j];
                else if (i >= n1 && j >= n1)  A[i][j] = Ay[i-n1][j-n1];
            }
        }

        // Step 2: Get border nodes in the interdependent network.
        ArrayList<Integer> borderXTemp = new ArrayList<Integer>();
        ArrayList<Integer> borderYTemp = new ArrayList<Integer>();
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < Axy[i].length; j++) { // for (int j = 0; j < n1; j++) {
                if (Axy[i][j] == 1)
                    if (!borderYTemp.contains(j))
                        borderYTemp.add(j);
            }
        }
        for (int i = 0; i < n2; i++) {
            for (int j = 0; j < Ayx[i].length; j++) { // for (int j = 0; j < n2; j++) {
                if (Ayx[i][j] == 1)
                    if (!borderXTemp.contains(j))
                        borderXTemp.add(j);
            }
        }

        // Convert the ArrayList instances to standard arrays.
        int[] borderX = new int[borderXTemp.size()];
        int[] borderY = new int[borderYTemp.size()];
        for (int i = 0; i < borderXTemp.size(); i++) borderX[i] = borderXTemp.get(i);
        for (int i = 0; i < borderYTemp.size(); i++) borderY[i] = borderYTemp.get(i);

        // Step 3: Calculate the costs for each nodes.
        int[][] cost = new int[n1+n2][n1+n2];
        for (int q = 0; q < n1 + n2; q++) {
            for (int t = 0; t < n1 + n2; t++) {
                // Figure out which portion of the IDN the edge (q, t) belongs to.
                if (q < n1 && t < n1) {
    				if (A[q][t] == 1)
                        cost[q][t] = (int) Math.ceil(kx * sum(col(Ax, t)));
    				else
                        cost[q][t] = 0;
                }

    			else if (q < n1 && t >= n1) {
    				if (A[q][t] == 1)
                        cost[q][t] = (int) Math.ceil(kxy * sum(col(Axy, t-n1)));
    				else
                        cost[q][t] = 0;
                }

    			else if (q >= n1 && t < n1) {
    				if (A[q][t] == 1)
                        cost[q][t] = (int) Math.ceil(kyx * sum(col(Ayx, t)));
    				else
                        cost[q][t] = 0;
                }

    			else if (q >= n1 && t >= n1) {
    				if (A[q][t] == 1)
                        cost[q][t] = (int) Math.ceil(ky * sum(col(Ay, t-n1)));
    				else
                        cost[q][t] = 0;
                }
            }
        }

        // Step 4: Calculate centralities for each node.
        int[][] C = cost;
        double[]   centrality = new double[n1+n2];
        int source, target;

        // Step 4.1: Calculate centralities for nodes in X.
        for (int i = 0; i < n1; i++) {
    		for (int j = 0; j < borderY.length; j++) {
    			source = i;
    			target = borderY[j] + n1;
                int[] path = dijkstraPath(C, source, target);
    			for (int k = 1; k < path.length - 1; k++) {
                    int s1 = path[k];
    				centrality[s1] = centrality[s1] + 1;
                }
            }
        }

        // Step 4.2: Calculate centralities for nodes in Y.
    	for (int i = n1; i < n1 + n2; i++) {
    		for (int j = 0; j < borderX.length; j++) {
    			source = i;
    			target = borderX[j];
                int[] path = dijkstraPath(C, source, target);
    			for (int k = 1; k < path.length - 1; k++) {
                    int s1 = path[k];
    				centrality[s1] = centrality[s1] + 1;
                }
            }
        }

        return centrality;
    }

    @Override
    public String toString() { return "Path-Degree"; }

    public int type() {
        return AbstractCentrality.PATH_DEGREE;
    }

    /**
     * Get the path from a source node to a target node using Dijkstra's algorithm.
     * @param adj    [description]
     * @param source [description]
     * @param target [description]
     * @return [description]
     */
    private int[] dijkstraPath(int[][] adj, int source, int target) {
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

            if (u == target) {
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
        return shortestPath(prev, target);
    }

    /**
     * Upon computing dijkstra's, find the path from the source node to the target node using the computed array of
     * previous nodes.
     * @param prev   Array of ints such that `prev[i]` will hold the index of the node that comes before `i` in the shortest path.
     * @param target The node in which we wish to find the shortest path for.
     * @return       An array of ints that will represent the shortest path to `target`.
     */
    private int[] shortestPath(int[] prev, int target) {
        ArrayList<Integer> S = new ArrayList<Integer>();
        while (0 <= target && target < prev.length) {
            S.add(0, target);
            target = prev[target];
        }

        int[] path = new int[S.size()];
        for (int i = 0; i < S.size(); i++) path[i] = S.get(i);
        return path;
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

    /**
     * Add every value belonging to an array and return the value.
     * @param arr Array of ints we wish to get the summation of.
     * @return Summation.
     */
    private double sum(int[] arr) {
        double total = 0.0;
        for (double d : arr) {
            total += d;
        }
        return total;
    }

    /**
     * Grab the n-th column in a two-dimensional array of ints.
     * @param arr Two-dimensional array of ints.
     * @param n   The column index we wish to grab.
     * @return The n-th column of `arr`.
     */
    private int[] col(int[][] arr, int n) {
        int[] column = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            column[i] = arr[i][n];
        }
        return column;
    }

}