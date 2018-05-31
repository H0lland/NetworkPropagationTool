package catworks.networks.metrics;

import catworks.networks.IDN;

public interface InterdependentCentrality {

    final int WBCCentrality = 0;

    public double[] getCentralities(IDN idn);
    public int type();

}
