package Backend;

public abstract class Employee {
    private int id;
    private String fullName;
    private int age;
    private String department;
    private String position;

    public Employee(int id, String fullName, int age, String department, String position) {
        setId(id);
        setFullName(fullName);
        setAge(age);
        setDepartment(department);
        setPosition(position);     
    }
    public int getId() {
        return id;
    }
    public  void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Employee ID must be a positive integer.");
        }
        this.id = id;
    }
    public String getFullName() {
        return fullName;
    }
    public  void setFullName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }
        this.fullName = fullName.trim();
    }
    public int getAge() {
        return age;
    }
    public  void setAge(int age) {
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be a positive integer.");
        }
        this.age = age;
    }
    public String getDepartment() {
        return department;
    }

    public  void setDepartment(String department) {
        if (department == null || department.isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }
        this.department = department;
    }
    public String getPosition() {
        return position;
    }
    public  void setPosition(String position) {
        if (position == null || position.isEmpty()) {
            throw new IllegalArgumentException("Position cannot be empty.");
        }
        this.position = position;
    }
    public abstract double calculateSalary();


}
