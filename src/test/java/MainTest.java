import ciserver.ContinuousIntegrationServer;
import org.eclipse.jetty.server.Server;
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
            Server server = ContinuousIntegrationServer.createServer(8080);
            assertEquals("STARTED", server.getState());
        } catch (Exception e) {
            fail("Exception in server creation: \n" + e);
        }



    }
}