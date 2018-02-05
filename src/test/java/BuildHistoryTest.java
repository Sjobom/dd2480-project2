import ciserver.CIHistory;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.stream.Collectors;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.fail;

public class BuildHistoryTest {

    @Test
    public void testStoreBuild() {
        // Contract: test that the storeBuild method stores the correct information
        try {
            // set up constants
            final boolean status =      false;
            final String commitID =     "f099a9ea1cfd49eea5f9b969779317d1f06e3f21";
            final String contributor =  "albinre";
            final String timestamp =    new Timestamp(System.currentTimeMillis()).toString();
            final String output =       "BUILD FAILED in 12s";

            // run the method
            CIHistory.storeBuild(
                    status,
                    commitID,
                    contributor,
                    timestamp,
                    output);

            // compile the oracle string representing the file contents
            String oracle = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>CI Build</title>\n" +
                    "    <link href=\"https://fonts.googleapis.com/css?family=Roboto\" rel=\"stylesheet\">\n" +
                    "    <style media=\"screen\">\n" +
                    "    body {\n" +
                    "        font-family: 'Roboto', sans-serif;\n" +
                    "        margin-left: 25px;\n" +
                    "    }\n" +
                    "    th, td {\n" +
                    "        text-align: left;\n" +
                    "    }\n" +
                    "    th {\n" +
                    "        padding-right: 5vw;\n" +
                    "    }\n" +
                    "    /* source: https://www.w3schools.com/howto/howto_js_collapsible.asp */\n" +
                    "    /* Style the button that is used to open and close the collapsible content */\n" +
                    "    #collapsible {\n" +
                    "        background-color: #eee;\n" +
                    "        color: #444;\n" +
                    "        cursor: pointer;\n" +
                    "        padding: 18px;\n" +
                    "        border: none;\n" +
                    "        text-align: left;\n" +
                    "        outline: none;\n" +
                    "        font-size: 15px;\n" +
                    "    }\n" +
                    "\n" +
                    "    /* source: https://www.w3schools.com/howto/howto_js_collapsible.asp */\n" +
                    "    /* Add a background color to the button if it is clicked on (add the .active class with JS), and when you move the mouse over it (hover) */\n" +
                    "    .active, .collapsible:hover {\n" +
                    "        background-color: #ccc;\n" +
                    "    }\n" +
                    "\n" +
                    "    /* source: https://www.w3schools.com/howto/howto_js_collapsible.asp */\n" +
                    "    /* Style the collapsible content. Note: hidden by default */\n" +
                    "    .content {\n" +
                    "        padding: 0 18px;\n" +
                    "        display: none;\n" +
                    "        overflow: hidden;\n" +
                    "        background-color: #f1f1f1;\n" +
                    "    }\n" +
                    "    </style>\n" +
                    "    <script type=\"text/javascript\">\n" +
                    "    window.onload = function () {\n" +
                    "        /* source: https://www.w3schools.com/howto/howto_js_collapsible.asp */\n" +
                    "        var coll = document.getElementById('collapsible');\n" +
                    "        coll.addEventListener(\"click\", function() {\n" +
                    "            this.classList.toggle(\"active\");\n" +
                    "            var content = this.nextElementSibling;\n" +
                    "            if (content.style.display === \"block\") {\n" +
                    "                content.style.display = \"none\";\n" +
                    "            } else {\n" +
                    "                content.style.display = \"block\";\n" +
                    "            }\n" +
                    "        })\n" +
                    "        // change color of success/failure\n" +
                    "        var status = document.getElementById('status');\n" +
                    "        if (status.innerHTML.trim() === \"Success\") {\n" +
                    "            status.style.color = \"green\";\n" +
                    "        } else if (status.innerHTML.trim() === \"Failure\") {\n" +
                    "            status.style.color = \"red\";\n" +
                    "        } else {\n" +
                    "            status.style.color = \"#DCDCDC\";\n" +
                    "        }\n" +
                    "    }\n" +
                    "</script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>Build history</h1>\n" +
                    "<table>\n" +
                    "    <!-- build status -->\n" +
                    "    <tr>\n" +
                    "        <th>Status</th>\n" +
                    "        <td id=\"status\" style=\"font-weight: bold;\">\n" +
                    "            Failure\n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "    <!-- blank row -->\n" +
                    "    <tr>\n" +
                    "        <td>\n" +
                    "            <br/>\n" +
                    "        </td>\n" +
                    "        <td>\n" +
                    "            <br/>\n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "    <!-- Additional details -->\n" +
                    "    <tr>\n" +
                    "        <th>Commit</th>\n" +
                    "        <td>\n" +
                    "            <a href=\"https://github.com/Sjobom/dd2480-project2/commit/f099a9ea1cfd49eea5f9b969779317d1f06e3f21\">\n" +
                    "                " + commitID + "\n" +
                    "            </a>\n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <th>Contributor</th>\n" +
                    "        <td>\n" +
                    "            <a href=\"https://github.com/Sjobom/dd2480-project2/commits?author=Sjobom\">\n" +
                    "                " + contributor + "\n" +
                    "            </a>\n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <th>Timestamp</th>\n" +
                    "        <td>" + timestamp + "</td>\n" +
                    "    </tr>\n" +
                    "</table>\n" +
                    "<!-- bulk output -->\n" +
                    "<!-- <h2 >Output</h2> -->\n" +
                    "<button id=\"collapsible\" style=\"margin-top: 5vh;\">Show output</button>\n" +
                    "<div class=\"content\">\n" +
                    "    <p>\n" +
                    "        " + output + "\n" +
                    "    </p>\n" +
                    "</div>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";

            // read the generated file
            File file = new File("ci-history/" + commitID + ".html");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String contents = br.lines().collect(Collectors.joining("\n"));

            // make the assertion
            assertEquals(oracle, contents);

            // remove the file when testing is complete
            file.delete();

        } catch (IOException e) {
            fail("IOException occurred: " + e);
        }
    }

    @Test
    public void testRequestingBuild() throws Exception {
        //contract: tests that a build can be retrieved from CI server by http regeuest for wanted commitID
        // as given string
        try {
            // set up constants
            final boolean status =      false;
            final String commitID =     "f099a9ea1cfd49eea5f9b969779317d1f06e3f21";
            final String contributor =  "albinre";
            final String timestamp =    new Timestamp(System.currentTimeMillis()).toString();
            final String output =       "BUILD FAILED in 12s";

            // run the method
            CIHistory.storeBuild(
                    status,
                    commitID,
                    contributor,
                    timestamp,
                    output);

            // compile the oracle string representing the file contents
            String oracle = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>CI Build</title>\n" +
                    "    <link href=\"https://fonts.googleapis.com/css?family=Roboto\" rel=\"stylesheet\">\n" +
                    "    <style media=\"screen\">\n" +
                    "    body {\n" +
                    "        font-family: 'Roboto', sans-serif;\n" +
                    "        margin-left: 25px;\n" +
                    "    }\n" +
                    "    th, td {\n" +
                    "        text-align: left;\n" +
                    "    }\n" +
                    "    th {\n" +
                    "        padding-right: 5vw;\n" +
                    "    }\n" +
                    "    /* source: https://www.w3schools.com/howto/howto_js_collapsible.asp */\n" +
                    "    /* Style the button that is used to open and close the collapsible content */\n" +
                    "    #collapsible {\n" +
                    "        background-color: #eee;\n" +
                    "        color: #444;\n" +
                    "        cursor: pointer;\n" +
                    "        padding: 18px;\n" +
                    "        border: none;\n" +
                    "        text-align: left;\n" +
                    "        outline: none;\n" +
                    "        font-size: 15px;\n" +
                    "    }\n" +
                    "\n" +
                    "    /* source: https://www.w3schools.com/howto/howto_js_collapsible.asp */\n" +
                    "    /* Add a background color to the button if it is clicked on (add the .active class with JS), and when you move the mouse over it (hover) */\n" +
                    "    .active, .collapsible:hover {\n" +
                    "        background-color: #ccc;\n" +
                    "    }\n" +
                    "\n" +
                    "    /* source: https://www.w3schools.com/howto/howto_js_collapsible.asp */\n" +
                    "    /* Style the collapsible content. Note: hidden by default */\n" +
                    "    .content {\n" +
                    "        padding: 0 18px;\n" +
                    "        display: none;\n" +
                    "        overflow: hidden;\n" +
                    "        background-color: #f1f1f1;\n" +
                    "    }\n" +
                    "    </style>\n" +
                    "    <script type=\"text/javascript\">\n" +
                    "    window.onload = function () {\n" +
                    "        /* source: https://www.w3schools.com/howto/howto_js_collapsible.asp */\n" +
                    "        var coll = document.getElementById('collapsible');\n" +
                    "        coll.addEventListener(\"click\", function() {\n" +
                    "            this.classList.toggle(\"active\");\n" +
                    "            var content = this.nextElementSibling;\n" +
                    "            if (content.style.display === \"block\") {\n" +
                    "                content.style.display = \"none\";\n" +
                    "            } else {\n" +
                    "                content.style.display = \"block\";\n" +
                    "            }\n" +
                    "        })\n" +
                    "        // change color of success/failure\n" +
                    "        var status = document.getElementById('status');\n" +
                    "        if (status.innerHTML.trim() === \"Success\") {\n" +
                    "            status.style.color = \"green\";\n" +
                    "        } else if (status.innerHTML.trim() === \"Failure\") {\n" +
                    "            status.style.color = \"red\";\n" +
                    "        } else {\n" +
                    "            status.style.color = \"#DCDCDC\";\n" +
                    "        }\n" +
                    "    }\n" +
                    "</script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>Build history</h1>\n" +
                    "<table>\n" +
                    "    <!-- build status -->\n" +
                    "    <tr>\n" +
                    "        <th>Status</th>\n" +
                    "        <td id=\"status\" style=\"font-weight: bold;\">\n" +
                    "            Failure\n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "    <!-- blank row -->\n" +
                    "    <tr>\n" +
                    "        <td>\n" +
                    "            <br/>\n" +
                    "        </td>\n" +
                    "        <td>\n" +
                    "            <br/>\n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "    <!-- Additional details -->\n" +
                    "    <tr>\n" +
                    "        <th>Commit</th>\n" +
                    "        <td>\n" +
                    "            <a href=\"https://github.com/Sjobom/dd2480-project2/commit/f099a9ea1cfd49eea5f9b969779317d1f06e3f21\">\n" +
                    "                " + commitID + "\n" +
                    "            </a>\n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <th>Contributor</th>\n" +
                    "        <td>\n" +
                    "            <a href=\"https://github.com/Sjobom/dd2480-project2/commits?author=Sjobom\">\n" +
                    "                " + contributor + "\n" +
                    "            </a>\n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <th>Timestamp</th>\n" +
                    "        <td>" + timestamp + "</td>\n" +
                    "    </tr>\n" +
                    "</table>\n" +
                    "<!-- bulk output -->\n" +
                    "<!-- <h2 >Output</h2> -->\n" +
                    "<button id=\"collapsible\" style=\"margin-top: 5vh;\">Show output</button>\n" +
                    "<div class=\"content\">\n" +
                    "    <p>\n" +
                    "        " + output + "\n" +
                    "    </p>\n" +
                    "</div>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";

            // read the generated file
            File file = new File("ci-history/" + commitID + ".html");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String contents = br.lines().collect(Collectors.joining("\n"));

            //test getting the build history by get request
            String reguestedBuildHTML = MainTest.LOCALHOST_GET_REQUEST(8080, "build/" + commitID);

            //ignore spaces and tabs and new lines
            oracle = oracle.replaceAll("\\s", "");
            reguestedBuildHTML = reguestedBuildHTML.replaceAll("\\s", "");

            assertEquals(oracle, reguestedBuildHTML);

            file.delete();

        } catch (IOException e) {
            fail("IOException occurred: " + e);
        }

    }

}
