package catworks.networks.metrics;

public interface Centrality {

    final int BETWEENNESS = 0;
    final int CLOSENESS   = 1;
    final int DEGREE      = 2;
    final int EIGENVECTOR = 3;
    final int MYCLOSENESS = 4;
    final int MYBETWEENNESS = 5;

    <E extends Number> E[] getCentralities(Integer[][] matrix);
    int type();

}
