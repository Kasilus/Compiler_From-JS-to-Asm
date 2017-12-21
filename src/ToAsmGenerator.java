import constructions.Constant;
import constructions.Node;
import constructions.Variable;

public class ToAsmGenerator implements Generator {

    private SemanthicAnalyzer semanthicAnalyzer = null;

    private boolean isEaxFree, isEbxFree;


    public ToAsmGenerator(SemanthicAnalyzer semanthicAnalyzer) {
        this.semanthicAnalyzer = semanthicAnalyzer;
    }

    @Override
    public void generateCode(TreeNode tree) {

        System.out.print(".386\n.model flat, stdcall\n.data\n");

        semanthicAnalyzer.getVariableMap().forEach((x, y) -> {
            System.out.print(x + " dd ?\n");
        });

        System.out.print(".code\nmain:\n");

        generateForNode(tree);

        System.out.print("end main\n");
    }

    private Node generateForNode(TreeNode node) {


        if (node != null) {


            if (node.getType() == TreeNode.Type.VARIABLE_TYPE) {

                generateForNode(node.getOp1());
                generateForNode(node.getOp2());


            } else if (node.getType() == TreeNode.Type.VARIABLE) {

                return semanthicAnalyzer.getVariableMap().get(node.getValue());


            } else if (node.getType() == TreeNode.Type.ASSIGNMENT) {

                Node leftNode = generateForNode(node.getOp1());

                Node rightNode = generateForNode(node.getOp2());


                if (rightNode instanceof Variable || rightNode instanceof Constant) {
                    System.out.print("push " + rightNode.getName() + "\n");
                }

                System.out.print("pop eax\n");
                System.out.print("mov " + leftNode.getName() + ", eax\n");


            } else if (node.getType() == TreeNode.Type.CONSTANT) {

                String constantValue;

                if (node.getValue().equals("true")){
                    constantValue = "1";
                } else if (node.getValue().equals("false")){
                    constantValue = "0";
                } else {
                    constantValue = node.getValue();
                }

                return new Constant(constantValue, Node.Type.Number);


            } else if (node.getType() == TreeNode.Type.ADDITION){


                Node leftNode, rightNode;

                leftNode = generateForNode(node.getOp1());

                if (leftNode instanceof Variable || leftNode instanceof Constant) {
                    System.out.print("push " + leftNode.getName() + "\n");
                }

                rightNode = generateForNode(node.getOp2());

                if (rightNode instanceof Variable || rightNode instanceof Constant) {
                    System.out.print("push " + rightNode.getName() + "\n");
                }

                System.out.print("pop ebx\n");
                System.out.print("pop eax\n");
                System.out.print("add eax, ebx\n");
                System.out.print("push eax\n");


                return new Node(node.getValue(), Node.Type.Number);


            }  else {
                generateForNode(node.getOp1());
                generateForNode(node.getOp2());
                generateForNode(node.getOp3());
            }

        }

        return new Node("Program", null);
    }
}
