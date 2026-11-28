package FileHandler;

import java.io.File;
import java.util.ArrayList;

public interface FileOperator<T> {
        boolean save(T item);
        boolean saveAll(ArrayList<T> items);
        ArrayList<T> load();
        static void ensureDirectoryExists(String file) {
                File parentDir = new File(file).getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                        parentDir.mkdirs();
                }
        }
}
