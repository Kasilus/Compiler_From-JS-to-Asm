import java.util.ArrayList;

public class Run {

    public static void main(String[] args) {

//        String expression = "{ \n if (a = 12) \n b = 6; else \n  a = 5; \n}";

//        String expression = "{var a = 15, b = false; a = 2 === b;}";

        String expression = "{ var a = false; var b = 50; var c = 2 + 3; }";

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

        SemanthicAnalyzer semanthicAnalyzer = new SemanthicAnalyzer();
        semanthicAnalyzer.checkTypes(tree);

        System.out.println(tree + "\n");

        Generator generator = new ToAsmGenerator(semanthicAnalyzer);
        generator.generateCode(tree);


    }

}
