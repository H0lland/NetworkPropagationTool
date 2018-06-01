package catworks.networks;

import java.util.ArrayList;
import java.util.Random;

import catworks.networks.metrics.InterdependentCentrality;
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
     * Calculate the centralities of each node in the Interdependent Network and return 
     * these values as an array of double. Order of this array matters. The element at the
     * 0th index is the centrality of the 0th node and so on.
     * @param  metric The centrality metric that will be used to calculate centrality.
     * @return        Array of Integers with each i-th element being the centrality
     *                of the i-th node in the Network.
     */
    public double[] getCentralities(InterdependentCentrality metric) {
        return metric.getCentralities(this);
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
    public int[] mostCentralNodes(InterdependentCentrality metric, int n) {
        double[] centralities = getCentralities(metric);
        int[] mostCentral = new int[n];

        for (int i = 0; i < n; i++) {
            // Set the current minimum value, at first, to the maximum Integer
            // value. This guarantees that the first element will be selected to
            // maintain simple logic. Then initialize the current index to -1.
            double currentMax = Double.NEGATIVE_INFINITY;
            int currentIndex = -1;

            // Iterate through each centrality in `centralities`. Each iteration
            // through the loop will find the smallest value in `centralities`.
            // Upon completion of the loop, the value of the smallest value will
            // be changed such that it is not redundantly selected.
            for (int j = 0; j < centralities.length; j++) {
                if (centralities[j] > currentMax) {
                    currentIndex = j;
                    currentMax = centralities[j];
                }
            }

            // Set the value at the i-th index in `leastCentral` to the i-th smallest
            // value in `centralities` and then set the i-th smallest value in
            // `centralities` to the largest Integer value so it's not re-selected.
            mostCentral[i] = currentIndex;
            centralities[currentIndex] = Double.NEGATIVE_INFINITY;
        }
        return mostCentral;
    }


    /**
     * Initialize new set of inter-edges between networks in IDN. The number of
     * inter-edges will be `interP * numOfNodes`.
     *
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


    /**
     * Initialize a new set of inter-edges, however, you now have the option to
     * 'halve' the probability. When you have 'halve' this process, the interP probability
     * is halved. The reason for this is that two inter-edges will be made, one from one
     * network C to some other network P and then one from the network P to network C.
     *
     * @param interP The probability of there being an inter-edge between networks.
     * @param halve  Boolean variable that either allows or denies the halving process to ensue.
     */
    public void randomInterEdges(double interP, boolean halve) {
        this.interP = interP;
        if (halve) {
            interP /= 2;
            int n = (int) (interP * getNumOfNodes() + 0.5);
            int i = 0;
            while (i < n) {
                // Make a random inter-edge by first selecting the source network and
                // the dest network.
                int srcNet  = (int) (Math.random() * getNumOfNetworks());
                int destNet = (int) (Math.random() * getNumOfNetworks());
                while (srcNet == destNet) destNet = (int) (Math.random() * getNumOfNetworks());

                // Select random nodes in each network for the first inter-edge.
                int srcNode1  = (int) (Math.random() * networks.get(srcNet).getNumOfNodes());
                int destNode1 = (int) (Math.random() * networks.get(destNet).getNumOfNodes());
                InterEdge interEdge1 = new InterEdge(srcNet, srcNode1, destNet, destNode1);

                // Select random nodes in each network for the second inter-edge.
                int srcNode2  = (int) (Math.random() * networks.get(destNet).getNumOfNodes());
                int destNode2 = (int) (Math.random() * networks.get(srcNet).getNumOfNodes());
                InterEdge interEdge2 = new InterEdge(destNet, srcNode2, srcNet, destNode2);

                // Create new inter-edge and add to array of inter-edges.
                if (!interEdges.contains(interEdge1) && !interEdges.contains(interEdge2)) {
                    interEdges.add(interEdge1);
                    interEdges.add(interEdge2);
                    i++;
                }
            }
        } else {
            randomInterEdges(interP);
        }
    }


    public int getNumOfInterEdges() {
        return interEdges.size();
    }

    public int[][] getInterEdgeMatrix(int i, int j) {
        if (i < 0 || i >= interEdges.size() || j < 0 || j >= interEdges.size()) {
            throw new IllegalArgumentException("Network ID out of bounds.");
        }

        int n1 = networks.get(i).getNumOfNodes();
        int n2 = networks.get(j).getNumOfNodes();
        int[][] adj = new int[n1][n2];
        for (InterEdge edge : interEdges) {
            if (i == edge.networkID() && j == edge.destNetworkID()) {
                int x = edge.sourceNodeID();
                int y = edge.destNodeID();
                adj[x][y] = 1;
            }
        }
        return adj;
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

    public InterEdge getInterEdge(int i){
      return interEdges.get(i);
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
        randomInterEdges(interP, true);
    }
}
