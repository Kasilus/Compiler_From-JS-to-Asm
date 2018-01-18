import errors.ExceptionPosition;

public class LexerToken {

    enum Type {
        VAR, CONSTANT, SYMBOL, WORD, EOF
    }

    private String name, value;
    private Type type;
    private ExceptionPosition position;

    public LexerToken(String name, String value, Type type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public LexerToken(String name, String value, Type type, ExceptionPosition position) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ExceptionPosition getPosition() {
        return position;
    }

    public void setPosition(ExceptionPosition position) {
        this.position = position;
    }
}
