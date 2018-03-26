package catworks;

import java.util.ArrayList;
import java.lang.System.*;

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

    public IDN(ArrayList<Network> nets){
      networks = nets;
      interEdges = new ArrayList<InterEdge>();
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
    public Network bridge(){
      int offset = 0;
      int size = 0;
      int [] sizesUp = new int[networks.size()];
      for(int h = 0; h < networks.size(); h += 1){ // get total size for simplicity sake
        sizesUp[h] = size;
        size += networks.get(h).getMatrix().size();
      }
      int [] [] rtn = new int [size] [size];  //create a size by size array for the return Value
      for(int i = 0; i < networks.size(); i += 1){
        int [] [] matrix = networks.get(i).getIntArrayMatrix();
        int len = matrix.length;
        for(int j = 0; j < len; j += 1){
          System.arraycopy(matrix[j],0,rtn[offset+j],offset,len);
        }
        offset += len;
      }
      for(int k = 0; k < interEdges.size(); k += 1){
        InterEdge curr = interEdges.get(k);
        int sourceOff = sizesUp[curr.networkID];
        int destOff = sizesUp[curr.destNetworkID];
        rtn[sourceOff + curr.sourceNodeID][destOff + curr.destNodeID] = 1;
      }
      Network bridged = new Network(rtn);
      return bridged;
    }
}
