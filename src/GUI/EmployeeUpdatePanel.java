package GUI;

import Backend.*;
import Helper.ValidationHelper;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EmployeeUpdatePanel extends JPanel {

    private final EmployeeManager employeeManager;
    private final DepartmentManager departmentManager;

    // Top lookup controls
    private final JComboBox<String> employeeSelectCombo;
    private final JTextField searchIdField;
    private final JButton loadButton;

    // Editable form container & fields
    private final JPanel editFormContainer;
    private final JComboBox<String> employeeTypeCombo;
    private final JTextField idField = new JTextField(20);
    private final JTextField nameField = new JTextField(20);
    private final JTextField ageField = new JTextField(20);
    private final JComboBox<String> departmentNameCombo;
    private final JComboBox<Integer> departmentIdCombo;
    private final JTextField positionField = new JTextField(20);

    // Type-specific fields & labels
    private final JTextField salaryField = new JTextField(20);
    private final JTextField bonusField = new JTextField(20);
    private final JTextField hourlyRateField = new JTextField(20);
    private final JTextField workedHoursField = new JTextField(20);

    private final JLabel salaryLabel = new JLabel("Salary:");
    private final JLabel bonusLabel = new JLabel("Bonus:");
    private final JLabel hourlyRateLabel = new JLabel("Hourly Rate:");
    private final JLabel workedHoursLabel = new JLabel("Worked Hours:");

    private final JPanel dynamicTypePanel;
    private final JButton updateButton;
    private final JButton deleteButton;

    private Integer loadedOriginalId = null;
    private boolean isPopulatingForm = false;

    public EmployeeUpdatePanel(EmployeeManager employeeManager, DepartmentManager departmentManager) {
        if (employeeManager == null || departmentManager == null) {
            throw new IllegalArgumentException("Managers cannot be null.");
        }
        this.employeeManager = employeeManager;
        this.departmentManager = departmentManager;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        Font labelFont = new Font("SansSerif", Font.BOLD, 13);

        // --- TOP SELECTION / SEARCH BAR ---
        JPanel lookupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        lookupPanel.setBackground(Color.WHITE);
        lookupPanel.setBorder(BorderFactory.createTitledBorder("Find Employee to Edit or Delete"));

        JLabel selectLabel = new JLabel("Employee:");
        selectLabel.setFont(labelFont);

        employeeSelectCombo = new JComboBox<>();
        employeeSelectCombo.setBackground(Color.WHITE);
        employeeSelectCombo.setPreferredSize(new Dimension(210, 26));

        JLabel orIdLabel = new JLabel("or ID:");
        orIdLabel.setFont(labelFont);

        searchIdField = new JTextField(8);
        loadButton = new JButton("Load");

        lookupPanel.add(selectLabel);
        lookupPanel.add(employeeSelectCombo);
        lookupPanel.add(orIdLabel);
        lookupPanel.add(searchIdField);
        lookupPanel.add(loadButton);

        add(lookupPanel, BorderLayout.NORTH);

        // --- EDITABLE FORM CONTAINER (Hidden until an employee is loaded) ---
        editFormContainer = new JPanel(new BorderLayout());
        editFormContainer.setBackground(Color.WHITE);
        editFormContainer.setVisible(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel employeeTypeLabel = new JLabel("Type:");
        employeeTypeLabel.setFont(labelFont);
        JLabel iDLabel = new JLabel("ID:");
        iDLabel.setFont(labelFont);
        JLabel employeeNameLabel = new JLabel("Name:");
        employeeNameLabel.setFont(labelFont);
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(labelFont);
        JLabel departmentNameLabel = new JLabel("Department Name:");
        departmentNameLabel.setFont(labelFont);
        JLabel departmentIdLabel = new JLabel("Department ID:");
        departmentIdLabel.setFont(labelFont);
        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setFont(labelFont);

        salaryLabel.setFont(labelFont);
        bonusLabel.setFont(labelFont);
        hourlyRateLabel.setFont(labelFont);
        workedHoursLabel.setFont(labelFont);

        employeeTypeCombo = new JComboBox<>(new String[]{"Full-Time", "Part-Time"});
        employeeTypeCombo.setBackground(Color.WHITE);

        departmentNameCombo = new JComboBox<>();
        departmentNameCombo.setBackground(Color.WHITE);

        departmentIdCombo = new JComboBox<>();
        departmentIdCombo.setBackground(Color.WHITE);

        addRow(formPanel, gbc, 0, employeeTypeLabel, employeeTypeCombo);
        addRow(formPanel, gbc, 1, iDLabel, idField);
        addRow(formPanel, gbc, 2, employeeNameLabel, nameField);
        addRow(formPanel, gbc, 3, ageLabel, ageField);
        addRow(formPanel, gbc, 4, departmentNameLabel, departmentNameCombo);
        addRow(formPanel, gbc, 5, departmentIdLabel, departmentIdCombo);
        addRow(formPanel, gbc, 6, positionLabel, positionField);

        // Dynamic salary/bonus or hourlyRate/workedHours sub-panel
        dynamicTypePanel = new JPanel(new GridBagLayout());
        dynamicTypePanel.setBackground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(4, 0, 0, 0);
        formPanel.add(dynamicTypePanel, gbc);

        // Action buttons (Update & Delete)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        updateButton = new JButton("Update Employee");
        updateButton.setFont(labelFont);
        updateButton.setBackground(new Color(40, 167, 69));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);

        deleteButton = new JButton("Delete Employee");
        deleteButton.setFont(labelFont);
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);

        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        editFormContainer.add(formPanel, BorderLayout.NORTH);
        add(editFormContainer, BorderLayout.CENTER);

        // Initial data load
        refreshData();

        // --- EVENT LISTENERS ---
        employeeSelectCombo.addActionListener(e -> {
            if (!isPopulatingForm) {
                loadFromComboSelection();
            }
        });
        loadButton.addActionListener(e -> loadFromSearchField());
        searchIdField.addActionListener(e -> loadFromSearchField());

        employeeTypeCombo.addActionListener(e -> updateDynamicTypeFields());
        departmentNameCombo.addActionListener(e -> getDepIds());

        updateButton.addActionListener(e -> performUpdate());
        deleteButton.addActionListener(e -> performDelete());
    }

    public void refreshData() {
        isPopulatingForm = true;
        loadedOriginalId = null;
        searchIdField.setText("");
        editFormContainer.setVisible(false);

        loadDepartmentsIntoCombo();
        loadEmployeesIntoCombo();
        isPopulatingForm = false;

        revalidate();
        repaint();
    }

    private void loadEmployeesIntoCombo() {
        employeeSelectCombo.removeAllItems();
        employeeSelectCombo.addItem("-- Select Employee --");
        for (Employee emp : employeeManager.getAllEmployees()) {
            employeeSelectCombo.addItem(emp.getId() + " - " + emp.getFullName());
        }
    }

    private void loadDepartmentsIntoCombo() {
        departmentNameCombo.removeAllItems();
        Set<String> seenNames = new HashSet<>();
        for (Department dept : departmentManager.getDepartments()) {
            String name = dept.getDepartmentName().trim();
            if (seenNames.add(name.toLowerCase())) {
                departmentNameCombo.addItem(name);
            }
        }
    }

    private void getDepIds() {
        departmentIdCombo.removeAllItems();
        String selectedDeptName = (String) departmentNameCombo.getSelectedItem();
        if (selectedDeptName == null) {
            return;
        }
        ArrayList<Department> deps = departmentManager.findDepartments(selectedDeptName);
        if (deps != null) {
            for (Department dep : deps) {
                departmentIdCombo.addItem(dep.getDepartmentId());
            }
        }
    }

    private void loadFromComboSelection() {
        String selected = (String) employeeSelectCombo.getSelectedItem();
        if (selected == null || selected.equals("-- Select Employee --")) {
            loadedOriginalId = null;
            editFormContainer.setVisible(false);
            return;
        }

        int dashIndex = selected.indexOf(" - ");
        if (dashIndex <= 0) {
            return;
        }
        String idPart = selected.substring(0, dashIndex).trim();
        if (!ValidationHelper.isValidStringPositiveInt(idPart)) {
            return;
        }

        Employee emp = employeeManager.searchById(Integer.parseInt(idPart));
        if (emp != null) {
            searchIdField.setText(String.valueOf(emp.getId()));
            populateFormWithEmployee(emp);
        }
    }

    private void loadFromSearchField() {
        String idText = searchIdField.getText().trim();
        if (!ValidationHelper.isValidStringPositiveInt(idText)) {
            showWarning("Please enter a valid positive whole number for Employee ID.");
            return;
        }

        int targetId = Integer.parseInt(idText);
        Employee emp = employeeManager.searchById(targetId);
        if (emp == null) {
            showWarning("No employee found with ID " + targetId + ".");
            return;
        }

        isPopulatingForm = true;
        employeeSelectCombo.setSelectedItem(emp.getId() + " - " + emp.getFullName());
        isPopulatingForm = false;

        populateFormWithEmployee(emp);
    }

    private void populateFormWithEmployee(Employee emp) {
        loadedOriginalId = emp.getId();

        idField.setText(String.valueOf(emp.getId()));
        nameField.setText(emp.getFullName());
        ageField.setText(String.valueOf(emp.getAge()));
        positionField.setText(emp.getPosition());

        // Set department name first (triggers getDepIds), then select the exact department ID
        departmentNameCombo.setSelectedItem(emp.getDepartment().getDepartmentName());
        departmentIdCombo.setSelectedItem(emp.getDepartment().getDepartmentId());

        if (emp.isFullTime()) {
            employeeTypeCombo.setSelectedItem("Full-Time");
            FullTimeEmployee ft = (FullTimeEmployee) emp;
            salaryField.setText(String.valueOf(ft.getBasicSalary()));
            bonusField.setText(String.valueOf(ft.getBonus()));
            hourlyRateField.setText("");
            workedHoursField.setText("");
        } else {
            employeeTypeCombo.setSelectedItem("Part-Time");
            PartTimeEmployee pt = (PartTimeEmployee) emp;
            hourlyRateField.setText(String.valueOf(pt.getHourlyRate()));
            workedHoursField.setText(String.valueOf(pt.getHoursWorked()));
            salaryField.setText("");
            bonusField.setText("");
        }

        updateDynamicTypeFields();
        editFormContainer.setVisible(true);
        revalidate();
        repaint();
    }

    private void updateDynamicTypeFields() {
        String selectedType = (String) employeeTypeCombo.getSelectedItem();
        dynamicTypePanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        if ("Full-Time".equals(selectedType)) {
            addRow(dynamicTypePanel, gbc, 0, salaryLabel, salaryField);
            addRow(dynamicTypePanel, gbc, 1, bonusLabel, bonusField);
        } else {
            addRow(dynamicTypePanel, gbc, 0, hourlyRateLabel, hourlyRateField);
            addRow(dynamicTypePanel, gbc, 1, workedHoursLabel, workedHoursField);
        }

        dynamicTypePanel.revalidate();
        dynamicTypePanel.repaint();
    }

    private void performUpdate() {
        if (loadedOriginalId == null) {
            showWarning("Please load an employee first.");
            return;
        }

        // 1. Validate Employee ID
        String idText = idField.getText().trim();
        if (!ValidationHelper.isValidStringPositiveInt(idText)) {
            showWarning("Please enter a valid positive whole number for Employee ID.");
            return;
        }
        int newId = Integer.parseInt(idText);
        if (newId != loadedOriginalId && employeeManager.searchById(newId) != null) {
            showWarning("Employee ID " + newId + " already exists.");
            return;
        }

        // 2. Validate Full Name
        String fullName = nameField.getText().trim();
        if (!ValidationHelper.isValidName(fullName)) {
            showWarning("Full Name must be 3 to 20 letters and spaces only.");
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
        String selectedDeptName = (String) departmentNameCombo.getSelectedItem();
        Integer selectedDeptId = (Integer) departmentIdCombo.getSelectedItem();
        if (selectedDeptName == null || selectedDeptId == null) {
            showWarning("Please select a valid Department and Department ID.");
            return;
        }
        Department selectedDept = departmentManager.findDepartment(selectedDeptId, selectedDeptName);
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

        // 6. Validate Type-Specific Fields & Build Updated Employee
        String selectedType = (String) employeeTypeCombo.getSelectedItem();
        Employee updatedEmployee;

        if ("Full-Time".equals(selectedType)) {
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
            updatedEmployee = new FullTimeEmployee(
                    newId, fullName, age, selectedDept, position,
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
            updatedEmployee = new PartTimeEmployee(
                    newId, fullName, age, selectedDept, position,
                    Double.parseDouble(rateText), Double.parseDouble(hoursText)
            );
        }

        employeeManager.updateEmployee(loadedOriginalId, updatedEmployee);
        JOptionPane.showMessageDialog(
                this,
                "Employee \"" + fullName + "\" updated successfully!",
                "Updated",
                JOptionPane.INFORMATION_MESSAGE
        );
        refreshData();
    }

    private void performDelete() {
        if (loadedOriginalId == null) {
            showWarning("Please load an employee first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete employee ID " + loadedOriginalId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            Department d=employeeManager.searchById(loadedOriginalId).getDepartment();
            d.setCount(d.getCount() - 1);
            employeeManager.deleteEmployee(loadedOriginalId);
            JOptionPane.showMessageDialog(
                    this,
                    "Employee deleted successfully.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );
            refreshData();
        }
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

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Input", JOptionPane.WARNING_MESSAGE);
    }
}
