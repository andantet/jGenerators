package io.github.andantedevs.jgenerators;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static io.github.andantedevs.jgenerators.Main.*;

public class Generator {
    public static class template {
        public template() throws IOException {
            // templates folder
            File templatesFolder = new File("src/resources/templates");
            File[] templatesFolderContents = templatesFolder.listFiles();

            String[] templateIds = { "" };
            for (File i : Objects.requireNonNull(templatesFolderContents)) {
                if (!i.isFile()) {
                    if (templateIds[0].isEmpty()) templateIds[0] = i.getName();
                        else templateIds = (String[])appendToArray(templateIds, i.getName());
                }
            }

            log("Templates: " + Arrays.toString(templateIds));

            for (String templateIdInput : Input.getString("Template IDs", "separated with commas").toLowerCase().split(",")) {
                // check if empty
                if (templateIdInput.isEmpty()) {
                    log("Empty template!");
                    new Generator.template();
                    return;
                }

                // check template exists
                boolean templateExists = false;
                File[] templateFiles = {};
                for (String i : Objects.requireNonNull(templateIds)) {
                    if (i.equals(templateIdInput)) {
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
                String[] definitionsFile = readStringFromFile(templatesFolder + "/" + templateIdInput + "/definitions.properties").split("\n");
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
                    if (!(templateFile.getName().equals("definitions.properties") || templateFile.getName().startsWith("_"))) Main.output(fillDefinitions(readStringFromFile(templateFile.getPath()), definitions), fillDefinitions(templateFile.getPath(), definitions));
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
            Main.output(fileWriteInput[0].replace("\\n","\n"), fileWriteInput[1]);
        }
    }
}
