package FileHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import Helper.*;

public class AccountHandler implements FileOperator<String[]>{

    private static final String LOGIN_DATA_CSV = "Data/LoginData.csv";

    @Override
    public boolean save(String[] account ) {
        if(account==null || account.length!=2)
            return false;
        String username = account[0];
        String password = account[1];
        if (!ValidationHelper.isValidUsername(username)
                || !ValidationHelper.isValidPassword(password))
            return false;

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(LOGIN_DATA_CSV, true))) {

            writer.println(username + "," + password);
            return true;

        } catch (IOException e) {
            System.out.println("Error saving login data: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean saveAll(ArrayList<String[]> loginData) {
        if(loginData==null)
            return false;
        for (String[] account: loginData ) {
            if (account == null || account.length != 2)
                return false;
            String username = account[0];
            String password = account[1];
            if (!ValidationHelper.isValidUsername(username)
                    || !ValidationHelper.isValidPassword(password))
                return false;
        }
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(LOGIN_DATA_CSV))) {
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
    @Override
    public ArrayList<String[]> load() {
        ArrayList<String[]> loginData = new ArrayList<>();
        File file = new File(LOGIN_DATA_CSV);
        try {
            if (file.createNewFile()) {
                System.out.println("Login data file created successfully.");
                loginData.add(new String[]{"Admin","Admin"});
                return loginData;
            }
            try (Scanner sc = new Scanner(file)){
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] data = line.split(",");
                    if (data.length == 2
                            && ValidationHelper.isValidUsername(data[0])
                            && ValidationHelper.isValidPassword(data[1]))
                        loginData.add(new String[]{data[0], data[1]});
                }
                if(loginData.size()==0)
                    loginData.add(new String[]{"Admin","Admin"});
        }
        } catch (IOException e) {
            System.out.println("Error loading login data: " + e.getMessage());
        }
        return loginData;
    }
}
