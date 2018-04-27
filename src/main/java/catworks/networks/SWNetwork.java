package catworks.networks;

public class SWNetwork extends Network {

    private int    n;
    private double beta;
    private int    K;

    /**
     * No-arge constructor that makes a call to the super constructor, Network();
     */
    public SWNetwork() {
        super();
    }


    /**
     * Small-World (SW) random network constructor, using the Watts–Strogatz model.
     * @param n    Number of nodes to be in the network.
     * @param beta Probability that a node has an edge to another node.
     * @param K    Average degree of each node, should be even.
     */
    public SWNetwork(int n, double beta, int K) {
        this.n = n;
        this.beta = beta;
        this.K = K;
        this.directed = UNDIRECTED;
        init(n, beta, K);
    }


    /**
     * Small-World (SW) random network constructor, using the Watts–Strogatz model.
     * @param n Number of nodes to be in the network.
     * @param beta Probability that a node has an edge to another node.
     * @param K ...
     */
    public SWNetwork(int n, double beta, int K, boolean directed) {
        this.n = n;
        this.beta = beta;
        this.K = K;
        this.directed = directed;
        init(n, beta, K);
    }


    /**
     * Rebuild the network using the provided `n` and `beta` values when originally
     * constructed. This will allows simulations to recreate networks to have a
     * more varied sample for calculations.
     */
    @Override
    public void regenerate() {
        init(n, beta, K);
    }


    /**
     * [init description]
     * @param n [description]
     * @param beta [description]
     */
    private void init(int n, double beta, int K) {
        int[][] graph;
        
        do {
            graph = new int[n][n]; // Initialize new adjacency graph.
            if (K % 2 != 0) K++;   // Ensure that `K` is even.

            // STEP 1: Make ring lattice graph.
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // Determine whether to add a link or not.
                    double temp = Math.abs(i - j) % (n - 1 - K/2.0);
                    if (0 < temp && temp <= K/2.0) {
                        graph[i][j] = 1;
                        graph[j][i] = 1;
                    }
                }
            }

            // STEP 2: Rewire the edges.
            for (int j = 0; j < n; j++) {
                for (int i = 0; i < j; i++) {
                    // Check if there is an edge.
                    if (graph[i][j] == 1) {
                        // Roll the die with `beta` probability.
                        if (Math.random() <= beta) {
                            // Randomly select a `k` value. If it is invalid, as in
                            // already an edge or is equal to `i`, pick another value.
                            int k = (int) (Math.random() * n);
                            while (k == i || graph[i][k] == 1)
                                k = (int) (Math.random() * n);

                            // Rewire the edges.
                            graph[i][j] = 0; graph[j][i] = 0;
                            graph[i][k] = 1; graph[k][i] = 1;
                        }
                    }
                }
            }
        } while (!isConnected(graph));

        // Build network's topology based on graph representation.
        setIntArrayMatrix(graph);
    }

}
