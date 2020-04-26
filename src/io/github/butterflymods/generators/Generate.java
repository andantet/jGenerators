package io.github.butterflymods.generators;

import java.io.IOException;

public class Generate {
    public static String[] colorBlocks = { "wool", "terracotta", "glazed_terracotta", "concrete", "concrete_powder", "stained_glass", "stained_glass_pane", "carpet", "shulker_box", "banner", "wall_banner", "bed" };
    public static String[] colorItems = { "dye", "wool", "terracotta", "glazed_terracotta", "concrete", "concrete_powder", "stained_glass", "stained_glass_pane", "carpet", "shulker_box", "banner", "bed" };

    public static String[] notDefaultColorBlocks = { "glazed_terracotta", "stained_glass_pane", "carpet", "banner", "wall_banner", "bed" };

    public static String[] folders = { "", "blockstates", "models", "models/item", "models/block" };

    public static void colorFiles() throws IOException {
        Main.log("Input color id:",false);
        String color = Main.INPUT_SCANNER.nextLine();

        for (String i : folders) Main.createFolder(i);

        for (String i : colorItems) {
            Main.write(jsonContents("\"parent\":\"block/" + i + "\""), "models/item/" + color + "_" + i + ".json");
        }
        String mod_id = "painttheworld";
        for (String i : colorBlocks) {
            switch (i) {
                case "glazed_terracotta":
                    Main.write(simpleBlockModel("template_glazed_terracotta", "pattern", color, mod_id, i), modelPath(color, "block", i));
                    break;
                case "stained_glass_pane":
                    Main.write(simpleBlockModel("template_glass_pane_noside", "pane", color, mod_id, i), modelPath(color, "block", i + "_noside"));
                    Main.write(simpleBlockModel("template_glass_pane_noside_alt", "pane", color, mod_id, i), modelPath(color, "block", i + "_noside_alt"));
                    Main.write(simpleBlockModel("white_stained_glass_pane_post", "pane", color, mod_id, i), modelPath(color, "block", i + "_post"));
                    Main.write(simpleBlockModel("white_stained_glass_pane_side", "pane", color, mod_id, i), modelPath(color, "block", i + "_side"));
                    Main.write(simpleBlockModel("white_stained_glass_pane_side_alt", "pane", color, mod_id, i), modelPath(color, "block", i + "_side_alt"));
                    break;
            }

            boolean hasGenerated = false;
            for (String i2 : notDefaultColorBlocks) {
                if (i2.equals(i)) {
                    hasGenerated = true;
                    break;
                }
            }

            Main.write(simpleBlockstate(color, i, mod_id), "blockstates/" + color + "_" + i + ".json");
            if (!hasGenerated) Main.write(simpleBlockModel("cube_all", "all", color, mod_id, i), modelPath(color, "block", i));
        }
    }

    public static void fileWithContents() {
        String[] fileWriteInput = GeneratorInput.getArray();
        try {
            Main.write(fileWriteInput[0], fileWriteInput[1]);
        } catch (Exception e) {
            Main.log("Error: " + e.toString());
        }
    }

    private static String jsonContents(String contents) {
        return "{\n    " + contents + "\n}";
    }
    private static String simpleBlockModel(String parent, String textureId, String color, String mod_id, String block) {
        if (block.startsWith("stained_glass_pane")) {
            return jsonContents("\"parent\":\"block/" + parent + "\",\n    \"textures\": {\n        \"" + textureId + "\": \"" + mod_id + ":block/" + color + "_stained_glass\"\n    }");
        } else return jsonContents("\"parent\":\"block/" + parent + "\",\n    \"textures\": {\n        \"" + textureId + "\": \"" + mod_id + ":block/" + color + "_" + block + "\"\n    }");
    }
    private static String modelPath(String color, String modelType, String block) {
        return "models/" + modelType + "/" + color + "_" + block + ".json";
    }
    private static String simpleBlockstate(String color, String block, String mod_id) {
        return jsonContents("\"variants\": {\n        \"\": { \"model\": \"" + mod_id + ":block/" + color + "_" + block + "\" }\n    }");
    }
}
