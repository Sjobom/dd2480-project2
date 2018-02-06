import org.eclipse.jetty.server.Server;
import ciserver.*;
import org.json.JSONObject;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

import static ciserver.BuildHandler.getContributor;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

    /**
     * Reads a JSON file and returns a JSON object from it
     * @param filename name of the file
     * @return a JSONObject made from the JSON-object in the file
     * @throws IOException
     */
    static JSONObject readJSON(String filename) throws IOException {
        // read the generated file
        File file = new File("testData/" + filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String contents = br.lines().collect(Collectors.joining("\n"));
        br.close();

        // convert string to jsonObject
        return new JSONObject(contents);
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
    public void testIsPullRequest(){
        //Contract: Test that isPullRequest-method returns true for valid pull request json and false for not pull request json
        try{
            JSONObject json_pr = readJSON("pr.json");
            JSONObject json_notPr = readJSON("not_pr.json");
            ;
            boolean pr_res = BuildHandler.isPullRequest(json_pr);
            boolean notPr_res = BuildHandler.isPullRequest(json_notPr);

            assertTrue(pr_res);
            assertFalse(notPr_res);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

	@Test
	public void testBuildList() {
		// Contract: Test that the server lists build urls properly
		try {
			File f = new File(System.getProperty("user.dir") + "//ci-history//test_build.html");
			f.createNewFile();
			String listing = LOCALHOST_GET_REQUEST(8080, "build");
			assertTrue(listing.contains(
				"<tr><td id=\"build\" style=\"font-weight: bold;\"><a href=\"/build/test_build\">test_build</a></td></tr>"
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

    @Test
    public void testGetContributor() {
        // Contract: the getContributor method should return the username and not actual name
        try {
            JSONObject obj = readJSON("get_contributor_oracle.json");
            String oracle = "remnestal";
            String actual = getContributor(obj);
            assertEquals(oracle, actual);
        } catch (IOException e) {
            fail("Exception occurred: " + e);
        }
    }
}
