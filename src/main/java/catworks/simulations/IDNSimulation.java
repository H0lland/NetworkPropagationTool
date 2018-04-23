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
public class IDNSimulation extends Simulation {

    private IDN     networks;
    private Network bridgedNetwork;
    private boolean separateCentralities;

    private double[][] minimumAndMaximum;

    public IDNSimulation(IDN networks, Phenomena phenomena, int timeSteps, int immuneCount, double infectFraction, boolean separateCentralities) {
        this.networks = networks;
        this.phenomena = phenomena;
        this.timeSteps = timeSteps;
        this.immuneCount = immuneCount;
        this.infectFraction = infectFraction;
        this.separateCentralities = separateCentralities;

        runID = 0;
        simulationID++;
    }

    public IDNSimulation(IDN networks, Phenomena phenomena, int timeSteps, double immuneFraction, double infectFraction, boolean separateCentralities) {
        this.networks = networks;
        this.phenomena = phenomena;
        this.timeSteps = timeSteps;
        this.immuneFraction = immuneFraction;
        this.infectFraction = infectFraction;
        this.separateCentralities = separateCentralities;

        runID = 0;
        immuneCount = (int) (networks.getNumOfNodes() * immuneFraction);
        if (immuneCount % 2 != 0) immuneCount++;
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
        initMinimumAndMaximum();
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
                switch (j) {
                    case BETWEENNESS_MIN_COL: break;
                    case BETWEENNESS_MAX_COL: break;
                    case CLOSENESS_MIN_COL:   break;
                    case CLOSENESS_MAX_COL:   break;
                    case DEGREE_MIN_COL:      break;
                    case DEGREE_MAX_COL:      break;
                    case EIGENVECTOR_MIN_COL: break;
                    case EIGENVECTOR_MAX_COL: break;
                    default:
                        data[i][j] = data[i][j] / (double) n;
                        break;
                }
            }
        }

        // (4) Prepare the final dataset, `finalData`, which will include the header
        // row. TODO: Figure out how to do minimum and maximum...
        Object[][] finalData = new Object[timeSteps+1][COLUMNS];
        for (int i = 0; i < COLUMNS;     i++) finalData[0][i] = HEADER[i];
        for (int i = 1; i < timeSteps+1; i++) {
            finalData[i][TIMESTAMP_COL]    = (Integer) ((int) data[i-1][TIMESTAMP_COL]);
            finalData[i][NODE_COUNT_COL]   = (Integer) ((int) data[i-1][NODE_COUNT_COL]);
            finalData[i][IMMUNE_COUNT_COL] = (Integer) ((int) data[i-1][IMMUNE_COUNT_COL]);

            finalData[i][BETWEENNESS_AVG_COL]  = (Double) data[i-1][BETWEENNESS_AVG_COL];
            finalData[i][BETWEENNESS_MIN_COL]  = (Double) minimumAndMaximum[i-1][BETWEENNESS_MIN_COL];
            finalData[i][BETWEENNESS_MAX_COL]  = (Double) minimumAndMaximum[i-1][BETWEENNESS_MAX_COL];

            finalData[i][CLOSENESS_AVG_COL] = (Double) data[i-1][CLOSENESS_AVG_COL];
            finalData[i][CLOSENESS_MIN_COL] = (Double) minimumAndMaximum[i-1][CLOSENESS_MIN_COL];
            finalData[i][CLOSENESS_MAX_COL] = (Double) minimumAndMaximum[i-1][CLOSENESS_MAX_COL];

            finalData[i][DEGREE_AVG_COL] = (Double) data[i-1][DEGREE_AVG_COL];
            finalData[i][DEGREE_MIN_COL] = (Double) minimumAndMaximum[i-1][DEGREE_MIN_COL];
            finalData[i][DEGREE_MAX_COL] = (Double) minimumAndMaximum[i-1][DEGREE_MAX_COL];

            finalData[i][EIGENVECTOR_AVG_COL] = (Double) data[i-1][EIGENVECTOR_AVG_COL];
            finalData[i][EIGENVECTOR_MIN_COL] = (Double) minimumAndMaximum[i-1][EIGENVECTOR_MIN_COL];
            finalData[i][EIGENVECTOR_MAX_COL] = (Double) minimumAndMaximum[i-1][EIGENVECTOR_MAX_COL];
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
        if ((networks.getNetwork(0) instanceof ERNetwork) || (networks.getNetwork(0) instanceof SFNetwork) || (networks.getNetwork(0) instanceof ERNetwork)) {
            log("networks.regenerate();");
            networks.regenerate();
        }
        else {
            log("networks.getNetwork(1).rewire();");
            networks.getNetwork(1).rewire(); // TODO: Complete this so that it works for an arbitrary number of networks.
        }
        Network bridgedNetwork = networks.bridge();
        Integer[][] matrix = bridgedNetwork.getArrayMatrix();
        final Integer N = networks.getNumOfNodes();

        // Initialize the two-dimensional data set that will store the data that
        // will be used to make the .CSV output file.
        double[][] data = initializeData();

        for (Centrality metric : CENTRALITIES) {
            // Declare index variables to be used to store values in `data`.
            int avgIndex, minIndex, maxIndex;

            // Determine which columns (average, minimum, and maximum) to modify
            // with respect to centrality.
            switch (metric.type()) {
                case Centrality.BETWEENNESS:
                    avgIndex = BETWEENNESS_AVG_COL;
                    minIndex = BETWEENNESS_MIN_COL;
                    maxIndex = BETWEENNESS_MAX_COL;
                    break;
                case Centrality.CLOSENESS:
                    avgIndex = CLOSENESS_AVG_COL;
                    minIndex = CLOSENESS_MIN_COL;
                    maxIndex = CLOSENESS_MAX_COL;
                    break;
                case Centrality.DEGREE:
                    avgIndex = DEGREE_AVG_COL;
                    minIndex = DEGREE_MIN_COL;
                    maxIndex = DEGREE_MAX_COL;
                    break;
                case Centrality.EIGENVECTOR:
                    avgIndex = EIGENVECTOR_AVG_COL;
                    minIndex = EIGENVECTOR_MIN_COL;
                    maxIndex = EIGENVECTOR_MAX_COL;
                    break;
                default:
                    throw new Exception("Invalid Centrality metric type.");
            }

            // Log the start of the simulation to the user.
            log("Starting Simulation " + runID + " -- " + metric);
            int[] initialState  = new int[N];

            // To perform an interdependent network simulation, we must either:
            //   (1) Bridge the network AFTER picking the most central nodes from
            //       each network separately.
            //   (2) Bridge the network BEFORE picking the most central nodes.
            // Upon doing that, immunize and infect the appropriate nodes in the
            // network and set that as the initial state, or the state at time step 0.

            // OPTION 1: Immunize most central nodes of separate networks before bridging.
            if (separateCentralities) {
                int offset = 0;
                int backupImmuneCount = immuneCount;
                immuneCount /= networks.getNumOfNetworks();
                for (int i = 0; i < networks.getNumOfNetworks(); i++) {
                    Network temp = networks.getNetwork(i);
                    immunize(initialState, temp, metric, offset);
                    // infect(initialState, offset); TODO: Change this later.
                    offset += temp.getNumOfNodes(); // Increment offset by number of nodes.
                }
                infectFirstNetworks(initialState, 0, 1); // Initiate inital failure in just the FIRST network.
                immuneCount = backupImmuneCount;
            }
            // OPTION 2: Immunize most central nodes of the bridged network.
            else {
                // In this case, we simply immunize and infect once and hard-code
                // `offset` to be 0.
                immunize(initialState, bridgedNetwork, metric, 0);
                // infect(initialState, 0); TODO: Change this later.
                infectFirstNetworks(initialState, 0, 1);
            }


            // Propagate phenomena throughout the network and store the state data at
            // the end of each time step.
            int[] currentState = Arrays.copyOf(initialState, N);
            for (int t = 0; t < timeSteps; t++) {
                // Store the number of infected nodes in `data` and then iterate
                // to the next state in the propagation process.
                data[t][IMMUNE_COUNT_COL] = immuneCount;

                // Store the number of infected nodes for the average column.
                int numOfInfected = numberOfInfectedNodes(currentState);
                data[t][avgIndex] = numOfInfected;

                // Store the minimum number of infected nodes for the min column.
                if (minimumAndMaximum[t][minIndex] > numOfInfected)
                    minimumAndMaximum[t][minIndex] = numOfInfected;

                // Store the maximum number of infected nodes for the max column.
                if (minimumAndMaximum[t][maxIndex] < numOfInfected)
                    minimumAndMaximum[t][maxIndex] = numOfInfected;

                // Store the next state of phenomena propagation.
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

        for (int t = 0; t < timeSteps; t++) {
            // Set the timestamp and node count columns.
            _data[t][TIMESTAMP_COL] = t;
            _data[t][NODE_COUNT_COL] = networks.getNumOfNodes();
        }

        return _data;
    }


    /**
     * [initMinimumAndMaximum description]
     */
    public void initMinimumAndMaximum() {
        // Initialize the 2D array to keep track of minimum and maximum values;
        minimumAndMaximum = new double[timeSteps][COLUMNS];

        for (int t = 0; t < timeSteps; t++) {
            // Set all the minimum columns to negative infinity.
            minimumAndMaximum[t][BETWEENNESS_MIN_COL] = Double.POSITIVE_INFINITY;
            minimumAndMaximum[t][CLOSENESS_MIN_COL]   = Double.POSITIVE_INFINITY;
            minimumAndMaximum[t][DEGREE_MIN_COL]      = Double.POSITIVE_INFINITY;
            minimumAndMaximum[t][EIGENVECTOR_MIN_COL] = Double.POSITIVE_INFINITY;

            // Set all the minimum columns to negative infinity.
            minimumAndMaximum[t][BETWEENNESS_MAX_COL] = Double.NEGATIVE_INFINITY;
            minimumAndMaximum[t][CLOSENESS_MAX_COL]   = Double.NEGATIVE_INFINITY;
            minimumAndMaximum[t][DEGREE_MAX_COL]      = Double.NEGATIVE_INFINITY;
            minimumAndMaximum[t][EIGENVECTOR_MAX_COL] = Double.NEGATIVE_INFINITY;
        }
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
    private void immunize(int[] state, Network network, Centrality metric, int offset) {
        Integer[] immuneIndices = network.mostCentralNodes(metric, immuneCount);
        for (Integer immuneIndex : immuneIndices) {
            state[immuneIndex + offset] = Phenomena.IMMUNE;
        }
    }


    /**
     * [infect description]
     * @param state [description]
     */
    private void infect(int[] state, int offset) {
        int denom = networks.getNumOfNetworks();
        int i = 0, upperBound = state.length / denom, randomIndex;
        int infectCount = (int) (state.length * infectFraction + 0.5);
        if (separateCentralities) infectCount /= denom;

        while (i < infectCount) {
            randomIndex = (int) (Math.random() * upperBound);
            if (state[randomIndex + offset] == Phenomena.UNAFFLICTED) {
                state[randomIndex + offset] =  Phenomena.AFFLICTED;
                i++;
            }
        }
    }

    /**
     * [infect description]
     * @param state  [description]
     * @param offset [description]
     * @param n      Select the first `n` networks in the IDN to infect.
     */
    private void infectFirstNetworks(int[] state, int offset, int n) {
        if (n > networks.getNumOfNetworks()) {
            throw new IllegalArgumentException("`n` must be <= number of networks in IDN (" + networks.getNumOfNetworks() + ")");
        }
        int length = 0;
        for (int i = 0; i < n; i++) {
            length += networks.getNetwork(i).getNumOfNodes();
        }

        int i = 0, upperBound = length, randomIndex;
        int infectCount = (int) (state.length * infectFraction + 0.5);
        // if (separateCentralities) infectCount /= denom;

        while (i < infectCount) {
            randomIndex = (int) (Math.random() * upperBound);
            if (state[randomIndex + offset] == Phenomena.UNAFFLICTED) {
                state[randomIndex + offset] =  Phenomena.AFFLICTED;
                i++;
            }
        }
    }

}
