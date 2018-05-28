package catworks.networks.metrics;

import catworks.networks.IDN;

public class WBCCentrality implements InterdependentCentrality{

  /**
  * Return the Weighted Boundary Closeness Centarlities of a given idn
  * @param idn the IDN for which we find the getCentralities
  */
  public double[] getCentralities(IDN idn){

  }

  public int type(){
    return InterdependentCentrality.WBCCentrality;
  }
}
