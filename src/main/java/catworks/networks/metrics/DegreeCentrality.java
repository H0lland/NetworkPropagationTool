package catworks.networks.metrics;

public class DegreeCentrality extends Centrality {

    /**
     * [getCentralities description]
     * @param  matrix Adjacency matrix representation of network to calculate centrality.
     * @return        An array with each i-th element being the centrality of the
     *                i-th node in the network that is being measured for centrality.
     */
    public double[] getCentralities(int[][] matrix) {
        int n = matrix.length;
        double[] centralities = new double[n];

		for (int i = 0; i < n; i++) {
			double degree = 0;
			for (int j = 0; j < n; j++) {
				if(matrix[i][j] > 0){
					degree += 1;
				}	
			}
			centralities[i] = degree;
		}
		return centralities;
    }

    /**
     * [type description]
     * @return [description]
     */
    public int type() {
        return AbstractCentrality.DEGREE;
    }

    @Override
    public String toString() {
        return "Degree";
    }

}
