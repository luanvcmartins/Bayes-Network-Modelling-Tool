package AIToolkit.Bayes.Net;

import AIToolkit.Supervisioned.KnowledgeBase.KnowledgeBase;
import AIToolkit.Supervisioned.KnowledgeBase.KnowledgeBaseHeader;
import AIToolkit.Supervisioned.KnowledgeBase.KnowledgeBaseItem;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luan
 */
public class BayesNet {

    private final KnowledgeBase<String> dataset;
    private final TreeMap<String, BayesNetItem> attributes;

    // NETWORK INSTANTIATION
    public BayesNet(KnowledgeBase<String> dataset, TreeMap<String, BayesNetItem> attributes) {
        this.dataset = dataset;
        this.attributes = attributes;
    }

    public static BayesNet instanciateFrom(KnowledgeBase<String> dataset) {
        TreeMap<String, BayesNetItem> attr = new TreeMap<>();

        // Now we must add the attribute list to the bayes network:
        for (KnowledgeBaseHeader header : dataset.getHeaders()) {
            attr.put(header.getTitle(), new BayesNetItem(header));
        }

        // There is no point calculating the marginal and conditional 
        // probabilities here as we don't have the network struct yet.
        return new BayesNet(dataset, attr);
    }
    //endregion NETWORK INSTANTIATION

    public TreeMap<String, BayesNetItem> getAttributes() {
        return attributes;
    }

    public BayesNetItem getAttribute(String title) {
        return attributes.get(title);
    }

    public void setAsParent(String titleChild, String titleParent) {
        attributes.get(titleChild).addParent(attributes.get(titleParent));
    }

    /**
     * Call this method to request the network to be synced so the marginal and
     * conditional probabilities may be calculated again.
     */
    public void syncNetwork() {
        TreeMap<BayesNetItem, HashSet<String>> attrValues = new TreeMap<>();
        ArrayList<KnowledgeBaseItem<String>> datasetItems = dataset.getItems();

        // First setp is to run the entire dataset and figure out what are the
        // possible values each attribute can receive. There is a limitation
        // with this approach: we don't know if there are more classes that
        // doesn't have any representation here:
        for (BayesNetItem attr : attributes.values()) {
            attrValues.put(attr, new HashSet<>());
            for (KnowledgeBaseItem<String> item : datasetItems) {
                attrValues.get(attr)
                        .add(item.getItem(attr.getIndex()));
            }
            attr.setPossibleValues(attrValues.get(attr));
        }
        Thread[] t = new Thread[attributes.values().size()];
        int tIndex = 0;

        // Now, we may calculate the probability table of each attribute:
        for (BayesNetItem attr : attributes.values()) {
            t[tIndex] = new Thread(() -> {
                System.out.println("Training started for " + attr.getTitle() + ".");
                TreeMap<String, Integer> attrProbabilityCounter = new TreeMap<>();
                HashSet<BayesNetItem> attrParents = attr.getParents();
                if (attrParents.isEmpty()) {
                    // This is a marginal probability. We start by scanning 
                    // all rows of the dataset:
                    int line = 0;
                    for (KnowledgeBaseItem<String> data : datasetItems) {
                        String cItem = data.getItem(attr.getIndex());
                        attrProbabilityCounter.put(cItem,
                                attrProbabilityCounter.getOrDefault(cItem, 0) + 1);
                    }

                    // Once the entire dataset has been read for this attribute, we 
                    // must finally calculate the probability, as we have all the data
                    // we need:
                    TreeMap<String, Double> attrProbabilityTable = new TreeMap<>();
                    for (String attrValue : attrValues.get(attr)) {
                        String tableHeader = attr.getTitle() + ":" + attrValue + ";";
                        attrProbabilityTable.put(tableHeader,
                                (double) (1 + attrProbabilityCounter.getOrDefault(attrValue, 0))
                                / (datasetItems.size() + attrValues.size()));
                    }

                    attr.setProbabilityTable(attrProbabilityTable);

                    System.out.println("End of calculation for " + attr.getTitle() + " = " + attrProbabilityTable);
                } else {
                    // This attribute requres a conditional probability 
                    // because it has parents.

                    // parentProbabilityCounter will only count the frequence of elements up
                    // to the parents of this attribute. probabilityCounter itself will
                    // count the frequence of the item as well as the parent.
                    TreeMap<String, Integer> parentProbabilityCounter = new TreeMap<>();
                    TreeMap<String, Integer> probabilityCounter = new TreeMap<>();

                    // This way might be a little more convoluted, but at least we 
                    // ensure that the dataset will only be read once per attribute
                    // by doing this way.
                    for (KnowledgeBaseItem<String> data : datasetItems) {
                        // For each line, we will only count the frequence of sequences
                        // that actually happens:
                        StringBuilder parentTitleSB = new StringBuilder();

                        // We must first generate the table's title, which is 
                        // composed of the actual value of this row:
                        for (BayesNetItem parent : attr.getParents()) {
                            parentTitleSB
                                    .append(parent.getTitle())
                                    .append(":")
                                    .append(data.getItem(parent.getIndex()))
                                    .append(";");
                        }

                        String parentTitle = parentTitleSB.toString();
                        parentProbabilityCounter.put(parentTitle,
                                parentProbabilityCounter.getOrDefault(parentTitle, 0) + 1);
                        String childTitle = parentTitle + attr.getTitle() + ":" + data.getItem(attr.getIndex()) + ";";
                        probabilityCounter.put(childTitle, probabilityCounter.getOrDefault(childTitle, 0) + 1);
                    }

                    // We read the dataset and know all the frequences, we must now
                    // calculate the probability:
                    TreeMap<String, Double> probabilityTable = new TreeMap<>();
                    // For each one of the parents possible combination found:
                    for (String parentKey : parentProbabilityCounter.keySet()) {
                        // We hold its' name and verify every possible value for
                        // the current attribute:
                        for (String possibleValue : attr.getPossibleValues()) {
                            String childKey = parentKey + attr.getTitle() + ":" + possibleValue + ";";

                            // We are only interested in counting when this 
                            // combination have been found, otherwise we will 
                            // return a default value.
                            if (probabilityCounter.containsKey(childKey)) {
                                System.out.println(childKey + " = " + ((double) (probabilityCounter.get(childKey) + 1)
                                        / (double) (parentProbabilityCounter.get(parentKey) + attr.getPossibleValues().size()))
                                        + ", because parent (" + parentKey + ") is: "
                                        + (parentProbabilityCounter.get(parentKey) + attr.getPossibleValues().size())
                                        + " and frequence is " + (probabilityCounter.get(childKey) + 1));
                                probabilityTable.put(childKey,
                                        ((double) (probabilityCounter.get(childKey) + 1)
                                        / (double) (parentProbabilityCounter.get(parentKey) + attr.getPossibleValues().size())));
                                attr.setParentDefaultProbability(parentProbabilityCounter);
                            }
                        }
                    }

                    System.out.println("End of calculation for " + attr.getTitle() + " = " + probabilityTable);
                    attr.setProbabilityTable(probabilityTable);
                }
                System.out.println("Training done for " + attr.getTitle() + ".");
            });
            t[tIndex++].start();
        }
        for (Thread c : t) {
            try {
                c.join();
            } catch (Exception ignored) {
            }
        }
    }

    public String runPrediction(Predictor predictor) {
        TreeMap<String, Double> p = predict(predictor);
        
        double value = Double.MIN_VALUE;
        String classAnswer = "";
        for (String className : p.keySet()) {
            if (value < p.get(className)) {
                value = p.get(className);
                classAnswer = className;
            }
        }
        return classAnswer;
    }

    public TreeMap<String, Double> predict(Predictor predictor) {

        TreeMap<String, Double> probability = new TreeMap<>();
        double pSum = 0;
        
        for (String ofValue : predictor.getTarget().getPossibleValues()) {
            double pOfValue = 1;
            TreeMap<BayesNetItem, String> givenThat = predictor.getGivenThat();
            givenThat.put(predictor.getTarget(), ofValue);

            for (BayesNetItem item : attributes.values()) {
                double value = item.getProbability(givenThat);
                pOfValue *= value == Double.MIN_VALUE ? 1 : value;
                System.out.println("[" + ofValue + "] " + item.getTitle() + ":" + givenThat.get(item) + " = " + value);
            }
            probability.put(ofValue, pOfValue);
            System.out.println(predictor.getTarget().getTitle() + ":" + ofValue + ": " + pOfValue);
            pSum += pOfValue;
        }
        System.out.println("pSum: " + pSum);
        for (String ofValue : probability.keySet()) {
            probability.put(ofValue, probability.get(ofValue) / pSum);
        }

        return probability;
    }
}
