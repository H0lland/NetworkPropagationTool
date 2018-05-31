package catworks.networks.metrics;

import catworks.networks.IDN;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PathDegreeCentrality implements InterdependentCentrality {

    private static double kx = 0.3,  ky = 0.3;  // Intra-edge link weights.
    private static double kxy = 0.4, kyx = 0.4; // Inter-edge link weights.

    /**
     * TODO: Look at the MatLab code that Khamfroush sent out earlier in the spring semester. Use that as a reference
     * to implement this centrality metric. The mathematical notation is a bit bloated and unclear.
     */
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

        // System.out.printf("[");
        // for (int[] row : A) {
        //     System.out.printf("[");
        //     for (int edge : row) {
        //         System.out.printf("%d, ", edge);
        //     }
        //     System.out.printf("], ");
        // }
        // System.out.printf("]\n\n");

        // Step 2: Get border nodes in the interdependent network.
        ArrayList<Integer> borderXTemp = new ArrayList<Integer>();
        ArrayList<Integer> borderYTemp = new ArrayList<Integer>();
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n1; j++) {
                if (Axy[i][j] == 1)
                    if (!borderYTemp.contains(j))
                        borderYTemp.add(j);
            }
        }
        for (int i = 0; i < n2; i++) {
            for (int j = 0; j < n2; j++) {
                if (Ayx[i][j] == 1)
                    if (!borderXTemp.contains(j))
                        borderXTemp.add(j);
            }
        }
        // Convert the ArrayList instances to standard arrays.
        Integer[] borderX = new Integer[borderXTemp.size()];
        Integer[] borderY = new Integer[borderYTemp.size()];
        borderX = borderXTemp.toArray(borderX);
        borderY = borderYTemp.toArray(borderY);

        // Step 3: Calculate the costs for each nodes.
        int[][] cost = new int[n1+n2][n1+n2];
        for (int q = 0; q < n1 + n2; q++) {
            for (int t = 0; t < n1 + n2; t++) {
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
        // System.out.println(C[21][8]); // Should be "cost[21][8] = 3".
        double[]   centrality = new double[n1+n2];
        int source, target;

        for (int i = 0; i < n1; i++) {
    		for (int j = 0; j < borderY.length; j++) {
    			source = i;
    			target = borderY[j] + n1;
                // dist, P = dijkstra(C, source, target); // TODO: Revisit this.
                int[] P = dijkstraPath(C, source, target);
    			for (int k = 1; k < P.length - 1; k++) {
                    int s1 = P[k];
    				centrality[s1] = centrality[s1] + 1;
                }
            }
        }

    	for (int i = n1; i < n1 + n2; i++) {
    		for (int j = 0; j < borderX.length; j++) {
    			source = i;
    			target = borderX[j];
                // dist, P = dijkstra(C, source, target); // TODO: Revisit this.
                int[] P = dijkstraPath(C, source, target);
                
                StringBuilder sb = new StringBuilder();
                for (int node : P) sb.append(node + " ");
                // System.out.println("path -> " + sb); // DEBUG

    			for (int k = 1; k < P.length - 1; k++) {
                    int s1 = P[k];
    				centrality[s1] = centrality[s1] + 1;
                }
            }
        }

        return centrality;
    }

    public int type() {
        return -1;
    }

    /**
     * [dijkstraPath description]
     * 
     * @param adj    [description]
     * @param source [description]
     * @param target [description]
     * @return [description]
     */
    private int[] dijkstraPath(int[][] adj, int source, int target) {
        int n = adj.length;
        HashMap<Integer, ArrayList<Integer>> adjList = toAdjacencyList(adj);

        double[] dist = new double[n];
        for (int i = 0; i < n; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
        }
        dist[source] = 0;

        HashMap<Integer, Double> previous = new HashMap<Integer, Double>();
        for (int i = 0; i < n; i++) {
            previous.put(i, Double.POSITIVE_INFINITY);
        }

        HashMap<Integer, ArrayList<Number>> paths = new HashMap<Integer, ArrayList<Number>>();
        for (int i = 0; i < n; i++) {
            paths.put(i, new ArrayList<Number>());
        }

        LinkedList<Integer> queue = new LinkedList<Integer>();
        for (int i = 0; i < n; i++) {
            queue.add(i);
        }

        while (!queue.isEmpty()) {
            Number[] temp = min(subset(dist, queue));
            double minDist = (double) temp[0];
            int minIndex = (int) temp[1];
            int u = queue.get(minIndex);

            // Termination condition.
            double t = u;
            while (previous.containsKey(t)) {
                paths.get(u).add(0, t);
                t = previous.get(t);
            }

            // We reached our target, return the path.
            if (u == target) {
                double pathLength = dist[u];
                int[] path = new int[paths.get(u).size()];
                for (int i = 0; i < path.length; i++)
                    path[i] = paths.get(u).get(i).intValue();
                
                System.out.printf("`pathLength` = %f\npath.length = %d\n\n", pathLength, path.length);
                return path;
            }

            queue.remove(new Integer(u)); // Needs to be an Integer object, otherwise `remove` will remove by index.
            for (int v = 0; v < adjList.get(u).size(); v++) {
                int val = adjList.get(u).get(v);
                double alt = dist[u] + adj[u][val];

                // System.out.printf("val = %d, alt = %f, dist[%d] = %f\n", val, alt, val, dist[val]);

                if (alt < dist[val]) {
                    dist[val] = alt;
                    previous.put(val, (double) u);
                    // System.out.printf("previous[%d] = %f\n", val, (double) u);
                }
            }
        }
        return new int[1];
    }

    /**
     * 
     */
    private HashMap<Integer, ArrayList<Integer>> toAdjacencyList(int[][] adjMatrix) {
        HashMap<Integer, ArrayList<Integer>> adjL = new HashMap<Integer, ArrayList<Integer>>();
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < adjMatrix.length; j++) {
                if (adjL.containsKey(i)) {
                    adjL.get(i).add(j);
                }
                else {
                    adjL.put(i, new ArrayList<Integer>());
                    adjL.get(i).add(j);
                }
            }
        }
        return adjL;
    }

    /**
     * Make and return a new ArrayList from `arr` that only contains the elements at the indices in `indices`.
     * @param arr     The array that we want a subset of. 
     * @param indices An ArrayList of indices.
     */
    private double[] subset(double[] arr, List<Integer> indices) {
        double[] subset = new double[indices.size()];
        int i = 0;
        for (Integer index : indices) {
            if (index < 0 || index >= arr.length)
                throw new IndexOutOfBoundsException();
            subset[i++] = arr[index];
        }
        return subset;
    }

    /**
     * Creates and returns a tuple (represented as an array of Numbers) in which the tuple contains the
     * minimum element value and the index of that value.
     * @param arr The array in which we wish to find the minimum value and minimum index of.
     */
    private Number[] min(double[] arr) {
        Number[] tuple = new Number[2];
        double minValue = Double.POSITIVE_INFINITY;
        int minIndex = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < minValue) {
                minValue = arr[i];
                minIndex = i;
            }
        }

        // Place the values into the tuple and then return it.
        tuple[0] = new Double(minValue);
        tuple[1] = new Integer(minIndex);
        return tuple;
    }

    /**
     * [sum description]
     * 
     * @param arr [description]
     * @return [description]
     */
    private double sum(double[] arr) {
        double total = 0.0;
        for (double d : arr) {
            total += d;
        }
        return total;
    }

    /**
     * [sum description]
     * 
     * @param arr [description]
     * @return [description]
     */
    private double sum(int[] arr) {
        double total = 0.0;
        for (double d : arr) {
            total += d;
        }
        return total;
    }

    /**
     * [col description]
     * 
     * @param arr [description]
     * @param n   [description]
     * @return [description]
     */
    private int[] col(int[][] arr, int n) {
        int[] column = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            column[i] = arr[i][n];
        }
        return column;
    }

}