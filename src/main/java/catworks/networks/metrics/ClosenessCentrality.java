package catworks.networks.metrics;

public class ClosenessCentrality extends Centrality {

  public ClosenessCentrality(){}
    /**
     * Return the closeness centralities of a given network.
     * @param matrix Adjacency Matrix representation for the network.
     */
    public double[] getCentralities(int[][] matrix) {
        // Initialize variables and data strutcures necessary for this method.
        int n = matrix.length;
        double[][] d = allPairsShortestPaths(matrix);
        double[] centralities = new double[n];

        // Sum up the reciprocals for every node and store the value in `h`.
        for (int x = 0; x < n; x++){
            for (int y = 0; y < n; y++){
                if (x != y){
                    centralities[x] += 1.0 / d[x][y];
                }
            }
            centralities[x] *= ((double) matrix.length-1);
        }
        // Return the array of centralities.
        return centralities;
    }

    /**
     * Return the closeness centralities of a given network.
     * @param matrix Adjacency Matrix representation for the network.
     * @param weight weight to multiply the reciprocal fairness by
     */
    public double[] getCentralities(int[][] matrix, double weight) {
        // Initialize variables and data strutcures necessary for this method.
        int n = matrix.length;
        double[][] d = allPairsShortestPaths(matrix);
        double[] centralities = new double[n];

        // Sum up the reciprocals for every node and store the value in `h`.
        for (int x = 0; x < n; x++){
            for (int y = 0; y < n; y++){
                if (x != y){
                    centralities[x] += 1.0 / d[x][y];
                }
            }
            centralities[x] *= weight;
        }
        // Return the array of centralities.
        return centralities;
    }

    /**
     * Get the type of centrality metric.
     * @return Type identifier of centrality metric.
     */
    public int type() {
        return AbstractCentrality.CLOSENESS;
    }

    @Override
    public String toString() {
        return "Closeness";
    }

    /**
     * An all-pairs-shortest-paths algorithm that generates the shortest path between every pair of nodes
     * belonging to the provided graph. This method will take advantage of the Floyd-Warshall algorithm
     * for calculating shortest distance.
     * @param  graph
     * @return
     */
    private double[][] allPairsShortestPaths(int[][] graph) {
        // Initialize `d`, which will serve as the two-dimensional array to store the shortest
        // paths among all pairs. Also, let `n` be the number of nodes for code brevity.
        int n = graph.length;
        double[][] d = prepare(graph);

        // Floyd-Warshall algorithm.
        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (i != j && i != k && j != k)
                        d[i][j] = min(d[i][j], d[i][k] + d[k][j]);

        // Return the two-dimensional array containing the shortest path among all pairs.
        return d;
    }

    /**
     * Make and return a modified copy of the passed in `graph` that is ready for the Floyd-Warshall
     * algorithm. By "ready", we mean that nodes values indicating no edge will become a positive infinity
     * and self-loops (if they exist) will be ignored/removed.
     * @param  graph
     * @return
     */
    private double[][] prepare(int[][] graph) {
        double[][] newGraph = new double[graph.length][graph.length];

        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (i == j)
                    newGraph[i][j] = 0.0;

                else if (graph[i][j] != 0)
                    newGraph[i][j] = graph[i][j];

                else // Note: graph[i][j] == 0.
                    newGraph[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        return newGraph;
    }

    /**
     * Get the smaller double value.
     * @param  a First double value.
     * @param  b Second double value.
     * @return Smaller/minimum double value between the provided arguments.
     */
    private double min(double a, double b) {
        return (a < b) ? a : b;
    }


    /**
     * A prior implementation using the GraphStream API. We have began to move away from
     * GraphStream's library as a whole. However, the results of our implementation of
     * Closeness Centrality slightly differs from the results of GraphStream's. With this
     * in mind, this method is simply serving to "backup" the original implementation.
     * @param matrix Adjacency matrix representation of graph underpinning network.
     */
    private double[] graphStreamImplementation(int[][] matrix) {
        final int N = matrix.length;
        org.graphstream.graph.implementations.SingleGraph graph = new org.graphstream.graph.implementations.SingleGraph("graph");

        for (int u = 0; u < N; u++)
            graph.addNode(u + "");

        for (int u = 0; u < N; u++) {
            for (int v = 0; v < N; v++) {
                // If there's an edge, add a directed edge.
                if (matrix[u][v] == 1)
                    graph.addEdge(u + "_" + v, u + "", v + "", true);
            }
        }

        org.graphstream.algorithm.measure.ClosenessCentrality centrality = new org.graphstream.algorithm.measure.ClosenessCentrality("centrality", org.graphstream.algorithm.measure.AbstractCentrality.NormalizationMode.NONE);
        centrality.init(graph);
        centrality.compute();

        double[] centralities = new double[N];
        for (int node = 0; node < N; node++) {
            centralities[node] = graph.getNode(node + "").getAttribute("centrality");
        }

        return centralities;
    }

}
