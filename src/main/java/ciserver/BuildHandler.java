package ciserver;

import org.eclipse.jetty.server.Request;
import org.json.JSONObject;

import java.io.File;

public class BuildHandler {
    /**
     * Perform all the continuous integration tasks
	 * @param baseRequest	Base request object
     */
    static void tryIntegration(Request baseRequest) {
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
        String checkResponse = runCheck(RepoHandler.getRepoFilePath(jsonObject));

        // 3rd generate build report
		RepoHandler.generateBuildReport(jsonObject, BuildResponseParser.gradleBuildStatus(checkResponse), checkResponse);

        // 4th delete repository
        RepoHandler.deleteRepository(jsonObject);
    }

    /**
     * Run gradle check on given path project folder
     * @param path path to the directory of execution
     * @return Gradle response msg from gradle check command
     */
    public static String runCheck(File path){
        //Create command
        String[] check_command = new String[2];
        check_command[0] = "gradle";
        check_command[1] = "check";

        return ShellCommand.exec(check_command, path);
    }
}
