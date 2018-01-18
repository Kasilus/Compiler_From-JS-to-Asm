package parser;

public enum Type {
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
