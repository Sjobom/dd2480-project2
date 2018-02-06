import org.eclipse.jetty.server.Server;
import ciserver.*;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainTest {


    /*
     * Helper methods
     */

    /**
     * Simulates a GET-request to the server
     * @return the response from the server
     */
    static String LOCALHOST_GET_REQUEST(int port, String target) throws Exception {

        // setup local server and send the GET request to it
        Server server = (ContinuousIntegrationServer.createServer(port)); // possible flakiness here from startup time?
        URL localhost = new URL("http://localhost:" + port + "/" + target);
        URLConnection connection = localhost.openConnection();

        // convert the resulting inputstream to a string
        InputStreamReader isr = new InputStreamReader(connection.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String response = br.lines().collect(Collectors.joining("\n"));

        // stop the local server and wait until it has finished
        server.stop();
        server.join();

        return response;

    }

    /*
     * Testcases
     */

    @Test
    public void testLocalhostGETRequest () {
        // Contract: the LOCALHOST_GET_REQUEST method to the /status api shall return the expected string
        try {
            String response = LOCALHOST_GET_REQUEST(8080, "status");
            assertEquals("CI server is up & running!", response);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception: " + e);
        }
    }

    @Test
    public void testServerCreation(){
        // Contract: Test that the createServer-method start the server
        try {
            Server server = ContinuousIntegrationServer.createServer(8080);
            assertEquals("STARTED", server.getState());
            server.stop();
            server.join();
        } catch (Exception e) {
            fail("Exception in server creation: \n" + e);
        }
    }

	@Test
	public void testBuildList() {
		// Contract: Test that the server lists build urls properly
		try {
			File f = new File(System.getProperty("user.dir") + "//ci-history//test_build.html");
			f.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write("2018-02-06 16:33:26.259");
			String listing = LOCALHOST_GET_REQUEST(8080, "build");
			assertTrue(listing.contains(
				"<tr><td id=\"build\" style=\"font-weight: bold;\">link</td>"
			));
			f.delete();
		} catch (Exception e) {
			fail(e.toString());
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
