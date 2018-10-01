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
public class PriorSampling<T> extends Sampling<T> {

    public PriorSampling(BayesNet network) {
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
                    attrValues.put(attr.get(attrName), raffleVariableValue(attrValues, attr.get(attrName)));
                } else {
                    // For this attr to be chosen we must already have asign a value
                    // to each of its' parents.

                    boolean useThis = true;
                    TreeMap<BayesNetItem, String> parent = new TreeMap<>();
                    for (BayesNetItem attrParent : attrParents) {
                        if (!attrValues.containsKey(attrParent)) {
                            useThis = false;
                            break;
                        } else {
                            parent.put(attrParent, attrValues.get(attrParent));
                        }
                    }
                    if (useThis) {
                        attrValues.put(attr.get(attrName), raffleVariableValue(attrValues, attr.get(attrName)));
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
        for (BayesNetItem item : network.getAttributes().values()) {
            sample.set(item.getIndex(), (T) attrValues.get(item));
        }

        return new SampleItem<>(new KnowledgeBaseItem<>(sample, ""), 1.0);
    }
}
