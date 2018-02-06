package ciserver;

import org.eclipse.jetty.server.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

public class BuildHandler {

    private static final String SERVER_HOSTNAME = "207.154.221.239:8080";
    private static final String BUILD_API = SERVER_HOSTNAME + "/build";

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

        // Break up the payload into JSON components
        JSONObject jsonObject = new JSONObject(payload);
        String lastCommit = jsonObject.getString("after");
        String buildURL = BUILD_API + "/" + lastCommit;

        // Set CI status to pending
        System.out.println("tryIntegration@" + lastCommit + ": setting PENDING status");
        try {
            StatusHandler.sendStatus(StatusHandler.PENDING, buildURL, lastCommit);
        } catch (IOException e) {
            System.err.println("Error setting PENDING status: " + e);
        }

        // Clone the repository
        System.out.println("tryIntegration@" + lastCommit + ": repo cloned");
        RepoHandler.cloneRepository(jsonObject);

        // Compile and run tests
        System.out.println("tryIntegration@" + lastCommit + ": running gradle");
        String gradleOutput = runCheck(RepoHandler.getRepoFilePath(jsonObject));
        boolean buildStatus = BuildResponseParser.gradleBuildStatus(gradleOutput);

        // Prepare build-info parameters
        System.out.println("tryIntegration@" + lastCommit + ": preparing log parameters");
        String contributor = null;
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        JSONArray commits = jsonObject.getJSONArray("commits");
        for (int i = 0; i < commits.length(); i++) {
            // Loop through the commits until the contributor of the last commit is found
            if (commits.getJSONObject(i).getString("id").equals(lastCommit)) {
                contributor = commits.getJSONObject(i)
                        .getJSONObject("author")
                        .getString("name");
            }
        }

        // Generate build report
        System.out.println("tryIntegration@" + lastCommit + ": generating build log");
        try {
            CIHistory.storeBuild(buildStatus, lastCommit, contributor, timestamp, gradleOutput);
        } catch (IOException e) {
            System.err.println("Error storing build-file: " + e);
        }

        // Set CI status to either success/failure
        System.out.println("tryIntegration@" + lastCommit + ": setting final status");
        try {
            if (buildStatus) {
                StatusHandler.sendStatus(StatusHandler.SUCCESS, buildURL, lastCommit);
            } else {
                StatusHandler.sendStatus(StatusHandler.FAILURE, buildURL, lastCommit);
            }
        } catch (IOException e) {
            System.err.println("Error setting SUCCESS/FAILURE status: " + e);
        }

        // Delete repository
        System.out.println("tryIntegration@" + lastCommit + ": removing repository");
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