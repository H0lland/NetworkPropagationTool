package catworks.networks;

public class SFNetwork extends Network {

    private int n;
    private int m0;

    /**
     * No-arge constructor that makes a call to the super constructor, Network();
     */
    public SFNetwork() {
        super();
    }


    /**
     * Scale-Free (SF) random network constructor, using the Barabasi-Albert model.
     * @param n Number of nodes to be in the network.
     * @param m Probability that a node has an edge to another node.
     */
    public SFNetwork(int n, int m0) {
        this.n  = n;
        this.m0 = m0;
        this.directed = UNDIRECTED;
        init(n, m0);
    }


    /**
     * Rebuild the network using the provided `n` and `p` values when originally
     * constructed. This will allows simulations to recreate networks to have a
     * more varied sample for calculations.
     */
    @Override
    public void regenerate() {
        init(n, m0);
    }


    /**
     * [init description]
     * @param n [description]
     * @param p [description]
     */
    private void init(int n, int m0) {
        // Make sure that the parameters are legal.
        if (m0 < 1 || m0 >= n) {
            throw new IllegalArgumentException("Barabasi-Albert network must have m0 >= 1 and m0 < n. (m0 = " + m0 + ", n = " + n + ")");
        }

        // STEP 1: Initialize a new adjacency matrix to represent the network.
        int[][] graph = new int[n][n];

        // STEP 2: Network begins with an initial connected network of m0 nodes.
        for (int node_i = 0; node_i < m0; node_i++) {
            // Select a random node in the range 0, 1, ..., m0. Prevent self-loops.
            int node_j = (int) (Math.random() * m0);
            while (node_j == node_i)
                node_j = (int) (Math.random() * m0);

            // Add a bidirectioanl edge between `node_i` and `node_j`.
            graph[node_i][node_j] = 1;
            graph[node_j][node_i] = 1;
        }

        // STEP 3: Connect new nodes to every pre-existing node_i with probability
        // p_i. The formal definition for p_i = k_i / sum(k_j).
        for (int newNode = m0; newNode < n; newNode++) {
            int newNodeEdgeCounter = 0;

            // Loop through the pre-existing nodes and make edges with a probability
            // with respect to the degrees of pre-existing nodes. However, the number
            // of new edges for `newNode` (m) must be < m0. If m >= m0, terminate loop.
            for (int node_i = 0; node_i < newNode && newNodeEdgeCounter < m0; node_i++) {
                double k_i = degree(graph, node_i);
                int    denominator = 0;
                for (int node_j = 0; node_j < newNode; node_j++) {
                    if (node_i == node_j) continue;
                    denominator += degree(graph, node_j);
                }

                // Calculate the probabilty and then make an undirected edge between
                //  `newNode` and `node_i` w.r.t. to this probability.
                double p_i = k_i / denominator;
                if (Math.random() <= p_i) {
                    graph[newNode][node_i] = 1;
                    graph[node_i][newNode] = 1;
                    newNodeEdgeCounter++;
                }
            }
        }

        // STEP 4: Initialize network using the scale-free adjacency matrix.
        setIntArrayMatrix(graph);

    }


    /**
     * [degree description]
     * @param  graph [description]
     * @param  node  [description]
     * @return       [description]
     */
    private int degree(int[][] graph, int node) {
        int degree = 0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                degree += (graph[i][j] + graph[j][i]);
            }
        }
        return degree;
    }

}
