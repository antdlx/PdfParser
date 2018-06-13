package beans;

import java.util.ArrayList;
import java.util.List;

public class SimpleTreeNode {
    private double left_border = -1f;
    private double right_border = -2f;
    private List<SimpleTreeNode> childs = new ArrayList<SimpleTreeNode>();
    private SimpleTreeNode parent;
    private String content;
    private int height = 1;
    private int width = 1;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public SimpleTreeNode(double left_border, double right_border, SimpleTreeNode parent, String content){
        this.left_border = left_border;
        this.right_border = right_border;
        this.parent = parent;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getLeft_border() {
        return left_border;
    }

    public void setLeft_border(float left_border) {
        this.left_border = left_border;
    }

    public double getRight_border() {
        return right_border;
    }

    public void setRight_border(float right_border) {
        this.right_border = right_border;
    }

    public List<SimpleTreeNode> getChilds() {
        return childs;
    }

    public void setChilds(List<SimpleTreeNode> childs) {
        this.childs = childs;
    }
    public void addChilds(SimpleTreeNode child){
        childs.add(child);
    }
    public void addLeftChilds(SimpleTreeNode child){
        childs.add(0,child);
    }

    public SimpleTreeNode getParent() {
        return parent;
    }

    public void setParent(SimpleTreeNode parent) {
        this.parent = parent;
    }
}
