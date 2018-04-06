package catworks.networks;

import java.util.ArrayList;

public class SWNetwork extends Network {

    private int    n;
    private double p;
    private int    K;

    /**
     * No-arge constructor that makes a call to the super constructor, Network();
     */
    public SWNetwork() {
        super();
    }


    /**
     * Erdos-Renyi (ER) random network constructor.
     * @param n Number of nodes to be in the network.
     * @param p Probability that a node has an edge to another node.
     */
    public SWNetwork(int n, double p, int K) {
        this.n = n;
        this.p = p;
        this.K = K;
        init(n, p, K);
    }


    /**
     * Rebuild the network using the provided `n` and `p` values when originally
     * constructed. This will allows simulations to recreate networks to have a
     * more varied sample for calculations.
     */
    @Override public void regenerate() {
        init(n, p, K);
    }


    /**
     * [init description]
     * @param n [description]
     * @param p [description]
     */
    private void init(int n, double p, int K) {
        matrix = new ArrayList<ArrayList<Integer>>();
		if (K % 2 != 0) K++; // Ensure that `K` is even.

		// 1) Make ring lattice graph.
		for (int i = 0; i < n; i++) {
            // Iniitalize an `edges` ArrayList for the i-th node.
            ArrayList<Integer> edges = new ArrayList();
			for (int j = 0; j < n; j++) {
                // Determine whether to add a link or not.
                double temp = Math.abs(i - j) % (n - 1 - K/2.0);
				if (0 < temp && temp <= K / 2.0)
                    edges.add(1);
                else
                    edges.add(0);
			}
            // Add row (representing links) to matrix.
            matrix.add(edges);
		}

		// 2) Rewire the edges.
		for (int j = 0; j < n; j++) {
			for (int i = 0; i < j; i++) {
				if (Math.random() <= p) {
					int k = (int) (Math.random() * n);
					while (k == i)
                        k = (int) (Math.random() * n);
					matrix.get(i).set(j, 0);
					matrix.get(i).set(k, 1);
				}
			}
		}
    }

}
