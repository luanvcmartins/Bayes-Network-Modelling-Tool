package AIToolkit.Bayes.Net.Sampling;

import AIToolkit.Bayes.Net.BayesNet;
import AIToolkit.Bayes.Net.BayesNetItem;
import AIToolkit.Supervisioned.KnowledgeBase.KnowledgeBaseItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

/**
 *
 * @author Luan
 */
public class RejectionSampling<T> extends Sampling<T> {

    public RejectionSampling(BayesNet network) {
        super(network);
    }

    @Override
    public SampleItem<T> getSample(HashMap<String, String> constraints) {
        TreeMap<BayesNetItem, String> attrValues = new TreeMap<>();
        TreeMap<String, BayesNetItem> attr = network.getAttributes();
        int generateTotal = attr.size();

        // We need to repeat this step until all attributes have a value
        while (generateTotal != 0) {
            //  System.out.println("Starting iteration: " + generateTotal);
            // We must pick one attribute to asign a value:
            for (String attrName : attr.keySet()) {
                HashSet<BayesNetItem> attrParents = attr.get(attrName).getParents();

                if (attrParents.isEmpty()) {
                    // We know this attr can be chosen when it has no parents
                    String value = raffleVariableValue(attrValues, attr.get(attrName));
                    attrValues.put(attr.get(attrName), value);

                    // Checking to see if sample still matches all contraints
                    if (constraints.containsKey(attrName) && !constraints.get(attrName).equals(value)) {
                        // It doesn't, we must return now
                        return null;
                    }
                } else {
                    // For this attr to be chosen we must already have asign a value
                    // to each of its' parents.
                    boolean useThis = true;
                    for (BayesNetItem attrParent : attrParents) {
                        if (!attrValues.containsKey(attrParent)) {
                            useThis = false;
                            break;
                        }
                    }
                    if (useThis) {
                        String value = raffleVariableValue(attrValues, attr.get(attrName));
                        attrValues.put(attr.get(attrName), value);

                        // We will now check if this sample matches the constraints 
                        // imposed by the user:
                        if (constraints.containsKey(attrName) && !constraints.get(attrName).equals(value)) {
                            // It doesn't, no sample is generated this instance.
                            return null;
                        }
                    }
                }
            }
            generateTotal = attrValues.size() - attr.size();
        }
        int listSize = network.getAttributes().size();
        ArrayList<T> sample = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            sample.add(null);
        }

        // It matches, so we can return it:
        for (BayesNetItem item : network.getAttributes().values()) {
            sample.set(item.getIndex(), (T) attrValues.get(item));
        }
        return new SampleItem<>(new KnowledgeBaseItem<>(sample, ""), 1.0);
    }
}
