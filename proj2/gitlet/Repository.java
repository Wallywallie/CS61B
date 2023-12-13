package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static final File COMMIT_DIR = join(GITLET_DIR, "objects");

    public static File HEAD;

    public static final File REF_DIR = join(GITLET_DIR, "refs");

    public static final File REFHEADS_DIR = join(GITLET_DIR, "heads");


    /* TODO: fill in the rest of this class. */

    /* ------------These methods handle the "init" command --------------------------- */

    /* This method checks whether GITLET_DIR exists, if exists, print error message and abort */
    public static void checkfolder() {
      if (GITLET_DIR.exists()) {
          System.out.println("A Gitlet version-control system already exists in the current directory.");
          System.exit(0);
      }
    }


    /* This method
    * check failure case
    * create folder and file :  .gitlet/
    *                           .gitlet/HEAD
    *                           .gitlet/objects/
    *                           .gitlet/refs/heads/
    * creates an initial commit, when initializing a commit should:
    *           ->timeStamp
    *           ->log message
    *           ->sha1 code
    *           ->save information to system
    *
    *
    * */
    public static void initialization() {
        checkfolder();
        GITLET_DIR.mkdir();
        HEAD = new File(".gitlet/HEAD");
        writeContents(HEAD,"refs/heads/master"); //初始化HEAD文件
        COMMIT_DIR.mkdir(); //------> not check yet
        REF_DIR.mkdir();
        REFHEADS_DIR.mkdir();

        String MSG = "initial commit";
        Commit initialCommit = new Commit(MSG);


    }


}
