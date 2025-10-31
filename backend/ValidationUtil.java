package util;

public class ValidationUtil {

    public static boolean isValidName(String name) {
        return name != null && name.matches("^[A-Za-z ]+$");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^[6-9][0-9]{9}$");
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }
}
