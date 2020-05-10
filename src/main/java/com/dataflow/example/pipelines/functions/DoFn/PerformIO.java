package com.dataflow.example.pipelines.functions.DoFn;

import com.dataflow.example.dtos.Record;
import com.dataflow.example.exceptions.InsertingIntoDatabaseException;
import com.dataflow.example.exceptions.PublishingToPubSub1Exception;
import com.dataflow.example.exceptions.PublishingToPubSub2Exception;
import com.dataflow.example.pipelines.options.PerformIOOptions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.transforms.DoFn;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
public class PerformIO extends DoFn<Record, Record> {

    private final PerformIOOptions options;
    private static transient JdbcTemplate jdbcTemplate;

    public PerformIO(PerformIOOptions options) {
        this.options = options;
    }

    @Setup
    public void setup() {
        configureExternalResources();
    }

    @ProcessElement
    public void processElement(ProcessContext context) {
        Record record = context.element();
        try {
            //publishingToPubSub1(record);
            //publishingToPubSub2(record);
            insertRecordIntoDatabase(record);
            context.output(record);
        } catch (InsertingIntoDatabaseException e) {
            log.error(e.getMessage(), e);
            // Proceed accordingly
        } catch (PublishingToPubSub1Exception e) {
            log.error(e.getMessage(), e);
            // Proceed accordingly
        } catch (PublishingToPubSub2Exception e) {
            log.error(e.getMessage(), e);
            // Proceed accordingly
        }
    }

    private void configureExternalResources() {
        // configurePubSub1Connection();
        // configurePubSub2Connection();
        configureDatabaseConnection();
    }

    private void configureDatabaseConnection() {
        try {
            jdbcTemplate = new JdbcTemplate(new HikariDataSource(getDataSourceConfig()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void insertRecordIntoDatabase(Record record) {
        try {
            jdbcTemplate.update("insert into beam (first_name, last_name, company_name, address, city, " +
                            "country, postal, phone1, phone2, email, web) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    record.getFirstName(), record.getLastName(), record.getCompanyName(), record.getAddress(),
                    record.getCity(), record.getCountry(), record.getPostal(), record.getPrimaryPhoneNumber(),
                    record.getSecondaryPhoneNumber(), record.getEmail(), record.getWeb());
        } catch (Exception e) {
            throw new InsertingIntoDatabaseException(e.getMessage(), e);
        }
    }

    private String getDatabaseConnectionUrl() {
        return String.format("jdbc:mysql://%s:%s/%s", options.getDbHost(), options.getDbPort(), options.getDbName());
    }

    private HikariConfig getDataSourceConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getDatabaseConnectionUrl());
        config.setUsername(options.getDbUsername());
        config.setPassword(options.getDbPassword());
        config.setIdleTimeout(options.getDbConnectionIdleTimeout());
        config.setMaxLifetime(options.getDbConnectionMaxLifetime());
        config.setMinimumIdle(options.getDbConnectionMinimumIdle());
        config.setMaximumPoolSize(options.getDbConnectionMaximumPoolSize());
        config.setPoolName(options.getDbPoolName());
        return config;
    }
}
