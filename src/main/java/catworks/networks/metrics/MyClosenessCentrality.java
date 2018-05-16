package catworks.networks.metrics;

public class MyClosenessCentrality implements Centrality {

public Double[] getCentralities(Integer[][] matrix) {

}

/**
 * [type description]
 * @return [description]
 */
public int type() {
    return Centrality.CLOSENESS;
}

@Override
public String toString() {
    return "closeness";
}

}
