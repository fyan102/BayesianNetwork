package org.fyan102.bayesiannetwork;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Window extends Application {
    // The view for network
    private NetworkView network;
    // The view for menu
    private MenuView menu;
    // menu groups
    private Group root;

    public Window() {
        menu = new MenuView();
        network = new NetworkView();
        root = new Group();
        Node node = new Node("Node1");
        node.addState("true");
        node.addState("false");
        Node parent1 = new Node("Parent1");
        Node parent2 = new Node("Parent2");
        parent1.addState("St1");
        parent1.addState("St2");
        parent1.setBeliefs(new double[]{0.3, 0.7});
        parent2.addState("St1");
        parent2.addState("St2");
        parent2.addState("St3");
        parent2.setBeliefs(new double[]{0.2, 0.3, 0.5});
        node.addParent(parent1);
        node.addParent(parent2);
        node.setProbs(new double[][]{{0.2, 0.8}, {0.15, 0.85}, {0.18, 0.82}, {0.07, 0.93}, {0.85, 0.15}, {0.62, 0.38}});
        network.addNode(parent1);
        network.addNode(parent2);
        network.addNode(node);
    }

    private void setMenu(Stage stage) {
        menu.getMenuItem("Open").setOnAction(actionEvent -> openFile(stage));
        menu.getMenuItem("Run").setOnAction(actionEvent -> network.calculate());
        menu.getMenuItem("Chance Node").setOnAction(actionEvent -> addNode());
    }

    private void addNode() {
        network.addNode(new Node());
        root.getChildren().addAll(network.getNode(network.getNetwork().getNumberOfNodes() - 1).getComponents());
    }

    private ToolBar setToolBox() {
        ToolBar toolBar = new ToolBar();
        final Separator separator = new Separator();
        Button bNew = new Button("New");
        Button bOpen = new Button("Open");
        bOpen.setOnAction(menu.getMenuItem("Open").getOnAction());
        Button bClose = new Button("Close");
        Button bNewNode = new Button("Chance Node");
        bNewNode.setOnAction(menu.getMenuItem("Chance Node").getOnAction());
        Button bRun = new Button("Run");
        bRun.setOnAction(menu.getMenuItem("Run").getOnAction());
        toolBar.getItems().addAll(bNew, bOpen, bClose, bNewNode, bRun);
        toolBar.getItems().add(3, separator);
        return toolBar;
    }

    private void openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Bayesian Network Files", "*.bne"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        fileChooser.setTitle("Open a Network");
        fileChooser.showOpenDialog(stage);
    }

    //@Override
    public void start(Stage stage) {
        //menu
        setMenu(stage);
        MenuBar mb = new MenuBar();
        mb.prefWidthProperty().bind(stage.widthProperty());
        mb.getMenus().addAll(menu.getMenus());
        //Tool bar
        VBox vb = new VBox(mb);
        vb.getChildren().addAll(setToolBox());

        root.getChildren().addAll(vb);
        root.getChildren().addAll(network.getAllComponents());
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Bayesian Network");
        stage.setScene(scene);
        stage.show();
    }
}
