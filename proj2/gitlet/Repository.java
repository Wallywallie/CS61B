package gitlet;

import java.io.File;
import java.util.HashMap;

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

    public static final File REFHEADS_DIR = join(REF_DIR, "heads");
    public static File master; //to record the head of current branch


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
        master = new File(".gitlet/refs/heads/master");

        //initial commit
        String MSG = "initial commit";
        Commit initialCommit = new Commit(MSG);
        initialCommit.saveCommit();
        initialCommit.writeInMaster(master);


        //initialize staging area
        Index index = new Index();
        index.saveIndex();

    }


    /* ------------These methods handle the "add" command --------------------------- */

    public static void add(String filename) {
        /* if the current working version of the file is identical to the version in the current commit,
        * do not stage it to be added,and remove it from the staging area if it is already there
        * */

        Index index;
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

        //create a file blob
        Blob blob = new Blob(fileToAdd);


        //check if the file is equal to current commit version
        //curr version->head
        //is equal->tracked file
        index = Index.fromFile();
        curr = Commit.getCurrCommit();
        if (curr.mapping.containsKey(blob.filename) && curr.mapping.get(blob.filename).equals(blob.sha1)) {
            if (index.isTracked(blob.filename)) {
                index.untrackFile(blob.filename);
            }
            return;
        }




        //write file name and its corresponding sha1 to index
        index.trackFile(filename, blob.sha1);
        //save file to repo in the name of sha1
        blob.saveBlob();
    }

    /* this method handles the commit command
    *
    * */
    public static void gitCommit(String msg) {
        Commit curr = new Commit(msg);
        curr.parent = Commit.HEAD;



        //record the staging area into commit;
        Index index = Index.fromFile();
        HashMap<String, String> mapping = index.getTrackedFile();
        if (mapping != null) {
            for (String key : mapping.keySet()) {
                curr.trackFile(key, mapping.get(key));
            }
        }

        curr.saveCommit();
        master = new File(".gitlet/refs/heads/master");//werid though...
        curr.writeInMaster(master);


    }


}
