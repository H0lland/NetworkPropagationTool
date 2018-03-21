package catworks;

public class DegreeCentrality implements Centrality {

    /**
     * [getCentralities description]
     * @param  matrix Adjacency matrix representation of network to calculate centrality.
     * @return        An array with each i-th element being the centrality of the
     *                i-th node in the network that is being measured for centrality.
     */
    public Integer[] getCentralities(Integer[][] matrix) {
        int n = matrix.length;
        Integer[] centralities = new Integer[n];

		for (int i = 0; i < n; i++) {
			int degree = 0;
			for (int j = 0; j < n; j++) {
				degree += (matrix[i][j] + matrix[j][i]);
			}
			centralities[i] = degree;
		}
		return centralities;
    }

}
