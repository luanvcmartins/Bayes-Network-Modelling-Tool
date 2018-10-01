package AIToolkit.Bayes.Net;

import AIToolkit.Supervisioned.KnowledgeBase.KnowledgeBaseHeader;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashSet;

/**
 * Represents a attribute in the dataset.
 *
 * @author Luan
 */
public class BayesNetItem implements Comparable<BayesNetItem> {

    private final KnowledgeBaseHeader header;
    private TreeMap<String, Double> probabilityTable;
    private TreeMap<String, Integer> parentDefaultProbability;
    private final HashSet<BayesNetItem> parents;
    private HashSet<String> possibleValues;

    public BayesNetItem(KnowledgeBaseHeader attrHeader) {
        this.header = attrHeader;
        this.parents = new HashSet<>();
        this.parentDefaultProbability = new TreeMap<>();
    }

    //region PARENT MANAGEMENT
    public void addParent(BayesNetItem parent) {
        this.parents.add(parent);
    }

    public void removeParent(BayesNetItem parent) {
        this.parents.remove(parent);
    }

    public HashSet<BayesNetItem> getParents() {
        return parents;
    }
    //endregion

    //region Header data
    public String getTitle() {
        return header.getTitle();
    }

    public int getIndex() {
        return header.getIndex();
    }

    public void setPossibleValues(HashSet<String> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public void setParentDefaultProbability(TreeMap<String, Integer> parentDefaultProbability) {
        this.parentDefaultProbability = parentDefaultProbability;
    }

    public HashSet<String> getPossibleValues() {
        return possibleValues;
    }

    //endregion
    //region PROBABILITY
    public void setProbabilityTable(TreeMap<String, Double> table) {
        this.probabilityTable = table;
    }

    public double getProbability(TreeMap<BayesNetItem, String> items) {
        String parentKey = this.getParentItemName(items),
                keyValue = parentKey + getTitle() + ":" + items.get(this) + ";";

        if (parentKey != null) {
            // We return the value of the table or 1 / parent_size if we don't 
            // know we haven't counted the possibility.      
           // System.out.println("Consulting for " + getTitle() + ": " + parentKey);
            return probabilityTable.getOrDefault(keyValue,
                    (double) 1
                    / (parentDefaultProbability.getOrDefault(parentKey, 0)
                    + possibleValues.size()));
        } else {
            // We return MIN_VALUE as a flag to signify this query
            // is inappropriate:
            return Double.MIN_VALUE;
        }
    }

    public TreeMap<String, Double> getProbabilityTable() {
        return probabilityTable;
    }
    
    
    //endregion

    @Override
    public String toString() {
        return "{ title: " + header.getTitle() + " }";
    }

    private String getParentItemName(TreeMap<BayesNetItem, String> items) {
        StringBuilder s = new StringBuilder();
        for (BayesNetItem parent : parents) {
            if (items.containsKey(parent)) {
                s.append(parent.header.getTitle()).append(":").append(items.get(parent)).append(";");
            } else {
                // All items in the parent must have a associated value
                // in the query, otherwise this is not valid query for this table:
                return null;
            }
        }

     //   System.out.println("Name: " + s.toString());
        return s.toString();
    }

    @Override
    public int compareTo(BayesNetItem t) {
        return t.getTitle().compareTo(getTitle());
    }
}
