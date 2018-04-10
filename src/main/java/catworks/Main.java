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
    private static final double THRESH      = 0.03;
    private static final int    TIME        = 100;
    private static final double IMMUNE      = 0.05;
    private static final double INFECT      = 0.02;
    private static final int    SIMULATIONS = 3;

    /**
     * For now, this method only demos the SimpleMatrix class in action from EJML.
     * @param String[] args N/A
     */
    public static void main(String[] args) throws Exception, IOException {
        new SimulationRunner();
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
        // String filename = "/Users/Nathaniel/Desktop/Network_Output/sample.csv";
        // BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        // for (Object[] row : data)
        //     outputCSVRow(writer, row);
        // writer.close();
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

}
