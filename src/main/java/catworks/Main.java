package catworks;

// Project import statements.
import catworks.networks.*;
import catworks.networks.metrics.*;
import catworks.phenomena.*;
import catworks.simulations.*;

// Additional import statement(s).
import java.io.IOException;

public class Main {

    /**
     * For now, this method only demos the SimpleMatrix class in action from EJML.
     * @param String[] args N/A
     */
    public static void main(String[] args) throws Exception, IOException {
        java.util.Scanner input = new java.util.Scanner(System.in);
        System.out.print("Enter simulation number to run (1-18), real-world (-1): ");
        int n = input.nextInt();
        new SimulationRunner(n);
    }

}
