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
        for (Department d : departments)
            if (d.getDepartmentId() == department.getDepartmentId())
                throw new IllegalArgumentException("Department ID already exists.");

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
    public Department findDepartment(int id, String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        String targetName = name.trim();
        for (Department d : departments) {
            if (d.getDepartmentId() == id && d.getDepartmentName().trim().equalsIgnoreCase(targetName)) {
                return d;
            }
        }
        return null;
    }

    public void updateDepartment(int originalId, String originalName, Department updatedDept) {
        if (updatedDept == null) {
            throw new IllegalArgumentException("Updated department cannot be null.");
        }

        Department existing = findDepartment(originalId, originalName);
        if (existing == null) {
            throw new IllegalArgumentException(
                    "Department \"" + originalName + "\" with ID " + originalId + " not found."
            );
        }

        boolean isSameKey = (updatedDept.getDepartmentId() == originalId)
                && updatedDept.getDepartmentName().trim().equalsIgnoreCase(originalName.trim());

        if (!isSameKey && findDepartment(updatedDept.getDepartmentId(), updatedDept.getDepartmentName()) != null) {
            throw new IllegalArgumentException(
                    "Department:" + updatedDept.getDepartmentName() + ", with ID "
                            + updatedDept.getDepartmentId() + " already exists."
            );
        }
        existing.setDepartmentName(updatedDept.getDepartmentName());
        existing.setDepartmentId(updatedDept.getDepartmentId());
        departmentHandler.saveAll(departments);
    }

    public void deleteDepartment(int id, String name) {
        Department target = findDepartment(id, name);
        if (target == null) {
            throw new IllegalArgumentException(
                    "Department:" + name + ", with ID " + id + " not found."
            );
        }
        if (target.getCount() != 0) {
            throw new IllegalStateException(
                    "Cannot delete department:" + name + " (ID: " + id + ") because it still has members."
            );
        }
        departments.remove(target);
        departmentHandler.saveAll(departments);
    }
}