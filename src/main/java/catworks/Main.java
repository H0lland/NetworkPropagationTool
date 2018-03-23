package catworks;

import org.ejml.simple.SimpleMatrix;
import org.jblas.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    /**
     * For now, this method only demos the SimpleMatrix class in action from EJML.
     * @param String[] args N/A
     */
    public static void main(String[] args) {
    /*    // Declare data to fill matrices.
        double[][] data1 = {{.1, .2, .3}, {.4, .5, .6}, {.7, .8, .9}};
		double[][] data2 = {{.7, .8, .9}, {.4, .5, .6}, {.1, .2, .3}};

        // Declare matrices, perform some linear calculations, print result.
        SimpleMatrix H = new SimpleMatrix(data1);
		SimpleMatrix P = new SimpleMatrix(data2);
		SimpleMatrix S = H.mult(P).mult(H.transpose());
		System.out.println(S);

        // Test the ThreeTuple class.
        InterEdge edge = new InterEdge(1, 4, 2, 6);
        System.out.println(edge);

        // Test the Network class, the DegreeCentrality class, and centrality methods.
        Integer[][] matrix = {
			{0, 0, 1, 1, 0},
			{0, 0, 1, 1, 0},
			{1, 1, 0, 1, 0},
			{1, 0, 1, 0, 0},
			{1, 1, 1, 1, 0},
		};
        Network network = new Network(25, .05);// Network(matrix);
        Centrality D = new DegreeCentrality();
        System.out.println("Adjacency Matrix for network:\n" + network);

        System.out.print("Centrality of each node:\n");
        for (Number centrality : network.getCentralities(D))
            System.out.print(centrality + "  ");

        System.out.print("\n\n3 Most Central Node Indices:\n");
        for (Integer index : network.mostCentralNodes(D, 3))
            System.out.print(index + "  ");

        System.out.print("\n\n3 Least Central Node Indices:\n");
        for (Integer index : network.leastCentralNodes(D, 3))
            System.out.print(index + "  ");

        // Attempt to use JBLAS library.
        DoubleMatrix dm = new DoubleMatrix(4, 5);
        System.out.println("\n\nJBLAS Library Sample:\n" + dm);*/
        ArrayList<Network> nets = new ArrayList<Network>();
        int [] [] line = {{0,1},{0,0}};
        int [] [] dot = {{0}};

        Network net1 = new Network(line);
        nets.add(net1);
        Network net2 = new Network(dot);
        nets.add(net2);
        IDN myIDN = new IDN(nets);
        InterEdge inter = new InterEdge(0,1,1,0);
        myIDN.addInterEdge(inter);
        Network net3 = myIDN.bridge();
        System.out.print(Arrays.toString(net3.getMatrix().toArray()));

    }

}
