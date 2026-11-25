package GUI;

import Backend.DepartmentManager;
import Backend.EmployeeManager;
import FileHandler.AccountHandler;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final EmployeeManager empManager;
    private final DepartmentManager deptManager;
    private final AccountHandler accountHandler;

    private JPanel currentCenterPanel;

    public MainFrame(EmployeeManager empManager, DepartmentManager deptManager, AccountHandler accountHandler) {
        this.empManager = empManager;
        this.deptManager = deptManager;
        this.accountHandler = accountHandler;

        setTitle("Employee Management System");
        setSize(950, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

    
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        
        switchCenterPanel(createPlaceholderPanel("Dashboard Overview"));
    }

    private void switchCenterPanel(JPanel newPanel) {
        if (currentCenterPanel != null) {
            remove(currentCenterPanel);
        }
        currentCenterPanel = newPanel;
        add(currentCenterPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBackground(Color.gray);
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel titleLabel = new JLabel("EMS Menu");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(titleLabel);
    

        JButton dashboardBtn = createNavButton("Dashboard");
        dashboardBtn.addActionListener(e ->
                switchCenterPanel(createPlaceholderPanel("Dashboard Overview"))
        );

        JButton addEmpBtn = createNavButton("Add Employee");
        addEmpBtn.addActionListener(e ->
                switchCenterPanel(createPlaceholderPanel("Add Employee Panel"))
        );

        JButton updateEmpBtn = createNavButton("Update Employee");
        updateEmpBtn.addActionListener(e ->
                switchCenterPanel(createPlaceholderPanel("Update Employee Panel"))
        );

        JButton viewEmpBtn = createNavButton("View Employees");
        viewEmpBtn.addActionListener(e ->
                switchCenterPanel(createPlaceholderPanel("View / Search Employees Panel"))
        );

        JButton deptBtn = createNavButton("Departments");
        deptBtn.addActionListener(e ->
                switchCenterPanel(createPlaceholderPanel("Department Management Panel"))
        );

        sidebar.add(dashboardBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(addEmpBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(updateEmpBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(viewEmpBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(deptBtn);

        sidebar.add(Box.createVerticalGlue()); //3ashann yenzel ta7t

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutBtn.addActionListener(e -> handleLogout());
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBackground(new Color(52, 58, 64));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return button;
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog( this, "Are you sure you want to logout?","Confirm Logout", JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private JPanel createPlaceholderPanel(String labelText) {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(label);
        return panel;
    }
}