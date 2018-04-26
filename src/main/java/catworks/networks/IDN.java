package catworks.networks;

import java.util.ArrayList;
import java.util.Random;

/**
 * Note: I removed the requirement that IDN extends AbstractNetwork. We need to
 * meet to elaborate as to why this is. The nature of adding nodes, deleting nodes,
 * etc. is inherently different because IDN needs to consider network IDs, whereas
 * a singular Network need not consider such a specification.
 */
public class IDN extends AbstractNetwork {

    private ArrayList<InterEdge> interEdges;
    private ArrayList<Network>   networks;
    private double               interP;
    private String               token;

    // private static final double INTER_EDGE_P = 10.0/600;

    public IDN() {
        interEdges = new ArrayList<InterEdge>();
        networks = new ArrayList<Network>();
        token = "";
    }

    public IDN(Network... nets) {
        networks = new ArrayList<Network>();
        interEdges = new ArrayList<InterEdge>();
        for (Network net : nets) {
            networks.add(net);
        }
        token = "";
    }

    public IDN(ArrayList<Network> nets){
        networks = nets;
        interEdges = new ArrayList<InterEdge>();
        token = "";
    }


    /**
     * Initialize new set of inter-edges between networks in IDN. The number of
     * inter-edges will be `interP * numOfNodes`.
     * @param interP [description]
     */
    public void randomInterEdges(double interP) {
        this.interP = interP;
        InterEdge newEdge;
        int n = (int) (interP * getNumOfNodes() + 0.5);
        int i = 0;
        while (i < n) {
            // Make a random inter-edge by first selecting the source network and
            // the dest network.
            int srcNet  = (int) (Math.random() * getNumOfNetworks());
            int destNet = (int) (Math.random() * getNumOfNetworks());
            while (srcNet == destNet) destNet = (int) (Math.random() * getNumOfNetworks());

            // Next, select random nodes in each network.
            int srcNode  = (int) (Math.random() * networks.get(srcNet).getNumOfNodes());
            int destNode = (int) (Math.random() * networks.get(destNet).getNumOfNodes());

            // Create new inter-edge and add to array of inter-edges.
            newEdge = new InterEdge(srcNet, srcNode, destNet, destNode);
            if (interEdges.contains(newEdge) == false)  {
                interEdges.add(newEdge);
                i++;
            }
        }
    }


    public int numberOfInterEdges() {
        return interEdges.size();
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
    public void addEdge(int networkID, int src, int dest) {
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
    public void deleteEdge(int networkID, int src, int dest) {
        for (Network network : networks) {
            if (network.getID() == networkID)
                network.deleteEdge(src, dest);
        }
    }


    /**
      * [addInterEdge]
      * @param InterEdge connection [description]
    **/
    public void addInterEdge(InterEdge connection){
      interEdges.add(connection);
    }


    /**
      * [bridge description]
    */
    public Network bridge() {
        int offset = 0;
        int size = 0;
        int [] sizesUp = new int[networks.size()];
        for (int h = 0; h < networks.size(); h += 1){ // get total size for simplicity sake
            sizesUp[h] = size;
            size += networks.get(h).getMatrix().size();
        }
        int[][] rtn = new int[size][size];  //create a size by size array for the return Value
        for (int i = 0; i < networks.size(); i++){
            int[][] matrix = networks.get(i).getIntArrayMatrix();
            int len = matrix.length;
            for (int j = 0; j < len; j += 1){
                System.arraycopy(matrix[j],0,rtn[offset+j],offset,len);
            }
            offset += len;
        }
        for (int k = 0; k < interEdges.size(); k++){
            InterEdge curr = interEdges.get(k);
            int sourceOff = sizesUp[curr.networkID];
            int destOff = sizesUp[curr.destNetworkID];
            rtn[sourceOff + curr.sourceNodeID][destOff + curr.destNodeID] = 1;
        }
        Network bridged = new Network(rtn);
        return bridged;
    }

    public void rewire(){
        for (int h = 0; h < networks.size(); h += 1) { // rewire all networks in the IDN
            networks.get(h).rewire();
        }
        for (int k = 0; k < interEdges.size(); k += 1) { // rewire the dest node of each interedge
            int dest = interEdges.get(k).destNetworkID;
            int size = networks.get(dest).getNumOfNodes(); // get the size of target network
            int newNeigh = new Random().nextInt(size + 1); // get random node in target network
            interEdges.get(k).changeDestNode(newNeigh); // change target node
        }
    }


    public void setToken(String token) { this.token = token; }
    public String getToken() { return token; }

    public Network getNetwork(int i) {
        return networks.get(i);
    }


    public int getNumOfNetworks() {
        return networks.size();
    }


    public int getNumOfNodes() {
        int total = 0;
        for (Network network : networks) {
            total += network.getNumOfNodes();
        }
        return total;
    }


    public void regenerate() {
        for (Network network : networks) {
            network.regenerate();
        }
        interEdges = new ArrayList<InterEdge>();
        randomInterEdges(interP); //INTER_EDGE_P);
    }
}
