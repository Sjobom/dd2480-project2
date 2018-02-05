package ciserver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Class that parses a Build Response String from gradle
    to determine:
    Build Success
    Build Fail
*/
public class BuildResponseParser{
    public BuildResponseParser(){

    }

    /*
        returns True if status is successful
        returns False if status is failed (assumed if not successful)
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

    public static String gradleSuccessMsg(){
        return "BUILD SUCCESSFUL";
    }

    public static String gradleFailMsg(){
        return "BUILD FAILED";
    }
}