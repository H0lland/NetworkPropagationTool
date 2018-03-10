package catworks;

public class InterEdge {
    int networkID;
    int sourceNodeID;
    int destNodeID;

    public InterEdge(int networkID, int sourceNodeID, int destNodeID) {
        this.networkID = networkID;
        this.sourceNodeID = sourceNodeID;
        this.destNodeID = destNodeID;
    }

    @Override public String toString() {
        return "(" + networkID + ": " + sourceNodeID + " -> " + destNodeID + ")";
    }
}
