package settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DBSettingsReader {
        public Map<String, String> getSettings() {
        try (InputStream fileStream = new FileInputStream(
                System.getProperty("user.dir") + "/src/main/resources/db.properties")) {

            Properties properties = new Properties();
            properties.load(fileStream);

            Map<String, String> settings = new HashMap<>();
            for (Map.Entry entry : properties.entrySet()) {
                settings.put((String) entry.getKey(), (String) entry.getValue());
            }
            return settings;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }
