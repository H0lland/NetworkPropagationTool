package catworks.networks.metrics;

public class MyClosenessCentrality implements Centrality {

public Double[] getCentralities(Integer[][] matrix) {
  Integer [][] dist = floydWarshall(matrix);
  Double [] close = new Double [matrix.length];
  Double len = (double)matrix.length;
  for(int i = 0; i < dist.length; i++){
    Double sum = 0.0;
    for(int j = 0; j < dist.length; j++){
      if(j != i){
        sum += dist[i][j].doubleValue();
      }
    }
    close[i] = len/sum;
  }
  return close;
}

/**
 * [type description]
 * @return [description]
 */
public int type() {
    return Centrality.CLOSENESS;
}

private Integer[][] floydWarshall(Integer[][] matrix){
  Integer[][] dist = new Integer[matrix.length][matrix.length];
  System.arraycopy(matrix,0,dist,0,matrix.length);
  Integer inf = 10000;
  for(int i = 0; i < matrix.length; i++){
    for(int j = 0; j < matrix.length; j++){
      if(matrix[i][j] == 0 && i != j){
        dist[i][j] = inf;
      }
    }
  }
  for(int i = 0; i < dist.length; i++){
    for(int j = 0; j < dist.length; j++){
      for(int k = 0; k < dist.length; k++){
        if(dist[i][j] > (dist[i][k] + dist[k][j])){
          dist[i][j] = dist[i][k] + dist[k][j];
        }
      }
    }
  }
  return dist;
}

@Override
public String toString() {
    return "closeness";
}

}
