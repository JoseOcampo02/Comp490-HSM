package myLang.parser;

public class BooleanLiteralExp implements Exp{

    public final boolean value;
    
    public BooleanLiteralExp(final boolean value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof BooleanLiteralExp &&
                value == ((BooleanLiteralExp)other).value);
    }
    
    @Override
    public int hashCode() {
        // Return 1 if true, 0 if false
        return (value) ? 1 : 0;
    }
    
    @Override
    public String toString() {
        return "BooleanLiteralExp(" + value + ")";
    }

}
