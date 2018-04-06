package catworks.phenomena;

import java.lang.System;
import java.util.Random;

public class ProbThreshPhenomena implements Phenomena{
  private static ProbabilityMatrix probMat;
  private static double [][] threshMat;

  public ProbThreshPhenomena(ProbabilityMatrix probMat, double [][] threshMat){
    this.probMat = probMat;
    this.threshMat = threshMat;
  }

  /**
   * [propagate]
   * @param matrix Adjacency matrix representation of the Network
   * @param start Array of 1's and 0's representing node affliction, there is
   *                an entry for each node in the array
   */
   public int[] propagate(int [][] matrix, int[] start){
     int len = start.length;
     int[] rtn = new int[len];
     int [] sizes = this.probMat.getSizes();
     System.arraycopy(start,0,rtn,0,len);
     for (int i = 0; i < len; i++){
       int xCoor = 0;
       for(int k = 0; k < sizes.length-1; k++){ //find the section "i" is in
         if(sizes [k] <= i && sizes[k+1] >= i){ //found bound for xCoor
          xCoor = k;
         }
       }
       if (start[i] == Phenomena.UNAFFLICTED){ //if node i is unafflicted
        for (int h = 0; h < sizes.length; h++){
          int threshSum = 0;
          int neighbors = 0;
          if(h+1 == sizes.length){ //last section of the adjacency matrix
            for(int j = sizes[h]; j < len; j++){ // go to the end of the matrix
              if(matrix[i][j] != 0){ //i adjacent to j
                neighbors++;
                if(start[j] == Phenomena.AFFLICTED){ //j is AFFLICTED
                  threshSum++;
                }
              }
            }
          }
          else{ //normal section of the adjacency matrix
            for(int j = sizes[h]; j < sizes[h+1]; j++){ // go to the next section start
              if(matrix[i][j] != 0){ // i adjacent to j
                neighbors++;
                if(start[j] == Phenomena.AFFLICTED){ //j is AFFLICTED
                  threshSum++;
                }
              }
            }
          }
          float p = new Random().nextFloat();
          if(p < this.probMat.getProbs()[xCoor][h]){
            rtn[i] = Phenomena.AFFLICTED;
          }
        }
       }
     }
   }

   @Override
   public String toString(){
     return "probabilistic threshold";
   }

   public String getType(){
     return "probabilistic threshold";
   }

   public void setThreshold(double threshold){
     return;
   }
}
