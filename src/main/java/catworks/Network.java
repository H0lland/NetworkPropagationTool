package catworks;

public class Network extends AbstractNetwork {

    private static int count;
    private int networkID;
    ArrayList<ArrayList<int>> matix;

    public Network() {
        networkID = count++;
    }

    /**
    * [addNode description]
    */
    public void addNode() {
        return; // TODO: Implement.
    }
    /**
    * [deleteNode description]
    * @param long nodeId [description]
    */
    public void deleteNode(long nodeID) {
        return; // TODO: Implement method.
    }

    /**
    * [addEdge description]
    * @param long source [description]
    * @param long dest [description]
    */
    public void addEdge(long source, long dest) {
        return; // TODO: Implement method.
    }

    /**
    * [deleteEdge description]
    * @param long source [description]
    * @param long dest [description]
    */
    public void deleteEdge(long source, long dest) {
        return; // TODO: Implement method.
    }

    // Accessor and mutator methods.
    public int getID() { return networkID; }
    public int getCount() { return count; }
    public ArrayList<ArrayList<int>> getMatrix() {return matrix; }
}
