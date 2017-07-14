package tmp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by coral on 2017/7/9.
 */
public class WriteTxt {
    public static void write(String data, String path) {
        try {
            File file = new File(path);
            //if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            //true --> append file
            System.out.println("write file to paths:  " + file.getAbsolutePath());
            FileWriter fileWritter = new FileWriter(file.getPath(), true);
            fileWritter.write(data);
            //***
            fileWritter.flush();
            fileWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
