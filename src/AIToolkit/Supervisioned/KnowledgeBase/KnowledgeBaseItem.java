package AIToolkit.Supervisioned.KnowledgeBase;

import java.util.ArrayList;

/**
 * Represents an example or an item in the knowledge base.
 * 
 * @author luan
 */
public class KnowledgeBaseItem<T> {

    private String name;
    private ArrayList<T> items;
    private String className;

    public KnowledgeBaseItem() {
    }

    public KnowledgeBaseItem(ArrayList<T> items, String className) {
        this.items = items;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public T getItem(int index) {
        return items.get(index);
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return " { name: " + className + ", values: " + items + "} ";
    }

}
