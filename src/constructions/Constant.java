package constructions;

public class Constant extends Node {

    private String value;

    public Constant(String name, Type type, String value) {
        super(name, type);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
