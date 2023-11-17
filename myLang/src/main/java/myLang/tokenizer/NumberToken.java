package myLang.tokenizer;

public class NumberToken implements Token{

    public final int value;
    
    public NumberToken(final int value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object other) {
        return (other instanceof NumberToken &&    // same type of object
                value == ((NumberToken)other).value);  // same name
    }
    
    @Override
    public int hashCode() {
        return value;
    }
    
    @Override
    public String toString() {
        return "NumberToken(" + value + ")";
    }

}
