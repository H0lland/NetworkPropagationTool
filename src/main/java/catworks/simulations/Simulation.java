package catworks.simulations;

// Project import statements.
import catworks.networks.*;
import catworks.networks.metrics.*;
import catworks.phenomena.*;

// Additional import statements.
import java.util.Arrays;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 *
 */
public abstract class Simulation {

    protected static final int COLUMNS = 7;
    protected static final String[] HEADER = { "Time Step", "Number of Nodes", "Number of Immune", "Infected - Betweenness", "Infected - Closeness", "Infected - Degree", "Infected - Eigenvector" };
    protected static final Centrality[] CENTRALITIES = { new BetweennessCentrality(), new ClosenessCentrality(), new DegreeCentrality(), new EigenvectorCentrality() };

    protected static final int TIMESTAMP_COL    = 0;
    protected static final int NODE_COUNT_COL   = 1;
    protected static final int IMMUNE_COUNT_COL = 2;
    protected static final int BETWEENNESS_COL  = 3;
    protected static final int CLOSENESS_COL    = 4;
    protected static final int DEGREE_COL       = 5;
    protected static final int EIGENVECTOR_COL  = 6;

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
    public    abstract Object[][] run(int n) throws Exception, IOException, IllegalArgumentException;

    /**
     * [run description]
     * @return [description]
     * @throws Exception     [description]
     * @throws IOException   [description]
     */
    protected abstract double[][] run() throws Exception, IOException;


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
