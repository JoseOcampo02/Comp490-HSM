package myLang.tokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

public class TokenizerTest {
    
    @Test
    public void testIdentifierEquals() {
        // Tests if two identifiers are equals based on the strings
        // they contain internally
        assertEquals(new IdentifierToken("foo"),
                     new IdentifierToken("foo"));
    }
    
    
    @Test
    public void testTokenizeIdentifier() throws TokenizerException {
        final Token[] tokens = Tokenizer.tokenize("foo");
        final Token[] expected = new Token[] { new IdentifierToken("foo")};
        assertArrayEquals(expected, tokens);
    }
    
    @Test
    public void testTokenizeWholeVardec() throws TokenizerException {
        final Token[] tokens = Tokenizer.tokenize("(vardec int x 7)");
        final Token[] expected = new Token[] {
                new LeftParenToken(),
                new VardecToken(),
                new IntToken(),
                new IdentifierToken("x"),
                new NumberToken(7),
                new RightParenToken()
        };
        assertArrayEquals(expected, tokens);
    }
    
    @Test
    public void testTokenizeAssign() throws TokenizerException {
        final Token[] tokens = Tokenizer.tokenize("(= y 5)");
        final Token[] expected = new Token[] {
                new LeftParenToken(),
                new SingleEqualsToken(),
                new IdentifierToken("y"),
                new NumberToken(5),
                new RightParenToken()
        };
    }
    
    @Test
    public void testTokenizeWholeLoop() throws TokenizerException{
        final Token[] tokens = Tokenizer.tokenize("(while (< x 10) (= x (+ x 1)))");
        final Token[] expected = new Token[] {
                new LeftParenToken(),
                new WhileToken(),
                new LeftParenToken(),
                new LessThanToken(),
                new IdentifierToken("x"),
                new NumberToken(10),
                new RightParenToken(),
                new LeftParenToken(),
                new SingleEqualsToken(),
                new IdentifierToken("x"),
                new LeftParenToken(),
                new PlusToken(),
                new IdentifierToken("x"),
                new NumberToken(1),
                new RightParenToken(),
                new RightParenToken(),
                new RightParenToken()
        };
        assertArrayEquals(tokens, expected);
    }
    
    @Test
    public void testTokenizeWholeVardecWithWhitespace() throws TokenizerException{
        final Token[] tokens = Tokenizer.tokenize("  (    vardec  int  x   7 )    ");
        final Token[] expected = new Token[] {
                new LeftParenToken(),
                new VardecToken(),
                new IntToken(),
                new IdentifierToken("x"),
                new NumberToken(7),
                new RightParenToken()
        };
        assertArrayEquals(expected, tokens);
    }
    
    @Test
    public void testTokenizeEmptyString() throws TokenizerException{
        final Token[] tokens = Tokenizer.tokenize("");
        final Token[] expected = new Token[0];
        assertArrayEquals(tokens, expected);
    }
    
    @Test
    public void testTokenizeWhitespaceOnly() throws TokenizerException{
        final Token[] tokens = Tokenizer.tokenize("              ");
        final Token[] expected = new Token[0];
        assertArrayEquals(tokens, expected);
    }
    
    @Test (expected = TokenizerException.class)
    public void testUnexpectedSymbolFailure() throws TokenizerException {
        Tokenizer.tokenize("(* 3 4)");
    }
    
    // (vardec int x 0)
    // (while (< x 10)
    //     (= x (+ x 1))
    // )
    
    @Test
    public void testTokenizeSimpleWhileLoopProgram() throws TokenizerException {
        final Token[] tokens = Tokenizer.tokenize("(vardec int x 0) (while (< x 10) (= x (+ x 1)))");
        final Token[] expected = new Token[] {
            new LeftParenToken(),
            new VardecToken(),
            new IntToken(),
            new IdentifierToken("x"),
            new NumberToken(0),
            new RightParenToken(),
            new LeftParenToken(),
            new WhileToken(),
            new LeftParenToken(),
            new LessThanToken(),
            new IdentifierToken("x"),
            new NumberToken(10),
            new RightParenToken(),
            new LeftParenToken(),
            new SingleEqualsToken(),
            new IdentifierToken("x"),
            new LeftParenToken(),
            new PlusToken(),
            new IdentifierToken("x"),
            new NumberToken(1),
            new RightParenToken(),
            new RightParenToken(),
            new RightParenToken()
        };
        assertArrayEquals(tokens, expected);
    }
    
}