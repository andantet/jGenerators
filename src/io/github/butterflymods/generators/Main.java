package io.github.butterflymods.generators;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static Scanner INPUT_SCANNER = new Scanner(System.in);

    public static String outputDir = "output" + "/" + LocalDateTime.now().toString().replace(":", "");
    public static String[] generatorTypes = { "color files", "file with contents" };

    public static void main(String[] args) throws IOException {
        log("Started");

        checkGeneratorType();
    }

    public static void checkGeneratorType() throws IOException {
        log("Please enter a generator type: " + Arrays.toString(generatorTypes));
        String generatorTypeInput = INPUT_SCANNER.nextLine().toLowerCase();

        switch (generatorTypeInput) {
            case "color files":
                Generate.colorFiles();
                return;
            case "file with contents":
                Generate.fileWithContents();
                return;
        }

        log("Invalid type!");
        checkGeneratorType();
    }

    public static void write(String data, String loc) throws IOException {
        loc = outputDir + "/" + loc;
        File file = new File(loc);
        if (file.createNewFile()) log("File created");
        else {
            log("File already exists - override?\n    (true/false)");

            boolean input = INPUT_SCANNER.nextBoolean();
            if (input) {
                file.delete();
                log("Continuing...");
            } else {
                log("Stopping...");
                return;
            }
        }

        FileWriter writer = new FileWriter(file);
        log("Writing to " + file.getAbsolutePath() + "...");
        writer.write(data + "\n");
        writer.close();
    }
    public static void createFolder(String loc) {
        new File(Main.outputDir + "/" + loc).mkdir();
    }

    // utils
    public static void log(String text, boolean prefix) {
        String output = text;
        if (prefix) output = "[BF-G] " + output;
        else output = "    " + output;

        System.out.println(output);
    }
    public static void log(String text) {
        log(text, true);
    }
}
