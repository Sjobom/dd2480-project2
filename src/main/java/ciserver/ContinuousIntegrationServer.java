package ciserver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONObject;


/**
 * ContinuousIntegrationServer acts as a webhook and webserver.
 * It is built on the Jetty webserver.
 * @see Jetty
 */
public class ContinuousIntegrationServer extends AbstractHandler
{

	/**
	 * The handle method is the primary request handler.
	 * All incoming requests go through here and are subsequently
	 * routed to different parts of the application.
	 *
	 * @param target		Target URI
	 * @param baseRequest	Base request object
	 * @param request		Request object
	 * @param response		Response object
	 * @throws IOException,ServletException
	 * @see	Jetty
	 */
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
                this.tryIntegration(baseRequest);
                this.setResponse200(response, "webhook");
                break;
            case "/build":
				if (m.group(2) != null) {
					// List specific build
					String buildHistory = CIHistory.fetchBuild(m.group(2));
					if (buildHistory != null)
						this.setResponse200(response, buildHistory);
					else
						this.setResponse404(response);
				} else {
					// List all previous builds
					this.setResponse200(response, CIHistory.createBuildListing());
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
	 * @param baseRequest	Base request object
     */
    void tryIntegration(Request baseRequest) {
        /* get payload from HTTP request and create JSON object */
        String payload = baseRequest.getParameter("payload");
        if (payload == null){
            System.err.println("Error: there was no payload present in the HTTP Request!");
            return;
        }
        JSONObject jsonObject = new JSONObject(payload);
        // 1st clone the repository
        RepoHandler.cloneRepository(jsonObject);

        //2nd compile and run tests
        String checkResponse = RepoHandler.runCheck(RepoHandler.getRepoFilePath(jsonObject));
            
			  // 3rd generate build report
			  // @TODO: pass build status and message instead of dummy values
			  RepoHandler.generateBuildReport(jsonObject, true, "TODO");
        // 4th delete repository
        RepoHandler.deleteRepository(jsonObject);
    }

    /**
     * Set the response status to 200 OK and add any additional payload
	 * @param response	Response object
	 * @param payload	Response message
	 * @throws IOException
     */
    void setResponse200(HttpServletResponse response, String payload) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        if (payload != null) {
            response.getWriter().println(payload);
        }
    }

    /**
     * Set the response status to 404 Not Found
	 * @param response	Response object
	 * @throws IOException
     */
    void setResponse404(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

	/**
	 * Start the CI server from the command line
	 * @param port	Server port
	 * @throws Exception
	 */
    public static Server createServer(int port) throws Exception
    {
        Server server = new Server(port);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        // server.join() has to be done after the object is returned if it is wanted
        return server;
    }

    public static void main(String[] args) throws Exception {
        Server server = createServer(8080);
        System.out.println("Server: " + server.getState());
        server.join();
    }
}
