package catworks.networks.metrics;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.LinkedList;

/**
 * Paper on an algorithm to implement Betweenness Centrality:
 * https://people.csail.mit.edu/jshun/6886-s18/papers/p35-prountzos.pdf
 */
public class BetweennessCentrality extends Centrality {

    /**
     * Calculate the betweenness centrality for each node in the network. This method implements
     * the Brandes Algorithm for betweenness centrality. The runtime for this algorithm on
     * unweighted networks in O(n*m) time and weighted networks in O(n*m + n^2*log(n)) time -- n
     * being the number of nodes and m being the number of edges.
     * @param graph Adjacency matrix representation of a network.
     * @return An array of Doubles, with the i-th element of the array representing 
     *         the betweenness centrality for node i.
     */
    public double[] getCentralities(int[][] graph) {
        // Generate a set of ids for the nodes in the graph (V), and the array 
        // that will track centrality for each node.
        int[] V = range(graph.length);
        double[] CB = new double[graph.length];
        Arrays.fill(CB, 0.0);

        // Initialize each data structure necessary for computing betweenness centrality.
        HashMap<Integer, ArrayList<Integer>> path = new HashMap<Integer, ArrayList<Integer>>();
        HashMap<Integer, Double> sigma = new HashMap<Integer, Double>();
        HashMap<Integer, Double> dist = new HashMap<Integer, Double>();
        HashMap<Integer, Double> delta = new HashMap<Integer, Double>();

        // Iterate through each vertex in the set of vertices.
        for (int s : V) {
            // Initialize necessary data structures for calculating betweenness centrality.
            Stack<Integer> stack = new Stack<Integer>();

            // 1. Initialize the HashMap of paths for each node, such that each value
            //    in the HashMap is a newly initialized ArrayList.
            for (int w : V) path.put(w, new ArrayList<Integer>());
            
            // 2. Initialize the HashMap for sigma values for each node, such that each value
            //    is 0.0. Then, upon initalizing the HashMap, set the value at key, s, to 1.0.
            for (int t : V) sigma.put(t, 0.0);
            sigma.put(s, 1.0);
            
            // 3. Initialize the HashMap for distance values for each node, such that each value
            //    is -1.0. Then, upon initalizing the HashMap, set the value at key, s, to 0.0.
            for (int t : V) dist.put(t, -1.0);
            dist.put(s, 0.0);
            
            // 4. Initialize the HashMap for delta values for each node, such that each value is 0.0.
            for (int v : V) delta.put(v, 0.0);

            // 5. Initialize a new queue and then add node `s` to the queue.
            LinkedList<Integer> queue = new LinkedList<Integer>();
            queue.add(s);

            // Loop until the queue is empty.
            while (!queue.isEmpty()) {
                Integer v = queue.removeFirst();
                stack.push(v);
                // Loop through the neighbors of node `v`.
                for (Integer w : neighbors(v, graph)) {
                    // If `w` is found for the first time.
                    if (dist.get(w) < 0) {
                        queue.add(w);
                        dist.put(w, dist.get(v) + 1.0);
                    }

                    // Shortest path to `w` via `v`.
                    if (dist.get(w) == (dist.get(v) + 1.0)) {
                        sigma.put(w, sigma.get(w) + sigma.get(v));
                        path.get(w).add(v);
                    }
                }
            }

            while (!stack.isEmpty()) {
                Integer w = stack.pop();
                for (Integer v : path.get(w)) {
                    double newValue = delta.get(v) + sigma.get(v)/sigma.get(w) * (1.0 + delta.get(w));
                    delta.put(v, newValue);
                }
                
                if (w != s) CB[w] = CB[w] + delta.get(w);               
            }
        }
        return CB;
    }

    /**
     * Creates and returns an incremental array of Integers of length n.
     * @param n The number of elements in the array to be returned.
     * @return An array of type Integer[] of the form, {0, 1, 2, ..., n}.
     */    
    private int[] range(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        return arr;
    }

    /**
     * Finds the nodes in a graph that have an edge to `node`.
     * @param node  The ID of the node that we wish to find the neighbors of.
     * @param graph The graph topology our node belongs to.
     * @return An ArrayList that contains the IDs of all nodes with edges to `node` that are not itself.
     */
    private ArrayList<Integer> neighbors(int node, int[][] graph) {
        ArrayList<Integer> neighbors = new ArrayList<Integer>();
        for (Integer i = 0; i < graph.length; i++) {
            if (i == node)
                continue;
            if (graph[i][node] == 1)
                neighbors.add(i);
        }
        return neighbors;
    }

    /**
     * Returns the type ID for this centrality metric.
     */
    public int type() {
        return AbstractCentrality.BETWEENNESS;
    }

    @Override
    public String toString() {
        return "Betweenness";
    }

}