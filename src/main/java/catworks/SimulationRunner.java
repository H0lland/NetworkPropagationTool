package catworks;

// Project import statements.
import catworks.networks.*;
import catworks.networks.metrics.*;
import catworks.phenomena.*;
import catworks.simulations.*;

// Additional import statements.
import java.util.Arrays;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class SimulationRunner {

    // Class-wide constants.
    private static final String slash = File.separator;
    private static final int    TIME = 200;
    private static final int    NUM_OF_SIMULATIONS = 100;

    // Network simulation variables to be modified.
    private static int nodes = 300, m0 = 6, k = 6, immune = 10;
    private static double infect = 5.0/600, INTER_P = 3.0/600;
    private static double p = 0.02, beta = 0.05;

    // Declare variables that will be used in simulation methods.
    private int simID;

    private int[] sizes;             private double[][] probs, threshMatrix;
    private Object[][] data;         private String filename, path;
    private Simulation simulation;   private Phenomena  phe;
    private ProbabilityMatrix probMatrix;
    private Network cyber, physical; private IDN idn;
    private static final String baseFilename = "out" + slash + "simulations" + slash + "lowered_thresh" + slash + "targeted_sample" + slash;

    private static final boolean[] IS_BRIDGED = { true, false };

    private static final double[][] LOWER_THRESH  = {{0.15, 0.10}, {0.35, 0.25}};
    private static final double[][] NORMAL_THRESH = {{0.20, 0.15}, {0.40, 0.30}};

    public SimulationRunner() throws Exception {
        simID = 1;
        probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
        threshMatrix = LOWER_THRESH;
        sizes = new int[] {0, nodes};
        simulation1();
        simulation2();
        simulation3();
        simulation4();
        simulation5();
        simulation6();
        simulation7();
        simulation8();
        simulation9();
        simulationRealWorld();
    }

    public SimulationRunner(int n) throws Exception {
        simID = n;
        probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
        threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
        sizes = new int[] {0, nodes};
        switch (n) {
            case 1:  simulation1();         break;
            case 2:  simulation2();         break;
            case 3:  simulation3();         break;
            case 4:  simulation4();         break;
            case 5:  simulation5();         break;
            case 6:  simulation6();         break;
            case 7:  simulation7();         break;
            case 8:  simulation8();         break;
            case 9:  simulation9();         break;
            case -1: simulationRealWorld(); break;
        }
    }


    /**
     * Simulation 1.
     */
    private void simulation1() throws IOException, Exception {
        // Variables to change from simulation to simulation.
        filename = "SF-SF-300";
        probMatrix = new ProbabilityMatrix(sizes, probs);
        phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new SFNetwork(nodes, m0);
        cyber    = new SFNetwork(nodes, m0);
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(INTER_P);      // Inter-edge probability.

        // Run the "bridged" and the "separate" simulations.
        for (boolean isBridged : IS_BRIDGED) {
            simulation = new IDNSimulation(idn, phe, TIME, immune, infect, isBridged);
            data = simulation.run(NUM_OF_SIMULATIONS);
            path = getPath(filename, isBridged);
            outputData(data, path);
        }
        simID++;
    }


    /**
     * Simulation 2.
     */
    private void simulation2() throws IOException, Exception {
    	// Variables to change from simulation to simulation.
    	filename = "ER-ER-300";
    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new ERNetwork(nodes, p);
    	cyber    = new ERNetwork(nodes, p);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(INTER_P);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, immune, infect, isBridged);
    		data = simulation.run(NUM_OF_SIMULATIONS);
    		path = getPath(filename, isBridged);
    		outputData(data, path);
    	}
        simID++;
    }

    /**
     * Simulation 3.
     */
    private void simulation3() throws IOException, Exception {
    	// Variables to change from simulation to simulation.
    	filename = "ER-SF-300";
    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new ERNetwork(nodes, p);
    	cyber    = new SFNetwork(nodes, m0);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(INTER_P);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, immune, infect, isBridged);
    		data = simulation.run(NUM_OF_SIMULATIONS);
    		path = getPath(filename, isBridged);
    		outputData(data, path);
    	}
        simID++;
    }


    /**
     * Simulation 4.
     */
    private void simulation4() throws IOException, Exception {
    	// Variables to change from simulation to simulation.
    	filename = "ER-SW-300";
    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new ERNetwork(nodes, p);
    	cyber    = new SWNetwork(nodes, beta, k);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(INTER_P);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, immune, infect, isBridged);
    		data = simulation.run(NUM_OF_SIMULATIONS);
    		path = getPath(filename, isBridged);
    		outputData(data, path);
    	}
        simID++;
    }


    /**
     * Simulation 5.
     */
    private void simulation5() throws IOException, Exception {
    	// Variables to change from simulation to simulation.
    	filename = "SF-ER-300";
    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new SFNetwork(nodes, m0);
    	cyber    = new ERNetwork(nodes, p);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(INTER_P);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, immune, infect, isBridged);
    		data = simulation.run(NUM_OF_SIMULATIONS);
    		path = getPath(filename, isBridged);
    		outputData(data, path);
    	}
        simID++;
    }


    /**
     * Simulation 6.
     */
    private void simulation6() throws IOException, Exception {
    	// Variables to change from simulation to simulation.
    	filename = "SF-SW-300";
    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new SFNetwork(nodes, m0);
    	cyber    = new SWNetwork(nodes, beta, k);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(INTER_P);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, immune, infect, isBridged);
    		data = simulation.run(NUM_OF_SIMULATIONS);
    		path = getPath(filename, isBridged);
    		outputData(data, path);
    	}
        simID++;
    }


    /**
     * Simulation 7.
     */
    private void simulation7() throws IOException, Exception {
    	// Variables to change from simulation to simulation.
    	filename = "SW-ER-300";
    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new SWNetwork(nodes, beta, k);
    	cyber    = new ERNetwork(nodes, p);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(INTER_P);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, immune, infect, isBridged);
    		data = simulation.run(NUM_OF_SIMULATIONS);
    		path = getPath(filename, isBridged);
    		outputData(data, path);
    	}
        simID++;
    }


    /**
     * Simulation 8.
     */
    private void simulation8() throws IOException, Exception {
    	// Variables to change from simulation to simulation.
    	filename = "SW-SF-300";
    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new SWNetwork(nodes, beta, k);
    	cyber    = new SFNetwork(nodes, m0);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(INTER_P);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, immune, infect, isBridged);
    		data = simulation.run(NUM_OF_SIMULATIONS);
    		path = getPath(filename, isBridged);
    		outputData(data, path);
    	}
        simID++;
    }


    /**
     * Simulation 9.
     */
    private void simulation9() throws IOException, Exception {
    	// Variables to change from simulation to simulation.
    	filename = "SW-SW-300";
    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new SWNetwork(nodes, beta, k);
    	cyber    = new SWNetwork(nodes, beta, k);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(INTER_P);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, immune, infect, isBridged);
    		data = simulation.run(NUM_OF_SIMULATIONS);
    		path = getPath(filename, isBridged);
    		outputData(data, path);
    	}
        simID++;
    }


    private void simulationRealWorld() throws IOException, Exception {
        // Variables to change from simulation to simulation.
        // infect = 0.15; immune = 0.05;
        filename = "IEEE";
        probMatrix = new ProbabilityMatrix(sizes, probs);
        phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new Network(RealWorld.IEEE300());
        cyber    = new Network(RealWorld.IEEE300()); cyber.rewire();
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(INTER_P);   // Inter-edge probability.

        // Run the "bridged" and the "separate" simulations.
        for (boolean isBridged : IS_BRIDGED) {
            simulation = new IDNSimulation(idn, phe, TIME, immune, infect, isBridged);
            data = simulation.run(NUM_OF_SIMULATIONS);
            path = getPath(filename, isBridged);
            outputData(data, path);
        }
        simID++;
    }


    /**
     * Return the entire path (directory and file extension) for a file given
     * just the name of the file, without the file extension.
     * @param  filename The name to be used to create the full path (don't include file extension).
     * @param  bridged  Is this file simulating a bridged or separate calculation of centralities.
     * @return          String of the entire path, with directory and filename w/extension.
     */
    private String getPath(String filename, boolean bridged) {
        String isBridged = (bridged) ? "bridged" : "separate";
        return baseFilename + isBridged + slash + filename + ".csv";
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
