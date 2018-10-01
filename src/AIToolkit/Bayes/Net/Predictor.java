package AIToolkit.Bayes.Net;

import java.util.TreeMap;

/**
 * Represents a predictor.
 *
 * @author Luan
 */
public class Predictor {

    private BayesNetItem target;
    private TreeMap<BayesNetItem, String> givenThat;

    public Predictor() {
        givenThat = new TreeMap<>();
    }

    public void setGivenThat(TreeMap<BayesNetItem, String> givenThat) {
        this.givenThat = givenThat;
    }

    public BayesNetItem getTarget() {
        return target;
    }

    public TreeMap<BayesNetItem, String> getGivenThat() {
        return givenThat;
    }

    public static Builder predict(BayesNetItem predict) {
        return new Builder(predict);
    }

    public void setTarget(BayesNetItem target) {
        this.target = target;
    }

    public static class Builder {

        private Predictor p;

        private Builder(BayesNetItem predict) {
            p = new Predictor();
            p.target = predict;
        }

        public Builder givenThat(BayesNetItem item, String value) {
            p.givenThat.put(item, value);
            return this;
        }

        public Predictor build() {
            return p;
        }
    }

}
