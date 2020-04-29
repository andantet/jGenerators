package io.github.butterflymods.generators;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Generate {
    static String[] colorIds = { "color_id" };
    static String[] modIds;

    public static class GenerationType {
        public static void template() throws IOException {
            // get input
            String[][] colorData = {  Input.getString("Color IDs", "separated with commas").toLowerCase().split(","), Input.getString("Mod ID", "separated with commas").toLowerCase().split(",") };
            colorIds = colorData[0];
            modIds = colorData[1];

            for (String templateId : Input.getString("Template IDs", "separated with commas").toLowerCase().split(",")) {
                generateFromTemplate(templateId);
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
    private static String runTemplateFilters(String str, String colorId, String modId) {
        return str.replace("${color_id}", colorId).replace("${mod_id}", modId);
    }
    private static void generateFromTemplate(String templateId) throws IOException {
        File folder = new File("src/resources/templates/" + templateId);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) for (File template : listOfFiles) {
            if (template.isFile()) {
                for (String modId : modIds) {
                    generateFolder(templateId + "/" + modId);

                    for (String colorId: colorIds) {
                        Main.write(runTemplateFilters(loadTemplate(template.getPath()), colorId, modId), runTemplateFilters(templateId + "/${mod_id}/" + template.getName(), colorId, modId));
                    }
                }
            } else Main.log("Ignored " + template.getName() + ", not file");
        }
    }
}
