import Backend.DepartmentManager;
import Backend.EmployeeManager;
import FileHandler.AccountHandler;
import FileHandler.DepartmentHandler;
import FileHandler.EmployeeHandler;
import GUI.MainFrame;


import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
            DepartmentHandler deptHandler = new DepartmentHandler();
            DepartmentManager deptManager = new DepartmentManager(deptHandler);

           
            EmployeeHandler empHandler = new EmployeeHandler(deptManager.getDepartments());
            EmployeeManager empManager = new EmployeeManager(empHandler);

           
            AccountHandler accountHandler = new AccountHandler();

            
            MainFrame mainFrame = new MainFrame(empManager, deptManager, accountHandler);
            mainFrame.setVisible(true);
        });
    }
}

