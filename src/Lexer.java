import java.util.List;

public interface Lexer {

    /**
     * Getter for lexeme table
     * @return lexemes, which were got from the expression
     */
    public List<LexerToken> getLexemeTable();

    /**
     * Analise input string and creates lexeme table
     * for this expression
     * @throws LexicalException if wrong lexeme is in expression
     */
    public void analise() throws LexicalException;

    public void outputExpression();

    public void outputLexerTable();

    void setExpression(String expression);
}
