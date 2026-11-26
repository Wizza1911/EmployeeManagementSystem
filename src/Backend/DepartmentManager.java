package Backend;

import FileHandler.FileOperator;
import java.util.ArrayList;

public class DepartmentManager {
    private ArrayList<Department> departments;
    private FileOperator<Department> departmentHandler;

    public DepartmentManager(FileOperator<Department> departmentHandler) {
        if (departmentHandler == null) {
            throw new IllegalArgumentException("DepartmentHandler cannot be null.");
        }
        this.departmentHandler = departmentHandler;
        this.departments = departmentHandler.load();
    }

    public ArrayList<Department> getDepartments() {
        return departments;
    }

    public void addDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null.");
        }
        for (Department d : departments) {
            if (d.getDepartmentId() == department.getDepartmentId()) {
                throw new IllegalArgumentException("Department ID already exists.");
            }
            if (d.getDepartmentName().trim().equalsIgnoreCase(department.getDepartmentName().trim())) {
                throw new IllegalArgumentException("Department name already exists.");
            }
        }

        departments.add(department);
        departmentHandler.save(department);
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
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        for (Department d : departments) {
            if (d.getDepartmentName().trim().equalsIgnoreCase(name.trim())) {
                return d;
            }
        }
        return null;
    }
    public ArrayList<Department> findDepartments(String name) {
        if (name == null || name.trim().isEmpty())
            return null;
        ArrayList<Department> deps = new ArrayList<>();
        for (Department d : departments)
            if (d.getDepartmentName().trim().equalsIgnoreCase(name.trim()))
                deps.add(d);
        return deps;
    }
}