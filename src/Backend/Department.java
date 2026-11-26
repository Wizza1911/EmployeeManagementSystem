package Backend;

public class Department {
    private int departmentId;
    private String departmentName;
    private int count;

    public Department(int departmentId, String departmentName) {
        setDepartmentId(departmentId);
        setDepartmentName(departmentName);
        count=0;
    }

    public int getDepartmentId() {
        return departmentId;
    }
    public int getCount() {return count;}
    public void setCount(int count) {this.count = count>=0?count:this.count;}
    public void setDepartmentId(int departmentId) {
        if (departmentId <= 0) {
            throw new IllegalArgumentException("Department ID must be a positive integer.");
        }
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        if (departmentName == null || departmentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be empty.");
        }
        if (departmentName.contains(",")) {
            throw new IllegalArgumentException("Department name cannot contain commas.");
        }
        this.departmentName = departmentName.trim();
    }

    @Override
    public String toString() {
        return getDepartmentId() + "," + getDepartmentName();
    }
}