package Backend;

import FileHandler.FileOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class LoginManager {
    private final ArrayList<String[]> accounts;
    private final FileOperator<String[]> accountHandler;
    public LoginManager(FileOperator<String[]> accountHandler) {
        if(accountHandler == null)
            throw new IllegalArgumentException("accountHandler cannot be null");
        this.accountHandler = accountHandler;
        this.accounts = accountHandler.load();
        sortAccounts();
    }
    public void addAccount(String username, String password) {
        if (username == null|| password==null) {
            throw new IllegalArgumentException("Account cannot have nulls.");
        }
        if (searchByUsername(username) != -1) {
            throw new IllegalArgumentException("Account " + username + " already exists.");
        }
        String[] account = new String[]{username, password};
        accounts.add(account);
        sortAccounts();
        accountHandler.save(account);
    }
    private void sortAccounts() {
        this.accounts.sort(Comparator.comparing(arr -> arr[0]));
        // lambda expression to denote that we will compare using the first element in the string array
    }
    private int searchByUsername(String username) {
        int index = Collections.binarySearch(
                this.accounts,
                new String[]{username, ""},
                Comparator.comparing(arr -> arr[0])
        );
        return index>=0?index:-1;
    }
    public boolean checkAccount(String username, String password) {
        if (username == null || password==null)
            throw new IllegalArgumentException("Account cannot have nulls.");
        int index = searchByUsername(username);
        if (index == -1)
            return false;
        return accounts.get(index)[1].equals(password);
    }
}
