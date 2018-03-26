package catworks;

import org.ejml.simple.SimpleMatrix;
import org.jblas.*;

public class Main {

    /**
     * For now, this method only demos the SimpleMatrix class in action from EJML.
     * @param String[] args N/A
     */
    public static void main(String[] args) {
        // Test the Network class, the DegreeCentrality class, and centrality methods.
        Integer[][] matrix = {
			{0, 0, 1, 1, 0},
			{0, 0, 1, 1, 0},
			{1, 1, 0, 1, 0},
			{1, 0, 1, 0, 0},
			{1, 1, 1, 1, 0},
		};
        Network network = new Network(100, .25);
        Centrality D = new DegreeCentrality();
        Centrality B = new BetweennessCentrality();
        Centrality E = new EigenvectorCentrality();

        // System.out.println("Adjacency Matrix for network:\n" + network);

        System.out.print("Betweenness Centrality of each node:\n");
        for (Number centrality : network.getCentralities(B))
            System.out.print(centrality + ", ");

        System.out.print("\n\nDegree Centrality of each node:\n");
        for (Number centrality : network.getCentralities(D))
            System.out.print(centrality + ", ");

        System.out.print("\n\nEigenvector Centrality of each node:\n");
        for (Number centrality : network.getCentralities(E))
            System.out.print(centrality + ", ");
    }

}
