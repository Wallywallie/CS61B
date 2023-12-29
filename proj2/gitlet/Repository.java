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
    *           ->save information to system
    * creates staging area
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

        //initial commit
        String MSG = "initial commit";
        Commit initialCommit = new Commit(MSG);
        initialCommit.saveCommit();

        //initialize staging area
        IndexTree index = new IndexTree();
        index.saveIndex();

    }

    public static void add(String filename) {
        /* if the current working version of the file is identical to the version in the current commit,
        * do not stage it to be added,and remove it from the staging area if it is already there
        * */

        IndexTree index;
        File fileToAdd = null;
        Commit curr;
        //find file to be added
        File[] files = CWD.listFiles();
        for (File file : files) {
            if (file.isFile() && filename.equals(file.getName())) {
                fileToAdd = file;
            }
        }
        //check if the file exists
        if (fileToAdd == null) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        //check if the file is equal to current commit version
        //curr version->head
        //is equal->tracked file
        index = IndexTree.fromFile();
        curr = Commit.getCurrCommit();


        //save file to repo in the name of sha1
        Blob blob = new Blob(fileToAdd);
        blob.saveBlob();

        //write file name and its corresponding sha1 to index

        index.trackFile(filename, blob.sha1);


    }

    /* this method handles the commit command
    *
    * */
    public static void gitCommit(String msg) {
        Commit curr = new Commit(msg);
        curr.parent = Commit.head;
        //handle files ->save tracked files ->save staging area

        //find the staging area and save it to repo;
        File[] files = GITLET_DIR.listFiles();
        File fileToSave = null;
        for (File f : files) {
            if (f.isFile() && f.getName().equals("index")) {
                fileToSave = f;
            }
        }
        if (fileToSave == null) {
            System.out.println("Staging Area not Found");
            System.exit(0);
        }
        String sha1 = Utils.sha1(fileToSave);
        String filename = ".gitlet/objects/" + sha1;

    }


}
