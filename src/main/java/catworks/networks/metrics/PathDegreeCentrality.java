package catworks.networks.metrics;

import catworks.networks.IDN;
import catworks.networks.Network;

public class PathDegreeCentrality implements InterdependentCentrality {

    /**
     * TODO: Look at the MatLab code that Khamfroush sent out earlier in the spring semester. Use that as a reference
     * to implement this centrality metric. The mathematical notation is a bit bloated and unclear.
     */
    public double[] getCentralities(IDN idn) {
        // Verify that the idn only has two networks, since PathDegree
        // only works with IDNs comprised of two networks.
        if (idn.getNumberOfNetworks() != 2) {
            throw new IllegalArgumentException("IDN must only be comprised of TWO networks.");
        }

        // Initialize variables needed for later computation.
        int[][] Ax = idn.getNetwork(0).getIntArray();
        int[][] Ay = idn.getNetwork(1).getIntArray();
        // TODO: get Axy and Ayx.
        InterEdge[] interEdges = idn.getInterEdges();
        int[][] A  = idn.bridge().getIntArray();
        int n1 = Ax.getNumberOfNodes();
        int n2 = Ay.getNumberOfNodes();

        // Step 1: Get border nodes in the interdependent network.
        ArrayList<Integer> borderX = new ArrayList<Integer>();
        ArrayList<Integer> borderY = new ArrayList<Integer>();
        for (InterEdge edge : interEdges) {
            switch (edge.getSourceNetwork()) {
                case 0:
                    borderX.add(edge.getSourceNode());
                    break;
                case 1:
                    borderY.add(edge.getSourceNode());
                    break;
                default:
                    break;
            }
        }

        // Step 2: Calculate the costs for each nodes.
        double[][] cost = new double[n1+n2][n1+n2];
        for (int q = 0; q < n1 + n2; q++) {
            for (int t = 0; t < n1 + n2; t++) {
                if (q < n1 && t < n1) {
    				if (A[q][t] == 1)
                        cost[q][t] = Math.ceil(kx * sum(col(Ax, t)));
    				else
                        cost[q][t] = 0;
                }

    			else if (q < n1 && t >= n1) {
    				if (A[q][t] == 1)
                        cost[q][t] = Math.ceil(kxy * sum(col(Axy, t-n1)));
    				else
                        cost[q][t] = 0;
                }

    			else if (q >= n1 && t < n1) {
    				if (A[q][t] == 1)
                        cost[q][t] = Math.ceil(kyx * sum(col(Ayx, t)));
    				else
                        cost[q][t] = 0;
                }

    			else if (q >= n1 && t >= n1) {
    				if (A[q][t] == 1)
                        cost[q][t] = Math.ceil(ky * sum(col(Ay, t-n1)));
    				else
                        cost[q][t] = 0;
                }
            }
        }

        // Step 3: Calculate centralities for each node.
        double[][] C = cost;
        double[]   centrality = new double[n1+n2];
        int source, target;

        for (int i = 0; i < n1; i++) {
    		for (int j = 0; j < borderY.size(); j++) {
    			source = i;
    			target = borderY[j] + n1;
    			// dist, P = dijkstra(C, source, target); // TODO: Revisit this.
    			for (int k = 1; k < P.length - 1; k++) {
    				centrality[s1] = centrality[P[k]] + 1;
                }
            }
        }

    	for (int i = n1; i < n1 + n2; i++) {
    		for (int j = 0; j < borderX.size(); j++) {
    			source = i;
    			target = borderX[j];
    			// dist, P = dijkstra(C, source, target); // TODO: Revisit this.
    			for (int k = 1; k < P.length - 1; k++) {
    				centrality[s1] = centrality[P[k]] + 1;
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
    private int[] dijkstraPath(double[][] adj, int source, int target) {
        // TODO: This needs to be implemented such that it returns the PATH of
        // the shortest path.
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