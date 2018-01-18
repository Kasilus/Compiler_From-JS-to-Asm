import errors.LexicalException;

import java.util.List;

public interface Lexer {


    public List<LexerToken> getLexemeTable();

    public void analise() throws LexicalException;

    public void outputExpression();

    public void outputLexerTable();

    void setExpression(String expression);

    void createLexemeTableFromExpression(String expression);
}
