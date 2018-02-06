import ciserver.RepoHandler;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.util.stream.Collectors;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import static junit.framework.TestCase.fail;

public class GitTest {

    @Test
    public void testGitClone() {
        // Contract: Test if git cloning works

        // Create test data
        JSONObject jsonObject = getJsonTestData("git_webhook_baxterthehacker.json");
        String commit_sha = jsonObject.getString("after");

        // Test to clone the repository
        RepoHandler.cloneRepository(jsonObject);

        // assert that the README file exists in the directory created
        File readme = new File(System.getProperty("user.dir") + "//temp-git//" + commit_sha + "//README.md");
        assertTrue(readme.exists());
        assertTrue(!readme.isDirectory());

        // cleanup
        File dir = new File(System.getProperty("user.dir") + "//temp-git//" + commit_sha);
        if(!FileSystemUtils.deleteRecursively(dir)) {
            System.out.println("Problem occurred when deleting the directory");
        }
    }

    @Test
    public void testGitDelete() {
        // Contract: Test if a cloned git repository can be removed correctly

        // create test data
        JSONObject jsonObject = getJsonTestData("git_webhook_baxterthehacker.json");
        String commit_sha = jsonObject.getString("after");

        // clone a repository
        RepoHandler.cloneRepository(jsonObject);

        // assert that the README file exists in the directory created
        File readme = new File(System.getProperty("user.dir") + "//temp-git//" + commit_sha + "//README.md");
        assertTrue(readme.exists());
        assertTrue(!readme.isDirectory());

        // delete the repository
        RepoHandler.deleteRepository(jsonObject);

        // assert that the README file does not exist anymore
        assertFalse(readme.exists());

        // cleanup
        File dir = new File(System.getProperty("user.dir") + "//temp-git//" + commit_sha);
        if(!FileSystemUtils.deleteRecursively(dir)) {
            System.out.println("Problem occurred when deleting the directory");
        }

    }

    /**
     *  Helper method to create a json object from test data available
     */
    JSONObject getJsonTestData(String fileName) {
        StringBuilder jsonData = new StringBuilder();
        try {
            BufferedReader webhook_file = new BufferedReader(new FileReader(System.getProperty("user.dir") +
                    "//testData//" + fileName ));
            String line;
            while ((line = webhook_file.readLine()) != null){
                jsonData.append(line);
            }
            System.out.println(jsonData);


        } catch (FileNotFoundException e) {
            System.out.println("Could not find test data file!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject(jsonData.toString());
    }

}
