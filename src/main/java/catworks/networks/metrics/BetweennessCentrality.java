package catworks.networks.metrics;

public class BetweennessCentrality implements Centrality {

    /**
     * Gets the centrality specifically for finding the Betweenness of the nodes in the matrix
     * @param  Integer[][] matrix        [description]
     * @return             [description]
     */
    public Double[] getCentralities(Integer[][] matrix) {
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

        org.graphstream.algorithm.BetweennessCentrality centrality = new org.graphstream.algorithm.BetweennessCentrality();
        centrality.computeEdgeCentrality(false);
        centrality.betweennessCentrality(graph);

        Double[] centralities = new Double[N];
        for (int node = 0; node < N; node++) {
            centralities[node] = graph.getNode(node + "").getAttribute("Cb");
        }
        return centralities;
    }

    @Override
    public String toString() {
        return "betweenness";
    }

}
