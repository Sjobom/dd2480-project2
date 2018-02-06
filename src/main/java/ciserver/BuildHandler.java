package ciserver;

import org.eclipse.jetty.server.Request;
import org.json.JSONObject;

import java.io.File;

import static ciserver.RepoHandler.runCheck;

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
}
