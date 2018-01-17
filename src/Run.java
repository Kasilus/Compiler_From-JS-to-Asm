import java.io.*;
import java.util.ArrayList;

public class Run {

    public static void main(String[] args) {

//        String expression = "{ \n if (a = 12) \n b = 6; else \n  a = 5; \n}";

//        String expression = "{var a = true, b = false; a === b; var g = false; g === a;}";

//        String expression = "{var a = 12; var b = 1; if (a > b) {a = a + b;} else {b = a + b;} }";

//        String expression = "{var a = 12; var b = 5; while (b>a) {b = b & 1; } }";

//        String expression = "{var a = 12; var b = 5; do {b = b - 1; } while (b>a);  }";

//        String expression = "{ var a = false; var b = 50; var c = 2 + 3; }";

//        String expression = "{if (a + 2) b = 3 else g -2 ; a = 5;}";


        String expression = readExpressionFromFile("input.txt");

        Lexer lexer = new JavaScriptLexer();
        lexer.setExpression(expression);
        lexer.outputExpression();
        lexer.analise();

        ArrayList<LexerToken> lexemeTable = (ArrayList<LexerToken>) lexer.getLexemeTable();

        Parser parser = new JavaScriptParser();
        TreeNode tree = null;

        if (lexemeTable.size() > 0){
            tree = parser.parse(lexemeTable);
        }

        SemanthicAnalyzer semanthicAnalyzer = new SemanthicAnalyzer();
        semanthicAnalyzer.checkTypes(tree);

        Generator generator = new ToAsmGenerator(semanthicAnalyzer);
        String generatedCode = generator.generateCode(tree);

        writeExpressionToFile(generatedCode, "output.txt");


    }

    private static String readExpressionFromFile(String fileName){

        StringBuilder expression = new StringBuilder();


        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){

            String currentLine;

            if ((currentLine= br.readLine()) != null){
                expression.append(currentLine);
            }

            while ((currentLine = br.readLine()) != null) {
                expression.append("\n").append(currentLine);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return expression.toString();

    }

    private static void writeExpressionToFile(String expression, String fileName){

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {

            bw.write(expression);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



}
