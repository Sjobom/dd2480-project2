package ciserver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.lang.StringBuilder;
import java.util.ArrayList;

/**
 * CIHistory deals with problems related to cataloging and fetching
 * build information.
 */
public class CIHistory {

    /**
     * Get information about a previous build
     * @param buildID the ID of the previous build
     * @return HTML-file with build-information or null if no such build
     */
    public static String fetchBuild(String buildID) {
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
	 * Generates a list of all previous builds
	 * @return ArrayList with all previous build-ids
	 */
	public static Map<String, String> getBuildList() {
		File dir = new File(System.getProperty("user.dir")+"//ci-history//.");
		File[] dirList = dir.listFiles();
        Map<String, String> prevBuilds = new TreeMap<String, String>();

		if (dirList != null) {
			for (File build : dirList) {
				if (!build.getName().equals("template.html") &&
					!build.getName().equals("listing.html") && !build.getName().equals("test_build.html")) {

				    try{
				        //read contents of build file
                        BufferedReader br = new BufferedReader(new FileReader(build));
                        String contents = br.lines().collect(Collectors.joining("\n"));

                        //regex for timestamps given by new Timestamp(System.currentTimeMillis()).toString();
                        String timeRegex = "[0-9]+-[0-9]+-[0-9]+ [0-9]+:[0-9]+:[0-9]+.[0-9]+";
                        Pattern p = Pattern.compile(timeRegex);
                        Matcher m = p.matcher(contents);

                        //Find timestamp in file
                        m.find();
                        String timeStamp = m.group();
                        String prevBuildName = build.getName().replaceAll(".html","");

                        //(k = buildName,v = timeStamp)
                        prevBuilds.put(prevBuildName,timeStamp);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

			}
		}
		return prevBuilds;
	}

	/**
	 * Generates the HTML listing of all previous
	 * builds
	 * @return HTML-formatted list of builds
	 */
	public static String createBuildListing() throws IOException {
        // read the template file
        String templatePath = "ci-history/listing.html";
        BufferedReader br = new BufferedReader(new FileReader(templatePath));
        String template = br.lines().collect(Collectors.joining("\n"));

		String format = "<tr><td id=\"build\" style=\"font-weight: bold;\"><a href=\"/build/%1$s\">%1$s</a></td>" +
                "<td style=\"font-weight: bold;\">%2$</td></tr>\r\n";
		Map<String, String> prevBuilds = getBuildList();
		StringBuilder table = new StringBuilder();
		
		for (String build : prevBuilds.values()) {
			table.append(String.format(format, build, prevBuilds.get(build)));
		}

		return template.replaceAll("\\{\\{\\s*listing\\s*\\}\\}", table.toString());
	}
}
