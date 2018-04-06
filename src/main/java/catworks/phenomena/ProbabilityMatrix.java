package catworks.phenomena;

import java.lang.System;

public class ProbabilityMatrix {
  private int [] sizes; //this contains the sizes for each network in the IDN
  private float [][] probs; //this contains the probability of spread for each "section" of the IDN

  public ProbabilityMatrix(int [] sizes, float [][] probs){
    this.sizes = sizes;
    this.probs = probs;
  }

  public int [] getSizes(){
    return sizes;
  }

  public float [][] getProbs(){
    return probs;
  }
}
