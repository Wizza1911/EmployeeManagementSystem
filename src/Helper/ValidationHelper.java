package Helper;

public class ValidationHelper {
    public static boolean isValidString(String data) {
        return data != null && !data.trim().isEmpty()
                && !data.contains(",")
                && !data.contains("\n")
                && !data.contains("\r");
    }
    public static boolean isValidUsername(String username) {
        return isValidString(username)&&username.matches("[a-zA-Z0-9_]{3,20}");
    }
    public static boolean isValidPassword(String password) {
        return isValidString(password)&& password.length() >= 8
                && password.length() <= 64;
    }
}
