package semantic;

import errors.SemanticException;
import parser.TreeNode;
import parser.Type;

import java.util.*;


public class JavaScriptSemanticAnalyzer implements SemanticAnalyzer {

    private List<Variable> vars = new ArrayList<>();
    private Map<String, Variable> variableMap = new LinkedHashMap<>();
    private Type currentType;

    @Override
    public boolean areAllTypesCorrect(TreeNode parseTree) {

            getNode(parseTree);

        return true;

    }


    private Node getNode(TreeNode node) {


        if (node != null) {


            if (node.getType() == Type.VARIABLE_TYPE) {

                currentType = node.getType();
                getNode(node.getOp1());
                getNode(node.getOp2());
                currentType = null;


            } else if (node.getType() == Type.VARIABLE) {

                if (currentType != null) {

                    Variable curVariable = variableMap.get(node.getValue());

                    // check if variable in map
                    if (curVariable == null) {

                        curVariable = new Variable(node.getValue(), Node.Type.Unsigned);
                        variableMap.put(curVariable.getName(), curVariable);

                        return curVariable;

                    } else {

                        throw new SemanticException("Variable '" + curVariable.getName() + "' has already assigned!", node.getPosition());

                    }

                } else {

                    Variable curVariable = variableMap.get(node.getValue());

                    if (curVariable != null) {
                        return curVariable;
                    } else {
                        throw new SemanticException("Variable '" + node.getValue() + "' hasn't been assigned!", node.getPosition());
                    }

                }

            } else if (node.getType() == Type.ASSIGNMENT) {


                Variable leftNode = (Variable) getNode(node.getOp1());
                Type bufCurrentType = currentType;
                currentType = null;
                Node rightNode = getNode(node.getOp2());
                currentType = bufCurrentType;

                if (currentType != null) {
                    leftNode.setType(rightNode.getType());
                } else {
                    Node.Type mainType = checkTypeCompatibility(leftNode, rightNode, node);
                    leftNode.setType(mainType);
                }


            } else if (node.getType() == Type.CONSTANT) {

                Node.Type type = recognizeType(node.getValue());
                return new Constant(node.getValue(), type);


            } else if (node.getType() == Type.ADDITION ||
                    node.getType() == Type.SUBTRACTION ||
                    node.getType() == Type.MULTIPLICATION ||
                    node.getType() == Type.DIVISION ||
                    node.getType() == Type.EQUALITY ||
                    node.getType() == Type.NON_EQUALITY ||
                    node.getType() == Type.STRICT_EQUALITY ||
                    node.getType() == Type.STRICT_NON_EQUALTIY ||
                    node.getType() == Type.LOGICAL_AND ||
                    node.getType() == Type.LOGICAL_OR ||
                    node.getType() == Type.BITWISE_AND ||
                    node.getType() == Type.BITWISE_OR ||
                    node.getType() == Type.BITWISE_XOR ||
                    node.getType() == Type.LESS_THAN ||
                    node.getType() == Type.GREATER_THAN
                    ) {

                Node leftNode, rightNode;

                leftNode = getNode(node.getOp1());
                rightNode = getNode(node.getOp2());

                Node.Type mainType = checkTypeCompatibility(leftNode, rightNode, node);

                if (mainType != null) {

                    return new Node(node.getValue(), mainType);

                } else {
                    throw new SemanticException("Incompatible types!", node.getPosition());
                }

            } else if (node.getType() == Type.UNARY) {
                return new Node(node.getValue(), getNode(node.getOp1()).getType());
            } else {
                getNode(node.getOp1());
                getNode(node.getOp2());
                getNode(node.getOp3());
            }

        }

        return new Node("Program", null);
    }

    private Node.Type recognizeType(String value) {

        if (value.equals("true") || value.equals("false")) {
            return Node.Type.Boolean;
        } else if (isInteger(value)) {
            return Node.Type.Number;
        } else {
            return Node.Type.String;
        }

    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }


    private static Node.Type checkTypeCompatibility(Node leftNode, Node rightNode, TreeNode actionNode) {

        Node.Type mainType = null;
        Node.Type leftNodeType = leftNode.getType();
        Node.Type rightNodeType = rightNode.getType();

        if (actionNode.getType() == Type.ASSIGNMENT) {
            mainType = rightNodeType;
        } else if (actionNode.getType() == Type.STRICT_EQUALITY || actionNode.getType() == Type.STRICT_NON_EQUALTIY) {

            if (leftNodeType == rightNodeType) {
                mainType = leftNodeType;
            }

        } else if (actionNode.getType() == Type.ADDITION || actionNode.getType() == Type.SUBTRACTION ||
                actionNode.getType() == Type.MULTIPLICATION || actionNode.getType() == Type.DIVISION) {
            mainType = Node.Type.Number;
        } else if (actionNode.getType() == Type.LOGICAL_AND) {
            if (leftNodeType == Node.Type.Number && rightNodeType == Node.Type.Number) {
                mainType = Node.Type.Number;
            } else {
                mainType = Node.Type.Boolean;
            }
        } else if (actionNode.getType() == Type.LOGICAL_OR) {
            if (leftNodeType == Node.Type.Boolean && rightNodeType == Node.Type.Boolean) {
                mainType = Node.Type.Boolean;
            } else {
                mainType = Node.Type.Number;
            }
        } else if (actionNode.getType() == Type.LESS_THAN || actionNode.getType() == Type.GREATER_THAN) {
            mainType = Node.Type.Boolean;
        } else if (actionNode.getType() == Type.BITWISE_AND || actionNode.getType() == Type.BITWISE_OR
                || actionNode.getType() == Type.BITWISE_XOR) {
            mainType = Node.Type.Number;
        }


        if (mainType == null) {
            throw new SemanticException("Invalid operands to " + actionNode.getType().getOutName() + " (have '" + leftNode.getType() +
                    "' and '" + rightNode.getType() + "') ", actionNode.getPosition());
        }

        return mainType;
    }

    public List<Variable> getVars() {
        return vars;
    }

    public void setVars(List<Variable> vars) {
        this.vars = vars;
    }

    @Override
    public Map<String, Variable> getVariableMap() {
        return variableMap;
    }

    public void setVariableMap(Map<String, Variable> variableMap) {
        this.variableMap = variableMap;
    }

    public Type getCurrentType() {
        return currentType;
    }

    public void setCurrentType(Type currentType) {
        this.currentType = currentType;
    }


}


