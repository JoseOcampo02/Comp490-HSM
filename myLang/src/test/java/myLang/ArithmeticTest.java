package myLang;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ArithmeticTest {

    @Test
    public void testAdd() {
        assertEquals(3, Arithmetic.add(1, 2));
    }
    
    @Test
    public void testSubtract() {
        assertEquals(-3, Arithmetic.subtract(3, 6));
    }

}
