import constructions.Constant;
import constructions.Node;
import constructions.Variable;
import errors.SemanticException;

import java.util.*;


public class JavaScriptSemanticAnalyzer implements SemanticAnalyzer {

    private List<Variable> vars = new ArrayList<>();
    private Map<String, Variable> variableMap = new LinkedHashMap<>();
    private TreeNode.Type currentType;

    @Override
    public boolean areAllTypesCorrect(TreeNode parseTree) {

            getNode(parseTree);

        return true;

    }


    private Node getNode(TreeNode node) {


        if (node != null) {


            if (node.getType() == TreeNode.Type.VARIABLE_TYPE) {

                currentType = node.getType();
                getNode(node.getOp1());
                getNode(node.getOp2());
                currentType = null;


            } else if (node.getType() == TreeNode.Type.VARIABLE) {

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

            } else if (node.getType() == TreeNode.Type.ASSIGNMENT) {


                Variable leftNode = (Variable) getNode(node.getOp1());
                TreeNode.Type bufCurrentType = currentType;
                currentType = null;
                Node rightNode = getNode(node.getOp2());
                currentType = bufCurrentType;

                if (currentType != null) {
                    leftNode.setType(rightNode.getType());
                } else {
                    Node.Type mainType = checkTypeCompatibility(leftNode, rightNode, node);
                    leftNode.setType(mainType);
                }


            } else if (node.getType() == TreeNode.Type.CONSTANT) {

                Node.Type type = recognizeType(node.getValue());
                return new Constant(node.getValue(), type);


            } else if (node.getType() == TreeNode.Type.ADDITION ||
                    node.getType() == TreeNode.Type.SUBTRACTION ||
                    node.getType() == TreeNode.Type.MULTIPLICATION ||
                    node.getType() == TreeNode.Type.DIVISION ||
                    node.getType() == TreeNode.Type.EQUALITY ||
                    node.getType() == TreeNode.Type.NON_EQUALITY ||
                    node.getType() == TreeNode.Type.STRICT_EQUALITY ||
                    node.getType() == TreeNode.Type.STRICT_NON_EQUALTIY ||
                    node.getType() == TreeNode.Type.LOGICAL_AND ||
                    node.getType() == TreeNode.Type.LOGICAL_OR ||
                    node.getType() == TreeNode.Type.BITWISE_AND ||
                    node.getType() == TreeNode.Type.BITWISE_OR ||
                    node.getType() == TreeNode.Type.BITWISE_XOR ||
                    node.getType() == TreeNode.Type.LESS_THAN ||
                    node.getType() == TreeNode.Type.GREATER_THAN
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

            } else if (node.getType() == TreeNode.Type.UNARY) {
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

        if (actionNode.getType() == TreeNode.Type.ASSIGNMENT) {
            mainType = rightNodeType;
        } else if (actionNode.getType() == TreeNode.Type.STRICT_EQUALITY || actionNode.getType() == TreeNode.Type.STRICT_NON_EQUALTIY) {

            if (leftNodeType == rightNodeType) {
                mainType = leftNodeType;
            }

        } else if (actionNode.getType() == TreeNode.Type.ADDITION || actionNode.getType() == TreeNode.Type.SUBTRACTION ||
                actionNode.getType() == TreeNode.Type.MULTIPLICATION || actionNode.getType() == TreeNode.Type.DIVISION) {
            mainType = Node.Type.Number;
        } else if (actionNode.getType() == TreeNode.Type.LOGICAL_AND) {
            if (leftNodeType == Node.Type.Number && rightNodeType == Node.Type.Number) {
                mainType = Node.Type.Number;
            } else {
                mainType = Node.Type.Boolean;
            }
        } else if (actionNode.getType() == TreeNode.Type.LOGICAL_OR) {
            if (leftNodeType == Node.Type.Boolean && rightNodeType == Node.Type.Boolean) {
                mainType = Node.Type.Boolean;
            } else {
                mainType = Node.Type.Number;
            }
        } else if (actionNode.getType() == TreeNode.Type.LESS_THAN || actionNode.getType() == TreeNode.Type.GREATER_THAN) {
            mainType = Node.Type.Boolean;
        } else if (actionNode.getType() == TreeNode.Type.BITWISE_AND || actionNode.getType() == TreeNode.Type.BITWISE_OR
                || actionNode.getType() == TreeNode.Type.BITWISE_XOR) {
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

    public TreeNode.Type getCurrentType() {
        return currentType;
    }

    public void setCurrentType(TreeNode.Type currentType) {
        this.currentType = currentType;
    }


}


