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
        init();
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
        init();
    }

    /**
     * Erdos-Renyi (ER) weighted random network constructor.
     * @param n Number of nodes to be in the network.
     * @param p Probability that a node has an edge to another node.
     * @param directed Boolean argument that determines if graph is directed (true) or undirected (false).
     * @param min minimum weight for edges
     * @param max maximum weight for edges
     */
    public ERNetwork(int n, double p, boolean directed, int min, int max) {
        this.n = n;
        this.p = p;
        this.directed = directed;
        initWeighted(min, max);
    }
    /**
     * Rebuild the network using the provided `n` and `p` values when originally
     * constructed. This will allows simulations to recreate networks to have a
     * more varied sample for calculations.
     */
    @Override
    public void regenerate() {
        init();
    }

    /**
     * Generate a random network using the Erdos-Renyi model. This algorithm is slightly modified.
     * This algorithm will ensure that every node has at least one edge, irrespective of probability.
     * Further, this algorithm will run breadth-first search after construction to check if the graph
     * is connected. If it is connected, then it will simply set the internal Adjacency Matrix to
     * the constructed graph. Otherwise, it will try again to create a random network until it forms a
     * connected graph.
     * @param n Number of nodes in the network.
     * @param p Probability of adding an edge (0 < p <= 1).
     */
    private void init() {
        // Check if `p` is valid; 0 <= p <= 1.
        if (p <= 0 || 1 < p) {
            throw new IllegalArgumentException("Value `p` must be in the range (0, 1].");
        }

        int[][] graph;
        do {
            graph = new int[n][n];
            for (int i = 0; i < n; i++) {
                int connections = 0;
                for (int j = 0; j < n; j++) {
                    if (i == j) continue; // Avoid self-loops.
                    if (Math.random() <= p/2) { // NOTE: This seems to hold for directed and undirected.
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
        } while (!isConnected(graph)); // If the graph is NOT connected, try again.
        setIntArrayMatrix(graph);
    }
    /**
     * Generate a random network using the Erdos-Renyi model. This algorithm is slightly modified.
     * This algorithm will ensure that every node has at least one edge, irrespective of probability.
     * Further, this algorithm will run breadth-first search after construction to check if the graph
     * is connected. If it is connected, then it will simply set the internal Adjacency Matrix to
     * the constructed graph. Otherwise, it will try again to create a random network until it forms a
     * connected graph.
     * @param n Number of nodes in the network.
     * @param p Probability of adding an edge (0 < p <= 1).
     */
    private void initWeighted(int min, int max) {
        // Check if `p` is valid; 0 <= p <= 1.
        if (p <= 0 || 1 < p) {
            throw new IllegalArgumentException("Value `p` must be in the range (0, 1].");
        }

        int[][] graph;
        int weight;
        do {
            graph = new int[n][n];
            for (int i = 0; i < n; i++) {
                int connections = 0;
                for (int j = 0; j < n; j++) {
                    if (i == j) continue; // Avoid self-loops.
                    if (Math.random() <= p/2) { // NOTE: This seems to hold for directed and undirected.
                        weight = Math.random().nextInt(max-min)+min;
                        graph[i][j] = weight;
                        if (!directed) graph[j][i] = weight;
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
                    weight = Math.random().nextInt(max-min)+min;
                    graph[i][randomEdge] = weight;
                    if (!directed) graph[randomEdge][i] = weight;
                }
            }
        } while (!isConnected(graph)); // If the graph is NOT connected, try again.
        setIntArrayMatrix(graph);
    }
}
