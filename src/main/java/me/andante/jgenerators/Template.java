package me.andante.jgenerators;

import com.google.common.collect.ImmutableMap;
import org.apache.log4j.Level;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Template {
    private final String id;
    private final JSONObject rawDefinitions;
    private final List<File> files;
    private final String outputDirectory;

    private final ImmutableMap.Builder<String, String> definitions = new ImmutableMap.Builder<>();
    private ImmutableMap<String, String> builtDefinitions = null;

    private int filesRead;
    private int linesRead;

    public Template(String id, JSONObject rawDefinitions, List<File> files) {
        this.id = id;
        this.rawDefinitions = rawDefinitions;
        this.files = files;
        this.outputDirectory = JGenerators.getOutputDirectory() + "/"+ this.getId();
    }

    public String getId() {
        return id;
    }
    public String getOutputDirectory() {
        return outputDirectory;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void generate() throws IOException {
        new File(this.getOutputDirectory()).mkdirs();
        this.rawDefinitions.toMap().forEach((definitionId, definitionName) -> this.definitions.put(definitionId, InputUtils.getString(JSONObject.valueToString(definitionName))));
        this.builtDefinitions = this.definitions.build();

        JGenerators.log("Generating...");

        files.forEach(file -> {
            try {
                this.write(this.replaceWithDefinitions(file), this.replaceWithDefinitions(file.getPath().replace(JGenerators.getInputDirectory() + this.getId(), "")));
            } catch (IOException e) {
                JGenerators.log(Level.ERROR, "Could not write to file " + file.getPath());
                e.printStackTrace();
            }
        });

        // print analytics
        this.write(
            new JSONObject()
                .put("lines_read", this.linesRead)
                .put("files_read", this.filesRead)
            .toString(2), "/analytics.json"
        );

        // open output directory
        JGenerators.log("Opening " + this.getOutputDirectory() + "...");
        Desktop.getDesktop().open(new File(outputDirectory));

        JGenerators.log("Generated " + this.files.size() + " from " + this + "!");
    }

    private String replaceWithDefinitions(File file) {
        return this.replaceWithDefinitions(this.readStringFromFile(file));
    }
    private String replaceWithDefinitions(String input) {
        final String[] output = { input };
        this.builtDefinitions.forEach((definitionId, definitionValue) -> output[0] = output[0].replace("${" + definitionId + "}", definitionValue));

        return output[0];
    }
    public String readStringFromFile(File file) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(file.getPath()), StandardCharsets.UTF_8)) {
            stream.forEach(s -> {
                contentBuilder.append(s).append("\n");
                this.linesRead++;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.filesRead++;
        return contentBuilder.toString();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void write(String data, String loc) throws IOException {
        File file = new File(this.getOutputDirectory() + "/" + loc);
        new File(file.getParent()).mkdirs();

        if (file.delete()) JGenerators.log(Level.WARN, "Replaced file " + file.getName());
        FileWriter writer = new FileWriter(file);
        writer.write(data);

        writer.close();
    }

    @Override
    public String toString() {
        return this.id;
    }
}
