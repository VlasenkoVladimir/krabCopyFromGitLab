package com.krab51.webapp.utilities;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Утилитарный класс для работы с безопасностью
 */
public class SecurityUtilites {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()_–{}:;',?/*~$^+=<>]).{8,20}$";

    private static final Pattern pattern = compile(PASSWORD_PATTERN);

    public static boolean isPasswordValid(final String password) {
        return pattern.matcher(password).matches();
    }
}