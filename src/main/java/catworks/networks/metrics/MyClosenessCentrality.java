package catworks.networks.metrics;

public class MyClosenessCentrality implements Centrality {

    /**
     * Return the closeness centralities of a given network.
     * @param matrix Adjacency Matrix representation for the network.
     */
    public Double[] getCentralities(Integer[][] matrix) {
        // Initialize variables and data strutcures necessary for this method.
        int n = matrix.length;
        Double[][] d = allPairsShortestPaths(matrix);
        Double[] centralities = new Double[n];
        for (int i = 0; i < n; i++) 
            centralities[i] = 0.0;
        
        // Sum up the reciprocals for every node and store the value in `h`.
        for (int x = 0; x < n; x++)
            for (int y = 0; y < n; y++)
                if (x != y)
                    centralities[x] += 1.0 / d[x][y];

        // Return the array of centralities.
        return centralities;
    }

    /**
     * Get the type identifier of the centrality class.
     * @return Type identifier of the centrality class, as defined in the Centrality abstract class.
     */
    public int type() {
        return Centrality.CLOSENESS;
    }

    /**
     * An all-pairs-shortest-paths algorithm that generates the shortest path between every pair of nodes
     * belonging to the provided graph. This method will take advantage of the Floyd-Warshall algorithm
     * for calculating shortest distance.
     * @param  graph
     * @return 
     */
    private Double[][] allPairsShortestPaths(Integer[][] graph) {
        // Initialize `d`, which will serve as the two-dimensional array to store the shortest
        // paths among all pairs. Also, let `n` be the number of nodes for code brevity.
        int n = graph.length;
        Double[][] d = prepare(graph);
        
        // Floyd-Warshall algorithm.
        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (i != j && i != k && j != k)
                        d[i][j] = (Double) min(d[i][j], d[i][k] + d[k][j]);
    
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
    private Double[][] prepare(Integer[][] graph) {
        Double[][] newGraph = new Double[graph.length][graph.length];

        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (i == j) 
                    newGraph[i][j] = 0.0;
                
                else if (graph[i][j] != 0) 
                    newGraph[i][j] = graph[i][j].doubleValue();
                
                else // Note: graph[i][j] == 0.
                    newGraph[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        return newGraph;
    }

    /**
     * [Description]
     * @param  a
     * @param  b
     * @return 
     */
    private Number min(Number a, Number b) {
        return (a.doubleValue() < b.doubleValue()) ? a : b;
    }

    @Override
    public String toString() {
        return "MyCloseness";
    }

}
