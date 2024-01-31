package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeMap;

public class Index implements Serializable {


    private HashMap<String, String> mapping; //using mapping to record which file to be tracked

    public Index() {

        mapping = new HashMap<>();
    }

    public void trackFile(String filename, String sha1) {
        mapping.put(filename, sha1);
    }
    public void untrackFile(String filename) {
        if (mapping.containsKey(filename)) {
            mapping.remove(filename);
        }
    }

    public boolean isTracked(String filename) {
        return mapping.containsKey(filename);
    }
    public HashMap<String, String> getTrackedFile () {
        return mapping;
    }


    public void saveIndex() {
        String filename = ".gitlet/index";
        File outfile = new File(filename);
        Utils.writeObject(outfile, this);
    }

    public static Index fromFile() {
        File[] files = Repository.GITLET_DIR.listFiles();
        Index returnIndex;
        for (File f : files) {
            if (f.isFile() && f.getName().equals("index")) {
                returnIndex = Utils.readObject(f, Index.class);
                return returnIndex;
            }
        }
        return null;
    }
}
