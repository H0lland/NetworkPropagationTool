package catworks.networks.metrics;

import java.util.ArrayList;
import java.util.List;

public class Algorithms {

    /**
     * Get the path from a source node to a target node using Dijkstra's algorithm.
     * @param adj    [description]
     * @param source [description]
     * @param target [description]
     * @return [description]
     */
    public static int[] dijkstraPath(int[][] adj, int source, int target) {
        int n = adj.length;
        ArrayList<Integer> queue = new ArrayList<Integer>();
        double[] dist = new double[n];
        int[] prev = new int[n];

        // Initialize the data structures necessary for computing dijkstra's algorithm
        // to initial values.
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
     * Upon computing dijkstra's, find the path from the source node to the target
     * node using the computed array of previous nodes.
     * 
     * @param prev   Array of ints such that `prev[i]` will hold the index of the
     *               node that comes before `i` in the shortest path.
     * @param target The node in which we wish to find the shortest path for.
     * @return An array of ints that will represent the shortest path to `target`.
     */
    public static int[] shortestPath(int[] prev, int target) {
        ArrayList<Integer> S = new ArrayList<Integer>();
        while (0 <= target && target < prev.length) {
            S.add(0, target);
            target = prev[target];
        }

        int[] path = new int[S.size()];
        for (int i = 0; i < S.size(); i++)
            path[i] = S.get(i);
        return path;
    }

    /**
     * This method works identically to the one-arg version of Python's native
     * `range` function. It returns an array of ints of length `n` such that each
     * element is incremental.
     * 
     * @param n The number of elements to belong to the array.
     * @return Newly created array of ints, e.g., {0, 1, 2, ..., n-1}.
     */
    private static int[] range(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++)
            arr[i] = i;
        return arr;
    }

    /**
     * Find the index of the smallest element in an array among the set of indices
     * we're interested in.
     * 
     * @param arr     The array in which we wish to find the minimum value's index.
     * @param indices Set of indices that will be used to search for. If an index in
     *                `arr`, does not belong in `indices`, then we are not concerned
     *                with it when finding the minimum value's index (hence, we
     *                ignore it).
     * @return Index of smallest value in `arr`, such that this index is among the
     *         set of the provided indices.
     */
    private static int min(double[] arr, List<Integer> indices) {
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

}