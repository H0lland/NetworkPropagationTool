package catworks;

import org.ejml.simple.SimpleMatrix;

/**
 * The following abstract class will serve as the development guideline for the
 * Network and IDN (Interdependent Network) classes.
 */
abstract class AbstractNetwork {

    // private HashMap<Node> nodes;
    // private HashMap<E> features;
    private int id;

    abstract void addNode();
    abstract void deleteNode();
    abstract void addEdge(long i, long j);
    abstract void deleteEdge(long i, long j);
    // abstract short[][] getMatrix();

    /**
     * The following methods are to be implemented in future iterations of the
     * project. For now, we will ignore their implementation until the issues
     * of CentralityMetric and Phenomena become more prevalent in terms of
     * development process.
     */
    // abstract SimpleMatrix getCentralNodes(int n);
    // protected abstract AbstractNetwork propogate();
    // abstract SimpleMatrix calculateCentrality(Centrality centrality);
}
