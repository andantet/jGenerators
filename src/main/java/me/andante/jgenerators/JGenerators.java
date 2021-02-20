package me.andante.jgenerators;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class JGenerators {
    public static final String NAME = "jGenerators";
    protected static final Logger LOGGER = LogManager.getLogger(NAME);

    private static final String INPUT_DIRECTORY = System.getProperty("user.dir") + "/assets/templates/";
    private static final String OUTPUT_DIRECTORY = "output/" + LocalDateTime.now().toString().replace(":", ".");

    public static void main(String[] args) throws IOException {
        JGenerators.log("Starting...");

        // load templates
        JGenerators.log("Loading templates into memory...");
        ImmutableMap.Builder<String, Template> templatesBuilder = new ImmutableMap.Builder<>();
        String[] availableTemplates = new String[0];
        File[] rawTemplates = JGenerators.getResource("").listFiles();
        assert rawTemplates != null;
        for (File rawTemplateFolder : rawTemplates) {
            if (rawTemplateFolder.isDirectory()) {
                final String templateId = rawTemplateFolder.getName();

                // load definitions
                File definitionsFile = JGenerators.getResource(templateId + "/definitions.json");
                // build json string
                StringBuilder contentsBuilder = new StringBuilder();
                try (Stream<String> stream = Files.lines(Paths.get(definitionsFile.toString()), StandardCharsets.UTF_8)) {
                    stream.forEach(s -> contentsBuilder.append(s).append("\n"));
                } catch (IOException e) {
                    JGenerators.log(Level.ERROR, "Folder '" + templateId + "' does not contain a definitions.json");
                    continue;
                }

                // get json object from definitions string
                JSONObject definitions = new JSONObject(contentsBuilder.toString());

                String preDefinitionsStr = "predefinitions";
                JSONObject preDefinitions = null;
                if (definitions.has(preDefinitionsStr)) {
                    preDefinitions = definitions.getJSONObject(preDefinitionsStr);
                    definitions.remove(preDefinitionsStr);
                }

                // add to list of templates
                Template template = new Template(
                    templateId,
                    definitions, preDefinitions,
                    Files.walk(new File(definitionsFile.getParent()).toPath())
                        .filter(p -> {
                            String fileName = p.toFile().getName();
                            return !Files.isDirectory(p) && !fileName.equals("definitions.json") && !fileName.startsWith("_");
                        })
                        .map(Path::toFile)
                        .collect(ImmutableList.toImmutableList())
                );
                templatesBuilder.put(template.getId(), template);

                availableTemplates = Arrays.copyOf(availableTemplates, availableTemplates.length + 1);
                availableTemplates[availableTemplates.length - 1] = template.getId();
            }
        }
        JGenerators.log("Loaded templates.");

        JGenerators.requestAndGenerateTemplates(templatesBuilder.build(), Arrays.toString(availableTemplates));

        JGenerators.log("Closing...\n");
    }

    private static void requestAndGenerateTemplates(Map<String, Template> templates, String availableTemplates) throws IOException {
        String[] templatesToGenerate = InputUtils.getArray("Templates: " + availableTemplates);
        if (templatesToGenerate.length == 1 && templatesToGenerate[0].isEmpty()) {
            JGenerators.log(Level.ERROR, "No templates provided!");
            JGenerators.requestAndGenerateTemplates(templates, availableTemplates);
        } else {
            for (String templateToGenerate : templatesToGenerate) {
                try {
                    templates.get(templateToGenerate).generate();
                } catch (NullPointerException e) {
                    JGenerators.log(Level.ERROR, "Template with id '" + templateToGenerate + "' is invalid");
                }
            }

            try {
                // open output directory
                JGenerators.log("Opening " + OUTPUT_DIRECTORY + "...");
                Desktop.getDesktop().open(new File(OUTPUT_DIRECTORY));
            } catch (IllegalArgumentException e) {
                JGenerators.log(Level.WARN, "Output directory was never created, not opening");
            }
        }
    }

    public static File getResource(String path) {
        return new File(JGenerators.getInputDirectory() + path);
    }

    public static String getInputDirectory() {
        return INPUT_DIRECTORY;
    }
    public static String getOutputDirectory() {
        return OUTPUT_DIRECTORY;
    }

    public static void log(Level level, String msg) {
        LOGGER.log(level, "[" + NAME + "] " + msg);
    }
    public static void log(String msg) {
        log(Level.INFO, msg);
    }
}
