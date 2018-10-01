package AIToolkit.Supervisioned.KnowledgeBase;

import java.util.Objects;

/**
 * Represents a header of the dataset. Useful to find attributes.
 *
 * @author Luan
 */
public class KnowledgeBaseHeader {

    private String title;
    private int index;

    public KnowledgeBaseHeader(String title, int index) {
        this.title = title;
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.title);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KnowledgeBaseHeader other = (KnowledgeBaseHeader) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        return true;
    }

}
