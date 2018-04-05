package catworks.networks;

import catworks.networks.metrics.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
  * Note: I changed the indices from longs to ints to work with ArrayLists, whose
  * specifications do not allow long-based indexing.  I will make this change
  * in other classes as well
  */
public class Network extends AbstractNetwork {

    private static int count;
    private int networkID;
    private int flag;
    protected ArrayList<ArrayList<Integer>> matrix;

    /**
     * No-arg constructor that creates an empty Network.
     */
    public Network() {
        networkID = count++;
        matrix = new ArrayList<ArrayList<Integer>>();
    }


    /**
     * One-arg Network constructor, given an int[][].
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
     * One-arg Network constructor, given an Integer[][].
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
     * One-arg Network constructor, given an ArrayList<ArrayList<Integer>>.
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
    public <E extends Number> E[] getCentralities(Centrality metric) {
        Integer[][] arrayMatrix = getArrayMatrix();
        return metric.getCentralities(arrayMatrix);
    }

    /**
     * [getNetworkType description]
     * @param  Centrality metric        [description]
     * @return            [description]
     */



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
        ArrayList<Number> centralities = new ArrayList(Arrays.asList((Number[]) getCentralities(metric)));
        Integer[] mostCentral = new Integer[n];

        for (int i = 0; i < n; i++) {
            // Set the current minimum value, at first, to the maximum Integer
            // value. This guarantees that the first element will be selected to
            // maintain simple logic. Then initialize the current index to -1.
            Number currentMax = Integer.MIN_VALUE;
            int currentIndex = -1;

            // Iterate through each centrality in `centralities`. Each iteration
            // through the loop will find the smallest value in `centralities`.
            // Upon completion of the loop, the value of the smallest value will
            // be changed such that it is not redundantly selected.
            for (int j = 0; j < centralities.size(); j++) {
                if (centralities.get(j).doubleValue() > currentMax.doubleValue()) {
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
        ArrayList<Number> centralities = new ArrayList(Arrays.asList((Number[]) getCentralities(metric)));
        Integer[] leastCentral = new Integer[n];

        // Find the i-th most central node for each iteration.
        for (int i = 0; i < n; i++) {
            // Set the current minimum value, at first, to the maximum Integer
            // value. This guarantees that the first element will be selected to
            // maintain simple logic. Then initialize the current index to -1.
            Number currentMin = Integer.MAX_VALUE;
            int currentIndex = -1;

            // Iterate through each centrality in `centralities`. Each iteration
            // through the loop will find the smallest value in `centralities`.
            // Upon completion of the loop, the value of the smallest value will
            // be changed such that it is not redundantly selected.
            for (int j = 0; j < centralities.size(); j++) {
                if (centralities.get(j).doubleValue() < currentMin.doubleValue()) {
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

    public void regenerate() {
        return;
    }

    public int neighbors(int nodeID){
      int sum = 0;
      int nodes = this.getNumOfNodes();
      int[][] mat = this.getIntArrayMatrix();
      for(int j = 0; j <= nodes; j++){
        if(mat[nodeID][j]!=0){
          sum++;
        }
      }
      return sum;
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

    public int[][] getIntArrayMatrix(){
      int size = this.getNumOfNodes();
      int [] [] rtn = new int [size][size];
      for(int i = 0; i < size; i += 1){
        for(int j = 0; j < size; j += 1){
          rtn[i][j] = this.matrix.get(i).get(j);
        }
      }
      return rtn;
    }

    public void rewire(){
      int [][] mat = this.getIntArrayMatrix(); //make a new int matrix
      int nodes = this.getNumOfNodes();
      int [] blank = new int[nodes]; //make a blank array
      for(int i = 0; i < nodes; i++){
        int neighs = this.neighbors(i); // get the number of neighbors for node i
        System.arraycopy(blank,0,mat[i],0,nodes); // make the adjacency line for node i 0s
        while(neighs>0){
          int dest = new Random().nextInt(nodes+1); // find new endpoint for new edge
          if(mat[i][dest] == 0 && i != dest){ // check that there are no self-loops and that i only links to another node once
            mat[i][dest] = 1; // connect to node dest
            neighs--; //reduce number of neighbors to connect to
          }
        }
      }
      matrix = new ArrayList<ArrayList<Integer>>();
      ArrayList<Integer> inner;
      for(int i = 0; i < mat.length; i++){
          inner = new ArrayList<Integer>();
          for(int j = 0; j< mat[i].length; j++){
              inner.add(mat[i][j]);
          }
          matrix.add(inner);
      }
      this.matrix = matrix;
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
