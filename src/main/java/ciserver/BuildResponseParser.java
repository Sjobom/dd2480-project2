package ciserver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BuildResponseParser parses the build response from gradle
 * to determine whether the build succeeded or failed
 */
public class BuildResponseParser{

	/**
	 * Default constructor
	 */
    public BuildResponseParser() {}

	/**
	 * Parses a gradle build status message and determines whether
	 * the build succeeded or failed
	 * @param buildResponse		A gradle build response
	 * @return True if the build succeeded, else false
	 */
    public static boolean gradleBuildStatus(String buildResponse) {
        String success = gradleSuccessMsg();
        Pattern pSuccess = Pattern.compile(success);
        Matcher m = pSuccess.matcher(buildResponse);
        if(m.find()){
            return true;
        }
        return false;
    }

	/**
	 * @return Gradle's build successful message
	 */
    public static String gradleSuccessMsg() {
        return "BUILD SUCCESSFUL";
    }

	/**
	 * @return Gradle's build failed message
	 */
    public static String gradleFailMsg() {
        return "BUILD FAILED";
    }
}
