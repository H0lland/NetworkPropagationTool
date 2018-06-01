package catworks.networks.metrics;

public abstract class Centrality extends AbstractCentrality {

    public abstract double[] getCentralities(int[][] matrix);
    public abstract int type();

}
