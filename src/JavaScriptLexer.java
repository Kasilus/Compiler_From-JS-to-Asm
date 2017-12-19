import java.util.ArrayList;
import java.util.List;

public class JavaScriptLexer implements Lexer {

    private LexerToken[] tokens;
    private List<LexerToken> lexemeTable;

    /**
     * Init tokens for javascript lexer
     */
    public JavaScriptLexer() {

        lexemeTable = new ArrayList<>();

        tokens = new LexerToken[]{
                // arithmetic singular
                new LexerToken("ADDITION", "+", LexerToken.Type.SYMBOL),
                new LexerToken("SUBTRACTION", "-", LexerToken.Type.SYMBOL),
                new LexerToken("MULTIPLICATION", "*", LexerToken.Type.SYMBOL),
                new LexerToken("DIVISION", "/", LexerToken.Type.SYMBOL),
                new LexerToken("REMAINDER", "%", LexerToken.Type.SYMBOL),
                // arithmetic plural
                new LexerToken("INCREMENT", "++", LexerToken.Type.SYMBOL),
                new LexerToken("DECREMENT", "--", LexerToken.Type.SYMBOL),
                // logical singular
                new LexerToken("BITWISE AND", "&", LexerToken.Type.SYMBOL),
                new LexerToken("BITWISE XOR", "^", LexerToken.Type.SYMBOL),
                new LexerToken("BITWISE OR", "|", LexerToken.Type.SYMBOL),
                new LexerToken("LOGICAL NOT", "!", LexerToken.Type.SYMBOL),
                new LexerToken("BITWISE NOT", "~", LexerToken.Type.SYMBOL),
                // logical plural
                new LexerToken("LOGICAL AND", "&&", LexerToken.Type.SYMBOL),
                new LexerToken("LOGICAL OR", "||", LexerToken.Type.SYMBOL),
                // assign simple
                new LexerToken("ASSIGNMENT", "=", LexerToken.Type.SYMBOL),
                // assign with arithmetic
                new LexerToken("ASSIGNMENT ADDITION", "+=", LexerToken.Type.SYMBOL),
                new LexerToken("ASSIGNMENT SUBTRACTION", "-=", LexerToken.Type.SYMBOL),
                new LexerToken("ASSIGNMENT MULTIPLICATION", "*=", LexerToken.Type.SYMBOL),
                new LexerToken("ASSIGNMENT DIVISION", "/=", LexerToken.Type.SYMBOL),
                new LexerToken("ASSIGN REMAINDER", "%=", LexerToken.Type.SYMBOL),
                // logical shifts
                new LexerToken("SHIFT LEFT", "<<", LexerToken.Type.SYMBOL),
                new LexerToken("SHIFT RIGHT", ">>", LexerToken.Type.SYMBOL),
                new LexerToken("SHIFT RIGHT ZERO FILL", ">>>", LexerToken.Type.SYMBOL),
                // assign simple
                // assign with logical
                new LexerToken("ASSIGNMENT BITWISE AND", "&=", LexerToken.Type.SYMBOL),
                new LexerToken("ASSIGNMENT BITWISE XOR", "^=", LexerToken.Type.SYMBOL),
                new LexerToken("ASSIGNMENT BITWISE OR", "|=", LexerToken.Type.SYMBOL),
                // equality
                new LexerToken("EQUALITY", "==", LexerToken.Type.SYMBOL),
                new LexerToken("STRICT EQUALITY", "===", LexerToken.Type.SYMBOL),
                // non-equality
                new LexerToken("INEQUALITY", "!=", LexerToken.Type.SYMBOL),
                new LexerToken("STRICT UNEQUALITY", "!==", LexerToken.Type.SYMBOL),
                // comparing
                new LexerToken("GREATER THAN", ">", LexerToken.Type.SYMBOL),
                new LexerToken("LESS THAN", "<", LexerToken.Type.SYMBOL),
                // comparing with eqauls
                new LexerToken("GREATER THAN OR EQUALS", ">=", LexerToken.Type.SYMBOL),
                new LexerToken("LESS THAN OR EQUALS", "<=", LexerToken.Type.SYMBOL),
                // ternary operator
                new LexerToken("THERNARY OPERATOR", "?", LexerToken.Type.SYMBOL),

                // brackets
                new LexerToken("LEFT BRACE", "{", LexerToken.Type.SYMBOL),
                new LexerToken("RIGHT BRACE", "}", LexerToken.Type.SYMBOL),
                new LexerToken("LEFT PARENTHESIS", "(", LexerToken.Type.SYMBOL),
                new LexerToken("RIGHT PARENTHESIS", ")", LexerToken.Type.SYMBOL),
                new LexerToken("LEFT SQUARE BRACKET", "[", LexerToken.Type.SYMBOL),
                new LexerToken("RIGHT SQUARE BRACKET", "]", LexerToken.Type.SYMBOL),

                // separating symbols
                new LexerToken("COLON", ":", LexerToken.Type.SYMBOL),
                new LexerToken("SEMICOLON", ";", LexerToken.Type.SYMBOL),
                new LexerToken("COMMA", ",", LexerToken.Type.SYMBOL),


                // reserved words
                // variable
                new LexerToken("VARIABLE", "var", LexerToken.Type.WORD),
                // branching
                new LexerToken("IF", "if", LexerToken.Type.WORD),
                new LexerToken("ELSE", "else", LexerToken.Type.WORD),
                new LexerToken("SWITCH", "switch", LexerToken.Type.WORD),
                new LexerToken("CASE", "case", LexerToken.Type.WORD),
                // cycles
                new LexerToken("WHILE", "while", LexerToken.Type.WORD),
                new LexerToken("DO", "do", LexerToken.Type.WORD),
                new LexerToken("FOR", "for", LexerToken.Type.WORD),
                // breakers
                //
                new LexerToken("BREAK", "break", LexerToken.Type.WORD),
                new LexerToken("CONTINUE", "continue", LexerToken.Type.WORD),
                // type checking
                new LexerToken("STRING CHECK", "String", LexerToken.Type.WORD),
                new LexerToken("NUMBER CHECK", "Number", LexerToken.Type.WORD),
                new LexerToken("BOOLEAN CHECK", "Boolean", LexerToken.Type.WORD),


        };
    }

    @Override
    public void analise(String expression) throws LexicalException {

        int currentPosition = 0;
        char currentCharacter;
        String currentLexeme = "";


        // direct passage throw expression
        outerLoop:
        while (currentPosition < expression.length() - 1) {

            currentCharacter = expression.charAt(currentPosition);

            // check if space
            if (currentCharacter == ' ') {
                if (currentPosition < expression.length() - 1) {
                    currentPosition++;
                }
                continue;
            }


            if (Character.isDigit(currentCharacter)) {

                do {
                    currentLexeme += currentCharacter;

                    if (currentPosition < expression.length() - 1) {

                        currentPosition++;

                        if (Character.isLetter(expression.charAt(currentPosition))) {
                            throw new LexicalException("Wrong variable assignment! Variable can't start from the digit.");
                        }

                    }
                } while (Character.isDigit(currentCharacter = expression.charAt(currentPosition)));


                lexemeTable.add(new LexerToken("CONSTANT", currentLexeme, LexerToken.Type.CONSTANT, currentPosition));
                currentPosition++;
                continue outerLoop;


            }
        }


        // check variables, keywords and symbols


    }


    @Override
    public List<LexerToken> getLexemeTable() {

        if (lexemeTable == null) {
            lexemeTable = new ArrayList<>();
        }
        return lexemeTable;

    }
}
