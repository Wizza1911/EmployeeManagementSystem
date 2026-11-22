package GUI;

import Backend.DepartmentManager;
import Backend.EmployeeManager;
import FileHandler.DepartmentHandler;
import FileHandler.EmployeeHandler;

public class Temp {
    public static void main(String[] args) {
        DepartmentManager deptManager = new DepartmentManager(new DepartmentHandler());
        EmployeeManager empManager = new EmployeeManager(new EmployeeHandler(deptManager.getDepartments()));
    }
}
