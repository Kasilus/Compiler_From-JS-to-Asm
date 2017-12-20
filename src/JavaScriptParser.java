import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    private TreeNode statement(){

        return null;
    }


}
