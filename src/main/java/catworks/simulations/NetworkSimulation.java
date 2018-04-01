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
public class NetworkSimulation extends Simulation {

    private Network network;

    public NetworkSimulation(Network network, Phenomena phenomena, int timeSteps, double immuneFraction, double infectFraction) {
        this.network = network;
        this.phenomena = phenomena;
        this.timeSteps = timeSteps;
        this.immuneFraction = immuneFraction;
        this.infectFraction = infectFraction;

        runID = 0;
        immuneCount = (int) (network.getNumOfNodes() * immuneFraction);
        simulationID++;
    }


    /**
     * [run description]
     * @param  n           [description]
     * @return             [description]
     * @throws Exception   [description]
     * @throws IOException [description]
     */
    public Object[][] run(int n) throws Exception, IOException, IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException("`n` must be a positive number.");
        }

        // (1) Initialize `data` such that the rows are of type Integer,
        //     initialized to value 0.
        double[][] data = new double[timeSteps][COLUMNS];
        for (int i = 0; i < timeSteps; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                data[i][j] = new Integer(0);
            }
        }

        // (2) Go through the number of simulations and sum up the values between
        //     data sets.
        for (int simulationNum = 0; simulationNum < n; simulationNum++) {
            sumData(data, run());
            runID++;
        }

        // (3) Go through the final dataset and simply divide each Integer value
        //     by `n` to calculate the average.
        for (int i = 0; i < timeSteps; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                data[i][j] = data[i][j] / (double) n;
            }
        }

        // (4) Prepare the final dataset, `finalData`, which will include the header
        // row.
        Object[][] finalData = new Object[timeSteps+1][COLUMNS];
        for (int i = 0; i < COLUMNS;     i++) finalData[0][i] = HEADER[i];
        for (int i = 1; i < timeSteps+1; i++) {
            finalData[i][TIMESTAMP_COL]    = (Integer) ((int) data[i-1][TIMESTAMP_COL]);
            finalData[i][NODE_COUNT_COL]   = (Integer) ((int) data[i-1][NODE_COUNT_COL]);
            finalData[i][IMMUNE_COUNT_COL] = (Integer) ((int) data[i-1][IMMUNE_COUNT_COL]);
            finalData[i][BETWEENNESS_COL]  = (Double)  data[i-1][BETWEENNESS_COL];
            finalData[i][CLOSENESS_COL]    = (Double)  data[i-1][CLOSENESS_COL];
            finalData[i][DEGREE_COL]       = (Double)  data[i-1][DEGREE_COL];
            finalData[i][EIGENVECTOR_COL]  = (Double)  data[i-1][EIGENVECTOR_COL];
        }
        return finalData;
    }


    /**
     * [run description]
     * @return [description]
     * @throws Exception     [description]
     * @throws IOException   [description]
     */
    protected double[][] run() throws Exception, IOException {
        // Get the adjacency matrix of the network and declare `N` to be the number
        // of nodes in the network.
        network.regenerate();
        Integer[][] matrix = network.getArrayMatrix();
        final Integer N = network.getNumOfNodes();

        // Initialize the two-dimensional data set that will store the data that
        // will be used to make the .CSV output file.
        double[][] data = initializeData();



        for (Centrality metric : CENTRALITIES) {
            // Log the start of the simulation to the user.
            log("Starting Simulation " + runID + " -- " + metric);

            // Immunize and infect the appropriate nodes in the network and set that
            // as the initial state, or the state at time step 0.
            int[] initialState  = new int[N];
            immunize(initialState, network, metric);
            infect(initialState); // TODO: Test these methods later.

            // Propagate phenomena throughout the network and store the state data at
            // the end of each time step.
            int[] currentState = Arrays.copyOf(initialState, N);
            for (int t = 0; t < timeSteps; t++) {
                int index;
                switch (metric.type()) {
                    case Centrality.BETWEENNESS: index = BETWEENNESS_COL; break;
                    case Centrality.CLOSENESS:   index = CLOSENESS_COL;   break;
                    case Centrality.DEGREE:      index = DEGREE_COL;      break;
                    case Centrality.EIGENVECTOR: index = EIGENVECTOR_COL; break;
                    default: throw new Exception("Invalid Centrality metric type.");
                }

                // Store the number of infected nodes in `data` and then iterate
                // to the next state in the propagation process.
                data[t][IMMUNE_COUNT_COL] = immuneCount;
                data[t][index] = numberOfInfectedNodes(currentState);
                currentState = phenomena.propagate(matrix, currentState);
            }
            log("Ending Simulation " + runID + " -- " + metric);
        }
        return data;
    }


    /**
     * [initializeData description]
     * @return [description]
     */
    protected double[][] initializeData() {
        double[][] _data = new double[timeSteps][COLUMNS];


        // 1) Fill the 0th column with the appropriate timestamp.
        for (int t = 0; t < timeSteps; t++) {
            _data[t][TIMESTAMP_COL] = t;
        }

        // 2) Fill the 1th column with the number of nodes in the current network.
        for (int t = 0; t < timeSteps; t++) {
            _data[t][NODE_COUNT_COL] = network.getNumOfNodes();
        }

        return _data;
    }


    /**
     * [numberOfInfectedNodes description]
     * @param  state [description]
     * @return       [description]
     */
    private Integer numberOfInfectedNodes(int[] state) {
        Integer count = 0;
        for (int nodeState : state) {
            if (nodeState == Phenomena.AFFLICTED)
                count++;
        }
        return count;
    }


    /**
     * [immunize description]
     * @param state   [description]
     * @param network [description]
     * @param metric  [description]
     */
    private void immunize(int[] state, Network network, Centrality metric) {
        Integer[] immuneIndices = network.mostCentralNodes(metric, immuneCount);
        for (Integer immuneIndex : immuneIndices) {
            state[immuneIndex] = Phenomena.IMMUNE;
        }
    }


    /**
     * [infect description]
     * @param state [description]
     */
    private void infect(int[] state) {
        int i = 0, upperBound = state.length, randomIndex;
        int infectCount = (int) (state.length * infectFraction + 0.5);

        while (i < infectCount) {
            randomIndex = (int) (Math.random() * upperBound);
            if (state[randomIndex] == Phenomena.UNAFFLICTED) {
                state[randomIndex] =  Phenomena.AFFLICTED;
                i++;
            }
        }
    }

}
