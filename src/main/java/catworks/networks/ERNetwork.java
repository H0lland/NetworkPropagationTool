package catworks.networks;

public class ERNetwork extends Network {

    private int    n;
    private double p;

    // For this study, we do not want to consider networks with disconnected nodes.
    // The context of cyber-physical networks makes a nodes without edges unintuitive.
    // For this reason, we include this variable. If necessary to change the nature
    // of this class such that disconnected nodes are possible, just change this
    // boolean variable to `false` (and vice versa).
    private final boolean PREVENT_DISCONNECTED_NODES = true;

    /**
     * No-arge constructor that makes a call to the super constructor, Network();
     */
    public ERNetwork() {
        super();
    }



    /**
     * Erdos-Renyi (ER) random network constructor.
     * @param n Number of nodes to be in the network.
     * @param p Probability that a node has an edge to another node.
     */
    public ERNetwork(int n, double p) {
        this.n = n;
        this.p = p;
        this.directed = UNDIRECTED;
        init(n, p);
    }


    /**
     * Erdos-Renyi (ER) random network constructor.
     * @param n Number of nodes to be in the network.
     * @param p Probability that a node has an edge to another node.
     * @param d Boolean argument that determines if graph is directed (true) or undirected (false).
     */
    public ERNetwork(int n, double p, boolean directed) {
        this.n = n;
        this.p = p;
        this.directed = directed;
        init(n, p);
    }

    /**
     * Rebuild the network using the provided `n` and `p` values when originally
     * constructed. This will allows simulations to recreate networks to have a
     * more varied sample for calculations.
     */
    @Override
    public void regenerate() {
        init(n, p);
    }

    /**
     * [init description]
     * @param n [description]
     * @param p [description]
     */
    private void init(int n, double p) {
        // Check if `p` is valid; 0 <= p <= 1.
        if (p < 0 || 1 < p) {
            throw new IllegalArgumentException("Value `p` must be in the range [0, 1].");
        }

        double rand;

        int[][] graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            int connections = 0;
            for (int j = 0; j < n; j++) {
                if (i == j) continue; // Avoid self-loops.
                // rand = (directed) ? p : p/2;
                if (Math.random() <= p/2) {// rand) { // NOTE: This seems to hold for directed and undirected.
                    graph[i][j] = 1;
                    if (!directed) graph[j][i] = 1;
                    connections++;
                }
            }

            // If no connections have been made and we are set to prevent disconnected
            // nodes, then createa random edge.
            if (connections == 0 && PREVENT_DISCONNECTED_NODES) {
                int randomEdge = (int) (Math.random() * n);
                if (randomEdge == i) {
                    if (i >= n-1) randomEdge--;
                    else          randomEdge++;
                }
                graph[i][randomEdge] = 1;
                if (!directed) graph[randomEdge][i] = 1;
            }
        }
        setIntArrayMatrix(graph);
    }

}
