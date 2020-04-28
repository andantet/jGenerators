package io.github.butterflymods.generators;

public class Input {
    public static String[] getArray() {
        return new String[]{ getString("Content"), getString("File path") };
    }

    public static String getString() {
        return Main.INPUT_SCANNER.nextLine();
    }
    public static String getString(String title) {
        Main.log(title + "? (string)", false);
        return getString();
    }
    public static String getString(String title, String note) {
        Main.log(title + "? (string, " + note + ")", false);
        return getString();
    }

    public static Boolean getBool() {
        return Main.INPUT_SCANNER.nextBoolean();
    }
    public static Boolean getBool(String title) {
        Main.log(title + "? (true/false)", false);
        return getBool();
    }
}
