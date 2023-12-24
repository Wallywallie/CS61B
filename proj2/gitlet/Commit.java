package gitlet;

// TODO: any imports you need here
import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;

import static gitlet.Repository.REFHEADS_DIR;
import static gitlet.Utils.*;

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
    public static String sha1code;

    /** The parent commit for current commit, store the parent's commit in the form of String, namely sha1 code*/
    public String parent;

    /** Store the head of current branch*/
    public static String head;

    public List<String> blobs;


    public Commit(String msg) {
        message = msg;
        timeStamp = new Date();


    }

    /** save commits in the name of sha1 code */
    public void saveCommit() {

        String filename = ".gitlet/objects/" + sha1code;
        File outfile = new File(filename);
        writeObject(outfile, this);



    }
    public void saveBranch() {

        File master = new File(".gitlet/refs/heads/master");
        writeContents(master, head);
    }

    public String generateSHA1() {
        sha1code = sha1(blobs);
        return sha1code;
    }


    @Override
    public void dump() {
        System.out.printf("size: %d%nmapping: %s%n", _size, _mapping);
    }

    int _size;
    TreeMap<String, String> _mapping = new TreeMap<>();
}
