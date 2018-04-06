package catworks.phenomena;

import java.lang.System;
import java.util.Random;

public class ProbPhenomena implements Phenomena {
  private static ProbabilityMatrix probMat;

  public ProbPhenomena(ProbabilityMatrix probMat){
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
      float [] sizes = this.probMat.getSizes();  //get the sizes of all networks in idn
      System.arraycopy(start,0,rtn,0,len);
      for (int i = 0; i < len; i++){
        if (start[i] == Phenomena.UNAFFLICTED) { //if node i is UNAFFLICTED
              for(int j = 0; j < len; j++){
                if (matrix[i][j] != 0){ //node i is adjacent to node j
                  int xCoor = 0;
                  int yCoor = 0;
                  for(int k = 0; k < sizes.length; k++){ //try to bound which networks i and j are in to know the correct probability
                    if(sizes[k] <= i && sizes[k] >= i){ //found bound for xCoor
                      xCoor = k;
                    }
                    if(sizes[k] <=   j && sizes[k] >= j){ //found bound for yCoor
                      yCoor = k;
                    }
                  }
                  float p = new Random().nextFloat();
                  if(p < this.probMat.getProbs()[xCoor][yCoor]){ // if the generated number less than the "infectiousness" of that region
                    rtn[i] = Phenomena.AFFLICTED; // infect node i
                  }
                }
              }
          }
      }
      return rtn;
  }

  @Override
  public String toString(){
    return "probabilistic";
  }

  public String getType(){
    return "probabilistic";
  }

  public void setThreshold(double threshold){
    return;
  }
}
