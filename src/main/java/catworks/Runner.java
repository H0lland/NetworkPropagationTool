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

public class Runner {

    // Directed/Undirected constants.
    private static final boolean DIRECTED   = true;
    private static final boolean UNDIRECTED = false;

    // IDN Topology Constants.
    private static final int ER_ER_TOPO = 0;
    private static final int SF_SF_TOPO = 1;
    private static final int SW_SW_TOPO = 2;
    private static final int ER_SW_TOPO = 3;
    private static final int SW_ER_TOPO = 3;
    private static final int ER_SF_TOPO = 4;
    private static final int SF_ER_TOPO = 4;
    private static final int SF_SW_TOPO = 5;
    private static final int SW_SF_TOPO = 5;

    private static String slash = File.separator;
    private Object[][] data;

    private Simulation simulation;
    private Phenomena  phe;
    private String filename;


    private static final int    N = 500;

    // Erdos-Reynis Network constant.
    private static final double INTRA = 0.15;

    // Small-World Network constants.
    private static final double K_FRACT = 0.10;
    private static final double SW_P    = 0.25;
    private static final int    K       = (int) (K_FRACT * N);

    // Scale-Free Network constants.
    private static final double M0_FRACT = 0.05;
    private static final int    M0       = (int) (M0_FRACT * N);

    private static final int    TIME = 100;
    private static final double INTER = 0.05;
    private static final double INFECT = 0.05;
    private static final double IMMUNE = 0.025;
    private static final int    SIMULATIONS = 100;

    private static final String baseFilename = ".." + slash + ".." + slash + ".." +  slash + "out" + slash + "simulations" + slash;

    public Runner() throws Exception {

        Phenomena[] phenomena = { new ThresholdPhenomena(0.05) };

        String topoToken;
        int[] topologies = { ER_ER_TOPO, SF_SF_TOPO, SW_SW_TOPO, ER_SF_TOPO, ER_SW_TOPO, SF_SW_TOPO };

        for (Phenomena phe : phenomena) {
            for (int topology : topologies) {
                // Initialize IDN and get the String token for the topology (to be used for filename).
                IDN idn = generateIDN(topology);
                topoToken = topologyToken(topology);

                // Separate simulations.
                filename = baseFilename + phenomena + slash + topoToken + "-Separate.csv";
                simulation = new IDNSimulation(idn, phe, TIME, INFECT, IMMUNE, true);
                data = simulation.run(SIMULATIONS);
                outputData(data, filename);

                // Bridged simulations.
                filename = baseFilename + phenomena + slash + topoToken + "-Bridged.csv";
                simulation = new IDNSimulation(idn, phe, TIME, INFECT, IMMUNE, true);
                data = simulation.run(SIMULATIONS);
                outputData(data, filename);
            }
        }
    }

    /**
     * [generateRandomIDN description]
     * @param  topology [description]
     * @return          [description]
     */
    private IDN generateIDN(int topology) {
        Network network1, network2;
        IDN idn;
        switch (topology) {
            case ER_ER_TOPO:
                network1 = new ERNetwork(N, INTRA, DIRECTED);
                network2 = new ERNetwork(N, INTRA, DIRECTED);
                idn = new IDN(network1, network2);
                idn.randomInterEdges(INTER);
                break;

            case SF_SF_TOPO:
                network1 = new SFNetwork(N, M0);
                network2 = new SFNetwork(N, M0);
                idn = new IDN(network1, network2);
                idn.randomInterEdges(INTER);
                break;

            case SW_SW_TOPO:
                network1 = new SWNetwork(N, SW_P, K, DIRECTED);
                network2 = new SWNetwork(N, SW_P, K, DIRECTED);
                idn = new IDN(network1, network2);
                idn.randomInterEdges(INTER);
                break;

            case ER_SW_TOPO:
                network1 = new ERNetwork(N, INTRA, DIRECTED);
                network2 = new SWNetwork(N, SW_P, K, DIRECTED);
                idn = new IDN(network1, network2);
                idn.randomInterEdges(INTER);
                break;

            case ER_SF_TOPO:
                network1 = new ERNetwork(N, INTRA, DIRECTED);
                network2 = new SFNetwork(N, M0);
                idn = new IDN(network1, network2);
                idn.randomInterEdges(INTER);
                break;

            case SF_SW_TOPO:
                network1 = new SFNetwork(N, M0);
                network2 = new SWNetwork(N, SW_P, K, DIRECTED);
                idn = new IDN(network1, network2);
                idn.randomInterEdges(INTER);
                break;

            default:
                throw new IllegalArgumentException("Invalid topology integer type.");
        }
        return idn;
    }


    /**
     * [topologyToken description]
     * @param  topology [description]
     * @return          [description]
     */
    private String topologyToken(int topology) {
        String token;
        switch (topology) {
            case ER_ER_TOPO:  token = "ER-ER";  break;
            case SF_SF_TOPO:  token = "SF-SF";  break;
            case SW_SW_TOPO:  token = "SW-SW";  break;
            case ER_SW_TOPO:  token = "ER-SW";  break;
            case ER_SF_TOPO:  token = "ER-SF";  break;
            case SF_SW_TOPO:  token = "SF-SW";  break;
            default:
                throw new IllegalArgumentException("Invalid topology integer type.");
        }
        return token;
    }


    /**
     * [outputData description]
     * @param data     [description]
     * @param filename [description]
     */
    public static void outputData(Object[][] data, String filename) throws IOException {
        File file = new File(filename);
        file.mkdirs();
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
