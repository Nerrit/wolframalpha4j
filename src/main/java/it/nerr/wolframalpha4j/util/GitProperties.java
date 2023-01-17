package it.nerr.wolframalpha4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GitProperties {

    public static final String APPLICATION_NAME = "application.name";
    /**
     * Use it on the properties given by {@link #getProperties()} to get project version captured at build time.
     */
    public static final String APPLICATION_VERSION = "git.build.version";
    public static final String APPLICATION_URL = "application.url";
    /**
     * Use it on the properties given by {@link #getProperties()} to get repository version captured at build time.
     */
    public static final String GIT_COMMIT_ID_DESCRIBE = "git.commit.id.describe";

    /**
     * Load a {@link Properties} object with application version data.
     *
     * @return a property list with application version details
     * @see GitProperties#APPLICATION_VERSION
     * @see GitProperties#GIT_COMMIT_ID_DESCRIBE
     */
    public static Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = GitProperties.class.getResourceAsStream("git.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException ignore) {
        }
        return properties;
    }
}
