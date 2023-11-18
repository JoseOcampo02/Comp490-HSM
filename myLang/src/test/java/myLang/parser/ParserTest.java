package myLang.parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import myLang.tokenizer.*;

public class ParserTest {

    @Test
    public void testTypeEquals() {
        assertEquals(new IntType(), new IntType());
    }
    
    
    @Test
    public void testParseIntType() {
        final Token[] input = new Token[] {
                new IntToken()
        };
        assertEquals(new IntType(), Parser.parse(input));
    }
    
    

}
