package list_duplicating_files;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap ;
import java.util.Iterator;
import java.util.Set;

class UniqueContentFinder {

    private HashMap <String, String> uniqueFiles  = new HashMap <String, String>();

    private static String md5OfFile(String filename) throws Exception {
        InputStream fis =  new FileInputStream(filename);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        byte[] md5Digest = complete.digest();
        String result = "";
        for (int i=0; i < md5Digest.length; i++) {
            result += Integer.toString( ( md5Digest[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    public void findUniqueFilesIn(String path) {
        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                findUniqueFilesIn(f.getAbsolutePath());
            }
            else {
                try {
                    String fileName = f.getAbsoluteFile().toString();
                    String md5HEX = md5OfFile(fileName);
                    //System.out.println("File:" + f.getAbsoluteFile() + ": " + md5HEX);
                    this.uniqueFiles.put(md5HEX, fileName);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showUniqueFiles() {
        Set set = uniqueFiles.keySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            String md5HEX = (String) i.next();
            System.out.println(uniqueFiles.get(md5HEX));
        }
    }
}

public class List_Duplicating_Files {

    public static void main(String[] args) {
        UniqueContentFinder finder = new UniqueContentFinder();
        finder.findUniqueFilesIn("D:\\test");
        finder.showUniqueFiles();
    }
    
}
