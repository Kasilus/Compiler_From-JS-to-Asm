public class FromJavaScriptToAsmCompiler implements Compiler {


    @Override
    public String compile(String inputExpression) {

        Lexer lexer = new JavaScriptLexer();
        lexer.createLexemeTableFromExpression(inputExpression);

        Parser parser = new JavaScriptParser();
        TreeNode parseTree = parser.parse(lexer.getLexemeTable());

        SemanticAnalyzer semanticAnalyzer = new JavaScriptSemanticAnalyzer();
        semanticAnalyzer.areAllTypesCorrect(parseTree);

        Generator generator = new ToAsmGenerator(semanticAnalyzer);
        String outputExpression = generator.generateCode(parseTree);

        return outputExpression;
    }
}
