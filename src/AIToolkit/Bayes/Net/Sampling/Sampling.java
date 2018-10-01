package AIToolkit.Bayes.Net.Sampling;

import AIToolkit.Bayes.Net.BayesNet;
import AIToolkit.Bayes.Net.BayesNetItem;
import AIToolkit.Supervisioned.KnowledgeBase.KnowledgeBaseItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Random;

/**
 *
 * @author Luan
 */
public abstract class Sampling<T> {

    private Random r;
    protected BayesNet network;

    public Sampling(BayesNet network) {
        this.network = network;
        this.r = new Random();
    }

    public abstract SampleItem<T> getSample(HashMap<String, String> constraints);

    public SampleItem<T> getSample() {
        return getSample(new HashMap<>());
    }

    public ArrayList<SampleItem<T>> getSampling(HashMap<String, String> constraints, int n) {
        ArrayList<SampleItem<T>> sampling = new ArrayList<>();
        while (sampling.size() < n) {
            SampleItem<T> sample = getSample(constraints);
            if (sample != null) {
                sampling.add(sample);
            }
        }
        return sampling;
    }

    public ArrayList<SampleItem<T>> getSampling(int n) {
        return getSampling(new HashMap<>(), n);
    }

    public String raffleVariableValue(TreeMap<BayesNetItem, String> parent, BayesNetItem attr) {
        LinkedHashMap<Double, String> valuesDistribution = new LinkedHashMap<>();
        double distributionSum = 0;
        for (String possibleValue : attr.getPossibleValues()) {
            parent.put(attr, possibleValue);
            distributionSum += attr.getProbability(parent);
            valuesDistribution.put(distributionSum, possibleValue);
        }
        double probability = r.nextDouble();

        Double lastValue = 0.0;
        for (Double value : valuesDistribution.keySet()) {
            if (probability < value) {
                return valuesDistribution.get(value);
            }
            lastValue = value;
        }
        return valuesDistribution.get(lastValue);
    }
}
