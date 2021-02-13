package me.andante.jgenerators;

import java.util.Scanner;

public class InputUtils {
    private static final Scanner INPUT_SCANNER = new Scanner(System.in);

    public static String[] getArray(String title) {
        return InputUtils.getString(title, "(string array)").toLowerCase().split(",");
    }

    public static String getString() {
        return InputUtils.INPUT_SCANNER.nextLine();
    }
    public static String getString(String title, String note) {
        InputUtils.log(title + "? " + note);
        return InputUtils.getString();
    }
    public static String getString(String title) {
        return InputUtils.getString(title, "(string)");
    }

    private static void log(String text) {
        System.out.println("    " + text);
    }
}
