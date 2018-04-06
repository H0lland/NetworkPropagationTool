package catworks;

// Project import statements.
import catworks.networks.*;
import catworks.networks.metrics.*;
import catworks.phenomena.*;
import catworks.simulations.*;

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

    // TEMPORARY constant values to be used to run simulations. TODO: Create XML
    // parser for a more streamlined approach in lieu of this for future iterations.
    private static final int    N           = 300;
    private static final double INTRA_P     = 0.25;//0.02;
    private static final double INTER_P     = 0.10;
    private static final float  THRESH      = 0.03f;
    private static final int    TIME        = 100;
    private static final double IMMUNE      = 0.05;
    private static final double INFECT      = 0.02;
    private static final int    SIMULATIONS = 3;

    /**
     * For now, this method only demos the SimpleMatrix class in action from EJML.
     * @param String[] args N/A
     */
    public static void main(String[] args) throws Exception, IOException {
        // Declare variable types for simulation(s).
        IDN idn;
        Simulation simulation;
        Phenomena phe;
        Object[][] data;
        String filename;

        // Initialize IDN and Phenomena.
        idn = fooRandomIDN(N, INTRA_P, INTER_P);
        phe = new ThresholdPhenomena(THRESH);

        // SIMULATION 1: Immunize most central nodes in each topology.
        filename = "/Users/Nathaniel/Desktop/Network_Output/demo_separate.csv";
        simulation = new IDNSimulation(idn, phe, TIME, IMMUNE, INFECT, true);
        data = simulation.run(SIMULATIONS);
        outputData(data, filename);

        // SIMULATION 2: Same network topology, but bridging occurs before immunization.
        filename = "/Users/Nathaniel/Desktop/Network_Output/demo_bridged.csv";
        simulation = new IDNSimulation(idn, phe, TIME, IMMUNE, INFECT, false);
        data = simulation.run(SIMULATIONS);
        outputData(data, filename);

        /*
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
        */
    }


    public static IDN fooRandomIDN(int n, double p, double IDN_p) {
        Network net1 = new ERNetwork(n, p);//, 4);
        Network net2 = new ERNetwork(n, p);//, 4);

        ArrayList<Network> networks = new ArrayList();
        networks.add(net1); networks.add(net2);
        IDN idn = new IDN(networks);
        fooRandomInterEdges(idn, net1, net2, IDN_p);

        return idn;
    }

    /**
     * This method adds random BIDIRECTIONAL inter-edges between two networks in
     * an IDN.
     * @param idn   [description]
     * @param net1  [description]
     * @param net2  [description]
     * @param IDN_p [description]
     */
    public static void fooRandomInterEdges(IDN idn, Network net1, Network net2, double IDN_p) {
        final int N1 = net1.getNumOfNodes(); int net1ID = 0;
        final int N2 = net2.getNumOfNodes(); int net2ID = 1;

        ArrayList<InterEdge> interEdges = new ArrayList();
        for (int i = 0; i < N1; i++) {
            for (int j = 0; j < N2; j++) {
                if (Math.random() < IDN_p) {
                    interEdges.add(new InterEdge(net1ID, i, net2ID, j));
                    interEdges.add(new InterEdge(net2ID, j, net1ID, i));
                }
            }
        }

        for (InterEdge interEdge : interEdges) {
            idn.addInterEdge(interEdge);
        }
    }


    /**
     * Sample simulation run. To be used for future simulation generation and testing.
     */
    public static void simulation1() throws Exception, IOException {
        Network net = new ERNetwork(300, 0.25);
        Phenomena phe = new ThresholdPhenomena(0.03f);

        Simulation simulation = new NetworkSimulation(net, phe, 100, 0.05, 0.10);
        Object[][] data = simulation.run(10);

        // Output the data to a .CSV file.
        String filename = "/Users/Nathaniel/Desktop/Network_Output/sample.csv";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (Object[] row : data)
            outputCSVRow(writer, row);
        writer.close();
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
            return null;
        }
    }


    /**
     * [outputData description]
     * @param data     [description]
     * @param filename [description]
     */
    public static void outputData(Object[][] data, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (Object[] row : data)
            outputCSVRow(writer, row);
        writer.close();
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

}
