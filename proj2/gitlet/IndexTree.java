package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;

public class IndexTree implements Serializable {


    private TreeMap<String, String> mapping;

    public IndexTree() {

        mapping = new TreeMap<>();
    }

    public void trackFile(String name, String sha1) {
        mapping.put(name, sha1);
    }

    public void saveIndex() {
        String filename = ".gitlet/index";
        File outfile = new File(filename);
        Utils.writeObject(outfile, this);
    }

    public static IndexTree fromFile() {
        File[] files = Repository.GITLET_DIR.listFiles();
        IndexTree returnTree;
        for (File f : files) {
            if (f.isFile() && f.getName().equals("index")) {
                returnTree = Utils.readObject(f, IndexTree.class);
                return returnTree;
            }
        }
        return null;
    }
}
