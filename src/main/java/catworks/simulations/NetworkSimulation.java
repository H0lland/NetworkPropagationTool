package catworks.simulations;

// Project import statements.
import catworks.networks.*;
import catworks.networks.metrics.*;
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

	/**
	* [runs iterations of the max-flow setup]
	* @param n	[the number of max-flow runs]
	**/
	public int[][] flowrun(int n){
		//centralities: [0, degree, closeness, betweenness, fT, fU]
		Centrality[] cents = {new DegreeCentrality(), new ClosenessCentrality(), new BetweennessCentrality()};
		int[][] data = new int[n][cents.length+1];
		for(int i =0; i< n; i+=1){
			for(int j=0; j<cents.length+1; j+=1){
				//construct a clone with the same adjacency matrix as network
				Network clone = this.network.clone();
				int size = clone.getNumOfNodes();
				//get the normal max flow so we have a baseline reading
				if(j==0){
					//add nodes for S and T
					clone.addNode();
					clone.addNode();
					int half = size/2;
					//attach S to all source nodes and from all dest nodes to T
					//each edge should have inf capacity
					for(int k = 0; k < half; k+=1){
						clone.addEdge(size,k,Integer.MAX_VALUE);
						clone.addEdge(k+half,size+1,Integer.MAX_VALUE);
					}
					//Run Ford-Fulkerson from S to T
				}
				//use the appropriate centrality metric
				else{
					//get most central nodes
					int [] nodes = clone.mostCentralNodes(cents[j-1], failedCount);
					//add nodes for S and T
					clone.addNode();
					clone.addNode();
					int half = size/2;
					//attach S to all source nodes and from all dest nodes to T
					//each edge should have inf capacity
					for(int k = 0; k < half; k+=1){
						clone.addEdge(size,k,Integer.MAX_VALUE);
						clone.addEdge(k+half,size+1,Integer.MAX_VALUE);
					}
					//remove most central nodes
					for(int l = 0; l < nodes.length; l+=1){
						clone.deleteNode(nodes[l]);
					}
					//run Ford-Fulkerson from S to T (reduce their indices, since you removed nodes)
				}
				//data[i][j]=flow;
			}
		}	
		return data;
	}
}
