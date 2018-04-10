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

    /**
     * For now, this method only demos the SimpleMatrix class in action from EJML.
     * @param String[] args N/A
     */
    public static void main(String[] args) throws Exception, IOException {
        new SimulationRunner();
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
