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

    /**
     * For now, this method only demos the SimpleMatrix class in action from EJML.
     * @param String[] args N/A
     */
    public static void main(String[] args) throws Exception, IOException {
        Network net = new ERNetwork(1000, 0.5);
        Phenomena phe = new ThresholdPhenomena(0.3f);

        Simulation simulation = new Simulation(net, phe, 100, 0.05, 0.10);
        Object[][] data = simulation.run(1000);

        // Output the data to a .CSV file.
        String filename = "/Users/Nathaniel/Desktop/Network_Output/ER_1000_single.csv";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (Object[] row : data)
            outputCSVRow(writer, row);
        writer.close();

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
