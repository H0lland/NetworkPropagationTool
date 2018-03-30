package catworks.networks.metrics;

public class ClosenessCentrality implements Centrality {

    /*
     * Gets the centrality specifically for finding the Eigenvectors of the matrix
     * @param  Integer[][] matrix        original matrix
     * @return             eigenvector values
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

        org.graphstream.algorithm.measure.ClosenessCentrality centrality = new org.graphstream.algorithm.measure.ClosenessCentrality("centrality", org.graphstream.algorithm.measure.AbstractCentrality.NormalizationMode.SUM_IS_1);
        centrality.init(graph);
        centrality.compute();

        Double[] centralities = new Double[N];
        for (int node = 0; node < N; node++) {
            centralities[node] = graph.getNode(node + "").getAttribute("centrality");
        }

        return centralities;
    }

    @Override
    public String toString() {
        return "closeness";
    }

}
