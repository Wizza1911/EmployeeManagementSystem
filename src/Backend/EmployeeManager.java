package Backend;
import FileHandler.*;
import java.util.ArrayList;

public class EmployeeManager {
    private ArrayList<Employee> employees;
    private FileOperator<Employee> employeeHandler;   

    public EmployeeManager(FileOperator<Employee> employeeHandler) {
        if (employeeHandler == null) {
            throw new IllegalArgumentException("EmployeeHandler cannot be null.");
        }
       this.employeeHandler = employeeHandler;
       this.employees = employeeHandler.load();
    }

   public void addEmployee(Employee emp) {
        if (emp == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        if (searchById(emp.getId()) != null) {
            throw new IllegalArgumentException("Employee ID " + emp.getId() + " already exists.");
        }

        employees.add(emp);
        employeeHandler.save(emp);
    }

   public void updateEmployee(int originalId, Employee updatedEmp) {
        if (updatedEmp == null) {
            throw new IllegalArgumentException("Updated employee cannot be null.");
        }

        Employee existing = searchById(originalId);
        if (existing == null) {
            throw new IllegalArgumentException("Employee with ID " + originalId + " not found.");
        }

        if (updatedEmp.getId() != originalId && searchById(updatedEmp.getId()) != null) {
            throw new IllegalArgumentException("Employee ID " + updatedEmp.getId() + " already exists.");
        }

        int targetIndex = employees.indexOf(existing);
        employees.set(targetIndex, updatedEmp);
        employeeHandler.saveAll(employees);
    }

    public void deleteEmployee(int id) {
        Employee target = searchById(id);
        if (target == null) {
            throw new IllegalArgumentException("Employee with ID " + id + " not found.");
        }

        employees.remove(target);
        employeeHandler.saveAll(employees);
    }

   public Employee searchById(int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }

    public ArrayList<Employee> filterByDepartment(String departmentName) {
        if (departmentName == null || departmentName.trim().isEmpty()) {
            return employees;
        }

        ArrayList<Employee> filtered = new ArrayList<>();
        String targetDept = departmentName.trim();
        for (Employee emp : employees) {
            if (emp.getDepartment().getDepartmentName().equalsIgnoreCase(targetDept)) {
                filtered.add(emp);
            }
        }
        return filtered;
    }
    public ArrayList<Employee> getAllEmployees() {
        return employees;
    }

    
}