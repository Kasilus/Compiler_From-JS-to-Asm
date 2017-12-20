import java.util.ArrayList;

public class Run {

    public static void main(String[] args) {

        String expression = "{ var a = 12, b = 6; a = 2 + 2 *2;}";

//        String expression = "{if (a + 2) b = 3 else g -2 ; a = 5;}";
        Lexer lexer = new JavaScriptLexer();
        lexer.setExpression(expression);
        lexer.outputExpression();
        lexer.analise();
        lexer.outputLexerTable();

        ArrayList<LexerToken> lexemeTable = (ArrayList<LexerToken>) lexer.getLexemeTable();

        Parser parser = new JavaScriptParser();
        TreeNode tree = null;

        if (lexemeTable.size() > 0){
            tree = parser.parse(lexemeTable);
        }

        System.out.println(tree);


    }

}
