package FileHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginHandler {
    public static void saveLoginData(String username, String password) {

        if (username == null || password == null ||
                username.trim().isEmpty() || password.trim().isEmpty()) {
            System.out.println("Username and password cannot be empty!");
            return;
        }
        if (username.contains(",") || password.contains(",")) {

            System.out.println("Invalid characters!");
            return;
        }
        ArrayList<String[]> users = loadLoginData();
        for (String[] user : users) {

            if (user[0].equalsIgnoreCase(username)) {
                System.out.println("Username already exists!");
                return;
            }
        }

        try (PrintWriter writer = new PrintWriter(
                new FileWriter("LoginData.csv", true))) {
            writer.println(username + "," + password);
        }
        catch (IOException e) {
            System.out.println("Error saving login data: " + e.getMessage());
        }
    }

    public static ArrayList<String[]> loadLoginData() {
        ArrayList<String[]> loginData = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("LoginData.csv"))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] data = line.split(",");
                if (data.length == 2 && !data[0].trim().isEmpty() && !data[1].trim().isEmpty())
                    loginData.add(new String[]{
                            data[0].trim(),
                            data[1]
                    });
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Login data file not found.");
        }
        return loginData;
    }
}
