package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties = new Properties();

    static {
        String env = System.getProperty("env", "staging"); // default = staging

        String fileName = "config/" + env + ".properties";

        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream(fileName)) {

            if (input == null) {
                throw new RuntimeException("Config file not found: " + fileName);
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config: " + fileName, e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}