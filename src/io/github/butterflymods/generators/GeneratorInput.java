package io.github.butterflymods.generators;

public class GeneratorInput {
    public static String[] getArray() {
        return new String[]{ getInput("Content"), getInput("File path") };
    }

    private static String getInput(String title) {
        Main.log(title + "?", false);
        return Main.INPUT_SCANNER.nextLine();
    }
}
