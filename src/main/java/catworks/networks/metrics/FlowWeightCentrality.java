package catworks.networks.metrics;

public class FlowWeightCentrality extends Centrality {
    /**
    * [getCentralities]
    * @param matrix Adjacency representation of network to calculate Centrality
    * @return An array with each i-th element being the centrality of the
    *         i-th node in the network that is being measured.
    */
    public double [] getCentralities(int[][] matrix){
      int n = matrix.length;
      double[] centralities = new double[n];
      for(int i =0; i<n; i++){
        double flow = 0;
        for(int j=0; j<n; j++){
          flow += (matrix[i][j] + matrix[j][i]);
        }
        centralities[i] = flow;
      }
      return centralities;
    }

    /*
    * [type description]
    * @return the type
    */
    public int type(){
      return AbstractCentrality.FLOW_WEIGHT;
    }

    @Override
    public string toString(){
      return "Flow Weight"
    }
}
