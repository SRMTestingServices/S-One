package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {

    private static final String TEST_DATA_DIR = "src/test/resources/testdata/";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Pattern ARRAY_PATTERN = Pattern.compile("(.+)\\[(\\d+)]");


    public static String getString(String fileName, String jsonPath) {
        JsonNode node = getNode(fileName, jsonPath);
        if (node != null && node.isValueNode()) {
            return node.asText();
        }
        throw new RuntimeException("Value not found or invalid at path: " + jsonPath);
    }

    public static int getInt(String fileName, String jsonPath) {
        JsonNode node = getNode(fileName, jsonPath);
        if (node != null && node.isInt()) {
            return node.asInt();
        }
        throw new RuntimeException("Integer value not found at path: " + jsonPath);
    }

    public static boolean getBoolean(String fileName, String jsonPath) {
        JsonNode node = getNode(fileName, jsonPath);
        if (node != null && node.isBoolean()) {
            return node.asBoolean();
        }
        throw new RuntimeException("Boolean value not found at path: " + jsonPath);
    }

    public static <T> T getAsPojo(String fileName, String jsonPath, Class<T> clazz) {
        try {
            JsonNode node = getNode(fileName, jsonPath);
            if (node != null) {
                return objectMapper.treeToValue(node, clazz);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON node to POJO at path: " + jsonPath, e);
        }
        throw new RuntimeException("POJO data not found at path: " + jsonPath);
    }

    // Core method for resolving node path with array support
    private static JsonNode getNode(String fileName, String jsonPath) {
        try {
            JsonNode rootNode = objectMapper.readTree(new File(TEST_DATA_DIR + fileName));
            String[] segments = jsonPath.split("\\.");
            JsonNode currentNode = rootNode;

            for (String segment : segments) {
                Matcher matcher = ARRAY_PATTERN.matcher(segment);
                if (matcher.matches()) {
                    String arrayName = matcher.group(1);
                    int index = Integer.parseInt(matcher.group(2));
                    currentNode = currentNode.path(arrayName);
                    if (currentNode.isArray() && currentNode.size() > index) {
                        currentNode = currentNode.get(index);
                    } else {
                        return null;
                    }
                } else {
                    currentNode = currentNode.path(segment);
                }
            }
            return currentNode;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + fileName, e);
        }
    }
}
