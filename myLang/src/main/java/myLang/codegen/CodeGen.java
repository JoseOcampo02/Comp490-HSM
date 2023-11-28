package myLang.codegen;
import myLang.parser.*;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class CodeGen {

    private final BufferedWriter writer;
    
    public CodeGen(final File outputFile) throws IOException {
        writer = new BufferedWriter(new FileWriter(outputFile));
    }
    
    public void writeOp (final Op op) throws IOException, CodeGenException {
        
        int opHashcode = op.hashCode();
        switch (opHashcode) {
        
        case (2):
            writer.write("+");
            break;
        case (3):
            writer.write("-");
            break;
        case (4):
            writer.write("&&");
            break;
        case (5):
            writer.write("||");
            break;
        case (6):
            writer.write("<");
            break;
        default:
            throw new CodeGenException("Unknown operator: " + op.toString());
        
        }
        
    }
    
    public void writeExp(final Exp exp) throws IOException, CodeGenException {
        
        if (exp instanceof NumberLiteralExp) {
            writer.write(Integer.toString(((NumberLiteralExp)exp).value));
        } else if (exp instanceof BooleanLiteralExp) {
            writer.write(Boolean.toString(((BooleanLiteralExp)exp).value));
        } else if (exp instanceof VariableExp) {
            writer.write(((VariableExp)exp).variable.name);
        } else if (exp instanceof BinaryOperatorExp) {
            // myLang:     '(' op expression expression ')'
            // JavaScript:  (expression op expression)
            // We MUST add the parenthesis to preserve the original semantics
            BinaryOperatorExp asBinOp = (BinaryOperatorExp)exp;
            writer.write("(");
            writeExp(asBinOp.left);
            writer.write(" ");
            writeOp(asBinOp.op);
            writer.write(" ");
            writeExp(asBinOp.right);
            writer.write(")");
        }
        
        assert(false);
    }
    
    // myLang:      '(' 'vardec' type var expression ')'
    // JavaScript:   let var = expression;
    public void writeVardec(final VardecStmt stmt) throws IOException, CodeGenException {
        writer.write("let " +
                     stmt.variable.name +
                     " = ");
        writeExp(stmt.exp);
        writer.write(";\n");
    }
    
    // myLang:      '(' "while' expression statement ')'
    // JavaScript:  while (expression) { statement }
    // (statement already adds semicolon)
    public void writeWhile(final WhileStmt stmt) throws IOException, CodeGenException {
        writer.write("while (");
        writeExp(stmt.guard);
        writer.write(") { ");
        writeStmt(stmt.body);
        writer.write(" }\n");
    }
    
    // myLang:     '(' '=' var expression ')'
    // JavaScript:  var = expression;
    public void writeAssign(final AssignStmt stmt) throws IOException, CodeGenException {
        
        writer.write(stmt.variable.name + " = ");
        writeExp(stmt.exp);
        writer.write(";\n");
        
    }
    
    public void writeStmt(final Stmt stmt) throws IOException, CodeGenException {
        if (stmt instanceof VardecStmt) {
            writeVardec((VardecStmt)stmt);
        } else if (stmt instanceof WhileStmt) {
            writeWhile((WhileStmt)stmt);
        } else if (stmt instanceof AssignStmt) {
            writeAssign((AssignStmt)stmt);
        } else {
            assert(false);
            throw new CodeGenException("Unknown statement: " + stmt.toString());
        }
    }
    
    public void writeProgram(final Program program) throws IOException, CodeGenException {
        
        for (final Stmt stmt: program.statements) {
            writeStmt(stmt);
        }
        
    }
    
    public void close() throws IOException {
        writer.close();
    }
    
    public static void writeProgram(final Program program,
                                    final File outputFile) throws IOException, CodeGenException {
        final CodeGen codegen = new CodeGen(outputFile);
        try {
            codegen.writeProgram(program);
        } finally {
            codegen.close();
        }
    }

}
