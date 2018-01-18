import errors.ExceptionPosition;

public class TreeNode {

    private Type type;
    private String value;
    private TreeNode op1;
    private TreeNode op2;
    private TreeNode op3;
    private ExceptionPosition position;

    enum Type {
        EMPTY,PROGRAM, STATEMENT, EXPRESSION, TEST, VARIABLE, CONSTANT,
        ADDITION("+"), SUBTRACTION("-"), MULTIPLICATION("*"),
        DIVISION("/"), REMAINDER("%"), INCREMENT("++"), DECREMENT("--"),
        BITWISE_AND("&"), BITWISE_XOR("^"), BITWISE_OR("|"),
        LOGICAL_NOT("!"), BITWISE_NOT("~"), LOGICAL_AND("&&"),
        LOGICAL_OR("||"), ASSIGNMENT("="), ASSIGNMENT_ADDITION("+="),
        ASSIGNMENT_SUBTRACTION("-="), ASSIGNMENT_MULTIPLICATION("*="),
        ASSIGNMENT_DIVISION("/="), ASSIGNMENT_REMAINDER("%="),
        SHIFT_LEFT("<<"), SHIFT_RIGHT(">>"), SHIFT_RIGHT_ZERO_FILL(">>>"),
        ASSIGNMENT_BITWISE_AND("&="), ASSIGNMENT_BITWISE_XOR("^="),
        ASSIGNMENT_BITWISE_OR("|="), EQUALITY("=="), STRICT_EQUALITY("==="),
        NON_EQUALITY("!="), STRICT_NON_EQUALTIY("!=="),
        GREATER_THAN(">"), LESS_THAN("<"), GREATER_THAN_OR_EQUALS(">="),
        LESS_THAN_OR_EQUALS("<="), THERNARY_OPERATOR("?"),COMMA(","),
        VARIABLE_TYPE("var"), IF, IF_ELSE, WHILE, DO_WHILE, SWITCH_CASE, FOR,
        ARRAY, UNARY;

        private String outName;

        Type() {
        }

        Type(String s) {
            this.outName = s;
        }

        public String getOutName() {
            return outName;
        }
    }

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
