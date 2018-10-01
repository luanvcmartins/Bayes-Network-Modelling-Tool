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
public class LikelihoodWeightSampling<T> extends Sampling<T> {

    public LikelihoodWeightSampling(BayesNet network) {
        super(network);
    }

    @Override
    public SampleItem<T> getSample(HashMap<String, String> constraints) {
        TreeMap<BayesNetItem, String> attrValues = new TreeMap<>();
        TreeMap<String, BayesNetItem> attr = network.getAttributes();
        int generateTotal = attr.size();
        double weight = 1;
//        System.out.println("Constraint " + constraints);

        // We need to repeat this step until all attributes have a value
        while (generateTotal != 0) {
            //  System.out.println("Starting iteration: " + generateTotal);
            // We must pick one attribute to asign a value:
            for (String attrName : attr.keySet()) {
                HashSet<BayesNetItem> attrParents = attr.get(attrName).getParents();

                // This attribute must be raffled normaly:
                if (attrParents.isEmpty()) {
                    // We know this attr can be chosen when it has no parents
                    if (constraints.containsKey(attrName)) {
                        attrValues.put(attr.get(attrName), constraints.get(attrName));
                        weight *= network.getAttribute(attrName).getProbability(attrValues);
//                        System.out.println(" -> Weight becomes " + weight + " because " + network.getAttribute(attrName).getProbability(attrValues));
                    } else {
                        attrValues.put(attr.get(attrName), raffleVariableValue(attrValues, attr.get(attrName)));
                    }

//                    System.out.println(attrName + " receives value " + attrValues.get(attr.get(attrName)) + " (constrained: " + constraints.containsKey(attrName) + ")");
                } else {
                    // For this attr to be chosen we must already have asign a value
                    // to each of its' parents.
//                    System.out.println(attrName + " have parents, we must check if we know their value.");
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
//                        System.out.println("Raffling for " + attrName + " with parent equals to " + parent);
                        if (constraints.containsKey(attrName)) {
                            attrValues.put(attr.get(attrName), constraints.get(attrName));
                            weight *= network.getAttribute(attrName).getProbability(attrValues);
//                            System.out.println(" -> Weight becomes " + weight + " because " + network.getAttribute(attrName).getProbability(attrValues));
                        } else {
                            attrValues.put(attr.get(attrName), raffleVariableValue(attrValues, attr.get(attrName)));
                        }

//                        System.out.println(attrName + " had parents, but they all have values, receives value " + attrValues.get(attr.get(attrName)));
                    } 
//                    else {
//                        System.out.println("We can't use " + attrName + " yet.");
//                    }
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

        return new SampleItem<>(new KnowledgeBaseItem<>(sample, ""), weight);
    }
}
