package io.github.butterflymods.generators;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Generate {
    static String colorId = "color_id";
    static String modId;

    public static class GenerationType {
        public static void template() throws IOException {
            // get input
            String[] colorData = {  Input.getString("Color ID").toLowerCase(), Input.getString("Mod ID").toLowerCase() };
            colorId = colorData[0];
            modId = colorData[1];

            // generate skeleton
            generateFolders(new String[]{ "assets", "assets/" + modId, "assets/" + modId });

            for (String templateId : Input.getString("Template IDs", "separated with commas").toLowerCase().split(",")) {
                generateFolder("assets/" + modId + "/" + templateId);
                generateFromTemplate(templateId, "assets/${mod_id}/" + templateId);
            }
        }
    }

    public static void fileWithContents() {
        String[] fileWriteInput = Input.getArray();
        try {
            Main.write(fileWriteInput[0], fileWriteInput[1]);
        } catch (Exception e) {
            Main.log("Error: " + e.toString());
        }
    }

    public static void generateFolders(String[] folders) {
        Main.createFolder("");
        for (String i : folders) generateFolder(i);
    }
    public static void generateFolder(String folder) {
        Main.createFolder(folder);
    }

    private static String loadTemplate(String path) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }
    private static String runTemplateFilters(String str) {
        return str.replace("${color_id}", colorId).replace("${mod_id}", modId);
    }
    private static void generateFromTemplate(String folderId, String outputDir) throws IOException {
        File folder = new File("src/resources/templates/" + folderId);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) for (File file : listOfFiles) {
            if (file.isFile()) {
                Main.write(runTemplateFilters(loadTemplate(file.getPath())), runTemplateFilters(outputDir + "/" + file.getName()));
            } else Main.log("Ignored " + file.getName() + ", not file");
        }
    }
}
