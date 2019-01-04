public class Node {
    private int degree;
    private Node child;
    private int data;
    private Node parent;
    private boolean childCut;
    private Node leftSibling;
    private Node rightSibling;
    private String keyword;


    public Node() {
    }

    public Node(int degree, Node child, int data, Node parent, boolean childCut, Node leftSibling, Node rightSibling, String keyword) {
        this.degree = degree;
        this.child = child;
        this.data = data;
        this.parent = parent;
        this.childCut = childCut;
        this.leftSibling = leftSibling;
        this.rightSibling = rightSibling;
        this.keyword = keyword;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isChildCut() {
        return childCut;
    }

    public void setChildCut(boolean childCut) {
        this.childCut = childCut;
    }

    public Node getLeftSibling() {
        return leftSibling;
    }

    public void setLeftSibling(Node leftSibling) {
        this.leftSibling = leftSibling;
    }

    public Node getRightSibling() {
        return rightSibling;
    }

    public void setRightSibling(Node rightSibling) {
        this.rightSibling = rightSibling;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Node createNode(int degree, Node child, int data, Node parent, boolean childCut, Node leftSibling, Node rightSibling, String keyword) {
        return new Node(degree, child, data, parent, childCut, leftSibling, rightSibling, keyword);
    }

}