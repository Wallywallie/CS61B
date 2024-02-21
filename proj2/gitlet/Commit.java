package gitlet;

// TODO: any imports you need here
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import static gitlet.Repository.REFHEADS_DIR;
import static gitlet.Utils.*;
import static gitlet.Utils.join;

import java.util.Date; // TODO: You'll likely use this in this class

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable, Dumpable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;


    /** The date of the commit */
    private Date timeStamp;

    /** The sha1 code for current commit */
    public String sha1code;

    /** The parent commit for current commit, store the parent's commit in the form of String, namely sha1 code*/
    public String parent;

    /** Store the head of current branch*/
    public static String HEAD;
    /* tracked files
    * should map the sha1 code of current commit to the file
    * each file has a sha1 code
    * */
    public List<String> blobs;

    //filename ->its sha1 code
    public TreeMap<String, String> mapping;

    private class CommitTree {

        String cur;
        CommitTree prev;
        CommitTree(String c ,CommitTree p) {
            cur = c;
            prev = p;
        }
        CommitTree(String c) {
            cur = c;
            prev = null;
        }

    }

    public CommitTree t;


    public Commit(String msg) {
        message = msg;
        timeStamp = new Date();
        mapping = new TreeMap<>();
        File masterfile = join(REFHEADS_DIR, "master");
        if (masterfile.isFile()) {
            parent = readContentsAsString(masterfile);
        } else {
            parent = "null";
        }


    }

    /** save commits in the name of sha1 code */
    public void saveCommit() {
        File outFile = new File(".gitlet/Commit");
        Utils.writeObject(outFile, this);
        File[] files = Repository.GITLET_DIR.listFiles();
        for (File f : files) {
            if (f.isFile() && f.getName().equals("Commit")) {
                sha1code = Utils.sha1(readContents(f));
                System.out.println(sha1code);
                String foldername = sha1code.substring(0,2);
                File folder = join(Repository.COMMIT_DIR, foldername);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                String newFilename = ".gitlet/objects/" + foldername + "/"+ sha1code.substring(2,sha1code.length());
                System.out.println(newFilename);
                File newFile = new File(newFilename);
                f.renameTo(newFile);
            }
        }

        //write current sha1 into branch, namely master
        HEAD = sha1code; // idea:this HEAD is not written in the commit file
        File masterfile = join(REFHEADS_DIR, "master");
        writeContents(masterfile, sha1code);

    }


    public void trackFile(TreeMap<String, String> mapping) {
        if (mapping != null) {
            for (String key : mapping.keySet()) {
                mapping.put(key,  mapping.get(key));
            }
        }

    }
    public static Commit getCurrCommit() {
        Commit curr = null;
        String branch = Commit.getCurrBranch();
        //Here for simplify ignore other branches --->to be solved
        File[] files = REFHEADS_DIR.listFiles();
        String sha1forCurrCommit;
        for (File f : files) {
            if (f.isFile() && f.getName().equals("master")) {
                sha1forCurrCommit = readContentsAsString(f);
                curr = fromFile(sha1forCurrCommit);
            }
        }
        return curr;
    }

    public static String getCurrBranch() {
        File[] files = Repository.GITLET_DIR.listFiles();
        File HEAD = null;
        for (File f : files) {
            if (f.isFile() && f.getName().equals("HEAD")) {
                HEAD = f;
            }
        }
        String branch = readContentsAsString(HEAD);
        return branch;
    }
    public static Commit fromFile(String sha1) {
        String foldername = sha1.substring(0, 2);
        File searchingDir = join(Repository.COMMIT_DIR, foldername);
        File[] files = searchingDir.listFiles();
        Commit commit = null;
        for (File f : files) {
            if (f.isFile() && f.getName().equals(sha1.substring(2, sha1.length()))) {
                commit = readObject(f, Commit.class);
            }
        }

        return commit;
    }


    public void saveBranch() {

        File master = new File(".gitlet/refs/heads/master");
        writeContents(master, HEAD);
    }

    public void generateSHA1() {
        sha1code = sha1(blobs);
    }


    @Override
    public void dump() {
        System.out.printf("size: %d%nmapping: %s%n", _size, mapping);
    }

    int _size;

}
