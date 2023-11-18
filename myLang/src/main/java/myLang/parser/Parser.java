package myLang.parser;
import java.util.ArrayList;
import java.util.List;

import myLang.tokenizer.*;

import myLang.tokenizer.Token;

// GRAMMAR
//-----------------------------------------------------------------------------------  

public class Parser {
    
    private final Token[] tokens;
    
    public Parser(final Token[] tokens) {
        this.tokens = tokens;
    }

    public Token getToken(final int position) throws ParseException {
        
        if (position >= 0 && position < tokens.length) {
            return tokens[position];
        } else {
            throw new ParseException("Out of tokens.");
        }
        
    }
    
    public void assertTokenIs(final int position, final Token expected) throws ParseException {
        final Token received = getToken(position);
        if (!expected.equals(received)) {
            throw new ParseException("ParseException. Expected: " + expected.toString() +
                                                      ", received: " + received.toString());
        }
    }
    
    // Expected entry point
    public static Program parseProgram(final Token[] tokens) throws ParseException {
        final Parser parser = new Parser(tokens);
        final ParseResult<Program> program = parser.parseProgram(0);
        // Should be no more token to process
        if (program.nextPosition == tokens.length) {
            return program.result;
        } else {
            throw new ParseException("ParseException. Remaining tokens at end, starting with " + 
                                                     parser.getToken(program.nextPosition).toString());
        }
    }
    
    // program ::= statement*
    public ParseResult<Program> parseProgram(int position) throws ParseException {
        final List<Stmt> stmts = new ArrayList<Stmt>();
        boolean shouldRun = true;
        while (shouldRun) {
            try {
                final ParseResult<Stmt> stmt = parseStmt(position);
                stmts.add(stmt.result);
                position = stmt.nextPosition;
            } catch (final ParseException e) {
                e.getMessage();
                shouldRun = false;
                System.out.println("setting false");
            }
        }
        
        return new ParseResult<Program>(new Program(stmts),
                                        position);
    }
    
    // expression ::= var | num | 'true' | 'false' | '(' op expression expression ')'
    public ParseResult<Exp> parseExp(final int position) throws ParseException {
        final Token token = getToken(position);
        if (token instanceof NumberToken) {
            return new ParseResult<Exp>(new NumberLiteralExp(((NumberToken)token).value),
                                        position + 1);
        } else if (token instanceof TrueToken) {
            return new ParseResult<Exp>(new BooleanLiteralExp(true),
                                        position + 1);
        } else if (token instanceof FalseToken) {
            return new ParseResult<Exp>(new BooleanLiteralExp(false),
                                        position + 1);
        } else if (token instanceof IdentifierToken) {
            return new ParseResult<Exp>(new VariableExp(new Variable(((IdentifierToken)token).name)),
                                        position + 1);
        } else if (token instanceof LeftParenToken) {
            final ParseResult<Op> op = parseOp(position + 1);
            final ParseResult<Exp> left = parseExp(op.nextPosition);
            final ParseResult<Exp> right = parseExp(left.nextPosition);
            assertTokenIs(right.nextPosition, new RightParenToken());
            
            return new ParseResult<Exp>(new BinaryOperatorExp(op.result,
                                                              left.result,
                                                              right.result),
                                        right.nextPosition + 1);
        } else {
            throw new ParseException("ParseException. Expected Expression, received " + token.toString());
        }
    }
    
    // op ::= '+' | '-' | '&&' | '||' | '<' | ... 
    public ParseResult<Op> parseOp(final int position) throws ParseException {
        final Token token = getToken(position);
        Op op = null;
        if (token instanceof PlusToken) {
            op = new PlusOp();
        } else if (token instanceof MinusToken) {
            op = new MinusOp();
        } else if (token instanceof LogicalAndToken) {
            op = new LogicalAndOp();
        } else if (token instanceof LogicalOrToken) {
            op = new LogicalOrOp();
        } else if (token instanceof LessThanToken) {
            op = new LessThanOp();
        } else {
            throw new ParseException("ParseException. Expected Operator, received " + token.toString());
        }
        
        return new ParseResult<Op>(op, position + 1);
    }
    
    // num is a Number 
    public ParseResult<NumberLiteralExp> parseNumber(final int position) throws ParseException {
        final Token token = getToken(position);
        if (token instanceof NumberLiteralExp) {
            return new ParseResult<NumberLiteralExp>(new NumberLiteralExp(((NumberToken)token).value),
                                                     position + 1);
        } else {
            throw new ParseException("ParseException. Expected Number, received " + token.toString());
        }
    }
    
    // var is a Variable 
    public ParseResult<Variable> parseVariable(final int position) throws ParseException {
        final Token token = getToken(position);
        if (token instanceof IdentifierToken) {
            return new ParseResult<Variable>(new Variable(((IdentifierToken)token).name),
                                                    position + 1);
        } else {
            throw new ParseException("ParseException. Expected Variable, received " + token.toString());
        }
    }
    
    // assign ::= '(' '=' var expression ')'
    public ParseResult<Stmt> parseAssign(final int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new SingleEqualsToken());
        final ParseResult<Variable> variable = parseVariable(position + 2);
        final ParseResult<Exp> exp = parseExp(variable.nextPosition);
        assertTokenIs(exp.nextPosition, new RightParenToken());
        
        return new ParseResult<Stmt>(new AssignStmt(variable.result,
                                                          exp.result),
                                           exp.nextPosition + 1);
    }
    
    // loop ::= '(' 'while' expression statement ')'
    public ParseResult<Stmt> parseLoop(final int position) throws ParseException {
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new WhileToken());
        final ParseResult<Exp> exp = parseExp(position + 2);
        final ParseResult<Stmt> stmt = parseStmt(exp.nextPosition);
        assertTokenIs(stmt.nextPosition, new RightParenToken());
        
        return new ParseResult<Stmt>(new WhileStmt(exp.result,
                                                        stmt.result),
                                          stmt.nextPosition + 1);
    }
    
    // vardec ::= '(' 'vardec' type var expression ')' 
    public ParseResult<Stmt> parseVardec(final int position) throws ParseException{
        assertTokenIs(position, new LeftParenToken());
        assertTokenIs(position + 1, new VardecToken());
        final ParseResult<Type> type = parseType(position + 2);
        final ParseResult<Variable> variable = parseVariable(type.nextPosition);
        final ParseResult<Exp> exp = parseExp(variable.nextPosition);
        assertTokenIs(exp.nextPosition, new RightParenToken());
        
        return new ParseResult<Stmt>(new VardecStmt(type.result,
                                                          variable.result,
                                                          exp.result),
                                           exp.nextPosition + 1);
    }
    
    // statement ::= vardec | loop | assign  
    public ParseResult<Stmt> parseStmt(final int position) throws ParseException {
        try {
            return parseVardec(position);
        } catch (final ParseException e1) {
            try {
                return parseLoop(position);
            } catch (final ParseException e2) {
                return parseAssign(position);
            }
        }
    }
    
    // type ::= 'int' | 'bool'
    public ParseResult<Type> parseType(final int position) throws ParseException{
        final Token token = getToken(position);
        if (token instanceof IntToken) {
            return new ParseResult<Type>(new IntType(), position + 1);
        } else if (token instanceof BoolToken){         
            return new ParseResult<Type>(new BoolType(), position + 1);
        } else {
            throw new ParseException("ParseException. Expected Type; received: " +
                                      token.toString());
        }
    }

}
