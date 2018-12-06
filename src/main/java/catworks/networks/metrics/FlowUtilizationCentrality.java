package catworks.networks.metrics;

public class FlowUtilizationCentrality extends Centrality {
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
        double inFlow = 0;
        double outFlow = 0;
        for(int j=0; j<n; j++){
          inFlow += matrix[j][i];
          outFlow += matrix[i][j];
        }
        centralities[i] = outFlow/inFlow;
        //1 is the maximum accepted centrality value
        if(centralities[i] > 1) centralities[1] = 1;
      }
      return centralities;
    }

    /*
    * [type description]
    * @return the type
    */
    public int type(){
      return AbstractCentrality.FLOW_UTIL;
    }

    @Override
    public string toString(){
      return "Flow Utilization"
    }
}
