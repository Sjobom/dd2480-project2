import ciserver.ContinuousIntegrationServer;
import org.json.JSONObject;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertTrue;

public class GitTest {
    @Test
    public void testGitClone() {
        // Contract: Test if git cloning works

        // Create test data
        JSONObject jsonObject = new JSONObject();
        try {
            BufferedReader webhook_file = new BufferedReader(new FileReader(System.getProperty("user.dir") +
                    "//testData//git_webhook_baxterthehacker.json" ));
            String line;
            StringBuilder jsonData = new StringBuilder();
            while ((line = webhook_file.readLine()) != null){
                jsonData.append(line);
            }
            System.out.println(jsonData);
            jsonObject = new JSONObject(jsonData.toString());
        } catch (FileNotFoundException e) {
            System.out.println("Could not find test data file!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test to clone the repository
        ContinuousIntegrationServer.cloneRepository(jsonObject);

        // assert that the README file exists in the directory created
        File readme = new File(System.getProperty("user.dir") + "//temp-git//0d1a26e67d8f5eaf1f6ba5c57fc3c7d91ac0fd1c//README.md");
        assertTrue(readme.exists());
        assertTrue(!readme.isDirectory());

        // cleanup
        File dir = new File(System.getProperty("user.dir") + "//temp-git//0d1a26e67d8f5eaf1f6ba5c57fc3c7d91ac0fd1c");
        dir.delete();
    }

}
