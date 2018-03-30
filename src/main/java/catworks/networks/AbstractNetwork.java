package catworks.networks;

import catworks.networks.metrics.*;

/**
 * The following abstract class will serve as the development guideline for the
 * Network and IDN (Interdependent Network) classes.
 */
abstract class AbstractNetwork {

    // private HashMap<Node> nodes;
    // private HashMap<E> features;
    private int id;

    abstract void addNode();
    abstract void deleteNode(int nodeID);
    abstract void addEdge(int source, int dest);
    abstract void deleteEdge(int source, int dest);
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
