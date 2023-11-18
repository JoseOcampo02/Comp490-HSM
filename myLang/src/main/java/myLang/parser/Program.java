package myLang.parser;
import java.util.List;
import java.util.ArrayList;

public class Program {

    public final List<Stmt> statements;
    
    public Program(final List<Stmt> statements) {
        this.statements = statements;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other instanceof Program) {
            final Program otherProgram = (Program)other;
            return statements.equals(otherProgram.statements);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return statements.hashCode();
    }

    @Override
    public String toString() {
        return "Program(" + statements.toString() +")";
    }
    
}
