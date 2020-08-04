package org.fyan102.bayesiannetwork;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class NodeView extends Rectangle {
    private Label title;
    private ArrayList<Label> states;
    private Line border;
    private Line divide;
    private ArrayList<Rectangle> percents;
    private Node node;
    private double x1;
    private double y1;
    private double x_stage;
    private double y_stage;

    public NodeView() {
        title = new Label();
        states = new ArrayList<>();
        divide = new Line();
        border = new Line();
        percents = new ArrayList<>();
        node = new Node();
        init();
    }

    public NodeView(double x, double y, double width, double height) {
        super(x, y, width, height);
        title = new Label();
        states = new ArrayList<>();
        divide = new Line();
        border = new Line();
        percents = new ArrayList<>();
        node = new Node();
        init();
    }

    public NodeView(Node node) {
        super(200, 150, 250, 60);
        this.node = node;
        title = new Label();
        states = new ArrayList<>();
        divide = new Line();
        border = new Line();
        percents = new ArrayList<>();
        init();
        setEvents();
    }

    public Line getBorder() {
        return border;
    }

    public ArrayList<javafx.scene.Node> getComponents() {
        ArrayList<javafx.scene.Node> components = new ArrayList<>();
        components.add(this);
        components.add(title);
        components.addAll(states);
        components.add(border);
        components.add(divide);
        components.addAll(percents);
        return components;
    }

    public Line getDivide() {
        return divide;
    }

    public Node getNode() {
        return node;
    }

    public ArrayList<Rectangle> getPercents() {
        return percents;
    }

    public ArrayList<Label> getStates() {
        return states;
    }

    public Label getTitle() {
        return title;
    }

    public void init() {
        title.setText(node.getName());
        title.setLayoutX(this.getX() + 5);
        title.setLayoutY(this.getY());
        this.setHeight((node.getNumberOfStates() + 1) * 20);
        for (int i = 0; i < node.getNumberOfStates(); i++) {
            Label state = new Label(node.getState(i) + "\t\t" + String.format("%.4f", node.getBelief(i)) + "\t");
            state.setLayoutX(this.getTitle().getLayoutX());
            state.setLayoutY(this.getTitle().getLayoutY() + 20 * (i + 1));
            states.add(state);
            Rectangle rectangle = new Rectangle(getX() + 100, getY() + (i + 1) * 20 + 5,
                    100 * node.getBelief(i), 10);
            percents.add(rectangle);
        }
        border = new Line(this.getX(), this.getY() + 20, this.getX() + this.getWidth(),
                this.getY() + 20);
        this.setFill(Color.YELLOW);
        this.setStroke(Color.BLACK);
    }

    public void repaint() {
        title.setLayoutX(this.getX() + 5);
        title.setLayoutY(this.getY());

        for (int i = 0; i < states.size(); i++) {
            states.get(i).setText(node.getState(i) + "\t\t" + String.format("%.4f", node.getBelief(i)) + "\t");
            states.get(i).setLayoutX(this.getTitle().getLayoutX());
            states.get(i).setLayoutY(this.getTitle().getLayoutY() + 20 * (i + 1));
            percents.get(i).setHeight(10);
            percents.get(i).setWidth(100 * node.getBelief(i));
            percents.get(i).setX(this.getX() + 100);
            percents.get(i).setY(this.getY() + (i + 1) * 20 + 5);
        }
        border.setStartX(this.getX());
        border.setStartY(this.getY() + 20);
        border.setEndX(this.getX() + this.getWidth());
        border.setEndY(this.getY() + 20);
    }

    public void setBorder(Line border) {
        this.border = border;
    }

    public void setDivide(Line divide) {
        this.divide = divide;
    }

    private void setEvents() {
        this.setOnMouseDragged(m ->
        {
            this.setX(x_stage + m.getScreenX() - x1);
            this.setY(y_stage + m.getScreenY() - y1);
            repaint();
        });
        this.setOnMousePressed(m ->
        {
            x1 = m.getScreenX();
            y1 = m.getScreenY();
            x_stage = this.getX();
            y_stage = this.getY();
        });
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // double click
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                }
                // right click
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    showRightButtonMenu(mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
            }
        });
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setPercents(ArrayList<Rectangle> percents) {
        this.percents = percents;
    }

    public void setStates(ArrayList<Label> states) {
        this.states = states;
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    private void showRightButtonMenu(double x, double y) {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem property = new MenuItem("Property");
        property.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Label secondLabel = new Label("I'm a Label on new Window");

                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(secondLabel);
                Scene secondScene = new Scene(secondaryLayout, 230, 100);
                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("Property");
                newWindow.setScene(secondScene);

                newWindow.show();
            }
        });
        contextMenu.getItems().addAll(property);
        contextMenu.show(this.getParent(), x, y);
    }
}
