package nextpos.app.nextpos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Value("${init.datasource.url}")
    private String dbUrl;

    @Value("${init.datasource.username}")
    private String dbUsername;

    @Value("${init.datasource.password}")
    private String dbPassword;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = 'nextpos'");
            if (!rs.next()) {
                stmt.executeUpdate("CREATE DATABASE nextpos");
                System.out.println("✅ Database 'nextpos' created successfully.");
            } else {
                System.out.println("ℹ️  Database 'nextpos' already exists.");
            }
        }
    }
}
