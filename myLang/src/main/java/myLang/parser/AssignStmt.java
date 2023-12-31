package myLang.parser;

public class AssignStmt implements Stmt{

    public final Variable variable;
    public final Exp exp;
    
    public AssignStmt(final Variable variable, final Exp exp) {
        this.variable = variable;
        this.exp = exp;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other instanceof AssignStmt) {
            final AssignStmt otherAssign = (AssignStmt)other;
            return (variable.equals(otherAssign.variable) &&
                    exp.equals(otherAssign.exp));
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return variable.hashCode() + exp.hashCode();
    }
    
    @Override
    public String toString() {
        return ("AssignStmt(" +
                variable.toString() + ", " +
                exp.toString() +")");
    }

}
