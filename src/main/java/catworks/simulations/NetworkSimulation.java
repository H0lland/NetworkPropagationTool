package catworks.simulations;

// Project import statements.
import catworks.networks.*;
import catworks.phenomena.*;

// Additional import statements.
import java.io.IOException;

/**
 *
 */
public class NetworkSimulation extends Simulation {

    private Network network;

    public NetworkSimulation(Network network, Phenomena phenomena, int timeSteps, int immuneCount, int failedCount) {
        this.network = network;
        this.phenomena = phenomena;
        this.timeSteps = timeSteps;
        this.immuneCount = immuneCount;
        this.failedCount = failedCount;

        runID = 0;
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
        IDN idn = new IDN(network);
        IDNSimulation sim = new IDNSimulation(idn, phenomena, timeSteps, immuneCount, failedCount, false);
        return sim.run(n);
    }

}
