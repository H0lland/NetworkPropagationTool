package catworks.networks.metrics;

public interface Centrality {

    <E extends Number> E[] getCentralities(Integer[][] matrix);

}
