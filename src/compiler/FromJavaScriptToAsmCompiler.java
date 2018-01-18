package compiler;

import generator.Generator;
import generator.ToAsmGenerator;
import lexer.JavaScriptLexer;
import lexer.Lexer;
import parser.JavaScriptParser;
import parser.Parser;
import parser.TreeNode;
import semantic.JavaScriptSemanticAnalyzer;
import semantic.SemanticAnalyzer;

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
