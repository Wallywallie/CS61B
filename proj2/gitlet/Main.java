package gitlet;
import static gitlet.Repository.*;
import static gitlet.Utils.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }

        String firstArg = args[0];

        switch(firstArg) {
            case "init":
                initialization();
                break;
            case "add":
                String filename = args[1];
                add(filename);
                break;

            case "commit":
                String msg = args[1];//---->unchecked
                gitCommit(msg);
                break;

            case "rm":
                String fname = args[1];
                remove(fname);
                break;
            case "log":
                log();
                break;
            case "global-log":
                globalLog();
                break;

        }


    }
}
