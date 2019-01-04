
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Max priority queue : Fibonacci Heap
 */
public class MaxFibonacciHeap {
    private Node max;

    /*
    Inserting the node in fib heap
     */

    public Node insertIntoFibHeap(Node node) {
        if (node == null) {
           // System.out.println("Invalid value of node, node cannot be null");
            return null;
        }

        node.setParent(null);
        node.setChildCut(false);
        /*
        Case of inserting first node in fib heap
         */

        if (max == null) {
            max = getSingleNode(node);
            return max;
        }

        /*
        If the new node has value greater than max, then update the max pointer, otherwise insert node next to max
         */

        if (node.getData() > max.getData()) {
            max = insertBegining(node, max);
        } else {
            insertEnd(node,max);
        }

        return node;

    }

    /*
    Deleting the node from Fib heap
     */
    public Node deleteMaxNodeFromFibHeap(int testCaseNumber) {

        /*
        This handles the case when there is no element is present in the fib heap and a delete request was made
         */
        if (max == null) {
          //  System.out.println("No nodes are available in the three to delete");
            return null;
        }

        Node child = max.getChild();
        Node maxNodeBeforeDelete = max;

        /*
        Insert all the child of the max node in the root list, and later remove the max node
         */
        if (child != null) {
            List<Node> listOfChildNodes = getListOfNodes(child);
            Node maxNode = max;
            insertListOfNodesAtRootLevel(listOfChildNodes, max);
            max = maxNode;
        }

        Node startNode = null;

        /*
        In case max.getRightSibling() == max, means there was only one node in the fib heap which is now deleted. So finally max becomes null as Fib heap now does not contain any element.
         */
        if (max.getRightSibling() == max) {
            max = null;
            return maxNodeBeforeDelete;
        } else {
            startNode = max.getRightSibling();
        }

        deleteNode(max);

        /*
        Here we call pairwise merge of the nodes at the root level. Combining all the nodes of same degree in one tree
         */
        pairWiseMergeOfSameDegree(startNode, testCaseNumber);
        maxNodeBeforeDelete.setDegree(0);
        maxNodeBeforeDelete.setChild(null);
        return maxNodeBeforeDelete;
    }


   /*
    This operation is used when there is already a keyword present in the Fib heap and query comes along to increase the frequency of the key
    */
    public void increaseKey (Node node, int count) {
        int newKey = node.getData() + count;
        node.setData(newKey);

        if (node.getParent() == null && node.getData() > max.getData()) {
            max = node;
        }

        /*
        Cascading cut.
            IF the child cut of the parent is true then keep on deleting the nodes from the root and inserting them into root till you find first parent whose child cut is false.
            Make child cut of that parent true

         */
        if (node.getParent() != null && node != max && newKey > node.getParent().getData()) {
            while (node.getParent() != null && node.getParent().isChildCut()) {
                Node nodeParent = node.getParent();
                nodeParent.setDegree(nodeParent.getDegree()-1);

                if (nodeParent.getChild() == node) {
                    if (node.getRightSibling() != node) {
                        nodeParent.setChild(node.getRightSibling());
                    } else {
                        nodeParent.setChild(null);
                    }
                }
                deleteNode(node);
                insertIntoFibHeap(node);
                node = nodeParent;
            }

            if (node.getParent() != null) {
                node.getParent().setChildCut(true);
                node.getParent().setDegree(node.getParent().getDegree()-1);

                if (node.getParent().getChild() == node) {
                    if (node.getRightSibling() != node) {
                        node.getParent().setChild(node.getRightSibling());
                    } else {
                        node.getParent().setChild(null);
                    }
                }
                deleteNode(node);
                insertIntoFibHeap(node);

            }
        }
    }

    /*
    Pairwise merge of same degree trees
     */
    private void pairWiseMergeOfSameDegree(Node startNode, int testCaseNumber) {
        Map<Integer, Node> degreeToNodeMapping = new HashMap<>();

        List<Node> listOfInitalNodes = getListOfNodes(startNode);
        List<Node> nodesLeftAfterPairwiseMerging = new ArrayList<>();

        for (Node currentNode : listOfInitalNodes) {

            while (degreeToNodeMapping.get(currentNode.getDegree()) != null) {
                Node nodeOfDegree = degreeToNodeMapping.get(currentNode.getDegree());
                Node newParent;
                int degreeOfNodeToBeRemoved = currentNode.getDegree();
                if (nodeOfDegree.getData() >= currentNode.getData()) {
                    newParent = parentChild(nodeOfDegree, currentNode);
                } else {
                    newParent = parentChild(currentNode, nodeOfDegree);
                }
                degreeToNodeMapping.remove(degreeOfNodeToBeRemoved);
                nodesLeftAfterPairwiseMerging.remove(nodeOfDegree);
                currentNode = newParent;
            }
            nodesLeftAfterPairwiseMerging.add(currentNode);
            degreeToNodeMapping.put(currentNode.getDegree(), currentNode);
        }

        // This is a test code, and it checks if at any point after pairwise merge the degree of node is not equal to number of children it have then there is a bug.
        for (Node node : nodesLeftAfterPairwiseMerging) {
            int numberOfChild = 0;
            Node child = node.getChild();
            if (child != null) {
                numberOfChild = 1;
                Node next = child.getRightSibling();
                while (next != child) {
                    numberOfChild++;
                    next = next.getRightSibling();
                }
            }

            if (numberOfChild != node.getDegree()) {
                //System.out.println(node.getKeyword() + " " + testCaseNumber);
            }
        }

        max = insertListOfNodesAtRootLevel(nodesLeftAfterPairwiseMerging, null);
    }


    private Node getSingleNode (Node node) {
        node.setLeftSibling(node);
        node.setRightSibling(node);
        return node;
    }


    private List<Node> getListOfNodes (Node startNode) {

        List<Node> listOfNodes = new ArrayList<>();

        listOfNodes.add(startNode);
        Node nextNode = startNode.getRightSibling();

        while (nextNode != startNode) {
            listOfNodes.add(nextNode);
            nextNode = nextNode.getRightSibling();
        }

        return listOfNodes;
    }


    private Node parentChild(Node parent, Node child) {
        Node currentChildOfParent = parent.getChild();

        if (currentChildOfParent == null) {
            child = getSingleNode(child);
            parent.setChild(child);
        } else {
          insertEnd(child,currentChildOfParent);
        }
        child.setParent(parent);
        child.setChildCut(false);
        parent.setDegree(parent.getDegree() + 1);

        return parent;
    }

    private Node deleteNode(Node node) {
        node.getLeftSibling().setRightSibling(node.getRightSibling());
        node.getRightSibling().setLeftSibling(node.getLeftSibling());
        node.setParent(null);
        return node;
    }

    private Node insertBegining(Node nodeToInsert, Node startNode) {
        Node lastNode = startNode.getLeftSibling();
        nodeToInsert.setRightSibling(startNode);
        nodeToInsert.setLeftSibling(lastNode);
        startNode.setLeftSibling(nodeToInsert);
        lastNode.setRightSibling(nodeToInsert);
        return nodeToInsert;
    }


    private void insertEnd (Node nodeToInsert, Node startNode) {
        nodeToInsert.setRightSibling(startNode);
        Node lastNode = startNode.getLeftSibling();
        nodeToInsert.setLeftSibling(startNode.getLeftSibling());
        startNode.setLeftSibling(nodeToInsert);
        lastNode.setRightSibling(nodeToInsert);
    }


    private Node insertListOfNodesAtRootLevel (List<Node> nodesList, Node afterThisNode) {
        Node maxFrequencyNode = nodesList.get(0);

        Node current  = afterThisNode;
        for (Node node : nodesList) {
            node.setParent(null);
            node.setChildCut(false);
            if (node.getData() > maxFrequencyNode.getData()) {
                maxFrequencyNode = node;
            }

            if (current == null) {
                current = getSingleNode(node);
            } else {
                Node lastNode = current.getLeftSibling();
                node.setRightSibling(current);
                node.setLeftSibling(current.getLeftSibling());
                current.setLeftSibling(node);
                lastNode.setRightSibling(node);
                current = node;
            }
        }

        return maxFrequencyNode;
    }

}
