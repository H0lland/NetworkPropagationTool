package catworks.networks.metrics;

import catworks.networks.IDN;

public abstract class InterdependentCentrality extends AbstractCentrality {

    public abstract double[] getCentralities(IDN idn);
    public abstract int type();

}
