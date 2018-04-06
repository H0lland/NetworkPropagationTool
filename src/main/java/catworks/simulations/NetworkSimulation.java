package catworks.simulations;

// Project import statements.
import catworks.networks.*;
import catworks.networks.metrics.*;
import catworks.phenomena.*;

// Additional import statements.
import java.util.Arrays;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 *
 */
public class NetworkSimulation extends Simulation {

    private Network network;

    public NetworkSimulation(Network network, Phenomena phenomena, int timeSteps, double immuneFraction, double infectFraction) {
        this.network = network;
        this.phenomena = phenomena;
        this.timeSteps = timeSteps;
        this.immuneFraction = immuneFraction;
        this.infectFraction = infectFraction;

        runID = 0;
        immuneCount = (int) (network.getNumOfNodes() * immuneFraction);
        simulationID++;
    }


    /**
     * [run description]
     * @param  n           [description]
     * @return             [description]
     * @throws Exception   [description]
     * @throws IOException [description]
     */
    public Object[][] run(int n) throws Exception, IOException, IllegalArgumentException {
        ArrayList<Network> networks = new ArrayList();
        networks.add(network);

        IDN idn = new IDN(networks);
        IDNSimulation sim = new IDNSimulation(idn, phenomena, timeSteps, immuneFraction, infectFraction, false);
        return sim.run(n);
    }

}
