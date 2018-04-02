package catworks.networks;

public class InterEdge {
    int networkID;
    int sourceNodeID;
    int destNetworkID;
    int destNodeID;

    public InterEdge(int networkID, int sourceNodeID, int destNetworkID, int destNodeID) {
        this.networkID = networkID;
        this.sourceNodeID = sourceNodeID;
        this.destNetworkID = destNetworkID;
        this.destNodeID = destNodeID;
    }

    @Override public String toString() {
        return "(" + networkID + ":" + sourceNodeID + " -> " + destNetworkID + ":" + destNodeID + ")";
    }
}
