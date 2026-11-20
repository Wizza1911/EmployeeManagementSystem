package FileHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import Helper.*;
public class LoginHandler {
    public static boolean saveAllLoginData(ArrayList<String[]> loginData) {
        for (String[] account: loginData ) {
            if (account == null || account.length != 2)
                return false;
            String username = account[0];
            String password = account[1];
            if (!ValidationHelper.isValidUsername(username)
                    && !ValidationHelper.isValidPassword(password))
                return false;
        }
        try (PrintWriter writer = new PrintWriter(
                new FileWriter("LoginData.csv"))) {
            for (String[] account: loginData) {
                String username = account[0];
                String password = account[1];
                writer.println(username + "," + password);
            }
        }
        catch (IOException e) {
            System.out.println("Error saving login data: " + e.getMessage());
            return false;
        }
        return true;
    }
    public static boolean saveLoginData(String username, String password) {

        if (!ValidationHelper.isValidUsername(username)
                || !ValidationHelper.isValidPassword(password))
            return false;

        try (PrintWriter writer = new PrintWriter(
                new FileWriter("LoginData.csv", true))) {

            writer.println(username + "," + password);
            return true;

        } catch (IOException e) {
            System.out.println("Error saving login data: " + e.getMessage());
            return false;
        }
    }

    public static ArrayList<String[]> loadLoginData() {
        ArrayList<String[]> loginData = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("LoginData.csv"))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] data = line.split(",");
                if (data.length == 2 && ValidationHelper.isValidUsername(data[0])
                        && ValidationHelper.isValidPassword(data[1]))
                    loginData.add(new String[]
                            {data[0],
                            data[1]});
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Login data file not found.");
        }
        return loginData;
    }
}
