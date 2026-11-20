package Backend;

import java.io.PrintWriter;

public class PartTimeEmployee extends Employee {
    private double hourlyRate;
    private double hoursWorked;

    public PartTimeEmployee(int id, String fullName, int age, String department,
                            String position, double hourlyRate, double hoursWorked) {
        super(id, fullName, age, department, position);
        setHourlyRate(hourlyRate);
        setHoursWorked(hoursWorked);
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public final void setHourlyRate(double hourlyRate) {
        if ( hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate must be a non-negative number.");
        }
        this.hourlyRate = hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public final void setHoursWorked(double hoursWorked) {
        if ( hoursWorked < 0) {
            throw new IllegalArgumentException("Hours worked must be a non-negative number.");
        }
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateSalary() {
        return hourlyRate*hoursWorked;
    }

    @Override
    public boolean isFullTime() { return false; }

    @Override
    public String toString() {
        return super.toString() + ",,," + this.getHourlyRate() + "," +
                    this.getHoursWorked() ;
    }

   
}