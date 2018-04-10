package catworks.phenomena;

import java.lang.System;
import java.util.Random;

public class ProbThreshPhenomena implements Phenomena {
    private static ProbabilityMatrix probMat; //all the probabilities in this are the respective P_max values
    private static double [][] threshMat;

    public ProbThreshPhenomena(ProbabilityMatrix probMat, double [][] threshMat) {
        this.probMat = probMat;
        this.threshMat = threshMat;
    }

   /**
    * [propagate]
    * @param matrix Adjacency matrix representation of the Network
    * @param start Array of 1's and 0's representing node affliction, there is
    *                an entry for each node in the array
    */
   public int[] propagate(Integer [][] matrix, int[] start) {
       int len = start.length;
       int[] rtn = new int[len];
       int [] sizes = this.probMat.getSizes();
       System.arraycopy(start,0,rtn,0,len);

       // Loop through each node.
       for (int destNode = 0; destNode < len; destNode++) {
           int yCoor = 0;
           for (int k = 0; k < sizes.length-1; k++) { //find the section "destNode" is in
                if (sizes [k] <= destNode && sizes[k+1] >= destNode) { //found bound for yCoor
                    yCoor = k;
                }
            }
            if (start[destNode] == Phenomena.UNAFFLICTED) { //if node destNode is unafflicted

                for (int currSection = 0; currSection < sizes.length; currSection++) {
                    double threshSum = 0;
                    double neighbors = 0;

                    int upperBound;

                    // Find the upper bound for the current section, to avoid
                    // index out of bounds errors.
                    if (currSection+1 == sizes.length) { //last section of the adjacency matrix
                        upperBound = len;
                    }
                    else { //normal section of the adjacency matrix
                        upperBound = sizes[currSection+1];
                    }

                    // Count the number of neighborsa and the number of infected
                    // neighbors.
                    for (int srcNode = sizes[currSection]; srcNode < upperBound; srcNode++) { // go to the next section start
                        if (matrix[srcNode][destNode] != 0) { // srcNode adjacent to destNode
                            neighbors++;
                            if (start[srcNode] == Phenomena.AFFLICTED) { //srcNode is AFFLICTED
                                threshSum++;
                            }
                        }
                    }

                    // Check to make sure the threshold is met, if so, roll a die with
                    // a probability to fail it. If srcNode has no neighbors, then don't
                    // roll die to fail it.
                    boolean cond = (threshSum/neighbors) > threshMat[currSection][yCoor];
                    if (cond && neighbors != 0) { //above the threshhold
                        double p = Math.random();
                        double q = probMat.getProbs()[currSection][yCoor] * (threshSum/neighbors);
                        if (p <= q) {
                            rtn[destNode] = Phenomena.AFFLICTED;
                        }
                    }
                }
            }
        }
        return rtn;
    }

    @Override
    public String toString() {
        return "probabilistic_threshold";
    }

    public String getType() {
        return "probabilistic_threshold";
    }

    public void setThreshold(double threshold) {
        return;
    }
}
