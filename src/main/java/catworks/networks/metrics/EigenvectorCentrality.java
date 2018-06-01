package catworks.networks.metrics;

public class EigenvectorCentrality extends Centrality {

    private static final int NUM_OF_ITERATIONS = 100;

    /*
     * Gets the centrality specifically for finding the Eigenvectors of the matrix
     * @param  Integer[][] matrix        original matrix
     * @return             eigenvector values
     */
    public double[] getCentralities(int[][] matrix) {
        final int N = matrix.length;
        double[] centralities = new double[N];
        for (int i = 0; i < N; i++) 
            centralities[i] = 1.0;

        // 2. Loop through the number of iterations to calculate eigenvector centralities for
        //    each node.
        for (int iteration = 0; iteration < NUM_OF_ITERATIONS; iteration++) {
            double[] w = new double[N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (i == j) continue;
                    if (matrix[i][j] == 1) {
                        w[j] = w[j] + centralities[i];
                    }
                }
            }
            centralities = w;
        }

        // Normalize and return centralities array.
        normalize(centralities);
        return centralities;
    }

    private void normalize(double[] arr) {
        double sum = sum(arr);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i] / sum;
        }
    }

    private double sum(double[] arr) {
        double sum = 0.0;
        for (double d : arr) sum += d;
        return sum;
    }

    /**
     * [type description]
     * @return [description]
     */
    public int type() {
        return Centrality.EIGENVECTOR;
    }

    @Override
    public String toString() {
        return "Eigenvector";
    }

}