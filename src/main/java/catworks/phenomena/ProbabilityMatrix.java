package catworks.phenomena;

public class ProbabilityMatrix {
    private int[] sizes; // this contains the sizes for each network in the IDN
    private double[][] probs; // this contains the probability of spread for each "section" of the IDN

    public ProbabilityMatrix(int[] sizes, double[][] probs) {
        this.sizes = sizes;
        this.probs = probs;
    }

    public int[] getSizes() {
        return sizes;
    }

    public double[][] getProbs() {
        return probs;
    }
}
