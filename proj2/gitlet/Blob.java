package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    private File file;

    public String sha1;

    public Blob(File f) {
        file = f;
    }

    public void saveBlob() {
        String filename = ".gitlet/Blob";
        File outFile = new File(filename);
        Utils.writeObject(outFile, this);
        File[] files = Repository.GITLET_DIR.listFiles();
        for (File f : files) {
            if (f.isFile() && f.getName().equals("Blob")) {
                sha1 = Utils.sha1(f);
                String foldername = sha1.substring(0,2);
                String newFilename = ".gitlet/objects/" + foldername + "/"+ sha1.substring(2,sha1.length());
                File newFile = new File(newFilename);
                f.renameTo(newFile);
            }
        }






    }

}
