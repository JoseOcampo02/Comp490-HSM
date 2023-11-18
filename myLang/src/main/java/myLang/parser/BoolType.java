package myLang.parser;

public class BoolType implements Type{

    @Override
    public boolean equals(Object other) {
        return other instanceof BoolType;
    }
    
    @Override 
    public int hashCode() {
        return 1;
    }
    
    @Override 
    public String toString() {
        return "BoolType";
    }

}
