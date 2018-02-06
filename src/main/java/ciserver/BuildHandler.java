package ciserver;

import org.eclipse.jetty.server.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

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
		generateBuildReport(jsonObject, BuildResponseParser.gradleBuildStatus(checkResponse), checkResponse);

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

    /**
	 * Generates a html build report file and places it
	 * in the ci-history directory
	 * @param jsonObject	Webhook json payload
	 * @param buildStatus	Build status, true if successful, else false
	 * @param output		Extra output message
	 */
	public static void generateBuildReport(JSONObject jsonObject, boolean buildStatus, String output) {
		// fetch build id (sha hash of head)
		String buildID = jsonObject.getString("after");

		// fetch contributors by iterating over commits
		JSONArray commits = jsonObject.getJSONArray("commits");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < commits.length(); i++) {
			sb.append(commits.getJSONObject(i)
						.getJSONObject("author")
						.getString("name"));
			if (i < commits.length()-1) sb.append(", ");
		}
		String contributors = sb.toString();

		String timestamp = jsonObject.getJSONObject("repository").getString("updated_at");

		try {
			CIHistory.storeBuild(buildStatus, buildID, contributors, timestamp, output);
		} catch (IOException e) { e.printStackTrace(); }
	}
}
