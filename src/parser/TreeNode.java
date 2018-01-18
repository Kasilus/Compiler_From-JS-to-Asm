package parser;

import errors.ExceptionPosition;

public class TreeNode {

    private Type type;
    private String value;
    private TreeNode op1;
    private TreeNode op2;
    private TreeNode op3;
    private ExceptionPosition position;



    public TreeNode(Type type, TreeNode op1, TreeNode op2) {
        this.type = type;
        this.op1 = op1;
        this.op2 = op2;
    }

    public TreeNode(Type type, String value, TreeNode op1, TreeNode op2) {
        this.type = type;
        this.value = value;
        this.op1 = op1;
        this.op2 = op2;
    }

    public TreeNode(Type type, String value, TreeNode op1, TreeNode op2, ExceptionPosition position) {
        this.type = type;
        this.value = value;
        this.op1 = op1;
        this.op2 = op2;
        this.position = position;
    }

    public TreeNode(Type type, String value, TreeNode op1, TreeNode op2, TreeNode op3, ExceptionPosition position) {
        this.type = type;
        this.value = value;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.position = position;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TreeNode getOp1() {
        return op1;
    }

    public void setOp1(TreeNode op1) {
        this.op1 = op1;
    }

    public TreeNode getOp2() {
        return op2;
    }

    public void setOp2(TreeNode op2) {
        this.op2 = op2;
    }

    public ExceptionPosition getPosition() {
        return position;
    }

    public void setPosition(ExceptionPosition position) {
        this.position = position;
    }

    public TreeNode getOp3() {
        return op3;
    }

    public void setOp3(TreeNode op3) {
        this.op3 = op3;
    }
}
