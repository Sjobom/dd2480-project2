package ciserver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;


/**
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler
{
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        // Use regex to create a 'middleware' mechanism for routing within the server
        Pattern r = Pattern.compile("^(\\/\\w*)(\\/.*)?");
        Matcher m = r.matcher(target);
        String route = m.find() ? m.group(1) : null;

        // choose action with regard to target route
        switch(route) {
            case "/webhook":
                this.tryIntegration();
                this.setResponse200(response, "webhook");
                break;
            case "/build":
                String buildHistory = this.fetchBuild(m.group(2));
                // if the requested build is not found, return 404 Not Found
                if (buildHistory != null) {
                    this.setResponse200(response, buildHistory);
                } else {
                    this.setResponse404(response);
                }
                break;
            case "/status":
                this.setResponse200(response, "CI server is up & running!");
                break;
            default:
                this.setResponse404(response);
        }
        baseRequest.setHandled(true);
    }

    /**
     * Perform all the continuous integration tasks
     */
    void tryIntegration() {
        // 1st clone the repository
        this.cloneRepository();
        // 2nd compile the code
        this.compileCode();
        // 3rd build the code
        this.runTests();
    }

    /**
     * Clone the repository into temporary storage
     */
    void cloneRepository() {

    }

    /**
     * Run the compile-procedure for the repository
     */
    void compileCode() {

    }

    /**
     * Run the test-suite for the repository
     */
    void runTests() {

    }

    /**
     * Get information about a previous build
     * @param buildID the ID of the previous build
     * @return HTML-file with build-information or null if no such build
     */
    String fetchBuild(String buildID) {
        String buildPath = "ci-history/" + buildID + ".html";
        if (Files.exists(Paths.get(buildPath))) {
            StringBuilder buildHistory = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new FileReader(buildPath))){
                String s;
                while ((s = in.readLine()) != null) {
                    buildHistory.append(s);
                    buildHistory.append("\n");
                }
            } catch (IOException e) {
                System.err.println("There was an error reading from '" + buildPath + "'");
                return null;
            }
            return buildHistory.toString();
        } else {
            return null;
        }
    }

    /**
     * Set the response status to 200 OK and add any additional payload
     */
    void setResponse200(HttpServletResponse response, String payload) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        if (payload != null) {
            response.getWriter().println(payload);
        }
    }

    /**
     * set the response status to 404 Not Found
     */
    void setResponse404(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    // start the CI server in command line
    public static Server createServer(int port) throws Exception
    {
        Server server = new Server(port);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        //server.join();
        return server;
    }

    public static void main(String[] args) throws Exception {
        Server server = createServer(8080);
        System.out.println("Server: " + server.getState());
    }
}
