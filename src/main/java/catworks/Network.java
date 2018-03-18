package catworks;

import java.util.ArrayList;
import java.util.Arrays;

/**
  * Note: I changed the indices from longs to ints to work with ArrayLists, whose
  * specifications do not allow long-based indexing.  I will make this change
  * in other classes as well
  */
public class Network extends AbstractNetwork {

    private static int count;
    private int networkID;
    private ArrayList<ArrayList<Integer>> matrix;

    /**
     * No-arg constructor that creates an empty Network.
     */
    public Network() {
        networkID = count++;
        matrix = new ArrayList<ArrayList<Integer>>();
    }


    /**
     * One-arg Network constructor.
     * @param adjacencyMatrix Two-dimensional array, an adjacency matrix, of ints
     *                        to be used to build a Network instance.
     */
    public Network(int[][] adjacencyMatrix){
        networkID = count++;
        matrix = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> inner;
        for(int i = 0; i < adjacencyMatrix.length; i++){
            inner = new ArrayList<Integer>();
            for(int j = 0; j< adjacencyMatrix[i].length; j++){
                inner.add(adjacencyMatrix[i][j]);
            }
            matrix.add(inner);
        }
    }


    /**
     * One-arg Network constructor.
     * @param adjacencyMatrix Two-dimensional array, an adjacency matrix, of Integers
     *                        to be used to build a Network instance.
     */
    public Network(Integer[][] adjacencyMatrix){
        networkID = count++;
        matrix = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> inner;
        for(int i = 0; i < adjacencyMatrix.length; i++){
            inner = new ArrayList<Integer>();
            for(int j = 0; j< adjacencyMatrix[i].length; j++){
                inner.add(adjacencyMatrix[i][j]);
            }
            matrix.add(inner);
        }
    }


    /**
     * One-arg Network constructor.
     * @param adjacencyMatrix Parameter will be used to represent Network.
     */
    public Network(ArrayList<ArrayList<Integer>> matrix){
        this.matrix = matrix;
        networkID = count++;
    }


    /**
     * Calculate the centralities of each node in a Network and return these values
     * as an array of Integers. Order of this array matters. The element at the
     * 0th index is the centrality of the 0th node and so on.
     * @param  metric The centrality metric that will be used to calculate centrality.
     * @return        Array of Integers with each i-th element being the centrality
     *                of the i-th node in the Network.
     */
    public Integer[] getCentralities(Centrality metric) {
        Integer[][] arrayMatrix = getArrayMatrix();
        return metric.getCentralities(arrayMatrix);
    }


    /**
     * Selects the "top `n`" central nodes in a Network given a centrality metric.
     * @param  metric The centrality metric that will be used to determine the
     *                centrality of nodes.
     * @param  n      The number of nodes to select based on centrality.
     * @return        An array of Integers that contains the indices of the most
     *                central nodes in the Network. The order of the indices matters
     *                as they are sorted by most central nodes to least central nodes.
     */
    public Integer[] mostCentralNodes(Centrality metric, int n) {
        ArrayList<Integer> centralities = new ArrayList(Arrays.asList(getCentralities(metric)));
        Integer[] mostCentral = new Integer[n];

        for (int i = 0; i < n; i++) {
            // Set the current minimum value, at first, to the maximum Integer
            // value. This guarantees that the first element will be selected to
            // maintain simple logic. Then initialize the current index to -1.
            Integer currentMax = Integer.MIN_VALUE;
            int currentIndex = -1;

            // Iterate through each centrality in `centralities`. Each iteration
            // through the loop will find the smallest value in `centralities`.
            // Upon completion of the loop, the value of the smallest value will
            // be changed such that it is not redundantly selected.
            for (int j = 0; j < centralities.size(); j++) {
                if (centralities.get(j) > currentMax) {
                    currentIndex = j;
                    currentMax = centralities.get(j);
                }
            }

            // Set the value at the i-th index in `leastCentral` to the i-th smallest
            // value in `centralities` and then set the i-th smallest value in
            // `centralities` to the largest Integer value so it's not re-selected.
            mostCentral[i] = currentIndex;
            centralities.set(currentIndex, Integer.MIN_VALUE);
        }
        return mostCentral;
    }


    /**
     * Selects the "bottom `n`" central nodes in a Network given a centrality metric.
     * @param  metric The centrality metric that will be used to determine the
     *                centrality of nodes.
     * @param  n      The number of nodes to select based on centrality.
     * @return        An array of Integers that contains the indices of the least
     *                central nodes in the Network. The order of the indices matters
     *                as they are sorted by least central nodes to most central nodes.
     */
    public Integer[] leastCentralNodes(Centrality metric, int n) {
        ArrayList<Integer> centralities = new ArrayList(Arrays.asList(getCentralities(metric)));
        Integer[] leastCentral = new Integer[n];

        // Find the i-th most central node for each iteration.
        for (int i = 0; i < n; i++) {
            // Set the current minimum value, at first, to the maximum Integer
            // value. This guarantees that the first element will be selected to
            // maintain simple logic. Then initialize the current index to -1.
            Integer currentMin = Integer.MAX_VALUE;
            int currentIndex = -1;

            // Iterate through each centrality in `centralities`. Each iteration
            // through the loop will find the smallest value in `centralities`.
            // Upon completion of the loop, the value of the smallest value will
            // be changed such that it is not redundantly selected.
            for (int j = 0; j < centralities.size(); j++) {
                if (centralities.get(j) < currentMin) {
                    currentIndex = j;
                    currentMin = centralities.get(j);
                }
            }

            // Set the value at the i-th index in `leastCentral` to the i-th smallest
            // value in `centralities` and then set the i-th smallest value in
            // `centralities` to the largest Integer value so it's not re-selected.
            leastCentral[i] = currentIndex;
            centralities.set(currentIndex, Integer.MAX_VALUE);
        }
        return leastCentral;
    }


    /**
    * [addNode description]
    */
    public void addNode() {
        ArrayList<Integer> newLine = new ArrayList<Integer>();
        for(int i = 0; i < matrix.size(); i++){
            matrix.get(i).add(0);
            newLine.add(0);
        }
        newLine.add(0);
        matrix.add(newLine);
    }
    /**
    * [deleteNode description]
    * @param int nodeId [description]
    */
    public void deleteNode(int nodeID) {
        for(int i = 0; i < matrix.size(); i++){
            matrix.get(i).remove(nodeID); //remove from each other node's adjacency
        }
        matrix.remove(nodeID); //remove the node itself
    }

    /**
    * [addEdge description]
    * @param int source [description]
    * @param int dest [description]
    */
    public void addEdge(int source, int dest) {
        matrix.get(source).set(dest, 1);
    }

    /**
    * [deleteEdge description]
    * @param int source [description]
    * @param int dest [description]
    */
    public void deleteEdge(int source, int dest) {
        matrix.get(source).set(dest, 0);
    }

    // Accessor and mutator methods.
    public int getID() {
        return networkID;
    }
    public int getCount() {
        return count;
    }
    public ArrayList<ArrayList<Integer>> getMatrix() {
        return matrix;
    }
    public int getNumOfNodes() {
        return matrix.size();
    }

    /**
     * Gets the 2D array representation of Network.
     * @return Primitive two-dimensional array of Integer values that represent
     *         the Network.
     */
    public Integer[][] getArrayMatrix() {
        Integer[][] array = new Integer[matrix.size()][];
        for (int i = 0; i < matrix.size(); i++) {
            ArrayList<Integer> row = matrix.get(i);
            array[i] = row.toArray(new Integer[row.size()]);
        }
        return array;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ArrayList<Integer> node : matrix) {
            for (Integer edge : node) {
                sb.append(edge + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // TODO: Add methods that provide support for our class and the EJML class,
    // such as a getEJMLMatrix method.
}
