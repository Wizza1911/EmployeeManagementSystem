package Helper;

public class ValidationHelper {
    public static boolean isValidString(String data) {
        return data != null && !data.trim().isEmpty()
                && !data.contains(",")
                && !data.contains("\n")
                && !data.contains("\r");
    }
    public static boolean isValidName(String data) {
        return isValidString(data)&&data.matches("[a-zA-Z ]{3,20}");
    }
    public static boolean isValidInt(String data) {
        if (!isValidString(data))
            return false;
        try {
            Integer.parseInt(data.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isValidStringPostiveInt(String data) {
        return isValidInt(data)&& Integer.parseInt(data) > 0;
    }
    public static boolean isValidStringNonNegativeInt(String data) {
        return isValidInt(data)&& Integer.parseInt(data) >= 0;
    }
    public static boolean isValidUsername(String username) {
        return isValidString(username)&&username.matches("[a-zA-Z0-9_]{3,20}");
        //3 to 20 characters allowed a to z, A TO Z, 0 to 9 and _ are allowed
    }
    public static boolean isValidPassword(String password) {
        return isValidString(password)&& password.length() >= 8
                && password.length() <= 64;
    }
}
