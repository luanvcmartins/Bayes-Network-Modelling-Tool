/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.Models;

import app.Components.GraphContainer;
import app.Components.JAttribute;

/**
 *
 * @author Luan
 */
public class LineData {
    private GraphContainer container;
    private JAttribute from, to;

    public LineData(JAttribute from, JAttribute to) {
        this.from = from;
        this.to = to;
    }

    public LineData(GraphContainer container, JAttribute from) {
        this.container = container;
        this.from = from;
    }

    public int getStartX() {
        return from.getLocation().x + (from.getWidth() / 2);
    }

    public int getEndX() {
        return to != null ? (to.getLocation().x + (to.getWidth() / 2)) 
                : container.getMousePosition() != null ? container.getMousePosition().x : 0;
    }

    public int getStartY() {
        return from.getLocation().y + from.getHeight();
    }

    public int getEndY() {
        return to != null ? to.getLocation().y : container.getMousePosition() != null ? container.getMousePosition().y : 0;
    }

    public JAttribute getFrom() {
        return from;
    }

    public JAttribute getTo() {
        return to;
    }

    
}
