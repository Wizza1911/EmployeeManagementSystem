package Backend;
import java.util.ArrayList;
public class DepartmentManager {
private ArrayList<Department>  departments = new ArrayList<Department>();
private FileHandler fileHandler;

public DepartmentManager(){
   fileHandler = new FileHandler();
   departments = fileHandler.loadDepartments();
}

public ArrayList<Department> getDepartments() {
    return departments;
}

public void addDepartments(Department department) {
    if(department==null){
        throw new IllegalArgumentException("can not find this department");
    }
    for(Department d:departments){
        if(d.getDepartmentId()==department.getDepartmentId()){
            throw new IllegalArgumentException("Department ID already exists.");
        }
        if(d.getDepartmentName().trim().equalsIgnoreCase(department.getDepartmentName().trim())){
            throw new IllegalArgumentException("Department name already exists.");
        }
        departments.add(department);
        fileHandler.saveDepartments(departments);
    }
    
}
public Department findDepartmentById(int id) {

    for (Department d : departments) {
        if (d.getDepartmentId() == id) {
            return d;
        }
    }

    return null;
}


public Department findDepartmentByName(String name) {

    for (Department d : departments) {
        if (d.getDepartmentName().equalsIgnoreCase(name.trim())) {
            return d;
        }
    }

    return null;
}
}