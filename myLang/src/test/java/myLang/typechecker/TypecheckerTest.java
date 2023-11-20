package myLang.typechecker;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import myLang.tokenizer.*;
import myLang.parser.*;
import myLang.typechecker.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class TypecheckerTest {

    public static final HashMap<Variable, Type> EMPTY_TYPE_ENV = new HashMap<Variable, Type>();
    
    @Test
    public void testTypecheckNum() throws TypeErrorException{
        assertEquals(new IntType(),
                     Typechecker.typecheckExp(new NumberLiteralExp(7), EMPTY_TYPE_ENV));
    }
    
    @Test
    public void testTypecheckBool() throws TypeErrorException{
        assertEquals(new BoolType(),
                     Typechecker.typecheckExp(new BooleanLiteralExp(true), EMPTY_TYPE_ENV));
    }
    
    
    // (vardec int x 0)
    // (vardec int y (+ x x))
    @Test
    public void testTypecheckVardecs() throws TypeErrorException {
        final List<Stmt> stmts = new ArrayList<Stmt>();
        stmts.add(new VardecStmt(new IntType(),
                                 new Variable("x"),
                                 new NumberLiteralExp(0)));
        stmts.add(new VardecStmt(new IntType(),
                                 new Variable("y"),
                                 new BinaryOperatorExp(new PlusOp(),
                                                       new VariableExp(new Variable("x")),
                                                       new VariableExp(new Variable("x")))));
        Typechecker.typecheckProgram(new Program(stmts));
        
        
    }
    
    @Test
    public void testTypecheckVardecsWithTokenizerAndParser() throws TokenizerException,
                                                                ParseException,
                                                                TypeErrorException{
        final Token[] input = Tokenizer.tokenize("(vardec int x 0) (vardec int y (+ x x))");
        final Parser parser = new Parser(input);
        Typechecker.typecheckProgram(parser.parseProgram(0).result);
        
    }
    
    // (vardec int x 0)
    // (while (< x 10)
    //     (= x (+ x 1))
    // )
    @Test
    public void testTypecheckSimpleLoopProgramWithTokenizerAndParser() throws TokenizerException,
                                                                ParseException,
                                                                TypeErrorException{
        final Token[] input = Tokenizer.tokenize("(vardec int x 0) (while (< x 10) (= x (+ x 1)))");
        final Parser parser = new Parser(input);
        Typechecker.typecheckProgram(parser.parseProgram(0).result);
        
    }
    
    // Check if typechecker can detect ill-typed statement
    // (vardec int x true)
    
    @Test (expected = TypeErrorException.class)
    public void testTypecheckVardecTypeMismatch() throws TypeErrorException, ParseException, TokenizerException {
        
        final Token[] input = Tokenizer.tokenize("(vardec int x true)");
        final Parser parser = new Parser(input);
        Typechecker.typecheckProgram(parser.parseProgram(0).result);
        
    }
    
    // Test if typechecker can recognize out of scope variable
    // (vardec int x true)
    
    @Test (expected = TypeErrorException.class)
    public void testTypecheckVariableOutOfScope() throws TypeErrorException, ParseException, TokenizerException {
        
        final Token[] input = Tokenizer.tokenize("(vardec int x y)");
        final Parser parser = new Parser(input);
        Typechecker.typecheckProgram(parser.parseProgram(0).result);
        
    }

}
