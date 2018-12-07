package catworks.simulations;

// Project import statements.
import catworks.networks.*;
import catworks.networks.metrics.*;
import catworks.phenomena.*;

// Additional import statements.
import java.io.IOException;
import java.util.LinkedList;
import java.lang.*;

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
		int flow;
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
					int s = size; 
					int t = size + 1;
					flow= fordFulkerson(this.network.getIntArrayMatrix(),s,t);
					
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
					int s = size - failedCount;
					int t = size - failedCount + 1;
					flow= fordFulkerson(this.network.getIntArrayMatrix(),s,t);
				}
				data[i][j]=flow;
			}
		}	
		return data;
	}
/*returns true if there is a path from source s to dest t in residual graph.  	Fills parent[] to store the path*/ 
	boolean bfs(int rGraph[][], int V,int s, int t, int parent[]){
		//create visited array and mark all vertices as not visited
		boolean visited[] = new boolean[V];
		for(int i =0; i< V; i+=1){
			visited[i] = false;
		}
		//create a queue, enqueue source vertex and mark it as visited
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(s);
		visited[s] = true;
		parent[s] = -1;
		//standard BFS
		while(queue.size()!=0){
			int u = queue.poll();
			for(int v = 0; v < V; v+=1){
				if(visited[v] == false && rGraph[u][v] > 0){
					queue.add(v);
					parent[v] = u;
					visited[v] = true;
				}
			}
		}
		//if we reached sink in BFS starting from source, return true
		return (visited[t] == true);
	}
	
	//returns the maximum flow from s to t in the given graph
	int fordFulkerson(int graph[][], int s, int t){
		int u,v; 
		int V = graph.length;
		//create residual graph and fill the residual graph with given
		//capacities
		//residual graph[i][j] indicates residual capacity of edge i to j
		int rGraph[][] = new int[V][V];
		for(u = 0; u < V; u+=1){
			for(v = 0; v < V; v+=1){
				rGraph[u][v] = graph[u][v];
			}
		}
		//this array is filled by BFS to store the path
		int parent[] = new int[V];
		int max_flow = 0;
		//Augment the flow while there is a path from source to sink with resi-
		//dual capactiy
		while(bfs(rGraph,V,s,t,parent)){
			//find minimum residual capacity of the edges along the path
			//found by BFS.  This equals maximum flow through that path
			int path_flow = Integer.MAX_VALUE;
			for(v=t; v!=s; v=parent[v]){
				u = parent[v];
				path_flow = Math.min(path_flow, rGraph[u][v]);
			}
			//update residual capacities of the edges and reverse edges
			for(v = t; v != s; v=parent[v]){
				u = parent[v];
				rGraph[u][v] -= path_flow;
				rGraph[v][u] += path_flow;
			}
			//add path flow to overall flow
			max_flow += path_flow;
		}
		return max_flow;
	}

}
