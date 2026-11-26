package GUI;

import Backend.Department;
import Backend.DepartmentManager;
import Backend.Employee;
import Backend.EmployeeManager;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private static final Color BG = new Color(240, 245, 250);
    private static final Color BORDER = new Color(225, 230, 240);
    private static final Color TEXT = new Color(15, 20, 40);
    private static final Color MUTED = new Color(100, 120, 140);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color GREEN = new Color(22, 163, 74);
    private static final Color ORANGE = new Color(217, 119, 6);

    private final EmployeeManager employeeManager;
    private final DepartmentManager departmentManager;

    private final JLabel employeesValue = new JLabel();
    private final JLabel departmentsValue = new JLabel();
    private final JLabel payrollValue = new JLabel();
    private final JPanel departmentRows = new JPanel();
    private final JPanel typeRows = new JPanel();

    public DashboardPanel(EmployeeManager employeeManager, DepartmentManager departmentManager) {
        if (employeeManager == null || departmentManager == null) {
            throw new IllegalArgumentException("Managers cannot be null.");
        }
        this.employeeManager = employeeManager;
        this.departmentManager = departmentManager;

        setLayout(new BorderLayout(0, 20));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setOpaque(false);
        header.add(text("Welcome back, Admin", Font.BOLD, 28, TEXT));
        header.add(text("Here is an overview of your company today.", Font.PLAIN, 15, MUTED));
        add(header, BorderLayout.NORTH);

        
        JPanel cards = new JPanel(new GridLayout(1, 3, 20, 0));
        cards.setOpaque(false);
        cards.setPreferredSize(new Dimension(0, 120));
        cards.add(statCard("Total Employees", employeesValue, BLUE));
        cards.add(statCard("Total Departments", departmentsValue, GREEN));
        cards.add(statCard("Monthly Payroll", payrollValue, ORANGE));

        
        JPanel details = new JPanel(new GridLayout(1, 2, 20, 0));
        details.setOpaque(false);
        details.add(panelCard("Employees by Department", departmentRows));
        details.add(panelCard("Full Time vs Part Time", typeRows));

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setOpaque(false);
        content.add(cards, BorderLayout.NORTH);
        content.add(details, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        refreshData();
    }


    private JLabel text(String s, int style, int size, Color color) {
        JLabel l = new JLabel(s);
        l.setFont(new Font("SansSerif", style, size));
        l.setForeground(color);
        return l;
    }

  
    private JPanel statCard(String title, JLabel value, Color accent) {
        value.setFont(new Font("SansSerif", Font.BOLD, 26));
        value.setForeground(TEXT);

        JPanel body = new JPanel(new GridLayout(2, 1, 0, 2));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        body.add(text(title, Font.PLAIN, 14, MUTED));
        body.add(value);

        JPanel bar = new JPanel();
        bar.setBackground(accent);
        bar.setPreferredSize(new Dimension(5, 0));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(BORDER));
        card.add(bar, BorderLayout.WEST);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    
    private JPanel panelCard(String title, JPanel rows) {
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        card.add(text(title, Font.BOLD, 18, TEXT), BorderLayout.NORTH);
        card.add(new JScrollPane(rows) {{
            setBorder(null);
            setOpaque(false);
            getViewport().setOpaque(false);
        }}, BorderLayout.CENTER);
        return card;
    }


    private JPanel barRow(String name, int value, int max, Color color) {
        JProgressBar bar = new JProgressBar(0, Math.max(max, 1));
        bar.setValue(value);
        bar.setForeground(color);
        bar.setBackground(new Color(226, 232, 240));
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(100, 10));

        JLabel label = text(name, Font.PLAIN, 14, TEXT);
        label.setPreferredSize(new Dimension(130, 20));
        JLabel number = text(String.valueOf(value), Font.BOLD, 14, TEXT);
        number.setPreferredSize(new Dimension(28, 20));

        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        row.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        row.add(label, BorderLayout.WEST);
        row.add(bar, BorderLayout.CENTER);
        row.add(number, BorderLayout.EAST);
        return row;
    }


    
    public void refreshData() {
        int fullTime = 0, partTime = 0;
        double payroll = 0;
        for (Employee emp : employeeManager.getAllEmployees()) {
            payroll += emp.calculateSalary(); 
            if (emp.isFullTime())
                 fullTime++;
            else
                 partTime++;
        }
        int total = fullTime + partTime;
        employeesValue.setText(String.valueOf(total));
        departmentsValue.setText(String.valueOf(departmentManager.getDepartments().size()));
        payrollValue.setText(String.format("$%,.2f", payroll));

        typeRows.removeAll();
        typeRows.add(barRow("Full-Time", fullTime, total, BLUE));
        typeRows.add(barRow("Part-Time", partTime, total, ORANGE));

        departmentRows.removeAll();
        for (Department d : departmentManager.getDepartments()) {
            int count = 0;
            for (Employee emp : employeeManager.getAllEmployees()) {
                if (emp.getDepartment().getDepartmentId() == d.getDepartmentId()) count++;
            }
            departmentRows.add(barRow(d.getDepartmentName(), count, total, GREEN));
        }
        revalidate();
        repaint();
    }
}
