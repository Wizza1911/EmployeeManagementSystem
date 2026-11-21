package FileHandler;

import java.util.ArrayList;

public interface FileOperator<T> {
        boolean save(T item);
        boolean saveAll(ArrayList<T> items);
        ArrayList<T> load();
}
