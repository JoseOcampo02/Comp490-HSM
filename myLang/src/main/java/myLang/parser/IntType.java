package myLang.parser;

public class IntType implements Type{

    @Override
    public boolean equals(Object other) {
        return other instanceof IntType;
    }
    
    @Override 
    public int hashCode() {
        return 0;
    }
    
    @Override 
    public String toString() {
        return "IntType";
    }

}
