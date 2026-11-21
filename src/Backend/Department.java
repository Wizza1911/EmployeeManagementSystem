package Backend;

public class Department {
    private int departmentId;
    private String departmentName;

    public Department(int departmentId, String departmentName) {
        setDepartmentId(departmentId);
        setDepartmentName(departmentName);
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public final void setDepartmentId(int departmentId) {
        if (departmentId <= 0) {
            throw new IllegalArgumentException("Department ID must be a positive integer.");
        }
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public final void setDepartmentName(String departmentName) {
        if (departmentName == null || departmentName.isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be empty.");
        }
        this.departmentName = departmentName;
    }
    public String toString() {
        return  getDepartmentId() +","+ getDepartmentName();
    }
}