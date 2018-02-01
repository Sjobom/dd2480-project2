import org.eclipse.jetty.server.Server;
import ciserver.*;
import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class MainTest {

    // @Test
    // public void testMethod() {
    //    // Contract:
    //    assertEquals(oracle, value);
    //}

    @Test
    public void testServerCreation(){
        // Contract: Test that the createServer-method start the server
        try {
            ContinuousIntegrationServer server = ContinuousIntegrationServer.createServer(8080);
            assertEquals("STARTED", server.getState());
        } catch (Exception e) {
            fail("Exception in server creation: \n" + e);
        }
    }
    
    @Test
    public void test1() {
        //contract: Tests that ShellCommand class can execute
        //a given command
        String r = ShellCommand.exec("echo test");
        assertEquals("test\n", r);
    }
}
