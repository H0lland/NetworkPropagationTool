package catworks.networks.metrics;

public abstract class AbstractCentrality {

    public final static int BETWEENNESS = 0;
    public final static int CLOSENESS   = 1;
    public final static int DEGREE      = 2;
    public final static int EIGENVECTOR = 3;
    public final static int PATH_DEGREE = 4;
    public final static int PROPOSAL = 5;
    public final static int WEIGHTED_BOUNDARY = 6;
    public final static int FLOW_WEIGHT = 7;
    public final static int FLOW_UTIL = 8;

    public abstract int type();

}
