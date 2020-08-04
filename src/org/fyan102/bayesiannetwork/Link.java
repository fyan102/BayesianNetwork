package org.fyan102.bayesiannetwork;

import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;

public class Link extends Polyline {
    private Polygon arrow;

    public Link() {
        super();
        arrow = new Polygon();
    }

    public Link(double... points) {
        super(points);
        this.setStrokeWidth(1);
        this.arrow = getArrow(points);
    }

    private Polygon getArrow(double[] points) {
        Polygon arrow = new Polygon();
        double endX = points[points.length - 2];
        double endY = points[points.length - 1];
        double secondEndX = points[points.length - 4];
        double secondEndY = points[points.length - 3];
        double atanTheta = Math.atan((secondEndY - endY) / (secondEndX - endX));
        double theta = (endX - secondEndX) > 0 ?
                atanTheta : atanTheta + Math.PI;
        double longSide = 15 / Math.cos(Math.PI / 12);
        arrow.getPoints().addAll(endX, endY,
                endX - Math.cos(theta + Math.PI / 12) * longSide,
                endY - Math.sin(theta + Math.PI / 12) * longSide,
                endX - Math.cos(theta - Math.PI / 12) * longSide,
                endY - Math.sin(theta - Math.PI / 12) * longSide);
        return arrow;
    }

    public ArrayList<Node> getComponents() {
        ArrayList<javafx.scene.Node> components = new ArrayList<>();
        components.add(this);
        components.add(this.arrow);
        return components;
    }
}
