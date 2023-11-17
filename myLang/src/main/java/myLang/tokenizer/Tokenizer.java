package myLang.tokenizer;
import java.util.List;
import java.util.ArrayList;

public class Tokenizer {

    // This implementation of identifying symbols allows us to more easily add
    // additional symbol later on without changing the tokenizer itself very much
    private final SymbolPair[] SYMBOLS = new SymbolPair[] {
            new SymbolPair("(", new LeftParenToken()),
            new SymbolPair(")", new RightParenToken()),
            new SymbolPair("=", new SingleEqualsToken()),
            new SymbolPair("+", new PlusToken()),
            new SymbolPair("-", new MinusToken()),
            new SymbolPair("&&", new LogicalAndToken()),
            new SymbolPair("||", new LogicalOrToken()),
            new SymbolPair("<", new LessThanToken())
            
    };
    private final String input;
    private int position;
    
    public Tokenizer(final String input) {
        this.input = input;
        this.position = 0;
    }
    
    public void skipWhitespace() {
        while (position < input.length() && 
               Character.isWhitespace(input.charAt(position)))
            position++;
    }
    
    public Token tokenizeSymbol() {
        for (final SymbolPair symbol: SYMBOLS) {
            if (input.startsWith(symbol.asString, position)) {
                position += symbol.asString.length();
                return symbol.asToken;
            }
        }
        return null;
    }
    
    public Token tokenizeIdentifierOrReservedWord() {
        
        String name = "";
        if (Character.isLetter(input.charAt(position))) {
            
            name += input.charAt(position);
            position++;
            while(position < input.length() && 
                  Character.isLetterOrDigit(input.charAt(position))) {
                
                name += input.charAt(position);
                position++;
                
            }
            
            if (name.equals("int")) {
                return new IntToken();
            } else if (name.equals("bool")) {
                return new BoolToken();
            } else if (name.equals("vardec")) {
                return new VardecToken();
            } else if (name.equals("true")) {
                return new  TrueToken();
            } else if (name.equals("false")) {
                return new FalseToken();
            } else if (name.equals("while")) {
                return new WhileToken();
            } else {
                return new IdentifierToken(name);
            }
            
        } else {
            return null;
        }
        
    }
    
    public NumberToken tokenizeNumber() {
        
        String digits = "";
        while(position < input.length() && 
              Character.isDigit(input.charAt(position))) {
            digits += input.charAt(position);
            position++;
        }
        if (digits.length() > 0) {
            return new NumberToken(Integer.parseInt(digits));
        } else {
            return null;
        }
        
    }
    
    // Attempts to read a token from the current character position
    public Token readToken() throws TokenizerException{
        
        Token retval = null;
        
        retval = tokenizeSymbol();
        if (retval != null)
            return retval;
        
        retval = tokenizeNumber();
        if (retval != null)
            return retval;
        
        retval = tokenizeIdentifierOrReservedWord();
        if (retval != null)
            return retval;
        
        // Could not tokenize
        throw new TokenizerException("Could not tokenize " + 
                                     input.charAt(position));
        
    }
    
    public Token[] tokenize() throws TokenizerException{
        final List<Token> retval = new ArrayList<Token>();
        skipWhitespace();
        while (position < input.length()) {
            retval.add(readToken());
            skipWhitespace();
        }
        return retval.toArray(new Token[retval.size()]);
    }
    
    public static Token[] tokenize(final String input) throws TokenizerException{
        return new Tokenizer(input).tokenize();
    }
    
}
