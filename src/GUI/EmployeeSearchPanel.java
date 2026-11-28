package GUI;

import Backend.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSearchPanel extends JPanel {

    private final EmployeeManager employeeManager;
    private final DepartmentManager departmentManager;

    private final DefaultTableModel tableModel;
    private final JTable employeeTable;

    private final JComboBox<String> searchTypeCombo;
    private final JPanel dynamicControlPanel;
    private final JTextField searchTextField;
    private final JComboBox<String> departmentCombo;
    private final JComboBox<String> employeeTypeCombo;
    private final JButton searchButton;

    public EmployeeSearchPanel(EmployeeManager employeeManager, DepartmentManager departmentManager) {
        if (employeeManager == null || departmentManager == null) {
            throw new IllegalArgumentException("Managers cannot be null.");
        }
        this.employeeManager = employeeManager;
        this.departmentManager = departmentManager;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBackground(Color.WHITE);

        JLabel searchByLabel = new JLabel("Search by:");
        searchByLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        String[] searchOptions = {
                "-- Select --",
                "Employee Name",
                "Department",
                "Employee Type",
                "Age",
                "Position",
                "ID"
        };
        searchTypeCombo = new JComboBox<>(searchOptions);
        searchTypeCombo.setBackground(Color.WHITE);

        dynamicControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        dynamicControlPanel.setBackground(Color.WHITE);

        searchTextField = new JTextField(16);

        departmentCombo = new JComboBox<>();
        departmentCombo.setBackground(Color.WHITE);
        // Display only departmentName instead of Department.toString() ("id,name")
        loadDepartmentsIntoCombo();

        employeeTypeCombo = new JComboBox<>(new String[]{"Full-Time", "Part-Time"});
        employeeTypeCombo.setBackground(Color.WHITE);

        searchButton = new JButton("Search");

        topPanel.add(searchByLabel);
        topPanel.add(searchTypeCombo);
        topPanel.add(dynamicControlPanel);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {
                "ID", "Full Name", "Department", "Age", "Position",
                "Type", "Bonus", "Hourly Rate", "Worked Hours", "Salary"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(25);
        employeeTable.setBackground(Color.WHITE);
        employeeTable.getTableHeader().setReorderingAllowed(false);

        employeeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(70);   // ID
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Full Name
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(160);  // Department
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(70);   // Age
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(200);  // Position
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(100);  // Type
        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(110);  // Bonus
        employeeTable.getColumnModel().getColumn(7).setPreferredWidth(110);  // Hourly Rate
        employeeTable.getColumnModel().getColumn(8).setPreferredWidth(110);  // Worked Hours
        employeeTable.getColumnModel().getColumn(9).setPreferredWidth(120);  // Total Salary

        JScrollPane scrollPane = new JScrollPane(
                employeeTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);

        populateTable(employeeManager.getAllEmployees());

        searchTypeCombo.addActionListener(e -> updateSearchControls());
        searchButton.addActionListener(e -> performSearch());
        searchTextField.addActionListener(e -> performSearch());
    }

    private void loadDepartmentsIntoCombo() {
        departmentCombo.removeAllItems();
        java.util.Set<String> seenNames = new java.util.HashSet<>();
        departmentCombo.addItem("-- Select --");
        for (Department dept : departmentManager.getDepartments()) {
            String name = dept.getDepartmentName().trim();
            if (seenNames.add(name.toLowerCase())) {
                departmentCombo.addItem(name);
            }
        }
    }

    public void refreshData() {
        loadDepartmentsIntoCombo();
        searchTypeCombo.setSelectedIndex(0);
        populateTable(employeeManager.getAllEmployees());
    }

    private void updateSearchControls() {
        String selectedType = (String) searchTypeCombo.getSelectedItem();
        dynamicControlPanel.removeAll();
        searchTextField.setText("");

        if (selectedType == null || selectedType.equals("-- Select --")) {
            populateTable(employeeManager.getAllEmployees());
        } else if (selectedType.equals("Department")) {
            loadDepartmentsIntoCombo();
            dynamicControlPanel.add(departmentCombo);
            dynamicControlPanel.add(searchButton);
        }
        else if (selectedType.equals("Employee Type")) {
            dynamicControlPanel.add(employeeTypeCombo);
            dynamicControlPanel.add(searchButton);
        }
        else {
            dynamicControlPanel.add(searchTextField);
            dynamicControlPanel.add(searchButton);
        }

        dynamicControlPanel.revalidate();
        dynamicControlPanel.repaint();
    }

    private void performSearch() {
        String selectedType = (String) searchTypeCombo.getSelectedItem();
        if (selectedType == null || selectedType.equals("-- Select --"))
            return;

        List<Employee> results = new ArrayList<>();

        switch (selectedType) {
            case "Employee Name": {
                String query = searchTextField.getText().trim().toLowerCase();
                for (Employee emp : employeeManager.getAllEmployees()) {
                    if (emp.getFullName().toLowerCase().contains(query)) {
                        results.add(emp);
                    }
                }
                break;
            }
            case "Department": {
                String selectedDeptName = (String) departmentCombo.getSelectedItem();
                if (selectedDeptName == null ||  selectedDeptName.equals("-- Select --")  )
                    results = employeeManager.getAllEmployees();
                else
                    results = employeeManager.filterByDepartment(selectedDeptName);
                break;
            }
            case "Employee Type": {
                String selectedEmpType = (String) employeeTypeCombo.getSelectedItem();
                boolean wantFullTime = "Full-Time".equals(selectedEmpType);
                for (Employee emp : employeeManager.getAllEmployees()) {
                    if (emp.isFullTime() == wantFullTime) {
                        results.add(emp);
                    }
                }
                break;
            }
            case "Age": {
                String text = searchTextField.getText().trim();
                if (text.isEmpty()) {
                    results = employeeManager.getAllEmployees();
                } else if (isValidPositiveInt(text)) {
                    int targetAge = Integer.parseInt(text);
                    for (Employee emp : employeeManager.getAllEmployees()) {
                        if (emp.getAge() == targetAge) {
                            results.add(emp);
                        }
                    }
                } else {
                    showInvalidNumberWarning(selectedType);
                    return;
                }
                break;
            }
            case "Position": {
                String query = searchTextField.getText().trim().toLowerCase();
                for (Employee emp : employeeManager.getAllEmployees()) {
                    if (emp.getPosition().toLowerCase().contains(query)) {
                        results.add(emp);
                    }
                }
                break;
            }
            case "ID": {
                String text = searchTextField.getText().trim();
                if (text.isEmpty())
                    results = employeeManager.getAllEmployees();
                else if (isValidPositiveInt(text)) {
                    int targetId = Integer.parseInt(text);
                    for(Employee emp : employeeManager.getAllEmployees()) {
                        if (emp.getId() == targetId)
                            results.add(emp);
                        }
                    }
                else {
                    showInvalidNumberWarning(selectedType);
                    return;
                }
                break;
            }
        }
        populateTable(results);
    }

    private boolean isValidPositiveInt(String text) {
        return text.matches("[1-9]\\d{0,8}");
    }

    private void showInvalidNumberWarning(String fieldName) {
        JOptionPane.showMessageDialog(
                this,
                "Please enter a valid positive whole number for " + fieldName + ".",
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void populateTable(List<Employee> employees) {
        tableModel.setRowCount(0);
        for (Employee emp : employees) {
            String type;
            String bonus = "-";
            String hourlyRate = "-";
            String workedHours = "-";
            String salary = "-";

            if (emp.isFullTime()) {
                type = "Full-Time";
                FullTimeEmployee ft = (FullTimeEmployee) emp;
                bonus = String.format("%.2f", ft.getBonus());
                salary= String.format("%.2f",ft.getBasicSalary());
            } else {
                type = "Part-Time";
                PartTimeEmployee pt = (PartTimeEmployee) emp;
                hourlyRate = String.format("%.2f", pt.getHourlyRate());
                workedHours = String.valueOf(pt.getHoursWorked());
            }

            tableModel.addRow(new Object[]{
                    emp.getId(),
                    emp.getFullName(),
                    emp.getDepartment().getDepartmentName(),
                    emp.getAge(),
                    emp.getPosition(),
                    type,
                    bonus,
                    hourlyRate,
                    workedHours,
                    salary
            });
        }
    }
}