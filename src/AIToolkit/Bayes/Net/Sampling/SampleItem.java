/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AIToolkit.Bayes.Net.Sampling;

import AIToolkit.Supervisioned.KnowledgeBase.KnowledgeBaseItem;
import java.util.Map;

/**
 *
 * @author Luan
 */
public class SampleItem<T> implements Map.Entry<KnowledgeBaseItem<T>, Double> {

    private final KnowledgeBaseItem<T> entry;
    private double weight;

    public SampleItem(KnowledgeBaseItem<T> entry, double weight) {
        this.entry = entry;
        this.weight = weight;
    }

    @Override
    public KnowledgeBaseItem<T> getKey() {
        return entry;
    }

    @Override
    public Double getValue() {
        return weight;
    }

    @Override
    public Double setValue(Double v) {
        this.weight = v;
        return v;
    }

}
