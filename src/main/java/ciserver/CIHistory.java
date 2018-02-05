package ciserver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

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
}
