package ciserver;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.lang.StringBuilder;
import java.sql.Timestamp;
import java.util.Date;

/**
 * RepoHandler handles functionality related to
 * Git, such as cloning repositories and compiling
 * code.
 */
public class RepoHandler {

    /**
     * Clone the repository into temporary storage
	 * @param jsonObject	Webhook json payload
	 * @throws JSONException
     */
    public static void cloneRepository(JSONObject jsonObject) throws JSONException {
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

  
    /*
         Run gradle check on given path project folder
         @Return returns Gradle response msg from gradle check command
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
		
		String timestamp = new Timestamp(System.currentTimeMillis()).toString();

		try {
			CIHistory.storeBuild(buildStatus, buildID, contributors, timestamp, output);
		} catch (IOException e) { e.printStackTrace(); }
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
		
		String timestamp = new Timestamp(System.currentTimeMillis()).toString();

		try {
			CIHistory.storeBuild(buildStatus, buildID, contributors, timestamp, output);
		} catch (IOException e) { e.printStackTrace(); }
	}


    /**
     * Delete the temporarily cloned repository (cleanup)
	 * @param jsonObject	Webhook json payload
     */
    public static void deleteRepository(JSONObject jsonObject) {
        File dir = getRepoFilePath(jsonObject);
        if(!FileSystemUtils.deleteRecursively(dir)) {
            System.out.println("Problem occurred when deleting the temporarily cloned git repo's directory");
        }
    }

	/**
	 * Get the filepath to a given repository
	 * @param jsonObject	Webhook json payload
	 */
    public static File getRepoFilePath(JSONObject jsonObject) {
        String latest_commit_sha = jsonObject.getString("after");
        return new File(System.getProperty("user.dir") + "//temp-git//" + latest_commit_sha);
    }
}
