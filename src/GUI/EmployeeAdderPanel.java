package GUI;

import Backend.*;
import Helper.ValidationHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class EmployeeAdderPanel extends JPanel {
    private final EmployeeManager employeeManager;
    private final DepartmentManager departmentManager;

    private final JTextField idField = new JTextField(20);
    private final JTextField nameField = new JTextField(20);
    private final JTextField ageField = new JTextField(20);
    private final JTextField positionField = new JTextField(20);
    private final JTextField salaryField = new JTextField(20);
    private final JTextField bonusField = new JTextField(20);
    private final JTextField hourlyRateField = new JTextField(20);
    private final JTextField workedHoursField = new JTextField(20);

    private final JLabel salaryLabel = new JLabel("Salary:");
    private final JLabel bonusLabel = new JLabel("Bonus:");
    private final JLabel hourlyRateLabel = new JLabel("Hourly Rate:");
    private final JLabel workedHoursLabel = new JLabel("Worked Hours:");
    private final JComboBox<String> departmentNameCombo;
    private final JComboBox<Integer> departmentIdCombo;
    private final JComboBox<String> employeeTypeCombo;
    private final JButton addButton;
    private final JPanel dynamicControlPanel;

    public EmployeeAdderPanel(EmployeeManager employeeManager, DepartmentManager departmentManager) {
        if (employeeManager == null || departmentManager == null) {
            throw new IllegalArgumentException("Managers cannot be null.");
        }
        this.employeeManager = employeeManager;
        this.departmentManager = departmentManager;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //up until this point most stuff is needed from previous
        Font labelFont = new Font("SansSerif", Font.BOLD, 13);

        JLabel employeeNameLabel = new JLabel("Name:");
        employeeNameLabel.setFont(labelFont);
        JLabel departmentNameLabel = new JLabel("Department Name:");
        departmentNameLabel.setFont(labelFont);
        JLabel departmentIdLabel = new JLabel("Department ID:");
        departmentIdLabel.setFont(labelFont);
        JLabel employeeTypeLabel = new JLabel("Type:");
        employeeTypeLabel.setFont(labelFont);
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(labelFont);
        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setFont(labelFont);
        JLabel iDLabel = new JLabel("ID:");
        iDLabel.setFont(labelFont);
        workedHoursLabel.setFont(labelFont);
        departmentNameCombo = new JComboBox<>();
        departmentIdCombo = new JComboBox<>();
        departmentNameCombo.setBackground(Color.WHITE);
        departmentIdCombo.setBackground(Color.WHITE);
        loadDepartmentsIntoCombo();
        getDepIds();

        employeeTypeCombo = new JComboBox<>(new String[]{"-- Select --","Full-Time", "Part-Time"});
        employeeTypeCombo.setBackground(Color.WHITE);

        addButton = new JButton("Add");

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        salaryLabel.setFont(labelFont);
        bonusLabel.setFont(labelFont);
        hourlyRateLabel.setFont(labelFont);
        workedHoursLabel.setFont(labelFont);

        addRow(topPanel, gbc, 0, employeeTypeLabel, employeeTypeCombo);
        addRow(topPanel, gbc, 1, iDLabel, idField);
        addRow(topPanel, gbc, 2, employeeNameLabel, nameField);
        addRow(topPanel, gbc, 3, ageLabel, ageField);
        addRow(topPanel, gbc, 4, departmentNameLabel, departmentNameCombo);
        addRow(topPanel, gbc, 5, departmentIdLabel, departmentIdCombo);
        addRow(topPanel, gbc, 6, positionLabel, positionField);

        dynamicControlPanel = new JPanel(new GridBagLayout());
        dynamicControlPanel.setBackground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(4, 0, 0, 0);
        topPanel.add(dynamicControlPanel, gbc);
        add(topPanel, BorderLayout.NORTH);

        employeeTypeCombo.addActionListener(e -> updateAddControls());
        departmentNameCombo.addActionListener(e -> getDepIds());
        addButton.addActionListener(e -> performAdding());

    }

    private void updateAddControls() {
        String selectedType = (String) employeeTypeCombo.getSelectedItem();
        dynamicControlPanel.removeAll();

        if (selectedType != null && !selectedType.equals("-- Select --")) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 8, 6, 8);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            if (selectedType.equals("Full-Time")) {
                addRow(dynamicControlPanel, gbc, 0, salaryLabel, salaryField);
                addRow(dynamicControlPanel, gbc, 1, bonusLabel, bonusField);
            } else {
                addRow(dynamicControlPanel, gbc, 0, hourlyRateLabel, hourlyRateField);
                addRow(dynamicControlPanel, gbc, 1, workedHoursLabel, workedHoursField);
            }

            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.insets = new Insets(12, 8, 6, 8);
            dynamicControlPanel.add(addButton, gbc);
        }

        dynamicControlPanel.revalidate();
        dynamicControlPanel.repaint();
    }



    public void refreshData() {
        loadDepartmentsIntoCombo();
        getDepIds();
    }

    private void performAdding() {
        String selectedType = (String) employeeTypeCombo.getSelectedItem();
        if (selectedType == null || selectedType.equals("-- Select --")) {
            showWarning("Please select an employee type.");
            return;
        }

        // 1. Validate Employee ID
        String idText = idField.getText().trim();
        if (!ValidationHelper.isValidStringPositiveInt(idText)) {
            showWarning("Please enter a valid positive whole number for Employee ID.");
            return;
        }
        int id = Integer.parseInt(idText);
        if (employeeManager.searchById(id) != null) {
            showWarning("Employee ID " + id + " already exists.");
            return;
        }

        // 2. Validate Full Name
        String fullName = nameField.getText().trim();
        if (!ValidationHelper.isValidName(fullName)) {
            showWarning("Full Name must be 3 to 40 letters and spaces only.");
            return;
        }

        // 3. Validate Age
        String ageText = ageField.getText().trim();
        if (!ValidationHelper.isValidStringPositiveInt(ageText)) {
            showWarning("Please enter a valid positive whole number for Age.");
            return;
        }
        int age = Integer.parseInt(ageText);

        // 4. Validate Department Selection
        Integer selectedDeptId = (Integer) departmentIdCombo.getSelectedItem();
        if (selectedDeptId == null) {
            showWarning("Please select a valid Department ID.");
            return;
        }
        Department selectedDept = departmentManager.findDepartmentById(selectedDeptId);
        if (selectedDept == null) {
            showWarning("Selected department was not found.");
            return;
        }

        // 5. Validate Position
        String position = positionField.getText().trim();
        if (!ValidationHelper.isValidString(position)) {
            showWarning("Position cannot be empty or contain commas.");
            return;
        }

        // 6. Validate Type-Specific Fields & Create Employee
        Employee newEmployee;
        if (selectedType.equals("Full-Time")) {
            String salaryText = salaryField.getText().trim();
            if (!ValidationHelper.isValidStringPositiveDouble(salaryText)) {
                showWarning("Please enter a valid positive number for Salary.");
                return;
            }

            String bonusText = bonusField.getText().trim();
            if (!ValidationHelper.isValidStringNonNegativeDouble(bonusText)) {
                showWarning("Please enter a valid non-negative number for Bonus.");
                return;
            }

            newEmployee = new FullTimeEmployee(
                    id, fullName, age, selectedDept, position,
                    Double.parseDouble(salaryText), Double.parseDouble(bonusText)
            );
        } else {
            String rateText = hourlyRateField.getText().trim();
            if (!ValidationHelper.isValidStringPositiveDouble(rateText)) {
                showWarning("Please enter a valid positive number for Hourly Rate.");
                return;
            }

            String hoursText = workedHoursField.getText().trim();
            if (!ValidationHelper.isValidStringNonNegativeDouble(hoursText)) {
                showWarning("Please enter a valid non-negative number for Worked Hours.");
                return;
            }

            newEmployee = new PartTimeEmployee(
                    id, fullName, age, selectedDept, position,
                    Double.parseDouble(rateText), Double.parseDouble(hoursText)
            );
        }

        employeeManager.addEmployee(newEmployee);
        JOptionPane.showMessageDialog(
                this,
                "Employee \"" + fullName + "\" added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
        clearFields();
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        positionField.setText("");
        salaryField.setText("");
        bonusField.setText("");
        hourlyRateField.setText("");
        workedHoursField.setText("");
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Input", JOptionPane.WARNING_MESSAGE);
    }

    private void loadDepartmentsIntoCombo() {
        departmentNameCombo.removeAllItems();
        java.util.Set<String> seenNames = new java.util.HashSet<>();
        departmentNameCombo.addItem("-- Select --");
        for (Department dept : departmentManager.getDepartments()) {
            String name = dept.getDepartmentName().trim();
            if (seenNames.add(name.toLowerCase()))
                departmentNameCombo.addItem(name);
        }
    }

    private void getDepIds() {
        departmentIdCombo.removeAllItems();
        String selectedType = (String) departmentNameCombo.getSelectedItem();
        if(selectedType == null || selectedType.equals("-- Select --") )
            return;
        ArrayList<Department> deps = departmentManager.findDepartments(selectedType);
        if (deps != null)
            for (Department dep : deps)
                departmentIdCombo.addItem(dep.getDepartmentId());
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, JLabel label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }
}