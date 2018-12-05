package catworks.networks;

import java.util.ArrayList;

public class SFNetwork extends Network {

    private int n;
    private int m0;
    private int m;

    /**
     * No-arge constructor that makes a call to the super constructor, Network();
     */
    public SFNetwork() {
        super();
    }


    /**
     * Scale-Free (SF) random network constructor, using the Barabasi-Albert model.
     * @param n  Number of nodes to be in the network.
     * @param m0 Probability that a node has an edge to another node.
     */
    public SFNetwork(int n, int m0) {
        // Make sure that the parameters are legal.
        if (m0 < 1 || m0 >= n) {
            throw new IllegalArgumentException("Barabasi-Albert network must have m0 >= 1 and m0 < n. (m0 = " + m0 + ", n = " + n + ")");
        }
        this.n  = n;
        this.m0 = m0;
        this.m  = m0;
        this.directed = UNDIRECTED;
        init();
    }

    /**
     * Scale-Free (SF) random network constructor, using the Barabasi-Albert model.
     * @param n  Number of nodes to be in the network.
     * @param m0 Probability that a node has an edge to another node.
     * @param directed boolean value on whether or not the network is directed
	*/
    public SFNetwork(int n, int m0, boolean directed) {
        // Make sure that the parameters are legal.
        if (m0 < 1 || m0 >= n) {
            throw new IllegalArgumentException("Barabasi-Albert network must have m0 >= 1 and m0 < n. (m0 = " + m0 + ", n = " + n + ")");
        }
        this.n  = n;
        this.m0 = m0;
        this.m  = m0;
        this.directed = directed;
        init();
	}
    /**
     * Scale-Free (SF) random network constructor, using the Barabasi-Albert model.
     * @param n  Number of nodes to be in the network.
     * @param m0 Probability that a node has an edge to another node.
     * @param m  
     */
    public SFNetwork(int n, int m0, int m) {
        // Make sure that the parameters are legal.
        if (m0 < 1 || m0 >= n) {
            throw new IllegalArgumentException("Barabasi-Albert network must have m0 >= 1 and m0 < n. (m0 = " + m0 + ", n = " + n + ")");
        }
        this.n  = n;
        this.m0 = m0;
        this.m  = m;
        this.directed = UNDIRECTED;
        init();
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
     * [init description]
     * @param n [description]
     * @param p [description]
     */
    private void init() {
        int[][] graph;
        do {
            // STEP 1: Initialize a new adjacency matrix to represent the network.
            graph = new int[n][n];
            int edges = 0;

            // STEP 2: Network begins with an initial connected network of m0 nodes.
            for (int i = 0; i < m0; i++) {
                for (int j = i+1; j < m0; j++) {
                    graph[i][j] = 1;
                    if(!directed) graph[j][i] = 1;
                    edges += 2;
                }
            }

            // STEP 3: Connect new nodes to every pre-existing node_i with probability
            // with respect to preferential attachment bias.
            for (int i = m0; i < n; i++) {
                int currDegree = 0;
                while (currDegree < m) {
                    // Grab the index of a node that node i has no edge to already.
                    int[] sample = getSample(graph, i);
                    int randIndex = (int)(Math.random() * sample.length);
                    int j  = sample[randIndex];

                    // Preferential attachment bias.
                    double beta = (double) degree(graph, j) / edges;
                    double rand = Math.random();
                    if (beta > rand) {
                        // Make directional edge
                        graph[i][j] = 1;
                        edges += 1;
						//Make directional edge if necessary
						if(!directed){
							graph[j][i] = 1;
                        	edges += 1;
						}
                    }
                    else {
                        boolean noConnection = true;
                        while (noConnection) {
                            // Grab the index of a node that node i has no edge to already.
                            sample = getSample(graph, i);
                            randIndex = (int)(Math.random() * sample.length);
                            int h  = sample[randIndex];

                            // Preferential attachment bias.
                            beta = (double) degree(graph, h) / edges;
                            rand = Math.random();
                            if (beta > rand) {
                                // Make directional edge.
                                graph[i][h] = 1;
                                edges += 1;
								//Make bidirectional edge if necessary
								if(!directed){
									graph[h][i] = 1;
                                	edges += 1;
                                }
								noConnection = false;
                            }
                        }
                    }
                    currDegree = degree(graph, i);
                }
            }
        } while (!isConnected(graph));

        // STEP 4: Initialize network using the scale-free adjacency matrix.
        setIntArrayMatrix(graph);
    }


    /**
     * A sample will contain the elements from the range [0:n) with the set of
     * node indices that node i has an edge to removed
     * @param  graph Adjacency matrix topology.
     * @param  i     Node index.
     * @return       Array of ints that represent the node indices that node i
     *               does NOT have an edge to.
     */
    private int[] getSample(int[][] graph, int i) {
		// Find the indicies of non-zero values.
		ArrayList<Integer> nonzero = new ArrayList<Integer>();
		for (int j = 0; j < graph[i].length; j++) {
			if (graph[i][j] != 0 || i == j) nonzero.add(j);
		}

		// Initialize `sample` to contain integers [0:N-1], then remove all non-zero indicies.
		ArrayList<Integer> sample = new ArrayList<Integer>();
		for (int j = 0; j < graph[i].length; j++) sample.add(j);
		sample.removeAll(nonzero);

		// Convert `sample` to a primitive array of type int.
		int[] ret = new int[sample.size()];
		for (int j = 0; j < ret.length; j++) {
			ret[j] = sample.get(j);
		}
		return ret;
	}


    /**
     * Access the number of nodes `node` has an edge to.
     * @param  graph Adjacency matrix topology.
     * @param  node  Index of node whose degree is wanted.
     * @return       The number of nodes `node` has an edge to.
     */
    private int degree(int[][] graph, int node) {
        int degree = 0;
        for (int i = 0; i < graph.length; i++) {
            degree += graph[node][i];
        }
        return degree;
    }

}
