package myLang.typechecker;
import java.util.Map;
import java.util.HashMap;

import myLang.parser.*;

public class Typechecker {

    public static Type typecheckBin(final BinaryOperatorExp binOpExp,
                                    final Map<Variable, Type> typeEnv) throws TypeErrorException{
        final Type leftType = typecheckExp(binOpExp.left, typeEnv);
        final Type rightType = typecheckExp(binOpExp.right, typeEnv);
        final Op op = binOpExp.op;
        
        if ((op instanceof PlusOp || op instanceof MinusOp) &&
             (leftType instanceof IntType && rightType instanceof IntType)) {
            return new IntType();
        } else if ((op instanceof LogicalAndOp || op instanceof LogicalOrOp) &&
                    (leftType instanceof BoolType && rightType instanceof BoolType)) {
            return new BoolType();
        } else if (op instanceof LessThanOp &&
                   leftType instanceof IntType &&
                   rightType instanceof IntType) {
            return new BoolType();
        } else {
            assert(false);
            throw new TypeErrorException("Typechecker Exception."
                                     + "\n  ill-typed binary operator expression."
                                     + "\n  " + binOpExp.toString());
        }
        
    } // typecheckBin
    
    public static Type typecheckExp(final Exp exp,
                                    final Map<Variable, Type> typeEnv) throws TypeErrorException{
        if(exp instanceof NumberLiteralExp) {
            return new IntType();
        } else if (exp instanceof BooleanLiteralExp) {
            return new BoolType();
        } else if (exp instanceof VariableExp) {
            final Variable variable = ((VariableExp)exp).variable;
            if (typeEnv.containsKey(variable)) {
                return typeEnv.get(variable);
            } else {
                throw new TypeErrorException("Typechecker Exception."
                                         + "\n  Variable " + ((VariableExp)exp).variable.toString() + " not in scope."
                                         + "\n  " + exp.toString());
            }
        } else if (exp instanceof BinaryOperatorExp) {
            return typecheckBin((BinaryOperatorExp)exp, typeEnv);
        } else {
            assert(false);
            throw new TypeErrorException("Unrecognized expression: " + exp.toString());
        }
    } // typecheckExp
    
    public static Map<Variable, Type> addToMap(final Variable variable,
                                               final Map<Variable, Type> typeEnv,
                                               final Type type) throws TypeErrorException {
        final Map<Variable, Type> retval = new HashMap<Variable, Type>();
        retval.putAll(typeEnv);
        retval.put(variable, type);
        return retval;
    }
    
    
    
    public static Map<Variable, Type> typecheckVardecStmt(final VardecStmt stmt,
                                                          final Map<Variable, Type> typeEnv) throws TypeErrorException {
        final Type expType = typecheckExp(stmt.exp, typeEnv);
        if (stmt.type.equals(expType)) {
            return addToMap(stmt.variable, typeEnv, expType);
        } else {
            throw new TypeErrorException("Typechecker Exception."
                                    + "\n  ill-typed vardec statement."
                                    + "\n  Cannot declare " + stmt.type.toString() + " variable with a " + expType.toString() + " value."
                                    + "\n  " + stmt.toString());
        }
    }
    
    public static Map<Variable, Type> typecheckWhileStmt(final WhileStmt stmt,
                                                         final Map<Variable, Type> typeEnv) throws TypeErrorException {
        // Check that the while condition check (the guard) is of BoolType
        if (typecheckExp(stmt.guard, typeEnv) instanceof BoolType){
            typecheckStmt(stmt.body, typeEnv);
            return typeEnv;
        } else {
            throw new TypeErrorException("Typechecker Exception."
                                    + "\n  ill-typed while statement."
                                    + "\n  While condition check should be BoolType"
                                    + "\n  " + stmt.toString());
        }
    }
    
    public static Map<Variable, Type> typecheckAssignStmt(final AssignStmt stmt,
                                                          final Map<Variable, Type> typeEnv) throws TypeErrorException {
        // Check that the variable is in scope
        if (typeEnv.containsKey(stmt.variable)) {
            final Type varType = typeEnv.get(stmt.variable);
            final Type expType = typecheckExp(stmt.exp, typeEnv);
            // Check for variable and expression mismatch
            if (varType.equals(expType)) {
                return typeEnv;
            } else {
                throw new TypeErrorException("Typechecker exception."
                        + "\n  ill-typed assign statement."
                        + "\n  Cannot assign " + varType.toString() + " variable a " + expType.toString() + " value."
                        + "\n  " + stmt.toString());
            }
            
        } else {
            throw new TypeErrorException("Typechecker exception."
                                     + "\n  Variable " + stmt.variable.toString() + " not in scope."
                                     + "\n  " + stmt.toString());
        }
    } // typecheckAssign
    
    public static Map<Variable, Type> typecheckStmt(final Stmt stmt,
                                                    final Map<Variable, Type> typeEnv) throws TypeErrorException {
        if (stmt instanceof VardecStmt) {
            return typecheckVardecStmt((VardecStmt)stmt, typeEnv);
        } else if (stmt instanceof WhileStmt) {
            return typecheckWhileStmt((WhileStmt)stmt, typeEnv);
        } else if (stmt instanceof AssignStmt) {
            return typecheckAssignStmt((AssignStmt)stmt, typeEnv);
        } else {
            assert(false);
            throw new TypeErrorException("Unrecognized statement: " + stmt.toString());
        }
    }
    
    public static void typecheckProgram(final Program program) throws TypeErrorException {
        Map<Variable, Type> typeEnv = new HashMap<Variable, Type>();
        for (final Stmt stmt: program.statements) {
            typeEnv = typecheckStmt(stmt,typeEnv);
        }
    } // typecheckProgram

} // Typechecker
