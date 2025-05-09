package org.fyan102.bayesiannetwork.ui;

import org.fyan102.bayesiannetwork.ui.config.UIConfig;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Link {
    private final NodeView from;
    private final NodeView to;
    private final Color color;
    private final Stroke stroke;
    private final int arrowSize = 10;

    public Link(NodeView from, NodeView to) {
        this.from = from;
        this.to = to;
        this.color = UIConfig.BORDER_COLOR;
        this.stroke = new BasicStroke(2.0f);
    }

    public NodeView getFrom() {
        return from;
    }

    public NodeView getTo() {
        return to;
    }

    public void draw(Graphics2D g2d) {
        Point2D fromPoint = from.getConnectionPoint(to.getCenter());
        Point2D toPoint = to.getConnectionPoint(from.getCenter());

        // Draw the line
        g2d.setColor(color);
        g2d.setStroke(stroke);
        g2d.draw(new Line2D.Double(fromPoint, toPoint));

        // Draw the arrow
        double angle = Math.atan2(toPoint.getY() - fromPoint.getY(), toPoint.getX() - fromPoint.getX());
        double arrowAngle1 = angle - Math.PI / 6;
        double arrowAngle2 = angle + Math.PI / 6;

        int[] xPoints = new int[3];
        int[] yPoints = new int[3];

        xPoints[0] = (int) toPoint.getX();
        yPoints[0] = (int) toPoint.getY();

        xPoints[1] = (int) (toPoint.getX() - arrowSize * Math.cos(arrowAngle1));
        yPoints[1] = (int) (toPoint.getY() - arrowSize * Math.sin(arrowAngle1));

        xPoints[2] = (int) (toPoint.getX() - arrowSize * Math.cos(arrowAngle2));
        yPoints[2] = (int) (toPoint.getY() - arrowSize * Math.sin(arrowAngle2));

        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    public boolean contains(Point point) {
        Point2D fromPoint = from.getConnectionPoint(to.getCenter());
        Point2D toPoint = to.getConnectionPoint(from.getCenter());
        Line2D line = new Line2D.Double(fromPoint, toPoint);
        return line.ptLineDist(point) < 5;
    }
}
