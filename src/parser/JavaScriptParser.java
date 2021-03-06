package parser;

import errors.ExceptionPosition;
import errors.SyntacticException;
import lexer.LexerToken;

import java.util.LinkedList;
import java.util.List;

public class JavaScriptParser implements Parser {

    LexerToken currentToken;
    LinkedList<LexerToken> queue;

    @Override
    public TreeNode parse(List<LexerToken> lexemeTable) {

        queue = new LinkedList<>(lexemeTable);
        currentToken = queue.pollFirst();

        TreeNode node = new TreeNode(Type.PROGRAM, null, statement(), null);

        return node;

    }


    private TreeNode statement() {

        TreeNode node = null;

        if (currentToken.getName().equals("LEFT BRACE")) {

            node = new TreeNode(Type.EMPTY, null, null);
            currentToken = queue.pollFirst();

            while (!currentToken.getName().equals("RIGHT BRACE")) {
                node = new TreeNode(Type.STATEMENT, null, node, statement());
            }

            currentToken = queue.pollFirst();
        } else if (currentToken.getName().equals("SEMICOLON")) {
            node = new TreeNode(Type.EMPTY, null, null);
        } else if (currentToken.getName().equals("IF")) {

            currentToken = queue.pollFirst();
            node = new TreeNode(Type.IF, null, parenExpr(), statement());

            if (currentToken.getName().equals("ELSE")) {
                node.setType(Type.IF_ELSE);

                currentToken = queue.pollFirst();
                node.setOp3(statement());
            }


        } else if (currentToken.getName().equals("WHILE")) {

            currentToken = queue.pollFirst();
            node = new TreeNode(Type.WHILE, null, parenExpr(), statement());


        } else if (currentToken.getName().equals("DO")) {

            currentToken = queue.pollFirst();

            node = new TreeNode(Type.DO_WHILE, null, statement(), null);

            if (currentToken.getName().equals("WHILE")) {

                currentToken = queue.pollFirst();

                node.setOp2(parenExpr());

                if (!currentToken.getValue().equals(";")) {
                    throw new SyntacticException("';' expected", currentToken.getPosition());
                }

                currentToken = queue.pollFirst();

            } else {
                throw new SyntacticException("'while' expected", currentToken.getPosition());
            }

        } else if (currentToken.getName().equals("VARIABLE TYPE")) {

            node = new TreeNode(Type.VARIABLE_TYPE, currentToken.getValue(), varAssignment(), null);
            currentToken = queue.pollFirst();

        } else {
            node = new TreeNode(Type.EXPRESSION, expression(), null);
            if (!currentToken.getValue().equals(";")) {
                throw new SyntacticException("';' expected", currentToken.getPosition());
            }
            currentToken = queue.pollFirst();
        }

        return node;
    }

    private TreeNode expression() {

        TreeNode node = null;

        if (currentToken.getType() != LexerToken.Type.VAR) {
            return temp4();
        }

        node = temp4();

        if (currentToken.getValue().equals("=")) {
            currentToken = queue.pollFirst();
            ExceptionPosition positionBuf = currentToken.getPosition();
            node = new TreeNode(Type.ASSIGNMENT, null, node, expression(), positionBuf);

        }

        return node;
    }

    private TreeNode varAssignment() {

        TreeNode node = null;

        node = assignment();

        while (currentToken.getValue().equals(",")) {

            node = new TreeNode(Type.COMMA, node, assignment());

        }

        if (!currentToken.getValue().equals(";")) {
            throw new SyntacticException("';' expected", currentToken.getPosition());
        }

        return node;
    }

    private TreeNode assignment() {

        TreeNode node = null;
        currentToken = queue.pollFirst();

        if (currentToken.getType() != LexerToken.Type.VAR) {
            throw new SyntacticException("var expected", currentToken.getPosition());
        }

        node = new TreeNode(Type.VARIABLE, currentToken.getValue(), null, null, currentToken.getPosition());
        currentToken = queue.pollFirst();


        if (currentToken.getName().equals("ASSIGNMENT")) {
            currentToken = queue.pollFirst();
            ExceptionPosition positionBuf = currentToken.getPosition();
            node = new TreeNode(Type.ASSIGNMENT, null, node, expression(), positionBuf);
        }


        return node;

    }

    private TreeNode temp4() {

        TreeNode node = temp5();
//        if (currentToken.getName().equals("?")){
//            currentToken = queue.pollFirst();
//            node = new parser.TreeNode(TreeNo)
//        }

        return node;

    }

    private TreeNode temp5() {

        TreeNode node = temp6();

        if (currentToken.getValue().equals("||")) {
            currentToken = queue.pollFirst();
            ExceptionPosition positionBuf = currentToken.getPosition();
            node = new TreeNode(Type.LOGICAL_OR, null, node, temp5(), positionBuf);
        }

        return node;

    }

    private TreeNode temp6() {

        TreeNode node = temp7();
        if (currentToken.getValue().equals("&&")) {
            currentToken = queue.pollFirst();
            ExceptionPosition positionBuf = currentToken.getPosition();
            node = new TreeNode(Type.LOGICAL_AND, null, node, temp6(), positionBuf);
        }

        return node;

    }

    private TreeNode temp7() {

        TreeNode node = temp8();

        if (currentToken.getValue().equals("|")) {
            currentToken = queue.pollFirst();
            ExceptionPosition positionBuf = currentToken.getPosition();
            node = new TreeNode(Type.BITWISE_OR, null, node, temp7(), positionBuf);
        }

        return node;

    }

    private TreeNode temp8() {

        TreeNode node = temp9();

        if (currentToken.getValue().equals("^")) {
            currentToken = queue.pollFirst();
            ExceptionPosition positionBuf = currentToken.getPosition();
            node = new TreeNode(Type.BITWISE_XOR, null, node, temp8(), positionBuf);
        }

        return node;

    }

    private TreeNode temp9() {

        TreeNode node = temp10();

        if (currentToken.getValue().equals("&")) {
            currentToken = queue.pollFirst();
            ExceptionPosition positionBuf = currentToken.getPosition();
            node = new TreeNode(Type.BITWISE_AND, null, node, temp9(), positionBuf);
        }

        return node;

    }

    private TreeNode temp10() {

        TreeNode node = temp11();

        if (currentToken.getValue().equals("==") ||
                currentToken.getValue().equals("!=") ||
                currentToken.getValue().equals("===") ||
                currentToken.getValue().equals("!==")) {


            Type type;

            if (currentToken.getValue().equals("==")) {
                type = Type.EQUALITY;
            } else if (currentToken.getValue().equals("!=")) {
                type = Type.NON_EQUALITY;
            } else if (currentToken.getValue().equals("===")) {
                type = Type.STRICT_EQUALITY;
            } else {
                type = Type.STRICT_NON_EQUALTIY;
            }

            currentToken = queue.pollFirst();
            ExceptionPosition positionBuf = currentToken.getPosition();

            node = new TreeNode(type, null, node, temp10(), positionBuf);
        }

        return node;

    }

    private TreeNode temp11() {

        TreeNode node = temp12();

        if (currentToken.getValue().equals(">") ||
                currentToken.getValue().equals(">=") ||
                currentToken.getValue().equals("<") ||
                currentToken.getValue().equals("<=")) {


            Type type;

            if (currentToken.getValue().equals(">")) {
                type = Type.GREATER_THAN;
            } else if (currentToken.getValue().equals(">=")) {
                type = Type.GREATER_THAN_OR_EQUALS;
            } else if (currentToken.getValue().equals("<")) {
                type = Type.LESS_THAN;
            } else {
                type = Type.LESS_THAN_OR_EQUALS;
            }

            currentToken = queue.pollFirst();
            ExceptionPosition positionBuf = currentToken.getPosition();

            node = new TreeNode(type, null, node, temp11(), positionBuf);
        }

        return node;


    }

    private TreeNode temp12() {

        TreeNode node = temp13();

        return node;

    }

    private TreeNode temp13() {

        TreeNode node = temp14();

        if (currentToken.getValue().equals("+") ||
                currentToken.getValue().equals("-")) {

            Type type;

            if (currentToken.getValue().equals("+")) {
                type = Type.ADDITION;
            } else {
                type = Type.SUBTRACTION;
            }

            currentToken = queue.pollFirst();
            ExceptionPosition positionBuf = currentToken.getPosition();

            node = new TreeNode(type, null, node, temp13(), positionBuf);
        }

        return node;


    }

    private TreeNode temp14() {

        TreeNode node = term();

        if (currentToken.getValue().equals("*") ||
                currentToken.getValue().equals("/") ||
                currentToken.getValue().equals("%")) {

            Type type;

            if (currentToken.getValue().equals("*")) {
                type = Type.MULTIPLICATION;
            } else if (currentToken.getValue().equals("/")) {
                type = Type.DIVISION;
            } else {
                type = Type.REMAINDER;
            }

            currentToken = queue.pollFirst();
            ExceptionPosition positionBuf = currentToken.getPosition();

            node = new TreeNode(type, null, node, temp14(), positionBuf);
        }

        return node;

    }

//    private parser.TreeNode temp15() {
//    }
//
//    private parser.TreeNode temp16() {
//    }
//
//    private parser.TreeNode temp17() {
//    }


    private TreeNode term() {
        TreeNode node = null;

        if (currentToken.getValue().equals("-") || currentToken.getValue().equals("+")) {
            node = new TreeNode(Type.UNARY, currentToken.getValue(), null, null);
            currentToken = queue.pollFirst();
            if (currentToken.getType().equals(LexerToken.Type.VAR)) {
                node.setOp1(new TreeNode(Type.VARIABLE, currentToken.getValue(), null, null, currentToken.getPosition()));
                currentToken = queue.pollFirst();
            } else if (currentToken.getType().equals(LexerToken.Type.CONSTANT)) {
                node.setOp1(new TreeNode(Type.CONSTANT, currentToken.getValue(), null, null, currentToken.getPosition()));
                currentToken = queue.pollFirst();
            }
            return node;

        } else if (currentToken.getType() == LexerToken.Type.VAR) {

            node = new TreeNode(Type.VARIABLE, currentToken.getValue(), null, null, currentToken.getPosition());
            currentToken = queue.pollFirst();

            return node;


        } else if (currentToken.getType() == LexerToken.Type.CONSTANT) {
            node = new TreeNode(Type.CONSTANT, currentToken.getValue(), null, null, currentToken.getPosition());
            currentToken = queue.pollFirst();
            return node;
        } else if (currentToken.getName().equals("FALSE") || currentToken.getName().equals("TRUE")) {
            node = new TreeNode(Type.CONSTANT, currentToken.getValue(), null, null, currentToken.getPosition());
            currentToken = queue.pollFirst();
            return node;
        } else {
            return parenExpr();
        }
    }

    private TreeNode parenExpr() {

        if (!currentToken.getValue().equals("(")) {
            throw new SyntacticException("'(' expected", currentToken.getPosition());
        }
        currentToken = queue.pollFirst();
        TreeNode node = expression();
        if (!currentToken.getValue().equals(")")) {
            throw new SyntacticException("')' expected", currentToken.getPosition());
        }
        currentToken = queue.pollFirst();
        return node;
    }

}
