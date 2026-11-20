package FileHandler;

import java.io.*;
import java.util.*;
import Backend.*;



public class FileHandler {

    public static void saveEmployees(ArrayList<Employee> employees) {

        ArrayList<Department> departments = loadDepartments();

        try (PrintWriter writer = new PrintWriter(new FileWriter("Data.csv"))) {
            // first line for formating the columns
            //it creates a new file if this one doesn't exist
            writer.println(
                "id,type,fullName,age,department,position,basicSalary,bonus,hourlyRate,hoursWorked"
            );

            for (Employee e : employees)
                writer.println(e.toString());

            writer.println("DepartmentID,DepartmentName");

            for (Department d : departments) {
                writer.println(
                    d.getDepartmentId() + "," + d.getDepartmentName()
                );
            }

        }
        catch (IOException e) {
            System.out.println("Error saving employees: " + e.getMessage());
            //The only reason for error is that the disk space is full or os error
            // hence java won't compile without it
        }
    }

    public static ArrayList<Employee> loadEmployees() {

        ArrayList<Employee> employees = new ArrayList<>();

        try (Scanner sc = new Scanner(new File("Data.csv"))) {

            if (sc.hasNextLine()) {
                sc.nextLine();
            }

            while (sc.hasNextLine()) {

                String line = sc.nextLine();

                if (line.equals("DepartmentID,DepartmentName")) {
                    break;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",", -1);

               
                if (data.length != 10) {
                    System.out.println("Skipping invalid employee record.");
                    continue;
                }

                try {

                    int id = Integer.parseInt(data[0]);
                    String type = data[1];
                    String fullName = data[2];
                    int age = Integer.parseInt(data[3]);
                    String department = data[4];
                    String position = data[5];

                   
                    if (id <= 0 || age <= 0 ||
                        fullName.trim().isEmpty() ||
                        department.trim().isEmpty() ||
                        position.trim().isEmpty()) {

                        System.out.println("Skipping invalid employee record.");
                        continue;
                    }

                    if (type.equals("FullTime")) {

                        double basicSalary = Double.parseDouble(data[6]);
                        double bonus = Double.parseDouble(data[7]);

                        if (basicSalary < 0 || bonus < 0) {
                            continue;
                        }

                        FullTimeEmployee f = new FullTimeEmployee(
                            id, fullName, age, department, position,
                            basicSalary, bonus
                        );

                        employees.add(f);

                    }
                    else if (type.equals("PartTime")) {

                        double hourlyRate = Double.parseDouble(data[8]);
                        double hoursWorked = Double.parseDouble(data[9]);

                        if (hourlyRate < 0 || hoursWorked < 0) {
                            continue;
                        }

                        PartTimeEmployee p = new PartTimeEmployee(
                            id, fullName, age, department, position,
                            hourlyRate, hoursWorked
                        );

                        employees.add(p);
                    }
                    else {
                        System.out.println("Skipping unknown employee type.");
                    }

                }
                catch (NumberFormatException e) {
                    System.out.println("Skipping employee with invalid numbers.");
                }
            }

        }
        catch (FileNotFoundException e) {
            System.out.println("Employee data file not found.");
        }

        return employees;
    }

    public static void saveDepartments(ArrayList<Department> departments) {

        ArrayList<Employee> employees = loadEmployees();

        try (PrintWriter writer = new PrintWriter(new FileWriter("Data.csv"))) {

            writer.println(
                "id,type,fullName,age,department,position,basicSalary,bonus,hourlyRate,hoursWorked"
            );

            for (Employee e : employees) {

                if (e instanceof FullTimeEmployee) {
                    FullTimeEmployee f = (FullTimeEmployee) e;

                    writer.println(
                        f.getId() + ",FullTime," + f.getFullName() + "," +
                        f.getAge() + "," + f.getDepartment() + "," +
                        f.getPosition() + "," + f.getBasicSalary() + "," +
                        f.getBonus() + ",,"
                    );

                }
                else if (e instanceof PartTimeEmployee) {
                    PartTimeEmployee p = (PartTimeEmployee) e;

                    writer.println(
                        p.getId() + ",PartTime," + p.getFullName() + "," +
                        p.getAge() + "," + p.getDepartment() + "," +
                        p.getPosition() + ",,," + p.getHourlyRate() + "," +
                        p.getHoursWorked()
                    );
                }
            }

            writer.println("DepartmentID,DepartmentName");

            for (Department d : departments) {
                writer.println(
                    d.getDepartmentId() + "," + d.getDepartmentName()
                );
            }

        }
        catch (IOException e) {
            System.out.println("Error saving departments: " + e.getMessage());
        }
    }

    public static ArrayList<Department> loadDepartments() {

        ArrayList<Department> departments = new ArrayList<>();

        try (Scanner sc = new Scanner(new File("Data.csv"))) {

            boolean departmentFlag = false;

            while (sc.hasNextLine()) {

                String line = sc.nextLine();

                if (line.equals("DepartmentID,DepartmentName")) {
                    departmentFlag = true;
                    continue;
                }

                if (!departmentFlag || line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",", -1);

                if (data.length != 2 || data[0].trim().isEmpty() || data[1].trim().isEmpty()) {
                    System.out.println("Skipping invalid department record.");
                    continue;
                }

                try {

                    int departmentId = Integer.parseInt(data[0]);
                    String departmentName = data[1];

                    if (departmentId <= 0 || departmentName.trim().isEmpty()) {
                        continue;
                    }

                    Department d = new Department(
                        departmentId, departmentName
                    );

                    departments.add(d);

                }
                catch (NumberFormatException e) {
                    System.out.println("Skipping department with invalid ID.");
                }
            }

        }
        catch (FileNotFoundException e) {
            System.out.println("Departments file not found.");
        }

        return departments;
    }
}
