package catworks;

import java.lang.System.*;

public class EpidemicPhenomena implements Phenomena{

  public EpidemicPhenomena(){}
  /**
    * [propagate]
    * @param matrix Adjacency matrix representation of the Network
    * @param start Array of 1's and 0's representing node affliction, there is
    *              an entry for each node in the array.
    */
    public int[] propagate(Integer[][] matrix, int[] start){
      int len = start.length;
      int[] rtn = new int[len];
      System.arraycopy(start,0,rtn,0,len);
      for(int i = 0; i < len; i += 1){
        if(start[i]==1){ //if node i is afflicted
          for(int j = 0; j < len; j+= 1){ //afflict its neighbors
            if(matrix[i][j]==1){//if node i neighbors node j
              rtn[j]=1; //affect the neighbor
            }
          }
        }
      }
      return rtn;
    }
}
