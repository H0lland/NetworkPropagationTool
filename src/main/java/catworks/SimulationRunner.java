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
    private static final int    NUM_OF_SIMULATIONS = 250;

    // Declare variables that will be used in simulation methods.
    private int n, m0, k, simID;     private double p, beta, immune, infect;
    private int[] sizes;             private double[][] probs, threshMatrix;
    private Object[][] data;         private String filename, path;
    private Simulation simulation;   private Phenomena  phe;
    private ProbabilityMatrix probMatrix;
    private Network cyber, physical; private IDN idn;
    private static final String baseFilename = "out" + slash + "simulations" + slash;

    private static final boolean[] IS_BRIDGED = { true, false };

    public SimulationRunner() throws Exception {
        simID = 1;
        simulation1();
        simulation2();
        simulation3();
        simulation4();
        simulation5();
        simulation6();
        simulation7();
        simulation8();
        simulation9();
        simulation10();
        simulation11();
        simulation12();
        simulation13();
        simulation14();
        simulation15();
        simulation16();
        simulation17();
        simulation18();
    }

    public SimulationRunner(int n) throws Exception {
        simID = n;
        switch (n) {
            case 1:  simulation1();  break;
            case 2:  simulation2();  break;
            case 3:  simulation3();  break;
            case 4:  simulation4();  break;
            case 5:  simulation5();  break;
            case 6:  simulation6();  break;
            case 7:  simulation7();  break;
            case 8:  simulation8();  break;
            case 9:  simulation9();  break;
            case 10: simulation10(); break;
            case 11: simulation11(); break;
            case 12: simulation12(); break;
            case 13: simulation13(); break;
            case 14: simulation14(); break;
            case 15: simulation15(); break;
            case 16: simulation16(); break;
            case 17: simulation17(); break;
            case 18: simulation18(); break;
        }
    }


    /**
     * Simulation 1.
     */
    private void simulation1() throws IOException, Exception {
        // Variables to change from simulation to simulation.
        n = 300;       m0 = 10;
        infect = 0.05; immune = 0.025;
        sizes = new int[] {0, n};
        probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
        threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
        filename = "SF-SF-300";

        probMatrix = new ProbabilityMatrix(sizes, probs);
        phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new SFNetwork(n, m0);
        cyber    = new SFNetwork(n, m0);
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(0.05);      // Inter-edge probability.

        // Run the "bridged" and the "separate" simulations.
        for (boolean isBridged : IS_BRIDGED) {
            simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
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
    	n = 300;       p = 0.02;
    	infect = 0.05; immune = 0.025;
    	sizes = new int[] {0, n};
    	probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
    	threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
    	filename = "ER-ER-300";

    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new ERNetwork(n, p);
    	cyber    = new ERNetwork(n, p);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(0.05);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
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
    	n = 300; p = 0.02; m0 = 10;
    	infect = 0.05; immune = 0.025;
    	sizes = new int[] {0, n};
    	probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
    	threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
    	filename = "ER-SF-300";

    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new ERNetwork(n, p);
    	cyber    = new SFNetwork(n, m0);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(0.05);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
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
    	n = 300; p = 0.02; beta = 0.02; k = 6;
    	infect = 0.05; immune = 0.025;
    	sizes = new int[] {0, n};
    	probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
    	threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
    	filename = "ER-SW-300";

    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new ERNetwork(n, p);
    	cyber    = new SWNetwork(n, beta, k);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(0.05);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
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
    	n = 300; m0 = 10; p = 0.02;
    	infect = 0.05; immune = 0.025;
    	sizes = new int[] {0, n};
    	probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
    	threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
    	filename = "SF-ER-300";

    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new SFNetwork(n, m0);
    	cyber    = new ERNetwork(n, p);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(0.05);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
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
    	n = 300; m0 = 10; beta = 0.02; k = 6;
    	infect = 0.05; immune = 0.025;
    	sizes = new int[] {0, n};
    	probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
    	threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
    	filename = "SF-SW-300";

    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new SFNetwork(n, m0);
    	cyber    = new SWNetwork(n, beta, k);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(0.05);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
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
    	n = 300; beta = 0.02; k = 6; p = 0.02;
    	infect = 0.05; immune = 0.025;
    	sizes = new int[] {0, n};
    	probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
    	threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
    	filename = "SW-ER-300";

    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new SWNetwork(n, beta, k);
    	cyber    = new ERNetwork(n, p);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(0.05);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
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
    	n = 300; beta = 0.02; k = 6; m0 = 10;
    	infect = 0.05; immune = 0.025;
    	sizes = new int[] {0, n};
    	probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
    	threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
    	filename = "SW-SF-300";

    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new SWNetwork(n, beta, k);
    	cyber    = new SFNetwork(n, m0);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(0.05);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
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
    	n = 300; beta = 0.02; k = 6;
    	infect = 0.05; immune = 0.025;
    	sizes = new int[] {0, n};
    	probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
    	threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
    	filename = "SW-SW-300";

    	probMatrix = new ProbabilityMatrix(sizes, probs);
    	phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

    	physical = new SWNetwork(n, beta, k);
    	cyber    = new SWNetwork(n, beta, k);
    	idn = new IDN(physical, cyber);  // Cyber network, physical network.
    	idn.randomInterEdges(0.05);      // Inter-edge probability.

    	// Run the "bridged" and the "separate" simulations.
    	for (boolean isBridged : IS_BRIDGED) {
    		simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
    		data = simulation.run(NUM_OF_SIMULATIONS);
    		path = getPath(filename, isBridged);
    		outputData(data, path);
    	}
        simID++;
    }


    /**
     * Simulation 10.
     */
    private void simulation10() throws IOException, Exception {
        // Variables to change from simulation to simulation.
        n = 500;       m0 = 10;
        infect = 0.05; immune = 0.025;
        sizes = new int[] {0, n};
        probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
        threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
        filename = "SF-SF-500";

        probMatrix = new ProbabilityMatrix(sizes, probs);
        phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new SFNetwork(n, m0);
        cyber    = new SFNetwork(n, m0);
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(0.05);      // Inter-edge probability.

        // Run the "bridged" and the "separate" simulations.
        for (boolean isBridged : IS_BRIDGED) {
            simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
            data = simulation.run(NUM_OF_SIMULATIONS);
            path = getPath(filename, isBridged);
            outputData(data, path);
        }
        simID++;
    }


    /**
     * Simulation 11.
     */
    private void simulation11() throws IOException, Exception {
        // Variables to change from simulation to simulation.
        n = 500;       p = 0.02;
        infect = 0.05; immune = 0.025;
        sizes = new int[] {0, n};
        probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
        threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
        filename = "ER-ER-500";

        probMatrix = new ProbabilityMatrix(sizes, probs);
        phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new ERNetwork(n, p);
        cyber    = new ERNetwork(n, p);
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(0.05);      // Inter-edge probability.

        // Run the "bridged" and the "separate" simulations.
        for (boolean isBridged : IS_BRIDGED) {
            simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
            data = simulation.run(NUM_OF_SIMULATIONS);
            path = getPath(filename, isBridged);
            outputData(data, path);
        }
        simID++;
    }

    /**
     * Simulation 12.
     */
    private void simulation12() throws IOException, Exception {
        // Variables to change from simulation to simulation.
        n = 500; p = 0.02; m0 = 10;
        infect = 0.05; immune = 0.025;
        sizes = new int[] {0, n};
        probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
        threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
        filename = "ER-SF-500";

        probMatrix = new ProbabilityMatrix(sizes, probs);
        phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new ERNetwork(n, p);
        cyber    = new SFNetwork(n, m0);
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(0.05);      // Inter-edge probability.

        // Run the "bridged" and the "separate" simulations.
        for (boolean isBridged : IS_BRIDGED) {
            simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
            data = simulation.run(NUM_OF_SIMULATIONS);
            path = getPath(filename, isBridged);
            outputData(data, path);
        }
        simID++;
    }


    /**
     * Simulation 13.
     */
    private void simulation13() throws IOException, Exception {
        // Variables to change from simulation to simulation.
        n = 500; p = 0.02; beta = 0.02; k = 6;
        infect = 0.05; immune = 0.025;
        sizes = new int[] {0, n};
        probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
        threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
        filename = "ER-SW-500";

        probMatrix = new ProbabilityMatrix(sizes, probs);
        phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new ERNetwork(n, p);
        cyber    = new SWNetwork(n, beta, k);
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(0.05);      // Inter-edge probability.

        // Run the "bridged" and the "separate" simulations.
        for (boolean isBridged : IS_BRIDGED) {
            simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
            data = simulation.run(NUM_OF_SIMULATIONS);
            path = getPath(filename, isBridged);
            outputData(data, path);
        }
        simID++;
    }


    /**
     * Simulation 14.
     */
    private void simulation14() throws IOException, Exception {
        // Variables to change from simulation to simulation.
        n = 500; m0 = 10; p = 0.02;
        infect = 0.05; immune = 0.025;
        sizes = new int[] {0, n};
        probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
        threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
        filename = "SF-ER-500";

        probMatrix = new ProbabilityMatrix(sizes, probs);
        phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new SFNetwork(n, m0);
        cyber    = new ERNetwork(n, p);
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(0.05);      // Inter-edge probability.

        // Run the "bridged" and the "separate" simulations.
        for (boolean isBridged : IS_BRIDGED) {
            simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
            data = simulation.run(NUM_OF_SIMULATIONS);
            path = getPath(filename, isBridged);
            outputData(data, path);
        }
        simID++;
    }


    /**
     * Simulation 15.
     */
    private void simulation15() throws IOException, Exception {
        // Variables to change from simulation to simulation.
        n = 500; m0 = 10; beta = 0.02; k = 6;
        infect = 0.05; immune = 0.025;
        sizes = new int[] {0, n};
        probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
        threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
        filename = "SF-SW-500";

        probMatrix = new ProbabilityMatrix(sizes, probs);
        phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new SFNetwork(n, m0);
        cyber    = new SWNetwork(n, beta, k);
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(0.05);      // Inter-edge probability.

        // Run the "bridged" and the "separate" simulations.
        for (boolean isBridged : IS_BRIDGED) {
            simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
            data = simulation.run(NUM_OF_SIMULATIONS);
            path = getPath(filename, isBridged);
            outputData(data, path);
        }
        simID++;
    }


    /**
     * Simulation 16.
     */
    private void simulation16() throws IOException, Exception {
        // Variables to change from simulation to simulation.
        n = 500; beta = 0.02; k = 6; p = 0.02;
        infect = 0.05; immune = 0.025;
        sizes = new int[] {0, n};
        probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
        threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
        filename = "SW-ER-500";

        probMatrix = new ProbabilityMatrix(sizes, probs);
        phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new SWNetwork(n, beta, k);
        cyber    = new ERNetwork(n, p);
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(0.05);      // Inter-edge probability.

        // Run the "bridged" and the "separate" simulations.
        for (boolean isBridged : IS_BRIDGED) {
            simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
            data = simulation.run(NUM_OF_SIMULATIONS);
            path = getPath(filename, isBridged);
            outputData(data, path);
        }
        simID++;
    }


    /**
     * Simulation 17.
     */
    private void simulation17() throws IOException, Exception {
        // Variables to change from simulation to simulation.
        n = 500; beta = 0.02; k = 6; m0 = 10;
        infect = 0.05; immune = 0.025;
        sizes = new int[] {0, n};
        probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
        threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
        filename = "SW-SF-500";

        probMatrix = new ProbabilityMatrix(sizes, probs);
        phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new SWNetwork(n, beta, k);
        cyber    = new SFNetwork(n, m0);
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(0.05);      // Inter-edge probability.

        // Run the "bridged" and the "separate" simulations.
        for (boolean isBridged : IS_BRIDGED) {
            simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
            data = simulation.run(NUM_OF_SIMULATIONS);
            path = getPath(filename, isBridged);
            outputData(data, path);
        }
        simID++;
    }


    /**
     * Simulation 18.
     */
    private void simulation18() throws IOException, Exception {
        // Variables to change from simulation to simulation.
        n = 500; beta = 0.02; k = 6;
        infect = 0.05; immune = 0.025;
        sizes = new int[] {0, n};
        probs = new double[][] {{0.8, 0.8}, {0.8, 0.8}};
        threshMatrix = new double[][] {{0.2, 0.15}, {0.4, 0.3}};
        filename = "SW-SW-500";

        probMatrix = new ProbabilityMatrix(sizes, probs);
        phe = new ProbThreshPhenomena(probMatrix, threshMatrix);

        physical = new SWNetwork(n, beta, k);
        cyber    = new SWNetwork(n, beta, k);
        idn = new IDN(physical, cyber);  // Cyber network, physical network.
        idn.randomInterEdges(0.05);      // Inter-edge probability.

        // Run the "bridged" and the "separate" simulations.
        for (boolean isBridged : IS_BRIDGED) {
            simulation = new IDNSimulation(idn, phe, TIME, infect, immune, isBridged);
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
        return baseFilename + "prob-thresh" + slash + isBridged + slash + filename + "_" + simID + ".csv";
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
