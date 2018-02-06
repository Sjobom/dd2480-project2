import ciserver.ContinuousIntegrationServer;
import ciserver.StatusHandler;
import jdk.net.SocketFlow;
import org.apache.http.HttpEntity;
import org.json.JSONObject;
import org.junit.Test;

import java.io.*;
import java.sql.Timestamp;
import java.util.stream.Collectors;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.fail;

public class StatusTest {

    @Test
    public void testSendStatusPending() {
        // Contract: setting the commit status to pending yields the expected result on GitHub.com
        try {
            HttpEntity entity = StatusHandler.sendStatus(
                    StatusHandler.PENDING,
                    "https://www.google.com",
                    "88d00c42e1bb0efbc6b89ecf57118e05c02fb9f0"
            );
            BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
            String response = br.lines().collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(response);

            String oracle = "pending";
            String actual = json.getString("state");
            assertEquals(oracle, actual);

        } catch (IOException e) {
            fail("Exception occurred: " + e);
        }
    }
    @Test
    public void testSendStatusSuccess() {
        // Contract: setting the commit status to success yields the expected result on GitHub.com
        try {
            HttpEntity entity = StatusHandler.sendStatus(
                    StatusHandler.SUCCESS,
                    "https://www.google.com",
                    "88d00c42e1bb0efbc6b89ecf57118e05c02fb9f0"
            );
            BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
            String response = br.lines().collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(response);

            String oracle = "success";
            String actual = json.getString("state");
            assertEquals(oracle, actual);

        } catch (IOException e) {
            fail("Exception occurred: " + e);
        }
    }

    @Test
    public void testSendStatusFailure() {
        // Contract: setting the commit status to failure yields the expected result on GitHub.com
        try {
            HttpEntity entity = StatusHandler.sendStatus(
                    StatusHandler.FAILURE,
                    "https://www.google.com",
                    "88d00c42e1bb0efbc6b89ecf57118e05c02fb9f0"
            );
            BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
            String response = br.lines().collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(response);

            String oracle = "failure";
            String actual = json.getString("state");
            assertEquals(oracle, actual);

        } catch (IOException e) {
            fail("Exception occurred: " + e);
        }
    }

    @Test
    public void testSendStatusError() {
        // Contract: setting the commit status to error yields the expected result on GitHub.com
        try {
            HttpEntity entity = StatusHandler.sendStatus(
                    StatusHandler.ERROR,
                    "https://www.google.com",
                    "88d00c42e1bb0efbc6b89ecf57118e05c02fb9f0"
            );
            BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
            String response = br.lines().collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(response);

            String oracle = "error";
            String actual = json.getString("state");
            assertEquals(oracle, actual);

        } catch (IOException e) {
            fail("Exception occurred: " + e);
        }
    }

}
