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

public class SimulationRunner {

    // Class-wide constants.
    private static final String slash = File.separator;
    private static final int    TIME = 200;
    private static final int    NUM_OF_SIMULATIONS = 25;

    // Declare variables that will be used in simulation methods.
    private int n, m0, simID;        private double p, immune, infect;
    private int[] sizes;             private double[][] probs, threshMatrix;
    private Object[][] data;         private String filename;
    private Simulation simulation;   private Phenomena  phe;
    private Network cyber, physical; private IDN idn;
    private static final String baseFilename = "out" + slash + "simulations" + slash;

    public SimulationRunner() throws Exception {
        simID = 1;
        simulation1();
        simulation2();
    }


    /**
     * Template for simulation.
     */
    private void simulation1() throws IOException, Exception {
        n = 100; m0 = 5;
        infect = 0.05; immune = 0.025;
        sizes = new int[] { 0, n };
        probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
        threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};

        ProbabilityMatrix probMatrix = new ProbabilityMatrix(sizes, probs);
        phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new SFNetwork(n, m0); // Num of nodes, size of connected component.
        cyber    = new SFNetwork(n, m0); // Num of nodes, size of connected component.
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(0.05);      // Inter-edge probability.

        // First, separate simulation.
        simulation = new IDNSimulation(idn, phe, TIME, infect, immune, true);
        data = simulation.run(NUM_OF_SIMULATIONS);
        filename = baseFilename + "prob-thresh" + slash + "demo-separate.csv"; //"SF-SF-seprate-" + (simID++) + ".csv";
        outputData(data, filename);

        // Second, bridged simulation.
        simulation = new IDNSimulation(idn, phe, TIME, infect, immune, false);
        data = simulation.run(NUM_OF_SIMULATIONS);
        filename = baseFilename + "prob-thresh" + slash + "demo-bridged.csv"; //"SF-SF-bridged-" + (simID++) + ".csv";
        outputData(data, filename);
    }


    /**
     * Template for simulation.
     */
    private void simulation2() throws IOException, Exception {
        n = 100; m0 = 2;
        infect = 0.05; immune = 0.025;
        sizes = new int[] { 0, n };
        probs = new double[][] {{0.8, 0.9}, {0.5, 0.6}};
        threshMatrix = new double[][] {{0.3, 0.2}, {0.4, 0.3}};

        ProbabilityMatrix probMatrix = new ProbabilityMatrix(sizes, probs);
        Phenomena phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new SFNetwork(n, m0); // Num of nodes, size of connected component.
        cyber    = new SFNetwork(n, m0); // Num of nodes, size of connected component.
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(0.05);      // Inter-edge probability.

        // First, separate simulation.
        simulation = new IDNSimulation(idn, phe, TIME, infect, immune, true);
        data = simulation.run(NUM_OF_SIMULATIONS);
        filename = baseFilename + "prob-thresh" + slash + "SF-SF-seprate-" + (simID++) + ".csv";
        outputData(data, filename);

        // Second, bridged simulation.
        simulation = new IDNSimulation(idn, phe, TIME, infect, immune, false);
        data = simulation.run(NUM_OF_SIMULATIONS);
        filename = baseFilename + "prob-thresh" + slash + "SF-SF-bridged-" + (simID++) + ".csv";
        outputData(data, filename);
    }


    /**
     * [outputData description]
     * @param data     [description]
     * @param filename [description]
     */
    public static void outputData(Object[][] data, String filename) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
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
