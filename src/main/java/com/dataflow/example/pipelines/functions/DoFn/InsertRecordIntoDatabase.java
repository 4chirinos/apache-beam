package com.dataflow.example.pipelines.functions.DoFn;

import com.dataflow.example.dtos.Record;
import com.dataflow.example.pipelines.options.RecordToDatabaseOptions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.transforms.DoFn;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
public class InsertRecordIntoDatabase extends DoFn<Record, Record> {

    private final RecordToDatabaseOptions options;
    private static transient DataSource dataSource;
    private static transient JdbcTemplate jdbcTemplate;

    public InsertRecordIntoDatabase(RecordToDatabaseOptions options) {
        this.options = options;
    }

    @StartBundle
    public void startBundle() {
        log.error("Starting bundle");
    }

    @Setup
    public void setup() {
        log.error("Creating new DoFn instance");
        configureDataSource();
    }

    @ProcessElement
    public void processElement(ProcessContext context) {
        Record record = context.element();
        try {
            insertRecordIntoDatabase(record);
            context.output(record);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void insertRecordIntoDatabase(Record record) {
        jdbcTemplate.update("insert into beam (first_name, last_name, company_name, address, city, " +
                        "country, postal, phone1, phone2, email, web) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                record.getFirstName(), record.getLastName(), record.getCompanyName(), record.getAddress(),
                record.getCity(), record.getCountry(), record.getPostal(), record.getPrimaryPhoneNumber(),
                record.getSecondaryPhoneNumber(), record.getEmail(), record.getWeb());
    }

    private String getDatabaseConnectionUrl() {
        return String.format("jdbc:mysql://%s:%s/%s", options.getDbHost(), options.getDbPort(), options.getDbName());
    }

    private void configureDataSource() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(getDatabaseConnectionUrl());
            config.setUsername(options.getDbUsername());
            config.setPassword(options.getDbPassword());
            config.setIdleTimeout(options.getDbConnectionIdleTimeout());
            config.setMaxLifetime(options.getDbConnectionMaxLifetime());
            config.setMinimumIdle(options.getDbConnectionMinimumIdle());
            config.setMaximumPoolSize(options.getDbConnectionMaximumPoolSize());
            config.setPoolName(options.getDbPoolName());
            dataSource = new HikariDataSource(config);
            jdbcTemplate = new JdbcTemplate(dataSource);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
