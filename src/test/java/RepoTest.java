import ciserver.BuildResponseParser;
import ciserver.RepoHandler;

import ciserver.ShellCommand;
import org.junit.Test;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
    Test functions related to doing something on an existing repository
 */
public class RepoTest {
    @Test
    public void testRunCheck() {
        //Contract: Test that runCheck runs and returns a respsonse string
        File project = new File(System.getProperty("user.dir")+"//temp-git//");
        String[] clone = new String[3];
        clone[0] = "git";
        clone[1] = "clone";
        clone[2] = "https://github.com/gradle/gradle-build-scan-quickstart.git";
        ShellCommand.exec(clone, project);
        project = new File(System.getProperty("user.dir")+"//temp-git//gradle-build-scan-quickstart");
        String gradleResponse = RepoHandler.runCheck(project);

        Pattern pSuccess = Pattern.compile(BuildResponseParser.gradleSuccessMsg());
        Pattern pFail = Pattern.compile(BuildResponseParser.gradleFailMsg());
        Matcher successMatcher = pSuccess.matcher(gradleResponse);
        Matcher failMatcher = pFail.matcher(gradleResponse);

        boolean validGradleResponse = false;

        if(successMatcher.find() || failMatcher.find()){
            validGradleResponse = true;
        }

        System.out.println(gradleResponse);

        // assert that the response string contains something
        assertTrue(validGradleResponse);

        // cleanup
        if(!FileSystemUtils.deleteRecursively(project)) {
            System.out.println("Problem occurred when deleting the directory");
        }
    }
}
