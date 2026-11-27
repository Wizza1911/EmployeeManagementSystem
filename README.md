# Employee Management System (EMS)

## Group Members
| Student Name | Student ID |
| :--- | :--- |
| **Ahmed Sameh Elkholy** | `9811` |
| **Abdelrahman Adel Zakaria** | `9906` |
| **Bashar Sherif Mohamed** | `11256` |
| **Moataz Ahmed Besher** | `10039` |

---

## System Description
The **Employee Management System (EMS)** is a Java Swing desktop application designed to streamline company workforce and department administration. 

### Key Features
* **Authentication:** Secure login portal backed by account validation.
* **Executive Dashboard:** Displays real-time summary cards for total employees, total departments, and total monthly payroll, alongside visual progress-bar breakdowns by department and employment type.
* **Employee Management (CRUD):** Add, view, update, and delete **Full-Time** (basic salary + bonus) and **Part-Time** (hourly rate $\times$ hours worked) employees with full input validation.
* **Multi-Criteria Search & Filtering:** Search employees dynamically by ID, Name, Department, Employment Type, Age, or Position.
* **Department Management:** Create, edit, and delete departments while tracking active member counts (preventing deletion of departments that still have assigned employees).

---

## Compiling and Running the Application

### Prerequisites
* **Java Development Kit (JDK):** Version 8 or higher installed and added to your system `PATH`.

### Project Structure
Ensure your source files are organized into their respective package folders:
```text
ProjectRoot/
├── Main.java
├── Backend/
│   ├── Department.java
│   ├── DepartmentManager.java
│   ├── Employee.java
│   ├── EmployeeManager.java
│   ├── FullTimeEmployee.java
│   ├── LoginManager.java
│   └── PartTimeEmployee.java
├── FileHandler/
│   ├── AccountHandler.java
│   ├── DepartmentHandler.java
│   ├── EmployeeHandler.java
│   └── FileOperator.java
├── Helper/
│   └── ValidationHelper.java
├── GUI/
│   ├── DashboardPanel.java
│   ├── DepartmentPanel.java
│   ├── EmployeeAdderPanel.java
│   ├── EmployeeSearchPanel.java
│   ├── EmployeeUpdatePanel.java
│   ├── Login.java
│   └── MainFrame.java
└── Data/                  <-- Data storage folder
```

### Command-Line Instructions
1. Open a terminal/command prompt in the project root directory.
2. Create the `Data` directory (required for initial CSV file generation) and an `out` directory for compiled classes:
   * **Windows (CMD/PowerShell):**
     ```bash
     mkdir Data out
     ```
   * **Linux / macOS:**
     ```bash
     mkdir -p Data out
     ```
3. Compile all Java source files:
   ```bash
   javac -d out Main.java Backend/*.java FileHandler/*.java Helper/*.java GUI/*.java
   ```
4. Run the application:
   ```bash
   java -cp out Main
   ```

*(Alternatively, open the project folder in an IDE such as IntelliJ IDEA, Eclipse, or NetBeans, ensure a `Data` folder exists in the project root, and run `Main.java`.)*

---

## Login Credentials
When the application runs for the first time (or if `Data/LoginData.csv` is empty), default administrator credentials are automatically generated:

* **Username:** `Admin`
* **Password:** `Admin`

---

## Data Storage Format and Location
All application data is persisted locally in plain-text **Comma-Separated Values (`.csv`)** format inside the `Data/` directory relative to the application root:

| File Path | Purpose | CSV Schema / Format |
| :--- | :--- | :--- |
| `Data/LoginData.csv` | User login credentials | `username,password` |
| `Data/Departments.csv` | Department records | `departmentId,departmentName` |
| `Data/Employees.csv` | Full-Time & Part-Time employee records | `id,type,fullName,age,departmentId,departmentName,position,basicSalary,bonus,hourlyRate,hoursWorked` |