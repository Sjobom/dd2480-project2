import ciserver.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainTest {

    // @Test
    // public void testMethod() {
    //    // Contract:
    //    assertEquals(oracle, value);
    //}
    @Test
    public void test1() {
        //contract: Tests that ShellCommand class can execute
        //a given command
        String r = ShellCommand.exec("echo test");
        assertEquals("test\n", r);
    }
}