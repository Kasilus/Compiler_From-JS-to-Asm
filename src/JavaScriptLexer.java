import java.util.ArrayList;
import java.util.List;

public class JavaScriptLexer implements Lexer {

    private LexerToken[] tokens;
    private List<LexerToken> lexemeTable;
    private final int MAX_SYMBOL_LENGTH = 3;
    private String expression;
    private int currentRow = 1;
    private int currentCol = 1;

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
                new LexerToken("ASSIGNMENT REMAINDER", "%=", LexerToken.Type.SYMBOL),
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
                new LexerToken("VARIABLE TYPE", "var", LexerToken.Type.WORD),
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
                // null and unsigned
                new LexerToken("NULL", "null", LexerToken.Type.WORD),
                new LexerToken("UNSIGNED", "unsigned", LexerToken.Type.WORD),


        };
    }

    @Override
    public void analise() throws LexicalException {

        int currentPosition = 0;
        char currentCharacter = expression.charAt(currentPosition);
        String currentLexeme = "";


        // direct passage throw expression
        outerLoop:
        while (currentPosition < expression.length() - 1) {

            currentLexeme = "";

            // check if space
            if (currentCharacter == ' ') {
                if (currentPosition < expression.length() - 1) {
                    currentCol++;
                    currentPosition++;
                    currentCharacter = expression.charAt(currentPosition);
                }
                continue;
            }


            if (Character.isDigit(currentCharacter)) {

                boolean hasPoint = false;

                do {
                    currentLexeme += currentCharacter;

                    if (currentPosition < expression.length() - 1) {

                        currentCol++;
                        currentPosition++;

                        if (Character.isLetter(expression.charAt(currentPosition))) {
                            throw new LexicalException("Wrong variable assignment! Variable can't start from the digit.", new Position(currentRow, currentCol));
                        } else if (currentCharacter == '.'){
                            if (!hasPoint) {
                                hasPoint = true;
                            } else {
                                throw new LexicalException("Allowed just one point in a constant", new Position(currentRow, currentCol));
                            }
                        }



                    }
                } while (Character.isDigit(currentCharacter = expression.charAt(currentPosition)) || currentCharacter == '.');


                lexemeTable.add(new LexerToken("CONSTANT", currentLexeme, LexerToken.Type.CONSTANT, new Position((currentRow), currentCol - currentLexeme.length())));
                continue outerLoop;

            }

            // check variables, keywords and symbols

            // firstly, check words and vars
            if (String.valueOf(currentCharacter).matches("[A-Za-z$_]")) {

                do {
                    currentLexeme += currentCharacter;

                    if (currentPosition < expression.length() - 1) {

                        currentCol++;
                        currentPosition++;

                    }

                }
                while (String.valueOf(currentCharacter = expression.charAt(currentPosition)).matches("[A-Za-z$_0-9]"));

                for (int i = 0; i < tokens.length; i++) {
                    if (tokens[i].getValue().equals(currentLexeme)) {
                        lexemeTable.add(new LexerToken(tokens[i].getName(), currentLexeme, LexerToken.Type.WORD, new Position(currentRow, currentCol - currentLexeme.length() + 1)));
//                        currentPosition++;
                        continue outerLoop;
                    }
                }


                lexemeTable.add(new LexerToken("VARIABLE", currentLexeme, LexerToken.Type.VAR, new Position(currentRow, currentCol - currentLexeme.length() + 1)));
                continue;


            }

            // check symbols
            if (String.valueOf(currentCharacter).matches("[+\\-*/&|^<>!=?{}\\[\\]()%.:;~]")) {

                LexerToken symbolToken = null;
                int currentSymbolLength = 1;

                currentLexeme += currentCharacter;


                do {

                    for (int j = 0; j < tokens.length; j++) {
                        if (tokens[j].getValue().equals(currentLexeme)) {
                            symbolToken = tokens[j];
                        }
                    }

                    if (currentPosition < expression.length() - 1) {
                        currentCol++;
                        currentPosition++;
                    }

                    currentSymbolLength++;

                    currentCharacter = expression.charAt(currentPosition);
                    currentLexeme += currentCharacter;

                }
                while (currentSymbolLength < MAX_SYMBOL_LENGTH);


                if (symbolToken != null) {
                    lexemeTable.add(new LexerToken(symbolToken.getName(), symbolToken.getValue(), symbolToken.getType(), new Position((currentRow), currentCol - currentLexeme.length() + 1)));
                    // correction of cur pos
                    currentCol = currentCol - (MAX_SYMBOL_LENGTH - symbolToken.getValue().length()) + 1;
                    currentPosition = currentPosition - (MAX_SYMBOL_LENGTH - symbolToken.getValue().length()) + 1;
                    currentCharacter = expression.charAt(currentPosition);
                    continue outerLoop;
                } else {
                    currentCol = currentCol - MAX_SYMBOL_LENGTH + 1;
                    throw new LexicalException("Unexpected symbol combination!", new Position(currentRow, currentCol));
                }

            }

            if (currentCharacter == '\n'){

                    currentRow++;
                    currentCol = 1;
                    currentPosition++;
                    currentCharacter = expression.charAt(currentPosition);
                    continue outerLoop;

            }

            // throw exception if mismatch
            throw new LexicalException("Unexpected character! : '" + currentLexeme + "'", new Position(currentRow, currentCol));


        }


    }

    @Override
    public void outputExpression() {
        if (lexemeTable == null){
            return;
        }

        String[] rows = expression.split("\n");

        for (int i = 0 ; i < rows.length; i++){
            System.out.println(rows[i]);
        }

        System.out.println();
    }

    @Override
    public void outputLexerTable() {
        if (lexemeTable == null){
            return;
        }


        for (LexerToken token : lexemeTable){

            System.out.print(token.getValue() + "\t\t\t\t|" + token.getName() + "\n");
        }
    }

    @Override
    public void setExpression(String expression) {
        this.expression = expression;
    }


    @Override
    public List<LexerToken> getLexemeTable() {

        if (lexemeTable == null) {
            lexemeTable = new ArrayList<>();
        }
        return lexemeTable;

    }
}
