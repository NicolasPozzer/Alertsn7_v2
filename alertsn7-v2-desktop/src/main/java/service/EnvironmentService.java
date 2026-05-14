package service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvironmentService implements IEnvironmentService {

    @Override
    public String urlServerBackend() {
        Properties props = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/config.properties")) {
            if (input == null) {
                System.out.println("config.properties no encontrado, usando URL por defecto");
                return "http://localhost:8077";
            }
            props.load(input);
            return props.getProperty("backend.url", "http://localhost:8077");
        } catch (IOException e) {
            e.printStackTrace();
            return "http://localhost:8077";
        }
    }
}