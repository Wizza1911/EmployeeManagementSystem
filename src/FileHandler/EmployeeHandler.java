package FileHandler;

import Backend.Department;
import Backend.Employee;
import Backend.FullTimeEmployee;
import Backend.PartTimeEmployee;
import Helper.ValidationHelper;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;



public class EmployeeHandler implements FileOperator<Employee>{
    private ArrayList<Department> departments;
    public EmployeeHandler(ArrayList<Department> departments) {
        this.departments = (departments != null) ? departments : new ArrayList<>();
    }
    private Department findDepartment(String name,int id) {
        for (Department d : departments)
            if (d.getDepartmentId() == id &&  d.getDepartmentName().equals(name))
                return d;
        return null;
    }
    @Override
    public boolean save(Employee employee){
        if(employee == null) return false;
        try (PrintWriter writer = new PrintWriter(new FileWriter("Employees.csv",true))){
            writer.println(employee.toString());
        }
        catch(IOException e){
            System.out.println("Error saving employee: " + e.getMessage());
            return false;
        }
        return true;
    }
    @Override
    public boolean saveAll(ArrayList<Employee> employees) {
        if(employees==null) return false;
        try (PrintWriter writer = new PrintWriter(new FileWriter("Employees.csv"))) {
            //it creates a new file if this one doesn't exist
            for (Employee e : employees)
                if (e!=null)
                    writer.println(e.toString());
        }
        catch (IOException e) {
            System.out.println("Error saving employees: " + e.getMessage());
            return false;
            //The only reason for error is that the disk space is full or os error
            // hence java won't compile without it
        }
        return true;
    }
    @Override
    public ArrayList<Employee> load() {
        ArrayList<Employee> employees = new ArrayList<>();
        File file = new File("Employees.csv");
        try {
            if (file.createNewFile()) {
                System.out.println("Employees data file created successfully.");
                return employees;
            }
            try (Scanner sc = new Scanner(file)){
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (line.trim().isEmpty())
                        continue;
                    String[] data = line.split(",",-1);
                    if (data.length != 11) {
                        System.out.println("Skipping invalid employee record.");
                        continue;
                    }
                    if(!ValidationHelper.isValidStringPostiveInt(data[0])
                        ||!ValidationHelper.isValidString(data[1])
                        ||!ValidationHelper.isValidName(data[2])
                        ||!ValidationHelper.isValidStringPostiveInt(data[3])
                        ||!ValidationHelper.isValidStringPostiveInt(data[4])
                        ||!ValidationHelper.isValidString(data[5])
                        ||!ValidationHelper.isValidName(data[6])){
                        System.out.println("Skipping invalid employee record.");
                        continue;
                    }
                    int id = Integer.parseInt(data[0].trim());
                    String fullName = data[2];
                    int age = Integer.parseInt(data[3].trim());
                    int departmentId = Integer.parseInt(data[4].trim());
                    String departmentName = data[5];
                    String position = data[6];
                    Department department = findDepartment(departmentName, departmentId);
                    if (department == null) {
                        System.out.println("Skipping invalid employee record.");
                        continue;
                    }
                    if(data[1].equals("FullTime")){
                        if(!ValidationHelper.isValidStringPostiveInt(data[7])
                            ||!ValidationHelper.isValidStringNonNegativeInt(data[8])){
                            System.out.println("Skipping invalid employee record.");
                            continue;}
                        int basicSalary = Integer.parseInt(data[7].trim());
                        int bonus = Integer.parseInt(data[8].trim());
                        employees.add(new FullTimeEmployee(id, fullName, age, department, position, basicSalary, bonus));
                    }
                    if(data[1].equals("PartTime")){
                        if(!ValidationHelper.isValidStringPostiveInt(data[9])
                                ||!ValidationHelper.isValidStringNonNegativeInt(data[10])){
                            System.out.println("Skipping invalid employee record.");
                            continue;}
                        int hourlyRate = Integer.parseInt(data[9].trim());
                        int workedHours = Integer.parseInt(data[10].trim());
                        employees.add(new PartTimeEmployee(id, fullName, age, department, position, hourlyRate, workedHours));
                    }
                }
            }
        } catch (IOException e){
            System.out.println("Error loading Employees data: " + e.getMessage());
        }
        return employees;
    }
}