package catworks.networks;

import java.util.ArrayList;
import java.util.Collections;

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
        connectedSubGraph(graph, m0);

        // NOTE: There's an error in this code that allows for an infinite loop.

        // STEP 3: Connect new nodes to every pre-existing node_i with probability
        // p_i. The formal definition for p_i = k_i / sum(k_j).
        for (int newNode = m0; newNode < n; newNode++) {
            int newNodeEdgeCounter = 0;

            // Loop through the pre-existing nodes and make edges with a probability
            // with respect to the degrees of pre-existing nodes. However, the number
            // of new edges for `newNode` (m) must be < m0. If m >= m0, terminate loop.
            for (int node_i = 0; node_i < newNode && newNodeEdgeCounter < m0; node_i++) {
                double k_i = degree(graph, node_i);
                int    k_j = 0;
                for (int node_j = 0; node_j < newNode; node_j++) {
                    k_j += degree(graph, node_j);
                }

                // Calculate the probabilty and then make an undirected edge between
                //  `newNode` and `node_i` w.r.t. to this probability.
                double p_i = k_i / k_j;
                if (Math.random() <= p_i) {
                    graph[newNode][node_i] = 1;
                    graph[node_i][newNode] = 1;
                    newNodeEdgeCounter++;
                }
            }

            // If no connection has been made, then add one at random.
            if (newNodeEdgeCounter == 0) {
                int j = (int) (Math.random() * newNode);
                graph[newNode][j] = 1;
                graph[j][newNode] = 1;
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
            degree += (graph[node][i] + graph[i][node]);
        }
        return degree;
    }


    /**
     * Given an empty graph of size MxM, make connections between the edges of
     * the subgraph comprised of the first `n` nodes such that that subgraph is
     * connected -- meaning any pair of nodes (v1, v2), there exists a path from
     * v1 to v2.
     * @param graph The adjacency matrix representing the entire network topology.
     * @param n     The dimension to designate the first nodes the subgraph belongs to.
     */
    private void connectedSubGraph(int[][] graph, int n) {
		ArrayList<Integer> links = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) links.add(i);
		Collections.shuffle(links);

        int i, j;
		int edges = 0;
		while (edges < n) {
            i = links.get(0);
			j = links.get((int) (Math.random() * links.size()));
			if (edges == n-1 || (graph[i][j] == 0 && i != j)) {
				graph[i][j] = 1;
				graph[j][i] = 1;
				i = links.remove(0);
				edges++;
			}
		}
	}

}
