import java.util.ArrayList;

public class Run {

    public static void main(String[] args) {

        String expression = "{ \n (g + 2.123); \n var a = b + 2; \n b-- + 400; \n var varc=5; \n for (var i=0; i < 3; i++) {a = b / (12 * 300); \n}  ";

        Lexer lexer = new JavaScriptLexer();
        lexer.setExpression(expression);
        lexer.outputExpression();
        lexer.analise();
        lexer.outputLexerTable();

        ArrayList<LexerToken> lexemeTable = (ArrayList<LexerToken>) lexer.getLexemeTable();

        Parser parser = new JavaScriptParser();

        if (lexemeTable.size() > 0){
            parser.parse(lexemeTable);
        }


    }

}
