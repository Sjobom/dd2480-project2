package ciserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * ShellCommand handles the execution of a given command in
 * the current runtime environment.
 */
public class ShellCommand {
	/**
	 * Default constructor
	 */
    public ShellCommand() {}

	/**
	 * Executes the given command
	 * @param command	The command to be executed
	 * @return Output from the command
	 */
    public static String exec(String command) {
        StringBuffer response = new StringBuffer();

        Process p;
        try{
            p = Runtime.getRuntime().exec(command);
            //wait for process to exec
            p.waitFor();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while((line = r.readLine()) != null) {
                response.append(line + "\n");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }

	/**
	 * Executes the given command
	 * @param command	An array of command tokens
	 * @param directory	The working directory the command
	 *					will be executed under
	 * @return Output from the command
	 */
    public static String exec(String[] command, File directory) {
        StringBuffer response = new StringBuffer();

        Process p;
        try{
            p = Runtime.getRuntime().exec(command, null, directory);
            //wait for process to exec
            p.waitFor();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while((line = r.readLine()) != null) {
                response.append(line + "\n");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}
