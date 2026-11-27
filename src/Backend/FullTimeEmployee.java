package Backend;

public class FullTimeEmployee extends Employee {
    private double basicSalary;
    private double bonus;
    
    public FullTimeEmployee(int id, String fullName, int age, Department department,
                            String position, double basicSalary, double bonus) {
        super(id, fullName, age, department, position);
        setBasicSalary(basicSalary);
        setBonus(bonus);
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public final void setBasicSalary(double basicSalary) {
        if ( basicSalary < 0) {
            throw new IllegalArgumentException("Basic salary must be a non-negative number.");
        }
        this.basicSalary = basicSalary;
    }

    public double getBonus() {
        return bonus;
    }

    public final void setBonus(double bonus) {
        if (bonus < 0) {
            throw new IllegalArgumentException("Bonus must be a non-negative number.");
        }
        this.bonus = bonus;
    }

    @Override
    public double calculateSalary() {
        return basicSalary + bonus;
    }

    @Override
    public boolean isFullTime() { return true; }

    @Override
    public String toString() {
       return super.toString() + "," + this.getBasicSalary() + "," +
                    this.getBonus() + ",,";

    }
}