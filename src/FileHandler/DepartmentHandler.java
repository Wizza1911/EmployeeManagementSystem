package FileHandler;

import Backend.Department;
import Helper.ValidationHelper;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DepartmentHandler implements FileOperator<Department> {

    private static final String DEPARTMENTS_CSV = "Data/Departments.csv";

    @Override
    public boolean save(Department department) {
        if(department==null)
            return false;
        try(PrintWriter writer = new PrintWriter(new FileWriter(DEPARTMENTS_CSV,true))) {
            writer.println(department.toString());
        }
        catch (IOException e) {
            System.out.println("Error saving Department data: " + e.getMessage());
            return false;
        }
        return true;
    }
    @Override
    public boolean saveAll(ArrayList<Department> departments) {
        if(departments==null)
            return false;
        try (PrintWriter writer = new PrintWriter(new FileWriter(DEPARTMENTS_CSV))) {
            for (Department d : departments)
                if(d!=null)
                    writer.println(d.toString());
        }
        catch (IOException e) {
            System.out.println("Error saving departments: " + e.getMessage());
            return false;
        }
        return true;
    }
    @Override
    public ArrayList<Department> load() {
        ArrayList<Department> departments = new ArrayList<>();
        File file = new File(DEPARTMENTS_CSV);
        try {
            if (file.createNewFile()) {
                System.out.println("Departments file created successfully.");
                return departments;
            }
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] data = line.split(",");
                    if (data.length != 2) {
                        System.out.println("Skipping invalid department record.");
                        continue;
                    }
                    String idStr = data[0].trim();
                    String departmentName = data[1].trim();
                    if (!ValidationHelper.isValidStringPositiveInt(idStr)
                            || !ValidationHelper.isValidString(departmentName)) {
                        System.out.println("Skipping invalid department record.");
                        continue;
                    }
                    departments.add(new Department(Integer.parseInt(idStr), departmentName));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading Departments file: " + e.getMessage());
        }
        return departments;
    }
}
