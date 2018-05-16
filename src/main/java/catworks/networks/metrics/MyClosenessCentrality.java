package catworks.networks.metrics;

public class ClosenessCentrality implements Centrality {

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
