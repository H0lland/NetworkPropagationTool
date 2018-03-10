package catworks;

public class Network extends AbstractNetwork {

    private static int count;
    private int networkID;

    public Network() {
        networkID = count++;
    }

    public void addNode() {
        return; // TODO: Implement.
    }

    public void deleteNode(int nodeID) {
        return; // TODO: Implement method.
    }

    public void addEdge(long source, long dest) {
        return; // TODO: Implement method.
    }

    public void deleteEdge(long source, long dest) {
        return; // TODO: Implement method.
    }

    // Accessor and mutator methods.
    public int getID() { return networkID; }
    public int getCount() { return count; }
}
