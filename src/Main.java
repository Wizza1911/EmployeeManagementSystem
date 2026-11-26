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


            AccountHandler accountHandler = new AccountHandler();

            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}

