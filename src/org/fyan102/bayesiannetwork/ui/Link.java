package org.fyan102.bayesiannetwork.ui;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class Link {
    private ArrayList<Point> points;
    private Path2D arrow;
    
    // Modern UI constants
    private static final Color LINE_COLOR = new Color(100, 100, 100);
    private static final Color ARROW_COLOR = new Color(100, 100, 100);
    private static final float LINE_WIDTH = 1.5f;
    private static final double ARROW_LENGTH = 12;
    private static final double ARROW_ANGLE = Math.PI / 8;

    public Link() {
        points = new ArrayList<>();
        arrow = new Path2D.Double();
    }

    public Link(Point... points) {
        this.points = new ArrayList<>();
        for (Point p : points) {
            this.points.add(p);
        }
        updateArrow();
    }

    private void updateArrow() {
        if (points.size() < 2) return;

        Point end = points.get(points.size() - 1);
        Point secondEnd = points.get(points.size() - 2);
        
        double dx = end.x - secondEnd.x;
        double dy = end.y - secondEnd.y;
        double theta = Math.atan2(dy, dx);
        
        arrow = new Path2D.Double();
        arrow.moveTo(end.x, end.y);
        
        double x1 = end.x - ARROW_LENGTH * Math.cos(theta + ARROW_ANGLE);
        double y1 = end.y - ARROW_LENGTH * Math.sin(theta + ARROW_ANGLE);
        arrow.lineTo(x1, y1);
        
        double x2 = end.x - ARROW_LENGTH * Math.cos(theta - ARROW_ANGLE);
        double y2 = end.y - ARROW_LENGTH * Math.sin(theta - ARROW_ANGLE);
        arrow.lineTo(x2, y2);
        
        arrow.closePath();
    }

    public void draw(Graphics2D g2d) {
        if (points.size() < 2) return;

        // Save original stroke and color
        Stroke originalStroke = g2d.getStroke();
        Color originalColor = g2d.getColor();

        // Set modern style
        g2d.setStroke(new BasicStroke(LINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(LINE_COLOR);

        // Draw the line
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g2d.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
        }

        // Draw the arrow
        g2d.setColor(ARROW_COLOR);
        g2d.fill(arrow);

        // Restore original stroke and color
        g2d.setStroke(originalStroke);
        g2d.setColor(originalColor);
    }

    public void addPoint(Point point) {
        points.add(point);
        updateArrow();
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
        updateArrow();
    }

    public ArrayList<Point> getPoints() {
        return points;
    }
}
