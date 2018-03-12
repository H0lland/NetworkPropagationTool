package catworks;

import java.util.ArrayList;

/**
 * Note: I removed the requirement that IDN extends AbstractNetwork. We need to
 * meet to elaborate as to why this is. The nature of adding nodes, deleting nodes,
 * etc. is inherently different because IDN needs to consider network IDs, whereas
 * a singular Network need not consider such a specification.
 */
public class IDN {

    ArrayList<InterEdge> interEdges;
    ArrayList<Network>   networks;

    public IDN() {
        interEdges = new ArrayList<InterEdge>();
        networks = new ArrayList<Network>();
    }


    /**
     * [addNode description]
     * @param int networkID [description]
     */
    public void addNode(int networkID) {
        for (Network network : networks) {
            if (network.getID() == networkID)
                network.addNode();
        }
    }


    /**
     * [deleteNode description]
     * @param int networkID [description]
     * @param int nodeID    [description]
     */
    public void deleteNode(int networkID, int nodeID) {
        for (Network network : networks) {
            if (network.getID() == networkID)
                network.deleteNode(nodeID);
        }
    }


    /**
     * [addEdge description]
     * @param int  networkID [description]
     * @param long src       [description]
     * @param long dest      [description]
     */
    public void addEdge(int networkID, long src, long dest) {
        for (Network network : networks) {
            if (network.getID() == networkID)
                network.addEdge(src, dest);
        }
    }


    /**
     * [deleteEdge description]
     * @param long src  [description]
     * @param long dest [description]
     */
    public void deleteEdge(int networkID, long src, long dest) {
        for (Network network : networks) {
            if (network.getID() == networkID)
                network.deleteEdge(src, dest);
        }
    }

}
