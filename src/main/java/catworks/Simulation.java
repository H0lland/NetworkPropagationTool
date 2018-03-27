package catworks;

import java.util.Arrays;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 *
 */
public class Simulation {

    // Instance variables for simulations.
    private Network    network;
    private Phenomena  phenomena;
    private Centrality metric;
    private int        time;
    private int        immuneNum;
    private static int simulationID = 0;
    private int[]      firstState;
    private double     threshold;
    private File       file;
    private String     dir;
    // private Phenomena phenomena;

    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }

    public Simulation() {
        simulationID++;
    }

    public Simulation(Network network, Phenomena phenomena, Centrality metric, int time, double threshold, String dir) {
        this.network = network;
        this.phenomena = phenomena;
        this.metric = metric;
        this.time = time;
        this.threshold = threshold;
        this.dir = dir;
        // this.propgation = propagation;
    }


    public void run() throws IOException {
        // Set the threshold for the current phenomena.
        phenomena.setThreshold(threshold);

        // Initialize and setup the process of outputting to
        // a CSV file.
        String directory = dir + File.separator + phenomena + File.separator + metric + File.separator + "n=" + firstState.length + File.separator;
        String filename = "simulation_" + simulationID++;
        File file = new File(directory + filename + ".csv");
        file.getParentFile().mkdirs();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

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
            log("row = {" + row[0] + ", "+ row[1] + ", "+ row[2] + ", "+ row[3] + "}");
            outputCSVRow(writer, row);
            // TODO: The values in the .CSV differ from the values here. Investigate as to why.

            // Get the next state as per the phenomena propagation.
            state = phenomena.propagate(matrix, state);
        }
        writer.close();

    }

    public void setNetwork(Network network)       { this.network = network; }
    public void setPhenomena(Phenomena phenomena) { this.phenomena = phenomena; }
    public void setCentrality(Centrality metric)  { this.metric = metric; }
    public void setTime(int time)                 { this.time = time; }
    public void setThreshold(double threshold)    { this.threshold = threshold; }
    public void setDirectory(String directory)    { this.dir = directory; }
    public void setImmuneNum(int immuneNum)       { this.immuneNum = immuneNum; }
    public void setFirstState(int[] firstState)   { this.firstState = firstState; }
    public void setID(int id)                     { this.simulationID = id; }

    public void log(String string) {
        System.out.println("[" + new java.util.Date() + "]$ " + string);
    }

    /**
     * [outputCSVRow description]
     * @param writer [description]
     * @param row    [description]
     */
    public void outputCSVRow(BufferedWriter writer, Object[] row) throws IOException {
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
    public void outputCSVRow(BufferedWriter writer, int[] row) throws IOException {
        StringBuilder sb = new StringBuilder();
		for (int i = 0; i < row.length; i++) {
			if (i < row.length-1)
				sb.append(row[i] + ", ");
			else
				sb.append(row[i] + "\n");
		}
        log("outputCSVRow -> " + sb.toString());
		writer.write(sb.toString());
    }

    public int getNumInfected(int[] state) {
        int total = 0;
        for (int node : state) {
            if (node == Phenomena.AFFLICTED) total++;
        }
        return total;
    }

}
