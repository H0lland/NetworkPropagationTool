package catworks;

public class ERNetwork extends Network {

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
        // Check if `p` is valid; 0 <= p <= 1.
        if (p < 0 || 1 < p) {
            throw new IllegalArgumentException("Value `p` must be in the range [0, 1].");
        }

        boolean addEdge;
        matrix = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < n; i++) {
            ArrayList<Integer> row = new ArrayList();
            for (int j = 0; j < n; j++) {
                addEdge = Math.random() < p;
                if (addEdge) row.add(1);
                else         row.add(0);
            }
            matrix.add(row);
        }
    }

}
