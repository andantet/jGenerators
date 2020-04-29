package io.github.butterflymods.generators;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static Scanner INPUT_SCANNER = new Scanner(System.in);
    public static int GENERATED_LINES = 0;
    public static int GENERATED_FILES = 0;

    public static String outputDir = "output/" + LocalDateTime.now().toString().replace(":", "");
    public static String[] generatorTypes = { "template", "file with contents" };

    public static void main(String[] args) throws IOException {
        log("Started");

        checkGeneratorType();

        String outputPath = Paths.get(outputDir).toAbsolutePath().toString();
        log("Output to " + outputPath);
        log("Generated " + GENERATED_LINES + " lines, in " + GENERATED_FILES + " files");
        Desktop.getDesktop().open(new File(outputPath));
    }

    public static void checkGeneratorType() throws IOException {
        log("Please enter a generator type: " + Arrays.toString(generatorTypes));
        String generatorTypeInput = Input.getString().toLowerCase();

        createFolder("output", false);
        createFolder(outputDir, false);

        switch (generatorTypeInput) {
            case "template":
                Generate.GenerationType.template();
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
        boolean catch1 = new File(new File(loc).getParent()).mkdirs();
        File file = new File(loc);
        if (file.createNewFile()) log("Created file " + file);
        else {
            boolean input = Input.getBool("File already exists - override " + file.getName());
            if (input) {
                if (!file.delete()) log("File override failed!");
                log("Continuing...");
            } else {
                log("Stopping...");
                return;
            }
        }
        FileWriter writer = new FileWriter(file);
        log("Writing to " + file);
        writer.write(data);

        writer.close();
    }
    public static void createFolder(String loc) {
        File folder = new File(outputDir + "/" + loc);

        log("Creating folder " + folder);
        if (!folder.mkdirs()) log("Folder creation failed!");
    }
    public static void createFolder(String loc, Boolean prependOutputDir) {
        if (prependOutputDir) createFolder(loc);
            else {
                File folder = new File(loc);

                log("Creating folder " + folder.getAbsolutePath());
                if (!folder.mkdirs()) log("Folder creation failed!");
        }
    }

    // utils
    public static void log(String text, boolean prefix) {
        String output = text;
        if (prefix) output = "[BM-G] " + output;
        else output = "    " + output;

        System.out.println(output);
    }
    public static void log(String text) {
        log(text, true);
    }
}
