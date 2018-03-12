package catworks;

import java.util.ArrayList;

/**
  * Note: I changed the indices from longs to ints to work with ArrayLists, whose
  * specifications do not allow long-based indexing.  I will make this change
  * in other classes as well
  */
public class Network extends AbstractNetwork {

    private static int count;
    private int networkID;
    private ArrayList<ArrayList<Integer>> matrix;

    public Network() {
        networkID = count++;
    }

    public Network(ArrayList<ArrayList<Integer>> adjacencyMatrix){
      matrix = new ArrayList<ArrayList<Integer>>(adjacencyMatrix);
      networkID = count++;
    }

    /**
    * [addNode description]
    */
    public void addNode() {
        ArrayList<Integer> newLine = new ArrayList<Integer>();
        for(int i=0; i < matrix.size(); i+=1){
          matrix.get(i).add(0);
          newLine.add(0);
        }
        newLine.add(0);
        matrix.add(newLine);

        return;
    }
    /**
    * [deleteNode description]
    * @param int nodeId [description]
    */
    public void deleteNode(int nodeID) {
        for(int i=0; i < matrix.size(); i+=1){
          matrix.get(i).remove(nodeID); //remove from each other node's adjacency
        }
        matrix.remove(nodeID); //remove the node itself
        return;
    }

    /**
    * [addEdge description]
    * @param int source [description]
    * @param int dest [description]
    */
    public void addEdge(int source, int dest) {
      matrix.get(source).set(dest,1);
      return;
    }

    /**
    * [deleteEdge description]
    * @param int source [description]
    * @param int dest [description]
    */
    public void deleteEdge(int source, int dest) {
      matrix.get(source).set(dest,0);
      return;
    }

    // Accessor and mutator methods.
    public int getID() { return networkID; }
    public int getCount() { return count; }
    public ArrayList<ArrayList<Integer>> getMatrix() {return matrix; }
}
