package ciserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
/*
    Executes given command from current runtime environment
    However relatives paths etc need to be taken in consideration in the input commands at the moment,
    prob add functions here to make that easier.
 */
public class ShellCommand {
    public ShellCommand(){

    }
    /*
    Executes command given as input string
    Return the String response from executed command
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