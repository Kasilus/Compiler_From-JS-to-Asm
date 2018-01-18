import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CompilerTest {

    Compiler compiler;

    @Before
    public void initCompiler(){
        compiler = new FromJavaScriptToAsmCompiler();
    }

    @Test
    public void initialisationTest(){

        String inputExpression = "{\n" +
                "    var a = 2;\n" +
                "    var b = 6;\n" +
                "    var c = a + b;\n" +
                "    a = 2 + 6;\n" +
                "    b = false;\n" +
                "    b = 12 / a;\n" +
                "};";

        String outputExpression = compiler.compile(inputExpression);
        String expectedExpression = ".386\n" +
                ".model flat, stdcall\n" +
                ".data\n" +
                "a dd ?\n" +
                "b dd ?\n" +
                "c dd ?\n" +
                ".code\n" +
                "main:\n" +
                "push 2\n" +
                "pop eax\n" +
                "mov a, eax\n" +
                "push 6\n" +
                "pop eax\n" +
                "mov b, eax\n" +
                "push a\n" +
                "push b\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "add eax, ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov c, eax\n" +
                "push 2\n" +
                "push 6\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "add eax, ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov a, eax\n" +
                "push 0\n" +
                "pop eax\n" +
                "mov b, eax\n" +
                "push 12\n" +
                "push a\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "idiv ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov b, eax\n" +
                "end main\n";

        assertEquals(expectedExpression, outputExpression);

    }

    @Test
    public void arithmeticTest(){

        String inputExpression = "{\n" +
                "    var a = 2;\n" +
                "    var b = 6;\n" +
                "    var c = a * b;\n" +
                "    c = b / a;\n" +
                "    var d = true;\n" +
                "    var g = false;\n" +
                "    a = d && g;\n" +
                "    b = g || d;\n" +
                "    c = a & g;\n" +
                "    c = b ^ g;\n" +
                "};";

        String outputExpression = compiler.compile(inputExpression);
        String expectedExpression = ".386\n" +
                ".model flat, stdcall\n" +
                ".data\n" +
                "a dd ?\n" +
                "b dd ?\n" +
                "c dd ?\n" +
                "d dd ?\n" +
                "g dd ?\n" +
                ".code\n" +
                "main:\n" +
                "push 2\n" +
                "pop eax\n" +
                "mov a, eax\n" +
                "push 6\n" +
                "pop eax\n" +
                "mov b, eax\n" +
                "push a\n" +
                "push b\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "imul ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov c, eax\n" +
                "push b\n" +
                "push a\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "idiv ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov c, eax\n" +
                "push 1\n" +
                "pop eax\n" +
                "mov d, eax\n" +
                "push 0\n" +
                "pop eax\n" +
                "mov g, eax\n" +
                "push d\n" +
                "push g\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "and eax, ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov a, eax\n" +
                "push g\n" +
                "push d\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "or eax, ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov b, eax\n" +
                "push a\n" +
                "push g\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "and eax, ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov c, eax\n" +
                "push b\n" +
                "push g\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "xor eax, ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov c, eax\n" +
                "end main\n";

        assertEquals(expectedExpression, outputExpression);

    }

    @Test
    public void branchingTest(){

        String inputExpression = "{\n" +
                "    var a = 12;\n" +
                "    var b = 1;\n" +
                "    if (a === b) {\n" +
                "        a = b - 1;\n" +
                "    }\n" +
                "\n" +
                "    if (a > b) {\n" +
                "        a = a + b;\n" +
                "    } else {\n" +
                "        b = a + b;\n" +
                "    }\n" +
                "};";

        String outputExpression = compiler.compile(inputExpression);
        String expectedExpression = ".386\n" +
                ".model flat, stdcall\n" +
                ".data\n" +
                "a dd ?\n" +
                "b dd ?\n" +
                ".code\n" +
                "main:\n" +
                "push 12\n" +
                "pop eax\n" +
                "mov a, eax\n" +
                "push 1\n" +
                "pop eax\n" +
                "mov b, eax\n" +
                "push a\n" +
                "push b\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "cmp eax, ebx\n" +
                "je @label1\n" +
                "push 0\n" +
                "jmp @label2\n" +
                "label1:\n" +
                "push 1\n" +
                "label2:\n" +
                "pop eax\n" +
                "cmp eax, 0\n" +
                "je @label3\n" +
                "push b\n" +
                "push 1\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "sub eax, ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov a, eax\n" +
                "label3:\n" +
                "push a\n" +
                "push b\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "cmp eax, ebx\n" +
                "jg @label4\n" +
                "push 0\n" +
                "jmp @label5\n" +
                "label4:\n" +
                "push 1\n" +
                "label5:\n" +
                "pop eax\n" +
                "cmp eax, 0\n" +
                "je @label6\n" +
                "push a\n" +
                "push b\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "add eax, ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov a, eax\n" +
                "jmp @label7\n" +
                "label6:\n" +
                "push a\n" +
                "push b\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "add eax, ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov b, eax\n" +
                "label7:\n" +
                "end main\n";

        assertEquals(expectedExpression, outputExpression);


    }

    @Test
    public void cycleTest(){

        String inputExpression = "{\n" +
                "    var a = 12;\n" +
                "    var b = 5;\n" +
                "    while (b>a) {\n" +
                "        b = b & 1;\n" +
                "    }\n" +
                "\n" +
                "    do {\n" +
                "        b = b - 1;\n" +
                "    } while (b<a);\n" +
                "};";

        String outputExpression = compiler.compile(inputExpression);
        String expectedExpression = ".386\n" +
                ".model flat, stdcall\n" +
                ".data\n" +
                "a dd ?\n" +
                "b dd ?\n" +
                ".code\n" +
                "main:\n" +
                "push 12\n" +
                "pop eax\n" +
                "mov a, eax\n" +
                "push 5\n" +
                "pop eax\n" +
                "mov b, eax\n" +
                "label1:\n" +
                "push b\n" +
                "push a\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "cmp eax, ebx\n" +
                "jg @label2\n" +
                "push 0\n" +
                "jmp @label3\n" +
                "label2:\n" +
                "push 1\n" +
                "label3:\n" +
                "pop eax\n" +
                "cmp eax, 0\n" +
                "je @label4\n" +
                "push b\n" +
                "push 1\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "and eax, ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov b, eax\n" +
                "jmp @label1\n" +
                "label4:\n" +
                "label5:\n" +
                "push b\n" +
                "push 1\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "sub eax, ebx\n" +
                "push eax\n" +
                "pop eax\n" +
                "mov b, eax\n" +
                "push b\n" +
                "push a\n" +
                "pop ebx\n" +
                "pop eax\n" +
                "cmp eax, ebx\n" +
                "jl @label6\n" +
                "push 0\n" +
                "jmp @label7\n" +
                "label6:\n" +
                "push 1\n" +
                "label7:\n" +
                "pop eax\n" +
                "cmp eax, 0\n" +
                "je @label8\n" +
                "jmp @label5\n" +
                "label8:\n" +
                "end main\n";

        assertEquals(expectedExpression, outputExpression);

    }

}
