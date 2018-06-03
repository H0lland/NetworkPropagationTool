package catworks.simulations;

// Project import statements.
import catworks.networks.metrics.*;
import catworks.phenomena.*;

// Additional import statements.
import java.io.IOException;

/**
 *
 */
public abstract class Simulation {

    protected static final int COLUMNS = 21;
    protected static final String[] HEADER = { "Time Step", "Number of Nodes", "Number of Immune", "Infected - Betweenness (AVG)", "Infected - Betweenness (MIN)", "Infected - Betweenness (MAX)", "Infected - Closeness (AVG)", "Infected - Closeness (MIN)", "Infected - Closeness (MAX)", "Infected - Degree (AVG)", "Infected - Degree (MIN)", "Infected - Degree (MAX)", "Infected - Eigenvector (AVG)", "Infected - Eigenvector (MIN)", "Infected - Eigenvector (MAX)", "Infected - Path Degree (AVG)", "Infected - Path Degree (MIN)", "Infected - Path Degree (MAX)", "Infected - Weighted Boundary (AVG)", "Infected - Weighted Boundary (MIN)", "Infected - Weighted Boundary (MAX)", };
    
    protected static final Centrality[] CENTRALITIES = { new BetweennessCentrality(), new ClosenessCentrality(), new DegreeCentrality(), new EigenvectorCentrality() };
    protected static final InterdependentCentrality[] INTERDEPENDENT_CENTRALITIES = { new PathDegreeCentrality(), new IdnCentrality1() };

    protected static final int TIMESTAMP_COL    = 0;
    protected static final int NODE_COUNT_COL   = 1;
    protected static final int IMMUNE_COUNT_COL = 2;

    protected static final int BETWEENNESS_AVG_COL = 3;
    protected static final int BETWEENNESS_MIN_COL = 4;
    protected static final int BETWEENNESS_MAX_COL = 5;

    protected static final int CLOSENESS_AVG_COL = 6;
    protected static final int CLOSENESS_MIN_COL = 7;
    protected static final int CLOSENESS_MAX_COL = 8;

    protected static final int DEGREE_AVG_COL = 9;
    protected static final int DEGREE_MIN_COL = 10;
    protected static final int DEGREE_MAX_COL = 11;

    protected static final int EIGENVECTOR_AVG_COL = 12;
    protected static final int EIGENVECTOR_MIN_COL = 13;
    protected static final int EIGENVECTOR_MAX_COL = 14;

    protected static final int PATH_DEGREE_AVG_COL = 15;
    protected static final int PATH_DEGREE_MIN_COL = 16;
    protected static final int PATH_DEGREE_MAX_COL = 17;

    protected static final int WBC_AVG_COL = 18;
    protected static final int WBC_MIN_COL = 19;
    protected static final int WBC_MAX_COL = 20;

    // Instance variables for simulations.
    // protected Network    network;
    protected Phenomena  phenomena;
    protected int        timeSteps;
    protected double     immuneFraction;
    protected double     infectFraction;
    protected int        immuneCount;
    protected static int simulationID = 0;
    protected int runID = 0;

    /**
     * [run description]
     * @param  n                        [description]
     * @return                          [description]
     * @throws Exception                [description]
     * @throws IOException              [description]
     * @throws IllegalArgumentException [description]
     */
    public abstract Object[][] run(int n) throws Exception, IOException, IllegalArgumentException;


    /**
     * [sumData description]
     * @param data  [description]
     * @param data2 [description]
     */
    protected void sumData(double[][] data, double[][] additive) {
        for (int i = 0; i < timeSteps; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                data[i][j] =  data[i][j] + additive[i][j];
            }
        }
    }

    /**
     * [log description]
     * @param string [description]
     */
    protected void log(String string) {
        System.out.println("[" + new java.util.Date() + "]$ " + string);
    }



}
