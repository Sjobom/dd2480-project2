import ciserver.ContinuousIntegrationServer;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.util.FileSystemUtils;

import java.io.*;

import static org.junit.Assert.assertTrue;

public class GitTest {
    @Test
    public void testGitClone() {
        // Contract: Test if git cloning works

        // Create test data
        JSONObject jsonObject = getJsonTestData("git_webhook_baxterthehacker.json");

        // Test to clone the repository
        ContinuousIntegrationServer.cloneRepository(jsonObject);

        // assert that the README file exists in the directory created
        File readme = new File(System.getProperty("user.dir") + "//temp-git//0d1a26e67d8f5eaf1f6ba5c57fc3c7d91ac0fd1c//README.md");
        assertTrue(readme.exists());
        assertTrue(!readme.isDirectory());

        // cleanup
        File dir = new File(System.getProperty("user.dir") + "//temp-git//0d1a26e67d8f5eaf1f6ba5c57fc3c7d91ac0fd1c");
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
