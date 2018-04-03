package catworks.phenomena;

import java.lang.System;
import java.util.Random;

public class ProbThresholdPhenomena implements Phenomena {
  private static ProbabilityMatrix probMat;

  public ProbThresholdPhenomena(ProbabilityMatrix probMat){
    this.probMat = probMat;
  }

  /**
    * [propagate]
    * @param matrix Adjacency matrix represetnation of the Network_Output
    * @param start Array of 1's and 0's representing node affliction, there is
    *               an entry for each node in the array.
    */
  public int[] propagate(Integer[][] matrix, int[] start){
      int len = start.length;
      int[] rtn = new int[len];
      System.arraycopy(start,0,rtn,0,len);
      for (int i = 0; i < len; i++){
        if (start[i] == Phenomena.UNAFFLICTED) { //if node i is UNAFFLICTED
          for (int j = 0; j < len; j++) { //check if it's beyond the threshold

          }
        }
            if ((threshSum/neighbors) > threshold && start[i] != Phenomena.IMMUNE) { //if above threshold
              float p = new Random().nextFloat();
              for(int j = 0; j < len; j++){

              }
          }
      }
  }
}
