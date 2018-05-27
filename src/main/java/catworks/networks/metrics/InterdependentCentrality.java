package catworks.networks.metrics;

import catworks.networks.IDN;

public interface InterdependentCentrality {

    public double[] getCentralities(IDN idn);
    public int type();

}