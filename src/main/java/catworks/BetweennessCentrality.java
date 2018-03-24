package catworks;
import java.util.*;


public class BetweennessCentrality implements Centrality {

    /**
     * Gets the centrality specifically for finding the Betweenness of the nodes in the matrix
     * @param  Integer[][] matrix        [description]
     * @return             [description]
     */

    private static final int NO_PARENT = -1;

    public Integer[] getCentralities(Integer[][] matrix) {

        //label for the vertex, centrality, and paths given
        System.out.print("Vertex\t Distance\tPath");
        int size = matrix[0].length;
        //initializing something to count nodes a part of shortest path
        Integer[] betweenness_counter = new Integer [size];
        Arrays.fill(betweenness_counter, 0);

        for (int focus_vertex = 0; focus_vertex < size; focus_vertex++){

            int nVertices = matrix[0].length;

            // shortestDistances[i] will hold the
            // shortest distance from src to i
            int[] shortestDistances = new int[nVertices];

            // added[i] will true if vertex i is
            // included / in shortest path tree
            // or shortest distance from src to
            // i is finalized
            boolean[] added = new boolean[nVertices];


            // Initialize all distances as
            // INFINITE and added[] as false
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++)
            {
                shortestDistances[vertexIndex] = Integer.MAX_VALUE;
                added[vertexIndex] = false;
            }

            // Distance of source vertex from
            // itself is always 0
            shortestDistances[focus_vertex] = 0;

            // Parent array to store shortest
            // path tree
            int[] parents = new int[nVertices];

            // The starting vertex does not
            // have a parent
            parents[focus_vertex] = NO_PARENT;

            // Find shortest path for all
            // vertices
            for (int i = 1; i < nVertices; i++)
            {

                // Pick the minimum distance vertex
                // from the set of vertices not yet
                // processed. nearestVertex is
                // always equal to startNode in
                // first iteration.
                int nearestVertex = -1;
                int shortestDistance = Integer.MAX_VALUE;
                for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++)
                {
                    if (!added[vertexIndex] &&
                        shortestDistances[vertexIndex] < shortestDistance)
                    {
                        nearestVertex = vertexIndex;
                        shortestDistance = shortestDistances[vertexIndex];
                    }
                }
                // Mark the picked vertex as
                // processed
                added[nearestVertex] = true;

                // Update dist value of the
                // adjacent vertices of the
                // picked vertex.
                for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++)
                {
                    int edgeDistance = matrix[nearestVertex][vertexIndex];

                    if (edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[vertexIndex]))
                    {
                        parents[vertexIndex] = nearestVertex;
                        shortestDistances[vertexIndex] = shortestDistance + edgeDistance;
                    }
                }
            }

            //prints paths of the matrix - can be taken out when debugging is done
            printSolution(focus_vertex, shortestDistances, parents, betweenness_counter);
        }

        //subtracts the four instances when the betweenness value will be the ending value of the path
        for (int i = 0; i < size; i++){
            betweenness_counter[i] = betweenness_counter[i] - 4;
        }

        System.out.print("\n \nBetweenness Centrality: ");
        return betweenness_counter;

    }

    /**
     * A utility function to print ther constructed distances array and shortest paths
     * @param  int       startVertex         keeps track of the node that the path stems from
     * @param  int       currentVertex       keeps track of the node being focused on
     * @param  int[]     parents             keeps track of the nodes within the shortest path
     * @param  Integer[] betweenness_counter keeps track of how many times thef node within the shortest paths appear
     */
    private static void printSolution(int startVertex, int[] distances, int[] parents, Integer[] betweenness_counter)
    {
        int nVertices = distances.length;
        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++)
        {
            if (vertexIndex != startVertex)
            {
                System.out.print("\n" + startVertex + " -> ");
                System.out.print(vertexIndex + " \t\t ");
                System.out.print(distances[vertexIndex] + "\t\t");
                printPath(startVertex, vertexIndex, parents, betweenness_counter);
            }
        }
    }


    /**
     * Function to print shortest path from source to currentVertex using parent array
     * @param  int       startVertex         keeps track of the node that the path stems from
     * @param  int       currentVertex       keeps track of the node being focused on
     * @param  int[]     parents             keeps track of the nodes within the shortest path
     * @param  Integer[] betweenness_counter keeps track of how many times thef node within the shortest paths appear
     * @return           returns the betweenness counter
     */
    private static Integer[] printPath(int startVertex, int currentVertex, int[] parents, Integer[] betweenness_counter)
    {
        // Base case : Source node has
        // been processed
        if (currentVertex == NO_PARENT)
        {
            return betweenness_counter;
        }
        printPath(startVertex, parents[currentVertex], parents, betweenness_counter);

        if (currentVertex != startVertex){
            betweenness_counter[currentVertex]++;
        }
        System.out.print(currentVertex + " ");
        return betweenness_counter;
    }


}
