package io.github.butterflymods.generators;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static io.github.butterflymods.generators.Main.*;

public class Generator {
    public static class template {
        public template() throws IOException {
            //
            for (String templateIdInput : Input.getString("Template IDs", "separated with commas").toLowerCase().split(",")) {
                // check if empty
                if (templateIdInput.isEmpty()) {
                    log("Empty template!");
                    new Generator.template();
                    return;
                }

                // templates folder
                File templatesFolder = new File("src/resources/templates");
                File[] templatesFolderContents = templatesFolder.listFiles();

                // check template exists
                boolean templateExists = false;
                File[] templateFiles = {};
                for (File i : Objects.requireNonNull(templatesFolderContents)) {
                    if (i.getName().equals(templateIdInput)) {
                        templateExists = true;
                        templateFiles = new File(templatesFolder.toString() + "/" + templateIdInput).listFiles();
                    }
                }
                if (!templateExists) {
                    log("Template doesn't exist!");
                    new Generator.template();
                    return;
                }

                // load definitions
                String[] definitionsFile = readStringFromFile(templatesFolder + "/definitions.properties").split("\n");
                String[][] definitions = {};
                for (String i : definitionsFile) {
                    if (!(i.isEmpty() || i.startsWith("#"))) {
                        String[] property = i.split("=");
                        property[1] = Input.getString(property[1]);

                        definitions = (String[][])appendToArray(definitions, property);
                    }
                }
                generate(templateFiles, definitions);
            }
        }

        private static void generate(File[] files, String[][] definitions) throws IOException {
            for (File templateFile : Objects.requireNonNull(files)) {
                if (templateFile.isFile()) {
                    Main.output(fillDefinitions(readStringFromFile(templateFile.getPath()), definitions), fillDefinitions(templateFile.getPath(), definitions));
                } else generate(templateFile.listFiles(), definitions); // if folder, check that folder
            }
        }
        private static String fillDefinitions(String str, String[][] definitions) {
            for (String[] i : definitions) {
                str = str.replace("${" + i[0] + "}", i[1]);
            }

            str = str.replace("src\\resources\\templates\\", "");

            return str;
        }
    }

    public static class fileWithContents {
        public fileWithContents() throws IOException {
            String[] fileWriteInput = { Input.getString("Content"), Input.getString("Path") };
            Main.output(fileWriteInput[0], fileWriteInput[1]);
        }
    }
}
