package ciserver;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Class for setting CI/build status labels using GitHubs API
 *
 * Source for Apache.http implementation:
 * http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html#d5e37
 * https://stackoverflow.com/questions/3324717/sending-http-post-request-in-java
 */
public class StatusHandler {

    private static final String GITHUB_USER = "repoClonerDD2480Project2";
    private static final String ACCESS_TOKEN = "946bae834398b494ad545a163a9ccd5a6def1ff1";
    private static final String GITHUB_API = "https://api.github.com/repos/Sjobom/dd2480-project2/statuses";

    public static final String PENDING = "pending";
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String ERROR = "error";

    private static final String STATUS_CONTEXT = "group5.ci";

    public static HttpEntity sendStatus(String state, String target_url, String sha) throws IOException {

        // Setup a basic HttpClient and prepare a HttpPost object
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(GITHUB_API + "/" + sha);
        httppost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        // Setup the authorization header using the github-bot's username and access token
        String auth = GITHUB_USER + ":" + ACCESS_TOKEN;
        String auth_base64 = new String(Base64.encodeBase64(auth.getBytes()));
        httppost.setHeader("Authorization", "Basic " + auth_base64);

        // Prepare the json parameters as a list of KV-pairs
        List<NameValuePair> KVPairs = new ArrayList<NameValuePair>();
        KVPairs.add(new BasicNameValuePair("state", state));
        KVPairs.add(new BasicNameValuePair("target_url", target_url));
        KVPairs.add(new BasicNameValuePair("description", state));
        KVPairs.add(new BasicNameValuePair("context", STATUS_CONTEXT));

        // Compile the json-parameters to a string-entity
        StringJoiner joiner = new StringJoiner(", ");
        for (NameValuePair kv: KVPairs) {
            joiner.add("\"" + kv.getName() + "\":\"" + kv.getValue() + "\"");
        }
        StringEntity params = new StringEntity("{" + joiner.toString() + "}");
        httppost.setEntity(params);

        // Execute and retrieve the response.
        HttpResponse response = httpclient.execute(httppost);
        return response.getEntity();
    }

}
