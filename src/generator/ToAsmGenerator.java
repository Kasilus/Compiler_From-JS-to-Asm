package generator;

import parser.TreeNode;
import parser.Type;
import semantic.Constant;
import semantic.Node;
import semantic.SemanticAnalyzer;
import semantic.Variable;

import static parser.Type.*;


public class ToAsmGenerator implements Generator {

    private SemanticAnalyzer semanthicAnalyzer = null;

    private int labelCounter = 1;
    private StringBuilder expression = new StringBuilder();


    public ToAsmGenerator(SemanticAnalyzer semanthicAnalyzer) {
        this.semanthicAnalyzer = semanthicAnalyzer;
    }

    @Override
    public String generateCode(TreeNode tree) {

        labelCounter = 1;

        expression.append(".386\n.model flat, stdcall\n.data\n");

        semanthicAnalyzer.getVariableMap().forEach((x, y) -> {
            expression.append(x + " dd ?\n");
        });

        expression.append(".code\nmain:\n");

        generateForNode(tree);

        expression.append("end main\n");

        return expression.toString();
    }

    private Node generateForNode(TreeNode node) {


        if (node != null) {


            if (node.getType() == Type.VARIABLE_TYPE) {

                generateForNode(node.getOp1());
                generateForNode(node.getOp2());


            } else if (node.getType() == Type.VARIABLE) {

                return semanthicAnalyzer.getVariableMap().get(node.getValue());


            } else if (node.getType() == Type.ASSIGNMENT) {

                Node leftNode = generateForNode(node.getOp1());

                Node rightNode = generateForNode(node.getOp2());


                if (rightNode instanceof Variable || rightNode instanceof Constant) {
                    expression.append("push " + rightNode.getName() + "\n");
                }

                expression.append("pop eax\n");
                expression.append("mov " + leftNode.getName() + ", eax\n");


            } else if (node.getType() == Type.CONSTANT) {

                String constantValue;

                if (node.getValue().equals("true")) {
                    constantValue = "1";
                } else if (node.getValue().equals("false")) {
                    constantValue = "0";
                } else {
                    constantValue = node.getValue();
                }

                return new Constant(constantValue, Node.Type.Number);


            } else if (node.getType() == ADDITION ||
                    node.getType() == Type.SUBTRACTION ||
                    node.getType() == Type.MULTIPLICATION ||
                    node.getType() == Type.DIVISION ||
                    node.getType() == Type.LOGICAL_AND ||
                    node.getType() == Type.LOGICAL_OR ||
                    node.getType() == Type.BITWISE_AND ||
                    node.getType() == Type.BITWISE_OR ||
                    node.getType() == Type.BITWISE_XOR
                    ) {


                Node leftNode, rightNode;

                leftNode = generateForNode(node.getOp1());

                if (leftNode instanceof Variable || leftNode instanceof Constant) {
                    expression.append("push " + leftNode.getName() + "\n");
                }

                rightNode = generateForNode(node.getOp2());

                if (rightNode instanceof Variable || rightNode instanceof Constant) {
                    expression.append("push " + rightNode.getName() + "\n");
                }

                expression.append("pop ebx\n");
                expression.append("pop eax\n");

                switch (node.getType()) {
                    case ADDITION:
                        expression.append("add eax, ebx\n");
                        break;
                    case SUBTRACTION:
                        expression.append("sub eax, ebx\n");
                        break;
                    case MULTIPLICATION:
                        expression.append("imul ebx\n");
                        break;
                    case DIVISION:
                        expression.append("idiv ebx\n");
                        break;
                    case BITWISE_AND:
                        expression.append("and eax, ebx\n");
                        break;
                    case LOGICAL_AND:
                        expression.append("and eax, ebx\n");
                        break;
                    case BITWISE_OR:
                        expression.append("or eax, ebx\n");
                        break;
                    case LOGICAL_OR:
                        expression.append("or eax, ebx\n");
                        break;
                    case BITWISE_XOR:
                        expression.append("xor eax, ebx\n");
                        break;
                }

                expression.append("push eax\n");


                return new Node(node.getValue(), Node.Type.Number);


            } else if (node.getType() == Type.EQUALITY ||
                    node.getType() == Type.NON_EQUALITY ||
                    node.getType() == Type.STRICT_EQUALITY ||
                    node.getType() == Type.STRICT_NON_EQUALTIY ||
                    node.getType() == Type.LESS_THAN ||
                    node.getType() == Type.GREATER_THAN) {

                Node leftNode, rightNode;

                leftNode = generateForNode(node.getOp1());

                if (leftNode instanceof Variable || leftNode instanceof Constant) {
                    expression.append("push " + leftNode.getName() + "\n");
                }

                rightNode = generateForNode(node.getOp2());

                if (rightNode instanceof Variable || rightNode instanceof Constant) {
                    expression.append("push " + rightNode.getName() + "\n");
                }

                expression.append("pop ebx\n");
                expression.append("pop eax\n");
                expression.append("cmp eax, ebx\n");

                if (node.getType() == Type.EQUALITY ||
                        node.getType() == Type.NON_EQUALITY ||
                        node.getType() == Type.STRICT_EQUALITY ||
                        node.getType() == Type.STRICT_NON_EQUALTIY) {
                    expression.append("je @label" + labelCounter + "\n");

                } else if (node.getType() == Type.GREATER_THAN) {

                    expression.append("jg @label" + labelCounter + "\n");

                } else if (node.getType() == Type.LESS_THAN) {
                    expression.append("jl @label" + labelCounter + "\n");
                }

                expression.append("push 0\n");
                expression.append("jmp @label" + (labelCounter + 1) + "\n");
                expression.append("label" + labelCounter + ":\n");
                expression.append("push 1\n");
                expression.append("label" + (labelCounter + 1) + ":\n");
                labelCounter += 2;

                return new Node(node.getValue(), Node.Type.Number);

            } else if (node.getType() == Type.IF) {

                Node leftNode = generateForNode(node.getOp1());

                expression.append("pop eax\n");
                expression.append("cmp eax, 0\n");
                expression.append("je @label" + labelCounter + "\n");

                Node rightNode = generateForNode(node.getOp2());

                expression.append("label" + labelCounter + ":\n");


                labelCounter++;


            } else if (node.getType() == Type.IF_ELSE) {

                Node leftNode = generateForNode(node.getOp1());

                expression.append("pop eax\n");
                expression.append("cmp eax, 0\n");


                expression.append("je @label" + labelCounter + "\n");


                Node rightNode = generateForNode(node.getOp2());

                StringBuilder expressionBuf = expression;
                expression = new StringBuilder();

                expression.append("label" + labelCounter + ":\n");
                Node rightRightNode = generateForNode(node.getOp3());

                expressionBuf.append("jmp @label" + (labelCounter + 1) + "\n");

                expressionBuf.append(expression);

                expression = expressionBuf;

                expression.append("label" + (labelCounter + 1) + ":\n");

                labelCounter += 2;


            } else if (node.getType() == Type.WHILE) {

                expression.append("label" + labelCounter + ":\n");

                int counterBuf = labelCounter;

                labelCounter++;

                Node leftNode = generateForNode(node.getOp1());

                expression.append("pop eax\n");
                expression.append("cmp eax, 0\n");

                StringBuilder expressionBuf = expression;
                expression = new StringBuilder();

                Node rightNode = generateForNode(node.getOp2());

                expressionBuf.append("je @label" + labelCounter + "\n");
                expressionBuf.append(expression);
                expression = expressionBuf;

                expression.append("jmp @label" + counterBuf + "\n");
                expression.append("label" + labelCounter + ":\n");

                labelCounter++;


            } else if (node.getType() == Type.DO_WHILE) {

                expression.append("label" + labelCounter + ":\n");

                int counterBuf = labelCounter;
                labelCounter++;

                Node leftNode = generateForNode(node.getOp1());

                Node rightNode = generateForNode(node.getOp2());

                expression.append("pop eax\n");
                expression.append("cmp eax, 0\n");
                expression.append("je @label" + labelCounter + "\n");
                expression.append("jmp @label" + counterBuf + "\n");
                expression.append("label" + labelCounter + ":\n");

                labelCounter++;


            } else {
                generateForNode(node.getOp1());
                generateForNode(node.getOp2());
                generateForNode(node.getOp3());
            }

        }

        return new Node("Program", null);
    }
}
