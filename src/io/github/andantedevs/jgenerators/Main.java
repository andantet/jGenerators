package io.github.andantedevs.jgenerators;

import io.github.andantedevs.jgenerators.util.CrashDialog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static Scanner INPUT_SCANNER = new Scanner(System.in);
    public static boolean catchWarning;

    public static int READ_LINES = 0;
    public static int GENERATED_FILES = 0;

    public static String outputDir = "output/" + LocalDateTime.now().toString().replace(":", "");

    public static void main(String[] args) {
        System.out.println("Loading jGenerators");

        try {
            GeneratorGUI.start();
        } catch (Exception e) {
            e.printStackTrace();
            new CrashDialog(e);
        }
    }

/*    public static void main(String[] args) throws IOException {
        // pre
        log("Started");

        // main
        createFolder("");
        checkGeneratorType();

        // post
        String outputPath = Paths.get(outputDir).toAbsolutePath().toString();
        Desktop.getDesktop().open(new File(outputPath));

        log("Output to " + outputPath);
        log("Generated " + GENERATED_FILES + " files from " + READ_LINES + " read template lines");
    }*/

    public static void checkGeneratorType() throws IOException {
        String[] generatorTypes = { "template", "file with contents" };
        log("Please enter a generator type: " + Arrays.toString(generatorTypes));
        String generatorTypeInput = Input.getString().toLowerCase();

        boolean passed = true;
        for (String ignored : generatorTypes) {
            if (generatorTypeInput.equals(generatorTypes[0])) {
                new Generator.template();
                break;
            } else if (generatorTypeInput.equals(generatorTypes[1])) {
                new Generator.fileWithContents();
                break;
            }
            passed = false;
        }

        if (!passed) {
            log("Invalid type!");
            checkGeneratorType();
        }
    }

    public static void output(String data, String loc) throws IOException {
        loc = outputDir + "/" + loc;
        write(data, loc);
    }
    private static void write(String data, String loc) throws IOException {
        File file = new File(loc);
        catchWarning = new File(file.getParent()).mkdirs();

        if (file.delete()) log("Deleted file " + file.getName());
        if (file.createNewFile()) log("Created file " + file.getName());

        FileWriter writer = new FileWriter(file);
        log("Writing to " + file);
        writer.write(data);

        writer.close();

        GENERATED_FILES++;
    }
    public static void createFolder(String loc) {
        if (!loc.isEmpty()) log("Creating folder at " + loc);
            else log("Creating folder at root");
        catchWarning = new File(outputDir + "/" + loc).mkdirs();
    }

    // utils
    public static void log(String text) {
        System.out.println("[jgenerators] " + text);
    }

    public static String readStringFromFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> {
                contentBuilder.append(s).append("\n");
                Main.READ_LINES++;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }
    public static Object[] appendToArray(Object[] original, Object toAppend) {
        original = Arrays.copyOf(original, original.length + 1);
        original[original.length - 1] = toAppend;

        return original;
    }
}
