package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("serenity.properties");
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not read serenity.properties file", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return getProperty("webdriver.base.url");
    }

    public static String getBrowser() {
        return getProperty("webdriver.driver");
    }

    public static String getChromeDriverPath() {
        return getProperty("webdriver.chrome.driver");
    }

    public static String getEnvironment() {
        return getProperty("environment");
    }
}