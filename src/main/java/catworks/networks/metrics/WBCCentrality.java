package catworks.networks.metrics;

import catworks.networks.*;

public class WBCCentrality implements InterdependentCentrality{

  /**
  * Return the Weighted Boundary Closeness Centarlities of a given idn
  * @param idn the IDN for which we find the getCentralities
  */
  public double[] getCentralities(IDN idn){
    double[] centralities = new double[idn.getNumOfNodes()];
    int offset = 0;
    ClosenessCentrality cloCent = new ClosenessCentrality();
    for(int i = 0; i < idn.getNumOfNetworks(); i++){
      Network net = idn.getNetwork(i).clone();
      int count = net.getCount();
      //int[][] matrix = net.getIntArrayMatrix();
      for(int j = 0 ; j< idn.getNumOfInterEdges(); j++){
        InterEdge ie = idn.getInterEdge(j);
        if(ie.getSourceNetwork() == i){
          net.addNode();
          net.addEdge(ie.getSourceNode(),net.getCount()); //make an edge from the source to the recently added node
        }
      }
      int[][] mat = net.getIntArrayMatrix();
      double[] ithCents = cloCent.getCentralities(mat);
      System.arraycopy(ithCents,0,centralities,offset,count);
      offset += count;
    }
    return centralities;
  }

  public int type(){
    return InterdependentCentrality.WBCCentrality;
  }
}
