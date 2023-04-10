package org.delfin.utils;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public class StringUtils {
    private static final String EMAIL_REGEX =  "^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$";
    private static final String PW = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!#+/_?%*><§])[A-Za-z\\d!#+/_?%*><§]{10,40}$";

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty() || value.replace(" ", "").isEmpty();
    }

    public static boolean isEmail(String value) {
        return value.matches(EMAIL_REGEX);
    }

    public static boolean isValidPassword(String value) {
        return value.matches(PW);
    }
}
