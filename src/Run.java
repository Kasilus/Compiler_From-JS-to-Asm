public class Run {

    public static void main(String[] args) {

        Lexer lexer = new JavaScriptLexer();
        lexer.analise("{(g + 2); var a = b + 2; b-- + 400; var varc=5; for (var i=0; i < 3; i++) {a = b / (12 * 300); }  ");
        lexer.outputExpression();
        lexer.outputLexerTable();

    }

}
