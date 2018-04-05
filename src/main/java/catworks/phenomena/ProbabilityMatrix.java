package catworks.phenomena;

import java.lang.System;

public class ProbabilityMatrix {
  private float [] sizes;
  private float [][] probs;

  public ProbabilityMatrix(float [] sizes, float [][] probs){
    this.sizes = sizes;
    this.probs = probs;
  }

  public float [] getSizes(){
    return sizes;
  }

  public float [][] getProbs(){
    return probs;
  }
}
