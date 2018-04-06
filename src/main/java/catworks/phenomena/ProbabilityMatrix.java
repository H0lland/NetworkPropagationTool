package catworks.phenomena;

import java.lang.System;

public class ProbabilityMatrix {
  private float [] sizes; //this contains the sizes for each network in the IDN
  private float [][] probs; //this contains the probability of spread for each "section" of the IDN

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
