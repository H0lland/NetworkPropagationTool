package catworks.networks;

/**
 * The following abstract class will serve as the development guideline for the
 * Network and IDN (Interdependent Network) classes.
 */
public abstract class AbstractNetwork {

    public final static boolean   DIRECTED = true;
    public final static boolean UNDIRECTED = false;

    abstract int  getNumOfNodes();
    abstract void regenerate();
    abstract void rewire();

}
