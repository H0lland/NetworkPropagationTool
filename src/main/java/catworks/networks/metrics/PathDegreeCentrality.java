package catworks.networks.metrics;

import catworks.networks.IDN;
import catworks.networks.Network;

public class PathDegreeCentrality implements InterdependentCentrality {

    private IDN idn;


    /**
     * TODO: Look at the MatLab code that Khamfroush sent out earlier in the spring semester. Use that as a reference
     * to implement this centrality metric. The mathematical notation is a bit bloated and unclear.
     */
    public double[] getCentralities(IDN networks) {
        if (networks.getNumOfNetworks() != 2) {
            throw new IllegalArgumentException("Path-Degree centrality only works with interdependent networks consisting of TWO networks.");
        }
        idn = networks;
        
        final int N = idn.getNumOfNodes();
        final Network X = idn.getNetwork(0);
        final Network Y = idn.getNetwork(1);
        final int OFFSET = X.getNumOfNodes();

        double[] c = new double[N];

        // 1. Calculate the path-degree centrality values for nodes belonging to `X`.
        for (int v = 0; v < OFFSET; v++) {
            for (int s = 0; s < OFFSET; s++) {
                for (int t : borderY) {
                    if (v != s)
                        c[v] = c[v] + sigma(s, t, v);
                }
            }
        }

        // 2. Calculate the path-degree centrality values for nodes belonging to `Y`.
        for (int v = OFFSET; v < N; v++) {
            for (int s = OFFSET; s < N; s++) {
                for (int t : borderX) {
                    if (v != s)
                        c[v] = c[v] + sigma(s, t, v);
                }
            }
        }
    }

    private double length(int node) {
        double val = 0;
        return val;
    }

    public int type() {
        return -1;
    }

}