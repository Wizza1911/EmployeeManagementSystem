package Backend;

import java.util.ArrayList;

public class EmployeeManager {
    private ArrayList<Employee> employees=new ArrayList<Employee>();
    private FileHandler fileHandler;
    public EmployeeManager(FileHandler fileHandler)
    {
        if (fileHandler == null) {
            throw new IllegalArgumentException("FileHandler cannot be null.");
        }
        this.fileHandler = fileHandler;
        this.employees = fileHandler.loadEmployees();

    }

     public Employee searchById(int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }






   public void addEmployee(Employee emp) {
        if (emp == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        if (searchById(emp.getId()) != null) {
            throw new IllegalArgumentException("Employee ID " + emp.getId() + " already exists.");
        }

        employees.add(emp);
        fileHandler.saveEmployees(employees);
    }


    public void updateEmployee(int originalId, Employee updatedEmp) {
        if (updatedEmp == null) {
            throw new IllegalArgumentException("Updated employee cannot be null.");
        }

        int targetIndex = -1;
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == originalId) {
                targetIndex = i;
                break;
            }
        }

        if (targetIndex == -1) {
            throw new IllegalArgumentException("Employee with ID " + originalId + " not found.");
        }

        if (updatedEmp.getId() != originalId && searchById(updatedEmp.getId()) != null) {
            throw new IllegalArgumentException("Employee ID " + updatedEmp.getId() + " already exists.");
        }

        employees.set(targetIndex, updatedEmp);
        fileHandler.saveEmployees(employees);
    }

    public void deleteEmployee(int id) {
        Employee target = searchById(id);
        if (target == null) {
            throw new IllegalArgumentException("Employee with ID " + id + " not found.");
        }

        employees.remove(target);
        fileHandler.saveEmployees(employees);
    }
}
