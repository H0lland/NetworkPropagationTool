package catworks;

import java.util.Arrays;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

import javax.swing.JFileChooser;

public class Main {

    // Constant values that will be used in the main simulation to generate
    // simulation permutations.
    private static final int[] SIZES = {1000};//{ 50, 100, 200, 500 };
    private static final double[] PROBAB = {0.0125};//{ .05, .10, .20, .30, .40, .50, .60, .70, .80, .90 };

    private static final int[] TIMES = { 100 };//, 500, 1000 };
    private static final double[] IMMUNE_RATES = { .05};//, .10, .15, .20, .25, .30, .35, .40, .45, .50 };
    private static final double[] INFECT = {0.025};//{ .05, .10, .15, .20, .25, .30, .35, .40, .45, .50 };

    private static final Phenomena[]  PHENOMENA = { new EpidemicPhenomena() };//, new ThresholdPhenomena(); };
    private static final Centrality[] CENTRALITIES = { new BetweennessCentrality(), new DegreeCentrality(), new EigenvectorCentrality() };

    /**
     * For now, this method only demos the SimpleMatrix class in action from EJML.
     * @param String[] args N/A
     */
    public static void main(String[] args) throws IOException {
        File outputDir = null;

        // Create a JFileChooser instance to allow the user to choose a Directory
        // to store the output results of the phenomena propagation.
        JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("~"));
		chooser.setDialogTitle("Choose CSV Output Directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

        // Let the user choose the directory and then output the selected directory.
		boolean selection = chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION;
		if (selection) {
            outputDir = chooser.getSelectedFile();
            log("Output directory: " + chooser.getSelectedFile());
        }
        else {
            log("No directory selected.");
        }


        // Generate the random networks that will be used for simulation of phenomena
        // propagation.
        ArrayList<Network> networks = new ArrayList();
        for (int size : SIZES) {
            for (double probability : PROBAB) {
                networks.add(new Network(size, probability));
            }
        }

        // Simulation
        for (int time : TIMES) {
            // ...
            for (Centrality metric : CENTRALITIES) {
                for (double immuneRate : IMMUNE_RATES) {
                    for (Network network : networks) {
                        int immuneNum = (int) (network.getNumOfNodes() * immuneRate);
                        Integer[] immuneNodeIndices = network.mostCentralNodes(metric, immuneNum);

                        // Initialize the initial nodes and then mark the most
                        // central nodes, as per the current centrality metric,
                        // as immune.
                        int[] immuneState = new int[network.getNumOfNodes()];
                        for (int i : immuneNodeIndices)
                            immuneState[i] = Phenomena.IMMUNE;

                        for (double infectionRate : INFECT) {
                            int[] firstState = Arrays.copyOf(immuneState, immuneState.length);
                            infect(firstState, infectionRate);

                            for (Phenomena phenomena : PHENOMENA) {
                                // Initialize and setup the process of outputting to
                                // a CSV file.
                                String filename = "n=" + firstState.length + "_" + metric + "_immuneRate=" + immuneRate + "_infectRate=" + infectionRate + "_phenomena=" + phenomena;
                                BufferedWriter writer = new BufferedWriter(new FileWriter(outputDir + File.separator + filename + ".csv"));

                                // TODO: FIll in later.
                                String[] header = { "Time Step", "Number of Nodes", "Infected Nodes", "Immune Nodes" };
                                outputCSVRow(writer, header);

                                // Initialize the variable to store the adjacency matrix
                                // for the Network.
                                Integer[][] matrix = network.getArrayMatrix();
                                int[] state = Arrays.copyOf(firstState, firstState.length);
                                int[] row = new int[4]; // { time,  numInfected }

                                log("Beginning simulation: n=" + state.length + ", phenomena=" + phenomena + ", centrality=" + metric + "...");
                                for (int timeStep = 0; timeStep < time; timeStep++) {
                                    // Get the values for the row to be stored in the CSV file.
                                    row[0] = timeStep;
                                    row[1] = state.length;
                                    row[2] = getNumInfected(state);
                                    row[3] = immuneNum;
                                    outputCSVRow(writer, row);

                                    // Get the next state as per the phenomena propagation.
                                    state = phenomena.propagate(matrix, state);
                                }
                                writer.close();
                                log("Finished simulation!\n");
                            }

                        }
                    }
                }
            }

        }

    }


    /**
     * [outputCSVRow description]
     * @param writer [description]
     * @param row    [description]
     */
    public static void outputCSVRow(BufferedWriter writer, Object[] row) throws IOException {
        StringBuilder sb = new StringBuilder();
		for (int i = 0; i < row.length; i++) {
			if (i < row.length-1)
				sb.append(row[i] + ", ");
			else
				sb.append(row[i] + "\n");
		}
		writer.write(sb.toString());
    }


    /**
     * [outputCSVRow description]
     * @param writer [description]
     * @param row    [description]
     */
    public static void outputCSVRow(BufferedWriter writer, int[] row) throws IOException {
        StringBuilder sb = new StringBuilder();
		for (int i = 0; i < row.length; i++) {
			if (i < row.length-1)
				sb.append(row[i] + ", ");
			else
				sb.append(row[i] + "\n");
		}
		writer.write(sb.toString());
    }


    public static void log(String string) {
        System.out.println("[" + new java.util.Date() + "] $ " + string);
    }


    public static void infect(int[] nodeStates, double rate) {
        for (int i = 0; i < nodeStates.length; i++)
            if (Math.random() <= rate)
                if (nodeStates[i] != Phenomena.IMMUNE)
                    nodeStates[i] =  Phenomena.AFFLICTED;
    }


    public static int getNumInfected(int[] state) {
        int total = 0;
        for (int node : state) {
            if (node == Phenomena.AFFLICTED) total++;
        }
        return total;
    }

}
