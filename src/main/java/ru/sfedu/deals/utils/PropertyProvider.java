package ru.sfedu.deals.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import ru.sfedu.deals.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration utility. Allows to get configuration properties from the
 * default configuration file
 *
 * @author Boris Jamalov
 */
public class PropertyProvider {
  private static final Logger log = (Logger) LogManager.getLogger(PropertyProvider.class);
  private static final String DEFAULT_CONFIG_PATH = Constants.DEFAULT_CONFIG_PATH;
  private static final Properties PROPERTIES = new Properties();

  /**
   * Hides default constructor
   */
  public PropertyProvider() {
  }

  private static Properties getProperties() throws IOException {
    if (PROPERTIES.isEmpty()) {
      loadProperty();
    }
    return PROPERTIES;
  }

  /**
   * Loads configuration from <code>DEFAULT_CONFIG_PATH</code>
   *
   * @throws IOException In case of the configuration file read failure
   */
  private static void loadProperty() throws IOException {
    File nf;
    if (System.getProperty(Constants.CONFIG_PATH) != null) {
      nf = new File(System.getProperty(Constants.CONFIG_PATH));
    } else {
      nf = new File(DEFAULT_CONFIG_PATH);
    }
    try (InputStream in = new FileInputStream(nf)) {
      PROPERTIES.load(in);
    } catch (IOException ex) {
      throw new IOException(ex);
    }
  }

  /**
   * Gets configuration entry value
   *
   * @param key Entry key
   * @return Entry value by key
   * @throws IOException In case of the configuration file read failure
   */
  public static String getProperty(String key) throws IOException {
    return getProperties().getProperty(key);
  }

}
