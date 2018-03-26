package catworks;

import java.io.File;

/**
 *
 */
public class Simulation {

    // Instance variables for simulations.
    private int time;
    private int size;
    private Centrality centrality;
    private Network network;
    private File file;
    // private Phenomena phenomena;

    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }

    public Simulation(int time, int size, Centrality centrality, Network network, File file) {
        this.time = time;
        this.size = size;
        this.centrality = centrality;
        this.network = network;
        this.file = file;
        // this.propgation = propagation;
    }

    public void run() {

        log("Starting simulation.");
        for (int timeStep = 0; timeStep < time; timeStep++) {
            recordNetworkState(timeStep);
            // network = phenomena.propagate();
            log("Propagating phenomena for time step " + timeStep + ".");
        }
        log("Simulation completed. Check " + file + " for record of phenomena propagation.");

    }

    public void log(String string) {
        System.out.println("[" + new java.util.Date() + "] $ " + string);
    }

    /**
     * This method will record a state of a network as a row in a spreadsheet.
     * @param timeStep The current time step for the row.
     */
    public void recordNetworkState(int timeStep) {

        /** TODO: Modify this code to fit this project, refer to Untitled.java.
            String filename = "sample.csv";
    		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
    		for (int[] row : matrix) {
    			StringBuilder sb = new StringBuilder();
    			for (int i = 0; i < row.length; i++) {
    				if (i < row.length-1)
    					sb.append(row[i] + ", ");
    				else
    					sb.append(row[i] + "\n");
    			}
    			writer.write(sb.toString());
    		}
    		writer.close();
        */

        return;
    }

}
