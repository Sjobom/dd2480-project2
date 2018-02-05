package ciserver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.FileSystemUtils;


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
                this.tryIntegration(baseRequest);
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
    void tryIntegration(Request baseRequest) {
        /* get payload from HTTP request and create JSON object */
        String payload = baseRequest.getParameter("payload");
        if (payload == null){
            System.err.println("Error: there was no payload present in the HTTP Request!");
            return;
        }
        JSONObject jsonObject = new JSONObject(payload);
            // 1st clone the repository
            cloneRepository(jsonObject);
            // 2nd compile the code
            compileCode();
            // 3rd build the code
            runTests();
            // 4th delete repository
            deleteRepository(jsonObject);

    }

    /**
     * Clone the repository into temporary storage
     */
    public static void cloneRepository(JSONObject jsonObject) throws JSONException{
        // get branch name
        String ref = jsonObject.getString("ref");
        String[] ref_parts = ref.split("/");
        String branch = ref_parts[ref_parts.length - 1];

        // get ssh url
        String ssh_url = jsonObject.getJSONObject("repository").getString("ssh_url");

        // get latest commit sha
        String latest_commit_sha = jsonObject.getString("after");

        // info about action
        System.out.println("Cloning branch " + branch.toUpperCase() + " from the " + ssh_url.toUpperCase() + " repo");

        // create and execute the clone command
        String[] clone_command = new String[6];
        clone_command[0] = "git";
        clone_command[1] = "clone";
        clone_command[2] = "--branch";
        clone_command[3] = branch;
        clone_command[4] = ssh_url;
        clone_command[5] = latest_commit_sha; // the temp folders name
        File directory = new File(System.getProperty("user.dir") + "//temp-git");
        directory.mkdir(); // if directory is not present
        ShellCommand.exec(clone_command, directory);

    }

    /**
     * Run the compile-procedure for the repository
     */
    public static void compileCode() {

    }

    /**
     * Run the test-suite for the repository
     */
    public static void runTests() {

    }

    /**
     * Delete the temporarily cloned repository (cleanup)
     */
    public static void deleteRepository(JSONObject jsonObject) {
        String latest_commit_sha = jsonObject.getString("after");
        File dir = new File(System.getProperty("user.dir") + "//temp-git//" + latest_commit_sha);
        if(!FileSystemUtils.deleteRecursively(dir)) {
            System.out.println("Problem occurred when deleting the temporarily cloned git repo's directory");
        }
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
     * Makes a build history-file that is routable via the API
     * @param status the success status of the build
     * @param buildID id of the build, for example a commit-id
     * @param contributor who is the owner of the latest commit
     * @param timestamp the time of the CI-build
     * @param output any other information to be recorded
     * @throws IOException if the template is missing or file cannot be saved
     */
    public static void storeBuild(boolean status, String buildID, String contributor, String timestamp, String output) throws IOException {

        // read the template file
        String templatePath = "ci-history/template.html";
        BufferedReader br = new BufferedReader(new FileReader(templatePath));
        String template = br.lines().collect(Collectors.joining("\n"));

        // replace each tag with its corresponding data value
        template = template.replaceAll("\\{\\{\\s*id\\s*}}",            buildID);
        template = template.replaceAll("\\{\\{\\s*status\\s*}}",        status? "Success":"Failure");
        template = template.replaceAll("\\{\\{\\s*output\\s*}}",        output);
        template = template.replaceAll("\\{\\{\\s*timestamp\\s*}}",     timestamp);
        template = template.replaceAll("\\{\\{\\s*contributor\\s*}}",   contributor);

        // save the altered template as a separate file
        File buildPath = new File("ci-history/" + buildID + ".html");
        FileWriter fw = new FileWriter(buildPath);
        fw.write(template);
        fw.close();

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
        // server.join() has to be done after the object is returned if it is wanted
        return server;
    }

    public static void main(String[] args) throws Exception {
        Server server = createServer(8080);
        System.out.println("Server: " + server.getState());
        server.join();
    }
}
