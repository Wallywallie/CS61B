package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Formatter;

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
        writeContents(HEAD,"master"); //初始化HEAD文件

        COMMIT_DIR.mkdir(); //------> not check yet
        REF_DIR.mkdir();
        REFHEADS_DIR.mkdir();
        master = new File(".gitlet/refs/heads/master");

        //initial commit
        String MSG = "initial commit";
        Commit initialCommit = new Commit(MSG);
        initialCommit.saveCommit();

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
        index.saveIndex();

        //save file to repo in the name of sha1
        blob.saveBlob();
    }

    /* this method handles the commit command
    *
    * */
    public static void gitCommit(String msg) {

        //failure cases: no comment
        if (msg.isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }

        //failure cases: no file has been staged
        Index index = Index.fromFile();
        System.out.println(index.getTrackedFile().toString());
        TreeMap<String, String> mapping = index.getTrackedFile();
        TreeMap<String, String> removal = index.getRemovalFile();
        if (mapping == null) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }


        Commit curr = new Commit(msg);

        //record the parent's recordings into current commit
        Commit parentCommit = null;
        if (curr.parent != null) {
            // to find the exact commit using sha1 code of current commit
            parentCommit = Commit.fromFile( curr.parent);
        }
        if (parentCommit != null) {
            curr.trackFile(parentCommit.mapping);
        }

        //record the staging area into commit;
        curr.trackFile(mapping);
        curr.untrackFile(removal);

        curr.saveCommit();

        //clean the staging area
        Index newIndex = new Index();
        newIndex.saveIndex();


    }

    /* ------------These methods handle the "remove" command --------------------------- */
    public static void remove(String filename) {
        //failure case: if file is not staged or tracked by current commit.
        Commit curr = Commit.getCurrCommit();
        Index idx = Index.fromFile();
        boolean isStaged = idx.isTracked(filename);
        boolean isTracked = curr.isTracked(filename);
        if (!(isStaged || isTracked)) {
            System.out.println("No reason to remove the file.");
            return;
        }

        //delete the relevant blob
        String sha1 = curr.mapping.get(filename);
        File f = join(COMMIT_DIR, sha1.substring(0, 2));
        f = join(f, sha1.substring(2, sha1.length()));
        if (f.exists()) {
            f.delete();
        }

        if (isStaged) {
            //untrack
            idx.untrackFile(filename);
        }

        if (isTracked) {
            //stage for removal
            idx.trackFileToRemove(filename, sha1);

            //delete file in working directory
            File fInWorkingDir = join(CWD, filename);
            if (fInWorkingDir.exists()) {
                restrictedDelete(fInWorkingDir);
            }

        }
    }

    /* ------------These methods handle the "log" command --------------------------- */
    public static void log() {
        Commit curr = Commit.getCurrCommit();

        String currBranch = Commit.getCurrBranch();
        File f = join(REFHEADS_DIR, currBranch);
        String sha1 = null;
        if (f.exists()) {
            sha1 = readContentsAsString(f);
        }

        //TODO:Merge case
        printLog(curr, sha1);

    }

    /* ------------These methods handle the "global-log" command --------------------------- */
    public static void globalLog() {
        //iterate through the head dir and get a list of sha1 String
        List<String> lst = plainFilenamesIn(REFHEADS_DIR);
        if (lst != null) {
            for (String i : lst) {
                File f = join(REFHEADS_DIR, i);
                String sha1 = null;
                Commit curr = null;
                if (f.exists()) {
                    sha1 = readContentsAsString(f);
                    curr = Commit.fromFile(sha1);
                }
                printLog( curr,sha1);
            }
        }


    }

    private static void printLog(Commit curr, String sha1) {
        while (curr != null) {
            System.out.println("===");
            System.out.println("commit: " + sha1);
            System.out.println("Date: " + curr.printDate());
            System.out.println(curr.message);
            System.out.println();

            String parentCommit = curr.parent;
            curr = Commit.fromFile(parentCommit);
            sha1 = parentCommit;
        }

    }

}
