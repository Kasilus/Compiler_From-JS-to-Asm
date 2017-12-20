import java.util.ArrayList;
import java.util.LinkedList;

public class JavaScriptParser implements Parser {

    LexerToken currentToken;
    LinkedList<LexerToken> queue;

    @Override
    public TreeNode parse(ArrayList<LexerToken> lexemeTable) {

        queue = new LinkedList<>(lexemeTable);
        currentToken = queue.pollFirst();

        TreeNode node = new TreeNode(TreeNode.Type.PROGRAM, null, statement(), null);

        return node;

    }


    private TreeNode statement() {

        TreeNode node = null;

        if (currentToken.getName().equals("LEFT BRACE")) {

            node = new TreeNode(TreeNode.Type.EMPTY, null, null);
            currentToken = queue.pollFirst();

            while (!currentToken.getName().equals("RIGHT BRACE")) {
                node = new TreeNode(TreeNode.Type.STATEMENT, null, node, statement());
            }

            currentToken = queue.pollFirst();
        } else if (currentToken.getName().equals("SEMICOLON")) {
            node = new TreeNode(TreeNode.Type.EMPTY, null, null);

        } else if (currentToken.getName().equals("IF")) {

        } else if (currentToken.getName().equals("WHILE")){

        } else if (currentToken.getName().equals("DO")){

        } else if (currentToken.getName().equals("VARIABLE TYPE")){

        } else {
            node = new TreeNode(TreeNode.Type.EXPRESSION, expression(), null);
            if (!currentToken.getValue().equals(";")) {
                throw new SyntacticException("';' expected", currentToken.getPosition());
            }
            currentToken = queue.pollFirst();
        }

        return node;
    }

    private TreeNode expression(){

        return null;
    }

}
