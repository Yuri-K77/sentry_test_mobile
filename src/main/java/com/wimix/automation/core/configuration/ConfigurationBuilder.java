package com.wimix.automation.core.configuration;

import org.apache.commons.configuration2.*;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigurationBuilder {

    private static CompositeConfiguration configuration;

    public ConfigurationBuilder() {
        configuration = new CompositeConfiguration();
    }

    public ConfigurationBuilder withSystemProperties() {
        configuration.addConfiguration(new SystemConfiguration());
        return this;
    }

    public ConfigurationBuilder withEnvironmentProperties() {
        configuration.addConfiguration(new EnvironmentConfiguration());
        return this;
    }

    public ConfigurationBuilder withClassPathProperties(String classPaths) {
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();

        InputStream resourceAsStream = this.getClass().getResourceAsStream(classPaths);

        if (resourceAsStream != null) {
            InputStreamReader reader = new InputStreamReader(resourceAsStream);
            try {
                propertiesConfiguration.read(reader);
            } catch (ConfigurationException | IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configuration.addConfiguration(propertiesConfiguration);
        return this;
    }

    public Configuration build() {
        return configuration;
    }
}