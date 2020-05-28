package io.github.andantedevs.jgenerators;

public class Input {
    public static String[] getArray(String title) {
        return Input.getString(title).toLowerCase().split(",");
    }
    public static String[] getArray(String title, String note) {
        return Input.getString(title, note).toLowerCase().split(",");
    }

    public static String getString() {
        return Main.INPUT_SCANNER.nextLine();
    }
    public static String getString(String title) {
        log(title + "? (string)");
        return getString();
    }
    public static String getString(String title, String note) {
        log(title + "? (string, " + note + ")");
        return getString();
    }

    public static Boolean getBool() {
        return Main.INPUT_SCANNER.nextBoolean();
    }
    public static Boolean getBool(String title) {
        log(title + "? (true/false)");
        return getBool();
    }
    public static Boolean getBool(String title, String note) {
        log(title + "? (true/false, " + note + ")");
        return getBool();
    }

    private static void log(String text) {
        System.out.println("    " + text);
    }
}
