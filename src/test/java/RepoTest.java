import ciserver.RepoHandler;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
    Test functions related to doing something on an existing repository
 */
public class RepoTest {
    @Test
    public void testRunCheck() {
        //Contract: Test that runCheck runs and returns a respsonse string
        File project = new File(System.getProperty("user.dir")+"//temp-git//dummy_project");

        String gradleResponse = RepoHandler.runCheck(project);
        // assert that the response string contains something
        assertFalse(gradleResponse.isEmpty());
    }
}
