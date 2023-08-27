package test.foodtosave.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("test.foodtosave")
@PropertySource("classpath:database.properties")
public class DataSourceConfigs {

    private final String URL = "url";
    private final String USER = "dbuser";
    private final String DRIVER = "driver";
    private final String PASSWORD = "dbpassword";

    @Autowired
    private Environment environment;

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(environment.getProperty(this.URL));
        driverManagerDataSource.setUsername(environment.getProperty(this.USER));
        driverManagerDataSource.setPassword(environment.getProperty(this.PASSWORD));
        driverManagerDataSource.setDriverClassName(environment.getProperty(this.DRIVER));

        return driverManagerDataSource;
    }
}
