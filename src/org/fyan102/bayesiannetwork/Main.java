package org.fyan102.bayesiannetwork;

public class Main {

    public static void main(String[] args) {
        // write your code here
       /* Node node = new Node("Node1");
        node.addState("true");
        node.addState("false");
        //System.out.println(node);
        Node parent1 = new Node("Parent1");
        parent1.addState("St1");
        parent1.addState("St2");
        parent1.setBeliefs(new double[]{0.3, 0.7});
        // parent1.addState("St3");
        Node parent2 = new Node("Parent2");
        parent2.addState("St1");
        parent2.addState("St2");
        parent2.addState("St3");
        parent2.setBeliefs(new double[]{0.2, 0.3, 0.5});
//        parent2.addState("St4");
//        Node parent3 = new Node();
//        parent3.addState("St1");
//        parent3.addState("St2");
//        parent3.addState("St3");
//        parent3.addState("St4");
        node.addParent(parent1);
        node.addParent(parent2);
        node.setProbs(new double[][]{{0.2, 0.8}, {0.15, 0.85}, {0.18, 0.82}, {0.07, 0.93}, {0.85, 0.15}, {0.62, 0.38}});
        Network network = new Network();
        network.addNode(parent1);
        network.addNode(parent2);
        network.addNode(node);
        network.calculate();
        System.out.println(network);
        //System.out.println(node.getPermutation(node.getParents().size()));*/
        javafx.application.Application.launch(Window.class);
    }
}
