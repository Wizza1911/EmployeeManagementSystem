package Backend;

import java.util.ArrayList;

public class EmployeeManager {
    private ArrayList<Employee> employees=new ArrayList<Employee>();
    private FileHandler fileHandler;
    public EmployeeManager(FileHandler fileHandler)
    {
        employees=fileHandler.loadEmployees();
        
    }
}
