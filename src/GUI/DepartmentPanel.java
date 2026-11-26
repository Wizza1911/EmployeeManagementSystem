package GUI;

import Backend.Department;
import Backend.DepartmentManager;
import Backend.Employee;
import Backend.EmployeeManager;
import Helper.ValidationHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class DepartmentPanel extends JPanel {

    private final DepartmentManager departmentManager;
    private final EmployeeManager employeeManager;

    private final DefaultTableModel tableModel;
    private final JTable departmentTable;

    // Tab 1: Add New Department
    private final JTextField newDeptNameField = new JTextField(18);
    private final JTextField newDeptIdField = new JTextField(18);
    private final JButton addNewDeptButton = new JButton("Add Department");

    // Tab 2: Add Another ID to Existing Department
    private final JComboBox<String> existingDeptNameCombo = new JComboBox<>();
    private final JTextField additionalDeptIdField = new JTextField(18);
    private final JButton addExistingIdButton = new JButton("Add ID");

    // Tab 3: Edit / Delete Department
    private final JComboBox<String> selectDeptToEditCombo = new JComboBox<>();
    private final JTextField editDeptIdField = new JTextField(18);
    private final JTextField editDeptNameField = new JTextField(18);
    private final JLabel memberCountValueLabel = new JLabel("-");
    private final JButton updateDeptButton = new JButton("Update Department");
    private final JButton deleteDeptButton = new JButton("Delete Department");

    private final JTabbedPane actionTabs = new JTabbedPane();
    private Integer selectedOriginalId = null;
    private String selectedOriginalName = null;
    private boolean isRefreshing = false;

    public DepartmentPanel(DepartmentManager departmentManager, EmployeeManager employeeManager) {
        if (departmentManager == null || employeeManager == null) {
            throw new IllegalArgumentException("Managers cannot be null.");
        }
        this.departmentManager = departmentManager;
        this.employeeManager = employeeManager;

        setLayout(new BorderLayout(12, 12));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        Font labelFont = new Font("SansSerif", Font.BOLD, 13);

        // --- TOP: ACTION TABS ---
        actionTabs.setBackground(Color.WHITE);
        actionTabs.addTab("Add New Department", buildAddNewTab(labelFont));
        actionTabs.addTab("Add ID to Existing Department", buildAddExistingIdTab(labelFont));
        actionTabs.addTab("Edit / Delete Department", buildEditDeleteTab(labelFont));
        add(actionTabs, BorderLayout.NORTH);

        // --- CENTER: DEPARTMENTS TABLE ---
        String[] columns = {"Department ID", "Department Name", "Members Count"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        departmentTable = new JTable(tableModel);
        departmentTable.setRowHeight(25);
        departmentTable.setBackground(Color.WHITE);
        departmentTable.getTableHeader().setReorderingAllowed(false);
        departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(departmentTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All Departments (Click a row to Edit or Delete)"));
        add(scrollPane, BorderLayout.CENTER);

        // --- EVENT LISTENERS ---
        addNewDeptButton.addActionListener(e -> performAddNewDepartment());
        addExistingIdButton.addActionListener(e -> performAddIdToExisting());

        selectDeptToEditCombo.addActionListener(e -> {
            if (!isRefreshing) {
                loadSelectedDepartmentForEdit();
            }
        });
        updateDeptButton.addActionListener(e -> performUpdateDepartment());
        deleteDeptButton.addActionListener(e -> performDeleteDepartment());

        // Clicking a row in the table loads it directly into the Edit / Delete tab
        departmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && departmentTable.getSelectedRow() != -1) {
                int row = departmentTable.getSelectedRow();
                int deptId = (int) tableModel.getValueAt(row, 0);
                String deptName = (String) tableModel.getValueAt(row, 1);
                Department dept = departmentManager.findDepartment(deptId, deptName);
                if (dept != null) {
                    actionTabs.setSelectedIndex(2);
                    selectDeptToEditCombo.setSelectedItem(formatDeptChoice(dept));
                }
            }
        });

        refreshData();
    }

    private JPanel buildAddNewTab(Font labelFont) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = createBaseGbc();

        addRow(panel, gbc, 0, "New Department Name:", newDeptNameField, labelFont);
        addRow(panel, gbc, 1, "Department ID:", newDeptIdField, labelFont);

        addNewDeptButton.setFont(labelFont);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(addNewDeptButton, gbc);

        return panel;
    }

    private JPanel buildAddExistingIdTab(Font labelFont) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = createBaseGbc();

        existingDeptNameCombo.setBackground(Color.WHITE);
        addRow(panel, gbc, 0, "Existing Department:", existingDeptNameCombo, labelFont);
        addRow(panel, gbc, 1, "Additional Department ID:", additionalDeptIdField, labelFont);

        addExistingIdButton.setFont(labelFont);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(addExistingIdButton, gbc);

        return panel;
    }

    private JPanel buildEditDeleteTab(Font labelFont) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = createBaseGbc();

        selectDeptToEditCombo.setBackground(Color.WHITE);
        memberCountValueLabel.setFont(labelFont);

        addRow(panel, gbc, 0, "Select Department:", selectDeptToEditCombo, labelFont);
        addRow(panel, gbc, 1, "Department ID:", editDeptIdField, labelFont);
        addRow(panel, gbc, 2, "Department Name:", editDeptNameField, labelFont);
        addRow(panel, gbc, 3, "Members Count:", memberCountValueLabel, labelFont);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        updateDeptButton.setFont(labelFont);
        updateDeptButton.setBackground(new Color(40, 167, 69));
        updateDeptButton.setForeground(Color.WHITE);
        updateDeptButton.setFocusPainted(false);

        deleteDeptButton.setFont(labelFont);
        deleteDeptButton.setBackground(new Color(220, 53, 69));
        deleteDeptButton.setForeground(Color.WHITE);
        deleteDeptButton.setFocusPainted(false);

        btnPanel.add(deleteDeptButton);
        btnPanel.add(updateDeptButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnPanel, gbc);

        return panel;
    }

    public void refreshData() {
        isRefreshing = true;

        // 1. Refresh Table
        tableModel.setRowCount(0);
        for (Department d : departmentManager.getDepartments()) {
            int displayCount = getEffectiveMemberCount(d);
            tableModel.addRow(new Object[]{
                    d.getDepartmentId(),
                    d.getDepartmentName(),
                    displayCount
            });
        }

        // 2. Refresh Deduplicated Existing Department Names Combo
        existingDeptNameCombo.removeAllItems();
        existingDeptNameCombo.addItem("-- Select Department --");
        Set<String> seenNames = new HashSet<>();
        for (Department d : departmentManager.getDepartments()) {
            String name = d.getDepartmentName().trim();
            if (seenNames.add(name.toLowerCase())) {
                existingDeptNameCombo.addItem(name);
            }
        }

        // 3. Refresh Edit/Delete Department Selector Combo
        selectDeptToEditCombo.removeAllItems();
        selectDeptToEditCombo.addItem("-- Select Department --");
        for (Department d : departmentManager.getDepartments()) {
            selectDeptToEditCombo.addItem(formatDeptChoice(d));
        }

        clearEditFields();
        isRefreshing = false;
    }

    private void performAddNewDepartment() {
        String name = newDeptNameField.getText().trim();
        String idText = newDeptIdField.getText().trim();
        if (!ValidationHelper.isValidString(name)) {
            showWarning("Department name cannot be empty or contain commas.");
            return;
        }
        if (!ValidationHelper.isValidStringPositiveInt(idText)) {
            showWarning("Please enter a valid positive whole number for Department ID.");
            return;
        }
        // Inside performAddNewDepartment():
        int id=Integer.parseInt(idText);
        if (departmentManager.findDepartment(id, name) != null) {
            showWarning("Department \"" + name + "\" with ID " + id + " already exists.");
            return;
        }

        departmentManager.addDepartment(new Department(id, name));
        JOptionPane.showMessageDialog(this, "Department:" + name + " (ID: " + id + ") added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        newDeptNameField.setText("");
        newDeptIdField.setText("");
        refreshData();
    }

    private void performAddIdToExisting() {
        String selectedName = (String) existingDeptNameCombo.getSelectedItem();
        if (selectedName == null || selectedName.equals("-- Select Department --")) {
            showWarning("Please select an existing department.");
            return;
        }

        String idText = additionalDeptIdField.getText().trim();
        if (!ValidationHelper.isValidStringPositiveInt(idText)) {
            showWarning("Please enter a valid positive whole number for Department ID.");
            return;
        }
        int newId = Integer.parseInt(idText);
        if (departmentManager.findDepartment(newId, selectedName) != null) {
            showWarning("Department \"" + selectedName + "\" already has ID " + newId + ".");
            return;
        }

        departmentManager.addDepartment(new Department(newId, selectedName));
        JOptionPane.showMessageDialog(this, "Added ID " + newId + " to department \"" + selectedName + "\"!", "Success", JOptionPane.INFORMATION_MESSAGE);
        additionalDeptIdField.setText("");
        refreshData();
    }

    private void loadSelectedDepartmentForEdit() {
        String selected = (String) selectDeptToEditCombo.getSelectedItem();
        if (selected == null || selected.equals("-- Select Department --")) {
            clearEditFields();
            return;
        }

        int dashIdx = selected.indexOf(" - ");
        if (dashIdx <= 0) {
            clearEditFields();
            return;
        }

        int id = Integer.parseInt(selected.substring(0, dashIdx).trim());
        String name = selected.substring(dashIdx + 3).trim();
        Department dept = departmentManager.findDepartment(id, name);
        if (dept == null) {
            clearEditFields();
            return;
        }

        selectedOriginalId = dept.getDepartmentId();
        selectedOriginalName = dept.getDepartmentName();
        editDeptIdField.setText(String.valueOf(dept.getDepartmentId()));
        editDeptNameField.setText(dept.getDepartmentName());

        int count = getEffectiveMemberCount(dept);
        memberCountValueLabel.setText(String.valueOf(count));
        deleteDeptButton.setEnabled(count == 0);
    }

    private void performUpdateDepartment() {
        if (selectedOriginalId == null || selectedOriginalName == null) {
            showWarning("Please select a department to update.");
            return;
        }

        String idText = editDeptIdField.getText().trim();
        if (!ValidationHelper.isValidStringPositiveInt(idText)) {
            showWarning("Please enter a valid positive whole number for Department ID.");
            return;
        }
        int newId = Integer.parseInt(idText);

        String newName = editDeptNameField.getText().trim();
        if (!ValidationHelper.isValidString(newName)) {
            showWarning("Department name cannot be empty or contain commas.");
            return;
        }

        boolean isSameKey = (newId == selectedOriginalId)
                && newName.equalsIgnoreCase(selectedOriginalName);
        if (!isSameKey && departmentManager.findDepartment(newId, newName) != null) {
            showWarning("Department \"" + newName + "\" with ID " + newId + " already exists.");
            return;
        }

        Department existingDept = departmentManager.findDepartment(selectedOriginalId, selectedOriginalName);
        Department updatedDept = new Department(newId, newName);
        if (existingDept != null) {
            updatedDept.setCount(getEffectiveMemberCount(existingDept));
        }

        departmentManager.updateDepartment(selectedOriginalId, selectedOriginalName, updatedDept);

        // Update and persist any employees assigned to the exact (originalId, originalName) department
        for (Employee emp : employeeManager.getAllEmployees()) {
            if (emp.getDepartment() != null
                    && emp.getDepartment().getDepartmentId() == selectedOriginalId
                    && emp.getDepartment().getDepartmentName().trim().equalsIgnoreCase(selectedOriginalName)) {
                emp.setDepartment(updatedDept);
                employeeManager.updateEmployee(emp.getId(), emp);
            }
        }

        JOptionPane.showMessageDialog(this, "Department updated successfully!", "Updated", JOptionPane.INFORMATION_MESSAGE);
        refreshData();
    }

    private void performDeleteDepartment() {
        if (selectedOriginalId == null || selectedOriginalName == null) {
            showWarning("Please select a department to delete.");
            return;
        }

        Department target = departmentManager.findDepartment(selectedOriginalId, selectedOriginalName);
        if (target == null) {
            showWarning("Department not found.");
            return;
        }

        int memberCount = getEffectiveMemberCount(target);
        if (memberCount != 0) {
            showWarning("Cannot delete \"" + target.getDepartmentName() + "\" (ID: " + target.getDepartmentId()
                    + ") because it has " + memberCount + " member(s).");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete department \"" + target.getDepartmentName() + "\" (ID: " + target.getDepartmentId() + ")?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            departmentManager.deleteDepartment(selectedOriginalId, selectedOriginalName);
            JOptionPane.showMessageDialog(this, "Department deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            refreshData();
        }
    }

    private int getEffectiveMemberCount(Department dept) {
        int liveCount = 0;
        for (Employee emp : employeeManager.getAllEmployees()) {
            if (emp != null && emp.getDepartment() != null
                    && emp.getDepartment().getDepartmentId() == dept.getDepartmentId()
                    && emp.getDepartment().getDepartmentName().trim()
                    .equalsIgnoreCase(dept.getDepartmentName().trim())) {
                liveCount++;
            }
        }
        dept.setCount(liveCount);
        return dept.getCount();
    }

    private void clearEditFields() {
        selectedOriginalId = null;
        selectedOriginalName = null;
        editDeptIdField.setText("");
        editDeptNameField.setText("");
        memberCountValueLabel.setText("-");
        deleteDeptButton.setEnabled(false);
    }

    private String formatDeptChoice(Department d) {
        return d.getDepartmentId() + " - " + d.getDepartmentName();
    }

    private GridBagConstraints createBaseGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field, Font font) {
        JLabel label = new JLabel(labelText);
        label.setFont(font);

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