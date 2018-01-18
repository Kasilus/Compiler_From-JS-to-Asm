import compiler.Compiler;
import compiler.FromJavaScriptToAsmCompiler;

import java.io.*;

public class Run {

    public static void main(String[] args) {

        // args[0]
        String inputExpression = readExpressionFromFile("testfiles/input/input4.txt");
        Compiler compiler = new FromJavaScriptToAsmCompiler();
        String outputExpression = compiler.compile(inputExpression);
        // args[1]
        writeExpressionToFile(outputExpression, "testfiles/output/output4.txt");

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

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {

            bw.write(expression);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



}
