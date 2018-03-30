package catworks;

// Project import statements.
import catworks.networks.*;
import catworks.networks.metrics.*;
import catworks.phenomena.*;

// Additional import statements.
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

import javax.swing.JFileChooser;

public class Main {

    // Constant values that will be used in the main simulation to generate
    // simulation permutations.
    private static final int[] SIZES = { 500, 1000 };
    private static final double[] PROBAB = { 0.1 };

    private static final int[] TIMES = { 100 };
    private static final double[] IMMUNE_RATES = { .01, .02, .03, .04, .05, .06, .07, .08, .09, .10, .11, .12, .13, .14, .15 };
    private static final double[] INFECT = { 0.01, 0.02, 0.03 };

    private static final Phenomena[]  PHENOMENA = { new EpidemicPhenomena(), new ThresholdPhenomena() };
    private static final Centrality[] CENTRALITIES = { new BetweennessCentrality(), new ClosenessCentrality(), new DegreeCentrality(), new EigenvectorCentrality() };

    /**
     * For now, this method only demos the SimpleMatrix class in action from EJML.
     * @param String[] args N/A
     */
    public static void main(String[] args) throws IOException {

        // (1) Initialize and declare the random network to be used for simulation.
        String  networkInput = "ER"; // TODO: Dummy line with fixed data. Change this later.
        Network network;
        switch (networkInput) {
            case "ER": network = ERNetwork(numNodes, p); break;
            case "SF": network = SFNetwork(...);         break;
            case "SW": network = SWNetwork(...);         break;
            default:
                throw new IllegalArgumentException("No declaration for random network type.");
                break;
        }

        // (2) Initialize and declare the phenomena to be used for simulation.
        String    phenomInput = "epidemic"; // TODO: Dummy line with fixed data. Change this later.
        Phenomena phenomena;
        switch (phenomInput) {
            case "epidemic":  phenomena = EpidemicPhenomena();           break;
            case "threshold": phenomena = ThresholdPhenomena(threshold); break;
            default:
                throw new IllegalArgumentException("No declaration for phenomena type.");
                break;
        }

        // (3, 4, 5) Initialize and declare the number of timesteps, immunity fraction, and infection fraction in a simulation.
        int timeSteps;
        double immunityFraction, infectionFraction;

        // (6) Initialize and declare the directory/filename of the output .csv file.
        String filename;



        Simulation simulation = new Simulation(network, phenomena, timeSteps, immunityFraction, infectionFraction, filename);
        simulation.runMultipleTimes(N); // Run the simulation N times and then save the average of data.
    }


    /**
     * [getOutputDirectory description]
     * @return [description]
     */
    public static File getOutputDirectory() {
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
            return chooser.getSelectedFile();
        }
        else {
            log("No directory selected.");
            return null;
        }
    }


    /**
     * [parameterizedSimulations description]
     */
    public static void parameterizedSimulations() throws IOException {

        File outputDir = getOutputDirectory();

        // Generate the random networks that will be used for simulation of phenomena
        // propagation.
        ArrayList<Network> networks = new ArrayList();
        for (int size : SIZES) {
            for (double probability : PROBAB) {
                networks.add(new ERNetwork(size, probability));
            }
        }

        // Simulation
        int simulationID = 0;
        for (int time : TIMES) {
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
                                double start = 1.0, end = 1.0, delta = 1.0;
                                if (phenomena.getType().equals("theshold")) {
                                    start = 0.3; end = 0.7; delta = 0.2;
                                }

                                // Run the phenomena with the given
                                for (double threshold = start; start <= end; start += delta) {
                                    Simulation simulation = new Simulation();
                                    simulation.setNetwork(network);
                                    simulation.setPhenomena(phenomena);
                                    simulation.setCentrality(metric);
                                    simulation.setTime(time);
                                    simulation.setFirstState(firstState);
                                    simulation.setImmuneNum(immuneNum);
                                    simulation.setThreshold(threshold);
                                    simulation.setDirectory(outputDir.toString());
                                    simulation.run();
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * [log description]
     * @param string [description]
     */
    public static void log(String string) {
        System.out.println("[" + new java.util.Date() + "] $ " + string);
    }


    /**
     * [infect description]
     * @param nodeStates [description]
     * @param rate       [description]
     */
    public static void infect(int[] nodeStates, double rate) {
        for (int i = 0; i < nodeStates.length; i++)
            if (Math.random() <= rate)
                if (nodeStates[i] != Phenomena.IMMUNE)
                    nodeStates[i] =  Phenomena.AFFLICTED;
    }


    /**
     * [getNumInfected description]
     * @param  state [description]
     * @return       [description]
     */
    public static int getNumInfected(int[] state) {
        int total = 0;
        for (int node : state) {
            if (node == Phenomena.AFFLICTED) total++;
        }
        return total;
    }

}
